/*
 * Decompiled with CFR 0_102.
 */
package org.jext.actions;

import java.awt.event.ActionEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.jext.EditAction;
import org.jext.JextTextArea;
import org.jext.MenuAction;

public class TabsToSpaces
extends MenuAction
implements EditAction {
    public TabsToSpaces() {
        super("tabs_to_spaces");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = TabsToSpaces.getTextArea(actionEvent);
        jextTextArea.beginCompoundEdit();
        SyntaxDocument syntaxDocument = jextTextArea.getDocument();
        try {
            Element element = syntaxDocument.getDefaultRootElement();
            int n = element.getElementCount();
            for (int i = 0; i < n; ++i) {
                Element element2 = element.getElement(i);
                int n2 = element2.getStartOffset();
                int n3 = element2.getEndOffset() - 1;
                int n4 = jextTextArea.getTabSize();
                String string = this.doTabsToSpaces(jextTextArea.getText(n2, n3-=n2), n4);
                syntaxDocument.remove(n2, n3);
                syntaxDocument.insertString(n2, string, null);
            }
        }
        catch (BadLocationException var4_5) {
            // empty catch block
        }
        jextTextArea.endCompoundEdit();
    }

    private String doTabsToSpaces(String string, int n) {
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = 0;
        block4 : for (int i = 0; i < string.length(); ++i) {
            switch (string.charAt(i)) {
                case '\t': {
                    int n3 = n - n2 % n;
                    n2+=n3;
                    while (--n3 >= 0) {
                        stringBuffer.append(' ');
                    }
                    continue block4;
                }
                case '\n': {
                    n2 = 0;
                    stringBuffer.append(string.charAt(i));
                    continue block4;
                }
                default: {
                    ++n2;
                    stringBuffer.append(string.charAt(i));
                }
            }
        }
        return stringBuffer.toString();
    }
}

