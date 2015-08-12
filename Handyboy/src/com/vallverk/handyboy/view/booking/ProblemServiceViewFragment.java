package com.vallverk.handyboy.view.booking;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.BookingAPIObject;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.BookingDataObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;

import org.json.JSONObject;

public class ProblemServiceViewFragment extends BaseFragment
{
	private ImageView backImageView;
	private TextView backTextView;
	private APIManager apiManager;
	private UserAPIObject customer;
	private BookingDataManager bookingDataManager;
	private BookingDataObject bookingDataObject;
	private BookingAPIObject bookingAPIObject;
	private UserAPIObject service;
	private View cancelButton;
    private View isLateButton;

    private Dialog cancelDialog;
    private Dialog clientIsLateDialog;


	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.problem_service_layout, container, false );
			backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
			backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
			cancelButton = view.findViewById ( R.id.cancelButton );
            isLateButton = view.findViewById(R.id.isLateButton);
		} else
		{
			//( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		apiManager = APIManager.getInstance ();
		bookingDataManager = BookingDataManager.getInstance ();
		bookingDataObject = bookingDataManager.getActiveBooking ();
		bookingAPIObject = bookingDataObject.getBookingAPIObject ();
		service = bookingDataObject.getService ();
		customer = bookingDataObject.getCustomer ();
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
				showCancelDialog();
			}
		} );

        isLateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showClientIsLateDialog();
            }
        });
	}

    private void showCancelDialog ()
    {
        if ( cancelDialog == null )
        {
            cancelDialog = new Dialog( getActivity () );
            cancelDialog.requestWindowFeature ( Window.FEATURE_NO_TITLE );
            cancelDialog.setContentView ( R.layout.available_dialog_layout );
            TextView noButton = (TextView)cancelDialog.findViewById ( R.id.dialogNoButton );
            TextView yesButton = (TextView) cancelDialog.findViewById ( R.id.dialogYesButton );
            TextView bodyText = (TextView) cancelDialog.findViewById(R.id.dialogBodyTextView);
            TextView dialogTitleTextView = (TextView) cancelDialog.findViewById(R.id.dialogTitleTextView);
            dialogTitleTextView.setText("Are you sure you want to cancel?");
            bodyText.setText("You will not receive payment for this gig.");
            noButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    cancelDialog.dismiss();
                }
            });

            yesButton.setOnClickListener ( new OnClickListener ()
            {

                @Override
                public void onClick ( View v )
                {
                    cancelDialog.dismiss ();
                    cancelGig();
                }
            } );
            cancelDialog.getWindow ().setBackgroundDrawable ( new ColorDrawable( android.graphics.Color.TRANSPARENT ) );
        }

        cancelDialog.show ();
    }

    private void showClientIsLateDialog ()
    {
        if ( clientIsLateDialog == null )
        {
            clientIsLateDialog = new Dialog( getActivity () );
            clientIsLateDialog.requestWindowFeature ( Window.FEATURE_NO_TITLE );
            clientIsLateDialog.setContentView ( R.layout.available_dialog_layout );
            TextView noButton = (TextView)clientIsLateDialog.findViewById ( R.id.dialogNoButton );
            TextView yesButton = (TextView) clientIsLateDialog.findViewById ( R.id.dialogYesButton );
            TextView bodyText = (TextView) clientIsLateDialog.findViewById(R.id.dialogBodyTextView);
            TextView dialogTitleTextView = (TextView) clientIsLateDialog.findViewById(R.id.dialogTitleTextView);
            dialogTitleTextView.setText("Are you sure you want to report the client as a no-show?");
            bodyText.setText("Please try and contact them first!");
            noButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    clientIsLateDialog.dismiss();
                }
            });

            yesButton.setOnClickListener ( new OnClickListener ()
            {

                @Override
                public void onClick ( View v )
                {
                    clientIsLateDialog.dismiss ();
                    //cancelGig();
                    clientIsLate();
                }
            } );

            clientIsLateDialog.getWindow ().setBackgroundDrawable ( new ColorDrawable( android.graphics.Color.TRANSPARENT ) );
        }

        clientIsLateDialog.show ();
    }

    private void clientIsLate(){
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
                    controller.setState ( VIEW_STATE.SERVICE_REVIEW );
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
                    //bookingAPIObject.changeStatus ( service, customer, BookingStatusEnum.CANCELED_BY_HB );
                    String request = ServerManager.getRequest(ServerManager.SEND_CL_ISLATE  + bookingAPIObject.getId() );
                    JSONObject jsonRequest = new JSONObject(request);
                    result = jsonRequest.getString("parameters");
                    if(result.isEmpty()){
                        result = jsonRequest.getString("message");
                    }
                } catch ( Exception ex )
                {
                    result = ex.getMessage ();
                    ex.printStackTrace ();
                }
                return result;
            }
        }.execute ();
    }

	protected void cancelGig ()
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
					controller.setState ( VIEW_STATE.SERVICE_REVIEW );
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
					bookingAPIObject.changeStatus ( service, customer, BookingStatusEnum.CANCELED_BY_HB );
				} catch ( Exception ex )
				{
					result = ex.getMessage ();
					ex.printStackTrace ();
				}
				return result;
			}
		}.execute ();
	}
}