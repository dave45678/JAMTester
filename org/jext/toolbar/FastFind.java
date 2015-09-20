/*
 * Decompiled with CFR 0_102.
 */
package org.jext.toolbar;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.search.Search;

public class FastFind
extends JTextField
implements ActionListener,
KeyListener {
    private JextFrame parent;

    public FastFind(JextFrame jextFrame) {
        this.parent = jextFrame;
        this.setCursor(Cursor.getPredefinedCursor(2));
        this.addActionListener(this);
        FontMetrics fontMetrics = this.getFontMetrics(this.getFont());
        Dimension dimension = new Dimension(fontMetrics.charWidth('m') * 10, this.getPreferredSize().height);
        this.setMinimumSize(dimension);
        this.setPreferredSize(dimension);
        this.setMaximumSize(new Dimension(fontMetrics.charWidth('m') * 80, this.getPreferredSize().height));
        this.addKeyListener(this);
    }

    public void keyPressed(KeyEvent keyEvent) {
    }

    public void keyTyped(KeyEvent keyEvent) {
    }

    public void keyReleased(KeyEvent keyEvent) {
        if (Jext.getBooleanProperty("find.incremental")) {
            JextTextArea jextTextArea = this.parent.getTextArea();
            jextTextArea.setCaretPosition(jextTextArea.getSelectionStart());
            this.find(jextTextArea, false);
            this.requestFocus();
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this) {
            JextTextArea jextTextArea = this.parent.getTextArea();
            jextTextArea.requestFocus();
            this.find(jextTextArea, true);
        }
    }

    private void find(JextTextArea jextTextArea, boolean bl) {
        Search.setFindPattern(this.getText());
        try {
            if (!Search.find(jextTextArea, jextTextArea.getCaretPosition()) && bl) {
                Object[] arrobject = new String[]{jextTextArea.getName()};
                int n = JOptionPane.showConfirmDialog(null, Jext.getProperty("find.matchnotfound", arrobject), Jext.getProperty("find.title"), 0, 3);
                switch (n) {
                    case 0: {
                        jextTextArea.setCaretPosition(0);
                        this.find(jextTextArea, false);
                    }
                }
            }
        }
        catch (Exception var3_4) {
            // empty catch block
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.parent = null;
    }
}

