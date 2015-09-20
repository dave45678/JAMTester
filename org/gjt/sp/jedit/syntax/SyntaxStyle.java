/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;

public class SyntaxStyle {
    private Color color;
    private boolean italic;
    private boolean bold;
    private Font lastFont;
    private Font lastStyledFont;
    private FontMetrics fontMetrics;

    public SyntaxStyle(Color color, boolean bl, boolean bl2) {
        this.color = color;
        this.italic = bl;
        this.bold = bl2;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean isPlain() {
        return !this.bold && !this.italic;
    }

    public boolean isItalic() {
        return this.italic;
    }

    public boolean isBold() {
        return this.bold;
    }

    public Font getStyledFont(Font font) {
        if (font == null) {
            throw new NullPointerException("font param must not be null");
        }
        if (font.equals(this.lastFont)) {
            return this.lastStyledFont;
        }
        this.lastFont = font;
        this.lastStyledFont = new Font(font.getFamily(), (this.bold ? 1 : 0) | (this.italic ? 2 : 0), font.getSize());
        return this.lastStyledFont;
    }

    public FontMetrics getFontMetrics(Font font) {
        if (font == null) {
            throw new NullPointerException("font param must not be null");
        }
        if (font.equals(this.lastFont) && this.fontMetrics != null) {
            return this.fontMetrics;
        }
        this.lastFont = font;
        this.lastStyledFont = new Font(font.getFamily(), (this.bold ? 1 : 0) | (this.italic ? 2 : 0), font.getSize());
        this.fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(this.lastStyledFont);
        return this.fontMetrics;
    }

    public void setGraphicsFlags(Graphics graphics, Font font) {
        Font font2 = this.getStyledFont(font);
        graphics.setFont(font2);
        graphics.setColor(this.color);
    }

    public String toString() {
        return this.getClass().getName() + "[color=" + this.color + (this.italic ? ",italic" : "") + (this.bold ? ",bold" : "") + "]";
    }
}

