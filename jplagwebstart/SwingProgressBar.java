/*
 * Decompiled with CFR 0_102.
 */
package jplagwebstart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.Border;
import jplagwebstart.ProgressBar;

public class SwingProgressBar
implements ProgressBar {
    JFrame frame = new JFrame("JPlag");
    JLabel label;
    JProgressBar progressBar;

    public SwingProgressBar() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        this.label = new JLabel("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", 0);
        this.label.setBorder(BorderFactory.createLoweredBevelBorder());
        this.label.setBackground(Color.WHITE);
        jPanel.add((Component)this.label, "North");
        this.progressBar = new JProgressBar(0);
        this.progressBar.setStringPainted(true);
        this.progressBar.setString("");
        this.progressBar.setBorderPainted(true);
        this.progressBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        this.progressBar.setBackground(Color.WHITE);
        jPanel.add((Component)this.progressBar, "South");
        jPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jPanel.setBackground(Color.WHITE);
        this.frame.setContentPane(jPanel);
        this.frame.pack();
        this.label.setText("");
        this.frame.setResizable(false);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dimension2 = this.frame.getSize();
        this.frame.setLocation((dimension.width - dimension2.width) / 2, (dimension.height - dimension2.height) / 2);
        this.frame.setLocation(dimension.width / 2, dimension.height / 2);
        this.frame.setDefaultCloseOperation(0);
        this.frame.setVisible(true);
        this.progressBar.setIndeterminate(true);
    }

    public void setSize(int n) {
        this.progressBar.setIndeterminate(false);
        this.progressBar.setMinimum(0);
        this.progressBar.setMaximum(n);
        this.setBar(0);
    }

    public void setBar(int n) {
        this.progressBar.setValue(n);
        this.progressBar.setString("" + (int)(this.progressBar.getPercentComplete() * 100.0) + "%");
    }

    public void setBar(String string) {
        this.label.setText(string);
    }

    public void finish() {
        this.frame.setVisible(false);
    }
}

