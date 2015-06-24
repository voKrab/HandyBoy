package com.vallverk.handyboy;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsManager
{ 
	public enum Params
	{
		IS_PUSH_NOTIFICATION, IS_LOGIN, APPLICATION_STATE, DATA_FROM_NOTIFICATION, IS_PICKY_DIALOG_SHOW, IS_LOGOUT
	}

	public static void setString ( Params param, String value, Context context )
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences ( context );
		prefs.edit ().putString ( param.toString (), value ).commit ();
	}

	public static void setBoolean ( Params param, boolean value, Context context )
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences ( context );
		prefs.edit ().putBoolean ( param.toString (), value ).commit ();
	}

	public static boolean getBoolean ( Params param, boolean defValue, Context context )
	{
		return PreferenceManager.getDefaultSharedPreferences ( context ).getBoolean ( param.toString (), defValue );
	}

	public static String getString ( Params param, String defValue, Context context )
	{
		return PreferenceManager.getDefaultSharedPreferences ( context ).getString ( param.toString (), defValue );
	}
}
