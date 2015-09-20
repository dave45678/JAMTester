/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AbstractDisposer
extends KeyAdapter {
    private Window parent;

    public AbstractDisposer(Window window) {
        this.parent = window;
    }

    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case 27: {
                this.parent.dispose();
                keyEvent.consume();
            }
        }
    }

    public void finalize() throws Throwable {
        super.finalize();
        this.parent = null;
    }
}

