package com.vallverk.handyboy.view.profile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.GalleryAPIObject;
import com.vallverk.handyboy.model.api.GalleryAPIObject.GalleryAPIParams;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.GalleryWidget.GalleryViewPager;
import com.vallverk.handyboy.view.base.GalleryWidget.UrlPagerAdapter;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class GalleryViewFragment extends BaseFragment
{
	private ImageView backImageView;
	private TextView backTextView;
	private View crossImageView;

	private APIManager apiManager;
	private UserAPIObject user;

	private GalleryViewPager mViewPager;

	private List < GalleryAPIObject > galleryItems;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.gallery_layout, container, false );
			backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
			backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
			crossImageView = view.findViewById ( R.id.crossImageView );
			mViewPager = ( GalleryViewPager ) view.findViewById ( R.id.viewer );
		} else
		{
			( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}

	@Override
	protected void init ()
	{
		updateGalery ();
	}

	private void updateGaleryPhotos ( List < GalleryAPIObject > galleryItems )
	{
		List < String > items = new ArrayList < String > ();
		for ( GalleryAPIObject apiObject : galleryItems )
		{
			items.add ( apiObject.getString ( GalleryAPIParams.URL ) );
		}

		UrlPagerAdapter pagerAdapter = new UrlPagerAdapter ( controller, items );
		mViewPager.setAdapter ( pagerAdapter );
	}

	private void updateGalery ()
	{
		// UPDATE GALLERY
		new AsyncTask < Void, Void, String > ()
		{
			public void onPostExecute ( String result )
			{
				super.onPostExecute ( result );
				updateGaleryPhotos ( galleryItems );
			}

			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				galleryItems = new ArrayList < GalleryAPIObject > ();
				try
				{
					JSONArray requestArray = new JSONArray ( ServerManager.getRequest ( ServerManager.GET_GALLERY_URI + user.getValue ( UserParams.SERVICE_ID ) ) );
					for ( int i = 0; i < requestArray.length (); i++ )
					{
						galleryItems.add ( new GalleryAPIObject ( requestArray.getJSONObject ( i ) ) );
					}
				} catch ( Exception e )
				{
					result = e.getMessage ();
					e.printStackTrace ();
				}
				return result;
			}
		}.execute ();
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		apiManager = APIManager.getInstance ();
		user = apiManager.getUser ();
		addListeners ();
	}

	@Override
	protected void updateFonts ()
	{
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

		crossImageView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{

			}
		} );
	}
}