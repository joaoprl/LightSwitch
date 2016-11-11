package org.ls.lightswitch;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by debios on 11/5/16.
 */

class AppClient extends AsyncTask<Void, Void, Void> {
    Socket socket;
    String host;
    int port;

    Boolean hasData;
    byte[] data;

    Boolean isRunning;

    Handler handler;
    TextView connectionStatus;

    public AppClient(String host, int port, TextView connectionStatus, Handler handler) {
        this.host = host;
        this.port = port;
        this.socket = null;

        this.hasData = false;
        this.data = null;

        this.isRunning = true;

        this.connectionStatus = connectionStatus;
        this.handler = handler;
    }

    public void setData(byte[] b){
        synchronized (this.hasData) {
            this.data = b;
            this.hasData = true;
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
        return this.socket != null && this.socket.isConnected();
    }

    private boolean postConnectionStatus(final String status){
        return handler.post(new Runnable(){
            public void run(){
                connectionStatus.setText(status);
            }
        });
    }

    @Override
    protected Void doInBackground(Void ... voids) {
        while(this.isRunning) {
            while(this.socket == null) {
                try {
                    this.socket = new Socket(host, port);
                    System.out.println("connected to " + host + " : " + port);
                    postConnectionStatus("Connected");

                } catch (IOException e) {
                    this.socket = null;
                    System.out.println("failed to connect");
                    postConnectionStatus("Disconnected");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            while (this.isConnected()) {
                if (this.hasData) {
                    synchronized (this.hasData) {
                        try {
                            byte[] b = new byte[1];
                            b[0] = 1;
                            this.socket.getOutputStream().write(b);
                        } catch (IOException e) {
                            System.out.println("Failed to write");
                        }
                        this.hasData = false;
                    }
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
