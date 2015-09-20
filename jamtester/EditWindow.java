/*
 * Decompiled with CFR 0_102.
 */
package jamtester;

import jamtester.EditFrame;
import jamtester.StatusBar;
import jamtester.studenttool.StudentToolTextArea;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class EditWindow
extends JInternalFrame {
    private StudentToolTextArea tA;
    private File f;
    private StatusBar sb;
    private JMenu file;

    public EditWindow(File file) {
        JMenu jMenu;
        super(file.getName(), true, false);
        this.f = file;
        this.setSize(400, 400);
        this.tA = new StudentToolTextArea(file, true);
        this.getContentPane().add(this.tA);
        JMenuBar jMenuBar = new JMenuBar();
        this.file = jMenu = new JMenu("File");
        jMenu.setMnemonic('F');
        JMenuItem jMenuItem = new JMenuItem("Save");
        jMenuItem.setMnemonic('S');
        jMenuItem.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                EditWindow.this.save();
            }
        });
        jMenu.add(jMenuItem);
        jMenuBar.add(jMenu);
        this.setJMenuBar(jMenuBar);
        this.sb = new StatusBar();
        this.sb.addToInternalFrame(this);
    }

    public void setEditable(boolean bl) {
        this.tA.setEditable(bl);
        this.file.setEnabled(bl);
    }

    public void showStatus(String string) {
        this.sb.display(string);
    }

    public void save() {
        try {
            EditFrame.backupFileIfTeacher(this.f);
            FileOutputStream fileOutputStream = new FileOutputStream(this.f);
            String string = this.tA.getText();
            for (int i = 0; i < string.length(); ++i) {
                fileOutputStream.write(string.charAt(i));
            }
            fileOutputStream.close();
            this.showStatus(this.f.getName() + " saved successfully");
        }
        catch (Exception var1_2) {
            this.showStatus("Unable to save file " + this.f.getName());
        }
    }

}

