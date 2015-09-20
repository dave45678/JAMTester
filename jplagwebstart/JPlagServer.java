/*
 * Decompiled with CFR 0_102.
 */
package jplagwebstart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import jplagwebstart.JPlagOptions;
import jplagwebstart.Program;

public class JPlagServer {
    private static final String baseURL = "http://wwwipd.ira.uka.de:2222/";
    private static final String serverURL = "http://wwwipd.ira.uka.de:2222/server/receive.cgi";
    private static final String languageURL = "http://wwwipd.ira.uka.de:2222/server/language.cgi?";
    private static final String userPageURL = "http://wwwipd.ira.uka.de:2222/user.cgi?user=";
    static String[] languageCache = null;
    private static final int pollTime = 15;
    private static final int maxTries = 70;

    public static String[] getLanguages() {
        String[] arrstring = null;
        if (languageCache == null) {
            try {
                String string;
                URL uRL = new URL("http://wwwipd.ira.uka.de:2222/server/language.cgi?");
                URLConnection uRLConnection = uRL.openConnection();
                uRLConnection.setDoInput(true);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRLConnection.getInputStream()));
                ArrayList<String> arrayList = new ArrayList<String>();
                while ((string = bufferedReader.readLine()) != null) {
                    if ((string = string.trim()).equals("")) continue;
                    arrayList.add(string);
                }
                bufferedReader.close();
                languageCache = new String[arrayList.size()];
                arrayList.toArray(languageCache);
            }
            catch (MalformedURLException var1_2) {
                Program.message("MalformedURLException!");
            }
            catch (Exception var1_3) {
                Program.message("Exception!\n" + var1_3);
            }
        }
        arrstring = new String[languageCache.length / 3];
        for (int i = 0; i < languageCache.length / 3; ++i) {
            arrstring[i] = languageCache[3 * i];
        }
        return arrstring;
    }

    public static String getMML(String string) {
        if (languageCache == null) {
            JPlagServer.getLanguages();
        }
        for (int i = JPlagServer.languageCache.length - 3; i >= 0; i-=3) {
            if (!string.equals(languageCache[i])) continue;
            return languageCache[i + 2];
        }
        return "";
    }

    public static String getSuffixes(String string) {
        if (languageCache == null) {
            JPlagServer.getLanguages();
        }
        for (int i = JPlagServer.languageCache.length - 3; i >= 0; i-=3) {
            if (!string.equals(languageCache[i])) continue;
            return languageCache[i + 1];
        }
        return "";
    }

    public static String getUserPageAddress(JPlagOptions jPlagOptions) {
        return "http://wwwipd.ira.uka.de:2222/user.cgi?user=" + jPlagOptions.getUser();
    }

    public static URLConnection getServerConnection() throws IOException {
        URL uRL = new URL("http://wwwipd.ira.uka.de:2222/server/receive.cgi");
        URLConnection uRLConnection = uRL.openConnection();
        uRLConnection.setUseCaches(false);
        uRLConnection.setDoInput(true);
        uRLConnection.setDoOutput(true);
        return uRLConnection;
    }

    public static String waitForResults(String string) {
        String string2 = null;
        System.out.println("Waiting for results:");
        try {
            int n;
            for (n = 0; (string2 = JPlagServer.noResult(string)) != null && n < 70; ++n) {
                Thread.sleep(15000);
                System.out.print(".");
            }
            System.out.println();
            if (n == 70) {
                Program.message("Connection timed out!");
            }
        }
        catch (InterruptedException var2_3) {
            Program.message("Interupted Exception!?");
        }
        return string2;
    }

    public static String noResult(String string) {
        String string2 = null;
        try {
            String string3;
            URL uRL = new URL("http://wwwipd.ira.uka.de:2222/user.cgi?user=" + string);
            URLConnection uRLConnection = uRL.openConnection();
            uRLConnection.setDoInput(true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRLConnection.getInputStream()));
            while ((string3 = bufferedReader.readLine()) != null) {
                if (!string3.startsWith("<meta zip")) continue;
                string2 = string3.substring(11, string3.length() - 2);
            }
            bufferedReader.close();
        }
        catch (MalformedURLException var2_3) {
            Program.message("MalformedURLException!");
        }
        catch (Exception var2_4) {
            Program.message("Exception!\n" + var2_4);
        }
        return string2;
    }

    public static void programLog(String string) {
        System.out.println("Downloading program log:");
        try {
            String string2;
            URL uRL = new URL("http://wwwipd.ira.uka.de:2222/user.cgi?user=" + string + "&func=textoutput");
            URLConnection uRLConnection = uRL.openConnection();
            uRLConnection.setDoInput(true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRLConnection.getInputStream()));
            while ((string2 = bufferedReader.readLine()) != null) {
                System.out.println("  " + string2);
            }
            bufferedReader.close();
        }
        catch (MalformedURLException var1_2) {
            Program.message("MalformedURLException!");
        }
        catch (Exception var1_3) {
            Program.message("Exception!\n" + var1_3);
        }
    }

    public static void downloadResult(String string, File file) {
        System.out.println("Downloading results:");
        try {
            ZipEntry zipEntry;
            URL uRL = new URL("http://wwwipd.ira.uka.de:2222/" + string);
            URLConnection uRLConnection = uRL.openConnection();
            uRLConnection.setDoInput(true);
            ZipInputStream zipInputStream = new ZipInputStream(uRLConnection.getInputStream());
            if (!file.exists()) {
                file.mkdir();
            }
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                JPlagServer.copyFile(zipInputStream, new File(file, zipEntry.getName()));
                System.out.print(".");
                System.out.flush();
            }
            System.out.println();
            zipInputStream.close();
        }
        catch (MalformedURLException var2_3) {
            Program.message("MalformedURLException!");
        }
        catch (Exception var2_4) {
            Program.message("Exception!\n" + var2_4);
            var2_4.printStackTrace(System.out);
        }
    }

    public static void removeResult(String string) {
        System.out.println("Removing results from server...");
        try {
            String string2;
            URL uRL = new URL("http://wwwipd.ira.uka.de:2222/user.cgi?user=" + string + "&func=remove");
            URLConnection uRLConnection = uRL.openConnection();
            uRLConnection.setDoInput(true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRLConnection.getInputStream()));
            while ((string2 = bufferedReader.readLine()) != null) {
            }
            bufferedReader.close();
        }
        catch (MalformedURLException var1_2) {
            Program.message("MalformedURLException!");
        }
        catch (Exception var1_3) {
            Program.message("Exception!\n" + var1_3);
            var1_3.printStackTrace(System.out);
        }
    }

    public static void copyFile(InputStream inputStream, File file) {
        byte[] arrby = new byte[10000];
        long l = arrby.length;
        try {
            int n;
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            do {
                if ((n = inputStream.read(arrby)) == -1) continue;
                fileOutputStream.write(arrby, 0, n);
            } while (n != -1);
            fileOutputStream.close();
        }
        catch (IOException var5_5) {
            Program.message("Error copying file: " + var5_5.toString());
        }
    }
}

