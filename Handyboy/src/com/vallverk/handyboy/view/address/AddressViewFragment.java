package com.vallverk.handyboy.view.address;

import java.util.List;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.AddressAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.AddressAPIObject.AddressParams;
import com.vallverk.handyboy.model.job.TypeJobEnum;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.controller.BookingController;

import android.animation.LayoutTransition;
import android.database.CursorJoiner.Result;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddressViewFragment extends BaseFragment
{
	private ImageView backImageView;
	private TextView backTextView;
	private LinearLayout mainContainer;

	public List < AddressAPIObject > listAddressAPIObjects;
	public LinearLayout addressContainer;
	public LayoutInflater layoutInflater;

	private AddressAPIObject selectedAddressAPIObject;

	private View addAddressButton;

	private APIManager apiManager;
	private UserAPIObject user;

	private Button saveButton;
	private BookingController bookingController;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.address_layout, container, false );
			backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
			backTextView = ( TextView ) view.findViewById ( R.id.backTextView );

			addressContainer = ( LinearLayout ) view.findViewById ( R.id.addressContainer );
			addAddressButton = view.findViewById ( R.id.addAddressButton );

			mainContainer = ( LinearLayout ) view.findViewById ( R.id.mainContainer );
			mainContainer.getLayoutTransition ().enableTransitionType ( LayoutTransition.CHANGING );

			saveButton = ( Button ) view.findViewById ( R.id.saveButton );
		} else
		{
			( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}

	@Override
	protected void init ()
	{
		new AsyncTask < Void, Void, String > ()
		{
			@Override
			protected String doInBackground ( Void... params )
			{
				String status = "";
				try
				{
					listAddressAPIObjects = bookingController.loadAddresses ();
				} catch ( Exception e )
				{
					e.printStackTrace ();
				}
				return status;
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
					updateComponents ();
					addListeners ();
					updateFonts ();
				} else
				{
					Toast.makeText ( controller, result, Toast.LENGTH_LONG ).show ();
					controller.onBackPressed ();
				}
			}
		}.execute ();
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		apiManager = APIManager.getInstance ();
		bookingController = controller.getBookingController ();
		user = apiManager.getUser ();
		layoutInflater = LayoutInflater.from ( controller );
	}

	private void updateComponents ()
	{
		if ( bookingController.isPersonalTrainer () )
		{
			addAddressButton.setVisibility ( bookingController.getTempData ().isOtherLocation ? View.VISIBLE : View.GONE );
		}
		updateAddressList ();
	}

	private void updateAddressList ()
	{
		addressContainer.removeAllViews ();
		AddressAPIObject savedAddressAPIObject = controller.getBookingController ().getAddress ();
		for ( AddressAPIObject addressAPIObject : listAddressAPIObjects )
		{
			LinearLayout addressItem = ( LinearLayout ) layoutInflater.inflate ( R.layout.address_item_layout, null );

			TextView addressTextView = ( TextView ) addressItem.findViewById ( R.id.addressTextView );
			addressTextView.setText ( addressAPIObject.getString ( AddressParams.DESCRIPTION ) );

			TextView addressDescTextView = ( TextView ) addressItem.findViewById ( R.id.addressDescTextView );
			addressDescTextView.setText ( addressAPIObject.getString ( AddressParams.ADDRESS ) );

			ImageView addressSettingsImageView = ( ImageView ) addressItem.findViewById ( R.id.addressSettingsImageView );
			addressSettingsImageView.setTag ( addressAPIObject );
			addressSettingsImageView.setOnClickListener ( addressSettingsClickListener );

			addressItem.setTag ( addressAPIObject );
			addressItem.setOnClickListener ( addressClickListener );
			if ( savedAddressAPIObject != null && savedAddressAPIObject.getId ().equals ( addressAPIObject.getId () ) )
			{
				addressItem.setActivated ( true );
			}
			addressContainer.addView ( addressItem );
		}
	}

	private OnClickListener addressClickListener = new OnClickListener ()
	{
		@Override
		public void onClick ( View view )
		{
			selectedAddressAPIObject = null;
			if ( view.isActivated () )
			{
				view.setActivated ( false );
			} else
			{
				for ( int i = 0; i < addressContainer.getChildCount (); i++ )
				{
					addressContainer.getChildAt ( i ).setActivated ( false );
				}
				view.setActivated ( true );
				selectedAddressAPIObject = ( AddressAPIObject ) view.getTag ();
			}
		}
	};

	private OnClickListener addressSettingsClickListener = new OnClickListener ()
	{

		@Override
		public void onClick ( View view )
		{
			controller.setCommunicationValue ( AddressAPIObject.class.getSimpleName (), ( AddressAPIObject ) view.getTag () );
			controller.setState ( VIEW_STATE.ADD_ADDRESS );
		}
	};

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

		addAddressButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.getBookingController ().setState ( VIEW_STATE.ADD_ADDRESS );
			}
		} );

		saveButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				if ( selectedAddressAPIObject != null )
				{
					controller.getBookingController ().setAddress ( selectedAddressAPIObject );
					controller.onBackPressed ();
				} else
				{
					Toast.makeText ( controller, getString ( R.string.please_select_address ), Toast.LENGTH_LONG ).show ();
				}
			}
		} );
	}
}