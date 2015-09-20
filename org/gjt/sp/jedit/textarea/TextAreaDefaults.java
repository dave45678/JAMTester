/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.textarea;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JPopupMenu;
import org.gjt.sp.jedit.syntax.SyntaxStyle;
import org.gjt.sp.jedit.syntax.SyntaxUtilities;
import org.gjt.sp.jedit.textarea.InputHandler;

public class TextAreaDefaults {
    private static TextAreaDefaults DEFAULTS;
    public InputHandler inputHandler;
    public boolean editable;
    public boolean caretVisible;
    public boolean caretBlinks;
    public boolean blockCaret;
    public int electricScroll;
    public boolean gutterCollapsed;
    public int gutterWidth;
    public Color gutterBgColor;
    public Color gutterFgColor;
    public Color gutterHighlightColor;
    public Color gutterBorderColor;
    public Color caretMarkColor;
    public Color anchorMarkColor;
    public Color selectionMarkColor;
    public int gutterBorderWidth;
    public int gutterNumberAlignment;
    public Font gutterFont;
    public int cols;
    public int rows;
    public SyntaxStyle[] styles;
    public Color caretColor;
    public Color selectionColor;
    public Color lineHighlightColor;
    public boolean lineHighlight;
    public Color bracketHighlightColor;
    public boolean bracketHighlight;
    public Color eolMarkerColor;
    public boolean eolMarkers;
    public boolean paintInvalid;
    public boolean wrapGuide;
    public Color wrapGuideColor;
    public int wrapGuideOffset;
    public boolean linesIntervalHighlight;
    public Color linesIntervalColor;
    public int linesInterval;
    public boolean antiAliasing;
    public JPopupMenu popup;

    public static TextAreaDefaults getDefaults() {
        if (DEFAULTS == null) {
            DEFAULTS = new TextAreaDefaults();
            TextAreaDefaults.DEFAULTS.editable = true;
            TextAreaDefaults.DEFAULTS.caretVisible = true;
            TextAreaDefaults.DEFAULTS.caretBlinks = true;
            TextAreaDefaults.DEFAULTS.electricScroll = 3;
            TextAreaDefaults.DEFAULTS.gutterCollapsed = true;
            TextAreaDefaults.DEFAULTS.gutterWidth = 40;
            TextAreaDefaults.DEFAULTS.gutterBgColor = Color.white;
            TextAreaDefaults.DEFAULTS.gutterFgColor = Color.black;
            TextAreaDefaults.DEFAULTS.gutterHighlightColor = new Color(8421568);
            TextAreaDefaults.DEFAULTS.gutterBorderColor = Color.gray;
            TextAreaDefaults.DEFAULTS.caretMarkColor = Color.green;
            TextAreaDefaults.DEFAULTS.anchorMarkColor = Color.red;
            TextAreaDefaults.DEFAULTS.selectionMarkColor = Color.blue;
            TextAreaDefaults.DEFAULTS.gutterBorderWidth = 4;
            TextAreaDefaults.DEFAULTS.gutterNumberAlignment = 4;
            TextAreaDefaults.DEFAULTS.gutterFont = new Font("monospaced", 0, 10);
            TextAreaDefaults.DEFAULTS.cols = 80;
            TextAreaDefaults.DEFAULTS.rows = 25;
            TextAreaDefaults.DEFAULTS.styles = SyntaxUtilities.getDefaultSyntaxStyles();
            TextAreaDefaults.DEFAULTS.caretColor = Color.red;
            TextAreaDefaults.DEFAULTS.selectionColor = new Color(13421823);
            TextAreaDefaults.DEFAULTS.lineHighlightColor = new Color(14737632);
            TextAreaDefaults.DEFAULTS.lineHighlight = true;
            TextAreaDefaults.DEFAULTS.bracketHighlightColor = Color.black;
            TextAreaDefaults.DEFAULTS.bracketHighlight = true;
            TextAreaDefaults.DEFAULTS.eolMarkerColor = new Color(39321);
            TextAreaDefaults.DEFAULTS.eolMarkers = true;
            TextAreaDefaults.DEFAULTS.paintInvalid = true;
            TextAreaDefaults.DEFAULTS.linesIntervalColor = new Color(15132415);
            TextAreaDefaults.DEFAULTS.linesIntervalHighlight = false;
            TextAreaDefaults.DEFAULTS.linesInterval = 5;
            TextAreaDefaults.DEFAULTS.wrapGuideColor = Color.red;
            TextAreaDefaults.DEFAULTS.wrapGuide = false;
            TextAreaDefaults.DEFAULTS.wrapGuideOffset = 80;
        }
        return DEFAULTS;
    }
}

