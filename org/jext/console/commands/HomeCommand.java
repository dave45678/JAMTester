/*
 * Decompiled with CFR 0_102.
 */
package org.jext.console.commands;

import org.jext.Jext;
import org.jext.console.Console;
import org.jext.console.commands.Command;

public class HomeCommand
extends Command {
    private static final String COMMAND_NAME = "home";

    public String getCommandName() {
        return "home";
    }

    public String getCommandSummary() {
        return Jext.getProperty("console.home.command.help");
    }

    public boolean handleCommand(Console console, String string) {
        if (string.equals("home")) {
            System.getProperties().put("user.dir", Jext.getHomeDirectory());
            return true;
        }
        return false;
    }
}

