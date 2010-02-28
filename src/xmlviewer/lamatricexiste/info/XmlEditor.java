/**
 * Links:
 * ------
 * http://www.saxproject.org/
 * http://www.anddev.org/parsing_xml_from_the_net_-_using_the_saxparser-t353.html 
 */

package xmlviewer.lamatricexiste.info;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class XmlEditor extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xmleditor);

        try {
            parseXml(new File("/sdcard/discovery/google-192.168.144.0.xml"));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseXml(File file) throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        XmlHandler handler = new XmlHandler();
        FileReader r = new FileReader(file);

        xr.setContentHandler(handler);
        xr.setErrorHandler(handler);
        xr.parse(new InputSource(r));
    }

    private class XmlHandler extends DefaultHandler {

        private final String TAG = "XmlHandler";

        public XmlHandler() {
            super();
        }

        @Override
        public void startDocument() {
            Log.i(TAG, "Start parsing of the file!");
        }

        @Override
        public void endDocument() {
            Log.i(TAG, "Parsing done!");
        }

        @Override
        public void startElement(String uri, String name, String qName, Attributes atts) {
            Log.i(TAG, "START=" + name);

            // Attributes
            for (int i = 0; i < atts.getLength(); i++) {
                Log.i(TAG, atts.getLocalName(i) + "=" + atts.getValue(i));
            }
        }

        @Override
        public void endElement(String uri, String name, String qName) {
            Log.i(TAG, "END=" + name);
        }

        @Override
        public void characters(char ch[], int start, int length) {
            // Log.i(TAG, "CHAR=" + new String(ch));
            String chars = "";
            for (int i = start; i < start + length; i++) {
                switch (ch[i]) {
                    case '\\':
                        // chars += "\\\\";
                        break;
                    case '"':
                        // chars += "\\\"";
                        break;
                    case '\n':
                        // chars += "\\n";
                        break;
                    case '\r':
                        // chars += "\\r";
                        break;
                    case '\t':
                        // chars += "\\t";
                        break;
                    default:
                        chars += "" + ch[i];
                        break;
                }
            }
            if (chars != "") {
                Log.i(TAG, "CHAR=" + chars);
            }

        }
    }

}
