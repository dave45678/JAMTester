/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.textarea;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventListener;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.gjt.sp.jedit.gui.KeyEventWorkaround;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.gjt.sp.jedit.syntax.SyntaxStyle;
import org.gjt.sp.jedit.syntax.Token;
import org.gjt.sp.jedit.syntax.TokenMarker;
import org.gjt.sp.jedit.textarea.Gutter;
import org.gjt.sp.jedit.textarea.InputHandler;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.textarea.TextAreaDefaults;
import org.gjt.sp.jedit.textarea.TextAreaPainter;
import org.gjt.sp.jedit.textarea.TextUtilities;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.OneClickAction;

public class JEditTextArea
extends JComponent {
    private JextFrame view;
    public static String LEFT_OF_SCROLLBAR = "los";
    Segment lineSegment;
    protected static String CENTER = "center";
    protected static String RIGHT = "right";
    protected static String LEFT = "left";
    protected static String BOTTOM = "bottom";
    protected static JEditTextArea focusedComponent;
    protected static Timer caretTimer;
    protected TextAreaPainter painter;
    protected Gutter gutter;
    protected JPopupMenu popup;
    protected EventListenerList listenerList;
    protected MutableCaretEvent caretEvent;
    protected boolean caretBlinks;
    protected boolean caretVisible;
    protected boolean blink;
    protected boolean editable;
    protected int maxHorizontalScrollWidth;
    protected int firstLine;
    protected int visibleLines;
    protected int electricScroll;
    protected int horizontalOffset;
    protected JScrollBar vertical;
    protected JScrollBar horizontal;
    protected boolean scrollBarsInitialized;
    protected InputHandler inputHandler;
    protected SyntaxDocument document;
    protected DocumentHandler documentHandler;
    protected boolean documentHandlerInstalled;
    protected int selectionStart;
    protected int selectionStartLine;
    protected int selectionEnd;
    protected int selectionEndLine;
    protected boolean biasLeft;
    protected int bracketPosition;
    protected int bracketLine;
    protected int magicCaret;
    protected boolean overwrite;
    protected boolean rectSelect;
    private int clickCount;
    protected ActionEvent oneClickEvent;
    public OneClickAction oneClick;
    private boolean dragText = false;
    private boolean dragCursor = false;
    private boolean dragControlDown = false;
    private int shadowCaretLine = -1;
    private int shadowCaretOffset = -1;

    public JEditTextArea(JextFrame jextFrame) {
        this(jextFrame, TextAreaDefaults.getDefaults());
    }

    public JEditTextArea(JextFrame jextFrame, TextAreaDefaults textAreaDefaults) {
        this.view = jextFrame;
        this.enableEvents(8);
        this.painter = new TextAreaPainter(this, textAreaDefaults);
        this.gutter = new Gutter(this, textAreaDefaults);
        this.documentHandler = new DocumentHandler();
        this.listenerList = new EventListenerList();
        this.caretEvent = new MutableCaretEvent();
        this.bracketPosition = -1;
        this.bracketLine = -1;
        this.blink = true;
        this.lineSegment = new Segment();
        this.setLayout(new ScrollLayout());
        this.add(LEFT, this.gutter);
        this.add(CENTER, this.painter);
        this.vertical = new JScrollBar(1);
        this.add(RIGHT, this.vertical);
        this.horizontal = new JScrollBar(0);
        this.add(BOTTOM, this.horizontal);
        this.vertical.addAdjustmentListener(new AdjustHandler());
        this.horizontal.addAdjustmentListener(new AdjustHandler());
        this.painter.addComponentListener(new ComponentHandler());
        this.painter.addMouseListener(new MouseHandler());
        this.painter.addMouseMotionListener(new DragHandler());
        this.addFocusListener(new FocusHandler());
        this.editable = textAreaDefaults.editable;
        this.caretVisible = textAreaDefaults.caretVisible;
        this.caretBlinks = textAreaDefaults.caretBlinks;
        this.electricScroll = textAreaDefaults.electricScroll;
        this.popup = textAreaDefaults.popup;
        this.setDocument(new SyntaxDocument());
        focusedComponent = this;
    }

    public boolean getFocusTraversalKeysEnabled() {
        return false;
    }

    public InputHandler getInputHandler() {
        return this.view.getInputHandler();
    }

    public final boolean isManagingFocus() {
        return true;
    }

    public final Dimension getMinimumSize() {
        return new Dimension(0, 0);
    }

    public final TextAreaPainter getPainter() {
        return this.painter;
    }

    public final Gutter getGutter() {
        return this.gutter;
    }

    public final boolean isCaretBlinkEnabled() {
        return this.caretBlinks;
    }

    public void setCaretBlinkEnabled(boolean bl) {
        this.caretBlinks = bl;
        if (!bl) {
            this.blink = false;
        }
        this.painter.invalidateSelectedLines();
    }

    public final boolean isCaretVisible() {
        return (!this.caretBlinks || this.blink) && this.caretVisible;
    }

    public void setCaretVisible(boolean bl) {
        this.caretVisible = bl;
        this.blink = true;
        this.painter.invalidateSelectedLines();
    }

    public final void blinkCaret() {
        if (this.caretBlinks) {
            this.blink = !this.blink;
            this.painter.invalidateSelectedLines();
        } else {
            this.blink = true;
        }
    }

    public final int getElectricScroll() {
        return this.electricScroll;
    }

    public final void setElectricScroll(int n) {
        this.electricScroll = n;
    }

    public void updateScrollBars() {
        int n;
        if (this.vertical != null && this.visibleLines != 0) {
            int n2;
            n = this.getLineCount();
            if (this.firstLine < 0) {
                this.setFirstLine(0);
                return;
            }
            if (n < this.firstLine + this.visibleLines && (n2 = Math.max(0, n - this.visibleLines)) != this.firstLine) {
                this.setFirstLine(n2);
                return;
            }
            this.vertical.setValues(this.firstLine, this.visibleLines, 0, this.getLineCount());
            this.vertical.setUnitIncrement(2);
            this.vertical.setBlockIncrement(this.visibleLines);
        }
        n = this.painter.getWidth();
        if (this.horizontal != null && n != 0) {
            this.maxHorizontalScrollWidth = 0;
            this.painter.repaint();
            this.horizontal.setUnitIncrement(this.painter.getFontMetrics().charWidth('w'));
            this.horizontal.setBlockIncrement(n / 2);
        }
    }

    public final int getFirstLine() {
        return this.firstLine;
    }

    public void setFirstLine(int n) {
        if (n == this.firstLine) {
            return;
        }
        int n2 = this.firstLine;
        this.firstLine = n;
        this.maxHorizontalScrollWidth = 0;
        if (n != this.vertical.getValue()) {
            this.updateScrollBars();
        }
        this.painter.repaint();
        this.gutter.repaint();
    }

    public final int getVisibleLines() {
        return this.visibleLines;
    }

    public final void recalculateVisibleLines() {
        if (this.painter == null) {
            return;
        }
        int n = this.painter.getHeight();
        int n2 = this.painter.getFontMetrics().getHeight();
        int n3 = this.visibleLines;
        this.visibleLines = n / n2;
        this.updateScrollBars();
    }

    void updateMaxHorizontalScrollWidth() {
        int n = this.getTokenMarker().getMaxLineWidth(this.firstLine, this.visibleLines);
        if (n != this.maxHorizontalScrollWidth) {
            this.maxHorizontalScrollWidth = n;
            this.horizontal.setValues(- this.horizontalOffset, this.painter.getWidth(), 0, this.maxHorizontalScrollWidth + this.painter.getFontMetrics().charWidth('w'));
        }
    }

    public final int getHorizontalOffset() {
        return this.horizontalOffset;
    }

    public void setHorizontalOffset(int n) {
        if (n == this.horizontalOffset) {
            return;
        }
        this.horizontalOffset = n;
        if (n != this.horizontal.getValue()) {
            this.updateScrollBars();
        }
        this.painter.repaint();
    }

    public boolean setOrigin(int n, int n2) {
        boolean bl = false;
        int n3 = this.firstLine;
        if (n2 != this.horizontalOffset) {
            this.horizontalOffset = n2;
            bl = true;
        }
        if (n != this.firstLine) {
            this.firstLine = n;
            bl = true;
        }
        if (bl) {
            this.updateScrollBars();
            this.painter.repaint();
            this.gutter.repaint();
        }
        return bl;
    }

    public boolean scrollToCaret() {
        int n = this.getCaretLine();
        int n2 = this.getLineStartOffset(n);
        int n3 = Math.max(0, Math.min(this.getLineLength(n) - 1, this.getCaretPosition() - n2));
        return this.scrollTo(n, n3);
    }

    public boolean scrollTo(int n, int n2) {
        if (this.visibleLines == 0) {
            this.setFirstLine(Math.max(0, n - this.electricScroll));
            return true;
        }
        int n3 = this.firstLine;
        int n4 = this.horizontalOffset;
        if (n < this.firstLine + this.electricScroll) {
            n3 = Math.max(0, n - this.electricScroll);
        } else if (n + this.electricScroll >= this.firstLine + this.visibleLines) {
            n3 = n - this.visibleLines + this.electricScroll + 1;
            if (n3 + this.visibleLines >= this.getLineCount()) {
                n3 = this.getLineCount() - this.visibleLines;
            }
            if (n3 < 0) {
                n3 = 0;
            }
        }
        int n5 = this.offsetToX(n, n2);
        int n6 = this.painter.getFontMetrics().charWidth('w');
        if (n5 < 0) {
            n4 = Math.min(0, this.horizontalOffset - n5 + n6 + 5);
        } else if (n5 + n6 >= this.painter.getWidth()) {
            n4 = this.horizontalOffset + (this.painter.getWidth() - n5) - n6 - 5;
        }
        return this.setOrigin(n3, n4);
    }

    public int lineToY(int n) {
        FontMetrics fontMetrics = this.painter.getFontMetrics();
        return (n - this.firstLine) * fontMetrics.getHeight() - (fontMetrics.getLeading() + fontMetrics.getMaxDescent());
    }

    public int yToLine(int n) {
        FontMetrics fontMetrics = this.painter.getFontMetrics();
        int n2 = fontMetrics.getHeight();
        return Math.max(0, Math.min(this.getLineCount() - 1, n / n2 + this.firstLine));
    }

    public int offsetToX(int n, int n2) {
        TokenMarker tokenMarker = this.getTokenMarker();
        FontMetrics fontMetrics = this.painter.getFontMetrics();
        this.getLineText(n, this.lineSegment);
        int n3 = this.lineSegment.offset;
        int n4 = this.horizontalOffset;
        if (tokenMarker == null) {
            this.lineSegment.count = n2;
            return n4 + Utilities.getTabbedTextWidth(this.lineSegment, fontMetrics, n4, this.painter, 0);
        }
        Token token = tokenMarker.markTokens(this.lineSegment, n);
        Toolkit toolkit = this.painter.getToolkit();
        Font font = this.painter.getFont();
        SyntaxStyle[] arrsyntaxStyle = this.painter.getStyles();
        byte by;
        while ((by = token.id) != 127) {
            fontMetrics = by == 0 ? this.painter.getFontMetrics() : arrsyntaxStyle[by].getFontMetrics(font);
            int n5 = token.length;
            if (n2 + n3 < this.lineSegment.offset + n5) {
                this.lineSegment.count = n2 - (this.lineSegment.offset - n3);
                return n4 + Utilities.getTabbedTextWidth(this.lineSegment, fontMetrics, n4, this.painter, 0);
            }
            this.lineSegment.count = n5;
            n4+=Utilities.getTabbedTextWidth(this.lineSegment, fontMetrics, n4, this.painter, 0);
            this.lineSegment.offset+=n5;
            token = token.next;
        }
        return n4;
    }

    public int xToOffset(int n, int n2) {
        TokenMarker tokenMarker = this.getTokenMarker();
        FontMetrics fontMetrics = this.painter.getFontMetrics();
        this.getLineText(n, this.lineSegment);
        char[] arrc = this.lineSegment.array;
        int n3 = this.lineSegment.offset;
        int n4 = this.lineSegment.count;
        int n5 = this.horizontalOffset;
        if (tokenMarker == null) {
            for (int i = 0; i < n4; ++i) {
                char c = arrc[i + n3];
                int n6 = c == '\t' ? (int)this.painter.nextTabStop(n5, i) - n5 : fontMetrics.charWidth(c);
                if (this.painter.isBlockCaretEnabled() ? n2 - n6 <= n5 : n2 - n6 / 2 <= n5) {
                    return i;
                }
                n5+=n6;
            }
            return n4;
        }
        Token token = tokenMarker.markTokens(this.lineSegment, n);
        int n7 = 0;
        Toolkit toolkit = this.painter.getToolkit();
        Font font = this.painter.getFont();
        SyntaxStyle[] arrsyntaxStyle = this.painter.getStyles();
        byte by;
        while ((by = token.id) != 127) {
            fontMetrics = by == 0 ? this.painter.getFontMetrics() : arrsyntaxStyle[by].getFontMetrics(font);
            int n8 = token.length;
            for (int i = 0; i < n8; ++i) {
                char c = arrc[n3 + n7 + i];
                int n9 = c == '\t' ? (int)this.painter.nextTabStop(n5, n7 + i) - n5 : fontMetrics.charWidth(c);
                if (this.painter.isBlockCaretEnabled() ? n2 - n9 <= n5 : n2 - n9 / 2 <= n5) {
                    return n7 + i;
                }
                n5+=n9;
            }
            n7+=n8;
            token = token.next;
        }
        return n7;
    }

    public int xyToOffset(int n, int n2) {
        int n3 = this.yToLine(n2);
        int n4 = this.getLineStartOffset(n3);
        return n4 + this.xToOffset(n3, n);
    }

    public final SyntaxDocument getDocument() {
        return this.document;
    }

    public void setDocument(SyntaxDocument syntaxDocument) {
        if (this.document == syntaxDocument) {
            return;
        }
        if (this.document != null) {
            this.document.removeDocumentListener(this.documentHandler);
        }
        this.document = syntaxDocument;
        syntaxDocument.addDocumentListener(this.documentHandler);
        this.documentHandlerInstalled = true;
        this.maxHorizontalScrollWidth = 0;
        this.select(0, 0);
        this.updateScrollBars();
        this.painter.repaint();
        this.gutter.repaint();
    }

    public final TokenMarker getTokenMarker() {
        return this.document.getTokenMarker();
    }

    public final void setTokenMarker(TokenMarker tokenMarker) {
        this.document.setTokenMarker(tokenMarker);
    }

    public final int getDocumentLength() {
        return this.document.getLength();
    }

    public final int getLineCount() {
        return this.document.getDefaultRootElement().getElementCount();
    }

    public final int getLineOfOffset(int n) {
        return this.document.getDefaultRootElement().getElementIndex(n);
    }

    public int getLineStartOffset(int n) {
        Element element = this.document.getDefaultRootElement().getElement(n);
        if (element == null) {
            return -1;
        }
        return element.getStartOffset();
    }

    public int getLineEndOffset(int n) {
        Element element = this.document.getDefaultRootElement().getElement(n);
        if (element == null) {
            return -1;
        }
        return element.getEndOffset();
    }

    public int getLineLength(int n) {
        Element element = this.document.getDefaultRootElement().getElement(n);
        if (element == null) {
            return -1;
        }
        return element.getEndOffset() - element.getStartOffset() - 1;
    }

    public String getText() {
        try {
            return this.document.getText(0, this.document.getLength());
        }
        catch (BadLocationException var1_1) {
            var1_1.printStackTrace();
            return null;
        }
    }

    public void setText(String string) {
        try {
            this.document.beginCompoundEdit();
            this.document.remove(0, this.document.getLength());
            this.document.insertString(0, string, null);
        }
        catch (BadLocationException var2_2) {
            var2_2.printStackTrace();
        }
        finally {
            this.document.endCompoundEdit();
        }
    }

    public final String getText(int n, int n2) {
        try {
            return this.document.getText(n, n2);
        }
        catch (BadLocationException var3_3) {
            var3_3.printStackTrace();
            return null;
        }
    }

    public final void getText(int n, int n2, Segment segment) {
        try {
            this.document.getText(n, n2, segment);
        }
        catch (BadLocationException var4_4) {
            var4_4.printStackTrace();
            segment.count = 0;
            segment.offset = 0;
        }
    }

    public final String getLineText(int n) {
        int n2 = this.getLineStartOffset(n);
        return this.getText(n2, this.getLineEndOffset(n) - n2 - 1);
    }

    public final void getLineText(int n, Segment segment) {
        int n2 = this.getLineStartOffset(n);
        this.getText(n2, this.getLineEndOffset(n) - n2 - 1, segment);
    }

    public final int getSelectionStart() {
        return this.selectionStart;
    }

    public int getSelectionStart(int n) {
        if (n == this.selectionStartLine) {
            return this.selectionStart;
        }
        if (this.rectSelect) {
            Element element = this.document.getDefaultRootElement();
            int n2 = this.selectionStart - element.getElement(this.selectionStartLine).getStartOffset();
            Element element2 = element.getElement(n);
            int n3 = element2.getStartOffset();
            int n4 = element2.getEndOffset() - 1;
            return Math.min(n4, n3 + n2);
        }
        return this.getLineStartOffset(n);
    }

    public final int getSelectionStartLine() {
        return this.selectionStartLine;
    }

    public final void setSelectionStart(int n) {
        this.select(n, this.selectionEnd);
    }

    public final int getSelectionEnd() {
        return this.selectionEnd;
    }

    public int getSelectionEnd(int n) {
        if (n == this.selectionEndLine) {
            return this.selectionEnd;
        }
        if (this.rectSelect) {
            Element element = this.document.getDefaultRootElement();
            int n2 = this.selectionEnd - element.getElement(this.selectionEndLine).getStartOffset();
            Element element2 = element.getElement(n);
            int n3 = element2.getStartOffset();
            int n4 = element2.getEndOffset() - 1;
            return Math.min(n4, n3 + n2);
        }
        return this.getLineEndOffset(n) - 1;
    }

    public final int getSelectionEndLine() {
        return this.selectionEndLine;
    }

    public final void setSelectionEnd(int n) {
        this.select(this.selectionStart, n);
    }

    public final int getCaretPosition() {
        return this.biasLeft ? this.selectionStart : this.selectionEnd;
    }

    public final int getCaretLine() {
        return this.biasLeft ? this.selectionStartLine : this.selectionEndLine;
    }

    public final int getMarkPosition() {
        return this.biasLeft ? this.selectionEnd : this.selectionStart;
    }

    public final int getMarkLine() {
        return this.biasLeft ? this.selectionEndLine : this.selectionStartLine;
    }

    public final void setCaretPosition(int n) {
        this.select(n, n);
    }

    public final void selectAll() {
        this.select(0, this.getDocumentLength());
    }

    public final void selectNone() {
        this.select(this.getCaretPosition(), this.getCaretPosition());
    }

    public void select(int n, int n2) {
        boolean bl;
        int n3;
        int n4;
        if (n <= n2) {
            n3 = n;
            n4 = n2;
            bl = false;
        } else {
            n3 = n2;
            n4 = n;
            bl = true;
        }
        if (n3 < 0 || n4 > this.getDocumentLength()) {
            throw new IllegalArgumentException("Bounds out of range: " + n3 + "," + n4);
        }
        if (n3 != this.selectionStart || n4 != this.selectionEnd || bl != this.biasLeft) {
            this.updateBracketHighlight(n2);
            int n5 = this.getLineOfOffset(n3);
            int n6 = this.getLineOfOffset(n4);
            this.painter.invalidateLineRange(this.selectionStartLine, this.selectionEndLine);
            this.painter.invalidateLineRange(n5, n6);
            this.document.addUndoableEdit(new CaretUndo(this.selectionStart, this.selectionEnd, n3, n4));
            this.selectionStart = n3;
            this.selectionEnd = n4;
            this.selectionStartLine = n5;
            this.selectionEndLine = n6;
            this.biasLeft = bl;
            this.gutter.repaint();
            this.fireCaretEvent();
        }
        this.blink = true;
        caretTimer.restart();
        if (this.selectionStart == this.selectionEnd) {
            this.rectSelect = false;
        }
        this.magicCaret = -1;
        this.scrollToCaret();
    }

    public final String getSelectedText() {
        if (this.selectionStart == this.selectionEnd) {
            return null;
        }
        if (this.rectSelect) {
            Element element = this.document.getDefaultRootElement();
            int n = this.selectionStart - element.getElement(this.selectionStartLine).getStartOffset();
            int n2 = this.selectionEnd - element.getElement(this.selectionEndLine).getStartOffset();
            if (n2 < n) {
                int n3 = n2;
                n2 = n;
                n = n3;
            }
            StringBuffer stringBuffer = new StringBuffer();
            Segment segment = new Segment();
            for (int i = this.selectionStartLine; i <= this.selectionEndLine; ++i) {
                Element element2 = element.getElement(i);
                int n4 = element2.getStartOffset();
                int n5 = element2.getEndOffset() - 1;
                int n6 = n5 - n4;
                n4 = Math.min(n4 + n, n5);
                n6 = Math.min(n2 - n, n5 - n4);
                this.getText(n4, n6, segment);
                stringBuffer.append(segment.array, segment.offset, segment.count);
                if (i == this.selectionEndLine) continue;
                stringBuffer.append('\n');
            }
            return stringBuffer.toString();
        }
        return this.getText(this.selectionStart, this.selectionEnd - this.selectionStart);
    }

    public void setSelectedText(String string) {
        if (!this.editable) {
            throw new InternalError("Text component read only");
        }
        this.document.beginCompoundEdit();
        try {
            if (this.rectSelect) {
                int n;
                int n2;
                Element element = this.document.getDefaultRootElement();
                int n3 = this.selectionStart - element.getElement(this.selectionStartLine).getStartOffset();
                int n4 = this.selectionEnd - element.getElement(this.selectionEndLine).getStartOffset();
                if (n4 < n3) {
                    n2 = n4;
                    n4 = n3;
                    n3 = n2;
                }
                n2 = 0;
                int n5 = 0;
                for (n = this.selectionStartLine; n <= this.selectionEndLine; ++n) {
                    Element element2 = element.getElement(n);
                    int n6 = element2.getStartOffset();
                    int n7 = element2.getEndOffset() - 1;
                    int n8 = Math.min(n7, n6 + n3);
                    this.document.remove(n8, Math.min(n7 - n8, n4 - n3));
                    if (string == null) continue;
                    n5 = string.indexOf(10, n2);
                    if (n5 == -1) {
                        n5 = string.length();
                    }
                    this.document.insertString(n8, string.substring(n2, n5), null);
                    n2 = Math.min(string.length(), n5 + 1);
                }
                if (string != null && n5 != string.length()) {
                    n = element.getElement(this.selectionEndLine).getEndOffset() - 1;
                    this.document.insertString(n, "\n", null);
                    this.document.insertString(n + 1, string.substring(n5 + 1), null);
                }
            } else {
                this.document.remove(this.selectionStart, this.selectionEnd - this.selectionStart);
                if (string != null) {
                    this.document.insertString(this.selectionStart, string, null);
                }
            }
        }
        catch (BadLocationException var2_3) {
            var2_3.printStackTrace();
            throw new InternalError("Cannot replace selection");
        }
        finally {
            this.document.endCompoundEdit();
        }
        this.setCaretPosition(this.selectionEnd);
    }

    public final boolean isEditable() {
        return this.editable;
    }

    public final void setEditable(boolean bl) {
        this.editable = bl;
    }

    public final JPopupMenu getRightClickPopup() {
        return this.popup;
    }

    public final void setRightClickPopup(JPopupMenu jPopupMenu) {
        this.popup = jPopupMenu;
    }

    public final int getMagicCaretPosition() {
        return this.magicCaret;
    }

    public final void setMagicCaretPosition(int n) {
        this.magicCaret = n;
    }

    public void overwriteSetSelectedText(String string) {
        if (!(this.overwrite && this.selectionStart == this.selectionEnd)) {
            this.setSelectedText(string);
            return;
        }
        int n = this.getCaretPosition();
        int n2 = this.getLineEndOffset(this.getCaretLine());
        if (n2 - n <= string.length()) {
            this.setSelectedText(string);
            return;
        }
        this.document.beginCompoundEdit();
        try {
            this.document.remove(n, string.length());
            this.document.insertString(n, string, null);
        }
        catch (BadLocationException var4_4) {
            var4_4.printStackTrace();
        }
        finally {
            this.document.endCompoundEdit();
        }
    }

    public final boolean isOverwriteEnabled() {
        return this.overwrite;
    }

    public final void setOverwriteEnabled(boolean bl) {
        this.overwrite = bl;
        this.painter.invalidateSelectedLines();
    }

    public final boolean isSelectionRectangular() {
        return this.rectSelect;
    }

    public final void setSelectionRectangular(boolean bl) {
        this.rectSelect = bl;
        this.painter.invalidateSelectedLines();
    }

    public final int getBracketPosition() {
        return this.bracketPosition;
    }

    public final int getBracketLine() {
        return this.bracketLine;
    }

    public final void addCaretListener(CaretListener caretListener) {
        Class class_ = CaretListener.class;
        this.listenerList.add(class_, caretListener);
    }

    public final void removeCaretListener(CaretListener caretListener) {
        Class class_ = CaretListener.class;
        this.listenerList.remove(class_, caretListener);
    }

    public void appendCut() {
        if (this.editable) {
            this.appendCopy();
            if (this.selectionStart == this.selectionEnd) {
                int n = this.getCaretLine();
                int n2 = this.getLineStartOffset(n);
                int n3 = this.getLineEndOffset(n);
                if (n3 == this.document.getLength() + 1) {
                    --n3;
                }
                try {
                    this.document.remove(n2, n3 - n2);
                }
                catch (BadLocationException var4_4) {}
            } else {
                this.setSelectedText("");
            }
        }
    }

    public void cut() {
        if (this.editable) {
            this.copy();
            if (this.selectionStart == this.selectionEnd) {
                int n = this.getCaretLine();
                int n2 = this.getLineStartOffset(n);
                int n3 = this.getLineEndOffset(n);
                if (n3 == this.document.getLength() + 1) {
                    --n3;
                }
                try {
                    this.document.remove(n2, n3 - n2);
                }
                catch (BadLocationException var4_4) {}
            } else {
                this.setSelectedText("");
            }
        }
    }

    public void appendCopy() {
        String string;
        if (this.selectionStart == this.selectionEnd) {
            int n = this.getCaretLine();
            int n2 = this.getLineStartOffset(n);
            int n3 = this.getLineEndOffset(n);
            string = this.getText(n2, n3 - n2);
            this.setSelectionStart(n2);
            this.setSelectionEnd(n2);
        } else {
            string = this.getSelectedText();
        }
        Clipboard clipboard = this.getToolkit().getSystemClipboard();
        try {
            String string2 = ((String)clipboard.getContents(this).getTransferData(DataFlavor.stringFlavor)).replace('\r', '\n');
            clipboard.setContents(new StringSelection(string2 + string), null);
        }
        catch (Exception var3_5) {
            clipboard.setContents(new StringSelection(string), null);
        }
    }

    public void copy() {
        String string;
        if (this.selectionStart == this.selectionEnd) {
            int n = this.getCaretLine();
            int n2 = this.getLineStartOffset(n);
            int n3 = this.getLineEndOffset(n);
            string = this.getText(n2, n3 - n2);
            this.setSelectionStart(n2);
            this.setSelectionEnd(n2);
        } else {
            string = this.getSelectedText();
        }
        Clipboard clipboard = this.getToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(string), null);
    }

    public void paste() {
        if (this.editable) {
            Clipboard clipboard = this.getToolkit().getSystemClipboard();
            try {
                String string = ((String)clipboard.getContents(this).getTransferData(DataFlavor.stringFlavor)).replace('\r', '\n');
                this.setSelectedText(string);
            }
            catch (Exception var2_3) {
                this.getToolkit().beep();
            }
        }
    }

    public Component getStatus() {
        return ((ScrollLayout)this.getLayout()).leftOfScrollBar;
    }

    public void addNotify() {
        super.addNotify();
        if (!this.documentHandlerInstalled) {
            this.documentHandlerInstalled = true;
            this.document.addDocumentListener(this.documentHandler);
        }
    }

    public void removeNotify() {
        super.removeNotify();
        if (focusedComponent == this) {
            focusedComponent = null;
        }
        if (this.documentHandlerInstalled) {
            this.document.removeDocumentListener(this.documentHandler);
            this.documentHandlerInstalled = false;
        }
    }

    protected void processKeyEvent(KeyEvent keyEvent) {
        if ((keyEvent = KeyEventWorkaround.processKeyEvent(keyEvent)) == null) {
            return;
        }
        InputHandler inputHandler = this.view.getInputHandler();
        KeyListener keyListener = this.view.getKeyEventInterceptor();
        switch (keyEvent.getID()) {
            case 400: {
                if (keyListener != null) {
                    keyListener.keyTyped(keyEvent);
                    break;
                }
                inputHandler.keyTyped(keyEvent);
                break;
            }
            case 401: {
                if (keyListener != null) {
                    keyListener.keyPressed(keyEvent);
                    break;
                }
                inputHandler.keyPressed(keyEvent);
                break;
            }
            case 402: {
                if (keyListener != null) {
                    keyListener.keyReleased(keyEvent);
                    break;
                }
                inputHandler.keyReleased(keyEvent);
            }
        }
        if (!keyEvent.isConsumed()) {
            super.processKeyEvent(keyEvent);
        }
    }

    protected void fireCaretEvent() {
        Object[] arrobject = this.listenerList.getListenerList();
        for (int i = arrobject.length - 2; i >= 0; --i) {
            if (arrobject[i] != (class$javax$swing$event$CaretListener == null ? JEditTextArea.class$("javax.swing.event.CaretListener") : class$javax$swing$event$CaretListener)) continue;
            ((CaretListener)arrobject[i + 1]).caretUpdate(this.caretEvent);
        }
    }

    protected void updateBracketHighlight(int n) {
        if (!this.painter.isBracketHighlightEnabled()) {
            return;
        }
        if (this.bracketLine != -1) {
            this.painter.invalidateLine(this.bracketLine);
        }
        if (n == 0) {
            this.bracketLine = -1;
            this.bracketPosition = -1;
            return;
        }
        try {
            int n2 = TextUtilities.findMatchingBracket(this.document, n - 1);
            if (n2 != -1) {
                this.bracketLine = this.getLineOfOffset(n2);
                this.bracketPosition = n2 - this.getLineStartOffset(this.bracketLine);
                if (this.bracketLine != -1) {
                    this.painter.invalidateLine(this.bracketLine);
                }
                return;
            }
        }
        catch (BadLocationException var2_3) {
            var2_3.printStackTrace();
        }
        this.bracketPosition = -1;
        this.bracketLine = -1;
    }

    protected void documentChanged(DocumentEvent documentEvent) {
        DocumentEvent.ElementChange elementChange = documentEvent.getChange(this.document.getDefaultRootElement());
        int n = elementChange == null ? 0 : elementChange.getChildrenAdded().length - elementChange.getChildrenRemoved().length;
        int n2 = this.getLineOfOffset(documentEvent.getOffset());
        if (n == 0) {
            this.painter.invalidateLine(n2);
        } else if (n2 < this.firstLine) {
            this.setFirstLine(this.firstLine + n);
        } else {
            this.painter.invalidateLineRange(n2, this.firstLine + this.visibleLines);
            this.gutter.repaint();
            this.updateScrollBars();
        }
    }

    public void setOneClick(OneClickAction oneClickAction) {
        this.setOneClick(oneClickAction, null);
    }

    public void setOneClick(OneClickAction oneClickAction, ActionEvent actionEvent) {
        this.oneClick = oneClickAction;
        this.oneClickEvent = actionEvent;
        JextTextArea jextTextArea = (JextTextArea)this;
        jextTextArea.getJextParent().setStatus(jextTextArea);
    }

    public void setShadowCaretPosition(int n) {
        this.shadowCaretOffset = n;
        if (n == -1) {
            this.shadowCaretLine = -1;
        } else {
            this.shadowCaretLine = this.getLineOfOffset(n);
            if (!this.scrollTo(this.shadowCaretLine, this.shadowCaretOffset - this.getLineStartOffset(this.shadowCaretLine))) {
                this.repaint();
            }
        }
    }

    public int getShadowCaretLine() {
        return this.shadowCaretLine;
    }

    public int getShadowCaretPosition() {
        return this.shadowCaretOffset;
    }

    static {
        caretTimer = new Timer(500, new CaretBlinker());
        caretTimer.setInitialDelay(500);
        caretTimer.start();
    }

    class CaretUndo
    extends AbstractUndoableEdit {
        private int start;
        private int end;
        private int newStart;
        private int newEnd;

        CaretUndo(int n, int n2, int n3, int n4) {
            this.start = n;
            this.end = n2;
            this.newStart = n3;
            this.newEnd = n4;
        }

        public boolean isSignificant() {
            return false;
        }

        public String getPresentationName() {
            return "caret move";
        }

        public void undo() throws CannotUndoException {
            super.undo();
            JEditTextArea.this.select(this.start, this.end);
        }

        public boolean addEdit(UndoableEdit undoableEdit) {
            if (undoableEdit instanceof CaretUndo) {
                CaretUndo caretUndo = (CaretUndo)undoableEdit;
                caretUndo.die();
                return true;
            }
            return false;
        }

        public String toString() {
            return this.getPresentationName() + "[start=" + this.start + ",end=" + this.end + "]";
        }
    }

    class MouseHandler
    extends MouseAdapter {
        MouseHandler() {
        }

        public void mouseReleased(MouseEvent mouseEvent) {
            int n = JEditTextArea.this.yToLine(mouseEvent.getY());
            int n2 = JEditTextArea.this.xToOffset(n, mouseEvent.getX());
            int n3 = JEditTextArea.this.getLineStartOffset(n) + n2;
            if (JEditTextArea.this.dragText) {
                if (n3 > JEditTextArea.this.getSelectionStart() && n3 < JEditTextArea.this.getSelectionEnd()) {
                    this.doSingleClick(mouseEvent, n, n2, n3);
                } else {
                    String string = JEditTextArea.this.getSelectedText();
                    try {
                        if (!mouseEvent.isControlDown()) {
                            if (JEditTextArea.this.getSelectionStart() < n3) {
                                n3-=string.length();
                            }
                            JEditTextArea.this.setSelectedText("");
                        }
                        JEditTextArea.this.document.insertString(n3, string, null);
                        JEditTextArea.this.select(n3, n3 + string.length());
                    }
                    catch (BadLocationException var6_6) {
                        // empty catch block
                    }
                }
                JEditTextArea.this.setShadowCaretPosition(-1);
                JEditTextArea.this.dragText = false;
                if (JEditTextArea.this.dragCursor) {
                    JEditTextArea.this.dragCursor = false;
                    JEditTextArea.this.painter.setCursor(Cursor.getPredefinedCursor(2));
                }
            }
        }

        public void mousePressed(MouseEvent mouseEvent) {
            JEditTextArea.this.requestFocus();
            JEditTextArea.this.setCaretVisible(true);
            JEditTextArea.focusedComponent = JEditTextArea.this;
            if ((mouseEvent.getModifiers() & 4) != 0 && JEditTextArea.this.popup != null) {
                this.doRightClick(mouseEvent);
                return;
            }
            int n = JEditTextArea.this.yToLine(mouseEvent.getY());
            int n2 = JEditTextArea.this.xToOffset(n, mouseEvent.getX());
            int n3 = JEditTextArea.this.getLineStartOffset(n) + n2;
            JEditTextArea.this.clickCount = mouseEvent.getClickCount();
            switch (JEditTextArea.this.clickCount) {
                case 1: {
                    if (JEditTextArea.this.getSelectionStart() != JEditTextArea.this.getSelectionEnd() && n3 > JEditTextArea.this.getSelectionStart() && n3 < JEditTextArea.this.getSelectionEnd()) {
                        JEditTextArea.this.dragText = true;
                        break;
                    }
                    this.doSingleClick(mouseEvent, n, n2, n3);
                    break;
                }
                case 2: {
                    try {
                        this.doDoubleClick(mouseEvent, n, n2, n3);
                    }
                    catch (BadLocationException var5_5) {
                        var5_5.printStackTrace();
                    }
                    break;
                }
                case 3: {
                    this.doTripleClick(mouseEvent, n, n2, n3);
                }
            }
        }

        private void doSingleClick(MouseEvent mouseEvent, int n, int n2, int n3) {
            if (mouseEvent.isShiftDown()) {
                JEditTextArea.this.rectSelect = mouseEvent.isControlDown();
                JEditTextArea.this.select(JEditTextArea.this.getMarkPosition(), n3);
            } else {
                JEditTextArea.this.setCaretPosition(n3);
            }
            if (JEditTextArea.this.oneClick != null) {
                JEditTextArea.this.oneClick.oneClickActionPerformed(JEditTextArea.this.oneClickEvent);
            }
            ((JextTextArea)JEditTextArea.this).endCurrentEdit();
        }

        private void doDoubleClick(MouseEvent mouseEvent, int n, int n2, int n3) throws BadLocationException {
            if (JEditTextArea.this.getLineLength(n) == 0) {
                return;
            }
            try {
                int n4 = TextUtilities.findMatchingBracket(JEditTextArea.this.document, Math.max(0, n3 - 1));
                if (n4 != -1) {
                    int n5 = JEditTextArea.this.getMarkPosition();
                    if (n4 > n5) {
                        ++n4;
                        --n5;
                    }
                    JEditTextArea.this.select(n5, n4);
                    return;
                }
            }
            catch (BadLocationException var5_6) {
                var5_6.printStackTrace();
            }
            String string = JEditTextArea.this.getLineText(n);
            String string2 = (String)JEditTextArea.this.document.getProperty("noWordSep");
            if (n2 == JEditTextArea.this.getLineLength(n)) {
                --n2;
            }
            int n6 = TextUtilities.findWordStart(string, n2, string2);
            int n7 = TextUtilities.findWordEnd(string, n2 + 1, string2);
            int n8 = JEditTextArea.this.getLineStartOffset(n);
            JEditTextArea.this.select(n8 + n6, n8 + n7);
        }

        private void doTripleClick(MouseEvent mouseEvent, int n, int n2, int n3) {
            JEditTextArea.this.select(JEditTextArea.this.getLineStartOffset(n), JEditTextArea.this.getLineEndOffset(n) - 1);
        }

        private void doRightClick(MouseEvent mouseEvent) {
            int n = mouseEvent.getX();
            int n2 = mouseEvent.getY();
            JextFrame jextFrame = ((JextTextArea)JEditTextArea.this).getJextParent();
            if (jextFrame != null) {
                Dimension dimension = jextFrame.getSize();
                Point point = jextFrame.getLocationOnScreen();
                Insets insets = jextFrame.getInsets();
                Point point2 = JEditTextArea.this.painter.getLocationOnScreen();
                Dimension dimension2 = JEditTextArea.this.popup.getSize();
                if (point2.x + n + dimension2.width > point.x + dimension.width - insets.right) {
                    n-=dimension2.width;
                }
                if (point2.y + n2 + dimension2.height > point.y + dimension.height - insets.bottom) {
                    n2 = point.y + dimension.height - insets.bottom - (point2.y + dimension2.height);
                }
            }
            JEditTextArea.this.popup.show(JEditTextArea.this.painter, n, n2);
        }
    }

    class FocusHandler
    implements FocusListener {
        FocusHandler() {
        }

        public void focusGained(FocusEvent focusEvent) {
            JEditTextArea.this.setCaretVisible(true);
            JEditTextArea.focusedComponent = JEditTextArea.this;
        }

        public void focusLost(FocusEvent focusEvent) {
            JEditTextArea.this.setCaretVisible(false);
            JEditTextArea.focusedComponent = null;
        }
    }

    class DragHandler
    implements MouseMotionListener {
        DragHandler() {
        }

        public void mouseDragged(MouseEvent mouseEvent) {
            if (JEditTextArea.this.popup != null && JEditTextArea.this.popup.isVisible()) {
                return;
            }
            if (JEditTextArea.this.dragText) {
                boolean bl = mouseEvent.isControlDown();
                if (bl != JEditTextArea.this.dragControlDown) {
                    JEditTextArea.this.dragCursor = false;
                }
                JEditTextArea.this.dragControlDown = bl;
                if (!JEditTextArea.this.dragCursor) {
                    if (mouseEvent.isControlDown()) {
                        JEditTextArea.this.painter.setCursor(DragSource.DefaultCopyDrop);
                    } else {
                        JEditTextArea.this.painter.setCursor(DragSource.DefaultMoveDrop);
                    }
                    JEditTextArea.this.dragCursor = true;
                }
                JEditTextArea.this.setShadowCaretPosition(JEditTextArea.this.xyToOffset(mouseEvent.getX(), mouseEvent.getY()));
                return;
            }
            JEditTextArea.this.setSelectionRectangular(mouseEvent.isControlDown());
            switch (JEditTextArea.this.clickCount) {
                case 1: {
                    this.doSingleDrag(mouseEvent);
                    break;
                }
                case 2: {
                    this.doDoubleDrag(mouseEvent);
                    break;
                }
                case 3: {
                    this.doTripleDrag(mouseEvent);
                }
            }
        }

        public void mouseMoved(MouseEvent mouseEvent) {
        }

        private void doSingleDrag(MouseEvent mouseEvent) {
            JEditTextArea.this.select(JEditTextArea.this.getMarkPosition(), JEditTextArea.this.xyToOffset(mouseEvent.getX(), mouseEvent.getY()));
        }

        private void doDoubleDrag(MouseEvent mouseEvent) {
            int n = JEditTextArea.this.getMarkLine();
            int n2 = JEditTextArea.this.getLineStartOffset(n);
            int n3 = JEditTextArea.this.getLineLength(n);
            int n4 = JEditTextArea.this.getMarkPosition() - n2;
            int n5 = JEditTextArea.this.yToLine(mouseEvent.getY());
            int n6 = JEditTextArea.this.getLineStartOffset(n5);
            int n7 = JEditTextArea.this.getLineLength(n5);
            int n8 = JEditTextArea.this.xToOffset(n5, mouseEvent.getX());
            String string = JEditTextArea.this.getLineText(n5);
            String string2 = JEditTextArea.this.getLineText(n);
            String string3 = (String)JEditTextArea.this.document.getProperty("noWordSep");
            if (n2 + n4 > n6 + n8) {
                if (n8 != 0 && n8 != n7) {
                    n8 = TextUtilities.findWordStart(string, n8, string3);
                }
                if (n3 != 0) {
                    n4 = TextUtilities.findWordEnd(string2, n4, string3);
                }
            } else {
                if (n8 != 0 && n7 != 0) {
                    n8 = TextUtilities.findWordEnd(string, n8, string3);
                }
                if (n4 != 0 && n4 != n3) {
                    n4 = TextUtilities.findWordStart(string2, n4, string3);
                }
            }
            JEditTextArea.this.select(n2 + n4, n6 + n8);
        }

        private void doTripleDrag(MouseEvent mouseEvent) {
            int n = JEditTextArea.this.getMarkLine();
            int n2 = JEditTextArea.this.yToLine(mouseEvent.getY());
            int n3 = JEditTextArea.this.xToOffset(n2, mouseEvent.getX());
            if (n > n2) {
                n = JEditTextArea.this.getLineEndOffset(n) - 1;
                n2 = n3 == JEditTextArea.this.getLineLength(n2) ? JEditTextArea.this.getLineEndOffset(n2) - 1 : JEditTextArea.this.getLineStartOffset(n2);
            } else {
                n = JEditTextArea.this.getLineStartOffset(n);
                n2 = n3 == 0 ? JEditTextArea.this.getLineStartOffset(n2) : JEditTextArea.this.getLineEndOffset(n2) - 1;
            }
            JEditTextArea.this.select(n, n2);
        }
    }

    class DocumentHandler
    implements DocumentListener {
        DocumentHandler() {
        }

        public void insertUpdate(DocumentEvent documentEvent) {
            int n;
            int n2;
            JEditTextArea.this.documentChanged(documentEvent);
            int n3 = documentEvent.getOffset();
            int n4 = documentEvent.getLength();
            boolean bl = false;
            if (JEditTextArea.this.selectionStart > n3 || JEditTextArea.this.selectionStart == JEditTextArea.this.selectionEnd && JEditTextArea.this.selectionStart == n3) {
                bl = true;
                n2 = JEditTextArea.this.selectionStart + n4;
            } else {
                n2 = JEditTextArea.this.selectionStart;
            }
            if (JEditTextArea.this.selectionEnd >= n3) {
                bl = true;
                n = JEditTextArea.this.selectionEnd + n4;
            } else {
                n = JEditTextArea.this.selectionEnd;
            }
            if (bl) {
                JEditTextArea.this.select(n2, n);
            } else {
                JEditTextArea.this.updateBracketHighlight(JEditTextArea.this.getCaretPosition());
            }
        }

        public void removeUpdate(DocumentEvent documentEvent) {
            int n;
            int n2;
            JEditTextArea.this.documentChanged(documentEvent);
            int n3 = documentEvent.getOffset();
            int n4 = documentEvent.getLength();
            boolean bl = false;
            if (JEditTextArea.this.selectionStart > n3) {
                bl = true;
                n2 = JEditTextArea.this.selectionStart > n3 + n4 ? JEditTextArea.this.selectionStart - n4 : n3;
            } else {
                n2 = JEditTextArea.this.selectionStart;
            }
            if (JEditTextArea.this.selectionEnd > n3) {
                bl = true;
                n = JEditTextArea.this.selectionEnd > n3 + n4 ? JEditTextArea.this.selectionEnd - n4 : n3;
            } else {
                n = JEditTextArea.this.selectionEnd;
            }
            if (bl) {
                JEditTextArea.this.select(n2, n);
            } else {
                JEditTextArea.this.updateBracketHighlight(JEditTextArea.this.getCaretPosition());
            }
        }

        public void changedUpdate(DocumentEvent documentEvent) {
        }
    }

    class ComponentHandler
    extends ComponentAdapter {
        ComponentHandler() {
        }

        public void componentResized(ComponentEvent componentEvent) {
            JEditTextArea.this.recalculateVisibleLines();
            JEditTextArea.this.scrollBarsInitialized = true;
        }
    }

    class AdjustHandler
    implements AdjustmentListener {
        AdjustHandler() {
        }

        public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
            if (!JEditTextArea.this.scrollBarsInitialized) {
                return;
            }
            SwingUtilities.invokeLater(new Runnable(this, adjustmentEvent){
                private final /* synthetic */ AdjustmentEvent val$evt;
                private final /* synthetic */ AdjustHandler this$1;

                public void run() {
                    if (this.val$evt.getAdjustable() == AdjustHandler.access$000((AdjustHandler)this.this$1).vertical) {
                        AdjustHandler.access$000(this.this$1).setFirstLine(AdjustHandler.access$000((AdjustHandler)this.this$1).vertical.getValue());
                    } else {
                        AdjustHandler.access$000(this.this$1).setHorizontalOffset(- AdjustHandler.access$000((AdjustHandler)this.this$1).horizontal.getValue());
                    }
                }
            });
        }

        static /* synthetic */ JEditTextArea access$000(AdjustHandler adjustHandler) {
            return adjustHandler.JEditTextArea.this;
        }
    }

    class MutableCaretEvent
    extends CaretEvent {
        MutableCaretEvent() {
            super(JEditTextArea.this);
        }

        public int getDot() {
            return JEditTextArea.this.getCaretPosition();
        }

        public int getMark() {
            return JEditTextArea.this.getMarkPosition();
        }
    }

    static class CaretBlinker
    implements ActionListener {
        CaretBlinker() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (JEditTextArea.focusedComponent != null && JEditTextArea.focusedComponent.hasFocus()) {
                JEditTextArea.focusedComponent.blinkCaret();
            }
        }
    }

    class ScrollLayout
    implements LayoutManager {
        Component center;
        Component left;
        Component right;
        Component bottom;
        Component leftOfScrollBar;

        ScrollLayout() {
        }

        public void addLayoutComponent(String string, Component component) {
            if (string.equals(JEditTextArea.CENTER)) {
                this.center = component;
            } else if (string.equals(JEditTextArea.RIGHT)) {
                this.right = component;
            } else if (string.equals(JEditTextArea.LEFT)) {
                this.left = component;
            } else if (string.equals(JEditTextArea.BOTTOM)) {
                this.bottom = component;
            } else if (string.equals(JEditTextArea.LEFT_OF_SCROLLBAR)) {
                this.leftOfScrollBar = component;
            }
        }

        public void removeLayoutComponent(Component component) {
            if (this.center == component) {
                this.center = null;
            } else if (this.right == component) {
                this.right = null;
            } else if (this.left == component) {
                this.left = null;
            } else if (this.bottom == component) {
                this.bottom = null;
            } else {
                this.leftOfScrollBar = null;
            }
        }

        public Dimension preferredLayoutSize(Container container) {
            Dimension dimension = new Dimension();
            Insets insets = JEditTextArea.this.getInsets();
            dimension.width = insets.left + insets.right;
            dimension.height = insets.top + insets.bottom;
            Dimension dimension2 = this.left.getPreferredSize();
            dimension.width+=dimension2.width;
            Dimension dimension3 = this.center.getPreferredSize();
            dimension.width+=dimension3.width;
            dimension.height+=dimension3.height;
            Dimension dimension4 = this.right.getPreferredSize();
            dimension.width+=dimension4.width;
            Dimension dimension5 = this.bottom.getPreferredSize();
            dimension.height+=dimension5.height;
            return dimension;
        }

        public Dimension minimumLayoutSize(Container container) {
            Dimension dimension = new Dimension();
            Insets insets = JEditTextArea.this.getInsets();
            dimension.width = insets.left + insets.right;
            dimension.height = insets.top + insets.bottom;
            Dimension dimension2 = this.left.getMinimumSize();
            dimension.width+=dimension2.width;
            Dimension dimension3 = this.center.getMinimumSize();
            dimension.width+=dimension3.width;
            dimension.height+=dimension3.height;
            Dimension dimension4 = this.right.getMinimumSize();
            dimension.width+=dimension4.width;
            Dimension dimension5 = this.bottom.getMinimumSize();
            dimension.height+=dimension5.height;
            return dimension;
        }

        public void layoutContainer(Container container) {
            Dimension dimension = container.getSize();
            Insets insets = container.getInsets();
            int n = insets.top;
            int n2 = insets.left;
            int n3 = insets.bottom;
            int n4 = insets.right;
            int n5 = this.right.getPreferredSize().width;
            int n6 = this.left.getPreferredSize().width;
            int n7 = this.bottom.getPreferredSize().height;
            int n8 = dimension.width - n6 - n5 - n2 - n4;
            int n9 = dimension.height - n7 - n - n3;
            this.left.setBounds(n2, n, n6, n9);
            this.center.setBounds(n2 + n6, n, n8, n9);
            this.right.setBounds(n2 + n6 + n8, n, n5, n9);
            if (this.leftOfScrollBar != null) {
                Dimension dimension2 = this.leftOfScrollBar.getPreferredSize();
                this.leftOfScrollBar.setBounds(n2, n + n9, dimension2.width, n7);
                n2+=dimension2.width;
            }
            this.bottom.setBounds(n2, n + n9, dimension.width - n5 - n2 - n4, n7);
        }
    }

}

