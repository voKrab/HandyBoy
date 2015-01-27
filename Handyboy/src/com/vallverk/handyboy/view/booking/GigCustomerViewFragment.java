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
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.view.base.BaseFragment;

public class GigCustomerViewFragment extends BaseFragment
{
	private boolean isIService;
	private ImageView backImageView;
	private TextView backTextView;

	private APIManager apiManager;

	private TextView cancelButton;
	private UserAPIObject user;
	private UserAPIObject service;
	private UserAPIObject userBooking;

	protected BookingAPIObject bookingAPIObject;

	private BookingDetailsView bookingDetailsView;
	private BookingDataManager bookingDataManager;

	private Button reportProblemOrRescheduleButton;
	private boolean isDeclined;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.gig_customer_layout, container, false );
			backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
			backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
			cancelButton = ( TextView ) view.findViewById ( R.id.cancelButton );
			bookingDetailsView = ( BookingDetailsView ) view.findViewById ( R.id.bookingDetailsView );
			reportProblemOrRescheduleButton = ( Button ) view.findViewById ( R.id.reportProblemButton );
		} else
		{
			( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		apiManager = APIManager.getInstance ();
		user = apiManager.getUser ();
		bookingDataManager = BookingDataManager.getInstance ();
		service = bookingDataManager.getData ().get ( bookingDataManager.getActiveDataIndex () ).getService ();
		userBooking = bookingDataManager.getData ().get ( bookingDataManager.getActiveDataIndex () ).getCustomer ();
		isIService = service.getId ().equals ( apiManager.getUser ().getId () );
		bookingDetailsView.setData ( bookingDataManager.getData ().get ( bookingDataManager.getActiveDataIndex () ), user.isService () );
		bookingDetailsView.setCustomerAvatarStyle ();
		bookingAPIObject = bookingDataManager.getActiveBooking ().getBookingAPIObject ();
		addListeners ();
		updateComponents ();
		super.onActivityCreated ( savedInstanceState );
	}

	private void updateComponents ()
	{
		
	}

	@Override
	protected void clearFields ()
	{
		BookingStatusEnum status = bookingDataManager.getActiveBookingStatus ();
        switch ( status )
        {
            case PENDING:
            {
                cancelButton.setVisibility ( View.GONE );
                reportProblemOrRescheduleButton.setVisibility ( View.GONE );
                break;
            }
            case DECLINED:
            {
                cancelButton.setVisibility ( View.GONE );
                reportProblemOrRescheduleButton.setVisibility ( View.VISIBLE );
                reportProblemOrRescheduleButton.setText ( R.string.reschedule_him );
                break;
            }
            case CONFIRMED:
            {
                cancelButton.setVisibility ( View.VISIBLE );
                reportProblemOrRescheduleButton.setVisibility ( View.GONE );
                break;
            }
            case PERFORMED:
            {
                cancelButton.setVisibility ( View.GONE );
                reportProblemOrRescheduleButton.setVisibility ( View.VISIBLE );
                reportProblemOrRescheduleButton.setText ( R.string.report_problem );
                break;
            }
        }
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
			public void onClick ( View view )
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

		reportProblemOrRescheduleButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
				if ( isDeclined )
				{
					controller.setCommunicationValue ( UserAPIObject.class.getSimpleName (), bookingDataManager.getActiveBooking ().getService () );
					controller.setState ( VIEW_STATE.HANDY_BOY_PAGE );
				} else
				{
					controller.setState ( VIEW_STATE.PROBLEM_CUSTOMER );
				}
			}
		} );

		cancelButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
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
							bookingAPIObject.changeStatus ( bookingDataManager.getActiveCustomer (), bookingDataManager.getActiveService (), BookingStatusEnum.CANCELED_BY_CUSTOMER );
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
		} );
	}
}