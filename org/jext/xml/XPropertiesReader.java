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
import org.jext.xml.XPropertiesHandler;

public class XPropertiesReader {
    public static boolean read(InputStream inputStream, String string) {
        return XPropertiesReader.read(inputStream, string, true);
    }

    public static boolean read(InputStream inputStream, String string, boolean bl) {
        InputStream inputStream2;
        InputStreamReader inputStreamReader;
        if (bl) {
            inputStream2 = Jext.getLanguageStream(inputStream, string);
            if (inputStream2 == null) {
                return false;
            }
        } else {
            inputStream2 = inputStream;
        }
        if ((inputStreamReader = new InputStreamReader(inputStream2)) == null) {
            return false;
        }
        XmlParser xmlParser = new XmlParser();
        xmlParser.setHandler((XmlHandler)new XPropertiesHandler());
        try {
            Class class_ = Jext.class;
            xmlParser.parse(class_.getResource("xproperties.dtd").toString(), null, (Reader)inputStreamReader);
        }
        catch (XmlException var6_6) {
            System.err.println("XProperties: Error parsing grammar " + string);
            System.err.println("XProperties: Error occured at line " + var6_6.getLine() + ", column " + var6_6.getColumn());
            System.err.println("XProperties: " + var6_6.getMessage());
            return false;
        }
        catch (Exception var6_7) {
            return false;
        }
        try {
            inputStream.close();
            inputStream2.close();
            inputStreamReader.close();
        }
        catch (IOException var6_8) {
            // empty catch block
        }
        return true;
    }
}

