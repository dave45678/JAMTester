/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.util;

import java.util.Random;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;
import org.jext.dawn.util.RandomFunction;

public class RandomizeFunction
extends Function {
    public RandomizeFunction() {
        super("randomize");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        RandomFunction._random.setSeed(System.currentTimeMillis());
    }
}

