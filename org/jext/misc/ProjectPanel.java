/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.gui.JextToggleButton;
import org.jext.misc.VirtualFolders;
import org.jext.misc.Workspaces;

public class ProjectPanel
extends JPanel {
    private JPanel panelCard;
    private CardLayout carder = new CardLayout();
    private JextToggleButton workspaces;
    private JextToggleButton bookmarks;

    public ProjectPanel(JextFrame jextFrame) {
        super(new BorderLayout());
        this.panelCard = new JPanel(this.carder);
        this.panelCard.add((Component)jextFrame.getWorkspaces(), "workspaces");
        this.panelCard.add((Component)jextFrame.getVirtualFolders(), "virtual folders");
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
        this.workspaces = new JextToggleButton(Jext.getProperty("ws.tab"));
        this.bookmarks = new JextToggleButton(Jext.getProperty("vf.tab"));
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(this.workspaces);
        buttonGroup.add(this.bookmarks);
        this.workspaces.setSelected(true);
        ToggleHandler toggleHandler = new ToggleHandler();
        this.workspaces.addActionListener(toggleHandler);
        this.bookmarks.addActionListener(toggleHandler);
        jToolBar.add(this.workspaces);
        jToolBar.add(this.bookmarks);
        this.carder.first(this.panelCard);
        this.add("North", jToolBar);
        this.add("Center", this.panelCard);
    }

    class ToggleHandler
    implements ActionListener {
        ToggleHandler() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            Object object = actionEvent.getSource();
            if (object == ProjectPanel.this.workspaces) {
                ProjectPanel.this.carder.first(ProjectPanel.this.panelCard);
            } else if (object == ProjectPanel.this.bookmarks) {
                ProjectPanel.this.carder.last(ProjectPanel.this.panelCard);
            }
        }
    }

}

