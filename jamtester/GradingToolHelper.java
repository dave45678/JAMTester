/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  junit.framework.Test
 *  junit.framework.TestResult
 *  junit.framework.TestSuite
 */
package jamtester;

import jamtester.CompileTestResult;
import jamtester.GradingTool;
import jamtester.GradingToolClassLoader;
import jamtester.GradingToolGui;
import jamtester.JavaUtilities;
import jamtester.NullOutputStream;
import jamtester.OutputTextbox;
import jamtester.Results;
import jamtester.StudentResult;
import jamtester.TestResult;
import jamtester.coverage.Coverage;
import jamtester.coverage.CoverageTestResult;
import jamtester.javatools.JAMWindowBlocker;
import jamtester.javatools.JavaParser;
import java.io.File;
import java.io.FileFilter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JCheckBoxMenuItem;
import junit.framework.Test;
import junit.framework.TestSuite;

public class GradingToolHelper {
    public File[] classpaths;
    public static PrintStream out = NullOutputStream.PRINTSTREAM;
    private ArrayList runningThreads = new ArrayList();
    public PrintStream sysout;
    private String className;
    private Method[] methods;
    private int outOf;
    private Method setup;
    private Method teardown;
    private int worked;
    private StringBuffer results;
    private int x;
    private Object tester;
    private Thread cur;
    private junit.framework.TestResult curTest;
    private StudentResult res;
    private GradingToolGui theGui;
    private ArrayList testres;
    private TestSuite suite;
    private static String[] testNames;
    private StudentResult sr;
    public static GradingToolClassLoader loader;
    private static boolean cancelled;

    public GradingToolHelper(GradingToolGui gradingToolGui) {
        this.theGui = gradingToolGui;
    }

    private void printHeader(Class class_) {
        int n;
        ArrayList<String> arrayList = new ArrayList<String>();
        this.sysout.print(class_.getName() + ",");
        StringBuffer stringBuffer = new StringBuffer("Name,#Correct/Total,%,");
        for (n = 0; n < class_.getMethods().length; ++n) {
            if (!class_.getMethods()[n].getName().startsWith("test")) continue;
            stringBuffer.append(class_.getMethods()[n].getName() + ",");
            arrayList.add(class_.getMethods()[n].getName());
        }
        testNames = new String[arrayList.size()];
        for (n = 0; n < arrayList.size(); ++n) {
            GradingToolHelper.testNames[n] = arrayList.get(n).toString();
        }
        this.sysout.println("Tested:  " + Calendar.getInstance().getTime().toString());
        this.sysout.println(stringBuffer);
    }

    public void addThread(Thread thread) {
        this.runningThreads.add(thread);
    }

    public void killRunningThreads(boolean bl) {
        if (this.cur != null) {
            try {
                String string = "Test Cancelled by " + System.getProperty("user.name");
                this.cur.stop();
                this.curTest.addError(this.suite.testAt(this.x), (Throwable)new Exception(string));
                cancelled = true;
            }
            catch (Exception var2_3) {
                // empty catch block
            }
            return;
        }
        for (int i = 0; i < this.runningThreads.size(); ++i) {
            try {
                ((Thread)this.runningThreads.get(i)).stop();
                continue;
            }
            catch (Exception var3_5) {
                // empty catch block
            }
        }
    }

    public void killRunningThreads() {
        this.killRunningThreads(false);
    }

    public boolean runTest(File file, File file2, boolean bl, File file3, Results results, boolean bl2) {
        return this.runTest(file, file2, bl, file3, results, bl2, false, null);
    }

    public boolean runTest(File file, File file2, boolean bl, File file3, Results results, final boolean bl2, boolean bl3, File file4) {
        URL[] arruRL;
        Object object;
        int n;
        String string;
        Class class_;
        Thread[] arrthread;
        Object object2;
        Object object3;
        Runtime.getRuntime().gc();
        File file5 = file;
        File file6 = GradingTool.testFile;
        String string2 = "";
        Serializable serializable = null;
        Coverage coverage = null;
        if (bl3) {
            int n2;
            coverage = new Coverage(file.getName());
            coverage.delete();
            coverage = new Coverage(file.getName());
            coverage.addFile(file);
            coverage.addFile(file2);
            coverage.augment();
            file = coverage.getDirectory();
            string = "";
            try {
                string = Coverage.findClassName(file2);
            }
            catch (Exception var15_15) {
                // empty catch block
            }
            while ((n2 = string.indexOf(46)) >= 0) {
                string = string.substring(0, n2) + File.separator + string.substring(n2 + 1);
            }
            string = File.separator + string;
            System.err.println("AAAAAAAAARGH! " + string);
            file2 = new File(file, string + ".java");
            try {
                string2 = Coverage.findClassName(file4);
            }
            catch (Exception var16_18) {
                // empty catch block
            }
        }
        if (GradingToolGui.outputOn.getState()) {
            out = new PrintStream(new OutputTextbox("Helper for " + file.getName()), true);
        }
        GradingTool.fileout = out;
        out.println("Original:  " + file6);
        this.sr = new StudentResult(file.getName(), false, null, results);
        this.sr.setDirectory(file);
        this.sr.setTheDir(file.getAbsolutePath());
        results.add(this.sr);
        results.setData(file2, this.classpaths);
        out.println(System.getProperty("user.dir"));
        out.println(System.getProperty("java.class.path"));
        string = null;
        out.println(file);
        StringBuffer stringBuffer = new StringBuffer(file.getName().replaceAll(",", "") + ",");
        String string3 = System.getProperty("java.class.path");
        boolean bl4 = true;
        System.err.println("Currently Working on:   " + file.getName());
        if (!JavaUtilities.compile(file, this.classpaths, file, this)) {
            out.println("" + file + " " + file5);
            out.println(JavaUtilities.getMostRecentOutput());
            if (bl3) {
                JavaUtilities.compile(file5, this.classpaths, file, this);
            }
            System.out.println(file.getName().replaceAll(",", "") + ",ERROR");
            this.sr.addResult(new CompileTestResult(JavaUtilities.getMostRecentOutput()));
            System.err.println(JavaUtilities.getMostRecentOutput());
            if (bl3) {
                coverage.delete();
            }
            return false;
        }
        this.theGui.increment();
        ArrayList<File> arrayList = new ArrayList<File>();
        for (int i = 0; i < this.classpaths.length; ++i) {
            arrayList.add(this.classpaths[i]);
        }
        arrayList.add(file);
        File[] arrfile = new File[arrayList.size()];
        for (int j = 0; j < arrayList.size(); ++j) {
            arrfile[j] = (File)arrayList.get(j);
        }
        if (!JavaUtilities.compile(file2, arrfile, file, this)) {
            out.println("" + file + " " + file5);
            out.println(JavaUtilities.getMostRecentOutput());
            JavaUtilities.getMostRecentOutput();
            if (bl3) {
                JavaUtilities.compile(file6, arrfile, file, this);
            }
            out.println(JavaUtilities.getMostRecentOutput());
            System.out.println(file.getName().replaceAll(",", "") + ",ERROR");
            System.err.println("Test file failed to compile");
            this.sr.addResult(new CompileTestResult(JavaUtilities.getMostRecentOutput()));
            System.err.println(JavaUtilities.getMostRecentOutput());
            if (bl3) {
                coverage.delete();
            }
            return false;
        }
        this.theGui.increment();
        this.className = JavaParser.parseFile(file2).getQualifiedClassName();
        Class class_2 = null;
        this.tester = null;
        this.setup = null;
        this.teardown = null;
        GradingToolClassLoader gradingToolClassLoader = null;
        try {
            int n3;
            out.println(this.className);
            System.err.println("TEST CLASS NAME: " + this.className);
            object = file.toString();
            object3 = new ArrayList<File>();
            arrthread = new Thread[]();
            for (n3 = 0; n3 < arrfile.length; ++n3) {
                File file7 = arrfile[n3];
                if (file7.getName().toLowerCase().endsWith(".class")) {
                    file7 = file7.getParentFile();
                }
                if (!arrthread.contains(file7)) {
                    arrthread.add((URL)file7.toURL());
                }
                if (object3.contains(file7) || !file7.isDirectory()) continue;
                object3.add((File)file7);
            }
            for (n3 = 0; n3 < object3.size(); ++n3) {
                object = (String)object + System.getProperty("path.separator") + ((File)object3.get(n3)).getAbsolutePath();
            }
            arrthread.add((URL)new File(System.getProperty("user.dir")).toURL());
            arruRL = new URL[arrthread.size()];
            out.println(arrthread);
            for (int k = 0; k < arrthread.size(); ++k) {
                arruRL[k] = (URL)arrthread.get(k);
            }
            out.println("Class Loader String:  " + (String)object);
            gradingToolClassLoader = GradingToolHelper.loader = new GradingToolClassLoader((String)object, arruRL);
            out.println("Here at " + new Date());
            if (bl3) {
                loader.loadClass(string2);
                serializable = coverage.getCoverageTestResult(gradingToolClassLoader.getLoadedClasses(), file4, file5);
                this.sr.addResult((TestResult)serializable);
            }
            class_ = gradingToolClassLoader.loadClass("junit.framework.TestResult");
            class_2 = gradingToolClassLoader.loadClass(this.className);
            out.println("Here at " + new Date());
            System.err.println(class_2);
            if (bl) {
                this.printHeader(class_2);
            }
        }
        catch (Exception var22_28) {
            var22_28.printStackTrace(System.err);
            var22_28.printStackTrace(out);
        }
        this.methods = class_2.getMethods();
        GradingTool.setNumSteps(this.numTests(this.methods) + 2, this.theGui);
        this.worked = 0;
        this.outOf = 0;
        this.results = new StringBuffer();
        object = System.out;
        this.testres = new ArrayList();
        this.suite = new TestSuite(class_2);
        results.setMethodNames(this.suite, bl3);
        this.methods = this.pruneMethods(this.methods);
        object3 = new JAMWindowBlocker(true);
        object3.attach();
        out.println("Before tests");
        arrthread = new Thread[this.suite.testCount()];
        arruRL = gradingToolClassLoader;
        class_ = serializable;
        Coverage coverage2 = coverage;
        this.x = 0;
        ArrayList<Exception> arrayList2 = new ArrayList<Exception>();
        cancelled = false;
        Runtime.getRuntime().gc();
        while (this.x < this.suite.testCount()) {
            this.cur = object2 = new Thread((JAMWindowBlocker)object3, bl3, (CoverageTestResult)class_, coverage2, (GradingToolClassLoader)arruRL, file5){
                private final /* synthetic */ JAMWindowBlocker val$blocker;
                private final /* synthetic */ boolean val$student;
                private final /* synthetic */ CoverageTestResult val$fctr;
                private final /* synthetic */ Coverage val$cov;
                private final /* synthetic */ GradingToolClassLoader val$gtcl;
                private final /* synthetic */ File val$origDir;

                public void run() {
                    while (GradingToolHelper.this.x < GradingToolHelper.this.suite.testCount()) {
                        try {
                            if (bl2 && testNames.length > 0) {
                                GradingToolHelper.this.theGui.setCurrent(testNames[GradingToolHelper.this.x]);
                            }
                            junit.framework.TestResult testResult = new junit.framework.TestResult();
                            if (!cancelled) {
                                GradingToolHelper.this.curTest = testResult;
                            }
                            if (!cancelled) {
                                try {
                                    GradingToolHelper.out.println("Starting to run test " + GradingToolHelper.this.x + " at " + new Date());
                                    GradingToolHelper.this.suite.runTest(GradingToolHelper.this.suite.testAt(GradingToolHelper.this.x), GradingToolHelper.this.curTest);
                                    GradingToolHelper.out.println("Done running test " + GradingToolHelper.this.x + " at " + new Date());
                                    this.val$blocker.closeAllWindows();
                                }
                                catch (Exception var2_4) {
                                    var2_4.printStackTrace(GradingToolHelper.out);
                                }
                                catch (Throwable var2_5) {
                                    // empty catch block
                                }
                            }
                            cancelled = false;
                            if (testResult.wasSuccessful()) {
                                GradingToolHelper.this.worked++;
                                GradingToolHelper.this.results.append("Y, ");
                            } else {
                                GradingToolHelper.this.results.append("N, ");
                            }
                            GradingToolHelper.this.theGui.increment();
                            GradingToolHelper.this.sr.addResult(new TestResult(testNames[GradingToolHelper.this.x], GradingToolHelper.this.curTest));
                        }
                        catch (Exception var1_2) {
                            var1_2.printStackTrace(GradingToolHelper.out);
                        }
                        if (this.val$student) {
                            this.val$fctr.setInfos(this.val$cov.getAllInfos(this.val$gtcl.getLoadedClasses(), this.val$origDir));
                        }
                        GradingToolHelper.out.println("Test number: " + GradingToolHelper.this.x);
                        GradingToolHelper.this.x++;
                    }
                }
            };
            try {
                object2.start();
                object2.join();
            }
            catch (Exception var30_39) {
                var30_39.printStackTrace();
                arrayList2.add(var30_39);
            }
        }
        object3.detach();
        System.setOut((PrintStream)object);
        this.outOf = this.suite.testCount();
        out.println("" + this.worked + "/" + this.outOf + " tests were successful");
        stringBuffer.append("" + this.worked + " of " + this.outOf + "," + (double)this.worked / (double)this.outOf * 100.0 + "%,");
        stringBuffer.append(this.results);
        out.println(stringBuffer);
        out.println(stringBuffer);
        object2 = new FileFilter(){

            public boolean accept(File file) {
                if (file.getName().toLowerCase().startsWith("gradingtool")) {
                    return false;
                }
                if (file.getName().toLowerCase().startsWith("javautilities")) {
                    return false;
                }
                if (file.getName().toLowerCase().startsWith("outputtextbox")) {
                    return false;
                }
                if (file.getName().toLowerCase().startsWith("nulloutputstream")) {
                    return false;
                }
                if (file.getName().toLowerCase().startsWith("jamtester")) {
                    return false;
                }
                if (file.getName().toLowerCase().startsWith("results")) {
                    return false;
                }
                if (file.getName().toLowerCase().startsWith("jam")) {
                    return false;
                }
                if (file.getName().toLowerCase().startsWith("studentresult")) {
                    return false;
                }
                if (file.getName().toLowerCase().startsWith("testresult")) {
                    return false;
                }
                if (file.getName().toLowerCase().startsWith("tools.jar")) {
                    return false;
                }
                if (file.getName().toLowerCase().endsWith(".csv")) {
                    return false;
                }
                return file.getName().toLowerCase().endsWith(".class");
            }
        };
        File file8 = file;
        File[] arrfile2 = file8.listFiles((FileFilter)object2);
        for (n = 0; n < arrfile2.length; ++n) {
            arrfile2[n].delete();
        }
        for (n = 0; n < stringBuffer.length(); ++n) {
            this.sysout.print(stringBuffer.charAt(n));
        }
        this.sysout.print("\n");
        results.setName(this.suite.getName());
        if (bl3) {
            coverage.delete();
        }
        return bl4;
    }

    private Method[] pruneMethods(Method[] arrmethod) {
        Method[] arrmethod2 = new Method[this.numTests(arrmethod)];
        int n = 0;
        for (int i = 0; i < arrmethod.length; ++i) {
            if (!arrmethod[i].getName().startsWith("test") || arrmethod[i].getParameterTypes().length != 0) continue;
            arrmethod2[n++] = arrmethod[i];
        }
        return arrmethod2;
    }

    private int numTests(Method[] arrmethod) {
        int n = 0;
        for (int i = 0; i < arrmethod.length; ++i) {
            if (!arrmethod[i].getName().startsWith("test") || arrmethod[i].getParameterTypes().length != 0) continue;
            ++n;
        }
        return n;
    }

}

