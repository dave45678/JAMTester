/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.textarea;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import org.gjt.sp.jedit.textarea.Gutter;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.textarea.TextAreaDefaults;
import org.gjt.sp.jedit.textarea.TextAreaHighlight;
import org.gjt.sp.jedit.textarea.TextAreaPainter;
import org.jext.JextTextArea;

public class Gutter
extends JComponent
implements SwingConstants {
    private boolean antiAliasing = false;
    private boolean wasAntiAliasing = false;
    private JEditTextArea textArea;
    private JPopupMenu context;
    private TextAreaHighlight highlights;
    private int baseline = 0;
    private int ileft = 0;
    private Dimension gutterSize = new Dimension(0, 0);
    private Dimension collapsedSize = new Dimension(0, 0);
    private Color intervalHighlight;
    private Color caretMark;
    private Color anchorMark;
    private Color selectionMark;
    private FontMetrics fm;
    private int alignment;
    private int interval = 0;
    private boolean lineNumberingEnabled = true;
    private boolean collapsed = false;

    public Gutter(JEditTextArea jEditTextArea, TextAreaDefaults textAreaDefaults) {
        this.textArea = jEditTextArea;
        this.setBackground(textAreaDefaults.gutterBgColor);
        this.setForeground(textAreaDefaults.gutterFgColor);
        this.setHighlightedForeground(textAreaDefaults.gutterHighlightColor);
        this.setCaretMark(textAreaDefaults.caretMarkColor);
        this.setAnchorMark(textAreaDefaults.anchorMarkColor);
        this.setSelectionMark(textAreaDefaults.selectionMarkColor);
        this.setFont(textAreaDefaults.gutterFont);
        this.setBorder(textAreaDefaults.gutterBorderWidth, textAreaDefaults.gutterBorderColor);
        this.setLineNumberAlignment(textAreaDefaults.gutterNumberAlignment);
        this.setGutterWidth(textAreaDefaults.gutterWidth);
        this.setCollapsed(textAreaDefaults.gutterCollapsed);
        GutterMouseListener gutterMouseListener = new GutterMouseListener();
        this.addMouseListener(gutterMouseListener);
        this.addMouseMotionListener(gutterMouseListener);
    }

    public void setAntiAliasingEnabled(boolean bl) {
        this.wasAntiAliasing = this.antiAliasing;
        this.antiAliasing = bl;
    }

    private void setAntiAliasing(Graphics graphics) {
        if (this.antiAliasing) {
            ((Graphics2D)graphics).setRenderingHints(TextAreaPainter.ANTI_ALIASED_RENDERING);
        } else if (this.wasAntiAliasing != this.antiAliasing) {
            ((Graphics2D)graphics).setRenderingHints(TextAreaPainter.DEFAULT_RENDERING);
        }
    }

    public void paintComponent(Graphics graphics) {
        if (!this.collapsed) {
            this.setAntiAliasing(graphics);
            Rectangle rectangle = graphics.getClipBounds();
            graphics.setColor(this.getBackground());
            graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            if (this.highlights != null) {
                this.paintCustomHighlights(graphics);
            }
            if (this.lineNumberingEnabled) {
                this.paintLineNumbers(graphics);
            }
        }
    }

    protected void paintLineNumbers(Graphics graphics) {
        FontMetrics fontMetrics = this.textArea.getPainter().getFontMetrics();
        int n = fontMetrics.getHeight();
        int n2 = (int)Math.round((double)(this.baseline + n - fontMetrics.getMaxDescent()) / 2.0);
        int n3 = this.textArea.getFirstLine() + 1;
        int n4 = n3 + this.getHeight() / n;
        int n5 = Math.max(1, n3);
        int n6 = Math.min(this.textArea.getLineCount(), n4);
        graphics.setFont(this.getFont());
        graphics.setColor(this.getForeground());
        int n7 = n3;
        while (n7 <= n4) {
            if (n7 >= n5) {
                if (n7 <= n6) {
                    int n8;
                    int n9;
                    String string = Integer.toString(n7);
                    switch (this.alignment) {
                        case 4: {
                            n9 = this.gutterSize.width - this.collapsedSize.width - (this.fm.stringWidth(string) + 1);
                            break;
                        }
                        case 0: {
                            n9 = (this.gutterSize.width - this.collapsedSize.width - this.fm.stringWidth(string)) / 2;
                            break;
                        }
                        default: {
                            n9 = 1;
                        }
                    }
                    if (this.interval > 1 && n7 % this.interval == 0) {
                        graphics.setColor(this.getHighlightedForeground());
                        graphics.drawString(string, this.ileft + n9, n2);
                        graphics.setColor(this.getForeground());
                    } else {
                        graphics.drawString(string, this.ileft + n9, n2);
                    }
                    if (n7 == this.textArea.getCaretLine() + 1) {
                        graphics.setColor(this.caretMark);
                        graphics.drawRect(this.ileft + n9 - 8, n2 - 6, 4, 4);
                    }
                    if ((n8 = ((JextTextArea)this.textArea).getAnchorOffset()) != -1 && n7 == this.textArea.getLineOfOffset(n8) + 1) {
                        graphics.setColor(this.anchorMark);
                        graphics.drawRect(this.ileft + n9 - 8, n2 - 6, 4, 4);
                    }
                    if (this.textArea.getSelectionStart() == this.textArea.getSelectionEnd()) {
                        graphics.setColor(this.getForeground());
                    } else {
                        if (n7 >= this.textArea.getSelectionStartLine() + 1 && n7 <= this.textArea.getSelectionEndLine() + 1) {
                            graphics.setColor(this.selectionMark);
                            graphics.fillRect(this.ileft + n9 - 7, n2 - 5, 3, 3);
                        }
                        graphics.setColor(this.getForeground());
                    }
                }
            }
            ++n7;
            n2+=n;
        }
    }

    protected void paintCustomHighlights(Graphics graphics) {
        int n = this.textArea.getPainter().getFontMetrics().getHeight();
        int n2 = this.textArea.getFirstLine();
        int n3 = n2 + this.getHeight() / n;
        int n4 = 0;
        int n5 = n2;
        while (n5 < n3) {
            this.highlights.paintHighlight(graphics, n5, n4);
            ++n5;
            n4+=n;
        }
    }

    public void addCustomHighlight(TextAreaHighlight textAreaHighlight) {
        textAreaHighlight.init(this.textArea, this.highlights);
        this.highlights = textAreaHighlight;
    }

    public void setBorder(int n, Color color) {
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, n, color));
    }

    public void setBorder(Border border) {
        super.setBorder(border);
        if (border == null) {
            this.ileft = 0;
            this.collapsedSize.width = 0;
            this.collapsedSize.height = 0;
        } else {
            Insets insets = border.getBorderInsets(this);
            this.ileft = insets.left;
            this.collapsedSize.width = insets.left + insets.right;
            this.collapsedSize.height = insets.top + insets.bottom;
        }
    }

    public void setFont(Font font) {
        super.setFont(font);
        this.fm = this.getFontMetrics(font);
        this.baseline = this.fm.getHeight() - this.fm.getMaxDescent();
    }

    public void setHighlightedForeground(Color color) {
        this.intervalHighlight = color;
    }

    public Color getHighlightedForeground() {
        return this.intervalHighlight;
    }

    public void setCaretMark(Color color) {
        this.caretMark = color;
    }

    public void setAnchorMark(Color color) {
        this.anchorMark = color;
    }

    public void setSelectionMark(Color color) {
        this.selectionMark = color;
    }

    public void setGutterWidth(int n) {
        if (n < this.collapsedSize.width) {
            n = this.collapsedSize.width;
        }
        this.gutterSize.width = n;
        if (!this.collapsed) {
            this.textArea.revalidate();
        }
    }

    public int getGutterWidth() {
        return this.gutterSize.width;
    }

    public Dimension getPreferredSize() {
        if (this.collapsed) {
            return this.collapsedSize;
        }
        return this.gutterSize;
    }

    public Dimension getMinimumSize() {
        return this.getPreferredSize();
    }

    public String getToolTipText(MouseEvent mouseEvent) {
        return this.highlights == null ? null : this.highlights.getToolTipText(mouseEvent);
    }

    public boolean isLineNumberingEnabled() {
        return this.lineNumberingEnabled;
    }

    public void setLineNumberingEnabled(boolean bl) {
        if (this.lineNumberingEnabled == bl) {
            return;
        }
        this.lineNumberingEnabled = bl;
        this.repaint();
    }

    public int getLineNumberAlignment() {
        return this.alignment;
    }

    public void setLineNumberAlignment(int n) {
        if (this.alignment == n) {
            return;
        }
        this.alignment = n;
        this.repaint();
    }

    public boolean isCollapsed() {
        return this.collapsed;
    }

    public void setCollapsed(boolean bl) {
        if (this.collapsed == bl) {
            return;
        }
        this.collapsed = bl;
        this.textArea.revalidate();
    }

    public void toggleCollapsed() {
        this.setCollapsed(!this.collapsed);
    }

    public int getHighlightInterval() {
        return this.interval;
    }

    public void setHighlightInterval(int n) {
        if (n <= 1) {
            n = 0;
        }
        this.interval = n;
        this.repaint();
    }

    public JPopupMenu getContextMenu() {
        return this.context;
    }

    public void setContextMenu(JPopupMenu jPopupMenu) {
        this.context = jPopupMenu;
    }

    static /* synthetic */ Dimension access$200(Gutter gutter) {
        return gutter.gutterSize;
    }

    static /* synthetic */ Dimension access$300(Gutter gutter) {
        return gutter.collapsedSize;
    }

    class GutterMouseListener
    extends MouseAdapter
    implements MouseMotionListener {
        private Point dragStart;
        private int startWidth;

        GutterMouseListener() {
            this.dragStart = null;
            this.startWidth = 0;
        }

        public void mouseClicked(MouseEvent mouseEvent) {
            int n = mouseEvent.getClickCount();
            if (n == 1) {
                if (Gutter.this.context == null || Gutter.this.context.isVisible()) {
                    return;
                }
                if ((mouseEvent.getModifiers() & 4) != 0) {
                    Gutter.this.textArea.requestFocus();
                    Gutter.this.context.show(Gutter.this, mouseEvent.getX(), mouseEvent.getY());
                }
            } else if (n >= 2) {
                Gutter.this.toggleCollapsed();
            }
        }

        public void mousePressed(MouseEvent mouseEvent) {
            this.dragStart = mouseEvent.getPoint();
            this.startWidth = Gutter.access$200((Gutter)Gutter.this).width;
        }

        public void mouseDragged(MouseEvent mouseEvent) {
            if (this.dragStart == null) {
                return;
            }
            if (Gutter.this.isCollapsed()) {
                Gutter.this.setCollapsed(false);
            }
            Point point = mouseEvent.getPoint();
            Gutter.access$200((Gutter)Gutter.this).width = this.startWidth + point.x - this.dragStart.x;
            if (Gutter.access$200((Gutter)Gutter.this).width < Gutter.access$300((Gutter)Gutter.this).width) {
                Gutter.access$200((Gutter)Gutter.this).width = this.startWidth;
                Gutter.this.setCollapsed(true);
            }
            SwingUtilities.invokeLater(new Runnable(this){
                private final /* synthetic */ GutterMouseListener this$1;

                public void run() {
                    Gutter.access$100(GutterMouseListener.access$400(this.this$1)).revalidate();
                }
            });
        }

        public void mouseExited(MouseEvent mouseEvent) {
            if (this.dragStart != null && this.dragStart.x > mouseEvent.getPoint().x) {
                Gutter.this.setCollapsed(true);
                Gutter.access$200((Gutter)Gutter.this).width = this.startWidth;
                SwingUtilities.invokeLater(new Runnable(this){
                    private final /* synthetic */ GutterMouseListener this$1;

                    public void run() {
                        Gutter.access$100(GutterMouseListener.access$400(this.this$1)).revalidate();
                    }
                });
            }
        }

        public void mouseMoved(MouseEvent mouseEvent) {
        }

        public void mouseReleased(MouseEvent mouseEvent) {
            this.dragStart = null;
        }

        static /* synthetic */ Gutter access$400(GutterMouseListener gutterMouseListener) {
            return gutterMouseListener.Gutter.this;
        }
    }

}

