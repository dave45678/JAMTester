/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.javaccess;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class MethodFunction
extends Function {
    public MethodFunction() {
        super("method");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 2);
        String string = dawnParser.popString();
        Object object = dawnParser.pop();
        if (!(object instanceof Class)) {
            throw new DawnRuntimeException(this, dawnParser, "" + object + " is not a class");
        }
        Method method = null;
        try {
            Method[] arrmethod = ((Class)object).getMethods();
            for (int i = 0; i < arrmethod.length; ++i) {
                Method method2 = arrmethod[i];
                Class<?>[] arrclass = method2.getParameterTypes();
                StringBuffer stringBuffer = new StringBuffer(method2.getName() + "(");
                for (int j = 0; j < arrclass.length; ++j) {
                    if (j > 0) {
                        stringBuffer.append(',');
                    }
                    stringBuffer.append(arrclass[j].getName());
                }
                stringBuffer.append(')');
                if (!string.equals(stringBuffer.toString())) continue;
                method = arrmethod[i];
                break;
            }
            if (method == null) {
                throw new DawnRuntimeException(this, dawnParser, "method " + string + " can not be found");
            }
        }
        catch (SecurityException var5_6) {
            throw new DawnRuntimeException(this, dawnParser, "security violation");
        }
        dawnParser.push(method);
    }
}

