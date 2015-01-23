package com.vallverk.handyboy.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.model.AddressWraper;
import com.vallverk.handyboy.view.base.AutocompleteAdapter;

public class AddressAutocompleteAdapter extends AutocompleteAdapter
{
	public AddressAutocompleteAdapter ( Activity context, String nameFilter )
	{
		super ( context, nameFilter );
		// TODO Auto-generated constructor stub
	}

	@Override
	protected List getData ( String constraint )
	{
		Geocoder gcd = new Geocoder ( MainActivity.getInstance ().getBaseContext (), Locale.getDefault () );
		List findedData = new ArrayList ();
		try
		{
			List < Address > addresses = gcd.getFromLocationName ( constraint, MAX_RESULTS );
			for ( Address address : addresses )
			{
				AddressWraper addressWraper = new AddressWraper ( address );
				if ( addressWraper.isValid () )
				{
					findedData.add ( addressWraper );
				}
			}
		} catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return findedData;
	}
	
}
