/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.Utilities;
import org.jext.gui.AbstractDisposer;
import org.jext.gui.JextHighlightButton;

public class AboutWindow
extends JDialog {
    public AboutWindow(JextFrame jextFrame) {
        super((Frame)jextFrame, false);
        this.setTitle(Jext.getProperty("about.title"));
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().setFont(new Font("Monospaced", 0, 14));
        Class class_ = Jext.class;
        this.getContentPane().add("North", new JLabel(Utilities.getIcon("images/splash" + Math.abs(new Random().nextInt()) % 6 + ".gif", class_)));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add("North", new JLabel("v3.2 <Qu\u00e9bec> b03.02.00.03", 0));
        jPanel.add("South", new JLabel("(C) 2002 Romain Guy -  www.jext.org", 0));
        this.getContentPane().add("Center", jPanel);
        JextHighlightButton jextHighlightButton = new JextHighlightButton(Jext.getProperty("general.ok.button"));
        jextHighlightButton.addActionListener(new AbstractAction(){

            public void actionPerformed(ActionEvent actionEvent) {
                AboutWindow.this.dispose();
            }
        });
        this.getRootPane().setDefaultButton(jextHighlightButton);
        JPanel jPanel2 = new JPanel();
        jPanel2.add(jextHighlightButton);
        this.getContentPane().add("South", jPanel2);
        this.addKeyListener(new AbstractDisposer(this));
        this.pack();
        Utilities.centerComponent(this);
        this.setResizable(false);
        this.setDefaultCloseOperation(2);
        this.setVisible(true);
    }

}

