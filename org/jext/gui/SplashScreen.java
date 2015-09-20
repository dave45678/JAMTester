/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.LayoutManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import org.jext.Jext;
import org.jext.Utilities;
import org.jext.gui.JextProgressBar;

public class SplashScreen
extends JWindow
implements Runnable {
    private Thread thread;
    private boolean finished;
    private String[] classes;
    private JextProgressBar progress;

    public SplashScreen() {
        this.setBackground(Color.lightGray);
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.setFont(new Font("Monospaced", 0, 14));
        Class class_ = Jext.class;
        jPanel.add("North", new JLabel(Utilities.getIcon("images/splash" + Math.abs(new Random().nextInt()) % 6 + ".gif", class_)));
        this.progress = new JextProgressBar(0, 100);
        this.progress.setStringPainted(true);
        this.progress.setFont(new Font("Monospaced", 1, 9));
        this.progress.setString("");
        this.progress.setBorder(new MatteBorder(0, 1, 1, 1, Color.black));
        jPanel.add("Center", this.progress);
        jPanel.add("South", new JLabel("v3.2 <Qu\u00e9bec> - (C) 2003 Romain Guy", 0));
        jPanel.setBorder(new EtchedBorder(0));
        this.getContentPane().add(jPanel);
        this.pack();
        boolean bl = Jext.getBooleanProperty("load.classes");
        if (bl) {
            this.createClassesList();
            this.thread = new Thread(this);
            this.thread.setDaemon(true);
            this.thread.setPriority(5);
        }
        Utilities.centerComponent(this);
        Utilities.setCursorOnWait(this, true);
        this.setVisible(true);
        if (bl) {
            this.thread.start();
        } else {
            this.finished = true;
            this.setProgress(0);
            this.setText(Jext.getProperty("startup.loading"));
        }
    }

    private void createClassesList() {
        Vector<String> vector = new Vector<String>(30);
        Class class_ = Jext.class;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(class_.getResourceAsStream("classlist")));
        try {
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                vector.addElement(string);
            }
            bufferedReader.close();
        }
        catch (IOException var4_4) {
            // empty catch block
        }
        this.classes = new String[vector.size()];
        vector.copyInto(this.classes);
        vector = null;
    }

    public void run() {
        String string = this.getClass().getName();
        int n = string.lastIndexOf(46);
        string = n >= 0 ? string.substring(0, n + 1) : "";
        for (n = 0; n < this.classes.length; ++n) {
            String string2 = this.classes[n];
            int n2 = string2.lastIndexOf(46);
            if (n2 < 0) {
                string2 = string + string2;
            }
            this.progress.setString(string2);
            try {
                Class class_ = Class.forName(string2);
            }
            catch (Exception var5_6) {
                // empty catch block
            }
            this.progress.setValue(100 * (n + 1) / this.classes.length);
        }
        this.finished = true;
        this.setText(Jext.getProperty("startup.loading"));
        this.stop();
    }

    public void setText(String string) {
        if (this.finished) {
            this.progress.setString(string);
        }
    }

    public void setProgress(int n) {
        if (this.finished) {
            this.progress.setValue(n);
        }
    }

    public void stop() {
        this.thread = null;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.thread = null;
        this.classes = null;
        this.progress = null;
    }
}

