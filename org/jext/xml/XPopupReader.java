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
import javax.swing.JPopupMenu;
import org.jext.Jext;
import org.jext.xml.XPopupHandler;

public class XPopupReader {
    public static JPopupMenu read(InputStream inputStream, String string) {
        InputStreamReader inputStreamReader = new InputStreamReader(Jext.getLanguageStream(inputStream, string));
        XPopupHandler xPopupHandler = new XPopupHandler();
        XmlParser xmlParser = new XmlParser();
        xmlParser.setHandler((XmlHandler)xPopupHandler);
        try {
            Class class_ = Jext.class;
            xmlParser.parse(class_.getResource("xpopup.dtd").toString(), null, (Reader)inputStreamReader);
        }
        catch (XmlException var5_5) {
            System.err.println("XPopup: Error parsing grammar " + inputStream);
            System.err.println("XPopup: Error occured at line " + var5_5.getLine() + ", column " + var5_5.getColumn());
            System.err.println("XPopup: " + var5_5.getMessage());
            return null;
        }
        catch (Exception var5_6) {
            var5_6.printStackTrace();
            return null;
        }
        try {
            inputStream.close();
            inputStreamReader.close();
        }
        catch (IOException var5_7) {
            // empty catch block
        }
        return xPopupHandler.getPopupMenu();
    }
}

