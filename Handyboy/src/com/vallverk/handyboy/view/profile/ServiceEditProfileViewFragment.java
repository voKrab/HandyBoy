package com.vallverk.handyboy.view.profile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
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
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.AddressWraper;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.GalleryAPIObject;
import com.vallverk.handyboy.model.api.GalleryAPIObject.GalleryAPIParams;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.model.api.UserDetailsAPIObject;
import com.vallverk.handyboy.model.api.UserDetailsAPIObject.UserDetailsParams;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.AddressAutocompleteAdapter;
import com.vallverk.handyboy.view.base.AutocompleteAdapter;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.DownloadableImageView;
import com.vallverk.handyboy.view.base.MultiChoiceSpinner;
import com.vallverk.handyboy.view.base.SingleChoiceSpinner;
import com.vallverk.handyboy.view.base.DownloadableImageView.Quality;

public class ServiceEditProfileViewFragment extends BaseFragment
{
	protected TextView feetTextView;
	protected TextView inchesTextView;
	protected SingleChoiceSpinner feetSpinner;
	protected SingleChoiceSpinner inchesSpinner;
	protected SingleChoiceSpinner weightSpinner;
	protected SingleChoiceSpinner hairColorSpinner;
	protected SingleChoiceSpinner eyeColorSpinner;
	protected MultiChoiceSpinner bodyTypeSpinner;
	protected SingleChoiceSpinner sexualitySpinner;
	protected MultiChoiceSpinner ethentitySpinner;
	protected AutoCompleteTextView locationEditText;
	protected Button saveButton;
	protected DownloadableImageView avatarImageView;
	protected EditText firstEditText;
	protected EditText lastEditText;
	protected View topbarContainer;
	protected Bitmap avatar;
	protected Address location;
	private UserAPIObject user;
	private UserDetailsAPIObject userDetails;
	private APIManager apiManager;
	private View menuImageView;
	private EditText introduceYouselfEditText;

	private GridLayout addPhotosGridLayout;
	private boolean isAvatarChoise = true;
	private List < GalleryAPIObject > galleryItems;

	private Integer[] weightArray;
	private int defaultWeight = 150;
	private boolean isLocationChange;
	private boolean isUserChange;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.service_edit_profile_layout, null );

		feetTextView = ( TextView ) view.findViewById ( R.id.feetTextView );
		inchesTextView = ( TextView ) view.findViewById ( R.id.inchesTextView );
		feetSpinner = ( SingleChoiceSpinner ) view.findViewById ( R.id.feetSpinner );
		inchesSpinner = ( SingleChoiceSpinner ) view.findViewById ( R.id.inchesSpinner );
		weightSpinner = ( SingleChoiceSpinner ) view.findViewById ( R.id.weightSpinner );
		hairColorSpinner = ( SingleChoiceSpinner ) view.findViewById ( R.id.hairColorSpinner );
		eyeColorSpinner = ( SingleChoiceSpinner ) view.findViewById ( R.id.eyeColorSpinner );
		bodyTypeSpinner = ( MultiChoiceSpinner ) view.findViewById ( R.id.bodyTypeSpinner );
		sexualitySpinner = ( SingleChoiceSpinner ) view.findViewById ( R.id.sexualitySpinner );
		ethentitySpinner = ( MultiChoiceSpinner ) view.findViewById ( R.id.ethentitySpinner );
		locationEditText = ( AutoCompleteTextView ) view.findViewById ( R.id.locationEditText );
		saveButton = ( Button ) view.findViewById ( R.id.saveButton );
		avatarImageView = ( DownloadableImageView ) view.findViewById ( R.id.avatarImageView );
		firstEditText = ( EditText ) view.findViewById ( R.id.firstEditText );
		lastEditText = ( EditText ) view.findViewById ( R.id.lastEditText );
		topbarContainer = view.findViewById ( R.id.topbarContainer );
		menuImageView = view.findViewById ( R.id.menuImageView );

		introduceYouselfEditText = ( EditText ) view.findViewById ( R.id.introduceYouselfEditText );

		addPhotosGridLayout = ( GridLayout ) view.findViewById ( R.id.addPhotosGridLayout );

		return view;
	}

	@Override
	protected void init ()
	{
		// UPDATE USER DETAILS
		new AsyncTask < Void, Void, String > ()
		{

			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					String serviceId = user.getId ();
					userDetails = ( UserDetailsAPIObject ) apiManager.getAPIObject ( serviceId, UserDetailsAPIObject.class, ServerManager.USER_DETAILS_FETCH_URI );
					userDetails.setId ( user.getValue ( UserParams.SERVICE_ID ).toString () );
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
					updateUserDetails ();
				} else
				{
					controller.onBackPressed ();
					Toast.makeText ( controller, result, Toast.LENGTH_LONG ).show ();
				}
			}
		}.execute ();

		avatarImageView.update ( user.getValue ( UserParams.AVATAR ).toString (), Quality.MEDIUM );
		updateGalery ();
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		apiManager = APIManager.getInstance ();
		user = apiManager.getUser ();
		isUserChange = false;
		isLocationChange = false;

		updateComponents ();
		addListeners ();
	}

	private void updateGalery ()
	{
		// UPDATE GALLERY
		new AsyncTask < Void, Void, String > ()
		{
			public void onPostExecute ( String result )
			{
				super.onPostExecute ( result );
				updateGaleryPhotos ( galleryItems.size (), galleryItems );
			}

			@Override
			protected String doInBackground ( Void... params )
			{
				try
				{
					galleryItems = new ArrayList < GalleryAPIObject > ();
					String url = ServerManager.GET_GALLERY_URI + user.getValue ( UserParams.SERVICE_ID ).toString ();
					String responseText = ServerManager.getRequest ( url );
					if ( responseText.isEmpty () )
					{
						throw new Exception ();
					}
					if ( responseText != null && !responseText.isEmpty () )
					{
						JSONArray arrayData = new JSONArray ( responseText );

						for ( int i = 0; i < arrayData.length (); i++ )
						{
							JSONObject jsonObject = new JSONObject ( arrayData.getString ( i ) );
							GalleryAPIObject galleryItem = new GalleryAPIObject ( jsonObject );
							galleryItems.add ( galleryItem );
						}
					}
				} catch ( Exception e )
				{
					e.printStackTrace ();
				}
				return null;
			}
		}.execute ();
	}

	private void uploadToGallery ( final Bitmap galleryItem )
	{
		// UPDATE USER DETAILS
		new AsyncTask < Void, Void, String > ()
		{
			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					if ( galleryItem != null )
					{
						String url = apiManager.saveBitmap ( galleryItem );
						JSONObject jsonObject = new JSONObject ();
						jsonObject.accumulate ( GalleryAPIParams.SERVICE_ID.toString (), user.getValue ( UserParams.SERVICE_ID ).toString () );
						jsonObject.accumulate ( GalleryAPIParams.URL.toString (), url );
						jsonObject.accumulate ( GalleryAPIParams.TYPE.toString (), "image" );
						// jsonObject.accumulate (
						// GalleryAPIParams.DESCRIPTION.toString (), "" );
						jsonObject.accumulate ( GalleryAPIParams.STATUS.toString (), "" );
						GalleryAPIObject galleryAPIObject = new GalleryAPIObject ( jsonObject );
						result = apiManager.insert ( galleryAPIObject, ServerManager.ADD_GALLERY_URI );
						// typejobAPiObject.putValue (
						// TypeJobServiceParams.APROVE_FILE, url );
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
					updateGalery ();
				} else
				{
					Toast.makeText ( controller, result, Toast.LENGTH_LONG ).show ();
				}
			}
		}.execute ();
	}

	private void updateComponents ()
	{
		Integer[] feetArray = new Integer[] { 4, 5, 6, 7, 8 };
		feetSpinner.setData ( feetArray );
		Integer[] ihchesArray = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
		inchesSpinner.setData ( ihchesArray );
		weightArray = new Integer[210];
		int startOffsetWeight = 90;
		for ( int i = 0; i < weightArray.length; i++ )
		{
			weightArray[i] = i + startOffsetWeight;
		}

		bodyTypeSpinner.setItems ( Arrays.asList ( controller.getResources ().getStringArray ( R.array.body_types ) ), "", "" );
		// sexualitySpinner.setItems ( Arrays.asList ( controller.getResources
		// ().getStringArray ( R.array.sexuality ) ), "", "" );
		ethentitySpinner.setItems ( Arrays.asList ( controller.getResources ().getStringArray ( R.array.ethentity ) ), "", "" );
		locationEditText.setAdapter ( new AddressAutocompleteAdapter ( getActivity (), locationEditText.getText ().toString () ) );

		firstEditText.setText ( user.getValue ( UserParams.FIRST_NAME ).toString () );
		lastEditText.setText ( user.getValue ( UserParams.LAST_NAME ).toString () );
		locationEditText.setText ( user.getLocationText () );
	}

	private void updateUserDetails ()
	{
		String height = ( String ) userDetails.getValue ( UserDetailsParams.HEIGHT );
		String[] heightSplited = height.split ( "," );
		inchesTextView.setText ( heightSplited[1] );
		feetSpinner.setSelected ( Integer.parseInt ( heightSplited[0] ) );
		inchesSpinner.setSelected ( Integer.parseInt ( heightSplited[1] ) );
		try
		{
			int weight = Integer.parseInt ( ( String ) userDetails.getValue ( UserDetailsParams.WEIGHT ) );
			if ( weightSpinner.getCount () == 1 )
			{
				weightSpinner.setData ( weightArray );
			}
			weightSpinner.setOnTouchListener ( null );
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
				hairColorSpinner.setData ( controller.getResources ().getStringArray ( R.array.hair_colors ) );
			}
			hairColorSpinner.setSelected ( heirColor );
		}

		String eyeColor = ( String ) userDetails.getValue ( UserDetailsParams.EYE_COLOR );
		if ( eyeColor != null && !eyeColor.isEmpty () )
		{
			if ( eyeColorSpinner.getAdapter () != null && ( eyeColorSpinner.getAdapter ().getCount () > 0 ) )
			{
				eyeColorSpinner.setData ( controller.getResources ().getStringArray ( R.array.eye_colors ) );
			}
			eyeColorSpinner.setSelected ( eyeColor );
		}

		String sexuality = ( String ) userDetails.getValue ( UserDetailsParams.SEX );
		if ( sexuality != null && !sexuality.isEmpty () )
		{
			if ( sexualitySpinner.getAdapter () != null && ( sexualitySpinner.getAdapter ().getCount () > 0 ) )
			{
				sexualitySpinner.setData ( controller.getResources ().getStringArray ( R.array.sexuality ) );
			}
			sexualitySpinner.setSelected ( sexuality );
		}

		bodyTypeSpinner.setSelection ( ( String ) userDetails.getValue ( UserDetailsParams.BODY_TYPE ) );
		// sexualitySpinner.setSelection ( ( String ) userDetails.getValue (
		// UserDetailsParams.SEX ) );
		ethentitySpinner.setSelection ( ( String ) userDetails.getValue ( UserDetailsParams.ETHNICITY ) );
		introduceYouselfEditText.setText ( ( String ) userDetails.getValue ( UserDetailsParams.DESCRIPTION ) );
	}

	private void updateGaleryPhotos ( int total, List < GalleryAPIObject > typejobs )
	{
		int MAX_COUNT = 6;
		int columnCount = 3;
		int rowCount = total / columnCount;
		addPhotosGridLayout.removeAllViews ();
		addPhotosGridLayout.setColumnCount ( columnCount );
		addPhotosGridLayout.setRowCount ( rowCount + 1 );
		if ( total == 0 )
		{
			addPhotosGridLayout.addView ( addGaleryItemView ( true, 0, 0, columnCount, null ) );
		} else
		{
			for ( int i = 0, column = 0, row = 0; i < total; i++, column++ )
			{
				if ( column == columnCount )
				{
					column = 0;
					row++;
				}
				addPhotosGridLayout.addView ( addGaleryItemView ( false, column, row, columnCount, typejobs.get ( i ).getValue ( GalleryAPIParams.URL ).toString () ) );
				if ( total != MAX_COUNT && i == ( total - 1 ) )
				{
					addPhotosGridLayout.addView ( addGaleryItemView ( true, column + 1, row, columnCount, null ) );
				}
			}
		}
	}

	private ImageView addGaleryItemView ( boolean isPlus, int column, int row, int columnCount, String imageUrl )
	{
		DownloadableImageView oImageView = new DownloadableImageView ( controller );
		if ( isPlus )
		{
			oImageView.setImageResource ( R.drawable.plus_photo_add );
		} else
		{
			oImageView.setImageResource ( R.drawable.avatar );
		}

		GridLayout.LayoutParams param = new GridLayout.LayoutParams ();
		param.height = Tools.fromDPToPX ( 30, controller );
		param.width = Tools.fromDPToPX ( 30, controller );
		param.topMargin = Tools.fromDPToPX ( 4, controller );

		if ( column != 2 || column != 5 )
		{
			param.rightMargin = Tools.fromDPToPX ( 4, controller );
		}
		param.setGravity ( Gravity.CENTER );
		if ( isPlus )
		{
			if ( column == columnCount )
			{
				param.columnSpec = GridLayout.spec ( 0 );
				param.rowSpec = GridLayout.spec ( row + 1 );
			} else
			{
				param.columnSpec = GridLayout.spec ( column );
				param.rowSpec = GridLayout.spec ( row );
			}
			oImageView.setLayoutParams ( param );
			oImageView.setOnClickListener ( new OnClickListener ()
			{

				@Override
				public void onClick ( View v )
				{
					isAvatarChoise = false;
					chooseMedia ( MediaType.PHOTO );
				}
			} );
		} else
		{
			param.columnSpec = GridLayout.spec ( column );
			param.rowSpec = GridLayout.spec ( row );
			oImageView.setLayoutParams ( param );
			oImageView.update ( imageUrl, Quality.LOW );
			oImageView.setOnClickListener ( new OnClickListener ()
			{

				@Override
				public void onClick ( View v )
				{
					controller.setState ( VIEW_STATE.GALLERY );
				}
			} );
		}
		return oImageView;
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
			public void onClick ( View view )
			{
				isAvatarChoise = true;
				chooseMedia ( MediaType.PHOTO );
			}
		} );
		feetSpinner.setOnItemSelectedListener ( new OnItemSelectedListener ()
		{
			@Override
			public void onItemSelected ( AdapterView < ? > adapter, View arg1, int selected, long arg3 )
			{
				Integer newValue = ( Integer ) adapter.getAdapter ().getItem ( selected );
				feetTextView.setText ( "" + newValue );
			}

			@Override
			public void onNothingSelected ( AdapterView < ? > arg0 )
			{
			}
		} );

		inchesSpinner.setOnItemSelectedListener ( new OnItemSelectedListener ()
		{
			@Override
			public void onItemSelected ( AdapterView < ? > adapter, View arg1, int selected, long arg3 )
			{
				Integer newValue = ( Integer ) adapter.getAdapter ().getItem ( selected );
				inchesTextView.setText ( "" + newValue );
			}

			@Override
			public void onNothingSelected ( AdapterView < ? > adapterView )
			{
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

		feetTextView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
				feetSpinner.performClick ();
			}
		} );

		inchesTextView.setOnClickListener ( new OnClickListener ()
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
						hairColorSpinner.setData ( controller.getResources ().getStringArray ( R.array.hair_colors ) );
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
						eyeColorSpinner.setData ( controller.getResources ().getStringArray ( R.array.eye_colors ) );
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
						sexualitySpinner.setData ( controller.getResources ().getStringArray ( R.array.sexuality ) );
					}
				}
				return false;
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
			Toast.makeText ( controller, R.string.whats_your_name, Toast.LENGTH_LONG ).show ();
			return;
		}
		final String lastName = lastEditText.getText ().toString ().trim ();
		if ( lastName.isEmpty () )
		{
			Toast.makeText ( controller, R.string.whats_your_name, Toast.LENGTH_LONG ).show ();
			return;
		}
		Integer feets = Integer.parseInt ( feetTextView.getText ().toString () );
		Integer inches = Integer.parseInt ( inchesTextView.getText ().toString () );
		if ( feets == 0 && inches == 0 )
		{
			Toast.makeText ( controller, R.string.how_tall_are_you, Toast.LENGTH_LONG ).show ();
			return;
		}
		if ( bodyTypeSpinner.isEmpty () )
		{
			Toast.makeText ( controller, R.string.what_is_your_body, Toast.LENGTH_LONG ).show ();
			return;
		}
		if ( locationEditText.getText ().toString ().isEmpty () )
		{
			Toast.makeText ( controller, R.string.what_is_your_location, Toast.LENGTH_LONG ).show ();
			return;
		}

		if ( sexualitySpinner.getSelectedItem ().toString ().isEmpty () )
		{
			Toast.makeText ( controller, R.string.what_is_your_sexuality, Toast.LENGTH_LONG ).show ();
			return;
		}

		if ( ethentitySpinner.isEmpty () )
		{
			Toast.makeText ( controller, R.string.what_is_your_ethentity, Toast.LENGTH_LONG ).show ();
			return;
		}

		if ( !firstName.equals ( user.getValue ( UserParams.FIRST_NAME ).toString () ) || !lastName.equals ( user.getValue ( UserParams.LAST_NAME ).toString () ) )
		{
			user.putValue ( UserParams.FIRST_NAME, firstName );
			user.putValue ( UserParams.LAST_NAME, lastName );
			isUserChange = true;
		}

		if ( location != null )
		{
			if ( !user.getValue ( UserParams.LONGITUDE ).toString ().equals ( location.getLongitude () ) || !user.getValue ( UserParams.LATITUDE ).toString ().equals ( location.getLatitude () ) )
			{
				user.putValue ( UserParams.LATITUDE, "" + location.getLatitude () );
				user.putValue ( UserParams.LONGITUDE, "" + location.getLongitude () );
				isLocationChange = true;
			}
		}

		userDetails.putValue ( UserDetailsParams.HEIGHT, feetTextView.getText ().toString () + "," + inchesTextView.getText ().toString () );
		userDetails.putValue ( UserDetailsParams.WEIGHT, "" + weightSpinner.getSelectedItem () );
		userDetails.putValue ( UserDetailsParams.HEIR_COLOR, hairColorSpinner.getSelectedItem ().toString () );
		userDetails.putValue ( UserDetailsParams.EYE_COLOR, eyeColorSpinner.getSelectedItem ().toString () );
		userDetails.putValue ( UserDetailsParams.BODY_TYPE, bodyTypeSpinner.getSelectedItems () );
		userDetails.putValue ( UserDetailsParams.SEX, sexualitySpinner.getSelectedItem ().toString () );
		userDetails.putValue ( UserDetailsParams.ETHNICITY, ethentitySpinner.getSelectedItems () );
		userDetails.putValue ( UserDetailsParams.DESCRIPTION, introduceYouselfEditText.getText () );

		new AsyncTask < Void, Void, String > ()
		{

			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					if ( avatar != null )
					{
						String url = apiManager.saveBitmap ( avatar );
						user.putValue ( UserParams.AVATAR, url );
						isUserChange = true;
					}

					if ( isUserChange )
					{
						apiManager.update ( user, ServerManager.USER_UPDATE_URI );
					}

					apiManager.update ( userDetails, ServerManager.USER_DETAILS_UPDATE_URI );
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
					controller.setState ( VIEW_STATE.DASHBOARD );
				} else
				{
					controller.onBackPressed ();
					Toast.makeText ( controller, result, Toast.LENGTH_LONG ).show ();
				}
			}
		}.execute ();

	}

	@Override
	protected void photoFromEditor ( Uri uri )
	{
		try
		{
			final Bitmap bitmap = Tools.decodeSampledBitmapFromResource ( MainActivity.MAX_POSTED_IMAGE_WIDTh, MainActivity.MAX_POSTED_IMAGE_HEIGHT, controller.getContentResolver (), uri );
			if ( isAvatarChoise )
			{
				avatar = bitmap;
				avatarImageView.setImageBitmap ( bitmap );
			} else
			{
				uploadToGallery ( bitmap );
			}

		} catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
	}

}