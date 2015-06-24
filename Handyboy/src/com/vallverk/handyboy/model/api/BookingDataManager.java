package com.vallverk.handyboy.model.api;

import android.os.AsyncTask;

import com.vallverk.handyboy.FileManager;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.MainActivity.ApplicationAction;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.CommunicationManager;
import com.vallverk.handyboy.server.ServerManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookingDataManager implements Serializable
{
	private static final long serialVersionUID = 1L;
	private List < BookingDataObject > data;
	private int activeDataIndex;
	private static BookingDataManager instance;
	private int amountUnreadBooking;
	private BookingFilter bookingType;

    public enum BookingFilter
	{
		CURRENT ( "current" ),
		PAST ( "past" );

		String name;

		BookingFilter ( String name )
		{
			this.name = name;
		}

		public String toString ()
		{
			return name;
		}
	}

	public static BookingDataManager getInstance ()
	{
		if ( instance == null )
		{
			instance = ( BookingDataManager ) FileManager.loadObject ( MainActivity.getInstance (), FileManager.BOOKING_DATA_MANAGER_CACHE_SAVE_PATH, new BookingDataManager () );
		}
		return instance;
	}

	public BookingDataManager ()
	{
		data = new ArrayList < BookingDataObject > ();
		bookingType = BookingFilter.CURRENT;
	}

	public List < BookingDataObject > getData ()
	{
		return data;
	}

	public int getActiveDataIndex ()
	{
		return activeDataIndex;
	}

	public void setActiveDataIndex ( int activeDataIndex )
	{
		this.activeDataIndex = activeDataIndex;
	}

	public void update () throws Exception
	{
		JSONArray requestJsonArray;
		JSONObject requestJsonObject;
		APIManager apiManager = APIManager.getInstance ();
		//&filter=current
		String request = ServerManager.getRequest ( ServerManager.GET_BOOKING_DETAILS.replace ( "id=1", "id=" + apiManager.getUserId () + "&filter=" + getBookingFilter ().toString ()) );
		requestJsonObject = new JSONObject ( request );
		requestJsonArray = new JSONArray ( requestJsonObject.getString ( "list" ) );
		data.clear ();
		for ( int i = 0; i < requestJsonArray.length (); i++ )
		{
			JSONObject tempObject = requestJsonArray.getJSONObject ( i );
			try
			{
				BookingDataObject bookingDataObject = new BookingDataObject ( tempObject );
				data.add ( bookingDataObject );
			} catch ( Exception ex )
			{
				ex.printStackTrace ();
			}
		}
	}

    public void updateBooking ( final String bookingId )
    {
        new AsyncTask<Void,Void,String>()
        {
            @Override
            protected String doInBackground(Void... params)
            {
                String result = "";
                try
                {
                    BookingAPIObject bookingAPIObject = getBookingDataObject ( bookingId ).getBookingAPIObject ();
                    bookingAPIObject.fetch ( ServerManager.GET_BOOKING.replace ( "id=1", "id=" + bookingId ) );
                } catch ( Exception ex )
                {
                    ex.printStackTrace ();
                    result = ex.getMessage ();
                }
                return result;
            }
        }.execute ();

    }

	public BookingDataObject getActiveBooking ()
	{
      if(getActiveDataIndex () <= data.size() - 1){
         return data.get ( getActiveDataIndex () );
      }else{
         setActiveDataIndex(0);
         return data.get ( getActiveDataIndex() );
      }
	}

    public BookingAPIObject getActiveBookingAPIObject ()
    {
        return data.get ( getActiveDataIndex () ).getBookingAPIObject();
    }

	public BookingStatusEnum getActiveBookingStatus ()
	{
		return getActiveBooking ().getSatus ();
	}

	public UserAPIObject getActiveCustomer ()
	{
		return getActiveBooking ().getCustomer ();
	}

	public UserAPIObject getActiveService ()
	{
		return getActiveBooking ().getService ();
	}

//	public void statusChanged ( NotificationWithDataAction action )
//	{
//		String bookingId = action.getString ( "id" );
//		BookingStatusEnum newStatus = BookingStatusEnum.fromInt ( ( Integer ) action.getValue ( BookingAPIParams.STATUS.toString () ) );
//		BookingDataObject bookingDataObject = getBookingDataObject ( bookingId );
//		if ( bookingDataObject != null )
//		{
//			activeDataIndex = data.indexOf ( bookingDataObject );
//			bookingDataObject.updateStatus ( newStatus );
//			if ( newStatus == BookingStatusEnum.WAITING_FOR_REVIEW && isICustomer () )
//			{
//				MainActivity.getInstance ().setState ( VIEW_STATE.SERVICED );
//			} else if ( newStatus == BookingStatusEnum.CANCELED_BY_HB && isICustomer () )
//			{
//				MainActivity.getInstance ().setState ( VIEW_STATE.CUSTOMER_REVIEW );
//			}
//		}
//	}

	public void statusChanged ( String bookingId, BookingStatusEnum status )
	{
		BookingStatusEnum newStatus = status;
		BookingDataObject bookingDataObject = getBookingDataObject ( bookingId );
		if ( bookingDataObject != null )
		{
			activeDataIndex = data.indexOf ( bookingDataObject );
			bookingDataObject.updateStatus ( newStatus );
			if ( newStatus == BookingStatusEnum.WAITING_FOR_REVIEW && isICustomer () )
			{
				MainActivity.getInstance ().setState ( VIEW_STATE.SERVICED );
			}/* else if ( newStatus == BookingStatusEnum.CANCELED_BY_HB && isICustomer () )
			{
				MainActivity.getInstance ().setState ( VIEW_STATE.CUSTOMER_REVIEW );
			} else if ( newStatus == BookingStatusEnum.CANCELED_BY_CUSTOMER && isIService () )
			{
				MainActivity.getInstance ().setState ( VIEW_STATE.SERVICE_REVIEW );
			}*/
		}
	}

	private BookingDataObject getBookingDataObject ( String bookingId )
	{
		for ( BookingDataObject bookDataObject : data )
		{
			if ( bookDataObject.getId ().equals ( bookingId ) )
			{
				return bookDataObject;
			}
		}
		return null;
	}

	private boolean isICustomer ()
	{
		return !isIService ();
	}

	public boolean isIService ()
	{
		return getActiveBooking ().getService ().getId ().equals ( APIManager.getInstance ().getUserId () );
	}

	public void save ()
	{
		FileManager.saveObject ( MainActivity.getInstance (), FileManager.BOOKING_DATA_MANAGER_CACHE_SAVE_PATH, this );
	}

	public void setActiveDataIndex ( String bookingId )
	{
		BookingDataObject bookingDataObject = getBookingDataObject ( bookingId );
		setActiveDataIndex ( data.indexOf ( bookingDataObject ) );
	}

	public int getAmountUnreadBooking ()
	{
		return amountUnreadBooking;
	}

	public void setAmountUnreadBooking ( int newValue )
	{
		if ( amountUnreadBooking != newValue )
		{
			 this.amountUnreadBooking = newValue;
			 CommunicationManager.getInstance ().fire ( ApplicationAction.AMOUNT_UNREAD_BOOKING, newValue );
		}
	}

	public void newBooking ()
	{
		setAmountUnreadBooking ( this.amountUnreadBooking + 1 );
	}

	public BookingFilter getBookingFilter ()
	{
		return bookingType;
	}

	public void setBookingFilter( BookingFilter bookingType )
	{
		this.bookingType = bookingType;
	}
}
