package com.vallverk.handyboy.view.booking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject.AddonServiceAPIParams;
import com.vallverk.handyboy.model.api.AddressAPIObject;
import com.vallverk.handyboy.model.api.AddressAPIObject.AddressParams;
import com.vallverk.handyboy.model.api.BookingAPIObject.BookingAPIParams;
import com.vallverk.handyboy.model.api.BookingDataObject.JobAddonDetailsObject;
import com.vallverk.handyboy.model.api.JobAddonsAPIObject;
import com.vallverk.handyboy.model.api.JobAddonsAPIObject.JobAddonsAPIParams;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.model.api.BookingDataObject;
import com.vallverk.handyboy.view.base.RatingView;

public class BookingDetailsView extends FrameLayout
{
	private LayoutInflater inflater;
	private DisplayImageOptions avatarLoadOptions = new DisplayImageOptions.Builder ().showImageOnFail ( R.drawable.avatar ).showImageForEmptyUri ( R.drawable.avatar ).cacheInMemory ( true ).cacheOnDisc ().build ();

	private ImageView avatarImageView;
	private Button navigationButton;
	private TextView gigTitleTextView;
	private TextView userNameTextView;
	private TextView addressNameTextView;
	private TextView addressTextView;
	private TextView dateTextView;
	private TextView periodTextView;
	private TextView priceTextView;

    private View commentContainer;
    private TextView commentTextView;

    private View specialReqeustContainer;
    private TextView specialReqeustTextView;

	private RatingView ratingView;
	private ImageView chatImageView;

	private LinearLayout addonsContainerLayout;

	public BookingDetailsView ( Context context )
	{
		super ( context );
		init ( context, null );
	}

	public BookingDetailsView ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		init ( context, attrs );
	}

	public BookingDetailsView ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
		init ( context, attrs );
	}

	private void init ( final Context context, AttributeSet attrs )
	{
		inflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
		View view = inflater.inflate ( R.layout.booking_view, null );
		avatarImageView = ( ImageView ) view.findViewById ( R.id.avatarImageView );
		navigationButton = ( Button ) view.findViewById ( R.id.navigationButton );
		userNameTextView = ( TextView ) view.findViewById ( R.id.userNameTextView );
		gigTitleTextView = ( TextView ) view.findViewById ( R.id.gigTitleTextView );
		addressNameTextView = ( TextView ) view.findViewById ( R.id.addressNameTextView );
		addressTextView = ( TextView ) view.findViewById ( R.id.addressTextView );
		dateTextView = ( TextView ) view.findViewById ( R.id.dateTextView );
		periodTextView = ( TextView ) view.findViewById ( R.id.periodTextView );
		priceTextView = ( TextView ) view.findViewById ( R.id.priceTextView );
		addonsContainerLayout = ( LinearLayout ) view.findViewById ( R.id.addonsContainerLayout );

        commentContainer = view.findViewById(R.id.commentContainer);
        commentTextView = (TextView) view.findViewById(R.id.commentTextView);

        specialReqeustContainer = view.findViewById(R.id.specialReqeustContainer);
        specialReqeustTextView = (TextView) view.findViewById(R.id.specialReqeustTextView);

		ratingView = ( RatingView ) view.findViewById ( R.id.ratingView );
		chatImageView = (ImageView) view.findViewById ( R.id.chatImageView );

		addView ( view );
	}

	public void setAvatar ( String url )
	{
		ImageLoader.getInstance ().displayImage ( url, avatarImageView, avatarLoadOptions );
	}

	public void setNavigationButton ( boolean isVisible )
	{
		if ( isVisible )
		{
			navigationButton.setVisibility ( View.VISIBLE );
		} else
		{
			navigationButton.setVisibility ( View.GONE );
		}
	}

	public void setData ( BookingDataObject bookingDataObject, boolean isIService )
	{
		gigTitleTextView.setText ( bookingDataObject.getTypeJobAPIObject ().getName () + " Session" );
		userNameTextView.setText ( bookingDataObject.getService ().getShortName () );
		addressNameTextView.setText ( bookingDataObject.getAddress ().getString ( AddressParams.DESCRIPTION ) );
		addressTextView.setText ( bookingDataObject.getAddress ().getString ( AddressParams.ADDRESS ) );
		dateTextView.setText ( Tools.toDateString ( bookingDataObject.getBookingAPIObject ().getString ( BookingAPIParams.DATE ) ) );
		
		String avatarUrl = "";
		if ( isIService )
		{
			avatarUrl = bookingDataObject.getCustomer ().getString ( UserParams.AVATAR );
		} else
		{
			avatarUrl = bookingDataObject.getService ().getString ( UserParams.AVATAR );
		}
		setAvatar ( avatarUrl);
		

		JSONArray timeJsonArray;
		JSONObject timePeriod;
		String startPeriod = "";
		String endPeriod = "";
		try
		{
			timeJsonArray = new JSONArray ( bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIParams.TIME ).toString () );
			timePeriod = timeJsonArray.getJSONObject ( 0 );
			startPeriod = timePeriod.getString ( "start" );
			endPeriod = timePeriod.getString ( "end" );
		} catch ( JSONException e )
		{
			e.printStackTrace ();
		}
		periodTextView.setText ( bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIParams.TOTAL_HOURS ).toString () + " Hours, " + startPeriod + " to " + endPeriod );
		priceTextView.setText ( "$" + bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIParams.TOTAL_PRICE ).toString () );
		addonsContainerLayout.removeAllViews ();
		for ( JobAddonDetailsObject jobAddonDetailsObject : bookingDataObject.getAddonsAPIObjects () )
		{
			View addonItemView = inflater.inflate ( R.layout.addon_item_view, null );
			TextView addonNameTextView = ( TextView ) addonItemView.findViewById ( R.id.addonNameTextView );
			TextView addonPriceTextView = ( TextView ) addonItemView.findViewById ( R.id.addonPriceTextView );
			addonNameTextView.setText ( "+" + jobAddonDetailsObject.addonsAPIObject.getValue ( JobAddonsAPIParams.NAME ) );
			addonPriceTextView.setText ( "$" + jobAddonDetailsObject.addonServiceAPIObject.getValue ( AddonServiceAPIParams.PRICE ) );
			addonsContainerLayout.addView ( addonItemView );
		}

        String comment = bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIParams.COMMENT ).toString ();
        if(comment.isEmpty()){
            commentContainer.setVisibility(GONE);
        }else{
            commentTextView.setText(comment);
            commentContainer.setVisibility(VISIBLE);
        }

        String specialRequest =  bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIParams.SPECIAL_REQUEST ).toString ();
        if(specialRequest.isEmpty()){
            specialReqeustContainer.setVisibility(GONE);
        }else{
            specialReqeustTextView.setText(specialRequest);
            specialReqeustContainer.setVisibility(VISIBLE);
        }
	}

	public void setRaiting ( float rating )
	{
		ratingView.setRating ( rating );
		ratingView.setVisibility ( VISIBLE );
	}
	
	public void setChatEnabled(boolean isVisible){
		if(isVisible){
			chatImageView.setVisibility ( VISIBLE );
		}else{
			chatImageView.setVisibility ( GONE );
		}
	}
	
	public void setCustomerAvatarStyle(){
		FrameLayout.LayoutParams params = ( FrameLayout.LayoutParams ) avatarImageView.getLayoutParams ();
		params.height = Tools.fromDPToPX ( 154, getContext () );
		avatarImageView.setLayoutParams ( params );
	}
}
