/*
 * Decompiled with CFR 0_102.
 */
package org.jext.console.commands;

import org.jext.Jext;
import org.jext.console.Console;
import org.jext.console.commands.Command;

public class ExitCommand
extends Command {
    private static final String COMMAND_NAME = "exit";

    public String getCommandName() {
        return "exit";
    }

    public String getCommandSummary() {
        return Jext.getProperty("console.exit.command.help");
    }

    public boolean handleCommand(Console console, String string) {
        if (string.equals("exit")) {
            Jext.exit();
            return true;
        }
        return false;
    }
}

