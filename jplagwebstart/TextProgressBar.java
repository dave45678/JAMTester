/*
 * Decompiled with CFR 0_102.
 */
package jplagwebstart;

import java.io.PrintStream;
import jplagwebstart.ProgressBar;

public class TextProgressBar
implements ProgressBar {
    private static final String progressString = "0%----------+-----------50%----------+--------100%\n";
    private int size;
    private int pos = 0;

    public void setSize(int n) {
        this.size = n;
    }

    public void setBar(int n) {
        int n2 = (int)((long)n * 50 / (long)this.size);
        if (n2 <= this.pos) {
            return;
        }
        System.out.print("0%----------+-----------50%----------+--------100%\n".substring(this.pos, n2));
        this.pos = n2;
    }

    public void setBar(String string) {
    }

    public void finish() {
        System.out.println();
    }
}

