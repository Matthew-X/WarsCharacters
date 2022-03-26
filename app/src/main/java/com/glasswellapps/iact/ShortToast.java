package com.glasswellapps.iact;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShortToast {
    public static void show(Activity context, String message){
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        View view = context.getLayoutInflater().inflate(
                R.layout.toast,
                null,
                false
        );
        ((TextView)view.findViewById(R.id.toast_text)).setText(message);


        toast.setView(view);
        //toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
