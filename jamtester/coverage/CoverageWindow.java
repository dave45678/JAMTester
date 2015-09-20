/*
 * Decompiled with CFR 0_102.
 */
package jamtester.coverage;

import jamtester.coverage.CoverageInfo;
import jamtester.coverage.CoverageViewer;
import jamtester.mdidesktop.MDIDesktopPane;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;

public class CoverageWindow
extends JFrame {
    private static ArrayList editors = new ArrayList();
    private MDIDesktopPane jDP;

    public CoverageWindow(String string, ArrayList arrayList) {
        super(string);
        this.setSize(800, 800);
        this.jDP = new MDIDesktopPane();
        for (int i = 0; i < arrayList.size(); ++i) {
            CoverageInfo coverageInfo = (CoverageInfo)arrayList.get(i);
            JInternalFrame jInternalFrame = new JInternalFrame(coverageInfo.getName(), true, false, false, true);
            jInternalFrame.getContentPane().setLayout(new GridLayout(1, 1));
            jInternalFrame.getContentPane().add(coverageInfo.getViewer());
            this.jDP.add(jInternalFrame);
            jInternalFrame.show();
            jInternalFrame.setSize(400, 400);
        }
        JMenuBar jMenuBar = new JMenuBar();
        this.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent windowEvent) {
                CoverageWindow.this.removeMe();
            }
        });
        this.setJMenuBar(jMenuBar);
        this.getContentPane().add(this.jDP);
    }

    public void show() {
        if (!CoverageWindow.alreadyOpen(this)) {
            editors.add(this);
            super.show();
        }
    }

    private void removeMe() {
        editors.remove(this);
    }

    private static boolean alreadyOpen(CoverageWindow coverageWindow) {
        for (int i = 0; i < editors.size(); ++i) {
            Object e = editors.get(i);
            if (!coverageWindow.getTitle().equals(((JFrame)e).getTitle())) continue;
            return true;
        }
        return false;
    }

}

