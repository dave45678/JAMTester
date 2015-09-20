/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  com.sun.javadoc.ClassDoc
 *  com.sun.javadoc.ConstructorDoc
 *  com.sun.javadoc.MethodDoc
 *  com.sun.javadoc.Parameter
 *  com.sun.javadoc.Type
 */
package jamtester.studenttool;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Type;
import jamtester.javatools.JavaParser;
import jamtester.studenttool.StudentToolTextArea;
import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class StudentTool {
    private JavaParser f;

    public StudentTool(JavaParser javaParser) {
        if (javaParser == null) {
            throw new RuntimeException("Invalid or un-Scannable source file!");
        }
        this.f = javaParser;
    }

    public String generateJUnitClass(String string) {
        int n;
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append("\tpublic static void main(String[] args)\n");
        stringBuffer2.append("\t{\n");
        stringBuffer2.append("\t\tjunit.textui.TestRunner.run(new junit.framework.TestSuite(" + this.f.getClassName() + "Test.class));\n");
        stringBuffer2.append("\t}\n");
        stringBuffer2.append("}\n");
        if (this.f.getPackage().length() > 0) {
            stringBuffer.append("package " + this.f.getPackage() + ";\n");
        }
        stringBuffer.append("\n");
        String[] arrstring = this.calculateImports();
        for (n = 0; n < arrstring.length; ++n) {
            stringBuffer.append(arrstring[n] + ";\n");
        }
        stringBuffer.append("\n");
        stringBuffer.append("public class " + string + " extends junit.framework.TestCase\n");
        stringBuffer.append("{\n");
        for (n = 0; n < this.f.getMethods().length; ++n) {
            stringBuffer.append(this.generateMethodStub(this.f.getMethods()[n], false, stringBuffer.toString() + stringBuffer2.toString()));
        }
        stringBuffer.append(stringBuffer2);
        return stringBuffer.toString();
    }

    public String getTopPart() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.f.getPackage().length() > 0) {
            stringBuffer.append("package " + this.f.getPackage() + ";\n");
        }
        stringBuffer.append("\n");
        String[] arrstring = this.calculateImports();
        for (int i = 0; i < arrstring.length; ++i) {
            stringBuffer.append(arrstring[i] + ";\n");
        }
        return stringBuffer.toString();
    }

    public File getFile() {
        return this.f.getFile();
    }

    public String generateJUnitClass() {
        return this.generateJUnitClass(this.f.getClassName() + "Test");
    }

    public String generateMethodStub(MethodDoc methodDoc, boolean bl, StudentToolTextArea studentToolTextArea) {
        MethodDoc[] arrmethodDoc = JavaParser.parseFile(studentToolTextArea.createTempJavaFile()).getMethods();
        String[] arrstring = new String[arrmethodDoc.length];
        for (int i = 0; i < arrmethodDoc.length; ++i) {
            arrstring[i] = arrmethodDoc[i].name();
        }
        List<String> list = Arrays.asList(arrstring);
        int n = 1;
        String string = "test" + methodDoc.name().substring(0, 1).toUpperCase() + methodDoc.name().substring(1);
        while (list.contains(this.methodName(string, n))) {
            ++n;
        }
        return this.generateMethodStub(methodDoc, bl, n);
    }

    public String generateMethodStub(MethodDoc methodDoc, boolean bl, String string) {
        StudentToolTextArea studentToolTextArea = new StudentToolTextArea();
        studentToolTextArea.setText(string);
        return this.generateMethodStub(methodDoc, bl, studentToolTextArea);
    }

    private String methodName(String string, int n) {
        if (n == 1) {
            return string;
        }
        return string + n;
    }

    public JavaParser getParser() {
        return this.f;
    }

    public String[] calculateImports() {
        int n;
        TreeSet<String> treeSet = new TreeSet<String>();
        for (int i = 0; i < this.f.getMethods().length; ++i) {
            if (!(JavaParser.isPrimitive(this.f.getMethods()[i].returnType()) || JavaParser.isPrimitiveArray(this.f.getMethods()[i].returnType()))) {
                treeSet.add(this.f.getMethods()[i].returnType().qualifiedTypeName());
            }
            for (n = 0; n < this.f.getMethods()[i].parameters().length; ++n) {
                if (JavaParser.isPrimitive(this.f.getMethods()[i].parameters()[n].type()) || JavaParser.isPrimitiveArray(this.f.getMethods()[i].parameters()[n].type())) continue;
                treeSet.add(this.f.getMethods()[i].parameters()[n].type().qualifiedTypeName());
            }
        }
        Iterator iterator = treeSet.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().toString().indexOf(46) >= 0) continue;
            iterator.remove();
        }
        iterator = treeSet.iterator();
        n = 0;
        String[] arrstring = new String[treeSet.size()];
        while (iterator.hasNext()) {
            arrstring[n] = "import " + iterator.next().toString();
            System.out.println(arrstring[n]);
            ++n;
        }
        return arrstring;
    }

    private String generateDefaultValue(Parameter parameter) {
        return this.generateDefaultValue(parameter.type());
    }

    private String generateDefaultValue(Type type) {
        if (!JavaParser.isPrimitive(type) && type.toString().equals("String")) {
            return "\"\"";
        }
        if (!JavaParser.isPrimitive(type)) {
            return "null";
        }
        if (type.toString().equals("int")) {
            return "0";
        }
        if (type.toString().equals("short")) {
            return "0";
        }
        if (type.toString().equals("long")) {
            return "0L";
        }
        if (type.toString().equals("byte")) {
            return "0";
        }
        if (type.toString().equals("double")) {
            return "0";
        }
        if (type.toString().equals("float")) {
            return "0F";
        }
        if (type.toString().equals("boolean")) {
            return "false";
        }
        if (type.toString().equals("char")) {
            return "'?'";
        }
        return "";
    }

    public String generateMethodStub(MethodDoc methodDoc, boolean bl) {
        return this.generateMethodStub(methodDoc, bl, 1);
    }

    public String generateMethodStub(MethodDoc methodDoc, boolean bl, int n) {
        Object object;
        StringBuffer stringBuffer = new StringBuffer();
        String string = methodDoc.name();
        String string2 = "obj";
        if (methodDoc.isStatic()) {
            string2 = bl ? methodDoc.containingClass().qualifiedTypeName() : methodDoc.containingClass().typeName();
        }
        string = string.substring(0, 1).toUpperCase() + string.substring(1);
        if (n == 1) {
            stringBuffer.append("\tpublic void test" + string + "()\n\t{\n");
        } else {
            stringBuffer.append("\tpublic void test" + string + n + "()\n\t{\n");
        }
        if (!methodDoc.isStatic()) {
            if (bl) {
                stringBuffer.append("\t\t" + methodDoc.containingClass().qualifiedTypeName() + " " + string2 + "=new " + methodDoc.containingClass().qualifiedTypeName() + "(");
            } else {
                stringBuffer.append("\t\t" + methodDoc.containingClass().typeName() + " " + string2 + "=new " + methodDoc.containingClass().qualifiedTypeName() + "(");
            }
            ConstructorDoc[] arrconstructorDoc = methodDoc.containingClass().constructors();
            object = arrconstructorDoc[0];
            for (int i = 0; i < object.parameters().length; ++i) {
                stringBuffer.append(this.generateDefaultValue(object.parameters()[i]));
                if (i >= object.parameters().length - 1) continue;
                stringBuffer.append(", ");
            }
            stringBuffer.append(");\n");
        }
        stringBuffer.append("\t\tString problemMsg=\"Problem with: " + this.methodName(methodDoc.name(), n) + "\";\n");
        for (int i = 0; i < methodDoc.parameters().length; ++i) {
            stringBuffer.append("\t\t");
            if (bl) {
                stringBuffer.append("" + methodDoc.parameters()[i].type().qualifiedTypeName() + methodDoc.parameters()[i].type().dimension() + " " + methodDoc.parameters()[i].name() + "=" + this.generateDefaultValue(methodDoc.parameters()[i]) + ";");
            } else {
                stringBuffer.append("" + methodDoc.parameters()[i].type().typeName() + methodDoc.parameters()[i].type().dimension() + " " + methodDoc.parameters()[i].name() + "=" + this.generateDefaultValue(methodDoc.parameters()[i]) + ";");
            }
            stringBuffer.append("\n");
        }
        String string3 = "";
        if (JavaParser.isPrimitive(methodDoc.returnType()) && methodDoc.returnType().toString().equals("boolean")) {
            string3 = "\t\tassertTrue(problemMsg, " + this.methodCall(methodDoc, string2) + ");\n";
            string3 = string3 + "//\t\tassertFalse(problemMsg, " + this.methodCall(methodDoc, string2) + ");";
        }
        if (JavaParser.isPrimitive(methodDoc.returnType()) && methodDoc.returnType().toString().equals("double")) {
            string3 = "\t\tassertEquals(problemMsg, 0, " + this.methodCall(methodDoc, string2) + ", 0.1);";
            string3 = string3 + "\n//NOTE: Final parameter is the tolerance for the comparison";
        }
        if (JavaParser.isPrimitive(methodDoc.returnType()) && methodDoc.returnType().toString().equals("float")) {
            string3 = "\t\tassertEquals(problemMsg, 0F, " + this.methodCall(methodDoc, string2) + ", 0.1F);";
            string3 = string3 + "\n//NOTE: Final parameter is the tolerance for the comparison";
        }
        if (JavaParser.isPrimitive(methodDoc.returnType()) && methodDoc.returnType().toString().equals("char")) {
            string3 = "\t\tassertEquals(problemMsg, '?', " + this.methodCall(methodDoc, string2) + ");";
        }
        if (JavaParser.isPrimitive(methodDoc.returnType()) && methodDoc.returnType().toString().equals("int")) {
            string3 = "\t\tassertEquals(problemMsg, 0, " + this.methodCall(methodDoc, string2) + ");";
        }
        if (JavaParser.isPrimitive(methodDoc.returnType()) && methodDoc.returnType().toString().equals("long")) {
            string3 = "\t\tassertEquals(problemMsg, 0L, " + this.methodCall(methodDoc, string2) + ");";
        }
        if (JavaParser.isPrimitive(methodDoc.returnType()) && methodDoc.returnType().toString().equals("byte")) {
            string3 = "\t\tassertEquals(problemMsg, 0, " + this.methodCall(methodDoc, string2) + ");";
        }
        if (JavaParser.isPrimitive(methodDoc.returnType()) && methodDoc.returnType().toString().equals("short")) {
            string3 = "\t\tassertEquals(problemMsg, 0, " + this.methodCall(methodDoc, string2) + ");";
        }
        if (JavaParser.isPrimitive(methodDoc.returnType()) && methodDoc.returnType().toString().equals("void")) {
            string3 = "\t\t" + this.methodCall(methodDoc, string2) + ";\n";
            string3 = string3 + "\t\t//Examine the object and assert something appropriate here";
        }
        if (!JavaParser.isPrimitive(methodDoc.returnType())) {
            object = "null";
            if (methodDoc.returnType().toString().equals("String")) {
                object = "\"\"";
            }
            string3 = "\t\tassertEquals(problemMsg, " + (String)object + ", " + this.methodCall(methodDoc, string2) + ");";
        }
        stringBuffer.append(string3);
        stringBuffer.append("\n");
        stringBuffer.append("\t}\n");
        return stringBuffer.toString();
    }

    private String methodCall(MethodDoc methodDoc, String string) {
        StringBuffer stringBuffer = new StringBuffer(string + ".");
        stringBuffer.append(methodDoc.name() + "(");
        for (int i = 0; i < methodDoc.parameters().length; ++i) {
            stringBuffer.append(methodDoc.parameters()[i].name());
            if (i >= methodDoc.parameters().length - 1) continue;
            stringBuffer.append(", ");
        }
        stringBuffer.append(")");
        return stringBuffer.toString();
    }
}

