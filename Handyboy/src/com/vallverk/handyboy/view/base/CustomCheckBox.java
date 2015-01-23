package com.vallverk.handyboy.view.base;

import com.vallverk.handyboy.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class CustomCheckBox extends TextView
{
	private boolean isChecked = false;

	public CustomCheckBox ( Context context )
	{
		super ( context );
		init ( context, null );
	}

	public CustomCheckBox ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		init ( context, attrs );
	}

	public CustomCheckBox ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
		init ( context, attrs );
	}

	public boolean isChecked ()
	{
		return isChecked;
	}

	public boolean setChecked ( boolean isChecked )
	{
		this.isChecked = isChecked;
		setCheckedResourse ();
		return isChecked;
	}

	private void setCheckedResourse ()
	{
		if ( isChecked )
		{
			setCompoundDrawablesWithIntrinsicBounds ( R.drawable.tick_a, 0, 0, 0 );
		} else
		{
			setCompoundDrawablesWithIntrinsicBounds ( R.drawable.tick_na, 0, 0, 0 );
		}
	}

	private void init ( Context context, AttributeSet attrs )
	{
		TypedArray typedArray = context.obtainStyledAttributes ( attrs, R.styleable.CustomCheckBox, 0, 0 );
		try
		{
			setChecked ( typedArray.getBoolean ( R.styleable.CustomCheckBox_isChecked, false ) );
		} catch ( Exception ex )
		{
			ex.printStackTrace ();
		} finally
		{
			typedArray.recycle ();
		}
		setCompoundDrawablePadding ( 20 );

		setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				if ( isChecked () )
				{
					setChecked ( false );
				} else
				{
					setChecked ( true );
				}
			}
		} );
	}
}
