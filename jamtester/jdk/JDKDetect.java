/*
 * Decompiled with CFR 0_102.
 */
package jamtester.jdk;

import jamtester.JAMProperties;
import jamtester.jdk.JDKChooser;
import java.awt.Frame;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public final class JDKDetect {
    private JProgressBar jPb;
    private static ClassLoader cl;
    public static final int MAX_FOLDER_LEVEL = 5;

    public JDKDetect(JProgressBar jProgressBar) {
        this.jPb = jProgressBar;
        if (jProgressBar == null) {
            this.jPb = new JProgressBar();
        }
    }

    public void findJDKs(ArrayList arrayList) {
        int n;
        arrayList.clear();
        File[] arrfile = File.listRoots();
        ArrayList[] arrarrayList = new ArrayList[arrfile.length];
        int n2 = 0;
        int n3 = 0;
        this.jPb.setMinimum(0);
        this.jPb.setMaximum(arrfile.length);
        for (n = 0; n < arrfile.length; ++n) {
            this.jPb.setString("Populating folder list for: " + arrfile[n]);
            arrarrayList[n] = this.getAllFolders(arrfile[n]);
            n2+=arrarrayList[n].size();
            this.jPb.setValue(n + 1);
        }
        this.jPb.setMinimum(0);
        this.jPb.setMaximum(n2);
        this.jPb.setValue(0);
        for (n = 0; n < arrfile.length; ++n) {
            ArrayList arrayList2 = arrarrayList[n];
            for (int i = 0; i < arrayList2.size(); ++i) {
                this.jPb.setString("Scanning...  " + arrarrayList[n].get(i));
                if (JDKDetect.isJDK((File)arrayList2.get(i))) {
                    arrayList.add(arrayList2.get(i));
                }
                this.jPb.setValue(++n3);
            }
        }
        this.jPb.setString("Finished detecting JDKs!");
    }

    public static void loadJDK() {
        JDKDetect.copyTools();
    }

    public static void selectNewJDK() {
        JAMProperties jAMProperties = JAMProperties.loadProperties();
        String string = jAMProperties.getJDKLoc();
        JDKChooser jDKChooser = new JDKChooser(null, true);
        jDKChooser.setVisible(true);
        jAMProperties = JAMProperties.loadProperties();
        JDKDetect.copyTools(true);
        if (!string.equals(jAMProperties.getJDKLoc())) {
            JOptionPane.showMessageDialog(null, "You must restart JAM*Tester before the changes can be applied.\n\nJAM*Tester will now be terminated.", "Restart JAM*Tester", 1);
        }
    }

    private static void copyTools() {
        JDKDetect.copyTools(false);
    }

    public static boolean needsToHaveJDK() {
        try {
            Class.forName("com.sun.tools.javac.Main");
            return new File("tools.jar").exists();
        }
        catch (Exception var0) {
            return true;
        }
    }

    private static void copyTools(boolean bl) {
        if (!JDKDetect.needsToHaveJDK()) {
            return;
        }
        JAMProperties jAMProperties = JAMProperties.loadProperties();
        String string = jAMProperties.getJDKLoc();
        if (string == null) {
            string = "";
        }
        File file = new File(string, "lib" + File.separator + "tools.jar");
        if (!(bl || file.exists())) {
            JDKDetect.selectNewJDK();
            System.exit(0);
        }
        File file2 = new File("tools.jar");
        try {
            FileInputStream fileInputStream;
            FileInputStream fileInputStream2 = new FileInputStream(file);
            try {
                fileInputStream = new FileInputStream(file2);
                if (fileInputStream.available() == fileInputStream2.available()) {
                    fileInputStream.close();
                    fileInputStream2.close();
                    return;
                }
                fileInputStream.close();
            }
            catch (Exception var6_8) {
                // empty catch block
            }
            fileInputStream = new FileOutputStream(file2);
            byte[] arrby = new byte[fileInputStream2.available()];
            fileInputStream2.read(arrby);
            fileInputStream.write(arrby);
            fileInputStream2.close();
            fileInputStream.close();
        }
        catch (Exception var5_6) {
            var5_6.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "The JDK configuration has been changed.  You must restart JAM*Tester before the changes can be applied.\n\nJAM*Tester must now be restarted and will be closed.", "Restart JAM*Tester", 1);
        System.exit(0);
    }

    private ArrayList getAllFolders(File file) {
        ArrayList arrayList = new ArrayList();
        this.getAllFolders(file, arrayList);
        return arrayList;
    }

    private void getAllFolders(File file, ArrayList arrayList) {
        if (!file.exists()) {
            return;
        }
        ArrayList<File> arrayList2 = new ArrayList<File>();
        arrayList2.add(file);
        ArrayList<File> arrayList3 = new ArrayList<File>();
        for (int i = 0; i < 5 && arrayList2.size() > 0; ++i) {
            for (int j = 0; j < arrayList2.size(); ++j) {
                arrayList.add(arrayList2.get(j));
                File file2 = (File)arrayList2.get(j);
                File[] arrfile = file2.listFiles(new FileFilter(){

                    public boolean accept(File file) {
                        return file.isDirectory() && !file.isHidden();
                    }
                });
                if (arrfile == null) break;
                for (int k = 0; k < arrfile.length; ++k) {
                    System.out.println(arrfile[k]);
                    arrayList3.add(arrfile[k]);
                }
            }
            System.out.println("Got here");
            arrayList2 = arrayList3;
            arrayList3 = new ArrayList();
        }
    }

    public static boolean isJDK(File file) {
        return new File(file, "lib" + File.separator + "tools.jar").exists();
    }

    private boolean containsToolsJar(File file) {
        File[] arrfile = file.listFiles();
        for (int i = 0; i < arrfile.length; ++i) {
            if (!arrfile[i].getName().equalsIgnoreCase("tools.jar")) continue;
            return true;
        }
        return false;
    }

    public static ClassLoader getAugmentedClassLoader() {
        return cl;
    }

}

