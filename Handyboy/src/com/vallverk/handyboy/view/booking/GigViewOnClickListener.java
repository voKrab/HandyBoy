package com.vallverk.handyboy.view.booking;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController;
import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.api.BookingAPIObject;
import com.vallverk.handyboy.model.api.BookingDataManager;

/**
 * Created by Олег on 27.01.2015.
 */
public class GigViewOnClickListener implements View.OnClickListener
{
	private MainActivity controller;
	private BookingDataManager bookingDataManager;
	private int selectedPosition;

	public GigViewOnClickListener ( MainActivity controller, BookingDataManager bookingDataManager, int selectedPosition )
	{
		this.controller = controller;
		this.bookingDataManager = bookingDataManager;
		this.selectedPosition = selectedPosition;
	}

	@Override
	public void onClick ( View view )
	{
		bookingDataManager.setActiveDataIndex ( selectedPosition );
		BookingStatusEnum status = bookingDataManager.getActiveBookingStatus ();
		// controller.setState ( VIEW_STATE.CHARGES );
		if ( bookingDataManager.isIService () )
		{
			switch ( status )
			{
				case PENDING:
				{
					serviceRequestedOnClick ();
					break;
				}
				case CONFIRMED:
				{
					controller.setState ( ViewStateController.VIEW_STATE.NEXT_GIG );
					break;
				}
				case DECLINED:
				{
					break;
				}
				case CANCELED_BY_HB:
				{
					break;
				}
				case APPROVED:
				{

					break;
				}
				case PERFORMED:
				{
					controller.setState ( ViewStateController.VIEW_STATE.ACTIVE_GIG );
					break;
				}
			}
		} else
		{
			switch ( status )
			{
				case PENDING:
				{
					customerRequestedOnClick();
					break;
				}
				case DECLINED:
				{
					controller.setState ( ViewStateController.VIEW_STATE.GIG_CUSTOMER );
					break;
				}
				case CONFIRMED:
				{
					controller.setState ( ViewStateController.VIEW_STATE.GIG_CUSTOMER );
                    break;
				}
				case CANCELED_BY_HB:
				{
					controller.setState ( ViewStateController.VIEW_STATE.BOOK_ANOTHER );
					break;
				}
				case APPROVED:
				{
                    controller.setState(ViewStateController.VIEW_STATE.GIG_COMPLETED);
					break;
				}
				case PERFORMED:
				{
                    customerPerformedOnClick();
					break;
				}
			}
		}
	}

    private void customerPerformedOnClick ()
    {
        new AsyncTask < Void, Void, String > ()
        {
            private boolean isAdditionalChangesState;

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
                    if ( isAdditionalChangesState )
                    {
                        controller.setState ( ViewStateController.VIEW_STATE.CHARGES );
                    } else
                    {
                        controller.setState ( ViewStateController.VIEW_STATE.GIG_CUSTOMER );
                    }
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
                    BookingAPIObject booking = bookingDataManager.getActiveBookingAPIObject ();
                    isAdditionalChangesState = booking.isAdditionalChargesState();
                } catch ( Exception ex )
                {
                    ex.printStackTrace ();
                    result = ex.getMessage ();
                }
                return result;
            }
        }.execute ();
    }

	private void customerRequestedOnClick ()
	{
		new AsyncTask < Void, Void, String > ()
		{
			private boolean isAdditionalChangesState;

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
					if ( isAdditionalChangesState )
					{
						controller.setState ( ViewStateController.VIEW_STATE.CHARGES );
					} else
					{
						controller.setState ( ViewStateController.VIEW_STATE.GIG_CUSTOMER );
					}
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
					BookingAPIObject booking = bookingDataManager.getActiveBookingAPIObject ();
					isAdditionalChangesState = booking.isAdditionalChargesState();
				} catch ( Exception ex )
				{
					ex.printStackTrace ();
					result = ex.getMessage ();
				}
				return result;
			}
		}.execute ();
	}

	private void serviceRequestedOnClick ()
	{
		new AsyncTask < Void, Void, String > ()
		{
			private boolean isAdditionalChangesState;

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
					if ( isAdditionalChangesState )
					{
						Toast.makeText ( controller, R.string.waiting_for_customer_decision_about_your_add_charges_request, Toast.LENGTH_LONG ).show ();
					} else
					{
						controller.setState ( ViewStateController.VIEW_STATE.GIG_SERVICE );
					}
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
					BookingAPIObject booking = bookingDataManager.getActiveBookingAPIObject ();
					isAdditionalChangesState = booking.isAdditionalChargesState();
				} catch ( Exception ex )
				{
					ex.printStackTrace ();
					result = ex.getMessage ();
				}
				return result;
			}
		}.execute ();
	}
}
