/*
 * Decompiled with CFR 0_102.
 */
package org.jext;

import java.awt.event.ActionEvent;
import org.jext.Jext;
import org.jext.MenuAction;

public class OneClickAction
extends MenuAction {
    private MenuAction action;

    public OneClickAction(String string) {
        super(string);
    }

    public OneClickAction(String string, String string2) {
        super(string);
        this.action = Jext.getAction(string2);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        OneClickAction.getTextArea(actionEvent).setOneClick(this, actionEvent);
    }

    public void oneClickActionPerformed(ActionEvent actionEvent) {
        if (this.action != null) {
            this.action.actionPerformed(actionEvent);
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.action = null;
    }
}

