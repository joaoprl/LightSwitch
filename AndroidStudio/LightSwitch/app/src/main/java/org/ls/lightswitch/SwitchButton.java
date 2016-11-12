package org.ls.lightswitch;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;

/**
 * Created by debios on 11/6/16.
 */

public class SwitchButton implements View.OnClickListener{
    MainActivity activity;
    public SwitchButton(MainActivity activity){
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        activity.switchButton();
    }
}
