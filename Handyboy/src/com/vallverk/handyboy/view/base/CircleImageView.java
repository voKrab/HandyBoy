package com.vallverk.handyboy.view.base;

import com.vallverk.handyboy.Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImageView extends ImageView
{
	private Bitmap originalBitmap;

	public CircleImageView ( Context context )
	{
		super ( context );
		// TODO Auto-generated constructor stub
	}

	public CircleImageView ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
	}

	public CircleImageView ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
		// TODO Auto-generated constructor stub
	}

	public void setImageBitmap ( Bitmap bitmap )
	{
		originalBitmap = bitmap;
		Bitmap fitedPhoto = Tools.fitToBounds ( bitmap, getLayoutParams ().width );
		Bitmap completeBitmap = BitmapUtils.getRoundedBitmap ( fitedPhoto, getLayoutParams ().width, getLayoutParams ().height );
		setScaleType ( ScaleType.CENTER );
		super.setImageBitmap ( completeBitmap );
	}
	
	public Bitmap getOriginalBitmap ()
	{
		return originalBitmap;
	}
}
