package com.vallverk.handyboy.model.api;

import com.vallverk.handyboy.server.ServerManager;

import org.json.JSONObject;

import java.io.Serializable;

public class UserDetailsAPIObject extends APIObject implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum UserDetailsParams
	{
		HEIGHT ( "height" ), WEIGHT ( "weight" ), BODY_TYPE ( "bodyType" ), HEIR_COLOR ( "heirColor" ), EYE_COLOR ( "eyeColor" ), ETHNICITY ( "ethnicity" ), RATING ( "rating" ), SEX ( "sex" ), RELIABILITY ( "reliability" ), DESCRIPTION ( "description" );

		String name;

		UserDetailsParams ( String name )
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

		public static UserDetailsParams fromString ( String string )
		{
			for ( UserDetailsParams category : UserDetailsParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}
	}

	public UserDetailsAPIObject ()
	{
		super ();
		
		putValue ( UserDetailsParams.HEIGHT, "" );
		putValue ( UserDetailsParams.WEIGHT, "" );
		putValue ( UserDetailsParams.HEIR_COLOR, "" );
		putValue ( UserDetailsParams.EYE_COLOR, "" );
		putValue ( UserDetailsParams.BODY_TYPE, "" );
		putValue ( UserDetailsParams.SEX, "" );
		putValue ( UserDetailsParams.ETHNICITY, "" );
//		putValue ( UserDetailsParams.LATITUDE, "" );
//		putValue ( UserDetailsParams.LONGITUDE, "" );
//		putValue ( UserDetailsParams.TYPE_JOB, "0" );
//		putValue ( UserDetailsParams.PRICE, "0" );
	}

	public void fetch () throws Exception
	{
		String uri = ServerManager.USER_DETAILS_FETCH_URI.replaceAll ( "id=1", "id=" + id );
		fetch ( uri );
	}
	
	@Override
	public Object[] getParams ()
	{
		return UserDetailsParams.values ();
	}

    public UserDetailsAPIObject ( JSONObject jsonObject ) throws Exception
    {
        update ( jsonObject );
    }
}
