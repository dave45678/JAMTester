/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  com.cenqua.clover.CloverInstr
 *  com.cenqua.clover.reporters.console.ConsoleReporter
 *  com.cenqua.clover.reporters.xml.XMLReporter
 */
package jamtester.coverage;

import com.cenqua.clover.CloverInstr;
import com.cenqua.clover.reporters.console.ConsoleReporter;
import com.cenqua.clover.reporters.xml.XMLReporter;
import jamtester.JavaUtilities;
import jamtester.NullOutputStream;
import jamtester.coverage.CoverageInfo;
import jamtester.coverage.CoverageTestResult;
import jamtester.javatools.JavaParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Coverage {
    private File directory;
    private String arrayName;
    private String lineListName;
    private PrintStream out;

    public Coverage(String string) {
        try {
            this.out = NullOutputStream.PRINTSTREAM;
            File file = File.createTempFile("jamcoverage", "tmp");
            this.directory = new File(file.getParent(), string);
            this.directory.mkdir();
            file.delete();
        }
        catch (Exception var2_3) {
            // empty catch block
        }
        this.arrayName = this.generateName();
        this.lineListName = this.arrayName + "lines";
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void addFile(File file) {
        if (file.isDirectory()) {
            ArrayList arrayList = JavaUtilities.getFilesToCompile(file);
            for (int i = 0; i < arrayList.size(); ++i) {
                this.addFile((File)arrayList.get(i));
            }
            return;
        }
        CloverInstr.mainImpl((String[])new String[]{Coverage.chooseJavaVersion(), "-i", new File(this.directory, "jamcoverage.db").getAbsolutePath(), "-d", this.directory.getAbsolutePath(), file.getAbsolutePath()});
        File file2 = new File(this.directory, file.getName());
        try {
            FileInputStream fileInputStream = new FileInputStream(file2);
            if (fileInputStream.available() == 0) {
                fileInputStream.close();
                FileInputStream fileInputStream2 = new FileInputStream(file);
                byte[] arrby = new byte[fileInputStream2.available()];
                fileInputStream2.read(arrby);
                fileInputStream2.close();
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                fileOutputStream.write(arrby);
                fileOutputStream.close();
                return;
            } else {
                fileInputStream.close();
            }
            return;
        }
        catch (Exception var3_6) {
            // empty catch block
        }
    }

    private String generateName() {
        StringBuffer stringBuffer = new StringBuffer("jamcoverage");
        for (int i = 0; i < 100; ++i) {
            stringBuffer.append((char)(97 + (int)(Math.random() * 26.0)));
        }
        return stringBuffer.toString();
    }

    public void augment() {
        ArrayList arrayList = JavaUtilities.getFilesToCompile(this.directory);
        for (int i = 0; i < arrayList.size(); ++i) {
            this.reAugment((File)arrayList.get(i));
        }
    }

    private String generateArrayCreation() {
        return "public static java.util.ArrayList " + this.arrayName + "=new java.util.ArrayList();";
    }

    private String generateLineList(Set set) {
        Iterator iterator = set.iterator();
        StringBuffer stringBuffer = new StringBuffer("public static Integer[] " + this.lineListName + "={");
        while (iterator.hasNext()) {
            stringBuffer.append("new Integer(" + iterator.next().toString() + ")");
            if (!iterator.hasNext()) continue;
            stringBuffer.append(",");
        }
        stringBuffer.append("};");
        return stringBuffer.toString();
    }

    private String generateCall(int n, Set set) {
        set.add(new Integer(n));
        return this.arrayName + ".add(new Integer(" + n + "));";
    }

    public CoverageInfo getCoverageInfo(Class class_, File file) {
        System.out.println(class_);
        try {
            ArrayList arrayList = (ArrayList)class_.getField(this.arrayName).get(null);
            Integer[] arrinteger = (Integer[])class_.getField(this.lineListName).get(null);
            TreeSet<Integer> treeSet = new TreeSet<Integer>(Arrays.asList(arrinteger));
            return new CoverageInfo(class_.getName(), treeSet, arrayList, file);
        }
        catch (Exception var3_4) {
            return null;
        }
    }

    private void reAugment(File file) {
        this.out.println("File before augmentation:  " + file);
        this.out.println(JavaUtilities.readFile(file));
        this.out.println("\n\n\nAFTER AUGMENTATION:");
        String string = "";
        try {
            string = JavaParser.parseFile(file).getClassName();
        }
        catch (Exception var3_3) {
            return;
        }
        Pattern pattern = Pattern.compile("[^+!]__CLOVER[^;]+\\+\\+;");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] arrby = new byte[fileInputStream.available()];
            fileInputStream.read(arrby);
            fileInputStream.close();
            String string2 = new String(arrby);
            Matcher matcher = pattern.matcher((CharSequence)string2);
            if (matcher.find()) {
                int n = 0;
                StringBuffer stringBuffer = new StringBuffer(string2.substring(0, n));
                TreeSet treeSet = new TreeSet();
                StringBuffer stringBuffer2 = new StringBuffer(string2.substring(n));
                matcher = pattern.matcher(stringBuffer2);
                while (matcher.find()) {
                    stringBuffer2 = new StringBuffer(matcher.replaceFirst("" + stringBuffer2.charAt(matcher.start()) + string + '.' + this.generateCall(this.countLinesToIndex(stringBuffer.toString(), stringBuffer2.toString(), matcher.start()), treeSet)));
                    matcher.reset(stringBuffer2);
                }
                stringBuffer2.insert(this.insideFirstBrace(stringBuffer2.toString()), this.generateLineList(treeSet) + this.generateArrayCreation());
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(("" + stringBuffer + stringBuffer2).toString().getBytes());
                fileOutputStream.close();
            }
        }
        catch (Exception var4_6) {
            // empty catch block
        }
        this.out.println(JavaUtilities.readFile(file));
    }

    private int insideFirstBrace(String string) {
        for (int i = 0; i < string.length() - 1; ++i) {
            if (string.charAt(i) == '{') {
                return i + 1;
            }
            if (string.charAt(i) != '/') continue;
            if (string.charAt(++i + 1) == '*') {
                while (i < string.length() - 1 && (string.charAt(i) != '*' || string.charAt(i + 1) != '/')) {
                    ++i;
                }
                ++i;
            }
            if (string.charAt(i + 1) != '/') continue;
            while (i < string.length() && string.charAt(i) != '\n') {
                ++i;
            }
        }
        return string.indexOf(123) + 1;
    }

    private int countLinesToIndex(String string, String string2, int n) {
        int n2;
        int n3 = 1;
        for (n2 = 0; n2 < string.length(); ++n2) {
            if (string.charAt(n2) != '\n') continue;
            ++n3;
        }
        for (n2 = 0; n2 < n && n2 < string2.length(); ++n2) {
            if (string2.charAt(n2) != '\n') continue;
            ++n3;
        }
        return n3;
    }

    public File getDirectory() {
        return this.directory;
    }

    public void delete() {
        this.delete(this.directory);
    }

    private void delete(File file) {
        if (file.isDirectory()) {
            File[] arrfile = file.listFiles();
            for (int i = 0; i < arrfile.length; ++i) {
                this.delete(arrfile[i]);
            }
        }
        file.delete();
    }

    public File createXMLOutput() {
        ConsoleReporter.main((String[])new String[]{"-i", new File(this.directory, "jamcoverage.db").getAbsolutePath()});
        XMLReporter.main((String[])new String[]{"-i", new File(this.directory, "jamcoverage.db").getAbsolutePath(), "-l", "-o", new File(this.directory, "jamcoverage.xml").getAbsolutePath()});
        return new File(this.directory, "jamcoverage.xml");
    }

    public static Map mapFilesToClassNames(File file) {
        HashMap hashMap = new HashMap();
        ArrayList arrayList = JavaUtilities.getFilesToCompile(file);
        for (int i = 0; i < arrayList.size(); ++i) {
            try {
                hashMap.put(Coverage.findClassName((File)arrayList.get(i)), arrayList.get(i));
                continue;
            }
            catch (Exception var4_4) {
                // empty catch block
            }
        }
        return hashMap;
    }

    public static Map mapClassNamesToClasses(ArrayList arrayList) {
        HashMap hashMap = new HashMap();
        for (int i = 0; i < arrayList.size(); ++i) {
            hashMap.put(((Class)arrayList.get(i)).getName(), arrayList.get(i));
        }
        return hashMap;
    }

    public ArrayList getAllInfos(ArrayList arrayList, File file) {
        Coverage coverage = this;
        ArrayList<CoverageInfo> arrayList2 = new ArrayList<CoverageInfo>();
        Map map = Coverage.mapFilesToClassNames(file);
        Map map2 = Coverage.mapClassNamesToClasses(arrayList);
        Object var7_7 = null;
        Set set = map.keySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Object k = iterator.next();
            CoverageInfo coverageInfo = coverage.getCoverageInfo((Class)map2.get(k), (File)map.get(k));
            if (coverageInfo == null) continue;
            arrayList2.add(coverageInfo);
        }
        return arrayList2;
    }

    public CoverageTestResult getCoverageTestResult(ArrayList arrayList, File file, File file2) {
        ArrayList arrayList2 = this.getAllInfos(arrayList, file2);
        System.err.println(arrayList2);
        CoverageInfo coverageInfo = null;
        for (int i = 0; i < arrayList2.size(); ++i) {
            CoverageInfo coverageInfo2 = (CoverageInfo)arrayList2.get(i);
            if (coverageInfo2.getFile() != file) continue;
            coverageInfo = coverageInfo2;
            break;
        }
        return new CoverageTestResult(coverageInfo, arrayList2);
    }

    private static String chooseJavaVersion() {
        if (System.getProperty("java.version").startsWith("1.5")) {
            return "-jdk15";
        }
        return "-jdk14";
    }

    public static void main(String[] arrstring) {
        System.out.println(System.getProperty("java.version"));
    }

    public static String findClassName(File file) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] arrby = new byte[fileInputStream.available()];
        fileInputStream.read(arrby);
        fileInputStream.close();
        String string = new String(arrby);
        string = string.replaceAll("//.*\n", "");
        string = string.replaceAll("\\n", " ");
        string = string.replaceAll("/\\*.*\\*/", "");
        StringTokenizer stringTokenizer = new StringTokenizer(string);
        String string2 = "";
        while (stringTokenizer.hasMoreTokens()) {
            String string3 = stringTokenizer.nextToken();
            if (string3.equals("class")) {
                string2 = string2 + stringTokenizer.nextToken().replaceAll("\\W", "");
                break;
            }
            if (!string3.equals("package")) continue;
            String string4 = stringTokenizer.nextToken();
            int n = string4.indexOf(";");
            string2 = n > 0 ? string2 + string4.substring(0, n) : string2 + string4;
            string2 = string2 + ".";
        }
        return string2;
    }
}

