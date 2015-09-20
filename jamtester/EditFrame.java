/*
 * Decompiled with CFR 0_102.
 */
package jamtester;

import jamtester.JAMProperties;
import jamtester.StatusBar;
import jamtester.studenttool.StudentToolTextArea;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class EditFrame
extends JFrame {
    private StudentToolTextArea tA;
    private File f;
    private static File rootFolder;
    private StatusBar sb;

    public EditFrame(File file) {
        super(file.getName());
        if (rootFolder != null) {
            file = new File(rootFolder, file.getName());
        }
        this.f = file;
        this.setSize(600, 600);
        this.tA = new StudentToolTextArea(file, true);
        this.getContentPane().add(this.tA);
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("File");
        jMenu.setMnemonic('F');
        JMenuItem jMenuItem = new JMenuItem("Save");
        jMenuItem.setMnemonic('S');
        jMenuItem.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                EditFrame.this.save();
            }
        });
        jMenu.add(jMenuItem);
        jMenuBar.add(jMenu);
        this.setJMenuBar(jMenuBar);
        this.sb = new StatusBar();
        this.sb.addToFrame(this);
    }

    public StudentToolTextArea getTextArea() {
        return this.tA;
    }

    public void showStatus(String string) {
        this.sb.display(string);
    }

    public static void setRootFolder(File file) {
        rootFolder = file;
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

    public static void backupFileIfTeacher(File file) {
        if (!JAMProperties.getVersion().equals("teacher")) {
            return;
        }
        try {
            File file2 = new File(file.getParentFile(), file.getName() + ".jamold");
            if (file2.exists()) {
                return;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] arrby = new byte[fileInputStream.available()];
            fileInputStream.read(arrby);
            fileOutputStream.write(arrby);
            fileInputStream.close();
            fileOutputStream.close();
        }
        catch (Exception var1_2) {
            var1_2.printStackTrace();
        }
    }

}

