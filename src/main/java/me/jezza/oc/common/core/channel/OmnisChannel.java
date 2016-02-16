package me.jezza.oc.common.core.channel;

import static cpw.mods.fml.common.network.FMLOutboundHandler.FML_MESSAGETARGET;
import static cpw.mods.fml.common.network.FMLOutboundHandler.FML_MESSAGETARGETARGS;
import static cpw.mods.fml.common.network.FMLOutboundHandler.OutboundTarget.*;
import static cpw.mods.fml.common.network.NetworkRegistry.CHANNEL_SOURCE;
import static me.jezza.oc.common.core.channel.ChannelDispatcher.NETWORK_UPDATE_RANGE;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.ChannelFuture;
import me.jezza.oc.common.interfaces.IChannel;
import me.jezza.oc.common.interfaces.IOmnisPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author Jezza
 */
public class OmnisChannel implements IChannel {
	private final FMLEmbeddedChannel channel;

	public OmnisChannel(FMLEmbeddedChannel channel) {
		this.channel = channel;
	}

	@Override
	public ChannelFuture sendToAll(IOmnisPacket packet) {
		channel.attr(FML_MESSAGETARGET).set(ALL);
		return channel.writeAndFlush(packet);
	}

	@Override
	public ChannelFuture sendTo(IOmnisPacket packet, EntityPlayerMP player) {
		channel.attr(FML_MESSAGETARGET).set(PLAYER);
		channel.attr(FML_MESSAGETARGETARGS).set(player);
		return channel.writeAndFlush(packet);
	}

	@Override
	public ChannelFuture sendTo(IOmnisPacket packet, EntityPlayer player) {
		channel.attr(FML_MESSAGETARGET).set(PLAYER);
		channel.attr(FML_MESSAGETARGETARGS).set(player);
		return channel.writeAndFlush(packet);
	}

	@Override
	public ChannelFuture sendToAllAround(IOmnisPacket packet, TargetPoint point) {
		channel.attr(FML_MESSAGETARGET).set(ALLAROUNDPOINT);
		channel.attr(FML_MESSAGETARGETARGS).set(point);
		return channel.writeAndFlush(packet);
	}

	@Override
	public ChannelFuture sendToAllAround(IOmnisPacket packet, TileEntity point) {
		return sendToAllAround(packet, point, NETWORK_UPDATE_RANGE);
	}

	@Override
	public ChannelFuture sendToAllAround(IOmnisPacket packet, TileEntity point, double range) {
		channel.attr(FML_MESSAGETARGET).set(ALLAROUNDPOINT);
		channel.attr(FML_MESSAGETARGETARGS).set(new TargetPoint(point.getWorldObj().provider.dimensionId, point.xCoord, point.yCoord, point.zCoord, range));
		return channel.writeAndFlush(packet);
	}

	@Override
	public ChannelFuture sendToAllAround(IOmnisPacket packet, World world, int x, int y, int z) {
		return sendToAllAround(packet, world, x, y, z, NETWORK_UPDATE_RANGE);
	}

	@Override
	public ChannelFuture sendToAllAround(IOmnisPacket packet, World world, int x, int y, int z, double range) {
		channel.attr(FML_MESSAGETARGET).set(ALLAROUNDPOINT);
		channel.attr(FML_MESSAGETARGETARGS).set(new TargetPoint(world.provider.dimensionId, x, y, z, range));
		return channel.writeAndFlush(packet);
	}

	@Override
	public ChannelFuture sendToDimension(IOmnisPacket packet, int dimId) {
		channel.attr(FML_MESSAGETARGET).set(DIMENSION);
		channel.attr(FML_MESSAGETARGETARGS).set(dimId);
		return channel.writeAndFlush(packet);
	}

	@Override
	public ChannelFuture sendToServer(IOmnisPacket packet) {
		channel.attr(FML_MESSAGETARGET).set(TOSERVER);
		return channel.writeAndFlush(packet);
	}

	@Override
	public Packet toMinecraftPacket(IOmnisPacket packet) {
		return channel.generatePacketFrom(packet);
	}

	@Override
	public Side source() {
		return channel.attr(CHANNEL_SOURCE).get();
	}
}
