package com.vallverk.handyboy.view.schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vallverk.handyboy.R;

public class DateChooserView extends LinearLayout
{
	private View prevMonthImageView;
	private View nextMonthImageView;
	private TextView monthTextView;
	private View prevDayImageView;
	private View nextDayImageView;
	private LinearLayout daysContainer;
	private View dayView;
	private ArrayList < TextView > daysList;
	private Date startDate;
	private ArrayList < Date > week;
	private int selectedDateIndex;
	private Handler dateChangeListener;

	public DateChooserView ( Context context )
	{
		super ( context );
		init ( context, null );
	}

	public DateChooserView ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		init ( context, attrs );
	}

	public DateChooserView ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
		init ( context, attrs );
	}

	private void init ( Context context, AttributeSet attrs )
	{
		// TypedArray ta = context.obtainStyledAttributes ( attrs,
		// R.styleable.AddonPriceView, 0, 0 );
		// try
		// {
		// headerLeftPadding = ta.getDimensionPixelSize (
		// R.styleable.AddonPriceView_leftPadding, 0 );
		// price = ta.getInt ( R.styleable.AddonPriceView_cost, 0 );
		// name = ta.getString ( R.styleable.AddonPriceView_name );
		// subName = ta.getString ( R.styleable.AddonPriceView_subName );
		// } finally
		// {
		// ta.recycle ();
		// }
		final LayoutInflater inflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
		final View view = inflater.inflate ( R.layout.calendar_layout, null );
		if ( !isInEditMode () )
		{
			initComponents ( view );
			initStates ();
			post ( new Runnable ()
			{
				@Override
				public void run ()
				{
					updateSizes ();
					updateComponents ();
					addListeners ();
				}
			} );
		}
		addView ( view, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT );
	}

	protected void updateComponents ()
	{
		Calendar calendar = Calendar.getInstance ();
		for ( int i = 0; i < week.size (); i++ )
		{
			TextView dayTextView = daysList.get ( i );
			Date date = week.get ( i );
			calendar.setTime ( date );
			dayTextView.setText ( "" + calendar.get ( Calendar.DAY_OF_MONTH ) );
			dayTextView.setTag ( date );
		}
		final Date selectedDate = week.get ( selectedDateIndex );
		calendar.setTime ( selectedDate );
		monthTextView.setText ( calendar.getDisplayName ( Calendar.MONTH, Calendar.LONG, Locale.US ) + ", " + calendar.get ( Calendar.YEAR ) );
		TextView selectedTextView = daysList.get ( selectedDateIndex );
		int duration = 250;
		dayView.animate ().translationX ( selectedTextView.getX () ).setDuration ( duration ).setListener ( new AnimatorListener ()
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
				if ( dateChangeListener != null )
				{
					dateChangeListener.dispatchMessage ( dateChangeListener.obtainMessage () );
				}
			}

			@Override
			public void onAnimationCancel ( Animator animation )
			{
				// TODO Auto-generated method stub

			}
		} ).start ();

	}

	protected void initStates ()
	{
		selectedDateIndex = 0; // center of week 0, 1, 2, "3", 4, 5, 6
		Calendar calendar = Calendar.getInstance ();
		startDate = calendar.getTime ();
		updateWeek ();
	}

	private void updateWeek ()
	{
		Calendar calendar = Calendar.getInstance ();
		calendar.setTime ( startDate );
		week = new ArrayList < Date > ();
		week.add ( startDate );
		for ( int i = 0; i < 6; i++ )
		{
			calendar.add ( Calendar.DAY_OF_YEAR, 1 );
			Date date = calendar.getTime ();
			week.add ( date );
		}
	}

	protected void addListeners ()
	{
		for ( int i = 0; i < daysList.size (); i++ )
		{
			final int index = i;
			TextView dayTextView = daysList.get ( i );
			dayTextView.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View view )
				{
					if ( selectedDateIndex == index )
					{
						return;
					}
					selectedDateIndex = index;
					updateComponents ();
				}
			} );
		}
		prevDayImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				Calendar calendar = Calendar.getInstance ();
				calendar.setTime ( startDate );
				calendar.add ( Calendar.WEEK_OF_YEAR, -1 );
				if ( isAfterCurrentDate ( calendar.getTime () ) )
				{
					startDate = calendar.getTime ();
				} else
				{
					startDate = new Date ( System.currentTimeMillis () );
				}
				updateWeek ();
				updateComponents ();
			}
		});
		nextDayImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				Calendar calendar = Calendar.getInstance ();
				calendar.setTime ( startDate );
				calendar.add ( Calendar.WEEK_OF_YEAR, 1 );
				if ( isAfterCurrentDate ( calendar.getTime () ) )
				{
					startDate = calendar.getTime ();
				} else
				{
					startDate = new Date ( System.currentTimeMillis () );
				}
				updateWeek ();
				updateComponents ();
			}
		});
		prevMonthImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				Calendar calendar = Calendar.getInstance ();
				calendar.setTime ( startDate );
				calendar.add ( Calendar.MONTH, -1 );
				if ( isAfterCurrentDate ( calendar.getTime () ) )
				{
					startDate = calendar.getTime ();
				} else
				{
					startDate = new Date ( System.currentTimeMillis () );
				}
				updateWeek ();
				updateComponents ();
			}
		});
		nextMonthImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				Calendar calendar = Calendar.getInstance ();
				calendar.setTime ( startDate );
				calendar.add ( Calendar.MONTH, 1 );
				if ( isAfterCurrentDate ( calendar.getTime () ) )
				{
					startDate = calendar.getTime ();
				} else
				{
					startDate = new Date ( System.currentTimeMillis () );
				}
				updateWeek ();
				updateComponents ();
			}
		});
	}

	protected boolean isAfterCurrentDate ( Date time )
	{
		Date now = new Date ( System.currentTimeMillis () );
		Calendar calendar = Calendar.getInstance ();
		calendar.setTime ( now );
		int day1 = calendar.get ( Calendar.DAY_OF_YEAR );
		calendar.setTime ( time );
		int day2 = calendar.get ( Calendar.DAY_OF_YEAR );
		return time.after ( now ) || day1 == day2;
	}

	private void updateSizes ()
	{
		int width = daysContainer.getWidth () / daysList.size ();
		dayView.getLayoutParams ().width = width;
		for ( TextView view : daysList )
		{
			view.getLayoutParams ().width = width;
			( ( LinearLayout.LayoutParams ) view.getLayoutParams () ).weight = 0;
		}
		daysContainer.requestLayout ();
		requestLayout ();
		invalidate ();
	}

	private void initComponents ( View view )
	{
		prevMonthImageView = view.findViewById ( R.id.prevMonthImageView );
		nextMonthImageView = view.findViewById ( R.id.nextMonthImageView );
		monthTextView = ( TextView ) view.findViewById ( R.id.monthTextView );
		prevDayImageView = view.findViewById ( R.id.prevDayImageView );
		nextDayImageView = view.findViewById ( R.id.nextDayImageView );
		daysContainer = ( LinearLayout ) view.findViewById ( R.id.daysContainer );
		dayView = view.findViewById ( R.id.dayView );
		daysList = new ArrayList < TextView > ();
		daysList.add ( ( TextView ) view.findViewById ( R.id._1TextView ) );
		daysList.add ( ( TextView ) view.findViewById ( R.id._2TextView ) );
		daysList.add ( ( TextView ) view.findViewById ( R.id._3TextView ) );
		daysList.add ( ( TextView ) view.findViewById ( R.id._4TextView ) );
		daysList.add ( ( TextView ) view.findViewById ( R.id._5TextView ) );
		daysList.add ( ( TextView ) view.findViewById ( R.id._6TextView ) );
		daysList.add ( ( TextView ) view.findViewById ( R.id._7TextView ) );
	}

	public Date getSelectedDate ()
	{
		return week.get ( selectedDateIndex );
	}

	public void setOnDateChangeListener ( Handler handler )
	{
		this.dateChangeListener = handler;
	}

	public void offSelectors ()
	{
		prevDayImageView.setOnClickListener ( null );
		nextDayImageView.setOnClickListener ( null );
		prevMonthImageView.setOnClickListener ( null );
		nextMonthImageView.setOnClickListener ( null );
		for ( int i = 0; i < daysList.size (); i++ )
		{
			TextView dayTextView = daysList.get ( i );
			dayTextView.setOnClickListener ( null );
		}
	}
}
