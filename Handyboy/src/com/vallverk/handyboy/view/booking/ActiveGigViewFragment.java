package com.vallverk.handyboy.view.booking;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.AdditionalChargesAPIObject;
import com.vallverk.handyboy.model.api.BookingAPIObject;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.BookingDataObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.view.base.BaseFragment;

public class ActiveGigViewFragment extends BaseFragment
{
	private ImageView backImageView;
	private TextView backTextView;

	private APIManager apiManager;
	private UserAPIObject user;
	private View finishGigButton;
	private View cancelButton;
	private BookingDataManager bookingDataManager;
	private BookingDataObject bookingDataObject;
	private UserAPIObject service;
	private UserAPIObject customer;
	private BookingAPIObject bookingAPIObject;
    private View additionalChargesButton;

    public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.active_gig_layout, container, false );
			backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
			backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
			finishGigButton = view.findViewById ( R.id.finishGigButton );
            additionalChargesButton = view.findViewById ( R.id.additionalChargesButton );
			cancelButton = view.findViewById ( R.id.cancelButton );
		} else
		{
			( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}

	@Override
	protected void init ()
	{
        new AsyncTask < Void, Void, String > ()
        {
            public AdditionalChargesAPIObject additionalCharges;

            @Override
            protected void onPreExecute ()
            {
                super.onPreExecute ();
                controller.showLoader ();
            }

            @Override
            protected void onPostExecute ( String result )
            {
                super.onPostExecute ( result );
                controller.hideLoader ();
                if ( result.isEmpty () )
                {
                    additionalChargesButton.setVisibility ( additionalCharges == null ? View.VISIBLE : View.GONE );
                    addListeners ();
                } else
                {
                    Toast.makeText ( controller, result, Toast.LENGTH_LONG ).show ();
                    controller.onBackPressed ();
                }
            }

            @Override
            protected String doInBackground ( Void... params )
            {
                String result = "";
                try
                {
                    additionalCharges = bookingDataObject.getBookingAPIObject ().getAdditionalCharges ();
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
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		apiManager = APIManager.getInstance ();
		user = apiManager.getUser ();
		bookingDataManager = BookingDataManager.getInstance ();
		bookingDataObject = bookingDataManager.getActiveBooking ();
		service = bookingDataObject.getService ();
		customer = bookingDataObject.getCustomer ();
		bookingAPIObject = bookingDataObject.getBookingAPIObject ();
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

		cancelButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
				//cancelGig ();
                controller.setState(VIEW_STATE.PROBLEM_SERVICE );
			}
		} );

		finishGigButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
				finishGig ();
			}
		} );

        additionalChargesButton.setOnClickListener ( new OnClickListener ()
        {
            @Override
            public void onClick ( View view )
            {
                controller.setState(VIEW_STATE.ADD_CHARGES );
            }
        } );
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
					bookingAPIObject.changeStatus ( service, customer, BookingStatusEnum.CANCELED_BY_HB );
				} catch ( Exception ex )
				{
					result = ex.getMessage ();
					ex.printStackTrace ();
				}
				return result;
			}
		}.execute ();
	}

	protected void finishGig ()
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
					controller.setState ( VIEW_STATE.SERVICE_REVIEW );
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
					bookingAPIObject.changeStatus ( service, bookingDataManager.getActiveCustomer (), BookingStatusEnum.WAITING_FOR_REVIEW );
					bookingDataManager.save ();
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