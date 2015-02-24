package com.vallverk.handyboy.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.pubnub.PubnubManager.ActionType;

import org.json.JSONObject;

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
                    case BOOKING_ADD_CHARGES_ACCEPTED:
                    {
//                        String bookingId = jsonObject.getString ( "bid" );
//                        manager.updateBooking ( bookingId );
                        new AsyncTask<Void,Void,String>()
                        {
                            @Override
                            protected String doInBackground(Void... params)
                            {
                                String result = "";
                                try
                                {
                                    BookingDataManager manager = BookingDataManager.getInstance ();
                                    manager.update ();
                                } catch ( Exception ex )
                                {
                                    ex.printStackTrace ();
                                    result = ex.getMessage ();
                                }
                                return result;
                            }
                        }.execute ();
                        break;
                    }

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
