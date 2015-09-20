/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  gnu.regexp.RE
 *  gnu.regexp.REException
 *  gnu.regexp.REMatch
 */
package org.jext.scripting.python;

import gnu.regexp.RE;
import gnu.regexp.REException;
import gnu.regexp.REMatch;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Writer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.Element;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.jext.GUIUtilities;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.Utilities;
import org.jext.gui.JextHighlightButton;

public class PythonLogWindow
extends JFrame
implements ActionListener {
    private RE regexp;
    private JextFrame parent;
    private Container content;
    private boolean docked = false;
    private JextHighlightButton clear;
    private JextHighlightButton dock;
    private JScrollPane textAreaScroller = null;
    private JTextArea textArea = new JTextArea(15, 40);
    private Writer writerStdOut;
    private Writer writeStdErr;

    public PythonLogWindow(JextFrame jextFrame) {
        super(Jext.getProperty("python.window.title"));
        this.writerStdOut = new Writer(){

            public void close() {
            }

            public void flush() {
                PythonLogWindow.this.textArea.repaint();
            }

            public void write(char[] arrc, int n, int n2) {
                PythonLogWindow.this.log(new String(arrc, n, n2));
            }
        };
        this.writeStdErr = new Writer(){

            public void close() {
            }

            public void flush() {
                PythonLogWindow.this.textArea.repaint();
            }

            public void write(char[] arrc, int n, int n2) {
                PythonLogWindow.this.log(new String(arrc, n, n2));
            }
        };
        this.parent = jextFrame;
        this.textArea.setEditable(false);
        this.textArea.addMouseListener(new MouseHandler());
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add("Center", new JScrollPane(this.textArea, 22, 32));
        JPanel jPanel = new JPanel(new BorderLayout());
        this.dock = new JextHighlightButton(Jext.getProperty("python.window.dock"));
        jPanel.add("West", this.dock);
        jPanel.add("Center", new JLabel(Jext.getProperty("python.window.advice")));
        this.clear = new JextHighlightButton(Jext.getProperty("python.window.clear"));
        jPanel.add("East", this.clear);
        this.getContentPane().add("South", jPanel);
        this.dock.addActionListener(this);
        this.clear.addActionListener(this);
        this.setDefaultCloseOperation(1);
        this.addKeyListener(new KeyAdapter(){

            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == 27) {
                    PythonLogWindow.this.setVisible(false);
                }
            }
        });
        this.setIconImage(GUIUtilities.getJextIconImage());
        try {
            this.regexp = new RE((Object)"File \"([^\"]+)\", line (\\d+),.*");
        }
        catch (REException var3_3) {
            this.dispose();
        }
        this.pack();
        Utilities.centerComponent(this);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        if (object == this.clear) {
            this.textArea.setText("");
        } else if (object == this.dock) {
            if (!this.docked) {
                this.dispose();
                this.content = this.getContentPane();
                this.parent.getVerticalTabbedPane().add(Jext.getProperty("python.window.tab"), this.content);
                this.dock.setLabel(Jext.getProperty("python.window.undock"));
                this.setContentPane(new JPanel());
                this.docked = true;
            } else {
                this.parent.getVerticalTabbedPane().remove(this.content);
                this.setContentPane(this.content);
                this.dock.setLabel(Jext.getProperty("python.window.dock"));
                SwingUtilities.invokeLater(new Runnable(){

                    public void run() {
                        PythonLogWindow.this.setVisible(true);
                        PythonLogWindow.this.toFront();
                        PythonLogWindow.this.docked = false;
                    }
                });
            }
        }
    }

    public boolean isDocked() {
        return this.docked;
    }

    public void log(String string) {
        this.textArea.append(string);
        this.textArea.setSelectionStart(this.textArea.getDocument().getLength());
        this.textArea.setSelectionEnd(this.textArea.getDocument().getLength());
    }

    public void logln(String string) {
        this.log(string + '\n');
    }

    public Writer getStdOut() {
        return this.writerStdOut;
    }

    public Writer getStdErr() {
        return this.writeStdErr;
    }

    class MouseHandler
    extends MouseAdapter {
        MouseHandler() {
        }

        public void mouseClicked(MouseEvent mouseEvent) {
            if (mouseEvent.getClickCount() == 2) {
                try {
                    Element element = PythonLogWindow.this.textArea.getDocument().getDefaultRootElement();
                    Element element2 = element.getElement(element.getElementIndex(PythonLogWindow.this.textArea.getCaretPosition()));
                    int n = element2.getStartOffset();
                    REMatch rEMatch = PythonLogWindow.this.regexp.getMatch((Object)PythonLogWindow.this.textArea.getText(n, element2.getEndOffset() - 1 - n));
                    if (rEMatch != null) {
                        String string = rEMatch.toString(1);
                        int n2 = 0;
                        n2 = Integer.parseInt(rEMatch.toString(2));
                        JextTextArea jextTextArea = null;
                        if (string.equals("<string>")) {
                            jextTextArea = PythonLogWindow.this.parent.getTextArea();
                        } else {
                            JextTextArea[] arrjextTextArea = PythonLogWindow.this.parent.getTextAreas();
                            for (int i = 0; i < arrjextTextArea.length; ++i) {
                                if (!string.equals(arrjextTextArea[i].getCurrentFile())) continue;
                                jextTextArea = arrjextTextArea[i];
                                break;
                            }
                            if (jextTextArea == null) {
                                jextTextArea = PythonLogWindow.this.parent.open(string, false);
                            }
                        }
                        element2 = jextTextArea.getDocument().getDefaultRootElement().getElement(n2 - 1);
                        if (element2 != null) {
                            jextTextArea.select(element2.getStartOffset(), element2.getEndOffset() - 1);
                        }
                    }
                }
                catch (Exception var2_3) {
                    // empty catch block
                }
            }
        }
    }

}

