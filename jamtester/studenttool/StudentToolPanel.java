/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  com.sun.javadoc.MethodDoc
 */
package jamtester.studenttool;

import com.sun.javadoc.MethodDoc;
import jamtester.Results;
import jamtester.javatools.JavaParser;
import jamtester.studenttool.StudentTool;
import jamtester.studenttool.StudentToolEditPanel;
import jamtester.studenttool.StudentToolLeftPanel;
import jamtester.studenttool.StudentToolResults;
import jamtester.studenttool.StudentToolTextArea;
import jamtester.studenttool.Tab;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

public class StudentToolPanel
extends JPanel {
    private String name;
    private StudentToolEditPanel rpanel;
    private StudentToolLeftPanel lpanel;
    private File parentDir;
    private File startFile;
    private Tab associatedTab;
    private Timer t;
    private JSplitPane split;

    public StudentToolPanel(File file, String string, File file2) {
        this.startFile = file;
        this.name = string;
        this.setLayout(new BorderLayout());
        if (file2 == null) {
            StudentTool studentTool = new StudentTool(JavaParser.parseFile(file));
            this.rpanel = new StudentToolEditPanel(studentTool, this);
        } else {
            StudentTool studentTool = new StudentTool(JavaParser.parseFile(file));
            this.rpanel = new StudentToolEditPanel(studentTool, file2, this);
        }
        this.lpanel = new StudentToolLeftPanel(new StudentToolResults(), this.rpanel, file, string);
        this.rpanel.setMinimumSize(new Dimension(100, 10));
        this.lpanel.setMinimumSize(new Dimension(266, 10));
        this.split = new JSplitPane(1, this.lpanel, this.rpanel);
        JPanel jPanel = this.lpanel.getResultsAndButtonPanel();
        jPanel.setPreferredSize(new Dimension(100, 200));
        ((StudentToolResults)this.lpanel.getResults()).getSP().getTopComponent().setSize(new Dimension(100, 200));
        ((StudentToolResults)this.lpanel.getResults()).getSP().resetToPreferredSizes();
        this.add((Component)jPanel, "North");
        this.add((Component)this.split, "Center");
        this.t = new Timer();
        this.t.schedule(new TimerTask(){

            public void run() {
                if (StudentToolPanel.this.isShowing()) {
                    StudentToolPanel.this.refreshClass();
                }
            }
        }, 0, 30000);
        if (this.rpanel.getTextArea().getText().length() == 0) {
            this.rpanel.setNew();
        }
    }

    public Tab getTab() {
        return this.associatedTab;
    }

    public StudentToolPanel(File file, String string) {
        this(file, string, null);
    }

    public String getFileName() {
        return this.startFile.getAbsolutePath();
    }

    void setTestFileName(File file) {
        this.name = file.getName().substring(0, file.getName().length() - ".java".length());
        if (this.associatedTab != null) {
            this.associatedTab.setTestFile(new File(this.startFile.getParentFile(), this.name + ".java").getAbsolutePath());
        }
        this.setName(this.name);
    }

    public void setTabName(String string) {
        ((JTabbedPane)this.getParent()).setTitleAt(((JTabbedPane)this.getParent()).indexOfComponent(this), string);
    }

    public String getTestFileName() {
        return this.name;
    }

    public void setParentDir(File file) {
        this.parentDir = file;
    }

    public void setAssociatedTab(Tab tab) {
        this.associatedTab = tab;
    }

    public JSplitPane getSP() {
        return ((StudentToolResults)this.lpanel.getResults()).getSP();
    }

    public void saveTestFile() {
        System.err.println("PARENT DIR:  " + this.parentDir);
        File file = this.rpanel.saveTemp();
        System.err.println("F:  " + file);
        File file2 = new File(this.startFile.getParentFile(), file.getName());
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            byte[] arrby = new byte[fileInputStream.available()];
            fileInputStream.read(arrby);
            fileOutputStream.write(arrby);
            fileInputStream.close();
            fileOutputStream.close();
        }
        catch (Exception var3_4) {
            // empty catch block
        }
    }

    public void refreshClass() {
        this.rpanel.setItems(new StudentTool(JavaParser.parseFile(this.startFile)).getParser().getMethods());
    }

    public StudentToolEditPanel getEditor() {
        return this.rpanel;
    }

    public StudentToolLeftPanel getLeft() {
        return this.lpanel;
    }

    public File getStartFile() {
        return this.startFile;
    }

}

