package com.vallverk.handyboy.view.jobdescription;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.vallverk.handyboy.R;

public class PricePerHourView extends FrameLayout
{
	private int minCost;
	private int maxCost;
	private int price;

	private SeekBar priceSeekBar;
	private TextView priceRangeTextView;
	private TextView priceTextView;

	public PricePerHourView ( Context context )
	{
		super ( context );
		init ( context, null );
	}

	public PricePerHourView ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		init ( context, attrs );
	}

	public PricePerHourView ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
		init ( context, attrs );
	}

	private void init ( Context context, AttributeSet attrs )
	{
		// LayoutTransition layoutTransition = getLayoutTransition ();
		// layoutTransition.enableTransitionType ( LayoutTransition.CHANGING );
		// setLayoutTransition ( new LayoutTransition () );
		TypedArray ta = context.obtainStyledAttributes ( attrs, R.styleable.PricePerHourView, 0, 0 );
		try
		{
			minCost = ta.getInteger ( R.styleable.PricePerHourView_minPrice, 0 );
			maxCost = ta.getInteger ( R.styleable.PricePerHourView_maxPrice, 0 );
		} finally
		{
			ta.recycle ();
		}
		final LayoutInflater inflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
		final View view = inflater.inflate ( R.layout.price_per_hour_layout, null );
		if ( !isInEditMode () )
		{
			priceTextView = ( TextView ) view.findViewById ( R.id.priceTextView );
			priceSeekBar = ( SeekBar ) view.findViewById ( R.id.priceSeekBar );
			priceRangeTextView = ( TextView ) view.findViewById ( R.id.priceRangeTextView );
			// priceRangeTextView.setText ( "$" + minCost + " - $" + maxCost );
			setRecommendPrice ( minCost, maxCost );
			priceSeekBar.setMax ( maxCost - minCost );
			priceSeekBar.setOnSeekBarChangeListener ( new OnSeekBarChangeListener ()
			{
				@Override
				public void onStopTrackingTouch ( SeekBar seekBar )
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onStartTrackingTouch ( SeekBar seekBar )
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onProgressChanged ( SeekBar seekBar, int progress, boolean fromUser )
				{
					int offset = minCost;
					price = progress + offset;
					priceTextView.setText ( "$" + price );
				}
			} );
			priceSeekBar.setProgress ( ( maxCost - minCost ) / 2 );
		}
		addView ( view, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT );
	}

	public int getPrice ()
	{
		return price;
	}

	public void setPrice ( int distancePrice )
	{
		priceSeekBar.setProgress ( distancePrice - minCost );
	}
	
	public void setMinMaxPrice ( int minCost, int maxCost )
	{
		this.maxCost = maxCost;
		this.minCost = minCost;
		priceSeekBar.setMax ( maxCost - minCost );
	}
	
	public void setRecommendPrice ( int recommendMinCost, int recommendMaxCost )
	{
		priceRangeTextView.setText ( "$" + recommendMinCost + " - $" + recommendMaxCost );
	}
}
