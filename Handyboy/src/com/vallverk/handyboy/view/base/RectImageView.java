package com.vallverk.handyboy.view.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RectImageView extends ImageView
{
	private Bitmap originalBitmap;

	public RectImageView ( Context context )
	{
		super ( context );
		// TODO Auto-generated constructor stub
	}

	public RectImageView ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
	}

	public RectImageView ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
	}

	public void setImageBitmap ( Bitmap bitmap )
	{
		originalBitmap = bitmap;
		setScaleType ( ScaleType.CENTER_CROP );
		super.setImageBitmap ( bitmap );
	}

	public Bitmap getOriginalBitmap ()
	{
		return originalBitmap;
	}
}
