/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  com.microstar.xml.HandlerBase
 *  com.microstar.xml.XmlHandler
 *  com.microstar.xml.XmlParser
 */
package org.jext.misc;

import com.microstar.xml.HandlerBase;
import com.microstar.xml.XmlHandler;
import com.microstar.xml.XmlParser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.jext.GUIUtilities;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.Utilities;
import org.jext.event.JextEvent;
import org.jext.event.JextListener;
import org.jext.gui.EnhancedMenuItem;
import org.jext.gui.JextHighlightButton;

public class VirtualFolders
extends JPanel
implements ActionListener,
JextListener,
TreeSelectionListener {
    private JextFrame parent;
    private JextHighlightButton deleteItem;
    private JextHighlightButton openFile;
    private JextHighlightButton addFile;
    private JextHighlightButton addAllFiles;
    private JextHighlightButton newFolder;
    private JPopupMenu popup;
    private EnhancedMenuItem deleteM;
    private EnhancedMenuItem openFileM;
    private EnhancedMenuItem addFileM;
    private EnhancedMenuItem addAllFilesM;
    private EnhancedMenuItem newFolderM;
    private JTree tree;
    private DefaultTreeModel treeModel;
    private VirtualFolderNode root;

    public VirtualFolders(JextFrame jextFrame) {
        super(new BorderLayout());
        this.parent = jextFrame;
        jextFrame.addJextListener(this);
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
        ImageIcon imageIcon = null;
        this.popup = new JPopupMenu();
        this.openFileM = new EnhancedMenuItem(Jext.getProperty("vf.open.label"));
        this.popup.add(this.openFileM);
        if (Jext.getProperty("vf.open.picture") != null) {
            Class class_ = Jext.class;
            imageIcon = Utilities.getIcon(Jext.getProperty("vf.open.picture").concat(Jext.getProperty("jext.look.icons")).concat(".gif"), class_);
            if (imageIcon != null) {
                this.openFileM.setIcon(imageIcon);
            }
        }
        this.openFileM.addActionListener(this);
        this.openFile = new JextHighlightButton(imageIcon);
        jToolBar.add(this.openFile);
        this.openFile.setToolTipText(Jext.getProperty("vf.open.tooltip"));
        this.openFile.addActionListener(this);
        Dimension dimension = new Dimension(this.openFile.getMaximumSize().height, this.openFile.getMaximumSize().height);
        this.openFile.setMaximumSize(dimension);
        this.openFile.setEnabled(false);
        this.openFileM.setEnabled(false);
        this.newFolderM = new EnhancedMenuItem(Jext.getProperty("vf.new.label"));
        this.popup.add(this.newFolderM);
        if (Jext.getProperty("vf.new.picture") != null && (imageIcon = Utilities.getIcon(Jext.getProperty("vf.new.picture").concat(Jext.getProperty("jext.look.icons")).concat(".gif"), Jext.class)) != null) {
            this.newFolderM.setIcon(imageIcon);
        }
        this.newFolderM.addActionListener(this);
        this.newFolder = new JextHighlightButton(imageIcon);
        jToolBar.add(this.newFolder);
        this.newFolder.setToolTipText(Jext.getProperty("vf.new.tooltip"));
        this.newFolder.addActionListener(this);
        this.newFolder.setMaximumSize(dimension);
        this.addFileM = new EnhancedMenuItem(Jext.getProperty("vf.add.label"));
        this.popup.add(this.addFileM);
        if (Jext.getProperty("vf.add.picture") != null && (imageIcon = Utilities.getIcon(Jext.getProperty("vf.add.picture").concat(Jext.getProperty("jext.look.icons")).concat(".gif"), Jext.class)) != null) {
            this.addFileM.setIcon(imageIcon);
        }
        this.addFileM.addActionListener(this);
        this.addFile = new JextHighlightButton(imageIcon);
        jToolBar.add(this.addFile);
        this.addFile.setToolTipText(Jext.getProperty("vf.add.tooltip"));
        this.addFile.addActionListener(this);
        this.addFile.setMaximumSize(dimension);
        this.addAllFilesM = new EnhancedMenuItem(Jext.getProperty("vf.addall.label"));
        this.popup.add(this.addAllFilesM);
        if (Jext.getProperty("vf.addall.picture") != null && (imageIcon = Utilities.getIcon(Jext.getProperty("vf.addall.picture").concat(Jext.getProperty("jext.look.icons")).concat(".gif"), Jext.class)) != null) {
            this.addAllFilesM.setIcon(imageIcon);
        }
        this.addAllFilesM.addActionListener(this);
        this.addAllFiles = new JextHighlightButton(imageIcon);
        jToolBar.add(this.addAllFiles);
        this.addAllFiles.setToolTipText(Jext.getProperty("vf.addall.tooltip"));
        this.addAllFiles.addActionListener(this);
        this.addAllFiles.setMaximumSize(dimension);
        this.deleteM = new EnhancedMenuItem(Jext.getProperty("vf.delete.label"));
        this.popup.add(this.deleteM);
        if (Jext.getProperty("vf.delete.picture") != null && (imageIcon = Utilities.getIcon(Jext.getProperty("vf.delete.picture").concat(Jext.getProperty("jext.look.icons")).concat(".gif"), Jext.class)) != null) {
            this.deleteM.setIcon(imageIcon);
        }
        this.deleteM.addActionListener(this);
        this.deleteItem = new JextHighlightButton(imageIcon);
        jToolBar.add(this.deleteItem);
        this.deleteItem.setToolTipText(Jext.getProperty("vf.delete.tooltip"));
        this.deleteItem.addActionListener(this);
        this.deleteItem.setMaximumSize(dimension);
        this.deleteItem.setEnabled(false);
        this.deleteM.setEnabled(false);
        jToolBar.setMaximumSize(new Dimension(dimension.width * 5, dimension.height));
        this.add((Component)jToolBar, "North");
        this.root = new VirtualFolderNode("VirtualFolders", false);
        this.treeModel = new DefaultTreeModel(this.root);
        this.tree = new JTree(this.treeModel);
        new DropTarget(this.tree, new DnDHandler());
        DefaultTreeCellRenderer defaultTreeCellRenderer = new DefaultTreeCellRenderer();
        defaultTreeCellRenderer.setOpenIcon(Utilities.getIcon("images/tree_open.gif", Jext.class));
        defaultTreeCellRenderer.setLeafIcon(Utilities.getIcon("images/tree_leaf.gif", Jext.class));
        defaultTreeCellRenderer.setClosedIcon(Utilities.getIcon("images/tree_close.gif", Jext.class));
        defaultTreeCellRenderer.setTextSelectionColor(GUIUtilities.parseColor(Jext.getProperty("vf.selectionColor")));
        defaultTreeCellRenderer.setBackgroundSelectionColor(this.tree.getBackground());
        defaultTreeCellRenderer.setBorderSelectionColor(this.tree.getBackground());
        this.tree.addMouseListener(new MouseHandler());
        this.tree.setCellRenderer(defaultTreeCellRenderer);
        this.tree.putClientProperty("JTree.lineStyle", "Angled");
        this.tree.setScrollsOnExpand(true);
        DefaultTreeSelectionModel defaultTreeSelectionModel = new DefaultTreeSelectionModel();
        defaultTreeSelectionModel.setSelectionMode(4);
        this.tree.setSelectionModel(defaultTreeSelectionModel);
        this.load();
        this.tree.clearSelection();
        this.tree.setRootVisible(false);
        this.tree.setShowsRootHandles(true);
        this.tree.addKeyListener(new KeyHandler());
        this.tree.addTreeSelectionListener(this);
        this.fixVisible();
        this.tree.expandPath(new TreePath(this.root.getPath()));
        JScrollPane jScrollPane = new JScrollPane(this.tree);
        jScrollPane.setBorder(null);
        this.add((Component)jScrollPane, "Center");
    }

    public void jextEventFired(JextEvent jextEvent) {
        if (jextEvent.getWhat() == 101) {
            this.save();
        }
    }

    private String toXML(VirtualFolderNode virtualFolderNode, int n) {
        String string = System.getProperty("line.separator");
        StringBuffer stringBuffer = new StringBuffer();
        if (virtualFolderNode.isLeaf() && virtualFolderNode != this.root) {
            TreePath treePath = new TreePath(virtualFolderNode.getPath());
            String string2 = this.tree.isVisible(treePath) ? " visible=\"yes\"" : "";
            stringBuffer.append(string).append(this.getIndentation(n + 1)).append("<file path=\"" + virtualFolderNode.filePath + "\"" + string2 + " />");
        } else {
            if (virtualFolderNode != this.root) {
                stringBuffer.append(string).append(this.getIndentation(n)).append("<folder name=\"" + virtualFolderNode.toString() + "\">");
            } else {
                stringBuffer.append(string).append("<folderlist>");
            }
            Enumeration enumeration = virtualFolderNode.children();
            while (enumeration.hasMoreElements()) {
                VirtualFolderNode virtualFolderNode2 = (VirtualFolderNode)enumeration.nextElement();
                stringBuffer.append(this.toXML(virtualFolderNode2, n + 1));
            }
            if (virtualFolderNode != this.root) {
                stringBuffer.append(string).append(this.getIndentation(n)).append("</folder>");
            } else {
                stringBuffer.append(string).append("</folderlist>");
            }
        }
        return stringBuffer.toString();
    }

    private String getIndentation(int n) {
        return Utilities.createWhiteSpace(n * 2);
    }

    private void save() {
        try {
            File file = new File(Jext.SETTINGS_DIRECTORY + File.separator + ".vf.xml");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            String string = this.toXML(this.root, 1);
            if (string.length() == 0) {
                string = "<folderlist />";
            }
            string = "<?xml version=\"1.0\"?>" + string;
            bufferedWriter.write(string, 0, string.length());
            bufferedWriter.flush();
            bufferedWriter.newLine();
            bufferedWriter.close();
        }
        catch (Exception var1_2) {
            var1_2.printStackTrace();
        }
    }

    private void load() {
        try {
            Object object;
            File file = new File(Jext.SETTINGS_DIRECTORY + File.separator + ".vf.xml");
            StringBuffer stringBuffer = new StringBuffer((int)file.length());
            if (file.exists() && file.length() > 0) {
                try {
                    BufferedReader stringReader = new BufferedReader(new FileReader(file));
                    object = stringReader.readLine();
                    while (object != null) {
                        stringBuffer.append((String)object);
                        object = stringReader.readLine();
                    }
                    stringReader.close();
                }
                catch (Exception var3_5) {
                    stringBuffer = new StringBuffer("<?xml version=\"1.0\"?><folderlist />");
                }
            } else {
                stringBuffer = new StringBuffer("<?xml version=\"1.0\"?><folderlist />");
            }
            StringReader stringReader = new StringReader(stringBuffer.toString());
            object = new XmlParser();
            object.setHandler((XmlHandler)new VirtualFoldersHandler());
            object.parse(null, null, (Reader)stringReader);
        }
        catch (Exception var1_2) {
            // empty catch block
        }
    }

    private void fixVisible() {
        TreePath treePath;
        Enumeration enumeration = this.root.depthFirstEnumeration();
        VirtualFolderNode virtualFolderNode = null;
        while (enumeration.hasMoreElements()) {
            virtualFolderNode = (VirtualFolderNode)enumeration.nextElement();
            treePath = new TreePath(virtualFolderNode.getPath());
            this.tree.collapsePath(treePath);
        }
        enumeration = this.root.depthFirstEnumeration();
        while (enumeration.hasMoreElements()) {
            virtualFolderNode = (VirtualFolderNode)enumeration.nextElement();
            if (!virtualFolderNode.shouldBeVisible()) continue;
            treePath = new TreePath(((VirtualFolderNode)virtualFolderNode.getParent()).getPath());
            this.tree.expandPath(treePath);
        }
    }

    private VirtualFolderNode createFolder(String string) {
        return this.createFolder(string, false);
    }

    private VirtualFolderNode createFolder(String string, boolean bl) {
        return this.createFolder(string, bl, this.root);
    }

    private VirtualFolderNode createFolder(String string, boolean bl, VirtualFolderNode virtualFolderNode) {
        if (VirtualFolders.folderExists(virtualFolderNode, string)) {
            return null;
        }
        VirtualFolderNode virtualFolderNode2 = new VirtualFolderNode(string, false);
        this.treeModel.insertNodeInto(virtualFolderNode2, virtualFolderNode, virtualFolderNode.getChildCount());
        TreePath treePath = new TreePath(virtualFolderNode2.getPath());
        this.tree.setSelectionPath(treePath);
        if (bl) {
            this.tree.expandPath(treePath);
        } else {
            this.tree.collapsePath(treePath);
        }
        return virtualFolderNode2;
    }

    private VirtualFolderNode createLeaf(VirtualFolderNode virtualFolderNode, String string) {
        if (virtualFolderNode == null || string == null) {
            return null;
        }
        Enumeration enumeration = virtualFolderNode.children();
        while (enumeration.hasMoreElements()) {
            if (!((VirtualFolderNode)enumeration.nextElement()).getFilePath().equalsIgnoreCase(string)) continue;
            return null;
        }
        VirtualFolderNode virtualFolderNode2 = new VirtualFolderNode(string, true);
        this.treeModel.insertNodeInto(virtualFolderNode2, virtualFolderNode, virtualFolderNode.getChildCount());
        return virtualFolderNode2;
    }

    public static boolean folderExists(VirtualFolderNode virtualFolderNode, String string) {
        boolean bl = false;
        Enumeration enumeration = virtualFolderNode.children();
        while (enumeration.hasMoreElements() && !bl) {
            VirtualFolderNode virtualFolderNode2 = (VirtualFolderNode)enumeration.nextElement();
            bl = virtualFolderNode2.toString().equals(string);
        }
        return bl;
    }

    private void newFolder() {
        TreePath[] arrtreePath = this.tree.getSelectionPaths();
        VirtualFolderNode virtualFolderNode = null;
        virtualFolderNode = arrtreePath == null || arrtreePath.length == 0 ? this.root : (VirtualFolderNode)arrtreePath[0].getLastPathComponent();
        if (virtualFolderNode.isLeaf() && !virtualFolderNode.isRoot()) {
            virtualFolderNode = (VirtualFolderNode)virtualFolderNode.getParent();
        }
        this.newFolder(virtualFolderNode);
    }

    private void newFolder(VirtualFolderNode virtualFolderNode) {
        String string = JOptionPane.showInputDialog(this.parent, Jext.getProperty("vf.add.input.msg"), Jext.getProperty("vf.add.input.title"), 3);
        if (string != null && string.length() > 0 && this.createFolder(string, true, virtualFolderNode) == null) {
            GUIUtilities.message(this.parent, "vf.folder.exists", null);
        }
    }

    private void removeItem() {
        VirtualFolderNode virtualFolderNode;
        int n;
        TreePath[] arrtreePath = this.tree.getSelectionPaths();
        if (arrtreePath != null) {
            for (n = 0; n < arrtreePath.length; ++n) {
                virtualFolderNode = (VirtualFolderNode)arrtreePath[n].getLastPathComponent();
                this.treeModel.removeNodeFromParent(virtualFolderNode);
            }
        }
        if ((n = this.root.getChildCount() - 1) >= 0) {
            virtualFolderNode = (VirtualFolderNode)this.root.getChildAt(n);
            this.tree.setSelectionPath(new TreePath(virtualFolderNode.getPath()));
        }
    }

    private void addFile() {
        JextTextArea jextTextArea = this.parent.getNSTextArea();
        if (jextTextArea.isNew()) {
            return;
        }
        this.addFile(jextTextArea.getCurrentFile());
    }

    private void addFile(String string) {
        TreePath treePath = this.tree.getSelectionPath();
        VirtualFolderNode virtualFolderNode = null;
        if (treePath == null) {
            virtualFolderNode = this.root;
        } else {
            virtualFolderNode = (VirtualFolderNode)treePath.getLastPathComponent();
            if (virtualFolderNode.isLeaf()) {
                virtualFolderNode = (VirtualFolderNode)virtualFolderNode.getParent();
            }
        }
        if (this.createLeaf(virtualFolderNode, string) == null) {
            GUIUtilities.message(this.parent, "vf.item.exists", null);
        }
    }

    private void addAllFiles() {
        TreePath treePath = this.tree.getSelectionPath();
        VirtualFolderNode virtualFolderNode = null;
        if (treePath == null) {
            virtualFolderNode = this.root;
        } else {
            virtualFolderNode = (VirtualFolderNode)treePath.getLastPathComponent();
            if (virtualFolderNode.isLeaf()) {
                virtualFolderNode = (VirtualFolderNode)virtualFolderNode.getParent();
            }
        }
        JextTextArea[] arrjextTextArea = this.parent.getTextAreas();
        for (int i = 0; i < arrjextTextArea.length; ++i) {
            if (arrjextTextArea[i].isNew() || this.createLeaf(virtualFolderNode, arrjextTextArea[i].getCurrentFile()) != null) continue;
            GUIUtilities.message(this.parent, "vf.item.exists", null);
        }
    }

    private void openSelection(boolean bl) {
        TreePath[] arrtreePath = this.tree.getSelectionPaths();
        if (arrtreePath != null) {
            for (int i = 0; i < arrtreePath.length; ++i) {
                VirtualFolderNode virtualFolderNode = (VirtualFolderNode)arrtreePath[i].getLastPathComponent();
                this.openNode(virtualFolderNode, bl);
            }
        }
    }

    public void openNode(VirtualFolderNode virtualFolderNode, boolean bl) {
        if (virtualFolderNode.isLeaf()) {
            this.parent.open(virtualFolderNode.getFilePath());
        } else if (bl) {
            Enumeration enumeration = virtualFolderNode.children();
            while (enumeration.hasMoreElements()) {
                VirtualFolderNode virtualFolderNode2 = (VirtualFolderNode)enumeration.nextElement();
                this.openNode(virtualFolderNode2, bl);
            }
        }
    }

    public void notifyChanges() {
        ArrayList arrayList = Jext.getInstances();
        for (int i = 0; i < arrayList.size(); ++i) {
            JextFrame jextFrame = (JextFrame)arrayList.get(i);
            if (jextFrame == this.parent) continue;
            jextFrame.getVirtualFolders().notify(this.treeModel);
        }
    }

    public void notify(DefaultTreeModel defaultTreeModel) {
        this.treeModel = defaultTreeModel;
        this.tree.setModel(this.treeModel);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        if (object == this.newFolder || object == this.newFolderM) {
            this.newFolder();
            this.notifyChanges();
        } else if (object == this.addFile || object == this.addFileM) {
            this.addFile();
            this.notifyChanges();
        } else if (object == this.addAllFiles || object == this.addAllFilesM) {
            this.addAllFiles();
            this.notifyChanges();
        } else if (object == this.deleteItem || object == this.deleteM) {
            this.removeItem();
            this.notifyChanges();
        } else if (object == this.openFile || object == this.openFileM) {
            this.openSelection(true);
        }
    }

    public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
        TreePath[] arrtreePath = this.tree.getSelectionPaths();
        boolean bl = false;
        if (arrtreePath != null) {
            this.openFileM.setEnabled(true);
            this.openFile.setEnabled(true);
            this.deleteM.setEnabled(true);
            this.deleteItem.setEnabled(true);
            for (int i = 0; i < arrtreePath.length; ++i) {
                VirtualFolderNode virtualFolderNode = (VirtualFolderNode)arrtreePath[i].getLastPathComponent();
                if (virtualFolderNode.isLeaf()) continue;
                bl = true;
            }
        } else {
            this.openFileM.setEnabled(false);
            this.openFile.setEnabled(false);
            this.deleteM.setEnabled(false);
            this.deleteItem.setEnabled(false);
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.parent = null;
        this.newFolder = null;
        this.openFile = null;
        this.addFile = null;
        this.addAllFiles = null;
        this.deleteItem = null;
        this.popup = null;
        this.deleteM = null;
        this.openFileM = null;
        this.addFileM = null;
        this.addAllFilesM = null;
        this.newFolderM = null;
        this.tree = null;
        this.treeModel = null;
        this.root = null;
    }

    class DnDHandler
    implements DropTargetListener {
        DnDHandler() {
        }

        public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
        }

        public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
            Point point = dropTargetDragEvent.getLocation();
            TreePath treePath = VirtualFolders.this.tree.getPathForLocation(point.x, point.y);
            VirtualFolders.this.tree.setSelectionPath(treePath);
            VirtualFolders.this.tree.expandPath(treePath);
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
                    while (iterator.hasNext()) {
                        VirtualFolders.this.addFile(((File)iterator.next()).getPath());
                    }
                    bl = true;
                    continue;
                }
                catch (Exception var6_7) {
                    // empty catch block
                }
            }
            dropTargetDropEvent.dropComplete(bl);
        }
    }

    class KeyHandler
    extends KeyAdapter {
        KeyHandler() {
        }

        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == 10) {
                VirtualFolders.this.openSelection(false);
            }
        }
    }

    class VirtualFolderNode
    extends DefaultMutableTreeNode {
        private boolean isLeaf;
        private String filePath;
        private String label;
        private boolean isVisible;

        VirtualFolderNode(String string) {
            this(string, true);
        }

        VirtualFolderNode(String string, boolean bl) {
            this.isVisible = false;
            this.filePath = string;
            this.isLeaf = bl;
            int n = string.lastIndexOf(File.separator);
            this.label = n != -1 ? string.substring(n + 1) : string;
        }

        public void ensureVisible(boolean bl) {
            this.isVisible = bl;
        }

        public boolean shouldBeVisible() {
            return this.isVisible;
        }

        public String getFilePath() {
            return this.filePath;
        }

        public boolean isLeaf() {
            return this.isLeaf;
        }

        public String toString() {
            return this.label;
        }
    }

    class VirtualFoldersHandler
    extends HandlerBase {
        VirtualFolderNode parent;
        String folderName;
        String fileName;
        boolean isVisible;

        VirtualFoldersHandler() {
            this.parent = null;
            this.folderName = null;
            this.fileName = null;
            this.isVisible = false;
        }

        public void startElement(String string) throws Exception {
            if (this.parent == null) {
                this.parent = VirtualFolders.this.root;
            }
            if (string.equalsIgnoreCase("folder")) {
                this.parent = VirtualFolders.this.createFolder(this.folderName, false, this.parent);
            }
            if (string.equalsIgnoreCase("file")) {
                VirtualFolderNode virtualFolderNode = VirtualFolders.this.createLeaf(this.parent, this.fileName);
                if (this.isVisible) {
                    virtualFolderNode.isVisible = this.isVisible;
                }
                this.isVisible = false;
            }
        }

        public void endElement(String string) throws Exception {
            if (string.equalsIgnoreCase("folder")) {
                if (this.parent != null) {
                    this.parent = (VirtualFolderNode)this.parent.getParent();
                }
                if (this.parent == null) {
                    this.parent = VirtualFolders.this.root;
                }
            }
        }

        public void attribute(String string, String string2, boolean bl) {
            if (string.equalsIgnoreCase("path")) {
                this.fileName = string2;
            }
            if (string.equalsIgnoreCase("name")) {
                this.folderName = string2;
            }
            if (string.equalsIgnoreCase("visible")) {
                this.isVisible = string2.equalsIgnoreCase("yes");
            }
        }
    }

    class MouseHandler
    extends MouseAdapter {
        MouseHandler() {
        }

        public void mousePressed(MouseEvent mouseEvent) {
            if (mouseEvent.getModifiers() == 4) {
                VirtualFolders.this.popup.show(VirtualFolders.this.tree, mouseEvent.getX(), mouseEvent.getY());
            } else {
                TreePath treePath = VirtualFolders.this.tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
                if (treePath == null) {
                    VirtualFolders.this.tree.clearSelection();
                }
                if (mouseEvent.getClickCount() == 2) {
                    VirtualFolders.this.openSelection(false);
                }
            }
        }
    }

}

