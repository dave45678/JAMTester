/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.textarea;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.swing.KeyStroke;
import org.gjt.sp.jedit.textarea.InputHandler;
import org.jext.JextTextArea;
import org.jext.OneClickAction;

public class DefaultInputHandler
extends InputHandler {
    private Hashtable bindings;
    private Hashtable currentBindings;

    public DefaultInputHandler() {
        this.bindings = this.currentBindings = new Hashtable();
    }

    public DefaultInputHandler(DefaultInputHandler defaultInputHandler) {
        this.bindings = this.currentBindings = defaultInputHandler.bindings;
    }

    public void addDefaultKeyBindings() {
        this.addKeyBinding("BACK_SPACE", BACKSPACE);
        this.addKeyBinding("C+BACK_SPACE", BACKSPACE_WORD);
        this.addKeyBinding("DELETE", DELETE);
        this.addKeyBinding("C+DELETE", DELETE_WORD);
        this.addKeyBinding("ENTER", INSERT_BREAK);
        this.addKeyBinding("TAB", INSERT_TAB);
        this.addKeyBinding("INSERT", OVERWRITE);
        this.addKeyBinding("HOME", HOME);
        this.addKeyBinding("END", END);
        this.addKeyBinding("S+HOME", SELECT_HOME);
        this.addKeyBinding("S+END", SELECT_END);
        this.addKeyBinding("C+HOME", DOCUMENT_HOME);
        this.addKeyBinding("C+END", DOCUMENT_END);
        this.addKeyBinding("CS+HOME", SELECT_DOC_HOME);
        this.addKeyBinding("CS+END", SELECT_DOC_END);
        this.addKeyBinding("PAGE_UP", PREV_PAGE);
        this.addKeyBinding("PAGE_DOWN", NEXT_PAGE);
        this.addKeyBinding("S+PAGE_UP", SELECT_PREV_PAGE);
        this.addKeyBinding("S+PAGE_DOWN", SELECT_NEXT_PAGE);
        this.addKeyBinding("LEFT", PREV_CHAR);
        this.addKeyBinding("S+LEFT", SELECT_PREV_CHAR);
        this.addKeyBinding("C+LEFT", PREV_WORD);
        this.addKeyBinding("CS+LEFT", SELECT_PREV_WORD);
        this.addKeyBinding("RIGHT", NEXT_CHAR);
        this.addKeyBinding("S+RIGHT", SELECT_NEXT_CHAR);
        this.addKeyBinding("C+RIGHT", NEXT_WORD);
        this.addKeyBinding("CS+RIGHT", SELECT_NEXT_WORD);
        this.addKeyBinding("UP", PREV_LINE);
        this.addKeyBinding("S+UP", SELECT_PREV_LINE);
        this.addKeyBinding("DOWN", NEXT_LINE);
        this.addKeyBinding("S+DOWN", SELECT_NEXT_LINE);
        this.addKeyBinding("C+ENTER", REPEAT);
    }

    public void addKeyBinding(String string, ActionListener actionListener) {
        Hashtable hashtable = this.bindings;
        StringTokenizer stringTokenizer = new StringTokenizer(string);
        while (stringTokenizer.hasMoreTokens()) {
            KeyStroke keyStroke = DefaultInputHandler.parseKeyStroke(stringTokenizer.nextToken());
            if (keyStroke == null) {
                return;
            }
            if (stringTokenizer.hasMoreTokens()) {
                Object object = hashtable.get(keyStroke);
                if (object instanceof Hashtable) {
                    hashtable = (Hashtable)object;
                    continue;
                }
                object = new Hashtable();
                hashtable.put(keyStroke, object);
                hashtable = (Hashtable)object;
                continue;
            }
            hashtable.put(keyStroke, actionListener);
        }
    }

    public void removeKeyBinding(String string) {
        throw new InternalError("Not yet implemented");
    }

    public void removeAllKeyBindings() {
        this.bindings.clear();
    }

    public void keyPressed(KeyEvent keyEvent) {
        int n = keyEvent.getKeyCode();
        int n2 = keyEvent.getModifiers();
        if (n == 17 || n == 16 || n == 18 || n == 157) {
            return;
        }
        if (keyEvent.isShiftDown()) {
            if (this.grabAction != null) {
                this.handleGrabAction(keyEvent);
                return;
            }
            KeyStroke keyStroke = KeyStroke.getKeyStroke(n, n2);
            Object v = this.currentBindings.get(keyStroke);
            if (v == null) {
                if (this.currentBindings != this.bindings) {
                    Toolkit.getDefaultToolkit().beep();
                    this.repeatCount = 0;
                    this.repeat = false;
                    keyEvent.consume();
                }
                this.currentBindings = this.bindings;
                return;
            }
            if (v instanceof ActionListener) {
                this.currentBindings = this.bindings;
                ((JextTextArea)DefaultInputHandler.getTextArea(keyEvent)).endCurrentEdit();
                this.executeAction((ActionListener)v, keyEvent.getSource(), null);
                keyEvent.consume();
                return;
            }
            if (v instanceof Hashtable) {
                this.currentBindings = (Hashtable)v;
                keyEvent.consume();
                return;
            }
        } else if (!keyEvent.isShiftDown() || keyEvent.isActionKey() || n == 8 || n == 127 || n == 10 || n == 9 || n == 27) {
            if (this.grabAction != null) {
                this.handleGrabAction(keyEvent);
                return;
            }
            KeyStroke keyStroke = KeyStroke.getKeyStroke(n, n2);
            Object v = this.currentBindings.get(keyStroke);
            if (v == null) {
                if (this.currentBindings != this.bindings) {
                    Toolkit.getDefaultToolkit().beep();
                    this.repeatCount = 0;
                    this.repeat = false;
                    keyEvent.consume();
                }
                this.currentBindings = this.bindings;
                return;
            }
            if (v instanceof ActionListener) {
                this.currentBindings = this.bindings;
                ((JextTextArea)DefaultInputHandler.getTextArea(keyEvent)).endCurrentEdit();
                this.executeAction((ActionListener)v, keyEvent.getSource(), null);
                keyEvent.consume();
                return;
            }
            if (v instanceof Hashtable) {
                this.currentBindings = (Hashtable)v;
                keyEvent.consume();
                return;
            }
        }
    }

    public void keyTyped(KeyEvent keyEvent) {
        char c = keyEvent.getKeyChar();
        if (!(c == '\uffff' || keyEvent.isAltDown() || c < ' ' || c == '')) {
            KeyStroke keyStroke = KeyStroke.getKeyStroke(Character.toUpperCase(c));
            Object v = this.currentBindings.get(keyStroke);
            if (v instanceof Hashtable) {
                this.currentBindings = (Hashtable)v;
                return;
            }
            if (v instanceof OneClickAction) {
                this.currentBindings = this.bindings;
                ((JextTextArea)DefaultInputHandler.getTextArea(keyEvent)).endCurrentEdit();
                this.executeOneClickAction((OneClickAction)v, keyEvent.getSource(), String.valueOf(c));
                return;
            }
            if (v instanceof ActionListener) {
                this.currentBindings = this.bindings;
                ((JextTextArea)DefaultInputHandler.getTextArea(keyEvent)).endCurrentEdit();
                this.executeAction((ActionListener)v, keyEvent.getSource(), String.valueOf(c));
                return;
            }
            this.currentBindings = this.bindings;
            if (this.grabAction != null) {
                this.handleGrabAction(keyEvent);
                return;
            }
            if (this.repeat && Character.isDigit(c)) {
                this.setRepeatCount(this.repeatCount * 10 + (c - 48));
                keyEvent.consume();
                return;
            }
            this.executeAction(this.inputAction, keyEvent.getSource(), String.valueOf(keyEvent.getKeyChar()));
            ((JextTextArea)DefaultInputHandler.getTextArea(keyEvent)).userInput(c);
            this.repeatCount = 0;
            this.repeat = false;
        }
    }

    public static KeyStroke parseKeyStroke(String string) {
        String string2;
        int n;
        if (string == null) {
            return null;
        }
        int n2 = 0;
        int n3 = string.indexOf(43);
        if (n3 != -1) {
            block8 : for (int i = 0; i < n3; ++i) {
                switch (Character.toUpperCase(string.charAt(i))) {
                    case 'A': {
                        n2|=8;
                        continue block8;
                    }
                    case 'C': {
                        n2|=Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
                        continue block8;
                    }
                    case 'M': {
                        n2|=4;
                        continue block8;
                    }
                    case 'S': {
                        n2|=1;
                    }
                }
            }
        }
        if ((string2 = string.substring(n3 + 1)).length() == 1) {
            char c = Character.toUpperCase(string2.charAt(0));
            if (n2 == 0) {
                return KeyStroke.getKeyStroke(c);
            }
            return KeyStroke.getKeyStroke((int)c, n2);
        }
        if (string2.length() == 0) {
            System.err.println("Invalid key stroke: " + string);
            return null;
        }
        try {
            Class class_ = KeyEvent.class;
            n = class_.getField("VK_".concat(string2)).getInt(null);
        }
        catch (Exception var5_7) {
            System.err.println("Invalid key stroke: " + string);
            return null;
        }
        return KeyStroke.getKeyStroke(n, n2);
    }
}

