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
import java.awt.Component;
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
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jext.GUIUtilities;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTabbedPane;
import org.jext.JextTextArea;
import org.jext.Utilities;
import org.jext.gui.JextHighlightButton;
import org.jext.gui.ModifiedCellRenderer;
import org.jext.misc.SaveDialog;
import org.jext.misc.WorkspaceSwitcher;

public class Workspaces
extends JPanel
implements ActionListener,
ListSelectionListener {
    private JextFrame parent;
    private JList workspacesList;
    private DefaultListModel model;
    private WorkspaceElement currentWorkspace;
    private boolean loading = false;
    private JextHighlightButton newWorkspace;
    private JextHighlightButton removeWorkspace;
    private JextHighlightButton switchWorkspace;

    public Workspaces(JextFrame jextFrame) {
        this.setLayout(new BorderLayout());
        this.parent = jextFrame;
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
        Class class_ = Jext.class;
        this.newWorkspace = new JextHighlightButton(Utilities.getIcon("images/menu_new" + Jext.getProperty("jext.look.icons") + ".gif", class_));
        jToolBar.add(this.newWorkspace);
        this.newWorkspace.setToolTipText(Jext.getProperty("ws.new.tooltip"));
        this.newWorkspace.addActionListener(this);
        this.removeWorkspace = new JextHighlightButton(Utilities.getIcon("images/button_remove" + Jext.getProperty("jext.look.icons") + ".gif", Jext.class));
        jToolBar.add(this.removeWorkspace);
        this.removeWorkspace.setToolTipText(Jext.getProperty("ws.remove.tooltip"));
        this.removeWorkspace.addActionListener(this);
        this.switchWorkspace = new JextHighlightButton(Utilities.getIcon("images/menu_goto" + Jext.getProperty("jext.look.icons") + ".gif", Jext.class));
        jToolBar.add(this.switchWorkspace);
        this.switchWorkspace.setToolTipText(Jext.getProperty("ws.sendTo.tooltip"));
        this.switchWorkspace.addMouseListener(new WorkspaceSwitcher(jextFrame));
        this.model = new DefaultListModel();
        this.workspacesList = new JList(this.model);
        this.workspacesList.setSelectionMode(0);
        this.workspacesList.setCellRenderer(new ModifiedCellRenderer());
        new DropTarget(this.workspacesList, new DnDHandler());
        this.add((Component)jToolBar, "North");
        JScrollPane jScrollPane = new JScrollPane(this.workspacesList);
        jScrollPane.setBorder(null);
        this.add((Component)jScrollPane, "Center");
    }

    public void load() {
        this.workspacesList.addListSelectionListener(this);
        this.loading = true;
        WorkspacesHandler workspacesHandler = null;
        try {
            BufferedReader bufferedReader;
            String string;
            String string2 = "";
            File file = new File(Jext.SETTINGS_DIRECTORY + File.separator + ".workspaces.xml");
            if (file.exists() && file.length() > 0 && Jext.getBooleanProperty("editor.saveSession")) {
                try {
                    bufferedReader = new BufferedReader(new FileReader(file));
                    string = bufferedReader.readLine();
                    while (string != null) {
                        string2 = string2 + string;
                        string = bufferedReader.readLine();
                    }
                    bufferedReader.close();
                }
                catch (Exception var4_6) {
                    string2 = "<?xml version=\"1.0\"?><workspaces><workspace name=\"" + Jext.getProperty("ws.default") + "\"/></workspaces>";
                }
            } else {
                string2 = "<?xml version=\"1.0\"?><workspaces><workspace name=\"" + Jext.getProperty("ws.default") + "\"/></workspaces>";
            }
            bufferedReader = new StringReader(string2);
            string = new XmlParser();
            workspacesHandler = new WorkspacesHandler();
            string.setHandler((XmlHandler)workspacesHandler);
            string.parse(null, null, (Reader)bufferedReader);
        }
        catch (Exception var2_3) {
            // empty catch block
        }
        this.loading = false;
        this.workspacesList.setSelectedIndex(0);
    }

    public void save() {
        if (!(Jext.getInstances().size() <= 1 && Jext.getBooleanProperty("editor.saveSession"))) {
            return;
        }
        try {
            File file = new File(Jext.SETTINGS_DIRECTORY + File.separator + ".workspaces.xml");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write("<?xml version=\"1.0\"?>");
            bufferedWriter.newLine();
            bufferedWriter.write("<workspaces>");
            bufferedWriter.newLine();
            for (int i = 0; i < this.model.size(); ++i) {
                WorkspaceElement workspaceElement = (WorkspaceElement)this.model.get(i);
                bufferedWriter.write("  <workspace name=\"" + this.convertToXML(workspaceElement.toString()) + "\" selectedIndex=\"" + workspaceElement.getSelectedIndex() + "\">");
                bufferedWriter.newLine();
                ArrayList arrayList = workspaceElement.contents;
                for (int j = 0; j < arrayList.size(); ++j) {
                    JextTextArea jextTextArea = (JextTextArea)arrayList.get(j);
                    if (jextTextArea.isNew()) continue;
                    bufferedWriter.write("    <file path=\"" + this.convertToXML(jextTextArea.getCurrentFile()) + "\" caretPosition=\"" + jextTextArea.getCaretPosition() + "\" />");
                    bufferedWriter.newLine();
                }
                bufferedWriter.write("  </workspace>");
                bufferedWriter.newLine();
            }
            bufferedWriter.write("</workspaces>");
            bufferedWriter.flush();
            bufferedWriter.close();
        }
        catch (Exception var1_8) {
            // empty catch block
        }
    }

    public String convertToXML(String string) {
        StringBuffer stringBuffer = new StringBuffer(string.length());
        block5 : for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            switch (c) {
                case '&': {
                    stringBuffer.append("&amp;");
                    continue block5;
                }
                case '\'': {
                    stringBuffer.append("&apos;");
                    continue block5;
                }
                case '\"': {
                    stringBuffer.append("&quot;");
                    continue block5;
                }
                default: {
                    stringBuffer.append(c);
                }
            }
        }
        return stringBuffer.toString();
    }

    public DefaultListModel getList() {
        return this.model;
    }

    public String[] getWorkspacesNames() {
        String[] arrstring = new String[this.model.size()];
        for (int i = 0; i < arrstring.length; ++i) {
            arrstring[i] = ((WorkspaceElement)this.model.get(i)).getName();
        }
        return arrstring;
    }

    public void addFile(JextTextArea jextTextArea) {
        this.currentWorkspace.contents.add(jextTextArea);
    }

    public void removeFile(JextTextArea jextTextArea) {
        this.currentWorkspace.contents.remove(this.currentWorkspace.contents.indexOf(jextTextArea));
        int n = this.currentWorkspace.contents.size();
        if (n == 0) {
            this.currentWorkspace.setSelectedIndex(0);
        } else if (n - 1 < this.currentWorkspace.getSelectedIndex()) {
            this.currentWorkspace.setSelectedIndex(n - 1);
        }
    }

    private void newWorkspace() {
        String string = JOptionPane.showInputDialog(this.parent, Jext.getProperty("ws.new.msg"), Jext.getProperty("ws.new.title"), 3);
        if (string != null && string.length() > 0) {
            this.createWorkspace(string);
        }
    }

    public WorkspaceElement createWorkspace(String string) {
        for (int i = 0; i < this.model.size(); ++i) {
            if (!string.equals(((WorkspaceElement)this.model.get(i)).getName())) continue;
            GUIUtilities.message(this.parent, "ws.exists", null);
            return null;
        }
        WorkspaceElement workspaceElement = new WorkspaceElement(string);
        this.model.addElement(workspaceElement);
        this.workspacesList.setSelectedIndex(this.model.size() - 1);
        return workspaceElement;
    }

    public void clear() {
        this.parent.getTabbedPane().removeAll();
        for (int i = 0; i < this.model.size(); ++i) {
            WorkspaceElement workspaceElement = (WorkspaceElement)this.model.get(i);
            workspaceElement.contents.clear();
            this.model.remove(i);
            workspaceElement = null;
        }
    }

    public void closeAllWorkspaces() {
        new SaveDialog(this.parent, 0);
    }

    private void removeWorkspace() {
        this.parent.closeAll();
        int n = this.workspacesList.getSelectedIndex();
        this.model.remove(n);
        Object e = this.workspacesList.getSelectedValue();
        e = null;
        if (this.model.size() == 0) {
            this.createWorkspace(Jext.getProperty("ws.default"));
        }
        this.workspacesList.setSelectedIndex(n == 0 ? 0 : n - 1);
    }

    public void loadTextAreas() {
        this.parent.setBatchMode(true);
        for (int i = 0; i < this.model.size(); ++i) {
            ArrayList arrayList = ((WorkspaceElement)this.model.get((int)i)).contents;
            for (int j = 0; j < arrayList.size(); ++j) {
                this.parent.loadTextArea((JextTextArea)arrayList.get(j));
            }
        }
        this.parent.setBatchMode(false);
    }

    public String getName() {
        if (this.currentWorkspace == null) {
            return Jext.getProperty("ws.default");
        }
        return this.currentWorkspace.toString();
    }

    public void selectWorkspaceOfName(String string) {
        if (string == null) {
            return;
        }
        for (int i = 0; i < this.model.size(); ++i) {
            if (!string.equals(((WorkspaceElement)this.model.get(i)).getName())) continue;
            this.workspacesList.setSelectedIndex(i);
            return;
        }
    }

    public void selectWorkspaceOfNameOrCreate(String string) {
        if (string == null) {
            return;
        }
        for (int i = 0; i < this.model.size(); ++i) {
            if (!string.equals(((WorkspaceElement)this.model.get(i)).getName())) continue;
            this.workspacesList.setSelectedIndex(i);
            return;
        }
        this.currentWorkspace = this.createWorkspace(string);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        if (object == this.newWorkspace) {
            this.newWorkspace();
        } else if (object == this.removeWorkspace) {
            this.removeWorkspace();
        }
    }

    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        WorkspaceElement workspaceElement;
        if (listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        this.parent.setBatchMode(true);
        if (this.currentWorkspace != null) {
            if (!this.currentWorkspace.first) {
                this.currentWorkspace.setSelectedIndex(this.parent.getTabbedPane().getSelectedIndex());
            } else {
                this.currentWorkspace.first = false;
            }
        }
        if ((workspaceElement = (WorkspaceElement)this.workspacesList.getSelectedValue()) == null) {
            this.parent.setBatchMode(false);
            return;
        }
        this.currentWorkspace = workspaceElement;
        JextTabbedPane jextTabbedPane = this.parent.getTabbedPane();
        jextTabbedPane.removeAll();
        if (workspaceElement.contents.size() == 0) {
            if (!this.loading) {
                this.parent.createFile();
            }
        } else {
            ArrayList arrayList = workspaceElement.contents;
            for (int i = 0; i < arrayList.size(); ++i) {
                jextTabbedPane.add((Component)arrayList.get(i));
            }
            jextTabbedPane.setSelectedIndex(this.currentWorkspace.getSelectedIndex());
        }
        this.parent.setBatchMode(false);
        this.parent.fireJextEvent(77);
        SwingUtilities.invokeLater(new Runnable(){

            public void run() {
                JextTextArea jextTextArea = Workspaces.this.parent.getTextArea();
                if (jextTextArea != null) {
                    jextTextArea.grabFocus();
                    jextTextArea.requestFocus();
                }
            }
        });
    }

    class DnDHandler
    implements DropTargetListener {
        DnDHandler() {
        }

        public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
        }

        public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
            Workspaces.this.workspacesList.setSelectedIndex(Workspaces.this.workspacesList.locationToIndex(dropTargetDragEvent.getLocation()));
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
                        Workspaces.this.parent.open(((File)iterator.next()).getPath());
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

    public static class WorkspaceElement {
        private String name;
        private int sIndex = 0;
        private boolean first = true;
        public ArrayList contents;

        WorkspaceElement(String string) {
            this.name = string;
            this.contents = new ArrayList();
        }

        public int getSelectedIndex() {
            return this.sIndex;
        }

        public String getName() {
            return this.name;
        }

        public void setSelectedIndex(int n) {
            if (n < this.contents.size()) {
                this.sIndex = n;
            }
        }

        public String toString() {
            return this.name;
        }
    }

    class WorkspacesHandler
    extends HandlerBase {
        int caretPosition;
        int selectedIndex;
        String fileName;
        String workspaceName;
        String currentWorkspaceName;

        WorkspacesHandler() {
            this.caretPosition = 0;
            this.selectedIndex = 0;
            this.fileName = null;
            this.workspaceName = null;
            this.currentWorkspaceName = null;
        }

        public void startElement(String string) throws Exception {
            JextTextArea jextTextArea;
            if (string.equalsIgnoreCase("workspace")) {
                Workspaces.this.currentWorkspace = Workspaces.this.createWorkspace(this.workspaceName);
            } else if (string.equalsIgnoreCase("file") && new File(this.fileName).exists() && this.caretPosition < (jextTextArea = Workspaces.this.parent.openForLoading(this.fileName)).getLength()) {
                jextTextArea.setCaretPosition(this.caretPosition);
            }
        }

        public void endElement(String string) throws Exception {
            if (string.equalsIgnoreCase("workspace")) {
                if (Workspaces.this.currentWorkspace != null) {
                    if (Workspaces.access$200((Workspaces)Workspaces.this).contents.size() == 0) {
                        Workspaces.this.parent.createFile();
                    } else {
                        Workspaces.this.currentWorkspace.setSelectedIndex(this.selectedIndex);
                    }
                }
                this.selectedIndex = 0;
            } else {
                this.caretPosition = 0;
            }
        }

        public void attribute(String string, String string2, boolean bl) {
            if (string.equalsIgnoreCase("path")) {
                this.fileName = string2;
            } else if (string.equalsIgnoreCase("name")) {
                this.workspaceName = string2;
            } else if (string.equalsIgnoreCase("caretPosition")) {
                try {
                    this.caretPosition = Integer.parseInt(string2);
                }
                catch (Exception var4_4) {
                    this.caretPosition = 0;
                }
            } else if (string.equalsIgnoreCase("selectedIndex")) {
                try {
                    this.selectedIndex = Integer.parseInt(string2);
                }
                catch (Exception var4_5) {
                    this.selectedIndex = 0;
                }
            }
        }
    }

}

