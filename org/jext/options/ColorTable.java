/*
 * Decompiled with CFR 0_102.
 */
package org.jext.options;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.jext.GUIUtilities;
import org.jext.Jext;
import org.jext.gui.DisabledCellRenderer;
import org.jext.options.StylesOptions;

public class ColorTable
extends JTable {
    public ColorTable() {
        this(new ColorTableModel());
    }

    public ColorTable(ColorTableModel colorTableModel) {
        super(colorTableModel);
        this.getTableHeader().setReorderingAllowed(false);
        this.getSelectionModel().addListSelectionListener(new ListHandler());
        this.getColumnModel().getColumn(1).setCellRenderer(new ColorTableModel.ColorRenderer());
        this.getColumnModel().getColumn(0).setCellRenderer(new DisabledCellRenderer());
    }

    public static class ColorTableModel
    extends AbstractTableModel {
        private ArrayList colorChoices = new ArrayList(24);

        public ColorTableModel() {
        }

        public ColorTableModel(Map map) {
            this();
            Iterator iterator = map.entrySet().iterator();
            Map.Entry entry = null;
            while (iterator.hasNext()) {
                entry = iterator.next();
                this.addColorChoice(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }

        public int getColumnCount() {
            return 2;
        }

        public int getRowCount() {
            return this.colorChoices.size();
        }

        public Object getValueAt(int n, int n2) {
            ColorChoice colorChoice = (ColorChoice)this.colorChoices.get(n);
            switch (n2) {
                case 0: {
                    return colorChoice.label;
                }
                case 1: {
                    return colorChoice.color;
                }
            }
            return null;
        }

        public void setValueAt(Object object, int n, int n2) {
            ColorChoice colorChoice = (ColorChoice)this.colorChoices.get(n);
            if (n2 == 1) {
                colorChoice.color = (Color)object;
            }
            this.fireTableRowsUpdated(n, n);
        }

        public String getColumnName(int n) {
            switch (n) {
                case 0: {
                    return Jext.getProperty("options.styles.object");
                }
                case 1: {
                    return Jext.getProperty("options.styles.color");
                }
            }
            return null;
        }

        public void save() {
            for (int i = 0; i < this.colorChoices.size(); ++i) {
                ColorChoice colorChoice = (ColorChoice)this.colorChoices.get(i);
                Jext.setProperty(colorChoice.property, GUIUtilities.getColorHexString(colorChoice.color));
            }
        }

        public void load() {
            for (int i = 0; i < this.colorChoices.size(); ++i) {
                ((ColorChoice)this.colorChoices.get(i)).resetColor();
            }
            this.fireTableRowsUpdated(0, this.colorChoices.size() - 1);
        }

        public void addColorChoice(String string, String string2) {
            this.colorChoices.add(new ColorChoice(Jext.getProperty(string), string2));
        }

        private static class ColorRenderer
        extends JLabel
        implements TableCellRenderer {
            public ColorRenderer() {
                this.setOpaque(true);
                this.setBorder(StylesOptions.noFocusBorder);
            }

            public Component getTableCellRendererComponent(JTable jTable, Object object, boolean bl, boolean bl2, int n, int n2) {
                if (bl) {
                    this.setBackground(jTable.getSelectionBackground());
                    this.setForeground(jTable.getSelectionForeground());
                } else {
                    this.setBackground(jTable.getBackground());
                    this.setForeground(jTable.getForeground());
                }
                if (object != null) {
                    this.setBackground((Color)object);
                }
                this.setBorder(bl2 ? UIManager.getBorder("Table.focusCellHighlightBorder") : StylesOptions.noFocusBorder);
                return this;
            }
        }

        private static class ColorChoice {
            String label;
            String property;
            Color color;

            ColorChoice(String string, String string2) {
                this.label = string;
                this.property = string2;
                this.color = GUIUtilities.parseColor(Jext.getProperty(string2));
            }

            public void resetColor() {
                this.color = GUIUtilities.parseColor(Jext.getProperty(this.property));
            }
        }

    }

    private class ListHandler
    implements ListSelectionListener {
        private ListHandler() {
        }

        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            if (listSelectionEvent.getValueIsAdjusting()) {
                return;
            }
            Color color = JColorChooser.showDialog(ColorTable.this, Jext.getProperty("colorChooser.title"), (Color)ColorTable.this.dataModel.getValueAt(ColorTable.this.getSelectedRow(), 1));
            if (color != null) {
                ColorTable.this.dataModel.setValueAt(color, ColorTable.this.getSelectedRow(), 1);
            }
        }
    }

}

