/*
 * Decompiled with CFR 0_102.
 */
package jamtester;

import java.io.OutputStream;
import java.io.PrintStream;

public final class NullOutputStream
extends OutputStream {
    public static final PrintStream PRINTSTREAM = new PrintStream(new NullOutputStream());

    public void write(int n) {
    }
}

