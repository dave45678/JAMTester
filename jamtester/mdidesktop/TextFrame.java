/*
 * Decompiled with CFR 0_102.
 */
package jamtester.mdidesktop;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;

public class TextFrame
extends JInternalFrame {
    private JTextArea textArea = new JTextArea();
    private JScrollPane scrollPane = new JScrollPane();

    public TextFrame() {
        this.setSize(200, 300);
        this.setTitle("Edit Text");
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setClosable(true);
        this.setResizable(true);
        this.scrollPane.getViewport().add(this.textArea);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add((Component)this.scrollPane, "Center");
    }
}

