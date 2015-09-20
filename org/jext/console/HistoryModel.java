/*
 * Decompiled with CFR 0_102.
 */
package org.jext.console;

import java.util.Vector;

public class HistoryModel {
    private int max;
    private Vector data;

    public HistoryModel(int n) {
        this.max = n;
        this.data = new Vector(n);
    }

    public void addItem(String string) {
        if (string == null || string.length() == 0) {
            return;
        }
        int n = this.data.indexOf(string);
        if (n != -1) {
            this.data.removeElementAt(n);
        }
        this.data.insertElementAt(string, 0);
        if (this.getSize() > this.max) {
            this.data.removeElementAt(this.getSize() - 1);
        }
    }

    public String getItem(int n) {
        return (String)this.data.elementAt(n);
    }

    public int getSize() {
        return this.data.size();
    }

    private void addItemToEnd(String string) {
        this.data.addElement(string);
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.data.clear();
        this.data = null;
    }
}

