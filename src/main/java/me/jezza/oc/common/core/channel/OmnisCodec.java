package me.jezza.oc.common.core.channel;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageCodec;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.core.channel.internal.OmnisBuffer;
import me.jezza.oc.common.interfaces.IOmnisPacket;
import me.jezza.oc.common.utils.collect.PacketShortHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import java.util.List;

import static cpw.mods.fml.common.network.NetworkRegistry.FML_CHANNEL;
import static cpw.mods.fml.common.network.NetworkRegistry.NET_HANDLER;

/**
 * @author Jezza
 */
@Sharable
public class OmnisCodec extends MessageToMessageCodec<FMLProxyPacket, IOmnisPacket> {
	private final PacketShortHashMap<Class<? extends IOmnisPacket>> packetMap = new PacketShortHashMap<>();

	public OmnisCodec() {
	}

	protected boolean registerPacket(Class<? extends IOmnisPacket> packetClass) {
		packetMap.add(packetClass);
		return true;
	}

	@Override
	public void read(ChannelHandlerContext ctx) throws Exception {
		try {
			super.read(ctx);
		} catch (RuntimeException e) {
			OmnisCore.logger.error("Caught exception while trying to read from channel: ", e);
			throw e;
		}
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		try {
			super.write(ctx, msg, promise);
		} catch (RuntimeException e) {
			OmnisCore.logger.error("Caught exception while trying to read from channel: ", e);
			throw e;
		}
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, IOmnisPacket packet, List<Object> out) throws Exception {
		short discriminator = packetMap.get(packet.getClass());
		OmnisBuffer buffer = new OmnisBuffer();
		buffer.writeVarShort(discriminator);
		packet.encode(ctx, buffer);
		String channelName = ctx.channel().attr(FML_CHANNEL).get();
		out.add(new FMLProxyPacket(buffer.copy(), channelName));
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, FMLProxyPacket proxyPacket, List<Object> out) throws Exception {
		OmnisBuffer buffer = new OmnisBuffer(proxyPacket.payload());
		IOmnisPacket packet = packetMap.get(buffer.readVarShort()).newInstance();
		packet.decode(ctx, buffer);

		EntityPlayer player;
		switch (FMLCommonHandler.instance().getEffectiveSide()) {
			case CLIENT:
				player = getClientPlayer();
				packet.processClientSide(player);
				break;
			case SERVER:
				INetHandler netHandler = ctx.channel().attr(NET_HANDLER).get();
				player = ((NetHandlerPlayServer) netHandler).playerEntity;
				packet.processServerSide(player);
				break;
			default:
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		OmnisCore.logger.catching(cause);
	}

	@SideOnly(Side.CLIENT)
	private EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
}
