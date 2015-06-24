package com.vallverk.handyboy.pubnub;

import android.util.Log;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.vallverk.handyboy.FileManager;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.model.CommunicationManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleg Barkov
 * 
 *         facilitates querying channels for messages and listening on channels
 *         for presence/message events
 */
public class PubnubManager extends Pubnub
{
	public enum ActionType
	{
		BOOKING ( "1" ), REMINDER ( "2" ), AVAILABLE_NOW ( "3" ), EXTRA_MONEY ( "4" ), BOOKING_STATUS ( "5" ), CHAT ( "6" ), BOOKING_ADD_CHARGES_REQUESTED ( "7" ), BOOKING_ADD_CHARGES_ACCEPTED ( "9" ), BOOKING_ADD_CHARGES_DECLINED ( "10" ), DOUBLE_SIGNIN("14");

		String name;

		ActionType ( String name )
		{
			this.name = name;
		}

		public int toInt ()
		{
			return Integer.parseInt ( name );
		}

		public String toString ()
		{
			return name;
		}

		public void setName ( String name )
		{
			this.name = name;
		}

		public static ActionType fromString ( String string )
		{
			for ( ActionType category : ActionType.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}
	}

	public static String PUBLISH_KEY = "pub-c-b7d8d41e-bb56-408a-9113-fe5c7371c26f";
	public static String SUBSCRIBE_KEY = "sub-c-09d9fbf8-2e05-11e4-9294-02ee2ddab7fe";
	public static String SECRET_KEY = "sec-c-YzA2MzE2ODctYjViYS00ZDk0LWEzNmQtMmI1MzNlMmQ1NTVm";

	private static PubnubManager instance;

	public static PubnubManager getInstance ()
	{
		if ( instance == null )
		{
			instance = new PubnubManager ( PUBLISH_KEY, SUBSCRIBE_KEY, SECRET_KEY );
		}
		return instance;
	}

	private String userId;

	public PubnubManager ( String publishKey, String subscribeKey, String secretKey )
	{
		super ( publishKey, subscribeKey, secretKey );
	}

	public void subscribe ( String userId ) throws Exception
	{
		subscribe ( userId, new Callback ()
		{
			@Override
			public void connectCallback ( String channel, Object message )
			{
				Log.d ( "PUBNUB", "SUBSCRIBE : CONNECT on channel:" + channel + " : " + message.getClass () + " : " + message.toString () );
			}

			@Override
			public void disconnectCallback ( String channel, Object message )
			{
				Log.d ( "PUBNUB", "SUBSCRIBE : DISCONNECT on channel:" + channel + " : " + message.getClass () + " : " + message.toString () );
			}

			public void reconnectCallback ( String channel, Object message )
			{
				Log.d ( "PUBNUB", "SUBSCRIBE : RECONNECT on channel:" + channel + " : " + message.getClass () + " : " + message.toString () );
			}

			@Override
			public void successCallback ( String channel, Object message )
			{
				try
				{
					final PubNubBaseAction action = PubNubActionFactory.createAction ( message );
					saveNotification ( action );
					MainActivity.getInstance ().runOnUiThread ( new Runnable ()
					{
						@Override
						public void run ()
						{
							CommunicationManager.getInstance ().fire ( action.getActionType (), action );
							updateInnerModel ( action );
						}
					} );
				} catch ( Exception e )
				{
					// TODO Auto-generated catch block
					e.printStackTrace ();
				}
			}

			@Override
			public void errorCallback ( String channel, PubnubError error )
			{
				Log.d ( "PUBNUB", "SUBSCRIBE : ERROR on channel " + channel + " : " + error.toString () );
			}
		} );
	}

	public void sendAction ( PubNubBaseAction action )
	{
		try
		{
			JSONObject jsonObject = action.toJSON ();
			publish ( action.getReciverId (), jsonObject, new Callback ()
			{
				public void successCallback ( String channel, Object response )
				{
					Log.d ( "PUBNUB", response.toString () );
				}

				public void errorCallback ( String channel, PubnubError error )
				{
					Log.d ( "PUBNUB", error.toString () );
				}
			} );
			updateInnerModel ( action );
		} catch ( JSONException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
	}

	private void updateInnerModel ( PubNubBaseAction action )
	{
		switch ( action.getActionType () )
		{
		// case CHAT:
		// {
		// ChatManager chatManager = ChatManager.getInstance ();
		// chatManager.newMessage ( ( NotificationWithDataAction ) action );
		// break;
		// }

		// case BOOKING_STATUS:
		// {
		// NotificationWithDataAction notificationWithDataAction = (
		// NotificationWithDataAction ) action;
		// BookingDataManager.getInstance ().statusChanged (
		// notificationWithDataAction );
		// break;
		// }
		}
	}

	private void saveNotification ( PubNubBaseAction notification )
	{
		List < PubNubBaseAction > notifications = ( List < PubNubBaseAction > ) FileManager.loadObject ( MainActivity.getInstance (), FileManager.NOTIFICATIONS_DATA_SAVE_PATH, new ArrayList < PubNubBaseAction > () );
		notifications.add ( 0, notification );
		if ( notifications.size () > MainActivity.NOTIFICATIONS_MAX_COUNT )
		{
			notifications = notifications.subList ( 0, MainActivity.NOTIFICATIONS_MAX_COUNT );
		}
		FileManager.saveObject ( MainActivity.getInstance (), FileManager.NOTIFICATIONS_DATA_SAVE_PATH, notifications );
	}

	public boolean isSubcribed ()
	{
		return userId != null;
	}

	public void unSubscribe ()
	{
		unsubscribeAll ();
		// PushService.unsubscribe ( FashionGramApplication.getContext (), user
		// );
		userId = null;
	}
}
