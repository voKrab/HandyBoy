package com.vallverk.handyboy.view.base;

import com.vallverk.handyboy.model.api.APIManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

public class DownloadableImageView extends ImageView
{
	private Bitmap bitmap;
	
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
		System.out.println ( "update" );
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
		}.execute ();
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

	@Override
	public void setImageBitmap ( Bitmap bitmap )
	{
		this.bitmap = bitmap;
		super.setImageBitmap ( bitmap );
	}
	
	public Bitmap getBitmap ()
	{
		return bitmap;
	}
}
