/*
 * Decompiled with CFR 0_102.
 */
package jamtester.jdk;

import jamtester.JAMProperties;
import jamtester.jdk.AugmentedArrayList;
import jamtester.jdk.JDKDetect;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.metal.MetalIconFactory;

public class JDKChooser
extends JDialog {
    private File jdkLoc;
    private ArrayList al = new AugmentedArrayList();
    private boolean searching = false;
    private Thread searchThread = null;
    private JButton jButton1;
    private JButton jButton2;
    private JList jList1;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JProgressBar jProgressBar1;
    private JScrollPane jScrollPane1;
    private JButton okButton;

    public JDKChooser(Frame frame, boolean bl) {
        super(frame, bl);
        this.initComponents();
        this.setSize(450, 225);
        JDKChooser.centerWindow(this);
        this.jProgressBar1.setStringPainted(true);
        this.jList1.setModel((AugmentedArrayList)this.al);
        ((TitledBorder)this.jPanel2.getBorder()).setTitle("Detected JDK Locations (your current VM is " + System.getProperty("java.vm.version") + "):");
        this.selectFirstAvailable();
    }

    public static void centerWindow(Window window) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        try {
            Dimension dimension = toolkit.getScreenSize();
            window.setLocation((int)(dimension.getWidth() - (double)window.getWidth()) / 2, (int)(dimension.getHeight() - (double)window.getHeight()) / 2);
        }
        catch (Exception var2_3) {
            var2_3.printStackTrace();
        }
    }

    private void selectFirstAvailable() {
        new Thread(){

            public void run() {
                while (JDKChooser.this.al.size() == 0) {
                }
                JDKChooser.this.jList1.setSelectedIndex(0);
            }
        }.start();
    }

    private void initComponents() {
        this.jPanel1 = new JPanel();
        this.jButton1 = new JButton();
        this.jButton2 = new JButton();
        this.okButton = new JButton();
        this.jPanel2 = new JPanel();
        this.jScrollPane1 = new JScrollPane();
        this.jList1 = new JList();
        this.jProgressBar1 = new JProgressBar();
        this.setDefaultCloseOperation(2);
        this.setTitle("JDK Selection...");
        this.setModal(true);
        this.setResizable(false);
        this.jPanel1.setPreferredSize(new Dimension(10, 30));
        this.jButton1.setText("Browse for JDK");
        this.jButton1.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                JDKChooser.this.jButton1ActionPerformed(actionEvent);
            }
        });
        this.jPanel1.add(this.jButton1);
        this.jButton2.setText("Search my drives");
        this.jButton2.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                JDKChooser.this.jButton2ActionPerformed(actionEvent);
            }
        });
        this.jPanel1.add(this.jButton2);
        this.okButton.setText("Ok");
        this.okButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                JDKChooser.this.okButtonActionPerformed(actionEvent);
            }
        });
        this.jPanel1.add(this.okButton);
        this.getContentPane().add((Component)this.jPanel1, "South");
        this.jPanel2.setLayout(new BorderLayout());
        this.jPanel2.setBorder(new TitledBorder(new EtchedBorder(), "Detected JDK Locations"));
        this.jList1.setSelectionMode(0);
        this.jScrollPane1.setViewportView(this.jList1);
        this.jPanel2.add((Component)this.jScrollPane1, "Center");
        this.getContentPane().add((Component)this.jPanel2, "Center");
        this.jProgressBar1.setPreferredSize(new Dimension(150, 25));
        this.getContentPane().add((Component)this.jProgressBar1, "North");
        this.pack();
    }

    private void jButton2ActionPerformed(ActionEvent actionEvent) {
        if (this.searching) {
            this.searchThread.stop();
            this.searching = false;
            this.jButton2.setText("Search my disks");
            return;
        }
        JOptionPane.showMessageDialog(this, "WARNING: This may take a few minutes", "WARNING", 2);
        this.searchThread = new Thread(){

            public void run() {
                JDKChooser.this.searching = true;
                new JDKDetect(JDKChooser.this.jProgressBar1).findJDKs(JDKChooser.this.al);
            }
        };
        this.searchThread.start();
        this.jButton2.setText("Stop searching");
    }

    private void jButton1ActionPerformed(ActionEvent actionEvent) {
        JFileChooser jFileChooser = new JFileChooser(){

            public boolean accept(File file) {
                return file.isDirectory();
            }
        };
        jFileChooser.setFileView(new JDKFileView());
        jFileChooser.setMultiSelectionEnabled(false);
        jFileChooser.showOpenDialog(this);
        if (JDKDetect.isJDK(jFileChooser.getSelectedFile())) {
            this.al.add(jFileChooser.getSelectedFile());
            this.jList1.setSelectedIndex(this.al.size() - 1);
        }
    }

    private void okButtonActionPerformed(ActionEvent actionEvent) {
        this.jdkLoc = (File)this.jList1.getSelectedValue();
        if (this.jdkLoc == null) {
            JOptionPane.showMessageDialog(this, "ERROR: no JDK selected.  Program cannot continue and will be terminated.\n\nIf you need to download a Java SDK, please visit http://java.sun.com", "ERROR: No JDK selected", 0);
            System.exit(0);
        }
        this.dispose();
        JAMProperties jAMProperties = JAMProperties.loadProperties();
        jAMProperties.setJDKLoc(this.jdkLoc.getAbsolutePath());
        jAMProperties.save();
    }

    public static void main(String[] arrstring) {
        JOptionPane.showMessageDialog(null, "BOO!");
        new JDKChooser(new JFrame(), true).setVisible(true);
    }

    private static class JDKFileView
    extends FileView {
        private JDKFileView() {
        }

        public String getDescription(File file) {
            if (JDKDetect.isJDK(file)) {
                return "JDK Directory";
            }
            return null;
        }

        public Icon getIcon(File file) {
            if (JDKDetect.isJDK(file)) {
                return new MetalIconFactory.FolderIcon16();
            }
            return null;
        }

        public String getName(File file) {
            return null;
        }

        public String getTypeDescription(File file) {
            if (JDKDetect.isJDK(file)) {
                return "JDK Directory";
            }
            return null;
        }

        public Boolean isTraversable(File file) {
            if (JDKDetect.isJDK(file)) {
                return new Boolean(false);
            }
            return null;
        }
    }

}

