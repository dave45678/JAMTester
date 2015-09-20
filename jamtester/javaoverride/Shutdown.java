/*
 * Decompiled with CFR 0_102.
 */
package jamtester.javaoverride;

import jamtester.javaoverride.Terminator;
import java.util.HashSet;
import java.util.Iterator;

class Shutdown {
    private static final int RUNNING = 0;
    private static final int HOOKS = 1;
    private static final int FINALIZERS = 2;
    private static int state = 0;
    private static boolean runFinalizersOnExit = false;
    private static HashSet hooks = null;
    private static Object lock = new Lock();

    Shutdown() {
    }

    static void setRunFinalizersOnExit(boolean bl) {
        Object object = lock;
        synchronized (object) {
            runFinalizersOnExit = bl;
        }
    }

    static void add(Thread thread) {
        Object object = lock;
        synchronized (object) {
            if (state > 0) {
                throw new IllegalStateException("Shutdown in progress");
            }
            if (thread.isAlive()) {
                throw new IllegalArgumentException("Hook already running");
            }
            if (hooks == null) {
                hooks = new HashSet(11);
                hooks.add(new WrappedHook(thread));
                Terminator.setup();
            } else {
                WrappedHook wrappedHook = new WrappedHook(thread);
                if (hooks.contains(wrappedHook)) {
                    throw new IllegalArgumentException("Hook previously registered");
                }
                hooks.add(wrappedHook);
            }
        }
    }

    static boolean remove(Thread thread) {
        Object object = lock;
        synchronized (object) {
            if (state > 0) {
                throw new IllegalStateException("Shutdown in progress");
            }
            if (thread == null) {
                throw new NullPointerException();
            }
            if (hooks == null) {
                return false;
            }
            boolean bl = hooks.remove(new WrappedHook(thread));
            if (bl && hooks.isEmpty()) {
                hooks = null;
                Terminator.teardown();
            }
            return bl;
        }
    }

    private static void runHooks() {
        if (hooks == null) {
            return;
        }
        Iterator iterator = hooks.iterator();
        while (iterator.hasNext()) {
            ((WrappedHook)iterator.next()).hook.start();
        }
        iterator = hooks.iterator();
        while (iterator.hasNext()) {
            try {
                ((WrappedHook)iterator.next()).hook.join();
            }
            catch (InterruptedException var1_1) {}
        }
    }

    static void halt(int n) {
    }

    private static native void runAllFinalizers();

    private static void sequence() {
        boolean bl;
        Object object = lock;
        synchronized (object) {
            if (state != 1) {
                return;
            }
        }
        Shutdown.runHooks();
        Object object2 = lock;
        synchronized (object2) {
            state = 2;
            bl = runFinalizersOnExit;
        }
        if (bl) {
            Shutdown.runAllFinalizers();
        }
    }

    static void exit(int n) {
        boolean bl = false;
        Object object = lock;
        synchronized (object) {
            if (n != 0) {
                runFinalizersOnExit = false;
            }
            switch (state) {
                case 0: {
                    state = 1;
                    break;
                }
                case 1: {
                    break;
                }
                case 2: {
                    if (n != 0) {
                        Shutdown.halt(n);
                        break;
                    }
                    bl = runFinalizersOnExit;
                }
            }
        }
        if (bl) {
            Shutdown.runAllFinalizers();
            Shutdown.halt(n);
        }
        Class class_ = Shutdown.class;
        object = class_;
        synchronized (class_) {
            Shutdown.sequence();
            Shutdown.halt(n);
            // ** MonitorExit[var2_2] (shouldn't be in output)
            return;
        }
    }

    static void shutdown() {
        Object object = lock;
        synchronized (object) {
            switch (state) {
                case 0: {
                    state = 1;
                    break;
                }
            }
        }
        Class class_ = Shutdown.class;
        object = class_;
        synchronized (class_) {
            Shutdown.sequence();
            // ** MonitorExit[var0] (shouldn't be in output)
            return;
        }
    }

    private static class Lock {
        private Lock() {
        }
    }

    private static class WrappedHook {
        private Thread hook;

        WrappedHook(Thread thread) {
            this.hook = thread;
        }

        public int hashCode() {
            return System.identityHashCode(this.hook);
        }

        public boolean equals(Object object) {
            if (!(object instanceof WrappedHook)) {
                return false;
            }
            return ((WrappedHook)object).hook == this.hook;
        }
    }

}

