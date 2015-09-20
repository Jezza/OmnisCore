package me.jezza.oc.common.core.network;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;

/**
 * @author Jezza
 */
public interface IChannel {
    void sendTo(IOmnisPacket packet, EntityPlayer player);

    void sendTo(IOmnisPacket packet, EntityPlayerMP player);

    void sendToAll(IOmnisPacket packet);

    void sendToAllAround(IOmnisPacket packet, TargetPoint point);

    void sendToAllAround(IOmnisPacket packet, TileEntity point);

    void sendToDimension(IOmnisPacket packet, int dimID);

    void sendToServer(IOmnisPacket packet);

    Packet asMCPacket(IOmnisPacket packet);
}
