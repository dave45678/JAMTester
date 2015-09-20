/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;
import javax.swing.undo.UndoableEdit;
import org.gjt.sp.jedit.syntax.Token;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class SyntaxDocument
extends PlainDocument {
    protected TokenMarker tokenMarker;

    public TokenMarker getTokenMarker() {
        return this.tokenMarker;
    }

    public void setTokenMarker(TokenMarker tokenMarker) {
        this.tokenMarker = tokenMarker;
        if (tokenMarker == null) {
            return;
        }
        this.tokenMarker.insertLines(0, this.getDefaultRootElement().getElementCount());
        this.tokenizeLines();
    }

    public void tokenizeLines() {
        this.tokenizeLines(0, this.getDefaultRootElement().getElementCount());
    }

    public void tokenizeLines(int n, int n2) {
        if (!(this.tokenMarker != null && this.tokenMarker.supportsMultilineTokens())) {
            return;
        }
        Segment segment = new Segment();
        Element element = this.getDefaultRootElement();
        n2+=n;
        try {
            for (int i = n; i < n2; ++i) {
                Element element2 = element.getElement(i);
                int n3 = element2.getStartOffset();
                this.getText(n3, element2.getEndOffset() - n3 - 1, segment);
                this.tokenMarker.markTokens(segment, i);
            }
        }
        catch (BadLocationException var5_6) {
            var5_6.printStackTrace();
        }
    }

    public void beginCompoundEdit() {
    }

    public void endCompoundEdit() {
    }

    public void addUndoableEdit(UndoableEdit undoableEdit) {
    }

    protected void fireInsertUpdate(DocumentEvent documentEvent) {
        DocumentEvent.ElementChange elementChange;
        if (this.tokenMarker != null && (elementChange = documentEvent.getChange(this.getDefaultRootElement())) != null) {
            this.tokenMarker.insertLines(elementChange.getIndex() + 1, elementChange.getChildrenAdded().length - elementChange.getChildrenRemoved().length);
        }
        super.fireInsertUpdate(documentEvent);
    }

    protected void fireRemoveUpdate(DocumentEvent documentEvent) {
        DocumentEvent.ElementChange elementChange;
        if (this.tokenMarker != null && (elementChange = documentEvent.getChange(this.getDefaultRootElement())) != null) {
            this.tokenMarker.deleteLines(elementChange.getIndex() + 1, elementChange.getChildrenRemoved().length - elementChange.getChildrenAdded().length);
        }
        super.fireRemoveUpdate(documentEvent);
    }
}

