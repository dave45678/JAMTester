/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.javaccess;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;
import org.jext.dawn.javaccess.NullFunction;

public class NewFunction
extends Function {
    static /* synthetic */ Class class$java$lang$Integer;
    static /* synthetic */ Class class$java$lang$Boolean;
    static /* synthetic */ Class class$java$lang$String;
    static /* synthetic */ Class class$java$lang$Float;
    static /* synthetic */ Class class$java$lang$Character;
    static /* synthetic */ Class class$java$lang$Short;

    public NewFunction() {
        super("new");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        Object object = dawnParser.pop();
        if (object instanceof Class) {
            this.useDefaultConstructor(dawnParser, (Class)object);
        } else if (object instanceof Constructor) {
            this.invokeConstructor(dawnParser, (Constructor)object);
        } else {
            throw new DawnRuntimeException(this, dawnParser, "" + object + " is not a class or a constructor");
        }
    }

    private void useDefaultConstructor(DawnParser dawnParser, Class class_) throws DawnRuntimeException {
        try {
            Object t = class_.newInstance();
            if (t != null) {
                dawnParser.push(t);
            }
        }
        catch (IllegalAccessException var3_4) {
            throw new DawnRuntimeException(this, dawnParser, "illegal access");
        }
        catch (InstantiationException var3_5) {
            throw new DawnRuntimeException(this, dawnParser, "instantiation failed");
        }
    }

    private void invokeConstructor(DawnParser dawnParser, Constructor constructor) throws DawnRuntimeException {
        Class<?>[] arrclass = constructor.getParameterTypes();
        int n = arrclass.length;
        dawnParser.checkArgsNumber(this, n);
        try {
            int n2;
            Object[] arrobject = new Object[n];
            for (n2 = n - 1; n2 >= 0; --n2) {
                arrobject[n2] = dawnParser.pop();
            }
            for (n2 = 0; n2 < n; ++n2) {
                if (arrobject[n2] == NullFunction.NULL) {
                    arrobject[n2] = null;
                    continue;
                }
                if (arrclass[n2] == Integer.TYPE || arrclass[n2] == (class$java$lang$Integer == null ? NewFunction.class$("java.lang.Integer") : class$java$lang$Integer)) {
                    if (!(arrobject[n2] instanceof Number)) continue;
                    arrobject[n2] = new Integer(((Number)arrobject[n2]).intValue());
                    continue;
                }
                if (arrclass[n2] == Boolean.TYPE || arrclass[n2] == (class$java$lang$Boolean == null ? NewFunction.class$("java.lang.Boolean") : class$java$lang$Boolean)) {
                    if (arrobject[n2] instanceof Number) {
                        arrobject[n2] = ((Number)arrobject[n2]).doubleValue() != 0.0 ? Boolean.TRUE : Boolean.FALSE;
                        continue;
                    }
                    if (!"\"\"".equals(arrobject[n2])) {
                        arrobject[n2] = Boolean.TRUE;
                        continue;
                    }
                    arrobject[n2] = Boolean.FALSE;
                    continue;
                }
                if (arrclass[n2] == (class$java$lang$String == null ? NewFunction.class$("java.lang.String") : class$java$lang$String)) {
                    String string = "" + arrobject[n2];
                    int n3 = string.length();
                    if (n3 >= 2 && string.charAt(0) == '\"' && string.charAt(n3 - 1) == '\"') {
                        string = string.substring(1, n3 - 1);
                    }
                    arrobject[n2] = string;
                    continue;
                }
                if (arrclass[n2] == Float.TYPE || arrclass[n2] == (class$java$lang$Float == null ? NewFunction.class$("java.lang.Float") : class$java$lang$Float)) {
                    if (!(arrobject[n2] instanceof Number)) continue;
                    arrobject[n2] = new Float(((Number)arrobject[n2]).floatValue());
                    continue;
                }
                if (arrclass[n2] == Character.TYPE || arrclass[n2] == (class$java$lang$Character == null ? NewFunction.class$("java.lang.Character") : class$java$lang$Character)) {
                    if (!(arrobject[n2] instanceof Number)) continue;
                    arrobject[n2] = new Character((char)((Number)arrobject[n2]).intValue());
                    continue;
                }
                if (arrclass[n2] != Short.TYPE && arrclass[n2] != (class$java$lang$Short == null ? NewFunction.class$("java.lang.Short") : class$java$lang$Short) || !(arrobject[n2] instanceof Number)) continue;
                arrobject[n2] = new Short(((Number)arrobject[n2]).shortValue());
            }
            dawnParser.push(constructor.newInstance(arrobject));
        }
        catch (IllegalAccessException var5_6) {
            throw new DawnRuntimeException(this, dawnParser, "illegal access");
        }
        catch (InstantiationException var5_7) {
            throw new DawnRuntimeException(this, dawnParser, "instantiation failed");
        }
        catch (InvocationTargetException var5_8) {
            throw new DawnRuntimeException(this, dawnParser, "invocation failed: " + var5_8.getTargetException().getMessage());
        }
    }
}

