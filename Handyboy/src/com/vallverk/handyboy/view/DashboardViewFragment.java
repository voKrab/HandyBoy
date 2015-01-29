package com.vallverk.handyboy.view;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserDetailsAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.CustomSwitcher;
import com.vallverk.handyboy.view.base.CustomSwitcher.OnSwitchedChangeListener;
import com.vallverk.handyboy.view.base.DownloadableImageView.Quality;
import com.vallverk.handyboy.view.base.FontUtils.FontStyle;
import com.vallverk.handyboy.view.base.FontUtils;

public class DashboardViewFragment extends BaseFragment
{
	private CustomSwitcher customSwitcher;
	private ImageView menuImageView;

	private TextView line1TextView;
	private TextView line2TextView;

	private Button weeklyScheduleButton;
	private Button customScheduleButton;
	private Button yourMoneyButton;
	private Button jobDescriptionsButton;

	private APIManager apiManager;
	private UserAPIObject user;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.service_dashboard_layout, null );
		customSwitcher = ( CustomSwitcher ) view.findViewById ( R.id.onOffSwitcer );
		menuImageView = ( ImageView ) view.findViewById ( R.id.menuImageView );

		line1TextView = ( TextView ) view.findViewById ( R.id.line1TextView );
		line2TextView = ( TextView ) view.findViewById ( R.id.line2TextView );

		weeklyScheduleButton = ( Button ) view.findViewById ( R.id.weeklyScheduleButton );
		customScheduleButton = ( Button ) view.findViewById ( R.id.customScheduleButton );
		yourMoneyButton = ( Button ) view.findViewById ( R.id.yourMoneyButton );
		jobDescriptionsButton = ( Button ) view.findViewById ( R.id.jobDescriptionsButton );

		return view;
	}

	@Override
	protected void init ()
	{
		new AsyncTask < Void, Void, Integer > ()
		{

			@Override
			protected Integer doInBackground ( Void... params )
			{
				int discount = 0;
				try
				{
					String request = ServerManager.getRequest ( ServerManager.GET_AVAILABLE_NOW + user.getString ( UserParams.SERVICE_ID ) );
					JSONObject jsonRequest = new JSONObject ( request );
					JSONObject answer = new JSONObject ( jsonRequest.getString ( "object" ) );
					discount = answer.getInt ( "discount" );
				} catch ( Exception e )
				{
					e.printStackTrace ();
				}
				return discount;
			}

			public void onPreExecute ()
			{
				super.onPreExecute ();
				//controller.showLoader ();
			}

			public void onPostExecute ( Integer discount )
			{
				super.onPostExecute ( discount );
				//controller.hideLoader ();
				if ( discount == 0 )
				{
					if ( customSwitcher.isActive () )
					{
						customSwitcher.setActive ( false, false );
					}
				} else
				{
					if ( !customSwitcher.isActive () )
					{
						customSwitcher.setActive ( true, false );
					}
				}
			}
		}.execute ();
	}

	private void sendAvailableNow ()
	{
		new AsyncTask < Void, Void, String > ()
		{
			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					JSONObject postParameters = new JSONObject ();
					postParameters.accumulate ( UserParams.SERVICE_ID.toString (), user.getString ( UserParams.SERVICE_ID ) );
					postParameters.accumulate ( "discount", 0 );
					String request = ServerManager.postRequest ( ServerManager.ADD_AVAILABLE_NOW, postParameters );
					JSONObject jsonRequest = new JSONObject ( request );
					result = jsonRequest.getString ( "parameters" );
				} catch ( Exception e )
				{
					result = e.getMessage ();
					e.printStackTrace ();
				}
				return result;
			}

			public void onPreExecute ()
			{
				super.onPreExecute ();
				controller.showLoader ();
			}

			public void onPostExecute ( String result )
			{
				super.onPostExecute ( result );
				controller.hideLoader ();
				if ( !result.isEmpty () )
				{
					Toast.makeText ( controller, result, Toast.LENGTH_LONG ).show ();
				}
			}
		}.execute ();
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
        controller.setSwipeEnabled(true);
		apiManager = APIManager.getInstance ();
		user = apiManager.getUser ();
		setListeners ();
		updateViews ();
	}

	private void updateViews ()
	{
		FontUtils.getInstance ( getActivity () ).applyStyle ( line1TextView, FontStyle.EXTRA_BOLD );
		FontUtils.getInstance ( getActivity () ).applyStyle ( line2TextView, FontStyle.REGULAR );

		FontUtils.getInstance ( getActivity () ).applyStyle ( weeklyScheduleButton, FontStyle.SEMIBOLD );
		FontUtils.getInstance ( getActivity () ).applyStyle ( customScheduleButton, FontStyle.SEMIBOLD );
		FontUtils.getInstance ( getActivity () ).applyStyle ( yourMoneyButton, FontStyle.SEMIBOLD );
		FontUtils.getInstance ( getActivity () ).applyStyle ( jobDescriptionsButton, FontStyle.SEMIBOLD );
	}

	private void setListeners ()
	{
		yourMoneyButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.setState ( VIEW_STATE.YOUR_MONEY );
			}
		} );
		weeklyScheduleButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.setState ( VIEW_STATE.WEEKLY_SCHEDULE );
			}
		} );
		customScheduleButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.setState ( VIEW_STATE.CUSTOM_SCHEDULE );
			}
		} );
		jobDescriptionsButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.setState ( VIEW_STATE.JOB_DESCRIPTIONS );
			}
		} );
		customSwitcher.setOnSwitchedChangeListener ( new OnSwitchedChangeListener ()
		{

			@Override
			public void onSwitchChange ( boolean isActive )
			{
				if ( isActive )
				{
					controller.setState ( VIEW_STATE.AVAILABLE_NOW );
				} else
				{
					sendAvailableNow ();
				}
			}
		} );

		menuImageView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				controller.openMenu ();
			}
		} );
	}
}