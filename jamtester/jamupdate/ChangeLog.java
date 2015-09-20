/*
 * Decompiled with CFR 0_102.
 */
package jamtester.jamupdate;

import jamtester.GradingToolAboutDialog;
import jamtester.studenttool.StudentToolTextArea;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.io.File;
import javax.swing.JFrame;

public class ChangeLog
extends JFrame {
    public ChangeLog() {
        super("JAM*Tester Change Log");
        this.setSize(800, 600);
        GradingToolAboutDialog.centerWindow(this);
        File file = new File("changeLog.txt");
        if (!file.exists()) {
            return;
        }
        StudentToolTextArea studentToolTextArea = new StudentToolTextArea(file, false);
        studentToolTextArea.scrollTo(0, 0);
        this.getContentPane().setLayout(new GridLayout(1, 1));
        this.getContentPane().add(studentToolTextArea);
        studentToolTextArea.removeTokenMarker();
        this.show();
    }
}

