/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;

public abstract class Function {
    private String name;

    public Function() {
    }

    public Function(String string) {
        this.name = string;
    }

    public String getHelp() {
        return "";
    }

    public String getName() {
        return this.name;
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        throw new DawnRuntimeException(this, dawnParser, "function is not implemented");
    }
}

