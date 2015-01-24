package com.vallverk.handyboy.view.booking;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.BookingAPIObject;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.BookingAPIObject.BookingAPIParams;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NextGigViewFragment extends BaseFragment
{
	private ImageView backImageView;
	private TextView backTextView;

	private APIManager apiManager;
	private UserAPIObject service;
	
	private ImageView avatarImageView;
	private View amHereStartButton;
	
	private BookingDetailsView bookingDetailsView;
	private BookingDataManager bookingDataManager;
	private BookingAPIObject bookingAPIObject;
	private View cancelButton;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.next_gig_layout, container, false );
			backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
			backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
			avatarImageView = (ImageView) view.findViewById ( R.id.avatarImageView );
			
			amHereStartButton = view.findViewById ( R.id.amHereStartButton );
			cancelButton = view.findViewById ( R.id.cancelButton );
			bookingDetailsView = ( BookingDetailsView ) view.findViewById ( R.id.bookingDetailsView );

		} else
		{
			( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}

	@Override
	protected void init ()
	{
		addListeners ();
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		apiManager = APIManager.getInstance ();
		service = apiManager.getUser ();
		bookingDataManager = BookingDataManager.getInstance ();
		bookingDetailsView.setData ( bookingDataManager.getData ().get ( bookingDataManager.getActiveDataIndex () ), service.isService () );
		bookingDetailsView.setNavigationButton ( true );
		bookingDetailsView.setChatEnabled ( true );
		//bookingDetailsView.setAvatar ( "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTZn0eoglnoSkQaUBLkC9qRkTEmyBjnkHGUsaPE7cKgMOb6Gkrk", false );
		bookingAPIObject = bookingDataManager.getActiveBooking ().getBookingAPIObject ();
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
		
		amHereStartButton.setOnClickListener ( new OnClickListener()
		{
			
			@Override
			public void onClick ( View v )
			{
				startGig ();
			}
		} );
		
		cancelButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				cancelGig ();
			}
		});
	}
	
	protected void cancelGig ()
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
					controller.setState ( VIEW_STATE.GIGS );
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
					bookingAPIObject.changeStatus ( service, bookingDataManager.getActiveCustomer (), BookingStatusEnum.CANCELED_BY_HB );
				} catch ( Exception ex )
				{
					result = ex.getMessage ();
					ex.printStackTrace ();
				}
				return result;
			}
		}.execute ();
	}
		
	protected void startGig ()
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
					controller.setState ( VIEW_STATE.ACTIVE_GIG );
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
					bookingAPIObject.changeStatus ( service, bookingDataManager.getActiveCustomer (), BookingStatusEnum.PERFORMED );
				} catch ( Exception ex )
				{
					result = ex.getMessage ();
					ex.printStackTrace ();
				}
				return result;
			}
		}.execute ();
	}
}