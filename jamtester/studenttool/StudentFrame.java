/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  com.sun.javadoc.ClassDoc
 */
package jamtester.studenttool;

import com.sun.javadoc.ClassDoc;
import jamtester.EditFrame;
import jamtester.FileEditor;
import jamtester.GradingToolGui;
import jamtester.JAMProperties;
import jamtester.Results;
import jamtester.ResultsMouseAdapter;
import jamtester.StatusBar;
import jamtester.StudentResult;
import jamtester.jamupdate.JAMUpdate;
import jamtester.javatools.JavaParser;
import jamtester.studenttool.StudentFrame;
import jamtester.studenttool.StudentHelpDialog;
import jamtester.studenttool.StudentTabbedPane;
import jamtester.studenttool.StudentToolAboutDialog;
import jamtester.studenttool.StudentToolEditPanel;
import jamtester.studenttool.StudentToolPanel;
import jamtester.studenttool.StudentToolTextArea;
import jamtester.studenttool.Tab;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.metal.MetalIconFactory;

public class StudentFrame
extends JFrame {
    private static StudentTabbedPane jtp;
    public static StudentFrame mainFrame;
    static File directory;
    private JMenuBar mb;
    private JMenuItem about;
    private JMenuItem instructions;
    private JMenu help;
    private JMenu file;
    private JMenuItem openProject;
    private JMenuItem openNonProject;
    private JMenuItem openForTesting;
    private JMenuItem openAllFiles;
    private JMenuItem closeCurrentTab;
    private JMenuItem saveCurrent;
    private JMenuItem saveAll;
    private JMenuItem refreshCurrent;
    private JMenuItem exit;
    private boolean contClose;
    public static StatusBar sb;
    private static final int ok = 0;
    private static final int no = 1;
    private static final int cancel = 2;
    private static final int close = -1;

    public StudentFrame() {
        super("JAM*Tester Student Version v1.30.011306");
        Results.ma = true;
        try {
            this.setIconImage(ImageIO.read(new File("StudentVersion.jpg")));
        }
        catch (Exception var1_1) {
            // empty catch block
        }
        jtp = new StudentTabbedPane();
        this.getContentPane().add(jtp);
        this.mb = new JMenuBar();
        this.help = new JMenu("Help");
        this.about = new JMenuItem("About", 97);
        this.about.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                StudentFrame.this.about();
            }
        });
        this.help.add(this.about);
        this.instructions = new JMenuItem("Instructions", 105);
        this.instructions.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                new StudentHelpDialog(StudentFrame.this);
            }
        });
        this.help.add(this.instructions, 0);
        this.file = new JMenu("File");
        this.openProject = new JMenuItem("Open Project", 112);
        this.openProject.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                StudentFrame.this.openProject();
                StudentFrame.this.enableButs();
            }
        });
        this.openNonProject = new JMenuItem("Select Project Folder", 106);
        this.openNonProject.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                StudentFrame.this.openNonProject();
                StudentFrame.this.enableButs();
            }
        });
        this.file.add(this.openNonProject);
        this.file.addSeparator();
        this.openForTesting = new JMenuItem("Open file for testing", 111);
        this.openForTesting.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                StudentFrame.this.openForTesting();
                StudentFrame.this.enableButs();
            }
        });
        this.file.add(this.openForTesting);
        this.file.addSeparator();
        this.saveCurrent = new JMenuItem("Save active test file", 115);
        this.saveCurrent.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                String string = jtp.getTitleAt(jtp.getSelectedIndex());
                ((StudentToolPanel)jtp.getSelectedComponent()).saveTestFile();
                StudentFrame.showStatus(string + " saved successfully");
            }
        });
        this.file.add(this.saveCurrent);
        this.saveAll = new JMenuItem("Save all test files", 97);
        this.saveAll.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                for (int i = 0; i < jtp.getTabCount(); ++i) {
                    ((StudentToolPanel)jtp.getComponentAt(i)).saveTestFile();
                }
                StudentFrame.showStatus("All test files saved successfully");
            }
        });
        this.file.add(this.saveAll);
        this.setDefaultCloseOperation(0);
        this.closeCurrentTab = new JMenuItem("Close selected tab", 116);
        this.closeCurrentTab.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                jtp.removeCurrent();
                StudentFrame.this.enableButs();
            }
        });
        this.file.add(this.closeCurrentTab);
        this.file.addSeparator();
        this.refreshCurrent = new JMenuItem("Refresh Methods for selected tab now", 114);
        this.refreshCurrent.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                ((StudentToolPanel)jtp.getSelectedComponent()).refreshClass();
            }
        });
        this.openAllFiles = new JMenuItem("Open Source Files (Mini-IDE)", 97);
        this.openAllFiles.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                ArrayList arrayList = StudentFrame.getFilesToOpen(StudentFrame.directory);
                File[] arrfile = new File[arrayList.size()];
                for (int i = 0; i < arrayList.size(); ++i) {
                    arrfile[i] = (File)arrayList.get(i);
                }
                FileEditor fileEditor = new FileEditor("JAM*Tester Student Version Mini-IDE", arrfile);
                fileEditor.show();
            }
        });
        this.file.add(this.openAllFiles);
        this.help.add(JAMUpdate.getNewMenuItem(), 0);
        this.help.add(GradingToolGui.changeLog);
        this.mb.add(this.file);
        this.mb.add(this.help);
        this.setJMenuBar(this.mb);
        this.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent windowEvent) {
                StudentFrame.this.contClose = true;
                if (StudentFrame.directory != null) {
                    StudentFrame.this.saveProject();
                }
                if (StudentFrame.this.contClose) {
                    System.exit(0);
                }
            }
        });
        this.enableButs();
        this.setSize(800, 600);
        sb = new StatusBar();
        sb.addToFrame(this);
        mainFrame = this;
    }

    public static void showStatus(String string) {
        sb.display(string);
    }

    private void enableButs() {
        this.closeCurrentTab.setEnabled(jtp.getTabCount() > 0);
        this.refreshCurrent.setEnabled(jtp.getTabCount() > 0);
        this.openForTesting.setEnabled(directory != null);
        this.openAllFiles.setEnabled(directory != null);
        this.saveCurrent.setEnabled(directory != null && jtp.getTabCount() > 0);
        this.saveAll.setEnabled(directory != null && jtp.getTabCount() > 0);
    }

    private void openProject() {
        File[] arrfile;
        if (directory != null) {
            this.contClose = true;
            this.saveProject();
            if (!this.contClose) {
                return;
            }
        }
        if ((arrfile = StudentFrame.showOpenDialog("Open a project", false, false, false, null, new String[]{"jamtester.jamst"}, true)).length == 0) {
            return;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(arrfile[0]);
            XMLDecoder xMLDecoder = new XMLDecoder(fileInputStream);
            ArrayList arrayList = (ArrayList)xMLDecoder.readObject();
            directory = arrfile[0].getParentFile();
            JOptionPane.showMessageDialog(this, arrayList.toString());
            for (int i = 0; i < arrayList.size(); ++i) {
                Tab tab = (Tab)arrayList.get(i);
                tab.setStartFile(this.addAbsolute(tab.getStartFile()));
                tab.setTestFile(this.addAbsolute(tab.getTestFile()));
            }
            JOptionPane.showMessageDialog(this, arrayList.toString());
            jtp.setTabs(arrayList);
            this.reassignDirs();
            this.openForTesting.setEnabled(true);
        }
        catch (Exception var2_3) {
            var2_3.printStackTrace(System.err);
        }
    }

    private void reassignDirs() {
        EditFrame.setRootFolder(directory);
        for (int i = 0; i < jtp.getTabCount(); ++i) {
            ((StudentToolPanel)jtp.getComponentAt(i)).setParentDir(directory);
        }
    }

    private void openNonProject() {
        File[] arrfile;
        if (directory != null) {
            this.contClose = true;
            this.saveProject();
            if (!this.contClose) {
                return;
            }
        }
        if ((arrfile = StudentFrame.showOpenDialog("Open a non-project (Choose a directory)", false, true, false, null, new String[0], true, true)).length == 0) {
            return;
        }
        if (StudentFrame.containsJamST(arrfile[0])) {
            try {
                arrfile[0] = new File(arrfile[0], "jamtester.jamst");
                FileInputStream fileInputStream = new FileInputStream(arrfile[0]);
                XMLDecoder xMLDecoder = new XMLDecoder(fileInputStream);
                ArrayList arrayList = (ArrayList)xMLDecoder.readObject();
                directory = arrfile[0].getParentFile();
                for (int i = 0; i < arrayList.size(); ++i) {
                    Tab tab = (Tab)arrayList.get(i);
                    tab.setStartFile(this.addAbsolute(tab.getStartFile()));
                    tab.setTestFile(this.addAbsolute(tab.getTestFile()));
                }
                jtp.setTabs(arrayList);
                this.reassignDirs();
                this.openForTesting.setEnabled(true);
            }
            catch (Exception var2_3) {
                var2_3.printStackTrace(System.err);
            }
            this.alertOpened();
            return;
        }
        jtp.removeAll();
        directory = arrfile[0];
        this.saveProject();
        this.openForTesting.setEnabled(true);
        this.alertOpened();
    }

    private void alertOpened() {
        StudentFrame.showStatus(directory.getName() + " was successfully opened as a project");
    }

    private static boolean containsJamST(File file) {
        if (!file.isDirectory()) {
            return true;
        }
        File[] arrfile = file.listFiles(new FilenameFilter(){

            public boolean accept(File file, String string) {
                if (string == null) {
                    return false;
                }
                return string.toLowerCase().equals("jamtester.jamst");
            }
        });
        if (arrfile == null) {
            return false;
        }
        return arrfile.length > 0;
    }

    private void openForTesting() {
        StudentToolPanel studentToolPanel;
        File[] arrfile = StudentFrame.showOpenDialog("Open .java file", false, false, true, directory, new String[]{".java"}, true);
        if (arrfile.length == 0) {
            return;
        }
        String string = this.chooseFileName(arrfile[0]);
        System.out.println("Checking for file:  " + new File(arrfile[0].getParentFile(), new StringBuffer().append(string).append(".java").toString()) + " " + new File(arrfile[0].getParentFile(), new StringBuffer().append(string).append(".java").toString()).exists());
        if (new File(arrfile[0].getParentFile(), string + ".java").exists()) {
            studentToolPanel = new StudentToolPanel(arrfile[0], string, new File(arrfile[0].getParentFile(), string + ".java"));
            jtp.addTab(string, studentToolPanel);
        } else {
            studentToolPanel = new StudentToolPanel(arrfile[0], string);
            jtp.addTab(string, studentToolPanel);
        }
        jtp.setSelectedIndex(jtp.getTabCount() - 1);
        studentToolPanel.getSP().setLastDividerLocation(studentToolPanel.getSP().getHeight() / 2);
        this.reassignDirs();
    }

    private String chooseFileName(File file) {
        String string = file.getName();
        string = string.replaceAll(".java", "");
        string = string + "Test";
        int n = 0;
        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i = 0; i < jtp.getTabCount(); ++i) {
            arrayList.add(jtp.getTitleAt(i));
        }
        if (arrayList.contains(string)) {
            ++n;
        }
        while (arrayList.contains(string + n)) {
            ++n;
        }
        if (n > 0) {
            string = string + n;
        }
        return string;
    }

    private void about() {
        StudentToolAboutDialog studentToolAboutDialog = new StudentToolAboutDialog(this);
    }

    private String removeAbsolute(String string) {
        if (string.startsWith(directory.getAbsolutePath())) {
            return string.substring(directory.getAbsolutePath().length(), string.length());
        }
        return string;
    }

    private String addAbsolute(String string) {
        return directory.getAbsolutePath() + string;
    }

    private void saveProject() {
        try {
            Object object;
            for (int i = 0; i < jtp.getTabCount(); ++i) {
                object = (StudentToolPanel)jtp.getComponentAt(i);
                if (!object.getEditor().getTextArea().isDirty()) continue;
                int n = this.askToSave(object.getTestFileName());
                if (n == 2) {
                    this.contClose = false;
                    return;
                }
                if (n != 0) continue;
                object.saveTestFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(new File(directory, "jamtester.jamst"));
            object = new XMLEncoder(fileOutputStream);
            ArrayList arrayList = new ArrayList(jtp.getTabs());
            for (int j = 0; j < arrayList.size(); ++j) {
                Tab tab = (Tab)arrayList.get(j);
                System.err.println("TAB: " + j + " Test File: " + tab.getTestFile());
                tab.setStartFile(this.removeAbsolute(tab.getStartFile()));
                tab.setTestFile(this.removeAbsolute(tab.getTestFile()));
            }
            object.writeObject(arrayList);
            object.close();
        }
        catch (Exception var1_3) {
            // empty catch block
        }
    }

    private int askToSave(String string) {
        int n = JOptionPane.showConfirmDialog(this.getParent(), string + " has been changed but not saved.  Would you like to save?", "Save?", 1, 2);
        return n;
    }

    public static File[] showOpenDialog(String string, boolean bl, boolean bl2, boolean bl3, File file, String[] arrstring, boolean bl4) {
        return StudentFrame.showOpenDialog(string, bl, bl2, bl3, file, arrstring, bl4, false);
    }

    public static File[] showOpenDialog(String string, boolean bl, boolean bl2, final boolean bl3, final File file, final String[] arrstring, boolean bl4, boolean bl5) {
        JFileChooser jFileChooser = new JFileChooser();
        if (bl5) {
            jFileChooser = new JFileChooser(){

                public boolean accept(File file) {
                    return file.isDirectory();
                }
            };
            jFileChooser.setFileView(new StudentProjectFileView());
            jFileChooser.setFileSelectionMode(2);
        }
        JAMProperties jAMProperties = JAMProperties.loadProperties();
        String string2 = jAMProperties.getLastFolder();
        File file2 = null;
        if (string2 != null) {
            file2 = new File(string2);
        }
        if (!(file2 == null || file2.isDirectory())) {
            file2 = file2.getParentFile();
        }
        if (string2 != null && !string2.equals("") && file2 != null && file2.exists()) {
            jFileChooser.setCurrentDirectory(file2);
        }
        if (bl2 && !bl5) {
            jFileChooser.setFileSelectionMode(1);
        }
        int n = 0;
        while (n < arrstring.length) {
            final int n2 = n++;
            FileFilter fileFilter = new FileFilter(){

                public boolean accept(File file2) {
                    if (file2.isDirectory()) {
                        return true;
                    }
                    return (!bl3 || file2.toString().startsWith(file.toString())) && file2.toString().toLowerCase().endsWith(arrstring[n2]);
                }

                public String getDescription() {
                    return arrstring[n2];
                }
            };
            jFileChooser.addChoosableFileFilter(fileFilter);
        }
        jFileChooser.setName(string);
        jFileChooser.setMultiSelectionEnabled(bl);
        if (bl3) {
            jFileChooser.setCurrentDirectory(file);
        }
        n = jFileChooser.showOpenDialog(null);
        if (!(bl4 || n != 1)) {
            System.exit(0);
        }
        if (n == 1) {
            return new File[0];
        }
        if (bl) {
            return jFileChooser.getSelectedFiles();
        }
        File[] arrfile = new File[]{jFileChooser.getSelectedFile()};
        jAMProperties = JAMProperties.loadProperties();
        jAMProperties.setLastFolder(arrfile[0].getAbsolutePath());
        if (!bl3) {
            jAMProperties.save();
        }
        return arrfile;
    }

    public static File[] getClassFiles(File file) {
        ArrayList<File> arrayList = new ArrayList<File>();
        File file2 = new File(file, "+libs");
        if (file2.exists()) {
            StudentFrame.getClassFiles(file2, arrayList);
        }
        arrayList.add(new File("junit.jar"));
        arrayList.add(new File("clover.jar"));
        File[] arrfile = new File[arrayList.size()];
        for (int i = 0; i < arrfile.length; ++i) {
            arrfile[i] = (File)arrayList.get(i);
        }
        return arrfile;
    }

    private static void getClassFiles(File file, ArrayList arrayList) {
        File[] arrfile = file.listFiles();
        for (int i = 0; i < arrfile.length; ++i) {
            if (arrfile[i].isDirectory()) {
                StudentFrame.getClassFiles(arrfile[i], arrayList);
                continue;
            }
            if (arrfile[i].getName().toLowerCase().endsWith(".class") && !arrayList.contains(arrfile[i].getParentFile())) {
                arrayList.add(arrfile[i].getParentFile());
            }
            if (!arrfile[i].getName().toLowerCase().endsWith(".jar")) continue;
            arrayList.add(arrfile[i]);
        }
    }

    public static ArrayList getFilesToOpen(File file) {
        ArrayList<File> arrayList = new ArrayList<File>();
        File[] arrfile = file.listFiles(new FilenameFilter(){

            public boolean accept(File file, String string) {
                if (string.toLowerCase().endsWith(".java") && StudentFrame.isJUnitTestCase(new File(file, string))) {
                    return false;
                }
                return string.toLowerCase().endsWith(".java") || new File(file, string).isDirectory();
            }
        });
        for (int i = 0; i < arrfile.length; ++i) {
            if (!arrfile[i].isDirectory()) {
                arrayList.add(arrfile[i]);
                continue;
            }
            ArrayList arrayList2 = StudentFrame.getFilesToOpen(arrfile[i]);
            for (int j = 0; j < arrayList2.size(); ++j) {
                arrayList.add((File)arrayList2.get(j));
            }
        }
        return arrayList;
    }

    public static boolean isJUnitTestCase(File file) {
        try {
            return StudentFrame.isJUnitTestCase(JavaParser.parseFile(file).getClassDoc());
        }
        catch (Exception var1_1) {
            return false;
        }
    }

    private static boolean isJUnitTestCase(ClassDoc classDoc) {
        if (classDoc == null) {
            return false;
        }
        if (classDoc.qualifiedTypeName().equals("junit.framework.TestCase")) {
            return true;
        }
        return StudentFrame.isJUnitTestCase(classDoc.superclass());
    }

    private static class StudentProjectFileView
    extends FileView {
        private StudentProjectFileView() {
        }

        public String getDescription(File file) {
            if (StudentFrame.containsJamST(file)) {
                return "JAM*Tester Student Version Project";
            }
            return null;
        }

        public Icon getIcon(File file) {
            if (StudentFrame.containsJamST(file)) {
                return new MetalIconFactory.FolderIcon16();
            }
            return null;
        }

        public String getName(File file) {
            return null;
        }

        public String getTypeDescription(File file) {
            if (StudentFrame.containsJamST(file)) {
                return "JAM*Tester Student Version Project";
            }
            return null;
        }

        public Boolean isTraversable(File file) {
            if (StudentFrame.containsJamST(file)) {
                return new Boolean(false);
            }
            return null;
        }
    }

    public static class StudentToolResultsMouseAdapter
    extends MouseAdapter {
        private JTable myTable;
        private JTextArea myText;
        private Results results;

        public StudentToolResultsMouseAdapter(JTable jTable, JTextArea jTextArea, Results results) {
            this.myTable = jTable;
            this.myText = jTextArea;
            this.results = results;
        }

        public void mouseClicked(MouseEvent mouseEvent) {
            Object object = this.myTable.getValueAt(this.myTable.rowAtPoint(mouseEvent.getPoint()), this.myTable.columnAtPoint(mouseEvent.getPoint()));
            if (object == null) {
                return;
            }
            if (!this.results.getRunning() && (mouseEvent.getButton() == 2 || mouseEvent.getButton() == 3 || mouseEvent.getClickCount() == 2 && object instanceof String)) {
                JPopupMenu jPopupMenu = new JPopupMenu();
                JMenuItem jMenuItem = new JMenuItem("Re-test");
                int n = this.myTable.rowAtPoint(mouseEvent.getPoint());
                jMenuItem.addActionListener(new ActionListener(this){
                    private final /* synthetic */ StudentToolResultsMouseAdapter this$0;

                    public void actionPerformed(ActionEvent actionEvent) {
                        java.lang.Thread thread = new java.lang.Thread(this){
                            private final /* synthetic */  this$1;

                            public void run() {
                                StudentTabbedPane studentTabbedPane = StudentFrame.access$500();
                                ((StudentToolPanel)studentTabbedPane.getSelectedComponent()).getLeft().getRunButton().doClick();
                            }
                        };
                        thread.start();
                    }
                });
                jPopupMenu.add(jMenuItem);
                jMenuItem = new JMenuItem("Open Source Files (Mini-IDE)");
                jMenuItem.addActionListener(new ActionListener(this){
                    private final /* synthetic */ StudentToolResultsMouseAdapter this$0;

                    public void actionPerformed(ActionEvent actionEvent) {
                        ArrayList arrayList = StudentFrame.getFilesToOpen(StudentFrame.directory);
                        File[] arrfile = new File[arrayList.size()];
                        for (int i = 0; i < arrayList.size(); ++i) {
                            arrfile[i] = (File)arrayList.get(i);
                        }
                        FileEditor fileEditor = new FileEditor("JAM*Tester Student Version Mini-IDE", arrfile);
                        fileEditor.show();
                    }
                });
                jPopupMenu.add(jMenuItem);
                if (((StudentResult)this.results.getElementAt(n)).getDir() == null) {
                    for (int i = 0; i < jPopupMenu.getComponentCount(); ++i) {
                        jPopupMenu.getComponentAtIndex(i).setEnabled(false);
                    }
                    jPopupMenu.add((Component)new JLabel("Cannot do anything, was loaded from saved file."), 0);
                }
                jPopupMenu.show((Component)mouseEvent.getSource(), mouseEvent.getX(), mouseEvent.getY());
                return;
            }
            ResultsMouseAdapter resultsMouseAdapter = new ResultsMouseAdapter(this.myTable, this.myText, this.results);
            resultsMouseAdapter.mouseClicked(mouseEvent);
        }
    }

}

