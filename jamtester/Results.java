/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  com.sun.javadoc.ClassDoc
 *  com.sun.javadoc.MethodDoc
 *  junit.framework.Test
 *  junit.framework.TestCase
 *  junit.framework.TestResult
 *  junit.framework.TestSuite
 */
package jamtester;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import jamtester.GradingToolAboutDialog;
import jamtester.JAMCellRenderer;
import jamtester.ResultsActionListener;
import jamtester.ResultsMouseAdapter;
import jamtester.StudentResult;
import jamtester.TestResult;
import jamtester.UntestedTestResult;
import jamtester.evalsupp.OtherGradePanel;
import jamtester.javatools.JavaParser;
import jamtester.studenttool.StudentFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import jplagwebstart.JPlagGUI;
import jplagwebstart.Program;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class Results
extends AbstractTableModel
implements ListModel,
Serializable {
    ArrayList dataListeners = new ArrayList();
    ArrayList students = new ArrayList();
    String name;
    String timeStamp;
    ArrayList testHeaders = new ArrayList();
    protected File testFile;
    private String directory;
    private int columnCount;
    private transient ArrayList tables = new ArrayList();
    private transient boolean notSerialized = true;
    private transient File[] classPath;
    private transient boolean running;
    public static boolean ma = false;
    protected ArrayList areas = new ArrayList();
    private ArrayList otherGradeHeaders;
    private JFrame otherGrader;
    private String[] cP;
    private String tF;
    private ArrayList defaultValues;
    private ArrayList otherGradePanels;

    public Results() {
        this.testHeaders.add("Success");
        this.testHeaders.add("Percentage");
        this.name = "";
        this.timeStamp = GregorianCalendar.getInstance().getTime().toString();
        this.fireTableDataChanged();
        this.setColumnCount(1);
        this.otherGradeHeaders = new ArrayList();
        this.defaultValues = new ArrayList();
        this.otherGradePanels = new ArrayList();
    }

    public ArrayList getDefaultValues() {
        return this.defaultValues;
    }

    public void setDefaultValues(ArrayList arrayList) {
        this.defaultValues = arrayList;
    }

    public String[] getCP() {
        return this.cP;
    }

    public void setCP(String[] arrstring) {
        this.cP = arrstring;
        this.classPath = new File[arrstring.length];
        for (int i = 0; i < arrstring.length; ++i) {
            this.classPath[i] = new File(this.cP[i]);
        }
    }

    public String getTF() {
        return this.tF;
    }

    public void setTF(String string) {
        this.tF = string;
        this.testFile = new File(string);
    }

    public void setOtherGradeHeaders(ArrayList arrayList) {
        this.otherGradeHeaders = arrayList;
    }

    public String getDirectory() {
        return this.directory;
    }

    public void setDirectory(String string) {
        this.directory = string;
    }

    public ArrayList getOtherGradeHeaders() {
        return this.otherGradeHeaders;
    }

    public void addOtherGradeHeader(String string, String string2) {
        this.otherGradeHeaders.add(string);
        this.defaultValues.add(string2);
        for (int i = 0; i < this.students.size(); ++i) {
            ((StudentResult)this.students.get(i)).addOtherGrade();
        }
    }

    public void setTestHeaders(ArrayList arrayList) {
        this.testHeaders = arrayList;
    }

    public ArrayList getTestHeaders() {
        return this.testHeaders;
    }

    public String getName() {
        return this.name;
    }

    public void setTimeStamp(String string) {
        this.timeStamp = string;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    File[] getClassPath() {
        return this.classPath;
    }

    File getTestFile() {
        return this.testFile;
    }

    boolean serialized() {
        return !this.notSerialized;
    }

    void setColumnCount(int n) {
        if (this.columnCount == n) {
            return;
        }
        for (int i = 0; i < this.students.size(); ++i) {
            ((StudentResult)this.students.get(i)).setNumColumns(n);
        }
        this.columnCount = n;
        this.fireTableStructureChanged();
    }

    public int getNumColumns() {
        return this.columnCount;
    }

    public void setNumColumns(int n) {
        this.setColumnCount(n);
    }

    public void setName(String string) {
        this.name = string;
    }

    public void empty() {
        this.students.clear();
        this.fireTableDataChanged();
    }

    public void clearAllTextAreas() {
        for (int i = 0; i < this.areas.size(); ++i) {
            ((JTextArea)this.areas.get(i)).setText("");
        }
    }

    public void setMethodNames(File file) {
        this.setMethodNames(file, false);
    }

    public void removeOtherGradeAt(int n) {
        this.otherGradeHeaders.remove(n);
        this.defaultValues.remove(n);
        for (int i = 0; i < this.students.size(); ++i) {
            ((StudentResult)this.students.get(i)).removeGradeAt(n);
        }
    }

    public void setMethodNames(File file, boolean bl) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Success");
        arrayList.add("Percentage");
        this.testFile = file;
        MethodDoc[] arrmethodDoc = null;
        try {
            try {
                arrmethodDoc = JavaParser.parseFile(file).getMethods();
                this.setName(arrmethodDoc[0].containingClass().name());
                if (bl) {
                    arrayList.add("Code Coverage");
                }
                for (int i = 0; i < arrmethodDoc.length; ++i) {
                    if (!arrmethodDoc[i].name().startsWith("test")) continue;
                    arrayList.add(arrmethodDoc[i].name());
                }
                Object var7_7 = null;
                if (!this.listsMatch(this.testHeaders, arrayList)) {
                    this.testHeaders = arrayList;
                    this.fireTableStructureChanged();
                }
            }
            catch (Exception var5_6) {
                arrayList.add("Failed to Compile");
                Object var7_8 = null;
                if (!this.listsMatch(this.testHeaders, arrayList)) {
                    this.testHeaders = arrayList;
                    this.fireTableStructureChanged();
                }
            }
        }
        catch (Throwable var6_10) {
            Object var7_9 = null;
            if (!this.listsMatch(this.testHeaders, arrayList)) {
                this.testHeaders = arrayList;
                this.fireTableStructureChanged();
            }
            throw var6_10;
        }
    }

    public void setMethodNames(TestSuite testSuite, boolean bl) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Success");
        arrayList.add("Percentage");
        Object var4_4 = null;
        try {
            this.setName(testSuite.getName());
            if (bl) {
                arrayList.add("Code Coverage");
            }
            for (int i = 0; i < testSuite.testCount(); ++i) {
                arrayList.add(((TestCase)testSuite.testAt(i)).getName());
            }
        }
        catch (Exception var5_6) {
            arrayList.add("Failed to Compile");
        }
        if (!this.listsMatch(this.testHeaders, arrayList)) {
            this.testHeaders = arrayList;
            this.fireTableStructureChanged();
        }
    }

    private boolean listsMatch(ArrayList arrayList, ArrayList arrayList2) {
        if (arrayList.size() != arrayList2.size()) {
            return false;
        }
        for (int i = 0; i < arrayList.size(); ++i) {
            if (arrayList.get(i).equals(arrayList2.get(i))) continue;
            return false;
        }
        return true;
    }

    public void add(StudentResult studentResult) {
        int n;
        studentResult.setNumColumns(this.columnCount);
        studentResult.setNumberOfOtherGrades(this.otherGradeHeaders.size());
        if (this.students.contains(studentResult)) {
            n = this.students.indexOf(studentResult);
            StudentResult studentResult2 = (StudentResult)this.students.get(n);
            this.students.remove(studentResult);
            studentResult.setOtherGrades(studentResult2.getOtherGrades());
            super.fireTableRowsDeleted(n, n);
            studentResult.setDirectory(studentResult2.getDir());
        }
        this.students.add(studentResult);
        Collections.sort(this.students);
        n = this.students.indexOf(studentResult);
        super.fireTableRowsInserted(n, n);
        this.notifyListeners(studentResult);
        for (int i = 0; i < this.otherGradePanels.size(); ++i) {
            ((OtherGradePanel)this.otherGradePanels.get(i)).load(this);
        }
    }

    public void removeAt(int n) {
        this.students.remove(n);
        super.fireTableRowsDeleted(n, n);
        for (int i = 0; i < this.otherGradePanels.size(); ++i) {
            ((OtherGradePanel)this.otherGradePanels.get(i)).load(this);
        }
    }

    public int getSize() {
        return this.students.size();
    }

    public Object getElementAt(int n) {
        return this.students.get(n);
    }

    public void addListDataListener(ListDataListener listDataListener) {
        if (!this.dataListeners.contains(listDataListener)) {
            this.dataListeners.add(listDataListener);
        }
    }

    public void removeListDataListener(ListDataListener listDataListener) {
        this.dataListeners.remove(listDataListener);
    }

    public String toCsv() {
        int n;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.name + "," + this.timeStamp + "\n");
        for (n = 0; n < this.columnCount; ++n) {
            stringBuffer.append("Field " + (n + 1) + ",");
        }
        for (n = 0; n < this.testHeaders.size(); ++n) {
            stringBuffer.append(this.testHeaders.get(n).toString() + ",");
        }
        for (n = 0; n < this.otherGradeHeaders.size(); ++n) {
            stringBuffer.append("\"" + this.otherGradeHeaders.get(n).toString() + "\"" + ",");
        }
        stringBuffer.append("Comments");
        stringBuffer.append('\n');
        for (n = 0; n < this.students.size(); ++n) {
            stringBuffer.append(((StudentResult)this.students.get(n)).toCsv());
            stringBuffer.append('\n');
        }
        return stringBuffer.toString();
    }

    public JTable generateTable() {
        if (this.tables == null) {
            this.tables = new ArrayList();
        }
        JTable jTable = new JTable(this);
        Class class_ = Object.class;
        jTable.setDefaultRenderer(class_, new JAMCellRenderer());
        jTable.setDefaultEditor(Object.class, null);
        jTable.setEnabled(true);
        jTable.setAutoscrolls(true);
        return jTable;
    }

    public void fireTableStructureChanged() {
        super.fireTableStructureChanged();
    }

    public void fireTableDataChanged() {
        super.fireTableDataChanged();
    }

    public JPanel generatePanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 1));
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("Help");
        jMenu.setMnemonic('H');
        JMenuItem jMenuItem = new JMenuItem("About");
        jMenuItem.setMnemonic('A');
        jMenuItem.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                GradingToolAboutDialog gradingToolAboutDialog = new GradingToolAboutDialog(null);
                gradingToolAboutDialog.show();
            }
        });
        jMenu.add(jMenuItem);
        jMenuBar.add(jMenu);
        JMenu jMenu2 = new JMenu("File");
        jMenu2.setMnemonic('F');
        JMenuItem jMenuItem2 = new JMenuItem("Save as JAM");
        jMenuItem2.setMnemonic('S');
        jMenuItem2.addActionListener(new ResultsActionListener(this, false));
        JMenuItem jMenuItem3 = new JMenuItem("Save as CSV");
        jMenuItem3.addActionListener(new ResultsActionListener(this, true));
        jMenuItem3.setMnemonic('C');
        jMenu2.add(jMenuItem2);
        jMenu2.add(jMenuItem3);
        jMenuBar.add((Component)jMenu2, 0);
        JScrollPane jScrollPane = null;
        JTable jTable = this.generateTable();
        JScrollPane jScrollPane2 = jScrollPane = new JScrollPane(jTable);
        this.tables.add(jScrollPane);
        jScrollPane.setVerticalScrollBarPolicy(22);
        JTextArea jTextArea = new JTextArea();
        this.areas.add(jTextArea);
        jTextArea.setEditable(false);
        jScrollPane = new JScrollPane(jTextArea);
        jScrollPane.setWheelScrollingEnabled(true);
        jScrollPane.setHorizontalScrollBarPolicy(32);
        jScrollPane.setVerticalScrollBarPolicy(22);
        JSplitPane jSplitPane = new JSplitPane(0, jScrollPane2, jScrollPane);
        if (!ma) {
            jTable.addMouseListener(new ResultsMouseAdapter(jTable, jTextArea, this));
        } else {
            jTable.addMouseListener(new StudentFrame.StudentToolResultsMouseAdapter(jTable, jTextArea, this));
        }
        jPanel.add(jSplitPane);
        jSplitPane.setPreferredSize(new Dimension(100, 200));
        jPanel.setPreferredSize(new Dimension(100, 200));
        jSplitPane.setResizeWeight(0.5);
        return jPanel;
    }

    public JFrame generateOutput() {
        JFrame jFrame = new JFrame("JAM*Tester Results for " + this.name);
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("Help");
        jMenu.setMnemonic('H');
        JMenuItem jMenuItem = new JMenuItem("About");
        jMenuItem.setMnemonic('A');
        jMenuItem.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                GradingToolAboutDialog gradingToolAboutDialog = new GradingToolAboutDialog(null);
            }
        });
        jMenu.add(jMenuItem);
        jMenuBar.add(jMenu);
        JMenu jMenu2 = new JMenu("File");
        JMenuItem jMenuItem2 = new JMenuItem("Detect new students");
        jMenuItem2.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                Results.this.addStudentsFromLocal();
            }
        });
        jMenu2.add(jMenuItem2);
        jMenu2.addSeparator();
        jMenu2.setMnemonic('F');
        JMenuItem jMenuItem3 = new JMenuItem("Save as JAM");
        jMenuItem3.setMnemonic('S');
        jMenuItem3.addActionListener(new ResultsActionListener(this, false));
        JMenuItem jMenuItem4 = new JMenuItem("Save as CSV");
        jMenuItem4.addActionListener(new ResultsActionListener(this, true));
        jMenuItem4.setMnemonic('C');
        jMenu2.add(jMenuItem3);
        jMenu2.add(jMenuItem4);
        jMenuBar.add((Component)jMenu2, 0);
        jFrame.setJMenuBar(jMenuBar);
        jFrame.setSize(800, 600);
        JTabbedPane jTabbedPane = new JTabbedPane();
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add((Component)this.generatePanel(), "Center");
        JButton jButton = new JButton("Check for plagiarism using JPlag");
        jButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                File file = Results.this.getRootDir();
                if (!(file != null && file.exists())) {
                    ((JButton)actionEvent.getSource()).setEnabled(false);
                }
                JPlagGUI jPlagGUI = Program.showGUI();
                if (file != null && file.exists()) {
                    jPlagGUI.setRootDir(Results.this.getRootDir());
                }
            }
        });
        jTabbedPane.add("Results", jPanel);
        OtherGradePanel otherGradePanel = new OtherGradePanel(this);
        jTabbedPane.add("Other Evaluation", otherGradePanel);
        jTabbedPane.setBackgroundAt(0, Color.YELLOW);
        jTabbedPane.setBackgroundAt(1, Color.BLUE);
        jTabbedPane.setForegroundAt(1, Color.WHITE);
        this.otherGradePanels.add(otherGradePanel);
        jFrame.getContentPane().setLayout(new GridLayout(1, 1));
        jFrame.getContentPane().add(jTabbedPane);
        return jFrame;
    }

    public File getRootDir() {
        if (this.students.size() == 0 || ((StudentResult)this.students.get(0)).getDir() == null) {
            return null;
        }
        return ((StudentResult)this.students.get(0)).getDir().getParentFile();
    }

    public JInternalFrame generateInternalFrame() {
        JInternalFrame jInternalFrame = new JInternalFrame("JAM*Tester Results for " + this.name);
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("Help");
        jMenu.setMnemonic('H');
        JMenuItem jMenuItem = new JMenuItem("About");
        jMenuItem.setMnemonic('A');
        jMenuItem.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                GradingToolAboutDialog gradingToolAboutDialog = new GradingToolAboutDialog(null);
            }
        });
        jMenu.add(jMenuItem);
        jMenuBar.add(jMenu);
        JMenu jMenu2 = new JMenu("File");
        jMenu2.setMnemonic('F');
        JMenuItem jMenuItem2 = new JMenuItem("Save as JAM");
        jMenuItem2.setMnemonic('S');
        jMenuItem2.addActionListener(new ResultsActionListener(this, false));
        JMenuItem jMenuItem3 = new JMenuItem("Save as CSV");
        jMenuItem3.addActionListener(new ResultsActionListener(this, true));
        jMenuItem3.setMnemonic('C');
        jMenu2.add(jMenuItem2);
        jMenu2.add(jMenuItem3);
        jMenuBar.add((Component)jMenu2, 0);
        jInternalFrame.setJMenuBar(jMenuBar);
        jInternalFrame.setSize(600, 300);
        jInternalFrame.getContentPane().setLayout(new GridLayout(1, 1));
        jInternalFrame.getContentPane().add(this.generatePanel());
        return jInternalFrame;
    }

    public ArrayList getStudents() {
        return this.students;
    }

    public void setStudents(ArrayList arrayList) {
        this.students = arrayList;
        this.setNumColumns(this.getNumColumns());
    }

    public int getRowCount() {
        return this.students.size();
    }

    public int getColumnCount() {
        return this.testHeaders.size() + this.columnCount;
    }

    public String getColumnName(int n) {
        if (n < this.columnCount) {
            return "Field " + (n + 1);
        }
        return this.testHeaders.get(n - this.columnCount).toString();
    }

    public boolean isCellEditable(int n, int n2) {
        return false;
    }

    public Object getValueAt(int n, int n2) {
        StudentResult studentResult = (StudentResult)this.students.get(n);
        TestResult[] arrtestResult = studentResult.getResults();
        if (n2 < this.columnCount) {
            return studentResult.column(n2);
        }
        if (n2 == this.columnCount) {
            return "" + studentResult.numSucceded() + " of " + studentResult.numTests();
        }
        if (n2 == this.columnCount + 1) {
            return "" + studentResult.percentage() + "%";
        }
        if (arrtestResult.length == 0) {
            junit.framework.TestResult testResult = new junit.framework.TestResult();
            testResult.addError(null, (Throwable)new Exception("Failed to compile"));
            return new TestResult(this.testHeaders.get(n2 - 3).toString(), testResult);
        }
        if (n2 - this.columnCount - 2 >= arrtestResult.length) {
            return null;
        }
        return arrtestResult[n2 - this.columnCount - 2];
    }

    public void notifyListeners(StudentResult studentResult) {
        final int n = this.students.indexOf(studentResult);
        super.fireTableRowsUpdated(n, n);
        for (int i = 0; i < this.tables.size(); ++i) {
            final JScrollPane jScrollPane = (JScrollPane)this.tables.get(i);
            SwingUtilities.invokeLater(new Runnable(){

                public void run() {
                    jScrollPane.getVerticalScrollBar().setValue((int)((double)jScrollPane.getVerticalScrollBar().getMaximum() * 1.0 * (double)n / (double)Results.this.students.size()));
                }
            });
            jScrollPane.invalidate();
            jScrollPane.repaint();
        }
    }

    public void notifyListeners(StudentResult studentResult, int n) {
        final int n2 = this.students.indexOf(studentResult);
        super.fireTableRowsUpdated(n2, n2);
        for (int i = 0; i < this.tables.size(); ++i) {
            final JScrollPane jScrollPane = (JScrollPane)this.tables.get(i);
            SwingUtilities.invokeLater(new Runnable(){

                public void run() {
                    jScrollPane.getVerticalScrollBar().setValue((int)((double)jScrollPane.getVerticalScrollBar().getMaximum() * 1.0 * (double)n2 / (double)Results.this.students.size()));
                }
            });
            jScrollPane.invalidate();
            jScrollPane.repaint();
        }
    }

    void setData(File file, File[] arrfile) {
        this.testFile = file;
        this.tF = file.getAbsolutePath();
        this.classPath = arrfile;
        this.cP = new String[arrfile.length];
        for (int i = 0; i < arrfile.length; ++i) {
            this.cP[i] = arrfile[i].getAbsolutePath();
        }
    }

    public void set(int n, StudentResult studentResult) {
        this.add(studentResult);
    }

    public void setRunning(boolean bl) {
        this.running = bl;
    }

    public boolean getRunning() {
        return this.isRunning();
    }

    boolean isRunning() {
        return this.running;
    }

    public void launchOtherGrader() {
        if (this.otherGrader == null) {
            this.otherGrader = new JFrame("Additional Evaluation Editor");
            this.otherGrader.getContentPane().setLayout(new GridLayout(1, 1));
            this.otherGrader.getContentPane().add(new OtherGradePanel(this));
            this.otherGrader.setSize(800, 600);
            this.otherGrader.setDefaultCloseOperation(1);
        }
        this.otherGrader.show();
    }

    public void save(File file) {
        try {
            System.err.println("Other: " + this.otherGradeHeaders);
            System.err.println("Students: " + this.students);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            XMLEncoder xMLEncoder = new XMLEncoder(fileOutputStream);
            xMLEncoder.setExceptionListener(new ExceptionListener(){

                public void exceptionThrown(Exception exception) {
                    System.err.println("XML Saving error:");
                    exception.printStackTrace(System.err);
                }
            });
            xMLEncoder.writeObject(this);
            xMLEncoder.close();
        }
        catch (Exception var2_3) {
            var2_3.printStackTrace(System.err);
        }
    }

    public static void save(File file, Results results) {
        results.save(file);
    }

    private void addStudentsFromLocal() {
        File file = new File(this.directory);
        File[] arrfile = file.listFiles(new FileFilter(){

            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        for (int i = 0; i < arrfile.length; ++i) {
            StudentResult studentResult = new StudentResult(arrfile[i].getName(), true, new ArrayList(), this);
            studentResult.setTheDir(arrfile[i].getAbsolutePath());
            for (int j = 0; j < this.testHeaders.size() - 2; ++j) {
                studentResult.addResult(new UntestedTestResult());
            }
            if (this.students.contains(studentResult)) continue;
            if (studentResult.getColumns().size() > this.columnCount) {
                this.setColumnCount(studentResult.getColumns().size());
            }
            this.add(studentResult);
        }
    }

    public static Results load(File file) {
        Results results = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            XMLDecoder xMLDecoder = new XMLDecoder(fileInputStream);
            results = (Results)xMLDecoder.readObject();
            xMLDecoder.close();
            results.addStudentsFromLocal();
        }
        catch (Exception var2_3) {
            // empty catch block
        }
        return results;
    }

}

