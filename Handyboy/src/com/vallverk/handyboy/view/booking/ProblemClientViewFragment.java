package com.vallverk.handyboy.view.booking;

import com.vallverk.handyboy.R;

import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.BookingAPIObject;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.BookingDataObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.view.base.BaseFragment;

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

public class ProblemClientViewFragment extends BaseFragment
{
	private ImageView backImageView;
	private TextView backTextView;
	private APIManager apiManager;
	private UserAPIObject user;
	private View cancelButton;
    private View isLateButton;
	private UserAPIObject service;
	private BookingDataManager bookingDataManager;
	private BookingAPIObject bookingAPIObject;
	private BookingDataObject bookingDataObject;
    private Dialog cancelDialog;
    private Dialog hbIsLateDialog;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.problem_client_layout, container, false );
			backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
			backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
			cancelButton = view.findViewById ( R.id.cancelButton );
            isLateButton = view.findViewById(R.id.isLateButton);
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
		bookingDataManager = BookingDataManager.getInstance ();
		bookingDataObject = bookingDataManager.getActiveBooking ();
		bookingAPIObject = bookingDataObject.getBookingAPIObject ();
		service = bookingDataObject.getService ();
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
				//cancelGig ();
                showCancelDialog();
			}
		} );

        isLateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showHBIsLateDialog();
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

    private void showHBIsLateDialog ()
    {
        if ( hbIsLateDialog == null )
        {
            hbIsLateDialog = new Dialog( getActivity () );
            hbIsLateDialog.requestWindowFeature ( Window.FEATURE_NO_TITLE );
            hbIsLateDialog.setContentView ( R.layout.available_dialog_layout );
            TextView noButton = (TextView)hbIsLateDialog.findViewById ( R.id.dialogNoButton );
            TextView yesButton = (TextView) hbIsLateDialog.findViewById ( R.id.dialogYesButton );
            TextView bodyText = (TextView) hbIsLateDialog.findViewById(R.id.dialogBodyTextView);
            TextView dialogTitleTextView = (TextView) hbIsLateDialog.findViewById(R.id.dialogTitleTextView);
            dialogTitleTextView.setText("Are you sure you want to report the HandyBoy as a no-show?");
            bodyText.setText("Please try and contact them first!");
            noButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    hbIsLateDialog.dismiss();
                }
            });

            yesButton.setOnClickListener ( new OnClickListener ()
            {

                @Override
                public void onClick ( View v )
                {
                    hbIsLateDialog.dismiss ();
                    cancelGig();
                }
            } );

            hbIsLateDialog.getWindow ().setBackgroundDrawable ( new ColorDrawable( android.graphics.Color.TRANSPARENT ) );
        }

        hbIsLateDialog.show ();
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
					controller.setState ( VIEW_STATE.GIGS );
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
					bookingAPIObject.changeStatus ( user, service, BookingStatusEnum.CANCELED_BY_CUSTOMER );
				} catch ( Exception ex )
				{
					result = ex.getMessage ();
					ex.printStackTrace ();
				}
				return result;
			}
		}.execute ();
	}

    protected void sendHBIsLate ()
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
                    controller.setState ( VIEW_STATE.GIGS );
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
                    bookingAPIObject.changeStatus ( user, service, BookingStatusEnum.CANCELED_BY_CUSTOMER );
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