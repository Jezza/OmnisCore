package me.jezza.dc.common.core.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import me.jezza.dc.client.gui.interfaces.IPacketGuiHandler;
import me.jezza.dc.common.core.CoreProperties;
import net.minecraft.entity.player.EntityPlayer;

public class PacketGuiNotify extends NetworkDispatcher.MessageAbstract<PacketGuiNotify, IMessage> {

    private int id, process;

    public PacketGuiNotify() {
    }

    public PacketGuiNotify(int id, int process) {
        this.id = id;
        this.process = process;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readInt();
        process = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(id);
        buf.writeInt(process);
    }

    @Override
    public IMessage onMessage(PacketGuiNotify message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        if (player.openContainer instanceof IPacketGuiHandler)
            ((IPacketGuiHandler) player.openContainer).onClientClick(player, message.id, message.process);
        return null;
    }

}
