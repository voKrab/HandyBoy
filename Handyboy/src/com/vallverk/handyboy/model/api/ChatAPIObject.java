package com.vallverk.handyboy.model.api;

import org.json.JSONObject;

import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;

public class ChatAPIObject extends APIObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum ChatParams
	{
		USER_ID_FIRST ( "userIdFirst" ), 
		USER_ID_SECOND ( "userIdSecond" );
					
		String name;
		
		ChatParams ( String name )
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

		public static ChatParams fromString ( String string )
		{
			for ( ChatParams category : ChatParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}		
	}
	
	public ChatAPIObject () throws Exception
	{
	}
	
	public ChatAPIObject ( JSONObject jsonItem ) throws Exception
	{
		update ( jsonItem );
	}
	
	@Override
	public Object[] getParams ()
	{
		return ChatParams.values ();
	}
}
