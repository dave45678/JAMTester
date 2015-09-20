/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.python.util.PythonInterpreter
 */
package org.jext;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.gjt.sp.jedit.textarea.DefaultInputHandler;
import org.gjt.sp.jedit.textarea.InputHandler;
import org.jext.GUIUtilities;
import org.jext.JARClassLoader;
import org.jext.JextFrame;
import org.jext.JextLoader;
import org.jext.JextTextArea;
import org.jext.MenuAction;
import org.jext.Mode;
import org.jext.ModeFileFilter;
import org.jext.Plugin;
import org.jext.PythonAction;
import org.jext.PythonEditAction;
import org.jext.Utilities;
import org.jext.actions.BeginLine;
import org.jext.actions.BoxComment;
import org.jext.actions.CompleteWord;
import org.jext.actions.CompleteWordAll;
import org.jext.actions.CreateTemplate;
import org.jext.actions.EndLine;
import org.jext.actions.JoinAllLines;
import org.jext.actions.JoinLines;
import org.jext.actions.LeftIndent;
import org.jext.actions.OpenUrl;
import org.jext.actions.Print;
import org.jext.actions.RemoveWhitespace;
import org.jext.actions.RightIndent;
import org.jext.actions.SimpleComment;
import org.jext.actions.SimpleUnComment;
import org.jext.actions.SpacesToTabs;
import org.jext.actions.TabsToSpaces;
import org.jext.actions.ToLowerCase;
import org.jext.actions.ToUpperCase;
import org.jext.actions.WingComment;
import org.jext.actions.WordCount;
import org.jext.gui.JextButton;
import org.jext.gui.SkinFactory;
import org.jext.gui.SkinManager;
import org.jext.gui.SplashScreen;
import org.jext.misc.TabSwitcher;
import org.jext.misc.VersionCheck;
import org.jext.oneclick.OneAutoIndent;
import org.jext.scripting.python.Run;
import org.jext.search.Search;
import org.jext.textarea.CsWord;
import org.jext.textarea.IndentOnEnter;
import org.jext.textarea.IndentOnTab;
import org.jext.textarea.NextLineIndent;
import org.jext.textarea.PrevLineIndent;
import org.jext.textarea.ScrollDown;
import org.jext.textarea.ScrollPageDown;
import org.jext.textarea.ScrollPageUp;
import org.jext.textarea.ScrollUp;
import org.jext.xml.OneClickActionsReader;
import org.jext.xml.PyActionsReader;
import org.jext.xml.XPropertiesReader;
import org.python.util.PythonInterpreter;

public class Jext {
    public static final String RELEASE = "3.2 <Qu\u00e9bec>";
    public static final String BUILD = "03.02.00.03";
    public static final boolean DELETE_OLD_SETTINGS = false;
    public static boolean DEBUG = false;
    public static final String[] NEW_LINE = new String[]{"\r", "\n", "\r\n"};
    public static final String SETTINGS_DIRECTORY = System.getProperty("user.home") + File.separator + ".jext" + File.separator;
    public static final String JEXT_HOME = System.getProperty("user.dir");
    public static final int JEXT_SERVER_PORT = 49152;
    public static ArrayList modes;
    public static ArrayList modesFileFilters;
    private static String language;
    private static ZipFile languagePack;
    private static ArrayList languageEntries;
    private static boolean flatMenus;
    private static boolean buttonsHighlight;
    private static JextLoader jextLoader;
    private static boolean isServerEnabled;
    private static ArrayList plugins;
    public static String usrProps;
    private static SplashScreen splash;
    private static Properties props;
    private static Properties defaultProps;
    private static ArrayList instances;
    private static HashMap actionHash;
    private static HashMap pythonActionHash;
    private static VersionCheck check;
    private static DefaultInputHandler inputHandler;
    private static final String USER_PROPS;
    private static boolean runInBg;
    private static boolean goingToKill;
    private static JextFrame builtTextArea;

    public static boolean getButtonsHighlight() {
        return buttonsHighlight;
    }

    public static boolean getFlatMenus() {
        return flatMenus;
    }

    public static void stopAutoCheck() {
        if (check != null) {
            check.interrupt();
            check = null;
        }
    }

    public static DefaultInputHandler getInputHandler() {
        return inputHandler;
    }

    public static void init() {
    }

    public static void addAction(MenuAction menuAction) {
        String string = menuAction.getName();
        actionHash.put(string, menuAction);
        String string2 = Jext.getProperty(string.concat(".shortcut"));
        if (string2 != null) {
            inputHandler.addKeyBinding(string2, menuAction);
        }
    }

    public static void addPythonAction(String string, String string2, boolean bl) {
        PythonAction pythonAction = !bl ? new PythonAction(string, string2) : new PythonEditAction(string, string2);
        pythonActionHash.put(string, pythonAction);
        String string3 = Jext.getProperty(string.concat(".shortcut"));
        if (string3 != null) {
            inputHandler.addKeyBinding(string3, pythonAction);
        }
    }

    public static MenuAction getAction(String string) {
        Object v = actionHash.get(string);
        if (v == null) {
            v = pythonActionHash.get(string);
        }
        return (MenuAction)v;
    }

    private static void initActions() {
        actionHash = new HashMap();
        inputHandler = new DefaultInputHandler();
        inputHandler.addDefaultKeyBindings();
        Class class_ = Jext.class;
        Jext.loadXMLActions(class_.getResourceAsStream("jext.actions.xml"), "jext.actions.xml");
        Jext.addAction(new BeginLine());
        Jext.addAction(new BoxComment());
        Jext.addAction(new CompleteWord());
        Jext.addAction(new CompleteWordAll());
        Jext.addAction(new CreateTemplate());
        Jext.addAction(new EndLine());
        Jext.addAction(new JoinAllLines());
        Jext.addAction(new JoinLines());
        Jext.addAction(new LeftIndent());
        Jext.addAction(new OpenUrl());
        Jext.addAction(new Print());
        Jext.addAction(new RemoveWhitespace());
        Jext.addAction(new RightIndent());
        Jext.addAction(new SimpleComment());
        Jext.addAction(new SimpleUnComment());
        Jext.addAction(new SpacesToTabs());
        Jext.addAction(new TabsToSpaces());
        Jext.addAction(new ToLowerCase());
        Jext.addAction(new ToUpperCase());
        Jext.addAction(new WingComment());
        Jext.addAction(new WordCount());
        Jext.addAction(new OneAutoIndent());
        Jext.loadXMLOneClickActions(Jext.class.getResourceAsStream("jext.oneclickactions.xml"), "jext.oneclickactions.xml");
        Jext.addJextKeyBindings();
    }

    public static void addJextKeyBindings() {
        Jext.addJextKeyBindings(inputHandler);
    }

    public static void addJextKeyBindings(InputHandler inputHandler) {
        inputHandler.addKeyBinding("CA+UP", new ScrollUp());
        inputHandler.addKeyBinding("CA+PAGE_UP", new ScrollPageUp());
        inputHandler.addKeyBinding("CA+DOWN", new ScrollDown());
        inputHandler.addKeyBinding("CA+PAGE_DOWN", new ScrollPageDown());
        inputHandler.addKeyBinding("C+UP", new PrevLineIndent());
        inputHandler.addKeyBinding("C+DOWN", new NextLineIndent());
        inputHandler.addKeyBinding("ENTER", new IndentOnEnter());
        inputHandler.addKeyBinding("TAB", new IndentOnTab());
        inputHandler.addKeyBinding("S+TAB", new LeftIndent());
        inputHandler.addKeyBinding("C+INSERT", Jext.getAction("copy"));
        inputHandler.addKeyBinding("S+INSERT", Jext.getAction("paste"));
        inputHandler.addKeyBinding("CA+LEFT", new CsWord(0, -1));
        inputHandler.addKeyBinding("CA+RIGHT", new CsWord(0, 1));
        inputHandler.addKeyBinding("CAS+LEFT", new CsWord(1, -1));
        inputHandler.addKeyBinding("CAS+RIGHT", new CsWord(1, 1));
        inputHandler.addKeyBinding("CA+BACK_SPACE", new CsWord(2, -1));
        inputHandler.addKeyBinding("CAS+BACK_SPACE", new CsWord(2, 1));
        if (Utilities.JDK_VERSION.charAt(2) >= '4') {
            inputHandler.addKeyBinding("C+PAGE_UP", new TabSwitcher(false));
            inputHandler.addKeyBinding("C+PAGE_DOWN", new TabSwitcher(true));
        }
    }

    private static void initPlugins() {
        plugins = new ArrayList();
        Jext.loadPlugins(JEXT_HOME + File.separator + "plugins");
        Jext.loadPlugins(SETTINGS_DIRECTORY + "plugins");
    }

    public static void assocPluginsToModes() {
        for (int i = 0; i < plugins.size(); ++i) {
            Plugin plugin = (Plugin)plugins.get(i);
            String string = Jext.getProperty("plugin." + plugin.getClass().getName() + ".modes");
            if (string == null) continue;
            StringTokenizer stringTokenizer = new StringTokenizer(string);
            while (stringTokenizer.hasMoreTokens()) {
                String string2 = stringTokenizer.nextToken();
                Mode mode = Jext.getMode(string2);
                mode.addPlugin(plugin);
            }
        }
    }

    public static void loadPlugins(String string) {
        Object[] arrobject = new String[]{string};
        System.out.println(Jext.getProperty("jar.scanningdir", arrobject));
        File file = new File(string);
        if (!(file.exists() || file.isDirectory())) {
            return;
        }
        String[] arrstring = file.list();
        if (arrstring == null) {
            return;
        }
        for (int i = 0; i < arrstring.length; ++i) {
            String string2 = arrstring[i];
            if (!string2.toLowerCase().endsWith(".jar")) continue;
            try {
                new JARClassLoader(string + File.separator + string2);
                continue;
            }
            catch (IOException var6_6) {
                Object[] arrobject2 = new String[]{string2};
                System.err.println(Jext.getProperty("jar.error.load", arrobject2));
                var6_6.printStackTrace();
            }
        }
    }

    public static void addPlugin(Plugin plugin) {
        plugins.add(plugin);
        try {
            plugin.start();
        }
        catch (Throwable var1_1) {
            System.err.println("#--An exception has occurred while starting plugin:");
            var1_1.printStackTrace();
        }
        if (plugin instanceof SkinFactory) {
            SkinManager.registerSkinFactory((SkinFactory)plugin);
        }
    }

    public static Plugin getPlugin(String string) {
        for (int i = 0; i < plugins.size(); ++i) {
            Plugin plugin = (Plugin)plugins.get(i);
            if (!plugin.getClass().getName().equalsIgnoreCase(string)) continue;
            return plugin;
        }
        return null;
    }

    public static Plugin[] getPlugins() {
        Plugin[] arrplugin = plugins.toArray(new Plugin[0]);
        return arrplugin;
    }

    public static JextFrame newWindow(String[] arrstring) {
        return Jext.newWindow(arrstring, true);
    }

    public static JextFrame newWindow() {
        Jext.initProperties();
        return Jext.newWindow(null, true);
    }

    static JextFrame newWindow(String[] arrstring, boolean bl) {
        ArrayList arrayList = instances;
        synchronized (arrayList) {
            JextFrame jextFrame;
            if (bl && builtTextArea != null) {
                if (arrstring != null) {
                    for (int i = 0; i < arrstring.length; ++i) {
                        builtTextArea.open(arrstring[i]);
                    }
                }
                builtTextArea.setVisible(true);
                jextFrame = builtTextArea;
                builtTextArea = null;
            } else {
                jextFrame = new JextFrame(arrstring, bl);
                instances.add(jextFrame);
            }
            return jextFrame;
        }
    }

    public static int getWindowsCount() {
        return instances.size();
    }

    public static void propertiesChanged() {
        for (int i = 0; i < instances.size(); ++i) {
            ((JextFrame)instances.get(i)).loadProperties();
        }
    }

    public static void recentChanged(JextFrame jextFrame) {
        for (int i = 0; i < instances.size(); ++i) {
            JextFrame jextFrame2 = (JextFrame)instances.get(i);
            if (jextFrame2 == jextFrame || jextFrame2 == null) continue;
            jextFrame2.reloadRecent();
        }
    }

    public static ArrayList getInstances() {
        return instances;
    }

    public static Toolkit getMyToolkit() {
        return Toolkit.getDefaultToolkit();
    }

    public static String getHomeDirectory() {
        return JEXT_HOME;
    }

    public static void saveProps() {
        if (usrProps != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(usrProps);
                props.store(fileOutputStream, "Jext Properties");
                fileOutputStream.close();
            }
            catch (IOException var0_1) {
                // empty catch block
            }
        }
    }

    public static void saveXMLProps(String string) {
        Jext.saveXMLProps(usrProps, string);
    }

    public static void saveXMLProps(String string, String string2) {
        if (string != null) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(string));
                String string3 = new String("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                bufferedWriter.write(string3, 0, string3.length());
                bufferedWriter.newLine();
                string3 = new String("<!DOCTYPE xproperties SYSTEM \"xproperties.dtd\" >");
                bufferedWriter.write(string3, 0, string3.length());
                bufferedWriter.newLine();
                string3 = "<!-- Last save: " + new Date().toString() + " -->";
                bufferedWriter.write(string3, 0, string3.length());
                bufferedWriter.newLine();
                if (string2 == null) {
                    string2 = new String("Properties");
                }
                string2 = "<!-- " + string2 + " -->";
                bufferedWriter.write(string2, 0, string2.length());
                bufferedWriter.newLine();
                bufferedWriter.newLine();
                string3 = new String("<xproperties>");
                bufferedWriter.write(string3, 0, string3.length());
                bufferedWriter.newLine();
                Jext.setProperty("properties.version", "03.02.00.03");
                char c = '\u0000';
                Enumeration enumeration = props.keys();
                Enumeration enumeration2 = props.elements();
                while (enumeration2.hasMoreElements()) {
                    StringBuffer stringBuffer = new StringBuffer("  <property name=\"");
                    stringBuffer.append(enumeration.nextElement());
                    stringBuffer.append("\" value=\"");
                    String string4 = (String)enumeration2.nextElement();
                    block11 : for (int i = 0; i < string4.length(); ++i) {
                        c = string4.charAt(i);
                        switch (c) {
                            case '\\': {
                                stringBuffer.append('\\');
                                stringBuffer.append('\\');
                                continue block11;
                            }
                            case '\'': {
                                stringBuffer.append("&apos;");
                                continue block11;
                            }
                            case '&': {
                                stringBuffer.append("&amp;");
                                continue block11;
                            }
                            case '\"': {
                                stringBuffer.append("&#34;");
                                continue block11;
                            }
                            case '\n': {
                                stringBuffer.append('\\');
                                stringBuffer.append('n');
                                continue block11;
                            }
                            case '\r': {
                                stringBuffer.append('\\');
                                stringBuffer.append('r');
                                continue block11;
                            }
                            default: {
                                stringBuffer.append(c);
                            }
                        }
                    }
                    stringBuffer.append("\" />");
                    bufferedWriter.write(stringBuffer.toString(), 0, stringBuffer.length());
                    bufferedWriter.newLine();
                }
                string3 = new String("</xproperties>");
                bufferedWriter.write(string3, 0, string3.length());
                bufferedWriter.close();
            }
            catch (IOException var2_3) {
                // empty catch block
            }
        }
        JARClassLoader.saveDisabledList();
    }

    public static Properties getProperties() {
        return props;
    }

    public static void loadXMLProps(InputStream inputStream, String string) {
        XPropertiesReader.read(inputStream, string);
    }

    public static void loadXMLProps(InputStream inputStream, String string, boolean bl) {
        XPropertiesReader.read(inputStream, string, bl);
    }

    public static void loadXMLActions(InputStream inputStream, String string) {
        PyActionsReader.read(inputStream, string);
    }

    public static void loadXMLOneClickActions(InputStream inputStream, String string) {
        OneClickActionsReader.read(inputStream, string);
    }

    public static InputStream getLanguageStream(InputStream inputStream, String string) {
        ZipEntry zipEntry;
        if (languagePack != null && (zipEntry = Jext.languagePackContains(string)) != null) {
            try {
                return languagePack.getInputStream(zipEntry);
            }
            catch (IOException var3_3) {
                return inputStream;
            }
        }
        return inputStream;
    }

    public static ZipEntry languagePackContains(String string) {
        for (int i = 0; i < languageEntries.size(); ++i) {
            ZipEntry zipEntry = (ZipEntry)languageEntries.get(i);
            if (!zipEntry.getName().equalsIgnoreCase(string)) continue;
            return zipEntry;
        }
        return null;
    }

    public static void loadProps(InputStream inputStream) {
        try {
            props.load(new BufferedInputStream(inputStream));
            inputStream.close();
        }
        catch (IOException var1_1) {
            // empty catch block
        }
    }

    public static void initDirectories() {
        File file = new File(SETTINGS_DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
            file = new File(SETTINGS_DIRECTORY + "plugins" + File.separator);
            if (!file.exists()) {
                file.mkdir();
            }
            if (!(file = new File(SETTINGS_DIRECTORY + "scripts" + File.separator)).exists()) {
                file.mkdir();
            }
            if (!(file = new File(SETTINGS_DIRECTORY + "xinsert" + File.separator)).exists()) {
                file.mkdir();
            }
        }
    }

    public static void initProperties() {
        Object object;
        Object object2;
        Object object3;
        usrProps = SETTINGS_DIRECTORY + ".jext-props.xml";
        defaultProps = Jext.props = new Properties();
        File file = new File(SETTINGS_DIRECTORY + ".lang");
        if (file.exists()) {
            try {
                object = new BufferedReader(new FileReader(file));
                object3 = object.readLine();
                object.close();
                if (!(object3 == null || object3.equals("English"))) {
                    object2 = new File(JEXT_HOME + File.separator + "lang" + File.separator + (String)object3 + "_pack.jar");
                    if (object2.exists()) {
                        languagePack = new ZipFile((File)object2);
                        languageEntries = new ArrayList();
                        Enumeration<? extends ZipEntry> enumeration = languagePack.entries();
                        while (enumeration.hasMoreElements()) {
                            languageEntries.add(enumeration.nextElement());
                        }
                        Jext.setLanguage((String)object3);
                    } else {
                        file.delete();
                    }
                }
            }
            catch (IOException var1_2) {
                // empty catch block
            }
        }
        Class class_ = Jext.class;
        Jext.loadXMLProps(class_.getResourceAsStream("jext-text.props.xml"), "jext-text.props.xml");
        Jext.loadXMLProps(Jext.class.getResourceAsStream("jext-keys.props.xml"), "jext-keys.props.xml");
        Jext.loadXMLProps(Jext.class.getResourceAsStream("jext-defs.props.xml"), "jext-defs.props.xml");
        Jext.loadXMLProps(Jext.class.getResourceAsStream("jext-tips.props.xml"), "jext-tips.props.xml");
        object = new Properties();
        object.put("python.cachedir", SETTINGS_DIRECTORY + "pythoncache" + File.separator);
        PythonInterpreter.initialize((Properties)System.getProperties(), (Properties)object, (String[])new String[0]);
        Jext.initModes();
        Jext.initPlugins();
        if (usrProps != null) {
            props = new Properties(defaultProps);
            try {
                Jext.loadXMLProps(new FileInputStream(USER_PROPS), ".jext-props.xml");
            }
            catch (FileNotFoundException var2_4) {
            }
            catch (IOException var2_5) {
                // empty catch block
            }
        }
        Search.load();
        if (Utilities.JDK_VERSION.charAt(2) >= '4') {
            try {
                object3 = Class.forName("org.jext.JavaSupport");
                object2 = object3.getMethod("initJavaSupport", new Class[0]);
                if (object2 != null) {
                    object2.invoke(null, new Object[0]);
                }
            }
            catch (Exception var2_6) {
                // empty catch block
            }
        }
        System.getProperties().put("java.protocol.handler.pkgs", "org.jext.protocol|" + System.getProperty("java.protocol.handler.pkgs", ""));
        Jext.initActions();
        JARClassLoader.initPlugins();
        Jext.sortModes();
        Jext.assocPluginsToModes();
    }

    public static void setLanguage(String string) {
        language = string;
    }

    public static String getLanguage() {
        return language;
    }

    public static void executeScripts(JextFrame jextFrame) {
        int n;
        String string = SETTINGS_DIRECTORY + "scripts" + File.separator;
        String[] arrstring = Utilities.getWildCardMatches(string, "*.jext-script", false);
        if (arrstring == null) {
            return;
        }
        for (n = 0; n < arrstring.length; ++n) {
            org.jext.scripting.dawn.Run.runScript(string + arrstring[n], jextFrame, false);
        }
        arrstring = Utilities.getWildCardMatches(string, "*.py", false);
        if (arrstring == null) {
            return;
        }
        for (n = 0; n < arrstring.length; ++n) {
            Run.runScript(string + arrstring[n], jextFrame);
        }
    }

    private static void sortModes() {
        Object[] arrobject = new String[modes.size()];
        for (int i = 0; i < arrobject.length; ++i) {
            arrobject[i] = ((Mode)modes.get(i)).getUserModeName();
        }
        Arrays.sort(arrobject);
        ArrayList arrayList = new ArrayList(arrobject.length);
        for (int j = 0; j < arrobject.length; ++j) {
            int n = 0;
            Object object = arrobject[j];
            while (!((Mode)modes.get(n)).getUserModeName().equals(object)) {
                if (n == modes.size() - 1) break;
                ++n;
            }
            arrayList.add(modes.get(n));
        }
        modes = arrayList;
        arrayList = null;
    }

    private static void initUI() {
        SkinManager.applySelectedSkin();
        flatMenus = Jext.getBooleanProperty("flatMenus");
        buttonsHighlight = Jext.getBooleanProperty("buttonsHighlight");
        JextButton.setRollover(Jext.getBooleanProperty("toolbarRollover"));
    }

    private static void initModes() {
        StringTokenizer stringTokenizer = new StringTokenizer(Jext.getProperty("jext.modes"), " ");
        modes = new ArrayList(stringTokenizer.countTokens());
        modesFileFilters = new ArrayList(stringTokenizer.countTokens());
        while (stringTokenizer.hasMoreTokens()) {
            Mode mode = new Mode(stringTokenizer.nextToken());
            modes.add(mode);
            modesFileFilters.add(new ModeFileFilter(mode));
        }
    }

    public static Mode getMode(String string) {
        for (int i = 0; i < modes.size(); ++i) {
            Mode mode = (Mode)modes.get(i);
            if (!mode.getModeName().equalsIgnoreCase(string)) continue;
            return mode;
        }
        return null;
    }

    public static ArrayList getModes() {
        return modes;
    }

    public static void addMode(Mode mode) {
        modes.add(mode);
        modesFileFilters.add(new ModeFileFilter(mode));
    }

    public static void setProperty(String string, String string2) {
        if (string == null || string2 == null) {
            return;
        }
        props.put(string, string2);
    }

    public static boolean getBooleanProperty(String string) {
        String string2 = Jext.getProperty(string);
        if (string2 == null) {
            return false;
        }
        return string2.equals("on") || string2.equals("true");
    }

    public static boolean getBooleanProperty(String string, String string2) {
        String string3 = Jext.getProperty(string, string2);
        if (string3 == null) {
            return false;
        }
        return string3.equals("on") || string3.equals("true");
    }

    public static String getProperty(String string) {
        return props.getProperty(string);
    }

    public static final String getProperty(String string, String string2) {
        return props.getProperty(string, string2);
    }

    public static final String getProperty(String string, Object[] arrobject) {
        if (string == null) {
            return null;
        }
        if (arrobject == null) {
            return props.getProperty(string, string);
        }
        return MessageFormat.format(props.getProperty(string, string), arrobject);
    }

    public static void unsetProperty(String string) {
        if (defaultProps.get(string) != null) {
            props.put(string, "");
        } else {
            props.remove(string);
        }
    }

    public static void exit() {
        ArrayList arrayList = instances;
        synchronized (arrayList) {
            Object[] arrobject = instances.toArray();
            for (int i = arrobject.length - 1; i >= 0; --i) {
                JextFrame jextFrame = (JextFrame)arrobject[i];
                Jext.closeToQuit(jextFrame);
            }
        }
    }

    static void finalCleanupAndExit() {
        System.exit(0);
    }

    static void stopPlugins() {
        Plugin[] arrplugin = Jext.getPlugins();
        for (int i = 0; i < arrplugin.length; ++i) {
            try {
                arrplugin[i].stop();
                continue;
            }
            catch (Throwable var2_2) {
                System.err.println("#--An exception has occurred while stopping plugin:");
                var2_2.printStackTrace();
            }
        }
    }

    public static void closeToQuit(JextFrame jextFrame) {
        Jext.closeToQuit(jextFrame, false);
    }

    static void closeToQuit(JextFrame jextFrame, boolean bl) {
        if (bl) {
            runInBg = false;
        }
        jextFrame.closeToQuit();
    }

    public static void closeWindow(JextFrame jextFrame) {
        ArrayList arrayList = instances;
        synchronized (arrayList) {
            if (Jext.getWindowsCount() == 1) {
                jextFrame.fireJextEvent(101);
            } else {
                jextFrame.fireJextEvent(99);
            }
            jextFrame.closeWindow();
            if (Jext.getWindowsCount() == 0) {
                if (!Jext.isRunningBg()) {
                    Jext.stopServer();
                }
                Search.save();
                if (!Jext.isRunningBg()) {
                    Jext.stopPlugins();
                }
                jextFrame.saveConsole();
                GUIUtilities.saveGeometry(jextFrame, "jext");
                Jext.saveXMLProps("Jext v3.2 <Qu\u00e9bec> b03.02.00.03");
                jextFrame = null;
                System.gc();
                if (Jext.isRunningBg()) {
                    builtTextArea = Jext.newWindow(null, false);
                } else {
                    System.exit(0);
                }
            }
        }
    }

    public static SplashScreen getSplashScreen() {
        return splash;
    }

    public static void setSplashProgress(int n) {
        if (splash != null) {
            splash.setProgress(n);
        }
    }

    public static void setSplashText(String string) {
        if (splash != null) {
            splash.setText(string);
        }
    }

    public static void killSplashScreen() {
        if (splash != null) {
            splash.dispose();
            splash = null;
        }
    }

    public static void stopServer() {
        if (jextLoader != null) {
            jextLoader.stop();
            jextLoader = null;
        }
    }

    public static boolean isServerEnabled() {
        return isServerEnabled;
    }

    public static void setServerEnabled(boolean bl) {
        isServerEnabled = bl;
    }

    public static void loadInSingleJVMInstance(String[] arrstring) {
        block17 : {
            File file;
            BufferedReader bufferedReader;
            try {
                file = new File(SETTINGS_DIRECTORY + ".security");
                if (!file.exists()) {
                    isServerEnabled = true;
                } else {
                    bufferedReader = new BufferedReader(new FileReader(file));
                    isServerEnabled = new Boolean(bufferedReader.readLine());
                    bufferedReader.close();
                }
            }
            catch (IOException var1_2) {
                // empty catch block
            }
            if (!(isServerEnabled || runInBg)) {
                return;
            }
            file = new File(SETTINGS_DIRECTORY + ".auth-key");
            if (file.exists()) {
                try {
                    bufferedReader = new BufferedReader(new FileReader(file));
                    int n = Integer.parseInt(bufferedReader.readLine());
                    String string = bufferedReader.readLine();
                    bufferedReader.close();
                    Socket socket = new Socket("127.0.0.1", 49152 + n);
                    if (!runInBg) {
                        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                        StringBuffer stringBuffer = new StringBuffer();
                        if (goingToKill) {
                            stringBuffer.append("kill");
                        } else {
                            stringBuffer.append("load_jext:");
                            for (int i = 0; i < arrstring.length; ++i) {
                                stringBuffer.append(arrstring[i]);
                                if (i == arrstring.length - 1) continue;
                                stringBuffer.append('?');
                            }
                        }
                        stringBuffer.append(':').append(string);
                        printWriter.write(stringBuffer.toString());
                        printWriter.flush();
                        printWriter.close();
                    } else {
                        System.out.println("Jext is already running, either in background or foreground.");
                    }
                    socket.close();
                    System.exit(5);
                }
                catch (Exception var2_4) {
                    file.delete();
                    if (goingToKill) {
                        System.err.println("No jext instance found!");
                        System.exit(0);
                        break block17;
                    }
                    jextLoader = new JextLoader();
                }
            } else if (!goingToKill) {
                jextLoader = new JextLoader();
            } else {
                System.err.println("No jext instance found!");
                System.exit(0);
            }
        }
    }

    public static boolean isRunningBg() {
        return runInBg;
    }

    private static String[] checkArgsForBg(String[] arrstring) {
        int n = arrstring.length;
        ArrayList<String> arrayList = new ArrayList<String>(n);
        for (int i = 0; i < n; ++i) {
            if ("-bg".equals(arrstring[i])) {
                runInBg = true;
                continue;
            }
            if ("-kill".equals(arrstring[i])) {
                goingToKill = true;
                continue;
            }
            arrayList.add(arrstring[i]);
        }
        return arrayList.toArray(new String[0]);
    }

    public static void main(String[] arrstring) {
        System.setErr(System.out);
        Jext.initDirectories();
        arrstring = Jext.checkArgsForBg(arrstring);
        ArrayList arrayList = instances;
        synchronized (arrayList) {
            Jext.loadInSingleJVMInstance(arrstring);
            Jext.initProperties();
            if (!Jext.isRunningBg()) {
                splash = new SplashScreen();
                JextFrame jextFrame = Jext.newWindow(arrstring);
            } else {
                builtTextArea = Jext.newWindow(arrstring, false);
            }
        }
        if (Jext.getBooleanProperty("check")) {
            check = new VersionCheck();
        }
    }

    static {
        language = "English";
        flatMenus = true;
        buttonsHighlight = true;
        instances = new ArrayList(1);
        pythonActionHash = new HashMap();
        USER_PROPS = SETTINGS_DIRECTORY + ".jext-props.xml";
        runInBg = false;
        goingToKill = false;
        builtTextArea = null;
        Jext.initDirectories();
        Jext.initProperties();
        Jext.addJextKeyBindings();
        Jext.initActions();
        Jext.setProperty("editor.enterIndent", "true");
        Jext.setProperty("editor.softTab", "false");
    }
}

