/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.python.core.Py
 *  org.python.core.PyModule
 *  org.python.core.PyObject
 *  org.python.core.PySystemState
 *  org.python.core.imp
 *  org.python.util.PythonInterpreter
 */
package org.jext.scripting.python;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import javax.swing.JOptionPane;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.scripting.python.PythonLogWindow;
import org.python.core.Py;
import org.python.core.PyModule;
import org.python.core.PyObject;
import org.python.core.PySystemState;
import org.python.core.imp;
import org.python.util.PythonInterpreter;

public final class Run {
    private static PythonInterpreter parser;

    public static PythonInterpreter getPythonInterpreter(JextFrame jextFrame) {
        if (parser == null) {
            parser = new PythonInterpreter();
            PyModule pyModule = imp.addModule((String)"__main__");
            parser.setLocals(pyModule.__dict__);
            Class class_ = Jext.class;
            InputStream inputStream = class_.getResourceAsStream("packages");
            if (inputStream != null) {
                PySystemState pySystemState = Py.getSystemState();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                try {
                    String string;
                    while ((string = bufferedReader.readLine()) != null) {
                        PySystemState.add_package((String)string);
                    }
                    bufferedReader.close();
                }
                catch (IOException var6_6) {
                    // empty catch block
                }
            }
        }
        parser.set("__jext__", (Object)jextFrame);
        if (jextFrame != null) {
            parser.setErr(jextFrame.getPythonLogWindow().getStdErr());
            parser.setOut(jextFrame.getPythonLogWindow().getStdOut());
        } else {
            parser.setOut((OutputStream)System.out);
            parser.setErr((OutputStream)System.err);
        }
        return parser;
    }

    public static PyObject eval(String string, String string2, Object[] arrobject, JextFrame jextFrame) {
        try {
            PythonInterpreter pythonInterpreter = Run.getPythonInterpreter(jextFrame);
            if (arrobject != null && string2 != null) {
                pythonInterpreter.set(string2, (Object)arrobject);
            }
            return pythonInterpreter.eval(string);
        }
        catch (Exception var4_5) {
            JOptionPane.showMessageDialog(jextFrame, Jext.getProperty("python.script.errMessage"), Jext.getProperty("python.script.error"), 0);
            if (Jext.getBooleanProperty("dawn.scripting.debug")) {
                System.err.println(var4_5.toString());
            }
            parser = null;
            return null;
        }
    }

    public static void execute(String string, JextFrame jextFrame) {
        try {
            PythonInterpreter pythonInterpreter = Run.getPythonInterpreter(jextFrame);
            pythonInterpreter.exec(string);
        }
        catch (Exception var2_3) {
            JOptionPane.showMessageDialog(jextFrame, Jext.getProperty("python.script.errMessage"), Jext.getProperty("python.script.error"), 0);
            if (Jext.getBooleanProperty("dawn.scripting.debug")) {
                jextFrame.getPythonLogWindow().logln(var2_3.toString());
            }
            parser = null;
        }
    }

    public static void runScript(String string, JextFrame jextFrame) {
        try {
            PythonInterpreter pythonInterpreter = Run.getPythonInterpreter(jextFrame);
            pythonInterpreter.execfile(string);
        }
        catch (Exception var2_3) {
            JOptionPane.showMessageDialog(jextFrame, Jext.getProperty("python.script.errMessage"), Jext.getProperty("python.script.error"), 0);
            if (Jext.getBooleanProperty("dawn.scripting.debug")) {
                jextFrame.getPythonLogWindow().logln(var2_3.toString());
            }
            parser = null;
        }
    }
}

