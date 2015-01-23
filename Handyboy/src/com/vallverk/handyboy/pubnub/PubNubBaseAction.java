package com.vallverk.handyboy.pubnub;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.vallverk.handyboy.pubnub.PubnubManager.ActionType;

public class PubNubBaseAction implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected String senderId;
	protected String reciverId;
	protected ActionType actionType;
	protected long createdAt;
	
	public PubNubBaseAction ( String senderId, String reciverId, ActionType actionType )
	{
		this.senderId = senderId;
		this.reciverId = reciverId;
		this.actionType = actionType;
		this.createdAt = System.currentTimeMillis ();
	}
	
	public String getSenderId ()
	{
		return senderId;
	}

	public void setSenderId ( String senderId )
	{
		this.senderId = senderId;
	}

	public String getReciverId ()
	{
		return reciverId;
	}

	public void setReciverId ( String reciverId )
	{
		this.reciverId = reciverId;
	}

	public ActionType getActionType ()
	{
		return actionType;
	}

	public void setActionType ( ActionType actionType )
	{
		this.actionType = actionType;
	}

	public JSONObject toJSON () throws JSONException
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public long getCreatedAt ()
	{
		return createdAt;
	}
}
