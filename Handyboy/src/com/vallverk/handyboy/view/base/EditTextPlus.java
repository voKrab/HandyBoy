package com.vallverk.handyboy.view.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.view.base.FontUtils.FontStyle;

public class EditTextPlus extends EditText {
	
    public EditTextPlus(Context context) {
        super(context);
    }

    public EditTextPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public EditTextPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
        
        int customFont = a.getInt(R.styleable.TextViewPlus_customFont, 0);
        Log.d("TextViewPlus", "setCustomFont = " + customFont);
        setCustomFont(ctx, FontStyle.fromId ( customFont ));
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, FontStyle fontStyle) {
    	Log.d("TextViewPlus", "setCustomFont");
    	FontUtils.getInstance ( getContext() ).applyStyle ( EditTextPlus.this, fontStyle );
        return true;
    }

}
