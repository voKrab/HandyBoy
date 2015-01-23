package com.vallverk.handyboy.view.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView.ScaleType;
import android.widget.ViewFlipper;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.view.base.DownloadableImageView.Quality;

public class MediaFlipperView extends ViewFlipper
{
	protected static final int FROM_GALLERY_MORE_PHOTO = 1000;
	protected List < Media > medias;
	protected Context context;
	protected BaseFragment parentView;
	protected FlipperStatusView statusLayout;
	protected static int MAX_ITEMS_COUNT = 7;

	public MediaFlipperView ( Context context )
	{
		super ( context );
		// TODO Auto-generated constructor stub
	}

	public MediaFlipperView ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );

		if ( !isInEditMode () )
		{
			this.context = context;

			LayoutInflater inflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
			medias = new ArrayList < Media > ();
		}
	}

	public void clearItems ()
	{
		removeAllViews ();
	}
	
	public void setMedia ( String mediaString )
	{
//		if ( media == null )
//		{
//			return;
//		}
//		if ( media.isPhoto () )
		if ( !mediaString.isEmpty () )
		{
			DownloadableImageView imageView = new DownloadableImageView ( context );
			imageView.setScaleType ( ScaleType.CENTER_CROP );
			imageView.update ( mediaString, Quality.MEDIUM );
			//imageView.setImageBitmap ( media.getBitmap () );
			addView ( imageView );
		} else
		{
//			TextureVideoView cropTextureView = new TextureVideoView ( context );
//			cropTextureView.setScaleType ( TextureVideoView.ScaleType.CENTER_CROP );
//			cropTextureView.setDataSource ( media.getUri ().toString () );
//			cropTextureView.setLooping ( true );
//			cropTextureView.play ();
//			addView ( cropTextureView );
		}
		statusLayout.on ( getChildCount () );
	}

	public void showNext ()
	{
		if ( getChildCount () == 1 )
		{
			return;
		}
		setInAnimation ( AnimationUtils.loadAnimation ( context, R.anim.go_next_in ) );
		setOutAnimation ( AnimationUtils.loadAnimation ( context, R.anim.go_next_out ) );
		int newSelectedIndex = statusLayout.select ( indexOfChild ( getCurrentView () ) + 1 );
		View view = getChildAt ( newSelectedIndex );
		if ( view instanceof TextureVideoView )
		{
			TextureVideoView videoView = ( TextureVideoView ) view;
			videoView.play ();
		} else
		{
			stopAllVideo ();
		}
		super.showNext ();
	}

	public void showPrevious ()
	{
		if ( getChildCount () == 1 )
		{
			return;
		}
		setInAnimation ( AnimationUtils.loadAnimation ( context, R.anim.go_prev_in ) );
		setOutAnimation ( AnimationUtils.loadAnimation ( context, R.anim.go_prev_out ) );
		int newSelectedIndex = statusLayout.select ( indexOfChild ( getCurrentView () ) - 1 );
		View view = getChildAt ( newSelectedIndex );
		if ( view instanceof TextureVideoView )
		{
			TextureVideoView videoView = ( TextureVideoView ) view;
			videoView.play ();
		} else
		{
			stopAllVideo ();
		}
		super.showPrevious ();
	}

	public void stopAllVideo ()
	{
		for ( int i = 0; i < getChildCount (); i++ )
		{
			View view = getChildAt ( i );
			if ( view instanceof TextureVideoView )
			{
				TextureVideoView videoView = ( TextureVideoView ) view;
				videoView.stop ();
			}
		}
	}
	
	public void setFragmentContext ( BaseFragment fragment )
	{
		this.parentView = fragment;
	}

	public void setStatusLayout ( View statusLayout )
	{
		this.statusLayout = ( FlipperStatusView ) statusLayout;
	}

	public List < Media > getMedias ()
	{
		return medias;
	}
}
