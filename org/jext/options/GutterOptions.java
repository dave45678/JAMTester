/*
 * Decompiled with CFR 0_102.
 */
package org.jext.options;

import java.awt.Component;
import javax.swing.ButtonModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import org.jext.Jext;
import org.jext.gui.AbstractOptionPane;
import org.jext.gui.FontSelector;
import org.jext.gui.JextCheckBox;
import org.jext.gui.ModifiedCellRenderer;

public class GutterOptions
extends AbstractOptionPane {
    private FontSelector font;
    private JComboBox numberAlignment;
    private JextCheckBox gutterExpanded;
    private JextCheckBox lineNumbersEnabled;
    private JTextField highlightInterval;
    private JTextField gutterBorderWidth;
    private JTextField gutterWidth = new JTextField();

    public GutterOptions() {
        super("gutter");
        this.addComponent(Jext.getProperty("options.gutter.width"), this.gutterWidth);
        this.gutterBorderWidth = new JTextField();
        this.addComponent(Jext.getProperty("options.gutter.borderWidth"), this.gutterBorderWidth);
        this.highlightInterval = new JTextField();
        this.addComponent(Jext.getProperty("options.gutter.interval"), this.highlightInterval);
        String[] arrstring = new String[]{"Left", "Center", "Right"};
        this.numberAlignment = new JComboBox<String>(arrstring);
        this.numberAlignment.setRenderer(new ModifiedCellRenderer());
        this.addComponent(Jext.getProperty("options.gutter.numberAlignment"), this.numberAlignment);
        this.font = new FontSelector("textArea.gutter");
        this.addComponent(Jext.getProperty("options.gutter.font"), this.font);
        this.gutterExpanded = new JextCheckBox(Jext.getProperty("options.gutter.expanded"));
        this.addComponent(this.gutterExpanded);
        this.lineNumbersEnabled = new JextCheckBox(Jext.getProperty("options.gutter.lineNumbers"));
        this.addComponent(this.lineNumbersEnabled);
        this.load();
    }

    public void load() {
        this.gutterWidth.setText(Jext.getProperty("textArea.gutter.width"));
        this.gutterBorderWidth.setText(Jext.getProperty("textArea.gutter.borderWidth"));
        this.highlightInterval.setText(Jext.getProperty("textArea.gutter.highlightInterval"));
        String string = Jext.getProperty("textArea.gutter.numberAlignment");
        if ("right".equals(string)) {
            this.numberAlignment.setSelectedIndex(2);
        } else if ("center".equals(string)) {
            this.numberAlignment.setSelectedIndex(1);
        } else {
            this.numberAlignment.setSelectedIndex(0);
        }
        this.gutterExpanded.setSelected(!"yes".equals(Jext.getProperty("textArea.gutter.collapsed")));
        this.lineNumbersEnabled.setSelected(!"no".equals(Jext.getProperty("textArea.gutter.lineNumbers")));
        this.font.load();
    }

    public void save() {
        Jext.setProperty("textArea.gutter.collapsed", this.gutterExpanded.getModel().isSelected() ? "no" : "yes");
        Jext.setProperty("textArea.gutter.lineNumbers", this.lineNumbersEnabled.getModel().isSelected() ? "yes" : "no");
        Jext.setProperty("textArea.gutter.width", this.gutterWidth.getText());
        Jext.setProperty("textArea.gutter.borderWidth", this.gutterBorderWidth.getText());
        Jext.setProperty("textArea.gutter.highlightInterval", this.highlightInterval.getText());
        String string = null;
        switch (this.numberAlignment.getSelectedIndex()) {
            case 2: {
                string = "right";
                break;
            }
            case 1: {
                string = "center";
                break;
            }
            default: {
                string = "left";
            }
        }
        Jext.setProperty("textArea.gutter.numberAlignment", string);
        this.font.save();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.font = null;
        this.numberAlignment = null;
        this.gutterExpanded = null;
        this.lineNumbersEnabled = null;
        this.highlightInterval = null;
        this.gutterBorderWidth = null;
        this.gutterWidth = null;
    }
}

