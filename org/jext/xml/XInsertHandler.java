/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  com.microstar.xml.HandlerBase
 */
package org.jext.xml;

import com.microstar.xml.HandlerBase;
import java.io.PrintStream;
import java.util.Stack;
import org.jext.xinsert.XTree;

public class XInsertHandler
extends HandlerBase {
    private XTree tree;
    private Stack stateStack;
    private String lastAttr;
    private String lastName;
    private String lastValue;
    private String lastAttrValue;
    private String lastModes;
    private String type;

    public XInsertHandler(XTree xTree) {
        this.tree = xTree;
    }

    public void attribute(String string, String string2, boolean bl) {
        if (string.equalsIgnoreCase("TYPE") && bl) {
            this.type = string2;
        } else if (string.equalsIgnoreCase("MODES")) {
            this.lastModes = string2;
        } else if (string.equalsIgnoreCase("NAME")) {
            this.lastAttr = string;
            this.lastAttrValue = string2;
        }
    }

    public void doctypeDecl(String string, String string2, String string3) throws Exception {
        if (!"XINSERT".equalsIgnoreCase(string)) {
            throw new Exception("Not a valid XInsert file !");
        }
    }

    public void charData(char[] arrc, int n, int n2) {
        if ("ITEM".equalsIgnoreCase((String)this.stateStack.peek())) {
            this.lastValue = new String(arrc, n, n2);
        }
    }

    public void startElement(String string) {
        this.stateStack.push(string);
        if ("NAME".equalsIgnoreCase(this.lastAttr) && "MENU".equalsIgnoreCase(string)) {
            this.tree.addMenu(this.lastAttrValue, this.lastModes);
            this.lastModes = null;
        }
    }

    public void endElement(String string) {
        if (string == null) {
            return;
        }
        String string2 = (String)this.stateStack.peek();
        if (string.equalsIgnoreCase(string2)) {
            if (string2.equalsIgnoreCase("MENU")) {
                this.tree.closeMenu();
            } else if (string2.equalsIgnoreCase("ITEM")) {
                int n = 0;
                if (this.type != null) {
                    n = this.type.equalsIgnoreCase("MIXED") ? 2 : (this.type.equalsIgnoreCase("SCRIPT") ? 1 : 0);
                }
                this.tree.addInsert(this.lastAttrValue, this.lastValue, n);
                this.type = null;
            }
            this.stateStack.pop();
        } else {
            System.err.println("XInsert: Unclosed tag: " + this.stateStack.peek());
        }
        this.lastAttr = null;
        this.lastAttrValue = null;
    }

    public void startDocument() {
        try {
            this.stateStack = new Stack();
            this.stateStack.push(null);
        }
        catch (Exception var1_1) {
            // empty catch block
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.tree = null;
        this.stateStack.clear();
        this.stateStack = null;
        this.lastAttr = null;
        this.lastName = null;
        this.lastValue = null;
        this.lastAttrValue = null;
        this.lastModes = null;
        this.type = null;
    }
}

