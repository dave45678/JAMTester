/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import org.jext.JARClassLoader;
import org.jext.Jext;
import org.jext.misc.AbstractChoiceForm;
import org.jext.misc.AbstractPlugReader;

public class PluginDownload {
    private static URL autoUpdateVersionUrl;
    private static final String jarName = "autoUpdate.jar";
    private static URL autoUpdateJarUrl;
    private static final File downloadedJarPath;
    private static String defaultJarPath;
    private static ClassLoader loader;
    private static ClassLoader newVerLoader;
    private static ClassLoader defLoader;

    private PluginDownload() {
    }

    private static String getDefaultJarPath() {
        if (defaultJarPath == null) {
            defaultJarPath = Jext.JEXT_HOME + File.separator + ".." + File.separator + "lib" + File.separator + "autoUpdate.jar";
        }
        return defaultJarPath;
    }

    public static void downloadBoot() {
        try {
            InputStream inputStream = autoUpdateVersionUrl.openStream();
            byte[] arrby = new byte[10];
            inputStream.read(arrby);
            int n = Integer.parseInt(Jext.getProperty("plugDownload.core.version"));
            int n2 = Integer.parseInt(new String(arrby));
            if (n < n2) {
                int n3;
                BufferedInputStream bufferedInputStream = new BufferedInputStream(autoUpdateJarUrl.openStream());
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(downloadedJarPath));
                arrby = new byte[500];
                while ((n3 = bufferedInputStream.read(arrby)) != -1) {
                    bufferedOutputStream.write(arrby, 0, n3);
                }
                bufferedInputStream.close();
                bufferedOutputStream.close();
            }
        }
        catch (IOException var0_1) {
            System.err.println("Caught exception while trying to update autoUpdate.jar");
            var0_1.printStackTrace();
        }
        PluginDownload.buildChainingClassLoader();
    }

    private static boolean buildChainingClassLoader() {
        try {
            defLoader = new JARClassLoader(PluginDownload.getDefaultJarPath(), false, null);
        }
        catch (IOException var0) {
            System.err.println("You haven't installed correctly Jext! The autoUpdate.jar file is missing.It should be in this position: " + PluginDownload.getDefaultJarPath());
        }
        if (downloadedJarPath.exists()) {
            try {
                newVerLoader = new JARClassLoader(downloadedJarPath.getName(), false, defLoader);
            }
            catch (IOException var0_1) {
                // empty catch block
            }
        }
        if (newVerLoader != null) {
            loader = newVerLoader;
        } else if (defLoader != null) {
            loader = defLoader;
        } else {
            return false;
        }
        return true;
    }

    private static Object getInstanceFromLoader(String string) {
        if (loader != null) {
            try {
                return loader.loadClass(string).newInstance();
            }
            catch (InstantiationException var1_1) {
            }
            catch (IllegalAccessException var1_2) {
            }
            catch (ClassNotFoundException var1_3) {
                return null;
            }
        }
        if (defLoader != null && defLoader != loader) {
            try {
                return defLoader.loadClass(string).newInstance();
            }
            catch (InstantiationException var1_4) {
            }
            catch (IllegalAccessException var1_5) {
            }
            catch (ClassNotFoundException var1_6) {
                // empty catch block
            }
        }
        return null;
    }

    public static AbstractPlugReader getUpdater() {
        return (AbstractPlugReader)PluginDownload.getInstanceFromLoader("PlugReader");
    }

    public static AbstractChoiceForm getUpdateWindow() {
        return (AbstractChoiceForm)PluginDownload.getInstanceFromLoader("ChoiceForm");
    }

    public static Reader getDtd() {
        return new BufferedReader(new InputStreamReader(loader.getResourceAsStream("pluglist.dtd")));
    }

    static {
        downloadedJarPath = new File(Jext.SETTINGS_DIRECTORY + "autoUpdate.jar");
        loader = null;
        newVerLoader = null;
        defLoader = null;
        try {
            autoUpdateVersionUrl = new URL("http://www.jext.org/plugReader.version");
            autoUpdateJarUrl = new URL("http://www.jext.org/autoUpdate.jar");
        }
        catch (MalformedURLException var0) {
            // empty catch block
        }
    }
}

