package com.glasswellapps.iact.character_screen.views;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.glasswellapps.iact.R;
public class IANumbers {
    public static int[] numbers = new int[]{
            R.drawable.number_0,
            R.drawable.number_1,
            R.drawable.number_2,
            R.drawable.number_3,
            R.drawable.number_4,
            R.drawable.number_5,
            R.drawable.number_6,
            R.drawable.number_7,
            R.drawable.number_8,
            R.drawable.number_9,
            R.drawable.number_10,
            R.drawable.number_11,
            R.drawable.number_12,
            R.drawable.number_13,
            R.drawable.number_14,
            R.drawable.number_15,
            R.drawable.number_16,
            R.drawable.number_17,
            R.drawable.number_18,
            R.drawable.number_19,
            R.drawable.number_20,
            R.drawable.number_21,
            R.drawable.number_22,
            R.drawable.number_23,
            R.drawable.number_24,
            R.drawable.number_25,
            R.drawable.number_26,
            R.drawable.number_27,
            R.drawable.number_28,
            R.drawable.number_29,
            R.drawable.number_30,
            R.drawable.number_31,
            R.drawable.number_32,
            R.drawable.number_33,
            R.drawable.number_34,
            R.drawable.number_35,
            R.drawable.number_36,
            R.drawable.number_37,
            R.drawable.number_38,
            R.drawable.number_39,
            R.drawable.number_40};

    public static Drawable getNumber(Resources resources, int number){

        return resources.getDrawable(numbers[number]);
    }
}
