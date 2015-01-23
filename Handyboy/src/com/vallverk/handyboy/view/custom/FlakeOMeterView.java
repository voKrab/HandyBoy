package com.vallverk.handyboy.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.vallverk.handyboy.R;

public class FlakeOMeterView extends FrameLayout
{
	float rating;
	private View backgroundView;
	private View ratingView;
	
	public FlakeOMeterView ( Context context )
	{
		super ( context );
		init ( context, null );
	}

	public FlakeOMeterView ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		init ( context, attrs );
	}

	public FlakeOMeterView ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
		init ( context, attrs );
	}

	private void init ( final Context context, AttributeSet attrs )
	{
		rating = 0;
		final LayoutInflater inflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
		final View view = inflater.inflate ( R.layout.flake_o_meter_layout, null );
		TypedArray ta = context.obtainStyledAttributes ( attrs, R.styleable.FlakeOMeterView, 0, 0 );
		try
		{
			rating = ta.getFloat ( R.styleable.FlakeOMeterView_value, 0 );
		} finally
		{
			ta.recycle ();
		}
		if ( !isInEditMode () )
		{
			backgroundView = view.findViewById ( R.id.backgroundView );
			ratingView = view.findViewById ( R.id.ratingView );
			setRating ( rating );
		}
		addView ( view );
	}

	public void setRating ( final float rating )
	{
		this.rating = rating;
		ratingView.post ( new Runnable ()
		{
			@Override
			public void run ()
			{
				int width = getWidth ();
				int ratingViewWidth = ( int ) ( width * rating );
				ratingView.setLayoutParams ( new RelativeLayout.LayoutParams ( ratingViewWidth, android.widget.RelativeLayout.LayoutParams.MATCH_PARENT ) );
			}
		} );
	}
}
