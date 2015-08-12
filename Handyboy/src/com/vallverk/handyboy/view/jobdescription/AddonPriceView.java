package com.vallverk.handyboy.view.jobdescription;

import android.animation.LayoutTransition;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject.AddonServiceAPIParams;
import com.vallverk.handyboy.model.api.JobAddonsAPIObject;

public class AddonPriceView extends AddonPriceViewBase
{
	private int headerLeftPadding;
	private int price;
	private String name;
	private String subName;

	private CheckBox checkBox;
	private TextView checkBoxSubTitle;
	private TextView priceTextView;
	private int darkBlueColor;
	private int textDarkGrey;

	public AddonPriceView ( Context context )
	{
		super ( context );
		init ( context, null );
	}

	public AddonPriceView ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		init ( context, attrs );
	}

	public AddonPriceView ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
		init ( context, attrs );
	}

	private void init ( Context context, AttributeSet attrs )
	{
		LayoutTransition layoutTransition = getLayoutTransition ();
		layoutTransition.enableTransitionType ( LayoutTransition.CHANGING );
		TypedArray ta = context.obtainStyledAttributes ( attrs, R.styleable.AddonPriceView, 0, 0 );
		try
		{
			headerLeftPadding = ta.getDimensionPixelSize ( R.styleable.AddonPriceView_leftPadding, 0 );
			price = ta.getInt ( R.styleable.AddonPriceView_cost, 0 );
			name = ta.getString ( R.styleable.AddonPriceView_name );
			subName = ta.getString ( R.styleable.AddonPriceView_subName );
		} finally
		{
			ta.recycle ();
		}
		final LayoutInflater inflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
		final View view = inflater.inflate ( R.layout.addon_price_layout, null );
		if ( !isInEditMode () )
		{
			checkBox = ( CheckBox ) view.findViewById ( R.id.headerCheckBox );
			checkBoxSubTitle = ( TextView ) view.findViewById ( R.id.checkBoxSubTitle );
			checkBox.setTranslationX ( headerLeftPadding );
			priceTextView = ( TextView ) view.findViewById ( R.id.priceTextView );
			darkBlueColor = context.getResources ().getColor ( R.color.dark_blue );
			textDarkGrey = context.getResources ().getColor ( R.color.text_dark_grey );
			checkBox.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View v )
				{
					updateComponents ();
				}
			} );
			if ( name != null && !name.isEmpty () )
			{
				checkBox.setText ( name );
			}
			if ( subName != null && !subName.isEmpty () )
			{
				checkBoxSubTitle.setVisibility ( View.VISIBLE );
			} else
			{
				checkBoxSubTitle.setVisibility ( View.GONE );
			}
			priceTextView.setText ( "+$" + price );
		}
		addView ( view, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT );
	}

	protected void updateComponents ()
	{
		boolean isChecked = checkBox.isChecked ();
		if ( isChecked )
		{
			checkBox.setTextColor ( darkBlueColor );
			priceTextView.setVisibility ( View.VISIBLE );
		} else
		{
			checkBox.setTextColor ( textDarkGrey );
			priceTextView.setVisibility ( View.GONE );
		}
	}

	public boolean isChecked ()
	{
		return checkBox.isChecked ();
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
                if ( addonService == null )
                {
                    return;
                }
                try{
                    price = Integer.parseInt ( addonService.getString ( AddonServiceAPIParams.PRICE ) );
                }catch (Exception ex){
                   // price = 0;
                }

                setChecked ( true );
            }
        });

	}

    @Override
    public void updateAddonsPrices(JobAddonsAPIObject addonsAPIObject) {
        int maxCost = addonsAPIObject.getInt(JobAddonsAPIObject.JobAddonsAPIParams.MAX_COST);
        if(maxCost == 0){
            priceTextView.setText ( "" );
        }else{
            priceTextView.setText ( "+$" + maxCost );
        }
    }

	public void setChecked ( boolean isChecked )
	{
		checkBox.setChecked ( isChecked );
		updateComponents ();
	}
}
