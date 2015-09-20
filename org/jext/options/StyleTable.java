/*
 * Decompiled with CFR 0_102.
 */
package org.jext.options;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
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
import org.gjt.sp.jedit.syntax.SyntaxStyle;
import org.jext.GUIUtilities;
import org.jext.Jext;
import org.jext.Utilities;
import org.jext.gui.DisabledCellRenderer;
import org.jext.gui.JextCheckBox;
import org.jext.gui.JextHighlightButton;
import org.jext.options.StylesOptions;

public class StyleTable
extends JTable {
    public StyleTable() {
        this(new StyleTableModel());
    }

    public StyleTable(StyleTableModel styleTableModel) {
        super(styleTableModel);
        this.getTableHeader().setReorderingAllowed(false);
        this.getSelectionModel().addListSelectionListener(new ListHandler());
        this.getColumnModel().getColumn(1).setCellRenderer(new StyleTableModel.StyleRenderer());
        this.getColumnModel().getColumn(0).setCellRenderer(new DisabledCellRenderer());
    }

    private static class StyleEditor
    extends JDialog
    implements ActionListener,
    KeyListener {
        private boolean okClicked;
        private JextCheckBox bold;
        private JextCheckBox italics;
        private JextHighlightButton ok;
        private JextHighlightButton cancel;
        private JButton color;

        StyleEditor(Component component, SyntaxStyle syntaxStyle) {
            super(JOptionPane.getFrameForComponent(component), Jext.getProperty("styleEditor.title"), true);
            this.getContentPane().setLayout(new BorderLayout());
            JPanel jPanel = new JPanel();
            this.italics = new JextCheckBox(Jext.getProperty("styleEditor.italics"));
            jPanel.add(this.italics);
            this.italics.getModel().setSelected(syntaxStyle.isItalic());
            this.bold = new JextCheckBox(Jext.getProperty("styleEditor.bold"));
            jPanel.add(this.bold);
            this.bold.getModel().setSelected(syntaxStyle.isBold());
            jPanel.add(new JLabel(Jext.getProperty("styleEditor.color")));
            this.color = new JButton("    ");
            jPanel.add(this.color);
            this.color.setBackground(syntaxStyle.getColor());
            this.color.setRequestFocusEnabled(false);
            this.color.addActionListener(this);
            this.getContentPane().add("Center", jPanel);
            jPanel = new JPanel();
            this.ok = new JextHighlightButton(Jext.getProperty("general.ok.button"));
            jPanel.add(this.ok);
            this.getRootPane().setDefaultButton(this.ok);
            this.ok.addActionListener(this);
            this.cancel = new JextHighlightButton(Jext.getProperty("general.cancel.button"));
            jPanel.add(this.cancel);
            this.cancel.addActionListener(this);
            this.getContentPane().add("South", jPanel);
            this.addKeyListener(this);
            this.pack();
            this.setDefaultCloseOperation(0);
            Utilities.centerComponent(this);
            this.show();
        }

        public void actionPerformed(ActionEvent actionEvent) {
            Color color;
            Object object = actionEvent.getSource();
            if (object == this.ok) {
                this.okClicked = true;
                this.dispose();
            } else if (object == this.cancel) {
                this.dispose();
            } else if (object == this.color && (color = JColorChooser.showDialog(this, Jext.getProperty("colorChooser.title"), this.color.getBackground())) != null) {
                this.color.setBackground(color);
            }
        }

        public void keyPressed(KeyEvent keyEvent) {
            switch (keyEvent.getKeyCode()) {
                case 10: {
                    this.okClicked = true;
                    this.dispose();
                    keyEvent.consume();
                    break;
                }
                case 27: {
                    this.dispose();
                    keyEvent.consume();
                }
            }
        }

        public void keyReleased(KeyEvent keyEvent) {
        }

        public void keyTyped(KeyEvent keyEvent) {
        }

        public SyntaxStyle getStyle() {
            if (!this.okClicked) {
                return null;
            }
            return new SyntaxStyle(this.color.getBackground(), this.italics.getModel().isSelected(), this.bold.getModel().isSelected());
        }
    }

    public static class StyleTableModel
    extends AbstractTableModel {
        private ArrayList styleChoices = new ArrayList(10);

        public StyleTableModel() {
        }

        public StyleTableModel(Map map) {
            this();
            Iterator iterator = map.entrySet().iterator();
            Map.Entry entry = null;
            while (iterator.hasNext()) {
                entry = iterator.next();
                this.addStyleChoice(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }

        public int getColumnCount() {
            return 2;
        }

        public int getRowCount() {
            return this.styleChoices.size();
        }

        public Object getValueAt(int n, int n2) {
            StyleChoice styleChoice = (StyleChoice)this.styleChoices.get(n);
            switch (n2) {
                case 0: {
                    return styleChoice.label;
                }
                case 1: {
                    return styleChoice.style;
                }
            }
            return null;
        }

        public void setValueAt(Object object, int n, int n2) {
            StyleChoice styleChoice = (StyleChoice)this.styleChoices.get(n);
            if (n2 == 1) {
                styleChoice.style = (SyntaxStyle)object;
            }
            this.fireTableRowsUpdated(n, n);
        }

        public String getColumnName(int n) {
            switch (n) {
                case 0: {
                    return Jext.getProperty("options.styles.object");
                }
                case 1: {
                    return Jext.getProperty("options.styles.style");
                }
            }
            return null;
        }

        public void save() {
            for (int i = 0; i < this.styleChoices.size(); ++i) {
                StyleChoice styleChoice = (StyleChoice)this.styleChoices.get(i);
                Jext.setProperty(styleChoice.property, GUIUtilities.getStyleString(styleChoice.style));
            }
        }

        public void load() {
            for (int i = 0; i < this.styleChoices.size(); ++i) {
                ((StyleChoice)this.styleChoices.get(i)).resetStyle();
            }
            this.fireTableRowsUpdated(0, this.styleChoices.size() - 1);
        }

        public void addStyleChoice(String string, String string2) {
            this.styleChoices.add(new StyleChoice(Jext.getProperty(string), string2));
        }

        private static class StyleRenderer
        extends JLabel
        implements TableCellRenderer {
            public StyleRenderer() {
                this.setOpaque(true);
                this.setBorder(StylesOptions.noFocusBorder);
                this.setText("Hello World");
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
                    SyntaxStyle syntaxStyle = (SyntaxStyle)object;
                    this.setForeground(syntaxStyle.getColor());
                    this.setFont(syntaxStyle.getStyledFont(this.getFont()));
                }
                this.setBorder(bl2 ? UIManager.getBorder("Table.focusCellHighlightBorder") : StylesOptions.noFocusBorder);
                return this;
            }
        }

        private static class StyleChoice {
            String label;
            String property;
            SyntaxStyle style;

            StyleChoice(String string, String string2) {
                this.label = string;
                this.property = string2;
                this.style = GUIUtilities.parseStyle(Jext.getProperty(string2));
            }

            public void resetStyle() {
                this.style = GUIUtilities.parseStyle(Jext.getProperty(this.property));
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
            SyntaxStyle syntaxStyle = new StyleEditor(StyleTable.this, (SyntaxStyle)StyleTable.this.dataModel.getValueAt(StyleTable.this.getSelectedRow(), 1)).getStyle();
            if (syntaxStyle != null) {
                StyleTable.this.dataModel.setValueAt(syntaxStyle, StyleTable.this.getSelectedRow(), 1);
            }
        }
    }

}

