package com.vallverk.handyboy.model;

import android.util.Log;

import com.vallverk.handyboy.FileManager;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.ChatAPIObject;
import com.vallverk.handyboy.model.api.ChatAPIObject.ChatParams;
import com.vallverk.handyboy.model.api.ChatMessageAPIObject.ChatMessageParams;
import com.vallverk.handyboy.pubnub.NotificationWithDataAction;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Oleg Barkov
 * 
 * Controls of all data/events related with chat.
 * 
 */
public class ChatManager implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static ChatManager instance;

	public static ChatManager getInstance ()
	{
		if ( instance == null )
		{
			instance = ( ChatManager ) FileManager.loadObject ( MainActivity.getInstance (), FileManager.CHAT_MANAGER_SAVE_PATH, new ChatManager () );
		}
		return instance;
	}

	public ChatManager ()
	{
		chats = new HashMap < String, ChatData > ();
	}

	protected Map < String, ChatData > chats;

	public void newMessage ( NotificationWithDataAction action )
	{
		ChatMessageData messageData = new ChatMessageData ( action.getSenderId (), action.getString ( "senderName" ), action.getString ( "senderAvatar" ), action.getCreatedAt (), action.getString ( ChatMessageParams.MESSAGE ) );
		String chatId = action.getString ( ChatMessageParams.CHAT_OBJECT_ID );
		ChatData chatData = chats.get ( chatId );
		if ( chatData == null )
		{
			chatData = new ChatData ( chatId, action.getSenderId (), action.getReciverId (), messageData );
			chats.put ( chatId, chatData );
		} else
		{
			chatData.addMessage ( messageData );
		}
	}
	
	public void newMessage ( JSONObject json ) throws Exception
	{
        Log.d("CHAT", "newMessage" + json.toString());
        JSONObject chatObject = json.getJSONObject("chatObject");
		ChatMessageData messageData = new ChatMessageData ( chatObject.getString ( "senderId" ), chatObject.getString ( "senderName" ), ""/*sender avatar*/, chatObject.getLong ( "createdAt" ), chatObject.has ( "message" ) ? chatObject.getString ( ChatMessageParams.MESSAGE.toString () ) : "" );
		String chatId = chatObject.getString ( ChatMessageParams.CHAT_OBJECT_ID.toString () );
		ChatData chatData = chats.get ( chatId );
		if ( chatData == null )
		{
			chatData = new ChatData ( chatId, chatObject.getString ( "senderId" ),  APIManager.getInstance().getUser().getId().toString(), messageData );
			chats.put ( chatId, chatData );
		} else
		{
			chatData.addMessage ( messageData );
		}
	}

	public void save ()
	{
		FileManager.saveObject ( MainActivity.getInstance (), FileManager.CHAT_MANAGER_SAVE_PATH, this );
	}

	public void chatViewed ( String chatId )
	{
		ChatData chatData = chats.get ( chatId );
		if ( chatData != null )
		{
			chatData.chatViewed ();
		}
	}

	/**
	 * create ChatData from ChatAPIObject
	 */
	public void update ( ChatAPIObject chatObject, JSONObject item ) throws Exception
	{
		ChatMessageData messageData = new ChatMessageData ( item.getString ( "companionId" ), item.getString ( "firstName" ) + " " + item.getString ( "lastName" ), item.getString ( "logo" ), item.getLong ( "date" ) * 1000, item.getString ( "message" ) );
		String chatId = chatObject.getId ();
		ChatData chatData = chats.get ( chatId );
		if ( chatData == null )
		{
			chatData = new ChatData ( chatId, chatObject.getString ( ChatParams.USER_ID_FIRST ), chatObject.getString ( ChatParams.USER_ID_SECOND ), messageData );
			chats.put ( chatId, chatData );
		} else
		{
			chatData.setLastMessage ( messageData );
		}
	}

	public ChatData getChat ( String id )
	{
		return chats.get ( id );
	}
	
	public int getAmountUnreadMessages ()
	{
		int amount = 0;
		for ( ChatData chatData : chats.values () )
		{
			amount += chatData.getAmountUnreadMessages ();
		}
		return amount;
	}

    public int getAmountUnreadDialogs ()
    {
        int amount = 0;
        for ( ChatData chatData : chats.values () )
        {
            int value = chatData.getAmountUnreadMessages ();
            if ( value > 0 )
            {
                amount++;
            }
        }
        return amount;
    }
}
