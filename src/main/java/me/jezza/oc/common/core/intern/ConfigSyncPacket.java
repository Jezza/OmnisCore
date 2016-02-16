package me.jezza.oc.common.core.intern;

import static me.jezza.oc.common.core.CoreProperties.MOD_ID;

import io.netty.channel.ChannelHandlerContext;
import me.jezza.oc.common.core.config.ConfigHandler;
import me.jezza.oc.common.interfaces.IOmnisPacket;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.ModPacket;
import me.jezza.oc.common.interfaces.OutputBuffer;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jezza
 */
@ModPacket(MOD_ID)
public class ConfigSyncPacket implements IOmnisPacket {

	private EntityPlayer player;

	public ConfigSyncPacket() {
	}

	public ConfigSyncPacket(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public void encode(ChannelHandlerContext ctx, OutputBuffer buffer) {
		ConfigHandler.writeSync(player, buffer);
	}

	@Override
	public void decode(ChannelHandlerContext ctx, InputBuffer buffer) {
		ConfigHandler.readSync(buffer);
	}

	@Override
	public void processClientSide(EntityPlayer player) {
	}

	@Override
	public void processServerSide(EntityPlayer player) {
	}
}
