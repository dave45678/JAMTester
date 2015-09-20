/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  junit.framework.TestFailure
 */
package jamtester;

import java.io.Serializable;
import junit.framework.TestFailure;

public class JAMTestFailure
implements Serializable {
    private String exceptionMessage;
    private boolean isFailure;
    private String toString;
    private Throwable thrownException;
    private String trace;
    private String augmentedException;

    public JAMTestFailure(TestFailure testFailure) {
        this(testFailure.exceptionMessage(), testFailure.isFailure(), testFailure.toString(), testFailure.thrownException(), testFailure.trace());
    }

    public JAMTestFailure(String string, boolean bl, String string2, Throwable throwable, String string3) {
        this.exceptionMessage = string;
        this.isFailure = bl;
        this.toString = string2;
        this.thrownException = throwable;
        this.trace = string3;
    }

    public JAMTestFailure() {
    }

    private String augment(String string, String string2) {
        if (!this.isFailure) {
            return string;
        }
        String string3 = this.getExpected(string);
        String string4 = this.getWas(string);
        String string5 = this.getProblem(string);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(string5 + "\nExpected:\t<" + string3 + ">\n");
        stringBuffer.append("But was:\t<" + string4 + ">\n");
        if (string3.indexOf("...") >= 0 || string4.indexOf("...") >= 0) {
            stringBuffer.append("\nSuch that \"...\" replaces a common prefix or suffix in expected and received values");
        }
        return stringBuffer.toString();
    }

    private String getExpected(String string) {
        int n = string.indexOf("expected:<");
        int n2 = string.indexOf("<", n) + 1;
        int n3 = string.indexOf(">", n);
        return string.substring(n2, n3);
    }

    private String getWas(String string) {
        int n = string.indexOf("but was:<");
        int n2 = string.indexOf("<", n) + 1;
        int n3 = string.indexOf(">", n);
        return string.substring(n2, n3);
    }

    private String getProblem(String string) {
        int n = string.indexOf(":");
        int n2 = n + 1;
        int n3 = string.indexOf("expected:<", n) - 1;
        return string.substring(n2, n3).trim();
    }

    public void augment() {
        this.augmentedException = this.augment(this.trace, this.exceptionMessage);
    }

    public String getException() {
        return this.exceptionMessage();
    }

    public void setException(String string) {
        this.exceptionMessage = string;
    }

    public boolean getFailure() {
        return this.isFailure();
    }

    public void setFailure(boolean bl) {
        this.isFailure = bl;
    }

    public String getTrace() {
        return this.trace();
    }

    public String getAugmentedTrace() {
        if (this.augmentedException == null) {
            return this.getException();
        }
        return this.augmentedException;
    }

    public void setAugmentedTrace(String string) {
        this.augmentedException = string;
    }

    public void setTrace(String string) {
        this.trace = string;
    }

    public String exceptionMessage() {
        return this.exceptionMessage;
    }

    public boolean isFailure() {
        return this.isFailure;
    }

    public String toString() {
        return this.toString;
    }

    public Throwable thrownException() {
        return this.thrownException;
    }

    public String trace() {
        return this.trace;
    }
}

