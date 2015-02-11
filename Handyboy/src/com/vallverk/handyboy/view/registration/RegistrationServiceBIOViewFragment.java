package com.vallverk.handyboy.view.registration;

import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.AddressWraper;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.model.api.UserDetailsAPIObject;
import com.vallverk.handyboy.model.api.UserDetailsAPIObject.UserDetailsParams;
import com.vallverk.handyboy.model.job.JobCategory;
import com.vallverk.handyboy.model.job.JobTypeManager;
import com.vallverk.handyboy.model.job.TypeJob;
import com.vallverk.handyboy.view.AddressAutocompleteAdapter;
import com.vallverk.handyboy.view.base.AutocompleteAdapter;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.MultiChoiceSpinner;
import com.vallverk.handyboy.view.base.SingleChoiceSpinner;
import com.vallverk.handyboy.view.controller.RegistrationController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class RegistrationServiceBIOViewFragment extends BaseFragment
{
	protected TextView feetEditText;
	protected TextView inchesEditText;
	protected SingleChoiceSpinner feetSpinner;
	protected SingleChoiceSpinner inchesSpinner;
	protected SingleChoiceSpinner weightSpinner;
	protected SingleChoiceSpinner hairColorSpinner;
	protected SingleChoiceSpinner eyeColorSpinner;
	protected MultiChoiceSpinner bodyTypeSpinner;
	protected SingleChoiceSpinner sexualitySpinner;
	protected MultiChoiceSpinner ethentitySpinner;
	protected MultiChoiceSpinner jobTypeSpinner;
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

	private EditText introduceYouselfEditText;

	private Integer[] weightArray;
	private int defaultWeight = 150;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.registration_sevice_bio_layout, null );

		feetEditText = ( TextView ) view.findViewById ( R.id.feetEditText );
		inchesEditText = ( TextView ) view.findViewById ( R.id.inchesEditText );
		feetSpinner = ( SingleChoiceSpinner ) view.findViewById ( R.id.feetSpinner );
		inchesSpinner = ( SingleChoiceSpinner ) view.findViewById ( R.id.inchesSpinner );
		weightSpinner = ( SingleChoiceSpinner ) view.findViewById ( R.id.weightSpinner );
		hairColorSpinner = ( SingleChoiceSpinner ) view.findViewById ( R.id.hairColorSpinner );
		eyeColorSpinner = ( SingleChoiceSpinner ) view.findViewById ( R.id.eyeColorSpinner );
		bodyTypeSpinner = ( MultiChoiceSpinner ) view.findViewById ( R.id.bodyTypeSpinner );
		sexualitySpinner = ( SingleChoiceSpinner ) view.findViewById ( R.id.sexualitySpinner );
		ethentitySpinner = ( MultiChoiceSpinner ) view.findViewById ( R.id.ethentitySpinner );
		jobTypeSpinner = ( MultiChoiceSpinner ) view.findViewById ( R.id.jobTypeSpinner );
		locationEditText = ( AutoCompleteTextView ) view.findViewById ( R.id.locationEditText );
		saveButton = ( Button ) view.findViewById ( R.id.saveButton );
		avatarImageView = ( ImageView ) view.findViewById ( R.id.avatarImageView );
		firstEditText = ( EditText ) view.findViewById ( R.id.firstEditText );
		lastEditText = ( EditText ) view.findViewById ( R.id.lastEditText );
		topbarContainer = view.findViewById ( R.id.topbarContainer );
		backImageView = view.findViewById ( R.id.backImageView );
		backTextView = view.findViewById ( R.id.backTextView );

		introduceYouselfEditText = ( EditText ) view.findViewById ( R.id.introduceYouselfEditText );

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
		Integer[] feetArray = new Integer[] { 4, 5, 6, 7, 8 };
		feetSpinner.setData ( feetArray );
		Integer[] ihchesArray = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
		inchesSpinner.setData ( ihchesArray );
		weightArray = new Integer[211];
		int startOffsetWeight = 90;
		for ( int i = 0; i < weightArray.length; i++ )
		{
			weightArray[i] = i + startOffsetWeight;
		}

		List < String > jobs = new ArrayList < String > ();
		for ( TypeJob typeJob : JobTypeManager.getInstance ().getJobTypes ( JobCategory.HOUSE_BOY ) )
		{
			jobs.add ( typeJob.getName () );
		}

		for ( TypeJob typeJob : JobTypeManager.getInstance ().getJobTypes ( JobCategory.MUSCLE_BOY ) )
		{
			jobs.add ( typeJob.getName () );
		}

		for ( TypeJob typeJob : JobTypeManager.getInstance ().getJobTypes ( JobCategory.PARTY_BOY ) )
		{
			jobs.add ( typeJob.getName () );
		}
		jobTypeSpinner.setItems ( jobs, "", "" );
		jobTypeSpinner.setSelection ( controller.getTypeJobs () );
		bodyTypeSpinner.setItems ( Arrays.asList ( getResources ().getStringArray ( R.array.body_types ) ), "", "" );
		//sexualitySpinner.setItems ( Arrays.asList ( getResources ().getStringArray ( R.array.sexuality ) ), "", "" );
		ethentitySpinner.setItems ( Arrays.asList ( getResources ().getStringArray ( R.array.ethentity ) ), "", "" );
		locationEditText.setAdapter ( new AddressAutocompleteAdapter ( getActivity (), locationEditText.getText ().toString () ) );

		firstEditText.setText ( user.getValue ( UserParams.FIRST_NAME ).toString () );
		lastEditText.setText ( user.getValue ( UserParams.LAST_NAME ).toString () );
		try
		{
			double latitude = Double.parseDouble ( ( String ) user.getValue ( UserParams.LATITUDE ) );
			double longitude = Double.parseDouble ( ( String ) user.getValue ( UserParams.LONGITUDE ) );
			Geocoder gcd = new Geocoder ( MainActivity.getInstance ().getBaseContext (), Locale.getDefault () );
			location = gcd.getFromLocation ( latitude, longitude, 1 ).get ( 0 );
			AddressWraper wraper = new AddressWraper ( location );
			locationEditText.setText ( wraper.toString () );
		} catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
		Bitmap avatar = controller.getAvatar ();
		if ( avatar != null )
		{
			this.avatar = avatar;
			avatarImageView.setImageBitmap ( avatar );
		}
		UserDetailsAPIObject userDetails = controller.getUserDetails ();
		if ( userDetails == null )
		{
			// setupTestData ();
		} else
		{
			int height = userDetails.getInt ( UserDetailsParams.HEIGHT );

            int inches = Tools.getInches ( height );
            int feets = Tools.getFeets ( height );

            inchesEditText.setText ( "" + inches );
            feetEditText.setText ( "" + feets );
            feetSpinner.setSelected ( "" + inches );
            inchesSpinner.setSelected ( "" + feets );
			try
			{
				int weight = Integer.parseInt ( ( String ) userDetails.getValue ( UserDetailsParams.WEIGHT ) );
				if ( weightSpinner.getCount () == 1 )
				{
					weightSpinner.setData ( weightArray );
					weightSpinner.setSelected ( defaultWeight );
				}
				weightSpinner.setSelected ( weight );
			} catch ( NumberFormatException e )
			{
				e.printStackTrace ();
			}

			String heirColor = ( String ) userDetails.getValue ( UserDetailsParams.HEIR_COLOR );
			if ( heirColor != null && !heirColor.isEmpty () )
			{
				if ( hairColorSpinner.getCount () == 1 )
				{
					hairColorSpinner.setData ( getResources ().getStringArray ( R.array.hair_colors ) );
				}
				hairColorSpinner.setSelected ( heirColor );
			}

			String eyeColor = ( String ) userDetails.getValue ( UserDetailsParams.EYE_COLOR );
			if ( eyeColor != null && !eyeColor.isEmpty () )
			{
				if ( eyeColorSpinner.getCount () == 1 )
				{
					eyeColorSpinner.setData ( getResources ().getStringArray ( R.array.eye_colors ) );
				}
				eyeColorSpinner.setSelected ( eyeColor );
			}
			
			String sexuality  = ( String ) userDetails.getValue ( UserDetailsParams.SEX );
			if ( sexuality != null && !sexuality.isEmpty () )
			{
				if ( sexualitySpinner.getAdapter () != null && ( sexualitySpinner.getAdapter ().getCount () > 0 ) )
				{
					sexualitySpinner.setData ( getResources ().getStringArray ( R.array.sexuality ) );
				}
				sexualitySpinner.setSelected ( sexuality );
			}

			bodyTypeSpinner.setSelection ( ( String ) userDetails.getValue ( UserDetailsParams.BODY_TYPE ) );
			//sexualitySpinner.setSelection ( ( String ) userDetails.getValue ( UserDetailsParams.SEX ) );
			ethentitySpinner.setSelection ( ( String ) userDetails.getValue ( UserDetailsParams.ETHNICITY ) );
			introduceYouselfEditText.setText ( 	userDetails.getString (UserDetailsParams.DESCRIPTION ).toString ());
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
		feetSpinner.setOnItemSelectedListener ( new OnItemSelectedListener ()
		{
			@Override
			public void onItemSelected ( AdapterView < ? > adapter, View arg1, int selected, long arg3 )
			{
				Integer newValue = ( Integer ) adapter.getAdapter ().getItem ( selected );
				feetEditText.setText ( "" + newValue );
			}

			@Override
			public void onNothingSelected ( AdapterView < ? > arg0 )
			{
				// TODO Auto-generated method stub
			}
		} );

		inchesSpinner.setOnItemSelectedListener ( new OnItemSelectedListener ()
		{
			@Override
			public void onItemSelected ( AdapterView < ? > adapter, View arg1, int selected, long arg3 )
			{
				Integer newValue = ( Integer ) adapter.getAdapter ().getItem ( selected );
				inchesEditText.setText ( "" + newValue );
			}

			@Override
			public void onNothingSelected ( AdapterView < ? > arg0 )
			{
				// TODO Auto-generated method stub

			}
		} );

		backImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.prevStep ();
			}
		} );
		backTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.prevStep ();
			}
		} );

		feetEditText.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View arg0 )
			{
				feetSpinner.performClick ();
			}
		} );

		inchesEditText.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				inchesSpinner.performClick ();
			}
		} );

		weightSpinner.setOnTouchListener ( new OnTouchListener ()
		{

			@Override
			public boolean onTouch ( View arg0, MotionEvent motionEvent )
			{
				if ( motionEvent.getAction () == MotionEvent.ACTION_UP )
				{
					if ( weightSpinner.getCount () == 1 )
					{
						weightSpinner.setData ( weightArray );
						weightSpinner.setSelected ( defaultWeight );
					}
				}
				return false;
			}
		} );

		hairColorSpinner.setOnTouchListener ( new OnTouchListener ()
		{

			@Override
			public boolean onTouch ( View arg0, MotionEvent motionEvent )
			{
				if ( motionEvent.getAction () == MotionEvent.ACTION_UP )
				{
					if ( hairColorSpinner.getCount () == 1 )
					{
						hairColorSpinner.setData ( getResources ().getStringArray ( R.array.hair_colors ) );
					}
				}
				return false;
			}
		} );

		eyeColorSpinner.setOnTouchListener ( new OnTouchListener ()
		{

			@Override
			public boolean onTouch ( View arg0, MotionEvent motionEvent )
			{
				if ( motionEvent.getAction () == MotionEvent.ACTION_UP )
				{
					if ( eyeColorSpinner.getCount () == 1 )
					{
						eyeColorSpinner.setData ( getResources ().getStringArray ( R.array.eye_colors ) );
					}
				}
				return false;
			}
		} );
		
		sexualitySpinner.setOnTouchListener ( new OnTouchListener ()
		{

			@Override
			public boolean onTouch ( View arg0, MotionEvent motionEvent )
			{
				if ( motionEvent.getAction () == MotionEvent.ACTION_UP )
				{
					if ( sexualitySpinner.getCount () == 1 )
					{
						sexualitySpinner.setData ( getResources ().getStringArray ( R.array.sexuality ) );
					}
				}
				return false;
			}
		} );
	}

	protected void next ()
	{
		//avatar = BitmapFactory.decodeResource(getActivity ().getResources(), R.drawable.your_money_button_a);
	
		if ( avatar == null )
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
		Integer feets = Integer.parseInt ( feetEditText.getText ().toString () );
		Integer inches = Integer.parseInt ( inchesEditText.getText ().toString () );
		if ( feets == 0 && inches == 0 )
		{
			Toast.makeText ( getActivity (), R.string.how_tall_are_you, Toast.LENGTH_LONG ).show ();
			return;
		}
		if ( bodyTypeSpinner.isEmpty () )
		{
			Toast.makeText ( getActivity (), R.string.what_is_your_body, Toast.LENGTH_LONG ).show ();
			return;
		}
		/*if ( sexualitySpinner.isEmpty () )
		{
			Toast.makeText ( getActivity (), R.string.what_is_your_sexuality, Toast.LENGTH_LONG ).show ();
			return;
		}*/
		if ( ethentitySpinner.isEmpty () )
		{
			Toast.makeText ( getActivity (), R.string.what_is_your_ethentity, Toast.LENGTH_LONG ).show ();
			return;
		}

		if ( jobTypeSpinner.isEmpty () )
		{
			Toast.makeText ( getActivity (), R.string.what_is_your_jobs, Toast.LENGTH_LONG ).show ();
			return;
		}

		if ( introduceYouselfEditText.getText ().toString ().isEmpty () )
		{
			Toast.makeText ( getActivity (), R.string.why_you_handy_boy, Toast.LENGTH_LONG ).show ();
			return;
		}

		controller.setAvatar ( avatar );
		user.putValue ( UserParams.FIRST_NAME, firstName );
		user.putValue ( UserParams.LAST_NAME, lastName );
		user.putValue ( UserParams.COMMENT,  introduceYouselfEditText.getText ().toString () + "("+ jobTypeSpinner.getSelectedItems ()+")");

		UserDetailsAPIObject service = controller.getUserDetails ();
		UserDetailsAPIObject userDetails = service == null ? new UserDetailsAPIObject () : service;
        userDetails.putValue ( UserDetailsParams.HEIGHT, Integer.parseInt(feetEditText.getText ().toString ()) * 12 + Integer.parseInt(inchesEditText.getText ().toString ()) );
		userDetails.putValue ( UserDetailsParams.WEIGHT, "" + weightSpinner.getSelectedItem () );
		userDetails.putValue ( UserDetailsParams.HEIR_COLOR, hairColorSpinner.getSelectedItem ().toString () );
		userDetails.putValue ( UserDetailsParams.EYE_COLOR, eyeColorSpinner.getSelectedItem ().toString () );
		userDetails.putValue ( UserDetailsParams.BODY_TYPE, bodyTypeSpinner.getSelectedItems () );
		userDetails.putValue ( UserDetailsParams.SEX, sexualitySpinner.getSelectedItem ().toString () );
		userDetails.putValue ( UserDetailsParams.ETHNICITY, ethentitySpinner.getSelectedItems () );
		userDetails.putValue ( UserDetailsParams.DESCRIPTION, introduceYouselfEditText.getText ().toString () );
		controller.setUserDetails ( userDetails );
		controller.setTypeJobs ( jobTypeSpinner.getSelectedItems () );
		controller.nextStep ();
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