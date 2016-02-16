package me.jezza.oc.common.core.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.google.common.base.Joiner;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.utils.collect.BranchedMap;
import net.minecraft.command.*;
import net.minecraft.util.ChatComponentText;

public abstract class CommandAbstract implements Comparable<ICommand> {
	protected final String commandName;
	protected final List<String> aliases;
	protected final BranchedMap<String, Method> methodMap;

	public CommandAbstract(String commandName, List<String> aliases, BranchedMap<String, Method> methodMap) {
		this.commandName = commandName;
		this.aliases = aliases;
		this.methodMap = methodMap;
	}

	public final String getCommandName() {
		return commandName;
	}

	public final List<String> getCommandAliases() {
		return aliases;
	}

	public void sendCommandUsage(ICommandSender sender) {
		sendChatMessage(sender, getCommandUsage(sender));
	}

	public void sendChatMessage(ICommandSender sender, String string) {
		sender.addChatMessage(new ChatComponentText(string));
	}

	public void processCommand(ICommandSender sender, String[] args) {
		Method method = methodMap.get(args);
		if (method == null) {
			sendCommandUsage(sender);
			return;
		}
		try {
			method.invoke(this, sender, args);
		} catch (IllegalAccessException | InvocationTargetException e) {
			OmnisCore.logger.error("Failed to invoke the command method: ", e);
		}
	}

	/**
	 * Adds the strings available in this command to the given list of tab completion options.
	 */
	public abstract List<String> addTabCompletionOptions(ICommandSender sender, String[] args);

	public boolean canUseCommand(ICommandSender sender) {
		return sender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
	}

	protected abstract int getRequiredPermissionLevel();

	protected abstract String getCommandUsage(ICommandSender sender);

	/**
	 * Return whether the specified command parameter index is a username parameter.
	 */
	protected abstract boolean isUsernameIndex(String[] args, int index);

	@Override
	public abstract int compareTo(ICommand o);

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(commandName);
		builder.append('|');
		Joiner.on('|').appendTo(builder, this.aliases);
		return builder.append(methodMap).toString();
	}

	/**
	 * Used to parse "~" at the start of an argument, so that the position can be set relative to the current one.
	 * This can also depend on the value that follows directly afterward.
	 *
	 * @param sender   The sender in question, whether a player or console.
	 * @param position The value that will be taken and adjusted accordingly, should it need adjusting.
	 * @param argument The string that contains the argument you wish to parse.
	 * @return The value given any necessary shifts.
	 */
	public static double parseRelativePosition(ICommandSender sender, double position, String argument) {
		return CommandBase.func_110665_a(sender, position, argument, -30000000, 30000000);
	}

	/**
	 * Used to parse "~" at the start of an argument, so that the position can be set relative to the current one.
	 * This can also depend on the value that follows directly afterward.
	 *
	 * @param sender     The sender in question, whether a player or console.
	 * @param position   The value that will be taken and adjusted accordingly, should it need adjusting.
	 * @param argument   The string that contains the argument you wish to parse.
	 * @param lowerBound If the adjusted number is beyond this, throws a {@link net.minecraft.command.NumberInvalidException}
	 * @param upperBound If the adjusted number is beyond this, throws a {@link net.minecraft.command.NumberInvalidException}
	 * @return The value given any necessary shifts.
	 */
	public static double parseRelativePosition(ICommandSender sender, double position, String argument, int lowerBound, int upperBound) {
		return CommandBase.func_110665_a(sender, position, argument, lowerBound, upperBound);
	}

	public static int parseInt(String number) {
		try {
			return Integer.parseInt(number);
		} catch (NumberFormatException numberformatexception) {
			throw new NumberInvalidException("commands.generic.num.invalid", number);
		}
	}

	public static boolean parseBoolean(String booleanValue) {
		if (!booleanValue.equals("true") && !booleanValue.equals("1")) {
			if (!booleanValue.equals("false") && !booleanValue.equals("0"))
				throw new CommandException("commands.generic.boolean.invalid", booleanValue);
			return false;
		}
		return true;
	}
}
