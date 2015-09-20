/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jext.Jext;
import org.jext.Utilities;
import org.jext.gui.AbstractDisposer;
import org.jext.gui.JextHighlightButton;
import org.jext.gui.ModifiedCellRenderer;

public class FontSelector
extends JextHighlightButton {
    private String key;
    private String[] styles = new String[]{Jext.getProperty("options.editor.plain"), Jext.getProperty("options.editor.bold"), Jext.getProperty("options.editor.italic"), Jext.getProperty("options.editor.boldItalic")};
    private static final String[] HIDEFONTS = new String[]{".bold", ".italic"};

    public FontSelector(String string) {
        this.key = string;
        this.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                FontSelector.this.changeFont(new SelectorFrame(FontSelector.this.getFont()).getSelectedFont());
            }
        });
        this.load();
    }

    public void load() {
        int n;
        int n2;
        try {
            n = Integer.parseInt(Jext.getProperty(this.key + ".fontSize"));
        }
        catch (Exception var4_2) {
            n = 12;
        }
        try {
            n2 = Integer.parseInt(Jext.getProperty(this.key + ".fontStyle"));
        }
        catch (Exception var4_3) {
            n2 = 0;
        }
        String string = Jext.getProperty(this.key + ".font");
        this.changeFont(new Font(string, n2, n));
    }

    public void save() {
        Font font = this.getFont();
        Jext.setProperty(this.key + ".font", font.getFamily());
        Jext.setProperty(this.key + ".fontSize", String.valueOf(font.getSize()));
        Jext.setProperty(this.key + ".fontStyle", String.valueOf(font.getStyle()));
    }

    private void changeFont(Font font) {
        if (font != null) {
            this.setFont(font);
        }
        this.setFontLabel();
    }

    private void setFontLabel() {
        Font font = this.getFont();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(font.getName()).append(':').append(font.getSize()).append(':');
        stringBuffer.append(this.styles[font.getStyle()]);
        this.setText(stringBuffer.toString());
    }

    public static String[] getAvailableFontFamilyNames() {
        String[] arrstring = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Vector<String> vector = new Vector<String>(arrstring.length);
        for (int i = 0; i < arrstring.length; ++i) {
            int n;
            for (n = 0; n < HIDEFONTS.length; ++n) {
                if (arrstring[i].indexOf(HIDEFONTS[n]) >= 0) break;
            }
            if (n != HIDEFONTS.length) continue;
            vector.addElement(arrstring[i]);
        }
        Object[] arrobject = new String[vector.size()];
        vector.copyInto(arrobject);
        vector = null;
        return arrobject;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.key = null;
        this.styles = null;
    }

    class SelectorFrame
    extends JDialog
    implements ListSelectionListener,
    ActionListener {
        private String[] sizes;
        private boolean fontSelected;
        private JLabel example;
        private JextHighlightButton ok;
        private JextHighlightButton cancel;
        private JList fontsList;
        private JList sizesList;
        private JList stylesList;
        private JTextField fontsField;
        private JTextField sizesField;
        private JTextField stylesField;

        SelectorFrame(Font font) {
            super(JOptionPane.getFrameForComponent(FontSelector.this), Jext.getProperty("font.selector.title"), true);
            this.sizes = new String[]{"9", "10", "12", "14", "16", "18", "24"};
            this.fontSelected = false;
            this.getContentPane().setLayout(new BorderLayout());
            this.fontsField = new JTextField();
            this.fontsList = new JList<String>(FontSelector.getAvailableFontFamilyNames());
            JPanel jPanel = this.createTextFieldAndListPanel("font.selector.family", this.fontsField, this.fontsList);
            this.fontsField.setText(font.getName());
            this.fontsField.setEnabled(false);
            this.fontsList.setCellRenderer(new ModifiedCellRenderer());
            this.sizesField = new JTextField(10);
            this.sizesList = new JList<String>(this.sizes);
            JPanel jPanel2 = this.createTextFieldAndListPanel("font.selector.size", this.sizesField, this.sizesList);
            this.sizesList.setSelectedValue(String.valueOf(font.getSize()), true);
            this.sizesField.setText(String.valueOf(font.getSize()));
            this.sizesList.setCellRenderer(new ModifiedCellRenderer());
            this.stylesField = new JTextField(10);
            this.stylesList = new JList<String>(FontSelector.this.styles);
            JPanel jPanel3 = this.createTextFieldAndListPanel("font.selector.style", this.stylesField, this.stylesList);
            this.stylesList.setSelectedIndex(font.getStyle());
            this.stylesField.setText((String)this.stylesList.getSelectedValue());
            this.stylesField.setEnabled(false);
            this.stylesList.setCellRenderer(new ModifiedCellRenderer());
            this.fontsList.addListSelectionListener(this);
            this.sizesList.addListSelectionListener(this);
            this.stylesList.addListSelectionListener(this);
            JPanel jPanel4 = new JPanel(new GridLayout(1, 3, 6, 6));
            jPanel4.add(jPanel);
            jPanel4.add(jPanel2);
            jPanel4.add(jPanel3);
            JPanel jPanel5 = new JPanel();
            jPanel5.setBorder(new TitledBorder(Jext.getProperty("font.selector.preview")));
            this.example = new JLabel(Jext.getProperty("font.selector.example"));
            jPanel5.add(this.example);
            JPanel jPanel6 = new JPanel();
            this.ok = new JextHighlightButton(Jext.getProperty("general.ok.button"));
            jPanel6.add(this.ok);
            this.ok.setMnemonic(Jext.getProperty("general.ok.mnemonic").charAt(0));
            this.cancel = new JextHighlightButton(Jext.getProperty("general.cancel.button"));
            jPanel6.add(this.cancel);
            this.cancel.setMnemonic(Jext.getProperty("general.cancel.mnemonic").charAt(0));
            this.ok.addActionListener(this);
            this.cancel.addActionListener(this);
            this.preview();
            this.getContentPane().add((Component)jPanel4, "North");
            this.getContentPane().add((Component)jPanel5, "Center");
            this.getContentPane().add((Component)jPanel6, "South");
            Dimension dimension = this.example.getPreferredSize();
            dimension.height = 30;
            this.example.setPreferredSize(dimension);
            this.getRootPane().setDefaultButton(this.ok);
            this.addKeyListener(new AbstractDisposer(this));
            this.setDefaultCloseOperation(2);
            this.fontsList.setSelectedValue(font.getName(), true);
            this.pack();
            Utilities.centerComponent(this);
            this.setResizable(false);
            this.setVisible(true);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            Object object = actionEvent.getSource();
            if (object == this.cancel) {
                this.dispose();
            } else if (object == this.ok) {
                this.fontSelected = true;
                this.dispose();
            }
        }

        public Font getSelectedFont() {
            if (!this.fontSelected) {
                return null;
            }
            return this.buildFont();
        }

        private Font buildFont() {
            int n;
            try {
                n = Integer.parseInt(this.sizesField.getText());
            }
            catch (Exception var2_2) {
                n = 12;
            }
            return new Font(this.fontsField.getText(), this.stylesList.getSelectedIndex(), n);
        }

        private void preview() {
            this.example.setFont(this.buildFont());
        }

        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            String string;
            Object object = listSelectionEvent.getSource();
            if (object == this.fontsList) {
                String string2 = (String)this.fontsList.getSelectedValue();
                if (string2 != null) {
                    this.fontsField.setText(string2);
                }
            } else if (object == this.sizesList) {
                String string3 = (String)this.sizesList.getSelectedValue();
                if (string3 != null) {
                    this.sizesField.setText(string3);
                }
            } else if (object == this.stylesList && (string = (String)this.stylesList.getSelectedValue()) != null) {
                this.stylesField.setText(string);
            }
            this.preview();
        }

        private JPanel createTextFieldAndListPanel(String string, JTextField jTextField, JList jList) {
            GridBagLayout gridBagLayout = new GridBagLayout();
            JPanel jPanel = new JPanel(gridBagLayout);
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridheight = 1;
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.fill = 1;
            gridBagConstraints.weightx = 1.0;
            JLabel jLabel = new JLabel(Jext.getProperty(string));
            gridBagLayout.setConstraints(jLabel, gridBagConstraints);
            jPanel.add(jLabel);
            gridBagConstraints.gridy = 1;
            Component component = Box.createVerticalStrut(6);
            gridBagLayout.setConstraints(component, gridBagConstraints);
            jPanel.add(component);
            gridBagConstraints.gridy = 2;
            gridBagLayout.setConstraints(jTextField, gridBagConstraints);
            jPanel.add(jTextField);
            gridBagConstraints.gridy = 3;
            component = Box.createVerticalStrut(6);
            gridBagLayout.setConstraints(component, gridBagConstraints);
            jPanel.add(component);
            gridBagConstraints.gridy = 4;
            gridBagConstraints.gridheight = 0;
            JScrollPane jScrollPane = new JScrollPane(jList);
            gridBagLayout.setConstraints(jScrollPane, gridBagConstraints);
            jPanel.add(jScrollPane);
            return jPanel;
        }
    }

}

