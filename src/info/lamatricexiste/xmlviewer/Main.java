package info.lamatricexiste.xmlviewer;

import android.util.Log;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.ActivityNotFoundException;
import android.widget.Toast;

public class Main extends Activity {

    public static final String ACTION_PICK_FILE = "org.openintents.action.PICK_FILE";
    public static final String EXTRA_TITLE = "org.openintents.extra.TITLE";
    public static final String EXTRA_BUTTON_TEXT = "org.openintents.extra.BUTTON_TEXT";
    private final int RESULT_PICK_FILE = 1;
    private final String TAG = "XmlViewer-Main";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Open
        Button btn_open = (Button) findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //openFile("/sdcard/discovery/google-192.168.144.0.xml");
                openFile("/sdcard/nmap.xml");
            }
        });

        // Browse
        Button btn_browse = (Button) findViewById(R.id.btn_browse);
        btn_browse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                browseFile();
            }
        });

        // New

        // Quit
        Button btn_quit = (Button) findViewById(R.id.btn_quit);
        btn_quit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
        case RESULT_PICK_FILE:
            if(resultCode == RESULT_OK && data != null) {
                String filename = data.getDataString();
                if(filename != null) {
                    if(filename.startsWith("file://")){
                        filename = filename.substring(7);
                    }
                    openFile(filename);
                }
            }
            break;
        }
    }

    private void openFile(String filename) {
        Uri uri = Uri.parse(filename);

        // Try to define Mime Type
        // MimeTypeMap mime = MimeTypeMap.getSingleton();
        ContentResolver cr = getContentResolver();
        String type = cr.getType(uri);
        if (type == null) {
            type = "text/plain";
        }

        // Create intent and start Activity
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(uri, type);
        startActivity(intent);
    }

    private void browseFile(){
        Intent intent = null;
        try {
            intent = new Intent(Main.ACTION_PICK_FILE);
            //intent.setData(Uri.parse("/sdcard"));
            intent.putExtra(Main.EXTRA_TITLE, "Select file to open");
            intent.putExtra(Main.EXTRA_BUTTON_TEXT, "Open");
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException e){
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, "Please install OI File Manager.", Toast.LENGTH_LONG).show();
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://search?q=pname:org.openintents.filemanager"));
            startActivity(intent);
        }
    }
}
