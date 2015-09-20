/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  com.microstar.xml.XmlException
 *  com.microstar.xml.XmlHandler
 *  com.microstar.xml.XmlParser
 */
package org.jext.xml;

import com.microstar.xml.XmlException;
import com.microstar.xml.XmlHandler;
import com.microstar.xml.XmlParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URL;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.xml.XMenuHandler;

public class XMenuReader
extends Thread {
    public static void read(JextFrame jextFrame, InputStream inputStream, String string) {
        InputStreamReader inputStreamReader = new InputStreamReader(Jext.getLanguageStream(inputStream, string));
        XMenuHandler xMenuHandler = new XMenuHandler(jextFrame);
        XmlParser xmlParser = new XmlParser();
        xmlParser.setHandler((XmlHandler)xMenuHandler);
        try {
            Class class_ = Jext.class;
            xmlParser.parse(class_.getResource("xmenubar.dtd").toString(), null, (Reader)inputStreamReader);
        }
        catch (XmlException var6_6) {
            System.err.println("XMenu: Error parsing grammar " + inputStream);
            System.err.println("XMenu: Error occured at line " + var6_6.getLine() + ", column " + var6_6.getColumn());
            System.err.println("XMenu: " + var6_6.getMessage());
        }
        catch (Exception var6_7) {
            var6_7.printStackTrace();
        }
        try {
            inputStream.close();
            inputStreamReader.close();
        }
        catch (IOException var6_8) {
            // empty catch block
        }
    }
}

