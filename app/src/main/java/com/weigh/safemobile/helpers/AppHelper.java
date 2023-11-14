package com.weigh.safemobile.helpers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weigh.safemobile.R;

import java.util.Hashtable;

public class AppHelper {
    /**
     * method for get a custom CustomToast
     *
     * @param Message this is the second parameter for CustomToast  method
     */
    public static void CustomToast(Context mContext, String Message) {

        LinearLayout CustomToastLayout = new LinearLayout(mContext.getApplicationContext());
        CustomToastLayout.setBackgroundResource(R.drawable.bg_custom_toast);
        CustomToastLayout.setGravity(Gravity.TOP);
        TextView message = new TextView(mContext.getApplicationContext());
        message.setTextColor(Color.WHITE);
        message.setTextSize(13);
        message.setPadding(20, 20, 20, 20);
        message.setGravity(Gravity.CENTER);
        message.setText(Message);
        CustomToastLayout.addView(message);
        Toast toast = new Toast(mContext.getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(CustomToastLayout);
        toast.setGravity(Gravity.CENTER, 0, 50);
        toast.show();
    }


    private static final Hashtable<String, Typeface> cache = new Hashtable<>();
    public static Typeface setTypeFace(Context c, String name) {
        synchronized (cache) {
            if (!cache.containsKey(name)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(), "fonts/" + name + ".ttf");
                    cache.put(name, t);
                } catch (Exception e) {
                    Log.e("TAG","LogCatThrowable " + e.toString());
                    return null;
                }
            }
            return cache.get(name);
        }
    }
}
