package com.vallverk.handyboy.view.schedule;

import android.location.Address;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.AddressWraper;
import com.vallverk.handyboy.model.schedule.ScheduleForDay;
import com.vallverk.handyboy.model.schedule.ScheduleManager;
import com.vallverk.handyboy.model.schedule.ScheduleManager.Day;
import com.vallverk.handyboy.view.AddressAutocompleteAdapter;
import com.vallverk.handyboy.view.base.AutocompleteAdapter;

public class CustomScheduleViewFragment extends WeeklyScheduleViewFragment
{
	protected DateChooserView dateChooserView;
	protected boolean isInited;
	protected CheckBox locationCheckBox;
	protected AutoCompleteTextView locationEditText;
	protected CheckBox dayOffCheckBox;
	protected ScheduleForDay scheduleForDay;

	@Override
	protected int getLayoutId ()
	{
		return R.layout.custom_schedule_layout;
	}

	@Override
	protected void initDateComponents ()
	{
		dateChooserView = ( DateChooserView ) view.findViewById ( R.id.dateChooserView );
		dayOffCheckBox = ( CheckBox ) view.findViewById ( R.id.dayOffCheckBox );
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
				if ( !isChecked )
				{
					locationEditText.setText ( "" );
					scheduleForDay.setAddress ( null );
				}
			}
		} );
		locationEditText.setOnItemClickListener ( new AdapterView.OnItemClickListener ()
		{
			public void onItemClick ( AdapterView < ? > arg0, View arg1, int position, long arg3 )
			{
				AutocompleteAdapter adapter = ( AutocompleteAdapter ) arg0.getAdapter ();
				AddressWraper addressWraper = ( AddressWraper ) adapter.getValue ( position );
				Address location = addressWraper.getAddress ();
				scheduleForDay.setAddress ( location );
			}
		} );
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
		} );
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
					scheduleForDay = scheduleManager.getCustomDaySchedule ();
				} catch ( Exception ex )
				{
					result = ex.getMessage ();
					ex.printStackTrace ();
				}
				return result;
			}
		}.execute ();
	}

	protected void updateComponents ()
	{
		if ( scheduleManager != null )
		{
			dayOffCheckBox.setChecked ( scheduleManager.isDayOff () );
			locationEditText.setAdapter ( new AddressAutocompleteAdapter ( getActivity (), locationEditText.getText ().toString () ) );
			Address address = scheduleForDay.getAddress ();
			locationCheckBox.setChecked ( address == null ? false : true );
			locationEditText.setText ( address == null ? "" : new AddressWraper ( address ).toString () );
		}
		super.updateComponents ();
	}

	@Override
	protected void saveSchedule ()
	{
		if ( locationCheckBox.isChecked () )
		{
			if ( scheduleForDay.getAddress () == null )
			{
				Toast.makeText ( controller, controller.getString ( R.string.setup_work_out_location ), Toast.LENGTH_LONG ).show ();
				return;
			}
		}
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
					scheduleManager.uploadToServer ( dateChooserView.getSelectedDate (), dayOffCheckBox.isChecked () );
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