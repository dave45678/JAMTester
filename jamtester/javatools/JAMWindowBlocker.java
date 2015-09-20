/*
 * Decompiled with CFR 0_102.
 */
package jamtester.javatools;

import java.awt.AWTEvent;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JFrame;

public class JAMWindowBlocker
implements AWTEventListener {
    private Set windows = new HashSet();
    private Set exempt = new HashSet();
    private boolean allowShown;

    public JAMWindowBlocker(boolean bl) {
        this.fillExempt();
        this.allowShown = bl;
    }

    public void eventDispatched(AWTEvent aWTEvent) {
        if (!(((long)aWTEvent.getID() & 64) != 64 || this.exempt.contains(aWTEvent.getSource()))) {
            if (aWTEvent.getSource() instanceof JFrame) {
                ((JFrame)aWTEvent.getSource()).setDefaultCloseOperation(1);
            }
            if (!this.allowShown) {
                ((Window)aWTEvent.getSource()).hide();
            }
            this.windows.add(aWTEvent.getSource());
        }
    }

    private void fillExempt() {
        Frame[] arrframe = Frame.getFrames();
        for (int i = 0; i < arrframe.length; ++i) {
            this.exempt.add(arrframe[i]);
        }
    }

    public void closeAllWindows() {
        Iterator iterator = this.windows.iterator();
        while (iterator.hasNext()) {
            ((Window)iterator.next()).dispose();
        }
    }

    public void attach() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this, 64);
    }

    public void detach() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
    }
}

