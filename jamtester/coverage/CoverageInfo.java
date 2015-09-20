/*
 * Decompiled with CFR 0_102.
 */
package jamtester.coverage;

import jamtester.coverage.CoverageViewer;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class CoverageInfo {
    private Set allLines;
    private ArrayList collected;
    private String name;
    private File file;

    CoverageInfo(String string, Set set, ArrayList arrayList, File file) {
        this.name = string;
        this.allLines = set;
        this.file = file;
        this.collected = arrayList;
    }

    public Set getLines() {
        return this.allLines;
    }

    public File getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }

    public CoverageViewer getViewer() {
        return new CoverageViewer(this, this.file);
    }

    public int timesRun(int n) {
        int n2 = 0;
        if (!this.allLines.contains(new Integer(n))) {
            return -1;
        }
        if (this.getNeglected().contains(new Integer(n))) {
            return 0;
        }
        for (int i = 0; i < this.collected.size(); ++i) {
            if (!this.collected.get(i).equals(new Integer(n))) continue;
            ++n2;
        }
        return n2;
    }

    public double getPercentageCovered() {
        return 100.0 * (double)this.getCovered().size() / (double)this.getLines().size();
    }

    public Set getCovered() {
        TreeSet treeSet = new TreeSet();
        for (int i = 0; i < this.collected.size(); ++i) {
            if (this.collected.get(i) == null) continue;
            treeSet.add(this.collected.get(i));
        }
        return treeSet;
    }

    public Set getNeglected() {
        TreeSet treeSet = new TreeSet();
        Iterator iterator = this.allLines.iterator();
        while (iterator.hasNext()) {
            Object e = iterator.next();
            if (this.collected.contains(e)) continue;
            treeSet.add(e);
        }
        return treeSet;
    }

    public String toString() {
        return "[ Name: " + this.name + " Covered: " + this.getCovered() + " Neglected:" + this.getNeglected() + " ]";
    }
}

