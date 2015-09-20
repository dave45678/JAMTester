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

public class SpacesToTabs
extends MenuAction
implements EditAction {
    public SpacesToTabs() {
        super("spaces_to_tabs");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = SpacesToTabs.getTextArea(actionEvent);
        jextTextArea.beginCompoundEdit();
        SyntaxDocument syntaxDocument = jextTextArea.getDocument();
        try {
            Element element = syntaxDocument.getDefaultRootElement();
            int n = element.getElementCount();
            for (int i = 0; i < n; ++i) {
                Element element2 = element.getElement(i);
                int n2 = element2.getStartOffset();
                int n3 = element2.getEndOffset() - 1;
                String string = this.doSpacesToTabs(jextTextArea.getText(n2, n3-=n2), jextTextArea.getTabSize());
                syntaxDocument.remove(n2, n3);
                syntaxDocument.insertString(n2, string, null);
            }
        }
        catch (BadLocationException var4_5) {
            // empty catch block
        }
        jextTextArea.endCompoundEdit();
    }

    private String doSpacesToTabs(String string, int n) {
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = 0;
        int n3 = 0;
        block5 : for (int i = 0; i < string.length(); ++i) {
            switch (string.charAt(i)) {
                case ' ': {
                    ++n3;
                    ++n2;
                    continue block5;
                }
                case '\t': {
                    int n4 = n - n2 % n;
                    n2+=n4;
                    n3+=n4;
                    continue block5;
                }
                case '\n': {
                    n3 = 0;
                    n2 = 0;
                    stringBuffer.append('\n');
                    continue block5;
                }
                default: {
                    if (n3 != 0) {
                        if (n3 >= n / 2 && n3 > 1) {
                            int n5 = n3 + (n2 - n3) % n;
                            int n6 = n5 / n;
                            int n7 = n5 % n;
                            while (n6-- > 0) {
                                stringBuffer.append('\t');
                            }
                            while (n7-- > 0) {
                                stringBuffer.append(' ');
                            }
                        } else {
                            while (n3-- > 0) {
                                stringBuffer.append(' ');
                            }
                        }
                        n3 = 0;
                    }
                    stringBuffer.append(string.charAt(i));
                    ++n2;
                }
            }
        }
        return stringBuffer.toString();
    }
}

