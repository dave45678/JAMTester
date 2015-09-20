/*
 * Decompiled with CFR 0_102.
 */
package org.jext.console;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import org.jext.console.Console;

public class ConsoleTextPane
extends JTextPane {
    private Console parent;
    private ConsoleKeyAdapter _keyListener;
    private MouseAdapter _mouseListener;

    public ConsoleTextPane(Console console) {
        this.parent = console;
        new DropTarget(this, new DnDHandler());
        this._keyListener = new ConsoleKeyAdapter();
        this.addKeyListener(this._keyListener);
        this._mouseListener = new MouseAdapter(){

            public void mousePressed(MouseEvent mouseEvent) {
                mouseEvent.consume();
                if (ConsoleTextPane.this.getCaretPosition() < ConsoleTextPane.this.parent.getUserLimit()) {
                    ConsoleTextPane.this.setCaretPosition(ConsoleTextPane.this.getDocument().getLength());
                }
            }
        };
        this.addMouseListener(this._mouseListener);
    }

    protected void finalize() throws Throwable {
        this.removeKeyListener(this._keyListener);
        this.removeMouseListener(this._mouseListener);
        super.finalize();
        this.parent = null;
        this._keyListener = null;
        this._mouseListener = null;
    }

    class DnDHandler
    implements DropTargetListener {
        DnDHandler() {
        }

        public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
        }

        public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
        }

        public void dragExit(DropTargetEvent dropTargetEvent) {
        }

        public void dragScroll(DropTargetDragEvent dropTargetDragEvent) {
        }

        public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
        }

        public void drop(DropTargetDropEvent dropTargetDropEvent) {
            DataFlavor[] arrdataFlavor = dropTargetDropEvent.getCurrentDataFlavors();
            if (arrdataFlavor == null) {
                return;
            }
            boolean bl = false;
            for (int i = arrdataFlavor.length - 1; i >= 0; --i) {
                if (!arrdataFlavor[i].isFlavorJavaFileListType()) continue;
                dropTargetDropEvent.acceptDrop(1);
                Transferable transferable = dropTargetDropEvent.getTransferable();
                try {
                    StringBuffer stringBuffer = new StringBuffer();
                    Iterator iterator = ((List)transferable.getTransferData(arrdataFlavor[i])).iterator();
                    while (iterator.hasNext()) {
                        stringBuffer.append(' ').append(((File)iterator.next()).getPath());
                    }
                    ConsoleTextPane.this.parent.add(stringBuffer.toString());
                    bl = true;
                    continue;
                }
                catch (Exception var6_7) {
                    // empty catch block
                }
            }
            dropTargetDropEvent.dropComplete(bl);
        }
    }

    class ConsoleKeyAdapter
    extends KeyAdapter {
        ConsoleKeyAdapter() {
        }

        public void keyPressed(KeyEvent keyEvent) {
            int n = keyEvent.getKeyCode();
            if (keyEvent.isControlDown()) {
                switch (n) {
                    case 67: {
                        return;
                    }
                    case 68: {
                        ConsoleTextPane.this.parent.stop();
                        try {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException var3_3) {
                            // empty catch block
                        }
                        ConsoleTextPane.this.parent.displayPrompt();
                    }
                }
                keyEvent.consume();
                return;
            }
            if (keyEvent.isShiftDown() && n == 9) {
                ConsoleTextPane.this.parent.doBackwardSearch();
                keyEvent.consume();
                return;
            }
            switch (n) {
                case 127: {
                    ConsoleTextPane.this.parent.deleteChar();
                    keyEvent.consume();
                    break;
                }
                case 8: {
                    ConsoleTextPane.this.parent.removeChar();
                    keyEvent.consume();
                    break;
                }
                case 10: {
                    String string = ConsoleTextPane.this.parent.getText();
                    if (string.equals("")) {
                        keyEvent.consume();
                        return;
                    }
                    ConsoleTextPane.this.parent.addHistory(string);
                    ConsoleTextPane.this.parent.execute(string);
                    keyEvent.consume();
                    break;
                }
                case 38: {
                    ConsoleTextPane.this.parent.historyPrevious();
                    keyEvent.consume();
                    break;
                }
                case 40: {
                    ConsoleTextPane.this.parent.historyNext();
                    keyEvent.consume();
                }
                case 37: {
                    if (ConsoleTextPane.this.getCaretPosition() > ConsoleTextPane.this.parent.getUserLimit()) {
                        ConsoleTextPane.this.setCaretPosition(ConsoleTextPane.this.getCaretPosition() - 1);
                    }
                    keyEvent.consume();
                    break;
                }
                case 9: {
                    ConsoleTextPane.this.parent.doCompletion();
                    keyEvent.consume();
                    break;
                }
                case 36: {
                    ConsoleTextPane.this.setCaretPosition(ConsoleTextPane.this.parent.getUserLimit());
                    keyEvent.consume();
                    break;
                }
                case 35: {
                    ConsoleTextPane.this.setCaretPosition(ConsoleTextPane.this.parent.getTypingLocation());
                    keyEvent.consume();
                    break;
                }
                case 27: {
                    ConsoleTextPane.this.parent.setText("");
                    keyEvent.consume();
                }
            }
        }

        public void keyTyped(KeyEvent keyEvent) {
            char c;
            if (ConsoleTextPane.this.parent.getTypingLocation() < ConsoleTextPane.this.getDocument().getLength()) {
                keyEvent.consume();
                return;
            }
            if (ConsoleTextPane.this.getCaretPosition() < ConsoleTextPane.this.parent.getUserLimit()) {
                ConsoleTextPane.this.setCaretPosition(ConsoleTextPane.this.parent.getUserLimit());
            }
            if (!((c = keyEvent.getKeyChar()) == '\uffff' || keyEvent.isAltDown() || c < ' ' || c == '')) {
                ConsoleTextPane.this.parent.add(String.valueOf(c));
            }
            keyEvent.consume();
        }
    }

}

