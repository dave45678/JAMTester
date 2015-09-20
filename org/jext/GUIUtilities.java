/*
 * Decompiled with CFR 0_102.
 */
package org.jext;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import org.gjt.sp.jedit.syntax.SyntaxStyle;
import org.jext.Jext;
import org.jext.Utilities;
import org.jext.gui.EnhancedMenuItem;
import org.jext.gui.JextMenu;
import org.jext.gui.JextMenuSeparator;

public class GUIUtilities {
    private static final Image ICON_IMAGE;
    public static Hashtable menuItemsActions;

    public static final Image getJextIconImage() {
        return ICON_IMAGE;
    }

    public static void setScrollableTabbedPane(JTabbedPane jTabbedPane) {
        if (!Jext.getBooleanProperty("scrollableTabbedPanes")) {
            return;
        }
        try {
            Class class_ = jTabbedPane.getClass();
            Method method = class_.getMethod("setTabLayoutPolicy", Integer.TYPE);
            if (method != null) {
                Field field = class_.getField("SCROLL_TAB_LAYOUT");
                method.invoke(jTabbedPane, new Integer(field.getInt(jTabbedPane)));
            }
        }
        catch (Exception var1_2) {
            // empty catch block
        }
    }

    public static void requestFocus(final Window window, final Component component) {
        window.addWindowListener(new WindowAdapter(){

            public void windowActivated(WindowEvent windowEvent) {
                component.requestFocus();
                window.removeWindowListener(this);
            }
        });
    }

    public static void saveGeometry(Window window, String string) {
        Dimension dimension = window.getSize();
        Jext.setProperty(string + ".width", String.valueOf(dimension.width));
        Jext.setProperty(string + ".height", String.valueOf(dimension.height));
        Point point = window.getLocation();
        int n = point.x;
        int n2 = point.y;
        if (n < -4) {
            n = -4;
        }
        if (n2 < -4) {
            n2 = -4;
        }
        Jext.setProperty(string + ".x", String.valueOf(n));
        Jext.setProperty(string + ".y", String.valueOf(n2));
    }

    public static void loadGeometry(Window window, String string) {
        int n;
        int n2;
        int n3;
        int n4;
        Dimension dimension;
        try {
            n = Integer.parseInt(Jext.getProperty(string + ".width"));
            n4 = Integer.parseInt(Jext.getProperty(string + ".height"));
        }
        catch (NumberFormatException var6_4) {
            dimension = window.getSize();
            n = dimension.width;
            n4 = dimension.height;
        }
        try {
            n3 = Integer.parseInt(Jext.getProperty(string + ".x"));
            n2 = Integer.parseInt(Jext.getProperty(string + ".y"));
        }
        catch (NumberFormatException var6_5) {
            dimension = window.getToolkit().getScreenSize();
            n3 = (dimension.width - n) / 2;
            n2 = (dimension.height - n4) / 2;
        }
        window.setLocation(n3 < -4 ? -4 : n3, n2 < -4 ? -4 : n2);
        window.setSize(n, n4);
    }

    public static void message(Frame frame, String string, Object[] arrobject) {
        JOptionPane.showMessageDialog(frame, Jext.getProperty(string.concat(".message"), arrobject), Jext.getProperty(string.concat(".title"), arrobject), 1);
    }

    public static void error(Frame frame, String string, Object[] arrobject) {
        JOptionPane.showMessageDialog(frame, Jext.getProperty(string.concat(".message"), arrobject), Jext.getProperty(string.concat(".title"), arrobject), 0);
    }

    public static Color parseColor(String string) {
        if (string == null) {
            return Color.black;
        }
        if (string.startsWith("#")) {
            try {
                return Color.decode(string);
            }
            catch (NumberFormatException var1_1) {
                return Color.black;
            }
        }
        return Color.black;
    }

    public static String getColorHexString(Color color) {
        String string = Integer.toHexString(color.getRGB() & 16777215);
        return "#000000".substring(0, 7 - string.length()).concat(string);
    }

    public static SyntaxStyle parseStyle(String string) throws IllegalArgumentException {
        Color color = Color.black;
        boolean bl = false;
        boolean bl2 = false;
        StringTokenizer stringTokenizer = new StringTokenizer(string);
        while (stringTokenizer.hasMoreTokens()) {
            String string2 = stringTokenizer.nextToken();
            if (string2.startsWith("color:")) {
                color = GUIUtilities.parseColor(string2.substring(6));
                continue;
            }
            if (string2.startsWith("style:")) {
                for (int i = 6; i < string2.length(); ++i) {
                    if (string2.charAt(i) == 'i') {
                        bl = true;
                        continue;
                    }
                    if (string2.charAt(i) == 'b') {
                        bl2 = true;
                        continue;
                    }
                    throw new IllegalArgumentException("Invalid style: " + string2);
                }
                continue;
            }
            throw new IllegalArgumentException("Invalid directive: " + string2);
        }
        return new SyntaxStyle(color, bl, bl2);
    }

    public static String getStyleString(SyntaxStyle syntaxStyle) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("color:" + GUIUtilities.getColorHexString(syntaxStyle.getColor()));
        if (!syntaxStyle.isPlain()) {
            stringBuffer.append(" style:" + (syntaxStyle.isItalic() ? "i" : "") + (syntaxStyle.isBold() ? "b" : ""));
        }
        return stringBuffer.toString();
    }

    public static JMenu loadMenu(String string) {
        return GUIUtilities.loadMenu(string, false);
    }

    public static JMenu loadMenu(String string, boolean bl) {
        String string2;
        JextMenu jextMenu;
        int n;
        if (string == null) {
            return null;
        }
        if (!bl) {
            string2 = Jext.getProperty(string + ".label");
            if (string2 == null) {
                string2 = string;
            }
        } else {
            string2 = string;
        }
        if ((n = string2.indexOf(36)) != -1 && string2.length() - n > 1) {
            jextMenu = new JextMenu(string2.substring(0, n).concat(string2.substring(++n)));
            jextMenu.setMnemonic(Character.toLowerCase(string2.charAt(n)));
        } else {
            jextMenu = new JextMenu(string2);
        }
        if (bl) {
            return jextMenu;
        }
        String string3 = Jext.getProperty(string);
        if (string3 != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(string3);
            while (stringTokenizer.hasMoreTokens()) {
                String string4 = stringTokenizer.nextToken();
                if (string4.equals("-")) {
                    if (Jext.getFlatMenus()) {
                        jextMenu.getPopupMenu().add(new JextMenuSeparator());
                        continue;
                    }
                    jextMenu.getPopupMenu().addSeparator();
                    continue;
                }
                JMenuItem jMenuItem = GUIUtilities.loadMenuItem(string4);
                if (jMenuItem == null) continue;
                jextMenu.add(jMenuItem);
            }
        }
        return jextMenu;
    }

    public static JMenuItem loadMenuItem(String string) {
        String string2 = Jext.getProperty(string.concat(".label"));
        if (string2 == null) {
            string2 = new Date().toString();
        }
        return GUIUtilities.loadMenuItem(string2, string, null, true, true);
    }

    public static JMenuItem loadMenuItem(String string, String string2, String string3, boolean bl) {
        return GUIUtilities.loadMenuItem(string, string2, string3, bl, true);
    }

    public static JMenuItem loadMenuItem(String string, String string2, String string3, boolean bl, boolean bl2) {
        EnhancedMenuItem enhancedMenuItem;
        Object object;
        String string4 = new String();
        if (string == null) {
            return null;
        }
        int n = string.indexOf(36);
        if (string2 != null && (object = Jext.getProperty(string2.concat(".shortcut"))) != null) {
            string4 = object;
        }
        if (n != -1 && string.length() - n > 1) {
            enhancedMenuItem = new EnhancedMenuItem(string.substring(0, n).concat(string.substring(++n)), string4);
            enhancedMenuItem.setMnemonic(Character.toLowerCase(string.charAt(n)));
        } else {
            enhancedMenuItem = new EnhancedMenuItem(string, string4);
        }
        if (string3 != null) {
            Class class_ = Jext.class;
            object = Utilities.getIcon(string3.concat(Jext.getProperty("jext.look.icons")).concat(".gif"), class_);
            if (object != null) {
                enhancedMenuItem.setIcon((Icon)object);
            }
        }
        if (string2 != null) {
            object = Jext.getAction(string2);
            if (object == null) {
                enhancedMenuItem.setEnabled(false);
            } else {
                enhancedMenuItem.addActionListener((ActionListener)object);
                enhancedMenuItem.setEnabled(bl);
                if (bl2) {
                    StringBuffer stringBuffer = new StringBuffer(string.length());
                    for (int i = 0; i < string.length(); ++i) {
                        char c = string.charAt(i);
                        if (c == '$') continue;
                        stringBuffer.append(c);
                    }
                    if (string2.startsWith("one_")) {
                        stringBuffer.append(" (One Click!)");
                    }
                    if (menuItemsActions.get(string2) == null) {
                        menuItemsActions.put(string2, stringBuffer.toString());
                    }
                }
            }
        } else {
            enhancedMenuItem.setEnabled(bl);
        }
        return enhancedMenuItem;
    }

    static {
        Class class_ = Jext.class;
        ICON_IMAGE = Utilities.getImage("images/window_icon.gif", class_);
        menuItemsActions = new Hashtable();
    }

}

