/*
 * Decompiled with CFR 0_102.
 */
package org.jext.print;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;
import org.jext.print.PrintingOptions;

public class PrintText {
    private int numberOfpages_ = 0;
    private Book pages_ = new Book();
    private int wrapOffset_ = 0;
    private String docTitle_;
    private String[] text_;
    private PrintingOptions printOptions_;
    private boolean softTabs_ = true;
    private int tabSize_ = 4;

    public PrintText(PlainDocument plainDocument) {
        this(plainDocument, "", new PrintingOptions(), false, 4);
    }

    public PrintText(PlainDocument plainDocument, String string, PrintingOptions printingOptions, boolean bl, int n) {
        this.printOptions_ = printingOptions;
        this.softTabs_ = bl;
        this.tabSize_ = n;
        this.docTitle_ = string != null ? string : "New Document";
        Element element = plainDocument.getDefaultRootElement();
        int n2 = element.getElementCount();
        String[] arrstring = new String[n2];
        Segment segment = new Segment();
        for (int i = 0; i < n2; ++i) {
            Element element2 = element.getElement(i);
            try {
                plainDocument.getText(element2.getStartOffset(), element2.getEndOffset() - element2.getStartOffset(), segment);
                arrstring[i] = segment.toString();
                continue;
            }
            catch (BadLocationException var12_12) {
                // empty catch block
            }
        }
        this.text_ = arrstring;
        this.printTextArray();
    }

    PrintText(String[] arrstring) {
        this.printOptions_ = new PrintingOptions();
        this.text_ = arrstring;
        this.printTextArray();
    }

    PrintText(String[] arrstring, Font font) {
        this.printOptions_ = new PrintingOptions();
        this.text_ = arrstring;
        this.printTextArray();
    }

    void printTextArray() {
        PageFormat pageFormat = this.printOptions_.getPageFormat();
        Font font = this.printOptions_.getPageFont();
        try {
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            this.text_ = this.removeEOLChar();
            if (this.printOptions_.getPrintLineNumbers()) {
                this.text_ = this.addLineNumbers();
            }
            if (this.printOptions_.getWrapText()) {
                this.text_ = this.wrapText();
            }
            this.pages_ = this.pageinateText();
            try {
                printerJob.setPageable(this.pages_);
                if (printerJob.printDialog()) {
                    printerJob.print();
                }
            }
            catch (Exception var4_5) {
                JOptionPane.showMessageDialog(null, "Printer Error", "Error", 0);
            }
        }
        catch (Exception var3_4) {
            JOptionPane.showMessageDialog(null, "Printer Error", "Error", 0);
        }
    }

    private String[] removeEOLChar() {
        int n = this.text_.length;
        String[] arrstring = new String[n];
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            if (this.text_[i].length() == 1) {
                arrstring[i] = " ";
                continue;
            }
            String string = this.text_[i].substring(this.text_[i].length() - 2, this.text_[i].length() - 1);
            String string2 = this.text_[i].substring(this.text_[i].length() - 1, this.text_[i].length());
            n2 = string.compareTo("\r") == 0 || string.compareTo("\n") == 0 ? 2 : (string2.compareTo("\r") == 0 || string2.compareTo("\n") == 0 ? 1 : 0);
            String string3 = this.text_[i].substring(0, this.text_[i].length() - n2);
            StringBuffer stringBuffer = new StringBuffer();
            int n3 = string3.length();
            for (int j = 0; j < n3; ++j) {
                if ("\t".equals(string3.substring(j, j + 1))) {
                    int n4 = stringBuffer.length() % this.tabSize_;
                    if (n4 == 0) {
                        n4 = this.tabSize_;
                    }
                    for (int k = 0; k < n4; ++k) {
                        stringBuffer.append(" ");
                    }
                    continue;
                }
                stringBuffer.append(string3.substring(j, j + 1));
            }
            arrstring[i] = stringBuffer.toString();
        }
        return arrstring;
    }

    private String[] addLineNumbers() {
        int n = this.text_.length;
        int n2 = 0;
        String[] arrstring = new String[n];
        Integer n3 = new Integer(n);
        String string = n3.toString();
        n2 = string.length();
        this.wrapOffset_ = n2 + 3;
        for (int i = 0; i < n; ++i) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(i + 1);
            int n4 = stringBuffer.length();
            StringBuffer stringBuffer2 = new StringBuffer();
            for (int j = 0; j < n2 - n4; ++j) {
                stringBuffer2.append(' ');
            }
            stringBuffer2.append(stringBuffer.toString());
            arrstring[i] = stringBuffer2.toString() + ".  " + this.text_[i];
        }
        return arrstring;
    }

    private String[] wrapText() {
        String string = null;
        Object var2_2 = null;
        Vector<String> vector = new Vector<String>();
        int n = this.text_.length;
        int n2 = 0;
        StringBuffer stringBuffer = new StringBuffer("");
        int n3 = 0;
        PageFormat pageFormat = this.printOptions_.getPageFormat();
        Font font = this.printOptions_.getPageFont();
        double d = pageFormat.getImageableWidth();
        for (n3 = 0; n3 < this.wrapOffset_; ++n3) {
            stringBuffer.append(' ');
        }
        for (n3 = 0; n3 < n; ++n3) {
            string = this.text_[n3];
            while (font.getStringBounds(string, new FontRenderContext(font.getTransform(), false, false)).getWidth() > d) {
                int n4 = (int)((double)string.length() * d / font.getStringBounds(string, new FontRenderContext(font.getTransform(), false, false)).getWidth());
                vector.add(string.substring(0, n4));
                string = stringBuffer.toString() + string.substring(n4, string.length());
            }
            vector.add(string);
        }
        n2 = vector.size();
        String[] arrstring = new String[n2];
        for (int i = 0; i < n2; ++i) {
            arrstring[i] = (String)vector.get(i);
        }
        return arrstring;
    }

    private Book pageinateText() {
        Book book = new Book();
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        PageFormat pageFormat = this.printOptions_.getPageFormat();
        Font font = this.printOptions_.getPageFont();
        int n4 = (int)pageFormat.getImageableHeight();
        int n5 = 0;
        n = n4 / (font.getSize() + 2);
        n5 = this.text_.length / n;
        this.convertUnprintables();
        if (this.printOptions_.getPrintHeader()) {
            n-=2;
        }
        while (n3 <= n5) {
            String[] arrstring = new String[n];
            for (int i = 0; i < n; ++i) {
                String string;
                try {
                    string = this.text_[n2];
                }
                catch (ArrayIndexOutOfBoundsException var12_12) {
                    string = " ";
                }
                arrstring[i] = string;
                ++n2;
            }
            book.append(new Page(arrstring, ++n3), pageFormat);
        }
        return book;
    }

    private void convertUnprintables() {
        int n = this.text_.length;
        while (n > 0) {
            String string;
            if ((string = this.text_[--n]) != null && !"".equals(string)) continue;
            this.text_[n] = " ";
        }
    }

    class PrintableText {
        private Font font_;
        private boolean newLine_;
        private String text_;
        private final /* synthetic */ PrintText this$0;

        PrintableText(PrintText printText) {
            this.this$0 = printText;
            this.newLine_ = true;
        }

        PrintableText(PrintText printText, String string, Font font, boolean bl) {
            this.this$0 = printText;
            this.newLine_ = true;
            this.text_ = string;
            this.font_ = font;
            this.newLine_ = bl;
        }

        String getText() {
            return this.text_;
        }

        void setText(String string) {
            this.text_ = string;
        }

        Font getFont() {
            return this.font_;
        }

        void setFont(Font font) {
            this.font_ = font;
        }

        boolean isNewLine() {
            return this.newLine_;
        }

        void setNewLine(boolean bl) {
            this.newLine_ = bl;
        }
    }

    class Page
    implements Printable {
        private String[] pageText_;
        private int pageNumber_;

        Page(String[] arrstring, int n) {
            this.pageNumber_ = 0;
            this.pageText_ = arrstring;
            this.pageNumber_ = n;
        }

        public int print(Graphics graphics, PageFormat pageFormat, int n) throws PrinterException {
            int n2;
            int n3 = 1;
            double d = pageFormat.getImageableWidth();
            Font font = PrintText.this.printOptions_.getPageFont();
            if (PrintText.this.printOptions_.getPrintHeader()) {
                StringBuffer stringBuffer = new StringBuffer();
                StringBuffer stringBuffer2 = new StringBuffer();
                boolean bl = false;
                boolean bl2 = false;
                boolean bl3 = false;
                Calendar calendar = Calendar.getInstance();
                stringBuffer.append(calendar.get(5));
                stringBuffer.append('/');
                stringBuffer.append(calendar.get(2) + 1);
                stringBuffer.append('/');
                stringBuffer.append(calendar.get(1));
                stringBuffer2.append("Page ");
                stringBuffer2.append(this.pageNumber_);
                double d2 = (pageFormat.getWidth() - pageFormat.getImageableWidth()) / 2.0;
                graphics.setFont(PrintText.this.printOptions_.getHeaderFont());
                graphics.setColor(Color.black);
                n2 = (int)pageFormat.getImageableY() + (PrintText.this.printOptions_.getHeaderFont().getSize() + 2);
                graphics.drawString(stringBuffer.toString(), (int)pageFormat.getImageableX(), n2);
                int n4 = (int)(pageFormat.getWidth() / 2.0 - (double)(graphics.getFontMetrics().stringWidth(PrintText.this.docTitle_) / 2));
                graphics.drawString(PrintText.this.docTitle_, n4, n2);
                n4 = (int)(pageFormat.getWidth() - d2 - (double)graphics.getFontMetrics().stringWidth(stringBuffer2.toString()));
                graphics.drawString(stringBuffer2.toString(), n4, n2);
                n3 = 3;
            }
            graphics.setFont(font);
            graphics.setColor(Color.black);
            for (int i = 0; i < this.pageText_.length; ++i) {
                n2 = (int)pageFormat.getImageableY() + (font.getSize() + 2) * (i + n3);
                graphics.drawString(this.pageText_[i], (int)pageFormat.getImageableX(), n2);
            }
            return 0;
        }
    }

}

