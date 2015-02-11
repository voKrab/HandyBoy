package com.vallverk.handyboy.view.booking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject;
import com.vallverk.handyboy.model.api.AddressAPIObject;
import com.vallverk.handyboy.model.api.BookingAPIObject;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.BookingDataObject;
import com.vallverk.handyboy.model.api.JobAddonsAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.view.base.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GigConfirmViewFragment extends BaseFragment
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
	//private TextView gigIdTextView;
	private TextView hoursTextView;
    private TextView gigStatusTextView;
    private TextView sessionTitle;
    private  TextView priceTextView;
    private  TextView addressNameTextView;
    private TextView addressTextView;
    private TextView dateTextView;
    private  TextView timeToTextView;
    private  TextView timeFromTextView;
    private  TextView totalPriceTextView;

    private LinearLayout addonContainer;
    private View detailsTitle;

    private View commentTitle;
    private TextView commentTextView;

    private View specialReqeustTitle;
    private TextView specialReqeustTextView;

	
	private BookingDataManager bookingDataManager;
	private BookingDataObject bookingDataObject;
	private DisplayImageOptions avatarLoadOptions = new DisplayImageOptions.Builder ().showImageOnFail ( R.drawable.avatar ).showImageForEmptyUri ( R.drawable.avatar ).cacheInMemory ( true ).cacheOnDisc ().build ();

    private LayoutInflater inflater;
	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.gig_confirm_layout, container, false );
			backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
			backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
			
			myAvatarImage = ( ImageView ) view.findViewById ( R.id.myAvatarImage );
			handyBoyAvatarImage = ( ImageView ) view.findViewById ( R.id.handyBoyAvatarImage );
			gigTitleTextView = ( TextView ) view.findViewById ( R.id.gigTitleTextView );
			//gigIdTextView = ( TextView ) view.findViewById ( R.id.gigIdTextView );
			gigStatusTextView = ( TextView ) view.findViewById ( R.id.gigStatusTextView );
            hoursTextView = (TextView) view.findViewById(R.id.hoursTextView);
            sessionTitle = (TextView) view.findViewById(R.id.sessionTitle);
            priceTextView = (TextView) view.findViewById(R.id.priceTextView);
            addressNameTextView = (TextView) view.findViewById(R.id.addressNameTextView);
            addressTextView = (TextView) view.findViewById(R.id.addressTextView);
            dateTextView = (TextView) view.findViewById(R.id.dateTextView);
            timeToTextView = (TextView) view.findViewById(R.id.timeToTextView);
            timeFromTextView = (TextView) view.findViewById(R.id.timeFromTextView);
            totalPriceTextView = (TextView) view.findViewById(R.id.totalPriceTextView);

            addonContainer = (LinearLayout) view.findViewById(R.id.addonContainer);
            detailsTitle = view.findViewById(R.id.detailsTitle);

            commentTitle = view.findViewById(R.id.commentTitle);
            commentTextView = (TextView) view.findViewById(R.id.commentTextView);

            specialReqeustTitle = view.findViewById(R.id.specialReqeustTitle);
            specialReqeustTextView = (TextView) view.findViewById(R.id.specialReqeustTextView);



		} else
		{
			( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}
	
	@Override
	protected void init ()
	{

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

        inflater = LayoutInflater.from(controller);


        ImageLoader.getInstance ().displayImage ( user.getString ( UserParams.AVATAR ), myAvatarImage, avatarLoadOptions );
        ImageLoader.getInstance ().displayImage (service.getString ( UserParams.AVATAR ), handyBoyAvatarImage, avatarLoadOptions );
        gigTitleTextView.setText ( "YOU + " + service.getShortName ().toUpperCase () );
       // gigIdTextView.setText ( "GIG#" + bookingDataObject.getId ().toString () );
        BookingStatusEnum status = bookingDataObject.getSatus ();
        gigStatusTextView.setText ( status.name () );
        gigStatusTextView.setBackgroundColor ( BookingAPIObject.getStatusColor ( status ) );

        hoursTextView.setText(bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIObject.BookingAPIParams.TOTAL_HOURS ).toString ());
        sessionTitle.setText( bookingDataObject.getTypeJobAPIObject ().getName () + " Session");
        priceTextView.setText ( "$" + bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIObject.BookingAPIParams.TOTAL_PRICE ).toString () );
        addressNameTextView.setText ( bookingDataObject.getAddress ().getString ( AddressAPIObject.AddressParams.DESCRIPTION ) );
        addressTextView.setText ( bookingDataObject.getAddress ().getString ( AddressAPIObject.AddressParams.ADDRESS ) );

        dateTextView.setText ( Tools.toDateString (bookingDataObject.getBookingAPIObject().getString(BookingAPIObject.BookingAPIParams.DATE)) );

        JSONArray timeJsonArray;
        JSONObject timePeriod;
        String startPeriod = "";
        String endPeriod = "";
        try
        {
            timeJsonArray = new JSONArray( bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIObject.BookingAPIParams.TIME ).toString () );
            timePeriod = timeJsonArray.getJSONObject ( 0 );
            startPeriod = timePeriod.getString ( "start" );
            endPeriod = timePeriod.getString ( "end" );
        } catch ( JSONException e )
        {
            e.printStackTrace ();
        }
        timeToTextView.setText(endPeriod);
        timeFromTextView.setText(startPeriod);
        totalPriceTextView.setText( "$" + bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIObject.BookingAPIParams.TOTAL_PRICE ).toString ());

        addonContainer.removeAllViews ();

        if(bookingDataObject.getAddonsAPIObjects ().size() > 0){
            detailsTitle.setVisibility(View.VISIBLE);
        }else{
            detailsTitle.setVisibility(View.GONE);
        }

        for ( BookingDataObject.JobAddonDetailsObject jobAddonDetailsObject : bookingDataObject.getAddonsAPIObjects () )
        {
            View addonItemView = inflater.inflate ( R.layout.addon_confirm_item_view, null );
            TextView addonNameTextView = ( TextView ) addonItemView.findViewById ( R.id.addonNameTextView );
            TextView addonPriceTextView = ( TextView ) addonItemView.findViewById ( R.id.addonPriceTextView );
            addonNameTextView.setText ( jobAddonDetailsObject.addonsAPIObject.getValue ( JobAddonsAPIObject.JobAddonsAPIParams.NAME ).toString() );
            addonPriceTextView.setText ( "$" + jobAddonDetailsObject.addonServiceAPIObject.getValue ( AddonServiceAPIObject.AddonServiceAPIParams.PRICE ) );
            addonContainer.addView ( addonItemView );
        }

        String comment = bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIObject.BookingAPIParams.COMMENT ).toString ();
        if ( comment.isEmpty () )
        {
            commentTitle.setVisibility(View.GONE);
            commentTextView.setVisibility ( View.GONE );
        } else
        {
            commentTextView.setText ( comment );
            commentTitle.setVisibility(View.VISIBLE);
            commentTextView.setVisibility ( View.VISIBLE );
        }

        String specialRequest = bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIObject.BookingAPIParams.SPECIAL_REQUEST ).toString ();
        if ( specialRequest.isEmpty () )
        {
            specialReqeustTitle.setVisibility(View.GONE);
            specialReqeustTextView.setVisibility ( View.GONE );
        } else
        {
            specialReqeustTextView.setText ( specialRequest );
            specialReqeustTitle.setVisibility(View.VISIBLE);
            specialReqeustTextView.setVisibility ( View.VISIBLE );
        }

        addListeners();
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