/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  gnu.regexp.RE
 *  gnu.regexp.REException
 *  gnu.regexp.RESyntax
 */
package org.jext.misc;

import gnu.regexp.RE;
import gnu.regexp.REException;
import gnu.regexp.RESyntax;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.gjt.sp.jedit.textarea.TextUtilities;
import org.jext.JextTextArea;
import org.jext.Utilities;

public class Indent {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean indent(JextTextArea jextTextArea, int n, boolean bl, boolean bl2) {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        if (n == 0) {
            return false;
        }
        String string = jextTextArea.getProperty("indentOpenBrackets");
        String string2 = jextTextArea.getProperty("indentCloseBrackets");
        String string3 = jextTextArea.getProperty("indentPrevLine");
        RE rE = null;
        SyntaxDocument syntaxDocument = jextTextArea.getDocument();
        if (string == null) {
            string = "";
        }
        if (string2 == null) {
            string2 = "";
        }
        if (string3 != null) {
            try {
                rE = new RE((Object)string3, 2, new RESyntax(RESyntax.RE_SYNTAX_PERL5).set(2).setLineSeparator("\n"));
            }
            catch (REException var9_9) {
                // empty catch block
            }
        }
        int n10 = n3 = jextTextArea.getTabSize();
        boolean bl3 = JextTextArea.getSoftTab();
        Element element = syntaxDocument.getDefaultRootElement();
        String string4 = null;
        String string5 = null;
        Element element2 = element.getElement(n);
        int n11 = element2.getStartOffset();
        try {
            string5 = syntaxDocument.getText(n11, element2.getEndOffset() - n11 - 1);
            for (n6 = n - 1; n6 >= 0; --n6) {
                element2 = element.getElement(n6);
                n5 = element2.getStartOffset();
                n8 = element2.getEndOffset() - n5 - 1;
                if (n8 == 0) continue;
                string4 = syntaxDocument.getText(n5, n8);
                break;
            }
            if (string4 == null) {
                return false;
            }
        }
        catch (BadLocationException var17_19) {
            return false;
        }
        n6 = rE == null ? 0 : (int)rE.isMatch((Object)string4) ? 1 : 0;
        n5 = 1;
        n8 = 0;
        int n12 = 0;
        block15 : for (n2 = 0; n2 < string4.length(); ++n2) {
            n9 = string4.charAt(n2);
            switch (n9) {
                case 32: {
                    if (n5 == 0) continue block15;
                    ++n8;
                    continue block15;
                }
                case 9: {
                    if (n5 == 0) continue block15;
                    n8+=n3 - n8 % n3;
                    continue block15;
                }
                default: {
                    n5 = 0;
                    if (string2.indexOf(n9) != -1) {
                        n12 = Math.max(n12 - 1, 0);
                        continue block15;
                    }
                    if (string.indexOf(n9) == -1) continue block15;
                    n6 = 0;
                    ++n12;
                }
            }
        }
        n2 = 1;
        n9 = 0;
        int n13 = 0;
        int n14 = 0;
        int n15 = -1;
        block16 : for (n4 = 0; n4 < string5.length(); ++n4) {
            n7 = string5.charAt(n4);
            switch (n7) {
                case 32: {
                    if (n2 == 0) continue block16;
                    ++n9;
                    ++n13;
                    continue block16;
                }
                case 9: {
                    if (n2 == 0) continue block16;
                    n9+=n3 - n9 % n3;
                    ++n13;
                    continue block16;
                }
                default: {
                    n2 = 0;
                    if (string2.indexOf(n7) != -1) {
                        if (n14 == 0) {
                            n15 = n4;
                            continue block16;
                        }
                        --n14;
                        continue block16;
                    }
                    if (string.indexOf(n7) == -1) continue block16;
                    n6 = 0;
                    ++n14;
                }
            }
        }
        try {
            if (n15 != -1) {
                n4 = TextUtilities.findMatchingBracket(syntaxDocument, element.getElement(n).getStartOffset() + n15);
                if (n4 == -1) return false;
                element2 = element.getElement(element.getElementIndex(n4));
                n7 = element2.getStartOffset();
                String string6 = syntaxDocument.getText(n7, element2.getEndOffset() - n7 - 1);
                n8 = Utilities.getLeadingWhiteSpaceWidth(string6, n3);
            } else {
                n8+=n12 * n10;
            }
            if (n6 != 0) {
                n8+=n10;
            }
            if (!(bl2 || n8 > n9)) {
                return false;
            }
            if (!(bl || n8 < n9)) {
                return false;
            }
            syntaxDocument.remove(n11, n13);
            syntaxDocument.insertString(n11, Utilities.createWhiteSpace(n8, bl3 ? 0 : n3), null);
            return true;
        }
        catch (BadLocationException var26_29) {
            return false;
        }
    }
}

