package com.vallverk.handyboy.model.api;

import org.json.JSONObject;

public class DeviceTokenAPIObject extends APIObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum DeviceTokenParams
	{
		USER_ID ( "user_id" ), 
		TOKEN ( "token" ),
		OS ( "os" );
					
		String name;
		
		DeviceTokenParams ( String name )
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

		public static DeviceTokenParams fromString ( String string )
		{
			for ( DeviceTokenParams category : DeviceTokenParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}		
	}
	
	public DeviceTokenAPIObject () throws Exception
	{
	}
	
	public DeviceTokenAPIObject ( JSONObject jsonItem ) throws Exception
	{
		update ( jsonItem );
	}
	
	public DeviceTokenAPIObject ( String userId, String token, String os )
	{
		putValue ( DeviceTokenParams.USER_ID, userId );
		putValue ( DeviceTokenParams.TOKEN, token );
		putValue ( DeviceTokenParams.OS, os );
	}

	@Override
	public Object[] getParams ()
	{
		return DeviceTokenParams.values ();
	}
}
