/*
 * Decompiled with CFR 0_102.
 */
package org.jext.console.commands;

import java.io.File;
import org.jext.Jext;
import org.jext.Utilities;
import org.jext.console.Console;
import org.jext.console.commands.Command;

public class ChangeDirCommand
extends Command {
    private static final String COMMAND_NAME = "cd";

    public String getCommandName() {
        return "cd <path>";
    }

    public String getCommandSummary() {
        return Jext.getProperty("console.cd.command.help");
    }

    public boolean handleCommand(Console console, String string) {
        if (string.equals("cd") || string.equals("cd -help")) {
            console.help(Jext.getProperty("console.cd.help"));
            return true;
        }
        if (string.startsWith("cd")) {
            String string2 = Utilities.constructPath(string.substring(3));
            if (new File(string2).exists()) {
                System.getProperties().put("user.dir", string2);
            } else {
                console.error(Jext.getProperty("console.cd.error"));
            }
            return true;
        }
        return false;
    }
}

