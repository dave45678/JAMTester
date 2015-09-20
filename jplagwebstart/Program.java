/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  apollo.BasicService
 *  apollo.ServiceManager
 */
package jplagwebstart;

import apollo.BasicService;
import apollo.ServiceManager;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import jplagwebstart.JPlagGUI;
import jplagwebstart.JPlagOptions;
import jplagwebstart.JPlagServer;
import jplagwebstart.ProgressBar;
import jplagwebstart.Submission;
import jplagwebstart.TextProgressBar;

public class Program {
    public static final String version = "JAM*Tester";
    public static final String nameLong = "JPlag-WebStart (Version JAM*Tester)";
    private static final long size_limit = 10000000;
    private static final long file_limit = 10000;
    private static final char directoryChar = '/';
    private static JPlagGUI submit = null;
    private static boolean verbose = false;

    public static void message(String string) {
        if (submit != null) {
            submit.message(string);
        } else {
            System.out.println(string);
        }
    }

    public static String[] checkSubmissions(Vector vector) {
        String[] arrstring = new String[3];
        int n = vector.size();
        long l = 0;
        int n2 = 0;
        arrstring[2] = null;
        arrstring[1] = null;
        arrstring[0] = null;
        for (int i = 0; i < n; ++i) {
            Submission submission = (Submission)vector.elementAt(i);
            for (int j = submission.files.length - 1; j >= 0; --j) {
                File file = new File(submission.dir, submission.files[j]);
                l+=file.length();
                ++n2;
            }
        }
        arrstring[1] = "" + n2 + " files";
        arrstring[2] = "" + l / 1024 + " KB";
        if (n2 == 0 || l == 0) {
            arrstring[0] = "Not enough files!";
        } else if ((long)n2 > 10000) {
            arrstring[0] = "Too many files! Current Limit is: 10000 files";
        } else if (l > 10000000) {
            arrstring[0] = "The submission is too large! Current Limit is:9765 KB\n";
        }
        return arrstring;
    }

    public static boolean send(JPlagOptions jPlagOptions, ProgressBar progressBar) {
        boolean bl = false;
        try {
            Object object;
            progressBar.setBar("Reading directories...");
            Vector vector = null;
            vector = Program.scanFiles(jPlagOptions);
            if (vector == null) {
                Program.message("No files found!");
                return false;
            }
            String[] arrstring = Program.checkSubmissions(vector);
            if (arrstring[0] != null) {
                Program.message(arrstring[0]);
                return false;
            }
            int n = vector.size();
            progressBar.setBar("Uploading files...\n");
            URLConnection uRLConnection = JPlagServer.getServerConnection();
            ZipOutputStream zipOutputStream = new ZipOutputStream(uRLConnection.getOutputStream());
            zipOutputStream.setLevel(9);
            zipOutputStream.setMethod(8);
            zipOutputStream.setComment(jPlagOptions.getUser() + "\n" + jPlagOptions.getPassword() + "\n" + jPlagOptions.getParameterString() + "\n" + "WebStart\n");
            progressBar.setSize(n);
            int n2 = new File(jPlagOptions.getOriginalDir()).getAbsolutePath().length();
            ++n2;
            Vector<String> vector2 = new Vector<String>();
            vector2.addElement("");
            for (int i = 0; i < n; ++i) {
                object = (Submission)vector.elementAt(i);
                for (int j = object.files.length - 1; j >= 0; --j) {
                    String string;
                    File file = new File(object.dir, object.files[j]);
                    String string2 = new File(file.getParent()).getAbsolutePath();
                    String string3 = string = string2.length() >= n2 ? string2.substring(n2) : "";
                    if ('/' != File.separatorChar) {
                        string = string.replace(File.separatorChar, '/');
                    }
                    if (!vector2.contains(string)) {
                        vector2.addElement(string);
                        zipOutputStream.putNextEntry(new ZipEntry(string + '/'));
                        zipOutputStream.closeEntry();
                    }
                    String string4 = file.getAbsolutePath().substring(n2);
                    if ('/' != File.separatorChar) {
                        string4 = string4.replace(File.separatorChar, '/');
                    }
                    zipOutputStream.putNextEntry(new ZipEntry(string4));
                    Program.copyFile(file, zipOutputStream);
                    zipOutputStream.closeEntry();
                }
                progressBar.setBar(i + 1);
            }
            zipOutputStream.close();
            progressBar.setBar("Waiting for reply...\n");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRLConnection.getInputStream()));
            while ((object = bufferedReader.readLine()) != null) {
                if (object.startsWith("OK")) {
                    bl = true;
                    continue;
                }
                Program.message((String)object);
            }
            bufferedReader.close();
            if (submit != null && bl) {
                BasicService basicService = ServiceManager.lookupBasicService();
                basicService.showDocument(new URL(JPlagServer.getUserPageAddress(jPlagOptions)));
            }
        }
        catch (MalformedURLException var3_4) {
            Program.message("MalformedURLException!");
            return false;
        }
        catch (IOException var3_5) {
            Program.message("IOException1: " + var3_5.toString());
            var3_5.printStackTrace();
            return false;
        }
        catch (Exception var3_6) {
            Program.message("Failed! Unknown exception while accessing files: " + var3_6.toString());
            var3_6.printStackTrace(System.out);
            return false;
        }
        return bl;
    }

    private static void copyFile(File file, OutputStream outputStream) {
        byte[] arrby = new byte[10000];
        try {
            int n;
            FileInputStream fileInputStream = new FileInputStream(file);
            do {
                if ((n = fileInputStream.read(arrby)) == -1) continue;
                outputStream.write(arrby, 0, n);
            } while (n != -1);
            fileInputStream.close();
        }
        catch (IOException var3_4) {
            Program.message("Error copying file: " + var3_4.toString());
        }
    }

    public static Vector scanFiles(JPlagOptions jPlagOptions) throws InterruptedException {
        File file;
        Vector<Submission> vector = new Vector<Submission>();
        String string = jPlagOptions.getSubDir();
        if (string != null && string.equals("")) {
            string = null;
        }
        if (!((file = new File(jPlagOptions.getOriginalDir())) != null && file.isDirectory())) {
            Program.message(jPlagOptions.getOriginalDir() + " is not a directory!");
            return null;
        }
        String[] arrstring = null;
        try {
            arrstring = file.list();
        }
        catch (SecurityException var5_5) {
            Program.message("Unable to retrieve directory: " + jPlagOptions.getOriginalDir());
            return null;
        }
        String[] arrstring2 = jPlagOptions.getSuffixes();
        boolean bl = jPlagOptions.getRecurse();
        for (int i = 0; i < arrstring.length; ++i) {
            File file2;
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            File file3 = new File(file, arrstring[i]);
            if (file3 == null) continue;
            if (!file3.isDirectory()) {
                boolean bl2 = false;
                String string2 = file3.getName();
                for (int j = 0; j < arrstring2.length; ++j) {
                    if (!string2.endsWith(arrstring2[j])) continue;
                    bl2 = true;
                    break;
                }
                if (!bl2) continue;
                vector.addElement(new Submission(string2, file));
                continue;
            }
            File file4 = file2 = string == null ? file3 : new File(file3, string);
            if (file2 != null && file2.isDirectory()) {
                vector.addElement(new Submission(file3.getName(), file2, bl, arrstring2));
                continue;
            }
            if (string != null) continue;
            Program.message("Cannot find directory: " + string);
            return null;
        }
        return vector;
    }

    public static String usage() {
        String string = "JPlag-WebStart (Version JAM*Tester), Copyright (c)2001 Guido Malpohl (malpohl@gmx.de)\nUsage: Submit [[ options ] <password> <root-dir>]\n(If no command line arguments are given, the GUI will be launched.)\n\n <password>    The password for account: \n <root-dir>    The root-directory that contains all submissions\n\noptions are:\n -l <language> (Language) Supported Languages:\n                 ";
        string = string + " -S <dir>      Look in directories <root-dir>" + File.separator + "*" + File.separator + "<dir> for programs.\n" + "               (default: <root-dir>" + File.separator + "*)\n" + " -s            (Subdirs) Look at files in subdirs too\n" + " -p <suffixes> <suffixes> is a comma-separated list of all filename " + "suffixes\n" + "               that are included. (\"-p ?\" for defaults)\n" + " -t <n>        (Token) Tune the sensitivity of the comparison. " + "A smaller\n               <n> increases the sensitivity.\n" + " -m <n>        (Matches) Number of matches that will be saved " + "(default:20)\n" + " -m <p>%       All matches with more than <p>% similarity will be " + "saved.\n" + " -r <dir>      (Result) Name of directory in which the web pages " + "will be\n" + "               stored (default: result)\n" + " -h            This page\n" + " -v            (Verbose) Show server-log before dowload\n";
        return string;
    }

    private static void addParameter(String string) {
    }

    private static int parseOption(JPlagOptions jPlagOptions, String[] arrstring, int n) {
        if (arrstring[n].equals("-S")) {
            if (n + 1 == arrstring.length) {
                Program.optionMissing("ERROR: Option '-S' needs the name of the sub-directory!");
            }
            jPlagOptions.setSubDir(arrstring[n + 1]);
            Program.addParameter(arrstring[n]);
            Program.addParameter(arrstring[n + 1]);
            ++n;
        } else if (arrstring[n].equals("-h")) {
            System.out.print(Program.usage());
            System.exit(0);
        } else if (arrstring[n].equals("-v")) {
            verbose = true;
        } else if (arrstring[n].equals("-s")) {
            jPlagOptions.setRecurse(true);
            Program.addParameter(arrstring[n]);
        } else if (arrstring[n].equals("-p")) {
            if (n + 1 == arrstring.length) {
                Program.optionMissing("ERROR: Option '-p' needs a list of suffixes!");
            }
            if (jPlagOptions.setSuffixes(arrstring[n + 1]) != null) {
                System.exit(-1);
            }
            Program.addParameter(arrstring[n]);
            Program.addParameter(arrstring[n + 1]);
            ++n;
        } else if (arrstring[n].equals("-t")) {
            if (n + 1 == arrstring.length) {
                Program.optionMissing("ERROR: Option '-t' needs the number of tokens!");
            }
            if (jPlagOptions.setMML(arrstring[n + 1]) == null) {
                Program.addParameter(arrstring[n]);
                Program.addParameter(arrstring[n + 1]);
            }
            ++n;
        } else if (arrstring[n].equals("-m")) {
            if (n + 1 == arrstring.length) {
                Program.optionMissing("ERROR: Option '-m' needs the number of matches!");
            }
            Program.addParameter(arrstring[n]);
            Program.addParameter(arrstring[n + 1]);
            ++n;
        } else if (arrstring[n].equals("-r")) {
            if (n + 1 == arrstring.length) {
                Program.optionMissing("ERROR: Option '-r' needs the directory for the results!");
            }
        } else if (arrstring[n].equals("-l")) {
            if (n + 1 == arrstring.length) {
                Program.optionMissing("ERROR: Option '-l' needs the name of the language!");
            }
            Program.addParameter(arrstring[n]);
            Program.addParameter(arrstring[n + 1]);
            ++n;
        }
        return n;
    }

    private static void optionMissing(String string) {
        System.out.println(string);
        System.exit(-1);
    }

    private static void commandLine(String[] arrstring) {
        TextProgressBar textProgressBar;
        JPlagOptions jPlagOptions = new JPlagOptions();
        for (int i = 0; i < arrstring.length; ++i) {
            if (arrstring[i].startsWith("-")) {
                i = Program.parseOption(jPlagOptions, arrstring, i);
                continue;
            }
            if (jPlagOptions.getPassword() == null) {
                jPlagOptions.setPassword(arrstring[i]);
                continue;
            }
            jPlagOptions.setOriginalDir(arrstring[i]);
        }
        if (jPlagOptions.getPassword() == null || jPlagOptions.getOriginalDir() == null) {
            System.out.println("Error: Password and/or root-directory are missing!");
            System.exit(-1);
        }
        if (Program.send(jPlagOptions, textProgressBar = new TextProgressBar())) {
            System.out.println("Submission successful!");
        } else {
            System.exit(-1);
        }
        String string = JPlagServer.waitForResults(jPlagOptions.getUser());
        if (verbose) {
            JPlagServer.programLog(jPlagOptions.getUser());
        }
        JPlagServer.removeResult(jPlagOptions.getUser());
    }

    public static void main(String[] arrstring) {
        if (arrstring.length < 1) {
            Program.showGUI();
        }
    }

    public static JPlagGUI showGUI() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        submit = new JPlagGUI("JPlag-WebStart (Version JAM*Tester)");
        submit.setResizable(false);
        submit.pack();
        Dimension dimension2 = submit.getSize();
        submit.setLocation((dimension.width - dimension2.width) / 2, (dimension.height - dimension2.height) / 2);
        submit.setDefaultCloseOperation(1);
        submit.show();
        return submit;
    }
}

