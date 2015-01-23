package com.vokrab.test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity
{

	private Button postButton;
	private TextView resultTextView;

	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate ( savedInstanceState );
		setContentView ( R.layout.activity_main );

		postButton = ( Button ) findViewById ( R.id.postButton );
		resultTextView = ( TextView ) findViewById ( R.id.resultTextView );

		postButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				new Thread ( new Runnable ()
				{
					@Override
					public void run ()
					{
						try
						{
							URI uri = new URI ( "http://142.4.217.86/user/auth" );

							HttpPost httppost = new HttpPost ( uri );
							HttpClient client = new DefaultHttpClient ();
							JSONObject jsonObject = new JSONObject ();
							jsonObject.accumulate ( "email", "email" );
							jsonObject.accumulate ( "password", "password" );
							List < NameValuePair > pairs = new ArrayList < NameValuePair > ();
							pairs.add ( new BasicNameValuePair ( "data", jsonObject.toString () ) );
							HttpEntity entity = new UrlEncodedFormEntity ( pairs );
							httppost.setEntity ( entity );

							final HttpResponse response = client.execute ( httppost );
							HttpEntity responseEntity = response.getEntity ();
							final String responseText = new String ( EntityUtils.toString ( responseEntity ) );
							resultTextView.post ( new Runnable ()
							{
								@Override
								public void run ()
								{
									resultTextView.setText ( response.getStatusLine () + "\n" + responseText );
								}
							} );
						} catch ( Exception ex )
						{
							ex.printStackTrace ();
						}
					}
				} ).start ();
			}
		} );
	}
}
