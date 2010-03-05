package xmlviewer.lamatricexiste.info;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {

    public String schema;
    public String name;
    public String content;
    public Node parentNode = null;
    public HashMap<String, String> attrs = new HashMap<String, String>();
    public ArrayList<Node> childs = new ArrayList<Node>();

}
