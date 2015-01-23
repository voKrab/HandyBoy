package com.vallverk.handyboy.view.schedule;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.schedule.ScheduleManager;
import com.vallverk.handyboy.model.schedule.ScheduleManager.Day;

public class CustomScheduleViewFragment extends WeeklyScheduleViewFragment
{
	protected DateChooserView dateChooserView;
	protected boolean isInited;
	protected CheckBox locationCheckBox;
	protected AutoCompleteTextView locationEditText;

	@Override
	protected int getLayoutId ()
	{
		return R.layout.custom_schedule_layout;
	}
	
	@Override
	protected void initDateComponents ()
	{
		dateChooserView = ( DateChooserView ) view.findViewById ( R.id.dateChooserView );
		locationCheckBox = ( CheckBox ) view.findViewById ( R.id.locationCheckBox );
		locationEditText = ( AutoCompleteTextView ) view.findViewById ( R.id.locationEditText );
	}
	
	@Override
	protected void updateSizesOfDateComponents ()
	{
		
	}
	
	@Override
	protected void addListeners ()
	{
		super.addListeners ();
		
		if ( locationCheckBox == null )
		{
			return;
		}
		locationCheckBox.setOnCheckedChangeListener ( new OnCheckedChangeListener ()
		{
			@Override
			public void onCheckedChanged ( CompoundButton buttonView, boolean isChecked )
			{
				locationEditText.setVisibility ( isChecked ? View.VISIBLE : View.GONE );
			}
		});
	}
	
	@Override
	protected void addDateComponentsListeners ()
	{
		dateChooserView.setOnDateChangeListener ( new Handler ()
		{
			public void dispatchMessage ( Message message )
			{
				init ();
			}
		});
	}
	
	@Override
	protected void init ()
	{
		segmentStartItem = null;
		scheduleManager = ScheduleManager.getInstance ();
		new AsyncTask < Void, Void, String > ()
		{
			public void onPreExecute ()
			{
				super.onPreExecute ();
				controller.showLoader ();
			}

			public void onPostExecute ( String result )
			{
				super.onPostExecute ( result );
				controller.hideLoader ();
				if ( result.isEmpty () )
				{
					updateComponents ();
					if ( !isInited )
					{
						addListeners ();
					}
				} else
				{
					controller.onBackPressed ();
					Toast.makeText ( controller, result, Toast.LENGTH_LONG ).show ();
				}
				isInited = true;
			}

			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					scheduleManager.updateFromServer ( dateChooserView.getSelectedDate () );
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
	protected void saveSchedule ()
	{
		new AsyncTask < Void, Void, String > ()
		{
			public void onPreExecute ()
			{
				super.onPreExecute ();
				controller.showLoader ();
			}

			public void onPostExecute ( String result )
			{
				super.onPostExecute ( result );
				controller.hideLoader ();
				if ( result.isEmpty () )
				{
				} else
				{
					Toast.makeText ( controller, result, Toast.LENGTH_LONG ).show ();
				}
			}

			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					scheduleManager.uploadToServer ( dateChooserView.getSelectedDate () );
				} catch ( Exception ex )
				{
					ex.printStackTrace ();
					result = ex.getMessage ();
				}
				return result;
			}
		}.execute ();
	}
	
	@Override
	protected Day getStartDay ()
	{
		return null;
	}
	
	@Override
	protected String getScreenName ()
	{
		return controller.getString ( R.string.customize );
	}
}