/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  apollo.BasicService
 *  apollo.FileContents
 *  apollo.PersistenceService
 *  apollo.ServiceManager
 */
package jplagwebstart;

import apollo.BasicService;
import apollo.FileContents;
import apollo.PersistenceService;
import apollo.ServiceManager;
import jamtester.JAMProperties;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleValue;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import jplagwebstart.JPlagOptions;
import jplagwebstart.JPlagServer;
import jplagwebstart.Program;
import jplagwebstart.ScanThread;
import jplagwebstart.SpringUtilities;
import jplagwebstart.Submission;
import jplagwebstart.SubmissionTree;
import jplagwebstart.SwingProgressBar;

public class JPlagGUI
extends JFrame
implements ActionListener,
ItemListener,
DocumentListener,
Runnable {
    private static final int SCAN_DELAY = 3000;
    private JTextField usertext;
    private JTextField rootDir;
    private JTextField subdir;
    private JTextField suffixes_t;
    private JTextField mmltext;
    private JPasswordField password;
    private JCheckBox readSubs;
    private JComboBox languageCombo;
    private JComboBox basecodeCombo;
    private JButton submit;
    private JButton browse;
    private JTextField scanStatus;
    private JTextField scanFiles;
    private JTextField scanSize;
    private JTextField scanSubmissions;
    private ScanThread scanThread = null;
    private JButton preview;
    private Timer scanTimer;
    private JPlagOptions previewOptions;
    private Vector previewSubs = null;
    private boolean changed;
    private SubmissionTree treePreview = null;

    public void setRootDir(File file) {
        this.rootDir.setText(file.getAbsolutePath());
    }

    public JPlagGUI(String string) {
        super(string);
        JPanel jPanel = new JPanel();
        JPanel jPanel2 = new JPanel();
        JPanel jPanel3 = new JPanel();
        JPanel jPanel4 = new JPanel();
        JPanel jPanel5 = new JPanel();
        JLabel jLabel = new JLabel();
        ImageIcon imageIcon = this.createImageIcon("logo.gif");
        ImageIcon imageIcon2 = this.createImageIcon("icon.gif");
        this.setIconImage(imageIcon2.getImage());
        jLabel.setIcon(imageIcon);
        this.setFont(new Font("serif", 1, 17));
        this.getContentPane().setLayout(new BorderLayout());
        jPanel.setBackground(Color.white);
        jPanel.add(jLabel);
        GridBagLayout gridBagLayout = new GridBagLayout();
        jPanel3.setLayout(gridBagLayout);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = 17;
        gridBagConstraints.fill = 0;
        gridBagConstraints.gridwidth = 1;
        JLabel jLabel2 = new JLabel("User ID ");
        gridBagLayout.setConstraints(jLabel2, gridBagConstraints);
        jPanel3.add(jLabel2);
        gridBagConstraints.gridwidth = 0;
        this.usertext = new JTextField(12);
        gridBagLayout.setConstraints(this.usertext, gridBagConstraints);
        jPanel3.add(this.usertext);
        gridBagConstraints.anchor = 17;
        gridBagConstraints.fill = 0;
        gridBagConstraints.gridwidth = 1;
        jLabel2 = new JLabel("Password ");
        gridBagLayout.setConstraints(jLabel2, gridBagConstraints);
        jPanel3.add(jLabel2);
        gridBagConstraints.gridwidth = 0;
        this.password = new JPasswordField(12);
        gridBagLayout.setConstraints(this.password, gridBagConstraints);
        jPanel3.add(this.password);
        gridBagConstraints.fill = 0;
        gridBagConstraints.gridwidth = 1;
        JLabel jLabel3 = new JLabel("Directory", 2);
        gridBagLayout.setConstraints(jLabel3, gridBagConstraints);
        jPanel3.add(jLabel3);
        gridBagConstraints.fill = 2;
        gridBagConstraints.gridwidth = 2;
        this.rootDir = new JTextField(25);
        this.rootDir.setBackground(Color.white);
        gridBagLayout.setConstraints(this.rootDir, gridBagConstraints);
        jPanel3.add(this.rootDir);
        gridBagConstraints.gridwidth = 0;
        gridBagConstraints.fill = 0;
        this.browse = new JButton("Browse");
        this.browse.addActionListener(this);
        gridBagLayout.setConstraints(this.browse, gridBagConstraints);
        jPanel3.add(this.browse);
        Panel panel = new Panel();
        panel.setLayout(new GridLayout(2, 1, 0, -4));
        panel.add(new JLabel("In each submission-directory, look"));
        panel.add(new JLabel("only in this sub-directory [Optional]"));
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(panel, gridBagConstraints);
        jPanel3.add(panel);
        this.subdir = new JTextField(10);
        gridBagLayout.setConstraints(this.subdir, gridBagConstraints);
        jPanel3.add(this.subdir);
        gridBagConstraints.gridwidth = 0;
        gridBagConstraints.anchor = 13;
        this.readSubs = new JCheckBox("Recurse into Directories");
        this.readSubs.setBackground(Color.white);
        this.readSubs.addItemListener(this);
        gridBagLayout.setConstraints(this.readSubs, gridBagConstraints);
        jPanel3.add(this.readSubs);
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.anchor = 17;
        gridBagConstraints.fill = 2;
        JLabel jLabel4 = new JLabel("Basecode: (Instructor supplied code) ");
        gridBagLayout.setConstraints(jLabel4, gridBagConstraints);
        jPanel3.add(jLabel4);
        this.basecodeCombo = new JComboBox();
        this.basecodeCombo.setBackground(Color.white);
        this.basecodeCombo.setEnabled(false);
        this.basecodeCombo.addItem("-none-");
        gridBagConstraints.anchor = 17;
        gridBagConstraints.fill = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridwidth = 0;
        gridBagLayout.setConstraints(this.basecodeCombo, gridBagConstraints);
        jPanel3.add(this.basecodeCombo);
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.anchor = 17;
        gridBagConstraints.fill = 2;
        JLabel jLabel5 = new JLabel("Language ");
        gridBagLayout.setConstraints(jLabel5, gridBagConstraints);
        jPanel3.add(jLabel5);
        this.languageCombo = new JComboBox();
        this.languageCombo.setBackground(Color.white);
        this.languageCombo.setEnabled(false);
        gridBagConstraints.anchor = 17;
        gridBagConstraints.fill = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridwidth = 0;
        gridBagLayout.setConstraints(this.languageCombo, gridBagConstraints);
        jPanel3.add(this.languageCombo);
        gridBagConstraints.fill = 3;
        gridBagConstraints.anchor = 17;
        JLabel jLabel6 = new JLabel("Language-specific parameters");
        gridBagLayout.setConstraints(jLabel6, gridBagConstraints);
        jPanel3.add(jLabel6);
        gridBagConstraints.fill = 0;
        gridBagConstraints.gridwidth = -1;
        gridBagConstraints.gridwidth = 1;
        JLabel jLabel7 = new JLabel("Suffixes");
        gridBagLayout.setConstraints(jLabel7, gridBagConstraints);
        jPanel3.add(jLabel7);
        gridBagConstraints.fill = 2;
        gridBagConstraints.gridwidth = 0;
        this.suffixes_t = new JTextField(15);
        gridBagLayout.setConstraints(this.suffixes_t, gridBagConstraints);
        jPanel3.add(this.suffixes_t);
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.fill = 0;
        gridBagConstraints.gridwidth = 1;
        JLabel jLabel8 = new JLabel("Sensibility");
        gridBagLayout.setConstraints(jLabel8, gridBagConstraints);
        jPanel3.add(jLabel8);
        gridBagConstraints.gridwidth = 0;
        this.mmltext = new JTextField(3);
        gridBagLayout.setConstraints(this.mmltext, gridBagConstraints);
        jPanel3.add(this.mmltext);
        gridBagConstraints.anchor = 17;
        gridBagConstraints.fill = 0;
        gridBagConstraints.gridwidth = 1;
        JLabel jLabel9 = new JLabel("         ");
        gridBagLayout.setConstraints(jLabel9, gridBagConstraints);
        jPanel3.add(jLabel9);
        this.submit = new JButton(" Submit ");
        this.submit.addActionListener(this);
        gridBagConstraints.fill = -1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(this.submit, gridBagConstraints);
        jPanel3.add(this.submit);
        gridBagConstraints.gridwidth = 0;
        jPanel3.setBackground(Color.white);
        jPanel3.setBorder(BorderFactory.createTitledBorder("Options"));
        jPanel4.setLayout(new SpringLayout());
        jLabel2 = new JLabel("Status:", 10);
        jPanel4.add(jLabel2);
        this.scanStatus = new JTextField();
        this.scanStatus.setEnabled(false);
        this.scanStatus.setDisabledTextColor(this.scanStatus.getForeground());
        jLabel2.setLabelFor(this.scanStatus);
        jPanel4.add(this.scanStatus);
        jLabel2 = new JLabel("Number of Programs: (Students)", 10);
        jPanel4.add(jLabel2);
        this.scanSubmissions = new JTextField(15);
        this.scanSubmissions.setEnabled(false);
        this.scanSubmissions.setDisabledTextColor(this.scanSubmissions.getForeground());
        jLabel2.setLabelFor(this.scanSubmissions);
        jPanel4.add(this.scanSubmissions);
        jLabel2 = new JLabel("Total Number of Files:", 10);
        jPanel4.add(jLabel2);
        this.scanFiles = new JTextField(15);
        this.scanFiles.setEnabled(false);
        this.scanFiles.setDisabledTextColor(this.scanFiles.getForeground());
        jLabel2.setLabelFor(this.scanFiles);
        jPanel4.add(this.scanFiles);
        jLabel2 = new JLabel("Total Size of Submission:", 10);
        jPanel4.add(jLabel2);
        this.scanSize = new JTextField(15);
        this.scanSize.setEnabled(false);
        this.scanSize.setDisabledTextColor(this.scanSize.getForeground());
        jLabel2.setLabelFor(this.scanSize);
        jPanel4.add(this.scanSize);
        jLabel2 = new JLabel("Preview file list:", 11);
        jPanel4.add(jLabel2);
        this.preview = new JButton(" View ");
        this.preview.addActionListener(this);
        this.preview.setEnabled(false);
        jLabel2.setLabelFor(this.preview);
        JPanel jPanel6 = new JPanel();
        jPanel6.add(this.preview);
        jPanel6.setLayout(new FlowLayout(0, 0, 0));
        jPanel6.setBackground(Color.white);
        jPanel4.add(jPanel6);
        jPanel4.setBackground(Color.white);
        jPanel4.setBorder(BorderFactory.createTitledBorder("Submission Preview"));
        jPanel2.add(jPanel3);
        jPanel2.add(jPanel4);
        jPanel2.setLayout(new BoxLayout(jPanel2, 1));
        this.getContentPane().add((Component)jPanel, "North");
        this.getContentPane().add((Component)jPanel2, "Center");
        this.getContentPane().add((Component)jPanel5, "South");
        this.getContentPane().add((Component)new JLabel(), "Last");
        SpringUtilities.makeCompactGrid(jPanel4, 5, 2, 6, 6, 6, 3);
        this.scanTimer = new Timer(3000, this);
        this.scanTimer.setRepeats(false);
        this.scanTimer.start();
        this.rootDir.getDocument().addDocumentListener(this);
        this.subdir.getDocument().addDocumentListener(this);
        this.suffixes_t.getDocument().addDocumentListener(this);
        this.mmltext.getDocument().addDocumentListener(this);
        this.addRemainingLanguages(this.readPersistentData());
        this.initFromProperties();
    }

    public void message(String string) {
        JOptionPane.showMessageDialog(new JFrame(), string, "Message", 1);
    }

    private void writePersistentData() {
        PersistenceService persistenceService = ServiceManager.lookupPersistenceService();
        BasicService basicService = ServiceManager.lookupBasicService();
        if (this.languageCombo.getItemCount() == 0) {
            return;
        }
        try {
            URL uRL = basicService.getCodeBase();
            URL uRL2 = new URL(uRL, "JPlag");
            try {
                persistenceService.delete(uRL2);
            }
            catch (Exception var5_6) {
                // empty catch block
            }
            persistenceService.create(uRL2, 1024);
            FileContents fileContents = persistenceService.get(uRL2);
            DataOutputStream dataOutputStream = new DataOutputStream(fileContents.getOutputStream(true));
            String string = this.usertext.getText() + "\n";
            string = string + new String(this.password.getPassword()) + "\n";
            string = string + this.rootDir.getText() + "\n";
            string = string + this.subdir.getText() + "\n";
            string = string + this.readSubs.getAccessibleContext().getAccessibleValue().getCurrentAccessibleValue().intValue() + "\n";
            string = string + this.languageCombo.getSelectedItem().toString() + "\n";
            string = string + this.suffixes_t.getText() + "\n";
            string = string + this.mmltext.getText() + "\n";
            dataOutputStream.writeBytes(string);
            dataOutputStream.flush();
            dataOutputStream.close();
        }
        catch (Exception var3_4) {
            this.message("Unable to write persistent data.");
            var3_4.printStackTrace(System.out);
            return;
        }
    }

    private boolean readPersistentData() {
        PersistenceService persistenceService = ServiceManager.lookupPersistenceService();
        BasicService basicService = ServiceManager.lookupBasicService();
        try {
            URL uRL = basicService.getCodeBase();
            URL uRL2 = new URL(uRL, "JPlag");
            FileContents fileContents = persistenceService.get(uRL2);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileContents.getInputStream()));
            this.usertext.setText(bufferedReader.readLine());
            this.password.setText(bufferedReader.readLine());
            this.rootDir.setText(bufferedReader.readLine());
            this.subdir.setText(bufferedReader.readLine());
            this.readSubs.getAccessibleContext().getAccessibleValue().setCurrentAccessibleValue(new Integer(bufferedReader.readLine()));
            this.languageCombo.addItem(bufferedReader.readLine());
            this.suffixes_t.setText(bufferedReader.readLine());
            this.mmltext.setText(bufferedReader.readLine());
            bufferedReader.close();
            persistenceService.delete(uRL2);
        }
        catch (Exception var3_4) {
            return false;
        }
        return true;
    }

    private void addListener() {
        this.languageCombo.addItemListener(this);
    }

    private void addRemainingLanguages(boolean bl) {
        Thread thread = bl ? new Thread(){

            public void run() {
                .yield();
                String string = (String)JPlagGUI.this.languageCombo.getSelectedItem();
                String[] arrstring = JPlagServer.getLanguages();
                for (int i = 0; i < arrstring.length; ++i) {
                    if (string == null || string.equals(arrstring[i])) continue;
                    JPlagGUI.this.languageCombo.addItem(arrstring[i]);
                }
                JPlagGUI.this.languageCombo.setEnabled(true);
                JPlagGUI.this.addListener();
            }
        } : new Thread(){

            public void run() {
                JPlagGUI.this.addListener();
                String[] arrstring = JPlagServer.getLanguages();
                for (int i = 0; i < arrstring.length; ++i) {
                    JPlagGUI.this.languageCombo.addItem(arrstring[i]);
                }
                JPlagGUI.this.languageCombo.setEnabled(true);
            }
        };
        thread.start();
    }

    private ImageIcon createImageIcon(String string) {
        URL uRL = null;
        try {
            uRL = new File(string).toURL();
        }
        catch (Exception var3_3) {
            // empty catch block
        }
        if (uRL != null) {
            return new ImageIcon(uRL);
        }
        System.err.println("Couldn't find file: " + string);
        return null;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        if (object == this.browse) {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setCurrentDirectory(new File(this.rootDir.getText()));
            jFileChooser.setDialogTitle("Choose Root Directory");
            jFileChooser.setFileSelectionMode(1);
            if (jFileChooser.showOpenDialog(this) == 0) {
                File file = jFileChooser.getSelectedFile();
                this.rootDir.setText(file.toString());
            }
        } else if (object == this.submit) {
            String string = new String(this.password.getPassword());
            if (this.usertext.getText().trim().equals("") || string.trim().equals("") || this.rootDir.getText().trim().equals("") || this.mmltext.getText().trim().equals("")) {
                this.message("Not sufficient data.");
            } else {
                Thread thread = new Thread(this);
                this.setEnableElements(false);
                thread.start();
            }
        } else if (object == this.scanTimer) {
            if (this.rootDir.getText().trim().equals("")) {
                this.guiSetPreview("No root directory given.", "", "", "");
            } else {
                JPlagOptions jPlagOptions = new JPlagOptions();
                String string = this.setOptions(jPlagOptions);
                if (string != null) {
                    this.guiSetPreview(string, "", "", "");
                } else {
                    if (this.treePreview != null) {
                        this.treePreview.setVisible(false);
                    }
                    this.scanThread = new ScanThread(this, jPlagOptions);
                    this.previewSubs = null;
                    this.changed = false;
                    this.preview.setEnabled(false);
                    this.scanThread.start();
                }
            }
        } else if (object == this.preview) {
            if (this.previewSubs == null) {
                return;
            }
            this.updateTree();
        }
    }

    public void run() {
        try {
            if (this.scanThread != null) {
                this.scanThread.interrupt();
            }
            SwingProgressBar swingProgressBar = new SwingProgressBar();
            swingProgressBar.setBar("Checking Options...");
            JPlagOptions jPlagOptions = new JPlagOptions();
            String string = this.setOptions(jPlagOptions);
            if (string != null) {
                this.message(string);
            } else {
                Program.send(jPlagOptions, swingProgressBar);
            }
            swingProgressBar.finish();
            jPlagOptions.setOriginalDir("");
            jPlagOptions.setBasecode("");
            JAMProperties jAMProperties = JAMProperties.loadProperties();
            jAMProperties.setJPlagOptions(jPlagOptions);
            jAMProperties.save();
        }
        catch (Throwable var1_2) {
            // empty catch block
        }
        this.setEnableElements(true);
    }

    private boolean initFromProperties() {
        JPlagOptions jPlagOptions;
        JAMProperties jAMProperties = JAMProperties.loadProperties();
        boolean bl = true;
        if (jAMProperties.getJPlagOptions() == null) {
            bl = false;
        }
        if ((jPlagOptions = jAMProperties.getJPlagOptions()) == null) {
            jPlagOptions = new JPlagOptions();
            jPlagOptions.setLanguage("java15");
            jPlagOptions.setSuffixes(".java,.jav,.JAVA,.JAV");
            jPlagOptions.setMML("8");
        }
        this.languageCombo.addItem(jPlagOptions.getLanguage());
        this.languageCombo.setSelectedItem(jPlagOptions.getLanguage());
        this.usertext.setText(jPlagOptions.getUser());
        this.password.setText(jPlagOptions.getPassword());
        this.mmltext.setText("8");
        this.suffixes_t.setText(".java,.jav,.JAVA,.JAV");
        this.subdir.setText(jPlagOptions.getSubDir());
        this.readSubs.setSelected(jPlagOptions.getRecurse());
        return bl;
    }

    private void setEnableElements(boolean bl) {
        this.submit.setEnabled(bl);
    }

    private String setOptions(JPlagOptions jPlagOptions) {
        jPlagOptions.setUser(this.usertext.getText());
        jPlagOptions.setPassword(new String(this.password.getPassword()));
        File file = new File(this.rootDir.getText());
        if (!file.isDirectory()) {
            return "`" + file.toString() + "` is not a directory!\n";
        }
        jPlagOptions.setOriginalDir(file.toString());
        String string = jPlagOptions.setMML(this.mmltext.getText());
        if (string != null) {
            return string;
        }
        string = jPlagOptions.setSuffixes(this.suffixes_t.getText());
        if (string != null) {
            return string;
        }
        jPlagOptions.setSubDir(this.subdir.getText());
        jPlagOptions.setLanguage(this.languageCombo.getSelectedItem().toString());
        jPlagOptions.setRecurse(this.readSubs.getAccessibleContext().getAccessibleValue().getCurrentAccessibleValue().intValue() == 1);
        if (this.basecodeCombo.getSelectedIndex() != 0) {
            jPlagOptions.setBasecode(this.basecodeCombo.getSelectedItem().toString());
        }
        return null;
    }

    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getSource() == this.languageCombo) {
            String string = (String)this.languageCombo.getSelectedItem();
            this.mmltext.setText(JPlagServer.getMML(string));
            this.suffixes_t.setText(JPlagServer.getSuffixes(string));
        } else if (itemEvent.getSource() == this.readSubs) {
            this.updateTimer();
        }
    }

    public void changedUpdate(DocumentEvent documentEvent) {
        this.updateTimer();
    }

    public void insertUpdate(DocumentEvent documentEvent) {
        this.updateTimer();
    }

    public void removeUpdate(DocumentEvent documentEvent) {
        this.updateTimer();
    }

    public void guiSetPreview(String string, String string2, String string3, String string4) {
        if (string != null) {
            this.scanStatus.setText(string);
        }
        if (string2 != null) {
            this.scanSubmissions.setText(string2);
        }
        if (string3 != null) {
            this.scanFiles.setText(string3);
        }
        if (string4 != null) {
            this.scanSize.setText(string4);
        }
    }

    private void updateTimer() {
        if (!this.changed) {
            this.guiSetPreview("preview not up to date...", null, null, null);
            if (this.treePreview != null) {
                this.treePreview.setVisible(false);
            }
            this.deactivateBasecodeCombo();
            this.preview.setEnabled(false);
            this.previewSubs = null;
        }
        this.changed = true;
        if (this.scanThread != null) {
            this.scanThread.interrupt();
        } else {
            this.scanTimer.restart();
        }
    }

    public void scanThreadEnds(Vector vector, JPlagOptions jPlagOptions) {
        if (vector != null) {
            this.preview.setEnabled(true);
        }
        this.previewOptions = jPlagOptions;
        this.previewSubs = vector;
        if (this.scanThread == Thread.currentThread()) {
            this.scanThread = null;
            if (this.changed) {
                this.scanTimer.restart();
            } else if (this.treePreview != null) {
                this.updateTree();
            } else {
                this.activateBasecodeCombo();
            }
        }
    }

    public void updateTree() {
        if (this.treePreview != null) {
            this.treePreview.setVisible(false);
        }
        this.treePreview = new SubmissionTree(this.previewSubs, this.previewOptions, this);
        this.activateBasecodeCombo();
    }

    public void deactivateBasecodeCombo() {
        this.basecodeCombo.setEnabled(false);
        this.basecodeCombo.setSelectedIndex(0);
    }

    public void activateBasecodeCombo() {
        if (this.previewSubs == null || this.changed) {
            this.deactivateBasecodeCombo();
            return;
        }
        Vector vector = this.previewSubs;
        this.basecodeCombo.removeAllItems();
        this.basecodeCombo.addItem("-none-");
        Iterator iterator = vector.iterator();
        while (iterator.hasNext()) {
            Submission submission = (Submission)iterator.next();
            this.basecodeCombo.addItem(submission.name);
        }
        this.basecodeCombo.setEnabled(true);
    }

    public void previewClosed() {
        this.treePreview = null;
    }

}

