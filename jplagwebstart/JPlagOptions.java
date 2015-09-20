/*
 * Decompiled with CFR 0_102.
 */
package jplagwebstart;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

public class JPlagOptions {
    private String user = "";
    private String language = "java12";
    private String basecode = null;
    private String originalDir = null;
    private int mml = 2;
    private String password = null;
    private String subDir = "";
    private String suffixS = null;
    private String[] suffixes = null;
    private boolean recurse = false;

    public String getParameterString() {
        String string = "";
        string = string + "-l " + this.language;
        string = string + " -t " + this.mml;
        if (this.suffixes != null) {
            string = string + " -p " + this.suffixS;
        }
        if (!(this.subDir == null || this.subDir.equals(""))) {
            string = string + " -S " + this.subDir;
        }
        if (this.recurse) {
            string = string + " -s";
        }
        string = string + " -d \"" + this.originalDir + "\"";
        if (this.basecode != null) {
            string = string + " -bc \"" + this.basecode + "\"";
        }
        return string;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String string) {
        this.user = string;
    }

    public void setLanguage(String string) {
        this.language = string;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setOriginalDir(String string) {
        this.originalDir = string.trim();
    }

    public String getOriginalDir() {
        return this.originalDir;
    }

    public void setBasecode(String string) {
        this.basecode = string.trim();
    }

    public String getBasecode() {
        return this.basecode;
    }

    public String setMML(String string) {
        int n;
        try {
            n = Integer.valueOf(string);
            if (n < 2) {
                return "`" + string + "` has to be bigger than 1.";
            }
        }
        catch (NumberFormatException var3_3) {
            return "`" + string + "` is not an integer!\n";
        }
        this.mml = n;
        return null;
    }

    public int getMML() {
        return this.mml;
    }

    public void setPassword(String string) {
        this.password = string;
    }

    public String getPassword() {
        return this.password;
    }

    public void setSubDir(String string) {
        String string2 = string.trim();
        this.subDir = !string2.equals("") ? string2 : null;
    }

    public String getSubDir() {
        return this.subDir;
    }

    public String setSuffixes(String string) {
        if (string.equals("")) {
            return "No Suffixes given!";
        }
        Vector<String> vector = new Vector<String>();
        try {
            StringTokenizer stringTokenizer = new StringTokenizer(string, ",");
            while (stringTokenizer.hasMoreTokens()) {
                String string2 = stringTokenizer.nextToken();
                string2.trim();
                if (string2.equals("")) continue;
                vector.addElement(string2);
            }
        }
        catch (NoSuchElementException var4_4) {
            return var4_4.toString() + "\n";
        }
        this.suffixes = new String[vector.size()];
        vector.copyInto(this.suffixes);
        this.suffixS = string;
        return null;
    }

    public String[] getSuffixes() {
        return this.suffixes;
    }

    public String getSuffixString() {
        return this.suffixS;
    }

    public void setRecurse(boolean bl) {
        this.recurse = bl;
    }

    public boolean getRecurse() {
        return this.recurse;
    }
}

