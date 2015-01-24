package com.vallverk.handyboy.gcm;

import org.json.JSONObject;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.pubnub.PubnubManager.ActionType;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class NotificationBroadcastReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive ( Context context, Intent intent )
	{
		try
		{
			Bundle extras = intent.getExtras ();
			if ( !extras.isEmpty () )
			{
				String data = extras.getString ( "message" );
				JSONObject jsonObject = new JSONObject ( data );
				String message = jsonObject.getString ( "message" );
				ActionType actionType = ActionType.fromString ( jsonObject.getString ( "actionType" ) );
				switch ( actionType )
				{
					case BOOKING_STATUS:
					{
						String bookingId = jsonObject.getString ( "bookingId" );
						BookingStatusEnum newStatus = BookingStatusEnum.fromInt ( jsonObject.getInt ( "status" ) );
						BookingDataManager.getInstance ().statusChanged ( bookingId, newStatus );
						break;
					}
					
					case CHAT:
					{
//						BookingDataManager.getInstance ().statusChanged ( bookingId, newStatus );
						break;
					}
					
					case BOOKING:
					{
						BookingDataManager.getInstance ().newBooking ();
						break;
					}

					default:
						break;
				}
			}
		} catch ( Exception ex )
		{
			ex.printStackTrace ();
		}
	}
}
