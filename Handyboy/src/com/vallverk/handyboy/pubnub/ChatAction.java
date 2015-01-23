package com.vallverk.handyboy.pubnub;

import org.json.JSONException;

import org.json.JSONObject;

import com.vallverk.handyboy.pubnub.PubnubManager.ActionType;

public class ChatAction extends PubNubBaseAction
{
	private static final long serialVersionUID = 1L;
	
	private String chatId;
	private String content;
	private JSONObject imageJSONObject;
	
	public ChatAction ( String senderId, String reciverId, ActionType actionType, String chatId, String content, JSONObject imageJSONObject )
	{
		super ( senderId, reciverId, actionType );

		this.chatId = chatId;
		this.content = content;
		this.imageJSONObject = imageJSONObject;
	}
	
	public ChatAction ( JSONObject json ) throws JSONException
	{
		this ( json.getString ( "senderId" ), json.getString ( "reciverId" ), ActionType.fromString ( "" + json.getString ( "type" ) ), json.getString ( "chatId" ), json.getString ( "content" ), json.isNull ( "image" ) ? null : json.getJSONObject ( "image" ) );
	}

	public String getChatId ()
	{
		return chatId;
	}
	
	public void setChatId ( String chatId )
	{
		this.chatId = chatId;
	}
	public String getContent ()
	{
		return content;
	}
	public void setContent ( String content )
	{
		this.content = content;
	}

	public JSONObject toJSON () throws JSONException
	{
		JSONObject json = new JSONObject ();
		json.put ( "reciverId", reciverId );
		json.put ( "chatId", chatId );
		json.put ( "content", content );
		json.put ( "type", actionType.toInt () );
		json.put ( "senderId", senderId );
		if ( imageJSONObject != null )
		{
			json.put ( "image", imageJSONObject );
		}
		System.out.println ( json.toString () );
		return json;
	}

	public JSONObject getImageJSONObject ()
	{
		return imageJSONObject;
	}

	public void setImageJSONObject ( JSONObject imageJSONObject )
	{
		this.imageJSONObject = imageJSONObject;
	}
	
	public String toString ()
	{
		return "Вам пришло сообщение: " + content;
	}
}
