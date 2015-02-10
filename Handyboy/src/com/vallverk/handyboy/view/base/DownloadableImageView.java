package com.vallverk.handyboy.view.base;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.APIManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class DownloadableImageView extends ImageView
{
	private Bitmap bitmap;
    private DisplayImageOptions avatarLoadOptions = new DisplayImageOptions.Builder ().showImageOnFail (  R.drawable.avatar ).showImageForEmptyUri ( R.drawable.avatar ).cacheInMemory ( true ).cacheOnDisc ().build ();


    public enum Quality
	{
		ORIGINAL, MEDIUM, LOW
	}
	
	public DownloadableImageView ( Context context )
	{
		super ( context );
		// TODO Auto-generated constructor stub
	}

	public DownloadableImageView ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		// TODO Auto-generated constructor stub
	}

	public DownloadableImageView ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
		// TODO Auto-generated constructor stub
	}
	
	public void update ( final String url, final Quality quality )
	{
        String urlWithQuality = createUrl ( url, quality );
        ImageLoader.getInstance().displayImage(urlWithQuality, this, avatarLoadOptions, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                DownloadableImageView.this.bitmap = null;
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                DownloadableImageView.this.bitmap = bitmap;
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
            }
        });
		/*System.out.println ( "update" );
		new AsyncTask < Void, Void, String > ()
		{
			private Bitmap bitmap;
			
			public void onPostExecute ( String result )
			{
				super.onPostExecute ( result );
				if ( result.isEmpty () )
				{
					setImageBitmap ( bitmap );
				}
			}
			
			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					APIManager apiManager = APIManager.getInstance ();
					String urlWithQuality = createUrl ( url, quality );
					bitmap = apiManager.loadBitmap ( urlWithQuality );
				} catch ( Exception ex )
				{
					ex.printStackTrace ();
					result = ex.getMessage ();
				}
				return result;
			}
		}.execute ();*/
	}

	private String createUrl ( String url, Quality quality )
	{
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

	/*public void setImageBitmap ( Bitmap bitmap )
	{
		this.bitmap = bitmap;
		super.setImageBitmap ( bitmap );
	}*/
	
	public Bitmap getBitmap ()
	{
		return bitmap;
	}
}
