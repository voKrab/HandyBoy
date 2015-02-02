package com.vallverk.handyboy;

import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.vallverk.handyboy.SettingsManager.Params;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.gcm.NotificationBroadcastReceiver;
import com.vallverk.handyboy.gcm.PushNotification;
import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.ChatManager;
import com.vallverk.handyboy.model.CommunicationManager;
import com.vallverk.handyboy.model.MyLocationManager;
import com.vallverk.handyboy.model.UserRole;
import com.vallverk.handyboy.model.UserStatus;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.BookingStatusAPIObject;
import com.vallverk.handyboy.model.api.BookingStatusAPIObject.BookingStateEnum;
import com.vallverk.handyboy.model.api.DiscountAPIObject;
import com.vallverk.handyboy.model.api.JobAddonsAPIObject;
import com.vallverk.handyboy.model.api.JobAddonsAPIObject.JobAddonsAPIParams;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.model.job.JobCategory;
import com.vallverk.handyboy.model.job.JobTypeManager;
import com.vallverk.handyboy.model.job.TypeJob;
import com.vallverk.handyboy.pubnub.PubnubManager;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.NavigationDrawerFragment;
import com.vallverk.handyboy.view.NavigationDrawerFragment.NavigationDrawerCallbacks;
import com.vallverk.handyboy.view.base.DialogFactory;
import com.vallverk.handyboy.view.base.TabBarView;
import com.vallverk.handyboy.view.controller.BookingController;
import com.vallverk.handyboy.view.controller.RegistrationController;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Oleg Barkov
 *         <p/>
 *         The class MainActivity. The main controller of application
 */
public class MainActivity extends FragmentActivity implements NavigationDrawerCallbacks
{
	private GoogleCloudMessaging gcm;
	private String regid;

	public static final String SENDER_ID = "673570231007";

	public enum ApplicationAction
	{
		USER, TABBAR, SEARCH, CUSTOMIZE_SCHEDULE_DATE, GCM_NOTIFICATION, NEW_MESSAGE, AMOUNT_UNREAD_MESSAGES, AMOUNT_UNREAD_BOOKING;
	}

	public enum ApplicationState
	{
		RESUME, PAUSE, CLOSE
	}

	private void Test ()
	{

	}

	/**
	 * Constants.
	 */
	public static int MAX_POSTED_IMAGE_WIDTh = 1080; // Full HD
	public static int MAX_POSTED_IMAGE_HEIGHT = 1920; // Full HD
	public static float MAX_POSTED_IMAGE_SIZE = 0.2f; // �������� 0.2 mb
	public static float MAX_AVATAR_SIZE = 0.25f;
	public static int MAX_AVATAR_WIDTH_AND_HEIGHT = 200;
	public static int MAX_GOODS_PHOTO_WIDTH_AND_HEIGHT = 800;
	public static int MAX_SHOP_BACKGROUND_SIZE = 400;
	public static final int CHAT_ATTACH_SIZE = 100;
	public static final int NOTIFICATIONS_MAX_COUNT = 100;
	public static final float MAX_VIDEO_SIZE = 10485760; // bytes
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	public static final int MAX_SERVICE_PRICE = 1000;
	public static final int DEFAULT_SERVICE_PRICE = 50;
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static ApplicationState applicationState;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * instance of singleton.
	 */
	private static MainActivity instance;

	/**
	 * Return instance.
	 */
	public static MainActivity getInstance ()
	{
		return instance;
	}

	/**
	 * Communication with server api tools.
	 */
	private APIManager apiManager;
	/**
	 * Controller for all possibles view states.
	 */
	private ViewStateController viewStateController;
	/**
	 * Dialog using for showing loader animation.
	 */
	private Dialog dialog;
	/**
	 * Using for communication between fragments.
	 */
	public HashMap < String, Object > communicationMap;
	/**
	 * facilitates querying channels for messages and listening on channels for
	 * presence/message events
	 */
	private PubnubManager pubnub;
	/**
	 * Controls the registration process of service or customer
	 */
	private RegistrationController registrationController;
	/**
	 * Manager of all exists job types
	 */
	private JobTypeManager jobTypeManager;
	/**
	 * Using for communication between fragments.
	 */
	private CommunicationManager communicationManager;
	/**
	 * Controls of all data/events related with chat.
	 */
	private ChatManager chatManager;
	/**
	 * Tabbar controller.
	 */
	private TabBarView tabBarView;
	/**
	 * is debug mode.
	 */
	private boolean debug;

	private DrawerLayout drawerLayout;
	private List < JobAddonsAPIObject > addons;
	private BookingController bookingController;
	private BookingDataManager bookingDataManager;
	private BookingStatusAPIObject bookingStatusAPIObject;
	private NotificationBroadcastReceiver notificationBroadcastReceiver;

	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment
	{
		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment ()
		{
			super ();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog ( Dialog dialog )
		{
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog ( Bundle savedInstanceState )
		{
			return mDialog;
		}
	}

	/**
	 * Init most application controllers.
	 */
	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate ( savedInstanceState );

		setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
		requestWindowFeature ( Window.FEATURE_NO_TITLE );
		setContentView ( R.layout.activity_main );

		instance = this;
		viewStateController = new ViewStateController ( instance );
		communicationMap = new HashMap < String, Object > ();
		// locationClient = new LocationClient ( this, this, this );
		// new PubnubManager ( PubnubManager.PUBLISH_KEY,
		// PubnubManager.SUBSCRIBE_KEY, PubnubManager.SECRET_KEY );
		pubnub = PubnubManager.getInstance (); // init pubnub
		apiManager = APIManager.getInstance ();
		bookingDataManager = BookingDataManager.getInstance ();
		jobTypeManager = JobTypeManager.getInstance ();
		communicationManager = CommunicationManager.getInstance ();
		chatManager = ChatManager.getInstance ();
		tabBarView = ( TabBarView ) findViewById ( R.id.tabBarView );
		communicationManager.addListener ( ApplicationAction.TABBAR, new Handler ()
		{
			@Override
			public void dispatchMessage ( Message message )
			{
				VIEW_STATE newState = ( VIEW_STATE ) message.obj;
				setState ( newState );
			}
		} );
		hideTabBar ();

		registerInBackground ();

		MyLocationManager.setUpLocationListener ( this );
		MyLocationManager.setOnLocationChangeListener ( new MyLocationManager.OnLocationChangeListener ()
		{
			@Override
			public void onLoacationChange ( final Location location )
			{
				new AsyncTask < Void, Void, String > ()
				{
					@Override
					protected String doInBackground ( Void... params )
					{
						String status = "";
						JSONObject postParams = new JSONObject ();
						try
						{
							postParams.accumulate ( "latitude", location.getLatitude () );
							postParams.accumulate ( "longitude", location.getLongitude () );
							postParams.accumulate ( "userId", apiManager.getUser ().getId () );
							JSONObject request = new JSONObject ( ServerManager.postRequest ( ServerManager.SAVE_USER_LOCATION, postParams.toString () ) );
							return request.getString ( "parameters" );
						} catch ( Exception e )
						{
							e.printStackTrace ();
							return e.getMessage ();
						}
					}

					@Override
					protected void onPostExecute ( String status )
					{
						if ( !status.isEmpty () )
						{
							Toast.makeText ( MainActivity.this, status, Toast.LENGTH_SHORT ).show ();
						}
					}
				}.execute ( null, null, null );
			}
		} );

		CommunicationManager.getInstance ().addListener ( ApplicationAction.USER, new Handler ()
		{
			@Override
			public void dispatchMessage ( Message message )
			{
				UserAPIObject user = ( UserAPIObject ) message.obj;
				if ( user == null )
				{
					pubnub.unSubscribe ();
				} else
				{
					try
					{
						pubnub.subscribe ( user.getId () );
					} catch ( Exception e )
					{
						// TODO Auto-generated catch block
						e.printStackTrace ();
					}
				}
			}
		} );

		Display display = getWindowManager ().getDefaultDisplay ();
		int width = display.getWidth (); // deprecated
		int height = display.getHeight (); // deprecated
		SCREEN_WIDTH = width;
		SCREEN_HEIGHT = height;

		mNavigationDrawerFragment = ( NavigationDrawerFragment ) getSupportFragmentManager ().findFragmentById ( R.id.navigation_drawer );
		mNavigationDrawerFragment.getView ().getLayoutParams ().width = ( int ) ( SCREEN_WIDTH * 0.85 );

		// Set up the drawer.
		drawerLayout = ( DrawerLayout ) findViewById ( R.id.drawer_layout );
		mNavigationDrawerFragment.setUp ( R.id.navigation_drawer, drawerLayout );

		ImageLoader.getInstance ().init ( ImageLoaderConfiguration.createDefault ( this ) );
		setState ( VIEW_STATE.SPLASH );
	}

	private boolean checkDataFromNotification ()
	{
		try
		{
			Log.d ( "Log", "onCreate" );
			String dataFromNotificationString = SettingsManager.getString ( Params.DATA_FROM_NOTIFICATION, "", this );
			if ( dataFromNotificationString.isEmpty () )
			{
				return false;
			} else
			{
				SettingsManager.setString ( Params.DATA_FROM_NOTIFICATION, "", this );
				updateComponents ();
				JSONObject dataFromNotification = new JSONObject ( dataFromNotificationString );
				PubnubManager.ActionType actionType = PubnubManager.ActionType.fromString ( dataFromNotification.getString ( "actionType" ) );
				Log.d ( "Log", "MainActivity:onCreate: actionType" + actionType );
				switch ( actionType )
				{
					case BOOKING_ADD_CHARGES_ACCEPTED:
					case BOOKING_STATUS:
					case BOOKING:
					{
						tabBarView.manualSelection ( VIEW_STATE.GIGS );
						break;
					}
					case CHAT:
					{
						setState ( VIEW_STATE.CHATS );
						break;
					}
					case EXTRA_MONEY:
					case REMINDER:
					{
						setState ( VIEW_STATE.GIGS );
						break;
					}
					case AVAILABLE_NOW:
					{
						setState ( VIEW_STATE.DASHBOARD );
						break;
					}
					case BOOKING_ADD_CHARGES_REQUESTED:
					{
						bookingDataManager.setActiveDataIndex ( dataFromNotification.getString ( "bookingId" ) );
						setState ( VIEW_STATE.CHARGES );
						break;
					}
					default:
					{
						setState ( VIEW_STATE.DASHBOARD );
					}
				}
				return true;
			}
		} catch ( Exception ex )
		{
			ex.printStackTrace ();
			return false;
		}
	}

	@Override
	protected void onStart ()
	{
		super.onStart ();
	}

	public void onResume ()
	{
		Log.d ( "Chat", "onResume" );
		super.onResume ();
		viewStateController.updateTabBar ();
		try
		{
			tabBarView.updateMessagesCount ();
		} catch ( Exception ex )
		{
			// ignore;
		}
		notificationBroadcastReceiver = new NotificationBroadcastReceiver ();
		registerReceiver ( notificationBroadcastReceiver, new IntentFilter ( ApplicationAction.GCM_NOTIFICATION.toString () ) );
		if ( viewStateController.getState () == VIEW_STATE.WAITING_FOR_VALIDATION )
		{
			setState ( VIEW_STATE.SPLASH );
		}
		setApplicationState ( ApplicationState.RESUME );
	}

	@Override
	protected void onPause ()
	{
		unregisterReceiver ( notificationBroadcastReceiver );
		super.onPause ();
		setApplicationState ( ApplicationState.PAUSE );
	}

	/*
	 * Called when the Activity is no longer visible.
	 */
	@Override
	protected void onStop ()
	{
		save ();
		// Log.d ( "Log", "onStop" );
		super.onStop ();
	}

	@Override
	protected void onDestroy ()
	{
		// Log.d ( "Log", "onDestroy" );
		super.onDestroy ();
	}

	/**
	 * Hardware back button pressed.
	 */
	@Override
	public void onBackPressed ()
	{
		viewStateController.onBackPressed ();
	}

	public void hideTabBar ()
	{
		tabBarView.setVisibility ( View.GONE );
	}

	public void showTabBar ()
	{
		tabBarView.setVisibility ( View.VISIBLE );
	}

	private void registerInBackground ()
	{
		new AsyncTask < Void, Void, String > ()
		{
			@Override
			protected String doInBackground ( Void... params )
			{
				String msg = "";
				try
				{
					if ( gcm == null )
					{
						gcm = GoogleCloudMessaging.getInstance ( instance );
					}
					regid = gcm.register ( SENDER_ID );
					msg = "Device registered, registration ID=" + regid;
					Log.d ( "GCM", msg );

				} catch ( IOException ex )
				{
					msg = "Error :" + ex.getMessage ();
				}
				return msg;
			}

			@Override
			protected void onPostExecute ( String msg )
			{
			}
		}.execute ( null, null, null );
	}

	@Override
	public void onNavigationDrawerItemSelected ( int position )
	{
		FragmentManager fragmentManager = getSupportFragmentManager ();
	}

	public void hideKeyboard ()
	{

		try
		{
			InputMethodManager inputManager = ( InputMethodManager ) getSystemService ( Context.INPUT_METHOD_SERVICE );

			// check if no view has focus:
			View v = getCurrentFocus ();
			if ( v == null )
				return;

			inputManager.hideSoftInputFromWindow ( v.getWindowToken (), InputMethodManager.HIDE_NOT_ALWAYS );
		} catch ( Exception ex )
		{
			ex.printStackTrace ();
		}

	}

	/**
	 * Change view state using viewStateController.
	 */
	public void setState ( VIEW_STATE newState )
	{
		if ( newState == VIEW_STATE.FEED || newState == VIEW_STATE.CHATS || newState == VIEW_STATE.DASHBOARD || newState == VIEW_STATE.CHOOSE_CATEGORY || newState == VIEW_STATE.CALENDAR || newState == VIEW_STATE.FAVORITES )
		{
			tabBarView.updateSelection ( newState );
		}
		viewStateController.setState ( newState );
	}

	public boolean isBookingState ()
	{
		return bookingController != null;
	}

	public void showLoader ()
	{
		hideLoader ();
		dialog = DialogFactory.show ( getString ( R.string.loading ), this );
	}

	public void hideLoader ()
	{
		if ( dialog != null )
		{
			dialog.dismiss ();
			dialog = null;
		}
	}

	public void setSwipeEnabled ( boolean isEnabled )
	{
		mNavigationDrawerFragment.setSwipeEnabled ( isEnabled );
	}

	public Object getCommunicationValue ( String key )
	{
		Object value = communicationMap.remove ( key );
		return value;
	}

	public void setCommunicationValue ( String key, Object value )
	{
		communicationMap.put ( key, value );
	}

	private void save ()
	{
		apiManager.save ();
		jobTypeManager.save ();
		chatManager.save ();
		bookingDataManager.save ();
	}

	public void enter ()
	{
		enter ( true );
	}

	/**
	 * try enter in main side of application
	 */
	public void enter ( final boolean isNewUser )
	{
		updateComponents ();
		new AsyncTask < Void, Void, Boolean > ()
		{
			public void onPreExecute ()
			{
				super.onPreExecute ();
				showLoader ();
			}

			public void onPostExecute ( Boolean result )
			{
				super.onPostExecute ( result );
				hideLoader ();

				if ( result )
				{
					apiManager.save (); // под вопросом
					UserAPIObject user = apiManager.getUser ();
					String status = user.getString ( UserParams.STATUS );
					String role = user.getString ( UserParams.ROLE );
					if ( status.equals ( UserStatus.NEW.toString () ) )
					{
						getRegistrationController ().nextStep ();
					} else
					{
						if ( bookingStatusAPIObject == null || bookingStatusAPIObject.getState () == BookingStateEnum.NORMAL )
						{
							if ( role == null || role.equals ( UserRole.GUEST.toString () ) )
							{
								setState ( VIEW_STATE.WAITING_FOR_VALIDATION );
							} else if ( role != null && role.equals ( UserRole.ACCEPTED.toString () ) )
							{
								successEnter ( isNewUser );
							}
						} else
						{
							if ( bookingDataManager.getInstance ().isIService () )
							{
								setState ( VIEW_STATE.SERVICE_REVIEW );
							} else
							{
								if ( bookingDataManager.getActiveBookingStatus () == BookingStatusEnum.WAITING_FOR_REVIEW )
								{
									setState ( VIEW_STATE.SERVICED );
								} else
								{
									setState ( VIEW_STATE.SERVICE_REVIEW );
								}
							}
						}
					}
				} else
				{
					Toast.makeText ( MainActivity.this, R.string.internet_connection_exception, Toast.LENGTH_LONG ).show ();
					setState ( VIEW_STATE.SPLASH );
				}
			}

			@Override
			protected Boolean doInBackground ( Void... params )
			{
				try
				{
					String responceString = ServerManager.getJobTypes ();
					jobTypeManager.update ( responceString );
					jobTypeManager.save ();
					loadFromServerJobAddons ();
					bookingDataManager.update ();
					bookingStatusAPIObject = apiManager.getBooingStatus ();
					PushNotification.updateGcmID ();
				} catch ( Exception ex )
				{
					ex.printStackTrace ();
					return jobTypeManager.isValid ();
				}
				return true;
			}
		}.execute ();
	}

	protected void loadFromServerJobAddons () throws Exception
	{
		addons = apiManager.loadList ( ServerManager.ADDONS_GET, JobAddonsAPIObject.class );
	}

	public void logout ()
	{
		new AsyncTask < Void, Void, String > ()
		{
			public void onPreExecute ()
			{
				super.onPreExecute ();
				showLoader ();
			}

			public void onPostExecute ( String result )
			{
				super.onPostExecute ( result );
				hideLoader ();
				if ( result.isEmpty () )
				{
					SettingsManager.setBoolean ( Params.IS_LOGIN, false, instance );
					setState ( VIEW_STATE.LOGIN );
				} else
				{
					Toast.makeText ( instance, result, Toast.LENGTH_LONG ).show ();
				}
			}

			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					pubnub.unSubscribe ();
					PushNotification.unSubscribe ();
					apiManager.logout ();
					viewStateController.clearBackStateStack ();
				} catch ( Exception ex )
				{
					result = ex.getMessage ();
					ex.printStackTrace ();
				}
				return result;
			}
		}.execute ();
	}

	/**
	 * Success enter in main side of application
	 */
	protected void successEnter ( boolean isNewUser )
	{
		SettingsManager.setBoolean ( Params.IS_LOGIN, true, instance );
		if ( isNewUser )
		{
			setupDefaultSettings ();
		}
		try
		{
			pubnub.subscribe ( apiManager.getUser ().getId () );
		} catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
		if ( checkDataFromNotification () )
		{
			return;
		}
		MainActivity.getInstance ().setState ( isService () ? VIEW_STATE.DASHBOARD : VIEW_STATE.CHOOSE_CATEGORY );
	}

	private void setupDefaultSettings ()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences ( this );
		Editor editor = prefs.edit ();
		editor.putBoolean ( PushNotification.class.getSimpleName (), true );
		editor.commit ();
	}

	private void updateComponents ()
	{
		mNavigationDrawerFragment.updateViews ();
		tabBarView.init ( this );
		setSwipeEnabled ( true );
	}

	public RegistrationController getRegistrationController ()
	{
		if ( registrationController == null )
		{
			registrationController = new RegistrationController ();
		}
		return registrationController;
	}

	public boolean isService ()
	{
		return apiManager.getUser ().isService ();
	}

	public List < TypeJob > getJobTypes ( JobCategory category )
	{
		return jobTypeManager.getJobTypes ( category );
	}

	public APIManager getAPIManager ()
	{
		return apiManager;
	}

	public boolean isMenuOpen ()
	{
		if ( drawerLayout.isDrawerOpen ( GravityCompat.START ) )
		{
			return true;
		} else
		{
			return false;
		}
	}

	public void openMenu ()
	{
		mNavigationDrawerFragment.show ();
	}

	public void closeMenu ()
	{
		mNavigationDrawerFragment.close ();
	}

	public String getUserId ()
	{
		return apiManager.getUserId ();
	}

	public boolean isDebugMode ()
	{
		return debug;
	}

	public void setDebugMode ( boolean debug )
	{
		this.debug = debug;
	}

	public JobAddonsAPIObject getAddon ( TypeJob typejob, int parentId )
	{
		for ( JobAddonsAPIObject addon : addons )
		{
			if ( addon.getInt ( JobAddonsAPIParams.TYPE_JOB_ID ) == typejob.getId () && addon.getInt ( JobAddonsAPIParams.PARENT ) == parentId )
			{
				return addon;
			}
		}
		return null;
	}

	public JobAddonsAPIObject getAddon ( int addonId )
	{
		for ( JobAddonsAPIObject addon : addons )
		{
			if ( addon.getId ().equals ( "" + addonId ) )
			{
				return addon;
			}
		}
		return null;
	}

	public JobAddonsAPIObject getAddon ( String messageType, String typeJobId )
	{
		for ( JobAddonsAPIObject addon : addons )
		{
			if ( addon.getString ( JobAddonsAPIParams.TYPE_JOB_ID ).equals ( typeJobId ) && addon.getString ( JobAddonsAPIParams.NAME ).equals ( messageType ) )
			{
				return addon;
			}
		}
		return null;
	}

	public JobAddonsAPIObject getAddon ( String addonId )
	{
		return getAddon ( Integer.parseInt ( addonId ) );
	}

	public String getAddonName ( String addonId )
	{
		return getAddon ( addonId ).getString ( JobAddonsAPIParams.NAME );
	}

	public void startBooking ( UserAPIObject handyBoy, TypeJobServiceAPIObject job, DiscountAPIObject discountAPIObject )
	{
		bookingController = new BookingController ( handyBoy, job, apiManager.getUser (), discountAPIObject, this );
		bookingController.setState ( VIEW_STATE.BOOKING );
	}

	public void cancelBooking ()
	{
		setCommunicationValue ( UserAPIObject.class.getSimpleName (), bookingController.getHandyBoy () );
		bookingController = null;
		setState ( VIEW_STATE.HANDY_BOY_PAGE );
	}

	public void finishBooking ()
	{
		bookingController = null;
	}

	public BookingController getBookingController ()
	{
		return bookingController;
	}

	@Override
	protected void onSaveInstanceState ( Bundle outState ) // Bug on API Level >
	// 11
	{
		// outState.putString ( "WORKAROUND_FOR_BUG_19917_KEY",
		// "WORKAROUND_FOR_BUG_19917_VALUE" );
		// super.onSaveInstanceState ( outState );
	}

	private void setApplicationState ( ApplicationState newState )
	{
		if ( applicationState != newState )
		{
			applicationState = newState;
			SettingsManager.setString ( Params.APPLICATION_STATE, newState.toString (), instance );
		}
	}
}
