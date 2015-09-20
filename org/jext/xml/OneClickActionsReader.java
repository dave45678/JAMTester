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
import org.jext.xml.OneClickActionsHandler;

public class OneClickActionsReader {
    public static boolean read(InputStream inputStream, String string) {
        InputStream inputStream2 = Jext.getLanguageStream(inputStream, string);
        if (inputStream2 == null) {
            return false;
        }
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream2);
        if (inputStreamReader == null) {
            return false;
        }
        XmlParser xmlParser = new XmlParser();
        xmlParser.setHandler((XmlHandler)new OneClickActionsHandler());
        try {
            Class class_ = Jext.class;
            xmlParser.parse(class_.getResource("oneclickactions.dtd").toString(), null, (Reader)inputStreamReader);
        }
        catch (XmlException var5_5) {
            System.err.println("One Click! actions: Error parsing grammar " + inputStream);
            System.err.println("One Click! actions: Error occured at line " + var5_5.getLine() + ", column " + var5_5.getColumn());
            System.err.println("One Click! actions: " + var5_5.getMessage());
            return false;
        }
        catch (Exception var5_6) {
            var5_6.printStackTrace();
            return false;
        }
        try {
            inputStream.close();
            inputStreamReader.close();
        }
        catch (IOException var5_7) {
            // empty catch block
        }
        return true;
    }
}

