package me.jezza.oc.api.channel;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jezza
 */
public interface IOmnisPacket {
    void encode(ChannelHandlerContext ctx, OutputBuffer buffer);
    void decode(ChannelHandlerContext ctx, InputBuffer buffer);

    void processClientSide(EntityPlayer player);
    void processServerSide(EntityPlayer player);
}
