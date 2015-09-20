/*
 * Decompiled with CFR 0_102.
 */
package org.jext.search;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTabbedPane;
import org.jext.JextTextArea;
import org.jext.Utilities;
import org.jext.gui.AbstractDisposer;
import org.jext.gui.JextCheckBox;
import org.jext.gui.JextHighlightButton;
import org.jext.gui.ModifiedCellRenderer;
import org.jext.search.Search;

public class FindReplace
extends JDialog
implements ActionListener {
    public static final int SEARCH = 1;
    public static final int REPLACE = 2;
    private int type;
    private JextFrame parent;
    private JComboBox fieldSearch;
    private JComboBox fieldReplace;
    private JTextField fieldSearchEditor;
    private JTextField fieldReplaceEditor;
    private JTextField script;
    private JextHighlightButton btnFind;
    private JextHighlightButton btnReplace;
    private JextHighlightButton btnReplaceAll;
    private JextHighlightButton btnCancel;
    private JextCheckBox checkIgnoreCase;
    private JextCheckBox saveStates;
    private JextCheckBox useRegexp;
    private JextCheckBox allFiles;
    private JextCheckBox scripted;

    private void buildConstraints(GridBagConstraints gridBagConstraints, int n, int n2, int n3, int n4, int n5, int n6) {
        gridBagConstraints.gridx = n;
        gridBagConstraints.gridy = n2;
        gridBagConstraints.gridwidth = n3;
        gridBagConstraints.gridheight = n4;
        gridBagConstraints.weightx = n5;
        gridBagConstraints.weighty = n6;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
    }

    public FindReplace(JextFrame jextFrame, int n, boolean bl) {
        super(jextFrame, n == 2 ? Jext.getProperty("replace.title") : Jext.getProperty("find.title"), bl);
        this.parent = jextFrame;
        this.type = n;
        this.fieldSearch = new JComboBox();
        this.fieldSearch.setRenderer(new ModifiedCellRenderer());
        this.fieldSearch.setEditable(true);
        this.fieldReplace = new JComboBox();
        this.fieldReplace.setRenderer(new ModifiedCellRenderer());
        this.fieldReplace.setEditable(true);
        KeyHandler keyHandler = new KeyHandler();
        this.fieldSearchEditor = (JTextField)this.fieldSearch.getEditor().getEditorComponent();
        this.fieldSearchEditor.addKeyListener(keyHandler);
        this.fieldReplaceEditor = (JTextField)this.fieldReplace.getEditor().getEditorComponent();
        this.fieldReplaceEditor.addKeyListener(keyHandler);
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        this.getContentPane().setLayout(gridBagLayout);
        ((JPanel)this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        JLabel jLabel = new JLabel(Jext.getProperty("find.label"));
        this.buildConstraints(gridBagConstraints, 0, 0, 1, 1, 25, 50);
        gridBagConstraints.anchor = 17;
        gridBagLayout.setConstraints(jLabel, gridBagConstraints);
        this.getContentPane().add(jLabel);
        this.buildConstraints(gridBagConstraints, 1, 0, 1, 1, 25, 50);
        gridBagConstraints.fill = 2;
        gridBagConstraints.anchor = 10;
        gridBagLayout.setConstraints(this.fieldSearch, gridBagConstraints);
        this.getContentPane().add(this.fieldSearch);
        this.btnFind = new JextHighlightButton(Jext.getProperty("find.button"));
        this.btnFind.setToolTipText(Jext.getProperty("find.tip"));
        this.btnFind.setMnemonic(Jext.getProperty("find.mnemonic").charAt(0));
        this.btnFind.addActionListener(this);
        this.buildConstraints(gridBagConstraints, 2, 0, 1, 1, 25, 50);
        gridBagConstraints.anchor = 10;
        gridBagLayout.setConstraints(this.btnFind, gridBagConstraints);
        this.getContentPane().add(this.btnFind);
        this.getRootPane().setDefaultButton(this.btnFind);
        this.btnCancel = new JextHighlightButton(Jext.getProperty("general.cancel.button"));
        this.btnCancel.setMnemonic(Jext.getProperty("general.cancel.mnemonic").charAt(0));
        this.btnCancel.addActionListener(this);
        this.buildConstraints(gridBagConstraints, 3, 0, 1, 1, 25, 50);
        gridBagConstraints.anchor = 10;
        gridBagLayout.setConstraints(this.btnCancel, gridBagConstraints);
        this.getContentPane().add(this.btnCancel);
        JLabel jLabel2 = new JLabel(Jext.getProperty("replace.label"));
        this.buildConstraints(gridBagConstraints, 0, 1, 1, 1, 25, 50);
        gridBagConstraints.anchor = 17;
        gridBagLayout.setConstraints(jLabel2, gridBagConstraints);
        this.getContentPane().add(jLabel2);
        if (n != 2) {
            jLabel2.setEnabled(false);
        }
        this.buildConstraints(gridBagConstraints, 1, 1, 1, 1, 25, 50);
        gridBagConstraints.fill = 2;
        gridBagConstraints.anchor = 10;
        gridBagLayout.setConstraints(this.fieldReplace, gridBagConstraints);
        this.getContentPane().add(this.fieldReplace);
        if (n != 2) {
            this.fieldReplace.setEnabled(false);
        }
        this.btnReplace = new JextHighlightButton(Jext.getProperty("replace.button"));
        this.btnReplace.setToolTipText(Jext.getProperty("replace.tip"));
        this.btnReplace.setMnemonic(Jext.getProperty("replace.mnemonic").charAt(0));
        if (n != 2) {
            this.btnReplace.setEnabled(false);
        }
        this.btnReplace.addActionListener(this);
        this.buildConstraints(gridBagConstraints, 2, 1, 1, 1, 25, 50);
        gridBagConstraints.anchor = 10;
        gridBagLayout.setConstraints(this.btnReplace, gridBagConstraints);
        this.getContentPane().add(this.btnReplace);
        this.btnReplaceAll = new JextHighlightButton(Jext.getProperty("replace.all.button"));
        this.btnReplaceAll.setToolTipText(Jext.getProperty("replace.all.tip"));
        this.btnReplaceAll.setMnemonic(Jext.getProperty("replace.all.mnemonic").charAt(0));
        if (n != 2) {
            this.btnReplaceAll.setEnabled(false);
        }
        this.btnReplaceAll.addActionListener(this);
        this.buildConstraints(gridBagConstraints, 3, 1, 1, 1, 25, 50);
        gridBagConstraints.anchor = 10;
        gridBagLayout.setConstraints(this.btnReplaceAll, gridBagConstraints);
        this.getContentPane().add(this.btnReplaceAll);
        this.scripted = new JextCheckBox(Jext.getProperty("replace.script"), Search.getPythonScript());
        if (n != 2) {
            this.scripted.setEnabled(false);
        } else {
            this.fieldReplace.setEnabled(!this.scripted.isSelected());
            this.scripted.addActionListener(this);
        }
        this.buildConstraints(gridBagConstraints, 0, 2, 1, 1, 50, 50);
        gridBagConstraints.anchor = 17;
        gridBagLayout.setConstraints(this.scripted, gridBagConstraints);
        this.getContentPane().add(this.scripted);
        this.script = new JTextField();
        if (n != 2) {
            this.script.setEnabled(false);
        } else {
            this.script.setEnabled(this.scripted.isSelected());
        }
        this.script.setText(Search.getPythonScriptString());
        this.buildConstraints(gridBagConstraints, 1, 2, 1, 1, 50, 50);
        gridBagConstraints.anchor = 10;
        gridBagLayout.setConstraints(this.script, gridBagConstraints);
        this.getContentPane().add(this.script);
        this.checkIgnoreCase = new JextCheckBox(Jext.getProperty("find.ignorecase.label"), Search.getIgnoreCase());
        this.buildConstraints(gridBagConstraints, 0, 3, 1, 1, 25, 50);
        gridBagConstraints.anchor = 17;
        gridBagLayout.setConstraints(this.checkIgnoreCase, gridBagConstraints);
        this.getContentPane().add(this.checkIgnoreCase);
        JPanel jPanel = new JPanel();
        this.saveStates = new JextCheckBox(Jext.getProperty("find.savevalues.label"), Jext.getBooleanProperty("savestates"));
        this.allFiles = new JextCheckBox(Jext.getProperty("find.allFiles.label"), Jext.getBooleanProperty("allfiles"));
        jPanel.add(this.saveStates);
        jPanel.add(this.allFiles);
        this.buildConstraints(gridBagConstraints, 1, 3, 1, 1, 25, 50);
        gridBagConstraints.anchor = 17;
        gridBagLayout.setConstraints(jPanel, gridBagConstraints);
        this.getContentPane().add(jPanel);
        this.useRegexp = new JextCheckBox(Jext.getProperty("find.useregexp.label"), Search.getRegexp());
        this.buildConstraints(gridBagConstraints, 2, 3, 2, 1, 50, 50);
        gridBagConstraints.anchor = 17;
        gridBagLayout.setConstraints(this.useRegexp, gridBagConstraints);
        this.getContentPane().add(this.useRegexp);
        this.load();
        this.setDefaultCloseOperation(0);
        this.addKeyListener(new AbstractDisposer(this));
        this.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent windowEvent) {
                FindReplace.this.exit();
            }
        });
        FontMetrics fontMetrics = this.getFontMetrics(this.getFont());
        this.fieldSearch.setPreferredSize(new Dimension(18 * fontMetrics.charWidth('m'), this.fieldSearch.getPreferredSize().height));
        this.fieldReplace.setPreferredSize(new Dimension(18 * fontMetrics.charWidth('m'), this.fieldReplace.getPreferredSize().height));
        this.pack();
        this.setResizable(false);
        Utilities.centerComponentChild(jextFrame, this);
        this.btnFind.addKeyListener(keyHandler);
        this.btnReplace.addKeyListener(keyHandler);
        this.btnReplaceAll.addKeyListener(keyHandler);
        this.btnCancel.addKeyListener(keyHandler);
        this.checkIgnoreCase.addKeyListener(keyHandler);
        this.saveStates.addKeyListener(keyHandler);
        this.useRegexp.addKeyListener(keyHandler);
        this.allFiles.addKeyListener(keyHandler);
        this.scripted.addKeyListener(keyHandler);
        this.script.addKeyListener(keyHandler);
        this.show();
    }

    private void load() {
        int n;
        String string;
        for (int i = 0; i < 25 && (string = Jext.getProperty("search.history." + i)) != null; ++i) {
            this.fieldSearch.addItem(string);
        }
        JextTextArea jextTextArea = this.parent.getTextArea();
        if (!Jext.getBooleanProperty("use.selection")) {
            string = Search.getFindPattern();
            if (string != null) {
                this.addSearchHistory(string);
                this.fieldSearch.setSelectedItem(string);
            }
        } else {
            string = jextTextArea.getSelectedText();
            if (string != null) {
                n = 0;
                StringBuffer stringBuffer = new StringBuffer(string.length());
                block4 : for (int j = 0; j < string.length(); ++j) {
                    char c = string.charAt(j);
                    n = c;
                    switch (c) {
                        case '\n': {
                            break block4;
                        }
                        default: {
                            stringBuffer.append((char)n);
                            continue block4;
                        }
                    }
                }
                string = stringBuffer.toString();
                this.addSearchHistory(string);
                this.fieldSearch.setSelectedItem(string);
            }
        }
        if (this.type == 2) {
            for (n = 0; n < 25 && (string = Jext.getProperty("replace.history." + n)) != null; ++n) {
                this.fieldReplace.addItem(string);
            }
            string = Search.getReplacePattern();
            if (string != null) {
                this.addReplaceHistory(string);
                this.fieldReplace.setSelectedItem(string);
            }
        }
        this.fieldSearchEditor.selectAll();
    }

    private void exit() {
        if (this.saveStates.isSelected()) {
            int n;
            for (n = 0; n < this.fieldSearch.getItemCount(); ++n) {
                Jext.setProperty("search.history." + n, (String)this.fieldSearch.getItemAt(n));
            }
            for (n = this.fieldSearch.getItemCount(); n < 25; ++n) {
                Jext.unsetProperty("search.history." + n);
            }
            if (this.type == 2) {
                for (n = 0; n < this.fieldReplace.getItemCount(); ++n) {
                    Jext.setProperty("replace.history." + n, (String)this.fieldReplace.getItemAt(n));
                }
                for (n = this.fieldReplace.getItemCount(); n < 25; ++n) {
                    Jext.unsetProperty("replace.history." + n);
                }
            }
        }
        Jext.setProperty("savestates", this.saveStates.isSelected() ? "on" : "off");
        Jext.setProperty("allfiles", this.allFiles.isSelected() ? "on" : "off");
        Search.setIgnoreCase(this.checkIgnoreCase.isSelected());
        Search.setRegexp(this.useRegexp.isSelected());
        this.dispose();
    }

    private void addSearchHistory() {
        this.addSearchHistory(this.fieldSearchEditor.getText());
    }

    private void addSearchHistory(String string) {
        int n;
        if (string == null) {
            return;
        }
        for (n = 0; n < this.fieldSearch.getItemCount(); ++n) {
            if (!((String)this.fieldSearch.getItemAt(n)).equals(string)) continue;
            return;
        }
        this.fieldSearch.insertItemAt(string, 0);
        if (this.fieldSearch.getItemCount() > 25) {
            n = 25;
            while (n < this.fieldSearch.getItemCount()) {
                this.fieldSearch.removeItemAt(n);
            }
        }
        this.fieldSearchEditor.setText((String)this.fieldSearch.getItemAt(0));
    }

    private void addReplaceHistory() {
        this.addReplaceHistory(this.fieldReplaceEditor.getText());
    }

    private void addReplaceHistory(String string) {
        int n;
        if (string == null) {
            return;
        }
        for (n = 0; n < this.fieldReplace.getItemCount(); ++n) {
            if (!((String)this.fieldReplace.getItemAt(n)).equals(string)) continue;
            return;
        }
        this.fieldReplace.insertItemAt(string, 0);
        if (this.fieldReplace.getItemCount() > 25) {
            n = 25;
            while (n < this.fieldReplace.getItemCount()) {
                this.fieldReplace.removeItemAt(n);
            }
        }
        this.fieldReplaceEditor.setText((String)this.fieldReplace.getItemAt(0));
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        if (object == this.btnCancel) {
            this.exit();
        } else if (object == this.btnFind) {
            this.doFind();
        } else if (object == this.btnReplace) {
            this.doReplace();
        } else if (object == this.btnReplaceAll) {
            this.doReplaceAll();
        } else if (object == this.scripted) {
            this.script.setEnabled(this.scripted.isSelected());
            this.fieldReplace.setEnabled(!this.scripted.isSelected());
        }
    }

    private void setSettings() {
        Search.setFindPattern(this.fieldSearchEditor.getText());
        Search.setIgnoreCase(this.checkIgnoreCase.isSelected());
        Search.setRegexp(this.useRegexp.isSelected());
        if (this.type == 2) {
            Search.setReplacePattern(this.fieldReplaceEditor.getText());
            Search.setPythonScript(this.scripted.isSelected());
            Search.setPythonScriptString(this.script.getText());
        }
    }

    private void doReplaceAll() {
        Utilities.setCursorOnWait(this, true);
        this.addReplaceHistory();
        this.addSearchHistory();
        try {
            if (this.allFiles.isSelected()) {
                this.parent.setBatchMode(true);
                JextTextArea[] arrjextTextArea = this.parent.getTextAreas();
                for (int i = 0; i < arrjextTextArea.length; ++i) {
                    JextTextArea jextTextArea = arrjextTextArea[i];
                    this.setSettings();
                    Search.replaceAll(jextTextArea, 0, jextTextArea.getLength());
                }
                this.parent.setBatchMode(false);
            } else {
                JextTextArea jextTextArea = this.parent.getTextArea();
                this.setSettings();
                if (Search.replaceAll(jextTextArea, 0, jextTextArea.getLength()) == 0) {
                    Utilities.beep();
                }
            }
        }
        catch (Exception var1_5) {}
        finally {
            Utilities.setCursorOnWait(this, false);
        }
    }

    private void doReplace() {
        block6 : {
            Utilities.setCursorOnWait(this, true);
            this.addReplaceHistory();
            this.addSearchHistory();
            try {
                JextTextArea jextTextArea = this.parent.getTextArea();
                this.setSettings();
                if (!Search.replace(jextTextArea)) {
                    Utilities.beep();
                    break block6;
                }
                this.find(jextTextArea);
            }
            catch (Exception var1_2) {}
            finally {
                Utilities.setCursorOnWait(this, false);
            }
        }
    }

    private void doFind() {
        Utilities.setCursorOnWait(this, true);
        this.addSearchHistory();
        this.find(this.parent.getTextArea());
        Utilities.setCursorOnWait(this, false);
    }

    private void find(JextTextArea jextTextArea) {
        this.setSettings();
        try {
            if (!Search.find(jextTextArea, jextTextArea.getCaretPosition())) {
                Object[] arrobject = new String[]{jextTextArea.getName()};
                int n = JOptionPane.showConfirmDialog(null, Jext.getProperty("find.matchnotfound", arrobject), Jext.getProperty("find.title"), this.allFiles.isSelected() ? 1 : 0, 3);
                switch (n) {
                    case 0: {
                        jextTextArea.setCaretPosition(0);
                        this.find(jextTextArea);
                        break;
                    }
                    case 1: {
                        if (!this.allFiles.isSelected()) break;
                        JextTabbedPane jextTabbedPane = this.parent.getTabbedPane();
                        int n2 = jextTabbedPane.indexOfComponent(jextTextArea);
                        Component component = null;
                        while (!(component != null || component instanceof JextTextArea)) {
                            if (++n2 == jextTabbedPane.getTabCount()) {
                                n2 = 0;
                            }
                            component = jextTabbedPane.getComponentAt(n2);
                        }
                        JextTextArea jextTextArea2 = (JextTextArea)component;
                        if (jextTextArea2 == jextTextArea) break;
                        this.find(jextTextArea2);
                        break;
                    }
                    case 2: {
                        return;
                    }
                }
            }
        }
        catch (Exception var2_3) {
            // empty catch block
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.parent = null;
        this.fieldSearch = null;
        this.fieldReplace = null;
        this.fieldSearchEditor = null;
        this.fieldReplaceEditor = null;
        this.btnFind = null;
        this.btnReplace = null;
        this.btnReplaceAll = null;
        this.btnCancel = null;
        this.checkIgnoreCase = null;
        this.saveStates = null;
        this.useRegexp = null;
        this.allFiles = null;
    }

    class KeyHandler
    extends KeyAdapter {
        KeyHandler() {
        }

        public void keyPressed(KeyEvent keyEvent) {
            switch (keyEvent.getKeyCode()) {
                case 10: {
                    if (keyEvent.getSource() == FindReplace.this.fieldSearchEditor) {
                        FindReplace.this.doFind();
                        break;
                    }
                    if (keyEvent.getSource() != FindReplace.this.fieldReplaceEditor) break;
                    FindReplace.this.doReplace();
                    break;
                }
                case 27: {
                    FindReplace.this.exit();
                }
            }
        }
    }

}

