package me.jezza.oc.common.core.command;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.interfaces.Command;
import me.jezza.oc.common.utils.collect.BranchedMap;
import me.jezza.oc.common.utils.collect.Collections3;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.util.ChatComponentText;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.jezza.oc.common.utils.helpers.StringHelper.*;
import static org.apache.http.util.Args.notNull;

public abstract class CommandAbstract {
	public static final String DEFAULT_JOINER = " ";
	public static final String DEFAULT_SPLITTER = "|";

	private static final Object[] EXPECTED_PARAMETER_TYPES = new Object[]{ICommandSender.class, String[].class};
	private static final String EXPECTED_PARAMETER_TYPES_STRING = Arrays.toString(EXPECTED_PARAMETER_TYPES);

	protected final String commandName;
	protected final List<String> aliases;
	protected final BranchedMap<String, Method> methodMap;

	public CommandAbstract() {
		Class<? extends CommandAbstract> commandClass = getClass();
		Command classCommand = commandClass.getAnnotation(Command.class);
		if (classCommand == null)
			throw new IllegalStateException(format("@{} was not found on the class: {}.", Command.class.getSimpleName(), commandClass.getCanonicalName()));
		Iterable<String> commandNames = Splitter.on(classCommand.nameSplitter()).trimResults().omitEmptyStrings().split(classCommand.value());
		String commandName = null;
		List<String> aliases = new ArrayList<>();
		for (String value : commandNames) {
			String name = split(value)[0];
			if (commandName == null) {
				commandName = name;
			} else {
				aliases.add(name);
			}
		}
		this.commandName = notNull(commandName, "commandName");
		this.methodMap = new BranchedMap<>();
		for (Method method : commandClass.getDeclaredMethods()) {
			Command methodCommand = method.getAnnotation(Command.class);
			if (methodCommand == null)
				continue;
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes.length != 2)
				throw new IllegalArgumentException(format("Method has been annotated with @{}, but it has an invalid parameter length. Expected parameters: {}, discovered: {}.", Command.class.getSimpleName(), EXPECTED_PARAMETER_TYPES_STRING, Arrays.toString(parameterTypes)));
			if (!arrayContentsEqual(EXPECTED_PARAMETER_TYPES, parameterTypes))
				throw new IllegalArgumentException(format("Method has been annotated with @{}, but it has an invalid parameter types. Expected parameters: {}, discovered: {}.", Command.class.getSimpleName(), EXPECTED_PARAMETER_TYPES_STRING, Arrays.toString(parameterTypes)));
			String value = methodCommand.value();
			if (!useable(value))
				throw new IllegalArgumentException(format("Method has been annotated with @{}, but it has an invalid command value. (This could mean it's either empty, or all whitespace.)", Command.class.getSimpleName(), EXPECTED_PARAMETER_TYPES_STRING, Arrays.toString(parameterTypes)));
			final String joiner;
			if (useable(methodCommand.joiner())) {
				joiner = methodCommand.joiner();
			} else if (useable(classCommand.joiner())) {
				joiner = classCommand.joiner();
			} else {
				joiner = DEFAULT_JOINER;
			}
			Iterable<String> sub = Splitter.on(methodCommand.nameSplitter()).trimResults().omitEmptyStrings().split(value);
			for (String name : commandNames) {
				for (String subCommand : sub) {
					String[] split = split(name + joiner + subCommand);
					Method oldMethod = methodMap.put(split, method);
					if (oldMethod != null)
						throw new IllegalStateException(format("Multiple methods exist with the same command signature. {}.{} and {}.{}", commandClass.getCanonicalName(), method.getName(), commandClass.getCanonicalName(), oldMethod.getName()));
					// if (!commandName.equals(split[0]) && !aliases.contains(split[0])) {}
				}
			}
		}
		this.aliases = Collections3.unmodifiable(aliases);
		System.out.println(commandName + "|" + Joiner.on('|').join(this.aliases));
		System.out.println(Joiner.on('|').join(commandNames));
		System.out.println();
		System.out.println(methodMap);
	}

	private static boolean arrayContentsEqual(Object[] a1, Object[] a2) {
		if (a1 == null)
			return a2 == null || a2.length == 0;
		if (a2 == null)
			return a1.length == 0;
		if (a1.length != a2.length)
			return false;
		for (int i = 0; i < a1.length; i++)
			if (a1[i] != a2[i])
				return false;
		return true;
	}

	private static boolean startsWith(String[] target, String[] with) {
		if (with.length > target.length)
			return false;
		for (int i = 0; i < with.length; i++)
			if (!with[i].equals(target[i]))
				return false;
		return true;
	}

	public static void main(String[] args) {
		new CommandTest();
//		BranchedMap<String, String> map = new BranchedMap<>("ThisIsTheRootValue");
//		map.put(new String[]{"first", "second", "first"}, "FIRST BRANCH VALUE");
//		map.put(new String[]{"first", "second", "second"}, "SECOND BRANCH VALUE");
//		map.put(new String[]{"first", "second", "third"}, "THIRD BRANCH VALUE");
//		map.put(new String[]{"first", "second", "third", "fourth"}, "THIRD BRANCH VALUE");
//		map.put(new String[]{"second", "second", "third", "fourth"}, "THIRD BRANCH VALUE");
//		map.put(new String[]{"third", "second", "third", "first"}, "THIRD BRANCH VALUE");
//		map.put(new String[]{"third", "second", "third", "second"}, "THIRD BRANCH VALUE");
//		map.put(new String[]{"third", "second", "third", "third"}, "THIRD BRANCH VALUE");

//		System.out.println(map.get(new String[]{"first", "second"}));
//		System.out.println(map.get(new String[]{"first", "second", "first"}));
//		System.out.println(map.get(new String[]{"first", "second", "second"}));
//		System.out.println(map.get(new String[]{"first", "second", "third"}));
//		System.out.println(map.get(new String[]{"first", "second", "third", "fourth"}));
//		System.out.println(map.get(new String[]{"first", "second", "third", "fifth"}));
//		System.out.println(map.get(new String[]{"first", "second", "fourth"}));
//		System.out.println(map);
//		System.out.println(map.remove(new String[]{"first", "second", "third"}));
//		System.out.println(map.get(new String[]{"first", "second", "third"}));
//		System.out.println(map.get(new String[]{"first", "second", "third", "fourth"}));
//		System.out.println(map);
	}

	public final String getCommandName() {
		return commandName;
	}

	public final List getCommandAliases() {
		return aliases;
	}

	public abstract String getCommandUsage(ICommandSender sender);

	public void sendCommandUsage(ICommandSender sender) {
		sendChatMessage(sender, getCommandUsage(sender));
	}

	public void sendChatMessage(ICommandSender sender, String string) {
		sender.addChatMessage(new ChatComponentText(string));
	}

	public void processCommand(ICommandSender sender, String[] args) {
		//	sendChatMessage(sender, Arrays.asList(args).toString());
		//	List<String> sentMessages = Client.sentMessages();
		//	System.out.println(sentMessages.get(sentMessages.size() - 1));
	}

	public boolean canUseCommand(ICommandSender sender) {
		return sender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
	}

	public int getRequiredPermissionLevel() {
		return 0;
	}

	/**
	 * Return whether the specified command parameter index is a username parameter.
	 */
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

	/**
	 * Adds the strings available in this command to the given list of tab completion options.
	 */
	public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		return null;
	}

	public int compareTo(Object o) {
		return 0;
	}


	public final CommandAbstract registerClient() {
		// TODO onRegisterMethods
		if (OmnisCore.proxy.isClient()) {
//			ClientCommandHandler.instance.registerCommand(this);
		}
		return this;
	}


	public final CommandAbstract registerServer() {
		// TODO onRegisterMethods
		if (OmnisCore.proxy.isServer()) {
//			((CommandHandler) MinecraftServer.getServer().getCommandManager()).registerCommand(this);
		}
		return this;
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
