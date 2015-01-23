package com.vallverk.handyboy.gcm;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.model.OperatingSystemEnum;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.DeviceTokenAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.pubnub.PubnubManager.ActionType;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.server.WebClientDevWrapper;

public class PushNotification
{
	public static String MY_API_AUTH_FROM_GOOGLE_API_CONSOLE_BROWSER_TOKEN = "key=AIzaSyC-rq9J867dZxqLfoME0kIrwvcbkF1fAY4";
	public static String SENDER_ID = "673570231007";

	/*public static void send ( UserAPIObject reciver, String message ) throws Exception
	{
		List < NameValuePair > formparams = new ArrayList < NameValuePair > ();

		formparams.add ( new BasicNameValuePair ( "registration_id", reciver.getString ( UserParams.GCM_ID ) ) );
		formparams.add ( new BasicNameValuePair ( "data.message", message ) );

		DefaultHttpClient httpClient = ( DefaultHttpClient ) WebClientDevWrapper.getNewHttpClient ();
		HttpPost httpPost = new HttpPost ( "https://android.googleapis.com/gcm/send" );
		HttpResponse response;

		httpPost.setHeader ( "Authorization", MY_API_AUTH_FROM_GOOGLE_API_CONSOLE_BROWSER_TOKEN );
		httpPost.setHeader ( "Content-Type", "application/x-www-form-urlencoded;charset=UTF-8" );

		httpPost.setEntity ( new UrlEncodedFormEntity ( formparams, "utf-8" ) );
		httpClient.execute ( httpPost );

		response = httpClient.execute ( httpPost );

		int responseCode = response.getStatusLine ().getStatusCode ();
		String responseText = Integer.toString ( responseCode );

		if ( response != null )
		{
			// InputStream in = response.getEntity ().getContent ();
			final String text = new String ( EntityUtils.toString ( response.getEntity () ) );
			System.out.println ( text );
		}
	}*/

	public static void send ( UserAPIObject reciver, JSONObject jsonObject ) throws Exception
	{
		JSONObject params = new JSONObject ();
		params.put ( "userId", reciver.getId () );
		params.put ( "message", jsonObject.getString ( "message" ) );
		params.put ( "params", jsonObject.toString () );
		ServerManager.postRequest ( ServerManager.SEND_PUSH, params.toString () );
	}

	public static void updateGcmID () throws Exception
	{
		APIManager apiManager = APIManager.getInstance ();
		UserAPIObject user = apiManager.getUser ();
		String gcmId = GoogleCloudMessaging.getInstance ( MainActivity.getInstance () ).register ( SENDER_ID );
		DeviceTokenAPIObject deviceTokenAPIObject = new DeviceTokenAPIObject ( user.getId (), gcmId, OperatingSystemEnum.ANDROID.toString () );
		apiManager.insert ( deviceTokenAPIObject, ServerManager.SAVE_DEVICE_TOKEN );
	}

	public static JSONObject createJSON ( String message, ActionType actionType ) throws Exception
	{
		JSONObject jsonObject = new JSONObject ();
		jsonObject.put ( "message", message );
		jsonObject.put ( "actionType", actionType.toString () );
		return jsonObject;
	}

	public static void unSubscribe () throws Exception
	{
		MainActivity controller = MainActivity.getInstance ();
		UserAPIObject user = APIManager.getInstance ().getUser ();
		GoogleCloudMessaging.getInstance ( controller ).unregister ();
		ServerManager.getRequest ( ServerManager.DELETE_DEVICE_TOKEN.replace ( "userId=1", "userId=" + user.getId () ) );
	}
}
