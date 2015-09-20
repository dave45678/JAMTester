/*
 * Decompiled with CFR 0_102.
 */
package org.jext.console.commands;

import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.Utilities;
import org.jext.console.Console;
import org.jext.console.commands.Command;
import org.jext.scripting.python.Run;

public class RunCommand
extends Command {
    private static final String COMMAND_NAME = "run:";

    public String getCommandName() {
        return "run:script";
    }

    public String getCommandSummary() {
        return Jext.getProperty("console.run.command.help");
    }

    public boolean handleCommand(Console console, String string) {
        if (string.startsWith("run:")) {
            String string2 = string.substring(4);
            if (string2.length() > 0) {
                Run.runScript(Utilities.constructPath(string2), console.getParentFrame());
            } else {
                console.error(Jext.getProperty("console.missing.argument"));
            }
            return true;
        }
        return false;
    }
}

