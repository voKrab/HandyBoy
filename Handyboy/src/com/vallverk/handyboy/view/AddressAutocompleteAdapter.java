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
	private static final int START_AFTER  = 5;
	public AddressAutocompleteAdapter ( Activity context, String nameFilter )
	{
		super ( context, nameFilter );
		// TODO Auto-generated constructor stub
	}

	@Override
	protected List getData ( String constraint )
	{
		List findedData = new ArrayList();
		if(constraint.length() >= 5) {
			Geocoder gcd = new Geocoder(MainActivity.getInstance().getBaseContext(), Locale.getDefault());

			try {
				List<Address> addresses = gcd.getFromLocationName(constraint, MAX_RESULTS);
				for (Address address : addresses) {
					AddressWraper addressWraper = new AddressWraper(address);
					if (addressWraper.isValid()) {
						findedData.add(addressWraper);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return findedData;
	}
	
}
