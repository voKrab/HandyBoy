package com.vallverk.handyboy.model.api;

import java.io.Serializable;

import org.json.JSONObject;

public class BookingAddonServiceAPIObject extends APIObject implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum BookingAddonServiceAPIParams
	{
		BOOKING_ID ( "bookingId" ),
		ADDON_SERVICE_ID ( "addonServiceId" );

		String name;

		BookingAddonServiceAPIParams ( String name )
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

		public static BookingAddonServiceAPIParams fromString ( String string )
		{
			for ( BookingAddonServiceAPIParams category : BookingAddonServiceAPIParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}
	}

	public BookingAddonServiceAPIObject ()
	{
		super ();
	}
	
	public BookingAddonServiceAPIObject ( JSONObject jsonObject ) throws Exception
	{
		update ( jsonObject );
	}
	
	@Override
	public Object[] getParams ()
	{
		return BookingAddonServiceAPIParams.values ();
	}
}
