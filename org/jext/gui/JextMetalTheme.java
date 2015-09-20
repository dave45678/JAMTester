/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

public class JextMetalTheme
extends DefaultMetalTheme {
    private ColorUIResource color = new ColorUIResource(0, 0, 0);
    private FontUIResource font = new FontUIResource("Dialog", 0, 11);

    public ColorUIResource getControlTextColor() {
        return this.color;
    }

    public ColorUIResource getMenuTextColor() {
        return this.color;
    }

    public ColorUIResource getSystemTextColor() {
        return this.color;
    }

    public ColorUIResource getUserTextColor() {
        return this.color;
    }

    public FontUIResource getControlTextFont() {
        return this.font;
    }

    public FontUIResource getMenuTextFont() {
        return this.font;
    }

    public FontUIResource getSystemTextFont() {
        return this.font;
    }

    public FontUIResource getUserTextFont() {
        return this.font;
    }

    public FontUIResource getWindowTitleFont() {
        return this.font;
    }
}

