package com.vallverk.handyboy.model.api;

import org.json.JSONObject;

import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;

public class CreditCardAPIObject extends APIObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum CreditCardParams
	{
		CARD_NUMBER ( "number" ), 
		CARD_NAME ( "name" ), 
		EXP_DATE ( "expdate" ), 
		CVV ( "cvv2" ), 
		//ZIP_CODE ( "zip" ),
		USER_ID ("userId");

		String name;

		CreditCardParams ( String name )
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

		public static CreditCardParams fromString ( String string )
		{
			for ( CreditCardParams category : CreditCardParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}
	}

	public CreditCardAPIObject () throws Exception
	{
	}

	public CreditCardAPIObject ( JSONObject jsonItem ) throws Exception
	{
		update ( jsonItem );
	}

	@Override
	public Object[] getParams ()
	{
		return CreditCardParams.values ();
	}
}
