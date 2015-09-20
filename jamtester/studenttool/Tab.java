/*
 * Decompiled with CFR 0_102.
 */
package jamtester.studenttool;

public class Tab {
    private String name;
    private String startFile;
    private String testFile;

    public void setName(String string) {
        this.name = string;
    }

    public String getName() {
        return this.name;
    }

    public String getTestFile() {
        return this.testFile;
    }

    public String getStartFile() {
        return this.startFile;
    }

    public void setTestFile(String string) {
        this.testFile = string;
    }

    public void setStartFile(String string) {
        this.startFile = string;
    }

    public String toString() {
        return "[Tab: Name=" + this.name + " StartFile=" + this.startFile + " TestFile=" + this.testFile + "]";
    }
}

