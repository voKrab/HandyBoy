package com.vallverk.handyboy.model.api;

import org.json.JSONObject;

import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;

public class BlockListAPIObject extends APIObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum BlockListParams
	{
		LAST_NAME ( "lastName" ),
		FIRST_NAME ( "firstName" ),
		USER_ID ( "userId" ),
		LATITUDE("latitude"),
		LONGITUDE("longitude"),
		LOGO("logo");
					
		String name;
		
		BlockListParams ( String name )
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

		public static BlockListParams fromString ( String string )
		{
			for ( BlockListParams category : BlockListParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}		
	}
	
	public BlockListAPIObject () throws Exception
	{
	}
	
	public BlockListAPIObject ( JSONObject jsonItem ) throws Exception
	{
		update ( jsonItem );
	}
	
	@Override
	public Object[] getParams ()
	{
		return BlockListParams.values ();
	}
}
