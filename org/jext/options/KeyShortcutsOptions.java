/*
 * Decompiled with CFR 0_102.
 */
package org.jext.options;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.gjt.sp.jedit.textarea.DefaultInputHandler;
import org.jext.GUIUtilities;
import org.jext.Jext;
import org.jext.Utilities;
import org.jext.gui.AbstractOptionPane;
import org.jext.gui.DisabledCellRenderer;

public class KeyShortcutsOptions
extends AbstractOptionPane {
    private JTable table;
    private String[] actions = new String[GUIUtilities.menuItemsActions.size()];
    private String[] labels = new String[this.actions.length];
    private String[] _keys = new String[this.actions.length];
    private KeysTableModel theTableModel;

    public KeyShortcutsOptions() {
        super("keyShortcuts");
        Enumeration enumeration = GUIUtilities.menuItemsActions.keys();
        int n = 0;
        while (enumeration.hasMoreElements()) {
            this.actions[n] = enumeration.nextElement().toString();
            ++n;
        }
        enumeration = null;
        Hashtable hashtable = GUIUtilities.menuItemsActions;
        for (int i = 0; i < this.actions.length; ++i) {
            this.labels[i] = hashtable.get(this.actions[i]).toString();
        }
        hashtable = null;
        KeyShortcutsOptions.sortStrings(this.labels, this.actions);
        this.setLayout(new GridLayout(1, 1));
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.add("North", new JLabel(Jext.getProperty("options.keyShortcuts.title")));
        jPanel.add("Center", this.createTableScroller());
        this.add(jPanel);
    }

    public void load() {
        this.theTableModel.load();
    }

    public void save() {
        for (int i = 0; i < this.actions.length; ++i) {
            String string = this._keys[i];
            if (string == null || string.length() == 0) continue;
            Jext.setProperty(this.actions[i].toString().concat(".shortcut"), string);
        }
    }

    private JScrollPane createTableScroller() {
        this.theTableModel = new KeysTableModel();
        this.table = new JTable(this.theTableModel);
        this.table.getTableHeader().setReorderingAllowed(false);
        this.table.setCellSelectionEnabled(false);
        this.table.getColumnModel().getColumn(0).setCellRenderer(new DisabledCellRenderer());
        Dimension dimension = this.table.getPreferredSize();
        JScrollPane jScrollPane = new JScrollPane(this.table);
        jScrollPane.setPreferredSize(new Dimension(dimension.width, 250));
        return jScrollPane;
    }

    public static void sortStrings(String[] arrstring, String[] arrstring2) {
        KeyShortcutsOptions.sortStrings(arrstring, arrstring2, 0, arrstring.length - 1);
    }

    public static void sortStrings(String[] arrstring, String[] arrstring2, int n, int n2) {
        int n3 = n;
        int n4 = n2;
        if (n2 > n) {
            String string = arrstring[(n + n2) / 2];
            while (n3 <= n4) {
                while (n3 < n2 && arrstring[n3].compareTo(string) < 0) {
                    ++n3;
                }
                while (n4 > n && arrstring[n4].compareTo(string) > 0) {
                    --n4;
                }
                if (n3 > n4) continue;
                KeyShortcutsOptions.swap(arrstring, n3, n4);
                KeyShortcutsOptions.swap(arrstring2, n3, n4);
                ++n3;
                --n4;
            }
            if (n < n4) {
                KeyShortcutsOptions.sortStrings(arrstring, arrstring2, n, n4);
            }
            if (n3 < n2) {
                KeyShortcutsOptions.sortStrings(arrstring, arrstring2, n3, n2);
            }
        }
    }

    public static void swap(String[] arrstring, int n, int n2) {
        String string = arrstring[n];
        arrstring[n] = arrstring[n2];
        arrstring[n2] = string;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.table = null;
        this.actions = null;
        this.labels = null;
        this._keys = null;
    }

    class KeysTableModel
    extends AbstractTableModel {
        KeysTableModel() {
            this.load();
        }

        void load() {
            for (int i = 0; i < KeyShortcutsOptions.this.actions.length; ++i) {
                String string = KeyShortcutsOptions.this.actions[i].toString();
                if (string == null) continue;
                KeyShortcutsOptions.access$100((KeyShortcutsOptions)KeyShortcutsOptions.this)[i] = Jext.getProperty(string.concat(".shortcut"));
            }
        }

        public int getColumnCount() {
            return 2;
        }

        public int getRowCount() {
            return KeyShortcutsOptions.this._keys.length;
        }

        public Object getValueAt(int n, int n2) {
            String[] arrstring = null;
            if (n2 == 0) {
                arrstring = KeyShortcutsOptions.this.labels;
            } else if (n2 == 1) {
                arrstring = KeyShortcutsOptions.this._keys;
            }
            if (arrstring == null) {
                return null;
            }
            return arrstring[n];
        }

        public boolean isCellEditable(int n, int n2) {
            return n2 == 1;
        }

        public String getColumnName(int n) {
            switch (n) {
                case 0: {
                    return Jext.getProperty("options.keyShortcuts.menu");
                }
                case 1: {
                    return Jext.getProperty("options.keyShortcuts.keys");
                }
            }
            return null;
        }

        public void setValueAt(Object object, int n, int n2) {
            String string = object.toString();
            if (string.trim().length() == 0) {
                Jext.unsetProperty(KeyShortcutsOptions.this.actions[n].toString().concat(".shortcut"));
                KeyShortcutsOptions.access$100((KeyShortcutsOptions)KeyShortcutsOptions.this)[n] = "";
                return;
            }
            boolean bl = false;
            StringTokenizer stringTokenizer = new StringTokenizer(string);
            while (stringTokenizer.hasMoreTokens()) {
                bl = DefaultInputHandler.parseKeyStroke(stringTokenizer.nextToken()) != null;
            }
            if (object == null || string.length() == 0 || bl) {
                int n3;
                boolean bl2 = false;
                if (string.length() != 0) {
                    for (n3 = 0; n3 < KeyShortcutsOptions.this._keys.length; ++n3) {
                        if (!string.equals(KeyShortcutsOptions.this._keys[n3])) continue;
                        bl2 = true;
                        break;
                    }
                }
                if (!bl2 || bl2 && n == n3) {
                    KeyShortcutsOptions.access$100((KeyShortcutsOptions)KeyShortcutsOptions.this)[n] = string;
                } else {
                    Utilities.showError(Jext.getProperty("options.keyShortcuts.errorMessage2"));
                }
            } else {
                Utilities.showError(Jext.getProperty("options.keyShortcuts.errorMessage"));
            }
        }
    }

}

