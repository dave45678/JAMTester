/*
 * Decompiled with CFR 0_102.
 */
package jamtester.launcher;

import jamtester.GradingToolGui;
import jamtester.jamupdate.JAMUpdate;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.GregorianCalendar;
import org.jext.Jext;

public final class Teacher {
    public static void main(String[] arrstring) {
        try {
            System.setOut(new PrintStream(new FileOutputStream("jamstdout.stdout", true)));
            System.setErr(new PrintStream(new FileOutputStream("jamstderr.sterr", true)));
        }
        catch (Exception var1_1) {
            // empty catch block
        }
        System.out.println("-----------------------------------");
        System.out.println(GregorianCalendar.getInstance().getTime());
        System.out.println("User:  " + System.getProperty("user.name"));
        System.out.println("-----------------------------------");
        System.err.println("-----------------------------------");
        System.err.println(GregorianCalendar.getInstance().getTime());
        System.err.println("User:  " + System.getProperty("user.name"));
        System.err.println("-----------------------------------");
        Jext.init();
        GradingToolGui.handleProperties();
        GradingToolGui.theGradingToolGui = new GradingToolGui();
        JAMUpdate.doBackgroundUpdateIfNecessary();
    }
}

