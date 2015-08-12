package com.vallverk.handyboy.view.jobdescription;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.job.AddonId;
import com.vallverk.handyboy.model.job.TypeJob;

public class GoGoBoyViewController extends BaseController
{
	private AddonPriceViewBase briefsView;
	private AddonPriceViewBase jockstrapView;
	private CheckBox attireCheckBox;
	private AddonPriceViewBase boxersAddonPriceView;
	private View attireDetailsContainer;
    private TypeJobServiceAPIObject typeJobServiceAPIObject;

    private TextView descriptionTextView;

	public GoGoBoyViewController ( TypeJobServiceAPIObject job, TypeJob typeJob )
	{
		super ( job, typeJob );
	}

	@Override
	public View createView ( LayoutInflater inflater, final JobDescriptionViewFragment fragment )
	{
		this.fragment = fragment;
		final View view = inflater.inflate ( R.layout.job_description_gogoboy_layout, null );
        descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        //descriptionTextView.setText(getJob().getValue(TypeJobServiceAPIObject.TypeJobServiceParams.CUSTOM_DESCRIPTION).toString());
		setupAddons(view);
		createView ( view );
		addListeners ();
		return view;
	}

	private void addListeners ()
	{
		attireCheckBox.setOnCheckedChangeListener ( new OnCheckedChangeListener ()
		{
			@Override
			public void onCheckedChanged ( CompoundButton buttonView, boolean isChecked )
			{
				if ( isChecked )
				{
					attireDetailsContainer.setVisibility ( View.VISIBLE );
				} else
				{
					attireDetailsContainer.setVisibility ( View.GONE );
					boxersAddonPriceView.setChecked ( false );
					briefsView.setChecked ( false );
					jockstrapView.setChecked ( false );
				}
			}
		} );
	}
	
	@Override
	protected void updatePriceAddons ()
	{
		super.updatePriceAddons ();
		
		AddonPriceViewBase[] attireOptions = new AddonPriceViewBase[] { boxersAddonPriceView, briefsView, jockstrapView };
		updateSelection ( attireCheckBox, attireDetailsContainer, attireOptions );
	}

	private void setupAddons ( View view )
	{
		attireCheckBox = ( CheckBox ) view.findViewById ( R.id.attireCheckBox );
		attireDetailsContainer = view.findViewById ( R.id.attireDetailsContainer );
		
		boxersAddonPriceView = ( AddonPriceViewBase ) view.findViewById ( R.id.boxersAddonPriceView );
		boxersAddonPriceView.setTag ( controller.getAddon ( AddonId.BOXERS ) );
        boxersAddonPriceView.updateAddonsPrices( controller.getAddon ( AddonId.BOXERS ));
		
		briefsView = ( AddonPriceViewBase ) view.findViewById ( R.id.briefsView );
		briefsView.setTag ( controller.getAddon ( AddonId.BRIEFS ) );
        briefsView.updateAddonsPrices( controller.getAddon ( AddonId.BRIEFS ));
		
		jockstrapView = ( AddonPriceViewBase ) view.findViewById ( R.id.jockstrapView );
		jockstrapView.setTag ( controller.getAddon ( AddonId.JOCKSTRAP ) );
        jockstrapView.updateAddonsPrices( controller.getAddon ( AddonId.JOCKSTRAP ));
	}

	@Override
	protected AddonPriceViewBase[] getPriceAddons ()
	{
		return new AddonPriceViewBase[] { boxersAddonPriceView, briefsView, jockstrapView };
	}
}
