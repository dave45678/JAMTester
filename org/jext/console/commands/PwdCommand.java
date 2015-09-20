/*
 * Decompiled with CFR 0_102.
 */
package org.jext.console.commands;

import org.jext.Jext;
import org.jext.Utilities;
import org.jext.console.Console;
import org.jext.console.commands.Command;

public class PwdCommand
extends Command {
    private static final String COMMAND_NAME = "pwd";

    public String getCommandName() {
        return "pwd";
    }

    public String getCommandSummary() {
        return Jext.getProperty("console.pwd.command.help");
    }

    public boolean handleCommand(Console console, String string) {
        if (string.equals("pwd")) {
            console.output(Utilities.getUserDirectory());
            return true;
        }
        return false;
    }
}

