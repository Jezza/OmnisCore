package me.jezza.oc.common.core.channel;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import io.netty.util.AttributeKey;
import me.jezza.oc.api.channel.IChannel;
import me.jezza.oc.api.channel.IOmnisPacket;
import me.jezza.oc.api.config.Config.ConfigDouble;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import static cpw.mods.fml.common.network.FMLOutboundHandler.FML_MESSAGETARGET;
import static cpw.mods.fml.common.network.FMLOutboundHandler.FML_MESSAGETARGETARGS;
import static cpw.mods.fml.common.network.FMLOutboundHandler.OutboundTarget.*;

/**
 * @author Jezza
 */
public class OmnisChannel extends FMLEmbeddedChannel implements IChannel {
    private static final AttributeKey<Boolean> CHANNEL_LOCKDOWN = new AttributeKey<>("omnis:lockdown");

    @ConfigDouble(category = "Networking", minValue = 5, maxValue = 120, comment = "The default network update range.")
    private static double NETWORK_UPDATE_RANGE = 60;

    private final OmnisCodec codec;

    public OmnisChannel(ModContainer mod, String modId, Side source, OmnisCodec codec) {
        super(mod, modId + "|OmnisCodec", source, codec);
        this.codec = codec;
        attr(CHANNEL_LOCKDOWN).setIfAbsent(Boolean.FALSE);
    }

    @Override
    public boolean registerPacket(Class<? extends IOmnisPacket> packetClass) {
        if (attr(CHANNEL_LOCKDOWN).get())
            throw new IllegalStateException("You cannot register any packets after FMLPostInitializationEvent is released!");
        return codec.registerPacket(packetClass);
    }

    @Override
    public void sendToAll(IOmnisPacket packet) {
        attr(CHANNEL_LOCKDOWN).compareAndSet(Boolean.FALSE, Boolean.TRUE);
        attr(FML_MESSAGETARGET).set(ALL);
        writeAndFlush(packet);
    }

    @Override
    public void sendTo(IOmnisPacket packet, EntityPlayerMP player) {
        attr(CHANNEL_LOCKDOWN).compareAndSet(Boolean.FALSE, Boolean.TRUE);
        attr(FML_MESSAGETARGET).set(PLAYER);
        attr(FML_MESSAGETARGETARGS).set(player);
        writeAndFlush(packet);
    }

    @Override
    public void sendTo(IOmnisPacket packet, EntityPlayer player) {
        attr(CHANNEL_LOCKDOWN).compareAndSet(Boolean.FALSE, Boolean.TRUE);
        attr(FML_MESSAGETARGET).set(PLAYER);
        attr(FML_MESSAGETARGETARGS).set(player);
        writeAndFlush(packet);
    }

    @Override
    public void sendToAllAround(IOmnisPacket packet, TargetPoint point) {
        attr(CHANNEL_LOCKDOWN).compareAndSet(Boolean.FALSE, Boolean.TRUE);
        attr(FML_MESSAGETARGET).set(ALLAROUNDPOINT);
        attr(FML_MESSAGETARGETARGS).set(point);
        writeAndFlush(packet);
    }

    @Override
    public void sendToAllAround(IOmnisPacket packet, TileEntity point) {
        sendToAllAround(packet, point, NETWORK_UPDATE_RANGE);
    }

    @Override
    public void sendToAllAround(IOmnisPacket packet, TileEntity point, double range) {
        attr(CHANNEL_LOCKDOWN).compareAndSet(Boolean.FALSE, Boolean.TRUE);
        attr(FML_MESSAGETARGET).set(ALLAROUNDPOINT);
        attr(FML_MESSAGETARGETARGS).set(new TargetPoint(point.getWorldObj().provider.dimensionId, point.xCoord, point.yCoord, point.zCoord, range));
        writeAndFlush(packet);
    }

    @Override
    public void sendToAllAround(IOmnisPacket packet, World world, int x, int y, int z) {
        sendToAllAround(packet, world, x, y, z, NETWORK_UPDATE_RANGE);
    }

    @Override
    public void sendToAllAround(IOmnisPacket packet, World world, int x, int y, int z, double range) {
        attr(CHANNEL_LOCKDOWN).compareAndSet(Boolean.FALSE, Boolean.TRUE);
        attr(FML_MESSAGETARGET).set(ALLAROUNDPOINT);
        attr(FML_MESSAGETARGETARGS).set(new TargetPoint(world.provider.dimensionId, x, y, z, range));
        writeAndFlush(packet);
    }

    @Override
    public void sendToDimension(IOmnisPacket packet, int dimId) {
        attr(CHANNEL_LOCKDOWN).compareAndSet(Boolean.FALSE, Boolean.TRUE);
        attr(FML_MESSAGETARGET).set(DIMENSION);
        attr(FML_MESSAGETARGETARGS).set(dimId);
        writeAndFlush(packet);
    }

    @Override
    public void sendToServer(IOmnisPacket packet) {
        attr(CHANNEL_LOCKDOWN).compareAndSet(Boolean.FALSE, Boolean.TRUE);
        attr(FML_MESSAGETARGET).set(TOSERVER);
        writeAndFlush(packet);
    }

    @Override
    public Packet mcPacket(IOmnisPacket packet) {
        return generatePacketFrom(packet);
    }
}
