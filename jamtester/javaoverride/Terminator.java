/*
 * Decompiled with CFR 0_102.
 */
package jamtester.javaoverride;

import jamtester.javaoverride.Shutdown;
import sun.misc.Signal;
import sun.misc.SignalHandler;

class Terminator {
    private static SignalHandler handler = null;

    Terminator() {
    }

    static void setup() {
        SignalHandler signalHandler;
        if (handler != null) {
            return;
        }
        handler = signalHandler = new SignalHandler(){

            public void handle(Signal signal) {
                Shutdown.exit(signal.getNumber() + 128);
            }
        };
        try {
            Signal.handle(new Signal("INT"), signalHandler);
            Signal.handle(new Signal("TERM"), signalHandler);
        }
        catch (IllegalArgumentException var1_1) {
            // empty catch block
        }
    }

    static void teardown() {
    }

}

