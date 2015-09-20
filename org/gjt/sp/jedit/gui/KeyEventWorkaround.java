/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.gui;

import java.awt.event.KeyEvent;
import org.gjt.sp.jedit.OperatingSystem;

public class KeyEventWorkaround {
    private static long lastKeyTime;
    private static int last;
    private static final int LAST_NOTHING = 0;
    private static final int LAST_ALT = 1;
    private static final int LAST_BROKEN = 2;
    private static final int LAST_NUMKEYPAD = 3;
    private static final int LAST_MOD = 4;

    public static KeyEvent processKeyEvent(KeyEvent keyEvent) {
        int n = keyEvent.getKeyCode();
        char c = keyEvent.getKeyChar();
        switch (keyEvent.getID()) {
            case 401: {
                switch (n) {
                    case 0: 
                    case 16: 
                    case 17: 
                    case 18: 
                    case 128: 
                    case 129: 
                    case 130: 
                    case 131: 
                    case 132: 
                    case 133: 
                    case 134: 
                    case 135: 
                    case 136: 
                    case 137: 
                    case 138: 
                    case 139: 
                    case 140: 
                    case 141: 
                    case 142: 
                    case 143: 
                    case 157: 
                    case 65406: {
                        return null;
                    }
                }
                if (!OperatingSystem.isMacOS()) {
                    KeyEventWorkaround.handleBrokenKeys(keyEvent, n);
                } else {
                    last = 0;
                }
                return keyEvent;
            }
            case 400: {
                if ((c < ' ' || c == '' || c == '\u00ff') && c != '\b') {
                    return null;
                }
                if (OperatingSystem.isMacOS() ? keyEvent.isControlDown() || keyEvent.isMetaDown() : keyEvent.isControlDown() ^ keyEvent.isAltDown() || keyEvent.isMetaDown()) {
                    return null;
                }
                if (last == 4) {
                    switch (c) {
                        case '!': 
                        case ',': 
                        case '?': 
                        case 'B': 
                        case 'M': 
                        case 'X': 
                        case 'c': {
                            last = 0;
                            return null;
                        }
                    }
                } else {
                    if (!(last != 2 || System.currentTimeMillis() - lastKeyTime >= 750 || Character.isLetter(c))) {
                        last = 0;
                        return null;
                    }
                    if (last == 1 && System.currentTimeMillis() - lastKeyTime < 750) {
                        last = 0;
                        return null;
                    }
                }
                return keyEvent;
            }
            case 402: {
                if (n == 18 && OperatingSystem.isWindows() && OperatingSystem.hasJava14()) {
                    last = 4;
                }
                return keyEvent;
            }
        }
        return keyEvent;
    }

    public static void numericKeypadKey() {
        last = 0;
    }

    private static void handleBrokenKeys(KeyEvent keyEvent, int n) {
        if (keyEvent.isAltDown() && keyEvent.isControlDown() && !keyEvent.isMetaDown()) {
            last = 0;
            return;
        }
        if (!(keyEvent.isAltDown() || keyEvent.isControlDown() || keyEvent.isMetaDown())) {
            last = 0;
            return;
        }
        if (keyEvent.isAltDown()) {
            last = 1;
        }
        switch (n) {
            case 8: 
            case 9: 
            case 10: 
            case 37: 
            case 38: 
            case 39: 
            case 40: 
            case 127: {
                last = 0;
                break;
            }
            default: {
                last = n < 65 || n > 90 ? 2 : 0;
            }
        }
        lastKeyTime = System.currentTimeMillis();
    }
}

