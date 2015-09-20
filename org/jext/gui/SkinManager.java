/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import java.io.PrintStream;
import java.util.HashMap;
import org.jext.Jext;
import org.jext.gui.BundledSkinFactory;
import org.jext.gui.Skin;
import org.jext.gui.SkinFactory;

public class SkinManager {
    private static HashMap skinList = new HashMap();
    private static Skin currSkin = null;

    private SkinManager() {
    }

    public static HashMap getSkinList() {
        return skinList;
    }

    public static void registerSkinFactory(SkinFactory skinFactory) {
        Skin[] arrskin = skinFactory.getSkins();
        if (arrskin != null) {
            int n = arrskin.length;
            for (int i = 0; i < n; ++i) {
                if (arrskin[i] == null || !arrskin[i].isAvailable()) continue;
                skinList.put(arrskin[i].getSkinInternName(), arrskin[i]);
            }
        }
    }

    public static boolean applySelectedSkin() {
        Skin skin = (Skin)skinList.get(Jext.getProperty("current_skin"));
        try {
            if (skin != null) {
                if (currSkin != null) {
                    try {
                        currSkin.unapply();
                    }
                    catch (Throwable var1_1) {
                        // empty catch block
                    }
                }
                skin.apply();
                currSkin = skin;
                return true;
            }
            System.err.println("Selected skin not found");
        }
        catch (Throwable var1_2) {
            System.err.println("An Exception occurred while selecting the skin " + Jext.getProperty("current_skin") + "; stack trace:");
            var1_2.printStackTrace();
        }
        skin = (Skin)skinList.get("jext");
        if (skin != null) {
            try {
                skin.apply();
                currSkin = skin;
            }
            catch (Throwable var1_3) {
                System.err.println("Impossible to apply the skin \"jext\"; serious problem! ");
            }
        } else {
            System.err.println("Missing skin \"jext\"; serious problem! ");
        }
        return false;
    }

    static {
        SkinManager.registerSkinFactory(new BundledSkinFactory());
    }
}

