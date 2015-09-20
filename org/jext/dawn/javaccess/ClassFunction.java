/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.javaccess;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class ClassFunction
extends Function {
    public ClassFunction() {
        super("class");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        String string = dawnParser.popString();
        Class class_ = null;
        try {
            class_ = Class.forName(string);
        }
        catch (ClassNotFoundException var4_4) {
            throw new DawnRuntimeException(this, dawnParser, "class " + string + " can not be found");
        }
        dawnParser.push(class_);
    }
}

