package me.jezza.oc.common.core.channel.internal;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import me.jezza.oc.api.channel.IChannel;
import me.jezza.oc.api.channel.IOmnisPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author Jezza
 */
public class ChannelFML implements IChannel {
    @Override
    public boolean registerPacket(Class<? extends IOmnisPacket> packetClass) {
        return false;
    }

    @Override
    public void sendTo(IOmnisPacket packet, EntityPlayer player) {

    }

    @Override
    public void sendTo(IOmnisPacket packet, EntityPlayerMP player) {

    }

    @Override
    public void sendToAll(IOmnisPacket packet) {

    }

    @Override
    public void sendToServer(IOmnisPacket packet) {

    }

    @Override
    public void sendToDimension(IOmnisPacket packet, int dimId) {

    }

    @Override
    public void sendToAllAround(IOmnisPacket packet, TargetPoint point) {

    }

    @Override
    public void sendToAllAround(IOmnisPacket packet, TileEntity point) {

    }

    @Override
    public void sendToAllAround(IOmnisPacket packet, TileEntity point, double range) {

    }

    @Override
    public void sendToAllAround(IOmnisPacket packet, World world, int x, int y, int z) {

    }

    @Override
    public void sendToAllAround(IOmnisPacket packet, World world, int x, int y, int z, double range) {

    }

    @Override
    public Packet mcPacket(IOmnisPacket packet) {
        return null;
    }
}
