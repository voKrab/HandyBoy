package com.vallverk.handyboy.model;

import java.io.Serializable;

import com.vallverk.handyboy.MainActivity.ApplicationAction;

/**
 * 
 * @author Oleg Barkov
 *
 * The class ChatData. Storing data of one chat of user to user
 *
 */
public class ChatData implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String id;
	private String firstUserId;
	private String secondUserId;
	private int amountUnreadMessages;
	private ChatMessageData lastMessage;
	private long createdAt;
	
	public ChatData ( String id, String firstUserId, String secondUserId, ChatMessageData lastMessage )
	{
		this.id = id;
		this.firstUserId = firstUserId;
		this.secondUserId = secondUserId;
		this.lastMessage = lastMessage;
		this.createdAt = System.currentTimeMillis ();
	}
	
	public String getId ()
	{
		return id;
	}
	public void setId ( String id )
	{
		this.id = id;
	}
	public String getFirstUserId ()
	{
		return firstUserId;
	}
	public void setFirstUserId ( String firstUserId )
	{
		this.firstUserId = firstUserId;
	}
	public String getSecondUserId ()
	{
		return secondUserId;
	}
	public void setSecondUserId ( String secondUserId )
	{
		this.secondUserId = secondUserId;
	}
	public int getAmountUnreadMessages ()
	{
		return amountUnreadMessages;
	}
	public void setAmountUnreadMessages ( int amountUnreadMessages )
	{
		if ( this.amountUnreadMessages != amountUnreadMessages )
		{
			this.amountUnreadMessages = amountUnreadMessages;
			CommunicationManager.getInstance ().fire ( ApplicationAction.AMOUNT_UNREAD_MESSAGES, this.amountUnreadMessages );
		}
	}
	public ChatMessageData getLastMessage ()
	{
		return lastMessage;
	}
	public void setLastMessage ( ChatMessageData lastMessage )
	{
		this.lastMessage = lastMessage;
	}

	public long getCreatedAt ()
	{
		return createdAt;
	}

	public void setCreatedAt ( long createdAt )
	{
		this.createdAt = createdAt;
	}

	public void addMessage ( ChatMessageData messageData )
	{
		setLastMessage ( messageData );
		setAmountUnreadMessages ( this.amountUnreadMessages + 1 );
	}

	public void chatViewed ()
	{
		setAmountUnreadMessages ( 0 );
	}
}
