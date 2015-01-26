package com.vallverk.handyboy.gcm;

import org.json.JSONObject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.MainActivity.ApplicationAction;
import com.vallverk.handyboy.MainActivity.ApplicationState;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.SettingsManager;
import com.vallverk.handyboy.SettingsManager.Params;
import com.vallverk.handyboy.model.ChatManager;
import com.vallverk.handyboy.pubnub.NotificationWithDataAction;
import com.vallverk.handyboy.pubnub.PubnubManager.ActionType;

public class GcmIntentService extends IntentService
{
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	private NotificationCompat.Builder builder;

	public GcmIntentService ()
	{
		super ( "GcmIntentService" );
	}

	public static final String TAG = "GCM Demo";

	@Override
	protected void onHandleIntent ( Intent intent )
	{
		System.out.println ( "GcmIntentService:onHandleIntent" );
		Bundle extras = intent.getExtras ();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance ( this );

		String messageType = gcm.getMessageType ( intent );

		if ( !extras.isEmpty () )
		{
			if ( GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals ( messageType ) )
			{
				sendNotification ( "Send error: " + extras.toString () );
			} else if ( GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals ( messageType ) )
			{
				sendNotification ( "Deleted messages on server: " + extras.toString () );
			} else if ( GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals ( messageType ) )
			{
				sendNotification ( extras.getString ( "message" ) );
			}
		}
		GcmBroadcastReceiver.completeWakefulIntent ( intent );
	}

	private void sendNotification ( String jsonText )
	{
		try
		{
			if ( !isNeedShowNotificationAndUpdateInnerModel ( jsonText ) )
			{
				return;
			}
			JSONObject jsonObject = new JSONObject ( jsonText );
			String message = jsonObject.getString ( "message" );
			ActionType actionType = ActionType.fromString ( jsonObject.getString ( "actionType" ) );
			updateApplication ( jsonObject );

			Uri alarmSound = RingtoneManager.getDefaultUri ( RingtoneManager.TYPE_NOTIFICATION );
			mNotificationManager = ( NotificationManager ) this.getSystemService ( Context.NOTIFICATION_SERVICE );

			Intent intent = new Intent ( this, MainActivity.class );
			intent.putExtra ( "actionType", actionType.toString () );
			intent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            SharedPreferences sharedPref = getSharedPreferences( "ControllerPreferences" ,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("actionType", actionType.toString ());
            editor.commit();

			PendingIntent contentIntent = PendingIntent.getActivity ( this, 0, intent, 0, null );

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder ( this ).setSound ( alarmSound ).setSmallIcon ( R.drawable.icon ).setContentTitle ( "HandyBoy" ).setStyle ( new NotificationCompat.BigTextStyle ().bigText ( message ) ).setAutoCancel ( true ).setContentText ( message );
			mBuilder.setContentIntent ( contentIntent );

			mNotificationManager.notify (  NOTIFICATION_ID , mBuilder.build () );
			Intent toApplicationIntent = new Intent ( ApplicationAction.GCM_NOTIFICATION.toString () );
			toApplicationIntent.putExtra ( "message", jsonText );
			sendBroadcast ( toApplicationIntent );
		} catch ( Exception ex )
		{
			ex.printStackTrace ();
		}
	}

	private boolean isNeedShowNotificationAndUpdateInnerModel ( String jsonText ) throws Exception
	{
		Context ctx = getApplicationContext ();
		JSONObject jsonObject = new JSONObject ( jsonText );
		ActionType actionType = ActionType.fromString ( jsonObject.getString ( "actionType" ) );
		boolean isPushNotificationEnabled = SettingsManager.getBoolean ( Params.IS_PUSH_NOTIFICATION, true, ctx ) && SettingsManager.getBoolean ( Params.IS_LOGIN, false, ctx );
		if ( !isPushNotificationEnabled )
		{
			return false;
		}
		if ( actionType == ActionType.CHAT )
		{
			ApplicationState applicationState = ApplicationState.valueOf ( SettingsManager.getString ( Params.APPLICATION_STATE, ApplicationState.PAUSE.toString (), ctx ) );
			if ( applicationState == ApplicationState.RESUME )
			{
				ChatManager chatManager = ChatManager.getInstance ();
				chatManager.newMessage ( jsonObject );
				return false;
			}
		}
		return true;
	}

	private void updateApplication ( JSONObject jsonObject ) throws Exception
	{
		ActionType actionType = ActionType.fromString ( jsonObject.getString ( "actionType" ) );
		switch ( actionType )
		{
		// case BOOKING:
		// {
		// BookingDataManager.getInstance ().update ();
		// break;
		// }
		}
	}
}
