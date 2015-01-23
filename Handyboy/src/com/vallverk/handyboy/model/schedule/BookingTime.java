package com.vallverk.handyboy.model.schedule;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.api.DiscountAPIObject;
import com.vallverk.handyboy.model.api.DiscountAPIObject.DiscountParams;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.jobdescription.HousekeeperViewController;

public class BookingTime extends ScheduleForDay
{
	private int disabled = 0;
	private Date date;

	public void updateFromServer ( Date customDate ) throws Exception
	{
		items = new int[10][5];
		UserAPIObject user = MainActivity.getInstance ().getBookingController ().getHandyBoy ();
		String responseText = ServerManager.getRequest ( ServerManager.SERVISE_FREE_TIME_FOR_DAY.replaceAll ( "serviceId=1", "serviceId=" + user.getString ( UserParams.SERVICE_ID ) ).replaceAll ( "date=1", "date=" + Tools.toSimpleString ( customDate ) ) );
		JSONObject responseObject = new JSONObject ( responseText );
		String parameters = responseObject.getString ( "parameters" );
		if ( !parameters.isEmpty () )
		{
			throw new Exception ( parameters );
		}
		updateFromServer ( responseObject.getJSONArray ( "list" ) );
	}

	public void updateFromDiscount ( DiscountAPIObject discount )
	{
		long startedAt = ( long ) ( ( Integer ) discount.getValue ( DiscountParams.STARTED_AT ) * 1000 );

		TimeZone tzSource = TimeZone.getTimeZone ( "EST" );
		TimeZone tzTarget = TimeZone.getDefault ();
		Date date = new Date ( startedAt );
		date = Tools.shiftTimeZone ( date, tzSource, tzTarget );

		Calendar calendar = Calendar.getInstance ();
		calendar.setTime ( date );
		// calendar.get ( Calendar.Hou );
		calendar.set ( Calendar.SECOND, 0 );
		calendar.set ( Calendar.MILLISECOND, 0 );
		int unroundedMinutes = calendar.get ( Calendar.MINUTE );
		int mod = unroundedMinutes % 30;
		calendar.add ( Calendar.MINUTE, mod < 15 ? -mod : ( 30 - mod ) );
		int hours = calendar.get ( Calendar.HOUR_OF_DAY );
		int minutes = calendar.get ( Calendar.MINUTE );
		String startTime = hours + ":" + ( minutes == 0 ? "00" : "30" );
		calendar.add ( Calendar.HOUR_OF_DAY, 3 );
		String endTime = calendar.get ( Calendar.HOUR_OF_DAY ) + ":" + ( minutes == 0 ? "00" : "30" );
		WorkWindow workWindow = new WorkWindow ( new WorkWindowPoint ( startTime ), new WorkWindowPoint ( endTime ) );
		applyWorkWindow ( workWindow );
	}

	protected void applyWorkWindow ( WorkWindow workWindow )
	{
		int sr = workWindow.startPoint.row;
		int er = workWindow.endPoint.row;
		int sc = workWindow.startPoint.col;
		int ec = workWindow.endPoint.col;
		for ( int i = sr; i <= er; i++ )
		{
			for ( int j = ( i == sr ? sc : 0 ); j <= ( i == er ? ec : items[i].length - 1 ); j++ )
			{
				items[i][j] = defaultItem;
			}
		}
	}

	@Override
	protected int getDefaultItem ()
	{
		return 2;
	}

	@Override
	protected int getSelectedItem ()
	{
		return 1;
	}

	public boolean isDisabled ( int row, int col )
	{
		return items[row][col] == disabled;
	}

	public int getWorkWindowCount ()
	{
		int workWindowCount = 0;
		int[] itemsArray = getItemsAsArray ();
		for ( int i = 1; i < itemsArray.length - 1; i++ )
		{
			if ( itemsArray[i - 1] == selectedItem && itemsArray[i] == selectedItem && itemsArray[i + 1] != selectedItem )
			{
				workWindowCount++;
			}
		}
		return workWindowCount;
	}

	public Date getDate ()
	{
		return date;
	}

	public void setDate ( Date date )
	{
		this.date = date;
	}

	public float getHours ()
	{
		int selectedItemsCount = 0;
		int[] itemsArray = getItemsAsArray ();
		for ( int i = 0; i < itemsArray.length - 1; i++ )
		{
			if ( itemsArray[i] == selectedItem )
			{
				selectedItemsCount++;
			}
		}
		selectedItemsCount = selectedItemsCount == 0 ? 0 : selectedItemsCount - 1;
		float hours = selectedItemsCount / 2f;
		return hours;
	}

	public boolean isEmpty ()
	{
		return getHours () == 0;
	}
}
