/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.Utilities;
import org.jext.gui.AbstractDisposer;
import org.jext.gui.DisabledCellRenderer;
import org.jext.gui.JextHighlightButton;

public class ZipExplorer
extends JDialog
implements ActionListener {
    private JextFrame parent;
    private String zipName;
    private JTable zipTable;
    private ZipFile zipFile;
    private JextHighlightButton open;
    private JextHighlightButton cancel;
    private JextTextArea textArea;
    private ZipTableModel zipModel;
    private Enumeration zipEntries;

    public ZipExplorer(JextFrame jextFrame, JextTextArea jextTextArea, String string) {
        super(jextFrame, Jext.getProperty("zip.explorer.title"), true);
        this.parent = jextFrame;
        this.textArea = jextTextArea;
        this.readZip(string);
        this.zipName = string;
        this.getContentPane().setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        this.open = new JextHighlightButton(Jext.getProperty("general.open.button"));
        jPanel.add(this.open);
        this.open.setToolTipText(Jext.getProperty("general.open.tip"));
        this.open.setMnemonic(Jext.getProperty("general.open.mnemonic").charAt(0));
        this.open.addActionListener(this);
        this.getRootPane().setDefaultButton(this.open);
        this.cancel = new JextHighlightButton(Jext.getProperty("general.cancel.button"));
        jPanel.add(this.cancel);
        this.cancel.setMnemonic(Jext.getProperty("general.cancel.mnemonic").charAt(0));
        this.cancel.addActionListener(this);
        this.getContentPane().add("Center", this.createZipTableScroller());
        this.getContentPane().add("South", jPanel);
        this.setDefaultCloseOperation(0);
        this.addKeyListener(new AbstractDisposer(this));
        this.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent windowEvent) {
                ZipExplorer.this.cancel();
            }
        });
        this.pack();
        Utilities.centerComponentChild(jextFrame, this);
        this.setVisible(true);
    }

    private JScrollPane createZipTableScroller() {
        this.zipTable = new JTable(new ZipTableModel());
        this.zipTable.getTableHeader().setReorderingAllowed(false);
        this.zipTable.getColumnModel().getColumn(1).setCellRenderer(new DisabledCellRenderer());
        JScrollPane jScrollPane = new JScrollPane(this.zipTable);
        jScrollPane.getViewport().setPreferredSize(new Dimension(300, 200));
        return jScrollPane;
    }

    private void readZip(String string) {
        if (string == null) {
            return;
        }
        try {
            File file = new File(string);
            if (!file.exists()) {
                Utilities.showError(Jext.getProperty("textarea.file.notexists"));
                return;
            }
            this.zipFile = new ZipFile(file);
            this.zipEntries = this.zipFile.entries();
        }
        catch (ZipException var2_3) {
        }
        catch (IOException var2_4) {
            // empty catch block
        }
    }

    private boolean readZipContent(String string) {
        if (this.zipFile == null || string == null) {
            return false;
        }
        try {
            ZipEntry zipEntry = this.zipFile.getEntry(string);
            if (zipEntry == null) {
                return false;
            }
            this.textArea.open(string, new InputStreamReader(this.zipFile.getInputStream(zipEntry)), (int)zipEntry.getSize());
            this.parent.resetStatus(this.textArea);
        }
        catch (IOException var2_3) {
            Object[] arrobject = new String[]{string};
            Utilities.showError(Jext.getProperty("zip.file.corrupted", arrobject));
            return false;
        }
        return true;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        if (object == this.open) {
            int[] arrn = this.zipTable.getSelectedRows();
            for (int i = 0; i < this.zipTable.getSelectedRowCount(); ++i) {
                String string = (String)this.zipTable.getValueAt(arrn[i], 0);
                String string2 = (String)this.zipTable.getValueAt(arrn[i], 1);
                if (string.endsWith(".jar") || string.endsWith(".zip")) {
                    Utilities.showError(Jext.getProperty("zip.file.corrupted"));
                    return;
                }
                if (i != 0) {
                    this.textArea = this.parent.createFile();
                }
                if (!string2.equals("/")) {
                    string = string2 + "/" + string;
                }
                if (this.readZipContent(string)) continue;
                this.cancel();
                return;
            }
            this.parent.saveRecent(this.zipName);
            this.cancel();
        } else if (object == this.cancel) {
            this.cancel();
        }
    }

    private void cancel() {
        try {
            this.zipFile.close();
        }
        catch (IOException var1_1) {
            // empty catch block
        }
        this.dispose();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.parent = null;
        this.zipName = null;
        this.zipTable = null;
        this.zipFile = null;
        this.open = null;
        this.cancel = null;
        this.textArea = null;
        this.zipModel = null;
        this.zipEntries = null;
    }

    class ZipTableModel
    extends AbstractTableModel {
        private ArrayList zipContents;

        ZipTableModel() {
            this.zipContents = new ArrayList();
            while (ZipExplorer.this.zipEntries.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry)ZipExplorer.this.zipEntries.nextElement();
                if (zipEntry == null || zipEntry.isDirectory()) continue;
                this.addZipEntry(zipEntry);
            }
        }

        public int getColumnCount() {
            return 2;
        }

        public int getRowCount() {
            return this.zipContents.size();
        }

        public Object getValueAt(int n, int n2) {
            String string;
            String string2;
            ZipEntry zipEntry = (ZipEntry)this.zipContents.get(n);
            String string3 = zipEntry.getName();
            int n3 = string3.lastIndexOf(47);
            if (n3 == -1) {
                string = string3;
                string2 = "/";
            } else {
                string = string3.substring(n3 + 1);
                string2 = string3.substring(0, n3);
            }
            switch (n2) {
                case 0: {
                    return string;
                }
                case 1: {
                    return string2;
                }
            }
            return null;
        }

        public boolean isCellEditable(int n, int n2) {
            return false;
        }

        public String getColumnName(int n) {
            switch (n) {
                case 0: {
                    return Jext.getProperty("zip.explorer.filenames");
                }
                case 1: {
                    return Jext.getProperty("zip.explorer.directories");
                }
            }
            return null;
        }

        private void addZipEntry(ZipEntry zipEntry) {
            for (int i = 0; i < this.zipContents.size(); ++i) {
                ZipEntry zipEntry2 = (ZipEntry)this.zipContents.get(i);
                if (zipEntry2.getName().compareTo(zipEntry.getName()) < 0) continue;
                this.zipContents.add(i, zipEntry);
                return;
            }
            this.zipContents.add(zipEntry);
        }
    }

}

