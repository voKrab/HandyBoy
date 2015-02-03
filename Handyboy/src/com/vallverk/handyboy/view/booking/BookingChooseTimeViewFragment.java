package com.vallverk.handyboy.view.booking;

import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.schedule.BookingTime;
import com.vallverk.handyboy.model.schedule.ScheduleManager.Day;
import com.vallverk.handyboy.view.controller.BookingController;
import com.vallverk.handyboy.view.schedule.CustomScheduleViewFragment;

public class BookingChooseTimeViewFragment extends CustomScheduleViewFragment
{
	protected BookingTime bookingTime;
	protected BookingController bookingController;

	protected void offComponents ()
	{
		showHideImageView.setVisibility ( View.INVISIBLE );
		descriptionContainer.setVisibility ( View.GONE );
		locationCheckBox.setVisibility ( View.GONE );
		locationEditText.setVisibility ( View.GONE );
        dayOffCheckBox.setVisibility ( View.GONE );
	}

	@Override
	protected void init ()
	{
		segmentStartItem = null;
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
					if ( !isInited )
					{
						bookingController = controller.getBookingController ();
						addCroosImageViewToItems ();
						addListeners ();
					}
					updateComponents ();
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
					bookingTime = new BookingTime ();
					bookingTime.updateFromServer ( dateChooserView.getSelectedDate () );
				} catch ( Exception ex )
				{
					result = ex.getMessage ();
					ex.printStackTrace ();
				}
				return result;
			}
		}.execute ();
	}

	protected void addCroosImageViewToItems ()
	{
		for ( int i = 0; i < items.length; i++ )
		{
			for ( int j = 0; j < items[i].length; j++ )
			{
				LinearLayout item = items[i][j];
				ImageView imageView = new ImageView ( controller );
				imageView.setImageResource ( R.drawable.cross_light_in_search_na );
				item.setGravity ( Gravity.CENTER );
				items[i][j].setBackgroundResource ( R.color.white );
				item.addView ( imageView );
			}
		}
	}

	@Override
	protected void saveSchedule ()
	{
		if ( !bookingTime.isEmpty () )
		{
			bookingTime.setDate ( dateChooserView.getSelectedDate () );
			bookingController.setBookingTime ( bookingTime );
		}
		else
		{
			bookingController.setBookingTime ( null );
		}
		controller.onBackPressed ();
	}

	@Override
	protected Day getStartDay ()
	{
		return null;
	}

	@Override
	protected String getScreenName ()
	{
		return controller.getString ( R.string.what_time );
	}

	@Override
	protected void updateItems ()
	{
		int[][] itemsMap = bookingTime.getItems ();
		for ( int i = 0; i < itemsMap.length; i++ )
		{
			for ( int j = 0; j < itemsMap[i].length; j++ )
			{
				LinearLayout item = items[i][j];
				if ( itemsMap[i][j] == 0 )
				{
					onOffItems ( item, false );
					item.setBackgroundResource ( android.R.color.transparent );
				} else
				{
					onOffItems ( item, true );
					item.setBackgroundResource ( itemsMap[i][j] == 1 ? R.color.red : R.color.dark_blue );
				}
			}
		}
	}

	private void onOffItems ( LinearLayout item, boolean isTextVisible )
	{
		for ( int i = 0; i < item.getChildCount (); i++ )
		{
			View view = item.getChildAt ( i );
			if ( view instanceof TextView )
			{
				view.setVisibility ( isTextVisible ? View.VISIBLE : View.GONE );
			} else if ( view instanceof ImageView )
			{
				view.setVisibility ( isTextVisible ? View.GONE : View.VISIBLE );
			}
		}
	}

	@Override
	protected int[][] getBridges ()
	{
		return bookingTime.getBridges ();
	}

	@Override
	protected boolean isSelected ( int row, int col )
	{
		return bookingTime.isSelected ( row, col );
	}

	@Override
	protected void setSelected ( int row, int col, boolean isSelected )
	{
		bookingTime.setSelected ( row, col, isSelected );
	}

	@Override
	protected void removeWindow ( int row, int col )
	{
		bookingTime.removeWindow ( row, col );
	}

	@Override
	protected boolean isDisabled ( int row, int col )
	{
		if ( bookingTime.isDisabled ( row, col ) )
		{
			return true;
		} else
		{
			if ( bookingTime.getWorkWindowCount () > 0 )
			{
				Toast.makeText ( controller, R.string.you_can_not_select_more_than_one_working_window, Toast.LENGTH_LONG ).show ();
				return true;
			} else
			{
				return false;
			}
		}
	}
}