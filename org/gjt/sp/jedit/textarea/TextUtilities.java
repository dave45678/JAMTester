/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.textarea;

import java.util.ArrayList;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.gjt.sp.jedit.syntax.Token;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class TextUtilities {
    public static final String BRACKETS = "([{}])";
    public static final int FORWARD = 1;
    public static final int BACKWARD = -1;

    private static ArrayList getTokenList(Token token, int n) {
        ArrayList<Token> arrayList = new ArrayList<Token>();
        while (token != null) {
            if (token.id == 127) {
                token = null;
                continue;
            }
            arrayList.add(Math.max(0, arrayList.size() * n), token);
            token = token.next;
        }
        return arrayList;
    }

    public static int findMatchingBracket(SyntaxDocument syntaxDocument, int n) throws BadLocationException {
        char c;
        if (syntaxDocument.getLength() == 0) {
            return -1;
        }
        Element element = syntaxDocument.getDefaultRootElement();
        Element element2 = syntaxDocument.getParagraphElement(n);
        Segment segment = new Segment();
        int n2 = element2.getStartOffset();
        int n3 = element2.getEndOffset() - n2 - 1;
        int n4 = element.getElementIndex(n2);
        syntaxDocument.getText(n2, n3, segment);
        n-=n2;
        try {
            c = segment.array[segment.offset + n];
        }
        catch (ArrayIndexOutOfBoundsException var9_9) {
            c = '\u0000';
        }
        int n5 = "([{}])".indexOf(c);
        if (n5 == -1) {
            return n5;
        }
        char c2 = "([{}])".charAt("([{}])".length() - 1 - n5);
        int n6 = n5 < "([{}])".length() / 2 ? 1 : -1;
        TokenMarker tokenMarker = syntaxDocument.getTokenMarker();
        if (tokenMarker == null) {
            return -1;
        }
        ArrayList arrayList = TextUtilities.getTokenList(tokenMarker.markTokens(segment, n4), n6);
        byte by = 10;
        int n7 = 0;
        int n8 = n6 == 1 ? 0 : arrayList.size() - 1;
        boolean bl = false;
        do {
            Token token = null;
            try {
                token = (Token)arrayList.get(n8);
            }
            catch (IndexOutOfBoundsException var19_21) {
                return -1;
            }
            if ((n7+=token.length) > n) {
                by = token.id;
                if (n6 == 1) {
                    n7-=token.length;
                }
                bl = true;
                continue;
            }
            n8+=n6;
        } while (!bl);
        if (by == 10) {
            return -1;
        }
        int n9 = 0;
        int n10 = n6 == 1 ? element.getElementCount() - n4 : n4 + 1;
        for (int i = 0; i < n10; ++i) {
            int n11;
            int n12 = n4 + i * n6;
            element2 = element.getElement(n12);
            n2 = element2.getStartOffset();
            n3 = element2.getEndOffset() - n2 - 1;
            syntaxDocument.getText(n2, n3, segment);
            if (n12 != n4) {
                arrayList = TextUtilities.getTokenList(tokenMarker.markTokens(segment, n4), n6);
                n8 = 0;
                if (n6 == 1) {
                    n7 = 0;
                    n11 = 0;
                } else {
                    n7 = n3;
                    n11 = n7 - 1;
                }
            } else {
                n11 = n;
            }
            while (n8 < arrayList.size()) {
                Token token = (Token)arrayList.get(n8);
                byte by2 = token.id;
                int n13 = token.length;
                if (by2 == by) {
                    int n14;
                    char[] arrc = new char[n13];
                    int n15 = n7 + (n6 == 1 ? 0 : n6 * arrc.length);
                    for (n14 = 0; n14 < arrc.length; ++n14) {
                        arrc[n14] = segment.array[segment.offset + n15 + n14];
                    }
                    n14 = n6 == 1 ? 0 : arrc.length - 1;
                    int n16 = n11 - n15 - n6;
                    do {
                        char c3;
                        if ((c3 = arrc[n16+=n6]) == c) {
                            ++n9;
                            continue;
                        }
                        if (c3 != c2 || --n9 != 0) continue;
                        return n2 + n15 + n16;
                    } while (n16 + n14 + 1 != arrc.length);
                }
                n11 = n7+=n13 * n6;
                if (n6 == -1) {
                    --n11;
                }
                ++n8;
            }
        }
        return -1;
    }

    public static int findWordStart(String string, int n, String string2) {
        char c = string.charAt(n);
        if (string2 == null) {
            string2 = "";
        }
        boolean bl = !Character.isLetterOrDigit(c) && string2.indexOf(c) == -1;
        int n2 = 0;
        for (int i = n; i >= 0; --i) {
            c = string.charAt(i);
            if (!(bl ^ (!Character.isLetterOrDigit(c) && string2.indexOf(c) == -1))) continue;
            n2 = i + 1;
            break;
        }
        return n2;
    }

    public static int findWordEnd(String string, int n, String string2) {
        if (n != 0) {
            --n;
        }
        char c = string.charAt(n);
        if (string2 == null) {
            string2 = "";
        }
        boolean bl = !Character.isLetterOrDigit(c) && string2.indexOf(c) == -1;
        int n2 = string.length();
        for (int i = n; i < string.length(); ++i) {
            c = string.charAt(i);
            if (!(bl ^ (!Character.isLetterOrDigit(c) && string2.indexOf(c) == -1))) continue;
            n2 = i;
            break;
        }
        return n2;
    }

    public static int findTypeChange(String string, int n, int n2) {
        int n3 = Character.getType(string.charAt(n));
        int n4 = n + n2;
        do {
            try {
                if (Character.getType(string.charAt(n4)) != n3) {
                    return n4;
                }
            }
            catch (IndexOutOfBoundsException var5_5) {
                return n4 - n2;
            }
            n4+=n2;
        } while (true);
    }
}

