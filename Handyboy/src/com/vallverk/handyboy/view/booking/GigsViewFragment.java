package com.vallverk.handyboy.view.booking;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vallverk.handyboy.MainActivity.ApplicationAction;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.CommunicationManager;
import com.vallverk.handyboy.model.CommunicationManager.CommunicationAction;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.BookingAPIObject;
import com.vallverk.handyboy.model.api.BookingAPIObject.BookingAPIParams;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.BookingDataManager.BookingFilter;
import com.vallverk.handyboy.model.api.BookingDataObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.BaseListFragment;
import com.vallverk.handyboy.view.base.BaseListFragment.Refresher;

import java.util.List;

public class GigsViewFragment extends BaseFragment
{
	private View menuImageView;
	private APIManager apiManager;
	private UserAPIObject user;
	private BaseListFragment listFragment;

	private View currentGigLayout;
	private View pastGigLayout;

	private BookingDataManager bookingDataManager;
	private View noBookingLayout;
	private View mainContainer;

	private List data;

	private boolean isShowLoader = true;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.gigs_layout, container, false );
			menuImageView = view.findViewById ( R.id.menuImageView );
			currentGigLayout = view.findViewById ( R.id.currentGigLayout );
			pastGigLayout = view.findViewById ( R.id.pastGigLayout );

			mainContainer = view.findViewById ( R.id.mainContainer );
			noBookingLayout = view.findViewById ( R.id.noBookingLayout );

		} else
		{
			( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}

	private Handler onRefreshHandler = new Handler ( new Callback ()
	{
		@Override
		public boolean handleMessage ( Message msg )
		{
			if ( data.isEmpty () )
			{
				if ( !pastGigLayout.isActivated () )
				{
					mainContainer.setVisibility ( View.GONE );
					noBookingLayout.setVisibility ( View.VISIBLE );
				} else
				{
					mainContainer.setVisibility ( View.VISIBLE );
					noBookingLayout.setVisibility ( View.GONE );
				}
			} else
			{
				mainContainer.setVisibility ( View.VISIBLE );
				noBookingLayout.setVisibility ( View.GONE );
			}

			return false;
		}
	} );

	@Override
	protected void init ()
	{
		addListeners ();
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		apiManager = APIManager.getInstance ();
		user = apiManager.getUser ();
		bookingDataManager = BookingDataManager.getInstance ();
		if ( listFragment == null )
		{
			listFragment = ( BaseListFragment ) getActivity ().getSupportFragmentManager ().findFragmentById ( R.id.baseListViewFragment );
			listFragment.setRefresher ( new Refresher ()
			{
				@Override
				public List < Object > refresh () throws Exception
				{
					if ( isShowLoader )
					{
						controller.runOnUiThread ( new Runnable ()
						{
							@Override
							public void run ()
							{
								controller.showLoader ();
							}
						} );
						isShowLoader = false;
					}

					bookingDataManager.update ();
					data = bookingDataManager.getData ();
					if ( user.isCustomer () )
					{
						onRefreshHandler.sendEmptyMessage ( 0 );
					}
					controller.runOnUiThread ( new Runnable ()
					{
						@Override
						public void run ()
						{
							controller.hideLoader ();
						}
					} );
					return data;
				}
			} );
			CommunicationManager.getInstance ().addListener ( ApplicationAction.SEARCH, new Handler ()
			{
				@Override
				public void dispatchMessage ( Message message )
				{
					listFragment.refreshData ();
				}
			} );
			listFragment.setAdapter ( new GigListAdapter ( controller ) );
		}

	}

	private class GigListAdapter extends ArrayAdapter < Object >
	{
		private DisplayImageOptions avatarLoadOptions = new DisplayImageOptions.Builder ().showImageOnFail ( R.drawable.avatar ).showImageForEmptyUri ( R.drawable.avatar ).cacheInMemory ( true ).cacheOnDisc ().build ();
		private LayoutInflater inflater;

		public GigListAdapter ( Context context )
		{
			super ( context, 0 );
			inflater = LayoutInflater.from ( context );
		}

		public void notifyDataSetChanged ()
		{
			super.notifyDataSetChanged ();
		}

		public class ViewHolder
		{
			ImageView myAvatarImage;
			ImageView handyBoyAvatarImage;
			TextView gigTitleTextView;
			TextView gigJobNameTextView;
			TextView gigDateTextView;
			TextView gigIdTextView;
			TextView hourTextView;
			TextView gigStatusTextView;
			TextView hoursTypeTextView;
		}

		@Override
		public View getView ( int position, View view, ViewGroup parent )
		{
			final ViewHolder viewHolder;
			final BookingDataObject bookingDataObject = ( BookingDataObject ) getItem ( position );
			final int selectedPosition = position;
			if ( view == null )
			{
				view = inflater.inflate ( R.layout.gig_list_item_layout, parent, false );
				viewHolder = new ViewHolder ();
				viewHolder.myAvatarImage = ( ImageView ) view.findViewById ( R.id.myAvatarImage );
				viewHolder.handyBoyAvatarImage = ( ImageView ) view.findViewById ( R.id.handyBoyAvatarImage );
				viewHolder.gigTitleTextView = ( TextView ) view.findViewById ( R.id.gigTitleTextView );
				viewHolder.gigJobNameTextView = ( TextView ) view.findViewById ( R.id.gigJobNameTextView );
				viewHolder.gigDateTextView = ( TextView ) view.findViewById ( R.id.gigDateTextView );
				viewHolder.gigIdTextView = ( TextView ) view.findViewById ( R.id.gigIdTextView );
				viewHolder.hourTextView = ( TextView ) view.findViewById ( R.id.hourTextView );
				viewHolder.gigStatusTextView = ( TextView ) view.findViewById ( R.id.gigStatusTextView );
				viewHolder.hoursTypeTextView = ( TextView ) view.findViewById ( R.id.hoursTypeTextView );
				view.setTag ( viewHolder );
			} else
			{
				viewHolder = ( ViewHolder ) view.getTag ();
			}

			GigViewOnClickListener listener = new GigViewOnClickListener ( controller, bookingDataManager, selectedPosition );
			view.setOnClickListener ( listener );

			ImageLoader.getInstance ().displayImage ( user.getString ( UserParams.AVATAR ), viewHolder.myAvatarImage, avatarLoadOptions );

			bookingDataManager.setActiveDataIndex ( selectedPosition );
			if ( !bookingDataManager.isIService () )
			{
				ImageLoader.getInstance ().displayImage ( bookingDataObject.getService ().getString ( UserParams.AVATAR ), viewHolder.handyBoyAvatarImage, avatarLoadOptions );
				viewHolder.gigTitleTextView.setText ( "YOU + " + bookingDataObject.getService ().getShortName ().toUpperCase () );
			} else
			{
				ImageLoader.getInstance ().displayImage ( bookingDataObject.getCustomer ().getString ( UserParams.AVATAR ), viewHolder.handyBoyAvatarImage, avatarLoadOptions );
				viewHolder.gigTitleTextView.setText ( "YOU + " + bookingDataObject.getCustomer ().getShortName ().toUpperCase () );
			}

			viewHolder.gigJobNameTextView.setText ( bookingDataObject.getTypeJobAPIObject ().getName () + " Session" );
			viewHolder.gigDateTextView.setText ( Tools.toDateString ( bookingDataObject.getBookingAPIObject ().getString ( BookingAPIParams.DATE ), "MMMM dd, yyyy" ) );
			viewHolder.gigIdTextView.setText ( "GIG#" + bookingDataObject.getBookingAPIObject ().getId ().toString () );
			String hours = bookingDataObject.getBookingAPIObject ().getTextTotalHours ();

			String sHours = getHours ( hours );
			viewHolder.hourTextView.setText ( sHours );

			if ( "1".equals ( sHours ) || "0.5".equals ( sHours ) )
			{
				viewHolder.hoursTypeTextView.setText ( "HR" );
			} else
			{
				viewHolder.hoursTypeTextView.setText ( "Hrs" );
			}
			BookingStatusEnum status = bookingDataObject.getSatus ();
			updateStatusComponents ( viewHolder, status );
			CommunicationManager.getInstance ().addListener ( CommunicationAction.BOOKING_STATUS, new Handler ()
			{
				public void dispatchMessage ( Message message )
				{
					BookingAPIObject booking = ( BookingAPIObject ) message.obj;
					if ( bookingDataObject.getId ().equals ( booking.getId () ) )
					{
						updateStatusComponents ( viewHolder, booking.getStatus () );
					}
				}
			} );
			return view;
		}

		private void updateStatusComponents ( ViewHolder viewHolder, BookingStatusEnum status )
		{
			viewHolder.gigStatusTextView.setText ( status.toString () );
			viewHolder.gigStatusTextView.setBackgroundColor ( BookingAPIObject.getStatusColor ( status ) );
		}
	}

	public String getHours ( String hours )
	{
		do
		{
			hours = hours.substring ( 0, hours.length () - 1 );
		} while ( hours.length () > 0 && hours.charAt ( hours.length () - 1 ) == '0' );

		if ( hours.length () > 0 && hours.charAt ( hours.length () - 1 ) == '.' )
		{
			hours = hours.substring ( 0, hours.length () - 1 );
		}
		return hours;
	}

	private OnClickListener onGigTypeClickListener = new OnClickListener ()
	{

		@Override
		public void onClick ( View view )
		{
			switch ( view.getId () )
			{
				case R.id.currentGigLayout:
					currentGigLayout.setActivated ( true );
					pastGigLayout.setActivated ( false );
					bookingDataManager.setBookingFilter ( BookingFilter.CURRENT );
					listFragment.refreshData ();
					break;

				case R.id.pastGigLayout:
					pastGigLayout.setActivated ( true );
					currentGigLayout.setActivated ( false );
					bookingDataManager.setBookingFilter ( BookingFilter.PAST );
					listFragment.refreshData ();
					break;

				default:
					break;
			}
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
		menuImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.openMenu ();
			}
		} );

		currentGigLayout.setOnClickListener ( onGigTypeClickListener );
		pastGigLayout.setOnClickListener ( onGigTypeClickListener );
		currentGigLayout.performClick ();
	}

	@Override
	public void onResume ()
	{
		super.onResume ();
		bookingDataManager.setAmountUnreadBooking ( 0 );
	}
}