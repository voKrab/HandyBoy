package com.vallverk.handyboy.view.jobdescription;

import com.vallverk.handyboy.model.api.AddonServiceAPIObject;
import com.vallverk.handyboy.model.api.JobAddonsAPIObject;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public abstract class AddonPriceViewBase extends LinearLayout
{
	public AddonPriceViewBase ( Context context )
	{
		super ( context );
	}

	public AddonPriceViewBase ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
	}

	public AddonPriceViewBase ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );

	}

	public abstract boolean isChecked ();
	public abstract int getPrice ();
	public abstract void updateComponents ( AddonServiceAPIObject addonService );
    public abstract void updateAddonsPrices ( JobAddonsAPIObject addonsAPIObject );

	public abstract void setChecked ( boolean isChecked );
}
