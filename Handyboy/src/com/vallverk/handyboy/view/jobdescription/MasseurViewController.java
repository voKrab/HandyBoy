package com.vallverk.handyboy.view.jobdescription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject.AddonServiceAPIParams;
import com.vallverk.handyboy.model.api.JobAddonsAPIObject;
import com.vallverk.handyboy.model.api.JobAddonsAPIObject.JobAddonsAPIParams;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.job.AddonId;
import com.vallverk.handyboy.model.job.TypeJob;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.MultiChoiceSpinner;

public class MasseurViewController extends BaseController
{
	private AddonPriceViewBase shirtlessView;
	private MultiChoiceSpinner massageTypesSpinner;
	private List < String > messageTypes;
	private ArrayList < JobAddonsAPIObject > messageTypeAddons;

	public MasseurViewController ( TypeJobServiceAPIObject job, TypeJob typeJob )
	{
		super ( job, typeJob );
	}

	@Override
	public View createView ( LayoutInflater inflater, final JobDescriptionViewFragment fragment )
	{
		this.fragment = fragment;
		final View view = inflater.inflate ( R.layout.job_description_masseur_layout, null );
		setupAddons ( view );
		createView ( view );
		return view;
	}

	private void setupAddons ( View view )
	{
		shirtlessView = ( AddonPriceViewBase ) view.findViewById ( R.id.shirtlessView );
		shirtlessView.setTag ( controller.getAddon ( AddonId.SHIRTLESS_1 ) );
        shirtlessView.updateAddonsPrices(controller.getAddon ( AddonId.SHIRTLESS_1 ));
		shirtlessView.setVisibility ( View.GONE );

		massageTypesSpinner = ( MultiChoiceSpinner ) view.findViewById ( R.id.massageTypesSpinner );
		messageTypes = Arrays.asList ( controller.getResources ().getStringArray ( R.array.massage_types ) );
		messageTypeAddons = new ArrayList < JobAddonsAPIObject > ();
		for ( String messageType : messageTypes )
		{
			messageTypeAddons.add ( controller.getAddon ( messageType, "" + typejob.getId () ) );
		}
		massageTypesSpinner.setItems ( messageTypes, controller.getString ( R.string.massage_types ), controller.getString ( R.string.all ) );

	}

	@Override
	protected String postFieldsValidate ()
	{
		List < Integer > indexes = massageTypesSpinner.getSelectedIndexes ();
		if ( indexes.isEmpty () )
		{
			return controller.getString ( R.string.choose_message_type );
		}
		return "";
	}

	@Override
	protected void saveAddons () throws Exception
	{
		List < Integer > indexes = massageTypesSpinner.getSelectedIndexes ();
		JSONArray jsonArray = new JSONArray ();
		for ( int index = 0; index < messageTypeAddons.size (); index++ )
		{
			JSONObject jsonObject = new JSONObject ();
			JobAddonsAPIObject addonItem = messageTypeAddons.get ( index );
			jsonObject.put ( AddonServiceAPIParams.TYPE_JOB_SERVICE_ID.toString (), job.getId () );
			jsonObject.put ( AddonServiceAPIParams.JOB_ADDONS_ID.toString (), addonItem.getId () );
			jsonObject.put ( "isChecked", indexes.contains ( index ) );
			jsonObject.put ( AddonServiceAPIParams.PRICE.toString (), 0 );
			jsonArray.put ( jsonObject );
		}
		ServerManager.postRequest ( ServerManager.ADDONS_SERVICE_SET, jsonArray );
	}

	@Override
	protected void updatePriceAddons ()
	{
		String selection = "";
		for ( int index = 0; index < messageTypeAddons.size (); index++ )
		{
			JobAddonsAPIObject addon = messageTypeAddons.get ( index );
			AddonServiceAPIObject addonService = getAddonService ( addon.getId () );
			if ( addonService != null )
			{
				selection += addon.getString ( JobAddonsAPIParams.NAME ) + ",";
			}
		}
		if ( selection.isEmpty () )
		{
			return;
		}
		selection = selection.substring ( 0, selection.length () - 1 );
		massageTypesSpinner.setSelection ( selection );
	}
}
