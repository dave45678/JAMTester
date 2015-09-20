/*
 * Decompiled with CFR 0_102.
 */
package org.jext.options;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.Plugin;
import org.jext.Utilities;
import org.jext.gui.AbstractOptionPane;
import org.jext.gui.JextHighlightButton;
import org.jext.gui.OptionGroup;
import org.jext.gui.OptionPane;
import org.jext.options.EditorOptions;
import org.jext.options.FileFiltersOptions;
import org.jext.options.GeneralOptions;
import org.jext.options.GutterOptions;
import org.jext.options.KeyShortcutsOptions;
import org.jext.options.LangOptions;
import org.jext.options.LoadingOptions;
import org.jext.options.PrintOptions;
import org.jext.options.SecurityOptions;
import org.jext.options.StylesOptions;
import org.jext.options.UIOptions;

public class OptionsDialog
extends JDialog
implements ActionListener,
TreeSelectionListener {
    private JTree paneTree;
    private JPanel cardPanel;
    private JLabel currentLabel;
    private JextHighlightButton ok;
    private JextHighlightButton cancel;
    private JextHighlightButton apply;
    private OptionGroup jextGroup;
    private OptionGroup pluginsGroup;
    private static OptionsDialog theInstance;
    private OptionTreeModel theTree;
    private boolean toReload = false;
    private boolean isLoadingPlugs;
    private String currPaneName;
    private Plugin currPlugin;
    private ArrayList cachPlugPanes;
    private ArrayList notCachPlugPanes;
    private ArrayList notCachPlugin;
    private JextFrame parent;
    static /* synthetic */ Class class$javax$swing$event$TreeModelListener;

    static OptionsDialog getInstance() {
        return theInstance;
    }

    public static void showOptionDialog(JextFrame jextFrame) {
        if (theInstance == null) {
            theInstance = new OptionsDialog(jextFrame);
        } else {
            theInstance.reload();
        }
        theInstance.setVisible(true);
    }

    private OptionsDialog(JextFrame jextFrame) {
        super(jextFrame, Jext.getProperty("options.title"), true);
        this.parent = jextFrame;
        this.parent.showWaitCursor();
        this.cachPlugPanes = new ArrayList(20);
        this.notCachPlugPanes = new ArrayList(20);
        this.notCachPlugin = new ArrayList(20);
        this.getContentPane().setLayout(new BorderLayout());
        ((JPanel)this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        JPanel jPanel = new JPanel(new BorderLayout(4, 8));
        jPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        this.getContentPane().add((Component)jPanel, "Center");
        this.currentLabel = new JLabel();
        this.currentLabel.setHorizontalAlignment(2);
        this.currentLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
        jPanel.add((Component)this.currentLabel, "North");
        this.cardPanel = new JPanel(new CardLayout());
        jPanel.add((Component)this.cardPanel, "Center");
        this.theTree = this.createOptionTreeModel();
        this.paneTree = new JTree(this.theTree);
        this.paneTree.setCellRenderer(new PaneNameRenderer());
        this.paneTree.putClientProperty("JTree.lineStyle", "Angled");
        this.paneTree.setShowsRootHandles(true);
        this.paneTree.setRootVisible(false);
        this.getContentPane().add((Component)new JScrollPane(this.paneTree, 22, 30), "West");
        JPanel jPanel2 = new JPanel();
        this.ok = new JextHighlightButton(Jext.getProperty("options.set.button"));
        this.ok.setMnemonic(Jext.getProperty("options.set.mnemonic").charAt(0));
        this.ok.addActionListener(this);
        jPanel2.add(this.ok);
        this.getRootPane().setDefaultButton(this.ok);
        this.cancel = new JextHighlightButton(Jext.getProperty("general.cancel.button"));
        this.cancel.setMnemonic(Jext.getProperty("general.cancel.mnemonic").charAt(0));
        this.cancel.addActionListener(this);
        jPanel2.add(this.cancel);
        this.apply = new JextHighlightButton(Jext.getProperty("options.apply.button"));
        this.apply.setMnemonic(Jext.getProperty("options.apply.mnemonic").charAt(0));
        this.apply.addActionListener(this);
        jPanel2.add(this.apply);
        this.getContentPane().add((Component)jPanel2, "South");
        this.addKeyListener(new KeyAdapter(){

            public void keyPressed(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case 10: {
                        OptionsDialog.this.ok();
                        break;
                    }
                    case 27: {
                        OptionsDialog.this.cancel();
                    }
                }
            }
        });
        this.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent windowEvent) {
                OptionsDialog.this.cancel();
            }
        });
        TreePath treePath = new TreePath(new Object[]{this.theTree.getRoot(), this.jextGroup, this.jextGroup.getMember(0)});
        this.paneTree.getSelectionModel().addTreeSelectionListener(this);
        this.paneTree.setSelectionPath(treePath);
        this.paneTree.addMouseListener(new MouseHandler());
        this.pack();
        Utilities.centerComponent(this);
        this.parent.hideWaitCursor();
    }

    private void ok(boolean bl) {
        OptionTreeModel optionTreeModel = (OptionTreeModel)this.paneTree.getModel();
        ((OptionGroup)optionTreeModel.getRoot()).save();
        Jext.propertiesChanged();
        if (bl) {
            this.setVisible(false);
        }
    }

    private void ok() {
        this.ok(true);
    }

    private void cancel() {
        this.toReload = true;
        this.setVisible(false);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        if (object == this.ok) {
            this.ok();
        } else if (object == this.cancel) {
            this.cancel();
        } else if (object == this.apply) {
            this.ok(false);
        }
    }

    private void reload() {
        if (this.toReload) {
            this.parent.showWaitCursor();
            this.reloadStdPanes();
            this.reloadPluginPanes();
            this.toReload = false;
            this.parent.hideWaitCursor();
        }
    }

    private void reloadStdPanes() {
        ArrayList arrayList = this.jextGroup.getMembers();
        for (int i = 0; i < arrayList.size(); ++i) {
            ((AbstractOptionPane)arrayList.get(i)).load();
        }
    }

    private void reloadPluginPanes() {
        Object object;
        ((CardLayout)this.cardPanel.getLayout()).show(this.cardPanel, ((OptionPane)this.jextGroup.getMember(0)).getName());
        Iterator iterator = this.cachPlugPanes.iterator();
        while (iterator.hasNext()) {
            object = null;
            try {
                object = (OptionPane)iterator.next();
                object.load();
            }
            catch (AbstractMethodError var3_4) {
                var3_4.printStackTrace();
                Utilities.showError("The option pane of the plugin containing " + object.getClass().toString() + " is not supported, and you will not see it in the option dialog. This is related to new Jext " + "release(from 3.2pre3). You should make aware of this Romain Guy, the plugin's author or " + "Blaisorblade <blaisorblade_work (at) yahoo.it, who will provide an upgraded version " + "of the plugin.Thanks");
            }
            catch (Throwable var3_5) {
                var3_5.printStackTrace();
            }
        }
        iterator = this.notCachPlugPanes.iterator();
        while (iterator.hasNext()) {
            this.cardPanel.remove(((OptionPane)iterator.next()).getComponent());
        }
        iterator = this.notCachPlugin.iterator();
        while (iterator.hasNext()) {
            object = null;
            try {
                object = (Plugin)iterator.next();
                object.createOptionPanes(this);
            }
            catch (AbstractMethodError var3_6) {
                var3_6.printStackTrace();
                Utilities.showError("The option pane of the plugin containing " + object.getClass().toString() + " is not supported, and you will not see it in the option dialog. This is related to new Jext " + "release(from 3.2pre3). You should make aware of this Romain Guy, the plugin's author or " + "Blaisorblade <blaisorblade_work (at) yahoo.it, who will provide an upgraded version " + "of the plugin.Thanks");
            }
            catch (Throwable var3_7) {
                var3_7.printStackTrace();
            }
        }
        ((CardLayout)this.cardPanel.getLayout()).show(this.cardPanel, this.currPaneName);
    }

    public void addOptionGroup(OptionGroup optionGroup) {
        this.addOptionGroup(optionGroup, this.pluginsGroup);
    }

    public void addOptionPane(OptionPane optionPane) {
        this.addOptionPane(optionPane, this.pluginsGroup);
    }

    private void addOptionGroup(OptionGroup optionGroup, OptionGroup optionGroup2) {
        ArrayList arrayList = optionGroup.getMembers();
        for (int i = 0; i < arrayList.size(); ++i) {
            Object e = arrayList.get(i);
            if (e instanceof OptionPane) {
                this.addOptionPane((OptionPane)e, optionGroup);
                continue;
            }
            if (!(e instanceof OptionGroup)) continue;
            this.addOptionGroup((OptionGroup)e, optionGroup);
        }
        optionGroup2.addOptionGroup(optionGroup);
    }

    private void addOptionPane(OptionPane optionPane, OptionGroup optionGroup) {
        String string = optionPane.getName();
        this.cardPanel.add(optionPane.getComponent(), string);
        optionGroup.addOptionPane(optionPane);
        if (this.isLoadingPlugs) {
            if (optionPane.isCacheable()) {
                this.cachPlugPanes.add(optionPane);
            } else {
                this.notCachPlugPanes.add(optionPane);
                if (this.currPlugin != null) {
                    this.notCachPlugin.add(this.currPlugin);
                    this.currPlugin = null;
                }
            }
        }
    }

    private OptionTreeModel createOptionTreeModel() {
        OptionTreeModel optionTreeModel = new OptionTreeModel();
        OptionGroup optionGroup = (OptionGroup)optionTreeModel.getRoot();
        this.jextGroup = new OptionGroup("jext");
        this.addOptionPane(new GeneralOptions(), this.jextGroup);
        this.addOptionPane(new LoadingOptions(), this.jextGroup);
        this.addOptionPane(new UIOptions(), this.jextGroup);
        this.addOptionPane(new EditorOptions(), this.jextGroup);
        this.addOptionPane(new PrintOptions(), this.jextGroup);
        this.addOptionPane(new GutterOptions(), this.jextGroup);
        this.addOptionPane(new StylesOptions(), this.jextGroup);
        this.addOptionPane(new KeyShortcutsOptions(), this.jextGroup);
        this.addOptionPane(new FileFiltersOptions(), this.jextGroup);
        this.addOptionPane(new LangOptions(), this.jextGroup);
        this.addOptionPane(new SecurityOptions(), this.jextGroup);
        this.addOptionGroup(this.jextGroup, optionGroup);
        this.pluginsGroup = new OptionGroup("plugins");
        Plugin[] arrplugin = Jext.getPlugins();
        this.isLoadingPlugs = true;
        for (int i = 0; i < arrplugin.length; ++i) {
            this.currPlugin = arrplugin[i];
            try {
                this.currPlugin.createOptionPanes(this);
                continue;
            }
            catch (AbstractMethodError var5_6) {
                var5_6.printStackTrace();
                Utilities.showError("The option pane of the plugin containing " + arrplugin[i].getClass().toString() + " is not supported, and you will not see it in the option dialog. This is related to new Jext " + "release(from 3.2pre3). You should make aware of this Romain Guy, the plugin's author or " + "Blaisorblade <blaisorblade_work (at) yahoo.it, who will provide an upgraded version " + "of the plugin.Thanks");
                continue;
            }
            catch (Throwable var5_7) {
                var5_7.printStackTrace();
            }
        }
        this.isLoadingPlugs = false;
        if (this.pluginsGroup.getMemberCount() > 0) {
            this.addOptionGroup(this.pluginsGroup, optionGroup);
        }
        return optionTreeModel;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void valueChanged(TreeSelectionEvent var1_1) {
        var2_2 = var1_1.getPath();
        if (var2_2 == null) return;
        if (!(var2_2.getLastPathComponent() instanceof OptionPane)) {
            return;
        }
        var3_3 = var2_2.getPath();
        var4_4 = new StringBuffer();
        this.currPaneName = null;
        var5_5 = var3_3.length - 1;
        v0 = var6_6 = this.paneTree.isRootVisible() != false ? 0 : 1;
        do {
            if (var6_6 > var5_5) {
                this.currentLabel.setText(var4_4.toString());
                ((CardLayout)this.cardPanel.getLayout()).show(this.cardPanel, this.currPaneName);
                return;
            }
            if (var3_3[var6_6] instanceof OptionPane) {
                this.currPaneName = ((OptionPane)var3_3[var6_6]).getName();
            } else if (var3_3[var6_6] instanceof OptionGroup) {
                this.currPaneName = ((OptionGroup)var3_3[var6_6]).getName();
            } else {
                ** GOTO lbl28
            }
            if (this.currPaneName != null) {
                var7_7 = Jext.getProperty("options." + this.currPaneName + ".label");
                if (var7_7 == null) {
                    var4_4.append(this.currPaneName);
                } else {
                    var4_4.append(var7_7);
                }
            }
            if (var6_6 != var5_5) {
                var4_4.append(": ");
            }
lbl28: // 5 sources:
            ++var6_6;
        } while (true);
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.paneTree = null;
        this.cardPanel = null;
        this.currentLabel = null;
        this.ok = null;
        this.cancel = null;
        this.apply = null;
        this.jextGroup = null;
        this.pluginsGroup = null;
        this.theTree = null;
        theInstance = null;
        this.currPlugin = null;
        this.cachPlugPanes.clear();
        this.cachPlugPanes = null;
        this.notCachPlugPanes.clear();
        this.notCachPlugPanes = null;
        this.notCachPlugin.clear();
        this.notCachPlugin = null;
        this.currPaneName = null;
        this.parent = null;
    }

    class OptionTreeModel
    implements TreeModel {
        private OptionGroup root;
        private EventListenerList listenerList;

        OptionTreeModel() {
            this.root = new OptionGroup("root");
            this.listenerList = new EventListenerList();
        }

        public void addTreeModelListener(TreeModelListener treeModelListener) {
            Class class_ = OptionsDialog.class$javax$swing$event$TreeModelListener == null ? (OptionsDialog.class$javax$swing$event$TreeModelListener = OptionsDialog.class$("javax.swing.event.TreeModelListener")) : OptionsDialog.class$javax$swing$event$TreeModelListener;
            this.listenerList.add(class_, treeModelListener);
        }

        public void removeTreeModelListener(TreeModelListener treeModelListener) {
            Class class_ = OptionsDialog.class$javax$swing$event$TreeModelListener == null ? (OptionsDialog.class$javax$swing$event$TreeModelListener = OptionsDialog.class$("javax.swing.event.TreeModelListener")) : OptionsDialog.class$javax$swing$event$TreeModelListener;
            this.listenerList.remove(class_, treeModelListener);
        }

        public Object getChild(Object object, int n) {
            if (object instanceof OptionGroup) {
                return ((OptionGroup)object).getMember(n);
            }
            return null;
        }

        public int getChildCount(Object object) {
            if (object instanceof OptionGroup) {
                return ((OptionGroup)object).getMemberCount();
            }
            return 0;
        }

        public int getIndexOfChild(Object object, Object object2) {
            if (object instanceof OptionGroup) {
                return ((OptionGroup)object).getMemberIndex(object2);
            }
            return -1;
        }

        public Object getRoot() {
            return this.root;
        }

        public boolean isLeaf(Object object) {
            return object instanceof OptionPane;
        }

        public void valueForPathChanged(TreePath treePath, Object object) {
        }

        protected void fireNodesChanged(Object object, Object[] arrobject, int[] arrn, Object[] arrobject2) {
            Object[] arrobject3 = this.listenerList.getListenerList();
            TreeModelEvent treeModelEvent = null;
            for (int i = arrobject3.length - 2; i >= 0; i-=2) {
                if (arrobject3[i] != (OptionsDialog.class$javax$swing$event$TreeModelListener == null ? OptionsDialog.class$("javax.swing.event.TreeModelListener") : OptionsDialog.class$javax$swing$event$TreeModelListener)) continue;
                if (treeModelEvent == null) {
                    treeModelEvent = new TreeModelEvent(object, arrobject, arrn, arrobject2);
                }
                ((TreeModelListener)arrobject3[i + 1]).treeNodesChanged(treeModelEvent);
            }
        }

        protected void fireNodesInserted(Object object, Object[] arrobject, int[] arrn, Object[] arrobject2) {
            Object[] arrobject3 = this.listenerList.getListenerList();
            TreeModelEvent treeModelEvent = null;
            for (int i = arrobject3.length - 2; i >= 0; i-=2) {
                if (arrobject3[i] != (OptionsDialog.class$javax$swing$event$TreeModelListener == null ? OptionsDialog.class$("javax.swing.event.TreeModelListener") : OptionsDialog.class$javax$swing$event$TreeModelListener)) continue;
                if (treeModelEvent == null) {
                    treeModelEvent = new TreeModelEvent(object, arrobject, arrn, arrobject2);
                }
                ((TreeModelListener)arrobject3[i + 1]).treeNodesInserted(treeModelEvent);
            }
        }

        protected void fireNodesRemoved(Object object, Object[] arrobject, int[] arrn, Object[] arrobject2) {
            Object[] arrobject3 = this.listenerList.getListenerList();
            TreeModelEvent treeModelEvent = null;
            for (int i = arrobject3.length - 2; i >= 0; i-=2) {
                if (arrobject3[i] != (OptionsDialog.class$javax$swing$event$TreeModelListener == null ? OptionsDialog.class$("javax.swing.event.TreeModelListener") : OptionsDialog.class$javax$swing$event$TreeModelListener)) continue;
                if (treeModelEvent == null) {
                    treeModelEvent = new TreeModelEvent(object, arrobject, arrn, arrobject2);
                }
                ((TreeModelListener)arrobject3[i + 1]).treeNodesRemoved(treeModelEvent);
            }
        }

        protected void fireTreeStructureChanged(Object object, Object[] arrobject, int[] arrn, Object[] arrobject2) {
            Object[] arrobject3 = this.listenerList.getListenerList();
            TreeModelEvent treeModelEvent = null;
            for (int i = arrobject3.length - 2; i >= 0; i-=2) {
                if (arrobject3[i] != (OptionsDialog.class$javax$swing$event$TreeModelListener == null ? OptionsDialog.class$("javax.swing.event.TreeModelListener") : OptionsDialog.class$javax$swing$event$TreeModelListener)) continue;
                if (treeModelEvent == null) {
                    treeModelEvent = new TreeModelEvent(object, arrobject, arrn, arrobject2);
                }
                ((TreeModelListener)arrobject3[i + 1]).treeStructureChanged(treeModelEvent);
            }
        }
    }

    class PaneNameRenderer
    extends JLabel
    implements TreeCellRenderer {
        private Border noFocusBorder;
        private Border focusBorder;
        private Font paneFont;
        private Font groupFont;

        public PaneNameRenderer() {
            this.noFocusBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);
            this.focusBorder = BorderFactory.createLineBorder(UIManager.getColor("Tree.selectionBorderColor"));
            this.setOpaque(true);
            this.paneFont = UIManager.getFont("Tree.font");
            this.groupFont = new Font(this.paneFont.getName(), this.paneFont.getStyle() | 1, this.paneFont.getSize());
        }

        public Component getTreeCellRendererComponent(JTree jTree, Object object, boolean bl, boolean bl2, boolean bl3, int n, boolean bl4) {
            if (bl) {
                this.setBackground(UIManager.getColor("Tree.selectionBackground"));
                this.setForeground(UIManager.getColor("Tree.selectionForeground"));
            } else {
                this.setBackground(jTree.getBackground());
                this.setForeground(jTree.getForeground());
            }
            String string = null;
            if (object instanceof OptionGroup) {
                string = ((OptionGroup)object).getName();
                this.setFont(this.groupFont);
            } else if (object instanceof OptionPane) {
                string = ((OptionPane)object).getName();
                this.setFont(this.paneFont);
            }
            if (string == null) {
                this.setText(null);
            } else {
                String string2 = Jext.getProperty("options." + string + ".label");
                if (string2 == null) {
                    this.setText(string);
                } else {
                    this.setText(string2);
                }
            }
            this.setBorder(bl4 ? this.focusBorder : this.noFocusBorder);
            return this;
        }
    }

    class MouseHandler
    extends MouseAdapter {
        MouseHandler() {
        }

        public void mouseClicked(MouseEvent mouseEvent) {
            TreePath treePath = OptionsDialog.this.paneTree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
            if (treePath == null) {
                return;
            }
            Object object = treePath.getLastPathComponent();
            if (object instanceof OptionGroup) {
                if (OptionsDialog.this.paneTree.isCollapsed(treePath)) {
                    OptionsDialog.this.paneTree.expandPath(treePath);
                } else {
                    OptionsDialog.this.paneTree.collapsePath(treePath);
                }
            }
        }
    }

}

