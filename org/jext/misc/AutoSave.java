/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;

public class AutoSave
extends Thread {
    private JextFrame parent;
    private static int interval;

    public AutoSave(JextFrame jextFrame) {
        super("----thread: autosave: jext----");
        this.parent = jextFrame;
        this.setDaemon(true);
        this.start();
    }

    public static int getInterval() {
        return interval;
    }

    public static void setInterval(int n) {
        interval = n;
    }

    public void run() {
        try {
            interval = Integer.parseInt(Jext.getProperty("editor.autoSaveDelay"));
        }
        catch (NumberFormatException var1_1) {
            interval = 60;
        }
        if (interval == 0) {
            return;
        }
        interval*=1000;
        do {
            try {
                Thread.sleep(interval);
            }
            catch (InterruptedException var1_3) {
                return;
            }
            JextTextArea[] arrjextTextArea = this.parent.getTextAreas();
            for (int i = 0; i < arrjextTextArea.length; ++i) {
                arrjextTextArea[i].autoSave();
            }
        } while (!AutoSave.interrupted());
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.parent = null;
    }
}

