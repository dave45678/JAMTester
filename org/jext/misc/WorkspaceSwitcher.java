/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTabbedPane;
import org.jext.JextTextArea;
import org.jext.gui.EnhancedMenuItem;
import org.jext.gui.JextMenuSeparator;
import org.jext.misc.WorkspaceSwitcher;
import org.jext.misc.Workspaces;

public class WorkspaceSwitcher
extends MouseAdapter {
    private String mode;
    private JextFrame parent;
    private JPopupMenu dropDown;

    public WorkspaceSwitcher(JextFrame jextFrame) {
        this.parent = jextFrame;
    }

    private void buildDropDownList() {
        this.dropDown = new JPopupMenu();
        EnhancedMenuItem enhancedMenuItem = new EnhancedMenuItem(Jext.getProperty("ws.sendTo.title"));
        enhancedMenuItem.setEnabled(false);
        this.dropDown.add(enhancedMenuItem);
        this.dropDown.add(new JextMenuSeparator());
        if (Jext.getFlatMenus()) {
            this.dropDown.setBorder(LineBorder.createBlackLineBorder());
        }
        Switcher switcher = new Switcher();
        String string = this.parent.getWorkspaces().getName();
        String[] arrstring = this.parent.getWorkspaces().getWorkspacesNames();
        for (int i = 0; i < arrstring.length; ++i) {
            if (arrstring[i].equals(string)) continue;
            enhancedMenuItem = new EnhancedMenuItem(arrstring[i]);
            enhancedMenuItem.setActionCommand(arrstring[i]);
            enhancedMenuItem.addActionListener(switcher);
            this.dropDown.add(enhancedMenuItem);
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        this.buildDropDownList();
        JComponent jComponent = (JComponent)mouseEvent.getComponent();
        this.dropDown.show(jComponent, 0, jComponent.getHeight());
    }

    class Switcher
    implements ActionListener {
        Switcher() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            String string = ((JMenuItem)actionEvent.getSource()).getActionCommand();
            JextTextArea jextTextArea = WorkspaceSwitcher.this.parent.getTextArea();
            Workspaces workspaces = WorkspaceSwitcher.this.parent.getWorkspaces();
            JextTabbedPane jextTabbedPane = WorkspaceSwitcher.this.parent.getTabbedPane();
            int n = jextTabbedPane.indexOfComponent(jextTextArea);
            if (n != -1) {
                workspaces.removeFile(jextTextArea);
                jextTabbedPane.removeTabAt(n);
                if (WorkspaceSwitcher.this.parent.getTextAreas().length == 0) {
                    WorkspaceSwitcher.this.parent.createFile();
                }
                workspaces.selectWorkspaceOfName(string);
                SwingUtilities.invokeLater(new Runnable(this, workspaces, jextTextArea, jextTabbedPane){
                    private final /* synthetic */ Workspaces val$workspaces;
                    private final /* synthetic */ JextTextArea val$textArea;
                    private final /* synthetic */ JextTabbedPane val$textAreasPane;
                    private final /* synthetic */ Switcher this$1;

                    public void run() {
                        this.val$workspaces.addFile(this.val$textArea);
                        this.val$textAreasPane.add(this.val$textArea);
                        this.val$textAreasPane.setSelectedComponent(this.val$textArea);
                    }
                });
            }
        }
    }

}

