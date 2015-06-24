package com.vallverk.handyboy.view;

import java.util.List;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.SettingsManager;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.FilterManager.SearchType;
import com.vallverk.handyboy.model.job.JobCategory;
import com.vallverk.handyboy.model.job.TypeJob;
import com.vallverk.handyboy.view.base.FontUtils;
import com.vallverk.handyboy.view.feed.FeedViewFragment.CommunicationSearch;
import com.vallverk.handyboy.view.base.BaseFragment;

public class ChooseJobTypeViewFragment extends BaseFragment
{
	private LinearLayout container;
	private JobCategory category;
	private List < TypeJob > jobTypes;
    private Dialog getPickyDialog;

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
        controller.setSwipeEnabled(true);
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
			textView.setTextSize ( 27 );
            FontUtils.getInstance(controller).applyStyle(textView, FontUtils.FontStyle.EXTRA_BOLD);
			textView.setTextColor ( getActivity ().getResources ().getColor ( R.color.dark_blue ) );
			int padding = Tools.fromDPToPX ( 7, getActivity () );
			textView.setPadding ( padding + Tools.fromDPToPX ( 21, getActivity () ), padding, padding, padding );

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
                    if(!SettingsManager.getBoolean(SettingsManager.Params.IS_PICKY_DIALOG_SHOW, false, controller)){
                        SettingsManager.setBoolean(SettingsManager.Params.IS_PICKY_DIALOG_SHOW, true, controller);
                        showGetPickyDialog();
                    }
					controller.setState ( VIEW_STATE.FEED );

				}
			} );

            View deviderView = new View(getActivity());
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
            layoutParams.height = Tools.fromDPToPX(1, controller);
            deviderView.setLayoutParams(layoutParams);
            deviderView.setBackgroundResource(R.color.grey_semi_alpha);
            container.addView ( textView );

            container.addView(deviderView);
		}
	}

    private void showGetPickyDialog(){
      
            if ( getPickyDialog == null )
            {
                getPickyDialog = new Dialog( getActivity () );
                getPickyDialog.requestWindowFeature ( Window.FEATURE_NO_TITLE );
                getPickyDialog.setContentView(R.layout.available_dialog_layout);
                View noButton = getPickyDialog.findViewById ( R.id.dialogNoButton );

                TextView dialogBodyTextView = (TextView) getPickyDialog.findViewById(R.id.dialogBodyTextView);
                dialogBodyTextView.setText("Click FILTER to choose a HandyBoy based on his availability, price, or something else!");

                TextView dialogTitleTextView = (TextView) getPickyDialog.findViewById(R.id.dialogTitleTextView);
                dialogTitleTextView.setText("Get picky!");

                noButton.setVisibility(View.GONE);
                TextView okButton = (TextView)getPickyDialog.findViewById ( R.id.dialogYesButton );
                okButton.setText("Ok");

                okButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getPickyDialog.dismiss();
                    }
                });

                getPickyDialog.getWindow().setBackgroundDrawable ( new ColorDrawable( android.graphics.Color.TRANSPARENT ) );
            }

            getPickyDialog.show();
        
    }

	private void addListeners ()
	{
	}

	protected void next ( JobCategory partyBoy )
	{
		MainActivity.getInstance ().setState ( VIEW_STATE.CHOOSE_JOB_TYPE );
	}
}