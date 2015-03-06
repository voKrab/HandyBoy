package com.vallverk.handyboy.view.registration;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.AddressWraper;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.model.api.UserDetailsAPIObject;
import com.vallverk.handyboy.model.api.UserDetailsAPIObject.UserDetailsParams;
import com.vallverk.handyboy.view.AddressAutocompleteAdapter;
import com.vallverk.handyboy.view.base.AutocompleteAdapter;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.MultiChoiceSpinner;
import com.vallverk.handyboy.view.base.SingleChoiceSpinner;
import com.vallverk.handyboy.view.controller.RegistrationController;

public class RegistrationCustomerBIOViewFragment extends BaseFragment
{
	protected AutoCompleteTextView locationEditText;
	protected Button saveButton;
	protected ImageView avatarImageView;
	protected EditText firstEditText;
	protected EditText lastEditText;
	protected View topbarContainer;
	protected Bitmap avatar;
	protected Address location;
	private UserAPIObject user;
	private RegistrationController controller;
	private View backImageView;
	private View backTextView;
    private String avatarUrl;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.registration_customer_bio_layout, null );

		locationEditText = ( AutoCompleteTextView ) view.findViewById ( R.id.locationEditText );
		saveButton = ( Button ) view.findViewById ( R.id.saveButton );
		avatarImageView = ( ImageView ) view.findViewById ( R.id.avatarImageView );
		firstEditText = ( EditText ) view.findViewById ( R.id.firstEditText );
		lastEditText = ( EditText ) view.findViewById ( R.id.lastEditText );
		topbarContainer = view.findViewById ( R.id.topbarContainer );
		backImageView = view.findViewById ( R.id.backImageView );
		backTextView = view.findViewById ( R.id.backTextView );

		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		
		controller = MainActivity.getInstance ().getRegistrationController ();
		user = APIManager.getInstance ().getUser ();
		
		updateComponents ();
		addListeners ();
	}

	private void updateComponents ()
	{
		locationEditText.setAdapter ( new AddressAutocompleteAdapter ( getActivity (), locationEditText.getText ().toString () ) );
	
		firstEditText.setText ( user.getValue ( UserParams.FIRST_NAME ).toString () );
		lastEditText.setText ( user.getValue ( UserParams.LAST_NAME ).toString () );
		try
		{
			double latitude = Double.parseDouble ( ( String ) user.getValue ( UserParams.LATITUDE ) );
			double longitude = Double.parseDouble  ( ( String ) user.getValue ( UserParams.LONGITUDE ) );
			Geocoder gcd = new Geocoder ( MainActivity.getInstance ().getBaseContext (), Locale.getDefault () );
			location = gcd.getFromLocation ( latitude, longitude, 1 ).get ( 0 );
			AddressWraper wraper = new AddressWraper ( location );
			locationEditText.setText ( wraper.toString () );
		} catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Bitmap avatar = controller.getAvatar ();
//		if ( avatar != null )
//		{
//			this.avatar = avatar;
//			avatarImageView.setImageBitmap ( avatar );
//		}

        avatarUrl = user.getString(UserParams.AVATAR);
        if(avatarUrl != null && !avatarUrl.isEmpty()){
            ImageLoader.getInstance().displayImage(avatarUrl, avatarImageView);
        }else{
            Bitmap avatar = controller.getAvatar ();
            if ( avatar != null )
            {
                this.avatar = avatar;
                avatarImageView.setImageBitmap ( avatar );
            }
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
				next ();
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
		
		backImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.prevStep ();
			}
		});
		backTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.prevStep ();
			}
		});
	}

	protected void next ()
	{
        if ( avatar == null && (avatarUrl == null && avatarUrl.isEmpty()))
		{
			Toast.makeText ( getActivity (), R.string.choose_avatar, Toast.LENGTH_LONG ).show ();
			return;
		}
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
		
//		if ( location == null )
//		{
//			Toast.makeText ( getActivity (), R.string.what_is_your_location, Toast.LENGTH_LONG ).show ();
//			return;
//		}
		
		controller.setAvatar ( avatar );
		user.putValue ( UserParams.FIRST_NAME, firstName );
		user.putValue ( UserParams.LAST_NAME, lastName );
//		user.putValue ( UserParams.LATITUDE, "" + location.getLatitude () );
//		user.putValue ( UserParams.LONGITUDE, "" + location.getLongitude () );

//		controller.nextStep ();
		controller.finish ();
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