package com.vallverk.handyboy.view.jobdescription;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.vallverk.handyboy.R;

public class DistancePriceView extends FrameLayout
{
	private int headerLeftPadding;
	private int minCost;
	private int maxCost;
	private int price;
	private CheckBox headerCheckBox;
	private View headerContainer;
	private SeekBar priceSeekBar;

	public DistancePriceView ( Context context )
	{
		super ( context );
		init ( context, null );
	}

	public DistancePriceView ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		init ( context, attrs );
	}

	public DistancePriceView ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
		init ( context, attrs );
	}

	private void init ( Context context, AttributeSet attrs )
	{
//		LayoutTransition layoutTransition = getLayoutTransition ();
//		layoutTransition.enableTransitionType ( LayoutTransition.CHANGING );
		TypedArray ta = context.obtainStyledAttributes ( attrs, R.styleable.AddonPriceSelectorView, 0, 0 );
		try
		{
			headerLeftPadding = ta.getDimensionPixelSize ( R.styleable.AddonPriceSelectorView_headerLeftPadding, 0 );
			minCost = ta.getInteger ( R.styleable.AddonPriceSelectorView_minCost, 5 );
			maxCost = ta.getInteger ( R.styleable.AddonPriceSelectorView_maxCost, 15 );
		} finally
		{
			ta.recycle ();
		}
		final LayoutInflater inflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
		final ViewGroup view = ( ViewGroup ) inflater.inflate ( R.layout.distance_price_layout, null );
		if ( !isInEditMode () )
		{
//			view.getLayoutTransition ().enableTransitionType ( LayoutTransition.CHANGING );
			headerContainer = view.findViewById ( R.id.headerContainer );
			headerCheckBox = ( CheckBox ) view.findViewById ( R.id.headerCheckBox );
			headerCheckBox.setClickable ( false );
			headerCheckBox.setTranslationX ( headerLeftPadding );
			final View detailsContainer = view.findViewById ( R.id.detailsContainer );
			//final int darkBlueColor = context.getResources ().getColor ( R.color.dark_blue );
			//final int textDarkGrey = context.getResources ().getColor ( R.color.text_dark_grey );
			headerContainer.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View v )
				{
					headerCheckBox.setChecked ( !headerCheckBox.isChecked () );
					boolean isChecked = headerCheckBox.isChecked ();
					if ( isChecked )
					{
						//headerCheckBox.setTextColor ( darkBlueColor );
						detailsContainer.setVisibility ( View.VISIBLE );
						// view.setVisibility ( View.GONE );
					} else
					{
						//headerCheckBox.setTextColor ( textDarkGrey );
						detailsContainer.setVisibility ( View.GONE );
					}
				}
			} );
			final TextView priceTextView = ( TextView ) view.findViewById ( R.id.priceTextView );
			priceSeekBar = ( SeekBar ) view.findViewById ( R.id.priceSeekBar );
			final TextView priceRangeTextView = ( TextView ) view.findViewById ( R.id.priceRangeTextView );
			priceRangeTextView.setText ( "$" + minCost + " - $" + maxCost );
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
		return headerCheckBox.isChecked () ? price : 0;
	}

	public void setPrice ( int distancePrice )
	{
		priceSeekBar.setProgress ( distancePrice - minCost );
		headerContainer.performClick ();
	}
}
