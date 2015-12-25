package me.jezza.oc.common.interfaces;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * To register a packet, simply place an {@link ModPacket} on the class.
 * <p/>
 * For a couple of examples, look at some of my internal packet classes.
 * <p/>
 * {@link me.jezza.oc.common.core.intern.GuiNotifyPacket}
 * {@link me.jezza.oc.common.core.intern.ConfigSyncPacket}
 *
 * @author Jezza
 */
public interface IOmnisPacket {

	/**
	 * Fired during packet encoding.
	 * This is where you'd write various representations to the {@link OutputBuffer}, which writes directly to the internal buffer.
	 *
	 * @param ctx    - The object used to access the {@link io.netty.channel.ChannelPipeline}.
	 * @param buffer - The buffer to be written from. The output.
	 */
	void encode(ChannelHandlerContext ctx, OutputBuffer buffer);

	/**
	 * Fired when the packet should be read from the buffer.
	 * {@link InputBuffer} will contain all the same data you wrote, and in the same order.
	 *
	 * @param ctx    - The object used to access the {@link io.netty.channel.ChannelPipeline}.
	 * @param buffer - The buffer to be read from. The input.
	 */
	void decode(ChannelHandlerContext ctx, InputBuffer buffer);

	/**
	 * This method is fired on the client side, right after {@link #decode(ChannelHandlerContext, InputBuffer)}.
	 * This is used for the actual processing of the data that the packet received.
	 *
	 * @param player - The client.
	 */
	void processClientSide(EntityPlayer player);

	/**
	 * This method is fired on the server side, right after {@link #decode(ChannelHandlerContext, InputBuffer)}.
	 * This is used for the actual processing of the data that the packet received.
	 *
	 * @param player - The player from which the packet originated.
	 */
	void processServerSide(EntityPlayer player);
}
