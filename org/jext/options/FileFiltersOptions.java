/*
 * Decompiled with CFR 0_102.
 */
package org.jext.options;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
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
import org.jext.Jext;
import org.jext.Mode;
import org.jext.gui.AbstractOptionPane;
import org.jext.gui.DisabledCellRenderer;

public class FileFiltersOptions
extends AbstractOptionPane {
    private JTable filtersTable;
    private ArrayList filters = new ArrayList(Jext.modes.size());
    private FiltersTableModel theTableModel;

    public FileFiltersOptions() {
        super("fileFilters");
        this.setLayout(new GridLayout(1, 1));
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.add("North", new JLabel(Jext.getProperty("options.fileFilters.title")));
        jPanel.add("Center", this.createTableScroller());
        this.add(jPanel);
    }

    public void save() {
        for (int i = 0; i < this.filters.size(); ++i) {
            FileFilter fileFilter = (FileFilter)this.filters.get(i);
            Jext.setProperty("mode." + fileFilter.getMode() + ".fileFilter", fileFilter.getFilter());
        }
    }

    public void load() {
        this.theTableModel.reload();
    }

    private JScrollPane createTableScroller() {
        this.theTableModel = new FiltersTableModel();
        this.filtersTable = new JTable(this.theTableModel);
        this.filtersTable.getTableHeader().setReorderingAllowed(false);
        this.filtersTable.setCellSelectionEnabled(false);
        this.filtersTable.getColumnModel().getColumn(0).setCellRenderer(new DisabledCellRenderer());
        Dimension dimension = this.filtersTable.getPreferredSize();
        JScrollPane jScrollPane = new JScrollPane(this.filtersTable);
        jScrollPane.setPreferredSize(new Dimension(dimension.width, 250));
        return jScrollPane;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.filtersTable = null;
        this.filters.clear();
        this.filters = null;
    }

    class FileFilter {
        private String _mode;
        private String _name;
        private String _filter;

        FileFilter(String string, String string2, String string3) {
            this._mode = string;
            this._name = string2;
            this._filter = string3;
        }

        public String getMode() {
            return this._mode;
        }

        public String getName() {
            return this._name;
        }

        public String getFilter() {
            return this._filter;
        }

        public void setFilter(String string) {
            this._filter = string;
        }
    }

    class FiltersTableModel
    extends AbstractTableModel {
        FiltersTableModel() {
            ArrayList arrayList = Jext.modes;
            for (int i = 0; i < arrayList.size(); ++i) {
                Mode mode = (Mode)arrayList.get(i);
                String string = mode.getModeName();
                if (string.equals("plain")) continue;
                FileFiltersOptions.this.filters.add(new FileFilter(string, mode.getUserModeName(), Jext.getProperty("mode." + string + ".fileFilter")));
            }
        }

        void reload() {
            ArrayList arrayList = Jext.modes;
            int n = 0;
            for (int i = 0; i < arrayList.size(); ++i) {
                String string = ((Mode)arrayList.get(i)).getModeName();
                if (!string.equals("plain")) {
                    ((FileFilter)FileFiltersOptions.this.filters.get(i - n)).setFilter(Jext.getProperty("mode." + string + ".fileFilter"));
                    continue;
                }
                n = 1;
            }
        }

        public int getColumnCount() {
            return 2;
        }

        public int getRowCount() {
            return FileFiltersOptions.this.filters.size();
        }

        public Object getValueAt(int n, int n2) {
            FileFilter fileFilter = (FileFilter)FileFiltersOptions.this.filters.get(n);
            return n2 == 0 ? fileFilter.getName() : fileFilter.getFilter();
        }

        public boolean isCellEditable(int n, int n2) {
            return n2 == 1;
        }

        public String getColumnName(int n) {
            switch (n) {
                case 0: {
                    return Jext.getProperty("options.fileFilters.modeName");
                }
                case 1: {
                    return Jext.getProperty("options.fileFilters.filter");
                }
            }
            return null;
        }

        public void setValueAt(Object object, int n, int n2) {
            ((FileFilter)FileFiltersOptions.this.filters.get(n)).setFilter((String)object);
        }
    }

}

