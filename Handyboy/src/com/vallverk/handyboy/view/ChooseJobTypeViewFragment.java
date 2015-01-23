package com.vallverk.handyboy.view;

import java.util.List;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.CommunicationManager;
import com.vallverk.handyboy.model.FilterManager.SearchType;
import com.vallverk.handyboy.model.job.JobCategory;
import com.vallverk.handyboy.model.job.TypeJob;
import com.vallverk.handyboy.view.feed.FeedViewFragment.CommunicationSearch;
import com.vallverk.handyboy.view.base.BaseFragment;

public class ChooseJobTypeViewFragment extends BaseFragment
{
	private LinearLayout container;
	private JobCategory category;
	private List < TypeJob > jobTypes;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.choose_job_type_layout, null );

		this.container = ( LinearLayout ) view.findViewById ( R.id.container );

		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );

		MainActivity controller = MainActivity.getInstance ();
		category = ( JobCategory ) controller.getCommunicationValue ( JobCategory.class.getSimpleName () );
		jobTypes = controller.getJobTypes ( category );
		updateComponents ();
		addListeners ();
	}

	private void updateComponents ()
	{
		// container.removeAllViews ();
		for ( final TypeJob jobType : jobTypes )
		{
			final TextView textView = new TextView ( getActivity () );
			textView.setLayoutParams ( new LayoutParams ( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT ) );
			textView.setText ( jobType.getName () );
			textView.setTextSize ( 28 );
			textView.setTypeface ( textView.getTypeface (), Typeface.BOLD );
			textView.setTextColor ( getActivity ().getResources ().getColor ( R.color.dark_blue ) );
			int padding = Tools.fromDPToPX ( 7, getActivity () );
			textView.setPadding ( padding + Tools.fromDPToPX ( 21, getActivity () ), padding, padding, padding );
			container.addView ( textView );
			final int whiteColor = getActivity ().getResources ().getColor ( R.color.white );
			final int darkBlueColor = getActivity ().getResources ().getColor ( R.color.dark_blue );
			textView.setOnTouchListener ( new OnTouchListener ()
			{
				@Override
				public boolean onTouch ( View view, MotionEvent event )
				{
					if ( event.getAction () == MotionEvent.ACTION_DOWN )
					{
						textView.setTextColor ( whiteColor );
						view.setBackgroundResource ( R.color.red );
					}
					if ( event.getAction () == MotionEvent.ACTION_UP )
					{
						textView.setTextColor ( darkBlueColor );
						view.setBackgroundResource ( R.color.white );
					}
					if ( event.getAction () == MotionEvent.ACTION_CANCEL )
					{
						textView.setTextColor ( darkBlueColor );
						view.setBackgroundResource ( R.color.white );
					}
					return false;
				}
			} );
			textView.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View view )
				{
					CommunicationSearch communicationSearch = new CommunicationSearch ();
					communicationSearch.sort = SearchType.GLOBAL;
					communicationSearch.jobTypeId = String.valueOf ( jobType.getId () );
					communicationSearch.jobTypeName = jobType.getName ();
					controller.setCommunicationValue ( CommunicationSearch.class.getSimpleName (), communicationSearch );
					controller.setState ( VIEW_STATE.FEED );
				}
			} );
		}
	}

	private void addListeners ()
	{
	}

	protected void next ( JobCategory partyBoy )
	{
		MainActivity.getInstance ().setState ( VIEW_STATE.CHOOSE_JOB_TYPE );
	}
}