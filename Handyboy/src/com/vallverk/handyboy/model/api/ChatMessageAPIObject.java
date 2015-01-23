package com.vallverk.handyboy.model.api;

import org.json.JSONObject;

import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;

import android.graphics.Bitmap;

public class ChatMessageAPIObject extends APIObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum ChatMessageParams
	{
		CHAT_OBJECT_ID ( "chatObjectId" ), 
		SENDER_ID ( "senderId" ), 
		MESSAGE ( "message" ),
		IMAGE_URL ( "imageUrl" ),
		DATE ( "date" );
			
		String name;
		
		ChatMessageParams ( String name )
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

		public static ChatMessageParams fromString ( String string )
		{
			for ( ChatMessageParams category : ChatMessageParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}		
	}
	
	private transient Bitmap attach;
	
	public ChatMessageAPIObject ( JSONObject jsonItem ) throws Exception
	{
		update ( jsonItem );
	}

	public ChatMessageAPIObject ()
	{
		// TODO Auto-generated constructor stub
	}

	public Bitmap getAttach ()
	{
		return attach;
	}

	public void setAttach ( Bitmap attach )
	{
		this.attach = attach;
	}
	
	@Override
	public Object[] getParams ()
	{
		return ChatMessageParams.values ();
	}
}
