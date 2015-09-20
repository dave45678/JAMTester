/*
 * Decompiled with CFR 0_102.
 */
package jamtester;

import jamtester.CompileTestResult;
import jamtester.JavaUtilities;
import jamtester.Results;
import jamtester.TestResult;
import jamtester.coverage.CoverageTestResult;
import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class StudentResult
implements Comparable,
Serializable {
    private ArrayList columns = new ArrayList();
    private ArrayList results;
    private boolean compiled;
    private Results parent;
    private ArrayList files;
    private File directory;
    private int numColumns;
    private String comments;
    private ArrayList otherGrades;
    private String theDir;

    public StudentResult(String string, boolean bl, ArrayList arrayList, Results results) {
        this.results = arrayList;
        if (arrayList == null) {
            this.results = new ArrayList();
        }
        this.compiled = bl;
        string = string.replaceAll(",", "");
        StringTokenizer stringTokenizer = new StringTokenizer(string);
        this.numColumns = stringTokenizer.countTokens();
        while (stringTokenizer.hasMoreElements()) {
            this.columns.add(stringTokenizer.nextElement());
        }
        this.parent = results;
        if (results != null) {
            results.notifyListeners(this);
        }
        this.comments = "";
        this.theDir = "";
        this.otherGrades = new ArrayList();
    }

    public StudentResult() {
        this("", false, null, null);
        this.numColumns = 1;
    }

    public void addOtherGrade() {
        this.otherGrades.add(this.parent.getDefaultValues().get(this.otherGrades.size()));
    }

    public void setOtherGrade(int n, String string) {
        this.otherGrades.set(n, string);
    }

    public void removeGradeAt(int n) {
        this.otherGrades.remove(n);
    }

    public ArrayList getResultsList() {
        return this.results;
    }

    public ArrayList getOtherGrades() {
        return this.otherGrades;
    }

    public void setOtherGrades(ArrayList arrayList) {
        this.otherGrades = arrayList;
        if (this.otherGrades == null) {
            this.otherGrades = new ArrayList();
        }
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String string) {
        this.comments = string;
    }

    public int getNumColumns() {
        return this.numColumns;
    }

    public void setNumberOfOtherGrades(int n) {
        if (this.otherGrades.size() > n) {
            throw new RuntimeException("Already has other grades");
        }
        this.otherGrades = new ArrayList();
        for (int i = 0; i < (n-=this.otherGrades.size()); ++i) {
            this.addOtherGrade();
        }
    }

    public void setResultsList(ArrayList arrayList) {
        this.results = arrayList;
    }

    public void setCompiled(boolean bl) {
        this.compiled = bl;
    }

    public boolean getCompiled() {
        return this.compiled;
    }

    public Results getParent() {
        return this.parent;
    }

    public void setParent(Results results) {
        this.parent = results;
        this.parent.notifyListeners(this);
    }

    public ArrayList getColumns() {
        return this.columns;
    }

    public void setColumns(ArrayList arrayList) {
        this.columns = arrayList;
    }

    void setDirectory(File file) {
        if (file == null) {
            throw new RuntimeException("Null directory");
        }
        this.directory = file;
        this.files = JavaUtilities.getFilesToCompile(file);
    }

    public File getDir() {
        return this.directory;
    }

    public void addResult(TestResult testResult) {
        this.results.add(testResult);
        if (!(testResult instanceof CompileTestResult)) {
            this.compiled = true;
        }
        this.parent.notifyListeners(this, this.results.size() - 1);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < this.columns.size(); ++i) {
            stringBuffer.append(this.columns.get(i).toString() + " ");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }

    public int numTests() {
        int n = 0;
        for (int i = 0; i < this.results.size(); ++i) {
            if (!(this.results.get(i) instanceof CoverageTestResult)) continue;
            ++n;
        }
        if (!this.compiled) {
            return 0;
        }
        return this.results.size() - n;
    }

    public File[] getFiles() {
        if (this.files == null) {
            return null;
        }
        File[] arrfile = new File[this.files.size()];
        for (int i = 0; i < arrfile.length; ++i) {
            arrfile[i] = (File)this.files.get(i);
        }
        return arrfile;
    }

    public String column(int n) {
        if (n >= this.parent.getNumColumns() - 1 && this.parent.getNumColumns() < this.columns.size()) {
            String string = "";
            for (int i = this.parent.getNumColumns() - 1; i < this.columns.size(); ++i) {
                string = string + this.columns.get(i);
            }
            return string;
        }
        if (n < this.columns.size()) {
            return "" + this.columns.get(n);
        }
        return "";
    }

    public void setNumColumns(int n) {
        this.numColumns = n;
    }

    public int numSucceded() {
        int n = 0;
        Object var2_2 = null;
        for (int i = 0; i < this.results.size(); ++i) {
            if (this.results.get(i) instanceof CoverageTestResult || !((TestResult)this.results.get(i)).succeded()) continue;
            ++n;
        }
        return n;
    }

    public String percentage() {
        if (this.numTests() == 0) {
            return "0";
        }
        return new DecimalFormat("#0.00").format((double)this.numSucceded() / (double)this.numTests() * 100.0);
    }

    public TestResult[] getResults() {
        if (!this.compiled) {
            TestResult[] arrtestResult = new TestResult[1000];
            if (this.results.size() == 0) {
                return arrtestResult;
            }
            for (int i = 0; i < arrtestResult.length; ++i) {
                arrtestResult[i] = (TestResult)this.results.get(0);
            }
            return arrtestResult;
        }
        Object[] arrobject = this.results.toArray();
        TestResult[] arrtestResult = new TestResult[arrobject.length];
        for (int i = 0; i < arrtestResult.length; ++i) {
            arrtestResult[i] = (TestResult)arrobject[i];
        }
        return arrtestResult;
    }

    public String toCsv() {
        int n;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < this.parent.getNumColumns(); ++i) {
            if (i < this.columns.size()) {
                stringBuffer.append("" + this.columns.get(i));
            }
            stringBuffer.append(",");
        }
        Object var2_3 = null;
        stringBuffer.append("" + this.numSucceded() + " of " + this.numTests() + ",");
        stringBuffer.append(this.percentage() + "%,");
        for (n = 0; n < this.parent.getTestHeaders().size() - 2; ++n) {
            stringBuffer.append(this.results.get(n % this.results.size()).toString() + ',');
        }
        for (n = 0; n < this.otherGrades.size(); ++n) {
            stringBuffer.append("\"" + this.otherGrades.get(n).toString() + "\"" + ',');
        }
        stringBuffer.append("\"" + this.comments + "\"");
        return stringBuffer.toString();
    }

    public int compareTo(Object object) {
        ArrayList arrayList = this.columns;
        ArrayList arrayList2 = ((StudentResult)object).columns;
        if (arrayList.size() > arrayList2.size()) {
            ArrayList arrayList3 = arrayList;
            arrayList = arrayList2;
            arrayList2 = arrayList3;
        }
        for (int i = 0; i < arrayList.size(); ++i) {
            if (arrayList.get(i).equals(arrayList2.get(i))) continue;
            return ((Comparable)this.columns.get(i)).compareTo(((StudentResult)object).columns.get(i));
        }
        return this.column(0).compareTo(((StudentResult)object).column(0));
    }

    public int hashCode() {
        return this.column(0).hashCode();
    }

    public boolean equals(Object object) {
        StudentResult studentResult = (StudentResult)object;
        try {
            for (int i = 0; i < studentResult.columns.size(); ++i) {
                if (studentResult.columns.get(i).equals(this.columns.get(i))) continue;
                return false;
            }
        }
        catch (Exception var3_4) {
            return false;
        }
        return studentResult.columns.size() == this.columns.size();
    }

    public void setTheDir(String string) {
        this.theDir = string;
        this.directory = new File(this.theDir);
    }

    public String getTheDir() {
        return this.theDir;
    }
}

