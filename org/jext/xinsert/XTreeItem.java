/*
 * Decompiled with CFR 0_102.
 */
package org.jext.xinsert;

public class XTreeItem {
    public static final int PLAIN = 0;
    public static final int SCRIPT = 1;
    public static final int MIXED = 2;
    private int type;
    private String content;

    public XTreeItem(String string) {
        this(string, 0);
    }

    public XTreeItem(String string, int n) {
        this.content = string;
        this.type = n;
    }

    public boolean isMixed() {
        return this.type == 2;
    }

    public boolean isScript() {
        return this.type == 1;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int n) {
        this.type = n;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String string) {
        this.content = string;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.content = null;
    }
}

