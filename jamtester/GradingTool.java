/*
 * Decompiled with CFR 0_102.
 */
package jamtester;

import jamtester.GradingToolGui;
import jamtester.GradingToolHelper;
import jamtester.JAMProperties;
import jamtester.NullOutputStream;
import jamtester.Results;
import jamtester.StudentResult;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class GradingTool {
    public static File testFile;
    private static File toCompile;
    public static File[] classpaths;
    private static Class testClass;
    private static PrintStream out;
    public static FileFilter javaFiles;
    public static FileFilter classFiles;
    private static Thread p;
    private static boolean cancelled;
    private static boolean compiled;
    private static FilenameFilter filter;
    private static File dir;
    private static int x;
    private static File saveFile;
    private static int numFolders;
    public static int numSteps;
    public static PrintStream fileout;
    private static GradingToolHelper currentHelper;
    private static boolean stop;

    public static File[] showOpenDialog(String string, boolean bl, boolean bl2, FileFilter fileFilter, boolean bl3) {
        JFileChooser jFileChooser = new JFileChooser();
        JAMProperties jAMProperties = JAMProperties.loadProperties();
        String string2 = jAMProperties.getLastFolder();
        File file = null;
        if (string2 != null) {
            file = new File(string2);
        }
        if (!(file == null || file.isDirectory())) {
            file = file.getParentFile();
        }
        if (string2 != null && !string2.equals("") && file != null && file.exists()) {
            jFileChooser.setCurrentDirectory(file);
        }
        if (bl2) {
            jFileChooser.setFileSelectionMode(1);
        }
        jFileChooser.setFileFilter(fileFilter);
        jFileChooser.setName(string);
        jFileChooser.setMultiSelectionEnabled(bl);
        int n = jFileChooser.showOpenDialog(null);
        if (!(bl3 || n != 1)) {
            System.exit(0);
        }
        if (bl) {
            return jFileChooser.getSelectedFiles();
        }
        File[] arrfile = new File[]{jFileChooser.getSelectedFile()};
        jAMProperties = JAMProperties.loadProperties();
        jAMProperties.setLastFolder(arrfile[0].getAbsolutePath());
        jAMProperties.save();
        return arrfile;
    }

    public static Results goThruDirsInDir(File file, File file2, File file3, GradingToolGui gradingToolGui) {
        return GradingTool.goThruDirsInDir(file, file2, file3, gradingToolGui, new Results());
    }

    public static Results goThruDirsInDir(File file, File file2, File file3, GradingToolGui gradingToolGui, final Results results) {
        int n;
        results.setDirectory(file.getAbsolutePath());
        results.setRunning(true);
        stop = false;
        gradingToolGui.setCompleted(0);
        results.setMethodNames(file2);
        numSteps = 2;
        PrintStream printStream = System.out;
        dir = file;
        saveFile = file3;
        testFile = file2;
        FileOutputStream fileOutputStream = null;
        if (file3 == null) {
            fileout = NullOutputStream.PRINTSTREAM;
            System.setOut(fileout);
        } else {
            try {
                fileOutputStream = new FileOutputStream(file3);
                fileout = new PrintStream(fileOutputStream);
                System.setOut(fileout);
            }
            catch (Exception var7_7) {
                // empty catch block
            }
        }
        System.setOut(printStream);
        compiled = false;
        filter = new FilenameFilter(){

            public boolean accept(File file, String string) {
                return new File(file, string).isDirectory();
            }
        };
        numFolders = file.listFiles(filter).length;
        int n2 = 0;
        for (n = 0; n < file.listFiles(filter).length; ++n) {
            StringTokenizer stringTokenizer = new StringTokenizer(file.listFiles(filter)[n].getName().replaceAll(",", ""));
            if (stringTokenizer.countTokens() <= n2) continue;
            n2 = stringTokenizer.countTokens();
        }
        gradingToolGui.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent windowEvent) {
                stop = true;
                while (results.isRunning()) {
                    GradingTool.killCurrent();
                }
            }
        });
        results.setColumnCount(n2);
        for (n = 0; !(n >= file.listFiles(filter).length || stop); ++n) {
            gradingToolGui.setMax(file.listFiles(filter).length * numSteps);
            x = n;
            gradingToolGui.setCurrent(file.listFiles(filter)[n].getName());
            compiled = GradingTool.runTest(dir.listFiles(filter)[x], GradingTool.prepareTestFile(testFile), !compiled, saveFile, gradingToolGui, results) || compiled;
            gradingToolGui.setCompleted((n + 1) * numSteps);
        }
        try {
            fileOutputStream.close();
        }
        catch (Exception var8_10) {
            // empty catch block
        }
        System.setOut(printStream);
        gradingToolGui.notifyComplete();
        results.setRunning(false);
        return results;
    }

    public static Results gradeSansGui(File file, File file2, File file3, File[] arrfile) {
        int n;
        ArrayList<File> arrayList = new ArrayList<File>();
        for (n = 0; n < arrfile.length; ++n) {
            arrayList.add(arrfile[n]);
        }
        arrayList.add(new File("junit.jar"));
        arrfile = new File[arrayList.size()];
        for (n = 0; n < arrayList.size(); ++n) {
            arrfile[n] = (File)arrayList.get(n);
        }
        classpaths = arrfile;
        GradingToolGui gradingToolGui = new GradingToolGui(false);
        gradingToolGui.show();
        return GradingTool.goThruDirsInDir(file, file2, file3, gradingToolGui);
    }

    public static Results testOneSansGui(Results results, StudentResult studentResult) {
        return GradingTool.testOneSansGui(results, studentResult, false);
    }

    public static Results testOneSansGui(Results results, StudentResult studentResult, boolean bl) {
        GradingTool.testOneSansGui(studentResult.getDir(), results.getTestFile(), results.getClassPath(), results, bl);
        return results;
    }

    public static Results testOneSansGui(File file, File file2, File[] arrfile) {
        return GradingTool.testOneSansGui(file, file2, arrfile, new Results());
    }

    public static Results testOneSansGui(File file, File file2, File[] arrfile, Results results) {
        return GradingTool.testOneSansGui(file, file2, arrfile, results, false);
    }

    public static Results testOneSansGui(File file, File file2, File[] arrfile, Results results, boolean bl) {
        return GradingTool.testOneSansGui(file, file2, arrfile, results, bl, false, null);
    }

    public static Results testOneSansGui(File file, File file2, File[] arrfile, Results results, boolean bl, boolean bl2, File file3) {
        int n;
        results.setMethodNames(file2, bl2);
        results.setRunning(true);
        numFolders = 1;
        ArrayList<File> arrayList = new ArrayList<File>();
        for (n = 0; n < arrfile.length; ++n) {
            arrayList.add(arrfile[n]);
        }
        arrayList.add(new File("junit.jar"));
        arrfile = new File[arrayList.size()];
        for (n = 0; n < arrayList.size(); ++n) {
            arrfile[n] = (File)arrayList.get(n);
        }
        classpaths = arrfile;
        GradingToolGui gradingToolGui = new GradingToolGui(false, bl);
        gradingToolGui.show();
        final Results results2 = results;
        gradingToolGui.setCompleted(0);
        numSteps = 2;
        PrintStream printStream = System.out;
        dir = file;
        testFile = file2;
        FileOutputStream fileOutputStream = null;
        if (saveFile == null) {
            fileout = NullOutputStream.PRINTSTREAM;
            System.setOut(fileout);
        } else {
            try {
                fileOutputStream = new FileOutputStream(saveFile);
                fileout = new PrintStream(fileOutputStream);
                System.setOut(fileout);
            }
            catch (Exception var12_13) {
                // empty catch block
            }
        }
        System.setOut(printStream);
        compiled = false;
        gradingToolGui.setMax(numSteps);
        gradingToolGui.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent windowEvent) {
                while (results2.isRunning()) {
                    GradingTool.killCurrent();
                }
            }
        });
        GradingTool.runTest(file, file2, true, null, gradingToolGui, results2, bl, bl2, file3);
        try {
            fileOutputStream.close();
        }
        catch (Exception var12_14) {
            // empty catch block
        }
        System.setOut(printStream);
        gradingToolGui.notifyComplete();
        results.setRunning(false);
        return results2;
    }

    public static void killCurrent() {
        try {
            currentHelper.killRunningThreads();
        }
        catch (ThreadDeath var0) {
            cancelled = true;
            fileout.println(dir.getName() + ",ERROR: Cancelled");
        }
        catch (Exception var0_1) {
            // empty catch block
        }
    }

    private static File prepareTestFile(File file) {
        return file;
    }

    public static void setNumSteps(int n, GradingToolGui gradingToolGui) {
        numSteps = n;
        gradingToolGui.setMax(numFolders * numSteps);
    }

    private static boolean runTest(File file, File file2, boolean bl, File file3, GradingToolGui gradingToolGui, Results results) {
        return GradingTool.runTest(file, file2, bl, file3, gradingToolGui, results, false);
    }

    private static boolean runTest(File file, File file2, boolean bl, File file3, GradingToolGui gradingToolGui, Results results, boolean bl2) {
        return GradingTool.runTest(file, file2, bl, file3, gradingToolGui, results, bl2, false, null);
    }

    private static boolean runTest(File file, File file2, boolean bl, File file3, GradingToolGui gradingToolGui, Results results, boolean bl2, boolean bl3, File file4) {
        GradingToolHelper gradingToolHelper;
        currentHelper = gradingToolHelper = new GradingToolHelper(gradingToolGui);
        gradingToolHelper.sysout = fileout;
        gradingToolHelper.classpaths = classpaths;
        return gradingToolHelper.runTest(file, file2, bl, file3, results, bl2, bl3, file4);
    }

    private static void printHeader(Class class_) {
        System.out.print(class_.getName() + ",");
        StringBuffer stringBuffer = new StringBuffer("Name,#Correct/Total,%,");
        for (int i = 0; i < class_.getMethods().length; ++i) {
            if (!class_.getMethods()[i].getName().startsWith("test")) continue;
            stringBuffer.append(class_.getMethods()[i].getName() + ",");
        }
        System.out.println("Tested:  " + Calendar.getInstance().getTime().toString());
        System.out.println(stringBuffer);
    }

    static {
        numSteps = 2;
        javaFiles = new FileFilter(){

            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toUpperCase().endsWith(".JAVA");
            }

            public String getDescription() {
                return "Java files (*.java)";
            }
        };
        classFiles = new FileFilter(){

            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toUpperCase().endsWith(".CLASS") || file.getName().toUpperCase().endsWith(".JAR");
            }

            public String getDescription() {
                return "Java Classes or Class libraries (*.jar and *.class)";
            }
        };
    }

}

