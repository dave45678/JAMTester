/*
 * Decompiled with CFR 0_102.
 */
package jamtester.studenttool;

import jamtester.Results;
import jamtester.studenttool.StudentFrame;
import jamtester.studenttool.StudentToolEditPanel;
import jamtester.studenttool.StudentToolLeftPanel;
import jamtester.studenttool.StudentToolPanel;
import jamtester.studenttool.Tab;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class StudentToolLeftPanel
extends JPanel {
    private Results results;
    private StudentToolEditPanel step;
    private JButton testBut;
    private JButton autoBut;
    private JButton newBut;
    private JButton loadBut;

    public StudentToolLeftPanel(Results results, StudentToolEditPanel studentToolEditPanel, File file, final String string) {
        this.step = studentToolEditPanel;
        this.results = results;
        this.setLayout(new GridLayout(1, 1, 0, 10));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, 1));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(1, 1));
        this.testBut = new JButton(">>  Run Test Class  <<");
        this.testBut.setFont(this.testBut.getFont().deriveFont(1, 15.0f));
        this.testBut.setForeground(Color.green.darker().darker());
        this.testBut.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                StudentToolLeftPanel.this.results.empty();
                StudentToolLeftPanel.this.step.save();
                File file = new File(StudentToolLeftPanel.access$100((StudentToolLeftPanel)StudentToolLeftPanel.this).parent.getTab().getTestFile());
                StudentToolLeftPanel.this.results.setMethodNames(file);
                Thread thread = new Thread(this, file){
                    private final /* synthetic */ File val$f;
                    private final /* synthetic */  this$1;

                    public void run() {
                        StudentToolLeftPanel.access$100((StudentToolLeftPanel).access$200(()this.this$1)).parent.saveTestFile();
                        StudentToolLeftPanel.access$000(.access$200(this.this$1)).clearAllTextAreas();
                        StudentFrame.showStatus(StudentToolLeftPanel.access$100((StudentToolLeftPanel).access$200(()this.this$1)).parent.getTestFileName() + " saved successfully");
                        jamtester.GradingTool.testOneSansGui(StudentFrame.directory, this.val$f, StudentFrame.getClassFiles(StudentFrame.directory), StudentToolLeftPanel.access$000(.access$200(this.this$1)), true, true, StudentToolLeftPanel.access$100((StudentToolLeftPanel).access$200(()this.this$1)).parent.getStartFile());
                    }
                };
                thread.start();
            }

            static /* synthetic */ StudentToolLeftPanel access$200( var0) {
                return var0.StudentToolLeftPanel.this;
            }
        });
        jPanel2.add(this.testBut);
        jPanel2.setSize(jPanel.getWidth() - 5, jPanel.getHeight() / 5 - 10);
        jPanel.add(jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridLayout(3, 1, 0, 10));
        jPanel3.setBorder(new TitledBorder("JUnit Test File Manipulation"));
        this.autoBut = new JButton("Auto-Generate JUnit Test Class for your Source");
        this.autoBut.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                if (StudentToolLeftPanel.this.askContinue()) {
                    StudentToolLeftPanel.this.step.autoGenerate(string);
                }
            }
        });
        jPanel3.add(this.autoBut);
        this.newBut = new JButton("Create new JUnit Test Class");
        this.newBut.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                if (StudentToolLeftPanel.this.askContinue()) {
                    StudentToolLeftPanel.this.step.setNew(string);
                }
            }
        });
        jPanel3.add(this.newBut);
        this.loadBut = new JButton("Import JUnit Test Class");
        this.loadBut.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                if (StudentToolLeftPanel.this.askContinue("Make sure you have saved your work before continuing.  Ok to proceed?")) {
                    File[] arrfile = StudentFrame.showOpenDialog("Open JUnit Test File", false, false, true, StudentFrame.directory, new String[]{".java"}, true);
                    if (arrfile.length == 0) {
                        return;
                    }
                    if (StudentFrame.isJUnitTestCase(arrfile[0])) {
                        StudentToolLeftPanel.this.step.openFile(arrfile[0]);
                        StudentToolLeftPanel.access$100((StudentToolLeftPanel)StudentToolLeftPanel.this).parent.setTabName(arrfile[0].getName().substring(0, arrfile[0].getName().length() - ".java".length()));
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid JUnit Test File, file not imported.", "Invalid", 0);
                    }
                }
            }
        });
        jPanel3.add(this.loadBut);
        jPanel.add(jPanel3);
        this.add(jPanel);
    }

    Results getResults() {
        return this.results;
    }

    JButton getRunButton() {
        return this.testBut;
    }

    JPanel getResultsAndButtonPanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        JPanel jPanel2 = this.getResults().generatePanel();
        jPanel.add((Component)jPanel2, "Center");
        jPanel.add((Component)this.getRunButton(), "South");
        return jPanel;
    }

    private boolean askContinue() {
        return this.askContinue("Taking this action will wipe out your existing JUnit test file.  Ok to proceed?");
    }

    private boolean askContinue(String string) {
        int n = JOptionPane.showConfirmDialog(this.getParent(), string, "Possible data loss", 0, 2);
        return n == 0;
    }

}

