/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn;

import org.jext.Utilities;

public class DawnUtilities
extends Utilities {
    public static String unescape(String string) {
        StringBuffer stringBuffer = new StringBuffer(string.length());
        char c = '\u0000';
        block7 : for (int i = 0; i < string.length(); ++i) {
            c = string.charAt(i);
            switch (c) {
                case '\\': {
                    stringBuffer.append('\\');
                    stringBuffer.append('\\');
                    continue block7;
                }
                case '\"': {
                    stringBuffer.append('\\');
                    stringBuffer.append('\"');
                    continue block7;
                }
                case '\'': {
                    stringBuffer.append('\\');
                    stringBuffer.append('\'');
                    continue block7;
                }
                case '\n': {
                    stringBuffer.append('\\');
                    stringBuffer.append('n');
                    continue block7;
                }
                case '\r': {
                    stringBuffer.append('\\');
                    stringBuffer.append('r');
                    continue block7;
                }
                default: {
                    stringBuffer.append(c);
                }
            }
        }
        return stringBuffer.toString();
    }

    public static String escape(String string) {
        StringBuffer stringBuffer = new StringBuffer(string.length());
        char c = '\u0000';
        block11 : for (int i = 0; i < string.length(); ++i) {
            c = string.charAt(i);
            switch (c) {
                case '\\': {
                    if (i < string.length() - 1) {
                        char c2 = '\u0000';
                        c2 = string.charAt(++i);
                        switch (c2) {
                            case 'n': {
                                stringBuffer.append('\n');
                                continue block11;
                            }
                            case 'r': {
                                stringBuffer.append('\r');
                                continue block11;
                            }
                            case 't': {
                                stringBuffer.append('\t');
                                continue block11;
                            }
                            case '\"': {
                                stringBuffer.append('\"');
                                continue block11;
                            }
                            case '\'': {
                                stringBuffer.append('\'');
                                continue block11;
                            }
                            case '\\': {
                                stringBuffer.append('\\');
                                continue block11;
                            }
                        }
                        stringBuffer.append('\\').append(c2);
                        continue block11;
                    }
                    stringBuffer.append(c);
                    continue block11;
                }
                default: {
                    stringBuffer.append(c);
                }
            }
        }
        return stringBuffer.toString();
    }
}

