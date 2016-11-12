package org.ls.lightswitch;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by debios on 11/5/16.
 */

class AppClient extends AsyncTask<Void, Void, Void> {
    final int retryMili = 2000;

    Socket socket;
    String host;
    Integer port;

    Boolean hasData;
    byte[] data;

    Boolean isRunning;

    MainActivity activity;

    public AppClient(String host, int port, MainActivity activity) {
        this.host = host;
        this.port = port;

        this.socket = null;
        this.hasData = false;
        this.data = null;
        this.isRunning = true;

        this.activity = activity;
    }

    public void setData(byte[] b){
        if(this.isConnected()) {
            synchronized (this.hasData) {
                this.data = b;
                this.hasData = true;
            }
        }
    }

    public void setIpAddress(String ipAddress){
        if(!this.isConnected()){
            this.host = ipAddress;
            System.out.println("IP set to " + this.host);
        }
    }

    public void setPortNumber(int port){
        if(!this.isConnected()) {
            this.port = port;
            System.out.println("Port set to " + this.port);
        }
    }

    public boolean isConnected(){
        return this.socket != null && this.socket.isConnected() && !this.socket.isClosed();
    }

    private void writeToSocket(byte[] bArray){
        try {
            this.socket.getOutputStream().write(bArray);
        } catch (IOException e) {
            System.out.println("Failed to write");
            this.socket = null;
        }
    }

    @Override
    protected Void doInBackground(Void ... voids) {
        while(this.isRunning) {
            while(!this.isConnected()) {
                try {
                    this.socket = new Socket(host, port);
                    System.out.println("connected to " + host + " : " + port);
                    this.activity.setConnected(true);

                } catch (IOException e) {
                    this.socket = null;
                    System.out.println("failed to connect");
                        this.activity.setConnected(false);
                    try {
                        Thread.sleep(this.retryMili);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            Long tStart = System.currentTimeMillis();
            while (this.isConnected()) {

                if (this.hasData) {
                    synchronized (this.hasData) {
                        this.writeToSocket(this.data);
                        this.hasData = false;
                    }
                    tStart = System.currentTimeMillis();
                }
                else if(System.currentTimeMillis() - tStart > this.retryMili){
                    byte[] b = new byte[1];
                    b[0] = 0;
                    this.writeToSocket(b);
                    tStart = System.currentTimeMillis();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void feed) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}
