package com.vallverk.handyboy.view.jobdescription;

import android.view.LayoutInflater;
import android.view.View;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.job.TypeJob;

public class ErrandBoyViewController extends BaseController
{
	public ErrandBoyViewController ( TypeJobServiceAPIObject job, TypeJob typeJob )
	{
		super ( job, typeJob );
	}

	@Override
	public View createView ( LayoutInflater inflater, final JobDescriptionViewFragment fragment )
	{
		this.fragment = fragment;
		final View view = inflater.inflate ( R.layout.job_description_errand_boy_layout, null );
		createView ( view );
		return view;
	}

	@Override
	protected void save ()
	{
		super.save ();
	}
}
