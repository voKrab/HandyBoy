package com.vallverk.handyboy.model.api;

import java.io.Serializable;

import org.json.JSONObject;

public class ReviewAPIObject extends APIObject implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum ReviewParams
	{
		OWNER_ID ( "ownerId" ), REVIEWER_ID ( "reviewerId" ), RATING ( "rating" ), CONTENT ( "content" ), TIME ( "time" );

		String name;

		ReviewParams ( String name )
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

		public static ReviewParams fromString ( String string )
		{
			for ( ReviewParams category : ReviewParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}
	}

	public ReviewAPIObject ()
	{
		super ();
	}

	public ReviewAPIObject ( JSONObject jsonObject ) throws Exception
	{
		update ( jsonObject );
	}

	@Override
	public Object[] getParams ()
	{
		return ReviewParams.values ();
	}
}
