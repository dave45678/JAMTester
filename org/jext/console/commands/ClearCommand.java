/*
 * Decompiled with CFR 0_102.
 */
package org.jext.console.commands;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.jext.Jext;
import org.jext.console.Console;
import org.jext.console.commands.Command;

public class ClearCommand
extends Command {
    private static final String COMMAND_NAME = "clear";

    public String getCommandName() {
        return "clear";
    }

    public String getCommandSummary() {
        return Jext.getProperty("console.clear.command.help");
    }

    public boolean handleCommand(Console console, String string) {
        if (string.equals("clear")) {
            try {
                console.getOutputDocument().remove(0, console.getOutputDocument().getLength());
            }
            catch (BadLocationException var3_3) {
                // empty catch block
            }
            return true;
        }
        return false;
    }
}

