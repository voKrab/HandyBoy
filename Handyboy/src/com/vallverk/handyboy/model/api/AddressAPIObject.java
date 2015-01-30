package com.vallverk.handyboy.model.api;

import android.location.Address;
import android.location.Geocoder;

import com.vallverk.handyboy.MainActivity;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class AddressAPIObject extends APIObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public Address getGeoPoint () throws Exception
    {
        Geocoder geocoder = new Geocoder ( MainActivity.getInstance(), Locale.getDefault () );
        String addressString = createAddressString ();
        List < Address > addressList = geocoder.getFromLocationName ( addressString, 1 );
        if ( addressList.isEmpty () )
        {
            throw new Exception ( "Location (" + addressString + ") not found" );
        }
        return addressList.get ( 0 );
    }

    private String createAddressString ()
    {
        String address = "";
        address += getString ( AddressParams.CITY ) + " " + getString ( AddressParams.STATE ) + " " + getString ( AddressParams.ZIP_CODE ) + " " + getString ( AddressParams.ADDRESS );
        return address;
    }

    public enum AddressParams
	{
		USER_ID ( "userId" ),
		CITY ( "city" ),
		STATE ( "state" ),
		ZIP_CODE ( "zipCode" ),
		ADDRESS ( "address" ),
		DESCRIPTION ( "description" );

		String name;

		AddressParams ( String name )
		{
			this.name = name;
		}

		public String toString ()
		{
			return name;
		}

		public void setName ( String name )
		{
			this.name = name;
		}

		public static AddressParams fromString ( String string )
		{
			for ( AddressParams category : AddressParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}
	}

	public AddressAPIObject () throws Exception
	{
	}

	public AddressAPIObject ( JSONObject jsonItem ) throws Exception
	{
		update ( jsonItem );
	}

	@Override
	public Object[] getParams ()
	{
		return AddressParams.values ();
	}
}
