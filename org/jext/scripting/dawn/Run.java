/*
 * Decompiled with CFR 0_102.
 */
package org.jext.scripting.dawn;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import javax.swing.JOptionPane;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.Utilities;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.scripting.dawn.DawnLogWindow;

public final class Run {
    public static void execute(String string, JextFrame jextFrame) {
        Run.execute(string, jextFrame, true);
    }

    public static void execute(String string, JextFrame jextFrame, boolean bl) {
        if (!bl) {
            try {
                String string2;
                if (!DawnParser.isInitialized()) {
                    DawnParser.init();
                    Class class_ = Jext.class;
                    DawnParser.installPackage(class_, "dawn-jext.scripting");
                }
                DawnParser dawnParser = new DawnParser(new StringReader(string));
                dawnParser.setProperty("JEXT.JEXT_FRAME", jextFrame);
                dawnParser.exec();
                if (Jext.getBooleanProperty("dawn.scripting.debug") && (string2 = dawnParser.dump()).length() > 0) {
                    jextFrame.getDawnLogWindow().logln(string2);
                }
            }
            catch (DawnRuntimeException var3_4) {
                JOptionPane.showMessageDialog(jextFrame, var3_4.getMessage(), Jext.getProperty("dawn.script.error"), 0);
            }
        } else {
            new ThreadExecuter(string, jextFrame);
        }
    }

    public static void runScript(String string, JextFrame jextFrame) {
        Run.runScript(string, jextFrame, true);
    }

    public static void runScript(String string, JextFrame jextFrame, boolean bl) {
        try {
            String string2;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(string)));
            StringBuffer stringBuffer = new StringBuffer();
            while ((string2 = bufferedReader.readLine()) != null) {
                stringBuffer.append(string2).append('\n');
            }
            bufferedReader.close();
            Run.execute(stringBuffer.toString(), jextFrame, bl);
        }
        catch (IOException var3_4) {
            Utilities.showError(Jext.getProperty("dawn.script.cannotread"));
        }
    }

    static class ThreadExecuter
    extends Thread {
        private String code;
        private JextFrame parent;

        ThreadExecuter(String string, JextFrame jextFrame) {
            super("---Thread:Dawn runtime---");
            this.code = string;
            this.parent = jextFrame;
            this.start();
        }

        public void run() {
            try {
                String string;
                if (!DawnParser.isInitialized()) {
                    DawnParser.init();
                    Class class_ = Run.class$org$jext$Jext == null ? (Run.class$org$jext$Jext = Run.class$("org.jext.Jext")) : Run.class$org$jext$Jext;
                    DawnParser.installPackage(class_, "dawn-jext.scripting");
                }
                DawnParser dawnParser = new DawnParser(new StringReader(this.code));
                dawnParser.setProperty("JEXT.JEXT_FRAME", this.parent);
                dawnParser.exec();
                if (Jext.getBooleanProperty("dawn.scripting.debug") && (string = dawnParser.dump()).length() > 0) {
                    this.parent.getDawnLogWindow().logln(string);
                }
            }
            catch (DawnRuntimeException var1_2) {
                JOptionPane.showMessageDialog(this.parent, var1_2.getMessage(), Jext.getProperty("dawn.script.error"), 0);
            }
        }
    }

}

