package xmlviewer.lamatricexiste.info;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NodeViewer extends Activity {

    private Node parentNode = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nodeviewer);

        // Get intent data
        Bundle b = getIntent().getExtras();
        NodesList nodes = b.getParcelable("nodes");

        // Get/Set parentNode info
        if (nodes.size() > 0) {
            parentNode = nodes.get(0);
            nodes.remove(0);
        }
        TextView name = (TextView) findViewById(R.id.node_name);
        TextView content = (TextView) findViewById(R.id.node_content);
        name.setText(parentNode.name);
        content.setText(parentNode.content);

        // Childs list
        ArrayAdapter<String> childs = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.list_nodes, R.id.list);
        ListView list_childs = (ListView) findViewById(R.id.list_childs);
        list_childs.setAdapter(childs);
        for (int i = 0; i < nodes.size(); i++) {
            childs.add(nodes.get(i).name);
        }
    }

}
