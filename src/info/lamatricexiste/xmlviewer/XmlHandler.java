package info.lamatricexiste.xmlviewer;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class XmlHandler extends DefaultHandler {
    private final String TAG = "XmlHandler";
    private final boolean LOG_INFO = true;
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
            currentNode.name = name;
        } else {
            if (currentNode != null) {
                parentNode = currentNode;
            }
            currentNode = new Node();
            currentNode.name = name;
            currentNode.parentNode = parentNode;
            // rootNode.childs.add(currentNode); // Add all nodes to rootNode
        }

        // Add to hierarchy
        if (parentNode != null) {
            parentNode.childs.add(currentNode);
        }

        // Attributes
        int size = attrs.getLength();
        for (int i = 0; i < size; i++) {
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
        int size = start + length;
        for (int i = start; i < size; i++) {
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
