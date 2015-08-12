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

public class CarWashBoyViewController extends BaseController
{
	private CheckBox attireCheckBox;
	private View attireDetailsContainer;
	private CheckBox bathingSuitCheckBox;
	private View bathingSuitDetailsContainer;
	private AddonPriceViewBase truckSuvView;
	private AddonPriceViewBase waxView;
	private AddonPriceViewBase detailingView;
	private AddonPriceViewBase boardShortsAddonPriceView;
	private AddonPriceViewBase trunksAddonPriceView;
	private AddonPriceViewBase speedoAddonPriceSelectorView;
	private AddonPriceViewBase shirtlessView;
	private AddonPriceViewBase tireDressingAddonPriceView;
    private TextView descriptionTextView;


	public CarWashBoyViewController ( TypeJobServiceAPIObject job, TypeJob typeJob )
	{
		super ( job, typeJob );
	}

	@Override
	public View createView ( LayoutInflater inflater, final JobDescriptionViewFragment fragment )
	{
		this.fragment = fragment;
		final View view = inflater.inflate ( R.layout.job_description_carwash_layout, null );
        descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
       // descriptionTextView.setText(getJob().getValue(TypeJobServiceAPIObject.TypeJobServiceParams.CUSTOM_DESCRIPTION).toString());
		setupAddons ( view );
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
					bathingSuitCheckBox.setChecked ( false );
				}
			}
		} );

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
	protected void updatePriceAddons ()
	{
		super.updatePriceAddons ();
		
		AddonPriceViewBase[] bathingSuitOptions = new AddonPriceViewBase[] { boardShortsAddonPriceView, trunksAddonPriceView, speedoAddonPriceSelectorView };
		updateSelection ( bathingSuitCheckBox, bathingSuitDetailsContainer, bathingSuitOptions );
			
		if ( shirtlessView.isChecked () || bathingSuitCheckBox.isChecked () )
		{
			attireCheckBox.setChecked ( true );
			attireDetailsContainer.setVisibility ( View.VISIBLE );
		} else
		{
			attireCheckBox.setChecked ( false );
			attireDetailsContainer.setVisibility ( View.GONE );
		}
	}

	private void setupAddons ( View view )
	{
		truckSuvView = ( AddonPriceViewBase ) view.findViewById ( R.id.truckSuvView );
		truckSuvView.setTag ( controller.getAddon ( AddonId.TRUCK_SUV ) );
        truckSuvView.updateAddonsPrices( controller.getAddon ( AddonId.TRUCK_SUV ) );
		
		waxView = ( AddonPriceViewBase ) view.findViewById ( R.id.waxView );
		waxView.setTag ( controller.getAddon ( AddonId.WAX ) );
        waxView.updateAddonsPrices( controller.getAddon ( AddonId.WAX ) );
		
		tireDressingAddonPriceView = ( AddonPriceViewBase ) view.findViewById ( R.id.tireDressingAddonPriceView );
		tireDressingAddonPriceView.setTag ( controller.getAddon ( AddonId.TIRE_DRESSING ) );
        tireDressingAddonPriceView.updateAddonsPrices( controller.getAddon ( AddonId.TIRE_DRESSING ) );
		
		detailingView = ( AddonPriceViewBase ) view.findViewById ( R.id.detailingView );
//		detailingView.setTag ( controller.getAddon ( AddonId.DETAILING ) );
		detailingView.setVisibility ( View.GONE );
		
		attireCheckBox = ( CheckBox ) view.findViewById ( R.id.attireCheckBox );
		attireDetailsContainer = view.findViewById ( R.id.attireDetailsContainer );

		shirtlessView = ( AddonPriceViewBase ) view.findViewById ( R.id.shirtlessView );
		shirtlessView.setTag ( controller.getAddon ( AddonId.SHIRTLESS_3 ) );
        shirtlessView.updateAddonsPrices( controller.getAddon ( AddonId.SHIRTLESS_3 ) );
		
		bathingSuitCheckBox = ( CheckBox ) view.findViewById ( R.id.bathingSuitCheckBox );
		bathingSuitDetailsContainer = view.findViewById ( R.id.bathingSuitContainer );
		
		boardShortsAddonPriceView = ( AddonPriceViewBase ) view.findViewById ( R.id.boardShortsAddonPriceView );
		boardShortsAddonPriceView.setTag ( controller.getAddon ( AddonId.BOARD_SHORTS_1 ) );
        boardShortsAddonPriceView.updateAddonsPrices( controller.getAddon ( AddonId.BOARD_SHORTS_1 ) );

		trunksAddonPriceView = ( AddonPriceViewBase ) view.findViewById ( R.id.trunksAddonPriceView );
		trunksAddonPriceView.setTag ( controller.getAddon ( AddonId.TRUNKS_1 ) );
        trunksAddonPriceView.updateAddonsPrices(  controller.getAddon ( AddonId.TRUNKS_1 ) );

		speedoAddonPriceSelectorView = ( AddonPriceViewBase ) view.findViewById ( R.id.speedoView );
		speedoAddonPriceSelectorView.setTag ( controller.getAddon ( AddonId.SPEEDO_1 ) );
        speedoAddonPriceSelectorView.updateAddonsPrices( controller.getAddon ( AddonId.SPEEDO_1 ) );
	}

	@Override
	protected AddonPriceViewBase[] getPriceAddons ()
	{
//		return new AddonPriceViewBase[] { truckSuvView, waxView, tireDressingAddonPriceView, detailingView, shirtlessView, boardShortsAddonPriceView, trunksAddonPriceView, speedoAddonPriceSelectorView };
		return new AddonPriceViewBase[] { truckSuvView, waxView, tireDressingAddonPriceView, shirtlessView, boardShortsAddonPriceView, trunksAddonPriceView, speedoAddonPriceSelectorView };
	}
}
