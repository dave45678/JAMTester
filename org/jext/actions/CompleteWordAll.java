/*
 * Decompiled with CFR 0_102.
 */
package org.jext.actions;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.TreeSet;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.gjt.sp.jedit.textarea.TextUtilities;
import org.jext.EditAction;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.MenuAction;
import org.jext.misc.CompleteWordList;

public class CompleteWordAll
extends MenuAction
implements EditAction {
    public CompleteWordAll() {
        super("complete_word_all");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        int n;
        JextFrame jextFrame = CompleteWordAll.getJextParent(actionEvent);
        JextTextArea jextTextArea = jextFrame.getTextArea();
        TreeSet<String> treeSet = new TreeSet<String>();
        JextTextArea[] arrjextTextArea = jextFrame.getTextAreas();
        SyntaxDocument syntaxDocument = jextTextArea.getDocument();
        String string = jextTextArea.getProperty("noWordSep");
        if (string == null) {
            string = "";
        }
        String string2 = jextTextArea.getLineText(jextTextArea.getCaretLine());
        int n2 = jextTextArea.getCaretPosition() - jextTextArea.getLineStartOffset(jextTextArea.getCaretLine());
        if (n2 == 0) {
            return;
        }
        int n3 = TextUtilities.findWordStart(string2, n2 - 1, string);
        String string3 = string2.substring(n3, n2);
        if (string3.length() == 0) {
            return;
        }
        int n4 = string3.length();
        jextFrame.showWaitCursor();
        for (n = 0; n < arrjextTextArea.length; ++n) {
            JextTextArea jextTextArea2 = arrjextTextArea[n];
            for (int i = 0; i < jextTextArea2.getLineCount(); ++i) {
                String string4;
                string2 = jextTextArea2.getLineText(i);
                if (string2.startsWith(string3) && (string4 = this.getWord(string2, 0, string)).length() != n4) {
                    treeSet.add(string4);
                }
                int n5 = string2.length() - string3.length();
                for (int j = 0; j < n5; ++j) {
                    String string5;
                    char c = string2.charAt(j);
                    if (Character.isLetterOrDigit(c) || string.indexOf(c) != -1 || !string2.regionMatches(j + 1, string3, 0, n4) || (string5 = this.getWord(string2, j + 1, string)).length() == n4) continue;
                    treeSet.add(string5);
                }
            }
        }
        if (treeSet.size() > 1) {
            n = String.valueOf(treeSet.first()).length();
            Iterator iterator = treeSet.iterator();
            while (iterator.hasNext()) {
                n = Math.min(n, this.getDivergentIndex(String.valueOf(treeSet.first()), String.valueOf(iterator.next())));
            }
            jextFrame.hideWaitCursor();
            if (n > n4) {
                jextTextArea.setSelectedText(String.valueOf(treeSet.first()).substring(n4, n));
            } else {
                new CompleteWordList(jextFrame, string3, treeSet.toArray(new String[treeSet.size()]));
            }
        } else {
            jextFrame.hideWaitCursor();
            if (treeSet.size() == 1) {
                jextTextArea.setSelectedText(String.valueOf(treeSet.first()).substring(n4));
            }
        }
    }

    private int getDivergentIndex(String string, String string2) {
        int n = string.length();
        if (!string.equals(string2)) {
            for (n = 0; n < string.length() && n < string2.length() && string.charAt(n) == string2.charAt(n); ++n) {
            }
        }
        return n;
    }

    private String getWord(String string, int n, String string2) {
        int n2 = TextUtilities.findWordEnd(string, n + 1, string2);
        return string.substring(n, n2);
    }
}

