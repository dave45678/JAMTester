/*
 * Decompiled with CFR 0_102.
 */
package org.jext.actions;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import javax.swing.text.PlainDocument;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.MenuAction;
import org.jext.Utilities;
import org.jext.print.PrintSyntax;
import org.jext.print.PrintText;
import org.jext.print.PrintingOptions;

public class Print
extends MenuAction {
    public Print() {
        super("print");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextFrame jextFrame = Print.getJextParent(actionEvent);
        JextTextArea jextTextArea = jextFrame.getTextArea();
        jextFrame.hideWaitCursor();
        try {
            if (Jext.getBooleanProperty("print.syntax")) {
                PrintSyntax printSyntax = new PrintSyntax();
                printSyntax.print(jextFrame, jextTextArea);
            } else {
                PrintingOptions printingOptions = new PrintingOptions();
                printingOptions.setPrintLineNumbers(Jext.getBooleanProperty("print.lineNumbers"));
                printingOptions.setPrintHeader(Jext.getBooleanProperty("print.header"));
                printingOptions.setWrapText(Jext.getBooleanProperty("print.wrapText"));
                printingOptions.setPageFont(new Font(Jext.getProperty("print.font"), 0, new Integer(Jext.getProperty("print.fontSize"))));
                PageFormat pageFormat = new PageFormat();
                Paper paper = pageFormat.getPaper();
                pageFormat.setOrientation(new Integer(Jext.getProperty("print.pageOrientation")));
                double d = new Double(Jext.getProperty("print.pageWidth"));
                double d2 = new Double(Jext.getProperty("print.pageHeight"));
                double d3 = new Double(Jext.getProperty("print.pageImgX"));
                double d4 = new Double(Jext.getProperty("print.pageImgY"));
                double d5 = new Double(Jext.getProperty("print.pageImgWidth"));
                double d6 = new Double(Jext.getProperty("print.pageImgHeight"));
                paper.setSize(d, d2);
                paper.setImageableArea(d3, d4, d5, d6);
                pageFormat.setPaper(paper);
                printingOptions.setPageFormat(pageFormat);
                PrintText printText = new PrintText(jextTextArea.getDocument(), jextTextArea.getName(), printingOptions, JextTextArea.getSoftTab(), jextTextArea.getTabSize());
            }
        }
        catch (Exception var4_6) {
            Utilities.showError(Jext.getProperty("textarea.print.error"));
        }
        jextFrame.hideWaitCursor();
    }
}

