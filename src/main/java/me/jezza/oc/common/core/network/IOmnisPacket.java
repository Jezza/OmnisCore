package me.jezza.oc.common.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jezza
 */
public interface IOmnisPacket {

    void encode(ChannelHandlerContext ctx, ByteBuf buffer);

    void decode(ChannelHandlerContext ctx, ByteBuf buffer);

    void clientSide(EntityPlayer player);

    void serverSide(EntityPlayer player);
}
