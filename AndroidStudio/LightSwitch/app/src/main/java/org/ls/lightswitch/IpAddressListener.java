package org.ls.lightswitch;

import android.graphics.Color;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by debios on 11/9/16.
 */

public class IpAddressListener implements TextView.OnEditorActionListener {
    AppClient client;
    public IpAddressListener(AppClient client){
        this.client = client;
    }

    private boolean checkIpAddress(String str){
        String[] nums = str.split("\\.");
        if(nums.length == 4){
            for(int i = 0; i < 4; i++) {
                if (nums[i].length() == 0 || new Integer(nums[i]) > 256)
                    return false;
            }
            if(new Integer(nums[0]) > 0)
                return true;
        }
        return false;
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if ((event != null && (event.getKeyCode() == event.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

            if(checkIpAddress(v.getText().toString()))
                client.setIpAddress(v.getText().toString());
            else
                v.setError("Invalid IP address");
        }
        return false;
    }
}
