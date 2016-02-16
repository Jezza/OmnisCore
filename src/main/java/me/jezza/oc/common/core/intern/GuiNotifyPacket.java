package me.jezza.oc.common.core.intern;

import static me.jezza.oc.common.core.CoreProperties.MOD_ID;

import io.netty.channel.ChannelHandlerContext;
import me.jezza.oc.client.gui.interfaces.IGuiMessageHandler;
import me.jezza.oc.common.interfaces.IOmnisPacket;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.ModPacket;
import me.jezza.oc.common.interfaces.OutputBuffer;
import net.minecraft.entity.player.EntityPlayer;

@ModPacket(MOD_ID)
public class GuiNotifyPacket implements IOmnisPacket {

	private int id, process;

	public GuiNotifyPacket() {
	}

	/**
	 * @param id      The ID of the button.
	 * @param process The ID of the process to be executed.
	 */
	public GuiNotifyPacket(int id, int process) {
		this.id = id;
		this.process = process;
	}

	@Override
	public void encode(ChannelHandlerContext ctx, OutputBuffer buffer) {
		buffer.writeInt(id);
		buffer.writeInt(process);
	}

	@Override
	public void decode(ChannelHandlerContext ctx, InputBuffer buffer) {
		id = buffer.readInt();
		process = buffer.readInt();
	}

	@Override
	public void processClientSide(EntityPlayer player) {
	}

	@Override
	public void processServerSide(EntityPlayer player) {
		if (player.openContainer instanceof IGuiMessageHandler)
			((IGuiMessageHandler) player.openContainer).onClientClick(player, id, process);
	}
}