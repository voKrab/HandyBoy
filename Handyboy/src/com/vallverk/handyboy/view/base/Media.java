package com.vallverk.handyboy.view.base;

import android.graphics.Bitmap;
import android.net.Uri;

public class Media
{
	private Bitmap bitmap;
	private Uri uri;
	
	public Media ( Bitmap bitmap )
	{
		this ( bitmap, null );
	}
	
	public Media ( Bitmap bitmap, Uri uri )
	{
		this.bitmap = bitmap;
		this.uri = uri;
	}
	
	public Bitmap getBitmap ()
	{
		return bitmap;
	}
	public void setBitmap ( Bitmap bitmap )
	{
		this.bitmap = bitmap;
	}
	public Uri getUri ()
	{
		return uri;
	}
	public void setUri ( Uri uri )
	{
		this.uri = uri;
	}
	
	public boolean isPhoto ()
	{
		return uri == null;
	}
	
	public boolean isVideo ()
	{
		return uri != null;
	}

//	public boolean isUploaded ()
//	{
//		return uri.toString ().startsWith ( "http://files.parsetfss.com" );
//	}
}
