package com.vallverk.handyboy.view.jobdescription;

import android.view.LayoutInflater;
import android.view.View;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.job.AddonId;
import com.vallverk.handyboy.model.job.TypeJob;

public class ServerViewController extends BaseController
{
	private AddonPriceViewBase shirtlessView;

	public ServerViewController ( TypeJobServiceAPIObject job, TypeJob typeJob )
	{
		super ( job, typeJob );
	}

	@Override
	public View createView ( LayoutInflater inflater, final JobDescriptionViewFragment fragment )
	{
		this.fragment = fragment;
		final View view = inflater.inflate ( R.layout.job_description_server_layout, null );
		setupAddons ( view );
		createView ( view );	
		
		return view;
	}

	private void setupAddons ( View view )
	{
		shirtlessView = ( AddonPriceViewBase ) view.findViewById ( R.id.shirtlessView );
		shirtlessView.setTag ( controller.getAddon ( AddonId.SHIRTLESS_1 ) );
	}

	@Override
	protected AddonPriceViewBase[] getPriceAddons ()
	{
		return new AddonPriceViewBase[] { shirtlessView };
	}
}
