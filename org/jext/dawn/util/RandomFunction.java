/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.util;

import java.util.Random;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class RandomFunction
extends Function {
    public static Random _random = new Random();

    public RandomFunction() {
        super("rand");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.pushNumber(_random.nextDouble());
    }
}

