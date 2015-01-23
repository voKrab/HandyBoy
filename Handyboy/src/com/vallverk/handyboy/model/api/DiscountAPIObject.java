package com.vallverk.handyboy.model.api;

import org.json.JSONObject;

public class DiscountAPIObject extends APIObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum DiscountParams
	{
		SERVICE_ID ( "serviceId" ), 
		STARTED_AT ( "startedAt" ), 
		DISCOUNT ( "discount" );
		
		String name;

		DiscountParams ( String name )
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

		public static DiscountParams fromString ( String string )
		{
			for ( DiscountParams category : DiscountParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}
	}

	public DiscountAPIObject () throws Exception
	{
	}

	public DiscountAPIObject ( JSONObject jsonItem ) throws Exception
	{
		update ( jsonItem );
	}

	@Override
	public Object[] getParams ()
	{
		return DiscountParams.values ();
	}
}
