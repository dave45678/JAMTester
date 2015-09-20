/*
 * Decompiled with CFR 0_102.
 */
package jamtester;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.OutputStream;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class OutputTextbox
extends OutputStream {
    private JFrame frame;
    private JTextArea tA;
    private JScrollPane sp;
    private OutputStream old;
    private StringBuffer buff;

    public OutputTextbox(String string) {
        this.frame = new JFrame(string);
        this.buff = new StringBuffer();
        this.frame.setSize(800, 600);
        this.tA = new JTextArea();
        this.tA.setEditable(false);
        this.sp = new JScrollPane(this.tA);
        this.frame.getContentPane().add(this.sp);
        this.tA.setFont(new Font("Courier", 0, 12));
        this.tA.setBackground(Color.blue);
        this.tA.setForeground(Color.white);
        this.frame.show();
    }

    public OutputTextbox(String string, boolean bl) {
        this(string);
        if (bl) {
            this.frame.addWindowListener(new WindowAdapter(){

                public void windowClosing(WindowEvent windowEvent) {
                    System.exit(0);
                }
            });
        }
    }

    public String getText() {
        return this.tA.getText();
    }

    public OutputTextbox(String string, boolean bl, OutputStream outputStream) {
        this(string, bl);
        this.old = outputStream;
    }

    public void write(int n) {
        this.buff.append("" + (char)n);
        if ((char)n == '\n') {
            this.flush();
        }
    }

    public void flush() {
        this.tA.append(this.buff.toString());
        this.buff = new StringBuffer();
        this.scrollDown();
    }

    private void scrollDown() {
        this.sp.getVerticalScrollBar().setValue(this.sp.getVerticalScrollBar().getMaximum() + 1);
    }

}

