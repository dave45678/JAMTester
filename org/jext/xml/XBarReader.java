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
import org.jext.xml.XBarHandler;

public class XBarReader {
    public static void read(JextFrame jextFrame, InputStream inputStream, String string) {
        InputStreamReader inputStreamReader = new InputStreamReader(Jext.getLanguageStream(inputStream, string));
        XBarHandler xBarHandler = new XBarHandler(jextFrame);
        XmlParser xmlParser = new XmlParser();
        xmlParser.setHandler((XmlHandler)xBarHandler);
        try {
            Class class_ = Jext.class;
            xmlParser.parse(class_.getResource("xtoolbar.dtd").toString(), null, (Reader)inputStreamReader);
        }
        catch (XmlException var6_6) {
            System.err.println("XBar: Error parsing grammar " + inputStream);
            System.err.println("XBar: Error occured at line " + var6_6.getLine() + ", column " + var6_6.getColumn());
            System.err.println("XBar: " + var6_6.getMessage());
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

