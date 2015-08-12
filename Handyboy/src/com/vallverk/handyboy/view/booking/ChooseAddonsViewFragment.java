package com.vallverk.handyboy.view.booking;

import android.animation.LayoutTransition;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject.AddonServiceAPIParams;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.model.job.AddonId;
import com.vallverk.handyboy.model.job.TypeJobEnum;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.controller.BookingController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChooseAddonsViewFragment extends BaseFragment
{
	private View backImageView;
	private View backTextView;
	private View specialReqeustContainer;
	private EditText specialReqeustEditText;
	private BookingController bookingController;
	private List < AddonServiceAPIObject > addons;
	private LinearLayout addonsContainer;
	private LinearLayout specialAddonsContainer;
	private LinearLayout mainContainer;
	private LinearLayout lawnContainer;
	private TextView lawnTextView;
	private List < AddonServiceAPIObject > selectedAddons;
	private LayoutInflater inflater;
	private View saveButton;

	private int countRooms;
	private int countBathRooms;

	private boolean isRoomsChecked;
	private boolean isBathRoomsChecked;

	private SeekBar bathRoomsSeekBar;
	private SeekBar roomsSeekBar;

	private TextView roomsTextView;
	private TextView bathroomsTextView;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.choose_addons_layout, null );

		backImageView = view.findViewById ( R.id.backImageView );
		backTextView = view.findViewById ( R.id.backTextView );
		addonsContainer = ( LinearLayout ) view.findViewById ( R.id.addonsContainer );
		specialAddonsContainer = ( LinearLayout ) view.findViewById ( R.id.specialAddonsContainer );
		specialReqeustEditText = ( EditText ) view.findViewById ( R.id.specialReqeustEditText );
		specialReqeustContainer = view.findViewById ( R.id.specialReqeustContainer );
		mainContainer = ( LinearLayout ) view.findViewById ( R.id.mainContainer );
		mainContainer.getLayoutTransition ().enableTransitionType ( LayoutTransition.CHANGING );
		saveButton = view.findViewById ( R.id.saveButton );
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
				String result = "";
				try
				{
					List < AddonServiceAPIObject > allAddons = APIManager.getInstance ().loadList ( ServerManager.ADDONS_SERVICE_GET.replace ( "serviceId=1", "serviceId=" + bookingController.getHandyBoy ().getString ( UserParams.SERVICE_ID ) ), AddonServiceAPIObject.class );
					addons = new ArrayList < AddonServiceAPIObject > ();
					for ( AddonServiceAPIObject addon : allAddons )
					{

						if ( addon.getValue ( AddonServiceAPIParams.TYPE_JOB_SERVICE_ID ).toString().equals(bookingController.getJob().getId()) ) {
                            Log.d("BokingAddons", controller.getAddonName(addon.getString(AddonServiceAPIParams.JOB_ADDONS_ID)) + " " + addon.getString(AddonServiceAPIParams.JOB_ADDONS_ID));
                            if (bookingController.getJob().getTypeJob().getEnumValue() == TypeJobEnum.PERSONAL_TRAINER) {
								/*if ( addon.getString ( AddonServiceAPIParams.JOB_ADDONS_ID ).equals ( "" + AddonId.SHIRTLESS_8 ) )
								{
									addons.add ( addon );
								}*/
                            } else {
                                if (bookingController.isMasseur()) {
                                    if (addon.getInt(AddonServiceAPIParams.JOB_ADDONS_ID) != AddonId.WHAT_TYPE_OF_MASSAGE_DO_YOU_OFFER) {
                                        addons.add(addon);
                                    }
                                } else if (bookingController.isGoGoBoy()) {
                                    if (addon.getInt(AddonServiceAPIParams.JOB_ADDONS_ID) != AddonId.ATTIRE_2) {
                                        addons.add(addon);
                                    }
                                } else if (bookingController.isCarWash()) {
                                    if (addon.getInt(AddonServiceAPIParams.JOB_ADDONS_ID) != AddonId.ATTIRE_1 && addon.getInt(AddonServiceAPIParams.JOB_ADDONS_ID) != AddonId.BATHING_SUIT_1 && addon.getInt(AddonServiceAPIParams.JOB_ADDONS_ID) != AddonId.EXTERIOR) {
                                        addons.add(addon);
                                    }
                                } else if (bookingController.isPoolBoy()) {
                                    if (addon.getInt(AddonServiceAPIParams.JOB_ADDONS_ID) != AddonId.BATHING_SUIT_2) { // costyl remove  Attire 25
                                        addons.add(addon);
                                    }
                                } else {
                                    addons.add(addon);
                                }
                            }
                        }
					}
                    Collections.sort(addons, new Comparator<AddonServiceAPIObject>() {
                        @Override
                        public int compare(AddonServiceAPIObject addonServiceAPIObject, AddonServiceAPIObject addonServiceAPIObject2) {
                            if(addonServiceAPIObject.getInt(AddonServiceAPIParams.JOB_ADDONS_ID) >  addonServiceAPIObject2.getInt(AddonServiceAPIParams.JOB_ADDONS_ID)){
                                return 1;
                            }if(addonServiceAPIObject.getInt(AddonServiceAPIParams.JOB_ADDONS_ID) <  addonServiceAPIObject2.getInt(AddonServiceAPIParams.JOB_ADDONS_ID)){
                                return -1;
                            }else {
                                return 0;
                            }
                        }
                    });
					selectedAddons = bookingController.getAddons ();
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
					updateComponents ();
					addListeners ();
				} else
				{
					Toast.makeText ( controller, result, Toast.LENGTH_LONG ).show ();
					controller.onBackPressed ();
				}
			}
		}.execute ();
	}

	protected void clearFields ()
	{
		// jobContainer.setVisibility ( View.INVISIBLE );
		// priceTextView.setVisibility ( View.INVISIBLE );
	}

	private void updateComponents ()
	{
        if ( bookingController.isYardWorker () )
        {
            addLawnMoving ();
            updateLawnMoving ();
        }

        if ( bookingController.isHouseKeeper () )
        {
            addRoomsSelector ();
            addBathRoomsSelector ();
            updateRooms ();
        }

		for ( AddonServiceAPIObject addon : addons )
		{
			final View view = inflater.inflate ( R.layout.choose_addons_row_layout, null );
			TextView addonTextView = ( TextView ) view.findViewById ( R.id.addonTextView );
			TextView priceTextView = ( TextView ) view.findViewById ( R.id.priceTextView );
            TextView plusTextView = ( TextView ) view.findViewById ( R.id.plusTextView );
			ImageView selectImageView = ( ImageView ) view.findViewById ( R.id.selectImageView );
			selectImageView.setVisibility ( View.GONE );
			addonTextView.setText ( controller.getAddonName ( addon.getString ( AddonServiceAPIParams.JOB_ADDONS_ID ) ) );
            int price = 0;
            try{
                price = addon.getInt ( AddonServiceAPIParams.PRICE );
            }catch (Exception ex){
                price = 0;
            }

            priceTextView.setText ( price == 0 ? "" : "$" + price );
            plusTextView.setVisibility ( price == 0 ? View.GONE : View.VISIBLE );
			view.setTag ( addon );
			view.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View view )
				{
					AddonServiceAPIObject addon = ( AddonServiceAPIObject ) view.getTag ();
					addonSelected ( addon );
					updateSelectionComponents ();
				}
			} );
			addonsContainer.addView ( view );
		}

		String specialRequest = bookingController.getSpecialRequest ();
		if ( specialRequest.isEmpty () )
		{
			specialReqeustEditText.setVisibility ( View.GONE );
			specialReqeustEditText.setText ( "" );
		} else
		{
			specialReqeustEditText.setVisibility ( View.VISIBLE );
			specialReqeustEditText.setText ( specialRequest );
		}
		updateSelectionComponents ();
	}

	protected void updateSelectionComponents ()
	{
		for ( int i = 0; i < addonsContainer.getChildCount (); i++ )
		{
			View view = addonsContainer.getChildAt ( i );
			ImageView selectImageView = ( ImageView ) view.findViewById ( R.id.selectImageView );
			selectImageView.setVisibility ( selectedAddons.contains ( view.getTag () ) ? View.VISIBLE : View.GONE );
		}
	}

	private void updateRooms ()
	{
		if ( bookingController.getCountBathRooms () > 0 && bathroomsTextView != null )
		{
			bathroomsTextView.performClick ();
			bathRoomsSeekBar.setProgress ( bookingController.getCountBathRooms () - 1 );
		}

		if ( bookingController.getCountRooms () > 0 && roomsTextView != null )
		{
			roomsTextView.performClick ();
			roomsSeekBar.setProgress ( bookingController.getCountRooms () - 1 );
		}
	}

	private void addRoomsSelector ()
	{
		final View roomsView = inflater.inflate ( R.layout.choose_count_rooms_layout, null );
		roomsTextView = ( TextView ) roomsView.findViewById ( R.id.addonTextView );
		TextView titleTextView = ( TextView ) roomsView.findViewById ( R.id.titleTextView );
		final TextView countTextView = ( TextView ) roomsView.findViewById ( R.id.countTextView );
		final ImageView selectImageView = ( ImageView ) roomsView.findViewById ( R.id.selectImageView );
		final LinearLayout roomsSeekBarContainer = ( LinearLayout ) roomsView.findViewById ( R.id.roomsSeekBarContainer );
		roomsSeekBar = ( SeekBar ) roomsView.findViewById ( R.id.countSeekBar );

		titleTextView.setText ( getString ( R.string.bedrooms_count ) );
		roomsTextView.setText ( getString ( R.string.bedrooms ) );

		selectImageView.setVisibility ( View.GONE );

		int maxCount = 5;
		final int minCount = 1;

		roomsSeekBar.setMax ( maxCount - minCount );
		roomsSeekBar.setOnSeekBarChangeListener ( new SeekBar.OnSeekBarChangeListener ()
		{
			@Override
			public void onStopTrackingTouch ( SeekBar seekBar )
			{
				// TODO Auto-generated method stub
			}

			@Override
			public void onStartTrackingTouch ( SeekBar seekBar )
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged ( SeekBar seekBar, int progress, boolean fromUser )
			{
				int offset = minCount;
				countRooms = progress + offset;
				countTextView.setText ( countRooms + "" );
			}
		} );

		roomsTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				if ( selectImageView.getVisibility () == View.GONE )
				{
					roomsSeekBarContainer.setVisibility ( View.VISIBLE );
					selectImageView.setVisibility ( View.VISIBLE );
					isRoomsChecked = true;
				} else
				{
					roomsSeekBarContainer.setVisibility ( View.GONE );
					selectImageView.setVisibility ( View.GONE );
					isRoomsChecked = false;
				}
			}
		} );
		roomsSeekBar.setProgress ( minCount );
		specialAddonsContainer.addView ( roomsView );
	}

	private void addBathRoomsSelector ()
	{
		final View roomsView = inflater.inflate ( R.layout.choose_count_rooms_layout, null );
		bathroomsTextView = ( TextView ) roomsView.findViewById ( R.id.addonTextView );
		TextView titleTextView = ( TextView ) roomsView.findViewById ( R.id.titleTextView );
		final TextView countTextView = ( TextView ) roomsView.findViewById ( R.id.countTextView );
		final ImageView selectImageView = ( ImageView ) roomsView.findViewById ( R.id.selectImageView );
		final LinearLayout roomsSeekBarContainer = ( LinearLayout ) roomsView.findViewById ( R.id.roomsSeekBarContainer );
		bathRoomsSeekBar = ( SeekBar ) roomsView.findViewById ( R.id.countSeekBar );

		titleTextView.setText ( getString ( R.string.bathrooms_count ) );
		bathroomsTextView.setText ( getString ( R.string.bathrooms ) );

		selectImageView.setVisibility ( View.GONE );

		int maxCount = 5;
		final int minCount = 1;

		bathRoomsSeekBar.setMax ( maxCount - minCount );
		bathRoomsSeekBar.setOnSeekBarChangeListener ( new SeekBar.OnSeekBarChangeListener ()
		{
			@Override
			public void onStopTrackingTouch ( SeekBar seekBar )
			{
			}

			@Override
			public void onStartTrackingTouch ( SeekBar seekBar )
			{
			}

			@Override
			public void onProgressChanged ( SeekBar seekBar, int progress, boolean fromUser )
			{
				int offset = minCount;
				countBathRooms = progress + offset;
				countTextView.setText ( countBathRooms + "" );
			}
		} );

		bathroomsTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				if ( selectImageView.getVisibility () == View.GONE )
				{
					roomsSeekBarContainer.setVisibility ( View.VISIBLE );
					selectImageView.setVisibility ( View.VISIBLE );
					isBathRoomsChecked = true;
				} else
				{
					roomsSeekBarContainer.setVisibility ( View.GONE );
					selectImageView.setVisibility ( View.GONE );
					isBathRoomsChecked = false;
				}
			}
		} );
		bathRoomsSeekBar.setProgress ( minCount );
		specialAddonsContainer.addView ( roomsView );

	}

	private void addLawnMoving ()
	{
		final View lawnView = inflater.inflate ( R.layout.choose_lawn_moving_layout, null );
		lawnTextView = ( TextView ) lawnView.findViewById ( R.id.addonTextView );
		final ImageView selectImageView = ( ImageView ) lawnView.findViewById ( R.id.selectImageView );
		lawnContainer = ( LinearLayout ) lawnView.findViewById ( R.id.lawnContainer );
		selectImageView.setVisibility ( View.GONE );
		lawnTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				if ( selectImageView.getVisibility () == View.GONE )
				{
					lawnContainer.setVisibility ( View.VISIBLE );
					selectImageView.setVisibility ( View.VISIBLE );
				} else
				{
					lawnContainer.setVisibility ( View.GONE );
					selectImageView.setVisibility ( View.GONE );
					uncheckedAllLawnMoving ( 0 );
				}
			}
		} );

		for ( int i = 0; i < lawnContainer.getChildCount (); i++ )
		{
			final CheckBox view = ( CheckBox ) lawnContainer.getChildAt ( i );

			view.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View view )
				{
					CheckBox checkBoxView = ( CheckBox ) view;
					if ( checkBoxView.isChecked () )
					{
						checkBoxView.setTextColor ( getResources ().getColor ( R.color.black ) );
					} else
					{
						checkBoxView.setTextColor ( getResources ().getColor ( R.color.text_dark_grey ) );
					}
					uncheckedAllLawnMoving ( checkBoxView.getId () );
				}
			} );

		}

		specialAddonsContainer.addView ( lawnView );
	}

	private void uncheckedAllLawnMoving ( int filterId )
	{
		if ( lawnContainer != null )
		{
			for ( int i = 0; i < lawnContainer.getChildCount (); i++ )
			{
				CheckBox view = ( CheckBox ) lawnContainer.getChildAt ( i );
				if ( view.getId () != filterId )
				{
					view.setChecked ( false );
					view.setTextColor ( getResources ().getColor ( R.color.text_dark_grey ) );
				}
			}
		}
	}

	private void updateLawnMoving ()
	{
		if ( lawnContainer != null && bookingController.getLawnMovingType () != null )
		{
			lawnTextView.performClick ();
			switch ( bookingController.getLawnMovingType () )
			{
				case SMALL:
				{
					( ( CheckBox ) lawnContainer.findViewById ( R.id.lawmMovingSmall ) ).setChecked ( true );
					break;
				}
				case MEDIUM:
				{
					( ( CheckBox ) lawnContainer.findViewById ( R.id.lawmMovingMedium ) ).setChecked ( true );
					break;
				}
				case LARGE:
				{
					( ( CheckBox ) lawnContainer.findViewById ( R.id.lawmMovingLarge ) ).setChecked ( true );
					break;
				}
				case EXTRA_LARGE:
				{
					( ( CheckBox ) lawnContainer.findViewById ( R.id.lawmMovingExtraLarge ) ).setChecked ( true );
					break;
				}

			}
		}
	}

	private BookingController.LawnMovingType getSelectedLawnMoving ()
	{
		if ( lawnContainer != null )
		{
			for ( int i = 0; i < lawnContainer.getChildCount (); i++ )
			{
				CheckBox view = ( CheckBox ) lawnContainer.getChildAt ( i );
				if ( view.isChecked () )
				{
					switch ( view.getId () )
					{
						case R.id.lawmMovingSmall:
							return BookingController.LawnMovingType.SMALL;
						case R.id.lawmMovingMedium:
							return BookingController.LawnMovingType.MEDIUM;
						case R.id.lawmMovingLarge:
							return BookingController.LawnMovingType.LARGE;
						case R.id.lawmMovingExtraLarge:
							return BookingController.LawnMovingType.EXTRA_LARGE;
					}
				}
			}
		}
		return null;
	}

	protected void addonSelected ( AddonServiceAPIObject addon )
	{
        if ( selectedAddons.contains ( addon ) )
        {
            selectedAddons.remove ( addon );
        } else
        {
            selectedAddons.add ( addon );
        }
        if(bookingController.isMasseur())
        {
            removeAllAddonExceptId( addon.getInt ( AddonServiceAPIParams.JOB_ADDONS_ID ));
        } else
        {
            //Pool BOY
            if ( addon.getString ( AddonServiceAPIParams.JOB_ADDONS_ID ).equals ( "" + AddonId.BOARD_SHORTS_2 ) )
            {
                removeAddonById(AddonId.TRUNKS_2);
                removeAddonById(AddonId.SPEEDO_2);
            }

            if ( addon.getString ( AddonServiceAPIParams.JOB_ADDONS_ID ).equals ( "" + AddonId.TRUNKS_2 ) )
            {
                removeAddonById(AddonId.BOARD_SHORTS_2);
                removeAddonById(AddonId.SPEEDO_2);
            }
            if ( addon.getString ( AddonServiceAPIParams.JOB_ADDONS_ID ).equals ( "" + AddonId.SPEEDO_2 ) )
            {
                removeAddonById(AddonId.TRUNKS_2);
                removeAddonById(AddonId.BOARD_SHORTS_2);
            }

            //GoGo Boy

            //int BOXERS = ATTIRE_2 + 1;									// 26
            //int BRIEFS = BOXERS + 1;                                    // 27
            // int JOCKSTRAP = BRIEFS + 1;                                 // 28
            if ( addon.getString ( AddonServiceAPIParams.JOB_ADDONS_ID ).equals ( "" + AddonId.BOXERS ) )
            {
                removeAddonById(AddonId.BRIEFS);
                removeAddonById(AddonId.JOCKSTRAP);
            }

            if ( addon.getString ( AddonServiceAPIParams.JOB_ADDONS_ID ).equals ( "" + AddonId.BRIEFS ) )
            {
                removeAddonById(AddonId.BOXERS);
                removeAddonById(AddonId.JOCKSTRAP);
            }
            if ( addon.getString ( AddonServiceAPIParams.JOB_ADDONS_ID ).equals ( "" + AddonId.JOCKSTRAP ) )
            {
                removeAddonById(AddonId.BRIEFS);
                removeAddonById(AddonId.BOXERS);
            }

            //Car Wash
            //int BOARD_SHORTS_1 = BATHING_SUIT_1 + 1;					// 11
            // int TRUNKS_1 = BOARD_SHORTS_1 + 1;							// 12
            //int SPEEDO_1 = TRUNKS_1 + 1;								// 13

            if ( addon.getString ( AddonServiceAPIParams.JOB_ADDONS_ID ).equals ( "" + AddonId.BOARD_SHORTS_1 ) )
            {
                removeAddonById(AddonId.TRUNKS_1);
                removeAddonById(AddonId.SPEEDO_1);
            }

            if ( addon.getString ( AddonServiceAPIParams.JOB_ADDONS_ID ).equals ( "" + AddonId.TRUNKS_1 ) )
            {
                removeAddonById(AddonId.BOARD_SHORTS_1);
                removeAddonById(AddonId.SPEEDO_1);
            }
            if ( addon.getString ( AddonServiceAPIParams.JOB_ADDONS_ID ).equals ( "" + AddonId.SPEEDO_1 ) )
            {
                removeAddonById(AddonId.TRUNKS_1);
                removeAddonById(AddonId.BOARD_SHORTS_1);
            }
        }
	}

    protected void removeAddonById ( int id )
    {
        for(int i = 0; i < selectedAddons.size(); i++){
            if(selectedAddons.get(i).getInt ( AddonServiceAPIParams.JOB_ADDONS_ID ) ==  id ){
                selectedAddons.remove(i);
            }
        }
    }

    protected void removeAllAddonExceptId ( int id )
    {
        for(int i = 0; i < selectedAddons.size(); i++){
            if(selectedAddons.get(i).getInt ( AddonServiceAPIParams.JOB_ADDONS_ID ) !=  id ){
                selectedAddons.remove(i);
            }
        }
    }

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		bookingController = controller.getBookingController ();
		inflater = LayoutInflater.from ( controller );
		LayoutTransition layoutTransition = specialAddonsContainer.getLayoutTransition ();
		layoutTransition.enableTransitionType ( LayoutTransition.CHANGING );
	}

	private void addListeners ()
	{
		saveButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				bookingController.setAddons ( selectedAddons );
				bookingController.setSpecialRequest ( specialReqeustEditText.getText ().toString ().trim () );
				bookingController.setLawnMovingType ( getSelectedLawnMoving () );
				if ( isBathRoomsChecked )
				{
					bookingController.setCountBathRooms ( bathRoomsSeekBar.getProgress () + 1 );
				} else
				{
					bookingController.setCountBathRooms ( 0 );
				}
				if ( isRoomsChecked )
				{
					bookingController.setCountRooms ( roomsSeekBar.getProgress () + 1 );
				} else
				{
					bookingController.setCountRooms ( 0 );
				}

				controller.onBackPressed ();
			}
		} );
		backImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.onBackPressed ();
			}
		} );
		backTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.onBackPressed ();
			}
		} );

		specialReqeustContainer.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
				if ( specialReqeustEditText.getVisibility () == View.GONE )
				{
					specialReqeustEditText.setVisibility ( View.VISIBLE );
				} else
				{
					specialReqeustEditText.setVisibility ( View.GONE );
				}
			}
		} );
	}
}