package com.vallverk.handyboy.model;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.os.Message;

/**
 * 
 * @author Oleg Barkov
 * 
 * Using for performing and handling events
 * 
 */
public class CommunicationManager
{	
	public enum CommunicationAction
	{
		ADD_EDIT_PRODUCT, SHOW_LOADER, HIDE_LOADER, USE_STICKER, BOOKING_STATUS
	}
	
	private static CommunicationManager instance;
	private Map < String, List < Handler > > listeners;
	
	public CommunicationManager ()
	{
		listeners = new HashMap < String, List < Handler > > ();
	}
	
	public static CommunicationManager getInstance ()
	{
		if ( instance == null )
		{
			instance = new CommunicationManager ();
		}
		return instance;
	}
	
	public void addListener ( Object actionType, Handler handler )
	{
		List < Handler > handlers = listeners.get ( actionType.toString () );
		if ( handlers == null )
		{
			handlers = new ArrayList < Handler > ();
		}
		handlers.add ( handler );
		listeners.put ( actionType.toString (), handlers );
	}
	
	public void fire ( Object actionType, Object value )
	{
		List < Handler > handlers = listeners.get ( actionType.toString () );
		if ( handlers != null )
		{
			for ( Handler handler : handlers )
			{
				Message message = handler.obtainMessage ( 0, value );
				handler.dispatchMessage ( message );
			}
		}
	}

	public void removeListener ( Object actionType, Handler handler )
	{
		List < Handler > handlers = listeners.get ( actionType.toString () );
		if ( handlers != null )
		{
			handlers.remove ( handler );
			listeners.put ( actionType.toString (), handlers );
		}
	}

	public void clear ()
	{
		listeners.clear ();
		instance = null;
	}
}
