package com.vallverk.handyboy.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.gcm.PushNotification;
import com.vallverk.handyboy.model.ChatManager;
import com.vallverk.handyboy.model.CommunicationManager;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.ChatAPIObject;
import com.vallverk.handyboy.model.api.ChatAPIObject.ChatParams;
import com.vallverk.handyboy.model.api.ChatMessageAPIObject;
import com.vallverk.handyboy.model.api.ChatMessageAPIObject.ChatMessageParams;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.pubnub.NotificationWithDataAction;
import com.vallverk.handyboy.pubnub.PubnubManager;
import com.vallverk.handyboy.pubnub.PubnubManager.ActionType;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.BitmapPreviewDialogFragment;
import com.vallverk.handyboy.view.base.DownloadableImageView;
import com.vallverk.handyboy.view.base.DownloadableImageView.Quality;

public class ChatViewFragment extends BaseFragment
{
	private View backImageView;
	private View backTextView;
	private ScrollView scrollview;
	private LinearLayout messagesContainer;
	private ImageView attachImageView;
	private EditText messageEditText;
	private View sendContainer;
	private UserAPIObject service;
	private UserAPIObject currentUser;
	private List < ChatMessageAPIObject > messages;
	private ChatAPIObject chatObject;
	private Handler chatHandler;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.chat_layout, null );

		backImageView = view.findViewById ( R.id.backImageView );
		backTextView = view.findViewById ( R.id.backTextView );
		scrollview = ( ScrollView ) view.findViewById ( R.id.scrollView );
		messagesContainer = ( LinearLayout ) view.findViewById ( R.id.messagesContainer );
		attachImageView = ( ImageView ) view.findViewById ( R.id.attachImageView );
		messageEditText = ( EditText ) view.findViewById ( R.id.messageEditText );
		sendContainer = view.findViewById ( R.id.sendContainer );

		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );

		final APIManager apiManager = controller.getAPIManager ();
		currentUser = apiManager.getUser ();
		UserAPIObject newService = ( UserAPIObject ) controller.getCommunicationValue ( UserAPIObject.class.getSimpleName () );
		if ( newService != null )
		{
			service = newService;
			new AsyncTask < Void, Void, String > ()
			{
				protected void onPreExecute ()
				{
					controller.showLoader ();
				}

				public void onPostExecute ( String result )
				{
					controller.hideLoader ();
					if ( result.isEmpty () )
					{
						updateComponents ();
						addListeners ();
					} else
					{
						Toast.makeText ( controller, result, Toast.LENGTH_LONG ).show ();
					}
				}

				@Override
				protected String doInBackground ( Void... params )
				{
					String result = "";
					try
					{
						String currentUserId = currentUser.getId ();
						String serviceId = service.getId ();
						// messages = apiManager.getMessages ( currentUserId,
						// serviceId );

						messages = new ArrayList < ChatMessageAPIObject > ();
						String url = ServerManager.MESSAGES_GET;
						// https://142.4.217.86/chat/?userIdFirst=1&userIdSecond=2
						url = url.replaceAll ( "userIdFirst=1", "userIdFirst=" + currentUserId );
						url = url.replaceAll ( "userIdSecond=2", "userIdSecond=" + serviceId );
						String requestString = ServerManager.getRequest ( url );
						JSONObject requestJSON = new JSONObject ( requestString );
						String listData = requestJSON.getString ( "list" );
						if ( !listData.equals ( "null" ) )
						{
							JSONArray jsonArray = new JSONArray ( listData );
							for ( int i = 0; i < jsonArray.length (); i++ )
							{
								JSONObject jsonItem = new JSONObject ( jsonArray.getString ( i ) );
								ChatMessageAPIObject chatMessage = new ChatMessageAPIObject ( jsonItem );
								messages.add ( chatMessage );
							}
						}

						chatObject = new ChatAPIObject ();
						chatObject.putValue ( ChatParams.USER_ID_FIRST, service.getId () );
						chatObject.putValue ( ChatParams.USER_ID_SECOND, currentUser.getId () );
						String chatId = requestJSON.getString ( "parameters" );
						if ( chatId.equals ( "-1" ) && messages.isEmpty () )
						{
							apiManager.insert ( chatObject, ServerManager.CHAT_INSERT_URI );
						} else
						{
							if ( chatId.isEmpty () )
							{
								chatId = messages.get ( 0 ).getString ( ChatMessageParams.CHAT_OBJECT_ID );
							}
							chatObject.setId ( chatId );
						}
					} catch ( Exception ex )
					{
						result = ex.getMessage ();
						ex.printStackTrace ();
					}
					return result;
				}
			}.execute ();
		}
	}

	private void createChatHandler ()
	{
		chatHandler = new Handler ()
		{
			@Override
			public void dispatchMessage ( Message message )
			{
				NotificationWithDataAction pubnubAction = ( NotificationWithDataAction ) message.obj;
				if ( isThisChat ( pubnubAction ) )
				{
					final ChatMessageAPIObject chatMessage = new ChatMessageAPIObject ();
					chatMessage.putValue ( ChatMessageParams.CHAT_OBJECT_ID, chatObject.getId () );
					chatMessage.putValue ( ChatMessageParams.SENDER_ID, pubnubAction.getSenderId () );
					chatMessage.putValue ( ChatMessageParams.MESSAGE, pubnubAction.getString ( ChatMessageParams.MESSAGE ) );
					chatMessage.putValue ( ChatMessageParams.DATE, pubnubAction.getString ( ChatMessageParams.DATE ) );
					chatMessage.putValue ( ChatMessageParams.IMAGE_URL, pubnubAction.getString ( ChatMessageParams.IMAGE_URL ) );
					addMessage ( chatMessage );
				}
			}

			private boolean isThisChat ( NotificationWithDataAction pubnubAction )
			{
				return pubnubAction.getSenderId ().equals ( service.getId () ) && pubnubAction.getReciverId ().equals ( currentUser.getId () );
			}
		};
	}

	private void updateComponents ()
	{
		updateMessagesContainer ();
	}

	private void updateMessagesContainer ()
	{
		messagesContainer.removeAllViews ();
		for ( ChatMessageAPIObject chatMessage : messages )
		{
			addMessage ( chatMessage );
		}
	}

	private void addMessage ( ChatMessageAPIObject chatMessage )
	{
		boolean isMyMessage = chatMessage.getString ( ChatMessageParams.SENDER_ID ).equals ( currentUser.getId () );
		final LayoutInflater inflater = ( LayoutInflater ) controller.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
		final View view = inflater.inflate ( isMyMessage ? R.layout.chat_right_message_layout : R.layout.chat_left_message_layout, null );
		updateMessageView ( view, isMyMessage, chatMessage );
		messagesContainer.addView ( view );
		scrollview.post ( new Runnable ()
		{
			@Override
			public void run ()
			{
				scrollview.setSmoothScrollingEnabled ( false );
				scrollview.setSmoothScrollingEnabled ( false );
				scrollview.fullScroll ( ScrollView.FOCUS_DOWN );
			}
		} );
		ChatManager.getInstance ().chatViewed ( chatObject.getId () );
	}

	private void updateMessageView ( View view, boolean isMyMessage, ChatMessageAPIObject chatMessage )
	{
		if ( !isMyMessage )
		{
			DownloadableImageView avatarImageView = ( DownloadableImageView ) view.findViewById ( R.id.avatarImageView );
			avatarImageView.update ( service.getString ( UserParams.AVATAR ), Quality.LOW );
		}
		TextView messageTextView = ( TextView ) view.findViewById ( R.id.messageTextView );
		final DownloadableImageView messageImageView = ( DownloadableImageView ) view.findViewById ( R.id.messageImageView );
		messageTextView.setVisibility ( View.GONE );
		messageImageView.setVisibility ( View.GONE );
		String message = chatMessage.getString ( ChatMessageParams.MESSAGE );
		if ( message == null || message.isEmpty () )
		{
			Bitmap attach = chatMessage.getAttach ();
			String imageUri = chatMessage.getString ( ChatMessageParams.IMAGE_URL );
			messageImageView.setVisibility ( View.VISIBLE );
			if ( imageUri == null || imageUri.isEmpty () ) // is mean attach !=
															// null
			{
				messageImageView.setImageBitmap ( attach );
			} else
			{
				messageImageView.update ( imageUri, Quality.LOW );
			}
			messageImageView.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View v )
				{
					Bitmap bitmap = messageImageView.getBitmap ();
					if ( bitmap == null )
					{
						return;
					}
					FragmentTransaction ft = getActivity ().getSupportFragmentManager ().beginTransaction ();
					DialogFragment newFragment = new BitmapPreviewDialogFragment ( bitmap );
					newFragment.show ( ft, null );
				}
			} );
		} else
		{
			messageTextView.setText ( message );
			messageTextView.setVisibility ( View.VISIBLE );
		}
		TextView timeTextView = ( TextView ) view.findViewById ( R.id.timeTextView );
		long dateInMiliseconds = Long.parseLong ( chatMessage.getString ( ChatMessageParams.DATE ) ) * 1000;
		// Date date = new Date ( dateInMiliseconds );
		String delegate = "hh:mm aaa";
		String formatedDate = ( String ) DateFormat.format ( delegate, dateInMiliseconds );
		timeTextView.setText ( formatedDate );
	}

	@Override
	public void onPause ()
	{
		super.onPause ();
		if ( chatHandler != null )
		{
			CommunicationManager.getInstance ().removeListener ( ActionType.CHAT, chatHandler );
		}
	}

	public void onResume ()
	{
		super.onResume ();
		createChatHandler ();
		CommunicationManager.getInstance ().addListener ( ActionType.CHAT, chatHandler );
	}

	private void addListeners ()
	{
		attachImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				chooseMedia ( MediaType.PHOTO );
			}
		} );
		backImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				getActivity ().onBackPressed ();
			}
		} );
		backTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				getActivity ().onBackPressed ();
			}
		} );
		sendContainer.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				sendMessage ();
			}
		} );
	}

	protected void sendMessage ()
	{
		final String message = messageEditText.getText ().toString ().trim ();
		if ( message.isEmpty () )
		{
			Toast.makeText ( controller, R.string.message_is_empty, Toast.LENGTH_LONG ).show ();
			return;
		}
		sendMessage ( message );
	}

	private void sendMessage ( final Object content )
	{
		final ChatMessageAPIObject chatMessage = new ChatMessageAPIObject ();
		chatMessage.putValue ( ChatMessageParams.CHAT_OBJECT_ID, chatObject.getId () );
		chatMessage.putValue ( ChatMessageParams.SENDER_ID, currentUser.getId () );
		if ( content instanceof String )
		{
			chatMessage.putValue ( ChatMessageParams.MESSAGE, content );
		} else
		{
			chatMessage.setAttach ( ( Bitmap ) content );
		}

		final String date = "" + ( System.currentTimeMillis () / 1000 );
		chatMessage.putValue ( ChatMessageParams.DATE, date );

		addMessage ( chatMessage );
		messageEditText.setText ( "" );
		new AsyncTask < Void, Void, String > ()
		{
			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					APIManager apiManager = controller.getAPIManager ();
					Bitmap attach = chatMessage.getAttach ();
					if ( attach != null )
					{
						String imageUri = apiManager.saveBitmap ( attach );
						chatMessage.putValue ( ChatMessageParams.IMAGE_URL, imageUri );
					}
					apiManager.insert ( chatMessage, ServerManager.CHAT_MESSAGE_INSERT_URI );

					Map < Object, Object > data = new HashMap < Object, Object > ();

					if ( content instanceof String )
					{
						data.put ( ChatMessageParams.MESSAGE, content );
						data.put ( ChatMessageParams.IMAGE_URL, "" );
					} else
					{
						data.put ( ChatMessageParams.MESSAGE, "" );
						data.put ( ChatMessageParams.IMAGE_URL, chatMessage.getString ( ChatMessageParams.IMAGE_URL ) );
					}

					data.put ( ChatMessageParams.DATE, date );
					data.put ( ChatMessageParams.CHAT_OBJECT_ID, chatObject.getId () );
					data.put ( "senderName", currentUser.getString ( UserParams.FIRST_NAME ) + " " + currentUser.getString ( UserParams.LAST_NAME ) );
					data.put ( "senderAvatar", currentUser.getString ( UserParams.AVATAR ) );
					NotificationWithDataAction pubnubAction = new NotificationWithDataAction ( currentUser.getId (), service.getId (), ActionType.CHAT, data );
					PubnubManager.getInstance ().sendAction ( pubnubAction );
					// GCM
					JSONObject gcmData = PushNotification.createJSON ( "", ActionType.CHAT );
					if ( content instanceof String )
					{
						gcmData.put ( ChatMessageParams.MESSAGE.toString (), currentUser.getShortName () + ": " + content );
						gcmData.put ( ChatMessageParams.IMAGE_URL.toString (), "" );
					} else
					{
						gcmData.put ( ChatMessageParams.MESSAGE.toString (), currentUser.getShortName () + ": send you image" );
						gcmData.put ( ChatMessageParams.IMAGE_URL.toString (), chatMessage.getString ( ChatMessageParams.IMAGE_URL ) );
					}
					gcmData.put ( ChatMessageParams.DATE.toString (), date );
					gcmData.put ( ChatMessageParams.CHAT_OBJECT_ID.toString (), chatObject.getId () );
					gcmData.put ( "senderName", currentUser.getShortName () );
					gcmData.put ( "senderAvatar", currentUser.getString ( UserParams.AVATAR ) );
					gcmData.put ( "senderId", currentUser.getId () );
					gcmData.put ( "reciverId", service.getId () );
					gcmData.put ( "type", ActionType.CHAT.toString () );
					gcmData.put ( "createdAt", System.currentTimeMillis () );
//					PushNotification.send ( service, gcmData );
				} catch ( Exception ex )
				{
					ex.printStackTrace ();
					result = ex.getMessage ();
				}
				return result;
			}
		}.execute ();
	}

	@Override
	protected void photoFromEditor ( Uri uri )
	{
		try
		{
			final Bitmap bitmap = Tools.decodeSampledBitmapFromResource ( MainActivity.MAX_POSTED_IMAGE_WIDTh, MainActivity.MAX_POSTED_IMAGE_HEIGHT, getActivity ().getContentResolver (), uri );
			sendMessage ( bitmap );
		} catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
	}
}