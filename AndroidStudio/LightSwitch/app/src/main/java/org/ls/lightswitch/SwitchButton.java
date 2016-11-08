package org.ls.lightswitch;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;

/**
 * Created by debios on 11/6/16.
 */

public class SwitchButton implements View.OnClickListener{
    Boolean buttonFlag;
    final Button button;
    AppClient client;

    public SwitchButton(Button button, AppClient client){
        this.client = client;

        this.buttonFlag = false;

        this.button = button;
        this.button.setBackgroundColor(Color.BLUE);
    }

    @Override
    public void onClick(View v) {
        System.out.println("click");

        byte[] b = new byte[1];
        b[0] = 1;
        client.setData(b);

        if(this.buttonFlag) {
            this.button.setBackgroundColor(Color.BLUE);
            this.buttonFlag = false;
        }
        else {
            this.button.setBackgroundColor(Color.WHITE);
            this.buttonFlag = true;
        }
    }
}
