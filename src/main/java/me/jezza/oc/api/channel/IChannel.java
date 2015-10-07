package me.jezza.oc.api.channel;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.ChannelFuture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author Jezza
 */
public interface IChannel {
    boolean registerPacket(Class<? extends IOmnisPacket> packetClass);

    ChannelFuture sendTo(IOmnisPacket packet, EntityPlayer player);
    ChannelFuture sendTo(IOmnisPacket packet, EntityPlayerMP player);
    ChannelFuture sendToAll(IOmnisPacket packet);
    ChannelFuture sendToServer(IOmnisPacket packet);
    ChannelFuture sendToDimension(IOmnisPacket packet, int dimId);
    ChannelFuture sendToAllAround(IOmnisPacket packet, TargetPoint point);
    ChannelFuture sendToAllAround(IOmnisPacket packet, TileEntity point);
    ChannelFuture sendToAllAround(IOmnisPacket packet, TileEntity point, double range);
    ChannelFuture sendToAllAround(IOmnisPacket packet, World world, int x, int y, int z);
    ChannelFuture sendToAllAround(IOmnisPacket packet, World world, int x, int y, int z, double range);

    void lockdown();

    Side source();
    Packet toMinecraftPacket(IOmnisPacket packet);
}
