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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.AdditionalChargesAPIObject;
import com.vallverk.handyboy.model.api.BookingAPIObject;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.BookingDataObject;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.job.JobTypeManager;
import com.vallverk.handyboy.view.base.BaseFragment;

import java.util.Calendar;
import java.util.Locale;

public class ChargesViewFragment extends BaseFragment
{
	private ImageView backImageView;
	private TextView backTextView;
	private APIManager apiManager;

	private UserAPIObject user;
	private UserAPIObject service;
	private UserAPIObject userBooking;
	private boolean isIService;

	private ImageView myAvatarImage;
	private ImageView handyBoyAvatarImage;
	private TextView gigTitleTextView;
	private TextView gigIdTextView;
	private TextView gigStatusTextView;

	private BookingDataManager bookingDataManager;
	private BookingDataObject bookingDataObject;
	private DisplayImageOptions avatarLoadOptions = new DisplayImageOptions.Builder ().showImageOnFail ( R.drawable.avatar ).showImageForEmptyUri ( R.drawable.avatar ).cacheInMemory ( true ).cacheOnDisc ().build ();
	private TextView hbNameTextView;
	private TextView bookingNameTextView;
	private TextView dateTextView;
	private TextView bookingPriceTextView;
	private AdditionalChargesAPIObject additionalCharges;
	private TextView additionalDescriptionTextView;
	private TextView totalPriceTextView;
	private TextView stickerTotalPriceTextView;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.charges_layout, container, false );
			backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
			backTextView = ( TextView ) view.findViewById ( R.id.backTextView );

			myAvatarImage = ( ImageView ) view.findViewById ( R.id.myAvatarImage );
			handyBoyAvatarImage = ( ImageView ) view.findViewById ( R.id.handyBoyAvatarImage );
			gigTitleTextView = ( TextView ) view.findViewById ( R.id.gigTitleTextView );
			gigIdTextView = ( TextView ) view.findViewById ( R.id.gigIdTextView );
			gigStatusTextView = ( TextView ) view.findViewById ( R.id.gigStatusTextView );
			hbNameTextView = ( TextView ) view.findViewById ( R.id.hbNameTextView );
			bookingNameTextView = ( TextView ) view.findViewById ( R.id.bookingNameTextView );
			dateTextView = ( TextView ) view.findViewById ( R.id.dateTextView );
			bookingPriceTextView = ( TextView ) view.findViewById ( R.id.bookingPriceTextView );
			additionalDescriptionTextView = ( TextView ) view.findViewById ( R.id.additionalDescriptionTextView );
			totalPriceTextView = ( TextView ) view.findViewById ( R.id.totalPriceTextView );
			stickerTotalPriceTextView = ( TextView ) view.findViewById ( R.id.stickerTotalPriceTextView );
		} else
		{
			( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}

	@Override
	protected void clearFields ()
	{
		hbNameTextView.setVisibility ( View.GONE );
		bookingNameTextView.setVisibility ( View.GONE );
		dateTextView.setVisibility ( View.GONE );
		bookingPriceTextView.setVisibility ( View.GONE );
		additionalDescriptionTextView.setVisibility ( View.GONE );
		totalPriceTextView.setVisibility ( View.GONE );
		stickerTotalPriceTextView.setVisibility ( View.GONE );
	}

	@Override
	protected void init ()
	{
		new AsyncTask < Void, Void, String > ()
		{
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

	private void updateComponents ()
	{
		ImageLoader.getInstance ().displayImage ( user.getString ( UserAPIObject.UserParams.AVATAR ), myAvatarImage, avatarLoadOptions );
		ImageLoader.getInstance ().displayImage ( service.getString ( UserAPIObject.UserParams.AVATAR ), handyBoyAvatarImage, avatarLoadOptions );
		gigTitleTextView.setText ( "YOU + " + service.getShortName ().toUpperCase () );
		gigIdTextView.setText ( "GIG#" + bookingDataObject.getId ().toString () );
		BookingStatusEnum status = bookingDataObject.getSatus ();
		gigStatusTextView.setText ( status.toString () );
		gigStatusTextView.setBackgroundColor ( BookingAPIObject.getStatusColor ( status ) );

		hbNameTextView.setVisibility ( View.VISIBLE );
		hbNameTextView.setText ( bookingDataObject.getService ().getShortName () );

		bookingNameTextView.setVisibility ( View.VISIBLE );
		bookingNameTextView.setText ( JobTypeManager.getInstance ().getJobType ( bookingDataObject.getTypeJobAPIObject ().getString ( TypeJobServiceAPIObject.TypeJobServiceParams.TYPEJOB_ID ) ).getName () + " Gig" );

		dateTextView.setVisibility ( View.VISIBLE );
		Calendar calendar = Calendar.getInstance ();
		calendar.setTimeInMillis ( System.currentTimeMillis () );
		String dayOfWeek = calendar.getDisplayName ( Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US );
		String month = calendar.getDisplayName ( Calendar.MONTH, Calendar.LONG, Locale.US );
		int dayOfMonth = calendar.get ( Calendar.DAY_OF_MONTH );
		dateTextView.setText ( dayOfWeek + ", " + month + " " + dayOfMonth );

		bookingPriceTextView.setVisibility ( View.VISIBLE );
		int price = additionalCharges.getInt ( AdditionalChargesAPIObject.AdditionalChargesParams.PRICE );
		bookingPriceTextView.setText ( "$" + price );

		additionalDescriptionTextView.setVisibility ( View.VISIBLE );
		additionalDescriptionTextView.setText ( additionalCharges.getString ( AdditionalChargesAPIObject.AdditionalChargesParams.REASON ) );

		totalPriceTextView.setVisibility ( View.VISIBLE );
		float newTotalPrice = price + Float.parseFloat ( bookingDataObject.getBookingAPIObject ().getString ( BookingAPIObject.BookingAPIParams.TOTAL_PRICE ) );
		totalPriceTextView.setText ( "For a NEW total of $" + newTotalPrice );

        stickerTotalPriceTextView.setVisibility ( View.VISIBLE );
        stickerTotalPriceTextView.setText ( "$" + newTotalPrice );
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		apiManager = APIManager.getInstance ();
		user = apiManager.getUser ();
		bookingDataManager = BookingDataManager.getInstance ();
		bookingDataObject = bookingDataManager.getData ().get ( bookingDataManager.getActiveDataIndex () );
		service = bookingDataObject.getService ();
		userBooking = bookingDataObject.getCustomer ();
		isIService = service.getId ().equals ( apiManager.getUser ().getId () );
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
	}
}