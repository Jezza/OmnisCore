package me.jezza.oc.common.core.channel;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import me.jezza.oc.api.channel.IOmnisPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import java.util.List;

/**
 * @author Jezza
 */
@Sharable
public class OmnisCodec extends MessageToMessageCodec<FMLProxyPacket, IOmnisPacket> {

    public OmnisCodec() {
    }

    protected boolean registerPacket(Class<? extends IOmnisPacket> packetClass) {
        return false;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, IOmnisPacket packet, List<Object> out) throws Exception {
        ByteBuf buffer = Unpooled.buffer();
        Class<? extends IOmnisPacket> packetClass = packet.getClass();
        System.out.println(packetClass.getCanonicalName());

        // if (!this.packets.contains(msg.getClass()))
        //     throw new NullPointerException("No Packet Registered for: " + msg.getClass().getCanonicalName());

        byte discriminator = 0; // (byte) this.packets.indexOf(packetClass);
        buffer.writeByte(discriminator);
        packet.encode(ctx, buffer);
        FMLProxyPacket proxyPacket = new FMLProxyPacket(buffer.copy(), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
        out.add(proxyPacket);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FMLProxyPacket proxyPacket, List<Object> out) throws Exception {
        ByteBuf payload = proxyPacket.payload();
        byte discriminator = payload.readByte();
        Class<? extends IOmnisPacket> packetClass = (Class<? extends IOmnisPacket>) out.get(0); //this.packets.get(discriminator);

        if (packetClass == null)
            throw new NullPointerException("No packet registered for discriminator: " + discriminator);

        IOmnisPacket pkt = packetClass.newInstance();
        pkt.decode(ctx, payload.slice());

        EntityPlayer player;
        switch (FMLCommonHandler.instance().getEffectiveSide()) {
            case CLIENT:
                player = this.getClientPlayer();
                pkt.processClientSide(player);
                break;

            case SERVER:
                INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
                player = ((NetHandlerPlayServer) netHandler).playerEntity;
                pkt.processServerSide(player);
                break;

            default:
        }
    }

    @SideOnly(Side.CLIENT)
    private EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }
}
