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
import org.jext.xinsert.XTree;
import org.jext.xml.XInsertHandler;

public class XInsertReader {
    public static boolean read(XTree xTree, InputStream inputStream, String string) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        XInsertHandler xInsertHandler = new XInsertHandler(xTree);
        XmlParser xmlParser = new XmlParser();
        xmlParser.setHandler((XmlHandler)xInsertHandler);
        try {
            Class class_ = Jext.class;
            xmlParser.parse(class_.getResource("xinsert.dtd").toString(), null, (Reader)inputStreamReader);
        }
        catch (XmlException var6_6) {
            System.err.println("XInsert: Error parsing grammar " + inputStream);
            System.err.println("XInsert: Error occured at line " + var6_6.getLine() + ", column " + var6_6.getColumn());
            System.err.println("XInsert: " + var6_6.getMessage());
            return false;
        }
        catch (Exception var6_7) {
            var6_7.printStackTrace();
            return false;
        }
        try {
            inputStream.close();
            inputStreamReader.close();
        }
        catch (IOException var6_8) {
            // empty catch block
        }
        return true;
    }
}

