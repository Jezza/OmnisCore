package me.jezza.oc.common.core.command;

import static me.jezza.oc.common.utils.helpers.StringHelper.format;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.Side;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.interfaces.Command;
import me.jezza.oc.common.utils.helpers.StringHelper;
import me.jezza.oc.common.utils.reflect.ASM;
import net.minecraft.command.ICommand;

/**
 * @author Jezza
 */
public class CommandRegistry {
	private static boolean init = false;

	private static List<CommandInstance> commands;

	private CommandRegistry() {
	}

	public static void init() {
		if (init)
			return;
		init = true;
		int activeSide = side();
		OmnisCore.logger.info("Active Side: {}", FMLLaunchHandler.side());
		Builder<CommandInstance> commands = ImmutableList.builder();
		for (Entry<ASMData, Class<?>> entry : ASM.classesWith(Command.class).entrySet()) {
			Class<?> annotatedClass = entry.getValue();
			if (Modifier.isAbstract(annotatedClass.getModifiers()))
				continue;
			if (!CommandAbstract.class.isAssignableFrom(annotatedClass))
				throw new IllegalStateException(format("Found @{} on {}, but it doesn't extend {}", Command.class.getSimpleName(), annotatedClass.getCanonicalName(), CommandAbstract.class.getCanonicalName()));
			@SuppressWarnings("unchecked")
			Class<? extends CommandAbstract> commandClass = (Class<? extends CommandAbstract>) annotatedClass;
			Map<String, Object> annotationInfo = entry.getKey().getAnnotationInfo();
			Integer sidesInteger = (Integer) annotationInfo.get("side");
			final int registerSide = sidesInteger == null ? Command.DEFAULT_SIDES : sidesInteger;
			if ((registerSide & activeSide) != 0) {
				OmnisCore.logger.info(StringHelper.format("Discovered @{}() on {}", Command.class.getSimpleName(), commandClass.getCanonicalName()));
				CommandInstance instance = new CommandInstance(commandClass, annotationInfo);
				for (CommandDelegate delegate : instance.delegateMap().values())
					registerCommand(delegate);
				commands.add(instance);
			}
		}
		CommandRegistry.commands = commands.build();
	}

	public static List<CommandInstance> commands() {
		return init ? CommandRegistry.commands : Collections.<CommandInstance>emptyList();
	}

	public static void registerCommand(ICommand command) {
		OmnisCore.proxy.commandHandler().registerCommand(command);
	}

	public static int side() {
		return FMLLaunchHandler.side() == Side.SERVER ? Command.SERVER_SIDE : Command.CLIENT_SIDE;
	}

	public static String name(int side) {
		switch (side) {
			case Command.NONE:
				return "NONE";
			case Command.CLIENT_SIDE:
				return "CLIENT";
			case Command.SERVER_SIDE:
				return "SERVER";
			case Command.BOTH_SIDES:
				return "BOTH SIDES";
			default:
				return "UNKNOWN";
		}
	}

}
