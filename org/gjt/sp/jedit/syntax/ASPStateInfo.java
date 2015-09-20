/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import java.io.PrintStream;

class ASPStateInfo {
    boolean client = true;
    String language = "javascript";
    private static Object[][] modes = new Object[][]{{new ASPStateInfo(true, "html"), new Byte(0)}, {new ASPStateInfo(false, "html"), new Byte(0)}, {new ASPStateInfo(true, "javascript"), new Byte(11)}, {new ASPStateInfo(true, "jscript"), new Byte(11)}, {new ASPStateInfo(false, "javascript"), new Byte(10)}, {new ASPStateInfo(false, "jscript"), new Byte(10)}, {new ASPStateInfo(true, "vbscript"), new Byte(9)}, {new ASPStateInfo(false, "vbscript"), new Byte(8)}, {new ASPStateInfo(true, "perlscript"), new Byte(13)}, {new ASPStateInfo(false, "perlscript"), new Byte(12)}};

    ASPStateInfo() {
    }

    ASPStateInfo(boolean bl, String string) {
        this.client = bl;
        this.language = string;
    }

    void init(boolean bl, String string) {
        this.client = bl;
        this.language = string;
    }

    public boolean equals(Object object) {
        if (!(object != null && object instanceof ASPStateInfo)) {
            return false;
        }
        ASPStateInfo aSPStateInfo = (ASPStateInfo)object;
        return this.client == aSPStateInfo.client && this.language.equals(aSPStateInfo.language);
    }

    byte toASPMode() {
        for (int i = 0; i < modes.length; ++i) {
            if (!this.equals(modes[i][0])) continue;
            return ((Byte)modes[i][1]).byteValue();
        }
        return 0;
    }

    void display(PrintStream printStream) {
        printStream.println("LANGUAGE: [" + this.language + "]");
        printStream.println("CLIENT:   [" + this.client + "]");
    }
}

