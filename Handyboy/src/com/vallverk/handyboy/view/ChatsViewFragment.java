package com.vallverk.handyboy.view;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.ChatData;
import com.vallverk.handyboy.model.ChatManager;
import com.vallverk.handyboy.model.ChatMessageData;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.ChatAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.BaseListFragment;
import com.vallverk.handyboy.view.base.DownloadableImageView;
import com.vallverk.handyboy.view.base.BaseListFragment.Refresher;
import com.vallverk.handyboy.view.base.DownloadableImageView.Quality;

public class ChatsViewFragment extends BaseFragment
{
	private ImageView menuImageView;
	private BaseListFragment chatsListViewFragment;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.chats_layout, container, false );
			menuImageView = ( ImageView ) view.findViewById ( R.id.menuImageView );
		} else
		{
//			container.removeView ( view );
			( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );

		if ( chatsListViewFragment == null )
		{
			chatsListViewFragment = ( BaseListFragment ) getActivity ().getSupportFragmentManager ().findFragmentById ( R.id.chatsListViewFragment );
			chatsListViewFragment.setAdapter ( new ChatsListAdapter ( controller ) );
			chatsListViewFragment.setRefresher ( new Refresher ()
			{
				@Override
				public List < Object > refresh () throws Exception
				{
					// List < Object > objects = new ArrayList < Object > ();
					// objects.add ( APIManager.getInstance ().getUser () );
					String uri = ServerManager.CHAT_GET_BY_USER_URI;
					uri = uri.replaceAll ( "userId=1", "userId=" + controller.getUserId () );
					String requestString = ServerManager.getRequest ( uri );
					JSONObject requestJSON = new JSONObject ( requestString );
					String dataString = requestJSON.getString ( "list" );
					List chats = new ArrayList ();
					if ( !dataString.equals ( "null" ) )
					{
						JSONArray jsonArray = new JSONArray ( dataString );
						for ( int i = 0; i < jsonArray.length (); i++ )
						{
							JSONObject item = new JSONObject ( jsonArray.getString ( i ) );
							ChatManager chatManager = ChatManager.getInstance ();
							ChatAPIObject chatObject = new ChatAPIObject ( item );
							chatObject.setId ( item.getString ( "chatId" ) );
							chatManager.update ( chatObject, item );
							chats.add ( chatObject );
						}
					}
					return chats;
				}
			} );
			chatsListViewFragment.refreshData ();
		}

		updateComponents ();
		addListeners ();
	}

	private void updateComponents ()
	{
		
	}

	private void addListeners () {
        menuImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                controller.openMenu();
            }
        });
    }

	public class ChatsListAdapter extends ArrayAdapter < Object >
	{
		private Context context;

		public ChatsListAdapter ( Context context )
		{
			super ( context, 0 );
			this.context = context;
		}

		public View getView ( int position, View convertView, ViewGroup parent )
		{
			View view = convertView;
			if ( view == null )
			{
				LayoutInflater inflater = ( LayoutInflater ) this.getContext ().getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
				view = inflater.inflate ( R.layout.chats_list_item_layout, parent, false );
			}

			final ChatAPIObject item = ( ChatAPIObject ) getItem ( position );
			ChatManager chatManager = ChatManager.getInstance ();
			ChatData chatData = chatManager.getChat ( item.getId () );
			final ChatMessageData chatMessageData = chatData.getLastMessage ();
			final DownloadableImageView avatarImageView = ( DownloadableImageView ) view.findViewById ( R.id.avatarImageView );
			avatarImageView.update ( chatMessageData.getSenderAvatar (), Quality.LOW );
			final TextView nameTextView = ( TextView ) view.findViewById ( R.id.nameTextView );
			nameTextView.setText ( chatMessageData.getSenderName () );
			final TextView messageTextView = ( TextView ) view.findViewById ( R.id.lastMessageTextView );
			messageTextView.setText ( chatMessageData.getMessage () );
			final TextView timeTextView = ( TextView ) view.findViewById ( R.id.timeTextView );
			timeTextView.setText ( Tools.getTimeAM_PM ( chatMessageData.getCreatedAt () ) );
			int unreadMessages = chatData.getAmountUnreadMessages ();
			final TextView unreadedTextView = ( TextView ) view.findViewById ( R.id.unreadedTextView );
			unreadedTextView.setText ( "" + unreadMessages );
			unreadedTextView.setVisibility ( unreadMessages == 0 ? View.INVISIBLE : View.VISIBLE );
			
			view.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View v )
				{
					try
					{
						new AsyncTask < Void, Void, String >()
						{
							private UserAPIObject oponentUser;
							
							public void onPostExecute ( String result )
							{
								super.onPostExecute ( result );
								if ( result.isEmpty () )
								{
									controller.setCommunicationValue ( UserAPIObject.class.getSimpleName (), oponentUser );
									controller.setState ( VIEW_STATE.CHAT );
								}
							}
							@Override
							protected String doInBackground ( Void... params )
							{
								String result = "";
								try
								{
									String oponentUserId = chatMessageData.getSenderId ();
									oponentUser = ( UserAPIObject ) APIManager.getInstance ().getAPIObject ( oponentUserId, UserAPIObject.class, ServerManager.USER_FETCH_URI );
								} catch ( Exception ex )
								{
									ex.printStackTrace ();
									result = ex.getMessage ();
								}
								return result;
							}
						}.execute ();
					} catch ( Exception ex )
					{
						ex.printStackTrace ();
					}
				}
			});
			
			return view;
		}
	}
}