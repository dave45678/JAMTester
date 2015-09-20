/*
 * Decompiled with CFR 0_102.
 */
package org.jext.xinsert;

import org.jext.xinsert.XTreeNode;

public class XTreeObject {
    private int index;
    private XTreeNode xtreeNode;

    public XTreeObject(XTreeNode xTreeNode, int n) {
        this.index = n;
        this.xtreeNode = xTreeNode;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int n) {
        this.index = n;
    }

    public XTreeNode getXTreeNode() {
        return this.xtreeNode;
    }

    public void setXTreeNode(XTreeNode xTreeNode) {
        this.xtreeNode = xTreeNode;
    }

    public void incrementIndex() {
        ++this.index;
    }

    public void decrementIndex() {
        --this.index;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.xtreeNode = null;
    }
}

