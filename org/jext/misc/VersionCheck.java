/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jext.Jext;
import org.jext.JextFrame;

public class VersionCheck
extends Thread {
    public VersionCheck() {
        super("----thread: version check: jext----");
        this.start();
    }

    public void run() {
        try {
            String string;
            URL uRL = new URL(Jext.getProperty("check.url"));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRL.openStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String string2 = null;
            String string3 = null;
            while ((string = bufferedReader.readLine()) != null) {
                if (string.startsWith("#")) continue;
                if (string.startsWith(".release")) {
                    string3 = string.substring(8).trim();
                    continue;
                }
                if (string.startsWith(".build")) {
                    string2 = string.substring(6).trim();
                    continue;
                }
                if (string.startsWith(".description")) {
                    while (!((string = bufferedReader.readLine()) == null || string.equals(".end"))) {
                        stringBuffer.append(string);
                    }
                    continue;
                }
                if (!string.startsWith(".end")) continue;
            }
            bufferedReader.close();
            if (string3 != null && string2 != null && "03.02.00.03".compareTo(string2) < 0) {
                String[] arrstring = new String[]{string3, string2};
                JEditorPane jEditorPane = new JEditorPane();
                jEditorPane.setContentType("text/html");
                jEditorPane.setText(stringBuffer.toString());
                jEditorPane.setEditable(false);
                JPanel jPanel = new JPanel(new BorderLayout());
                jPanel.add("North", new JLabel(Jext.getProperty("check.changes")));
                jPanel.add("Center", new JScrollPane(jEditorPane));
                JOptionPane.showMessageDialog((JextFrame)Jext.getInstances().get(0), jPanel, Jext.getProperty("check.new"), 1);
            }
        }
        catch (Exception var1_2) {}
        finally {
            Jext.stopAutoCheck();
        }
    }
}

