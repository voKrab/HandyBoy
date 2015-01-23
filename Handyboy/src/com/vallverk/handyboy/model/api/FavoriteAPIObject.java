package com.vallverk.handyboy.model.api;

import org.json.JSONObject;

import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;

public class FavoriteAPIObject extends APIObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum FavoriteParams
	{
		LAST_NAME ( "lastName" ),
		FIRST_NAME ( "firstName" ),
		SERVICE_ID ( "serviceId" ),
		LATITUDE("latitude"),
		LONGITUDE("longitude"),
		LOGO("logo");
					
		String name;
		
		FavoriteParams ( String name )
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

		public static FavoriteParams fromString ( String string )
		{
			for ( FavoriteParams category : FavoriteParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}		
	}
	
	public FavoriteAPIObject () throws Exception
	{
	}
	
	public FavoriteAPIObject ( JSONObject jsonItem ) throws Exception
	{
		update ( jsonItem );
	}
	
	@Override
	public Object[] getParams ()
	{
		return FavoriteParams.values ();
	}
}
