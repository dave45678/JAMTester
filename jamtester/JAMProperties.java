/*
 * Decompiled with CFR 0_102.
 */
package jamtester;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import jplagwebstart.JPlagOptions;

public class JAMProperties {
    private boolean firstRun = true;
    private JPlagOptions options;
    private String lastVersionRun = "Teacher";
    private String jdkLoc;
    private String lastFolder;
    private static String version = "teacher";

    public String getLastVersionRun() {
        return this.lastVersionRun;
    }

    public String getJDKLoc() {
        return this.jdkLoc;
    }

    public void setJDKLoc(String string) {
        this.jdkLoc = string;
    }

    public void setLastVersionRun(String string) {
        this.lastVersionRun = string;
    }

    public boolean getFirstRun() {
        return this.firstRun;
    }

    public void setFirstRun(boolean bl) {
        this.firstRun = bl;
    }

    public String getLastFolder() {
        return this.lastFolder;
    }

    public void setLastFolder(String string) {
        this.lastFolder = string;
    }

    public void setJPlagOptions(JPlagOptions jPlagOptions) {
        this.options = jPlagOptions;
    }

    public JPlagOptions getJPlagOptions() {
        return this.options;
    }

    public void save() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(System.getProperty("user.home"), "jam.properties"));
            XMLEncoder xMLEncoder = new XMLEncoder(fileOutputStream);
            xMLEncoder.writeObject(this);
            xMLEncoder.close();
        }
        catch (Exception var1_2) {
            var1_2.printStackTrace(System.err);
        }
    }

    public static JAMProperties loadProperties() {
        JAMProperties jAMProperties = new JAMProperties();
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(System.getProperty("user.home"), "jam.properties"));
            XMLDecoder xMLDecoder = new XMLDecoder(fileInputStream);
            jAMProperties = (JAMProperties)xMLDecoder.readObject();
            xMLDecoder.close();
        }
        catch (Exception var1_2) {
            var1_2.printStackTrace(System.err);
        }
        return jAMProperties;
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String string) {
        version = string.toLowerCase();
    }
}

