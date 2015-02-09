package com.vallverk.handyboy.model.api;

import org.json.JSONObject;

public class MyMoneyAPIObject extends APIObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum MyMoneyParams
	{
		BOOKING_ID ( "bookingId" ), CREATED_AT ( "createdAt" ), USER_ID ( "userId" ), AMOUNT ( "amount" ), REASON ( "reason" ), CARD_ID ( "cardId" ), TOTAL_HOURS("totalHours");

		String name;

		MyMoneyParams ( String name )
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

		public static MyMoneyParams fromString ( String string )
		{
			for ( MyMoneyParams category : MyMoneyParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}
	}

	public MyMoneyAPIObject () throws Exception
	{
	}

	public MyMoneyAPIObject ( JSONObject jsonItem ) throws Exception
	{
		update ( jsonItem );
	}

	@Override
	public Object[] getParams ()
	{
		return MyMoneyParams.values ();
	}
}
