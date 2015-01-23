package com.vallverk.handyboy.view;

import java.util.Timer;
import java.util.TimerTask;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.view.base.BaseFragment;

public class SplashViewFragment extends BaseFragment
{
	private static long DELAY_TIME = 1500;
	
	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.splash_layout, null );
		return view;
	}
	
	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		
		Timer timer = new Timer ();
		timer.schedule ( new TimerTask ()
		{
			
			@Override
			public void run ()
			{
				login ();
			}
		}, DELAY_TIME );
	}

	protected void login ()
	{
		new AsyncTask < Void, Void, String > ()
		{
			public void onPostExecute ( String result )
			{
				super.onPostExecute ( result );
				if ( result.isEmpty () ) //success login
				{
					MainActivity.getInstance ().enter ( false );
				} else
				{
					MainActivity.getInstance ().setState ( VIEW_STATE.LOGIN );
				}
			}
			
			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					UserAPIObject user = APIManager.getInstance ().getUser ();
					String loginResult = user.login ();
					if ( !loginResult.isEmpty () )
					{
						result = loginResult;
					}
				} catch ( Exception ex )
				{
					result = getActivity ().getString ( R.string.error );
					ex.printStackTrace ();
				}
				return result;
			}
		}.execute ();
	}
}