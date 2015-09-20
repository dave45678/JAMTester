/*
 * Decompiled with CFR 0_102.
 */
package org.jext.scripting.dawn;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.Document;
import org.jext.GUIUtilities;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.Utilities;
import org.jext.gui.JextHighlightButton;
import org.jext.scripting.dawn.Run;

public class DawnLogWindow
extends JFrame
implements ActionListener {
    private JextHighlightButton clear;
    private JextFrame parent;
    private JTextField immediate;
    private JTextArea textArea = new JTextArea(20, 40);

    public DawnLogWindow(JextFrame jextFrame) {
        super(Jext.getProperty("dawn.window.title"));
        this.parent = jextFrame;
        this.textArea.setEditable(false);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add("Center", new JScrollPane(this.textArea, 22, 32));
        JPanel jPanel = new JPanel();
        jPanel.add(new JLabel(Jext.getProperty("dawn.window.immediate")));
        this.immediate = new JTextField(40);
        jPanel.add(this.immediate);
        this.clear = new JextHighlightButton(Jext.getProperty("dawn.window.clear"));
        jPanel.add(this.clear);
        this.getContentPane().add("South", jPanel);
        this.clear.addActionListener(this);
        this.immediate.addActionListener(this);
        this.setDefaultCloseOperation(1);
        this.addKeyListener(new KeyAdapter(){

            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == 27) {
                    DawnLogWindow.this.setVisible(false);
                }
            }
        });
        this.setIconImage(GUIUtilities.getJextIconImage());
        this.pack();
        Utilities.centerComponent(this);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        if (object == this.clear) {
            this.textArea.setText("");
        } else if (object == this.immediate) {
            Run.execute(this.immediate.getText(), this.parent);
            this.immediate.setText("");
        }
    }

    public void log(String string) {
        this.textArea.append(string);
        this.textArea.setSelectionStart(this.textArea.getDocument().getLength());
        this.textArea.setSelectionEnd(this.textArea.getDocument().getLength());
    }

    public void logln(String string) {
        this.log(string + '\n');
    }

}

