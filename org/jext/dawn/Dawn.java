/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.DawnUtilities;
import org.jext.dawn.Function;

public class Dawn {
    private static String line = null;
    private static DawnParser parser = null;
    private static BufferedReader in = null;
    private static final String consoleCode = "\"\" command ->\nwhile inputLine dup command -> \"exit\" same not repeat\ncommand rcl eval consoleDump \"\\n>\" print\nwend";

    public static void main(String[] arrstring) {
        if (arrstring.length == 0) {
            System.out.println("Dawn v1.1.1 final [$12:12:55 07/08/00]\nUsage: java org.jext.dawn.Dawn <scipt file>\nOptional parameter:\n\t-console (enables Dawn-written console)\n\t-nativeConsole (enables native console");
            return;
        }
        if (arrstring[0].equals("-console")) {
            DawnParser.init();
            System.out.print("Dawn console\nType some code then ENTER to execute it\nType exit to quit\n\n>");
            DawnParser.addGlobalFunction(new Function(){

                public String getName() {
                    return "consoleDump";
                }

                public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
                    System.out.print("" + '\n' + dawnParser.dump());
                }
            });
            parser = new DawnParser(new StringReader("\"\" command ->\nwhile inputLine dup command -> \"exit\" same not repeat\ncommand rcl eval consoleDump \"\\n>\" print\nwend"));
            Dawn.console();
        } else if (arrstring[0].equals("-nativeConsole")) {
            DawnParser.init();
            System.out.print("Dawn console\nType some code then ENTER to execute it\nType exit to quit\n\n>");
            in = new BufferedReader(new InputStreamReader(System.in));
            Dawn.nativeConsole();
        } else {
            try {
                in = new BufferedReader(new InputStreamReader(new FileInputStream(DawnUtilities.constructPath(arrstring[0]))));
                StringBuffer stringBuffer = new StringBuffer();
                while ((Dawn.line = in.readLine()) != null) {
                    stringBuffer.append(line).append('\n');
                }
                in.close();
                DawnParser.init();
                parser = new DawnParser(new StringReader(stringBuffer.toString()));
                parser.exec();
                System.out.print(parser.dump());
            }
            catch (Exception var1_2) {
                System.out.println(var1_2.getMessage());
            }
        }
    }

    public static void console() {
        try {
            parser.exec();
        }
        catch (DawnRuntimeException var0) {
            System.out.println(var0.getMessage());
            System.out.print("\n>");
            parser = new DawnParser(new StringReader("\"\" command ->\nwhile inputLine dup command -> \"exit\" same not repeat\ncommand rcl eval consoleDump \"\\n>\" print\nwend"));
            Dawn.console();
        }
    }

    public static void nativeConsole() {
        try {
            while (!(Dawn.line = in.readLine()).equals("exit")) {
                parser = new DawnParser(new StringReader(line));
                parser.exec();
                System.out.print(parser.dump());
                System.out.print("\n>");
            }
        }
        catch (Exception var0) {
            System.out.println(var0.getMessage());
            System.out.print("\n>");
            Dawn.nativeConsole();
        }
    }

}

