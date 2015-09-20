/*
 * Decompiled with CFR 0_102.
 */
package jplagwebstart;

import java.util.Vector;
import jplagwebstart.JPlagGUI;
import jplagwebstart.JPlagOptions;
import jplagwebstart.Program;

class ScanThread
extends Thread {
    private JPlagGUI gui;
    private JPlagOptions options;

    public ScanThread(JPlagGUI jPlagGUI, JPlagOptions jPlagOptions) {
        this.gui = jPlagGUI;
        this.options = jPlagOptions;
    }

    public void run() {
        Vector vector = null;
        try {
            this.gui.guiSetPreview("Scanning Directories...", null, null, null);
            vector = Program.scanFiles(this.options);
            if (vector == null) {
                this.gui.guiSetPreview("No files found!", "", "", "");
                throw new RuntimeException();
            }
            String[] arrstring = Program.checkSubmissions(vector);
            if (arrstring[0] == null) {
                arrstring[0] = "OK";
            }
            this.gui.guiSetPreview(arrstring[0], arrstring[1], arrstring[2], Integer.toString(vector.size()));
        }
        catch (InterruptedException var2_3) {
            this.gui.guiSetPreview("(interrupted)", "", "", "");
        }
        this.gui.scanThreadEnds(vector, this.options);
    }
}

