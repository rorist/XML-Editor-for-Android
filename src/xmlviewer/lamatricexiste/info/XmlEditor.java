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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class XmlEditor extends Activity {

    private final String TAG = "XmlEditor";
    private Node rootNode = null;
    private LayoutInflater mInflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = LayoutInflater.from(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.xmleditor);
        editRootNode();
    }

    private void displayNode(Node node) {
        // Node information
        LinearLayout layout = (LinearLayout) findViewById(R.id.node_info);
        layout.addView(createInfoTextView("Name", node.name));
        if (node.content != null) {
            layout.addView(createInfoTextView("Content", node.content));
        }

        // Childs list
        ArrayAdapter<String> childs = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.list_nodes, R.id.list);
        ListView list_childs = (ListView) findViewById(R.id.list_childs);
        list_childs.setAdapter(childs);
        for (int i = 0; i < node.childs.size(); i++) {
            childs.add(node.childs.get(i).name);
        }
    }

    private LinearLayout createInfoTextView(String label_str, String value_str) {
        LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.list_info, null);
        TextView label = (TextView) layout.findViewById(R.id.label);
        TextView value = (TextView) layout.findViewById(R.id.value);
        label.setText(label_str+":");
        value.setText(value_str);
        return layout;
    }

    private void editRootNode() {
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
                displayNode(rootNode);
            } else {
                Log.e(TAG, "rootNode does not exist!");
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
