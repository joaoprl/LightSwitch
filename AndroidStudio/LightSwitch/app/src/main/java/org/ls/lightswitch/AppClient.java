package org.ls.lightswitch;


import android.os.AsyncTask;

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

    public AppClient(String host, int port) {
        this.host = host;
        this.port = port;

        this.hasData = false;
        this.data = null;
    }

    public void setData(byte[] b){
        synchronized (this.hasData) {
            this.data = b;
            this.hasData = true;
        }
    }

    @Override
    protected Void doInBackground(Void ... voids) {
        try {
            this.socket = new Socket(host, port);
            System.out.println("connected");
        }catch(IOException e){
            System.out.println("failed to connect");
            return null;
        }

        while(this.socket.isConnected()) {
            if(this.hasData) {
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
        return null;
    }

    @Override
    protected void onPostExecute(Void feed) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}
