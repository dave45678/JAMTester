/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit;

import java.io.File;
import javax.swing.UIManager;

public class OperatingSystem {
    private static final int UNIX = 201527;
    private static final int WINDOWS_9x = 1600;
    private static final int WINDOWS_NT = 1638;
    private static final int OS2 = 57005;
    private static final int MAC_OS_X = 2748;
    private static final int UNKNOWN = 2989;
    private static int os;
    private static boolean java14;

    public static final boolean isDOSDerived() {
        return OperatingSystem.isWindows() || OperatingSystem.isOS2();
    }

    public static final boolean isWindows() {
        return os == 1600 || os == 1638;
    }

    public static final boolean isWindows9x() {
        return os == 1600;
    }

    public static final boolean isWindowsNT() {
        return os == 1638;
    }

    public static final boolean isOS2() {
        return os == 57005;
    }

    public static final boolean isUnix() {
        return os == 201527 || os == 2748;
    }

    public static final boolean isMacOS() {
        return os == 2748;
    }

    public static final boolean isMacOSLF() {
        return OperatingSystem.isMacOS() && UIManager.getLookAndFeel().isNativeLookAndFeel();
    }

    public static final boolean hasJava14() {
        return java14;
    }

    static {
        String string;
        os = System.getProperty("mrj.version") != null ? 2748 : ((string = System.getProperty("os.name")).indexOf("Windows 9") != -1 || string.indexOf("Windows ME") != -1 ? 1600 : (string.indexOf("Windows") != -1 ? 1638 : (string.indexOf("OS/2") != -1 ? 57005 : (File.separatorChar == '/' ? 201527 : 2989))));
        if (System.getProperty("java.version").compareTo("1.4") >= 0) {
            java14 = true;
        }
    }
}

