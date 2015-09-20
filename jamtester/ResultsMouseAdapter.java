/*
 * Decompiled with CFR 0_102.
 */
package jamtester;

import jamtester.CompileTestResult;
import jamtester.EditFrame;
import jamtester.FileEditor;
import jamtester.JAMTestFailure;
import jamtester.JavaUtilities;
import jamtester.Results;
import jamtester.ResultsMouseAdapter;
import jamtester.StudentResult;
import jamtester.TestResult;
import jamtester.UntestedTestResult;
import jamtester.coverage.CoverageInfo;
import jamtester.coverage.CoverageTestResult;
import jamtester.coverage.CoverageWindow;
import jamtester.studenttool.StudentToolTextArea;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextArea;

public class ResultsMouseAdapter
extends MouseAdapter {
    private JTable myTable;
    private JTextArea myText;
    private Results results;

    public ResultsMouseAdapter(JTable jTable, JTextArea jTextArea, Results results) {
        this.myTable = jTable;
        this.myText = jTextArea;
        this.results = results;
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        JPopupMenu jPopupMenu;
        Object object = this.myTable.getValueAt(this.myTable.rowAtPoint(mouseEvent.getPoint()), this.myTable.columnAtPoint(mouseEvent.getPoint()));
        if (object == null) {
            return;
        }
        if (!this.results.isRunning() && (mouseEvent.getButton() == 2 || mouseEvent.getButton() == 3 || mouseEvent.getClickCount() == 2 && object instanceof String)) {
            jPopupMenu = new JPopupMenu();
            JMenuItem jMenuItem = new JMenuItem("Re-test");
            final int n = this.myTable.rowAtPoint(mouseEvent.getPoint());
            jMenuItem.addActionListener(new ActionListener(){

                public void actionPerformed(ActionEvent actionEvent) {
                    Thread thread = new Thread(this){
                        private final /* synthetic */  this$1;

                        public void run() {
                            Results results = jamtester.GradingTool.testOneSansGui(ResultsMouseAdapter.access$100(.access$000(this.this$1)), (StudentResult)ResultsMouseAdapter.access$100(.access$000(this.this$1)).getElementAt(.access$200(this.this$1)));
                        }
                    };
                    thread.start();
                }

                static /* synthetic */ ResultsMouseAdapter access$000( var0) {
                    return var0.ResultsMouseAdapter.this;
                }

                static /* synthetic */ int access$200( var0) {
                    return var0.n;
                }
            });
            jPopupMenu.add(jMenuItem);
            JMenuItem jMenuItem2 = new JMenuItem("Edit all files in project");
            jMenuItem2.addActionListener(new ActionListener(){

                public void actionPerformed(ActionEvent actionEvent) {
                    FileEditor fileEditor = new FileEditor(((StudentResult)ResultsMouseAdapter.this.results.getElementAt(n)).getDir().getName(), JavaUtilities.getFilesToCompile(((StudentResult)ResultsMouseAdapter.this.results.getElementAt(n)).getDir()).toArray(new File[0]));
                    fileEditor.show();
                }
            });
            jPopupMenu.add(jMenuItem2);
            if (!((StudentResult)this.results.getElementAt(n)).getDir().exists()) {
                final int n2 = n;
                for (int i = 0; i < jPopupMenu.getComponentCount(); ++i) {
                    jPopupMenu.getComponentAtIndex(i).setEnabled(false);
                }
                jPopupMenu.add((Component)new JLabel("Cannot do anything else, appropriate folder no longer exists."), 0);
                JMenuItem jMenuItem3 = new JMenuItem("Remove from results");
                jMenuItem3.addActionListener(new ActionListener(){

                    public void actionPerformed(ActionEvent actionEvent) {
                        if (JOptionPane.showConfirmDialog((JComponent)actionEvent.getSource(), "Are you sure you want to remove this student?", "Confirm remove", 0, 3) == 0) {
                            ResultsMouseAdapter.this.results.removeAt(n2);
                        }
                    }
                });
                jPopupMenu.add((Component)jMenuItem3, 0);
            }
            jPopupMenu.show((Component)mouseEvent.getSource(), mouseEvent.getX(), mouseEvent.getY());
        }
        System.out.println(object);
        System.out.println(object.getClass());
        if (object instanceof CompileTestResult) {
            this.myText.setText(((CompileTestResult)object).getError());
        } else if (object instanceof CoverageTestResult) {
            jPopupMenu = (CoverageTestResult)object;
            this.myText.setText("Double-click to view coverage.\n\nCoverage:\t" + jPopupMenu + "\n\n" + jPopupMenu.mainInfo());
            if (!(this.results.isRunning() || mouseEvent.getClickCount() != 2)) {
                CoverageWindow coverageWindow = new CoverageWindow("JAM*Tester Code Coverage Viewer", jPopupMenu.coverageInfos());
                coverageWindow.show();
            }
        } else if (object instanceof UntestedTestResult) {
            this.myText.setText(((UntestedTestResult)object).getError());
        } else if (object instanceof TestResult) {
            if (!((TestResult)object).succeded()) {
                ((TestResult)object).exception().augment();
            }
            if (((TestResult)object).succeded()) {
                this.myText.setText("Success");
            } else {
                this.myText.setText(((TestResult)object).exception().getAugmentedTrace());
            }
        } else if (object instanceof JAMTestFailure) {
            ((JAMTestFailure)object).augment();
            this.myText.setText(((JAMTestFailure)object).getAugmentedTrace());
        } else {
            this.myText.setText(object.toString());
        }
        this.myText.setCaretPosition(0);
        if (mouseEvent.getClickCount() == 2 && object instanceof CompileTestResult) {
            jPopupMenu = (CompileTestResult)object;
            EditFrame editFrame = new EditFrame(jPopupMenu.getFile());
            StudentToolTextArea studentToolTextArea = editFrame.getTextArea();
            JPanel jPanel = new JPanel();
            studentToolTextArea.scrollTo(jPopupMenu.lineNumber() - 1, 0);
            studentToolTextArea.setCaretPosition(studentToolTextArea.getLineStartOffset(jPopupMenu.lineNumber() - 1));
            editFrame.show();
        }
    }

}

