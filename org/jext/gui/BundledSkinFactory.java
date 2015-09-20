/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.util.ArrayList;
import javax.swing.UIManager;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import org.jext.gui.GenericSkin;
import org.jext.gui.JextMetalTheme;
import org.jext.gui.Skin;
import org.jext.gui.SkinFactory;

class BundledSkinFactory
implements SkinFactory {
    BundledSkinFactory() {
    }

    public Skin[] getSkins() {
        ArrayList<Skin> arrayList = new ArrayList<Skin>(8);
        arrayList.add(new MetalSkin());
        arrayList.add(new JextSkin());
        arrayList.add(new GenericSkin("Unix Motif Skin", "motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel"));
        if (!UIManager.getSystemLookAndFeelClassName().equals(UIManager.getCrossPlatformLookAndFeelClassName())) {
            arrayList.add(new GenericSkin("Native Skin", "native", UIManager.getSystemLookAndFeelClassName()));
        }
        this.addSkinIfPresent(arrayList, "MacOs Native Skin", "_macos", "javax.swing.plaf.mac.MacLookAndFeel");
        this.addSkinIfPresent(arrayList, "MacOs Native Skin", "macos", "com.sun.java.swing.plaf.mac.MacLookAndFeel");
        arrayList.add(()new GenericSkin("Windows Native Skin", "windows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"){

            public boolean isAvailable() {
                return new WindowsLookAndFeel().isSupportedLookAndFeel();
            }
        });
        return arrayList.toArray(new Skin[0]);
    }

    private boolean addSkinIfPresent(ArrayList arrayList, String string, String string2, String string3) {
        try {
            Class class_ = Class.forName(string3);
            if (class_ != null) {
                arrayList.add(new GenericSkin(string, string2, string3));
                return true;
            }
        }
        catch (Exception var5_6) {
            // empty catch block
        }
        return false;
    }

    private class JextSkin
    extends Skin {
        private JextSkin() {
        }

        public String getSkinName() {
            return "Jext Metal Skin";
        }

        public String getSkinInternName() {
            return "jext";
        }

        public void apply() throws Throwable {
            MetalLookAndFeel.setCurrentTheme(new JextMetalTheme());
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
    }

    private class MetalSkin
    extends Skin {
        private MetalSkin() {
        }

        public String getSkinName() {
            return "Standard Metal Skin";
        }

        public String getSkinInternName() {
            return "metal";
        }

        public void apply() throws Throwable {
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
    }

}

