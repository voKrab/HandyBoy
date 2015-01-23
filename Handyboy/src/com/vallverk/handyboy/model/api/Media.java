package com.vallverk.handyboy.model.api;

import android.graphics.Bitmap;

import com.vallverk.handyboy.view.SerialBitmap;

public class Media
{
	private SerialBitmap serialBitmap;
	private String key;
	
	public Media ( SerialBitmap serialBitmap, String key )
	{
		this.serialBitmap = serialBitmap;
		this.key = key;
	}

	public String getKey ()
	{
		return key;
	}

	public void setKey ( String key )
	{
		this.key = key;
	}

	public Bitmap getBitmap ()
	{
		return serialBitmap.bitmap;
	}
}
