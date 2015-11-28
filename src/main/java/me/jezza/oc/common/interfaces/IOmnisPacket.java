package me.jezza.oc.common.interfaces;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * To register a packet, simply place an @OmnisPacket on the class.
 *
 * @author Jezza
 */
public interface IOmnisPacket {
	void encode(ChannelHandlerContext ctx, OutputBuffer buffer);
	void decode(ChannelHandlerContext ctx, InputBuffer buffer);

	void processClientSide(EntityPlayer player);
	void processServerSide(EntityPlayer player);
}
