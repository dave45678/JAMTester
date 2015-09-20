/*
 * Decompiled with CFR 0_102.
 */
package org.jext.console.commands;

import org.jext.Jext;
import org.jext.console.Console;
import org.jext.console.commands.Command;

public class JythonCommand
extends Command {
    private static final String COMMAND_NAME = "jython";

    public String getCommandName() {
        return "jython";
    }

    public String getCommandSummary() {
        return Jext.getProperty("console.jython.command.help");
    }

    public boolean handleCommand(Console console, String string) {
        if (string.equals("jython")) {
            if (Jext.getBooleanProperty("console.jythonMode")) {
                Jext.setProperty("console.jythonMode", "false");
            } else {
                Jext.setProperty("console.jythonMode", "true");
            }
            return true;
        }
        return false;
    }
}

