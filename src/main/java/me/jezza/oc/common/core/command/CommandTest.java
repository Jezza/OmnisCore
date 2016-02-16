package me.jezza.oc.common.core.command;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import me.jezza.oc.common.interfaces.Command;
import me.jezza.oc.common.utils.collect.BranchedMap;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

/**
 * @author Jezza
 */
@Command("oc|omniscore|set test")
public class CommandTest extends CommandAbstract {

	public CommandTest(String commandName, List<String> aliases, BranchedMap<String, Method> methodMap) {
		super(commandName, aliases, methodMap);
	}

	@Override
	protected int getRequiredPermissionLevel() {
		return 0;
	}

	@Command(value = "help", joiner = ".")
	public void help(ICommandSender sender, String[] args) {
		// oc.help
		// set test.help
		// omniscore.help
		sendChatMessage(sender, "help: " + Arrays.toString(args));
	}

	@Command("example")
	public void example(ICommandSender sender, String[] args) {
		// oc example
		// set test example
		// omniscore example
		sendChatMessage(sender, "example: " + Arrays.toString(args));
	}

	@Command("help two")
	public void help2(ICommandSender sender, String[] args) {
		// oc help two
		// set test help two
		// omniscore help two
		sendChatMessage(sender, "Help2: " + Arrays.toString(args));
	}

	@Command(value = "help three", joiner = "=")
	public void help3(ICommandSender sender, String[] args) {
		// oc=help two
		// set test=help two
		// omniscore=help two
		sendChatMessage(sender, "Help3: " + Arrays.toString(args));
	}

	@Command(value = "original|alternative test", joiner = "-")
	public void swapping(ICommandSender sender, String[] args) {
		// oc-original test
		// set test-original test
		// omniscore-original test
		// oc-alternative test
		// set test-alternative test
		// omniscore-alternative test
		sendChatMessage(sender, "Swapping: " + Arrays.toString(args));
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "Fuck off mate";
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
		return null;
	}

	@Override
	protected boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

	@Override
	public int compareTo(ICommand o) {
		return 0;
	}
}
