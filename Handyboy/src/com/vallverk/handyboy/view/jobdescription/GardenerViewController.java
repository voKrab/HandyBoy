package com.vallverk.handyboy.view.jobdescription;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.job.AddonId;
import com.vallverk.handyboy.model.job.TypeJob;

public class GardenerViewController extends BaseController
{
	private AddonPriceViewBase shirtlessView;
    private TextView descriptionTextView;

	public GardenerViewController ( TypeJobServiceAPIObject job, TypeJob typeJob )
	{
		super ( job, typeJob );
	}

	@Override
	public View createView ( LayoutInflater inflater, final JobDescriptionViewFragment fragment )
	{
		this.fragment = fragment;
		final View view = inflater.inflate ( R.layout.job_description_gardener_layout, null );
        descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        /*if(getJob() != null) {
            descriptionTextView.setText(getJob().getValue(TypeJobServiceAPIObject.TypeJobServiceParams.CUSTOM_DESCRIPTION).toString());
        }*/
		setupAddons ( view );
		createView ( view );	
		return view;
	}

	private void setupAddons ( View view )
	{
		shirtlessView = ( AddonPriceViewBase ) view.findViewById ( R.id.shirtlessView );
		shirtlessView.setTag ( controller.getAddon ( AddonId.SHIRTLESS_4 ) );
        shirtlessView.updateAddonsPrices( controller.getAddon ( AddonId.SHIRTLESS_4 ) );
	}

	@Override
	protected AddonPriceViewBase[] getPriceAddons ()
	{
		return new AddonPriceViewBase[] { shirtlessView };
	}
}
