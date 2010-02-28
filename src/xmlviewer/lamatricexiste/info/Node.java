package xmlviewer.lamatricexiste.info;

import java.util.HashMap;

public class Node {

    public String schema;
    public String content;
    public Node declaration;
    public HashMap<String, String> attrs = new HashMap<String, String>();
    public HashMap<Integer, Node> childs = new HashMap<Integer, Node>();
    
}
