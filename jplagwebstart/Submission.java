/*
 * Decompiled with CFR 0_102.
 */
package jplagwebstart;

import java.io.File;
import java.io.FilenameFilter;

public class Submission {
    public String name;
    public File dir;
    public String[] files = new String[0];
    private boolean readSubDirs = false;
    private String[] suffixes;
    static boolean exact_match = false;

    public Submission(String string, File file, boolean bl, String[] arrstring) throws InterruptedException {
        this.name = string;
        this.dir = file;
        this.readSubDirs = bl;
        this.suffixes = arrstring;
        this.lookupDir(file, "");
    }

    public Submission(String string, File file) {
        this.dir = file;
        this.name = string;
        this.readSubDirs = false;
        this.files = new String[1];
        this.files[0] = string;
    }

    private void lookupDir(File file, String string) throws InterruptedException {
        String[] arrstring;
        File file2 = new File(file, string);
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
        if (this.readSubDirs && (arrstring = file2.list(new FilenameFilter(){

            public boolean accept(File file, String string) {
                return new File(file, string).isDirectory();
            }
        })) != null) {
            int n;
            if (string != "") {
                for (n = 0; n < arrstring.length; ++n) {
                    this.lookupDir(file, string + File.separator + arrstring[n]);
                }
            } else {
                for (n = 0; n < arrstring.length; ++n) {
                    this.lookupDir(file, arrstring[n]);
                }
            }
        }
        arrstring = file2.list(new FilenameFilter(){

            public boolean accept(File file, String string) {
                if (!new File(file, string).isFile()) {
                    return false;
                }
                for (int i = 0; i < Submission.this.suffixes.length; ++i) {
                    if (!(!Submission.exact_match ? string.endsWith(Submission.this.suffixes[i]) : string.equals(Submission.this.suffixes[i]))) continue;
                    return true;
                }
                return false;
            }
        });
        String[] arrstring2 = this.files;
        this.files = new String[(arrstring2 != null ? arrstring2.length : 0) + (arrstring != null ? arrstring.length : 0)];
        if (arrstring != null) {
            if (string != "" && arrstring != null) {
                for (int i = 0; i < arrstring.length; ++i) {
                    this.files[i] = string + File.separator + arrstring[i];
                }
            } else {
                System.arraycopy(arrstring, 0, this.files, 0, arrstring.length);
            }
            if (arrstring2 != null) {
                System.arraycopy(arrstring2, 0, this.files, arrstring.length, arrstring2.length);
            }
        }
    }

}

