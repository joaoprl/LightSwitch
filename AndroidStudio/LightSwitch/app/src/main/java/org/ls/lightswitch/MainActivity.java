package org.ls.lightswitch;

import android.app.AlarmManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
    Button button;
    EditText ipAddressText, portNumberText;
    TextView connectionStatusText;

    AppClient appClient;
    SharedPreferences.Editor editor;

    Handler handler;

    public void switchButton(){
        byte[] b = new byte[1];
        b[0] = 1;
        this.appClient.setData(b);
    }

    public void setPortNumber(Integer portNumber){
        this.appClient.setPortNumber(portNumber);
        this.editor.putInt("portNumber", portNumber);
        this.editor.apply();
    }

    public void setIpAddress(String ipAddress){
        this.appClient.setIpAddress(ipAddress);
        this.editor.putString("ipAddress", ipAddress);
        this.editor.apply();
    }

    private boolean postConnectionStatus(final Boolean connected){

        return this.handler.post(new Runnable() {
            public void run() {
                if(connected) {
                    connectionStatusText.setText("Connected");
                    ipAddressText.setFocusable(false);
                    ipAddressText.setFocusableInTouchMode(false);
                    ipAddressText.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

                    portNumberText.setFocusable(false);
                    portNumberText.setFocusableInTouchMode(false);
                    portNumberText.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                }
                else {
                    connectionStatusText.setText("Disconnected");
                    ipAddressText.setFocusable(true);
                    ipAddressText.setFocusableInTouchMode(true);
                    ipAddressText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

                    portNumberText.setFocusable(true);
                    portNumberText.setFocusableInTouchMode(true);
                    portNumberText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                }
            }
        });
    }

    public void setConnected(Boolean connected){
        this.postConnectionStatus(connected);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected())
            return; // network is not available

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String serverAddress = sharedPreferences.getString("ipAddress", null);
        Integer portNumber = sharedPreferences.getInt("portNumber", 0);
        this.editor = sharedPreferences.edit();

        this.connectionStatusText = (TextView) findViewById(R.id.connectionStatus);

        this.handler = new Handler();
        this.appClient = new AppClient(serverAddress, portNumber, this);
        this.appClient.execute();

        this.button = (Button) findViewById(R.id.switchButton);
        this.button.setOnClickListener(new SwitchButton(this));

        this.ipAddressText = (EditText) findViewById(R.id.ipText);
        this.ipAddressText.setOnEditorActionListener(new IpAddressListener(this));
        this.ipAddressText.setText(serverAddress);

        this.portNumberText = (EditText) findViewById(R.id.portText);
        this.portNumberText.setOnEditorActionListener(new PortNumberListener(this));
        this.portNumberText.setText(portNumber.toString());

        AlarmManager mgr=(AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        if(mgr.getNextAlarmClock() != null)
            System.out.println(mgr.getNextAlarmClock().getTriggerTime() - System.currentTimeMillis());
    }
}
