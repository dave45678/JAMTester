/*
 * Decompiled with CFR 0_102.
 */
package org.jext.textarea;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.text.BadLocationException;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.gjt.sp.jedit.textarea.TextUtilities;
import org.jext.JextTextArea;
import org.jext.MenuAction;

public class CsWord
extends MenuAction {
    public static final String[] DIRECTIONS = new String[]{"bkd", "fwd"};
    public static final String[] ACTIONS = new String[]{"nil", "sel", "del"};
    public static final int NO_ACTION = 0;
    public static final int SELECT = 1;
    public static final int DELETE = 2;
    private int action;
    private int direction;

    public CsWord(int n, int n2) {
        super("CsWord__" + ACTIONS[n] + "_" + DIRECTIONS[n2 > 0 ? 1 : 0]);
        this.action = n;
        this.direction = n2;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea;
        int n;
        int n2;
        int n3;
        int n4;
        char c;
        block17 : {
            jextTextArea = CsWord.getTextArea(actionEvent);
            n2 = jextTextArea.getSelectionStart();
            if (this.action == 2 && n2 != jextTextArea.getSelectionEnd()) {
                jextTextArea.setSelectedText("");
                return;
            }
            n = jextTextArea.getCaretPosition();
            int n5 = jextTextArea.getCaretLine();
            n3 = jextTextArea.getLineStartOffset(n5);
            n-=n3;
            String string = jextTextArea.getLineText(jextTextArea.getCaretLine());
            n+=this.direction;
            try {
                char c2;
                n4 = n;
                c = string.charAt(n);
                if (!(this.direction != 1 || Character.isLetterOrDigit(c2 = string.charAt(n - this.direction)))) {
                    n-=this.direction;
                    c = c2;
                }
                if (n4 == (n = TextUtilities.findTypeChange(string, n, this.direction))) break block17;
                c2 = string.charAt(n);
                if ((!Character.isLetterOrDigit(c) || !Character.isLetterOrDigit(c2) || Character.isUpperCase(c) && Character.isLowerCase(c2)) && this.direction == -1) {
                    n-=this.direction;
                }
                if (Character.isLetterOrDigit(c) && Character.isLetterOrDigit(string.charAt(n)) && n + 1 == string.length() && this.direction == 1) {
                    n+=this.direction;
                }
                if (!Character.isWhitespace(c) || !Character.isWhitespace(c2)) break block17;
                try {
                    while (Character.isWhitespace(string.charAt(n))) {
                        n+=this.direction;
                    }
                }
                catch (IndexOutOfBoundsException var11_14) {
                    n-=this.direction;
                }
            }
            catch (IndexOutOfBoundsException var8_9) {
                try {
                    jextTextArea.getText().charAt(n3 + n);
                }
                catch (IndexOutOfBoundsException var9_12) {
                    jextTextArea.getToolkit().beep();
                    return;
                }
            }
        }
        if (this.action == 1) {
            jextTextArea.select(jextTextArea.getMarkPosition(), n3 + n);
        } else if (this.action == 2) {
            try {
                n4 = n + n3;
                c = Math.abs(n2 - n4);
                jextTextArea.getDocument().remove(this.direction == 1 ? n2 : n4, c);
            }
            catch (BadLocationException var8_10) {
                var8_10.printStackTrace();
            }
        } else {
            jextTextArea.setCaretPosition(n3 + n);
        }
    }
}

