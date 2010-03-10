/**
 * Links:
 * ------
 * http://www.saxproject.org/
 * http://www.anddev.org/parsing_xml_from_the_net_-_using_the_saxparser-t353.html 
 * http://www.saxproject.org/?selected=get-set
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
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class XmlEditor extends Activity {

    private final String TAG = "XmlEditor";
    private Node rootNode = null;
    private Node currentNode = null;
    private LayoutInflater mInflater;
    private File file = null;
    private LinearLayout mainView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.xmleditor);
        mInflater = LayoutInflater.from(getApplicationContext());
        mainView = (LinearLayout) findViewById(R.id.main_view);

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

        setButtons();
    }

    private Node parseXml() throws SAXException, ParserConfigurationException, IOException,
            ParseException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        XmlHandler handler = new XmlHandler();
        FileReader r = new FileReader(file);

        // Set XML Handler
        xr.setContentHandler(handler);
        xr.setErrorHandler(handler);
        try {
            xr.setFeature("http://xml.org/sax/features/validation", true);
        } catch (SAXNotSupportedException e) {
            Log.i(TAG, "Validation schema not found!");
        }
        xr.parse(new InputSource(r));
        // Log.i(TAG, "rootNode="+rootNode.childs.size());
        return handler.getRootNode();
    }
    
    private void setButtons(){
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
        Button btn_prev = (Button) findViewById(R.id.btn_prev);
        btn_prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (currentNode.parentNode != null && currentNode.position > 0) {
                    displayNode(currentNode.parentNode.childs.get(currentNode.position - 1));
                }
            }
        });
        Button btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (currentNode.parentNode != null
                        && currentNode.position < currentNode.parentNode.childs.size() - 1) {
                    displayNode(currentNode.parentNode.childs.get(currentNode.position + 1));
                }
            }
        });
    }

    private TextView createTitle(String value) {
        TextView title = (TextView) mInflater.inflate(R.layout.list_title, null);
        title.setText(value);
        return title;
    }

    private TableLayout createContainer() {
        TableLayout container = (TableLayout) mInflater.inflate(R.layout.list_table, null);
        return container;
    }

    private void displayNode(final Node node) {
        currentNode = node;

        // Reset views
        mainView.removeAllViews();

        // Node information
        mainView.addView(createTitle("Node:"));
        TableLayout layout_info = createContainer();
        layout_info.addView(createInfoTextView("Name (" + node.position + ")", node.name));
        if (node.content != null) {
            layout_info.addView(createInfoTextView("Content", node.content));
        }
        mainView.addView(layout_info);

        // Attributes
        if (node.attrs.size() > 0) {
            mainView.addView(createTitle("Attributes:"));
            TableLayout layout_attrs = createContainer();
            for (Map.Entry<String, String> entry : node.attrs.entrySet()) {
                layout_attrs.addView(createInfoTextView(entry.getKey(), entry.getValue()));
            }
            mainView.addView(layout_attrs);
        }

        // Childs list
        if (node.childs.size() > 0) {
            mainView.addView(createTitle("Childs:"));
            TableLayout layout_childs = createContainer();
            int size = node.childs.size();
            for (int i = 0; i < size; i++) {
                layout_childs.addView(createNodeTextView(node.childs.get(i).name, i));
            }
            mainView.addView(layout_childs);
        }
    }

    private LinearLayout createInfoTextView(String label_str, String value_str) {
        LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.list_info, null);
        ((TextView) layout.findViewById(R.id.label)).setText(label_str);
        ((TextView) layout.findViewById(R.id.value)).setText(value_str);
        return layout;
    }

    private LinearLayout createNodeTextView(String str, final int position) {
        LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.list_nodes, null);
        ((TextView) layout.findViewById(R.id.name)).setText(str);
        layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                displayNode(currentNode.childs.get(position));
            }
        });
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
}
