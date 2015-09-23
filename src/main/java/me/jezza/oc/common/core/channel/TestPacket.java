package me.jezza.oc.common.core.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import me.jezza.oc.api.channel.IOmnisPacket;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jezza
 */
public class TestPacket implements IOmnisPacket {

    @Override
    public void encode(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeInt(15161);
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf buffer) {
        int i = buffer.readInt();
        System.out.println(i);
    }

    @Override
    public void processClientSide(EntityPlayer player) {

    }

    @Override
    public void processServerSide(EntityPlayer player) {

    }
}
