/*
 * Decompiled with CFR 0_102.
 */
package jamtester;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;

public class StatusBar
extends JComponent {
    private Font font;
    private long displayTime;
    private Timer curTimer;
    private String message;

    public StatusBar() {
        this.setFont(new Font("courier", 1, 12));
        this.displayTime = 5000;
        this.message = "";
        this.setBorder(new SoftBevelBorder(1));
    }

    public void addToFrame(JFrame jFrame) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(jFrame.getContentPane().getLayout());
        Component[] arrcomponent = jFrame.getContentPane().getComponents();
        jFrame.getContentPane().removeAll();
        for (int i = 0; i < arrcomponent.length; ++i) {
            jPanel.add(arrcomponent[i]);
        }
        jFrame.getContentPane().setLayout(new BorderLayout());
        jFrame.getContentPane().add((Component)jPanel, "Center");
        jFrame.getContentPane().add((Component)this, "South");
    }

    public void addToInternalFrame(JInternalFrame jInternalFrame) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(jInternalFrame.getContentPane().getLayout());
        Component[] arrcomponent = jInternalFrame.getContentPane().getComponents();
        jInternalFrame.getContentPane().removeAll();
        for (int i = 0; i < arrcomponent.length; ++i) {
            jPanel.add(arrcomponent[i]);
        }
        jInternalFrame.getContentPane().setLayout(new BorderLayout());
        jInternalFrame.getContentPane().add((Component)jPanel, "Center");
        jInternalFrame.getContentPane().add((Component)this, "South");
    }

    private void killTimer() {
        if (this.curTimer != null) {
            this.curTimer.cancel();
        }
    }

    public long getDisplayTime() {
        return this.displayTime;
    }

    public void setDisplayTime(long l) {
        this.displayTime = l;
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(Font font) {
        this.font = font;
        this.setPreferredSize(new Dimension(0, this.font.getSize() + 6));
    }

    public void display(String string) {
        this.message = string;
        this.repaint();
        this.curTimer = new Timer();
        this.curTimer.schedule(new TimerTask(){

            public void run() {
                StatusBar.this.message = "";
                StatusBar.this.repaint();
            }
        }, this.displayTime);
    }

    public void paint(Graphics graphics) {
        this.getBorder().paintBorder(this, graphics, 0, 0, this.getWidth(), this.getHeight());
        graphics.setFont(this.font);
        int n = graphics.getFontMetrics().stringWidth(this.message);
        int n2 = this.font.getSize();
        graphics.drawString(this.message, this.getWidth() - (n + 10), this.getHeight() - n2 / 2);
    }

}

