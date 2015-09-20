/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  com.microstar.xml.HandlerBase
 */
package org.jext.xml;

import com.microstar.xml.HandlerBase;
import java.util.Properties;
import org.jext.Jext;

public class XPropertiesHandler
extends HandlerBase {
    private Properties props;
    private String pName;
    private String pValue;

    private String parse(String string) {
        StringBuffer stringBuffer = new StringBuffer(string.length());
        char c = '\u0000';
        block10 : for (int i = 0; i < string.length(); ++i) {
            c = string.charAt(i);
            switch (c) {
                case '\\': {
                    if (i < string.length() - 1) {
                        char c2 = '\u0000';
                        c2 = string.charAt(++i);
                        switch (c2) {
                            case 'n': {
                                stringBuffer.append('\n');
                                continue block10;
                            }
                            case 'r': {
                                stringBuffer.append('\r');
                                continue block10;
                            }
                            case 't': {
                                stringBuffer.append('\t');
                                continue block10;
                            }
                            case 'w': {
                                stringBuffer.append(' ');
                                continue block10;
                            }
                            case '\\': {
                                stringBuffer.append('\\');
                            }
                        }
                        continue block10;
                    }
                    stringBuffer.append(c);
                    continue block10;
                }
                default: {
                    stringBuffer.append(c);
                }
            }
        }
        return stringBuffer.toString();
    }

    public void attribute(String string, String string2, boolean bl) {
        if (string.equalsIgnoreCase("VALUE")) {
            this.pValue = this.parse(string2);
        } else if (string.equalsIgnoreCase("NAME")) {
            this.pName = string2;
        }
    }

    public void doctypeDecl(String string, String string2, String string3) throws Exception {
        if (!"xproperties".equalsIgnoreCase(string)) {
            throw new Exception("Not a valid XProperties file !");
        }
    }

    public void charData(char[] arrc, int n, int n2) {
    }

    public void startElement(String string) {
    }

    public void endElement(String string) {
        if (string == null) {
            return;
        }
        if (string.equalsIgnoreCase("PROPERTY") && this.pName != null && this.pValue != null) {
            this.props.put(this.pName, this.pValue);
            this.pValue = null;
            this.pName = null;
        }
    }

    public void startDocument() {
        this.props = Jext.getProperties();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.props = null;
        this.pName = null;
        this.pValue = null;
    }
}

