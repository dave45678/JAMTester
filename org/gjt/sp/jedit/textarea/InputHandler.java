/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.textarea;

import java.awt.Component;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Hashtable;
import javax.swing.JPopupMenu;
import javax.swing.text.BadLocationException;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.textarea.TextUtilities;
import org.jext.EditAction;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.OneClickAction;
import org.jext.Utilities;
import org.jext.misc.WordMove;

public abstract class InputHandler
extends KeyAdapter {
    public static final String SMART_HOME_END_PROPERTY = "InputHandler.homeEnd";
    public static final ActionListener BACKSPACE = new backspace();
    public static final ActionListener BACKSPACE_WORD = new backspace_word();
    public static final ActionListener DELETE = new delete();
    public static final ActionListener DELETE_WORD = new delete_word();
    public static final ActionListener END = new end(false);
    public static final ActionListener DOCUMENT_END = new document_end(false);
    public static final ActionListener SELECT_END = new end(true);
    public static final ActionListener SELECT_DOC_END = new document_end(true);
    public static final ActionListener INSERT_BREAK = new insert_break();
    public static final ActionListener INSERT_TAB = new insert_tab();
    public static final ActionListener HOME = new home(false);
    public static final ActionListener DOCUMENT_HOME = new document_home(false);
    public static final ActionListener SELECT_HOME = new home(true);
    public static final ActionListener SELECT_DOC_HOME = new document_home(true);
    public static final ActionListener NEXT_CHAR = new next_char(false);
    public static final ActionListener NEXT_LINE = new next_line(false);
    public static final ActionListener NEXT_PAGE = new next_page(false);
    public static final ActionListener NEXT_WORD = new next_word(false);
    public static final ActionListener SELECT_NEXT_CHAR = new next_char(true);
    public static final ActionListener SELECT_NEXT_LINE = new next_line(true);
    public static final ActionListener SELECT_NEXT_PAGE = new next_page(true);
    public static final ActionListener SELECT_NEXT_WORD = new next_word(true);
    public static final ActionListener OVERWRITE = new overwrite();
    public static final ActionListener PREV_CHAR = new prev_char(false);
    public static final ActionListener PREV_LINE = new prev_line(false);
    public static final ActionListener PREV_PAGE = new prev_page(false);
    public static final ActionListener PREV_WORD = new prev_word(false);
    public static final ActionListener SELECT_PREV_CHAR = new prev_char(true);
    public static final ActionListener SELECT_PREV_LINE = new prev_line(true);
    public static final ActionListener SELECT_PREV_PAGE = new prev_page(true);
    public static final ActionListener SELECT_PREV_WORD = new prev_word(true);
    public static final ActionListener REPEAT = new repeat();
    public static final ActionListener INSERT_CHAR = new insert_char();
    private static Hashtable actions = new Hashtable<K, V>();
    protected ActionListener inputAction = INSERT_CHAR;
    protected ActionListener grabAction;
    protected boolean repeat;
    protected int repeatCount;
    protected MacroRecorder recorder;

    public static ActionListener getAction(String string) {
        return (ActionListener)actions.get(string);
    }

    public static String getActionName(ActionListener actionListener) {
        Enumeration enumeration = InputHandler.getActions();
        while (enumeration.hasMoreElements()) {
            String string = (String)enumeration.nextElement();
            ActionListener actionListener2 = InputHandler.getAction(string);
            if (actionListener2 != actionListener) continue;
            return string;
        }
        return null;
    }

    public static Enumeration getActions() {
        return actions.keys();
    }

    public abstract void addDefaultKeyBindings();

    public abstract void addKeyBinding(String var1, ActionListener var2);

    public abstract void removeKeyBinding(String var1);

    public abstract void removeAllKeyBindings();

    public void grabNextKeyStroke(ActionListener actionListener) {
        this.grabAction = actionListener;
    }

    public boolean isRepeatEnabled() {
        return this.repeat;
    }

    public void setRepeatEnabled(boolean bl) {
        this.repeat = bl;
        if (!bl) {
            this.repeatCount = 0;
        }
    }

    public int getRepeatCount() {
        return this.repeat ? Math.max(1, this.repeatCount) : 1;
    }

    public void setRepeatCount(int n) {
        this.repeatCount = n;
    }

    public ActionListener getInputAction() {
        return this.inputAction;
    }

    public void setInputAction(ActionListener actionListener) {
        this.inputAction = actionListener;
    }

    public MacroRecorder getMacroRecorder() {
        return this.recorder;
    }

    public void setMacroRecorder(MacroRecorder macroRecorder) {
        this.recorder = macroRecorder;
    }

    public void executeOneClickAction(OneClickAction oneClickAction, Object object, String string) {
        ActionEvent actionEvent = new ActionEvent(object, 1001, string);
        oneClickAction.oneClickActionPerformed(actionEvent);
    }

    public void executeAction(ActionListener actionListener, Object object, String string) {
        ActionEvent actionEvent = new ActionEvent(object, 1001, string);
        if (actionListener instanceof EditAction && !InputHandler.getTextArea(actionEvent).isEditable()) {
            return;
        }
        InputHandler.getTextArea(actionEvent).setOneClick(null);
        if (actionListener instanceof Wrapper) {
            actionListener.actionPerformed(actionEvent);
            return;
        }
        boolean bl = this.repeat;
        int n = this.getRepeatCount();
        if (actionListener instanceof NonRepeatable) {
            actionListener.actionPerformed(actionEvent);
        } else {
            for (int i = 0; i < Math.max(1, n); ++i) {
                actionListener.actionPerformed(actionEvent);
            }
        }
        if (this.grabAction == null) {
            if (!(this.recorder == null || actionListener instanceof NonRecordable)) {
                if (n != 1) {
                    this.recorder.actionPerformed(REPEAT, String.valueOf(n));
                }
                this.recorder.actionPerformed(actionListener, string);
            }
            if (bl) {
                this.setRepeatEnabled(false);
            }
        }
    }

    public static JEditTextArea getTextArea(EventObject eventObject) {
        Object object;
        if (eventObject != null && (object = eventObject.getSource()) instanceof Component) {
            Component component = (Component)object;
            do {
                if (component instanceof JEditTextArea) {
                    return (JEditTextArea)component;
                }
                if (component == null) break;
                if (component instanceof JPopupMenu) {
                    component = ((JPopupMenu)component).getInvoker();
                    continue;
                }
                if (component instanceof JextFrame) {
                    component = ((JextFrame)component).getTextArea();
                    continue;
                }
                component = component.getParent();
            } while (true);
        }
        System.err.println("BUG: getTextArea() returning null");
        System.err.println("Report this to Romain Guy <romain.guy@jext.org>");
        return null;
    }

    protected void handleGrabAction(KeyEvent keyEvent) {
        ActionListener actionListener = this.grabAction;
        this.grabAction = null;
        char c = keyEvent.getKeyChar();
        int n = keyEvent.getKeyCode();
        String string = c != '\u0000' ? String.valueOf(c) : (n == 9 ? "\t" : (n == 10 ? "\n" : "\u0000"));
        this.executeAction(actionListener, keyEvent.getSource(), string);
    }

    static {
        actions.put("backspace", BACKSPACE);
        actions.put("backspace-word", BACKSPACE_WORD);
        actions.put("delete", DELETE);
        actions.put("delete-word", DELETE_WORD);
        actions.put("end", END);
        actions.put("select-end", SELECT_END);
        actions.put("document-end", DOCUMENT_END);
        actions.put("select-doc-end", SELECT_DOC_END);
        actions.put("insert-break", INSERT_BREAK);
        actions.put("insert-tab", INSERT_TAB);
        actions.put("home", HOME);
        actions.put("select-home", SELECT_HOME);
        actions.put("document-home", DOCUMENT_HOME);
        actions.put("select-doc-home", SELECT_DOC_HOME);
        actions.put("next-char", NEXT_CHAR);
        actions.put("next-line", NEXT_LINE);
        actions.put("next-page", NEXT_PAGE);
        actions.put("next-word", NEXT_WORD);
        actions.put("select-next-char", SELECT_NEXT_CHAR);
        actions.put("select-next-line", SELECT_NEXT_LINE);
        actions.put("select-next-page", SELECT_NEXT_PAGE);
        actions.put("select-next-word", SELECT_NEXT_WORD);
        actions.put("overwrite", OVERWRITE);
        actions.put("prev-char", PREV_CHAR);
        actions.put("prev-line", PREV_LINE);
        actions.put("prev-page", PREV_PAGE);
        actions.put("prev-word", PREV_WORD);
        actions.put("select-prev-char", SELECT_PREV_CHAR);
        actions.put("select-prev-line", SELECT_PREV_LINE);
        actions.put("select-prev-page", SELECT_PREV_PAGE);
        actions.put("select-prev-word", SELECT_PREV_WORD);
        actions.put("repeat", REPEAT);
        actions.put("insert-char", INSERT_CHAR);
    }

    public static class insert_char
    implements ActionListener,
    NonRepeatable {
        public void actionPerformed(ActionEvent actionEvent) {
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            String string = actionEvent.getActionCommand();
            int n = jEditTextArea.getInputHandler().getRepeatCount();
            if (jEditTextArea.isEditable()) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < n; ++i) {
                    stringBuffer.append(string);
                }
                jEditTextArea.overwriteSetSelectedText(stringBuffer.toString());
            } else {
                jEditTextArea.getToolkit().beep();
            }
        }
    }

    public static class repeat
    implements ActionListener,
    NonRecordable {
        public void actionPerformed(ActionEvent actionEvent) {
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            jEditTextArea.getInputHandler().setRepeatEnabled(true);
            String string = actionEvent.getActionCommand();
            if (string != null) {
                jEditTextArea.getInputHandler().setRepeatCount(Integer.parseInt(string));
            }
        }
    }

    public static class prev_word
    implements ActionListener {
        private boolean select;

        public prev_word(boolean bl) {
            this.select = bl;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            int n;
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            int n2 = jEditTextArea.getCaretLine();
            int n3 = jEditTextArea.getLineStartOffset(n2);
            String string = jEditTextArea.getLineText(n2);
            boolean bl = WordMove.isSpaceBefore();
            boolean bl2 = WordMove.isEnabled();
            if ((n-=n3) == 0) {
                if (n3 == 0) {
                    jEditTextArea.getToolkit().beep();
                    return;
                }
                --n;
            } else {
                if (bl2) {
                    for (n = jEditTextArea.getCaretPosition(); n > 0 && string.charAt(n - 1) == ' '; --n) {
                    }
                }
                if (n > 0) {
                    String string2 = ((JextTextArea)jEditTextArea).getProperty("noWordSep");
                    if (bl) {
                        for (n = TextUtilities.findWordStart((String)string, (int)(n - 1), (String)string2); n > 0 && string.charAt(n - 1) == ' '; --n) {
                        }
                    }
                }
            }
            if (this.select) {
                jEditTextArea.select(jEditTextArea.getMarkPosition(), n3 + n);
            } else {
                jEditTextArea.setCaretPosition(n3 + n);
            }
        }
    }

    public static class prev_page
    implements ActionListener {
        private boolean select;

        public prev_page(boolean bl) {
            this.select = bl;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            int n = jEditTextArea.getFirstLine();
            int n2 = jEditTextArea.getVisibleLines();
            int n3 = jEditTextArea.getCaretLine();
            if (n < n2) {
                n = n2;
            }
            jEditTextArea.setFirstLine(n - n2);
            int n4 = jEditTextArea.getLineStartOffset(Math.max(0, n3 - n2));
            if (this.select) {
                jEditTextArea.select(jEditTextArea.getMarkPosition(), n4);
            } else {
                jEditTextArea.setCaretPosition(n4);
            }
        }
    }

    public static class prev_line
    implements ActionListener {
        private boolean select;

        public prev_line(boolean bl) {
            this.select = bl;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            int n = jEditTextArea.getCaretPosition();
            int n2 = jEditTextArea.getCaretLine();
            if (n2 == 0) {
                jEditTextArea.getToolkit().beep();
                return;
            }
            int n3 = jEditTextArea.getMagicCaretPosition();
            if (n3 == -1) {
                n3 = jEditTextArea.offsetToX(n2, n - jEditTextArea.getLineStartOffset(n2));
            }
            n = jEditTextArea.getLineStartOffset(n2 - 1) + jEditTextArea.xToOffset(n2 - 1, n3 + 1);
            if (this.select) {
                jEditTextArea.select(jEditTextArea.getMarkPosition(), n);
            } else {
                jEditTextArea.setCaretPosition(n);
            }
            jEditTextArea.setMagicCaretPosition(n3);
        }
    }

    public static class prev_char
    implements ActionListener {
        private boolean select;

        public prev_char(boolean bl) {
            this.select = bl;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            int n = jEditTextArea.getCaretPosition();
            if (n == 0) {
                jEditTextArea.getToolkit().beep();
                return;
            }
            if (this.select) {
                jEditTextArea.select(jEditTextArea.getMarkPosition(), n - 1);
            } else {
                jEditTextArea.setCaretPosition(n - 1);
            }
        }
    }

    public static class overwrite
    implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            JEditTextArea jEditTextArea;
            jEditTextArea.setOverwriteEnabled(!(jEditTextArea = InputHandler.getTextArea(actionEvent)).isOverwriteEnabled());
        }
    }

    public static class next_word
    implements ActionListener {
        private boolean select;

        public next_word(boolean bl) {
            this.select = bl;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            int n;
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            int n2 = jEditTextArea.getCaretLine();
            int n3 = jEditTextArea.getLineStartOffset(n2);
            String string = jEditTextArea.getLineText(n2);
            int n4 = string.length();
            boolean bl = WordMove.isSpaceAfter();
            boolean bl2 = WordMove.isEnabled();
            if ((n-=n3) == n4) {
                if (n3 + n == jEditTextArea.getDocumentLength()) {
                    jEditTextArea.getToolkit().beep();
                    return;
                }
                ++n;
            } else {
                if (bl2) {
                    for (n = jEditTextArea.getCaretPosition(); n < n4 && string.charAt(n) == ' '; ++n) {
                    }
                }
                if (n < n4) {
                    String string2 = ((JextTextArea)jEditTextArea).getProperty("noWordSep");
                    if (bl) {
                        for (n = TextUtilities.findWordEnd((String)string, (int)(n + 1), (String)string2); n < n4 && string.charAt(n) == ' '; ++n) {
                        }
                    }
                }
            }
            if (this.select) {
                jEditTextArea.select(jEditTextArea.getMarkPosition(), n3 + n);
            } else {
                jEditTextArea.setCaretPosition(n3 + n);
            }
        }
    }

    public static class next_page
    implements ActionListener {
        private boolean select;

        public next_page(boolean bl) {
            this.select = bl;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            int n = jEditTextArea.getLineCount();
            int n2 = jEditTextArea.getFirstLine();
            int n3 = jEditTextArea.getVisibleLines();
            int n4 = jEditTextArea.getCaretLine();
            if ((n2+=n3) + n3 >= n - 1) {
                n2 = n - n3;
            }
            jEditTextArea.setFirstLine(n2);
            int n5 = jEditTextArea.getLineStartOffset(Math.min(jEditTextArea.getLineCount() - 1, n4 + n3));
            if (this.select) {
                jEditTextArea.select(jEditTextArea.getMarkPosition(), n5);
            } else {
                jEditTextArea.setCaretPosition(n5);
            }
        }
    }

    public static class next_line
    implements ActionListener {
        private boolean select;

        public next_line(boolean bl) {
            this.select = bl;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            int n = jEditTextArea.getCaretPosition();
            int n2 = jEditTextArea.getCaretLine();
            if (n2 == jEditTextArea.getLineCount() - 1) {
                jEditTextArea.getToolkit().beep();
                return;
            }
            int n3 = jEditTextArea.getMagicCaretPosition();
            if (n3 == -1) {
                n3 = jEditTextArea.offsetToX(n2, n - jEditTextArea.getLineStartOffset(n2));
            }
            n = jEditTextArea.getLineStartOffset(n2 + 1) + jEditTextArea.xToOffset(n2 + 1, n3 + 1);
            if (this.select) {
                jEditTextArea.select(jEditTextArea.getMarkPosition(), n);
            } else {
                jEditTextArea.setCaretPosition(n);
            }
            jEditTextArea.setMagicCaretPosition(n3);
        }
    }

    public static class next_char
    implements ActionListener {
        private boolean select;

        public next_char(boolean bl) {
            this.select = bl;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            int n = jEditTextArea.getCaretPosition();
            if (n == jEditTextArea.getDocumentLength()) {
                jEditTextArea.getToolkit().beep();
                return;
            }
            if (this.select) {
                jEditTextArea.select(jEditTextArea.getMarkPosition(), n + 1);
            } else {
                jEditTextArea.setCaretPosition(n + 1);
            }
        }
    }

    public static class insert_tab
    implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            if (!jEditTextArea.isEditable()) {
                jEditTextArea.getToolkit().beep();
                return;
            }
            jEditTextArea.overwriteSetSelectedText("\t");
        }
    }

    public static class insert_break
    implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            if (!jEditTextArea.isEditable()) {
                jEditTextArea.getToolkit().beep();
                return;
            }
            jEditTextArea.setSelectedText("\n");
        }
    }

    public static class document_home
    implements ActionListener {
        private boolean select;

        public document_home(boolean bl) {
            this.select = bl;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            if (this.select) {
                jEditTextArea.select(jEditTextArea.getMarkPosition(), 0);
            } else {
                jEditTextArea.setCaretPosition(0);
            }
        }
    }

    public static class home
    implements ActionListener {
        private boolean select;

        public home(boolean bl) {
            this.select = bl;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            int n = jEditTextArea.getCaretPosition();
            int n2 = jEditTextArea.getFirstLine();
            int n3 = jEditTextArea.getLineStartOffset(jEditTextArea.getCaretLine());
            int n4 = n2 == 0 ? 0 : n2 + jEditTextArea.getElectricScroll();
            int n5 = jEditTextArea.getLineStartOffset(n4);
            if (n == 0) {
                jEditTextArea.getToolkit().beep();
                return;
            }
            if (!Boolean.TRUE.equals(jEditTextArea.getClientProperty("InputHandler.homeEnd"))) {
                int n6 = Utilities.getLeadingWhiteSpace(jEditTextArea.getLineText(jEditTextArea.getCaretLine()));
                n = n == (n6+=n3) ? n3 : n6;
            } else if (n == n5) {
                n = 0;
            } else if (n == n3) {
                n = n5;
            } else {
                int n7 = Utilities.getLeadingWhiteSpace(jEditTextArea.getLineText(jEditTextArea.getCaretLine()));
                n = n == (n7+=n3) ? n3 : n7;
            }
            if (this.select) {
                jEditTextArea.select(jEditTextArea.getMarkPosition(), n);
            } else {
                jEditTextArea.setCaretPosition(n);
            }
        }
    }

    public static class document_end
    implements ActionListener {
        private boolean select;

        public document_end(boolean bl) {
            this.select = bl;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            if (this.select) {
                jEditTextArea.select(jEditTextArea.getMarkPosition(), jEditTextArea.getDocumentLength());
            } else {
                jEditTextArea.setCaretPosition(jEditTextArea.getDocumentLength());
            }
        }
    }

    public static class end
    implements ActionListener {
        private boolean select;

        public end(boolean bl) {
            this.select = bl;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            int n = jEditTextArea.getCaretPosition();
            int n2 = jEditTextArea.getLineEndOffset(jEditTextArea.getCaretLine()) - 1;
            int n3 = jEditTextArea.getFirstLine() + jEditTextArea.getVisibleLines();
            n3 = n3 >= jEditTextArea.getLineCount() ? Math.min(jEditTextArea.getLineCount() - 1, n3) : (n3-=jEditTextArea.getElectricScroll() + 1);
            int n4 = jEditTextArea.getLineEndOffset(n3) - 1;
            int n5 = jEditTextArea.getDocumentLength();
            if (n == n5) {
                jEditTextArea.getToolkit().beep();
                return;
            }
            n = !Boolean.TRUE.equals(jEditTextArea.getClientProperty("InputHandler.homeEnd")) ? n2 : (n == n4 ? n5 : (n == n2 ? n4 : n2));
            if (this.select) {
                jEditTextArea.select(jEditTextArea.getMarkPosition(), n);
            } else {
                jEditTextArea.setCaretPosition(n);
            }
        }
    }

    public static class delete_word
    implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            String string;
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            int n = jEditTextArea.getSelectionStart();
            if (n != jEditTextArea.getSelectionEnd()) {
                jEditTextArea.setSelectedText("");
                return;
            }
            int n2 = jEditTextArea.getCaretLine();
            int n3 = jEditTextArea.getLineStartOffset(n2);
            int n4 = n - n3;
            if (n4 == (string = jEditTextArea.getLineText(jEditTextArea.getCaretLine())).length()) {
                if (n3 + n4 == jEditTextArea.getDocumentLength()) {
                    jEditTextArea.getToolkit().beep();
                    return;
                }
                ++n4;
            } else {
                String string2 = ((JextTextArea)jEditTextArea).getProperty("noWordSep");
                n4 = TextUtilities.findWordEnd(string, n4 + 1, string2);
            }
            try {
                jEditTextArea.getDocument().remove(n, n4 + n3 - n);
            }
            catch (BadLocationException var8_9) {
                var8_9.printStackTrace();
            }
        }
    }

    public static class delete
    implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            if (!jEditTextArea.isEditable()) {
                jEditTextArea.getToolkit().beep();
                return;
            }
            if (jEditTextArea.getSelectionStart() != jEditTextArea.getSelectionEnd()) {
                jEditTextArea.setSelectedText("");
            } else {
                int n = jEditTextArea.getCaretPosition();
                if (n == jEditTextArea.getDocumentLength()) {
                    jEditTextArea.getToolkit().beep();
                    return;
                }
                try {
                    jEditTextArea.getDocument().remove(n, 1);
                }
                catch (BadLocationException var4_4) {
                    var4_4.printStackTrace();
                }
            }
        }
    }

    public static class backspace_word
    implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            int n = jEditTextArea.getSelectionStart();
            if (n != jEditTextArea.getSelectionEnd()) {
                jEditTextArea.setSelectedText("");
                return;
            }
            int n2 = jEditTextArea.getCaretLine();
            int n3 = jEditTextArea.getLineStartOffset(n2);
            int n4 = n - n3;
            String string = jEditTextArea.getLineText(jEditTextArea.getCaretLine());
            if (n4 == 0) {
                if (n3 == 0) {
                    jEditTextArea.getToolkit().beep();
                    return;
                }
                --n4;
            } else {
                String string2 = ((JextTextArea)jEditTextArea).getProperty("noWordSep");
                n4 = TextUtilities.findWordStart(string, n4 - 1, string2);
            }
            try {
                jEditTextArea.getDocument().remove(n4 + n3, n - (n4 + n3));
            }
            catch (BadLocationException var8_9) {
                var8_9.printStackTrace();
            }
        }
    }

    public static class backspace
    implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            JEditTextArea jEditTextArea = InputHandler.getTextArea(actionEvent);
            if (!jEditTextArea.isEditable()) {
                jEditTextArea.getToolkit().beep();
                return;
            }
            if (jEditTextArea.getSelectionStart() != jEditTextArea.getSelectionEnd()) {
                jEditTextArea.setSelectedText("");
            } else {
                int n = jEditTextArea.getCaretPosition();
                if (n == 0) {
                    jEditTextArea.getToolkit().beep();
                    return;
                }
                try {
                    jEditTextArea.getDocument().remove(n - 1, 1);
                }
                catch (BadLocationException var4_4) {
                    var4_4.printStackTrace();
                }
            }
        }
    }

    public static interface MacroRecorder {
        public void actionPerformed(ActionListener var1, String var2);
    }

    public static interface Wrapper {
    }

    public static interface NonRecordable {
    }

    public static interface NonRepeatable {
    }

}

