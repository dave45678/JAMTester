/*
 * Decompiled with CFR 0_102.
 */
package org.jext;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import org.gjt.sp.jedit.gui.KeyEventWorkaround;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.gjt.sp.jedit.syntax.SyntaxStyle;
import org.gjt.sp.jedit.textarea.DefaultInputHandler;
import org.gjt.sp.jedit.textarea.Gutter;
import org.gjt.sp.jedit.textarea.InputHandler;
import org.gjt.sp.jedit.textarea.TextAreaPainter;
import org.jext.GUIUtilities;
import org.jext.JARClassLoader;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTabbedPane;
import org.jext.JextTextArea;
import org.jext.Mode;
import org.jext.ModeFileFilter;
import org.jext.OneClickAction;
import org.jext.Plugin;
import org.jext.RegisterablePlugin;
import org.jext.Utilities;
import org.jext.console.Console;
import org.jext.event.JextEvent;
import org.jext.event.JextListener;
import org.jext.gui.JextButton;
import org.jext.gui.JextHighlightButton;
import org.jext.gui.JextToggleButton;
import org.jext.gui.VoidComponent;
import org.jext.menus.JextMenuBar;
import org.jext.menus.JextRecentMenu;
import org.jext.misc.AutoSave;
import org.jext.misc.FindAccessory;
import org.jext.misc.SaveDialog;
import org.jext.misc.VirtualFolders;
import org.jext.misc.Workspaces;
import org.jext.project.DefaultProjectManagement;
import org.jext.project.Project;
import org.jext.project.ProjectManagement;
import org.jext.project.ProjectManager;
import org.jext.scripting.dawn.DawnLogWindow;
import org.jext.scripting.python.PythonLogWindow;
import org.jext.toolbar.JextToolBar;
import org.jext.xinsert.XTree;
import org.jext.xml.XBarReader;
import org.jext.xml.XMenuReader;

public class JextFrame
extends JFrame {
    private JextToolBar toolBar;
    private JMenu pluginsMenu;
    private JextRecentMenu menuRecent;
    private int _dividerSize;
    private XTree xtree;
    private Console console;
    private DawnLogWindow dawnLogWindow;
    private PythonLogWindow pythonLogWindow;
    private Workspaces workspaces;
    private VirtualFolders virtualFolders;
    private AutoSave auto;
    private JFileChooser chooser;
    private FindAccessory accessory;
    private JPanel centerPane;
    private JextTabbedPane textAreasPane;
    private JSplitPane split = new JSplitPane();
    private JTabbedPane hTabbedPane;
    private JTabbedPane vTabbedPane;
    private JSplitPane splitter = new JSplitPane(0);
    private JSplitPane textAreaSplitter = new JSplitPane(0);
    private JextTextArea splittedTextArea;
    private JLabel message = new JLabel();
    private JLabel status = new JLabel("v3.2 <Qu\u00e9bec> - (C)1999-2001 Romain Guy - www.jext.org");
    private int waitCount;
    private int batchMode;
    private ArrayList jextListeners = new ArrayList();
    private ArrayList transientItems = new ArrayList();
    private boolean transientSwitch;
    private KeyListener keyEventInterceptor;
    private InputHandler inputHandler;
    private ProjectManager currentProjectMgr;
    private HashMap projectMgmts;
    private ProjectManagement defaultProjectMgmt;

    public JextTabbedPane getTabbedPane() {
        return this.textAreasPane;
    }

    public JTabbedPane getVerticalTabbedPane() {
        return this.vTabbedPane;
    }

    public JTabbedPane getHorizontalTabbedPane() {
        return this.hTabbedPane;
    }

    public XTree getXTree() {
        return this.xtree;
    }

    public VirtualFolders getVirtualFolders() {
        return this.virtualFolders;
    }

    public Workspaces getWorkspaces() {
        return this.workspaces;
    }

    public Console getConsole() {
        if (this.console == null) {
            Console console = new Console(this);
            console.setPromptPattern(Jext.getProperty("console.prompt"));
            console.displayPrompt();
            return console;
        }
        return this.console;
    }

    public DawnLogWindow getDawnLogWindow() {
        if (this.dawnLogWindow == null) {
            this.dawnLogWindow = new DawnLogWindow(this);
        }
        return this.dawnLogWindow;
    }

    public PythonLogWindow getPythonLogWindow() {
        if (this.pythonLogWindow == null) {
            this.pythonLogWindow = new PythonLogWindow(this);
        }
        return this.pythonLogWindow;
    }

    public JFileChooser getFileChooser(int n) {
        if (this.chooser == null) {
            this.chooser = new JFileChooser();
            this.accessory = new FindAccessory(this.chooser);
        }
        switch (n) {
            default: {
                this.chooser.setDialogType(0);
                this.chooser.setDialogTitle(Jext.getProperty("filechooser.open.title"));
                if (this.chooser.getAccessory() == null) {
                    this.chooser.setAccessory(this.accessory);
                }
                if (this.chooser.getChoosableFileFilters().length > 1) break;
                FileFilter fileFilter = null;
                String string = this.getTextArea().getColorizingMode();
                for (int i = 0; i < Jext.modesFileFilters.size(); ++i) {
                    ModeFileFilter modeFileFilter = (ModeFileFilter)Jext.modesFileFilters.get(i);
                    this.chooser.addChoosableFileFilter(modeFileFilter);
                    if (fileFilter != null || !string.equals(modeFileFilter.getModeName())) continue;
                    fileFilter = modeFileFilter;
                }
                this.chooser.setFileFilter(fileFilter == null ? this.chooser.getAcceptAllFileFilter() : fileFilter);
                break;
            }
            case 2: {
                this.chooser.setDialogType(0);
                this.chooser.setDialogTitle(Jext.getProperty("filechooser.script.title"));
                this.chooser.setAccessory(this.accessory);
                this.chooser.resetChoosableFileFilters();
                break;
            }
            case 1: {
                this.chooser.setDialogType(1);
                this.chooser.setDialogTitle(Jext.getProperty("filechooser.save.title"));
                this.chooser.setAccessory(null);
                this.chooser.resetChoosableFileFilters();
            }
        }
        this.chooser.setSelectedFile(new File(""));
        this.chooser.rescanCurrentDirectory();
        return this.chooser;
    }

    public void setBatchMode(boolean bl) {
        if (bl && this.batchMode == 0) {
            this.fireJextEvent(20);
        } else if (!(bl || this.batchMode != 1)) {
            this.fireJextEvent(21);
        }
        this.batchMode+=bl ? 1 : -1;
        if (this.batchMode < 0) {
            this.batchMode = 0;
        }
    }

    public boolean getBatchMode() {
        return this.batchMode > 0;
    }

    public final KeyListener getKeyEventInterceptor() {
        return this.keyEventInterceptor;
    }

    public void setKeyEventInterceptor(KeyListener keyListener) {
        this.keyEventInterceptor = keyListener;
    }

    public void freeze() {
        this.transientSwitch = true;
        this.getJextToolBar().freeze();
    }

    public void itemAdded(Component component) {
        if (this.transientSwitch) {
            this.transientItems.add(component);
        }
    }

    public void reset() {
        this.getJextToolBar().reset();
        for (int i = 0; i < this.transientItems.size(); ++i) {
            Container container;
            Component component = (Component)this.transientItems.get(i);
            if (component == null || (container = component.getParent()) == null) continue;
            container.remove(component);
        }
        if (this.getJextMenuBar() != null) {
            this.getJextMenuBar().reset();
        }
    }

    public void setPluginsMenu(JMenu jMenu) {
        this.pluginsMenu = jMenu;
    }

    public void fireJextEvent(int n) {
        JextEvent jextEvent = new JextEvent(this, n);
        Iterator iterator = this.jextListeners.iterator();
        while (iterator.hasNext()) {
            ((JextListener)iterator.next()).jextEventFired(jextEvent);
        }
    }

    public void fireJextEvent(JextTextArea jextTextArea, int n) {
        JextEvent jextEvent = new JextEvent(this, jextTextArea, n);
        Iterator iterator = this.jextListeners.iterator();
        while (iterator.hasNext()) {
            try {
                ((JextListener)iterator.next()).jextEventFired(jextEvent);
            }
            catch (Throwable var5_5) {
                var5_5.printStackTrace();
            }
        }
    }

    public void removeAllJextListeners() {
        this.jextListeners.clear();
    }

    public void addJextListener(JextListener jextListener) {
        this.jextListeners.add(jextListener);
    }

    public void removeJextListener(JextListener jextListener) {
        this.jextListeners.remove(jextListener);
    }

    public void loadProperties() {
        this.loadProperties(true);
    }

    public void loadProperties(boolean bl) {
        if (Jext.getBooleanProperty("editor.autoSave")) {
            this.startAutoSave();
        } else {
            this.stopAutoSave();
        }
        if (Jext.getBooleanProperty("tips")) {
            String string;
            try {
                string = "Tip: " + Jext.getProperty(new StringBuffer().append("tip.").append(Math.abs(new Random().nextInt()) % Integer.parseInt(Jext.getProperty("tip.count"))).toString());
            }
            catch (Exception var3_3) {
                string = this.status.getText();
            }
            this.status.setText("" + ' ' + string);
        }
        if (bl) {
            this.triggerTabbedPanes();
        }
        this.splitEditor();
        this.loadButtonsProperties();
        this.loadConsoleProperties();
        this.loadTextAreaProperties();
        this.getTextArea().setParentTitle();
        this.fireJextEvent(0);
    }

    public void loadButtonsProperties() {
        this.toolBar.setGrayed(Jext.getBooleanProperty("toolbar.gray"));
        this.toolBar.setVisible(Jext.getBooleanProperty("toolbar"));
        JextButton.setHighlightColor(GUIUtilities.parseColor(Jext.getProperty("buttons.highlightColor")));
        JextHighlightButton.setHighlightColor(GUIUtilities.parseColor(Jext.getProperty("buttons.highlightColor")));
        JextToggleButton.setHighlightColor(GUIUtilities.parseColor(Jext.getProperty("buttons.highlightColor")));
    }

    public void triggerTabbedPanes() {
        boolean bl;
        boolean bl2 = Jext.getBooleanProperty("leftPanel.show");
        if (bl2 && this.vTabbedPane.getTabCount() > 0) {
            if (this.split.getDividerSize() == 0) {
                this.split.setDividerSize(this._dividerSize);
                this.split.setLeftComponent(this.vTabbedPane);
                this.split.resetToPreferredSizes();
            }
        } else if (this.split.getDividerSize() != 0) {
            this.split.setDividerSize(0);
            this.split.setLeftComponent(new VoidComponent());
            this.split.resetToPreferredSizes();
        }
        if ((bl = Jext.getBooleanProperty("topPanel.show")) && this.hTabbedPane.getTabCount() > 0) {
            if (this.splitter.getDividerSize() == 0) {
                this.splitter.setDividerSize(this._dividerSize);
                this.splitter.setTopComponent(this.hTabbedPane);
                this.splitter.resetToPreferredSizes();
                this.splitter.setBottomComponent(this.split);
                this.centerPane.add("Center", this.splitter);
                this.centerPane.validate();
                this.splitter.setVisible(true);
            }
        } else {
            if (this.splitter.getDividerSize() != 0) {
                this.splitter.setDividerSize(0);
                this.splitter.setTopComponent(new VoidComponent());
                this.splitter.resetToPreferredSizes();
                this.centerPane.remove(this.splitter);
                this.centerPane.add("Center", this.split);
                this.centerPane.validate();
            }
            bl = false;
        }
        if (bl2) {
            this.split.setBorder(bl ? null : new MatteBorder(1, 0, 0, 0, Color.gray));
        } else {
            this.split.setBorder(null);
        }
    }

    public void splitEditor() {
        if (Jext.getBooleanProperty("editor.splitted")) {
            if (this.split.getRightComponent() != this.textAreaSplitter) {
                this.split.remove(this.textAreasPane);
                this.textAreaSplitter.setTopComponent(this.textAreasPane);
                this.split.setRightComponent(this.textAreaSplitter);
                this.textAreaSplitter.setDividerLocation(0.5);
            }
            this.textAreaSplitter.setOrientation("Horizontal".equals(Jext.getProperty("editor.splitted.orientation")) ? 1 : 0);
            this.updateSplittedTextArea(this.getTextArea());
        } else {
            this.textAreaSplitter.remove(this.textAreasPane);
            this.split.setRightComponent(this.textAreasPane);
            JextTextArea jextTextArea = this.getTextArea();
            if (jextTextArea != null) {
                jextTextArea.grabFocus();
                jextTextArea.requestFocus();
            }
        }
    }

    public void loadConsoleProperties() {
        if (this.console != null) {
            String string = Jext.getProperty("console.prompt");
            if (!(string == null || string.equals(this.console.getPromptPattern()))) {
                this.console.setPromptPattern(string);
                this.console.displayPrompt();
            }
            this.console.setErrorColor(GUIUtilities.parseColor(Jext.getProperty("console.errorColor")));
            this.console.setPromptColor(GUIUtilities.parseColor(Jext.getProperty("console.promptColor")));
            this.console.setOutputColor(GUIUtilities.parseColor(Jext.getProperty("console.outputColor")));
            this.console.setInfoColor(GUIUtilities.parseColor(Jext.getProperty("console.infoColor")));
            this.console.setBgColor(GUIUtilities.parseColor(Jext.getProperty("console.bgColor")));
            this.console.setSelectionColor(GUIUtilities.parseColor(Jext.getProperty("console.selectionColor")));
        }
    }

    public void loadTextAreaProperties() {
        this.loadTextArea(this.splittedTextArea);
        this.splittedTextArea.setElectricScroll(0);
        this.workspaces.loadTextAreas();
    }

    public void loadTextArea(JextTextArea jextTextArea) {
        try {
            jextTextArea.setTabSize(Integer.parseInt(Jext.getProperty("editor.tabSize")));
        }
        catch (NumberFormatException var2_2) {
            jextTextArea.setTabSize(8);
            Jext.setProperty("editor.tabSize", "8");
        }
        try {
            jextTextArea.setElectricScroll(Integer.parseInt(Jext.getProperty("editor.autoScroll")));
        }
        catch (NumberFormatException var2_3) {
            jextTextArea.setElectricScroll(0);
        }
        String string = Jext.getProperty("editor.newLine");
        if (string == null) {
            Jext.setProperty("editor.newLine", System.getProperty("line.separator"));
        }
        try {
            jextTextArea.setFontSize(Integer.parseInt(Jext.getProperty("editor.fontSize")));
        }
        catch (NumberFormatException var3_5) {
            jextTextArea.setFontSize(12);
            Jext.setProperty("editor.fontSize", "12");
        }
        try {
            jextTextArea.setFontStyle(Integer.parseInt(Jext.getProperty("editor.fontStyle")));
        }
        catch (NumberFormatException var3_6) {
            jextTextArea.setFontStyle(0);
            Jext.setProperty("editor.fontStyle", "0");
        }
        jextTextArea.setFontName(Jext.getProperty("editor.font"));
        TextAreaPainter textAreaPainter = jextTextArea.getPainter();
        try {
            textAreaPainter.setLinesInterval(Integer.parseInt(Jext.getProperty("editor.linesInterval")));
        }
        catch (NumberFormatException var4_8) {
            textAreaPainter.setLinesInterval(0);
        }
        try {
            textAreaPainter.setWrapGuideOffset(Integer.parseInt(Jext.getProperty("editor.wrapGuideOffset")));
        }
        catch (NumberFormatException var4_9) {
            textAreaPainter.setWrapGuideOffset(0);
        }
        textAreaPainter.setAntiAliasingEnabled(Jext.getBooleanProperty("editor.antiAliasing"));
        textAreaPainter.setLineHighlightEnabled(Jext.getBooleanProperty("editor.lineHighlight"));
        textAreaPainter.setEOLMarkersPainted(Jext.getBooleanProperty("editor.eolMarkers"));
        textAreaPainter.setBlockCaretEnabled(Jext.getBooleanProperty("editor.blockCaret"));
        textAreaPainter.setLinesIntervalHighlightEnabled(Jext.getBooleanProperty("editor.linesIntervalEnabled"));
        textAreaPainter.setWrapGuideEnabled(Jext.getBooleanProperty("editor.wrapGuideEnabled"));
        textAreaPainter.setBracketHighlightColor(GUIUtilities.parseColor(Jext.getProperty("editor.bracketHighlightColor")));
        textAreaPainter.setLineHighlightColor(GUIUtilities.parseColor(Jext.getProperty("editor.lineHighlightColor")));
        textAreaPainter.setHighlightColor(GUIUtilities.parseColor(Jext.getProperty("editor.highlightColor")));
        textAreaPainter.setEOLMarkerColor(GUIUtilities.parseColor(Jext.getProperty("editor.eolMarkerColor")));
        textAreaPainter.setCaretColor(GUIUtilities.parseColor(Jext.getProperty("editor.caretColor")));
        textAreaPainter.setSelectionColor(GUIUtilities.parseColor(Jext.getProperty("editor.selectionColor")));
        textAreaPainter.setBackground(GUIUtilities.parseColor(Jext.getProperty("editor.bgColor")));
        textAreaPainter.setForeground(GUIUtilities.parseColor(Jext.getProperty("editor.fgColor")));
        textAreaPainter.setLinesIntervalHighlightColor(GUIUtilities.parseColor(Jext.getProperty("editor.linesHighlightColor")));
        textAreaPainter.setWrapGuideColor(GUIUtilities.parseColor(Jext.getProperty("editor.wrapGuideColor")));
        this.loadGutter(jextTextArea.getGutter());
        this.loadStyles(textAreaPainter);
        if (jextTextArea.isNew() && jextTextArea.isEmpty()) {
            jextTextArea.setColorizing(Jext.getProperty("editor.colorize.mode"));
        }
        jextTextArea.putClientProperty("InputHandler.homeEnd", new Boolean(Jext.getBooleanProperty("editor.smartHomeEnd")));
        jextTextArea.setCaretBlinkEnabled(Jext.getBooleanProperty("editor.blinkingCaret"));
        jextTextArea.setParentTitle();
        jextTextArea.repaint();
    }

    private void loadGutter(Gutter gutter) {
        int n;
        try {
            n = Integer.parseInt(Jext.getProperty("textArea.gutter.width"));
            gutter.setGutterWidth(n);
        }
        catch (NumberFormatException var2_3) {
            // empty catch block
        }
        gutter.setCollapsed("yes".equals(Jext.getProperty("textArea.gutter.collapsed")));
        gutter.setLineNumberingEnabled(!"no".equals(Jext.getProperty("textArea.gutter.lineNumbers")));
        try {
            n = Integer.parseInt(Jext.getProperty("textArea.gutter.highlightInterval"));
            gutter.setHighlightInterval(n);
        }
        catch (NumberFormatException var2_4) {
            // empty catch block
        }
        gutter.setAntiAliasingEnabled(Jext.getBooleanProperty("editor.antiAliasing"));
        gutter.setBackground(GUIUtilities.parseColor(Jext.getProperty("textArea.gutter.bgColor")));
        gutter.setForeground(GUIUtilities.parseColor(Jext.getProperty("textArea.gutter.fgColor")));
        gutter.setHighlightedForeground(GUIUtilities.parseColor(Jext.getProperty("textArea.gutter.highlightColor")));
        gutter.setCaretMark(GUIUtilities.parseColor(Jext.getProperty("textArea.gutter.caretMarkColor")));
        gutter.setAnchorMark(GUIUtilities.parseColor(Jext.getProperty("textArea.gutter.anchorMarkColor")));
        gutter.setSelectionMark(GUIUtilities.parseColor(Jext.getProperty("textArea.gutter.selectionMarkColor")));
        String string = Jext.getProperty("textArea.gutter.numberAlignment");
        if ("right".equals(string)) {
            gutter.setLineNumberAlignment(4);
        } else if ("center".equals(string)) {
            gutter.setLineNumberAlignment(0);
        } else {
            gutter.setLineNumberAlignment(2);
        }
        try {
            int n2 = Integer.parseInt(Jext.getProperty("textArea.gutter.borderWidth"));
            gutter.setBorder(n2, GUIUtilities.parseColor(Jext.getProperty("textArea.gutter.borderColor")));
        }
        catch (NumberFormatException var3_7) {
            // empty catch block
        }
        try {
            String string2 = Jext.getProperty("textArea.gutter.font");
            int n3 = Integer.parseInt(Jext.getProperty("textArea.gutter.fontSize"));
            int n4 = Integer.parseInt(Jext.getProperty("textArea.gutter.fontStyle"));
            gutter.setFont(new Font(string2, n4, n3));
        }
        catch (NumberFormatException var3_9) {
            // empty catch block
        }
    }

    private void loadStyles(TextAreaPainter textAreaPainter) {
        try {
            SyntaxStyle[] arrsyntaxStyle = new SyntaxStyle[12];
            arrsyntaxStyle[1] = GUIUtilities.parseStyle(Jext.getProperty("editor.style.comment1"));
            arrsyntaxStyle[2] = GUIUtilities.parseStyle(Jext.getProperty("editor.style.comment2"));
            arrsyntaxStyle[6] = GUIUtilities.parseStyle(Jext.getProperty("editor.style.keyword1"));
            arrsyntaxStyle[7] = GUIUtilities.parseStyle(Jext.getProperty("editor.style.keyword2"));
            arrsyntaxStyle[8] = GUIUtilities.parseStyle(Jext.getProperty("editor.style.keyword3"));
            arrsyntaxStyle[3] = GUIUtilities.parseStyle(Jext.getProperty("editor.style.literal1"));
            arrsyntaxStyle[4] = GUIUtilities.parseStyle(Jext.getProperty("editor.style.literal2"));
            arrsyntaxStyle[9] = GUIUtilities.parseStyle(Jext.getProperty("editor.style.operator"));
            arrsyntaxStyle[10] = GUIUtilities.parseStyle(Jext.getProperty("editor.style.invalid"));
            arrsyntaxStyle[5] = GUIUtilities.parseStyle(Jext.getProperty("editor.style.label"));
            arrsyntaxStyle[11] = GUIUtilities.parseStyle(Jext.getProperty("editor.style.method"));
            textAreaPainter.setStyles(arrsyntaxStyle);
        }
        catch (Exception var2_3) {
            // empty catch block
        }
    }

    private void registerPlugins() {
        Plugin[] arrplugin = Jext.getPlugins();
        for (int i = 0; i < arrplugin.length; ++i) {
            if (arrplugin[i] instanceof RegisterablePlugin) {
                try {
                    ((RegisterablePlugin)arrplugin[i]).register(this);
                }
                catch (Throwable var3_3) {
                    System.err.println("#--Exception occurred while registering plugin:");
                    var3_3.printStackTrace();
                }
            }
            if (!(arrplugin[i] instanceof ProjectManagement)) continue;
            this.addProjectManagement((ProjectManagement)arrplugin[i]);
        }
    }

    public void updatePluginsMenu() {
        Plugin[] arrplugin;
        int n;
        if (this.pluginsMenu == null) {
            return;
        }
        if (this.pluginsMenu.getMenuComponentCount() != 0) {
            this.pluginsMenu.removeAll();
        }
        if ((arrplugin = Jext.getPlugins()).length == 0) {
            this.pluginsMenu.add(GUIUtilities.loadMenuItem(Jext.getProperty("no.plugins"), null, null, false));
            return;
        }
        Vector vector = new Vector();
        Vector vector2 = new Vector();
        for (n = 0; n < arrplugin.length; ++n) {
            String string = Jext.getProperty("plugin." + arrplugin[n].getClass().getName() + ".modes");
            if (string != null) continue;
            try {
                arrplugin[n].createMenuItems(this, vector, vector2);
                continue;
            }
            catch (Throwable var6_6) {
                System.err.println("#--Exception while constructing menu items:");
                var6_6.printStackTrace();
            }
        }
        for (n = 0; n < vector.size(); ++n) {
            this.pluginsMenu.add((JMenu)vector.elementAt(n));
        }
        for (n = 0; n < vector2.size(); ++n) {
            this.pluginsMenu.add((JMenuItem)vector2.elementAt(n));
        }
        if (this.pluginsMenu.getItemCount() == 0) {
            this.pluginsMenu.add(GUIUtilities.loadMenuItem(Jext.getProperty("no.plugins"), null, null, false));
        }
        this.freeze();
    }

    public void startAutoSave() {
        if (this.auto == null) {
            this.auto = new AutoSave(this);
        }
    }

    public void stopAutoSave() {
        if (this.auto != null) {
            this.auto.interrupt();
            this.auto = null;
        }
    }

    public void updateStatus(JextTextArea jextTextArea) {
        int n = jextTextArea.getCaretPosition();
        Element element = jextTextArea.getDocument().getDefaultRootElement();
        int n2 = element.getElementIndex(n);
        Element element2 = element.getElement(n2);
        int n3 = element2.getStartOffset();
        int n4 = element2.getEndOffset();
        int n5 = element.getElementCount();
        this.status.setText("" + ' ' + (n - n3 + 1) + ':' + (n4 - n3) + " - " + (n2 + 1) + '/' + n5 + " - [ " + jextTextArea.getLineTermName() + " ] - " + (n2 + 1) * 100 / n5 + '%');
    }

    public void setStatus(JextTextArea jextTextArea) {
        String string;
        StringBuffer stringBuffer = new StringBuffer();
        if (jextTextArea.isEditable()) {
            stringBuffer.append(jextTextArea.isDirty() ? Jext.getProperty("editor.modified") : "");
        } else {
            stringBuffer.append(Jext.getProperty("editor.readonly"));
        }
        if (jextTextArea.oneClick != null) {
            if (stringBuffer.length() > 0) {
                stringBuffer.append(" : ");
            }
            stringBuffer.append("one click!");
        }
        if ((string = stringBuffer.toString()).length() > 0) {
            this.message.setText("" + '(' + string + ')');
        } else {
            this.message.setText("");
        }
    }

    public void resetStatus(JextTextArea jextTextArea) {
        jextTextArea.clean();
        this.message.setText("");
        this.textAreasPane.setCleanIcon(jextTextArea);
    }

    public void setNew(JextTextArea jextTextArea) {
        this.message.setText(jextTextArea.isEditable() ? "" : Jext.getProperty("editor.readonly"));
        this.textAreasPane.setCleanIcon(jextTextArea);
        this.updateStatus(jextTextArea);
    }

    public void setChanged(JextTextArea jextTextArea) {
        this.textAreasPane.setDirtyIcon(jextTextArea);
        this.setStatus(jextTextArea);
    }

    public void setSaved(JextTextArea jextTextArea) {
        this.textAreasPane.setCleanIcon(jextTextArea);
        this.message.setText("");
    }

    public void closeToQuit() {
        this.workspaces.closeAllWorkspaces();
        Iterator iterator = this.projectMgmts.values().iterator();
        while (iterator.hasNext()) {
            ProjectManager projectManager = ((ProjectManagement)iterator.next()).getProjectManager();
            Project[] arrproject = projectManager.getProjects();
            for (int i = 0; i < arrproject.length; ++i) {
                projectManager.saveProject(arrproject[i]);
            }
        }
    }

    public void closeWindow() {
        this.closeWindow(true);
    }

    public void closeWindow(boolean bl) {
        if (this.console != null) {
            this.console.stop();
        }
        this.stopAutoSave();
        this.removeAllJextListeners();
        Jext.getInstances().remove(this);
        this.dispose();
    }

    void saveConsole() {
        if (this.console != null && Jext.getBooleanProperty("console.save")) {
            this.console.save();
        }
    }

    void cleanMemory() {
        this.workspaces.clear();
        this.workspaces = null;
        this.transientItems.clear();
        this.toolBar = null;
        this.pluginsMenu = null;
        this.menuRecent = null;
        this.xtree = null;
        this.console = null;
        this.auto = null;
        this.chooser = null;
        this.accessory = null;
        this.centerPane = null;
        this.splitter = null;
        this.split = null;
        this.textAreaSplitter = null;
        this.hTabbedPane = null;
        this.vTabbedPane = null;
        this.textAreasPane = null;
        this.splittedTextArea = null;
        this.inputHandler = null;
        this.jextListeners = null;
        this.transientItems = null;
        this.keyEventInterceptor = null;
        System.gc();
    }

    public boolean checkContent(JextTextArea jextTextArea) {
        if (jextTextArea.isDirty() && !jextTextArea.isEmpty()) {
            this.textAreasPane.setSelectedComponent(jextTextArea);
            Object[] arrobject = new String[]{jextTextArea.getName()};
            int n = JOptionPane.showConfirmDialog(this, Jext.getProperty("general.save.question", arrobject), Jext.getProperty("general.save.title"), 1, 3);
            switch (n) {
                case 0: {
                    jextTextArea.saveContent();
                    break;
                }
                case 1: {
                    break;
                }
                case 2: {
                    return false;
                }
            }
        }
        return true;
    }

    public void setRecentMenu(JextRecentMenu jextRecentMenu) {
        this.menuRecent = jextRecentMenu;
        this.reloadRecent();
    }

    public void reloadRecent() {
        this.menuRecent.createRecent();
    }

    public void removeRecent() {
        this.menuRecent.removeRecent();
    }

    public void saveRecent(String string) {
        this.menuRecent.saveRecent(string);
    }

    public void showWaitCursor() {
        if (this.waitCount++ == 0) {
            Cursor cursor = Cursor.getPredefinedCursor(3);
            this.setCursor(cursor);
            JextTextArea[] arrjextTextArea = this.getTextAreas();
            for (int i = 0; i < arrjextTextArea.length; ++i) {
                arrjextTextArea[i].getPainter().setCursor(cursor);
            }
        }
    }

    public void hideWaitCursor() {
        if (this.waitCount > 0) {
            --this.waitCount;
        }
        if (this.waitCount == 0) {
            Cursor cursor = Cursor.getPredefinedCursor(0);
            this.setCursor(cursor);
            cursor = Cursor.getPredefinedCursor(2);
            JextTextArea[] arrjextTextArea = this.getTextAreas();
            for (int i = 0; i < arrjextTextArea.length; ++i) {
                arrjextTextArea[i].getPainter().setCursor(cursor);
            }
        }
    }

    public boolean selectProjectManagement(String string) {
        boolean bl;
        ProjectManager projectManager;
        boolean bl2 = bl = this.projectMgmts.containsKey(string) && this.projectMgmts.get(string) != null;
        if (bl && (bl = (projectManager = ((ProjectManagement)this.projectMgmts.get(string)).getProjectManager()) != null) && this.currentProjectMgr != projectManager) {
            if (this.currentProjectMgr != null) {
                this.vTabbedPane.remove(this.currentProjectMgr.getUI());
            }
            this.currentProjectMgr = projectManager;
            if (this.currentProjectMgr.getUI() != null) {
                this.vTabbedPane.add(Jext.getProperty("vTabbedPane.project"), projectManager.getUI());
            }
        }
        return bl;
    }

    public ProjectManager getProjectManager() {
        return this.currentProjectMgr;
    }

    public void setJextToolBar(JextToolBar jextToolBar) {
        this.toolBar = jextToolBar;
    }

    public JextToolBar getJextToolBar() {
        return this.toolBar;
    }

    public JextMenuBar getJextMenuBar() {
        return (JextMenuBar)this.getJMenuBar();
    }

    public JextTextArea getTextArea() {
        if (this.splittedTextArea != null && this.splittedTextArea.hasFocus()) {
            return this.splittedTextArea;
        }
        return this.getNSTextArea();
    }

    public JextTextArea getNSTextArea() {
        Component component = this.textAreasPane.getSelectedComponent();
        if (component instanceof JextTextArea) {
            return (JextTextArea)component;
        }
        for (int i = this.textAreasPane.getTabCount() - 1; i >= 0; --i) {
            component = this.textAreasPane.getComponentAt(i);
            if (!(component instanceof JextTextArea)) continue;
            return (JextTextArea)component;
        }
        return null;
    }

    public JextTextArea[] getTextAreas() {
        Vector<Component> vector = new Vector<Component>(this.textAreasPane.getTabCount());
        for (int i = 0; i < this.textAreasPane.getTabCount(); ++i) {
            Component component = this.textAreasPane.getComponentAt(i);
            if (!(component instanceof JextTextArea)) continue;
            vector.addElement(component);
        }
        Object[] arrobject = new JextTextArea[vector.size()];
        vector.copyInto(arrobject);
        vector = null;
        return arrobject;
    }

    public void close(JextTextArea jextTextArea) {
        this.close(jextTextArea, true);
    }

    public void close(JextTextArea jextTextArea, boolean bl) {
        if (bl && !this.checkContent(jextTextArea)) {
            return;
        }
        int n = this.textAreasPane.indexOfComponent(jextTextArea);
        if (n != -1) {
            this.workspaces.removeFile(jextTextArea);
            this.textAreasPane.removeTabAt(n);
            jextTextArea.getPainter().setDropTarget(null);
            this.fireJextEvent(jextTextArea, 79);
            if (this.getTextAreas().length == 0) {
                this.createFile();
            }
            jextTextArea = null;
        }
    }

    public void closeAll() {
        SaveDialog saveDialog = new SaveDialog(this, 1);
    }

    public JextTextArea open(String string) {
        return this.open(string, true);
    }

    public JextTextArea open(String string, boolean bl) {
        JextTextArea jextTextArea;
        if (string == null) {
            return null;
        }
        if (!new File(string).exists()) {
            Object[] arrobject = new String[]{string};
            Utilities.showError(Jext.getProperty("textarea.file.notfound", arrobject));
            return null;
        }
        JextTextArea[] arrjextTextArea = this.getTextAreas();
        block4 : for (int i = 0; i < arrjextTextArea.length; ++i) {
            String string2;
            jextTextArea = arrjextTextArea[i];
            if (jextTextArea.isNew() || (string2 = jextTextArea.getCurrentFile()) == null || !string2.equals(string)) continue;
            int n = JOptionPane.showConfirmDialog(this, Jext.getProperty("textarea.file.opened.msg", new Object[]{string2}), Jext.getProperty("textarea.file.opened.title"), 1, 3);
            switch (n) {
                case 0: {
                    jextTextArea.open(string2, bl);
                    this.textAreasPane.setSelectedComponent(jextTextArea);
                    return jextTextArea;
                }
                case 1: {
                    break block4;
                }
                default: {
                    return null;
                }
            }
        }
        jextTextArea = this.createTextArea();
        jextTextArea.open(string, bl);
        this.addTextAreaInTabbedPane(jextTextArea);
        JextTextArea jextTextArea2 = (JextTextArea)this.textAreasPane.getComponentAt(0);
        if (this.textAreasPane.getTabCount() == 2 && jextTextArea2.isNew() && jextTextArea2.getLength() == 0) {
            this.close(jextTextArea2);
        }
        return jextTextArea;
    }

    public JextTextArea openForLoading(String string) {
        if (string == null) {
            return null;
        }
        if (!new File(string).exists()) {
            Object[] arrobject = new String[]{string};
            Utilities.showError(Jext.getProperty("textarea.file.notfound", arrobject));
            return null;
        }
        JextTextArea jextTextArea = new JextTextArea(this);
        new DropTarget(jextTextArea.getPainter(), new DnDHandler());
        jextTextArea.setDocument(new SyntaxDocument());
        jextTextArea.open(string, false);
        this.addTextAreaInTabbedPane(jextTextArea);
        return jextTextArea;
    }

    private JextTextArea createTextArea() {
        JextTextArea jextTextArea = new JextTextArea(this);
        new DropTarget(jextTextArea.getPainter(), new DnDHandler());
        jextTextArea.setDocument(new SyntaxDocument());
        this.loadTextArea(jextTextArea);
        return jextTextArea;
    }

    private void addTextAreaInTabbedPane(JextTextArea jextTextArea) {
        if (this.workspaces != null) {
            this.workspaces.addFile(jextTextArea);
        }
        this.textAreasPane.add(jextTextArea);
        this.fireJextEvent(jextTextArea, 78);
        this.textAreasPane.setSelectedComponent(jextTextArea);
    }

    private void addProjectManagement(ProjectManagement projectManagement) {
        this.projectMgmts = this.projectMgmts == null ? new HashMap() : this.projectMgmts;
        this.projectMgmts.put(projectManagement.getLabel(), projectManagement);
    }

    public JextTextArea createFile() {
        JextTextArea jextTextArea = this.createTextArea();
        this.addTextAreaInTabbedPane(jextTextArea);
        return jextTextArea;
    }

    public void setTextAreaName(JextTextArea jextTextArea, String string) {
        this.textAreasPane.setTitleAt(this.textAreasPane.indexOfComponent(jextTextArea), string);
    }

    public void updateSplittedTextArea(JextTextArea jextTextArea) {
        if (this.textAreaSplitter.getBottomComponent() == null || jextTextArea == null) {
            return;
        }
        this.splittedTextArea.setDocument(jextTextArea.getDocument());
        String string = jextTextArea.getColorizingMode();
        if (!string.equals(this.splittedTextArea.getColorizingMode())) {
            this.splittedTextArea.setColorizing(string);
        }
        this.splittedTextArea.discard();
        this.splittedTextArea.setEditable(jextTextArea.isEditable());
    }

    public void disableSplittedTextArea() {
        if (this.textAreaSplitter.getBottomComponent() == null) {
            return;
        }
        this.splittedTextArea.setDocument(new SyntaxDocument());
        this.splittedTextArea.setEditable(false);
    }

    public JextFrame() {
        this(null, true);
    }

    public JextFrame(String[] arrstring) {
        this(arrstring, true);
    }

    public JextFrame(String[] arrstring, boolean bl) {
        super("Jext - Java Text Editor");
        this.getContentPane().setLayout(new BorderLayout());
        Jext.setSplashProgress(10);
        Jext.setSplashText(Jext.getProperty("startup.gui"));
        this.defaultProjectMgmt = new DefaultProjectManagement(this);
        this.addProjectManagement(this.defaultProjectMgmt);
        this.registerPlugins();
        this.setIconImage(GUIUtilities.getJextIconImage());
        Class class_ = Jext.class;
        XMenuReader.read(this, class_.getResourceAsStream("jext.menu.xml"), "jext.menu.xml");
        this.inputHandler = new DefaultInputHandler(Jext.getInputHandler());
        Jext.setSplashProgress(20);
        Jext.setSplashText(Jext.getProperty("startup.toolbar.build"));
        XBarReader.read(this, Jext.class.getResourceAsStream("jext.toolbar.xml"), "jext.toolbar.xml");
        this.splittedTextArea = this.createTextArea();
        this.textAreasPane = new JextTabbedPane(this);
        Jext.setSplashProgress(30);
        Jext.setSplashText(Jext.getProperty("startup.files"));
        this.workspaces = new Workspaces(this);
        this.workspaces.load();
        this.textAreaSplitter.setContinuousLayout(true);
        this.textAreaSplitter.setBorder(null);
        this.textAreaSplitter.setTopComponent(this.textAreasPane);
        this.textAreaSplitter.setBottomComponent(this.splittedTextArea);
        Jext.setSplashProgress(50);
        Jext.setSplashText(Jext.getProperty("startup.xinsert"));
        Jext.setSplashText(Jext.getProperty("startup.xinsert.build"));
        this.split.setOneTouchExpandable(true);
        this.split.setRightComponent(this.textAreaSplitter);
        this.split.setContinuousLayout(true);
        this.split.setBorder(null);
        this._dividerSize = this.split.getDividerSize();
        this.vTabbedPane = new JTabbedPane();
        GUIUtilities.setScrollableTabbedPane(this.vTabbedPane);
        this.virtualFolders = new VirtualFolders(this);
        this.selectProjectManagement(Jext.getProperty("projectManagement.current", this.defaultProjectMgmt.getLabel()));
        if (Jext.getBooleanProperty("xtree.enabled")) {
            this.xtree = new XTree(this, "jext.insert.xml");
            this.vTabbedPane.add(Jext.getProperty("vTabbedPane.xinsert"), this.xtree);
        }
        this.split.setLeftComponent(this.vTabbedPane);
        Jext.setSplashProgress(60);
        this.splitter.setOneTouchExpandable(true);
        this.splitter.setContinuousLayout(true);
        this.splitter.setBorder(null);
        this.hTabbedPane = new JTabbedPane();
        GUIUtilities.setScrollableTabbedPane(this.hTabbedPane);
        if (Jext.getBooleanProperty("console.enabled")) {
            this.console = new Console(this);
            this.console.setPromptPattern(Jext.getProperty("console.prompt"));
            this.console.displayPrompt();
            this.hTabbedPane.add(Jext.getProperty("hTabbedPane.console"), this.console);
            this.hTabbedPane.setPreferredSize(this.console.getPreferredSize());
        }
        this.splitter.setTopComponent(this.hTabbedPane);
        this.splitter.setBottomComponent(this.split);
        this.centerPane = new JPanel(new BorderLayout());
        this.centerPane.add("Center", this.splitter);
        Jext.setSplashProgress(70);
        Jext.setSplashText(Jext.getProperty("startup.gui"));
        this.status.addMouseListener(new MouseAdapter(){

            public void mouseClicked(MouseEvent mouseEvent) {
                JextTextArea jextTextArea = JextFrame.this.getNSTextArea();
                jextTextArea.rotateLineTerm();
                JextFrame.this.updateStatus(jextTextArea);
                if (jextTextArea.isDirty()) {
                    JextFrame.this.textAreasPane.setDirtyIcon(jextTextArea);
                } else {
                    JextFrame.this.textAreasPane.setCleanIcon(jextTextArea);
                }
                JextFrame.this.setStatus(jextTextArea);
            }
        });
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add("West", this.status);
        jPanel.add("East", this.message);
        this.centerPane.add("South", jPanel);
        this.getContentPane().add("Center", this.centerPane);
        this.getContentPane().add("North", this.toolBar);
        Jext.setSplashProgress(80);
        Jext.setSplashText(Jext.getProperty("startup.props"));
        this.pack();
        GUIUtilities.loadGeometry(this, "jext");
        this.loadProperties(false);
        this.addWindowListener(new WindowHandler());
        Jext.setSplashProgress(90);
        Jext.setSplashText(Jext.getProperty("startup.files"));
        if (arrstring != null) {
            this.workspaces.selectWorkspaceOfNameOrCreate(Jext.getProperty("ws.default"));
            this.setBatchMode(true);
            for (int i = 0; i < arrstring.length; ++i) {
                if (arrstring[i] == null) continue;
                this.open(Utilities.constructPath(arrstring[i]));
            }
            this.setBatchMode(false);
        }
        this.updateSplittedTextArea(this.getTextArea());
        Jext.setSplashProgress(95);
        Jext.setSplashText(Jext.getProperty("startup.plugins"));
        Jext.executeScripts(this);
        JARClassLoader.executeScripts(this);
        this.updatePluginsMenu();
        this.toolBar.addMisc(this);
        this.triggerTabbedPanes();
        Jext.setSplashProgress(100);
        this.setDefaultCloseOperation(0);
        this.addJextListener(new PluginHandler());
        this.fireJextEvent(98);
        this.getTextArea().setParentTitle();
        Jext.killSplashScreen();
        this.setVisible(bl);
        this.getTextArea().grabFocus();
        this.getTextArea().requestFocus();
    }

    public final InputHandler getInputHandler() {
        return this.inputHandler;
    }

    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    public void processKeyEvent(KeyEvent keyEvent) {
        Object object;
        if (this.getFocusOwner() instanceof JComponent) {
            Object object2;
            object = (JComponent)this.getFocusOwner();
            InputMap inputMap = object.getInputMap();
            ActionMap actionMap = object.getActionMap();
            if (inputMap != null && actionMap != null && object.isEnabled() && (object2 = inputMap.get(KeyStroke.getKeyStrokeForEvent(keyEvent))) != null && actionMap.get(object2) != null) {
                return;
            }
        }
        if (this.getFocusOwner() instanceof JTextComponent) {
            if (keyEvent.getID() == 401) {
                switch (keyEvent.getKeyCode()) {
                    case 8: 
                    case 9: 
                    case 10: {
                        return;
                    }
                }
            }
            if ((object = ((JTextComponent)this.getFocusOwner()).getKeymap()).getAction(KeyStroke.getKeyStrokeForEvent(keyEvent)) != null) {
                return;
            }
        }
        if (keyEvent.isConsumed()) {
            return;
        }
        if ((keyEvent = KeyEventWorkaround.processKeyEvent(keyEvent)) == null) {
            return;
        }
        switch (keyEvent.getID()) {
            case 400: {
                if (this.keyEventInterceptor != null) {
                    this.keyEventInterceptor.keyTyped(keyEvent);
                    break;
                }
                if (!this.inputHandler.isRepeatEnabled()) break;
                this.inputHandler.keyTyped(keyEvent);
                break;
            }
            case 401: {
                if (this.keyEventInterceptor != null) {
                    this.keyEventInterceptor.keyPressed(keyEvent);
                    break;
                }
                this.inputHandler.keyPressed(keyEvent);
                break;
            }
            case 402: {
                if (this.keyEventInterceptor != null) {
                    this.keyEventInterceptor.keyReleased(keyEvent);
                    break;
                }
                this.inputHandler.keyReleased(keyEvent);
            }
        }
        if (!keyEvent.isConsumed()) {
            super.processKeyEvent(keyEvent);
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.toolBar = null;
        this.pluginsMenu = null;
        this.menuRecent = null;
        this.xtree = null;
        this.console = null;
        this.dawnLogWindow = null;
        this.pythonLogWindow = null;
        this.workspaces = null;
        this.virtualFolders = null;
        this.auto = null;
        this.chooser = null;
        this.accessory = null;
        this.centerPane = null;
        this.textAreasPane = null;
        this.split = null;
        this.hTabbedPane = null;
        this.vTabbedPane = null;
        this.splitter = null;
        this.textAreaSplitter = null;
        this.splittedTextArea = null;
        this.message = null;
        this.status = null;
        this.jextListeners = null;
        this.transientItems = null;
        this.keyEventInterceptor = null;
        this.inputHandler = null;
    }

    class DnDHandler
    implements DropTargetListener {
        DnDHandler() {
        }

        public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
        }

        public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
        }

        public void dragExit(DropTargetEvent dropTargetEvent) {
        }

        public void dragScroll(DropTargetDragEvent dropTargetDragEvent) {
        }

        public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
        }

        public void drop(DropTargetDropEvent dropTargetDropEvent) {
            DataFlavor[] arrdataFlavor = dropTargetDropEvent.getCurrentDataFlavors();
            if (arrdataFlavor == null) {
                return;
            }
            boolean bl = false;
            for (int i = arrdataFlavor.length - 1; i >= 0; --i) {
                if (!arrdataFlavor[i].isFlavorJavaFileListType()) continue;
                dropTargetDropEvent.acceptDrop(1);
                Transferable transferable = dropTargetDropEvent.getTransferable();
                try {
                    Iterator iterator = ((List)transferable.getTransferData(arrdataFlavor[i])).iterator();
                    SwingUtilities.invokeLater(new Runnable(this, iterator){
                        private final /* synthetic */ Iterator val$iterator;
                        private final /* synthetic */ DnDHandler this$1;

                        public void run() {
                            while (this.val$iterator.hasNext()) {
                                DnDHandler.access$100(this.this$1).open(((File)this.val$iterator.next()).getPath());
                            }
                        }
                    });
                    bl = true;
                    continue;
                }
                catch (Exception var6_7) {
                    // empty catch block
                }
            }
            dropTargetDropEvent.dropComplete(bl);
        }

        static /* synthetic */ JextFrame access$100(DnDHandler dnDHandler) {
            return dnDHandler.JextFrame.this;
        }
    }

    class PluginHandler
    implements JextListener {
        PluginHandler() {
        }

        public void jextEventFired(JextEvent jextEvent) {
            int n = jextEvent.getWhat();
            if (n == 1 || n == 77 || n == 98) {
                JextFrame.this.reset();
                String string = jextEvent.getTextArea().getColorizingMode();
                Mode mode = Jext.getMode(string);
                ArrayList arrayList = mode.getPlugins();
                for (int i = 0; i < arrayList.size(); ++i) {
                    Plugin plugin = (Plugin)arrayList.get(i);
                    if (plugin == null) continue;
                    Vector vector = new Vector();
                    Vector vector2 = new Vector();
                    try {
                        plugin.createMenuItems(JextFrame.this, vector, vector2);
                        continue;
                    }
                    catch (Throwable var10_10) {
                        System.err.println("#--Exception while constructing menu items:");
                        var10_10.printStackTrace();
                    }
                }
            }
        }
    }

    class WindowHandler
    extends WindowAdapter {
        WindowHandler() {
        }

        public void windowClosing(WindowEvent windowEvent) {
            Jext.closeToQuit(JextFrame.this);
        }
    }

}

