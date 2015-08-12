package com.vallverk.handyboy.view.booking;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController;
import com.vallverk.handyboy.model.AdditionalChargeStatusEnum;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.AdditionalChargesAPIObject;
import com.vallverk.handyboy.model.api.BookingAPIObject;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;

public class AddChargesViewFragment extends BaseFragment
{
	private ImageView backImageView;
	private TextView backTextView;
	private View crossImageView;
	private APIManager apiManager;
	private UserAPIObject user;
	private EditText reasonEditText;
	private EditText priceEditText;
	private TextView sendRequestButton;
	private TextView cancelButton;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.add_charges_layout, container, false );
			backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
			backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
			crossImageView = view.findViewById ( R.id.crossImageView );
			reasonEditText = ( EditText ) view.findViewById ( R.id.reasonEditText );
			priceEditText = ( EditText ) view.findViewById ( R.id.priceEditText );
			sendRequestButton = ( TextView ) view.findViewById ( R.id.sendRequestButton );
			cancelButton = ( TextView ) view.findViewById ( R.id.cancelButton );
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
		user = apiManager.getUser ();
		initViews ();
		addListeners ();
	}

	private void initViews ()
	{
		float scale = getResources ().getDisplayMetrics ().density;
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
				controller.onBackPressed ();
			}
		} );
		sendRequestButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View view )
			{
				send ();
			}
		} );

        priceEditText.addTextChangedListener(priceTextWatcher);
	}

    private TextWatcher priceTextWatcher = new TextWatcher ()
    {

        @Override
        public void onTextChanged ( CharSequence s, int start, int before, int count )
        {
        }

        @Override
        public void beforeTextChanged ( CharSequence s, int start, int count, int after )
        {
        }

        @Override
        public void afterTextChanged ( Editable s )
        {
            String phone = s.toString ().replace ( "$", "" ).trim ();
            if ( phone != null && !phone.isEmpty () )
            {
                phone = "$" + phone;
                priceEditText.removeTextChangedListener ( priceTextWatcher );
                priceEditText.setText ( phone );
                priceEditText.setSelection ( priceEditText.getText ().length () );
                priceEditText.addTextChangedListener ( priceTextWatcher );
            }
        }
    };

	private void send ()
	{
        final String reason = reasonEditText.getText().toString ().trim ();
        if ( reason.isEmpty () )
        {
            Toast.makeText ( controller, controller.getString(R.string.setup_reason ), Toast.LENGTH_LONG ).show ();
            return;
        }
        final String price = priceEditText.getText().toString ().replace("$", "").trim ();
        if ( price.isEmpty () )
        {
            Toast.makeText ( controller, controller.getString(R.string.setup_price ), Toast.LENGTH_LONG ).show ();
            return;
        }
        new AsyncTask < Void, Void, String > ()
        {
            @Override
            protected void onPreExecute ()
            {
                super.onPreExecute ();
                controller.showLoader();
            }

            @Override
            protected void onPostExecute ( String result )
            {
                super.onPostExecute ( result );
                controller.hideLoader();
                if ( result.isEmpty () )
                {
                    controller.setState ( ViewStateController.VIEW_STATE.GIGS );
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
                    BookingAPIObject bookingAPIObject = BookingDataManager.getInstance().getActiveBookingAPIObject();
                    AdditionalChargesAPIObject addChargesAO = new AdditionalChargesAPIObject ( reason, price, bookingAPIObject.getId(), AdditionalChargeStatusEnum.REQUESTED );
                    APIManager.getInstance ().insert ( addChargesAO, ServerManager.ADD_CHARGES_SAVE );
                } catch ( Exception ex )
                {
                    ex.printStackTrace();
                    result = ex.getMessage();
                }
                return result;
            }
        }.execute ();
	}
}