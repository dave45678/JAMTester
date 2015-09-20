/*
 * Decompiled with CFR 0_102.
 */
package org.jext;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;

public class Utilities {
    public static final int OPEN = 0;
    public static final int SAVE = 1;
    public static final int SCRIPT = 2;
    public static final String JDK_VERSION = System.getProperty("java.version");

    public static void showMessage(String string) {
        JOptionPane.showMessageDialog(null, string, Jext.getProperty("utils.message"), 1);
    }

    public static void showError(String string) {
        JOptionPane.showMessageDialog(null, string, Jext.getProperty("utils.error"), 0);
    }

    public static void showMessage(String string, String string2) {
        JOptionPane.showMessageDialog(null, string2, string, 1);
    }

    public static Dimension getScreenDimension() {
        return Jext.getMyToolkit().getScreenSize();
    }

    public static void centerComponent(Component component) {
        component.setLocation(new Point((Utilities.getScreenDimension().width - component.getSize().width) / 2, (Utilities.getScreenDimension().height - component.getSize().height) / 2));
    }

    public static void centerComponentChild(Component component, Component component2) {
        Rectangle rectangle = component.getBounds();
        Rectangle rectangle2 = component2.getBounds();
        component2.setLocation(new Point(rectangle.x + (rectangle.width - rectangle2.width) / 2, rectangle.y + (rectangle.height - rectangle2.height) / 2));
    }

    public static String classToFile(String string) {
        return string.replace('.', '/').concat(".class");
    }

    public static String fileToClass(String string) {
        char[] arrc = string.toCharArray();
        for (int i = arrc.length - 6; i >= 0; --i) {
            if (arrc[i] != '/') continue;
            arrc[i] = 46;
        }
        return new String(arrc, 0, arrc.length - 6);
    }

    public static void beep() {
        Jext.getMyToolkit().beep();
    }

    public static void setCursorOnWait(Component component, boolean bl) {
        if (bl) {
            if (component instanceof JextFrame) {
                ((JextFrame)component).showWaitCursor();
            } else {
                component.setCursor(Cursor.getPredefinedCursor(3));
            }
        } else if (component instanceof JextFrame) {
            ((JextFrame)component).hideWaitCursor();
        } else {
            component.setCursor(Cursor.getPredefinedCursor(0));
        }
    }

    public static ImageIcon getIcon(String string, Class class_) {
        return new ImageIcon(Jext.getMyToolkit().getImage(class_.getResource(string)));
    }

    public static Image getImage(String string, Class class_) {
        return Jext.getMyToolkit().getImage(class_.getResource(string));
    }

    public static String[] chooseFiles(Component component, int n) {
        if (JDK_VERSION.charAt(2) <= '2') {
            return new String[]{Utilities.chooseFile(component, n)};
        }
        JFileChooser jFileChooser = Utilities.getFileChooser(component, n);
        jFileChooser.setMultiSelectionEnabled(true);
        if (jFileChooser.showDialog(component, null) == 0) {
            Jext.setProperty("lastdir." + n, jFileChooser.getSelectedFile().getParent());
            File[] arrfile = jFileChooser.getSelectedFiles();
            if (arrfile == null) {
                return null;
            }
            String[] arrstring = new String[arrfile.length];
            for (int i = 0; i < arrstring.length; ++i) {
                arrstring[i] = arrfile[i].getAbsolutePath();
            }
            return arrstring;
        }
        component.repaint();
        return null;
    }

    public static String chooseFile(Component component, int n) {
        JFileChooser jFileChooser = Utilities.getFileChooser(component, n);
        jFileChooser.setMultiSelectionEnabled(false);
        if (jFileChooser.showDialog(component, null) == 0) {
            Jext.setProperty("lastdir." + n, jFileChooser.getSelectedFile().getParent());
            return jFileChooser.getSelectedFile().getAbsolutePath();
        }
        component.repaint();
        return null;
    }

    private static JFileChooser getFileChooser(Component component, int n) {
        JFileChooser jFileChooser = null;
        String string = Jext.getProperty("lastdir." + n);
        if (string == null) {
            string = Jext.getHomeDirectory();
        }
        if (component instanceof JextFrame) {
            jFileChooser = ((JextFrame)component).getFileChooser(n);
            if (Jext.getBooleanProperty("editor.dirDefaultDialog") && n != 2) {
                String string2 = ((JextFrame)component).getTextArea().getCurrentFile();
                if (string2 != null) {
                    jFileChooser.setCurrentDirectory(new File(string2));
                }
            } else {
                jFileChooser.setCurrentDirectory(new File(string));
            }
        } else {
            jFileChooser = new JFileChooser(string);
            if (n == 1) {
                jFileChooser.setDialogType(1);
            } else {
                jFileChooser.setDialogType(0);
            }
        }
        jFileChooser.setFileSelectionMode(0);
        jFileChooser.setFileHidingEnabled(true);
        return jFileChooser;
    }

    public static String createWhiteSpace(int n) {
        return Utilities.createWhiteSpace(n, 0);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String createWhiteSpace(int n, int n2) {
        StringBuffer stringBuffer = new StringBuffer();
        if (n2 == 0) {
            while (n-- > 0) {
                stringBuffer.append(' ');
            }
            return stringBuffer.toString();
        } else {
            int n3 = n / n2;
            while (n3-- > 0) {
                stringBuffer.append('\t');
            }
            n3 = n % n2;
            while (n3-- > 0) {
                stringBuffer.append(' ');
            }
        }
        return stringBuffer.toString();
    }

    public static int getLeadingWhiteSpace(String string) {
        int n;
        block3 : for (n = 0; n < string.length(); ++n) {
            switch (string.charAt(n)) {
                case '\t': 
                case ' ': {
                    continue block3;
                }
            }
        }
        return n;
    }

    public static int getLeadingWhiteSpaceWidth(String string, int n) {
        int n2 = 0;
        block4 : for (int i = 0; i < string.length(); ++i) {
            switch (string.charAt(i)) {
                case ' ': {
                    ++n2;
                    continue block4;
                }
                case '\t': {
                    n2+=n - n2 % n;
                    continue block4;
                }
                default: {
                    break block4;
                }
            }
        }
        return n2;
    }

    public static int getRealLength(String string, int n) {
        int n2 = 0;
        block3 : for (int i = 0; i < string.length(); ++i) {
            switch (string.charAt(i)) {
                case '\t': {
                    n2+=n;
                    continue block3;
                }
                default: {
                    ++n2;
                }
            }
        }
        return n2;
    }

    public static String getShortStringOf(String string, int n) {
        int n2;
        int n3;
        int n4 = string.length();
        if (n4 <= n) {
            return string;
        }
        if (string.indexOf(92) == -1 && string.indexOf(47) == -1) {
            StringBuffer stringBuffer = new StringBuffer(string.substring(string.length() - n));
            for (int i = 0; i < 3; ++i) {
                stringBuffer.setCharAt(i, '.');
            }
            return stringBuffer.toString();
        }
        int n5 = n3 = n4 / 2;
        for (n2 = n3 - 1; n2 >= 0; --n2) {
            if (string.charAt(n2) != '\\' && string.charAt(n2) != '/') continue;
            n3 = n2;
            break;
        }
        for (n2 = n5 + 1; n2 < n4; ++n2) {
            if (string.charAt(n2) != '\\' && string.charAt(n2) != '/') continue;
            n5 = n2;
            break;
        }
        block9 : while (n4 - (n5 - n3) > n) {
            block10 : for (n2 = n3 - 1; n2 >= 0; --n2) {
                switch (string.charAt(n2)) {
                    case '/': 
                    case '\\': {
                        n3 = n2;
                        break block10;
                    }
                    default: {
                        continue block10;
                    }
                }
            }
            if (n4 - (n5 - n3) < n) break;
            for (n2 = n5 + 1; n2 < n4; ++n2) {
                switch (string.charAt(n2)) {
                    case '/': 
                    case '\\': {
                        n5 = n2;
                        continue block9;
                    }
                }
            }
        }
        return string.substring(0, n3 + 1) + "..." + string.substring(n5);
    }

    public static String constructPath(String string) {
        int n;
        if (Utilities.beginsWithRoot(string)) {
            return string;
        }
        StringBuffer stringBuffer = new StringBuffer(Utilities.getUserDirectory());
        int n2 = 0;
        boolean bl = false;
        string = string.trim();
        StringBuffer stringBuffer2 = new StringBuffer(string.length());
        block5 : for (n = 0; n < string.length(); ++n) {
            int n3 = string.charAt(n);
            switch (n3) {
                case 46: {
                    if (n2 == 46) {
                        String string2 = new File(stringBuffer.toString()).getParent();
                        if (string2 != null) {
                            stringBuffer = new StringBuffer(string2);
                        }
                    } else if (n2 != 0 && n2 != 92 && n2 != 47 || n < string.length() - 1 && string.charAt(n + 1) != '.') {
                        stringBuffer2.append('.');
                    }
                    n2 = 46;
                    continue block5;
                }
                case 47: 
                case 92: {
                    if (n2 == 0) {
                        stringBuffer = new StringBuffer(Utilities.getRoot(stringBuffer.toString()));
                    } else {
                        char c = stringBuffer.charAt(stringBuffer.length() - 1);
                        if (c != '\\' && c != '/') {
                            stringBuffer.append(File.separator).append(stringBuffer2.toString());
                        } else {
                            stringBuffer.append(stringBuffer2.toString());
                        }
                        stringBuffer2 = new StringBuffer();
                        bl = false;
                    }
                    n2 = 92;
                    continue block5;
                }
                case 126: {
                    if (n < string.length() - 1) {
                        if (string.charAt(n + 1) == '\\' || string.charAt(n + 1) == '/') {
                            stringBuffer = new StringBuffer(Utilities.getHomeDirectory());
                        } else {
                            stringBuffer2.append('~');
                        }
                    } else if (n == 0) {
                        stringBuffer = new StringBuffer(Utilities.getHomeDirectory());
                    } else {
                        stringBuffer2.append('~');
                    }
                    n2 = 126;
                    continue block5;
                }
                default: {
                    n2 = n3;
                    stringBuffer2.append((char)n3);
                    bl = true;
                }
            }
        }
        if (bl) {
            n = stringBuffer.charAt(stringBuffer.length() - 1);
            if (n != 92 && n != 47) {
                stringBuffer.append(File.separator).append(stringBuffer2.toString());
            } else {
                stringBuffer.append(stringBuffer2.toString());
            }
        }
        return stringBuffer.toString();
    }

    public static boolean beginsWithRoot(String string) {
        if (string.length() == 0) {
            return false;
        }
        File file = new File(string);
        File[] arrfile = File.listRoots();
        for (int i = 0; i < arrfile.length; ++i) {
            if (!string.regionMatches(true, 0, arrfile[i].getPath(), 0, arrfile[i].getPath().length())) continue;
            return true;
        }
        return false;
    }

    public static String getUserDirectory() {
        return System.getProperty("user.dir");
    }

    public static String getHomeDirectory() {
        return System.getProperty("user.home");
    }

    public static String getRoot(String string) {
        File file = new File(string);
        File[] arrfile = File.listRoots();
        for (int i = 0; i < arrfile.length; ++i) {
            if (!string.startsWith(arrfile[i].getPath())) continue;
            return arrfile[i].getPath();
        }
        return string;
    }

    public static String[] getWildCardMatches(String string, boolean bl) {
        return Utilities.getWildCardMatches(null, string, bl);
    }

    public static String[] getWildCardMatches(String string, String string2, boolean bl) {
        String[] arrstring;
        if (string2 == null) {
            return null;
        }
        String string3 = new String(string2.trim());
        ArrayList<String> arrayList = new ArrayList<String>();
        if (string == null) {
            string = Utilities.getUserDirectory();
        }
        if ((arrstring = new File(string).list()) == null) {
            return null;
        }
        for (int i = 0; i < arrstring.length; ++i) {
            if (!Utilities.match(string3, arrstring[i])) continue;
            File file = new File(Utilities.getUserDirectory(), arrstring[i]);
            arrayList.add(new String(file.getName()));
        }
        Object[] arrobject = arrayList.toArray();
        Object[] arrobject2 = new String[arrobject.length];
        for (int j = 0; j < arrobject.length; ++j) {
            arrobject2[j] = arrobject[j].toString();
        }
        arrobject = null;
        arrayList = null;
        if (bl) {
            Arrays.sort(arrobject2);
        }
        return arrobject2;
    }

    public static boolean match(String string, String string2) {
        int n = 0;
        do {
            int n2 = 0;
            do {
                boolean bl;
                boolean bl2 = n2 >= string2.length();
                boolean bl3 = bl = n >= string.length() || string.charAt(n) == '|';
                if (bl2 && bl) {
                    return true;
                }
                if (bl2 || bl) break;
                if (string.charAt(n) != '?') {
                    if (string.charAt(n) == '*') {
                        ++n;
                        for (int i = string2.length(); i >= n2; --i) {
                            if (!Utilities.match(string.substring(n), string2.substring(i))) continue;
                            return true;
                        }
                        break;
                    }
                    if (string.charAt(n) != string2.charAt(n2)) break;
                }
                ++n;
                ++n2;
            } while (true);
            if ((n = string.indexOf(124, n)) == -1) {
                return false;
            }
            ++n;
        } while (true);
    }

    public static void sortStrings(String[] arrstring) {
        Arrays.sort(arrstring);
    }

    public static File[] listFiles(String[] arrstring, boolean bl) {
        return Utilities.listFiles(arrstring, Utilities.getUserDirectory(), bl);
    }

    public static File[] listFiles(String[] arrstring, String string, boolean bl) {
        File[] arrfile = new File[arrstring.length];
        if (bl && !string.endsWith(File.separator)) {
            string = string + File.separator;
        }
        for (int i = 0; i < arrfile.length; ++i) {
            arrfile[i] = bl ? new File(string + arrstring[i]) : new File(arrstring[i]);
        }
        return arrfile;
    }

    public static String globToRE(String string) {
        char c = '\u0000';
        boolean bl = false;
        boolean bl2 = false;
        StringBuffer stringBuffer = new StringBuffer(string.length());
        block9 : for (int i = 0; i < string.length(); ++i) {
            c = string.charAt(i);
            if (bl) {
                stringBuffer.append('\\');
                stringBuffer.append(c);
                bl = false;
                continue;
            }
            switch (c) {
                case '*': {
                    stringBuffer.append('.').append('*');
                    continue block9;
                }
                case '?': {
                    stringBuffer.append('.');
                    continue block9;
                }
                case '\\': {
                    bl = true;
                    continue block9;
                }
                case '.': {
                    stringBuffer.append('\\').append('.');
                    continue block9;
                }
                case '{': {
                    stringBuffer.append('(');
                    bl2 = true;
                    continue block9;
                }
                case '}': {
                    stringBuffer.append(')');
                    bl2 = false;
                    continue block9;
                }
                case ',': {
                    if (bl2) {
                        stringBuffer.append('|');
                        continue block9;
                    }
                    stringBuffer.append(',');
                    continue block9;
                }
                default: {
                    stringBuffer.append(c);
                }
            }
        }
        return stringBuffer.toString();
    }
}

