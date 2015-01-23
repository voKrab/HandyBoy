package com.vallverk.handyboy.pubnub;

import org.json.JSONException;

import org.json.JSONObject;

import com.vallverk.handyboy.pubnub.PubnubManager.ActionType;

public class NotificationAction extends PubNubBaseAction
{
	private String end;
	private String pref;
	private JSONObject imageJSONObject;
	private String reciverName;
	private String targetId;
	
	public NotificationAction ( String senderId, String reciverId, ActionType actionType, String end, String pref, JSONObject imageJSONObject, String reciverName )
	{
		this ( senderId, reciverId, actionType, end, pref, imageJSONObject, reciverName, "" );
	}
	
	public NotificationAction ( String senderId, String reciverId, ActionType actionType, String end, String pref, JSONObject imageJSONObject, String reciverName, String targetId )
	{
		super ( senderId, reciverId, actionType );

		this.end = end;
		this.pref = pref;
		this.imageJSONObject = imageJSONObject;
		this.reciverName = reciverName;
		this.targetId = targetId;
	}

	public NotificationAction ( JSONObject json ) throws JSONException
	{
		this ( json.getString ( "senderId" ), json.getString ( "reciverId" ), ActionType.fromString ( "" + json.getString ( "type" ) ), json.getString ( "end" ), json.getString ( "pref" ), null, "", json.getString ( "targetId" ) );
	}

	public String getEnd ()
	{
		return end;
	}

	public void setEnd ( String end )
	{
		this.end = end;
	}

	public String getPref ()
	{
		return pref;
	}

	public void setPref ( String pref )
	{
		this.pref = pref;
	}
	
	public JSONObject toJSON () throws JSONException
	{
		JSONObject json = new JSONObject ();
		json.put ( "reciverId", reciverId );
		json.put ( "senderId", senderId );
		json.put ( "type", actionType.toInt () );
		json.put ( "end", end );
		json.put ( "pref", pref );
		json.put ( "entityName", "ActionNotification" ); //for ios
		json.put ( "reciverName", reciverName ); 
		if ( imageJSONObject != null )
		{
			json.put ( "avatar", imageJSONObject );
		}
		json.put ( "targetId", targetId ); 
		return json;
	}
	
	public String toString ()
	{
//		switch ( actionType )
//		{
//			case COMMENT:
//			{
//				return getPref () + " оставил комментарий по товару " + getEnd ();
//			}
//			case LIKE:
//			{
//				return getPref () + " понравился ваш товар " + getEnd ();
//			}
//			case ORDER:
//			{
//				return getPref () + " купил ваш товар " + getEnd ();
//			}
//			case ORDER_STATUS:
//			{
//				//return getPref () + " изменил статус на " + OrderStatus.values ()[Integer.parseInt ( getEnd () )].toCharacterString ();
//			}
//		}
		return "NOT SUPPORT NOTIFICATION";
	}

	public String getTargetId ()
	{
		return targetId;
	}

	public void setTargetId ( String targetId )
	{
		this.targetId = targetId;
	}
}
