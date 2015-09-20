/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  com.microstar.xml.HandlerBase
 */
package org.jext.xml;

import com.microstar.xml.HandlerBase;
import org.jext.Jext;
import org.jext.OneClickAction;

public class OneClickActionsHandler
extends HandlerBase {
    private String pName;
    private String iName;

    public void attribute(String string, String string2, boolean bl) {
        if (string.equalsIgnoreCase("NAME")) {
            this.pName = string2;
        } else if (string.equalsIgnoreCase("INTERNAL")) {
            this.iName = string2;
        }
    }

    public void doctypeDecl(String string, String string2, String string3) throws Exception {
        if (!"oneclickactions".equalsIgnoreCase(string)) {
            throw new Exception("Not a valid One Click! actions file !");
        }
    }

    public void endElement(String string) {
        if (string == null) {
            return;
        }
        if (string.equalsIgnoreCase("ACTION") && this.pName != null && this.iName != null) {
            Jext.addAction(new OneClickAction(this.pName, this.iName));
            this.iName = null;
            this.pName = null;
        }
    }
}

