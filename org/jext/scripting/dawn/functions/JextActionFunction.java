/*
 * Decompiled with CFR 0_102.
 */
package org.jext.scripting.dawn.functions;

import java.awt.event.ActionEvent;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.MenuAction;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class JextActionFunction
extends Function {
    public JextActionFunction() {
        super("jextAction");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        String string = dawnParser.popString();
        MenuAction menuAction = Jext.getAction(string);
        if (menuAction == null) {
            throw new DawnRuntimeException(this, dawnParser, "unkown jext action named " + string);
        }
        menuAction.actionPerformed(new ActionEvent((JextFrame)dawnParser.getProperty("JEXT.JEXT_FRAME"), 1001, null));
    }
}

