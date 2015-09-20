/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.python.util.PythonInterpreter
 */
package org.jext;

import java.awt.event.ActionEvent;
import java.io.PrintStream;
import org.jext.MenuAction;
import org.jext.scripting.python.Run;
import org.python.util.PythonInterpreter;

public class PythonAction
extends MenuAction {
    private String script;

    public PythonAction(String string, String string2) {
        super(string);
        this.script = string2;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (this.script != null && this.script.length() > 0) {
            try {
                PythonInterpreter pythonInterpreter = Run.getPythonInterpreter(PythonAction.getJextParent(actionEvent));
                pythonInterpreter.set("__evt__", (Object)actionEvent);
                pythonInterpreter.exec(this.script);
            }
            catch (Exception var2_3) {
                System.out.println("python action: " + this.getName());
                var2_3.printStackTrace();
            }
        }
    }
}

