package com.vallverk.handyboy.model.api;

import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;

import android.os.AsyncTask;

public class Tester
{
	public void work ()
	{
		new AsyncTask < Void, Void, Void > ()
		{
			@Override
			protected Void doInBackground ( Void... params )
			{
				try
				{
					UserAPIObject user = new UserAPIObject ( "123456", "vokrab@gmail.com" );
					user.login ();
					APIManager apiManager = new APIManager ();
					UserDetailsAPIObject userDetails = ( UserDetailsAPIObject ) apiManager.getAPIObject ( ( String ) user.getValue ( UserParams.SERVICE_ID ), UserDetailsAPIObject.class );
					//apiManager.putAPIObject ( userDetails );
				}
				catch ( Exception ex )
				{
					ex.printStackTrace ();
				}
				return null;
			}
		}.execute ();
	}
}
