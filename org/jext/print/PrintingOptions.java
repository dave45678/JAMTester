/*
 * Decompiled with CFR 0_102.
 */
package org.jext.print;

import java.awt.Font;
import java.awt.print.PageFormat;

public class PrintingOptions {
    private boolean printLineNumbers_ = false;
    private boolean wrapText_ = false;
    private boolean printHeader_ = false;
    private boolean printSyntax_ = false;
    private Font pageFont_ = new Font("Courier", 0, 10);
    private Font headerFont_ = new Font("Courier", 1, 10);
    private PageFormat pageFormat_ = null;

    public PrintingOptions() {
        this.pageFormat_ = new PageFormat();
    }

    public PrintingOptions(boolean bl, boolean bl2, boolean bl3, boolean bl4, Font font, PageFormat pageFormat) {
        this.printLineNumbers_ = bl;
        this.wrapText_ = bl2;
        this.printHeader_ = bl3;
        this.printSyntax_ = bl4;
        this.pageFont_ = font;
        this.pageFormat_ = pageFormat;
        this.headerFont_ = new Font(this.pageFont_.getName(), 1, this.pageFont_.getSize());
    }

    public void setPrintLineNumbers(boolean bl) {
        this.printLineNumbers_ = bl;
    }

    public boolean getPrintLineNumbers() {
        return this.printLineNumbers_;
    }

    public void setWrapText(boolean bl) {
        this.wrapText_ = bl;
    }

    public boolean getWrapText() {
        return this.wrapText_;
    }

    public void setPrintHeader(boolean bl) {
        this.printHeader_ = bl;
    }

    public boolean getPrintHeader() {
        return this.printHeader_;
    }

    public void setPrintSyntax(boolean bl) {
        this.printSyntax_ = bl;
    }

    public boolean getPrintSyntax() {
        return this.printSyntax_;
    }

    public void setPageFont(Font font) {
        this.pageFont_ = font;
        this.headerFont_ = new Font(this.pageFont_.getName(), 1, this.pageFont_.getSize());
    }

    public Font getPageFont() {
        return this.pageFont_;
    }

    public Font getHeaderFont() {
        return this.headerFont_;
    }

    public void setPageFormat(PageFormat pageFormat) {
        this.pageFormat_ = pageFormat;
    }

    public PageFormat getPageFormat() {
        return this.pageFormat_;
    }
}

