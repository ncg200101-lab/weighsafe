package com.weigh.safemobile.helpers;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterMax implements InputFilter {

    private int max;

    public InputFilterMax(int max) {
        this.max = max;
    }

    public InputFilterMax(String max) {
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            String replacement = source.subSequence(start, end).toString();

            String newVal = dest.toString().substring(0, dstart) + replacement +dest.toString().substring(dend, dest.toString().length());

            int input = Integer.parseInt(newVal);

            if (input<=max)
                return null;
        } catch (NumberFormatException nfe) { }
//Maybe notify user that the value is not good
        return "";
    }
}