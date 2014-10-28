package me.jezza.oc.common.core;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public abstract class CommandAbstract extends CommandBase {

    private String commandName = "";
    private String commandUsage = "";

    public CommandAbstract(String commandName, String commandUsage) {
        this.commandName = commandName;
        this.commandUsage = commandUsage;
    }

    public CommandAbstract register() {
        CommandHandler ch = (CommandHandler) MinecraftServer.getServer().getCommandManager();
        ch.registerCommand(this);
        return this;
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + commandName + " " + commandUsage;
    }

    public void sendCommandUsage(ICommandSender commandSender) {
        sendChatMessage(commandSender, getCommandUsage(commandSender));
    }

    public void sendChatMessage(ICommandSender commandSender, String string) {
        commandSender.addChatMessage(new ChatComponentText(string));
    }

    /**
     * Oxford/Serial comma.
     * joinNiceString("Science", "Art", "Religion") returns "Science, Art, and Religion"
     */
    public static String joinNiceString(Object[] objects) {
        StringBuilder stringbuilder = new StringBuilder();

        for (int i = 0; i < objects.length; ++i) {
            if (i > 0) {
                stringbuilder.append(", ");
                if (i == objects.length - 1)
                    stringbuilder.append("and ");
            }

            stringbuilder.append(objects[i].toString());
        }

        return stringbuilder.toString();
    }

    /**
     * Oxford/Serial comma.
     * joinNiceString("Science", "Art", "Religion") returns "Science, Art, and Religion"
     */
    public static IChatComponent joinNiceString(IChatComponent[] components) {
        ChatComponentText chatcomponenttext = new ChatComponentText("");

        for (int i = 0; i < components.length; ++i) {
            if (i > 0) {
                chatcomponenttext.appendText(", ");
                if (i == components.length - 1)
                    chatcomponenttext.appendText("and ");
            }

            chatcomponenttext.appendSibling(components[i]);
        }

        return chatcomponenttext;
    }
}
