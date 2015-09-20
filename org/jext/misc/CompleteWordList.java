/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import org.jext.GUIUtilities;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.gui.ModifiedCellRenderer;

public class CompleteWordList
extends JWindow
implements CaretListener {
    private JList itemsList;
    private String word;
    private JextFrame parent;
    private JextTextArea textArea;

    public CompleteWordList(JextFrame jextFrame, String string, String[] arrstring) {
        super(jextFrame);
        this.parent = jextFrame;
        this.textArea = jextFrame.getTextArea();
        this.word = string;
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        Font font = new Font("Monospaced", 0, 11);
        Object[] arrobject = new String[]{string};
        JLabel jLabel = new JLabel(Jext.getProperty("completeWord.list.title", arrobject));
        jLabel.setFont(font);
        jPanel.add((Component)jLabel, "North");
        this.itemsList = new JList<String>(arrstring);
        this.itemsList.setFont(font);
        this.itemsList.setVisibleRowCount(arrstring.length < 5 ? arrstring.length : 5);
        this.itemsList.setSelectedIndex(0);
        this.itemsList.setSelectionMode(0);
        this.itemsList.addMouseListener(new MouseHandler());
        this.itemsList.setCellRenderer(new ModifiedCellRenderer());
        FontMetrics fontMetrics = this.getFontMetrics(font);
        this.itemsList.setPreferredSize(new Dimension(15 * fontMetrics.charWidth('m'), this.itemsList.getPreferredSize().height));
        JScrollPane jScrollPane = new JScrollPane(this.itemsList);
        jScrollPane.setBorder(null);
        jPanel.add((Component)jScrollPane, "South");
        jPanel.setBorder(LineBorder.createBlackLineBorder());
        this.getContentPane().add(jPanel);
        this.setBackground(Color.lightGray);
        GUIUtilities.requestFocus(this, this.itemsList);
        this.pack();
        int n = this.textArea.getCaretPosition();
        int n2 = this.textArea.getCaretLine();
        int n3 = this.textArea.offsetToX(n2, n - this.textArea.getLineStartOffset(n2));
        Dimension dimension = jextFrame.getSize();
        Point point = jextFrame.getLocationOnScreen();
        Insets insets = jextFrame.getInsets();
        Point point2 = this.textArea.getLocationOnScreen();
        Dimension dimension2 = this.getSize();
        if ((n3+=point2.x) + dimension2.width > point.x + dimension.width - insets.right) {
            n3-=dimension2.width;
        }
        this.setLocation(n3, point2.y + this.textArea.lineToY(n2) + fontMetrics.getHeight() + fontMetrics.getDescent() + fontMetrics.getLeading());
        this.setVisible(true);
        KeyHandler keyHandler = new KeyHandler();
        this.addKeyListener(keyHandler);
        this.itemsList.addKeyListener(keyHandler);
        jextFrame.setKeyEventInterceptor(keyHandler);
        this.textArea.addCaretListener(this);
    }

    public void caretUpdate(CaretEvent caretEvent) {
        this.dispose();
    }

    public void dispose() {
        this.parent.setKeyEventInterceptor(null);
        this.textArea.removeCaretListener(this);
        super.dispose();
        SwingUtilities.invokeLater(new Runnable(){

            public void run() {
                CompleteWordList.this.textArea.requestFocus();
            }
        });
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.itemsList = null;
        this.word = null;
        this.parent = null;
        this.textArea = null;
    }

    class MouseHandler
    extends MouseAdapter {
        MouseHandler() {
        }

        public void mouseClicked(MouseEvent mouseEvent) {
            CompleteWordList.this.textArea.setSelectedText(((String)CompleteWordList.this.itemsList.getSelectedValue()).substring(CompleteWordList.this.word.length()));
            CompleteWordList.this.dispose();
        }
    }

    class KeyHandler
    extends KeyAdapter {
        KeyHandler() {
        }

        public void keyTyped(KeyEvent keyEvent) {
            char c = keyEvent.getKeyChar();
            if (keyEvent.getModifiers() == 0 && c != '\b') {
                CompleteWordList.this.textArea.setSelectedText(String.valueOf(c));
            }
        }

        public void keyPressed(KeyEvent keyEvent) {
            switch (keyEvent.getKeyCode()) {
                case 10: 
                case 32: {
                    CompleteWordList.this.textArea.setSelectedText(((String)CompleteWordList.this.itemsList.getSelectedValue()).substring(CompleteWordList.this.word.length()));
                    keyEvent.consume();
                    CompleteWordList.this.dispose();
                    break;
                }
                case 27: {
                    CompleteWordList.this.dispose();
                    keyEvent.consume();
                    break;
                }
                case 33: {
                    if (CompleteWordList.this.getFocusOwner() == CompleteWordList.this.itemsList) {
                        return;
                    }
                    int n = CompleteWordList.this.itemsList.getSelectedIndex();
                    if ((n-=5) < 0) {
                        n = CompleteWordList.this.itemsList.getModel().getSize() - 1;
                    }
                    CompleteWordList.this.itemsList.setSelectedIndex(n);
                    CompleteWordList.this.itemsList.ensureIndexIsVisible(n);
                    keyEvent.consume();
                    break;
                }
                case 34: {
                    if (CompleteWordList.this.getFocusOwner() == CompleteWordList.this.itemsList) {
                        return;
                    }
                    int n = CompleteWordList.this.itemsList.getSelectedIndex();
                    if ((n+=5) >= CompleteWordList.this.itemsList.getModel().getSize()) {
                        n = 0;
                    }
                    CompleteWordList.this.itemsList.setSelectedIndex(n);
                    CompleteWordList.this.itemsList.ensureIndexIsVisible(n);
                    keyEvent.consume();
                    break;
                }
                case 38: {
                    if (CompleteWordList.this.getFocusOwner() == CompleteWordList.this.itemsList) {
                        return;
                    }
                    int n = CompleteWordList.this.itemsList.getSelectedIndex();
                    n = n == 0 ? CompleteWordList.this.itemsList.getModel().getSize() - 1 : --n;
                    CompleteWordList.this.itemsList.setSelectedIndex(n);
                    CompleteWordList.this.itemsList.ensureIndexIsVisible(n);
                    keyEvent.consume();
                    break;
                }
                case 40: {
                    if (CompleteWordList.this.getFocusOwner() == CompleteWordList.this.itemsList) {
                        return;
                    }
                    int n = CompleteWordList.this.itemsList.getSelectedIndex();
                    if (n == CompleteWordList.this.itemsList.getModel().getSize() - 1) {
                        return;
                    }
                    CompleteWordList.this.itemsList.setSelectedIndex(++n);
                    CompleteWordList.this.itemsList.ensureIndexIsVisible(n);
                    keyEvent.consume();
                    break;
                }
                default: {
                    if (!keyEvent.isActionKey()) break;
                    CompleteWordList.this.dispose();
                    CompleteWordList.this.parent.processKeyEvent(keyEvent);
                }
            }
        }
    }

}

