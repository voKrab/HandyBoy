package com.vallverk.handyboy.model.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.vallverk.handyboy.FileManager;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.MainActivity.ApplicationAction;
import com.vallverk.handyboy.model.CommunicationManager;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.server.ServerErrors;
import com.vallverk.handyboy.server.ServerManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Communication with server api, caching requested data 
 */
public class APIManager implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static APIManager instance;

	public static APIManager getInstance ()
	{
		if ( instance == null )
		{
			instance = ( APIManager ) FileManager.loadObject ( MainActivity.getInstance (), FileManager.API_MANAGER_CACHE_SAVE_PATH, new APIManager () );
		}
		return instance;
	}

	public APIManager ()
	{
		cache = new HashMap < Object, APIObject > ();
		user = new UserAPIObject ();
		fileCache = new HashMap < String, APIFile > ();
	}

	private UserAPIObject user;
	private Map < Object, APIObject > cache;
	private Map < String, APIFile > fileCache;

	public void putAPIObject ( APIObject object )
	{
		cache.put ( object.getId (), object );
	}

	public void logout ()
	{
		user = null;
	}

	public UserAPIObject getUser ()
	{
		if ( user == null )
		{
			user = new UserAPIObject ();
		}
		return user;
	}

	public void setUser ( UserAPIObject user )
	{
		this.user = user;
		CommunicationManager.getInstance ().fire ( ApplicationAction.USER, user );
	}

	public void save ()
	{
		FileManager.saveObject ( MainActivity.getInstance (), FileManager.API_MANAGER_CACHE_SAVE_PATH, this );
	}

	public String getUserId ()
	{
		return user.getId ();
	}

	public String saveBitmap ( Bitmap bitmap ) throws Exception
	{
		return saveBitmap ( bitmap, true );
	}
	
	public String saveBitmap ( Bitmap bitmap, boolean uploadToServer ) throws Exception
	{
		byte[] data = bitmapToBytes ( bitmap );
		APIFile apiFile = new APIFile ( data );
		if ( uploadToServer )
		{
			apiFile.save ();
		}
		fileCache.put ( apiFile.getUrl (), apiFile );
		return apiFile.getUrl ();
	}

	public Bitmap loadBitmap ( String urlString ) throws Exception
	{
		APIFile apiFile = fileCache.get ( urlString );
		if ( apiFile == null )
		{
			URL url = new URL ( urlString );
			Bitmap bitmap = BitmapFactory.decodeStream ( url.openConnection ().getInputStream () );
			byte[] data = bitmapToBytes ( bitmap );
			apiFile = new APIFile ( data );
			apiFile.setUrl ( urlString );
			fileCache.put ( apiFile.getUrl (), apiFile );
			return bitmap;
		} else
		{
			byte[] data = apiFile.load ();
			return BitmapFactory.decodeByteArray ( data, 0, data.length );
		}
	}

	private byte[] bitmapToBytes ( Bitmap bitmap )
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream ();
		bitmap.compress ( Bitmap.CompressFormat.PNG, 100, stream );
		byte[] data = stream.toByteArray ();
		return data;
	}

	public String insert ( APIObject object, String methodUrl ) throws Exception
	{
		return insert ( object, methodUrl, null );
	}
	
	public String insert ( APIObject object, String methodUrl, KeyValuePair... additionalParams ) throws Exception
	{
		String responseText = object.insert ( methodUrl, additionalParams );
		JSONObject responseObject = new JSONObject ( responseText );
		String resultStatus = responseObject.getString ( "parameters" );
		if ( resultStatus.isEmpty () )
		{
			if ( !responseObject.isNull ( "id" ) )
			{
				object.setId ( responseObject.getString ( "id" ) );
			}
			putAPIObject ( object );
		} else
		{
			throw new Exception ( resultStatus );
		}
		return resultStatus;
	}

	public APIObject getAPIObject ( String objectId, Class < ? > objectClass ) throws Exception
	{
//		APIObject apiObject = cache.get ( objectId );
		APIObject apiObject = null;
		if ( apiObject == null )
		{
			apiObject = ( APIObject ) objectClass.newInstance ();
			apiObject.setId ( objectId );
			apiObject.fetch ();
			cache.put ( objectId, apiObject );
		}
		return apiObject;
	}
	
	public APIObject getAPIObject ( String objectId, Class < ? > objectClass, String uri ) throws Exception
	{
//		APIObject apiObject = cache.get ( objectId );
		APIObject apiObject = null;
		if ( apiObject == null )
		{
			uri = uri.replace ( "id=1", "id=" + objectId );
			apiObject = ( APIObject ) objectClass.newInstance ();
			apiObject.setId ( objectId );
			apiObject.fetch ( uri );
			cache.put ( objectId, apiObject );
		}
		return apiObject;
	}
	
	public String update ( APIObject object, String methodUrl ) throws Exception
	{
		String responseText = object.update ( methodUrl );
		JSONObject responseObject = new JSONObject ( responseText );
		String resultStatus = responseObject.getString ( "parameters" );
		if ( resultStatus.isEmpty () )
		{
			// JSONObject objectData = responseObject.getJSONObject ( "object"
			// );
			// object.setId ( objectData.getString ( "id" ) );
			// putAPIObject ( object );
		} else
		{
			throw new Exception ( resultStatus );
		}
		return resultStatus;
	}
	

	public String delete ( TypeJobServiceAPIObject object ) throws Exception
	{
		JSONObject jsonObject = new JSONObject ();
		jsonObject.put ( "id", object.getId () );
		String responseText = ServerManager.postRequest ( object.getDeleteURI (), jsonObject );
		JSONObject responseObject = new JSONObject ( responseText );
		String resultStatus = responseObject.getString ( "parameters" );
		if ( resultStatus.isEmpty () )
		{

		} else
		{
			throw new Exception ( resultStatus );
		}
		return resultStatus;		
	}

	public List < UserAPIObject > getServices () throws Exception
	{
		String responseText = ServerManager.getRequest ( ServerManager.SERVICE_GET );
		JSONObject responseJsonObject = new JSONObject ( responseText );
		JSONArray arrayData = responseJsonObject.getJSONArray ( "list" );
		List < UserAPIObject > services = new ArrayList < UserAPIObject > ();
		for ( int i = 0; i < arrayData.length (); i++ )
		{
			JSONObject jsonObject = new JSONObject ( arrayData.getString ( i ) );
			UserAPIObject service = new UserAPIObject ( jsonObject );
			//APIObject serviceDetails = new APIObject ();
//			serviceDetails.update ( uri );
			services.add ( service );
			cache.put ( service.getId (), service );
		}
		return services;
	}

	public List < ChatMessageAPIObject > getMessages ( String currentUserId, String serviceId ) throws Exception
	{
		List < ChatMessageAPIObject > messages = new ArrayList < ChatMessageAPIObject > ();
		String url = ServerManager.MESSAGES_GET;
		//https://142.4.217.86/chat/?userIdFirst=1&userIdSecond=2
		url = url.replaceAll ( "userIdFirst=1", "userIdFirst=" + currentUserId );
		url = url.replaceAll ( "userIdSecond=2", "userIdSecond=" + serviceId );
		String requestString = ServerManager.getRequest ( url );
		JSONObject requestJSON = new JSONObject ( requestString );
		String listData = requestJSON.getString ( "list" );
		if ( !listData.equals ( "null" ) )
		{
			JSONArray jsonArray = new JSONArray ( listData );
			for ( int i = 0; i < jsonArray.length (); i++ )
			{
				JSONObject jsonItem = new JSONObject ( jsonArray.getString ( i ) );
				ChatMessageAPIObject chatMessage = new ChatMessageAPIObject ( jsonItem );
				messages.add ( chatMessage );
			}
		}
		return messages;
	}

	public List < TypeJobServiceAPIObject > getTypeJobs ( UserAPIObject user ) throws Exception
	{
		List < TypeJobServiceAPIObject > typejobs = new ArrayList < TypeJobServiceAPIObject > ();
		String url = ServerManager.GET_SERVICE_TYPEJOBS_URI;
		url = url.replace ( "serviceId=1", "serviceId=" + user.getString ( UserParams.SERVICE_ID ) );
		String responseText = ServerManager.getRequest ( url );
		if ( responseText.isEmpty () )
		{
			throw new Exception ();
		}
		JSONObject responseObject = new JSONObject ( responseText );
		String resultStatus = responseObject.getString ( "parameters" );
		if ( resultStatus.equals ( ServerErrors.JOBS_DOESN_TOT_EXIST ) ) // user have no one job
		{
			return typejobs;
		}
		if ( !resultStatus.isEmpty () )
		{
			throw new Exception ( resultStatus );
		}
        typejobs = getTypeJobs ( new JSONArray ( responseObject.getString ( "list" ) ) );
        return  typejobs;
	}

    public List < TypeJobServiceAPIObject > getTypeJobs ( JSONArray arrayData ) throws Exception
    {
        List < TypeJobServiceAPIObject > typejobs = new ArrayList < TypeJobServiceAPIObject > ();
        for ( int i = 0; i < arrayData.length (); i++ )
        {
            JSONObject jsonObject = new JSONObject ( arrayData.getString ( i ) );
            TypeJobServiceAPIObject typejob = new TypeJobServiceAPIObject ( jsonObject );
            typejobs.add ( typejob );
        }
        return typejobs;
    }


    public List < TypeJobServiceAPIObject > getOnOnlyTypeJobs ( JSONArray arrayData ) throws Exception
    {
        List < TypeJobServiceAPIObject > typejobs = new ArrayList < TypeJobServiceAPIObject > ();
        for ( int i = 0; i < arrayData.length (); i++ )
        {
            JSONObject jsonObject = new JSONObject ( arrayData.getString ( i ) );
            TypeJobServiceAPIObject typejob = new TypeJobServiceAPIObject ( jsonObject );
            if(typejob.isOn()) {
                typejobs.add(typejob);
            }
        }
        return typejobs;
    }




	public List loadList ( String url, Class type ) throws Exception
	{
		String responceString = ServerManager.getRequest ( url );
		ServerManager.checkErrors ( responceString );
		JSONArray jsonArray = ServerManager.getList ( responceString );
		List list = new ArrayList ();
		for ( int i = 0; i < jsonArray.length (); i++ )
		{
			JSONObject jsonObject = jsonArray.getJSONObject ( i );
			APIObject item = ( APIObject ) type.newInstance ();
			item.update ( jsonObject );
			list.add ( item );
		}
		return list;
	}

	public BookingStatusAPIObject getBooingStatus () throws Exception
	{
		BookingStatusAPIObject bookingStatusAPIObject = null;
		String responseText = ServerManager.getRequest ( ServerManager.GET_BOOKING_STATE.replace ( "userId=1", "userId=" + user.getId () ) );
		try
		{
			ServerManager.checkErrors ( responseText );
			bookingStatusAPIObject = new BookingStatusAPIObject ();
			JSONObject jsonObject = ServerManager.getObject ( responseText );
			bookingStatusAPIObject.update ( jsonObject );
		} catch ( Exception ex )
		{
			//ignore
		}
		if ( bookingStatusAPIObject.getId () == null )//TODO Kostyan govnyk 
		{
			bookingStatusAPIObject = null;
		}
		return bookingStatusAPIObject;
	}
}
