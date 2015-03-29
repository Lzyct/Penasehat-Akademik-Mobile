package com.ukietux.pamobile.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomEditTextView extends EditText {

public CustomEditTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
}

public CustomEditTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
}

public CustomEditTextView(Context context) {
    super(context);
    init();
}

private void init() {
    if (!isInEditMode()) {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto.ttf");
        setTypeface(tf);
    }
}
}