package com.vallverk.handyboy.view.booking;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.ReviewAPIObject;
import com.vallverk.handyboy.model.api.ReviewAPIObject.ReviewParams;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.BaseListFragment;
import com.vallverk.handyboy.view.base.RatingView;
import com.vallverk.handyboy.view.base.BaseListFragment.Refresher;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReviewsClientViewFragment extends BaseFragment
{
	private ImageView backImageView;
	private TextView backTextView;
	private UserAPIObject customer;
	private BookingDataManager bookingDataManager;

	private BaseListFragment listFragment;
	private List data;

	public static class ReviewItemUserObject
	{
		public UserAPIObject userAPIObject;
		public ReviewAPIObject reviewAPIObject;
	}

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.review_client_layout, container, false );
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
		bookingDataManager = BookingDataManager.getInstance ();
		customer = bookingDataManager.getData ().get ( bookingDataManager.getActiveDataIndex () ).getCustomer ();
		if ( listFragment == null )
		{
			listFragment = ( BaseListFragment ) getActivity ().getSupportFragmentManager ().findFragmentById ( R.id.reviewsListViewFragment );
			listFragment.setRefresher ( new Refresher ()
			{
				@Override
				public List < Object > refresh () throws Exception
				{
					data = new ArrayList < ReviewItemUserObject > ();
					JSONArray requestJsonArray;
					JSONObject requestJsonObject;
					APIManager apiManager = APIManager.getInstance ();
					String request = ServerManager.getRequest ( ServerManager.GET_REVIEWS + "ownerId=" + customer.getId () );
					requestJsonObject = new JSONObject ( request );
					requestJsonArray = new JSONArray ( requestJsonObject.getString ( "list" ) );
					data.clear ();
					for ( int i = 0; i < requestJsonArray.length (); i++ )
					{
						JSONObject tempJsonObject = requestJsonArray.getJSONObject ( i );
						ReviewItemUserObject reviewItemUserObject = new ReviewItemUserObject ();
						reviewItemUserObject.userAPIObject = new UserAPIObject ( tempJsonObject.getJSONObject ( "reviewer" ) );

						reviewItemUserObject.reviewAPIObject = new ReviewAPIObject ( tempJsonObject );
						data.add ( reviewItemUserObject );
					}
					return data;
				}
			} );
			listFragment.setAdapter ( new ReviewsListAdapter ( controller ) );
		}
		listFragment.refreshData ();
		addListeners ();
	}

	public static class ReviewsListAdapter extends ArrayAdapter < Object >
	{
		private DisplayImageOptions avatarLoadOptions = new DisplayImageOptions.Builder ().showImageOnFail ( R.drawable.avatar ).showImageForEmptyUri ( R.drawable.avatar ).cacheInMemory ( true ).cacheOnDisc ().build ();
		private LayoutInflater inflater;

		public ReviewsListAdapter ( Context context )
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
			ImageView avatarImage;
			TextView nameTextView;
			TextView descriptionTextView;
			RatingView ratingView;
			TextView dateTextView;
		}

		@Override
		public View getView ( int position, View view, ViewGroup parent )
		{
			final ViewHolder viewHolder;
			ReviewItemUserObject reviewItemUserObject = ( ReviewItemUserObject ) getItem ( position );
			if ( view == null )
			{
				view = inflater.inflate ( R.layout.review_list_item_layout, parent, false );
				viewHolder = new ViewHolder ();
				viewHolder.nameTextView = ( TextView ) view.findViewById ( R.id.nameTextView );
				viewHolder.avatarImage = ( ImageView ) view.findViewById ( R.id.myAvatarImage );
				viewHolder.descriptionTextView = ( TextView ) view.findViewById ( R.id.descriptionTextView );
				viewHolder.ratingView = ( RatingView ) view.findViewById ( R.id.ratingView );
				viewHolder.dateTextView = ( TextView ) view.findViewById ( R.id.dateTextView );
				view.setTag ( viewHolder );
			} else
			{
				viewHolder = ( ViewHolder ) view.getTag ();
			}

			ImageLoader.getInstance ().displayImage ( reviewItemUserObject.userAPIObject.getString ( UserParams.AVATAR ), viewHolder.avatarImage, avatarLoadOptions );
			viewHolder.nameTextView.setText ( reviewItemUserObject.userAPIObject.getShortName () );
			viewHolder.descriptionTextView.setText ( reviewItemUserObject.reviewAPIObject.getString ( ReviewParams.CONTENT ) );
			try
			{
              viewHolder.dateTextView.setText ( Tools.fromTimestampToString ( Long.parseLong ( reviewItemUserObject.reviewAPIObject.getValue(ReviewParams.TIME).toString()) * 1000));
              // viewHolder.dateTextView.setText (Long.parseLong ( reviewItemUserObject.reviewAPIObject.getValue(ReviewParams.TIME).toString()) + "");

                viewHolder.ratingView.setRating ( Float.parseFloat ( reviewItemUserObject.reviewAPIObject.getValue ( ReviewParams.RATING ).toString () ) );
			} catch ( NumberFormatException ex )
			{
				ex.printStackTrace ();
			}
			return view;
		}
	}

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
}