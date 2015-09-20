/*
 * Decompiled with CFR 0_102.
 */
package org.jext.options;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import org.jext.Jext;
import org.jext.Utilities;
import org.jext.console.Console;
import org.jext.gui.AbstractOptionPane;
import org.jext.gui.JextCheckBox;
import org.jext.gui.ModifiedCellRenderer;

public class GeneralOptions
extends AbstractOptionPane
implements ActionListener {
    private JComboBox prompt;
    private JTextField saveDelay;
    private JTextField maxRecent;
    private JTextField promptPattern;
    private JTextField templatesDir;
    private JextCheckBox check;
    private JextCheckBox tips;
    private JextCheckBox console;
    private JextCheckBox fullFileName;
    private JextCheckBox autoSave;
    private JextCheckBox labeledSeparator;
    private JextCheckBox saveSession;
    private JextCheckBox scriptingDebug;
    private JextCheckBox leftPanel;
    private JextCheckBox topPanel;
    private JextCheckBox newWindow;
    private JextCheckBox scrollableTabbedPanes;
    private JextCheckBox jythonMode;

    public GeneralOptions() {
        super("general");
        String[] arrstring = new String[]{"DOS", "Jext", "Linux", "Solaris"};
        this.prompt = new JComboBox<String>(arrstring);
        this.prompt.setRenderer(new ModifiedCellRenderer());
        this.addComponent(Jext.getProperty("options.prompt.label"), this.prompt);
        this.promptPattern = new JTextField(4);
        this.addComponent(Jext.getProperty("options.pattern.label"), this.promptPattern);
        this.promptPattern.setCursor(Cursor.getPredefinedCursor(2));
        this.maxRecent = new JTextField(4);
        this.addComponent(Jext.getProperty("options.maxrecent.label"), this.maxRecent);
        this.maxRecent.setCursor(Cursor.getPredefinedCursor(2));
        this.saveDelay = new JTextField(4);
        this.addComponent(Jext.getProperty("options.delay.label"), this.saveDelay);
        this.saveDelay.setCursor(Cursor.getPredefinedCursor(2));
        this.templatesDir = new JTextField(10);
        this.addComponent(Jext.getProperty("options.templates.label"), this.templatesDir);
        this.templatesDir.setCursor(Cursor.getPredefinedCursor(2));
        this.newWindow = new JextCheckBox(Jext.getProperty("options.newwindow.label"));
        this.addComponent(this.newWindow);
        this.jythonMode = new JextCheckBox(Jext.getProperty("options.jythonmode.label"));
        this.addComponent(this.jythonMode);
        this.scriptingDebug = new JextCheckBox(Jext.getProperty("options.scriptingdebug.label"));
        this.addComponent(this.scriptingDebug);
        this.autoSave = new JextCheckBox(Jext.getProperty("options.autosave.label"));
        this.addComponent(this.autoSave);
        this.saveSession = new JextCheckBox(Jext.getProperty("options.savesession.label"));
        this.addComponent(this.saveSession);
        this.check = new JextCheckBox(Jext.getProperty("options.check.label"));
        this.addComponent(this.check);
        this.console = new JextCheckBox(Jext.getProperty("options.console.label"));
        this.addComponent(this.console);
        this.fullFileName = new JextCheckBox(Jext.getProperty("options.full.filename.label"));
        this.addComponent(this.fullFileName);
        this.tips = new JextCheckBox(Jext.getProperty("options.tips.label"));
        this.addComponent(this.tips);
        this.scrollableTabbedPanes = new JextCheckBox(Jext.getProperty("options.scrollabletabbedpanes.label"));
        this.addComponent(this.scrollableTabbedPanes);
        this.scrollableTabbedPanes.setEnabled(Utilities.JDK_VERSION.charAt(2) >= '4');
        this.leftPanel = new JextCheckBox(Jext.getProperty("options.leftPanel.label"));
        this.addComponent(this.leftPanel);
        this.topPanel = new JextCheckBox(Jext.getProperty("options.topPanel.label"));
        this.addComponent(this.topPanel);
        this.load();
        this.prompt.addActionListener(this);
    }

    public void load() {
        String string = Jext.getProperty("console.prompt");
        this.promptPattern.setText(string);
        this.prompt.setSelectedIndex(-1);
        for (int i = 0; i < Console.DEFAULT_PROMPTS.length; ++i) {
            if (!string.equals(Console.DEFAULT_PROMPTS[i])) continue;
            this.prompt.setSelectedIndex(i);
            break;
        }
        this.maxRecent.setText(Jext.getProperty("max.recent"));
        String string2 = Jext.getProperty("editor.autoSaveDelay");
        if (string2 == null) {
            string2 = "60";
        }
        this.saveDelay.setText(string2);
        this.templatesDir.setText(Jext.getProperty("templates.directory", Jext.JEXT_HOME + File.separator + "templates"));
        this.newWindow.setSelected(Jext.getBooleanProperty("jextLoader.newWindow"));
        this.jythonMode.setSelected(Jext.getBooleanProperty("console.jythonMode"));
        this.scriptingDebug.setSelected(Jext.getBooleanProperty("dawn.scripting.debug"));
        this.autoSave.setSelected(Jext.getBooleanProperty("editor.autoSave"));
        this.saveSession.setSelected(Jext.getBooleanProperty("editor.saveSession"));
        this.check.setSelected(Jext.getBooleanProperty("check"));
        this.console.setSelected(Jext.getBooleanProperty("console.save", "on"));
        this.fullFileName.setSelected(Jext.getBooleanProperty("full.filename", "off"));
        this.tips.setSelected(Jext.getBooleanProperty("tips"));
        this.scrollableTabbedPanes.setSelected(Jext.getBooleanProperty("scrollableTabbedPanes"));
        this.leftPanel.setSelected(Jext.getBooleanProperty("leftPanel.show"));
        this.topPanel.setSelected(Jext.getBooleanProperty("topPanel.show"));
    }

    public Component getComponent() {
        JScrollPane jScrollPane = new JScrollPane(this);
        Dimension dimension = this.getPreferredSize();
        jScrollPane.setPreferredSize(new Dimension(dimension.width, 410));
        jScrollPane.setBorder(LineBorder.createBlackLineBorder());
        return jScrollPane;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        int n;
        if (actionEvent.getSource() == this.prompt && (n = this.prompt.getSelectedIndex()) != -1) {
            this.promptPattern.setText(Console.DEFAULT_PROMPTS[n]);
        }
    }

    public void save() {
        Jext.setProperty("max.recent", this.maxRecent.getText());
        Jext.setProperty("templates.directory", this.templatesDir.getText());
        Jext.setProperty("check", this.check.isSelected() ? "on" : "off");
        Jext.setProperty("tips", this.tips.isSelected() ? "on" : "off");
        String string = this.promptPattern.getText();
        Jext.setProperty("console.prompt", string.length() == 0 ? "> " : string);
        Jext.setProperty("console.save", this.console.isSelected() ? "on" : "off");
        Jext.setProperty("console.jythonMode", this.jythonMode.isSelected() ? "on" : "off");
        Jext.setProperty("full.filename", this.fullFileName.isSelected() ? "on" : "off");
        Jext.setProperty("editor.autoSave", this.autoSave.isSelected() ? "on" : "off");
        Jext.setProperty("editor.autoSaveDelay", this.saveDelay.getText());
        Jext.setProperty("editor.saveSession", this.saveSession.isSelected() ? "on" : "off");
        Jext.setProperty("dawn.scripting.debug", this.scriptingDebug.isSelected() ? "on" : "off");
        Jext.setProperty("leftPanel.show", this.leftPanel.isSelected() ? "on" : "off");
        Jext.setProperty("topPanel.show", this.topPanel.isSelected() ? "on" : "off");
        Jext.setProperty("jextLoader.newWindow", this.newWindow.isSelected() ? "on" : "off");
        Jext.setProperty("scrollableTabbedPanes", this.scrollableTabbedPanes.isSelected() ? "on" : "off");
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.prompt = null;
        this.saveDelay = null;
        this.maxRecent = null;
        this.promptPattern = null;
        this.templatesDir = null;
        this.check = null;
        this.tips = null;
        this.console = null;
        this.fullFileName = null;
        this.autoSave = null;
        this.labeledSeparator = null;
        this.saveSession = null;
        this.scriptingDebug = null;
        this.leftPanel = null;
        this.topPanel = null;
        this.newWindow = null;
        this.scrollableTabbedPanes = null;
    }
}

