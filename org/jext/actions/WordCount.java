/*
 * Decompiled with CFR 0_102.
 */
package org.jext.actions;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.text.Element;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.jext.Jext;
import org.jext.JextTextArea;
import org.jext.MenuAction;

public class WordCount
extends MenuAction {
    private boolean wing;
    private String[] comments = new String[3];
    private int characters;
    private int lines;
    private int words;
    private int codeLines;

    public WordCount() {
        super("word_count");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = WordCount.getTextArea(actionEvent);
        jextTextArea.beginCompoundEdit();
        this.wing = false;
        this.codeLines = 0;
        this.words = 0;
        this.lines = 0;
        this.characters = 0;
        this.comments[0] = jextTextArea.getProperty("blockComment");
        this.comments[1] = jextTextArea.getProperty("commentStart");
        this.comments[2] = jextTextArea.getProperty("commentEnd");
        Element element = jextTextArea.getDocument().getDefaultRootElement();
        int n = element.getElementCount();
        for (int i = 0; i < n; ++i) {
            Element element2 = element.getElement(i);
            int n2 = element2.getStartOffset();
            int n3 = element2.getEndOffset() - 1;
            this.doWordCount(jextTextArea.getText(n2, n3-=n2));
        }
        jextTextArea.endCompoundEdit();
        Object[] arrobject = new Object[4];
        arrobject[0] = Jext.getProperty("wordcount.characters") + String.valueOf(this.characters);
        arrobject[1] = Jext.getProperty("wordcount.words") + String.valueOf(this.words);
        arrobject[2] = Jext.getProperty("wordcount.lines") + String.valueOf(this.lines);
        arrobject[3] = Jext.getProperty("wordcount.codeLines") + (this.codeLines == -1 ? "n/a" : String.valueOf(this.codeLines));
        Object[] arrobject2 = arrobject;
        JOptionPane.showMessageDialog(WordCount.getJextParent(actionEvent), arrobject2, Jext.getProperty("wordcount.title"), 1);
    }

    private void doWordCount(String string) {
        ++this.lines;
        boolean bl = false;
        this.characters+=string.length();
        if (this.comments[0] != null && this.comments[1] != null && this.comments[2] != null) {
            String string2 = string.trim();
            if (this.wing) {
                this.wing = !string2.endsWith(this.comments[2]);
            } else if (!string2.startsWith(this.comments[1])) {
                if (!(string2.startsWith(this.comments[0]) || string2.equals(""))) {
                    ++this.codeLines;
                }
            } else {
                this.wing = !string2.endsWith(this.comments[2]);
            }
        } else {
            this.codeLines = -1;
        }
        block3 : for (int i = 0; i < string.length(); ++i) {
            switch (string.charAt(i)) {
                case '\t': 
                case ' ': {
                    if (!bl) continue block3;
                    ++this.words;
                    bl = false;
                    continue block3;
                }
                default: {
                    bl = true;
                }
            }
        }
    }
}

