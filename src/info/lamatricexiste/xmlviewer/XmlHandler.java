package info.lamatricexiste.xmlviewer;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;

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
            currentNode.name = name;
        } else {
            if (currentNode != null) {
                parentNode = currentNode;
            }
            currentNode = new Node();
            currentNode.uri = uri;
            currentNode.name = name;
            currentNode.parentNode = parentNode;
            // rootNode.childs.add(currentNode); // Add all nodes to rootNode
        }

        // Add to hierarchy
        if (parentNode != null) {
            parentNode.childs.add(currentNode);
            currentNode.position = parentNode.childs.indexOf(currentNode);
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
        // logi("CHAR=" + new String(ch));
        // FIXME: Try with new String(ch,start,length);
        String chars = "";
        int size = start + length;
        for (int i = start; i < size; i++) {
            switch (ch[i]) {
                case '\\':
                    chars += "\\\\";
                    break;
                case '"':
                    chars += "\\\"";
                    break;
                case '\n':
                case '\r':
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

    @Override
    public void error(SAXParseException e) throws SAXException {
        Log.e(TAG, e.getMessage());
        super.error(e);
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        Log.e(TAG, e.getMessage());
        super.fatalError(e);
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        Log.e(TAG, e.getMessage());
        super.warning(e);
    }
}
