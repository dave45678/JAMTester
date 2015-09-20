/*
 * Decompiled with CFR 0_102.
 */
package jamtester;

import jamtester.TestResult;

public class UntestedTestResult
extends TestResult {
    public String toString() {
        return "U";
    }

    public String getError() {
        return "Not yet tested.  Please right-click and choose \"Re-test\"";
    }

    public boolean getSuccessful() {
        return false;
    }
}

