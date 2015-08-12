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

public class PoolBoyViewController extends BaseController
{
	private CheckBox bathingSuitCheckBox;
	private View bathingSuitDetailsContainer;
	private AddonPriceViewBase shirtlessView;
	private AddonPriceViewBase boardShortsAddonPriceView;
	private AddonPriceViewBase trunksAddonPriceView;
	private AddonPriceViewBase speedoAddonPriceSelectorView;
    private TextView descriptionTextView;


	public PoolBoyViewController ( TypeJobServiceAPIObject job, TypeJob typeJob )
	{
		super ( job, typeJob );
	}

	@Override
	public View createView ( LayoutInflater inflater, final JobDescriptionViewFragment fragment )
	{
		this.fragment = fragment;
		final View view = inflater.inflate ( R.layout.job_description_poolboy_layout, null );
        descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        //descriptionTextView.setText(getJob().getValue(TypeJobServiceAPIObject.TypeJobServiceParams.CUSTOM_DESCRIPTION).toString());
		setupAddons ( view );
		createView ( view );
		addListeners ();
		return view;
	}

	@Override
	protected void updatePriceAddons ()
	{
		super.updatePriceAddons ();
		
		AddonPriceViewBase[] bathingSuitOptions = new AddonPriceViewBase[] { boardShortsAddonPriceView, trunksAddonPriceView, speedoAddonPriceSelectorView };
		updateSelection ( bathingSuitCheckBox, bathingSuitDetailsContainer, bathingSuitOptions );
	}

	private void setupAddons ( View view )
	{
		bathingSuitCheckBox = ( CheckBox ) view.findViewById ( R.id.bathingSuitCheckBox );
		bathingSuitDetailsContainer = view.findViewById ( R.id.bathingSuitDetailsContainer );

		shirtlessView = ( AddonPriceViewBase ) view.findViewById ( R.id.shirtlessView );
		shirtlessView.setTag ( controller.getAddon ( AddonId.SHIRTLESS_6 ) );
        shirtlessView.updateAddonsPrices( controller.getAddon ( AddonId.SHIRTLESS_6 ));


		boardShortsAddonPriceView = ( AddonPriceViewBase ) view.findViewById ( R.id.boardShortsAddonPriceView );
		boardShortsAddonPriceView.setTag ( controller.getAddon ( AddonId.BOARD_SHORTS_2 ) );
        boardShortsAddonPriceView.updateAddonsPrices( controller.getAddon ( AddonId.BOARD_SHORTS_2 ) );

		trunksAddonPriceView = ( AddonPriceViewBase ) view.findViewById ( R.id.trunksAddonPriceView );
		trunksAddonPriceView.setTag ( controller.getAddon ( AddonId.TRUNKS_2 ) );
        trunksAddonPriceView.updateAddonsPrices(  controller.getAddon ( AddonId.TRUNKS_2 ) );

		speedoAddonPriceSelectorView = ( AddonPriceViewBase ) view.findViewById ( R.id.speedoAddonPriceSelectorView );
		speedoAddonPriceSelectorView.setTag ( controller.getAddon ( AddonId.SPEEDO_2 ) );
        speedoAddonPriceSelectorView.updateAddonsPrices(   controller.getAddon ( AddonId.SPEEDO_2 ) );
	}

	private void addListeners ()
	{
		bathingSuitCheckBox.setOnCheckedChangeListener ( new OnCheckedChangeListener ()
		{
			@Override
			public void onCheckedChanged ( CompoundButton buttonView, boolean isChecked )
			{
				if ( isChecked )
				{
					bathingSuitDetailsContainer.setVisibility ( View.VISIBLE );
				} else
				{
					bathingSuitDetailsContainer.setVisibility ( View.GONE );
					boardShortsAddonPriceView.setChecked ( false );
					trunksAddonPriceView.setChecked ( false );
					speedoAddonPriceSelectorView.setChecked ( false );
				}
			}
		} );
	}

	@Override
	protected AddonPriceViewBase[] getPriceAddons ()
	{
		return new AddonPriceViewBase[] { shirtlessView, boardShortsAddonPriceView, trunksAddonPriceView, speedoAddonPriceSelectorView };
	}
}
