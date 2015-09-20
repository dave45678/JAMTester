/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import org.jext.dawn.CodeSnippet;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class DawnParser {
    public static final String DAWN_VERSION = "Dawn v1.1.1 final [$12:12:55 07/08/00]";
    public static final int DAWN_NUMERIC_TYPE = 0;
    public static final int DAWN_STRING_TYPE = 1;
    public static final int DAWN_LITERAL_TYPE = 2;
    public static final int DAWN_ARRAY_TYPE = 3;
    private static Hashtable functions = new Hashtable(200);
    private static Hashtable variables = new Hashtable();
    private static Vector installedPackages = new Vector();
    private static Vector installedRuntimePackages = new Vector();
    private static boolean isInited = false;
    private boolean stopped = false;
    private Hashtable properties = new Hashtable();
    private StreamTokenizer st;
    private Stack stack;
    private Hashtable runtimeFunctions;
    private Hashtable runtimeVariables;
    public int lineno = 1;
    public PrintStream out = System.out;
    public PrintStream err = System.err;
    public InputStream in = System.in;

    public static void init() {
        System.out.println("Dawn v1.1.1 final [$12:12:55 07/08/00]");
        DawnParser.installPackage("dawn.array");
        DawnParser.installPackage("dawn.err");
        DawnParser.installPackage("dawn.io");
        DawnParser.installPackage("dawn.javaccess");
        DawnParser.installPackage("dawn.loop");
        DawnParser.installPackage("dawn.math");
        DawnParser.installPackage("dawn.naming");
        DawnParser.installPackage("dawn.stack");
        DawnParser.installPackage("dawn.string");
        DawnParser.installPackage("dawn.test");
        DawnParser.installPackage("dawn.util");
        System.out.println();
        isInited = true;
    }

    public static boolean isInitialized() {
        return isInited;
    }

    public static void installPackage(String string) {
        Class class_ = DawnParser.class;
        DawnParser.installPackage(class_, string, null);
    }

    public static void installPackage(Class class_, String string) {
        DawnParser.installPackage(class_, string, null);
    }

    public static void installPackage(Class class_, String string, DawnParser dawnParser) {
        if (string == null || class_ == null) {
            return;
        }
        if (installedPackages.contains(string)) {
            System.out.println("Dawn:<installPackage>:package " + string + " is already installed");
            return;
        }
        String[] arrstring = DawnParser.getClasses(class_, string);
        if (arrstring == null) {
            System.out.println("Dawn:<installPackage:err>:couldn't install " + string);
            return;
        }
        Object var4_4 = null;
        Class class_2 = null;
        String string2 = null;
        Function function = null;
        CodeSnippet codeSnippet = null;
        try {
            for (int i = 0; i < arrstring.length; ++i) {
                string2 = arrstring[i];
                class_2 = Class.forName(string2);
                if (class_2 == null) {
                    System.out.println("Dawn:<installPackage:err>:couldn't find class " + string2 + " in package " + string);
                    continue;
                }
                var4_4 = class_2.newInstance();
                if (var4_4 instanceof Function) {
                    function = var4_4;
                    (dawnParser == null ? functions : dawnParser.getRuntimeFunctions()).put(function.getName(), function);
                    continue;
                }
                if (!(var4_4 instanceof CodeSnippet)) continue;
                codeSnippet = var4_4;
                if (dawnParser == null) {
                    DawnParser.createGlobalFunction(codeSnippet.getName(), codeSnippet.getCode());
                    continue;
                }
                dawnParser.createRuntimeFunction(codeSnippet.getName(), codeSnippet.getCode());
            }
        }
        catch (Exception var9_10) {
            System.out.println("Dawn:<installPackage:err>:couldn't load class " + string2 + " from package " + string);
            System.out.println("Dawn:<installPackage:err>:package " + string + " wasn't loaded");
            return;
        }
        System.out.println("Dawn:<installPackage>:\t" + string + (string.length() < 8 ? "\t\t" : "\t") + "successfully installed");
        (dawnParser == null ? installedPackages : installedRuntimePackages).addElement(string);
    }

    private static String[] getClasses(Class class_, String string) {
        Vector<String> vector = new Vector<String>();
        InputStream inputStream = class_.getResourceAsStream(string);
        if (inputStream == null) {
            return null;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String string2;
            while ((string2 = bufferedReader.readLine()) != null) {
                if ((string2 = string2.trim()).length() == 0 || string2.charAt(0) == '#') continue;
                if (string2.startsWith("needs")) {
                    int n = string2.indexOf(32);
                    if (n == -1 || n + 1 == string2.length()) {
                        System.out.println("Dawn:<installPackage:err>:package " + string + " contains a bad 'needs' statement");
                        continue;
                    }
                    DawnParser.installPackage(class_, string2.substring(n + 1), null);
                    continue;
                }
                vector.addElement(string2);
            }
            bufferedReader.close();
        }
        catch (IOException var6_7) {
            return null;
        }
        if (vector.size() > 0) {
            Object[] arrobject = new String[vector.size()];
            vector.copyInto(arrobject);
            vector = null;
            return arrobject;
        }
        return null;
    }

    public DawnParser(Reader reader) {
        this.st = this.createTokenizer(reader);
        this.stack = new Stack();
        this.runtimeFunctions = new Hashtable();
        this.runtimeVariables = new Hashtable();
    }

    public void setOut(PrintStream printStream) {
        this.out = printStream;
    }

    public void setErr(PrintStream printStream) {
        this.err = printStream;
    }

    public void setIn(InputStream inputStream) {
        this.in = inputStream;
    }

    public void setStream(StreamTokenizer streamTokenizer) {
        this.st = streamTokenizer;
    }

    public StreamTokenizer getStream() {
        return this.st;
    }

    public StreamTokenizer createTokenizer(Reader reader) {
        StreamTokenizer streamTokenizer = new StreamTokenizer(reader);
        streamTokenizer.resetSyntax();
        streamTokenizer.eolIsSignificant(true);
        streamTokenizer.whitespaceChars(0, 32);
        streamTokenizer.wordChars(33, 255);
        streamTokenizer.quoteChar(34);
        streamTokenizer.quoteChar(39);
        streamTokenizer.commentChar(35);
        streamTokenizer.parseNumbers();
        streamTokenizer.eolIsSignificant(true);
        return streamTokenizer;
    }

    public static Hashtable getFunctions() {
        return functions;
    }

    public Hashtable getRuntimeFunctions() {
        return this.runtimeFunctions;
    }

    public Stack getStack() {
        return this.stack;
    }

    public void checkVarName(Function function, String string) throws DawnRuntimeException {
        if (string.equals("needs") || string.equals("needsGlobal")) {
            throw new DawnRuntimeException(function, this, "you cannot use reserved keyword'needs' or 'needsGlobal'");
        }
        boolean bl = false;
        for (int i = 0; i < string.length(); ++i) {
            if (Character.isDigit(string.charAt(i)) && !bl) {
                throw new DawnRuntimeException(function, this, "bad variable/function name:" + string);
            }
            bl = true;
        }
    }

    public void checkArgsNumber(Function function, int n) throws DawnRuntimeException {
        if (this.stack.size() < n) {
            throw new DawnRuntimeException(function, this, "bad arguments number, " + n + " are required");
        }
    }

    public void checkEmpty(Function function) throws DawnRuntimeException {
        if (this.stack.isEmpty()) {
            throw new DawnRuntimeException(function, this, "empty stack");
        }
    }

    public void checkLevel(Function function, int n) throws DawnRuntimeException {
        if (n >= this.stack.size() || n < 0) {
            throw new DawnRuntimeException(function, this, "stack level out of bounds:" + n);
        }
    }

    public void setProperty(Object object, Object object2) {
        if (object == null || object2 == null) {
            return;
        }
        this.properties.put(object, object2);
    }

    public Object getProperty(Object object) {
        if (object == null) {
            return null;
        }
        return this.properties.get(object);
    }

    public void unsetProperty(Object object) {
        this.properties.remove(object);
    }

    public void stop() {
        this.stopped = true;
    }

    public void exec() throws DawnRuntimeException {
        if (this.st == null) {
            throw new DawnRuntimeException(this, "parser cannot execute a non-existent script");
        }
        try {
            do {
                if (this.stopped) {
                    return;
                }
                switch (this.st.nextToken()) {
                    case 10: {
                        ++this.lineno;
                        break;
                    }
                    case -1: {
                        return;
                    }
                    case -2: {
                        this.stack.push(new Double(this.st.nval));
                        break;
                    }
                    case -3: {
                        if (this.st.sval.equals("needs") || this.st.sval.equals("needsGlobal")) {
                            boolean bl;
                            boolean bl2 = bl = !this.st.sval.equals("needs");
                            if (this.st.nextToken() == -3) {
                                if (bl) {
                                    DawnParser.installPackage(this.st.sval);
                                    break;
                                }
                                DawnParser.installPackage(class$org$jext$dawn$DawnParser == null ? DawnParser.class$("org.jext.dawn.DawnParser") : class$org$jext$dawn$DawnParser, this.st.sval, this);
                                break;
                            }
                            this.st.pushBack();
                            throw new DawnRuntimeException(this, "bad usage of 'needs' or 'needsGlobal'reserved keyword");
                        }
                        Function function = (Function)functions.get(this.st.sval);
                        if (function != null) {
                            function.invoke(this);
                            break;
                        }
                        function = (Function)this.runtimeFunctions.get(this.st.sval);
                        if (function != null) {
                            function.invoke(this);
                            break;
                        }
                        this.stack.push(this.st.sval);
                        break;
                    }
                    case 45: {
                        Function function;
                        if (this.st.nextToken() == -3) {
                            function = (Function)functions.get("" + '-' + this.st.sval);
                            if (function == null && (function = (Function)this.runtimeFunctions.get("" + '-' + this.st.sval)) == null) {
                                this.st.pushBack();
                                function = (Function)functions.get("-");
                            }
                        } else {
                            this.st.pushBack();
                            function = (Function)functions.get("-");
                        }
                        if (function == null) break;
                        function.invoke(this);
                        break;
                    }
                    case 34: 
                    case 39: {
                        this.pushString(this.st.sval);
                    }
                }
            } while (true);
        }
        catch (IOException var1_3) {
            throw new DawnRuntimeException(this, "unexpected error occured during parsing");
        }
    }

    public Hashtable getVariables() {
        return this.runtimeVariables;
    }

    public Hashtable getGlobalVariables() {
        return variables;
    }

    public Object getVariable(String string) {
        Object v = variables.get(string);
        if (v == null) {
            v = this.runtimeVariables.get(string);
        }
        return v;
    }

    public void setVariable(String string, Object object) {
        if (object == null) {
            this.runtimeVariables.remove(string);
        } else if (!(functions.contains(string) || this.runtimeFunctions.contains(string))) {
            this.runtimeVariables.put(string, object);
        }
    }

    public static void setGlobalVariable(String string, Object object) {
        if (object == null) {
            variables.remove(string);
        } else if (!functions.contains(string)) {
            variables.put(string, object);
        }
    }

    public static void clearGlobalVariables() {
        variables.clear();
    }

    public int lineno() {
        return this.lineno;
    }

    public String dump() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < this.stack.size(); ++i) {
            stringBuffer.append(this.stack.size() - 1 - i).append(':');
            Object e = this.stack.elementAt(i);
            if (e instanceof Vector) {
                stringBuffer.append("array[").append(((Vector)e).size()).append(']');
            } else {
                stringBuffer.append(e);
            }
            stringBuffer.append('\n');
        }
        return stringBuffer.toString();
    }

    public double popNumber() throws DawnRuntimeException {
        this.checkEmpty(null);
        Object e = this.stack.pop();
        if (!(e instanceof Double)) {
            throw new DawnRuntimeException(this, "bad argument type");
        }
        return (Double)e;
    }

    public double peekNumber() throws DawnRuntimeException {
        this.checkEmpty(null);
        Object e = this.stack.peek();
        if (!(e instanceof Double)) {
            throw new DawnRuntimeException(this, "bad argument type");
        }
        return (Double)e;
    }

    public void pushNumber(double d) {
        this.stack.push(new Double(d));
    }

    public String popString() throws DawnRuntimeException {
        this.checkEmpty(null);
        String string = this.stack.pop().toString();
        if (string.length() != 0 && string.startsWith("\"") && string.endsWith("\"")) {
            string = string.substring(1, string.length() - 1);
        }
        return string;
    }

    public String peekString() throws DawnRuntimeException {
        this.checkEmpty(null);
        String string = this.stack.peek().toString();
        if (string.length() != 0 && string.startsWith("\"") && string.endsWith("\"")) {
            string = string.substring(1, string.length() - 1);
        }
        return string;
    }

    public void pushString(String string) {
        if (string.length() == 2 && string.charAt(0) == '\"' && string.charAt(1) == '\"') {
            this.stack.push("\"\"");
        } else {
            this.stack.push("" + '\"' + string + '\"');
        }
    }

    public Vector popArray() throws DawnRuntimeException {
        this.checkEmpty(null);
        Object e = this.stack.pop();
        if (!(e instanceof Vector)) {
            throw new DawnRuntimeException(this, "bad argument type");
        }
        return (Vector)e;
    }

    public Vector peekArray() throws DawnRuntimeException {
        this.checkEmpty(null);
        Object e = this.stack.peek();
        if (!(e instanceof Vector)) {
            throw new DawnRuntimeException(this, "bad argument type");
        }
        return (Vector)e;
    }

    public void pushArray(Vector vector) {
        this.stack.push(vector);
    }

    public Object pop() throws DawnRuntimeException {
        this.checkEmpty(null);
        return this.stack.pop();
    }

    public Object peek() throws DawnRuntimeException {
        this.checkEmpty(null);
        return this.stack.peek();
    }

    public void push(Object object) {
        this.stack.push(object);
    }

    public boolean isTopNumeric() {
        return this.stack.peek() instanceof Double;
    }

    public boolean isTopString() {
        String string;
        Object e = this.stack.peek();
        if (e instanceof String && (string = (String)e).startsWith("\"") && string.endsWith("\"")) {
            return true;
        }
        return false;
    }

    public boolean isTopArray() {
        return this.stack.peek() instanceof Vector;
    }

    public boolean isTopLiteral() {
        return !this.isTopString() && !this.isTopNumeric() && !this.isTopArray();
    }

    public int getTopType() {
        if (this.isTopNumeric()) {
            return 0;
        }
        if (this.isTopString()) {
            return 1;
        }
        if (this.isTopArray()) {
            return 3;
        }
        return 2;
    }

    public static void addGlobalFunction(Function function) {
        if (function == null) {
            return;
        }
        String string = function.getName();
        if (!(string.equals("needs") || string.equals("needsGlobal"))) {
            functions.put(string, function);
        }
    }

    public void addRuntimeFunction(Function function) {
        if (function == null) {
            return;
        }
        String string = function.getName();
        if (!(string.equals("needs") || string.equals("needsGlobal"))) {
            this.runtimeFunctions.put(string, function);
        }
    }

    public Function createOnFlyFunction(final String string) {
        return new Function(){

            public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
                StreamTokenizer streamTokenizer = DawnParser.this.st;
                Hashtable hashtable = (Hashtable)DawnParser.this.runtimeVariables.clone();
                DawnParser.this.st = DawnParser.this.createTokenizer(new StringReader(string));
                DawnParser.this.exec();
                DawnParser.this.st = streamTokenizer;
                Enumeration enumeration = DawnParser.this.runtimeVariables.keys();
                while (enumeration.hasMoreElements()) {
                    String string2 = (String)enumeration.nextElement();
                    if (hashtable.get(string2) == null) continue;
                    hashtable.put(string2, DawnParser.this.runtimeVariables.get(string2));
                }
                DawnParser.this.runtimeVariables = (Hashtable)hashtable.clone();
            }
        };
    }

    public static void createGlobalFunction(String string, final String string2) {
        if (string == null || string.length() == 0 || string.equals("needs") || string.equals("needsGlobal") || string2 == null) {
            return;
        }
        functions.put(string, new Function(string){

            public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
                StreamTokenizer streamTokenizer = dawnParser.getStream();
                dawnParser.setStream(dawnParser.createTokenizer(new StringReader(string2)));
                dawnParser.exec();
                dawnParser.setStream(streamTokenizer);
            }
        });
    }

    public void createRuntimeFunction(String string, final String string2) {
        if (string == null || string.length() == 0 || string.equals("needs") || string.equals("needsGlobal") || string2 == null) {
            return;
        }
        this.runtimeFunctions.put(string, new Function(string){

            public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
                StreamTokenizer streamTokenizer = DawnParser.this.st;
                DawnParser.this.st = DawnParser.this.createTokenizer(new StringReader(string2));
                DawnParser.this.exec();
                DawnParser.this.st = streamTokenizer;
            }
        });
    }

}

