package me.jezza.oc.common.core.command;

import me.jezza.oc.common.interfaces.Command;
import net.minecraft.command.ICommandSender;

/**
 * @author Jezza
 */
@Command("oc|set test|omniscore")
public class CommandTest extends CommandAbstract {

	public CommandTest() {
	}

	@Command(value = "help", joiner = ".")
	public void help(ICommandSender sender, String[] args) {
		// oc.help
		// set test.help
		// omniscore.help
	}

	@Command("example")
	public void example(ICommandSender sender, String[] args) {
		// oc example
		// set test example
		// omniscore example
	}

	@Command("help two")
	public void help2(ICommandSender sender, String[] args) {
		// oc help two
		// set test help two
		// omniscore help two
	}

	@Command(value = "help three", joiner = "=")
	public void help3(ICommandSender sender, String[] args) {
		// oc=help two
		// set test=help two
		// omniscore=help two
	}

	@Command(value = "original|alternative test", joiner = "-")
	public void swapping(ICommandSender sender, String[] args) {
		// oc-original test
		// set test-original test
		// omniscore-original test
		// oc-alternative test
		// set test-alternative test
		// omniscore-alternative test
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "Fuck off mate";
	}
}
