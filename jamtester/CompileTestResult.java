/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  junit.framework.TestResult
 */
package jamtester;

import jamtester.JAMTestFailure;
import jamtester.TestResult;
import java.io.File;

public class CompileTestResult
extends TestResult {
    private String compileError;

    public CompileTestResult(String string) {
        super("Compile Error", null);
        this.compileError = string;
        super.setException(new JAMTestFailure(string, true, string, null, string));
    }

    public CompileTestResult() {
    }

    public File getFile() {
        return new File(this.compileError.substring(0, this.compileError.indexOf(".java") + ".java".length()));
    }

    public int lineNumber() {
        try {
            String string = this.compileError.substring(this.compileError.indexOf(".java") + ".java".length() + 1);
            string = string.substring(0, string.indexOf(":"));
            return new Integer(string);
        }
        catch (Exception var1_2) {
            return 0;
        }
    }

    public String toString() {
        return "C";
    }

    public boolean succeeded() {
        return false;
    }

    public String getError() {
        return this.compileError;
    }

    public void setError(String string) {
        this.compileError = string;
    }
}

