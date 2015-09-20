/*
 * Decompiled with CFR 0_102.
 */
package jamtester.mdidesktop;

import jamtester.mdidesktop.MDIDesktopPane;
import jamtester.mdidesktop.TextFrame;
import jamtester.mdidesktop.WindowMenu;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

public class Notepad
extends JFrame {
    private MDIDesktopPane desktop = new MDIDesktopPane();
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem newMenu = new JMenuItem("New");
    private JScrollPane scrollPane = new JScrollPane();

    public Notepad() {
        this.menuBar.add(this.fileMenu);
        this.menuBar.add(new WindowMenu(this.desktop));
        this.fileMenu.add(this.newMenu);
        this.setJMenuBar(this.menuBar);
        this.setTitle("MDI Test");
        this.scrollPane.getViewport().add(this.desktop);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add((Component)this.scrollPane, "Center");
        this.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        this.newMenu.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                Notepad.this.desktop.add(new TextFrame());
            }
        });
    }

    public static void main(String[] arrstring) {
        Notepad notepad = new Notepad();
        notepad.setSize(600, 400);
        notepad.show();
    }

}

