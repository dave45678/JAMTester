/*
 * Decompiled with CFR 0_102.
 */
package org.jext.console;

import java.awt.Color;
import java.io.File;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import org.jext.Jext;
import org.jext.Utilities;
import org.jext.console.Console;

public class ConsoleListDir {
    private static Console parent;
    private static int indentSize;
    private static String indent;
    private static String pattern;
    private static boolean moreInfos;
    private static boolean fullNames;
    private static boolean longDates;
    private static boolean hiddenFiles;
    private static boolean noDates;
    private static boolean onlyDirs;
    private static boolean onlyFiles;
    private static boolean recursive;
    private static boolean noInfos;
    private static boolean kiloBytes;
    private static boolean sort;
    private static boolean canList;

    public static void list(Console console, String string) {
        parent = console;
        boolean bl = true;
        if (ConsoleListDir.buildFlags(string)) {
            String string2 = Utilities.getUserDirectory();
            ConsoleListDir.run();
            if (recursive || !canList) {
                System.getProperties().put("user.dir", string2);
            }
            fullNames = false;
            longDates = false;
            hiddenFiles = false;
            moreInfos = false;
            noDates = false;
            onlyDirs = false;
            onlyFiles = false;
            recursive = false;
            kiloBytes = false;
            sort = false;
            pattern = "";
            canList = true;
            indentSize = 0;
        }
    }

    private static final void print(String string) {
        parent.append(string + "\n", ConsoleListDir.parent.outputColor);
    }

    private static boolean buildFlags(String string) {
        if (string == null) {
            return true;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(string);
        while (stringTokenizer.hasMoreTokens()) {
            String string2 = stringTokenizer.nextToken();
            if (string2.startsWith("-")) {
                if (string2.equals("-help")) {
                    ConsoleListDir.help();
                    return false;
                }
                block14 : for (int i = 1; i < string2.length(); ++i) {
                    switch (string2.charAt(i)) {
                        case 'h': {
                            hiddenFiles = true;
                            continue block14;
                        }
                        case 'm': {
                            moreInfos = true;
                            continue block14;
                        }
                        case 'l': {
                            longDates = true;
                            continue block14;
                        }
                        case 'f': {
                            fullNames = true;
                            continue block14;
                        }
                        case 'n': {
                            noDates = true;
                            continue block14;
                        }
                        case 'd': {
                            onlyDirs = true;
                            continue block14;
                        }
                        case 'a': {
                            onlyFiles = true;
                            continue block14;
                        }
                        case 'r': {
                            recursive = true;
                            continue block14;
                        }
                        case 'i': {
                            noInfos = true;
                            continue block14;
                        }
                        case 'k': {
                            kiloBytes = true;
                            continue block14;
                        }
                        case 's': {
                            sort = true;
                        }
                    }
                }
                continue;
            }
            pattern = string2;
        }
        return true;
    }

    private static final void run() {
        Date date = new Date();
        StringBuffer stringBuffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss");
        File[] arrfile = Utilities.listFiles(Utilities.getWildCardMatches(pattern.equals("") ? "*" : pattern, sort), true);
        int n = 0;
        int n2 = 0;
        long l = 0;
        if (canList && pattern.indexOf("*") == -1 && pattern.indexOf("?") == -1) {
            File file = new File(Utilities.constructPath(pattern));
            if (!(file != null && file.isDirectory())) {
                parent.error(Jext.getProperty("console.ls.error"));
                return;
            }
            canList = false;
            pattern = "*";
            System.getProperties().put("user.dir", Utilities.constructPath(file.getAbsolutePath()));
            ConsoleListDir.run();
            return;
        }
        ConsoleListDir.print("");
        for (int i = 0; i < arrfile.length; ++i) {
            StringBuffer stringBuffer2;
            StringBuffer stringBuffer3 = new StringBuffer();
            File file = arrfile[i];
            String string = file.getName();
            if (!fullNames) {
                string = Utilities.getShortStringOf(string, 24);
            }
            int n3 = 32 - string.length();
            int n4 = 0;
            n4 = n3 > 6 ? 6 : (n3 >= 0 ? n3 : 0);
            if (file.isDirectory()) {
                stringBuffer3.append(string).append(Utilities.createWhiteSpace(n3).substring(n4)).append("<DIR>");
                if (moreInfos) {
                    stringBuffer3 = new StringBuffer("   ").append(Utilities.createWhiteSpace(8)).append(stringBuffer3);
                }
                ++n;
            } else if (file.isFile()) {
                stringBuffer3.append(string).append(Utilities.createWhiteSpace(n3)).append(file.length());
                l+=file.length();
                if (moreInfos) {
                    stringBuffer2 = new StringBuffer();
                    stringBuffer2.append(file.canWrite() ? 'w' : '-');
                    stringBuffer2.append(file.canRead() ? 'r' : '-');
                    stringBuffer2.append(file.isHidden() ? 'h' : '-');
                    stringBuffer2.append(Utilities.createWhiteSpace(8));
                    stringBuffer3 = stringBuffer2.append(stringBuffer3);
                }
                ++n2;
            }
            stringBuffer2 = new StringBuffer();
            if (!noDates) {
                date.setTime(file.lastModified());
                if (longDates) {
                    stringBuffer2.append(date.toString());
                } else {
                    stringBuffer.setLength(0);
                    stringBuffer2.append(simpleDateFormat.format(date, stringBuffer, fieldPosition));
                }
                stringBuffer2.append(Utilities.createWhiteSpace(8));
            }
            if ((hiddenFiles && file.isHidden() || !file.isHidden()) && (file.isDirectory() && !onlyFiles || file.isFile() && !onlyDirs || onlyDirs && onlyFiles) && Utilities.match(pattern, file.getName())) {
                ConsoleListDir.print(indent + stringBuffer2.toString() + stringBuffer3.toString());
            }
            if (!recursive || !file.isDirectory()) continue;
            System.getProperties().put("user.dir", Utilities.constructPath(file.getAbsolutePath()));
            indent = ConsoleListDir.createIndent(++indentSize);
            ConsoleListDir.run();
            if (onlyDirs) continue;
            ConsoleListDir.print("");
        }
        StringBuffer stringBuffer4 = new StringBuffer();
        if (kiloBytes) {
            stringBuffer4.append(ConsoleListDir.formatNumber(Long.toString(l / 1024))).append('k');
        } else {
            stringBuffer4.append(ConsoleListDir.formatNumber(Long.toString(l))).append("bytes");
        }
        if (!noInfos) {
            ConsoleListDir.print("\n" + indent + n2 + " files - " + n + " directories - " + stringBuffer4.toString());
        }
        indent = ConsoleListDir.createIndent(--indentSize);
    }

    private static final String formatNumber(String string) {
        StringBuffer stringBuffer = new StringBuffer(string);
        for (int i = string.length(); i > 0; i-=3) {
            stringBuffer.insert(i, ' ');
        }
        return stringBuffer.toString();
    }

    private static final String createIndent(int n) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < n; ++i) {
            stringBuffer.append('-');
            stringBuffer.append('-');
        }
        return stringBuffer.toString();
    }

    public static void help() {
        parent.help(Jext.getProperty("console.ls.help"));
    }

    static {
        indentSize = 0;
        indent = new String();
        pattern = new String();
        canList = true;
    }
}

