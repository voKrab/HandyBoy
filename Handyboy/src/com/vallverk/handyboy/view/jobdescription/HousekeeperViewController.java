package com.vallverk.handyboy.view.jobdescription;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.job.AddonId;
import com.vallverk.handyboy.model.job.TypeJob;

public class HousekeeperViewController extends BaseController
{
	private AddonPriceView windowsAddonPriceView;
	private AddonPriceView laundryAddonPriceView;
	private AddonPriceView cleaningSuppliesAddonPriceView;
	private AddonPriceView ecoFriendlyAddonPriceView;
    private TextView descriptionTextView;
	private AddonPriceSelectorView shirtlessView;

	public HousekeeperViewController ( TypeJobServiceAPIObject job, TypeJob typeJob )
	{
		super ( job, typeJob );
	}

	@Override
	public View createView ( LayoutInflater inflater, final JobDescriptionViewFragment fragment )
	{
		this.fragment = fragment;
		final View view = inflater.inflate ( R.layout.job_description_housekeeper_layout, null );
        descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        //descriptionTextView.setText(getJob().getValue(TypeJobServiceAPIObject.TypeJobServiceParams.CUSTOM_DESCRIPTION).toString());
		setupAddons ( view );
		createView ( view );
		return view;
	}

	private void setupAddons ( View view )
	{
		windowsAddonPriceView = ( AddonPriceView ) view.findViewById ( R.id.windowsAddonPriceView );
		windowsAddonPriceView.setTag ( controller.getAddon ( AddonId.WINDOWS ) );
        windowsAddonPriceView.updateAddonsPrices( controller.getAddon ( AddonId.WINDOWS ));
		
		laundryAddonPriceView = ( AddonPriceView ) view.findViewById ( R.id.laundryAddonPriceView );
		laundryAddonPriceView.setTag ( controller.getAddon ( AddonId.LAUNDRY ) );
        laundryAddonPriceView.updateAddonsPrices( controller.getAddon ( AddonId.LAUNDRY ));
		
		cleaningSuppliesAddonPriceView = ( AddonPriceView ) view.findViewById ( R.id.cleaningSuppliesAddonPriceView );
		cleaningSuppliesAddonPriceView.setTag ( controller.getAddon ( AddonId.BRINGS_OWN_CLEANING_SUPPLIES ) );
        cleaningSuppliesAddonPriceView.updateAddonsPrices( controller.getAddon ( AddonId.BRINGS_OWN_CLEANING_SUPPLIES ));

		ecoFriendlyAddonPriceView = ( AddonPriceView ) view.findViewById ( R.id.ecoFriendlyAddonPriceView );
		ecoFriendlyAddonPriceView.setTag ( controller.getAddon ( AddonId.ECO_FRIENDLY ) );
        ecoFriendlyAddonPriceView.updateAddonsPrices( controller.getAddon ( AddonId.ECO_FRIENDLY ));

		shirtlessView = ( AddonPriceSelectorView ) view.findViewById ( R.id.shirtlessView );
		shirtlessView.setTag ( controller.getAddon ( AddonId.SHIRTLESS_5 ) );
        shirtlessView.updateAddonsPrices( controller.getAddon ( AddonId.SHIRTLESS_5 ));
	}

	@Override
	protected AddonPriceViewBase[] getPriceAddons ()
	{
		return new AddonPriceViewBase[] { windowsAddonPriceView, laundryAddonPriceView, cleaningSuppliesAddonPriceView, ecoFriendlyAddonPriceView, shirtlessView };
	}
}
