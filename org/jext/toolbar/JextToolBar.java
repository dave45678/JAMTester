/*
 * Decompiled with CFR 0_102.
 */
package org.jext.toolbar;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JToolBar;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.gui.JextButton;
import org.jext.gui.JextSeparator;
import org.jext.toolbar.FastFind;
import org.jext.toolbar.FastSyntax;

public class JextToolBar
extends JToolBar {
    private boolean grayed = false;
    private JToolBar buttonsPanel;
    private JToolBar persistentToolBar = new JToolBar();
    private JToolBar transientToolBar = new JToolBar();

    public JextToolBar(JextFrame jextFrame) {
        this.setFloatable(false);
        this.persistentToolBar.putClientProperty("JEXT_INSTANCE", jextFrame);
        this.persistentToolBar.setFloatable(false);
        this.persistentToolBar.setBorderPainted(false);
        super.add(this.persistentToolBar);
        this.transientToolBar.putClientProperty("JEXT_INSTANCE", jextFrame);
        this.transientToolBar.setFloatable(false);
        this.transientToolBar.setBorderPainted(false);
        super.add(this.transientToolBar);
        this.buttonsPanel = this.persistentToolBar;
    }

    public void addMisc(JextFrame jextFrame) {
        this.add(Box.createHorizontalStrut(10));
        JextButton jextButton = new JextButton(Jext.getProperty(Jext.getBooleanProperty("find.incremental") ? "find.incremental.label" : "find.label"));
        jextButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                if (Jext.getBooleanProperty("find.incremental")) {
                    ((JextButton)actionEvent.getSource()).setText(Jext.getProperty("find.label"));
                    Jext.setProperty("find.incremental", "off");
                } else {
                    ((JextButton)actionEvent.getSource()).setText(Jext.getProperty("find.incremental.label"));
                    Jext.setProperty("find.incremental", "on");
                }
            }
        });
        this.add(jextButton);
        Box box = new Box(1);
        box.add(Box.createVerticalGlue());
        box.add(new FastFind(jextFrame));
        box.add(Box.createVerticalGlue());
        this.add(box);
        this.add(Box.createHorizontalStrut(10));
        Box box2 = new Box(1);
        box2.add(Box.createVerticalGlue());
        box2.add(new FastSyntax(jextFrame));
        box2.add(Box.createVerticalGlue());
        this.add(box2);
    }

    public void setGrayed(boolean bl) {
        Component component;
        if (this.grayed == bl) {
            return;
        }
        int n = -1;
        while ((component = this.buttonsPanel.getComponentAtIndex(++n)) != null) {
            if (!(component instanceof JextButton)) continue;
            ((JextButton)component).setGrayed(bl);
        }
        this.grayed = bl;
    }

    public void addButton(JextButton jextButton) {
        jextButton.setMargin(new Insets(1, 1, 1, 1));
        this.buttonsPanel.add(jextButton);
    }

    public void addButtonSeparator() {
        this.buttonsPanel.add(new JextSeparator());
    }

    public void freeze() {
        this.buttonsPanel = this.transientToolBar;
    }

    public void reset() {
        this.transientToolBar.removeAll();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.buttonsPanel = null;
        this.persistentToolBar = null;
        this.transientToolBar = null;
    }

}

