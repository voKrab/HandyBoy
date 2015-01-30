package com.vallverk.handyboy.view.address;

import org.json.JSONObject;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.AddressAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.AddressAPIObject.AddressParams;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.SingleChoiceSpinner;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class AddAddressViewFragment extends BaseFragment
{
	private ImageView backImageView;
	private TextView backTextView;

	private SingleChoiceSpinner stateSpinner;
	private AddressAPIObject selectedAddressAPIObject;

	private Button addAddressButton;

	private EditText addressDescriptionEditText;
	private EditText addressEditText;
	private EditText subAddressEditText;
	private EditText cityEditText;
	private TextView stateTextView;
	private EditText zipEditText;
    private Button deleteButton;

	private APIManager apiManager;
	private UserAPIObject user;

	private TextView screenTextView;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.add_address_layout, container, false );
			backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
			backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
			screenTextView = ( TextView ) view.findViewById ( R.id.screenTextView );

			stateTextView = ( TextView ) view.findViewById ( R.id.stateTextView );
			stateSpinner = ( SingleChoiceSpinner ) view.findViewById ( R.id.stateSpinner );

			addAddressButton = ( Button ) view.findViewById ( R.id.addAddressButton );

			addressDescriptionEditText = ( EditText ) view.findViewById ( R.id.addressDescriptionEditText );
			addressEditText = ( EditText ) view.findViewById ( R.id.addressEditText );
			subAddressEditText = ( EditText ) view.findViewById ( R.id.subAddressEditText );
			cityEditText = ( EditText ) view.findViewById ( R.id.cityEditText );
			zipEditText = ( EditText ) view.findViewById ( R.id.zipEditText );

            deleteButton = (Button) view.findViewById(R.id.deleteButton);

		} else
		{
			( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}

	@Override
	protected void init ()
	{

		updateComponents ();
		addListeners ();
		updateFonts ();
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		apiManager = APIManager.getInstance ();
		user = apiManager.getUser ();
	}

	private void updateComponents ()
	{
		selectedAddressAPIObject = ( AddressAPIObject ) controller.getCommunicationValue ( AddressAPIObject.class.getSimpleName () );
		if(selectedAddressAPIObject != null){
			screenTextView.setText ( getString ( R.string.update_address_title ) );
			addAddressButton.setText (  getString (R.string.update_address_title ));
            deleteButton.setVisibility(View.VISIBLE);
		}else{
			screenTextView.setText ( getString ( R.string.add_address_title ) );
			addAddressButton.setText (  getString (R.string.add_new_destination_save ) );
            deleteButton.setVisibility(View.GONE);
		}
		
		stateSpinner.setData ( getActivity ().getResources ().getStringArray ( R.array.states ) );
		if ( selectedAddressAPIObject != null )
		{
			String[] address = selectedAddressAPIObject.getString ( AddressParams.ADDRESS ).toString ().split ( "," );
			addressDescriptionEditText.setText ( selectedAddressAPIObject.getString ( AddressParams.DESCRIPTION ) );
			addressEditText.setText ( address.length > 0 ? address[0] : "" );
			subAddressEditText.setText ( address.length > 1 ? address[1] : "" );
			cityEditText.setText ( selectedAddressAPIObject.getString ( AddressParams.CITY ) );
			zipEditText.setText ( selectedAddressAPIObject.getString ( AddressParams.ZIP_CODE ) );
			stateTextView.setText ( selectedAddressAPIObject.getString ( AddressParams.STATE ) );
		}
	}

	@Override
	protected void updateFonts ()
	{
		// FontUtils.getInstance ( controller ).applyStyle ( noFavoritesTitle,
		// FontStyle.LIGHT );
		// FontUtils.getInstance ( controller ).applyStyle ( noFavoritesBody,
		// FontStyle.REGULAR );
	}

    private void deleteAddress(){
        new AsyncTask < Void, Void, String > ()
        {
            @Override
            protected String doInBackground ( Void... params )
            {
                String result = "";
                JSONObject jsonRequest;
                String request;
                try
                {
                    if(selectedAddressAPIObject != null){
                        JSONObject postParameters = new JSONObject ();
                        postParameters.accumulate ( "id", selectedAddressAPIObject.getId () );
                        request = ServerManager.postRequest(ServerManager.DELETE_ADDRESS, postParameters);
                        jsonRequest = new JSONObject(request);
                        result = jsonRequest.getString ( "parameters" );
                    }else{
                        result = "Delete error";
                    }
                } catch ( Exception e )
                {
                    result = e.getMessage ();
                    e.printStackTrace ();
                }
                return result;
            }

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
                    controller.onBackPressed ();
                } else
                {
                    Toast.makeText ( controller, result, Toast.LENGTH_LONG ).show ();
                }
            }
        }.execute ();
    }

	private void addAddress ()
	{
		final String addressDescription = addressDescriptionEditText.getText ().toString ();
		if ( addressDescription.isEmpty () )
		{
			Toast.makeText ( controller, getString ( R.string.address_description_title ), Toast.LENGTH_LONG ).show ();
			return;
		}

		final String address = addressEditText.getText ().toString ();
		if ( address.isEmpty () )
		{
			Toast.makeText ( controller, getString ( R.string.address_field_title ), Toast.LENGTH_LONG ).show ();
			return;
		}

		final String subAddress = subAddressEditText.getText ().toString ();
		if ( subAddress.isEmpty () )
		{
			Toast.makeText ( controller, getString ( R.string.address_field_title ), Toast.LENGTH_LONG ).show ();
			return;
		}

		final String city = cityEditText.getText ().toString ();
		if ( city.isEmpty () )
		{
			Toast.makeText ( controller, getString ( R.string.address_city_field_title ), Toast.LENGTH_LONG ).show ();
			return;
		}

		final String zip = zipEditText.getText ().toString ();
		if ( zip.isEmpty () )
		{
			Toast.makeText ( controller, getString ( R.string.address_zip_field_title ), Toast.LENGTH_LONG ).show ();
			return;
		}

		new AsyncTask < Void, Void, String > ()
		{
			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				JSONObject jsonRequest;
				String request;
				try
				{
					JSONObject postParameters = new JSONObject ();
					postParameters.accumulate ( AddressParams.USER_ID.toString (), user.getId () );
					postParameters.accumulate ( AddressParams.CITY.toString (), city );
					postParameters.accumulate ( AddressParams.STATE.toString (), stateTextView.getText ().toString () );
					postParameters.accumulate ( AddressParams.ZIP_CODE.toString (), zip );
					postParameters.accumulate ( AddressParams.ADDRESS.toString (), address + ", " + subAddress );
					postParameters.accumulate ( AddressParams.DESCRIPTION.toString (), addressDescription );

					if ( selectedAddressAPIObject == null )
					{
						request = ServerManager.postRequest ( ServerManager.INSERT_ADDRESS, postParameters );
						jsonRequest = new JSONObject ( request );
					} else
					{
						selectedAddressAPIObject.update ( postParameters );
						String string = selectedAddressAPIObject.createJSON ( null ).toString ();
						request = ServerManager.postRequest ( ServerManager.UPDATE_ADDRESS, selectedAddressAPIObject.createJSON ( null ).toString () );
						jsonRequest = new JSONObject ( request );
					}
					result = jsonRequest.getString ( "parameters" );

				} catch ( Exception e )
				{
					result = e.getMessage ();
					e.printStackTrace ();
				}
				return result;
			}

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
					controller.onBackPressed ();
				} else
				{
					Toast.makeText ( controller, result, Toast.LENGTH_LONG ).show ();
				}
			}
		}.execute ();
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

		stateSpinner.setOnItemSelectedListener ( new OnItemSelectedListener ()
		{
			@Override
			public void onItemSelected ( AdapterView < ? > adapter, View view, int selected, long l )
			{
				stateTextView.setText ( adapter.getAdapter ().getItem ( selected ).toString () );
			}

			@Override
			public void onNothingSelected ( AdapterView < ? > adapterView )
			{
			}

		} );

		addAddressButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				addAddress ();
			}
		} );

        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAddress();
            }
        });
	}
}