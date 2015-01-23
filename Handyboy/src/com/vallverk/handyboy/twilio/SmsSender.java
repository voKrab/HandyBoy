package com.vallverk.handyboy.twilio;

import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import com.vallverk.handyboy.FileManager;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.VerificationCode;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class SmsSender
{

	/* Find your sid and token at twilio.com/user/account */
	public static final String ACCOUNT_SID = "ACf41156763a2f01ad9b005da6793ce31b";
	public static final String AUTH_TOKEN = "bb45bc11b6710410dd6da2da277f473d";
	public static final String NUMBER = "+13238922279"; 
	public static int SMS_LIMIT_PER_DAY = 10000;
	
	public static void send ( String to, VerificationCode verificationCode ) throws Exception
	{
		List < Long > sendedTimes = ( List < Long > ) FileManager.loadObject ( MainActivity.getInstance (), FileManager.VERIFICATION_SMS_SENDED_TIMES_SAVE_PATH, new ArrayList < Long > () );
		//remove times > 24 hours
		long day = 1000 * 60 * 60 * 24;
		long now = System.currentTimeMillis ();
		for ( int i = 0; i < sendedTimes.size (); i++ )
		{
			long time = sendedTimes.get ( i );
			if ( now - time > day )
			{
				sendedTimes.remove ( i );
				i--;
			}
		}
		if ( sendedTimes.size () >= SMS_LIMIT_PER_DAY )
		{
			throw new Exception ( MainActivity.getInstance ().getString ( R.string.your_sms_limit_per_day_is_maxed ) );
		}
		TwilioRestClient client = new TwilioRestClient ( ACCOUNT_SID, AUTH_TOKEN );
		Account account = client.getAccount ();
		MessageFactory messageFactory = account.getMessageFactory ();
		List < NameValuePair > params = new ArrayList < NameValuePair > ();
		params.add ( new BasicNameValuePair ( "To", to ) );
		params.add ( new BasicNameValuePair ( "From", NUMBER ) );
		params.add ( new BasicNameValuePair ( "Body", "Your code is " + verificationCode.getCode () ) );
		Message sms = messageFactory.create ( params );
		sendedTimes.add ( System.currentTimeMillis () );
		FileManager.saveObject ( MainActivity.getInstance (), FileManager.VERIFICATION_SMS_SENDED_TIMES_SAVE_PATH, sendedTimes );
	}
}