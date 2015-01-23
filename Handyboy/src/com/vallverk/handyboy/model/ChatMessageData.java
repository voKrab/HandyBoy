package com.vallverk.handyboy.model;

import java.io.Serializable;

/**
 * 
 * @author Oleg Barkov
 *
 * The class ChatMessageData. Storing data of one chat message
 *
 */
public class ChatMessageData implements Serializable
{
	private String senderName;
	private String senderAvatar;
	private String senderId;
	private long createdAt;
	private String message;
	
	public ChatMessageData ( String senderId, String senderName, String senderAvatar, long createdAt, String message )
	{
		this.senderId = senderId;
		this.senderName = senderName;
		this.senderAvatar = senderAvatar;
		this.createdAt = createdAt;
		this.message = message;
	}
	
	public String getSenderName ()
	{
		return senderName;
	}
	public void setSenderName ( String senderName )
	{
		this.senderName = senderName;
	}
	public String getSenderAvatar ()
	{
		return senderAvatar;
	}
	public void setSenderAvatar ( String senderAvatar )
	{
		this.senderAvatar = senderAvatar;
	}
	public long getCreatedAt ()
	{
		return createdAt;
	}
	public void setCreatedAt ( long createdAt )
	{
		this.createdAt = createdAt;
	}
	public String getMessage ()
	{
		return message;
	}
	public void setMessage ( String message )
	{
		this.message = message;
	}

	public String getSenderId ()
	{
		return senderId;
	}

	public void setSenderId ( String senderId )
	{
		this.senderId = senderId;
	}
}
