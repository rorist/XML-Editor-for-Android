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

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.os.Bundle;

public class XmlEditor extends Activity {

    private final String TAG = "XmlEditor";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xmleditor);

        Node rootNode = null;
        try {
            rootNode = parseXml(new File("/sdcard/discovery/google-192.168.144.0.xml"));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (rootNode != null) {
            }
        }
    }

    private Node parseXml(File file) throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        XmlHandler handler = new XmlHandler();
        FileReader r = new FileReader(file);
        xr.setContentHandler(handler);
        xr.setErrorHandler(handler);
        xr.parse(new InputSource(r));
        // Log.i(TAG, "rootNode="+rootNode.childs.size());
        return handler.getRootNode();
    }
}
