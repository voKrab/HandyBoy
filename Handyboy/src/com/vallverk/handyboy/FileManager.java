package com.vallverk.handyboy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import android.content.Context;

public class FileManager
{
	public static final String USER_DATA_SAVE_PATH = "user_data_save_path";
	public static final String VERIFICATION_SMS_SENDED_TIMES_SAVE_PATH = "verification_sms_sended_times_save_path";
	public static final String API_MANAGER_CACHE_SAVE_PATH = "api_manager_cache_save_path";
	public static final String NOTIFICATIONS_DATA_SAVE_PATH = "notifications_data_save_path";
	public static final String JOB_TYPE_MANAGER_SAVE_PATH = "job_type_manager_save_path";
	public static final String CHAT_MANAGER_SAVE_PATH = "chat_manager_save_path";
	public static final String SETTINGS_SAVE_PATH = "settings_save_path";
	public static final String BOOKING_DATA_MANAGER_CACHE_SAVE_PATH = "booking_data_manager_cache_save_path";

	/**
	 * Save object.
	 */
	public static void saveObject ( Context context, String path, Object object )
	{
		try
		{
			FileOutputStream fos = context.openFileOutput ( path, Context.MODE_PRIVATE );
			ObjectOutputStream os = new ObjectOutputStream ( fos );
			os.writeObject ( object );
			os.close ();
		} catch ( Exception ex )
		{
			ex.printStackTrace ();
		}
	}

	/**
	 * Load object. if throws exception then return defaultValue
	 */
	public static Object loadObject ( Context context, String path, Object defaultValue )
	{
		try
		{
			FileInputStream fis = context.openFileInput ( path );
			ObjectInputStream is = new ObjectInputStream ( fis );
			Object object = is.readObject ();
			is.close ();
			return object;
		} catch ( Exception ex )
		{
			ex.printStackTrace ();
		}
		return defaultValue;
	}
}
