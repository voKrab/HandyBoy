package com.vallverk.handyboy.pubnub;

import org.json.JSONException;

import org.json.JSONObject;

import com.vallverk.handyboy.pubnub.PubnubManager.ActionType;

public class PubNubActionFactory
{
	public static PubNubBaseAction createAction ( Object message ) throws JSONException
	{
		JSONObject json = ( JSONObject ) message;
		String actionType = json.getString ( "type" );
		switch ( ActionType.fromString ( actionType ) )
		{
			case BOOKING_STATUS:
			case CHAT:
			{
				return new NotificationWithDataAction ( json );
			}
			default:
			{
				return null;
			}
		}
	}
}
