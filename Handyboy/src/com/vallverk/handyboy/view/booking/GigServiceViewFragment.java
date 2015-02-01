package com.vallverk.handyboy.view.booking;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.BookingAPIObject;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.BookingDataObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.view.base.BaseFragment;

public class GigServiceViewFragment extends BaseFragment
{
	private boolean isIService;
	private ImageView backImageView;
	private TextView backTextView;
	private View reviewImageView;

	private APIManager apiManager;

	private UserAPIObject user;
	private UserAPIObject service;
	private UserAPIObject customer;

	private ImageView avatarImageView;
	private Button acceptAddToCalendarButton;
	private Button contactHimButton;

	protected BookingAPIObject bookingAPIObject;

	private BookingDetailsView bookingDetailsView;
	private BookingDataManager bookingDataManager;
	private BookingDataObject bookingDataObject;
	private View cancelButton;
	private Button additionalChargesButton;
    private boolean isAddChargesRequested;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.gig_service_layout, container, false );
			backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
			backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
			reviewImageView = view.findViewById ( R.id.reviewImageView );
			avatarImageView = ( ImageView ) view.findViewById ( R.id.avatarImageView );
			bookingDetailsView = ( BookingDetailsView ) view.findViewById ( R.id.bookingDetailsView );
			additionalChargesButton = ( Button ) view.findViewById ( R.id.additionalChargesButton );
			acceptAddToCalendarButton = ( Button ) view.findViewById ( R.id.acceptAddToCalendarButton );
			contactHimButton = ( Button ) view.findViewById ( R.id.contactHimButton );
			cancelButton = view.findViewById ( R.id.cancelButton );
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
		bookingDataManager = BookingDataManager.getInstance ();
		bookingDataObject = bookingDataManager.getActiveBooking ();
		service = bookingDataManager.getData ().get ( bookingDataManager.getActiveDataIndex () ).getService ();
		customer = bookingDataManager.getData ().get ( bookingDataManager.getActiveDataIndex () ).getCustomer ();
		isIService = service.getId ().equals ( apiManager.getUser ().getId () );
		bookingDetailsView.setData ( bookingDataManager.getData ().get ( bookingDataManager.getActiveDataIndex () ), user.isService () );
		bookingDetailsView.setRaiting ( 4.0f );
		bookingAPIObject = bookingDataObject.getBookingAPIObject ();
	}

    @Override
    protected void init ()
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
                    updateComponents ();
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
                    isAddChargesRequested = bookingAPIObject.isAdditionalChargesRequested ();
                } catch ( Exception ex )
                {
                    result = ex.getMessage ();
                    ex.printStackTrace ();
                }
                return result;
            }
        }.execute();
    }

    private void updateComponents ()
	{
		additionalChargesButton.setVisibility ( isAddChargesRequested ? View.GONE : View.VISIBLE );
	}

	private void addListeners ()
	{
		additionalChargesButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.setState ( VIEW_STATE.ADD_CHARGES );
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

		backImageView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
				controller.onBackPressed ();
			}
		} );

		acceptAddToCalendarButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				accept ();
			}
		} );

		cancelButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				declineGig ();
			}
		} );

		contactHimButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
				if ( !isIService )
				{
					controller.setCommunicationValue ( UserAPIObject.class.getSimpleName (), service );
					controller.setState ( VIEW_STATE.CHAT );
				} else
				{
					controller.setCommunicationValue ( UserAPIObject.class.getSimpleName (), customer );
					controller.setState ( VIEW_STATE.CHAT );
				}
			}
		} );

		reviewImageView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				controller.setState ( VIEW_STATE.REVIEWS_CLIENT );
			}
		} );
	}

	protected void declineGig ()
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
					bookingAPIObject.changeStatus ( service, bookingDataManager.getActiveCustomer (), BookingStatusEnum.DECLINED );
				} catch ( Exception ex )
				{
					result = ex.getMessage ();
					ex.printStackTrace ();
				}
				return result;
			}
		}.execute ();
	}

	protected void accept ()
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
					controller.setState ( VIEW_STATE.NEXT_GIG );
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
					bookingAPIObject.changeStatus ( service, bookingDataManager.getActiveCustomer (), BookingStatusEnum.CONFIRMED );
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