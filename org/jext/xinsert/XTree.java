/*
 * Decompiled with CFR 0_102.
 */
package org.jext.xinsert;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.Utilities;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.event.JextEvent;
import org.jext.event.JextListener;
import org.jext.gui.JextCheckBox;
import org.jext.gui.JextHighlightButton;
import org.jext.misc.Indent;
import org.jext.scripting.dawn.Run;
import org.jext.xinsert.XTreeItem;
import org.jext.xinsert.XTreeNode;
import org.jext.xinsert.XTreeObject;
import org.jext.xml.XInsertReader;

public class XTree
extends JPanel
implements TreeSelectionListener,
ActionListener,
Runnable,
JextListener {
    private static Vector inserts;
    private String file;
    private String currentMode;
    private JTree tree;
    private JextFrame parent;
    private DefaultTreeModel treeModel;
    private JextHighlightButton expand;
    private JextHighlightButton collapse;
    private JextHighlightButton reload;
    private JextCheckBox carriageReturn;
    private JextCheckBox executeScript;
    private JextCheckBox textSurrounding;
    private int rootIndex;
    private XTreeNode root;
    private Stack menuStack = null;
    private XTreeObject xtreeObj = null;
    private static final ImageIcon[] leaves;

    public void addMenu(String string, String string2) {
        this.xtreeObj = new XTreeObject(new XTreeNode(string, string2), 0);
        if (this.menuStack.empty()) {
            this.treeModel.insertNodeInto(this.xtreeObj.getXTreeNode(), this.root, this.rootIndex);
            ++this.rootIndex;
        } else {
            XTreeObject xTreeObject = (XTreeObject)this.menuStack.peek();
            this.treeModel.insertNodeInto(this.xtreeObj.getXTreeNode(), xTreeObject.getXTreeNode(), xTreeObject.getIndex());
            xTreeObject.incrementIndex();
        }
        this.menuStack.push(this.xtreeObj);
    }

    public void closeMenu() {
        try {
            this.xtreeObj = (XTreeObject)this.menuStack.pop();
        }
        catch (Exception var1_1) {
            this.xtreeObj = null;
        }
    }

    public void addInsert(String string, String string2, int n) {
        inserts.addElement(new XTreeItem(string2, n));
        XTreeNode xTreeNode = new XTreeNode(string, null, inserts.size());
        if (this.xtreeObj == null) {
            this.treeModel.insertNodeInto(xTreeNode, this.root, this.rootIndex);
            ++this.rootIndex;
        } else {
            XTreeObject xTreeObject = (XTreeObject)this.menuStack.peek();
            this.treeModel.insertNodeInto(xTreeNode, xTreeObject.getXTreeNode(), xTreeObject.getIndex());
            xTreeObject.incrementIndex();
        }
    }

    public XTree(JextFrame jextFrame, String string) {
        this.parent = jextFrame;
        jextFrame.addJextListener(this);
        this.setLayout(new BorderLayout());
        this.root = new XTreeNode("XInsert");
        this.treeModel = new DefaultTreeModel(this.root);
        this.tree = new JTree(this.treeModel);
        this.tree.addTreeSelectionListener(this);
        this.tree.putClientProperty("JTree.lineStyle", "Angled");
        if (!Jext.getBooleanProperty("useSkin")) {
            this.tree.setCellRenderer(new XTreeCellRenderer());
        }
        this.init(string);
        String string2 = Jext.getProperty("jext.look.icons");
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
        Class class_ = Jext.class;
        this.collapse = new JextHighlightButton(Jext.getProperty("xtree.collapse.button"), Utilities.getIcon("images/button_collapse" + string2 + ".gif", class_));
        jToolBar.add(this.collapse);
        this.collapse.setMnemonic(Jext.getProperty("xtree.collapse.mnemonic").charAt(0));
        this.collapse.addActionListener(this);
        this.expand = new JextHighlightButton(Jext.getProperty("xtree.expand.button"), Utilities.getIcon("images/button_expand" + string2 + ".gif", Jext.class));
        jToolBar.add(this.expand);
        this.expand.setMnemonic(Jext.getProperty("xtree.expand.mnemonic").charAt(0));
        this.expand.addActionListener(this);
        this.reload = new JextHighlightButton(Jext.getProperty("xtree.reload.button"), Utilities.getIcon("images/menu_reload" + string2 + ".gif", Jext.class));
        jToolBar.add(this.reload);
        this.reload.setMnemonic(Jext.getProperty("xtree.reload.mnemonic").charAt(0));
        this.reload.addActionListener(this);
        this.add((Component)jToolBar, "North");
        this.add((Component)new JScrollPane(this.tree), "Center");
        JPanel jPanel = new JPanel(new BorderLayout());
        this.carriageReturn = new JextCheckBox(Jext.getProperty("xtree.carriage.label"));
        jPanel.add((Component)this.carriageReturn, "North");
        this.carriageReturn.setSelected(Jext.getBooleanProperty("carriage"));
        this.carriageReturn.addActionListener(this);
        this.executeScript = new JextCheckBox(Jext.getProperty("xtree.execute.label"));
        jPanel.add((Component)this.executeScript, "Center");
        this.executeScript.setSelected(Jext.getBooleanProperty("execute"));
        if (Jext.getProperty("execute") == null) {
            this.executeScript.setSelected(true);
        }
        this.executeScript.addActionListener(this);
        this.textSurrounding = new JextCheckBox(Jext.getProperty("xtree.surrounding.label"));
        jPanel.add((Component)this.textSurrounding, "South");
        this.textSurrounding.setSelected(Jext.getBooleanProperty("surrounding"));
        if (Jext.getProperty("surrounding") == null) {
            this.textSurrounding.setSelected(true);
        }
        this.textSurrounding.addActionListener(this);
        this.add((Component)jPanel, "South");
    }

    private void init(String string) {
        this.init(string, true);
    }

    private void init(String string, boolean bl) {
        this.file = string;
        if (bl) {
            Thread thread = new Thread((Runnable)this, "--XTree builder thread");
            thread.start();
        } else {
            this.run();
        }
    }

    public void stop() {
    }

    public void run() {
        inserts = new Vector(200);
        this.menuStack = new Stack();
        this.rootIndex = 0;
        Class class_ = Jext.class;
        if (XInsertReader.read(this, class_.getResourceAsStream(this.file), this.file)) {
            this.build();
            SwingUtilities.invokeLater(new Runnable(){

                public void run() {
                    XTree.this.associateXTreeToMode(false);
                }
            });
        }
        this.tree.expandRow(0);
        this.tree.setRootVisible(false);
        this.tree.setShowsRootHandles(true);
        this.file = null;
    }

    private void build() {
        String string = Jext.SETTINGS_DIRECTORY + "xinsert" + File.separator;
        String[] arrstring = Utilities.getWildCardMatches(string, "*.insert.xml", false);
        if (arrstring == null) {
            return;
        }
        try {
            for (int i = 0; i < arrstring.length; ++i) {
                String string2 = string + arrstring[i];
                if (!XInsertReader.read(this, new FileInputStream(string2), string2)) continue;
                Object[] arrobject = new String[]{arrstring[i]};
                System.out.println(Jext.getProperty("xtree.loaded", arrobject));
            }
        }
        catch (FileNotFoundException var3_5) {
            // empty catch block
        }
    }

    public void jextEventFired(JextEvent jextEvent) {
        int n = jextEvent.getWhat();
        if (n == 1 || n == 77 || n == 98) {
            this.associateXTreeToMode();
        }
    }

    private void associateXTreeToMode() {
        this.associateXTreeToMode(true);
    }

    private void associateXTreeToMode(boolean bl) {
        JextTextArea jextTextArea = this.parent.getTextArea();
        if (jextTextArea == null) {
            return;
        }
        String string = jextTextArea.getColorizingMode();
        if (bl && string.equals(this.currentMode)) {
            return;
        }
        int n = 0;
        XTreeNode xTreeNode = new XTreeNode("XInsert");
        DefaultTreeModel defaultTreeModel = new DefaultTreeModel(xTreeNode);
        for (int i = 0; i < this.root.getChildCount(); ++i) {
            XTreeNode xTreeNode2 = (XTreeNode)this.root.getChildAt(i);
            if (!xTreeNode2.isAssociatedToMode(string)) continue;
            xTreeNode2.setParent(null);
            if (xTreeNode2.isPermanent()) {
                xTreeNode.add(xTreeNode2);
                continue;
            }
            if (xTreeNode2.toString().equalsIgnoreCase(string)) {
                xTreeNode.insert(xTreeNode2, 0);
            } else {
                xTreeNode.insert(xTreeNode2, n);
            }
            ++n;
        }
        this.tree.setModel(defaultTreeModel);
        this.tree.expandRow(0);
        this.currentMode = string;
    }

    public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
        JTree jTree = (JTree)treeSelectionEvent.getSource();
        if (jTree.isSelectionEmpty()) {
            return;
        }
        XTreeNode xTreeNode = (XTreeNode)jTree.getSelectionPath().getLastPathComponent();
        if (xTreeNode.getIndex() == -1) {
            this.parent.getTextArea().grabFocus();
            return;
        }
        try {
            this.insert(xTreeNode.getIndex() - 1);
        }
        catch (Exception var4_4) {
            // empty catch block
        }
        jTree.setSelectionPath(jTree.getPathForRow(-1));
    }

    public void reload(DefaultTreeModel defaultTreeModel) {
        this.treeModel = defaultTreeModel;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        if (object == this.expand) {
            for (int i = 0; i < this.tree.getRowCount(); ++i) {
                this.tree.expandRow(i);
            }
            return;
        }
        if (object == this.collapse) {
            for (int i = this.tree.getRowCount(); i >= 0; --i) {
                this.tree.collapseRow(i);
            }
            return;
        } else {
            if (object == this.reload) {
                this.root.removeAllChildren();
                this.treeModel.reload();
                this.init("jext.insert.xml", false);
                this.associateXTreeToMode(false);
                ArrayList arrayList = Jext.getInstances();
                for (int i = 0; i < arrayList.size(); ++i) {
                    JextFrame jextFrame = (JextFrame)arrayList.get(i);
                    if (jextFrame == this.parent) continue;
                    jextFrame.getXTree().reload(this.treeModel);
                    jextFrame.getXTree().associateXTreeToMode(false);
                }
                return;
            }
            if (object == this.carriageReturn) {
                Jext.setProperty("carriage", this.carriageReturn.isSelected() ? "on" : "off");
                return;
            } else if (object == this.executeScript) {
                Jext.setProperty("execute", this.executeScript.isSelected() ? "on" : "off");
                return;
            } else {
                if (object != this.textSurrounding) return;
                Jext.setProperty("surrounding", this.textSurrounding.isSelected() ? "on" : "off");
            }
        }
    }

    private void insert(int n) throws BadLocationException {
        char c = '\u0000';
        XTreeItem xTreeItem = (XTreeItem)inserts.elementAt(n);
        String string = xTreeItem.getContent();
        boolean bl = xTreeItem.isScript();
        boolean bl2 = xTreeItem.isMixed();
        StringBuffer stringBuffer = new StringBuffer(string.length());
        block15 : for (int i = 0; i < string.length(); ++i) {
            c = string.charAt(i);
            switch (c) {
                case '\\': {
                    if (i < string.length() - 1) {
                        switch (string.charAt(i + 1)) {
                            case 'n': {
                                ++i;
                                stringBuffer.append('\n');
                                continue block15;
                            }
                            case 't': {
                                ++i;
                                stringBuffer.append('\t');
                                continue block15;
                            }
                            case '\\': {
                                ++i;
                                stringBuffer.append(c);
                            }
                        }
                        continue block15;
                    }
                    stringBuffer.append(c);
                    continue block15;
                }
                default: {
                    stringBuffer.append(c);
                }
            }
        }
        string = stringBuffer.toString();
        JextTextArea jextTextArea = this.parent.getTextArea();
        if (bl && this.executeScript.isSelected()) {
            Run.execute(string, this.parent);
        } else {
            int n2;
            int n3;
            jextTextArea.beginProtectedCompoundEdit();
            boolean bl3 = JextTextArea.getEnterIndent();
            SyntaxDocument syntaxDocument = jextTextArea.getDocument();
            String string2 = "";
            if (jextTextArea.getSelectionStart() != jextTextArea.getSelectionEnd()) {
                string2 = jextTextArea.getSelectedText();
                jextTextArea.setSelectedText("");
            }
            stringBuffer = new StringBuffer(string.length());
            int n4 = 0;
            int n5 = 0;
            int n6 = -1;
            int n7 = jextTextArea.getCaretPosition();
            int n8 = string.length();
            StringBuffer stringBuffer2 = new StringBuffer(30);
            boolean bl4 = false;
            boolean bl5 = true;
            boolean bl6 = false;
            block16 : for (n2 = 0; n2 < string.length(); ++n2) {
                c = string.charAt(n2);
                switch (c) {
                    case '|': {
                        if (bl4) {
                            stringBuffer2.append('|');
                            continue block16;
                        }
                        if (n2 < string.length() - 1 && string.charAt(n2 + 1) == '|') {
                            ++n2;
                            stringBuffer.append('|');
                            continue block16;
                        }
                        if (n4 != 0) continue block16;
                        n8 = n5 + stringBuffer.length();
                        n4 = 1;
                        if (!bl5) continue block16;
                        bl6 = true;
                        continue block16;
                    }
                    case '\n': {
                        if (bl4) {
                            stringBuffer2.append('\n');
                            continue block16;
                        }
                        if (bl3 && !bl5) {
                            syntaxDocument.insertString(n7 + n5, stringBuffer.toString(), null);
                            n5+=stringBuffer.length();
                            stringBuffer = new StringBuffer(string.length() - stringBuffer.length());
                            n3 = syntaxDocument.getLength();
                            Indent.indent(jextTextArea, jextTextArea.getCaretLine(), true, true);
                            int n9 = syntaxDocument.getLength() - n3;
                            if (n4 == 1) {
                                if (!bl6) {
                                    n8+=n9;
                                }
                                n4 = 2;
                            }
                            n5+=n9;
                            bl6 = false;
                        }
                        stringBuffer.append('\n');
                        bl5 = false;
                        n6 = n2;
                        continue block16;
                    }
                    case '%': {
                        if (bl2) {
                            if (n2 < string.length() - 1 && string.charAt(n2 + 1) == '%') {
                                ++n2;
                                (bl4 ? stringBuffer2 : stringBuffer).append('%');
                                continue block16;
                            }
                            if (bl4) {
                                bl4 = false;
                                try {
                                    if (!DawnParser.isInitialized()) {
                                        DawnParser.init();
                                        DawnParser.installPackage(class$org$jext$Jext == null ? XTree.class$("org.jext.Jext") : class$org$jext$Jext, "dawn-jext.scripting");
                                    }
                                    DawnParser dawnParser = new DawnParser(new StringReader(stringBuffer2.toString()));
                                    dawnParser.setProperty("JEXT.JEXT_FRAME", this.parent);
                                    dawnParser.exec();
                                    if (!dawnParser.getStack().isEmpty()) {
                                        stringBuffer.append(dawnParser.popString());
                                    }
                                }
                                catch (DawnRuntimeException var22_25) {
                                    JOptionPane.showMessageDialog(this.parent, var22_25.getMessage(), Jext.getProperty("dawn.script.error"), 0);
                                }
                                stringBuffer2 = new StringBuffer(30);
                                continue block16;
                            }
                            bl4 = true;
                            continue block16;
                        }
                    }
                    default: {
                        (bl4 ? stringBuffer2 : stringBuffer).append(c);
                    }
                }
            }
            syntaxDocument.insertString(n7 + n5, stringBuffer.toString(), null);
            if (!bl5) {
                n2 = syntaxDocument.getLength();
                Indent.indent(jextTextArea, jextTextArea.getCaretLine(), true, true);
                if (n6 < n8 && n4 <= 1) {
                    n8+=syntaxDocument.getLength() - n2;
                }
            }
            if ((n2 = n7 + n8) > (n3 = syntaxDocument.getLength())) {
                n2 = n3;
            }
            if (string2.length() > 0 && this.textSurrounding.isSelected()) {
                syntaxDocument.insertString(n2, string2, null);
            }
            jextTextArea.setCaretPosition(n2);
            jextTextArea.endProtectedCompoundEdit();
        }
        jextTextArea.grabFocus();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.file = null;
        this.currentMode = null;
        this.tree = null;
        this.parent = null;
        this.treeModel = null;
        this.expand = null;
        this.collapse = null;
        this.reload = null;
        this.carriageReturn = null;
        this.executeScript = null;
        this.textSurrounding = null;
        this.root = null;
        this.menuStack.clear();
        this.menuStack = null;
        this.xtreeObj = null;
    }

    static {
        ImageIcon[] arrimageIcon = new ImageIcon[3];
        Class class_ = Jext.class;
        arrimageIcon[0] = Utilities.getIcon("images/tree_leaf.gif", class_);
        arrimageIcon[1] = Utilities.getIcon("images/tree_leaf_script.gif", Jext.class);
        arrimageIcon[2] = Utilities.getIcon("images/tree_leaf_mixed.gif", Jext.class);
        leaves = arrimageIcon;
    }

    class XTreeCellRenderer
    extends DefaultTreeCellRenderer {
        XTreeCellRenderer() {
            Class class_ = XTree.class$org$jext$Jext == null ? (XTree.class$org$jext$Jext = XTree.class$("org.jext.Jext")) : XTree.class$org$jext$Jext;
            this.openIcon = Utilities.getIcon("images/tree_open.gif", class_);
            this.closedIcon = Utilities.getIcon("images/tree_close.gif", XTree.class$org$jext$Jext == null ? (XTree.class$org$jext$Jext = XTree.class$("org.jext.Jext")) : XTree.class$org$jext$Jext);
            this.textSelectionColor = Color.red;
            this.borderSelectionColor = XTree.this.tree.getBackground();
            this.backgroundSelectionColor = XTree.this.tree.getBackground();
        }

        public Component getTreeCellRendererComponent(JTree jTree, Object object, boolean bl, boolean bl2, boolean bl3, int n, boolean bl4) {
            TreePath treePath;
            int n2;
            XTreeNode xTreeNode;
            if (bl3 && (treePath = jTree.getPathForRow(n)) != null && (n2 = (xTreeNode = (XTreeNode)treePath.getLastPathComponent()).getIndex()) != -1) {
                this.leafIcon = leaves[((XTreeItem)inserts.elementAt(n2 - 1)).getType()];
            }
            return super.getTreeCellRendererComponent(jTree, object, bl, bl2, bl3, n, bl4);
        }
    }

}

