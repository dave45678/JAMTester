/*
 * Decompiled with CFR 0_102.
 */
package org.jext.console.commands;

import org.jext.Jext;
import org.jext.console.Console;
import org.jext.console.commands.Command;

public class HelpCommand
extends Command {
    private static final String COMMAND_NAME = "help";

    public String getCommandName() {
        return "help";
    }

    public String getCommandSummary() {
        return Jext.getProperty("console.help.command.help");
    }

    public boolean handleCommand(Console console, String string) {
        if (string.equals("help")) {
            console.help();
            return true;
        }
        return false;
    }
}

