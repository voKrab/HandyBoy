package com.vallverk.handyboy.model.api;

import java.io.Serializable;

import org.json.JSONObject;

import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.UserStatus;
import com.vallverk.handyboy.server.ServerManager;

public class UserAPIObject extends APIObject implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum UserParams
	{
		LATITUDE ( "latitude" ), 
		LONGITUDE ( "longitude" ), 
		ID ( "id" ), 
		FIRST_NAME ( "firstName" ),
		LAST_NAME ( "lastName" ),
		AVATAR ( "logo" ), 
		FACEBOOK_UID ( "facebookUid" ), 
		PASSWORD ( "password" ),
		EMAIL ( "email" ),
		PHONE_NUMBER ( "phoneNumber" ),
		DOB ( "dob" ),
		STATUS ( "status" ),
		SERVICE_ID ( "serviceId" ),
		ROLE ( "role" ),
		COMMENT ( "comment" ),
		GCM_ID ( "gcmId" );

		String name;

		UserParams ( String name )
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

		public static UserParams fromString ( String string )
		{
			for ( UserParams category : UserParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}
	}

	private String sessionId;

	public UserAPIObject ()
	{
		super ();
	}

	public UserAPIObject ( String password, String email )
	{
		super ();

		putValue ( UserParams.PASSWORD, password );
		putValue ( UserParams.EMAIL, email );
	}

	public UserAPIObject ( JSONObject jsonObject ) throws Exception
	{
		update ( jsonObject );
	}

	public String getSessionId ()
	{
		return sessionId;
	}

	public void setSessionId ( String sessionId )
	{
		this.sessionId = sessionId;
	}

	public String login () throws Exception
	{
		JSONObject jsonObject = new JSONObject ();
		jsonObject.accumulate ( UserParams.EMAIL.toString (), getValue ( UserParams.EMAIL ) );
		jsonObject.accumulate ( UserParams.PASSWORD.toString (), getValue ( UserParams.PASSWORD ) );
		final String responseText = ServerManager.postRequest ( ServerManager.USER_AUTH_URI, jsonObject );
		JSONObject responseObject = new JSONObject ( responseText );
		String resultStatus = responseObject.getString ( "parameters" );
		if ( resultStatus.isEmpty () )
		{
			updateFromString ( responseObject.getString ( "object" ) );
			setSessionId ( responseObject.getString ( "session_id" ) );
			// save ();
		}
		return resultStatus;
	}

	public String loginWithFacebook () throws Exception
	{
		JSONObject jsonObject = new JSONObject ();
		jsonObject.accumulate ( UserParams.FACEBOOK_UID.toString (), getValue ( UserParams.FACEBOOK_UID ) );
		final String responseText = ServerManager.postRequest ( ServerManager.USER_FACEBOOK_AUTH_URI, jsonObject );
		JSONObject responseObject = new JSONObject ( responseText );
		String resultStatus = responseObject.getString ( "parameters" );
		if ( resultStatus.isEmpty () )
		{
			updateFromString ( responseObject.getString ( "object" ) );
			setSessionId ( responseObject.getString ( "session_id" ) );
			// save ();
		}
		return resultStatus;
	}

	public void updateFromString ( String dataString ) throws Exception
	{
		if ( dataString.startsWith ( "[" ) )
		{
			dataString = dataString.substring ( 1, dataString.length () - 1 );
		}
		JSONObject dataObject = new JSONObject ( dataString );
		update ( dataObject );
	}

	public String registration () throws Exception
	{
		JSONObject jsonObject = new JSONObject ();
		jsonObject.accumulate ( UserParams.FIRST_NAME.toString (), getValue ( UserParams.FIRST_NAME ) );
		jsonObject.accumulate ( UserParams.LAST_NAME.toString (), getValue ( UserParams.LAST_NAME ) );
		jsonObject.accumulate ( UserParams.AVATAR.toString (), "" );
		jsonObject.accumulate ( UserParams.PASSWORD.toString (), getValue ( UserParams.PASSWORD ) );
		jsonObject.accumulate ( UserParams.EMAIL.toString (), getValue ( UserParams.EMAIL ) );
		jsonObject.accumulate ( UserParams.PHONE_NUMBER.toString (), getValue ( UserParams.PHONE_NUMBER ) );
		jsonObject.accumulate ( UserParams.DOB.toString (), (( Long ) getValue ( UserParams.DOB )) / 1000 );
		jsonObject.accumulate ( UserParams.STATUS.toString (), getString ( UserParams.STATUS ) );
		String facebookId = ( String ) getValue ( UserParams.FACEBOOK_UID );
		if ( facebookId != null && !facebookId.isEmpty () )
		{
			jsonObject.accumulate ( "facebookUid", facebookId );
		}
		final String responseText = ServerManager.postRequest ( ServerManager.USER_REGISTRATION_URI, jsonObject );
		JSONObject responseObject = new JSONObject ( responseText );
		String resultStatus = responseObject.getString ( "parameters" );
		if ( resultStatus.isEmpty () )
		{
			// save ();
			setSessionId ( responseObject.getString ( "session_id" ) );
			setId ( responseObject.getString ( "userId" ) );
		}
		return resultStatus;
	}

	public String update () throws Exception
	{
		String responseText = update ( ServerManager.USER_UPDATE_URI );
		JSONObject responseObject = new JSONObject ( responseText );
		String resultStatus = responseObject.getString ( "parameters" );
		return resultStatus;
	}

	public boolean isService ()
	{
		return getValue ( UserParams.STATUS ).toString ().equals ( UserStatus.NEW_SERVICE.toString () );
	}

	public boolean isCustomer ()
	{
		return !isService ();
	}

	public String getLocationText ()
	{
		String location = "";
		try
		{
			double latitude = Double.parseDouble ( getString ( UserParams.LATITUDE ) );
			double longitude = Double.parseDouble ( getString ( UserParams.LONGITUDE ) );
			location = Tools.getLocationText ( latitude, longitude );
		} catch ( Exception ex )
		{
			ex.printStackTrace ();
		}
		return location;
	}

	@Override
	public Object[] getParams ()
	{
		return UserParams.values ();
	}

	public String getShortName ()
	{
		return getString ( UserParams.FIRST_NAME ) + " " + getString ( UserParams.LAST_NAME ).charAt ( 0 );
	}
}
