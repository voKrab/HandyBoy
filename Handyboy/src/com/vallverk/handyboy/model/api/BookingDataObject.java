package com.vallverk.handyboy.model.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.api.BookingAPIObject.BookingAPIParams;

public class BookingDataObject implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected UserAPIObject service;
	protected UserAPIObject cutomer;
	protected TypeJobServiceAPIObject typeJobAPIObject;
	protected BookingAPIObject bookingAPIObject;
	protected AddressAPIObject address;
	protected List < JobAddonDetailsObject > addonDetailsObjects;

	public class JobAddonDetailsObject implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public JobAddonsAPIObject addonsAPIObject;
		public AddonServiceAPIObject addonServiceAPIObject;
	}

	public BookingDataObject ( UserAPIObject service, TypeJobServiceAPIObject typeJobAPIObject, BookingAPIObject bookingAPIObject, List < JobAddonDetailsObject > addonsAPIObjects )
	{
		this.service = service;
		this.typeJobAPIObject = typeJobAPIObject;
		this.bookingAPIObject = bookingAPIObject;
		this.addonDetailsObjects = addonsAPIObjects;
	}

	public BookingDataObject ( JSONObject jsonObject ) throws Exception
	{
		service = new UserAPIObject ( jsonObject.getJSONObject ( "service" ) );
		cutomer = new UserAPIObject ( jsonObject.getJSONObject ( "user" ) );
		typeJobAPIObject = new TypeJobServiceAPIObject ( jsonObject.getJSONObject ( "typeJobService" ) );
		bookingAPIObject = new BookingAPIObject ( jsonObject.getJSONObject ( "booking" ) );
		address = new AddressAPIObject ( jsonObject.getJSONObject ( "address" ) );
		addonDetailsObjects = new ArrayList < JobAddonDetailsObject > ();

		JSONArray addonsJsonArray = jsonObject.getJSONArray ( "addons" );
		for ( int i = 0; i < addonsJsonArray.length (); i++ )
		{
			JobAddonDetailsObject addonDetailsObject = new JobAddonDetailsObject ();
			addonDetailsObject.addonsAPIObject = new JobAddonsAPIObject ( addonsJsonArray.getJSONObject ( i ) );
			addonDetailsObject.addonServiceAPIObject = new AddonServiceAPIObject ( addonsJsonArray.getJSONObject ( i ) );
			addonDetailsObjects.add ( addonDetailsObject );
		}
	}

	public UserAPIObject getService ()
	{
		return service;
	}

	public void setService ( UserAPIObject service )
	{
		this.service = service;
	}

	public UserAPIObject getCustomer ()
	{
		return cutomer;
	}

	public void setCustomer ( UserAPIObject user )
	{
		this.cutomer = user;
	}

	public TypeJobServiceAPIObject getTypeJobAPIObject ()
	{
		return typeJobAPIObject;
	}

	public void setTypeJobAPIObject ( TypeJobServiceAPIObject typeJobAPIObject )
	{
		this.typeJobAPIObject = typeJobAPIObject;
	}

	public BookingAPIObject getBookingAPIObject ()
	{
		return bookingAPIObject;
	}

	public void setBookingAPIObject ( BookingAPIObject bookingAPIObject )
	{
		this.bookingAPIObject = bookingAPIObject;
	}

	public List < JobAddonDetailsObject > getAddonsAPIObjects ()
	{
		return addonDetailsObjects;
	}

	public void setAddonsAPIObjects ( List < JobAddonDetailsObject > addonDetailsObjects )
	{
		this.addonDetailsObjects = addonDetailsObjects;
	}

	public AddressAPIObject getAddress ()
	{
		return address;
	}

	public void setAddress ( AddressAPIObject address )
	{
		this.address = address;
	}

	public BookingStatusEnum getSatus ()
	{
		return bookingAPIObject.getStatus ();
	}
	
	public String getId ()
	{
		return bookingAPIObject.getId ();
	}

	public void updateStatus ( BookingStatusEnum newStatus )
	{
		bookingAPIObject.updateStatus ( newStatus );
	}
}
