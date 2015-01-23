package com.vallverk.handyboy.view.booking;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.BookingAPIObject;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.BookingDataObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.view.base.BaseFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ServicedViewFragment extends BaseFragment
{
	private boolean isIService;
	private ImageView backImageView;
	private TextView backTextView;

	private APIManager apiManager;
	
	private UserAPIObject user;
	private View leaveTipButton;
	private View noThanksButton;
	

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.serviced_layout, container, false );
			backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
			backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
			leaveTipButton = view.findViewById ( R.id.leaveTipButton );
			noThanksButton = view.findViewById ( R.id.noThanksButton );
		} else
		{
			( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		apiManager = APIManager.getInstance ();
		user = apiManager.getUser ();
		addListeners ();
	}

	@Override
	protected void updateFonts ()
	{
		// FontUtils.getInstance ( controller ).applyStyle ( noFavoritesTitle,
		// FontStyle.LIGHT );
		// FontUtils.getInstance ( controller ).applyStyle ( noFavoritesBody,
		// FontStyle.REGULAR );
	}

	private void addListeners ()
	{
		backTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.onBackPressed ();
			}
		} );

		backImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View view )
			{
				controller.onBackPressed ();
			}
		} );
		leaveTipButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View view )
			{
				controller.setState ( VIEW_STATE.LEAVE_TIP );
			}
		} );
		noThanksButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View view )
			{
				controller.setState ( VIEW_STATE.CUSTOMER_REVIEW );
			}
		} );
	}
}