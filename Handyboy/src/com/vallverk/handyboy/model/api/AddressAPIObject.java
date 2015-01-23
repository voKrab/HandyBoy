package com.vallverk.handyboy.model.api;

import org.json.JSONObject;

import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;

public class AddressAPIObject extends APIObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
