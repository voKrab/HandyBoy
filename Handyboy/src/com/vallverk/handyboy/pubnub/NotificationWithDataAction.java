package com.vallverk.handyboy.pubnub;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.vallverk.handyboy.pubnub.PubnubManager.ActionType;

public class NotificationWithDataAction extends PubNubBaseAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map < Object, Object > data;

	public NotificationWithDataAction ( String senderId, String reciverId, ActionType actionType, Map < Object, Object > data )
	{
		super ( senderId, reciverId, actionType );

		this.data = data;
	}

	public NotificationWithDataAction ( JSONObject json ) throws JSONException
	{
		super ( json.getString ( "senderId" ), json.getString ( "reciverId" ), ActionType.fromString ( "" + json.getString ( "type" ) ) );

		JSONObject dataJSON = json.getJSONObject ( "data" );
		data = new HashMap < Object, Object > ();
		Iterator < String > keys = dataJSON.keys ();
		while ( keys.hasNext () )
		{
			String key = keys.next ();
			Object value = dataJSON.get ( key );
			data.put ( key, value );
		}
	}

	public JSONObject toJSON () throws JSONException
	{
		JSONObject json = new JSONObject ();
		json.put ( "reciverId", reciverId );
		json.put ( "senderId", senderId );
		json.put ( "type", actionType.toInt () );
		JSONObject dataJSON = new JSONObject ();
		for ( Object key : data.keySet () )
		{
			dataJSON.put ( key.toString (), data.get ( key ) );
		}
		json.put ( "data", dataJSON );
		return json;
	}
	
	public Object getValue ( Object key )
	{
		return data.get ( key.toString () );
	}
	
	public String getString ( Object key )
	{
		return ( String ) getValue ( key );
	}
}
