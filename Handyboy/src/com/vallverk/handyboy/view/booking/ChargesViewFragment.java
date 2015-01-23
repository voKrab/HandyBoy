package com.vallverk.handyboy.view.booking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.BookingAPIObject;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.BookingDataObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.view.base.BaseFragment;

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
		ImageLoader.getInstance ().displayImage ( user.getString ( UserParams.AVATAR ), myAvatarImage, avatarLoadOptions );
		ImageLoader.getInstance ().displayImage (service.getString ( UserParams.AVATAR ), handyBoyAvatarImage, avatarLoadOptions );
		gigTitleTextView.setText ( "YOU + " + service.getShortName ().toUpperCase () );
		gigIdTextView.setText ( "GIG#" + bookingDataObject.getId ().toString () );
		BookingStatusEnum status = bookingDataObject.getSatus ();
		gigStatusTextView.setText ( status.name () );
		gigStatusTextView.setBackgroundColor ( BookingAPIObject.getStatusColor ( status ) );
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
		userBooking =  bookingDataObject.getCustomer();
		isIService =  service.getId ().equals ( apiManager.getUser ().getId () );
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
	}
}