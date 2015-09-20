/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  com.sun.javadoc.MethodDoc
 */
package jamtester.studenttool;

import com.sun.javadoc.MethodDoc;
import jamtester.javatools.JavaParser;
import jamtester.studenttool.StudentTool;
import jamtester.studenttool.StudentToolPanel;
import jamtester.studenttool.StudentToolTextArea;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.text.AttributeSet;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.gjt.sp.jedit.textarea.TextAreaPainter;

public class StudentToolEditPanel
extends JPanel {
    JComboBox cb;
    StudentTool tool;
    StudentToolTextArea tA;
    StudentToolPanel parent;
    File file;

    public StudentToolEditPanel(StudentTool studentTool, boolean bl, StudentToolPanel studentToolPanel) {
        this(studentTool, studentToolPanel);
        if (bl) {
            this.tA.setText(studentTool.generateJUnitClass());
        } else {
            this.setNew();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("public class " + studentTool.getParser().getClassName() + "Test extends junit.framework.TestCase\n{\n\n\t//Your code here\n\n");
            stringBuffer.append("\tpublic static void main(String[] args)\n");
            stringBuffer.append("\t{\n");
            stringBuffer.append("\t\tjunit.textui.TestRunner.run(new junit.framework.TestSuite(" + studentTool.getParser().getClassName() + "Test.class));\n");
            stringBuffer.append("\t}\n\n}\n");
        }
    }

    public StudentTool getTool() {
        return this.tool;
    }

    public StudentToolEditPanel(StudentTool studentTool, File file, StudentToolPanel studentToolPanel) {
        this(studentTool, studentToolPanel);
        this.tA.setInitialText(StudentToolEditPanel.readFile(file));
    }

    public StudentToolEditPanel(final StudentTool studentTool, StudentToolPanel studentToolPanel) {
        this.parent = studentToolPanel;
        this.file = studentTool.getFile();
        this.cb = new JComboBox();
        this.tA = new StudentToolTextArea();
        this.tA.getPainter().setFont(this.tA.getPainter().getFont().deriveFont(12.0f));
        this.tool = studentTool;
        this.cb.addItemListener(new ItemListener(){

            public void itemStateChanged(ItemEvent itemEvent) {
                if (itemEvent.getStateChange() == 2) {
                    return;
                }
                if (itemEvent.getItem() instanceof String) {
                    return;
                }
                int n = StudentToolEditPanel.this.tA.insideFirstBracket();
                try {
                    StudentToolEditPanel.this.tA.getDocument().insertString(n, "\n" + studentTool.generateMethodStub((MethodDoc)itemEvent.getItem(), true, StudentToolEditPanel.this.tA), null);
                }
                catch (Exception var3_3) {
                    // empty catch block
                }
                StudentToolEditPanel.this.tA.setFirstLine(StudentToolEditPanel.this.tA.getLineOfOffset(n));
                StudentToolEditPanel.this.cb.setSelectedIndex(0);
            }
        });
        BorderLayout borderLayout = new BorderLayout();
        this.setLayout(borderLayout);
        borderLayout.setVgap(3);
        this.add((Component)this.cb, "North");
        this.add((Component)this.tA, "Center");
        this.setItems(studentTool.getParser().getMethods());
    }

    public File getFile() {
        return this.file;
    }

    public void setTool(StudentTool studentTool) {
        this.tool = studentTool;
    }

    public void autoGenerate() {
        this.tool = new StudentTool(JavaParser.parseFile(this.parent.getStartFile()));
        this.tA.setText(this.tool.generateJUnitClass());
    }

    public void autoGenerate(String string) {
        this.tool = new StudentTool(JavaParser.parseFile(this.parent.getStartFile()));
        this.tA.setText(this.tool.generateJUnitClass(string));
    }

    public void openFile(File file) {
        if (!(file != null && file.exists())) {
            return;
        }
        this.tA.setInitialText(StudentToolEditPanel.readFile(file));
        this.parent.setTestFileName(file);
    }

    public void setNew() {
        this.setNew(this.tool.getParser().getClassName() + "Test");
    }

    public void setNew(String string) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.tool.getTopPart());
        stringBuffer.append("public class " + string + " extends junit.framework.TestCase\n{\n\n\t//Your code here\n\n");
        stringBuffer.append("\tpublic static void main(String[] args)\n");
        stringBuffer.append("\t{\n");
        stringBuffer.append("\t\tjunit.textui.TestRunner.run(new junit.framework.TestSuite(" + string + ".class));\n");
        stringBuffer.append("\t}\n\n}\n");
        this.tA.setText(stringBuffer.toString());
    }

    public static String readFile(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] arrby = new byte[fileInputStream.available()];
            fileInputStream.read(arrby);
            fileInputStream.close();
            return new String(arrby);
        }
        catch (Exception var1_2) {
            return "";
        }
    }

    public File saveTemp() {
        File file = null;
        try {
            File file2 = this.tA.createTempJavaFile();
            try {
                file = new File(file2.getParent(), JavaParser.parseFile(file2).getClassName() + ".java");
            }
            catch (Exception var3_4) {
                file = new File(file2.getParent(), StudentToolEditPanel.findClassName(file2) + ".java");
            }
            FileInputStream fileInputStream = new FileInputStream(file2);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] arrby = new byte[fileInputStream.available()];
            fileInputStream.read(arrby);
            System.out.println(new String(arrby));
            fileOutputStream.write(arrby);
            fileInputStream.close();
            fileOutputStream.close();
            file2.delete();
            file.deleteOnExit();
        }
        catch (Exception var2_3) {
            var2_3.printStackTrace(System.err);
        }
        return file;
    }

    public static String findClassName(File file) {
        String string = "";
        String string2 = StudentToolEditPanel.readFile(file);
        int n = (string2 = StudentToolEditPanel.removeCommentsAndTrim(string2)).indexOf("public class");
        if (n < 0) {
            n = string2.indexOf("class");
            if (n < 0) {
                return "Unknown";
            }
            n+="class ".length();
        } else {
            n+="public class ".length();
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = n; i < string2.length() && string2.charAt(i) != ' ' && string2.charAt(i) != '{'; ++i) {
            stringBuffer.append(string2.charAt(i));
        }
        return stringBuffer.toString();
    }

    private static String removeCommentsAndTrim(String string) {
        string = string.replaceAll("//[^\n]*\n", "");
        while (string.indexOf("/*") >= 0) {
            int n = string.indexOf("/*");
            int n2 = string.indexOf("*/", n + 2);
            n2 = n2 < 0 ? string.length() - 1 : (n2+=2);
            string = string.substring(0, n - 1) + string.substring(n2);
        }
        string = string.replaceAll("[\\s]+", " ");
        return string;
    }

    public void save() {
        this.parent.saveTestFile();
    }

    public void setItems(MethodDoc[] arrmethodDoc) {
        this.cb.removeAllItems();
        this.cb.addItem("Add a test method stub");
        for (int i = 0; i < arrmethodDoc.length; ++i) {
            this.cb.addItem(arrmethodDoc[i]);
        }
    }

    public StudentToolTextArea getTextArea() {
        return this.tA;
    }

}

