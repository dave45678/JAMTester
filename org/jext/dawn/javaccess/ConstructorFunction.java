/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.javaccess;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class ConstructorFunction
extends Function {
    public ConstructorFunction() {
        super("constructor");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 2);
        String string = dawnParser.popString();
        Object object = dawnParser.pop();
        if (!(object instanceof Class)) {
            throw new DawnRuntimeException(this, dawnParser, "" + object + " is not a class");
        }
        Constructor constructor = null;
        try {
            Constructor<?>[] arrconstructor = ((Class)object).getConstructors();
            for (int i = 0; i < arrconstructor.length; ++i) {
                Constructor constructor2 = arrconstructor[i];
                Class<?>[] arrclass = constructor2.getParameterTypes();
                StringBuffer stringBuffer = new StringBuffer("(");
                for (int j = 0; j < arrclass.length; ++j) {
                    if (j > 0) {
                        stringBuffer.append(',');
                    }
                    stringBuffer.append(arrclass[j].getName());
                }
                stringBuffer.append(')');
                if (!string.equals(stringBuffer.toString())) continue;
                constructor = arrconstructor[i];
                break;
            }
            if (constructor == null) {
                throw new DawnRuntimeException(this, dawnParser, "constructor " + string + " can not be found");
            }
        }
        catch (SecurityException var5_6) {
            throw new DawnRuntimeException(this, dawnParser, "security violation");
        }
        dawnParser.push(constructor);
    }
}

