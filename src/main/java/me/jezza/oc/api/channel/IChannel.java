package me.jezza.oc.api.channel;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
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

    void sendTo(IOmnisPacket packet, EntityPlayer player);

    void sendTo(IOmnisPacket packet, EntityPlayerMP player);

    void sendToAll(IOmnisPacket packet);

    void sendToServer(IOmnisPacket packet);

    void sendToDimension(IOmnisPacket packet, int dimId);

    void sendToAllAround(IOmnisPacket packet, TargetPoint point);

    void sendToAllAround(IOmnisPacket packet, TileEntity point);

    void sendToAllAround(IOmnisPacket packet, TileEntity point, double range);

    void sendToAllAround(IOmnisPacket packet, World world, int x, int y, int z);

    void sendToAllAround(IOmnisPacket packet, World world, int x, int y, int z, double range);

    Packet mcPacket(IOmnisPacket packet);
}
