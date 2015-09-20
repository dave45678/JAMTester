/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.python.util.PythonInterpreter
 */
package org.jext.console.commands;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.console.Console;
import org.jext.console.commands.Command;
import org.jext.scripting.python.PythonLogWindow;
import org.jext.scripting.python.Run;
import org.python.util.PythonInterpreter;

public class EvalCommand
extends Command {
    private static final String COMMAND_NAME = "eval:";

    public String getCommandName() {
        return "eval:scriptlet";
    }

    public String getCommandSummary() {
        return Jext.getProperty("console.eval.command.help");
    }

    public boolean handleCommand(Console console, String string) {
        if (string.startsWith("eval:")) {
            String string2 = string.substring(5);
            if (string2.length() > 0) {
                try {
                    PythonInterpreter pythonInterpreter = Run.getPythonInterpreter(console.getParentFrame());
                    pythonInterpreter.set("__evt__", (Object)new ActionEvent(console.getParentFrame().getTextArea(), 1705, null));
                    pythonInterpreter.exec("import org.jext\n" + string2);
                }
                catch (Exception var4_5) {
                    JOptionPane.showMessageDialog(console.getParentFrame(), Jext.getProperty("python.script.errMessage"), Jext.getProperty("python.script.error"), 0);
                    if (Jext.getBooleanProperty("dawn.scripting.debug")) {
                        console.getParentFrame().getPythonLogWindow().logln(var4_5.toString());
                    }
                }
            } else {
                console.error(Jext.getProperty("console.missing.argument"));
            }
            return true;
        }
        return false;
    }
}

