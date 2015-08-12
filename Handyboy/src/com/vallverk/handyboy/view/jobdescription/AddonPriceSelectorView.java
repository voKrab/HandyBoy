package com.vallverk.handyboy.view.jobdescription;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject.AddonServiceAPIParams;
import com.vallverk.handyboy.model.api.JobAddonsAPIObject;
import com.vallverk.handyboy.model.api.JobAddonsAPIObject.JobAddonsAPIParams;

public class AddonPriceSelectorView extends AddonPriceViewBase
{
	private int headerLeftPadding;
	private int minCost;
	private int maxCost;
	private String titleText;
	private ViewGroup detailsContainer;
	private int price;
	private CheckBox headerCheckBox;
	private SeekBar priceSeekBar;
	private int darkBlueColor;
	private int textDarkGrey;
	
	private TextView priceRangeTextView;

	public AddonPriceSelectorView ( Context context )
	{
		super ( context );
		init ( context, null );
	}

	public AddonPriceSelectorView ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		init ( context, attrs );
	}

	public AddonPriceSelectorView ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
		init ( context, attrs );
	}

	private void init ( Context context, AttributeSet attrs )
	{
		LayoutTransition layoutTransition = getLayoutTransition ();
		layoutTransition.enableTransitionType ( LayoutTransition.CHANGING );
		// setLayoutTransition ( layoutTransition );
		TypedArray ta = context.obtainStyledAttributes ( attrs, R.styleable.AddonPriceSelectorView, 0, 0 );
		try
		{
			headerLeftPadding = ta.getDimensionPixelSize ( R.styleable.AddonPriceSelectorView_headerLeftPadding, 0 );
			minCost = ta.getInteger ( R.styleable.AddonPriceSelectorView_minCost, 0 );
			maxCost = ta.getInteger ( R.styleable.AddonPriceSelectorView_maxCost, 0 );
			titleText = ta.getString ( R.styleable.AddonPriceSelectorView_titleText );
		} finally
		{
			ta.recycle ();
		}
		final LayoutInflater inflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
		final View view = inflater.inflate ( R.layout.addon_price_selector_layout, null );
		// ( ( ViewGroup ) view ).setLayoutTransition ( layoutTransition );
		if ( !isInEditMode () )
		{
			headerCheckBox = ( CheckBox ) view.findViewById ( R.id.headerCheckBox );
			headerCheckBox.setTranslationX ( headerLeftPadding );
			detailsContainer = ( ViewGroup ) view.findViewById ( R.id.detailsContainer );
			darkBlueColor = context.getResources ().getColor ( R.color.dark_blue );
			textDarkGrey = context.getResources ().getColor ( R.color.text_dark_grey );
			headerCheckBox.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View v )
				{
					updateDetails ();
				}
			} );
			if ( titleText != null && !titleText.isEmpty () )
			{
				headerCheckBox.setText ( titleText );
			}
			final TextView priceTextView = ( TextView ) view.findViewById ( R.id.priceTextView );
			priceSeekBar = ( SeekBar ) view.findViewById ( R.id.priceSeekBar );
			priceRangeTextView = ( TextView ) view.findViewById ( R.id.priceRangeTextView );
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

	protected void updateDetails ()
	{
		boolean isChecked = headerCheckBox.isChecked ();
		if ( isChecked )
		{
			headerCheckBox.setTextColor ( darkBlueColor );
			detailsContainer.setVisibility ( View.VISIBLE );
			// view.setVisibility ( View.GONE );
		} else
		{
			headerCheckBox.setTextColor ( textDarkGrey );
			detailsContainer.setVisibility ( View.GONE );
		}
	}

	public boolean isChecked ()
	{
		return headerCheckBox.isChecked ();
	}

	public int getPrice ()
	{
		return price;
	}
	
	@Override
	public void updateComponents ( final AddonServiceAPIObject addonService )
	{
        post(new Runnable() {
            @Override
            public void run() {
                if (addonService == null) {
                    return;
                }
                try {
                    int newPrice = Integer.parseInt(addonService.getString(AddonServiceAPIParams.PRICE));
                    setPrice(newPrice);
                }catch (Exception ex){ }
               ;
                setChecked(true);
            }
        });
	}

    @Override
    public void updateAddonsPrices(JobAddonsAPIObject addonsAPIObject) {
        maxCost = addonsAPIObject.getInt(JobAddonsAPIParams.MAX_COST);
        minCost = addonsAPIObject.getInt(JobAddonsAPIParams.MIN_COST);
        //priceRangeTextView.setText("$" + minCost + " - $" + maxCost);
        priceSeekBar.setMax(maxCost - minCost);
    }

	public void setPrice ( int newPrice )
	{
		priceSeekBar.setProgress ( newPrice - minCost );
        //priceSeekBar.setProgress ( newPrice);
	}

	@Override
	public void setChecked ( boolean isChecked )
	{
		headerCheckBox.setChecked ( isChecked );
		updateDetails ();
	}
}
