package com.vallverk.handyboy.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.MyMoneyAPIObject;
import com.vallverk.handyboy.model.api.MyMoneyAPIObject.MyMoneyParams;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.BaseListFragment;
import com.vallverk.handyboy.view.base.BaseListFragment.Refresher;

public class YourMoneyViewFragment extends BaseFragment
{
	private View backImageView;
	private View backTextView;
	private ImageView strelkaImageView;
	private BaseListFragment moneyListViewFragment;

	private int weekTime;
	private APIManager apiManager;
	private UserAPIObject user;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.your_money_layout, null );

		backImageView = view.findViewById ( R.id.backImageView );
		backTextView = view.findViewById ( R.id.backTextView );
		strelkaImageView = ( ImageView ) view.findViewById ( R.id.strelkaImageView );
		return view;
	}

	private int hoursToDegree ( int weekTime )
	{
		if ( weekTime >= 0 && weekTime <= 10 )
		{
			return weekTime * 9;
		} else if ( weekTime >= 10 && weekTime <= 20 )
		{
			return 90 + ( weekTime * 3 );
		} else if ( weekTime > 20 )
		{
			return 150 + ( weekTime / 2 );
		}
		return 0;
	}

	private void go ()
	{
		RotateAnimation anim = new RotateAnimation ( 0f, 180.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, 0.5f );
		anim.setInterpolator ( new LinearInterpolator () );
		anim.setDuration ( 500 );
		anim.setAnimationListener ( new AnimationListener ()
		{
			@Override
			public void onAnimationStart ( Animation animation )
			{
			}

			@Override
			public void onAnimationRepeat ( Animation animation )
			{
			}

			@Override
			public void onAnimationEnd ( Animation animation )
			{
				RotateAnimation anim = new RotateAnimation ( 180f, hoursToDegree ( weekTime ), Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, 0.5f );
				anim.setInterpolator ( new LinearInterpolator () );
				anim.setDuration ( 500 );
				anim.setAnimationListener ( new AnimationListener ()
				{

					@Override
					public void onAnimationStart ( Animation animation )
					{
					}

					@Override
					public void onAnimationRepeat ( Animation animation )
					{
					}

					@Override
					public void onAnimationEnd ( Animation animation )
					{
						RotateAnimation anim = new RotateAnimation ( hoursToDegree ( weekTime ) - 1, hoursToDegree ( weekTime ) + 1, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, 0.5f );
						anim.setInterpolator ( new LinearInterpolator () );
						anim.setDuration ( 100 );
						anim.setRepeatCount ( 1000000 );
						anim.setRepeatMode ( Animation.REVERSE );
						strelkaImageView.setAnimation ( anim );
						strelkaImageView.startAnimation ( anim );
					}
				} );
				strelkaImageView.setAnimation ( anim );
				strelkaImageView.startAnimation ( anim );
			}
		} );
		strelkaImageView.setAnimation ( anim );
		strelkaImageView.startAnimation ( anim );
	}

	@Override
	protected void init ()
	{
		new AsyncTask < Void, Void, String > ()
		{
			public void onPreExecute ()
			{
				super.onPreExecute ();
			}

			public void onPostExecute ( String result )
			{
				super.onPostExecute ( result );
				if ( result.isEmpty () )
				{
					go ();
				} else
				{
					controller.onBackPressed ();
					Toast.makeText ( controller, result, Toast.LENGTH_LONG ).show ();
				}
			}

			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					String request = ServerManager.getRequest ( ServerManager.GET_WEEK_TIME + user.getString ( UserParams.SERVICE_ID ) );
					JSONObject jsonRequest = new JSONObject ( request );
					weekTime = jsonRequest.getInt ( "hours" );
					result = jsonRequest.getString ( "parameters" );
				} catch ( Exception ex )
				{
					result = ex.getMessage ();
					ex.printStackTrace ();
				}
				return result;
			}
		}.execute ();
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );

		apiManager = APIManager.getInstance ();
		user = apiManager.getUser ();

		moneyListViewFragment = ( BaseListFragment ) getActivity ().getSupportFragmentManager ().findFragmentById ( R.id.moneyListViewFragment );
		moneyListViewFragment.setIsShowEmpty ( false );
		moneyListViewFragment.setAdapter ( new MoneyListAdapter ( controller ) );
		moneyListViewFragment.setRefresher ( new Refresher ()
		{
			@Override
			public List < Object > refresh () throws Exception
			{
				return APIManager.getInstance ().loadList ( ServerManager.GET_MY_MONEY + user.getId (), MyMoneyAPIObject.class );
			}
		} );
		moneyListViewFragment.refreshData ();
		addListeners ();
	}

	protected void addListeners ()
	{
		backImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.onBackPressed ();
			}
		} );
		backTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.onBackPressed ();
			}
		} );
	}

	public class MoneyListAdapter extends ArrayAdapter < Object >
	{
		private LayoutInflater inflater;

		public MoneyListAdapter ( Context context )
		{
			super ( context, 0 );
			inflater = LayoutInflater.from ( context );
		}

		public View getView ( int position, View view, ViewGroup parent )
		{
			MyMoneyAPIObject myMoneyAPIObject = (MyMoneyAPIObject)getItem ( position );
			if ( view == null )
			{
				view = inflater.inflate ( R.layout.money_list_item_layout, parent, false );
			}
			
			Date date =  Tools.fromStringToDate ( myMoneyAPIObject.getString ( MyMoneyParams.CREATED_AT ), "yyyy-MM-dd hh:mm:ss" );
			((TextView) view.findViewById ( R.id.dayTextView )).setText ( Tools.fromDateToString ( date, "EEEE" ));
			((TextView) view.findViewById ( R.id.monthDayTextView )).setText (Tools.fromDateToString ( date, "MM/dd" ));
			return view;
		}
	}

}