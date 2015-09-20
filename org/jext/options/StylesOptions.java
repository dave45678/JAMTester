/*
 * Decompiled with CFR 0_102.
 */
package org.jext.options;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import org.jext.Jext;
import org.jext.gui.AbstractOptionPane;
import org.jext.options.ColorTable;
import org.jext.options.StyleTable;

public class StylesOptions
extends AbstractOptionPane {
    public static final EmptyBorder noFocusBorder = new EmptyBorder(1, 1, 1, 1);
    private ColorTable.ColorTableModel colorModel;
    private ColorTable colorTable;
    private StyleTable.StyleTableModel styleModel;
    private StyleTable styleTable;

    public StylesOptions() {
        super("styles");
        this.setLayout(new GridLayout(2, 1));
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.add("North", new JLabel(Jext.getProperty("options.styles.colors")));
        jPanel.add("Center", this.createColorTableScroller());
        this.add(jPanel);
        this.setLayout(new GridLayout(2, 1));
        jPanel = new JPanel(new BorderLayout());
        jPanel.add("North", new JLabel(Jext.getProperty("options.styles.styles")));
        jPanel.add("Center", this.createStyleTableScroller());
        this.add(jPanel);
    }

    public void save() {
        this.colorModel.save();
        this.styleModel.save();
    }

    public void load() {
        this.colorModel.load();
        this.styleModel.load();
    }

    private JScrollPane createColorTableScroller() {
        this.colorModel = this.createColorTableModel();
        this.colorTable = new ColorTable(this.colorModel);
        Dimension dimension = this.colorTable.getPreferredSize();
        dimension.height = Math.min(dimension.height, 100);
        JScrollPane jScrollPane = new JScrollPane(this.colorTable);
        jScrollPane.setPreferredSize(dimension);
        return jScrollPane;
    }

    private ColorTable.ColorTableModel createColorTableModel() {
        ColorTable.ColorTableModel colorTableModel = new ColorTable.ColorTableModel();
        colorTableModel.addColorChoice("options.styles.bgColor", "editor.bgColor");
        colorTableModel.addColorChoice("options.styles.fgColor", "editor.fgColor");
        colorTableModel.addColorChoice("options.styles.caretColor", "editor.caretColor");
        colorTableModel.addColorChoice("options.styles.selectionColor", "editor.selectionColor");
        colorTableModel.addColorChoice("options.styles.highlightColor", "editor.highlightColor");
        colorTableModel.addColorChoice("options.styles.lineHighlightColor", "editor.lineHighlightColor");
        colorTableModel.addColorChoice("options.styles.linesHighlightColor", "editor.linesHighlightColor");
        colorTableModel.addColorChoice("options.styles.bracketHighlightColor", "editor.bracketHighlightColor");
        colorTableModel.addColorChoice("options.styles.wrapGuideColor", "editor.wrapGuideColor");
        colorTableModel.addColorChoice("options.styles.eolMarkerColor", "editor.eolMarkerColor");
        colorTableModel.addColorChoice("options.styles.gutterBgColor", "textArea.gutter.bgColor");
        colorTableModel.addColorChoice("options.styles.gutterFgColor", "textArea.gutter.fgColor");
        colorTableModel.addColorChoice("options.styles.gutterHighlightColor", "textArea.gutter.highlightColor");
        colorTableModel.addColorChoice("options.styles.gutterBorderColor", "textArea.gutter.borderColor");
        colorTableModel.addColorChoice("options.styles.gutterAnchorMarkColor", "textArea.gutter.anchorMarkColor");
        colorTableModel.addColorChoice("options.styles.gutterCaretMarkColor", "textArea.gutter.caretMarkColor");
        colorTableModel.addColorChoice("options.styles.gutterSelectionMarkColor", "textArea.gutter.selectionMarkColor");
        colorTableModel.addColorChoice("options.styles.consoleBgColor", "console.bgColor");
        colorTableModel.addColorChoice("options.styles.consoleOutputColor", "console.outputColor");
        colorTableModel.addColorChoice("options.styles.consolePromptColor", "console.promptColor");
        colorTableModel.addColorChoice("options.styles.consoleErrorColor", "console.errorColor");
        colorTableModel.addColorChoice("options.styles.consoleInfoColor", "console.infoColor");
        colorTableModel.addColorChoice("options.styles.consoleSelectionColor", "console.selectionColor");
        colorTableModel.addColorChoice("options.styles.vfSelectionColor", "vf.selectionColor");
        colorTableModel.addColorChoice("options.styles.buttonsHighlightColor", "buttons.highlightColor");
        return colorTableModel;
    }

    private JScrollPane createStyleTableScroller() {
        this.styleModel = this.createStyleTableModel();
        this.styleTable = new StyleTable(this.styleModel);
        Dimension dimension = this.styleTable.getPreferredSize();
        dimension.height = Math.min(dimension.height, 100);
        JScrollPane jScrollPane = new JScrollPane(this.styleTable);
        jScrollPane.setPreferredSize(dimension);
        return jScrollPane;
    }

    private StyleTable.StyleTableModel createStyleTableModel() {
        StyleTable.StyleTableModel styleTableModel = new StyleTable.StyleTableModel();
        styleTableModel.addStyleChoice("options.styles.comment1Style", "editor.style.comment1");
        styleTableModel.addStyleChoice("options.styles.comment2Style", "editor.style.comment2");
        styleTableModel.addStyleChoice("options.styles.literal1Style", "editor.style.literal1");
        styleTableModel.addStyleChoice("options.styles.literal2Style", "editor.style.literal2");
        styleTableModel.addStyleChoice("options.styles.labelStyle", "editor.style.label");
        styleTableModel.addStyleChoice("options.styles.keyword1Style", "editor.style.keyword1");
        styleTableModel.addStyleChoice("options.styles.keyword2Style", "editor.style.keyword2");
        styleTableModel.addStyleChoice("options.styles.keyword3Style", "editor.style.keyword3");
        styleTableModel.addStyleChoice("options.styles.operatorStyle", "editor.style.operator");
        styleTableModel.addStyleChoice("options.styles.invalidStyle", "editor.style.invalid");
        styleTableModel.addStyleChoice("options.styles.methodStyle", "editor.style.method");
        return styleTableModel;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.colorModel = null;
        this.colorTable = null;
        this.styleModel = null;
        this.styleTable = null;
    }
}

