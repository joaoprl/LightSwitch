package org.ls.lightswitch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Button;

import android.net.ConnectivityManager;
import android.content.Context;
import android.net.NetworkInfo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
        } else {
            return;
        }

        AppClient client = new AppClient("192.168.1.63", 20000);
        client.execute();

        final Button button = (Button) findViewById(R.id.switchButton);
        button.setOnClickListener(new SwitchButton(button, client));
    }
}
