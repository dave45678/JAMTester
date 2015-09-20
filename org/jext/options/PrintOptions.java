/*
 * Decompiled with CFR 0_102.
 */
package org.jext.options;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import org.jext.Jext;
import org.jext.gui.AbstractOptionPane;
import org.jext.gui.FontSelector;
import org.jext.gui.JextCheckBox;
import org.jext.gui.JextHighlightButton;

public class PrintOptions
extends AbstractOptionPane
implements ActionListener {
    private FontSelector fonts = new FontSelector("print");
    private JextHighlightButton pageLayout;
    private PageFormat pgfmt = new PageFormat();
    private JextCheckBox lineNumbers;
    private JextCheckBox wrap;
    private JextCheckBox syntax;
    private JextCheckBox header;
    private JextCheckBox footer;

    public PrintOptions() {
        super("print");
        this.addComponent(Jext.getProperty("options.fonts.label"), this.fonts);
        this.lineNumbers = new JextCheckBox(Jext.getProperty("print.printLineNumbers.label"));
        this.addComponent(this.lineNumbers);
        this.wrap = new JextCheckBox(Jext.getProperty("print.wrapText.label"));
        this.addComponent(this.wrap);
        this.header = new JextCheckBox(Jext.getProperty("print.printHeader.label"));
        this.addComponent(this.header);
        this.footer = new JextCheckBox(Jext.getProperty("print.printFooter.label"));
        this.addComponent(this.footer);
        this.syntax = new JextCheckBox(Jext.getProperty("print.printSyntax.label"));
        this.addComponent(this.syntax);
        this.syntax.addActionListener(this);
        this.pageLayout = new JextHighlightButton(Jext.getProperty("print.pageLayout.label"));
        this.pageLayout.addActionListener(this);
        this.add(this.pageLayout);
        this.load();
    }

    public void load() {
        this.fonts.load();
        this.lineNumbers.setSelected(Jext.getBooleanProperty("print.lineNumbers"));
        this.wrap.setSelected(Jext.getBooleanProperty("print.wrapText"));
        this.header.setSelected(Jext.getBooleanProperty("print.header"));
        this.footer.setSelected(Jext.getBooleanProperty("print.footer"));
        this.syntax.setSelected(Jext.getBooleanProperty("print.syntax"));
        Paper paper = this.pgfmt.getPaper();
        this.pgfmt.setOrientation(Integer.parseInt(Jext.getProperty("print.pageOrientation")));
        double d = Double.parseDouble(Jext.getProperty("print.pageWidth"));
        double d2 = Double.parseDouble(Jext.getProperty("print.pageHeight"));
        double d3 = Double.parseDouble(Jext.getProperty("print.pageImgX"));
        double d4 = Double.parseDouble(Jext.getProperty("print.pageImgY"));
        double d5 = Double.parseDouble(Jext.getProperty("print.pageImgWidth"));
        double d6 = Double.parseDouble(Jext.getProperty("print.pageImgHeight"));
        paper.setSize(d, d2);
        paper.setImageableArea(d3, d4, d5, d6);
        this.pgfmt.setPaper(paper);
        this.handleComponents();
    }

    private void handleComponents() {
        if (this.syntax.isSelected()) {
            this.footer.setEnabled(true);
            this.pageLayout.setEnabled(false);
            this.wrap.setEnabled(false);
        } else {
            this.footer.setEnabled(false);
            this.pageLayout.setEnabled(true);
            this.wrap.setEnabled(true);
        }
    }

    public void save() {
        Jext.setProperty("print.lineNumbers", this.lineNumbers.isSelected() ? "on" : "off");
        Jext.setProperty("print.wrapText", this.wrap.isSelected() ? "on" : "off");
        Jext.setProperty("print.header", this.header.isSelected() ? "on" : "off");
        Jext.setProperty("print.footer", this.footer.isSelected() ? "on" : "off");
        Jext.setProperty("print.syntax", this.syntax.isSelected() ? "on" : "off");
        Paper paper = this.pgfmt.getPaper();
        Jext.setProperty("print.pageOrientation", Integer.toString(this.pgfmt.getOrientation()));
        Jext.setProperty("print.pageWidth", Double.toString(paper.getWidth()));
        Jext.setProperty("print.pageHeight", Double.toString(paper.getHeight()));
        Jext.setProperty("print.pageImgX", Double.toString(paper.getImageableX()));
        Jext.setProperty("print.pageImgY", Double.toString(paper.getImageableY()));
        Jext.setProperty("print.pageImgWidth", Double.toString(paper.getImageableWidth()));
        Jext.setProperty("print.pageImgHeight", Double.toString(paper.getImageableHeight()));
        this.fonts.save();
    }

    public void pageLayout() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        this.pgfmt = printerJob.pageDialog(this.pgfmt);
        this.pgfmt = printerJob.validatePage(this.pgfmt);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        if (object == this.pageLayout) {
            this.pageLayout();
        } else if (object == this.syntax) {
            this.handleComponents();
        }
    }
}

