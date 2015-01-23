package com.vallverk.handyboy.view.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class SquareFrameLayoutContainer extends FrameLayout
{
	public SquareFrameLayoutContainer ( Context context )
	{
		super ( context );
		// TODO Auto-generated constructor stub
	}

	public SquareFrameLayoutContainer ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		// TODO Auto-generated constructor stub
	}

	public SquareFrameLayoutContainer ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onMeasure ( int widthMeasureSpec, int heightMeasureSpec )
	{
		super.onMeasure ( widthMeasureSpec, widthMeasureSpec );
	}
}
