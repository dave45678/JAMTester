/*
 * Decompiled with CFR 0_102.
 */
package jamtester;

import jamtester.FancyKillButton;
import jamtester.GradingTool;
import jamtester.GradingToolAboutDialog;
import jamtester.GradingToolGui;
import jamtester.JAMProperties;
import jamtester.Results;
import jamtester.StatusBar;
import jamtester.jamupdate.ChangeLog;
import jamtester.jamupdate.JAMUpdate;
import jamtester.jdk.JDKDetect;
import jamtester.studenttool.StudentFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import jplagwebstart.JPlagGUI;
import jplagwebstart.Program;
import org.jext.Jext;

public class GradingToolGui
extends JFrame {
    public static GradingToolGui theGradingToolGui;
    public static PrintStream out;
    public static PrintStream err;
    public static final FileFilter jamFF;
    public static final FileFilter csvFF;
    JPanel pnPanel0;
    JPanel topPanel;
    JPanel bottomPanel;
    JProgressBar pbProgressBar0;
    JTextField tfFolder;
    JLabel lbLabel0;
    JButton btFolderBrowse;
    JLabel lbLabel2;
    JTextField tfTestFile;
    JButton btTestBrowse;
    JTextArea tbIncludeTable;
    JTextField tfSaveLocation;
    JButton btSaveBrowse;
    JButton btBrowseInclude;
    JButton btBut6;
    JButton btKill;
    JMenu fileMenu;
    JMenuItem openItem;
    JMenu helpMenu;
    private JMenu optionsMenu;
    private StatusBar sb;
    public static JCheckBoxMenuItem outputOn;
    JMenuItem aboutMenuItem;
    JMenuItem directionsMenuItem;
    private ArrayList classPaths = new ArrayList();
    private boolean isSansGui;
    private Results current;
    public static JMenuItem changeLog;
    private JFrame results;

    public static void main(String[] arrstring) {
        try {
            out = new PrintStream(new FileOutputStream("jamstdout.stdout", false));
            System.setOut(out);
            err = new PrintStream(new FileOutputStream("jamstderr.sterr", false));
            System.setErr(err);
        }
        catch (Exception var1_1) {
            // empty catch block
        }
        System.out.println("-----------------------------------");
        System.out.println(GregorianCalendar.getInstance().getTime());
        System.out.println("User:  " + System.getProperty("user.name"));
        System.out.println("-----------------------------------");
        System.err.println("-----------------------------------");
        System.err.println(GregorianCalendar.getInstance().getTime());
        System.err.println("User:  " + System.getProperty("user.name"));
        System.err.println("-----------------------------------");
        System.out.println("JAVA VERSION: " + System.getProperty("java.vm.version"));
        Jext.init();
        Runtime.getRuntime().traceInstructions(true);
        Runtime.getRuntime().traceMethodCalls(true);
        GradingToolGui.handleProperties();
        if (arrstring.length == 0) {
            arrstring = GradingToolGui.doChooseVersion();
        }
        JAMProperties.setVersion(arrstring[0].toLowerCase());
        if (arrstring[0].toLowerCase().equals("student")) {
            JDKDetect.loadJDK();
            StudentFrame studentFrame = new StudentFrame();
            studentFrame.show();
        } else if (arrstring[0].toLowerCase().equals("jdk select")) {
            JDKDetect.selectNewJDK();
            System.exit(0);
        } else {
            JDKDetect.loadJDK();
            theGradingToolGui = new GradingToolGui();
        }
        JAMUpdate.doBackgroundUpdateIfNecessary();
    }

    private static String[] doChooseVersion() {
        int n;
        Object[] arrobject = new String[]{"Teacher", "Student", "JDK Select"};
        if (!JDKDetect.needsToHaveJDK()) {
            arrobject = new String[]{"Teacher", "Student"};
        }
        if ((n = JOptionPane.showOptionDialog(null, "Please choose which version of JAM*Tester you would like to use.", "Which version?", 0, 3, null, arrobject, JAMProperties.loadProperties().getLastVersionRun())) == -1) {
            System.exit(0);
        }
        JAMProperties jAMProperties = JAMProperties.loadProperties();
        jAMProperties.setLastVersionRun((String)arrobject[n]);
        jAMProperties.save();
        return new String[]{arrobject[n]};
    }

    private static boolean needsToLoadJDK() {
        try {
            Class.forName("com.sun.tools.java.Main");
            return false;
        }
        catch (Exception var0) {
            return true;
        }
    }

    public static void handleProperties() {
        JAMProperties jAMProperties = JAMProperties.loadProperties();
        if (jAMProperties.getFirstRun()) {
            new ChangeLog();
        }
        jAMProperties.setFirstRun(false);
        jAMProperties.save();
    }

    private void init() {
        this.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent windowEvent) {
                Results results = GradingToolGui.this.current;
                if (results == null) {
                    return;
                }
                if (results.isRunning()) {
                    while (results.isRunning()) {
                        GradingTool.killCurrent();
                    }
                }
            }
        });
    }

    void setCurrent(Results results) {
        this.current = results;
    }

    public void setAllEnabled(boolean bl) {
        if (this.btBrowseInclude != null) {
            this.btBrowseInclude.setEnabled(bl);
        }
        if (this.btBut6 != null) {
            this.btBut6.setEnabled(bl);
        }
        if (this.btFolderBrowse != null) {
            this.btFolderBrowse.setEnabled(bl);
        }
        if (this.btSaveBrowse != null) {
            this.btSaveBrowse.setEnabled(bl);
        }
        if (this.btTestBrowse != null) {
            this.btTestBrowse.setEnabled(bl);
        }
        if (this.tbIncludeTable != null) {
            this.tbIncludeTable.setEnabled(bl);
        }
        if (this.btKill != null) {
            this.btKill.setEnabled(!bl);
        }
    }

    public void setMax(int n) {
        this.pbProgressBar0.setMaximum(n);
        this.pbProgressBar0.setMinimum(0);
    }

    public void showMessage(String string) {
        this.sb.display(string);
    }

    public void setCurrent(String string) {
        this.pbProgressBar0.setString(string);
    }

    public void setCompleted(int n) {
        this.pbProgressBar0.setValue(n);
    }

    public void increment() {
        this.pbProgressBar0.setValue(this.pbProgressBar0.getValue() + 1);
    }

    public void notifyComplete() {
        this.setAllEnabled(true);
        this.pbProgressBar0.setString("Done");
        if (this.isSansGui) {
            this.setVisible(false);
        }
    }

    public void showAbout() {
        new GradingToolAboutDialog(this);
    }

    private File showSaveDialog() {
        JFileChooser jFileChooser = new JFileChooser();
        FileFilter fileFilter = new FileFilter(){

            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toLowerCase().endsWith(".csv");
            }

            public String getDescription() {
                return "*.csv (Comma Separated Values) files";
            }
        };
        jFileChooser.setFileFilter(fileFilter);
        jFileChooser.showSaveDialog(this);
        File file = jFileChooser.getSelectedFile();
        if (file.toString().toLowerCase().endsWith(".csv")) {
            return file;
        }
        return new File(file.toString() + ".csv");
    }

    public static File showSaveDialog(FileFilter fileFilter, String string) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(fileFilter);
        jFileChooser.showSaveDialog(null);
        File file = jFileChooser.getSelectedFile();
        if (file.toString().toLowerCase().endsWith(string)) {
            return file;
        }
        return new File(file.toString() + string);
    }

    public GradingToolGui(boolean bl, boolean bl2) {
        this(bl);
        if (bl2) {
            this.getContentPane().removeAll();
            this.getContentPane().setLayout(new BorderLayout());
            ActionListener[] arractionListener = this.btKill.getActionListeners();
            this.btKill = new FancyKillButton();
            this.getContentPane().add((Component)this.btKill, "Center");
            this.getContentPane().add((Component)this.pbProgressBar0, "South");
            this.setSize(this.btKill.getWidth(), this.btKill.getHeight() + this.pbProgressBar0.getHeight());
            for (int i = 0; i < arractionListener.length; ++i) {
                this.btKill.addActionListener(arractionListener[i]);
            }
            GradingToolAboutDialog.centerWindow(this);
        }
    }

    public GradingToolGui(boolean bl) {
        super("JAM*Tester JUnit Auto-Matic Tester v1.30.011306");
        this.isSansGui = true;
        theGradingToolGui = this;
        this.getContentPane().setLayout(new GridLayout(2, 1));
        this.btKill = new JButton("Kill Current Test");
        this.btKill.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                GradingTool.killCurrent();
            }
        });
        this.getContentPane().add(this.btKill);
        this.setSize(400, 50);
        this.pbProgressBar0 = new JProgressBar();
        this.pbProgressBar0.setStringPainted(true);
        this.pbProgressBar0.setString("");
        this.pbProgressBar0.setValue(0);
        this.pbProgressBar0.setMinimum(0);
        this.pbProgressBar0.setMaximum(10);
        this.getContentPane().add(this.pbProgressBar0);
        outputOn = new JCheckBoxMenuItem();
        this.btKill.setBackground(Color.red.brighter());
        this.btKill.setForeground(Color.black);
        this.setSize(250, 75);
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.setLocation((int)graphicsEnvironment.getCenterPoint().getX() - this.getWidth() / 2, (int)graphicsEnvironment.getCenterPoint().getY() - this.getHeight() / 2);
        this.setResizable(false);
        this.init();
    }

    public GradingToolGui(JFrame jFrame) {
        this(true);
        this.results = jFrame;
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.setLocation((int)graphicsEnvironment.getCenterPoint().getX() - this.getWidth() / 2, (int)graphicsEnvironment.getCenterPoint().getY() - this.getHeight() + jFrame.getHeight() / 2);
        this.btKill.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
            }
        });
    }

    private JProgressBar getProgressBar() {
        return this.pbProgressBar0;
    }

    private JButton getKillButton() {
        return this.btKill;
    }

    public GradingToolGui() {
        super("JAM*Tester JUnit Auto-Matic Tester v1.30.011306");
        try {
            this.setIconImage(ImageIO.read(new File("jamt.jpg")));
        }
        catch (Exception var1_1) {
            // empty catch block
        }
        this.isSansGui = false;
        theGradingToolGui = this;
        this.pnPanel0 = new JPanel();
        this.pnPanel0.setLayout(new BorderLayout());
        this.topPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(3, 2);
        gridLayout.setVgap(10);
        this.topPanel.setLayout(gridLayout);
        this.bottomPanel = new JPanel();
        this.pnPanel0.add((Component)this.topPanel, "North");
        this.pnPanel0.add((Component)this.bottomPanel, "South");
        TitledBorder titledBorder = new TitledBorder(null, "A Grading Tool for Teachers", 2, 0, new Font("Dialog", 1, 12));
        titledBorder.setTitleColor(new Color(204, 0, 204));
        this.pnPanel0.setBorder(titledBorder);
        this.pbProgressBar0 = new JProgressBar();
        this.pbProgressBar0.setStringPainted(true);
        this.pbProgressBar0.setString("");
        this.pbProgressBar0.setValue(0);
        this.pbProgressBar0.setMinimum(0);
        this.pbProgressBar0.setMaximum(10);
        this.tfFolder = new JTextField();
        this.tfTestFile = new JTextField();
        this.lbLabel0 = new JLabel("");
        this.topPanel.add(this.lbLabel0);
        this.btFolderBrowse = new JButton("select folder of student folders...");
        this.btFolderBrowse.setForeground(Color.blue);
        this.btFolderBrowse.setToolTipText("select folder of student folders");
        this.topPanel.add(this.btFolderBrowse);
        this.btFolderBrowse.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                GradingToolGui.this.tfFolder.setText(GradingTool.showOpenDialog("Choose folder to go through", false, true, null, true)[0].toString());
                String string = GradingToolGui.this.tfFolder.getText();
                if (GradingToolGui.this.tfFolder.getText().length() > 50) {
                    string = "<snip>..." + GradingToolGui.this.tfFolder.getText().substring(GradingToolGui.this.tfFolder.getText().length() - 50);
                }
                GradingToolGui.this.btFolderBrowse.setText(string);
            }
        });
        this.lbLabel2 = new JLabel("");
        this.topPanel.add(this.lbLabel2);
        this.btTestBrowse = new JButton("select JUnit test class(.java)...");
        this.btTestBrowse.setForeground(Color.blue);
        this.btTestBrowse.setToolTipText("select JUnit test class(.java)");
        this.topPanel.add(this.btTestBrowse);
        this.btTestBrowse.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                GradingToolGui.this.tfTestFile.setText(GradingTool.showOpenDialog("Choose test file", false, false, GradingTool.javaFiles, true)[0].toString());
                String string = GradingToolGui.this.tfTestFile.getText();
                if (GradingToolGui.this.tfTestFile.getText().length() > 50) {
                    string = "<snip>..." + GradingToolGui.this.tfTestFile.getText().substring(GradingToolGui.this.tfTestFile.getText().length() - 50);
                }
                GradingToolGui.this.btTestBrowse.setText(string);
            }
        });
        this.tbIncludeTable = new JTextArea("Listed here are the additional class/.jar files \nyou are including to make your project compile/run.\n\nIncluded so far: <none>");
        this.tbIncludeTable.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(this.tbIncludeTable);
        this.topPanel.add(jScrollPane);
        this.btBrowseInclude = new JButton("Add .class/.jar files");
        this.btBrowseInclude.setToolTipText("Add .class/.jar files");
        this.topPanel.add(this.btBrowseInclude);
        this.btBrowseInclude.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                File[] arrfile = GradingTool.showOpenDialog("Add files to include", true, false, GradingTool.classFiles, true);
                for (int i = 0; i < arrfile.length; ++i) {
                    if (GradingToolGui.this.classPaths.contains(arrfile[i])) continue;
                    GradingToolGui.this.classPaths.add(arrfile[i]);
                }
                String string = "";
                for (int j = 0; j < GradingToolGui.this.classPaths.size(); ++j) {
                    string = string + "\n" + GradingToolGui.this.classPaths.get(j);
                }
                GradingToolGui.this.tbIncludeTable.replaceRange("", 0, GradingToolGui.this.tbIncludeTable.getText().length());
                GradingToolGui.this.tbIncludeTable.append(string);
            }
        });
        this.btBut6 = new JButton("Grade All Labs");
        JButton jButton = new JButton("Check for plagiarism using JPlag");
        jButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                File file = new File(GradingToolGui.this.tfFolder.getText());
                if (!(file != null && file.exists())) {
                    ((JButton)actionEvent.getSource()).setEnabled(false);
                }
                JPlagGUI jPlagGUI = Program.showGUI();
                jPlagGUI.addWindowListener(new WindowAdapter(this, actionEvent){
                    private final /* synthetic */ ActionEvent val$e;
                    private final /* synthetic */  this$1;

                    public void windowClosing(WindowEvent windowEvent) {
                        ((JButton)this.val$e.getSource()).setEnabled(true);
                    }
                });
                if (!jPlagGUI.isVisible()) {
                    ((JButton)actionEvent.getSource()).setEnabled(true);
                }
                if (file != null && file.exists()) {
                    jPlagGUI.setRootDir(file);
                }
            }
        });
        this.bottomPanel.add(this.btBut6);
        this.bottomPanel.add(jButton);
        this.btBut6.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                int n;
                try {
                    new File(GradingToolGui.this.tfFolder.getText());
                    new File(GradingToolGui.this.tfTestFile.getText());
                    if (GradingToolGui.this.tfFolder.getText().length() == 0 || GradingToolGui.this.tfTestFile.getText().length() == 0) {
                        throw new Exception("no go");
                    }
                }
                catch (Exception var2_2) {
                    return;
                }
                ArrayList<File> arrayList = new ArrayList<File>();
                for (n = 0; n < GradingToolGui.this.classPaths.size(); ++n) {
                    File file = (File)GradingToolGui.this.classPaths.get(n);
                    if (file.getName().toLowerCase().endsWith(".class")) {
                        file = file.getParentFile();
                    }
                    if (arrayList.contains(file)) continue;
                    arrayList.add(file);
                }
                GradingTool.classpaths = new File[arrayList.size() + 1];
                for (n = 0; n < arrayList.size(); ++n) {
                    GradingTool.classpaths[n] = (File)arrayList.get(n);
                }
                GradingTool.classpaths[GradingTool.classpaths.length - 1] = new File("junit.jar");
                new Thread(this){
                    private final /* synthetic */  this$1;

                    public void run() {
                        Results results = new Results();
                        JFrame jFrame = results.generateOutput();
                        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
                        jFrame.setLocation((int)graphicsEnvironment.getCenterPoint().getX() - jFrame.getWidth() / 2, (int)graphicsEnvironment.getCenterPoint().getY() - jFrame.getHeight() / 2);
                        GradingToolGui gradingToolGui = new GradingToolGui(jFrame);
                        JProgressBar jProgressBar = .access$200(()this.this$1).pbProgressBar0;
                        JButton jButton = .access$200(()this.this$1).btKill;
                        .access$200(()this.this$1).btKill = GradingToolGui.access$300(gradingToolGui);
                        .access$200(()this.this$1).pbProgressBar0 = GradingToolGui.access$400(gradingToolGui);
                        .access$200(this.this$1).setAllEnabled(false);
                        jFrame.show();
                        gradingToolGui.show();
                        results = GradingTool.goThruDirsInDir(new File(.access$200(()this.this$1).tfFolder.getText()), new File(.access$200(()this.this$1).tfTestFile.getText()), null, gradingToolGui, results);
                        jFrame.setTitle("JAM*Tester Results for " + results.getName());
                        .access$200(this.this$1).setAllEnabled(true);
                        .access$200(()this.this$1).btKill = jButton;
                        .access$200(()this.this$1).pbProgressBar0 = jProgressBar;
                    }
                }.start();
            }

            static /* synthetic */ GradingToolGui access$200( var0) {
                return var0.GradingToolGui.this;
            }
        });
        this.helpMenu = new JMenu("Help");
        this.helpMenu.setMnemonic(72);
        this.directionsMenuItem = new JMenuItem("Directions");
        this.directionsMenuItem.setMnemonic(68);
        this.directionsMenuItem.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                String string = "Before running this tool, you should have a single folder\nwithin which are the student folders(one per student). Each\nstudent's folder contains the .java file being graded. If there are\nother .java files for the lab that the student edited/wrote that are\nbeing used by the .java file being graded, the student should have\nthose files in the folder as well. \n\nThe student folders should have descriptive names and each field\nwithin the folder name should be separated by a space. Each field\nwill become a column in the spreadsheet created.\n\nOften times the teacher will provide completed java files as part \nof the lab - those should not be in the student folders - those will\nbe included by the teacher when running this tool (see #3 below).\n\n1.\tuse the first button to select the folder of student folders\n2.\tuse the second button to select the JUnit test file\n3.\t(optional) use the third button and add all the \n   necessary .class/.jar files to enable the whole lab to run\n4.\tpress the \"Grade All Labs\" button to begin grading\n\nIf necessary, use the \"Kill Current Test\" button if a particular\nmethod has entered an infinite loop or unstable state.\n";
                JOptionPane.showMessageDialog(null, string);
            }
        });
        this.aboutMenuItem = new JMenuItem("About");
        this.aboutMenuItem.setMnemonic(65);
        this.aboutMenuItem.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                GradingToolGui.this.showAbout();
            }
        });
        this.helpMenu.add(this.aboutMenuItem);
        this.helpMenu.add(this.directionsMenuItem);
        this.helpMenu.add(changeLog);
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add(this.helpMenu);
        this.setJMenuBar(jMenuBar);
        this.btKill = new JButton("Kill Current Test");
        this.btKill.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                GradingTool.killCurrent();
            }
        });
        this.optionsMenu = new JMenu("Options");
        this.optionsMenu.setMnemonic(79);
        outputOn = new JCheckBoxMenuItem("Output On");
        outputOn.setMnemonic(79);
        this.optionsMenu.add(outputOn);
        this.fileMenu = new JMenu("File");
        this.fileMenu.setMnemonic(70);
        this.openItem = new JMenuItem("Open JAM File");
        this.openItem.setMnemonic(79);
        this.openItem.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                File file = GradingTool.showOpenDialog("Select file to open", false, false, GradingToolGui.jamFF, true)[0];
                try {
                    Results results = Results.load(file);
                    results.generateOutput().show();
                }
                catch (Exception var3_4) {
                    var3_4.printStackTrace(System.err);
                }
            }
        });
        this.fileMenu.add(this.openItem);
        jMenuBar.add((Component)this.fileMenu, 0);
        this.helpMenu.add(JAMUpdate.getNewMenuItem(), 0);
        this.setJMenuBar(jMenuBar);
        this.btKill.setBackground(Color.red.brighter());
        this.btKill.setForeground(Color.black);
        this.btBut6.setForeground(Color.black);
        this.btBut6.setBackground(Color.green);
        this.setAllEnabled(true);
        this.setDefaultCloseOperation(3);
        this.setContentPane(this.pnPanel0);
        this.pack();
        this.setSize(750, 350);
        this.setResizable(false);
        this.sb = new StatusBar();
        this.show();
        this.init();
    }

    static /* synthetic */ JButton access$300(GradingToolGui gradingToolGui) {
        return gradingToolGui.getKillButton();
    }

    static /* synthetic */ JProgressBar access$400(GradingToolGui gradingToolGui) {
        return gradingToolGui.getProgressBar();
    }

    static {
        jamFF = new FileFilter(){

            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toLowerCase().endsWith(".jam");
            }

            public String getDescription() {
                return "JAM*Tester Results (*.jam)";
            }
        };
        csvFF = new FileFilter(){

            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toLowerCase().endsWith(".csv");
            }

            public String getDescription() {
                return "Comma Separated Values (*.csv)";
            }
        };
        UIManager.LookAndFeelInfo[] arrlookAndFeelInfo = UIManager.getInstalledLookAndFeels();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (ClassNotFoundException var1_1) {
        }
        catch (InstantiationException var1_2) {
        }
        catch (IllegalAccessException var1_3) {
        }
        catch (UnsupportedLookAndFeelException var1_4) {
            // empty catch block
        }
        changeLog = new JMenuItem("View Change Log");
        changeLog.setMnemonic(118);
        changeLog.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                new ChangeLog();
            }
        });
    }

}

