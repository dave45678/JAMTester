/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import java.util.Arrays;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

public class StringSorter {
    public static void sort(Document document, boolean bl) {
        StringSorter.sort(document, 0, document.getLength(), bl);
    }

    public static void sort(Document document, int n, int n2, boolean bl) {
        if (document == null) {
            return;
        }
        Element element = document.getDefaultRootElement();
        int n3 = element.getElementIndex(n);
        int n4 = element.getElementIndex(n + n2);
        Object[] arrobject = new String[n4 - n3 + 1];
        try {
            int n5;
            for (int i = 0; i < arrobject.length; ++i) {
                Element element2 = element.getElement(n3 + i);
                arrobject[i] = document.getText(element2.getStartOffset(), element2.getEndOffset() - element2.getStartOffset());
                if (!arrobject[i].endsWith("\n")) continue;
                arrobject[i] = arrobject[i].substring(0, arrobject[i].length() - 1);
            }
            Arrays.sort(arrobject);
            StringBuffer stringBuffer = new StringBuffer();
            if (bl) {
                for (n5 = arrobject.length - 1; n5 > 0; --n5) {
                    stringBuffer.append(arrobject[n5].concat("\n"));
                }
                stringBuffer.append((String)arrobject[0]);
            } else {
                for (n5 = 0; n5 < arrobject.length - 1; ++n5) {
                    stringBuffer.append(arrobject[n5].concat("\n"));
                }
                stringBuffer.append((String)arrobject[arrobject.length - 1]);
            }
            n5 = element.getElement(n3).getStartOffset();
            int n6 = element.getElement(n4).getEndOffset() - n5 - 1;
            document.remove(n5, n6);
            document.insertString(n5, stringBuffer.toString(), null);
        }
        catch (BadLocationException var9_10) {
            // empty catch block
        }
    }
}

