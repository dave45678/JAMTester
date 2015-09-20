/*
 * Decompiled with CFR 0_102.
 */
package jamtester.evalsupp;

import jamtester.EditWindow;
import jamtester.JavaUtilities;
import jamtester.StudentResult;
import jamtester.mdidesktop.MDIDesktopPane;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.io.File;
import java.io.PrintStream;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class FileViewerFrame
extends JPanel {
    private MDIDesktopPane dP;

    public FileViewerFrame() {
        this.initComponents();
        this.dP = new MDIDesktopPane();
        this.add((Component)this.dP, "Center");
    }

    public void load(StudentResult studentResult) {
        JInternalFrame[] arrjInternalFrame = this.dP.getAllFrames();
        for (int i = 0; i < arrjInternalFrame.length; ++i) {
            this.dP.remove(arrjInternalFrame[i]);
        }
        try {
            File file = studentResult.getDir();
            File[] arrfile = JavaUtilities.getFilesToCompile(file).toArray(new File[0]);
            for (int j = 0; j < arrfile.length; ++j) {
                try {
                    System.out.println(arrfile[j]);
                    EditWindow editWindow = new EditWindow(arrfile[j]);
                    this.dP.add(editWindow);
                    editWindow.show();
                    editWindow.setEditable(true);
                    continue;
                }
                catch (Exception var6_9) {
                    return;
                }
            }
        }
        catch (Exception var3_5) {
            return;
        }
        this.dP.validate();
        this.dP.tileFrames();
        this.dP.repaint();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
    }
}

