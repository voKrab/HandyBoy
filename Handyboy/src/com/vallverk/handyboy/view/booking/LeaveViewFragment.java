package com.vallverk.handyboy.view.booking;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.BookingAPIObject;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;

import org.json.JSONObject;

public class LeaveViewFragment extends BaseFragment
{
	private ImageView backImageView;
	private TextView backTextView;

	private APIManager apiManager;
	
	private UserAPIObject user;
	private View leaveButton;
	private EditText tipsEditText;
	

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.leave_layout, container, false );
			backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
			backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
			leaveButton = view.findViewById ( R.id.leaveButton );
			tipsEditText = ( EditText ) view.findViewById ( R.id.tipsEditText );
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
		addListeners ();
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
		leaveButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				leaveTips ();
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
	}

    private void sendTips(final String tip){

        new AsyncTask< Void, Void, String >()
        {
            public void onPreExecute ()
            {
                super.onPreExecute ();
                controller.showLoader();
            }

            public void onPostExecute ( String result )
            {
                super.onPostExecute ( result );
                controller.hideLoader();
                controller.setState ( VIEW_STATE.CUSTOMER_REVIEW );
            }

            @Override
            protected String doInBackground ( Void... params )
            {
                String result = "";
                try
                {
                    BookingAPIObject bookingAPIObject =  BookingDataManager.getInstance().getActiveBooking().getBookingAPIObject();
                    bookingAPIObject.putValue(BookingAPIObject.BookingAPIParams.TIP, tip );
                    bookingAPIObject.update (ServerManager.BOOKING_SAVE);
                } catch ( Exception ex )
                {
                    result = ex.getMessage ();
                    ex.printStackTrace ();
                }
                return result;
            }
        }.execute ();


    }

	protected void leaveTips ()
	{
		String tips = tipsEditText.getText ().toString ().trim ();
		if ( tips.isEmpty () )
		{
			Toast.makeText ( controller, "Please leave tip", Toast.LENGTH_LONG ).show ();
			return;
		}
        sendTips(tips);
	}
}