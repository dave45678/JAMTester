/*
 * Decompiled with CFR 0_102.
 */
package jamtester.studenttool;

import jamtester.studenttool.StudentToolInputHandler;
import jamtester.studenttool.StudentToolLineNumGutter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import org.gjt.sp.jedit.syntax.JavaTokenMarker;
import org.gjt.sp.jedit.syntax.PlainTokenMarker;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.gjt.sp.jedit.syntax.TokenMarker;
import org.gjt.sp.jedit.textarea.Gutter;
import org.gjt.sp.jedit.textarea.InputHandler;
import org.gjt.sp.jedit.textarea.TextAreaPainter;
import org.jext.JextFrame;
import org.jext.JextTextArea;

public class StudentToolTextArea
extends JextTextArea {
    private StudentToolLineNumGutter gutter;
    private JPanel panel;
    private BorderLayout layout;

    public StudentToolTextArea() {
        super(new JextFrame(null, false));
        this.addMouseWheelListener(new MouseWheel(this.vertical()));
        this.panel = new JPanel();
        this.layout = new BorderLayout();
        this.panel.setLayout(this.layout);
        this.panel.add((Component)this.painter, "Center");
        this.remove(this.painter);
        this.add((Component)this.panel, CENTER);
        this.layout.layoutContainer(this.panel);
        this.getJextParent().setInputHandler(new StudentToolInputHandler());
        this.getDocument().setTokenMarker(new JavaTokenMarker());
        super.getPainter().setBracketHighlightColor(Color.green.brighter().brighter());
        this.getPainter().setEOLMarkersPainted(false);
        this.setFontName("Courier New");
        super.getGutter().setCollapsed(false);
        super.getGutter().setLineNumberingEnabled(true);
    }

    public static boolean getSoftTab() {
        return false;
    }

    public int getTabSize() {
        return 3;
    }

    public void removeTokenMarker() {
        this.getDocument().setTokenMarker(new PlainTokenMarker());
    }

    public StudentToolTextArea(File file, boolean bl) {
        this();
        StringBuffer stringBuffer = new StringBuffer();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] arrby = new byte[fileInputStream.available()];
            fileInputStream.read(arrby);
            stringBuffer.append(new String(arrby));
            fileInputStream.close();
        }
        catch (Exception var4_5) {
            JOptionPane.showMessageDialog(this, "Unable to open file: " + file.getName() + "\nPath not found.", "Unable to open file", 0);
        }
        this.setInitialText(stringBuffer.toString());
        this.setEditable(bl);
    }

    public File createTempJavaFile() {
        File file = null;
        try {
            file = File.createTempFile("temporary", ".java");
            file.deleteOnExit();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            String string = this.getText();
            fileOutputStream.write(string.getBytes());
            fileOutputStream.close();
        }
        catch (Exception var2_3) {
            var2_3.printStackTrace(System.out);
        }
        return file;
    }

    public JScrollBar horizontal() {
        return this.horizontal;
    }

    public JScrollBar vertical() {
        return this.vertical;
    }

    public void setInitialText(String string) {
        TokenMarker tokenMarker = this.document.getTokenMarker();
        this.setDocument(new SyntaxDocument());
        super.setText(string);
        this.document.setTokenMarker(tokenMarker);
    }

    public int insideFirstBracket() {
        int n = -1;
        try {
            n = this.document.getText(0, this.document.getLength()).indexOf("{") + 1;
        }
        catch (Exception var2_2) {
            return -1;
        }
        return n;
    }

    public void setText(String string) {
        super.setText(string);
        this.setFirstLine(0);
    }

    private static class MouseWheel
    implements MouseWheelListener {
        JScrollBar bar;

        public MouseWheel(JScrollBar jScrollBar) {
            this.bar = jScrollBar;
        }

        public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
            int n = mouseWheelEvent.getWheelRotation() + this.bar.getValue();
            if (n < 0) {
                n = 0;
            }
            this.bar.setValueIsAdjusting(true);
            this.bar.setValue(n);
        }
    }

}

