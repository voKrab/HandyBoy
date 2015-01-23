package com.vallverk.handyboy.view.schedule;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.textservice.TextInfo;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.schedule.ScheduleManager;
import com.vallverk.handyboy.model.schedule.ScheduleManager.Day;
import com.vallverk.handyboy.view.base.BaseFragment;

public class WeeklyScheduleViewFragment extends BaseFragment
{
	protected UserAPIObject user;
	protected View backImageView;
	protected View backTextView;
	private View dayView;
	private View sundayTextView;
	private View mondayTextView;
	private View tuesdayTextView;
	private View wednesdayTextView;
	private View thursdayTextView;
	private View fridayTextView;
	private View saturdayTextView;
	protected Day currentDay;
	protected int duration = 250;
	protected LinearLayout[][] items;
	protected View[][] bridges;
	protected ScheduleManager scheduleManager;
	protected SelectedItem segmentStartItem;
	protected SelectedItem segmentEndItem;
	protected View saveButton;
	private boolean isDescriptionOpen;
	protected ImageView showHideImageView;
	protected View descriptionContainer;
	private TextView screenTextView;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		view = inflater.inflate ( getLayoutId (), null );

		backImageView = view.findViewById ( R.id.backImageView );
		backTextView = view.findViewById ( R.id.backTextView );
		saveButton = view.findViewById ( R.id.saveButton );
		showHideImageView = ( ImageView ) view.findViewById ( R.id.showHideImageView );
		descriptionContainer = view.findViewById ( R.id.descriptionContainer );
		screenTextView = ( TextView ) view.findViewById ( R.id.screenTextView );
		
		initDateComponents ();
		
		ViewGroup viewGroup = ( ViewGroup ) view;
		items = new LinearLayout[10][5];
		bridges = new View[10][4];
		List < LinearLayout > rows = new ArrayList < LinearLayout > ();
		rows.add ( ( LinearLayout ) viewGroup.findViewById ( R.id.row0Container ) );
		rows.add ( ( LinearLayout ) viewGroup.findViewById ( R.id.row1Container ) );
		rows.add ( ( LinearLayout ) viewGroup.findViewById ( R.id.row2Container ) );
		rows.add ( ( LinearLayout ) viewGroup.findViewById ( R.id.row3Container ) );
		rows.add ( ( LinearLayout ) viewGroup.findViewById ( R.id.row4Container ) );
		rows.add ( ( LinearLayout ) viewGroup.findViewById ( R.id.row5Container ) );
		rows.add ( ( LinearLayout ) viewGroup.findViewById ( R.id.row6Container ) );
		rows.add ( ( LinearLayout ) viewGroup.findViewById ( R.id.row7Container ) );
		rows.add ( ( LinearLayout ) viewGroup.findViewById ( R.id.row8Container ) );
		rows.add ( ( LinearLayout ) viewGroup.findViewById ( R.id.row9Container ) );
		for ( int row = 0; row < rows.size (); row++ )
		{
			LinearLayout rowView = rows.get ( row );
			int index1 = 0;
			int index2 = 0;
			for ( int i = 0; i < rowView.getChildCount (); i++ )
			{
				View v = rowView.getChildAt ( i );
				if ( v instanceof LinearLayout )
				{
					items[row][index1++] = ( LinearLayout ) v;
				} else
				{
					bridges[row][index2++] = v;
					v.setVisibility ( View.INVISIBLE );
				}
			}
		}
		updateDescriptionComponents ();
		offComponents ();
		return view;
	}

	protected String getScreenName ()
	{
		return controller.getString ( R.string.schedule );
	}

	protected void offComponents ()
	{
		// TODO Auto-generated method stub
		
	}

	protected int getLayoutId ()
	{
		return R.layout.schedule_layout;
	}

	protected void initDateComponents ()
	{
		dayView = view.findViewById ( R.id.dayView );
		sundayTextView = view.findViewById ( R.id.sundayTextView );
		mondayTextView = view.findViewById ( R.id.mondayTextView );
		tuesdayTextView = view.findViewById ( R.id.tuesdayTextView );
		wednesdayTextView = view.findViewById ( R.id.wednesdayTextView );
		thursdayTextView = view.findViewById ( R.id.thursdayTextView );
		fridayTextView = view.findViewById ( R.id.fridayTextView );
		saturdayTextView = view.findViewById ( R.id.saturdayTextView );
	}

	@Override
	protected void init ()
	{
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
					addListeners ();
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
					scheduleManager.updateFromServer ();
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

		currentDay = getStartDay ();
		user = APIManager.getInstance ().getUser ();
		screenTextView.setText ( getScreenName () );
		updateSizesOfDateComponents ();
		isDescriptionOpen = true;
	}

	private void updateDescriptionComponents ()
	{
		showHideImageView.setImageResource ( isDescriptionOpen ? R.drawable.question_button_hide_selector : R.drawable.question_button_selector );
		descriptionContainer.setVisibility ( isDescriptionOpen ? View.VISIBLE : View.GONE );
	}

	protected Day getStartDay ()
	{
		return Day.SUNDAY;
	}

	protected void updateSizesOfDateComponents ()
	{
		int width = MainActivity.SCREEN_WIDTH / Day.values ().length;
		dayView.getLayoutParams ().width = width;
		sundayTextView.getLayoutParams ().width = width;
		mondayTextView.getLayoutParams ().width = width;
		tuesdayTextView.getLayoutParams ().width = width;
		wednesdayTextView.getLayoutParams ().width = width;
		thursdayTextView.getLayoutParams ().width = width;
		fridayTextView.getLayoutParams ().width = width;
		saturdayTextView.getLayoutParams ().width = width;
	}

	protected void updateComponents ()
	{
		updateItems ();
		updateBridges ();
		if ( segmentStartItem != null )
		{
			segmentStartItem.select ();
		}
	}

	private void updateBridges ()
	{
		int[][] bridgesMap = getBridges ();
		for ( int i = 0; i < bridgesMap.length; i++ )
		{
			for ( int j = 0; j < bridgesMap[i].length; j++ )
			{
				bridges[i][j].setVisibility ( bridgesMap[i][j] == 1 ? View.VISIBLE : View.INVISIBLE );
			}
		}
	}

	protected int[][] getBridges ()
	{
		return scheduleManager.getBridges ( currentDay );
	}

	protected void updateItems ()
	{
		int[][] itemsMap = scheduleManager.getItems ( currentDay );
		for ( int i = 0; i < itemsMap.length; i++ )
		{
			for ( int j = 0; j < itemsMap[i].length; j++ )
			{
				items[i][j].setBackgroundResource ( itemsMap[i][j] == 1 ? R.color.red : R.color.dark_blue );
			}
		}
	}

	protected void addListeners ()
	{
		showHideImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				isDescriptionOpen = !isDescriptionOpen;
				updateDescriptionComponents ();
			}
		} );

		saveButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				saveSchedule ();
			}
		} );

		for ( int i = 0; i < items.length; i++ )
		{
			for ( int j = 0; j < items[i].length; j++ )
			{
				final int row = i;
				final int col = j;
				final View item = items[i][j];
				item.setOnClickListener ( new OnClickListener ()
				{
					@Override
					public void onClick ( View v )
					{
						boolean isSelected = isSelected ( row, col );
						if ( segmentStartItem != null && segmentStartItem.isEquals ( row, col ) )
						{
							setSelected ( row, col, false );
							segmentStartItem = null;
						} else
						{
							if ( isSelected )
							{
								removeWindow ( row, col );
							} else
							{
								if ( isDisabled ( row, col ) )
								{
									return;
								}
								if ( segmentStartItem == null )
								{
									segmentStartItem = new SelectedItem ( row, col, item );
								} else
								{
									segmentEndItem = new SelectedItem ( row, col, item );
									createWorkWindow ();
								}
							}
						}
						updateComponents ();
					}
				} );
			}
		}

		addDateComponentsListeners ();

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

	protected boolean isDisabled ( int row, int col )
	{
		return false;
	}

	protected void removeWindow ( int row, int col )
	{
		scheduleManager.removeWindow ( row, col, currentDay );
	}

	protected void setSelected ( int row, int col, boolean isSelected )
	{
		scheduleManager.setSelected ( row, col, currentDay, isSelected );
	}

	protected boolean isSelected ( int row, int col )
	{
		return scheduleManager.isSelected ( row, col, currentDay );
	}

	protected void addDateComponentsListeners ()
	{
		sundayTextView.setTag ( Day.SUNDAY );
		mondayTextView.setTag ( Day.MONDAY );
		tuesdayTextView.setTag ( Day.TUESDAY );
		wednesdayTextView.setTag ( Day.WEDNESDAY );
		thursdayTextView.setTag ( Day.THURSDAY );
		fridayTextView.setTag ( Day.FRIDAY );
		saturdayTextView.setTag ( Day.SATURDAY );
		OnClickListener listener = new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				Day newDay = ( Day ) v.getTag ();
				if ( currentDay == newDay )
				{
					return;
				}
				currentDay = newDay;
				segmentStartItem = null;
				updateComponents ();
				dayView.animate ().translationX ( v.getX () ).setDuration ( duration ).setListener ( new AnimatorListener ()
				{
					@Override
					public void onAnimationStart ( Animator animation )
					{

					}

					@Override
					public void onAnimationRepeat ( Animator animation )
					{
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd ( Animator animation )
					{

					}

					@Override
					public void onAnimationCancel ( Animator animation )
					{
						// TODO Auto-generated method stub

					}
				} );
			}
		};
		sundayTextView.setOnClickListener ( listener );
		mondayTextView.setOnClickListener ( listener );
		tuesdayTextView.setOnClickListener ( listener );
		wednesdayTextView.setOnClickListener ( listener );
		thursdayTextView.setOnClickListener ( listener );
		fridayTextView.setOnClickListener ( listener );
		saturdayTextView.setOnClickListener ( listener );
	}

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
					scheduleManager.uploadToServer ();
				} catch ( Exception ex )
				{
					ex.printStackTrace ();
					result = ex.getMessage ();
				}
				return result;
			}
		}.execute ();
	}

	protected void createWorkWindow ()
	{
		if ( segmentEndItem.length ( items[0].length ) < segmentStartItem.length ( items[0].length ) )
		{
			SelectedItem temp = new SelectedItem ( segmentStartItem.row, segmentStartItem.col, segmentStartItem.view );
			segmentStartItem = segmentEndItem;
			segmentEndItem = temp;
		}
		for ( int i = segmentStartItem.row; i <= segmentEndItem.row; i++ )
		{
			for ( int j = ( i == segmentStartItem.row ? segmentStartItem.col : 0 ); j <= ( i == segmentEndItem.row ? segmentEndItem.col : items[i].length - 1 ); j++ )
			{
				setSelected ( i, j, true );
			}
		}
		segmentStartItem = null;
		segmentEndItem = null;
	}
}