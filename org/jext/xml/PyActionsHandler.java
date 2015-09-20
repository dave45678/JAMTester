/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  com.microstar.xml.HandlerBase
 */
package org.jext.xml;

import com.microstar.xml.HandlerBase;
import org.jext.Jext;

public class PyActionsHandler
extends HandlerBase {
    private boolean pEditAction;
    private String pName;
    private String pValue;

    public void attribute(String string, String string2, boolean bl) {
        if (string.equalsIgnoreCase("NAME")) {
            this.pName = string2;
        } else if (string.equalsIgnoreCase("EDIT")) {
            this.pEditAction = "yes".equals(string2);
        }
    }

    public void doctypeDecl(String string, String string2, String string3) throws Exception {
        if (!"pyactions".equalsIgnoreCase(string)) {
            throw new Exception("Not a valid PyActions file !");
        }
    }

    public void charData(char[] arrc, int n, int n2) {
        this.pValue = new String(arrc, n, n2);
    }

    public void endElement(String string) {
        if (string == null) {
            return;
        }
        if (string.equalsIgnoreCase("ACTION") && this.pName != null && this.pValue != null) {
            Jext.addPythonAction(this.pName, this.pValue, this.pEditAction);
            this.pValue = null;
            this.pName = null;
        }
    }
}

