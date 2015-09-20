/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.javaccess;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class NullFunction
extends Function {
    public static final Object NULL = new Object(){

        public String toString() {
            return "null";
        }
    };

    public NullFunction() {
        super("null");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.push(NULL);
    }

}

