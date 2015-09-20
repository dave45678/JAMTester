/*
 * Decompiled with CFR 0_102.
 */
package org.jext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JOptionPane;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.Plugin;
import org.jext.Utilities;
import org.jext.scripting.dawn.Run;

public class JARClassLoader
extends ClassLoader {
    public static ArrayList pluginsNames = new ArrayList();
    public static ArrayList pluginsRealNames = new ArrayList();
    private static final String langsPrefix = "trans" + File.separator;
    private static ArrayList disabledPlugins = new ArrayList();
    private static final String DISABLED_LIST_PATH = Jext.SETTINGS_DIRECTORY + ".disabledPlugins";
    private static ArrayList classLoaders;
    private int index;
    private ArrayList pluginClasses = new ArrayList();
    private URL url;
    private ZipFile zipFile;

    public JARClassLoader(String string) throws IOException {
        this(string, true, null);
    }

    public JARClassLoader(String string, boolean bl, ClassLoader classLoader) throws IOException {
        super(classLoader);
        this.url = new File(string).toURL();
        this.zipFile = new ZipFile(string);
        if (bl) {
            int n = string.lastIndexOf(File.separator);
            String string2 = string.substring(n + 1, string.length() - 4);
            if (!JARClassLoader.isEnabled(string2)) {
                Object[] arrobject = new String[]{string2};
                System.err.println(Jext.getProperty("jar.disabled", arrobject));
                pluginsRealNames.add(string2);
                pluginsNames.add(string2);
                return;
            }
            String string3 = langsPrefix + Jext.getLanguage() + File.separator;
            Enumeration<? extends ZipEntry> enumeration = this.zipFile.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement();
                String string4 = zipEntry.getName();
                String string5 = string4.toLowerCase();
                if (string5.startsWith(langsPrefix)) continue;
                if (string5.endsWith(".props")) {
                    Jext.loadProps(this.zipFile.getInputStream(zipEntry));
                    continue;
                }
                if (string5.endsWith(".props.xml")) {
                    InputStream inputStream = this.zipFile.getInputStream(zipEntry);
                    Jext.loadXMLProps(inputStream, string4, false);
                    ZipEntry zipEntry2 = this.zipFile.getEntry(string3 + string4);
                    inputStream = zipEntry2 != null ? this.zipFile.getInputStream(zipEntry2) : Jext.getLanguageStream(this.zipFile.getInputStream(zipEntry), string4);
                    Jext.loadXMLProps(inputStream, string4, false);
                    continue;
                }
                if (string5.endsWith(".actions.xml")) {
                    Jext.loadXMLActions(this.zipFile.getInputStream(zipEntry), string4);
                    continue;
                }
                if (!string4.endsWith("Plugin.class")) continue;
                this.pluginClasses.add(string4);
                pluginsNames.add(string4);
                pluginsRealNames.add(string2);
            }
            this.index = classLoaders.size();
            classLoaders.add(this);
        }
    }

    public static void setEnabled(String string, boolean bl) {
        int n = disabledPlugins.indexOf(string);
        if (bl) {
            if (n != -1) {
                disabledPlugins.remove(n);
            }
        } else if (n == -1) {
            disabledPlugins.add(string);
        }
    }

    public static boolean isEnabled(String string) {
        return disabledPlugins.indexOf(string) == -1;
    }

    static void saveDisabledList() {
        try {
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(DISABLED_LIST_PATH)));
            Iterator iterator = disabledPlugins.iterator();
            while (iterator.hasNext()) {
                printWriter.println((String)iterator.next());
            }
            printWriter.close();
        }
        catch (IOException var0_1) {
            // empty catch block
        }
    }

    public Class loadClass(String string, boolean bl) throws ClassNotFoundException {
        return this.loadClassFromZip(string, bl, true);
    }

    public InputStream getResourceAsStream(String string) {
        try {
            ZipEntry zipEntry = this.zipFile.getEntry(string);
            if (zipEntry == null) {
                return JARClassLoader.getSystemResourceAsStream(string);
            }
            return this.zipFile.getInputStream(zipEntry);
        }
        catch (IOException var2_3) {
            return null;
        }
    }

    public URL getResource(String string) {
        try {
            return new URL(this.getResourceAsPath(string));
        }
        catch (MalformedURLException var2_2) {
            return null;
        }
    }

    public String getResourceAsPath(String string) {
        return "jextresource:" + this.index + "/" + string;
    }

    public String getPath() {
        return this.zipFile.getName();
    }

    public static void initPlugins() {
        for (int i = 0; i < classLoaders.size(); ++i) {
            JARClassLoader jARClassLoader = (JARClassLoader)classLoaders.get(i);
            jARClassLoader.loadAllPlugins();
        }
    }

    public static JARClassLoader getClassLoader(int n) {
        return (JARClassLoader)classLoaders.get(n);
    }

    public static int getClassLoaderCount() {
        return classLoaders.size();
    }

    public static void reloadPluginsProperties() throws IOException {
        for (int i = 0; i < classLoaders.size(); ++i) {
            JARClassLoader jARClassLoader = (JARClassLoader)classLoaders.get(i);
            ZipFile zipFile = jARClassLoader.getZipFile();
            Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            String string = langsPrefix + Jext.getLanguage() + File.separator;
            while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement();
                String string2 = zipEntry.getName();
                String string3 = string2.toLowerCase();
                if (string3.startsWith(langsPrefix)) continue;
                if (string3.endsWith(".props")) {
                    Jext.loadProps(zipFile.getInputStream(zipEntry));
                    continue;
                }
                if (!string3.endsWith(".props.xml")) continue;
                ZipEntry zipEntry2 = zipFile.getEntry(string + string2);
                InputStream inputStream = zipEntry2 != null ? zipFile.getInputStream(zipEntry2) : Jext.getLanguageStream(zipFile.getInputStream(zipEntry), string2);
                Jext.loadXMLProps(inputStream, string2, false);
            }
        }
    }

    public static void executeScripts(JextFrame jextFrame) {
        for (int i = 0; i < classLoaders.size(); ++i) {
            JARClassLoader jARClassLoader = (JARClassLoader)classLoaders.get(i);
            ZipFile zipFile = jARClassLoader.getZipFile();
            Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement();
                String string = zipEntry.getName().toLowerCase();
                if (!string.endsWith(".jext-script") && !string.endsWith(".py")) continue;
                try {
                    String string2;
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(zipEntry)));
                    StringBuffer stringBuffer = new StringBuffer();
                    while ((string2 = bufferedReader.readLine()) != null) {
                        stringBuffer.append(string2).append('\n');
                    }
                    if (string.endsWith(".jext-script")) {
                        Run.execute(stringBuffer.toString(), jextFrame, false);
                        continue;
                    }
                    org.jext.scripting.python.Run.execute(stringBuffer.toString(), jextFrame);
                }
                catch (IOException var7_8) {}
            }
        }
    }

    public ZipFile getZipFile() {
        return this.zipFile;
    }

    private void loadAllPlugins() {
        for (int i = 0; i < this.pluginClasses.size(); ++i) {
            String string = (String)this.pluginClasses.get(i);
            try {
                this.loadPluginClass(string);
                continue;
            }
            catch (Throwable var3_3) {
                Object[] arrobject = new String[]{string};
                System.err.println(Jext.getProperty("jar.error.init", arrobject));
                var3_3.printStackTrace();
            }
        }
    }

    private void loadPluginClass(String string) throws Exception {
        string = Utilities.fileToClass(string);
        Plugin[] arrplugin = Jext.getPlugins();
        for (int i = 0; i < arrplugin.length; ++i) {
            if (!arrplugin[i].getClass().getName().equals(string)) continue;
            Object[] arrobject = new String[]{string};
            System.err.println(Jext.getProperty("jar.error.duplicateName", arrobject));
            return;
        }
        if (!this.checkDependencies(string)) {
            return;
        }
        Class class_ = this.loadClass(string, true);
        int n = class_.getModifiers();
        Class class_2 = Plugin.class;
        if (!(!class_2.isAssignableFrom(class_) || Modifier.isInterface(n) || Modifier.isAbstract(n))) {
            Plugin plugin = (Plugin)class_.newInstance();
            Jext.addPlugin(plugin);
            int n2 = string.lastIndexOf(46);
            string = string.substring(n2 == -1 ? 0 : n2 + 1);
            Object[] arrobject = new String[]{Jext.getProperty("plugin." + string + ".name")};
            System.out.println(Jext.getProperty("jar.loaded", arrobject));
        }
    }

    private boolean checkDependencies(String string) {
        Object[] arrobject;
        int n;
        String string2;
        int n2 = 0;
        StringBuffer stringBuffer = new StringBuffer();
        boolean bl = true;
        while ((string2 = Jext.getProperty("plugin." + string + ".depend." + n2++)) != null) {
            n = string2.indexOf(32);
            if (n == -1) {
                stringBuffer.append(string2);
                stringBuffer.append('\n');
                bl = false;
                continue;
            }
            arrobject = string2.substring(0, n);
            String string3 = string2.substring(n + 1);
            Object[] arrobject2 = new String[1];
            arrobject2[0] = arrobject.equals("jext") ? "03.02.00.03" : string3;
            stringBuffer.append(Jext.getProperty("jar.what." + (String)arrobject, arrobject2));
            stringBuffer.append('\n');
            if (arrobject.equals("jdk")) {
                if (System.getProperty("java.version").compareTo(string3) >= 0) continue;
                bl = false;
                continue;
            }
            if (arrobject.equals("deprecateJDK")) {
                if (System.getProperty("java.version").compareTo(string3) < 0) continue;
                bl = false;
                continue;
            }
            if (arrobject.equals("jext")) {
                if ("03.02.00.03".compareTo(string3) >= 0) continue;
                bl = false;
                continue;
            }
            if (arrobject.equals("os")) {
                bl = System.getProperty("os.name").indexOf(string3) != -1;
                continue;
            }
            if (arrobject.equals("class")) {
                try {
                    this.loadClass(string3, false);
                }
                catch (Exception var10_11) {
                    bl = false;
                }
                continue;
            }
            bl = false;
        }
        if (!(bl || Jext.getProperty("plugin." + string + ".disabled") != null)) {
            n = string.lastIndexOf(46);
            string = string.substring(n == -1 ? 0 : n + 1);
            arrobject = new String[]{Jext.getProperty("plugin." + string + ".name"), stringBuffer.toString()};
            int n3 = JOptionPane.showConfirmDialog(null, Jext.getProperty("plugin.disable.question", arrobject), Jext.getProperty("plugin.disable.title"), 0, 3);
            Jext.setProperty("plugin." + string + ".disabled", n3 == 0 ? "yes" : "no");
        }
        return bl;
    }

    private Class findOtherClass(String string, boolean bl) throws ClassNotFoundException {
        for (int i = 0; i < classLoaders.size(); ++i) {
            JARClassLoader jARClassLoader = (JARClassLoader)classLoaders.get(i);
            Class class_ = jARClassLoader.loadClassFromZip(string, bl, false);
            if (class_ == null) continue;
            return class_;
        }
        ClassLoader classLoader = this.getClass().getClassLoader();
        if (classLoader != null) {
            return classLoader.loadClass(string);
        }
        return this.findSystemClass(string);
    }

    private Class loadClassFromZip(String string, boolean bl, boolean bl2) throws ClassNotFoundException {
        Class class_ = this.findLoadedClass(string);
        if (class_ != null) {
            if (bl) {
                this.resolveClass(class_);
            }
            return class_;
        }
        String string2 = Utilities.classToFile(string);
        try {
            String string3;
            ZipEntry zipEntry = this.zipFile.getEntry(string2);
            if (zipEntry == null) {
                if (bl2) {
                    return this.findOtherClass(string, bl);
                }
                return null;
            }
            InputStream inputStream = this.zipFile.getInputStream(zipEntry);
            int n = (int)zipEntry.getSize();
            byte[] arrby = new byte[n];
            int n2 = 0;
            int n3 = 0;
            while (n2 < n) {
                if ((n2 = inputStream.read(arrby, n3+=n2, n-=n2)) != -1) continue;
                Object[] arrobject = new String[]{string, this.zipFile.getName()};
                System.err.println(Jext.getProperty("jar.error.zip", arrobject));
                throw new ClassNotFoundException(string);
            }
            int n4 = string.lastIndexOf(46);
            String string4 = string3 = n4 < 0 ? null : string2.replace('/', '.').substring(0, n4);
            if (string3 != null && this.getPackage(string3) == null) {
                Package package_ = this.definePackage(string3, null, null, null, null, null, null, this.url);
            }
            class_ = this.defineClass(string, arrby, 0, arrby.length);
            if (bl) {
                this.resolveClass(class_);
            }
            return class_;
        }
        catch (IOException var6_7) {
            throw new ClassNotFoundException(string);
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.url = null;
        this.zipFile = null;
    }

    static {
        try {
            File file = new File(DISABLED_LIST_PATH);
            if (file.exists()) {
                String string;
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                while ((string = bufferedReader.readLine()) != null) {
                    disabledPlugins.add(string);
                }
                bufferedReader.close();
            }
        }
        catch (IOException var0_1) {
            var0_1.printStackTrace();
        }
        classLoaders = new ArrayList();
    }
}

