/**
 * Links:
 * ------
 * http://www.saxproject.org/
 * http://www.anddev.org/parsing_xml_from_the_net_-_using_the_saxparser-t353.html 
 */

package info.lamatricexiste.xmlviewer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.ParseException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import org.xmlpull.v1.XmlPullParser;
import android.util.AttributeSet;
import android.util.Xml;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class XmlEditor extends Activity {

    private final String TAG = "XmlEditor";
    private Node rootNode = null;
    private Node currentNode = null;
    private LayoutInflater mInflater;
    private File file = null;
    private ArrayAdapter<String> childs;
    private LinearLayout mainView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = LayoutInflater.from(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.xmleditor);
        mainView = (LinearLayout) findViewById(R.id.main_view);
        childs = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_nodes, R.id.list);

        // Get Intent data
        Uri uri = getIntent().getData();
        file = new File(uri.getPath());
        if (file.exists()) {
            parseXmlAndEditRootNode();
        } else {
            Toast.makeText(getApplicationContext(),
                    "File: " + file.getName() + ": Does not exist!", Toast.LENGTH_LONG).show();
            finish();
        }

        // Set buttons action
        Button btn_root = (Button) findViewById(R.id.btn_root);
        btn_root.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                displayNode(rootNode);
            }
        });
        Button btn_up = (Button) findViewById(R.id.btn_up);
        btn_up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (currentNode.parentNode != null) {
                    displayNode(currentNode.parentNode);
                }
            }
        });
    }

    private TextView createTitle(String value){
        TextView title = new TextView(getApplicationContext(), null, R.style.Title);
        title.setText(value);
        return title;
    }

    private TableLayout createContainer(){
        XmlPullParser parser = getResources().getXml(R.style.Box);
        AttributeSet attrs = Xml.asAttributeSet(parser);
        TableLayout container = new TableLayout(getApplicationContext(), attrs);
        return container;
    }

    private void displayNode(final Node node) {
        currentNode = node;

        // Reset views
        mainView.removeAllViews();

        // Node information
        createTitle("Node:");
        TableLayout layout_info = createContainer();
        layout_info.addView(createInfoTextView("Name", node.name));
        if (node.content != null) {
            layout_info.addView(createInfoTextView("Content", node.content));
        }
        mainView.addView(layout_info);

        // Attributes
        if (node.attrs.size() > 0) {
            TableLayout layout_attrs = createContainer();
            for (Map.Entry<String, String> entry : node.attrs.entrySet()) {
                layout_attrs.addView(createInfoTextView(entry.getKey(), entry.getValue()));
            }
            mainView.addView(layout_attrs);
        }

        // Childs list
        if (node.childs.size() > 0) {
            TableLayout layout_childs = createContainer();
            /*
            list_childs.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> av, View v, int index, long arg) {
                    displayNode(node.childs.get(index));
                }
            });
            */
            int size = node.childs.size();
            for (int i = 0; i < size; i++) {
                layout_childs.addView(createInfoTextView(node.childs.get(i).name, ""));
            }
            mainView.addView(layout_childs);
        }
    }

    private LinearLayout createInfoTextView(String label_str, String value_str) {
        LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.list_info, null);
        TextView label = (TextView) layout.findViewById(R.id.label);
        TextView value = (TextView) layout.findViewById(R.id.value);
        label.setText(label_str);
        value.setText(value_str);
        return layout;
    }

    private void parseXmlAndEditRootNode() {
        try {
            rootNode = parseXml();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (rootNode != null) {
                displayNode(rootNode);
            } else {
                String msg = "Unable to parse the file!";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Log.e(TAG, msg);
                finish();
            }
        }
    }

    private Node parseXml() throws SAXException, ParserConfigurationException, IOException,
            ParseException {
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
