package org.ls.lightswitch;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Button;

import android.net.ConnectivityManager;
import android.content.Context;
import android.net.NetworkInfo;
import android.widget.EditText;
import android.widget.TextView;

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

        final TextView connectionStatus = (TextView) findViewById(R.id.connectionStatus);

        Handler handler = new Handler();
        AppClient client = new AppClient("192.168.0.255", 20001, connectionStatus, handler);
        client.execute();

        final Button button = (Button) findViewById(R.id.switchButton);
        button.setOnClickListener(new SwitchButton(button, client));

        final EditText ipAddress = (EditText) findViewById(R.id.ipText);
        ipAddress.setOnEditorActionListener(new IpAddressListener(client));

        final EditText portNumber = (EditText) findViewById(R.id.portText);
        portNumber.setOnEditorActionListener(new PortNumberListener(client));

    }
}
