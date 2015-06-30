package com.vallverk.handyboy.view.profile;

import java.io.IOException;
import java.util.Locale;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.AddressWraper;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.AddressAutocompleteAdapter;
import com.vallverk.handyboy.view.base.AutocompleteAdapter;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.DownloadableImageView;
import com.vallverk.handyboy.view.base.DownloadableImageView.Quality;

public class CustomerEditProfileViewFragment extends BaseFragment
{
	protected AutoCompleteTextView locationEditText;
	protected Button saveButton;
	protected DownloadableImageView avatarImageView;
	protected EditText firstEditText;
	protected EditText lastEditText;
	protected View topbarContainer;
	protected Bitmap avatar;
	protected Address location;
	private UserAPIObject user;
	private View menuImageView;
	
	private APIManager apiManager;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.customer_edit_profile_layout, null );

		locationEditText = ( AutoCompleteTextView ) view.findViewById ( R.id.locationEditText );
		saveButton = ( Button ) view.findViewById ( R.id.saveButton );
		avatarImageView = ( DownloadableImageView ) view.findViewById ( R.id.avatarImageView );
		firstEditText = ( EditText ) view.findViewById ( R.id.firstEditText );
		lastEditText = ( EditText ) view.findViewById ( R.id.lastEditText );
		topbarContainer = view.findViewById ( R.id.topbarContainer );
		menuImageView = view.findViewById ( R.id.menuImageView );

		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated(savedInstanceState);
		apiManager = APIManager.getInstance ();
		user = apiManager.getUser ();

		updateComponents();
		addListeners();
	}

	private void updateComponents ()
	{
		locationEditText.setAdapter ( new AddressAutocompleteAdapter ( getActivity (), locationEditText.getText ().toString () ) );

		firstEditText.setText ( user.getValue ( UserParams.FIRST_NAME ).toString () );
		lastEditText.setText ( user.getValue ( UserParams.LAST_NAME ).toString () );
		locationEditText.setText ( user.getLocationText () );
		try {
			avatarImageView.update(user.getValue(UserParams.AVATAR).toString(), Quality.MEDIUM);
		}catch (Exception ex){
			//avatar is null
		}
	}

	protected void addListeners ()
	{
		locationEditText.setOnItemClickListener ( new OnItemClickListener ()
		{
			public void onItemClick ( AdapterView < ? > arg0, View arg1, int position, long arg3 )
			{
				AutocompleteAdapter adapter = ( AutocompleteAdapter ) arg0.getAdapter ();
				AddressWraper addressWraper = ( AddressWraper ) adapter.getValue ( position );
				location = addressWraper.getAddress ();
			}
		} );
		saveButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				save ();
			}
		} );
		avatarImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View arg0 )
			{
				chooseMedia ( MediaType.PHOTO );
			}
		} );
		
		menuImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.openMenu ();
			}
		} );
	}

	protected void save ()
	{
		/*
		 * if ( avatar == null ) { Toast.makeText ( getActivity (),
		 * R.string.choose_avatar, Toast.LENGTH_LONG ).show (); return; }
		 */
		final String firstName = firstEditText.getText ().toString ().trim ();
		if ( firstName.isEmpty () )
		{
			Toast.makeText ( getActivity (), R.string.whats_your_name, Toast.LENGTH_LONG ).show ();
			return;
		}
		final String lastName = lastEditText.getText ().toString ().trim ();
		if ( lastName.isEmpty () )
		{
			Toast.makeText ( getActivity (), R.string.whats_your_name, Toast.LENGTH_LONG ).show ();
			return;
		}

		// if ( location == null )
		// {
		// Toast.makeText ( getActivity (), R.string.what_is_your_location,
		// Toast.LENGTH_LONG ).show ();
		// return;
		// }

		user.putValue ( UserParams.FIRST_NAME, firstName );
		user.putValue ( UserParams.LAST_NAME, lastName );
		if ( location != null )
		{
			user.putValue ( UserParams.LATITUDE, "" + location.getLatitude () );
			user.putValue ( UserParams.LONGITUDE, "" + location.getLongitude () );
		}

		new AsyncTask < Void, Void, String > ()
		{

			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					result = apiManager.update ( user, ServerManager.USER_UPDATE_URI );
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
				} else
				{
					Toast.makeText ( controller, result, Toast.LENGTH_LONG ).show ();
				}
				controller.setState ( VIEW_STATE.FEED );
			}
		}.execute ();
	}

	@Override
	protected void photoFromEditor ( Uri uri )
	{
		try
		{
			final Bitmap bitmap = Tools.decodeSampledBitmapFromResource ( MainActivity.MAX_POSTED_IMAGE_WIDTh, MainActivity.MAX_POSTED_IMAGE_HEIGHT, getActivity ().getContentResolver (), uri );
			avatar = bitmap;
			avatarImageView.setImageBitmap ( bitmap );
		} catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
	}
}