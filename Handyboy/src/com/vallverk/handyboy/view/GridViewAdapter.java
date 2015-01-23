package com.vallverk.handyboy.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.api.APIObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.view.base.DownloadableImageView.Quality;

public class GridViewAdapter extends ArrayAdapter < Object > implements ImageLoadingListener
{
	private LayoutInflater inflater;
	private DisplayImageOptions avatarLoadOptions = new DisplayImageOptions.Builder ().showImageOnFail (  R.drawable.avatar ).showImageForEmptyUri ( R.drawable.avatar ).cacheInMemory ( true ).cacheOnDisc ().build ();

	public GridViewAdapter ( Context context )
	{
		super ( context, 0 );
		inflater = LayoutInflater.from ( context );
	}

	public void notifyDataSetChanged ()
	{
		super.notifyDataSetChanged ();
	}

	public static class ViewHolder
	{
		public ImageView photoImageView;
	}

	public View getView ( int position, View view, ViewGroup parent )
	{
		final ViewHolder viewHolder;
		final APIObject item = ( APIObject ) getItem ( position );
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.grid_item_layout, parent, false );
			view.getLayoutParams ().height = MainActivity.SCREEN_WIDTH / 3;
			view.getLayoutParams ().width = MainActivity.SCREEN_WIDTH / 3;
			viewHolder = new ViewHolder ();
			viewHolder.photoImageView = ( ImageView ) view.findViewById ( R.id.photoImageView );
			view.setTag ( viewHolder );
		}else{
			viewHolder = ( ViewHolder ) view.getTag ();
		}
		String url = createUrl (  item.getValue ( UserParams.AVATAR ).toString (), Quality.LOW );
		ImageLoader.getInstance ().displayImage (url , viewHolder.photoImageView, avatarLoadOptions, this );
	
		view.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				MainActivity controller = MainActivity.getInstance ();
				controller.setCommunicationValue ( UserAPIObject.class.getSimpleName (), item );
				controller.setState ( VIEW_STATE.HANDY_BOY_PAGE );
			}
		} );
		return view;
	}
	
	private String createUrl ( String url, Quality quality )
	{
		if(url.equals ( "null" )){
			return "";
		}
		switch ( quality )
		{
			case ORIGINAL:
			{
				return url;
			}
			case MEDIUM:
			{
				return url.replace ( ".png", "_medium.png" );
			}
			case LOW:
			{
				return url.replace ( ".png", "_small.png" );
			}
			default:
			{
				return url;
			}
		}
	}
	
	@Override
	public void onLoadingCancelled ( String string, View view )
	{
	}

	@Override
	public void onLoadingComplete ( String string, View view, Bitmap bitmap )
	{
		View parent = (View) view.getParent ();
		parent.findViewById ( R.id.imageLoadProgress ).setVisibility ( View.GONE );
		view.setVisibility ( View.VISIBLE );
	}

	@Override
	public void onLoadingFailed ( String string, View view, FailReason reason )
	{
		View parent = (View) view.getParent ();
		parent.findViewById ( R.id.imageLoadProgress ).setVisibility ( View.GONE );
		view.setVisibility ( View.VISIBLE );
	}

	@Override
	public void onLoadingStarted ( String string, View view )
	{
		View parent = (View) view.getParent ();
		parent.findViewById ( R.id.imageLoadProgress ).setVisibility ( View.VISIBLE );
		view.setVisibility ( View.GONE );
	}
}