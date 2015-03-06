package com.vallverk.handyboy.view.booking;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.ViewStateController;
import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.AdditionalChargesAPIObject;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject;
import com.vallverk.handyboy.model.api.AddressAPIObject;
import com.vallverk.handyboy.model.api.BookingAPIObject;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.BookingDataObject;
import com.vallverk.handyboy.model.api.JobAddonsAPIObject;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.view.base.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
    private View cancelButton;

    private View additionalChargesTitle;
    private LinearLayout chargesContainer;
    private List<AdditionalChargesAPIObject> additionalChargesList;

    private View tipTitle;
    private LinearLayout tipsContainer;

	private BookingDataManager bookingDataManager;
	private BookingDataObject bookingDataObject;
	private DisplayImageOptions avatarLoadOptions = new DisplayImageOptions.Builder ().showImageOnFail ( R.drawable.avatar ).showImageForEmptyUri ( R.drawable.avatar ).cacheInMemory ( true ).cacheOnDisc ().build ();

    private LayoutInflater inflater;

    private float totalPrice = 0f;
    protected BookingAPIObject bookingAPIObject;
    private BookingStatusEnum status;


    private Dialog dialog;
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

            additionalChargesTitle = view.findViewById(R.id.additionalChargesTitle);
            chargesContainer = (LinearLayout) view.findViewById(R.id.chargesContainer);

            tipTitle = view.findViewById(R.id.tipTitle);
            tipsContainer = (LinearLayout) view.findViewById(R.id.tipsContainer);
            cancelButton = view.findViewById(R.id.cancelButton);

		} else
		{
			( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
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
                        chargesContainer.removeAllViews ();
                        additionalChargesTitle.setVisibility(View.VISIBLE);
                        chargesContainer.setVisibility(View.VISIBLE);
                        for ( AdditionalChargesAPIObject object : additionalChargesList )
                        {
                            if ( object.isAccepted() )
                            {
                                //return object;


                                /*View addonItemView = inflater.inflate ( R.layout.addon_item_view, null );
                                TextView addonNameTextView = ( TextView ) addonItemView.findViewById ( R.id.addonNameTextView );
                                TextView addonPriceTextView = ( TextView ) addonItemView.findViewById ( R.id.addonPriceTextView );
                                addonNameTextView.setText ( "+" + object.getValue(AdditionalChargesAPIObject.AdditionalChargesParams.REASON).toString() );
                                addonPriceTextView.setText ( "$" +object.getValue(AdditionalChargesAPIObject.AdditionalChargesParams.PRICE).toString() );*/

                                View addonItemView = inflater.inflate ( R.layout.addon_confirm_item_view, null );
                                TextView addonNameTextView = ( TextView ) addonItemView.findViewById ( R.id.addonNameTextView );
                                TextView addonPriceTextView = ( TextView ) addonItemView.findViewById ( R.id.addonPriceTextView );
                                addonNameTextView.setText ( object.getValue(AdditionalChargesAPIObject.AdditionalChargesParams.REASON).toString()  );

                                float price = Float.parseFloat(object.getValue(AdditionalChargesAPIObject.AdditionalChargesParams.PRICE).toString());
                                addToTotal(price);
                                addonPriceTextView.setText ( "$" + Tools.decimalFormat(price) );
                                chargesContainer.addView ( addonItemView );
                            }
                        }

                        //chargesContainer.setVisibility(VISIBLE);
                        //chargesTextView.setText(additionalCharges.getValue(AdditionalChargesAPIObject.AdditionalChargesParams.REASON).toString());
                        //chargespriceTextView.setText("$" + additionalCharges.getValue(AdditionalChargesAPIObject.AdditionalChargesParams.PRICE).toString());
                    }else{
                        chargesContainer.setVisibility(View.GONE);
                        additionalChargesTitle.setVisibility(View.GONE);
                    }
                }else{
                    chargesContainer.setVisibility(View.GONE);
                    additionalChargesTitle.setVisibility(View.GONE);
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

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
        super.onActivityCreated ( savedInstanceState );
		apiManager = APIManager.getInstance ();
		user = apiManager.getUser ();
		bookingDataManager = BookingDataManager.getInstance ();
		bookingDataObject = bookingDataManager.getData ().get ( bookingDataManager.getActiveDataIndex () );
        bookingAPIObject = bookingDataManager.getActiveBooking ().getBookingAPIObject ();
        status = bookingDataManager.getActiveBookingStatus ();
		service = bookingDataObject.getService ();
		userBooking =  bookingDataObject.getCustomer();
		isIService =  service.getId ().equals ( apiManager.getUser ().getId () );
        inflater = (LayoutInflater) controller.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        updateButtons();


        ImageLoader.getInstance ().displayImage ( user.getString ( UserParams.AVATAR ), myAvatarImage, avatarLoadOptions );
        ImageLoader.getInstance ().displayImage (service.getString ( UserParams.AVATAR ), handyBoyAvatarImage, avatarLoadOptions );
        gigTitleTextView.setText ( "YOU + " + service.getShortName ().toUpperCase () );
       // gigIdTextView.setText ( "GIG#" + bookingDataObject.getId ().toString () );
        //BookingStatusEnum status = bookingDataObject.getSatus ();
        gigStatusTextView.setText ( status.name () );
        gigStatusTextView.setBackgroundColor ( BookingAPIObject.getStatusColor ( status ) );

        hoursTextView.setText(bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIObject.BookingAPIParams.TOTAL_HOURS ).toString ());
        sessionTitle.setText( bookingDataObject.getTypeJobAPIObject ().getName () + " Session");

        //priceTextView.setText ( "$" + bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIObject.BookingAPIParams.TOTAL_PRICE ).toString () );
        float priceByHour = Float.parseFloat(bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIObject.BookingAPIParams.TOTAL_HOURS ).toString ()) * Float.parseFloat(bookingDataObject.getTypeJobAPIObject().getValue(TypeJobServiceAPIObject.TypeJobServiceParams.PRICE).toString());
        priceTextView.setText("$" + Tools.decimalFormat(priceByHour));
        addToTotal(priceByHour);

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
        timeToTextView.setText(Tools.getAmericanTime(endPeriod));
        timeFromTextView.setText((Tools.getAmericanTime(startPeriod)));
        //totalPriceTextView.setText( "$" + bookingDataObject.getBookingAPIObject ().getValue ( BookingAPIObject.BookingAPIParams.TOTAL_PRICE ).toString ());

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
            float addonPrice = Float.parseFloat(jobAddonDetailsObject.addonServiceAPIObject.getValue ( AddonServiceAPIObject.AddonServiceAPIParams.PRICE ).toString());
            addToTotal(addonPrice);
            addonPriceTextView.setText ( "$" +  Tools.decimalFormat(addonPrice));
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

        tipsContainer.removeAllViews();

        if(status == BookingStatusEnum.APPROVED) {
            View addonItemView = inflater.inflate(R.layout.addon_confirm_item_view, null);
            TextView addonNameTextView = (TextView) addonItemView.findViewById(R.id.addonNameTextView);
            TextView addonPriceTextView = (TextView) addonItemView.findViewById(R.id.addonPriceTextView);
            addonNameTextView.setText("Tip");
            tipTitle.setVisibility(View.VISIBLE);
            tipsContainer.addView(addonItemView);
        }else{
            tipTitle.setVisibility(View.GONE);
        }

        getAdditionalCharges();

        addListeners();
	}

    private void addToTotal(float price){
        totalPrice += price;
        totalPriceTextView.setText( "$" + Tools.decimalFormat(totalPrice));
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
                showDeclineDialog();
            }
        } );
	}

    private void showDeclineDialog ()
    {
        if ( dialog == null )
        {
            dialog = new Dialog( getActivity () );
            dialog.requestWindowFeature ( Window.FEATURE_NO_TITLE );
            dialog.setContentView ( R.layout.available_dialog_layout );
            TextView noButton = (TextView)dialog.findViewById ( R.id.dialogNoButton );
            TextView yesButton = (TextView) dialog.findViewById ( R.id.dialogYesButton );
            TextView bodyText = (TextView) dialog.findViewById(R.id.dialogBodyTextView);
            TextView dialogTitleTextView = (TextView) dialog.findViewById(R.id.dialogTitleTextView);
            dialogTitleTextView.setText("Are you sure you want to cancel this booking?");
            bodyText.setVisibility(View.GONE);
            noButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener ( new OnClickListener ()
            {

                @Override
                public void onClick ( View v )
                {
                    dialog.dismiss ();
                    cancelGig();
                }
            } );

            dialog.getWindow ().setBackgroundDrawable ( new ColorDrawable( android.graphics.Color.TRANSPARENT ) );
        }

        dialog.show ();
    }
    private  void cancelGig(){
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
                    controller.setState ( ViewStateController.VIEW_STATE.GIGS );
                } else
                {
                    Toast.makeText(controller, result, Toast.LENGTH_LONG).show ();
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

    private void updateButtons ()
    {
        switch ( status )
        {
            case PENDING:
                cancelButton.setVisibility(View.VISIBLE);
                break;

            case DECLINED:
                break;
            case CONFIRMED:
                cancelButton.setVisibility(View.VISIBLE);
                break;
            case PERFORMED:
                break;
            default: cancelButton.setVisibility(View.GONE);
        }
    }
}