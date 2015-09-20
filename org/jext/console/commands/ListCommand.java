/*
 * Decompiled with CFR 0_102.
 */
package org.jext.console.commands;

import org.jext.Jext;
import org.jext.console.Console;
import org.jext.console.ConsoleListDir;
import org.jext.console.commands.Command;

public class ListCommand
extends Command {
    private static final String COMMAND_NAME = "ls";
    private static final String COMMAND_NAME_ALTERNATE = "dir";

    public String getCommandName() {
        return "ls";
    }

    public String getCommandSummary() {
        return Jext.getProperty("console.ls.command.help");
    }

    public boolean handleCommand(Console console, String string) {
        if (string.equals("ls") || string.equals("dir")) {
            ConsoleListDir.list(console, null);
            return true;
        }
        if (string.startsWith("ls") || string.startsWith("dir")) {
            ConsoleListDir.list(console, string.substring(2));
            return true;
        }
        return false;
    }
}

