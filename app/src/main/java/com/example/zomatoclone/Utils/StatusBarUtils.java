package com.example.zomatoclone.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.Window;

import com.example.zomatoclone.R;

public class StatusBarUtils {
    public static void setStatsBarColor(Context context, Window window, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(context.getColor(color));
        }
    }
}
