/*
 * Decompiled with CFR 0_102.
 */
package org.jext.console.commands;

import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.Utilities;
import org.jext.console.Console;
import org.jext.console.commands.Command;

public class FileCommand
extends Command {
    private static final String COMMAND_NAME = "file:";

    public String getCommandName() {
        return "file:filename";
    }

    public String getCommandSummary() {
        return Jext.getProperty("console.file.command.help");
    }

    public boolean handleCommand(Console console, String string) {
        if (string.startsWith("file:")) {
            String string2 = string.substring(5);
            if (string2.length() > 0) {
                console.getParentFrame().open(Utilities.constructPath(string2));
            } else {
                console.error(Jext.getProperty("console.missing.argument"));
            }
            return true;
        }
        return false;
    }
}

