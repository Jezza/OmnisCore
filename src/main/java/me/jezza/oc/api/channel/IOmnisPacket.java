package me.jezza.oc.api.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jezza
 */
public interface IOmnisPacket {

    void encode(ChannelHandlerContext ctx, ByteBuf buffer);

    void decode(ChannelHandlerContext ctx, ByteBuf buffer);

    void processClientSide(EntityPlayer player);

    void processServerSide(EntityPlayer player);
}
