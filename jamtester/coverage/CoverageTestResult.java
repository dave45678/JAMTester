/*
 * Decompiled with CFR 0_102.
 */
package jamtester.coverage;

import jamtester.TestResult;
import jamtester.coverage.CoverageInfo;
import java.util.ArrayList;

public class CoverageTestResult
extends TestResult {
    private ArrayList infos;
    private CoverageInfo mainInfo;

    public CoverageTestResult(CoverageInfo coverageInfo, ArrayList arrayList) {
        this.mainInfo = coverageInfo;
        this.infos = arrayList;
        if (this.mainInfo == null) {
            this.mainInfo = (CoverageInfo)arrayList.get(0);
        }
    }

    public String toString() {
        return "" + this.mainInfo.getPercentageCovered() + "%";
    }

    public void setMain(CoverageInfo coverageInfo) {
        this.mainInfo = coverageInfo;
    }

    public boolean succeeded() {
        return true;
    }

    public void setInfos(ArrayList arrayList) {
        this.infos = arrayList;
    }

    public CoverageInfo mainInfo() {
        return this.mainInfo;
    }

    public ArrayList coverageInfos() {
        return this.infos;
    }
}

