package com.vallverk.handyboy.server;

import android.graphics.Bitmap;

import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.Media;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ServerManager
{
    // https://api.handyboy.com
    public static String PROTOCOL = "https://";
    public static String HOST = "api.handyboy.com";

    public static String USER_AUTH_URI = PROTOCOL + HOST + "/user/auth";
    public static String USER_FACEBOOK_AUTH_URI = PROTOCOL + HOST + "/user/authfb";
    public static String USER_REGISTRATION_URI = PROTOCOL + HOST + "/user/registration";
    public static String USER_UPDATE_URI = PROTOCOL + HOST + "/user/update";
    public static String RESTORE_PASSWORD_URI = PROTOCOL + HOST + "/user/forget";
    public static String USER_DETAILS_INSERT_URI = PROTOCOL + HOST + "/service/add";
    public static String USER_DETAILS_UPDATE_URI = PROTOCOL + HOST + "/service/update";
    public static String USER_DETAILS_FETCH_URI = PROTOCOL + HOST + "/service/?id=1";
    public static String GET_GALLERY_URI = PROTOCOL + HOST + "/gallery/?serviceId=";
    public static String ADD_GALLERY_URI = PROTOCOL + HOST + "/gallery/add";
    public static String DELETE_GALLERY_URI = PROTOCOL + HOST + "/gallery/delete?id=";
    public static String USER_FETCH_URI = PROTOCOL + HOST + "/user/?id=1";
    public static String UPLOAD_FILE = PROTOCOL + HOST + "/user/upload";
    public static String JOBTYPE_GET = PROTOCOL + HOST + "/typejob";
    public static String SERVICE_GET = PROTOCOL + HOST + "/service";
    public static String MESSAGES_GET = PROTOCOL + HOST + "/chat/?userIdFirst=1&userIdSecond=2";
    public static String CHAT_INSERT_URI = PROTOCOL + HOST + "/chat/add";
    public static String CHAT_MESSAGE_INSERT_URI = PROTOCOL + HOST + "/chat/message";
    public static String CHAT_GET_BY_USER_URI = PROTOCOL + HOST + "/chat/?userId=1";
    public static String USER_GET_BY_ID_URI = PROTOCOL + HOST + "/user/?id=1";
    public static String TYPEJOB_SERVICE_INSERT_URI = PROTOCOL + HOST + "/service/bindadd";
    public static String GET_SERVICE_TYPEJOBS_URI = PROTOCOL + HOST + "/service/job/?serviceId=1";

    public static String CHECK_FAVORITE_URI = PROTOCOL + HOST + "/favorite/check";
    public static String ADD_FAVORITE_URI = PROTOCOL + HOST + "/favorite/add";
    public static String DELETE_FAVORITE_URI = PROTOCOL + HOST + "/favorite/delete";
    public static String FAVS_GET_BY_USER_URI = PROTOCOL + HOST + "/favorite/getbyuserid?id=";

    public static String TYPE_JOB_SERVICE_SAVE = PROTOCOL + HOST + "/typejobservice/save";

    public static String CHECK_BLOCK_URI = PROTOCOL + HOST + "/block/check";
    public static String ADD_BLOCK_URI = PROTOCOL + HOST + "/block/add";
    public static String DELETE_BLOCK_URI = PROTOCOL + HOST + "/block/delete";
    public static String BLOCK_GET_BY_USER_URI = PROTOCOL + HOST + "/block/getbyuserid/?id=";
    public static String TYPE_JOB_SERVICE_DELETE = PROTOCOL + HOST + "/typejobservice/delete";
    public static String SCHEDULE_ADD = PROTOCOL + HOST + "/schedule/add";
    public static String SCHEDULE_ADD_WEEK = PROTOCOL + HOST + "/schedule/addweek";
    public static String SCHEDULE_GET_WEEK = PROTOCOL + HOST + "/schedule/?serviceId=1";
    public static String SCHEDULE_GET_CUSTOM_DAY = PROTOCOL + HOST + "/schedule/?current=yes&serviceId=1&date=1";
    public static String SCHEDULE_ADD_CUSTOM_DAY = PROTOCOL + HOST + "/schedule/add/?current=yes";

    public static String SEARCH_URL = PROTOCOL + HOST + "/search/?";
    public static String SEARCH_FOR_JOB_TYPE_URL = PROTOCOL + HOST + "/search/byjob?";
    public static String FILTER_URL = PROTOCOL + HOST + "/search/filter/?";
    public static String ADDONS_GET = PROTOCOL + HOST + "/addons";
    public static String ADDONS_SERVICE_SET = PROTOCOL + HOST + "/addons/set";
    public static String ADDONS_SERVICE_GET = PROTOCOL + HOST + "/addons/get?serviceId=1";

    public static String SERVICE_GET_ENABLED_TYPE_JOBS = PROTOCOL + HOST + "/service/getavailable?serviceId=1";

    public static String LICENSE_ADD = PROTOCOL + HOST + "/license/add";

    public static String ADD_AVAILABLE_NOW = PROTOCOL + HOST + "/availablenow/add";
    public static String GET_AVAILABLE_NOW = PROTOCOL + HOST + "/availablenow/?serviceId=";

    public static String INSERT_ADDRESS = PROTOCOL + HOST + "/address/insert";
    public static String UPDATE_ADDRESS = PROTOCOL + HOST + "/address/update";
    public static String DELETE_ADDRESS = PROTOCOL + HOST + "/address/delete";
    public static String GET_ADDRESSES = PROTOCOL + HOST + "/address/get?id=";
    public static String GET_ADDRESS = PROTOCOL + HOST + "/address/get?objectId=";
    public static String CREATE_BOOKING = PROTOCOL + HOST + "/booking/createbooking";
    public static String SERVISE_FREE_TIME_FOR_DAY = PROTOCOL + HOST + "/booking/getfreetime?serviceId=1&date=1";

    public static String GET_BOOKING_DETAILS = PROTOCOL + HOST + "/booking/getbookingdetails?id=1";
    public static String BOOKING_SAVE = PROTOCOL + HOST + "/booking/savebooking";

    public static String GET_CREDIT_CARDS = PROTOCOL + HOST + "/payment/getallcards?id=";
    public static String SAVE_CARD = PROTOCOL + HOST + "/payment/savecard";
    public static String DELETE_CARD = PROTOCOL + HOST + "/payment/deletecard?id=";

    public static final String REVIEW_SAVE = PROTOCOL + HOST + "/review/add";
    public static final String GET_REVIEWS = PROTOCOL + HOST + "/review/?";
    public static final String GET_BOOKING_STATE = PROTOCOL + HOST + "/booking/getbookingstatus/?userId=1";

    public static final String GET_WEEK_TIME = PROTOCOL + HOST + "/service/getweektime?userId=";
    public static final String GET_MY_MONEY = PROTOCOL + HOST + "/payment/getmymoney?id=";
    public static final String SAVE_DEVICE_TOKEN = PROTOCOL + HOST + "/notify/token";
    public static final String DELETE_DEVICE_TOKEN = PROTOCOL + HOST + "/notify/unregistertoken?userId=1";

    public static final String SEND_PUSH = PROTOCOL + HOST + "/notify/sendpush";

    public static final String GET_TRANSACTION_HISTORY = PROTOCOL + HOST + "/payment/transactionhistory?id=";
    public static final String ADD_CHARGES_SAVE = PROTOCOL + HOST + "/booking/saveaddcharges";
    public static final String IS_ADD_CHARGES_STATE = PROTOCOL + HOST + "/booking/isstatusaddcharges?bookingId=1";
    public static final String BOOKING_GET_ADD_CHARGES = PROTOCOL + HOST + "/booking/addcharges?bookingId=1";
    public static final String AVAILABLE_NOW_IS_CAN = PROTOCOL + HOST + "/availablenow/can?serviceId=1";

    public static final String SAVE_USER_LOCATION = PROTOCOL + HOST + "/user/location";




    public static String getRequest ( String uriString ) throws Exception
    {
        URI uri = new URI ( uriString );
        HttpGet httpget = new HttpGet ( uri );
        httpget.setHeader ( "PHPSESSID", getSessionId () );

        // HttpClient client = new DefaultHttpClient ();
        DefaultHttpClient client = ( DefaultHttpClient ) WebClientDevWrapper.getNewHttpClient ();
        System.out.println ( "GET: to " + uriString );
        final HttpResponse response = client.execute ( httpget );
        HttpEntity responseEntity = response.getEntity ();
        final String responseText = new String ( EntityUtils.toString ( responseEntity ) );
        System.out.println ( "RESPONSE: " + responseText );
        return responseText;
    }

    public static String postRequest ( String uriString, Object content ) throws Exception
    {
        URI uri = new URI ( uriString );

        HttpPost httppost = new HttpPost ( uri );
        httppost.setHeader ( "PHPSESSID", "session_id=" + getSessionId () );

        DefaultHttpClient client = ( DefaultHttpClient ) WebClientDevWrapper.getNewHttpClient ();
        List < NameValuePair > pairs = new ArrayList < NameValuePair > ();
        System.out.println ( "POST: to " + uriString + "\ndata: " + content.toString () );
        pairs.add ( new BasicNameValuePair ( "data", content.toString () ) );
        HttpEntity entity = new UrlEncodedFormEntity ( pairs );
        httppost.setEntity ( entity );

        final HttpResponse response = client.execute ( httppost );
        HttpEntity responseEntity = response.getEntity ();
        final String responseText = new String ( EntityUtils.toString ( responseEntity ) );
        System.out.println ( "RESPONSE: " + responseText );
        checkErrors ( responseText );
        return responseText;
    }

    public static String multipartPostRequest ( String uriString, byte[] data ) throws Exception
    {
        String boundary = "qwerty";
        URI uri = new URI ( uriString );
        DefaultHttpClient client = ( DefaultHttpClient ) WebClientDevWrapper.getNewHttpClient ();
        HttpPost httppost = new HttpPost ( uri );
        httppost.setHeader ( "Content-Type", "multipart/form-data; boundary=" + boundary );
        MultipartEntity entity = new MultipartEntity ( HttpMultipartMode.STRICT, boundary, Charset.forName ( "UTF-8" ) );
        entity.addPart ( "userId", new StringBody ( APIManager.getInstance ().getUserId () ) );
        entity.addPart ( "media", new ByteArrayBody ( data, "application/octet-stream", "media" ) );
        httppost.setEntity ( entity );
        System.out.println ( "POST: to " + uriString + "\ndata length: " + data.length );
        BasicHttpResponse response = ( BasicHttpResponse ) client.execute ( httppost );
        HttpEntity responseEntity = response.getEntity ();
        final String responseText = new String ( EntityUtils.toString ( responseEntity ) );
        System.out.println ( "RESPONSE: " + responseText );
        return responseText;
    }

    public static String getSessionId ()
    {
        String sessionId = APIManager.getInstance ().getUser ().getSessionId ();
        return sessionId;
    }

    public static String multipartPostRequest ( String uriString, JSONObject content, List < Media > medias ) throws Exception
    {
        String boundary = "qwerty";
        URI uri = new URI ( uriString );
        DefaultHttpClient client = ( DefaultHttpClient ) WebClientDevWrapper.getNewHttpClient ();
        HttpPost httppost = new HttpPost ( uri );
        httppost.setHeader ( "Content-Type", "multipart/form-data; boundary=" + boundary );
        httppost.setHeader ( "Content-Type", "multipart/form-data; boundary=" + boundary );
        MultipartEntity entity = new MultipartEntity ( HttpMultipartMode.STRICT, boundary, Charset.forName ( "UTF-8" ) );
        entity.addPart ( "data", new StringBody ( content.toString () ) );

        for ( Media media : medias )
        {
            Bitmap bitmap = media.getBitmap ();

            ByteArrayOutputStream stream = new ByteArrayOutputStream ();
            bitmap.compress ( Bitmap.CompressFormat.PNG, 100, stream );
            byte[] byteArray = stream.toByteArray ();

            entity.addPart ( media.getKey (), new ByteArrayBody ( byteArray, "application/octet-stream", media.getKey () ) );
        }

        httppost.setEntity ( entity );
        BasicHttpResponse response = ( BasicHttpResponse ) client.execute ( httppost );
        HttpEntity responseEntity = response.getEntity ();
        final String responseText = new String ( EntityUtils.toString ( responseEntity ) );
        return responseText;
    }

    public static String restorePassswordRequets ( String email ) throws Exception
    {
        JSONObject jsonObject = new JSONObject ();
        jsonObject.accumulate ( "email", email );
        final String responseText = ServerManager.postRequest ( ServerManager.RESTORE_PASSWORD_URI, jsonObject );
        JSONObject responseObject = new JSONObject ( responseText );
        String resultStatus = responseObject.getString ( "parameters" );
        return resultStatus;
    }

    public static String getJobTypes () throws Exception
    {
        return getRequest ( JOBTYPE_GET );
    }

    public static void checkErrors ( String jsonString ) throws Exception
    {
        JSONObject jsonObject = new JSONObject ( jsonString );
        String error = jsonObject.getString ( "parameters" );
        if ( !error.isEmpty () )
        {
            throw new Exception ( error );
        }
    }

    public static JSONArray getList ( String jsonString ) throws Exception
    {
        JSONObject jsonObject = new JSONObject ( jsonString );
        Object listObject = jsonObject.get ( "list" );
        if ( listObject instanceof String )
        {
            return new JSONArray ( ( String ) listObject );
        } else if ( listObject instanceof JSONArray )
        {
            return ( JSONArray ) listObject;
        }
        return null;
    }

    public static JSONObject getObject ( String responseText ) throws Exception
    {
        JSONObject jsonObject = new JSONObject ( responseText );
        String data = jsonObject.getString ( "object" );
        if ( data.startsWith ( "[" ) )
        {
            data = data.substring ( 1, data.length () - 1 );
        }
        JSONObject object = new JSONObject ( data );
        return object;
    }
}
