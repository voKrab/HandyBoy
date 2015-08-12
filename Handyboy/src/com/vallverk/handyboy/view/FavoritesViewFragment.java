package com.vallverk.handyboy.view;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.FavoriteAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.FavoriteAPIObject.FavoriteParams;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.BaseListFragment;
import com.vallverk.handyboy.view.base.BaseListFragment.Refresher;
import com.vallverk.handyboy.view.base.DownloadableImageView.Quality;
import com.vallverk.handyboy.view.base.DownloadableImageView;
import com.vallverk.handyboy.view.base.FontUtils;
import com.vallverk.handyboy.view.base.FontUtils.FontStyle;

public class FavoritesViewFragment extends BaseFragment
{
	private ImageView menuImageView;
	private BaseListFragment favoritesListViewFragment;
	private LinearLayout noFavoritesLayout;
	private Dialog acceptDialog;
	private List favs;

	private TextView noFavoritesTitle;
	private TextView noFavoritesBody;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.favorites_layout, container, false );
			menuImageView = ( ImageView ) view.findViewById ( R.id.menuImageView );
			noFavoritesLayout = ( LinearLayout ) view.findViewById ( R.id.noFavoritesLayout );
			noFavoritesTitle = ( TextView ) view.findViewById ( R.id.noFavoritesTitle );
			noFavoritesBody = ( TextView ) view.findViewById ( R.id.noFavoritesBody );

		} else
		{
			//( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}

	private Handler onRefreshHandler = new Handler ( new Callback ()
	{

		@Override
		public boolean handleMessage ( Message msg )
		{
			if ( favs.size () > 0 )
			{
				noFavoritesLayout.setVisibility ( View.GONE );
			} else
			{
				noFavoritesLayout.setVisibility ( View.VISIBLE );
			}
			return false;
		}
	} );

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
        //getChildFragmentManager().beginTransaction().add( R.id.favoritesListViewFragment, new BaseListFragment(), "favoritesListViewFragment").commit();
        if ( favoritesListViewFragment == null ) {
            favoritesListViewFragment = (BaseListFragment) getChildFragmentManager().findFragmentById(R.id.favoritesListViewFragment);
            favoritesListViewFragment.setIsShowEmpty(false);
            favoritesListViewFragment.setAdapter(new FavsListAdapter(controller));
            favoritesListViewFragment.setRefresher(new Refresher() {
                @Override
                public List<Object> refresh() throws Exception {
                    String uri = ServerManager.FAVS_GET_BY_USER_URI + controller.getUserId();
                    String requestString = ServerManager.getRequest(uri);
                    JSONObject requestJSON = new JSONObject(requestString);

                    String dataString = requestJSON.getString("object");
                    favs = new ArrayList();
                    if (!dataString.equals("null")) {
                        JSONArray jsonArray = new JSONArray(dataString);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = new JSONObject(jsonArray.getString(i));
                            UserAPIObject favObject = new UserAPIObject(item);
                            favs.add(favObject);
                        }
                    }
                    onRefreshHandler.sendEmptyMessage(0);

                    return favs;
                }
            });
            favoritesListViewFragment.refreshData ();
        }


		updateComponents ();
		addListeners ();
		updateFonts ();
	}

	private void updateComponents ()
	{

	}

	@Override
	protected void updateFonts ()
	{
		FontUtils.getInstance ( controller ).applyStyle ( noFavoritesTitle, FontStyle.LIGHT );
		FontUtils.getInstance ( controller ).applyStyle ( noFavoritesBody, FontStyle.REGULAR );
	}

	private void addListeners ()
	{
		menuImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View arg0 )
			{
				controller.openMenu ();
			}
		} );
	}

	private void updateLike ( final int serviceId )
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
				favoritesListViewFragment.refreshData ();
				controller.hideLoader ();
			};

			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					String url = ServerManager.DELETE_FAVORITE_URI;
					JSONObject favoriteObject = new JSONObject ();
					favoriteObject.put ( "userId", APIManager.getInstance ().getUser ().getId () );
					favoriteObject.put ( "serviceId", serviceId );
					ServerManager.postRequest ( url, favoriteObject );

				} catch ( Exception ex )
				{
					ex.printStackTrace ();
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
		dialogTitleTextView.setText ( controller.getString ( R.string.dialog_delete_title ) );

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
				updateLike ( serviceId );
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
			UserAPIObject userAPIObject = null;
			switch ( view.getId () )
			{
				case R.id.likeImageView:
					showDeleteDialog ( Integer.parseInt ( view.getTag ().toString () ) );
					break;
				case R.id.shareImageView:

					break;
				case R.id.chatImageView:
					userAPIObject = ( UserAPIObject ) view.getTag ();
					if ( userAPIObject != null )
					{
						controller.setCommunicationValue ( UserAPIObject.class.getSimpleName (), userAPIObject );
						controller.setState ( VIEW_STATE.CHAT );
					}
					break;

				case R.id.nameTextView:
				case R.id.lastNameTextView:
				case R.id.avatarImageView:
					userAPIObject = ( UserAPIObject ) view.getTag ();
					if ( userAPIObject != null )
					{
						controller.setCommunicationValue ( UserAPIObject.class.getSimpleName (), userAPIObject );
						controller.setState ( VIEW_STATE.HANDY_BOY_PAGE );
					}
					break;

				default:
					break;
			}
		}
	};
	
	public class FavsListAdapter extends ArrayAdapter < Object >
	{
		private LayoutInflater inflater;

		public FavsListAdapter ( Context context )
		{
			super ( context, 0 );
			inflater = LayoutInflater.from ( context );
		}

		public View getView ( int position, View view, ViewGroup parent )
		{
			if ( view == null )
			{
				view = inflater.inflate ( R.layout.favs_list_item_layout, parent, false );
			}

			final UserAPIObject item = ( UserAPIObject ) getItem ( position );
			TextView nameTextView = ( TextView ) view.findViewById ( R.id.nameTextView );
			nameTextView.setText ( item.getValue ( FavoriteParams.FIRST_NAME ).toString () );
			nameTextView.setTag ( item );
			nameTextView.setOnClickListener ( listItemsClickListener );

			TextView lastNameTextView = ( TextView ) view.findViewById ( R.id.lastNameTextView );
			lastNameTextView.setText ( item.getValue ( FavoriteParams.LAST_NAME ).toString () );
			lastNameTextView.setTag ( item );
			lastNameTextView.setOnClickListener ( listItemsClickListener );

			DownloadableImageView avatar = ( DownloadableImageView ) view.findViewById ( R.id.avatarImageView );
			avatar.update ( item.getValue ( FavoriteParams.LOGO ).toString (), Quality.LOW );
			avatar.setTag ( item );
			avatar.setOnClickListener ( listItemsClickListener );

			FontUtils.getInstance ( getContext () ).applyStyle ( nameTextView, FontStyle.BOLD );
			FontUtils.getInstance ( getContext () ).applyStyle ( lastNameTextView, FontStyle.REGULAR );

			View likeImageView = view.findViewById ( R.id.likeImageView );
			likeImageView.setOnClickListener ( listItemsClickListener );
			likeImageView.setTag ( item.getValue ( FavoriteParams.SERVICE_ID ) );

			View shareImageView = view.findViewById ( R.id.shareImageView );
			shareImageView.setOnClickListener ( listItemsClickListener );

			View chatImageView = view.findViewById ( R.id.chatImageView );
			chatImageView.setOnClickListener ( listItemsClickListener );
			chatImageView.setTag ( item );

			return view;
		}
	}
}