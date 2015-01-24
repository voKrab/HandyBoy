package com.vallverk.handyboy.view.base;

import java.util.ArrayList;
import java.util.Arrays;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.MainActivity.ApplicationAction;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.ChatManager;
import com.vallverk.handyboy.model.CommunicationManager;
import com.vallverk.handyboy.model.api.BookingDataManager;

public class TabBarView extends LinearLayout
{
	private int[] resources;
	private int[] selectedResources;
	private VIEW_STATE[] states;
	private int selectedTab;
	private ArrayList < ImageButton > buttons;

	private BadgeView chatBadgeView;
	private BadgeView bookingBadgeView;

	public TabBarView ( Context context )
	{
		super ( context );
	}

	public TabBarView ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		// TODO Auto-generated constructor stub
	}

	public TabBarView ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
		// TODO Auto-generated constructor stub
		Callback f;
	}

	private void playNotification ()
	{
		try
		{
			Uri notification = RingtoneManager.getDefaultUri ( RingtoneManager.TYPE_NOTIFICATION );
			Ringtone r = RingtoneManager.getRingtone ( getContext (), notification );
			r.play ();
		} catch ( Exception e )
		{
			e.printStackTrace ();
		}
	}

	private void showMessageCount ()
	{
		chatBadgeView.hide ();
		chatBadgeView.setText ( ChatManager.getInstance ().getAmountUnreadMessages () + "" );
		TranslateAnimation anim = new TranslateAnimation ( -100, 0, 0, 0 );
		anim.setInterpolator ( new BounceInterpolator () );
		anim.setDuration ( 1000 );
		chatBadgeView.toggle ( anim, null );
	}
	
	private void showBookingCount ()
	{
		bookingBadgeView.hide ();
		bookingBadgeView.setText ( BookingDataManager.getInstance ().getAmountUnreadBooking () + "" );
		TranslateAnimation anim = new TranslateAnimation ( -100, 0, 0, 0 );
		anim.setInterpolator ( new BounceInterpolator () );
		anim.setDuration ( 1000 );
		bookingBadgeView.toggle ( anim, null );
	}

	private Handler messageHandler = new Handler ( new Callback ()
	{

		@Override
		public boolean handleMessage ( Message msg )
		{
			if ( ChatManager.getInstance ().getAmountUnreadMessages () == 0 )
			{
				chatBadgeView.hide ();
			} else
			{
				playNotification ();
				showMessageCount ();
			}
			return true;
		}
	} );
	
	private Handler bookingCountHandler = new Handler ( new Callback ()
	{

		@Override
		public boolean handleMessage ( Message msg )
		{
			if ( BookingDataManager.getInstance ().getAmountUnreadBooking () == 0 )
			{
				bookingBadgeView.hide ();
			} else
			{
				//playNotification ();
				showBookingCount();
			}
			return true;
		}
	} );
	
	public void updateMessagesCount(){
		if ( ChatManager.getInstance ().getAmountUnreadMessages () > 0 ){
			showMessageCount ();
		}
	}
	
	public void updateBookingCount(){
		if ( BookingDataManager.getInstance ().getAmountUnreadBooking () > 0 ){
			showBookingCount ();
		}
	}

	public void init ( MainActivity controller )
	{
		resources = new int[] { R.drawable.chat_icon_bot_bar_na, R.drawable.dots_icon_bot_bar_na, controller.isService () ? R.drawable.speedometer_icon_bot_bar_na : R.drawable.mans_icon_bot_bar_na, R.drawable.calendar_icon_bot_bar_na, R.drawable.favourites_icon_bot_bar_na };
		selectedResources = new int[] { R.drawable.chat_icon_bot_bar_a, R.drawable.dots_icon_bot_bar_a, controller.isService () ? R.drawable.speedometer_icon_bot_bar_a : R.drawable.mans_icon_bot_bar_a, R.drawable.calendar_icon_bot_bar_a, R.drawable.favourites_icon_bot_bar_a };
		states = new VIEW_STATE[] { VIEW_STATE.CHATS, VIEW_STATE.FEED, controller.isService () ? VIEW_STATE.DASHBOARD : VIEW_STATE.CHOOSE_CATEGORY, VIEW_STATE.GIGS, VIEW_STATE.FAVORITES };

		ImageButton chatImageButton = ( ImageButton ) findViewById ( R.id.imageButton1 );
		chatBadgeView = new BadgeView ( controller, chatImageButton );
		chatBadgeView.setBackgroundResource ( R.drawable.white_oval );
		
		
		buttons = new ArrayList < ImageButton > ();
		buttons.add ( chatImageButton );
		buttons.add ( ( ImageButton ) findViewById ( R.id.imageButton2 ) );
		buttons.add ( ( ImageButton ) findViewById ( R.id.imageButton3 ) );
		
		ImageButton bookingImageButton = ( ImageButton ) findViewById ( R.id.imageButton4 );
		bookingBadgeView = new BadgeView ( controller, bookingImageButton );
		bookingBadgeView.setBackgroundResource ( R.drawable.white_oval );
		
		buttons.add ( ( ImageButton ) findViewById ( R.id.imageButton4 ) );
		buttons.add ( ( ImageButton ) findViewById ( R.id.imageButton5 ) );

		selectedTab = 2;
		
		updateMessagesCount ();
		updateBookingCount();
		
		updateSelection ();
		addListeners ();
	}

	private void addListeners ()
	{
		for ( final ImageButton item : buttons )
		{
			item.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View view )
				{
					selectedTab = buttons.indexOf ( item );
					selectTab ();
				}
			} );
		}

		CommunicationManager.getInstance ().addListener ( ApplicationAction.AMOUNT_UNREAD_MESSAGES, new Handler ()
		{
			@Override
			public void dispatchMessage ( Message message )
			{
				messageHandler.sendEmptyMessage ( 0 );
			}
		} );
		
		CommunicationManager.getInstance ().addListener ( ApplicationAction.AMOUNT_UNREAD_BOOKING, new Handler ()
		{
			@Override
			public void dispatchMessage ( Message message )
			{
				bookingCountHandler.sendEmptyMessage ( 0 );
			}
		} );
	}

	protected void selectTab ()
	{
		updateSelection ();
		CommunicationManager.getInstance ().fire ( ApplicationAction.TABBAR, states[selectedTab] );
	}

	private void updateSelection ()
	{
		int index = 0;
		for ( ImageButton item : buttons )
		{
			item.setImageResource ( resources[index++] );
		}
		buttons.get ( selectedTab ).setImageResource ( selectedResources[selectedTab] );
	}

	public void updateSelection ( VIEW_STATE newState )
	{
		selectedTab = Arrays.asList ( states ).indexOf ( newState );
		if ( selectedTab == -1 )
		{
			return;
		}
		updateSelection ();
	}

	public void manualSelection ( VIEW_STATE newTab )
	{
		selectedTab = Arrays.asList ( states ).indexOf ( newTab );
		selectTab ();
	}
}
