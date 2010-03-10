package info.lamatricexiste.xmlviewer;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {

    public int position;
    public String schema;
    public String uri;
    public String name;
    public String content = null;
    public Node parentNode = null;
    public HashMap<String, String> attrs = new HashMap<String, String>();
    public ArrayList<Node> childs = new ArrayList<Node>();

}
