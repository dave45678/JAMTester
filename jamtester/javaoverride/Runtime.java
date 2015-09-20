/*
 * Decompiled with CFR 0_102.
 */
package jamtester.javaoverride;

import jamtester.javaoverride.Shutdown;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Permission;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

public class Runtime {
    private static Runtime currentRuntime = new Runtime();

    public static Runtime getRuntime() {
        return currentRuntime;
    }

    private Runtime() {
    }

    public void exit(int n) {
        JOptionPane.showMessageDialog(null, "Exit called");
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkExit(n);
        }
        Thread.currentThread().getThreadGroup().stop();
    }

    public void addShutdownHook(Thread thread) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("shutdownHooks"));
        }
        Shutdown.add(thread);
    }

    public boolean removeShutdownHook(Thread thread) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("shutdownHooks"));
        }
        return Shutdown.remove(thread);
    }

    public void halt(int n) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkExit(n);
        }
        Shutdown.halt(n);
    }

    public static void runFinalizersOnExit(boolean bl) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            try {
                securityManager.checkExit(0);
            }
            catch (SecurityException var2_2) {
                throw new SecurityException("runFinalizersOnExit");
            }
        }
        Shutdown.setRunFinalizersOnExit(bl);
    }

    private native Process execInternal(String[] var1, String[] var2, String var3) throws IOException;

    public Process exec(String string) throws IOException {
        return this.exec(string, null);
    }

    public Process exec(String string, String[] arrstring) throws IOException {
        return this.exec(string, arrstring, null);
    }

    public Process exec(String string, String[] arrstring, File file) throws IOException {
        int n = 0;
        if (string.length() == 0) {
            throw new IllegalArgumentException("Empty command");
        }
        StringTokenizer stringTokenizer = new StringTokenizer(string);
        n = stringTokenizer.countTokens();
        String[] arrstring2 = new String[n];
        stringTokenizer = new StringTokenizer(string);
        n = 0;
        while (stringTokenizer.hasMoreTokens()) {
            arrstring2[n++] = stringTokenizer.nextToken();
        }
        return this.exec(arrstring2, arrstring, file);
    }

    public Process exec(String[] arrstring) throws IOException {
        return this.exec(arrstring, null);
    }

    public Process exec(String[] arrstring, String[] arrstring2) throws IOException {
        return this.exec(arrstring, arrstring2, null);
    }

    public Process exec(String[] arrstring, String[] arrstring2, File file) throws IOException {
        int n;
        SecurityManager securityManager;
        arrstring = (String[])arrstring.clone();
        String[] arrstring3 = arrstring2 = arrstring2 != null ? (String[])arrstring2.clone() : null;
        if (arrstring.length == 0) {
            throw new IndexOutOfBoundsException();
        }
        for (n = 0; n < arrstring.length; ++n) {
            if (arrstring[n] != null) continue;
            throw new NullPointerException();
        }
        if (arrstring2 != null) {
            for (n = 0; n < arrstring2.length; ++n) {
                if (arrstring2[n] != null) continue;
                throw new NullPointerException();
            }
        }
        if ((securityManager = System.getSecurityManager()) != null) {
            securityManager.checkExec(arrstring[0]);
        }
        String string = file == null ? null : file.getPath();
        return this.execInternal(arrstring, arrstring2, string);
    }

    public native int availableProcessors();

    public native long freeMemory();

    public native long totalMemory();

    public native long maxMemory();

    public native void gc();

    private static native void runFinalization0();

    public void runFinalization() {
        Runtime.runFinalization0();
    }

    public native void traceInstructions(boolean var1);

    public native void traceMethodCalls(boolean var1);

    public void load(String string) {
    }

    synchronized void load0(Class class_, String string) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkLink(string);
        }
        if (!new File(string).isAbsolute()) {
            throw new UnsatisfiedLinkError("Expecting an absolute path of the library: " + string);
        }
    }

    public void loadLibrary(String string) {
    }

    synchronized void loadLibrary0(Class class_, String string) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkLink(string);
        }
        if (string.indexOf(File.separatorChar) != -1) {
            throw new UnsatisfiedLinkError("Directory separator should not appear in library name: " + string);
        }
    }

    public InputStream getLocalizedInputStream(InputStream inputStream) {
        return inputStream;
    }

    public OutputStream getLocalizedOutputStream(OutputStream outputStream) {
        return outputStream;
    }
}

