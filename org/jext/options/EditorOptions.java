/*
 * Decompiled with CFR 0_102.
 */
package org.jext.options;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import org.jext.Jext;
import org.jext.gui.AbstractOptionPane;
import org.jext.gui.FontSelector;
import org.jext.gui.JextCheckBox;
import org.jext.gui.ModifiedCellRenderer;

public class EditorOptions
extends AbstractOptionPane {
    private FontSelector fonts;
    private JTextField autoScroll = new JTextField(4);
    private JTextField linesInterval;
    private JTextField wrapGuide;
    private JComboBox newline;
    private JComboBox tabSize;
    private JComboBox modes;
    private JComboBox encoding;
    private JComboBox orientation;
    private JextCheckBox enterIndent;
    private JextCheckBox tabIndent;
    private JextCheckBox softTabs;
    private JextCheckBox blockCaret;
    private JextCheckBox selection;
    private JextCheckBox smartHomeEnd;
    private JextCheckBox splitArea;
    private JextCheckBox fullFileName;
    private JextCheckBox lineHighlight;
    private JextCheckBox eolMarkers;
    private JextCheckBox blinkCaret;
    private JextCheckBox tabStop;
    private JextCheckBox linesIntervalEnabled;
    private JextCheckBox wrapGuideEnabled;
    private JextCheckBox dirDefaultDialog;
    private JextCheckBox overSpace;
    private JextCheckBox addExtraLineFeed;
    private JextCheckBox preserveLineTerm;
    private String[] modeNames;

    public EditorOptions() {
        String[] arrstring;
        super("editor");
        this.addComponent(Jext.getProperty("options.autoscroll.label"), this.autoScroll);
        this.autoScroll.setCursor(Cursor.getPredefinedCursor(2));
        this.linesInterval = new JTextField(4);
        this.addComponent(Jext.getProperty("options.linesinterval.label"), this.linesInterval);
        this.linesInterval.setCursor(Cursor.getPredefinedCursor(2));
        this.wrapGuide = new JTextField(4);
        this.addComponent(Jext.getProperty("options.wrapguide.label"), this.wrapGuide);
        this.wrapGuide.setCursor(Cursor.getPredefinedCursor(2));
        String[] arrstring2 = new String[]{"ASCII", "Cp850", "Cp1252", "iso-8859-1", "iso-8859-2", "KOI8_R", "MacRoman", "UTF8", "UTF16", "Unicode"};
        this.encoding = new JComboBox<String>(arrstring2);
        this.encoding.setRenderer(new ModifiedCellRenderer());
        this.encoding.setEditable(true);
        this.addComponent(Jext.getProperty("options.encoding.label"), this.encoding);
        this.fonts = new FontSelector("editor");
        this.addComponent(Jext.getProperty("options.fonts.label"), this.fonts);
        String[] arrstring3 = new String[]{"2", "4", "8", "16"};
        this.tabSize = new JComboBox<String>(arrstring3);
        this.tabSize.setEditable(true);
        this.addComponent(Jext.getProperty("options.tabsize.label"), this.tabSize);
        this.tabSize.setRenderer(new ModifiedCellRenderer());
        int n = Jext.modes.size();
        String[] arrstring4 = new String[n];
        this.modeNames = new String[n];
        for (int i = 0; i < n; ++i) {
            arrstring = (String[])Jext.modes.get(i);
            this.modeNames[i] = arrstring.getModeName();
            arrstring4[i] = arrstring.getUserModeName();
        }
        this.modes = new JComboBox<String>(arrstring4);
        this.modes.setRenderer(new ModifiedCellRenderer());
        this.addComponent(Jext.getProperty("options.syntax.mode.label"), this.modes);
        String[] arrstring5 = new String[]{"MacOS (\\r)", "Unix (\\n)", "Windows (\\r\\n)"};
        this.newline = new JComboBox<String>(arrstring5);
        this.newline.setRenderer(new ModifiedCellRenderer());
        this.addComponent(Jext.getProperty("options.newline.label"), this.newline);
        arrstring = new String[]{"Vertical", "Horizontal"};
        this.orientation = new JComboBox<String>(arrstring);
        this.orientation.setRenderer(new ModifiedCellRenderer());
        this.addComponent(Jext.getProperty("options.orientation.label"), this.orientation);
        this.linesIntervalEnabled = new JextCheckBox(Jext.getProperty("options.linesintervalenabled.label"));
        this.addComponent(this.linesIntervalEnabled);
        this.wrapGuideEnabled = new JextCheckBox(Jext.getProperty("options.wrapguideenabled.label"));
        this.addComponent(this.wrapGuideEnabled);
        this.splitArea = new JextCheckBox(Jext.getProperty("options.splitarea.label"));
        this.addComponent(this.splitArea);
        this.blockCaret = new JextCheckBox(Jext.getProperty("options.blockcaret.label"));
        this.addComponent(this.blockCaret);
        this.blinkCaret = new JextCheckBox(Jext.getProperty("options.blinkingcaret.label"));
        this.addComponent(this.blinkCaret);
        this.lineHighlight = new JextCheckBox(Jext.getProperty("options.linehighlight.label"));
        this.addComponent(this.lineHighlight);
        this.eolMarkers = new JextCheckBox(Jext.getProperty("options.eolmarkers.label"));
        this.addComponent(this.eolMarkers);
        this.softTabs = new JextCheckBox(Jext.getProperty("options.softtabs.label"));
        this.addComponent(this.softTabs);
        this.tabIndent = new JextCheckBox(Jext.getProperty("options.tabindent.label"));
        this.addComponent(this.tabIndent);
        this.enterIndent = new JextCheckBox(Jext.getProperty("options.enterindent.label"));
        this.addComponent(this.enterIndent);
        this.tabStop = new JextCheckBox(Jext.getProperty("options.tabstop.label"));
        this.addComponent(this.tabStop);
        this.overSpace = new JextCheckBox(Jext.getProperty("options.wordmove.go_over_space.label"));
        this.addComponent(this.overSpace);
        this.smartHomeEnd = new JextCheckBox(Jext.getProperty("options.smartHomeEnd.label"));
        this.addComponent(this.smartHomeEnd);
        this.dirDefaultDialog = new JextCheckBox(Jext.getProperty("options.defaultdirloaddialog.label"));
        this.addComponent(this.dirDefaultDialog);
        this.selection = new JextCheckBox(Jext.getProperty("options.selection.label"));
        this.addComponent(this.selection);
        this.addExtraLineFeed = new JextCheckBox(Jext.getProperty("options.extra_line_feed.label"));
        this.addComponent(this.addExtraLineFeed);
        this.preserveLineTerm = new JextCheckBox(Jext.getProperty("options.line_end_preserved.label"));
        this.addComponent(this.preserveLineTerm);
        this.load();
    }

    public void load() {
        int n;
        int n2;
        this.autoScroll.setText(Jext.getProperty("editor.autoScroll"));
        this.linesInterval.setText(Jext.getProperty("editor.linesInterval"));
        this.wrapGuide.setText(Jext.getProperty("editor.wrapGuideOffset"));
        this.encoding.setSelectedItem(Jext.getProperty("editor.encoding", System.getProperty("file.encoding")));
        this.tabSize.setSelectedItem(Jext.getProperty("editor.tabSize"));
        String string = Jext.getProperty("editor.colorize.mode");
        for (n2 = 0; n2 < this.modeNames.length; ++n2) {
            if (string.equals(this.modeNames[n2])) break;
        }
        this.modes.setSelectedIndex(n2);
        String string2 = Jext.getProperty("editor.newLine");
        for (n = 0; n < Jext.NEW_LINE.length; ++n) {
            if (Jext.NEW_LINE[n].equals(string2)) break;
        }
        this.newline.setSelectedIndex(n);
        this.orientation.setSelectedItem(Jext.getProperty("editor.splitted.orientation"));
        this.linesIntervalEnabled.setSelected(Jext.getBooleanProperty("editor.linesIntervalEnabled"));
        this.wrapGuideEnabled.setSelected(Jext.getBooleanProperty("editor.wrapGuideEnabled"));
        this.splitArea.setSelected(Jext.getBooleanProperty("editor.splitted"));
        this.blockCaret.setSelected(Jext.getBooleanProperty("editor.blockCaret"));
        this.blinkCaret.setSelected(Jext.getBooleanProperty("editor.blinkingCaret"));
        this.lineHighlight.setSelected(Jext.getBooleanProperty("editor.lineHighlight"));
        this.eolMarkers.setSelected(Jext.getBooleanProperty("editor.eolMarkers"));
        this.tabIndent.setSelected(Jext.getBooleanProperty("editor.tabIndent"));
        this.enterIndent.setSelected(Jext.getBooleanProperty("editor.enterIndent"));
        this.softTabs.setSelected(Jext.getBooleanProperty("editor.softTab"));
        this.tabStop.setSelected(Jext.getBooleanProperty("editor.tabStop"));
        this.smartHomeEnd.setSelected(Jext.getBooleanProperty("editor.smartHomeEnd"));
        this.dirDefaultDialog.setSelected(Jext.getBooleanProperty("editor.dirDefaultDialog"));
        this.selection.setSelected(Jext.getBooleanProperty("use.selection"));
        this.overSpace.setSelected(Jext.getBooleanProperty("editor.wordmove.go_over_space"));
        this.addExtraLineFeed.setSelected(Jext.getBooleanProperty("editor.extra_line_feed"));
        this.preserveLineTerm.setSelected(Jext.getBooleanProperty("editor.line_end.preserve"));
        this.fonts.load();
    }

    public Component getComponent() {
        JScrollPane jScrollPane = new JScrollPane(this);
        Dimension dimension = this.getPreferredSize();
        jScrollPane.setPreferredSize(new Dimension(dimension.width, 410));
        jScrollPane.setBorder(LineBorder.createBlackLineBorder());
        return jScrollPane;
    }

    public void save() {
        Jext.setProperty("editor.colorize.mode", this.modeNames[this.modes.getSelectedIndex()]);
        Jext.setProperty("editor.tabIndent", this.tabIndent.isSelected() ? "on" : "off");
        Jext.setProperty("editor.enterIndent", this.enterIndent.isSelected() ? "on" : "off");
        Jext.setProperty("editor.softTab", this.softTabs.isSelected() ? "on" : "off");
        Jext.setProperty("editor.tabStop", this.tabStop.isSelected() ? "on" : "off");
        Jext.setProperty("editor.tabSize", (String)this.tabSize.getSelectedItem());
        Jext.setProperty("editor.encoding", (String)this.encoding.getSelectedItem());
        Jext.setProperty("editor.blockCaret", this.blockCaret.isSelected() ? "on" : "off");
        Jext.setProperty("editor.blinkingCaret", this.blinkCaret.isSelected() ? "on" : "off");
        Jext.setProperty("editor.lineHighlight", this.lineHighlight.isSelected() ? "on" : "off");
        Jext.setProperty("editor.newLine", Jext.NEW_LINE[this.newline.getSelectedIndex()]);
        Jext.setProperty("editor.eolMarkers", this.eolMarkers.isSelected() ? "on" : "off");
        Jext.setProperty("editor.smartHomeEnd", this.smartHomeEnd.isSelected() ? "on" : "off");
        Jext.setProperty("editor.dirDefaultDialog", this.dirDefaultDialog.isSelected() ? "on" : "off");
        Jext.setProperty("editor.splitted", this.splitArea.isSelected() ? "on" : "off");
        Jext.setProperty("editor.autoScroll", this.autoScroll.getText());
        Jext.setProperty("editor.linesInterval", this.linesInterval.getText());
        Jext.setProperty("editor.linesIntervalEnabled", this.linesIntervalEnabled.isSelected() ? "on" : "off");
        Jext.setProperty("editor.wrapGuideOffset", this.wrapGuide.getText());
        Jext.setProperty("editor.wrapGuideEnabled", this.wrapGuideEnabled.isSelected() ? "on" : "off");
        Jext.setProperty("editor.splitted.orientation", (String)this.orientation.getSelectedItem());
        Jext.setProperty("use.selection", this.selection.isSelected() ? "on" : "off");
        Jext.setProperty("editor.wordmove.go_over_space", this.overSpace.isSelected() ? "on" : "off");
        Jext.setProperty("editor.extra_line_feed", this.addExtraLineFeed.isSelected() ? "on" : "off");
        Jext.setProperty("editor.line_end.preserve", this.preserveLineTerm.isSelected() ? "on" : "off");
        this.fonts.save();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.fonts = null;
        this.autoScroll = null;
        this.linesInterval = null;
        this.wrapGuide = null;
        this.newline = null;
        this.tabSize = null;
        this.modes = null;
        this.encoding = null;
        this.orientation = null;
        this.enterIndent = null;
        this.tabIndent = null;
        this.softTabs = null;
        this.blockCaret = null;
        this.selection = null;
        this.smartHomeEnd = null;
        this.splitArea = null;
        this.fullFileName = null;
        this.lineHighlight = null;
        this.eolMarkers = null;
        this.blinkCaret = null;
        this.tabStop = null;
        this.linesIntervalEnabled = null;
        this.wrapGuideEnabled = null;
        this.dirDefaultDialog = null;
        this.overSpace = null;
        this.modeNames = null;
        this.addExtraLineFeed = null;
        this.preserveLineTerm = null;
    }
}

