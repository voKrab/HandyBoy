package com.vallverk.handyboy.view.base;

import android.content.Context;
import android.util.AttributeSet;

public class MediaSquareViewFlipper extends MediaFlipperView
{
	public MediaSquareViewFlipper ( Context context )
	{
		super ( context );
		// TODO Auto-generated constructor stub
	}

	public MediaSquareViewFlipper ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onMeasure ( int widthMeasureSpec, int heightMeasureSpec )
	{
		super.onMeasure ( widthMeasureSpec, widthMeasureSpec );
	}
}
