package xmlviewer.lamatricexiste.info;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class XmlHandler extends DefaultHandler {
    private final String TAG = "XmlHandler";
    private final boolean LOG_INFO = false;
    private Node rootNode = null;
    private Node currentNode = null;

    public XmlHandler() {
        super();
    }

    public Node getRootNode() {
        return rootNode;
    }

    private void logi(String str) {
        if (LOG_INFO) {
            Log.i(TAG, str);
        }
    }

    @Override
    public void startDocument() {
        logi("Start parsing of the file!");
    }

    @Override
    public void endDocument() {
        logi("Parsing done!");
    }

    @Override
    public void startElement(String uri, String name, String qName, Attributes attrs) {
        logi("START=" + name);

        Node parentNode = null;
        if (rootNode == null) {
            rootNode = new Node();
            currentNode = rootNode;
        } else {
            if (currentNode != null) {
                parentNode = currentNode;
            }
            currentNode = new Node();
            currentNode.parentNode = parentNode;
        }
        currentNode.name = name;

        if (parentNode != null) {
            parentNode.childs.add(currentNode);
        }

        // Attributes
        for (int i = 0; i < attrs.getLength(); i++) {
            logi(attrs.getLocalName(i) + "=" + attrs.getValue(i));
            currentNode.attrs.put(attrs.getLocalName(i), attrs.getValue(i));
        }
    }

    @Override
    public void endElement(String uri, String name, String qName) {
        logi("END=" + name);
        if (currentNode != null) {
            currentNode = currentNode.parentNode;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) {
        logi("CHAR=" + new String(ch));
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
            logi("CHAR=" + chars);
            currentNode.content = chars;
        }

    }
}
