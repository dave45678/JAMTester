/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  junit.framework.TestFailure
 *  junit.framework.TestResult
 */
package jamtester;

import jamtester.JAMTestFailure;
import java.io.Serializable;
import java.util.Enumeration;
import junit.framework.TestFailure;

public class TestResult
implements Serializable {
    private boolean successful;
    private String name;
    private JAMTestFailure exception;

    public TestResult(String string, junit.framework.TestResult testResult) {
        this.name = string;
        if (testResult != null) {
            this.successful = testResult.wasSuccessful();
            this.exception = this.collectException(testResult);
        }
    }

    public TestResult() {
    }

    public boolean getSuccessful() {
        return this.succeded();
    }

    public void setSuccessful(boolean bl) {
        this.successful = bl;
    }

    public String getName() {
        return this.name();
    }

    public void setName(String string) {
        this.name = string;
    }

    public JAMTestFailure getException() {
        return this.exception();
    }

    public void setException(JAMTestFailure jAMTestFailure) {
        this.exception = jAMTestFailure;
    }

    public String name() {
        return this.name;
    }

    private JAMTestFailure collectException(junit.framework.TestResult testResult) {
        if (this.succeded()) {
            return null;
        }
        if (testResult.errorCount() > 0) {
            return new JAMTestFailure((TestFailure)testResult.errors().nextElement());
        }
        return new JAMTestFailure((TestFailure)testResult.failures().nextElement());
    }

    public JAMTestFailure exception() {
        return this.exception;
    }

    public boolean succeded() {
        return this.successful;
    }

    public String toString() {
        if (this.succeded()) {
            return "";
        }
        if (this.exception.isFailure()) {
            return "F";
        }
        return "E";
    }
}

