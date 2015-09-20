/*
 * Decompiled with CFR 0_102.
 */
package org.jext.xinsert;

import java.util.StringTokenizer;
import javax.swing.tree.DefaultMutableTreeNode;

public class XTreeNode
extends DefaultMutableTreeNode {
    private int pos = -1;
    private String modes;

    public XTreeNode(String string) {
        super(string);
    }

    public XTreeNode(String string, String string2) {
        super(string);
        this.modes = string2;
    }

    public XTreeNode(String string, String string2, int n) {
        super(string);
        this.modes = string2;
        this.pos = n;
    }

    public int getIndex() {
        return this.pos;
    }

    public void setIndex(int n) {
        this.pos = n;
    }

    public boolean isPermanent() {
        return this.modes == null;
    }

    public boolean isAssociatedToMode(String string) {
        if (this.modes == null) {
            return true;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(this.modes);
        while (stringTokenizer.hasMoreTokens()) {
            if (!stringTokenizer.nextToken().equals(string)) continue;
            return true;
        }
        return false;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.modes = null;
    }
}

