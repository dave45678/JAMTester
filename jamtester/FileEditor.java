/*
 * Decompiled with CFR 0_102.
 */
package jamtester;

import jamtester.EditWindow;
import jamtester.mdidesktop.MDIDesktopPane;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class FileEditor
extends JFrame {
    private static ArrayList editors = new ArrayList();
    private MDIDesktopPane jDP;

    public FileEditor(String string, File[] arrfile) {
        AbstractButton abstractButton;
        super(string);
        this.setSize(800, 800);
        this.jDP = new MDIDesktopPane();
        for (int i = 0; i < arrfile.length; ++i) {
            abstractButton = new EditWindow(arrfile[i]);
            this.jDP.add((JInternalFrame)abstractButton);
            abstractButton.setSize(400, 400);
            abstractButton.show();
        }
        JMenuBar jMenuBar = new JMenuBar();
        abstractButton = new JMenu("File");
        abstractButton.setMnemonic('F');
        JMenuItem jMenuItem = new JMenuItem("Save");
        jMenuItem.setMnemonic('S');
        jMenuItem.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                ((EditWindow)FileEditor.this.jDP.getSelectedFrame()).save();
            }
        });
        abstractButton.add(jMenuItem);
        JMenuItem jMenuItem2 = new JMenuItem("Save All");
        jMenuItem2.setMnemonic('A');
        jMenuItem2.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                JInternalFrame[] arrjInternalFrame = FileEditor.this.jDP.getAllFrames();
                for (int i = 0; i < arrjInternalFrame.length; ++i) {
                    ((EditWindow)arrjInternalFrame[i]).save();
                }
            }
        });
        this.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent windowEvent) {
                FileEditor.this.removeMe();
            }
        });
        abstractButton.add(jMenuItem2);
        jMenuBar.add((JMenu)abstractButton);
        this.setJMenuBar(jMenuBar);
        this.getContentPane().add(this.jDP);
    }

    public void show() {
        if (!FileEditor.alreadyOpen(this)) {
            editors.add(this);
            super.show();
        }
    }

    private void removeMe() {
        editors.remove(this);
    }

    private static boolean alreadyOpen(FileEditor fileEditor) {
        for (int i = 0; i < editors.size(); ++i) {
            Object e = editors.get(i);
            if (!fileEditor.getTitle().equals(((JFrame)e).getTitle())) continue;
            return true;
        }
        return false;
    }

}

