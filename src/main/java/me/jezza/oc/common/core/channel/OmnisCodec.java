package me.jezza.oc.common.core.channel;

import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageCodec;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.interfaces.IOmnisPacket;
import me.jezza.oc.common.interfaces.Packet;
import me.jezza.oc.common.utils.ASM;
import me.jezza.oc.common.utils.collect.PacketShortHashMap;
import me.jezza.oc.common.utils.helpers.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static cpw.mods.fml.common.network.NetworkRegistry.FML_CHANNEL;
import static cpw.mods.fml.common.network.NetworkRegistry.NET_HANDLER;
import static me.jezza.oc.common.interfaces.Packet.DEFAULT_SPLITTER;
import static me.jezza.oc.common.utils.helpers.StringHelper.format;
import static me.jezza.oc.common.utils.helpers.StringHelper.useable;

/**
 * @author Jezza
 */
@Sharable
public class OmnisCodec extends MessageToMessageCodec<FMLProxyPacket, IOmnisPacket> {
	private final PacketShortHashMap<Class<? extends IOmnisPacket>> packetMap = new PacketShortHashMap<>();
	private static ListMultimap<String, Class<? extends IOmnisPacket>> classMap;

	protected OmnisCodec(String modId) {
		List<Class<? extends IOmnisPacket>> classes = dataMap().get(modId);
		if (classes.isEmpty()) {
			OmnisCore.logger.info(format("No packet classes found for {}. Please annotate the desired packet classes with @{}", modId, Packet.class.getSimpleName()));
		} else {
			packetMap.addAll(classes);
		}
	}

	private static ListMultimap<String, Class<? extends IOmnisPacket>> dataMap() {
		if (classMap == null) {
			classMap = ArrayListMultimap.create();
			for (Entry<ASMData, Class<?>> entry : ASM.classesWith(Packet.class).entrySet()) {
				Class<?> clazz = entry.getValue();
				if (!IOmnisPacket.class.isAssignableFrom(clazz))
					throw new IllegalArgumentException(format("@{} was found on a class that doesn't implement {}.", Packet.class.getSimpleName(), IOmnisPacket.class.getCanonicalName()));
				if (Modifier.isAbstract(clazz.getModifiers()))
					throw new IllegalArgumentException(format("@{} was found on a class that was abstract.", Packet.class.getSimpleName()));
				Map<String, Object> info = entry.getKey().getAnnotationInfo();
				String splitter = (String) info.get("splitter");
				if (!useable(splitter))
					splitter = DEFAULT_SPLITTER;
				String ids = info.get("value").toString();
				if (!useable(ids))
					throw new IllegalArgumentException(format("No valid ModID(s) found for {}", clazz.getCanonicalName()));
				List<String> modIds = Splitter.on(splitter).trimResults().omitEmptyStrings().splitToList(ids);
				if (modIds.isEmpty())
					throw new IllegalArgumentException(format("No valid ModID(s) found for {}", clazz.getCanonicalName()));
				for (String modId : modIds) {
					@SuppressWarnings("unchecked")
					Class<? extends IOmnisPacket> packetClass = (Class<? extends IOmnisPacket>) clazz;
					OmnisCore.logger.info(StringHelper.format("Discovered @{}({}) on {}", Packet.class.getSimpleName(), modId, packetClass.getCanonicalName()));
					classMap.put(modId, packetClass);
				}
			}
		}
		return classMap;
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
