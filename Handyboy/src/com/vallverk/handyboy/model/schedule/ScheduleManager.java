package com.vallverk.handyboy.model.schedule;

import android.location.Address;
import android.location.Geocoder;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.AddressWraper;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.server.ServerManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ScheduleManager
{
    public enum Day
	{
		SUNDAY ( "1" ), MONDAY ( "2" ), TUESDAY ( "3" ), WEDNESDAY ( "4" ), THURSDAY ( "5" ), FRIDAY ( "6" ), SATURDAY ( "7" );

		String name;

		Day ( String name )
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

		public static Day fromString ( String string )
		{
			for ( Day category : Day.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}
	}

	private static ScheduleManager instance;

	public static ScheduleManager getInstance ()
	{
		if ( instance == null )
		{
			instance = new ScheduleManager ();
		}
		return instance;
	}

	private Map < Day, ScheduleForDay > weeklySchedule;
	private ScheduleForDay customDaySchedule;

	public ScheduleManager ()
	{
		initWeekly ();
		customDaySchedule = new ScheduleForDay ();
	}

	private void initWeekly ()
	{
		weeklySchedule = new HashMap < Day, ScheduleForDay > ();
		for ( Day day : Day.values () )
		{
			weeklySchedule.put ( day, new ScheduleForDay () );
		}
	}

	public int[][] getBridges ( Day day )
	{
		ScheduleForDay scheduleForDay = getScheduleForDay ( day );
		return scheduleForDay.getBridges ();
	}

	public int[][] getItems ( Day day )
	{
		ScheduleForDay scheduleForDay = getScheduleForDay ( day );
		return scheduleForDay.getItems ();
	}

	private ScheduleForDay getScheduleForDay ( Day day )
	{
		ScheduleForDay scheduleForDay = day == null ? customDaySchedule : weeklySchedule.get ( day );
		return scheduleForDay;
	}

	public void setSelected ( int row, int col, Day day, boolean isSelected )
	{
		ScheduleForDay scheduleForDay = getScheduleForDay ( day );
		;
		scheduleForDay.setSelected ( row, col, isSelected );
	}

	public boolean isSelected ( int row, int col, Day day )
	{
		ScheduleForDay scheduleForDay = getScheduleForDay ( day );
		;
		return scheduleForDay.isSelected ( row, col );
	}

	public void removeWindow ( int row, int col, Day day )
	{
		ScheduleForDay scheduleForDay = getScheduleForDay ( day );
		;
		scheduleForDay.removeWindow ( row, col );
	}

	public void uploadToServer () throws Exception
	{
		UserAPIObject user = APIManager.getInstance ().getUser ();
		JSONObject jsonObject = new JSONObject ();
		jsonObject.put ( "serviceId", user.getString ( UserParams.SERVICE_ID ) );
		JSONObject daysJSONObject = new JSONObject ();
		for ( Day day : weeklySchedule.keySet () )
		{
			ScheduleForDay daySchedule = weeklySchedule.get ( day );
			daysJSONObject.put ( day.toString (), daySchedule.createTimeJSONArray () );
		}
		jsonObject.put ( "days", daysJSONObject );
		String responseText = ServerManager.postRequest ( ServerManager.SCHEDULE_ADD_WEEK, jsonObject );
		JSONObject responseObject = new JSONObject ( responseText );
		String resultStatus = responseObject.getString ( "parameters" );
		if ( resultStatus.isEmpty () )
		{

		} else
		{
			throw new Exception ( resultStatus );
		}
	}

	public void uploadToServer ( Date date, boolean dayOff ) throws Exception
	{
        Address address = customDaySchedule.getAddress ();
		UserAPIObject user = APIManager.getInstance ().getUser ();
		JSONObject jsonObject = new JSONObject ();
		jsonObject.put ( "serviceId", user.getString ( UserParams.SERVICE_ID ) );
		jsonObject.put ( "date", Tools.toSimpleString(date) );
		jsonObject.put ( "time", dayOff ? "[]" : customDaySchedule.createTimeJSONArray ().toString () );
		jsonObject.put ( "longitude", address == null ? "" : address.getLongitude() );
		jsonObject.put ( "latitude", address == null ? "" : address.getLatitude () );
		jsonObject.put ( "addressTitle", address == null ? "" : new AddressWraper ( address ).toString () );
		String responseText = ServerManager.postRequest ( ServerManager.SCHEDULE_ADD_CUSTOM_DAY, jsonObject );
		JSONObject responseObject = new JSONObject ( responseText );
		String resultStatus = responseObject.getString ( "parameters" );
		if ( resultStatus.isEmpty () )
		{

		} else
		{
			throw new Exception ( resultStatus );
		}
	}

	public void updateFromServer () throws Exception
	{
		initWeekly ();
		UserAPIObject user = APIManager.getInstance ().getUser ();
		String responseText = ServerManager.getRequest ( ServerManager.SCHEDULE_GET_WEEK.replaceAll ( "serviceId=1", "serviceId=" + user.getString ( UserParams.SERVICE_ID ) ) );
		JSONObject responseObject = new JSONObject ( responseText );
		String parameters = responseObject.getString ( "parameters" );
		if ( !parameters.isEmpty () )
		{
			throw new Exception ( parameters );
		}
		Object data = responseObject.get ( "list" );
		if ( ! ( data instanceof JSONArray ) )
		{
			return;
		}
		JSONArray jsonArray = responseObject.getJSONArray ( "list" );
		for ( int i = 0; i < jsonArray.length (); i++ )
		{
			JSONObject jsonObject = new JSONObject ( jsonArray.getString ( i ) );
			Day day = Day.fromString ( jsonObject.getString ( "day" ) );
			ScheduleForDay scheduleForDay = weeklySchedule.get ( day );
			scheduleForDay.updateFromServer ( new JSONArray ( jsonObject.getString ( "time" ) ) );
		}
	}

	public void updateFromServer ( Date customDate ) throws Exception
	{
		customDaySchedule = new ScheduleForDay ();
		UserAPIObject user = APIManager.getInstance ().getUser ();
		String responseText = ServerManager.getRequest ( ServerManager.SCHEDULE_GET_CUSTOM_DAY.replaceAll ( "serviceId=1", "serviceId=" + user.getString ( UserParams.SERVICE_ID ) ).replaceAll ( "date=1", "date=" + Tools.toSimpleString ( customDate ) ) );
		JSONObject responseObject = new JSONObject ( responseText );
		String parameters = responseObject.getString ( "parameters" );
		if ( !parameters.isEmpty () )
		{
			throw new Exception ( parameters );
		}
		String data = responseObject.getString ( "object" );
		if ( data.equals ( "[]" ) )
		{
			return;
		}
		JSONObject jsonObject = new JSONObject ( data );
		String time = jsonObject.getString ( "time" );
		if ( time.equals ( "[]" ) )
		{
			customDaySchedule.setDayOff ( true );
		} else
		{
			customDaySchedule.updateFromServer ( new JSONArray ( time ) );
		}
        String addressTittle = jsonObject.getString ( "addressTitle" );
        if ( addressTittle.isEmpty () )
        {
            return;
        }
        double longitude = jsonObject.getDouble ( "longitude" );
        double latitude = jsonObject.getDouble ( "latitude" );
        Geocoder gcd = new Geocoder ( MainActivity.getInstance().getBaseContext (), Locale.getDefault() );
        List < Address > result = gcd.getFromLocation ( latitude, longitude, 1 );
        if ( result.isEmpty () )
        {
            return;
        }
        customDaySchedule.setAddress ( result.get ( 0 ) );
    }

    public ScheduleForDay getCustomDaySchedule ()
    {
        return customDaySchedule;
    }

    public boolean isDayOff ()
	{
		return customDaySchedule.isDayOff ();
	}
}
