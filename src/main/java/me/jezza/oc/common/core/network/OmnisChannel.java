package me.jezza.oc.common.core.network;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * @author Jezza
 */
public class OmnisChannel extends MessageToMessageCodec<FMLProxyPacket, IOmnisPacket> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, IOmnisPacket packet, List<Object> list) throws Exception {
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, FMLProxyPacket fmlProxyPacket, List<Object> list) throws Exception {
    }
}
