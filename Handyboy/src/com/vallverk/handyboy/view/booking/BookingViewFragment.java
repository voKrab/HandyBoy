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
import com.vallverk.handyboy.model.api.AddonServiceAPIObject;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject.AddonServiceAPIParams;
import com.vallverk.handyboy.model.api.AddressAPIObject;
import com.vallverk.handyboy.model.api.AddressAPIObject.AddressParams;
import com.vallverk.handyboy.model.api.CreditCardAPIObject;
import com.vallverk.handyboy.model.api.CreditCardAPIObject.CreditCardParams;
import com.vallverk.handyboy.model.schedule.BookingTime;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.controller.BookingController;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingViewFragment extends BaseFragment
{
	private View backImageView;
	private View backTextView;
	private View addonsContainer;
	private View addressContainer;
	private View dateContainer;
	private View creditCardContainer;
	private TextView jobTextView;
	private TextView infoTextView;
	private ImageView jobImageView;
	private BookingController bookingController;
	private TextView priceTextView;
	private View jobContainer;
	private View choosedAddonsContainer;
	private TextView addon1TextView;
	private TextView addon2TextView;
	private TextView addonsPriceTextView;
	private TextView addonsTextView;
	private TextView addressTextView;
	private View choosedAddressFrameLayout;
	private TextView choosedAddressNameTextView;
	private TextView choosedAddressLocationTextView;
	private TextView dateTextView;
	private View choosedDateFrameLayout;
	private Object findViewById;
	private TextView choosedDayTextView;
	private TextView choosedDateTextView;
	private TextView choosedCreditCardDateTextView;
	private TextView choosedCreditCardNameTextView;
	private View choosedCreditCardFrameLayout;
	private TextView creditCardTextView;
	private Button bookButton;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.booking_layout, null );

		backImageView = view.findViewById ( R.id.backImageView );
		backTextView = view.findViewById ( R.id.backTextView );

		addonsContainer = view.findViewById ( R.id.addonsContainer );
		addressContainer = view.findViewById ( R.id.addressContainer );
		dateContainer = view.findViewById ( R.id.dateContainer );
		creditCardContainer = view.findViewById ( R.id.creditCardContainer );
		jobTextView = ( TextView ) view.findViewById ( R.id.jobTextView );
		infoTextView = ( TextView ) view.findViewById ( R.id.infoTextView );
		jobImageView = ( ImageView ) view.findViewById ( R.id.jobImageView );
		priceTextView = ( TextView ) view.findViewById ( R.id.priceTextView );
		jobContainer = view.findViewById ( R.id.jobContainer );
		choosedAddonsContainer = view.findViewById ( R.id.choosedAddonsContainer );
		addon1TextView = ( TextView ) view.findViewById ( R.id.addon1TextView );
		addon2TextView = ( TextView ) view.findViewById ( R.id.addon2TextView );
		addonsPriceTextView = ( TextView ) view.findViewById ( R.id.addonsPriceTextView );
		addonsTextView = ( TextView ) view.findViewById ( R.id.addonsTextView );
		bookButton = ( Button ) view.findViewById ( R.id.bookButton );

		addressTextView = ( TextView ) view.findViewById ( R.id.addressTextView );
		choosedAddressFrameLayout = view.findViewById ( R.id.choosedAddressFrameLayout );
		choosedAddressNameTextView = ( TextView ) view.findViewById ( R.id.choosedAddressNameTextView );
		choosedAddressLocationTextView = ( TextView ) view.findViewById ( R.id.choosedAddressLocationTextView );

		dateTextView = ( TextView ) view.findViewById ( R.id.dateTextView );
		choosedDateFrameLayout = view.findViewById ( R.id.choosedDateFrameLayout );
		choosedDayTextView = ( TextView ) view.findViewById ( R.id.choosedDayTextView );
		choosedDateTextView = ( TextView ) view.findViewById ( R.id.choosedDateTextView );

		creditCardTextView = ( TextView ) view.findViewById ( R.id.creditCardTextView );
		choosedCreditCardFrameLayout = view.findViewById ( R.id.choosedCreditCardFrameLayout );
		choosedCreditCardNameTextView = ( TextView ) view.findViewById ( R.id.choosedCreditCardNameTextView );
		choosedCreditCardDateTextView = ( TextView ) view.findViewById ( R.id.choosedCreditCardDateTextView );

		return view;
	}

	@Override
	protected void init ()
	{
		updateComponents ();
		addListeners ();
	}

	protected void clearFields ()
	{
		jobContainer.setVisibility ( View.INVISIBLE );
		priceTextView.setVisibility ( View.INVISIBLE );
		choosedAddonsContainer.setVisibility ( View.INVISIBLE );
		choosedCreditCardFrameLayout.setVisibility ( View.INVISIBLE );
		choosedDateFrameLayout.setVisibility ( View.INVISIBLE );
		choosedAddressFrameLayout.setVisibility ( View.INVISIBLE );
	}

	private void updateComponents ()
	{
		jobTextView.setText ( bookingController.getJobName () );
		infoTextView.setText ( bookingController.getJobInfo () );
		jobImageView.setImageResource ( bookingController.getImageResource () );
		priceTextView.setText ( bookingController.getPriceString () );

		jobContainer.setVisibility ( View.VISIBLE );
		priceTextView.setVisibility ( View.VISIBLE );

		updateAddonsContainer ();
		updateAddressContainer ();
		updateTimeContainer ();
		updateCreditCardContainer ();
        checkSuggestionHours ();
	}

    private void checkSuggestionHours ()
    {
        float selectedHours = bookingController.getHours();
        if ( selectedHours == 0 )
        {
            return;
        }
        float suggestionHours = bookingController.getSuggestionHours ();

        if ( suggestionHours > selectedHours )
        {
            Toast.makeText ( controller, "We suggest " + suggestionHours + " hours. Booking for less may result in incomplete services!", Toast.LENGTH_LONG ).show ();
        }
    }

    private void updateCreditCardContainer ()
	{
		CreditCardAPIObject creditCard = bookingController.getCreditCard ();
		if ( creditCard == null )
		{
			creditCardTextView.setVisibility ( View.VISIBLE );
			choosedCreditCardFrameLayout.setVisibility ( View.INVISIBLE );
		} else
		{
			creditCardTextView.setVisibility ( View.INVISIBLE );
			choosedCreditCardFrameLayout.setVisibility ( View.VISIBLE );
			choosedCreditCardNameTextView.setText ( creditCard.getString ( CreditCardParams.CARD_NAME ) );
			choosedCreditCardDateTextView.setText ( creditCard.getString ( CreditCardParams.EXP_DATE ) );
		}
	}

	private void updateTimeContainer ()
	{
		BookingTime bookingTime = bookingController.getBookingTime ();
		if ( bookingTime == null )
		{
			dateTextView.setVisibility ( View.VISIBLE );
			choosedDateFrameLayout.setVisibility ( View.INVISIBLE );
		} else
		{
			dateTextView.setVisibility ( View.INVISIBLE );
			choosedDateFrameLayout.setVisibility ( View.VISIBLE );
			Date date = bookingTime.getDate ();
			Calendar calendar = Calendar.getInstance ();
			calendar.setTime ( date );
			choosedDayTextView.setText ( calendar.getDisplayName ( Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US ) );
			choosedDateTextView.setText ( calendar.getDisplayName ( Calendar.MONTH, Calendar.LONG, Locale.US ) + ", " + calendar.get ( Calendar.DAY_OF_MONTH ) );
		}
	}

	private void updateAddressContainer ()
	{
		AddressAPIObject address = bookingController.getAddress ();
		if ( address == null )
		{
			addressTextView.setVisibility ( View.VISIBLE );
			choosedAddressFrameLayout.setVisibility ( View.INVISIBLE );
		} else
		{
			addressTextView.setVisibility ( View.INVISIBLE );
			choosedAddressFrameLayout.setVisibility ( View.VISIBLE );
			if ( address.getString ( AddressParams.ZIP_CODE ).isEmpty () )
			{
				choosedAddressNameTextView.setText ( address.getString ( AddressParams.DESCRIPTION ) );
			} else
			{
				choosedAddressNameTextView.setText ( address.getString ( AddressParams.ZIP_CODE ) + " " + address.getString ( AddressParams.CITY ) + ", " + address.getString ( AddressParams.STATE ) );
			}
			choosedAddressLocationTextView.setText ( address.getString ( AddressParams.ADDRESS ) );
		}
	}

	private void updateAddonsContainer ()
	{
		List < AddonServiceAPIObject > addons = bookingController.getAddons ();
		if ( addons.isEmpty () )
		{
			addonsTextView.setVisibility ( View.VISIBLE );
			choosedAddonsContainer.setVisibility ( View.INVISIBLE );
		} else
		{
			addonsTextView.setVisibility ( View.INVISIBLE );
			choosedAddonsContainer.setVisibility ( View.VISIBLE );
			AddonServiceAPIObject addon1 = addons.get ( 0 );
			addon1TextView.setText ( controller.getAddonName ( addon1.getString ( AddonServiceAPIParams.JOB_ADDONS_ID ) ) );
			if ( addons.size () > 1 )
			{
				AddonServiceAPIObject addon2 = addons.get ( 1 );
				addon2TextView.setText ( controller.getAddonName ( addon2.getString ( AddonServiceAPIParams.JOB_ADDONS_ID ) ) );
			} else
			{
				addon2TextView.setVisibility ( View.GONE );
			}
			addonsPriceTextView.setText ( "$" + bookingController.getAddonsPrice () );
		}
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );

		bookingController = controller.getBookingController ();
	}

	@Override
	protected void updateFonts ()
	{
		// FontUtils.getInstance ( getActivity () ).applyStyle ( line1TextView,
		// FontStyle.EXTRA_BOLD );
		// FontUtils.getInstance ( getActivity () ).applyStyle ( line2TextView,
		// FontStyle.REGULAR );
		//
		// FontUtils.getInstance ( getActivity () ).applyStyle (
		// weeklyScheduleButton, FontStyle.SEMIBOLD );
		// FontUtils.getInstance ( getActivity () ).applyStyle (
		// customScheduleButton, FontStyle.SEMIBOLD );
		// FontUtils.getInstance ( getActivity () ).applyStyle (
		// yourMoneyButton, FontStyle.SEMIBOLD );
		// FontUtils.getInstance ( getActivity () ).applyStyle (
		// jobDescriptionsButton, FontStyle.SEMIBOLD );
	}

	private void addListeners ()
	{
		dateContainer.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				if ( bookingController.isHasDiscount () )
				{
					bookingController.setState ( VIEW_STATE.BOOKING_CHOOSE_DISCOUNT_TIME );
				} else
				{
					bookingController.setState ( VIEW_STATE.BOOKING_CHOOSE_TIME );
				}
			}
		} );
		addonsContainer.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				bookingController.setState ( VIEW_STATE.CHOOSE_ADDONS );
			}
		} );
		addressContainer.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				bookingController.setState ( VIEW_STATE.CHOOSE_ADDRESS );
			}
		} );
		backImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.onBackPressed ();
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

		creditCardContainer.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				bookingController.setState ( VIEW_STATE.CREDIT_CARD );
			}
		} );

		bookButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				booking ();
			}
		} );
	}

	protected void booking ()
	{
		new AsyncTask < Void, Void, String > ()
		{
			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					bookingController.booking ();
				} catch ( Exception e )
				{
					result = e.getMessage ();
					e.printStackTrace ();
				}
				return result;
			}

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
					controller.cancelBooking ();
					bookingController.setState ( VIEW_STATE.BOOKING_CHECKOUT );
				} else
				{
					Toast.makeText ( controller, result, Toast.LENGTH_LONG ).show ();
				}
			}
		}.execute ();
	}
}