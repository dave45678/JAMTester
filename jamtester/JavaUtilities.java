/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  com.sun.tools.javac.Main
 *  junit.runner.TestCaseClassLoader
 */
package jamtester;

import com.sun.tools.javac.Main;
import jamtester.GradingToolHelper;
import jamtester.NullOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import junit.runner.TestCaseClassLoader;

public class JavaUtilities {
    private static PrintStream out = NullOutputStream.PRINTSTREAM;
    private static Process p;
    private static File[] classpaths;
    private static File outputLocation;
    private static File theFile;
    private static boolean worked;
    private static String error;

    public static boolean compile(File file, File[] arrfile, File file2, GradingToolHelper gradingToolHelper) {
        out.println("Running with: " + file + " " + file2);
        classpaths = arrfile;
        outputLocation = file2;
        theFile = file;
        worked = false;
        System.err.println("Compiling");
        Thread thread = new Thread(){

            public void run() {
                Object object;
                int n;
                ArrayList<Object> arrayList = new ArrayList<Object>();
                Main main = new Main();
                int n2 = 0;
                Runtime runtime = Runtime.getRuntime();
                arrayList.add("-classpath");
                StringBuffer stringBuffer = new StringBuffer("." + System.getProperty("path.separator"));
                for (n = 0; classpaths != null && n < classpaths.length; ++n) {
                    if (classpaths[n] != null && classpaths[n].getName().toLowerCase().endsWith(".class")) {
                        JavaUtilities.access$000()[n] = classpaths[n].getParentFile();
                    }
                    object = classpaths[n].toString();
                    if (classpaths[n] == null) continue;
                    stringBuffer.append((String)object + System.getProperty("path.separator"));
                }
                stringBuffer.append("%CLASSPATH%");
                arrayList.add("\"" + stringBuffer + "\"");
                if (outputLocation != null) {
                    arrayList.add("-d");
                    arrayList.add(outputLocation.toString());
                }
                if (theFile.isDirectory()) {
                    n = 1;
                    object = JavaUtilities.getFilesToCompile(theFile);
                    for (int i = 0; i < object.size(); ++i) {
                        arrayList.add(object.get(i));
                    }
                } else {
                    arrayList.add(theFile.getAbsolutePath());
                }
                System.err.println("The thing to be compiled is: " + theFile);
                arrayList.add(0, "-nowarn");
                StringWriter stringWriter = new StringWriter();
                try {
                    object = new PrintWriter(stringWriter);
                    String[] arrstring = new String[arrayList.size()];
                    for (int i = 0; i < arrayList.size(); ++i) {
                        arrstring[i] = arrayList.get(i).toString();
                        out.println(arrstring[i]);
                    }
                    File file = theFile.isDirectory() ? theFile : new File(System.getProperty("user.dir"));
                    out.println("Got here " + file.toString());
                    n2 = Main.compile((String[])arrstring, (PrintWriter)object);
                    out.println(stringWriter);
                    error = stringWriter.toString();
                    error = error.replaceAll("Note: .*", "");
                    error = error.trim();
                    stringWriter.close();
                    out.println("Got here2");
                    System.err.println("ERROR: " + error);
                    System.err.println("RESULT: " + n2);
                    worked = error.length() == 0;
                }
                catch (Exception var7_9) {
                    System.err.println("Caught an exception");
                    System.err.println(var7_9);
                    worked = false;
                    error = stringWriter.toString();
                }
            }
        };
        gradingToolHelper.addThread(thread);
        thread.start();
        try {
            thread.join();
        }
        catch (Exception var5_5) {
            // empty catch block
        }
        System.err.println("Done Compiling");
        return worked;
    }

    private static String concatenateArray(Object[] arrobject) {
        String string = arrobject[0].toString();
        for (int i = 1; i < arrobject.length; ++i) {
            string = string + '\n' + arrobject[i].toString();
        }
        return string;
    }

    public static String getMostRecentOutput() {
        return error;
    }

    public static ArrayList getFilesToCompile(File file) {
        ArrayList<File> arrayList = new ArrayList<File>();
        File[] arrfile = file.listFiles(new FilenameFilter(){

            public boolean accept(File file, String string) {
                return string.toLowerCase().endsWith(".java") || new File(file, string).isDirectory();
            }
        });
        for (int i = 0; i < arrfile.length; ++i) {
            if (!arrfile[i].isDirectory()) {
                arrayList.add(arrfile[i]);
                continue;
            }
            ArrayList arrayList2 = JavaUtilities.getFilesToCompile(arrfile[i]);
            for (int j = 0; j < arrayList2.size(); ++j) {
                arrayList.add((File)arrayList2.get(j));
            }
        }
        return arrayList;
    }

    public static void reloadClasses(File[] arrfile) {
        for (int i = 0; i < arrfile.length; ++i) {
            out.println(arrfile[i]);
            try {
                TestCaseClassLoader testCaseClassLoader = new TestCaseClassLoader();
                if (!arrfile[i].isDirectory()) continue;
                JavaUtilities.loadClasses(testCaseClassLoader);
                continue;
            }
            catch (Exception var2_3) {
                var2_3.printStackTrace(out);
            }
        }
    }

    private static void loadClasses(TestCaseClassLoader testCaseClassLoader) {
        File[] arrfile = JavaUtilities.getClassFiles();
        for (int i = 0; i < arrfile.length; ++i) {
            out.println(arrfile[i]);
            try {
                testCaseClassLoader.loadClass(arrfile[i].getName().substring(0, arrfile[i].getName().length() - ".class".length()), false);
                continue;
            }
            catch (Exception var3_3) {
                var3_3.printStackTrace(out);
            }
        }
    }

    public static void reloadClasses(File file) {
        File[] arrfile = new File[]{file};
        JavaUtilities.reloadClasses(arrfile);
    }

    public static void reloadClasses() {
        JavaUtilities.reloadClasses(new File(System.getProperty("user.dir")));
    }

    public static File[] getClassFiles() {
        FileFilter fileFilter = new FileFilter(){

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
                if (file.getName().toLowerCase().endsWith(".csv")) {
                    return false;
                }
                return file.getName().toLowerCase().endsWith(".class");
            }
        };
        File file = new File(System.getProperty("user.dir"));
        return file.listFiles(fileFilter);
    }

    public static String readFile(File file) {
        String string = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] arrby = new byte[fileInputStream.available()];
            fileInputStream.read(arrby);
            fileInputStream.close();
            string = new String(arrby);
        }
        catch (Exception var2_3) {
            // empty catch block
        }
        return string;
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
    }

}

