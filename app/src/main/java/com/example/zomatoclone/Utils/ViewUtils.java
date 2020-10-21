package com.example.zomatoclone.Utils;

import android.content.res.Resources;

public class ViewUtils {

    public static Float pxToDp(Float px){
        float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return  px / (densityDpi / 160f);
    }

    public static Integer dpToPx(Float dp){
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
