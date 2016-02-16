package me.jezza.oc.common.core.command;

import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

/**
 * @author Jezza
 */
public class CommandDelegate implements ICommand {
	private final String commandName;
	private final CommandAbstract delegate;

	public CommandDelegate(String commandName, CommandAbstract delegate) {
		this.commandName = commandName;
		this.delegate = delegate;
	}

	@Override
	public String getCommandName() {
		return commandName;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return delegate.getCommandUsage(sender);
	}

	@Override
	public List getCommandAliases() {
		return delegate.getCommandAliases();
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		String[] newArgs = new String[args.length + 1];
		System.arraycopy(args, 0, newArgs, 1, args.length);
		newArgs[0] = commandName;
		delegate.processCommand(sender, newArgs);
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return delegate.canUseCommand(sender);
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		String[] newArgs = new String[args.length + 1];
		System.arraycopy(args, 0, newArgs, 1, args.length);
		newArgs[0] = commandName;
		return delegate.addTabCompletionOptions(sender, newArgs);
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		String[] newArgs = new String[args.length + 1];
		System.arraycopy(args, 0, newArgs, 1, args.length);
		newArgs[0] = commandName;
		return delegate.isUsernameIndex(newArgs, index);
	}

	@Override
	public int compareTo(Object o) {
		return delegate.compareTo((ICommand) o);
	}
}
