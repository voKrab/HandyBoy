package com.vallverk.handyboy.view.account;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.BlockListAPIObject;
import com.vallverk.handyboy.model.api.BlockListAPIObject.BlockListParams;
import com.vallverk.handyboy.model.api.FavoriteAPIObject;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.BaseListFragment;
import com.vallverk.handyboy.view.base.BaseListFragment.Refresher;
import com.vallverk.handyboy.view.base.DownloadableImageView.Quality;
import com.vallverk.handyboy.view.base.DownloadableImageView;
import com.vallverk.handyboy.view.base.FontUtils;
import com.vallverk.handyboy.view.base.FontUtils.FontStyle;

public class BlockListViewFragment extends BaseFragment
{
	private BaseListFragment blockListListViewFragment;
	private Dialog acceptDialog;
	private List blockList;
	
	private ImageView backImageView;
	private TextView backTextView;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.block_list_layout, container, false );
			
			backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
			backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
		} else
		{
			( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}
	
	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );

		blockListListViewFragment = ( BaseListFragment ) getActivity ().getSupportFragmentManager ().findFragmentById ( R.id.blockListViewFragment );
		blockListListViewFragment.setIsShowEmpty ( true );
		blockListListViewFragment.setAdapter ( new BlockListAdapter ( controller ) );
		blockListListViewFragment.setRefresher ( new Refresher ()
		{
			@Override
			public List < Object > refresh () throws Exception
			{
				String uri = ServerManager.BLOCK_GET_BY_USER_URI + controller.getUserId ();
				String requestString = ServerManager.getRequest ( uri );
				JSONObject requestJSON = new JSONObject ( requestString );

				String dataString = requestJSON.getString ( "object" );
				blockList = new ArrayList ();
				if ( !dataString.equals ( "null" ) )
				{
					JSONArray jsonArray = new JSONArray ( dataString );
					for ( int i = 0; i < jsonArray.length (); i++ )
					{
						JSONObject item = new JSONObject ( jsonArray.getString ( i ) );
						BlockListAPIObject blockListObject = new BlockListAPIObject ( item );
						blockList.add ( blockListObject );
					}
				}
				//onRefreshHandler.sendEmptyMessage ( 0 );

				return blockList;
			}
		} );
		blockListListViewFragment.refreshData ();
		
		addListeners ();
		updateFonts();
	}

	private void addListeners ()
	{
		backTextView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				controller.onBackPressed ();
			}
		} );

		backImageView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
				controller.onBackPressed ();
			}
		} );

	}
	
	private void updateBlock (final int serviceId)
	{
		new AsyncTask < Void, Void, String > ()
		{
			protected void onPreExecute ()
			{
				super.onPreExecute ();
				controller.showLoader ();
			};

			protected void onPostExecute ( String result )
			{
				super.onPostExecute ( result );
				blockListListViewFragment.refreshData ();
				controller.hideLoader ();
			};
			
			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					String url = ServerManager.DELETE_BLOCK_URI;
					JSONObject favoriteObject = new JSONObject ();
					favoriteObject.put ( "userId", APIManager.getInstance ().getUser ().getId () );
					favoriteObject.put ( "blockUserId", serviceId );
				
					ServerManager.postRequest ( url, favoriteObject );
				} catch ( Exception ex )
				{
					result = ex.getMessage ();
				}

				return result;
			}
		}.execute ();

	}
	
	private void showDeleteDialog ( final int serviceId )
	{
		acceptDialog = new Dialog ( getActivity () );
		acceptDialog.requestWindowFeature ( Window.FEATURE_NO_TITLE );
		acceptDialog.setContentView ( R.layout.available_dialog_layout );

		TextView dialogTitleTextView = ( TextView ) acceptDialog.findViewById ( R.id.dialogTitleTextView );
		TextView dialogBodyTextView = ( TextView ) acceptDialog.findViewById ( R.id.dialogBodyTextView );
		dialogBodyTextView.setVisibility ( View.GONE );
		dialogTitleTextView.setText ( controller.getString ( R.string.dialog_delete_from_block_list_title ) );

		View noButton = acceptDialog.findViewById ( R.id.dialogNoButton );
		View yesButton = acceptDialog.findViewById ( R.id.dialogYesButton );
		noButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				acceptDialog.dismiss ();
			}
		} );

		yesButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				acceptDialog.dismiss ();
				updateBlock ( serviceId );
			}
		} );

		acceptDialog.getWindow ().setBackgroundDrawable ( new ColorDrawable ( android.graphics.Color.TRANSPARENT ) );

		acceptDialog.show ();
	}

	private OnClickListener listItemsClickListener = new OnClickListener ()
	{

		@Override
		public void onClick ( View view )
		{
			showDeleteDialog ( Integer.parseInt ( view.getTag ().toString () ) );
		}
	};

	public class BlockListAdapter extends ArrayAdapter < Object >
	{
		private LayoutInflater inflater;

		public BlockListAdapter ( Context context )
		{
			super ( context, 0 );
			inflater = LayoutInflater.from ( context );
		}

		public View getView ( int position, View view, ViewGroup parent )
		{
			if ( view == null )
			{
				view = inflater.inflate ( R.layout.block_list_item_layout, parent, false );
			}

			final BlockListAPIObject item = ( BlockListAPIObject ) getItem ( position );
			TextView nameTextView = ( TextView ) view.findViewById ( R.id.nameTextView );
			nameTextView.setText ( item.getValue ( BlockListParams.FIRST_NAME ).toString () );

			TextView lastNameTextView = ( TextView ) view.findViewById ( R.id.lastNameTextView );
			lastNameTextView.setText ( item.getValue ( BlockListParams.LAST_NAME ).toString () );

			DownloadableImageView avatar = ( DownloadableImageView ) view.findViewById ( R.id.avatarImageView );
			avatar.update ( item.getValue ( BlockListParams.LOGO ).toString (), Quality.LOW );

			FontUtils.getInstance ( getContext () ).applyStyle ( nameTextView, FontStyle.BOLD );
			FontUtils.getInstance ( getContext () ).applyStyle ( lastNameTextView, FontStyle.REGULAR );

			View unblockButton = view.findViewById ( R.id.unblockButton );
			unblockButton.setOnClickListener ( listItemsClickListener );
			unblockButton.setTag ( item.getValue ( BlockListParams.USER_ID ) );

			return view;
		}
	}
}