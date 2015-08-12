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
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.BookingDataObject;
import com.vallverk.handyboy.model.api.KeyValuePair;
import com.vallverk.handyboy.model.api.ReviewAPIObject;
import com.vallverk.handyboy.model.api.ReviewAPIObject.ReviewParams;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.RatingView;

public class ServiceReviewViewFragment extends BaseFragment
{
	private boolean isIService;
	private ImageView backImageView;
	private TextView backTextView;
	private BookingDataManager bookingDataManager;

	private APIManager apiManager;
	
	private UserAPIObject user;
	private EditText reviewEditText;
	private RatingView ratingView;
	private TextView reviewButton;
	
	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.review_layout, container, false );
			backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
			backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
			reviewEditText = ( EditText ) view.findViewById ( R.id.reviewEditText );
			ratingView = ( RatingView ) view.findViewById ( R.id.ratingView );
			reviewButton = ( TextView ) view.findViewById ( R.id.reviewButton );
			apiManager = APIManager.getInstance ();
			user = apiManager.getUser ();
			bookingDataManager = BookingDataManager.getInstance ();
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
	}

	@Override
	protected void clearFields ()
	{
//		if ( bookingDataManager.isIService () ) // for all now
//		{
//			backImageView.setVisibility ( View.GONE );
//			backTextView.setVisibility ( View.GONE );
//		}
		backImageView.setVisibility ( View.GONE );
		backTextView.setVisibility ( View.GONE );
	}
	
	@Override
	protected void init ()
	{
		updateComponents ();
		addListeners ();
	}
	
	private void updateComponents ()
	{
		ratingView.setCanClicable ( true );
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
		reviewButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				review ();
			}
		});
	}

	protected void review ()
	{
		final String review = reviewEditText.getText ().toString ().trim ();
		if ( review.isEmpty () )
		{
			Toast.makeText ( controller, R.string.how_did_it_go, Toast.LENGTH_LONG ).show ();
			return;
		}
		final float rating = ratingView.getRating ();
		if ( rating == 0 )
		{
			Toast.makeText ( controller, R.string.how_did_they_do, Toast.LENGTH_LONG ).show ();
			return;
		}
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
					controller.setSwipeEnabled ( true );
                    changeState();
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
					BookingDataObject bookingDataObject = bookingDataManager.getActiveBooking ();
					ReviewAPIObject reviewAPIObject = createReviewAPIObject ( bookingDataObject, rating, review );
					apiManager.insert ( reviewAPIObject, ServerManager.REVIEW_SAVE, new KeyValuePair ( "bookingId", bookingDataObject.getId () ) );
					changeStatus ();
				} catch ( Exception ex )
				{
					result = ex.getMessage ();
					ex.printStackTrace ();
				}
				return result;
			}
		}.execute ();
	}

	protected void changeStatus () throws Exception
	{
		// TODO Auto-generated method stub
		
	}

    protected void changeState ()
    {
        controller.setState ( VIEW_STATE.GIGS );
    }

	protected ReviewAPIObject createReviewAPIObject ( BookingDataObject bookingDataObject, float rating, String review )
	{
		ReviewAPIObject reviewAPIObject = new ReviewAPIObject ();
		reviewAPIObject.putValue ( ReviewParams.OWNER_ID, bookingDataObject.getCustomer ().getId () );
		reviewAPIObject.putValue ( ReviewParams.REVIEWER_ID, bookingDataObject.getService ().getId () );
		reviewAPIObject.putValue ( ReviewParams.RATING, rating );
		reviewAPIObject.putValue ( ReviewParams.CONTENT, review );
		return reviewAPIObject;
	}
}