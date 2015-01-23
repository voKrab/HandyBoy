package com.vallverk.handyboy.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.job.JobCategory;
import com.vallverk.handyboy.view.base.BaseFragment;

public class ChooseCategoryViewFragment extends BaseFragment
{
	private LinearLayout partyContainer;
	private LinearLayout homeContainer;
	private LinearLayout muscleContainer;
	private LinearLayout prettyContainer;
	private TextView partyTextView1;
	private TextView partyTextView2;
	private TextView homeTextView1;
	private TextView homeTextView2;
	private TextView muscleTextView1;
	private TextView muscleTextView2;
	private TextView prettyTextView1;
	private TextView prettyTextView2;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.choose_category_layout, null );
		partyContainer = ( LinearLayout ) view.findViewById ( R.id.partyContainer );
		homeContainer = ( LinearLayout ) view.findViewById ( R.id.homeContainer );
		muscleContainer = ( LinearLayout ) view.findViewById ( R.id.muscleContainer );
		prettyContainer = ( LinearLayout ) view.findViewById ( R.id.prettyContainer );
		partyTextView1 = ( TextView ) view.findViewById ( R.id.partyTextView1 );
		partyTextView2 = ( TextView ) view.findViewById ( R.id.partyTextView2 );
		homeTextView1 = ( TextView ) view.findViewById ( R.id.homeTextView1 );
		homeTextView2 = ( TextView ) view.findViewById ( R.id.homeTextView2 );
		muscleTextView1 = ( TextView ) view.findViewById ( R.id.muscleTextView1 );
		muscleTextView2 = ( TextView ) view.findViewById ( R.id.muscleTextView2 );
		prettyTextView1 = ( TextView ) view.findViewById ( R.id.prettyTextView1 );
		prettyTextView2 = ( TextView ) view.findViewById ( R.id.prettyTextView2 );
		return view;
	}
	
	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		
		addListeners ();
	}

	private void addListeners ()
	{
		final int whiteColor = getActivity ().getResources ().getColor ( R.color.white );
		final int redColor = getActivity ().getResources ().getColor ( R.color.red );
		final int darkBlueColor = getActivity ().getResources ().getColor ( R.color.dark_blue );
		partyContainer.setOnTouchListener ( new OnTouchListener ()
		{
			@Override
			public boolean onTouch ( View view, MotionEvent event )
			{
				if ( event.getAction () == MotionEvent.ACTION_DOWN )
				{
					partyTextView1.setTextColor ( whiteColor );
					partyTextView2.setTextColor ( whiteColor );
					view.setBackgroundResource ( R.color.red );
				}
				if ( event.getAction () == MotionEvent.ACTION_UP )
				{
					partyTextView1.setTextColor ( darkBlueColor );
					partyTextView2.setTextColor ( darkBlueColor );
					view.setBackgroundResource ( R.color.white );
				}
				if ( event.getAction () == MotionEvent.ACTION_CANCEL )
				{
					partyTextView1.setTextColor ( darkBlueColor );
					partyTextView2.setTextColor ( darkBlueColor );
					view.setBackgroundResource ( R.color.white );
				}
				return false;
			}
		} );
		
		homeContainer.setOnTouchListener ( new OnTouchListener ()
		{
			@Override
			public boolean onTouch ( View view, MotionEvent event )
			{
				if ( event.getAction () == MotionEvent.ACTION_DOWN )
				{
					homeTextView1.setTextColor ( whiteColor );
					homeTextView2.setTextColor ( whiteColor );
					view.setBackgroundResource ( R.color.red );
				}
				if ( event.getAction () == MotionEvent.ACTION_UP )
				{
					homeTextView1.setTextColor ( darkBlueColor );
					homeTextView2.setTextColor ( darkBlueColor );
					view.setBackgroundResource ( R.color.white );
				}
				if ( event.getAction () == MotionEvent.ACTION_CANCEL )
				{
					homeTextView1.setTextColor ( darkBlueColor );
					homeTextView2.setTextColor ( darkBlueColor );
					view.setBackgroundResource ( R.color.white );
				}
				return false;
			}
		} );
		
		muscleContainer.setOnTouchListener ( new OnTouchListener ()
		{
			@Override
			public boolean onTouch ( View view, MotionEvent event )
			{
				if ( event.getAction () == MotionEvent.ACTION_DOWN )
				{
					muscleTextView1.setTextColor ( whiteColor );
					muscleTextView2.setTextColor ( whiteColor );
					view.setBackgroundResource ( R.color.red );
				}
				if ( event.getAction () == MotionEvent.ACTION_UP )
				{
					muscleTextView1.setTextColor ( darkBlueColor );
					muscleTextView2.setTextColor ( darkBlueColor );
					view.setBackgroundResource ( R.color.white );
				}
				if ( event.getAction () == MotionEvent.ACTION_CANCEL )
				{
					muscleTextView1.setTextColor ( darkBlueColor );
					muscleTextView2.setTextColor ( darkBlueColor );
					view.setBackgroundResource ( R.color.white );
				}
				return false;
			}
		} );
		
		/*prettyContainer.setOnTouchListener ( new OnTouchListener ()
		{
			@Override
			public boolean onTouch ( View view, MotionEvent event )
			{
				if ( event.getAction () == MotionEvent.ACTION_DOWN )
				{
					prettyTextView1.setTextColor ( whiteColor );
					prettyTextView2.setTextColor ( whiteColor );
					view.setBackgroundResource ( R.color.red );
				}
				if ( event.getAction () == MotionEvent.ACTION_UP )
				{
					prettyTextView1.setTextColor ( darkBlueColor );
					prettyTextView2.setTextColor ( darkBlueColor );
					view.setBackgroundResource ( R.color.white );
				}
				if ( event.getAction () == MotionEvent.ACTION_CANCEL )
				{
					prettyTextView1.setTextColor ( darkBlueColor );
					prettyTextView2.setTextColor ( darkBlueColor );
					view.setBackgroundResource ( R.color.white );
				}
				return false;
			}
		} );*/
		
		partyContainer.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				next ( JobCategory.PARTY_BOY );
			}
		} );
		homeContainer.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				next ( JobCategory.HOUSE_BOY );
			}
		} );
		muscleContainer.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				next ( JobCategory.MUSCLE_BOY );
			}
		} );
		/*prettyContainer.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				next ( JobCategory.PRETTY_BOY );
			}
		} );*/
	}

	protected void next ( JobCategory category )
	{
		MainActivity controller = MainActivity.getInstance ();
		controller.setCommunicationValue ( JobCategory.class.getSimpleName (), category );
		controller.setState ( VIEW_STATE.CHOOSE_JOB_TYPE );
	}
}