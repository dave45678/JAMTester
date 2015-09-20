/*
 * Decompiled with CFR 0_102.
 */
package jamtester.jamupdate;

import jamtester.JAMProperties;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class JAMUpdate {
    private static final int version = 8;
    private static boolean updatedThisRun = false;
    private static boolean updateFailed;
    private static Map backups;
    private static String[] tocFiles;

    public static void doBackgroundUpdateIfNecessary() {
        Thread thread = new Thread(){

            public void run() {
                System.out.println(JAMUpdate.doUpdateIfNecessary());
            }
        };
        thread.start();
    }

    private static URL[] getNewFiles() {
        try {
            URL[] arruRL;
            ArrayList<URL> arrayList = new ArrayList<URL>();
            URL uRL = new URL("http://www.jamtester.com/jamupdate/toc.toc");
            URLConnection uRLConnection = uRL.openConnection();
            uRLConnection.connect();
            int n = uRLConnection.getContentLength();
            InputStream inputStream = uRLConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            byte[] arrby = new byte[n];
            int n2 = inputStream.read(arrby);
            stringBuffer.append(new String(arrby));
            System.out.println("Bytes read: " + n2 + " Size: " + n);
            StringTokenizer stringTokenizer = new StringTokenizer(stringBuffer.toString());
            while (stringTokenizer.hasMoreTokens()) {
                arruRL = stringTokenizer.nextToken();
                String string = stringTokenizer.nextToken();
                System.out.println("" + (String)arruRL + " " + string);
                if (!JAMUpdate.fileNeedsUpdate((String)arruRL, string)) continue;
                arrayList.add(new URL("http://www.jamtester.com/jamupdate/" + (String)arruRL));
            }
            inputStream.close();
            arruRL = new URL[arrayList.size()];
            for (int i = 0; i < arruRL.length; ++i) {
                arruRL[i] = (URL)arrayList.get(i);
            }
            return arruRL;
        }
        catch (Exception var0_1) {
            var0_1.printStackTrace(System.out);
            return new URL[0];
        }
    }

    private static void updateFile(URL uRL) {
        try {
            Object object;
            InputStream inputStream;
            FileInputStream fileInputStream = null;
            FileOutputStream fileOutputStream = null;
            File file = new File(uRL.getFile().substring("jamupdate/".length() + 1));
            if (file.exists()) {
                object = File.createTempFile("jamold", ".tmp");
                fileInputStream = new FileInputStream(file);
                fileOutputStream = new FileOutputStream((File)object);
                inputStream = (InputStream)new byte[fileInputStream.available()];
                fileInputStream.read((byte[])inputStream);
                fileOutputStream.write((byte[])inputStream);
                fileInputStream.close();
                fileOutputStream.close();
                backups.put(file, object);
            }
            object = uRL.openConnection();
            object.connect();
            inputStream = object.getInputStream();
            File file2 = File.createTempFile("jamupdate", ".tmp");
            int n = object.getContentLength();
            fileOutputStream = new FileOutputStream(file2);
            byte[] arrby = new byte[n];
            int n2 = 0;
            while (n2 + 1 < n) {
                int n3 = inputStream.read(arrby);
                System.out.println("Bytes read so far: " + (n2+=n3) + " Size: " + n);
                fileOutputStream.write(arrby, 0, n3);
            }
            System.out.println("got here after downloading " + arrby.length);
            fileOutputStream.close();
            fileInputStream = new FileInputStream(file2);
            fileOutputStream = new FileOutputStream(file);
            arrby = new byte[fileInputStream.available()];
            fileInputStream.read(arrby);
            fileOutputStream.write(arrby);
            fileInputStream.close();
            fileOutputStream.close();
            inputStream.close();
            file2.delete();
        }
        catch (Exception var1_2) {
            updateFailed = true;
            var1_2.printStackTrace(System.out);
        }
    }

    private static boolean fileNeedsUpdate(String string, String string2) {
        try {
            File file = new File(string);
            if (!file.exists()) {
                return true;
            }
            int n = Integer.parseInt(string2);
            FileInputStream fileInputStream = new FileInputStream(file);
            int n2 = fileInputStream.available();
            fileInputStream.close();
            if (n != n2) {
                return true;
            }
        }
        catch (Exception var2_3) {
            // empty catch block
        }
        return false;
    }

    public static void doThreadedUpdate(final boolean bl) {
        Thread thread = new Thread(){

            public void run() {
                System.out.println(JAMUpdate.doUpdateIfNecessary(bl));
            }
        };
        thread.start();
    }

    public static JMenuItem getNewMenuItem() {
        JMenuItem jMenuItem = new JMenuItem("Update JAM*Tester", 117);
        jMenuItem.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                JAMUpdate.doThreadedUpdate(true);
            }
        });
        return jMenuItem;
    }

    public static boolean isUpdateNecessary() {
        URL[] arruRL = JAMUpdate.getNewFiles();
        return arruRL.length > 0;
    }

    public static boolean doUpdateIfNecessary() {
        return JAMUpdate.doUpdateIfNecessary(false);
    }

    public static boolean doUpdateIfNecessary(boolean bl) {
        updateFailed = false;
        if (updatedThisRun) {
            JOptionPane.showMessageDialog(null, "An update has already been downloaded and extracted.  Please restart JAM*Tester for the changes to take effect.", "Update waiting", 0);
            return false;
        }
        if (!JAMUpdate.isUpdateNecessary()) {
            if (bl) {
                JOptionPane.showMessageDialog(null, "No update to JAM*Tester is available at this time.", "No update available", 1);
            }
            return false;
        }
        int n = JOptionPane.showConfirmDialog(null, "A newer version of JAM*Tester is available!  If you choose yes, the update will be downloaded while you work.  To keep the same version, choose no.", "Update available", 0);
        if (n == 1) {
            return false;
        }
        URL[] arruRL = JAMUpdate.getNewFiles();
        backups = new HashMap();
        for (int i = 0; i < arruRL.length; ++i) {
            JAMUpdate.updateFile(arruRL[i]);
        }
        updatedThisRun = true;
        if (!updateFailed) {
            JOptionPane.showMessageDialog(null, "JAM*Tester has been updated.  Please save your work and then restart the program.", "Update complete", 1);
        } else {
            JAMUpdate.restoreBackups();
            JOptionPane.showMessageDialog(null, "JAM*Tester failed to update", "Update failure", 0);
        }
        if (!updateFailed) {
            JAMProperties jAMProperties = JAMProperties.loadProperties();
            jAMProperties.setFirstRun(true);
            jAMProperties.save();
        }
        return true;
    }

    private static void extractJar(File file) throws Exception {
        try {
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement();
                if (zipEntry.isDirectory()) continue;
                long l = zipEntry.getSize();
                InputStream inputStream = zipFile.getInputStream(zipEntry);
                FileOutputStream fileOutputStream = new FileOutputStream(zipEntry.getName());
                int n = 0;
                while ((long)n < l) {
                    fileOutputStream.write(inputStream.read());
                    ++n;
                }
                fileOutputStream.close();
                inputStream.close();
            }
        }
        catch (Exception var1_2) {
            var1_2.printStackTrace(System.out);
        }
    }

    private static void generateTOC() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("toc.toc");
            PrintStream printStream = new PrintStream(fileOutputStream);
            for (int i = 0; i < tocFiles.length; ++i) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(tocFiles[i]);
                    printStream.println(tocFiles[i] + " " + fileInputStream.available());
                    fileInputStream.close();
                    continue;
                }
                catch (Exception var3_5) {
                    var3_5.printStackTrace(System.out);
                }
            }
            fileOutputStream.close();
        }
        catch (Exception var0_1) {
            var0_1.printStackTrace(System.out);
        }
    }

    private static void restoreBackups() {
        Set set = backups.keySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            try {
                Object k = iterator.next();
                FileInputStream fileInputStream = new FileInputStream((File)backups.get(k));
                FileOutputStream fileOutputStream = new FileOutputStream((File)k);
                byte[] arrby = new byte[fileInputStream.available()];
                fileInputStream.read(arrby);
                fileOutputStream.write(arrby);
                fileInputStream.close();
                fileOutputStream.close();
            }
            catch (Exception var2_3) {
                var2_3.printStackTrace(System.err);
            }
        }
    }

    public static void main(String[] arrstring) {
        JAMUpdate.generateTOC();
    }

    static {
        tocFiles = new String[]{"Bug.jpg", "BugMouseOver.jpg", "jamt.jpg", "JAMTester.jar", "junit.jar", "SquishedBug.jpg", "Student.ico", "StudentVersion.jpg", "Teacher.ico", "jext.jar", "jython.jar", "dawn.jar", "clover.jar", "clover.license", "changelog.txt", "houston.jar", "apollo.jar", "logo.gif", "icon.gif"};
    }

}

