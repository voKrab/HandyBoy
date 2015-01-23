package com.vallverk.handyboy.view.jobdescription;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject;
import com.vallverk.handyboy.model.api.AddressAPIObject;
import com.vallverk.handyboy.model.api.AddressAPIObject.AddressParams;
import com.vallverk.handyboy.model.api.JobAddonsAPIObject;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject.AddonServiceAPIParams;
import com.vallverk.handyboy.model.job.AddonId;
import com.vallverk.handyboy.model.job.TypeJob;
import com.vallverk.handyboy.server.ServerManager;

public class PersonalTrainerViewController extends BaseController
{
	private AddonPriceSelectorView gymShirtlessView;
	private ViewGroup gymDetailsContainer;
	private ViewGroup yourLocationDetailsContainer;
	private CheckBox gymCheckBox;
	private EditText addressNameEditText;
	private EditText addressEditText;
	private CheckBox yourLocationCheckBox;
	private AddonPriceSelectorView yourLocationShirtlessView;

	public PersonalTrainerViewController ( TypeJobServiceAPIObject job, TypeJob typeJob )
	{
		super ( job, typeJob );
	}

	@Override
	public View createView ( LayoutInflater inflater, final JobDescriptionViewFragment fragment )
	{
		final View view = inflater.inflate ( R.layout.job_description_personal_trainer_layout, null );
		this.fragment = fragment;
		setupAddons ( view );
		createView ( view );
		return view;
	}
	
	private void setupAddons ( View view )
	{
		final int darkBlueColor = MainActivity.getInstance ().getResources ().getColor ( R.color.dark_blue );
		final int textDarkGrey = MainActivity.getInstance ().getResources ().getColor ( R.color.text_dark_grey );

		gymCheckBox = ( CheckBox ) view.findViewById ( R.id.gymCheckBox );
		gymDetailsContainer = ( ViewGroup ) view.findViewById ( R.id.gymDetailsContainer );
		gymShirtlessView = ( AddonPriceSelectorView ) view.findViewById ( R.id.gymShirtlessView );
		gymShirtlessView.setVisibility ( View.GONE );
		addressNameEditText = ( EditText ) view.findViewById ( R.id.addressNameEditText );
		addressEditText = ( EditText ) view.findViewById ( R.id.addressEditText );
		
//		layoutTransition.enableTransitionType ( LayoutTransition.CHANGING );
//		gymDetailsContainer.setLayoutTransition ( gymLayoutTransition );
		gymCheckBox.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				boolean isChecked = gymCheckBox.isChecked ();
				if ( isChecked )
				{
					gymCheckBox.setTextColor ( darkBlueColor );
					gymDetailsContainer.setVisibility ( View.VISIBLE );
				} else
				{
					gymCheckBox.setTextColor ( textDarkGrey );
					gymDetailsContainer.setVisibility ( View.GONE );
				}
			}
		} );

		yourLocationCheckBox = ( CheckBox ) view.findViewById ( R.id.yourLocationCheckBox );
		yourLocationDetailsContainer = ( ViewGroup ) view.findViewById ( R.id.yourLocationDetailsContainer );
		yourLocationShirtlessView = ( AddonPriceSelectorView ) view.findViewById ( R.id.yourLocationShirtlessView );
//		yourLocationCheckBox.setOnClickListener ( new OnClickListener ()
//		{
//			@Override
//			public void onClick ( View v )
//			{
//				boolean isChecked = yourLocationCheckBox.isChecked ();
//				yourLocationShirtlessView.setSelected ( isChecked );
//				if ( isChecked )
//				{
//					yourLocationCheckBox.setTextColor ( darkBlueColor );
//					yourLocationDetailsContainer.setVisibility ( View.VISIBLE );
//				} else
//				{
//					yourLocationCheckBox.setTextColor ( textDarkGrey );
//					yourLocationDetailsContainer.setVisibility ( View.GONE );
//				}
//			}
//		} );
	}
	
	@Override
	protected String postFieldsValidate ()
	{
		if ( gymCheckBox.isChecked () )
		{
			if ( addressNameEditText.getText ().toString ().trim ().isEmpty () )
			{
				return controller.getString ( R.string.setup_gym_address_name );
			}
			if ( addressEditText.getText ().toString ().trim ().isEmpty () )
			{
				return controller.getString ( R.string.setup_gym_address );
			}
		}
		if ( !gymCheckBox.isChecked () && !yourLocationCheckBox.isChecked () )
		{
			return controller.getString ( R.string.you_must_choose_gym_or_other_location );
		}
		return "";
	}
	
	@Override
	protected void saveAddons () throws Exception
	{
		JSONArray jsonArray = new JSONArray ();
		
		JobAddonsAPIObject gymAddon = controller.getAddon ( AddonId.GYM );
		String addressName = addressNameEditText.getText ().toString ().trim ();
		String address = addressEditText.getText ().toString ().trim ();
		AddressAPIObject gymAddressAPIObject = new AddressAPIObject ();
		gymAddressAPIObject.putValue ( AddressParams.DESCRIPTION, addressName );
		gymAddressAPIObject.putValue ( AddressParams.ADDRESS, address );
		controller.getAPIManager ().insert ( gymAddressAPIObject, ServerManager.INSERT_ADDRESS );
		JSONObject gymData = new JSONObject ();
		gymData.accumulate ( "name", addressName );
		gymData.accumulate ( "address", address );
		gymData.accumulate ( "id", gymAddressAPIObject.getId () );
		addAddonInformation ( jsonArray, gymAddon, gymCheckBox.isChecked (), 0, gymData );
		
		JobAddonsAPIObject otherLocationAddon = controller.getAddon ( AddonId.OTHER_LOCATION );
		addAddonInformation ( jsonArray, otherLocationAddon, yourLocationCheckBox.isChecked (), 0, null );
		
//		JobAddonsAPIObject otherLocationShirtlessAddon = controller.getAddon ( AddonId.SHIRTLESS_8 );
//		addAddonInformation ( jsonArray, otherLocationShirtlessAddon, yourLocationShirtlessView.isChecked (), yourLocationShirtlessView.getPrice (), null );
		
		ServerManager.postRequest ( ServerManager.ADDONS_SERVICE_SET, jsonArray );
	}

	private void addAddonInformation ( JSONArray jsonArray, JobAddonsAPIObject addonItem, boolean checked, int price, JSONObject data ) throws Exception
	{
		JSONObject jsonObject = new JSONObject ();
		jsonObject.put ( AddonServiceAPIParams.TYPE_JOB_SERVICE_ID.toString (), job.getId () );
		jsonObject.put ( AddonServiceAPIParams.JOB_ADDONS_ID.toString (), addonItem.getId () );
		jsonObject.put ( "isChecked", checked );
		jsonObject.put ( AddonServiceAPIParams.PRICE.toString (), price );
		if ( data != null )
		{
			jsonObject.put ( AddonServiceAPIParams.DATA.toString (), data.toString () );
		}
		jsonArray.put ( jsonObject );
	}
	
	@Override
	protected void updatePriceAddons ()
	{
		JobAddonsAPIObject gymAddon = controller.getAddon ( AddonId.GYM );
		AddonServiceAPIObject gymAddonService = getAddonService ( gymAddon.getId () );
		if ( gymAddonService != null )
		{
			gymCheckBox.setChecked ( true );
			gymDetailsContainer.setVisibility ( View.VISIBLE );
			try
			{
				JSONObject data = new JSONObject ( gymAddonService.getString ( AddonServiceAPIParams.DATA ) );
				addressNameEditText.setText ( data.getString ( "name" ) );
				addressEditText.setText ( data.getString ( "address" ) );
			} catch ( Exception ex )
			{
				ex.printStackTrace ();
			}
		}
		
		JobAddonsAPIObject otherLocationAddon = controller.getAddon ( AddonId.OTHER_LOCATION );
		AddonServiceAPIObject otherLocationAddonService = getAddonService ( otherLocationAddon.getId () );
		yourLocationCheckBox.setChecked ( otherLocationAddonService == null ? false : true );
//		yourLocationDetailsContainer.setVisibility ( otherLocationAddonService == null ? View.GONE : View.VISIBLE );
		
//		JobAddonsAPIObject otherLocationShirtlessAddon = controller.getAddon ( AddonId.SHIRTLESS_8 );
//		AddonServiceAPIObject otherLocationShirtlessAddonService = getAddonService ( otherLocationShirtlessAddon.getId () );
//		yourLocationShirtlessView.updateComponents ( otherLocationShirtlessAddonService );
	}
}
