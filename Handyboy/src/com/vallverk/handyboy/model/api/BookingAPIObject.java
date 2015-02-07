package com.vallverk.handyboy.model.api;

import android.graphics.Color;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.gcm.PushNotification;
import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.CommunicationManager;
import com.vallverk.handyboy.model.CommunicationManager.CommunicationAction;
import com.vallverk.handyboy.pubnub.NotificationWithDataAction;
import com.vallverk.handyboy.pubnub.PubnubManager;
import com.vallverk.handyboy.pubnub.PubnubManager.ActionType;
import com.vallverk.handyboy.server.ServerManager;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingAPIObject extends APIObject implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static int getStatusColor ( BookingStatusEnum bookingStatus )
	{
		switch ( bookingStatus )
		{
			case CANCELED_BY_CUSTOMER:
			case CANCELED_BY_HB:
				return Color.parseColor ( "#DF2839" );
			case PENDING:
				return Color.parseColor ( "#24ACB8" );
			case CONFIRMED:
				return Color.parseColor ( "#3CB44A" );
			case PERFORMED:
				return Color.parseColor ( "#FF9000" );
			case APPROVED:
				return Color.parseColor ( "#6F3CB4" );
			case DECLINED:
				return MainActivity.getInstance ().getResources ().getColor ( R.color.red );
			case WAITING_FOR_REVIEW:
				return Color.parseColor ( "#999999" );
			default:
				return -1;
		}
	}

    public AdditionalChargesAPIObject getAdditionalCharges () throws Exception
    {
        List < AdditionalChargesAPIObject > list = APIManager.getInstance ().loadList ( ServerManager.BOOKING_GET_ADD_CHARGES.replace ( "bookingId=1", "bookingId=" + getId() ), AdditionalChargesAPIObject.class );
        for ( AdditionalChargesAPIObject object : list )
        {
            if ( object.isRequested () )
            {
                return object;
            }
        }
        return null;
    }

    public boolean isAdditionalChargesState () throws Exception
    {
        String jsonString = ServerManager.getRequest ( ServerManager.IS_ADD_CHARGES_STATE.replace ( "bookingId=1", "bookingId=" + getId () ) );
        ServerManager.checkErrors ( jsonString );
        JSONObject jsonObject = new JSONObject ( jsonString );
        boolean result = jsonObject.getBoolean ( "isset" );
        return result;
    }

    public boolean isAdditionalChargesRequested () throws Exception
    {
        List < AdditionalChargesAPIObject > list = APIManager.getInstance ().loadList ( ServerManager.BOOKING_GET_ADD_CHARGES.replace ( "bookingId=1", "bookingId=" + getId () ), AdditionalChargesAPIObject.class );
        return list.size () > 0;
    }

    public String getTextTotalHours()
    {
        String hours = getValue ( BookingAPIParams.TOTAL_HOURS ).toString ();
        hours = hours.replace ( ".50", ".30" );
        return hours;
    }

    public enum BookingAPIParams
	{
		SERVICE_ID ( "serviceId" ), CUSTOMER_ID ( "customerId" ), TYPE_JOB_SERVICE_ID ( "typeJobServiceId" ), ADDRESS_ID ( "addressId" ), CREDIT_CARD_ID ( "creditCardId" ), TIME ( "time" ), DATE ( "date" ), TOTAL_PRICE ( "totalPrice" ), TOTAL_HOURS ( "totalHours" ), STATUS ( "status" ), SPECIAL_REQUEST ( "additional_charges_text" ), SPECIAL_REQUEST_PRICE ( "additional_charges_price" ), COMMENT("comment");

		String name;

		BookingAPIParams ( String name )
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

		public static BookingAPIParams fromString ( String string )
		{
			for ( BookingAPIParams category : BookingAPIParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}
	}

	public BookingAPIObject ()
	{
		super ();
	}

	public BookingAPIObject ( JSONObject jsonObject ) throws Exception
	{
		update ( jsonObject );
	}

	@Override
	public Object[] getParams ()
	{
		return BookingAPIParams.values ();
	}

	public BookingStatusEnum getStatus ()
	{
		return BookingStatusEnum.fromInt ( getInt ( BookingAPIParams.STATUS ) );
	}

	public void changeStatus ( UserAPIObject sender, UserAPIObject reciver, BookingStatusEnum status ) throws Exception
	{
		int newStatus = status.toInt ();
		updateStatus ( status );
		APIManager.getInstance ().update ( this, ServerManager.BOOKING_SAVE );
		Map < Object, Object > data = new HashMap < Object, Object > ();
		data.put ( BookingAPIParams.STATUS.toString (), newStatus );
		data.put ( "id", getId () );
		NotificationWithDataAction pubnubAction = new NotificationWithDataAction ( sender.getId (), reciver.getId (), ActionType.BOOKING_STATUS, data );
		PubnubManager.getInstance ().sendAction ( pubnubAction );

		JSONObject jsonObject = PushNotification.createJSON ( sender.getShortName () + " changed the status to " + status.toString (), ActionType.BOOKING_STATUS );
		jsonObject.put ( "bookingId", getId () );
		jsonObject.put ( "status", status.toInt () );
//		PushNotification.send ( reciver, jsonObject );
	}

	public void updateStatus ( BookingStatusEnum newStatus )
	{
		putValue ( BookingAPIParams.STATUS, "" + newStatus.toInt () );
		CommunicationManager.getInstance ().fire ( CommunicationAction.BOOKING_STATUS, this );
	}
}
