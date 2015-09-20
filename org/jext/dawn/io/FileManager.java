/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.DawnUtilities;
import org.jext.dawn.Function;

public class FileManager {
    public static final String NEW_LINE = System.getProperty("line.separator");

    public static void openFileForInput(String string, String string2, Function function, DawnParser dawnParser) throws DawnRuntimeException {
        if (!FileManager.isFileAvailable(string, dawnParser)) {
            try {
                dawnParser.setProperty("DAWN.IO#FILE." + string, new BufferedReader(new FileReader(DawnUtilities.constructPath(string2))));
            }
            catch (IOException var4_4) {
                throw new DawnRuntimeException(function, dawnParser, "file " + string2 + " not found");
            }
        } else {
            throw new DawnRuntimeException(function, dawnParser, "file ID " + string + " already exists");
        }
    }

    public static void openFileForOutput(String string, String string2, Function function, DawnParser dawnParser) throws DawnRuntimeException {
        if (!FileManager.isFileAvailable(string, dawnParser)) {
            try {
                dawnParser.setProperty("DAWN.IO#FILE." + string, new BufferedWriter(new FileWriter(DawnUtilities.constructPath(string2))));
            }
            catch (IOException var4_4) {
                throw new DawnRuntimeException(function, dawnParser, "file " + string2 + " not found");
            }
        } else {
            throw new DawnRuntimeException(function, dawnParser, "file ID " + string + " already exists");
        }
    }

    public static String readLine(String string, Function function, DawnParser dawnParser) throws DawnRuntimeException {
        return FileManager.read(true, string, function, dawnParser);
    }

    public static String read(String string, Function function, DawnParser dawnParser) throws DawnRuntimeException {
        return FileManager.read(false, string, function, dawnParser);
    }

    public static String read(boolean bl, String string, Function function, DawnParser dawnParser) throws DawnRuntimeException {
        block7 : {
            Object object = dawnParser.getProperty("DAWN.IO#FILE." + string);
            if (!(object instanceof BufferedReader)) {
                throw new DawnRuntimeException(function, dawnParser, "attempted to read from an output file");
            }
            BufferedReader bufferedReader = (BufferedReader)object;
            if (bufferedReader != null) {
                try {
                    if (bl) {
                        String string2 = bufferedReader.readLine();
                        if (string2 == null) {
                            FileManager.closeFile(string, function, dawnParser);
                            break block7;
                        }
                        return string2;
                    }
                    char c = (char)bufferedReader.read();
                    if (c == '\u0000') {
                        FileManager.closeFile(string, function, dawnParser);
                        break block7;
                    }
                    return "" + c;
                }
                catch (IOException var6_8) {
                    throw new DawnRuntimeException(function, dawnParser, "file ID " + string + " cannot be read properly");
                }
            }
            throw new DawnRuntimeException(function, dawnParser, "file ID " + string + " points to a non-opened file");
        }
        return null;
    }

    public static void writeLine(String string, String string2, Function function, DawnParser dawnParser) throws DawnRuntimeException {
        FileManager.write(true, string, string2, function, dawnParser);
    }

    public static void write(String string, String string2, Function function, DawnParser dawnParser) throws DawnRuntimeException {
        FileManager.write(false, string, string2, function, dawnParser);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void write(boolean bl, String string, String string2, Function function, DawnParser dawnParser) throws DawnRuntimeException {
        if (string2 == null) {
            throw new DawnRuntimeException(function, dawnParser, "attempted to write a null string");
        }
        Object object = dawnParser.getProperty("DAWN.IO#FILE." + string);
        if (!(object instanceof BufferedWriter)) {
            throw new DawnRuntimeException(function, dawnParser, "attempted to write into an input file");
        }
        BufferedWriter bufferedWriter = (BufferedWriter)object;
        if (bufferedWriter == null) throw new DawnRuntimeException(function, dawnParser, "file ID " + string + " points to a non-opened file");
        try {
            bufferedWriter.write(string2, 0, string2.length());
            if (!bl) return;
            bufferedWriter.write(NEW_LINE);
            return;
        }
        catch (IOException var7_7) {
            throw new DawnRuntimeException(function, dawnParser, "file ID " + string + " cannot be written properly");
        }
    }

    public static void closeFile(String string, Function function, DawnParser dawnParser) throws DawnRuntimeException {
        Object object = dawnParser.getProperty("DAWN.IO#FILE." + string);
        if (object == null) {
            return;
        }
        if (!(object instanceof Reader || object instanceof Writer)) {
            throw new DawnRuntimeException(function, dawnParser, "error, given ID " + string + " does not point to a file");
        }
        try {
            if (object instanceof Reader) {
                ((BufferedReader)object).close();
            } else {
                BufferedWriter bufferedWriter = (BufferedWriter)object;
                bufferedWriter.flush();
                bufferedWriter.close();
            }
            dawnParser.unsetProperty("DAWN.IO#FILE." + string);
        }
        catch (IOException var4_5) {
            throw new DawnRuntimeException(function, dawnParser, "cannot close file ID " + string);
        }
    }

    public static boolean isFileAvailable(String string, DawnParser dawnParser) {
        return dawnParser.getProperty("DAWN.IO#FILE." + string) != null;
    }
}

