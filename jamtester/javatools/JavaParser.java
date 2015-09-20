/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  com.sun.javadoc.ClassDoc
 *  com.sun.javadoc.ConstructorDoc
 *  com.sun.javadoc.Doclet
 *  com.sun.javadoc.MethodDoc
 *  com.sun.javadoc.PackageDoc
 *  com.sun.javadoc.RootDoc
 *  com.sun.javadoc.Type
 *  com.sun.tools.javadoc.Main
 */
package jamtester.javatools;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Type;
import com.sun.tools.javadoc.Main;
import jamtester.Results;
import jamtester.jamupdate.JAMUpdate;
import jamtester.javatools.NullWriter;
import jamtester.studenttool.StudentFrame;
import jamtester.studenttool.StudentTool;
import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.UIManager;

public class JavaParser
extends Doclet {
    private static RootDoc root;
    private File file;
    private RootDoc doc;

    private static RootDoc getMostRecentRoot() {
        RootDoc rootDoc = root;
        root = null;
        return rootDoc;
    }

    public static boolean start(RootDoc rootDoc) {
        root = rootDoc;
        return true;
    }

    public File getFile() {
        return this.file;
    }

    private JavaParser(RootDoc rootDoc, File file) {
        this.doc = rootDoc;
        this.file = file;
    }

    public MethodDoc[] getMethods() {
        try {
            ClassDoc[] arrclassDoc = this.doc.classes();
            ArrayList<MethodDoc> arrayList = new ArrayList<MethodDoc>();
            for (int i = 0; i < arrclassDoc.length; ++i) {
                MethodDoc[] arrmethodDoc = arrclassDoc[i].methods();
                for (int j = 0; j < arrmethodDoc.length; ++j) {
                    if (arrmethodDoc[j].isPrivate() || arrmethodDoc[j].isProtected()) continue;
                    arrayList.add(arrmethodDoc[j]);
                }
            }
            MethodDoc[] arrmethodDoc = new MethodDoc[arrayList.size()];
            for (int j = 0; j < arrayList.size(); ++j) {
                arrmethodDoc[j] = (MethodDoc)arrayList.get(j);
            }
            return arrmethodDoc;
        }
        catch (Exception var1_2) {
            return new MethodDoc[0];
        }
    }

    public ClassDoc getClassDoc() {
        return this.doc.classNamed(this.getClassName());
    }

    public String getPackage() {
        PackageDoc packageDoc = this.doc.classes()[0].containingPackage();
        return packageDoc.name();
    }

    public static boolean isPrimitive(Type type) {
        boolean bl = false;
        if (type.toString().equals("int")) {
            bl = true;
        }
        if (type.toString().equals("byte")) {
            bl = true;
        }
        if (type.toString().equals("long")) {
            bl = true;
        }
        if (type.toString().equals("short")) {
            bl = true;
        }
        if (type.toString().equals("double")) {
            bl = true;
        }
        if (type.toString().equals("float")) {
            bl = true;
        }
        if (type.toString().equals("boolean")) {
            bl = true;
        }
        if (type.toString().equals("char")) {
            bl = true;
        }
        if (type.toString().equals("void")) {
            bl = true;
        }
        return bl;
    }

    public static boolean isPrimitiveArray(Type type) {
        boolean bl = false;
        if (type.typeName().equals("int")) {
            bl = true;
        }
        if (type.typeName().equals("byte")) {
            bl = true;
        }
        if (type.typeName().equals("long")) {
            bl = true;
        }
        if (type.typeName().equals("short")) {
            bl = true;
        }
        if (type.typeName().equals("double")) {
            bl = true;
        }
        if (type.typeName().equals("float")) {
            bl = true;
        }
        if (type.typeName().equals("boolean")) {
            bl = true;
        }
        if (type.typeName().equals("char")) {
            bl = true;
        }
        if (type.typeName().equals("void")) {
            bl = true;
        }
        return bl && type.dimension().length() > 0;
    }

    public static ConstructorDoc[] getConstructors(ClassDoc classDoc) {
        int n;
        ClassDoc classDoc2 = classDoc;
        ConstructorDoc[] arrconstructorDoc = classDoc2.constructors();
        ArrayList<ConstructorDoc> arrayList = new ArrayList<ConstructorDoc>();
        for (n = 0; n < arrconstructorDoc.length; ++n) {
            if (!arrconstructorDoc[n].isPublic() && !arrconstructorDoc[n].isPackagePrivate()) continue;
            arrayList.add(arrconstructorDoc[n]);
        }
        arrconstructorDoc = new ConstructorDoc[arrayList.size()];
        for (n = 0; n < arrconstructorDoc.length; ++n) {
            arrconstructorDoc[n] = (ConstructorDoc)arrayList.get(n);
        }
        return arrconstructorDoc;
    }

    public String getQualifiedClassName() {
        return this.doc.classes()[0].qualifiedName();
    }

    public String getClassName() {
        return this.doc.classes()[0].name();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Class Name: " + this.getClassName() + "\n");
        for (int i = 0; i < this.getMethods().length; ++i) {
            stringBuffer.append("Method: " + this.getMethods()[i].isStatic() + " " + (Object)this.getMethods()[i].returnType() + " " + (Object)this.getMethods()[i] + "\n");
        }
        return stringBuffer.toString();
    }

    public static File findFileContainingLibs(File file) {
        if (!file.isDirectory()) {
            return JavaParser.findFileContainingLibs(file.getParentFile());
        }
        if (new File(file, "+libs").exists()) {
            return file;
        }
        if (file.getParentFile() == null) {
            return null;
        }
        return JavaParser.findFileContainingLibs(file.getParentFile());
    }

    private static String getClasspath(File file) {
        StringBuffer stringBuffer = new StringBuffer();
        File file2 = JavaParser.findFileContainingLibs(file);
        File[] arrfile = StudentFrame.getClassFiles(file2);
        for (int i = 0; i < arrfile.length; ++i) {
            stringBuffer.append(arrfile[i].getAbsolutePath());
            if (i == arrfile.length - 1) continue;
            stringBuffer.append(';');
        }
        return stringBuffer.toString();
    }

    public static synchronized JavaParser parseFile(String string) {
        String string2 = JavaParser.getClasspath(new File(string));
        Main.execute((String)"JAMJavaParser", (PrintWriter)NullWriter.PRINTWRITER, (PrintWriter)NullWriter.PRINTWRITER, (PrintWriter)NullWriter.PRINTWRITER, (String)"jamtester.javatools.JavaParser", (String[])new String[]{"-classpath", string2, string, "-private"});
        return new JavaParser(JavaParser.getMostRecentRoot(), new File(string));
    }

    public static synchronized JavaParser parseFile(File file) {
        return JavaParser.parseFile(file.toString());
    }

    public static void main(String[] arrstring) {
        try {
            UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[2].getClassName());
        }
        catch (Exception var1_1) {
            // empty catch block
        }
        System.out.println(System.getProperty("user.dir"));
        File file = new File("C:\\Program Files\\Xinox Software\\JCreatorV3\\MyProjects\\GradingToolGui\\src\\jamtester\\javatools\\JavaParser.java");
        file = new File("C:\\Documents and Settings\\David Eitan Poll\\Desktop\\JAMTester\\JAM\\folder of student folders\\Epstein David\\SomeClass.java");
        JavaParser javaParser = JavaParser.parseFile(file.toString());
        Results results = new Results();
        StudentTool studentTool = new StudentTool(javaParser);
        StudentFrame studentFrame = new StudentFrame();
        studentFrame.setSize(800, 600);
        studentFrame.show();
        JAMUpdate.doBackgroundUpdateIfNecessary();
    }
}

