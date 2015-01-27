package com.vallverk.handyboy.view.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.view.base.FontUtils.FontStyle;

public class TextViewPlus extends TextView
{

	public TextViewPlus ( Context context )
	{
		super ( context );
	}

	public TextViewPlus ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		setCustomFont ( context, attrs );
	}

	public TextViewPlus ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
		setCustomFont ( context, attrs );
	}

	private void setCustomFont ( Context ctx, AttributeSet attrs )
	{
        if ( isInEditMode () )
        {
            return;
        }
		TypedArray a = ctx.obtainStyledAttributes ( attrs, R.styleable.TextViewPlus );

		int customFont = a.getInt ( R.styleable.TextViewPlus_customFont, 0 );
		Log.d ( "TextViewPlus", "setCustomFont = " + customFont );
		setCustomFont ( ctx, FontStyle.fromId ( customFont ) );
		a.recycle ();
	}

	public boolean setCustomFont ( Context ctx, FontStyle fontStyle )
	{
		Log.d ( "TextViewPlus", "setCustomFont" );
		FontUtils.getInstance ( getContext () ).applyStyle ( TextViewPlus.this, fontStyle );
		return true;
	}

}
