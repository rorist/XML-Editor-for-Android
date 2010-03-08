package info.lamatricexiste.xmlviewer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btn_open = (Button) findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFile("/sdcard/discovery/google-192.168.144.0.xml");
            }
        });
        // openFile("/sdcard/test.html");
        openFile("/sdcard/nmap.xml");
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
}