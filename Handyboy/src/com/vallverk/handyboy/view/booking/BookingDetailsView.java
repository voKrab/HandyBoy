package com.vallverk.handyboy.view.booking;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.api.AdditionalChargesAPIObject;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject.AddonServiceAPIParams;
import com.vallverk.handyboy.model.api.AddressAPIObject.AddressParams;
import com.vallverk.handyboy.model.api.BookingAPIObject;
import com.vallverk.handyboy.model.api.BookingAPIObject.BookingAPIParams;
import com.vallverk.handyboy.model.api.BookingDataObject;
import com.vallverk.handyboy.model.api.BookingDataObject.JobAddonDetailsObject;
import com.vallverk.handyboy.model.api.JobAddonsAPIObject.JobAddonsAPIParams;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.view.base.RatingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    private TextView priceByHoorTextView;
    private TextView totalPriceTextView;

	private View commentContainer;
	private TextView commentTextView;

	private View specialReqeustContainer;
	private TextView specialReqeustTextView;

	private RatingView ratingView;
	private ImageView chatImageView;

	private LinearLayout addonsContainerLayout;

    private List<AdditionalChargesAPIObject> additionalChargesList;
    private BookingDataObject bookingDataObject;

    private View chargesContainer;
    private LinearLayout chargesContainerLayout;

    private float totalPrice = 0f;

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
        priceByHoorTextView =(TextView) view.findViewById(R.id.priceByHoorTextView);
        totalPriceTextView = (TextView) view.findViewById(R.id.totalPriceTextView);
		addonsContainerLayout = ( LinearLayout ) view.findViewById ( R.id.addonsContainerLayout );

		commentContainer = view.findViewById ( R.id.commentContainer );
		commentTextView = ( TextView ) view.findViewById ( R.id.commentTextView );

		specialReqeustContainer = view.findViewById ( R.id.specialReqeustContainer );
		specialReqeustTextView = ( TextView ) view.findViewById ( R.id.specialReqeustTextView );

		ratingView = ( RatingView ) view.findViewById ( R.id.ratingView );
		chatImageView = ( ImageView ) view.findViewById ( R.id.chatImageView );

        chargesContainer = view.findViewById(R.id.chargesContainer);
        chargesContainerLayout = (LinearLayout) view.findViewById(R.id.chargesContainerLayout);

		addView ( view );
	}

    private void getAdditionalCharges(){
        new AsyncTask< Void, Void, String >()
        {
            @Override
            protected void onPostExecute ( String result )
            {
                super.onPostExecute ( result );

                if (result.isEmpty () )
                {
                    if(additionalChargesList != null && additionalChargesList.size() > 0){
                        chargesContainerLayout.removeAllViews ();
                        chargesContainer.setVisibility(View.VISIBLE);
                        for ( AdditionalChargesAPIObject object : additionalChargesList )
                        {
                            if ( object.isAccepted() )
                            {
                                //return object;


                                 View addonItemView = inflater.inflate ( R.layout.addon_item_view, null );
                                 TextView addonNameTextView = ( TextView ) addonItemView.findViewById ( R.id.addonNameTextView );
                                 TextView addonPriceTextView = ( TextView ) addonItemView.findViewById ( R.id.addonPriceTextView );
                                 addonNameTextView.setText ( "+" + object.getValue(AdditionalChargesAPIObject.AdditionalChargesParams.REASON).toString() );
                                 //addonPriceTextView.setText ( "$" +object.getValue(AdditionalChargesAPIObject.AdditionalChargesParams.PRICE).toString() );

                                float price = Float.parseFloat(object.getValue(AdditionalChargesAPIObject.AdditionalChargesParams.PRICE).toString());
                                addToTotal(price);

                                addonPriceTextView.setText ( "$" + Tools.decimalFormat(price) );
                                chargesContainerLayout.addView ( addonItemView );
                            }
                        }

                        //chargesContainer.setVisibility(VISIBLE);
                        //chargesTextView.setText(additionalCharges.getValue(AdditionalChargesAPIObject.AdditionalChargesParams.REASON).toString());
                        //chargespriceTextView.setText("$" + additionalCharges.getValue(AdditionalChargesAPIObject.AdditionalChargesParams.PRICE).toString());
                    }else{
                        chargesContainer.setVisibility(GONE);
                    }
                }else{
                    chargesContainer.setVisibility(GONE);
                }
            }

            @Override
            protected String doInBackground ( Void... params )
            {
                String result = "";
                try
                {
                    additionalChargesList = bookingDataObject.getBookingAPIObject ().getAdditionalChargesList();
                } catch ( Exception ex )
                {
                    ex.printStackTrace ();
                    result = ex.getMessage ();
                }
                return result;
            }
        }.execute ();
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
        this.bookingDataObject = bookingDataObject;
		gigTitleTextView.setText ( bookingDataObject.getTypeJobAPIObject ().getName () + " Session" );
		userNameTextView.setText ( bookingDataObject.getService ().getShortName () );
		addressNameTextView.setText ( bookingDataObject.getAddress ().getString ( AddressParams.DESCRIPTION ) );
		addressTextView.setText ( bookingDataObject.getAddress ().getString ( AddressParams.ADDRESS ) );
		dateTextView.setText ( Tools.toDateString ( bookingDataObject.getBookingAPIObject ().getString ( BookingAPIParams.DATE ) ) );
        //totalPriceTextView.setText( "$" + bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIObject.BookingAPIParams.TOTAL_PRICE ).toString ());

		String avatarUrl = "";
		if ( isIService )
		{
			avatarUrl = bookingDataObject.getCustomer ().getString ( UserParams.AVATAR );
		} else
		{
			avatarUrl = bookingDataObject.getService ().getString ( UserParams.AVATAR );
		}
		setAvatar ( avatarUrl );

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
        String totalHours = bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIParams.TOTAL_HOURS ).toString ();

        float hours = Float.parseFloat(getHours(totalHours));
        String sHours = " Hour";
        if(hours > 1){
            sHours = " Hours";
        }
		periodTextView.setText ( Tools.decimalHoursFormat(hours) + sHours + ", " + Tools.getAmericanTime(startPeriod) + " to " + Tools.getAmericanTime(endPeriod) );

		//priceTextView.setText ( "$" + bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIParams.TOTAL_PRICE ).toString () );
        //float priceByHour = Float.parseFloat(bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIParams.TOTAL_HOURS ).toString ()) * Float.parseFloat(bookingDataObject.getTypeJobAPIObject().getValue(TypeJobServiceAPIObject.TypeJobServiceParams.PRICE).toString());
        //priceTextView.setText("$" + priceByHour);

        //float priceByHour = Float.parseFloat(bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIObject.BookingAPIParams.TOTAL_HOURS ).toString ()) * Float.parseFloat(bookingDataObject.getTypeJobAPIObject().getValue(TypeJobServiceAPIObject.TypeJobServiceParams.PRICE).toString());
        float priceByHour = Float.parseFloat(bookingDataObject.getTypeJobAPIObject().getValue(TypeJobServiceAPIObject.TypeJobServiceParams.PRICE).toString());
        priceByHoorTextView.setText("$" + Tools.decimalFormat(priceByHour) + "/hr");

        float price = priceByHour *  Float.parseFloat(bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIParams.TOTAL_HOURS ).toString ());
        priceTextView.setText("$" + Tools.decimalFormat(price));
        addToTotal(price);


		addonsContainerLayout.removeAllViews();
		for ( JobAddonDetailsObject jobAddonDetailsObject : bookingDataObject.getAddonsAPIObjects () )
		{
			View addonItemView = inflater.inflate ( R.layout.addon_item_view, null );
			TextView addonNameTextView = ( TextView ) addonItemView.findViewById ( R.id.addonNameTextView );
			TextView addonPriceTextView = ( TextView ) addonItemView.findViewById ( R.id.addonPriceTextView );
			addonNameTextView.setText ( "+" + jobAddonDetailsObject.addonsAPIObject.getValue ( JobAddonsAPIParams.NAME ) );
            float addonPrice = Float.parseFloat(jobAddonDetailsObject.addonServiceAPIObject.getValue ( AddonServiceAPIObject.AddonServiceAPIParams.PRICE ).toString());
            addToTotal(addonPrice);
            addonPriceTextView.setText ( "$" +  Tools.decimalFormat(addonPrice));
			addonsContainerLayout.addView ( addonItemView );
		}

		String comment = bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIParams.COMMENT ).toString ();
		if ( comment.isEmpty () )
		{
			commentContainer.setVisibility ( GONE );
		} else
		{
			commentTextView.setText ( comment );
			commentContainer.setVisibility ( VISIBLE );
		}

		String specialRequest = bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIParams.SPECIAL_REQUEST ).toString ();
		if ( specialRequest.isEmpty () )
		{
			specialReqeustContainer.setVisibility ( GONE );
		} else
		{
			specialReqeustTextView.setText ( specialRequest );
			specialReqeustContainer.setVisibility ( VISIBLE );
		}

        getAdditionalCharges();
	}

	public void setRaiting ( float rating )
	{
		ratingView.setRating ( rating );
		ratingView.setVisibility ( VISIBLE );
	}

	public void setChatEnabled ( boolean isVisible )
	{
		if ( isVisible )
		{
			chatImageView.setVisibility ( VISIBLE );
		} else
		{
			chatImageView.setVisibility ( GONE );
		}
	}

	public void setCustomerAvatarStyle ()
	{
		FrameLayout.LayoutParams params = ( FrameLayout.LayoutParams ) avatarImageView.getLayoutParams ();
		params.height = Tools.fromDPToPX ( 154, getContext () );
		avatarImageView.setLayoutParams ( params );
	}

    public String getHours(String hours){
       hours = hours.replace(".30", ".50");
       do{
           hours = hours.substring(0, hours.length()-1);
       }while (hours.length() > 0 && hours.charAt(hours.length()-1)=='0');

       if (hours.length() > 0 && hours.charAt(hours.length()-1)=='.') {
            hours = hours.substring(0, hours.length()-1);
       }
       return  hours;
    }

    private void addToTotal(float price){
        totalPrice += price;
        totalPriceTextView.setText( "$" + Tools.decimalFormat(totalPrice));
    }

    /*public String getTime(String time){
        DateFormat sdf = new SimpleDateFormat("hh:mm");
        try {
            Date date = sdf.parse(time);
            DateFormat format = new SimpleDateFormat( "h:mm a" );
            String str = format.format( date.getTime() );
            return  str;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //DateFormat formatter = new SimpleDateFormat("HH:mm");
        return "";
    }*/
}
