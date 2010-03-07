package info.lamatricexiste.xmlviewer;

import android.app.Activity;
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

        Button btn_0 = (Button) findViewById(R.id.btn_0);
        btn_0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFile("/sdcard/discovery/google-192.168.144.0.xml");
            }
        });
        openFile("/sdcard/test.html");
    }

    private void openFile(String filename) {
        Uri uri = Uri.parse(filename);
        // ContentResolver cr = getContentResolver();
        // MimeTypeMap mime = MimeTypeMap.getSingleton();
        // Log.v("XMLEDITOR", "MIME=" + cr.getType(uri));

        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "text/xml");
        startActivity(intent);
    }
}