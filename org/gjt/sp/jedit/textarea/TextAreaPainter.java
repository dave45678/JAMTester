/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.textarea;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;
import javax.swing.text.Segment;
import javax.swing.text.TabExpander;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.gjt.sp.jedit.syntax.SyntaxStyle;
import org.gjt.sp.jedit.syntax.SyntaxUtilities;
import org.gjt.sp.jedit.syntax.Token;
import org.gjt.sp.jedit.syntax.TokenMarker;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.textarea.TextAreaDefaults;
import org.gjt.sp.jedit.textarea.TextAreaHighlight;
import org.jext.JextTextArea;
import org.jext.Utilities;

public class TextAreaPainter
extends JComponent
implements TabExpander {
    public static RenderingHints ANTI_ALIASED_RENDERING = null;
    public static RenderingHints DEFAULT_RENDERING = null;
    private boolean antiAliasing = false;
    private boolean wasAntiAliasing = false;
    private boolean wrapGuide;
    private Color wrapGuideColor;
    private int wrapGuideOffset;
    private boolean linesIntervalHighlight;
    private Color linesIntervalColor;
    private int linesInterval;
    protected JEditTextArea textArea;
    protected SyntaxStyle[] styles;
    protected Color caretColor;
    protected Color selectionColor;
    protected Color lineHighlightColor;
    protected Color highlightColor;
    protected Color bracketHighlightColor;
    protected Color eolMarkerColor;
    protected boolean blockCaret;
    protected boolean lineHighlight;
    protected boolean bracketHighlight;
    protected boolean paintInvalid;
    protected boolean eolMarkers;
    protected int cols;
    protected int rows;
    protected int tabSize;
    protected FontMetrics fm;
    protected TextAreaHighlight highlights;
    protected TextAreaHighlight firstPriorityHighlights;

    private static void initRenderingings() {
        if (ANTI_ALIASED_RENDERING == null) {
            ANTI_ALIASED_RENDERING = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ANTI_ALIASED_RENDERING.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            ANTI_ALIASED_RENDERING.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            ANTI_ALIASED_RENDERING.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }
        if (DEFAULT_RENDERING == null) {
            DEFAULT_RENDERING = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
    }

    public TextAreaPainter(JEditTextArea jEditTextArea, TextAreaDefaults textAreaDefaults) {
        TextAreaPainter.initRenderingings();
        this.textArea = jEditTextArea;
        this.setAutoscrolls(true);
        this.setDoubleBuffered(true);
        this.setOpaque(true);
        ToolTipManager.sharedInstance().registerComponent(this);
        this.setCursor(Cursor.getPredefinedCursor(2));
        this.setFont(new Font("Monospaced", 0, 12));
        this.setForeground(Color.black);
        this.setBackground(Color.white);
        this.blockCaret = textAreaDefaults.blockCaret;
        this.styles = textAreaDefaults.styles;
        this.cols = textAreaDefaults.cols;
        this.rows = textAreaDefaults.rows;
        this.caretColor = textAreaDefaults.caretColor;
        this.selectionColor = textAreaDefaults.selectionColor;
        this.lineHighlightColor = textAreaDefaults.lineHighlightColor;
        this.lineHighlight = textAreaDefaults.lineHighlight;
        this.bracketHighlightColor = textAreaDefaults.bracketHighlightColor;
        this.bracketHighlight = textAreaDefaults.bracketHighlight;
        this.paintInvalid = textAreaDefaults.paintInvalid;
        this.eolMarkerColor = textAreaDefaults.eolMarkerColor;
        this.eolMarkers = textAreaDefaults.eolMarkers;
        this.wrapGuide = textAreaDefaults.wrapGuide;
        this.wrapGuideColor = textAreaDefaults.wrapGuideColor;
        this.wrapGuideOffset = textAreaDefaults.wrapGuideOffset;
        this.linesIntervalHighlight = textAreaDefaults.linesIntervalHighlight;
        this.linesIntervalColor = textAreaDefaults.linesIntervalColor;
        this.linesInterval = textAreaDefaults.linesInterval;
    }

    public boolean isAntiAliasingEnabled() {
        return this.antiAliasing;
    }

    public void setAntiAliasingEnabled(boolean bl) {
        this.wasAntiAliasing = this.antiAliasing;
        this.antiAliasing = bl;
    }

    private void setAntiAliasing(Graphics graphics) {
        if (this.antiAliasing) {
            ((Graphics2D)graphics).setRenderingHints(ANTI_ALIASED_RENDERING);
        } else if (this.wasAntiAliasing != this.antiAliasing) {
            ((Graphics2D)graphics).setRenderingHints(DEFAULT_RENDERING);
        }
    }

    public void setWrapGuideEnabled(boolean bl) {
        this.wrapGuide = bl;
    }

    public void setWrapGuideOffset(int n) {
        this.wrapGuideOffset = n;
    }

    public void setWrapGuideColor(Color color) {
        this.wrapGuideColor = color;
    }

    public void setLinesIntervalHighlightEnabled(boolean bl) {
        this.linesIntervalHighlight = bl;
    }

    public void setLinesInterval(int n) {
        this.linesInterval = n;
    }

    public void setLinesIntervalHighlightColor(Color color) {
        this.linesIntervalColor = color;
    }

    public final boolean isManagingFocus() {
        return false;
    }

    public final SyntaxStyle[] getStyles() {
        return this.styles;
    }

    public final void setStyles(SyntaxStyle[] arrsyntaxStyle) {
        this.styles = arrsyntaxStyle;
        this.repaint();
    }

    public final Color getCaretColor() {
        return this.caretColor;
    }

    public final void setCaretColor(Color color) {
        this.caretColor = color;
        this.invalidateSelectedLines();
    }

    public final Color getSelectionColor() {
        return this.selectionColor;
    }

    public final void setSelectionColor(Color color) {
        this.selectionColor = color;
        this.invalidateSelectedLines();
    }

    public final Color getHighlightColor() {
        return this.lineHighlightColor;
    }

    public final void setHighlightColor(Color color) {
        this.highlightColor = color;
        this.repaint();
    }

    public final Color getLineHighlightColor() {
        return this.lineHighlightColor;
    }

    public final void setLineHighlightColor(Color color) {
        this.lineHighlightColor = color;
        this.invalidateSelectedLines();
    }

    public final boolean isLineHighlightEnabled() {
        return this.lineHighlight;
    }

    public final void setLineHighlightEnabled(boolean bl) {
        this.lineHighlight = bl;
        this.invalidateSelectedLines();
    }

    public final Color getBracketHighlightColor() {
        return this.bracketHighlightColor;
    }

    public final void setBracketHighlightColor(Color color) {
        this.bracketHighlightColor = color;
        this.invalidateLine(this.textArea.getBracketLine());
    }

    public final boolean isBracketHighlightEnabled() {
        return this.bracketHighlight;
    }

    public final void setBracketHighlightEnabled(boolean bl) {
        this.bracketHighlight = bl;
        this.invalidateLine(this.textArea.getBracketLine());
    }

    public final boolean isBlockCaretEnabled() {
        return this.blockCaret;
    }

    public final void setBlockCaretEnabled(boolean bl) {
        this.blockCaret = bl;
        this.invalidateSelectedLines();
    }

    public final Color getEOLMarkerColor() {
        return this.eolMarkerColor;
    }

    public final void setEOLMarkerColor(Color color) {
        this.eolMarkerColor = color;
        this.repaint();
    }

    public final boolean getEOLMarkersPainted() {
        return this.eolMarkers;
    }

    public final void setEOLMarkersPainted(boolean bl) {
        this.eolMarkers = bl;
        this.repaint();
    }

    public boolean getInvalidLinesPainted() {
        return this.paintInvalid;
    }

    public void setInvalidLinesPainted(boolean bl) {
        this.paintInvalid = bl;
    }

    public void addCustomHighlight(TextAreaHighlight textAreaHighlight) {
        textAreaHighlight.init(this.textArea, this.highlights);
        this.highlights = textAreaHighlight;
    }

    public void addCustomFirstPriorityHighlight(TextAreaHighlight textAreaHighlight) {
        textAreaHighlight.init(this.textArea, this.firstPriorityHighlights);
        this.firstPriorityHighlights = textAreaHighlight;
    }

    public String getToolTipText(MouseEvent mouseEvent) {
        if (this.highlights != null) {
            return this.highlights.getToolTipText(mouseEvent);
        }
        return null;
    }

    public FontMetrics getFontMetrics() {
        return this.fm;
    }

    public void setFont(Font font) {
        super.setFont(font);
        this.fm = Toolkit.getDefaultToolkit().getFontMetrics(font);
        this.textArea.recalculateVisibleLines();
    }

    public void paint(Graphics graphics) {
        this.tabSize = this.fm.charWidth(' ') * (Integer)this.textArea.getDocument().getProperty("tabSize");
        this.tabSize = 20;
        Rectangle rectangle = graphics.getClipBounds();
        graphics.setColor(this.getBackground());
        graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        int n = this.fm.getHeight();
        int n2 = this.textArea.getFirstLine();
        int n3 = n2 + rectangle.y / n;
        int n4 = n2 + (rectangle.y + rectangle.height - 1) / n;
        int n5 = this.textArea.getHorizontalOffset();
        int n6 = this.textArea.getLineCount();
        try {
            int n7;
            TokenMarker tokenMarker = this.textArea.getDocument().getTokenMarker();
            int n8 = this.textArea.maxHorizontalScrollWidth;
            boolean bl = false;
            for (n7 = n3; n7 <= n4; ++n7) {
                boolean bl2 = n7 >= 0 && n7 < n6;
                int n9 = this.paintLine(graphics, tokenMarker, bl2, n7, n5) - n5 + 5;
                if (!bl2) continue;
                tokenMarker.setLineWidth(n7, n9);
                if (n9 <= n8) continue;
                bl = true;
            }
            if (tokenMarker.isNextLineRequested()) {
                n7 = rectangle.y + rectangle.height;
                this.repaint(0, n7, this.getWidth(), this.getHeight() - n7);
            }
            if (bl) {
                this.textArea.updateMaxHorizontalScrollWidth();
            }
        }
        catch (Exception var9_10) {
            System.err.println("Error repainting line range {" + n3 + "," + n4 + "}:");
            var9_10.printStackTrace();
        }
    }

    public final void invalidateLine(int n) {
        this.repaint(0, this.textArea.lineToY(n) + this.fm.getMaxDescent() + this.fm.getLeading(), this.getWidth(), this.fm.getHeight());
    }

    public final void invalidateLineRange(int n, int n2) {
        this.repaint(0, this.textArea.lineToY(n) + this.fm.getMaxDescent() + this.fm.getLeading(), this.getWidth(), (n2 - n + 1) * this.fm.getHeight());
    }

    public final void invalidateSelectedLines() {
        this.invalidateLineRange(this.textArea.getSelectionStartLine(), this.textArea.getSelectionEndLine());
    }

    public float nextTabStop(float f, int n) {
        if (this.tabSize == 0) {
            this.tabSize = this.fm.charWidth(' ') * (Integer)this.textArea.getDocument().getProperty("tabSize");
            this.tabSize = 20;
        }
        int n2 = this.textArea.getHorizontalOffset();
        int n3 = ((int)f - n2) / this.tabSize;
        return (n3 + 1) * this.tabSize + n2;
    }

    public Dimension getPreferredSize() {
        Dimension dimension = new Dimension();
        dimension.width = this.fm.charWidth('w') * this.cols;
        dimension.height = this.fm.getHeight() * this.rows;
        return dimension;
    }

    public Dimension getMinimumSize() {
        return this.getPreferredSize();
    }

    protected int paintLine(Graphics graphics, TokenMarker tokenMarker, boolean bl, int n, int n2) {
        Font font = this.getFont();
        Color color = this.getForeground();
        int n3 = this.textArea.lineToY(n);
        if (!bl) {
            if (this.paintInvalid) {
                this.paintHighlight(graphics, n, n3);
                this.styles[10].setGraphicsFlags(graphics, font);
                graphics.drawString("~", 0, n3 + this.fm.getHeight());
            }
        } else {
            n2 = this.paintSyntaxLine(graphics, tokenMarker, n, font, color, n2, n3);
        }
        return n2;
    }

    protected int paintSyntaxLine(Graphics graphics, TokenMarker tokenMarker, int n, Font font, Color color, int n2, int n3) {
        Token token;
        if (this.firstPriorityHighlights != null) {
            this.firstPriorityHighlights.paintHighlight(graphics, n, n3);
        }
        Segment segment = this.textArea.lineSegment;
        this.textArea.getLineText(n, this.textArea.lineSegment);
        Token token2 = token = tokenMarker.markTokens(this.textArea.lineSegment, n);
        int n4 = n3 + this.fm.getLeading() + this.fm.getMaxDescent();
        int n5 = this.fm.getHeight();
        int n6 = this.fm.charWidth('w');
        int n7 = n2;
        int n8 = 0;
        int n9 = 0;
        String string = this.textArea.lineSegment.toString();
        JextTextArea jextTextArea = (JextTextArea)this.textArea;
        while (token.id != 127) {
            n8 = n6 * Utilities.getRealLength(string.substring(n9, n9 + token.length), jextTextArea.getTabSize());
            n9+=token.length;
            if (token.highlightBackground) {
                graphics.setColor(this.highlightColor);
                graphics.fillRect(n7, n4, n8, n5);
            }
            n7+=n8;
            token = token.next;
        }
        this.textArea.lineSegment = segment;
        this.paintHighlight(graphics, n, n3);
        this.textArea.getLineText(n, this.textArea.lineSegment);
        graphics.setFont(font);
        graphics.setColor(color);
        n2 = SyntaxUtilities.paintSyntaxLine(this.textArea.lineSegment, token2, this.styles, this, graphics, n2, n3+=this.fm.getHeight());
        if (this.eolMarkers) {
            graphics.setColor(this.eolMarkerColor);
            graphics.drawString(".", n2, n3);
        }
        return n2;
    }

    protected void paintHighlight(Graphics graphics, int n, int n2) {
        if (n >= this.textArea.getSelectionStartLine() && n <= this.textArea.getSelectionEndLine()) {
            this.paintLineHighlight(graphics, n, n2);
        }
        if (this.linesIntervalHighlight && this.linesInterval > 0) {
            this.paintLinesInterval(graphics, n, n2);
        }
        if (this.wrapGuide && this.wrapGuideOffset > 0) {
            this.paintWrapGuide(graphics, n, n2);
        }
        if (this.highlights != null) {
            this.highlights.paintHighlight(graphics, n, n2);
        }
        if (this.bracketHighlight && n == this.textArea.getBracketLine()) {
            this.paintBracketHighlight(graphics, n, n2);
        }
        if (n == this.textArea.getCaretLine()) {
            this.paintCaret(graphics, n, n2);
        }
        if (n == this.textArea.getShadowCaretLine()) {
            this.paintShadowCaret(graphics, n, n2);
        }
    }

    protected void paintWrapGuide(Graphics graphics, int n, int n2) {
        graphics.setColor(this.wrapGuideColor);
        int n3 = n2 + (this.fm.getLeading() + this.fm.getMaxDescent());
        int n4 = n > 0 ? n3 : 0;
        int n5 = n != this.textArea.getLineCount() - 1 ? n3 + this.fm.getHeight() : this.textArea.getHeight();
        int n6 = this.fm.charWidth('m');
        int n7 = this.textArea.getHorizontalOffset();
        int n8 = this.textArea.getWidth();
        int n9 = this.wrapGuideOffset * n6 + n7;
        if (n9 >= 0 && n9 < n8) {
            graphics.drawLine(n9, n4, n9, n5);
        }
    }

    protected void paintLinesInterval(Graphics graphics, int n, int n2) {
        if ((n + 1) % this.linesInterval == 0) {
            int n3;
            int n4;
            int n5 = this.fm.getHeight();
            int n6 = n2 + this.fm.getLeading() + this.fm.getMaxDescent();
            int n7 = this.textArea.getSelectionStart();
            int n8 = this.textArea.getSelectionEnd();
            graphics.setColor(this.linesIntervalColor);
            graphics.fillRect(0, n6, this.getWidth(), n5);
            graphics.setColor(this.selectionColor);
            int n9 = this.textArea.getSelectionStartLine();
            int n10 = this.textArea.getSelectionEndLine();
            int n11 = this.textArea.getLineStartOffset(n);
            if (this.textArea.isSelectionRectangular()) {
                int n12 = this.textArea.getLineLength(n);
                n4 = this.textArea.offsetToX(n, Math.min(n12, n7 - this.textArea.getLineStartOffset(n9)));
                if (n4 == (n3 = this.textArea.offsetToX(n, Math.min(n12, n8 - this.textArea.getLineStartOffset(n10))))) {
                    ++n3;
                }
            } else if (n9 == n10) {
                n4 = this.textArea.offsetToX(n, n7 - n11);
                n3 = this.textArea.offsetToX(n, n8 - n11);
            } else if (n == n9) {
                n4 = this.textArea.offsetToX(n, n7 - n11);
                n3 = this.textArea.offsetToX(n, this.textArea.getLineLength(n));
            } else if (n == n10) {
                n4 = 0;
                n3 = this.textArea.offsetToX(n, n8 - n11);
            } else {
                n4 = 0;
                n3 = this.textArea.offsetToX(n, this.textArea.getLineLength(n));
            }
            graphics.fillRect(n4 > n3 ? n3 : n4, n6, n4 > n3 ? n4 - n3 : n3 - n4, n5);
        }
    }

    protected void paintLineHighlight(Graphics graphics, int n, int n2) {
        int n3;
        int n4 = this.fm.getHeight();
        n2+=this.fm.getLeading() + this.fm.getMaxDescent();
        int n5 = this.textArea.getSelectionStart();
        if (n5 == (n3 = this.textArea.getSelectionEnd())) {
            if (this.lineHighlight) {
                graphics.setColor(this.lineHighlightColor);
                graphics.fillRect(0, n2, this.getWidth(), n4);
            }
        } else {
            int n6;
            int n7;
            int n8;
            graphics.setColor(this.selectionColor);
            int n9 = this.textArea.getSelectionStartLine();
            int n10 = this.textArea.getSelectionEndLine();
            int n11 = this.textArea.getLineStartOffset(n);
            if (this.textArea.isSelectionRectangular()) {
                n6 = this.textArea.getLineLength(n);
                n8 = this.textArea.offsetToX(n, Math.min(n6, n5 - this.textArea.getLineStartOffset(n9)));
                if (n8 == (n7 = this.textArea.offsetToX(n, Math.min(n6, n3 - this.textArea.getLineStartOffset(n10))))) {
                    ++n7;
                }
            } else if (n9 == n10) {
                n8 = this.textArea.offsetToX(n, n5 - n11);
                n7 = this.textArea.offsetToX(n, n3 - n11);
            } else if (n == n9) {
                n8 = this.textArea.offsetToX(n, n5 - n11);
                n7 = this.textArea.offsetToX(n, this.textArea.getLineLength(n));
            } else if (n == n10) {
                n8 = 0;
                n7 = this.textArea.offsetToX(n, n3 - n11);
            } else {
                n8 = 0;
                n7 = this.textArea.offsetToX(n, this.textArea.getLineLength(n));
            }
            int n12 = n6 = n8 > n7 ? n8 - n7 : n7 - n8;
            if (n6 == 0) {
                n6 = 4;
            }
            graphics.fillRect(n8 > n7 ? n7 : n8, n2, n6, n4);
        }
    }

    protected void paintBracketHighlight(Graphics graphics, int n, int n2) {
        int n3 = this.textArea.getBracketPosition();
        if (n3 == -1) {
            return;
        }
        int n4 = this.textArea.offsetToX(n, n3);
        graphics.setColor(this.bracketHighlightColor);
        graphics.fillRect(n4, n2+=this.fm.getLeading() + this.fm.getMaxDescent(), this.fm.charWidth('(') - 1, this.fm.getHeight() - 1);
    }

    protected void paintCaret(Graphics graphics, int n, int n2) {
        if (this.textArea.isCaretVisible()) {
            int n3 = this.textArea.getCaretPosition() - this.textArea.getLineStartOffset(n);
            int n4 = this.textArea.offsetToX(n, n3);
            int n5 = this.blockCaret || this.textArea.isOverwriteEnabled() ? this.fm.charWidth('w') : 1;
            n2+=this.fm.getLeading() + this.fm.getMaxDescent();
            int n6 = this.fm.getHeight();
            graphics.setColor(this.caretColor);
            if (this.textArea.isOverwriteEnabled()) {
                graphics.fillRect(n4, n2 + n6 - 1, n5, 1);
            } else if (n5 > 1) {
                graphics.drawRect(n4, n2, n5 - 1, n6 - 1);
            } else {
                graphics.drawLine(n4, n2, n4, n2 + n6 - 1);
            }
        }
    }

    protected void paintShadowCaret(Graphics graphics, int n, int n2) {
        int n3 = this.textArea.getShadowCaretPosition() - this.textArea.getLineStartOffset(n);
        int n4 = this.textArea.offsetToX(n, n3);
        n2+=this.fm.getLeading() + this.fm.getMaxDescent();
        int n5 = this.fm.getHeight();
        graphics.setColor(this.caretColor.darker());
        for (int i = 0; i < n5; i+=3) {
            graphics.drawLine(n4, n2 + i, n4, n2 + i + 1);
        }
    }
}

