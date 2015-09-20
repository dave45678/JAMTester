/*
 * Decompiled with CFR 0_102.
 */
package jamtester.javatools;

import java.io.PrintWriter;
import java.io.Writer;

public class NullWriter
extends Writer {
    public static final PrintWriter PRINTWRITER = new PrintWriter(new NullWriter());

    public void close() {
    }

    public void flush() {
    }

    public void write(char[] arrc, int n, int n2) {
    }
}

