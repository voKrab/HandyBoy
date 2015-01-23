/*
 * Copyright 2013, Edmodo, Inc. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License.
 * You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" 
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License. 
 */

package com.vallverk.handyboy.view.base.rangeslider;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.util.Log;
import android.util.TypedValue;

/**
 * Represents a thumb in the RangeBar slider. This is the handle for the slider
 * that is pressed and slid.
 */
class Thumb
{

	// Private Constants ///////////////////////////////////////////////////////

	// The radius (in dp) of the touchable area around the thumb. We are basing
	// this value off of the recommended 48dp Rhythm. See:
	// http://developer.android.com/design/style/metrics-grids.html#48dp-rhythm
	private static final float MINIMUM_TARGET_RADIUS_DP = 24;

	// Sets the default values for radius, normal, pressed if circle is to be
	// drawn but no value is given.
	private static final float DEFAULT_THUMB_RADIUS_DP = 14;

	// Corresponds to android.R.color.holo_blue_light.
	private static final int DEFAULT_THUMB_COLOR_NORMAL = 0xff33b5e5;
	private static final int DEFAULT_THUMB_COLOR_PRESSED = 0xff33b5e5;

	// Member Variables ////////////////////////////////////////////////////////

	// Radius (in pixels) of the touch area of the thumb.
	private float mTargetRadiusPx;

	// The normal and pressed images to display for the thumbs.
	private Bitmap mImageNormal = null;
	private Bitmap mImagePressed;

	// Variables to store half the width/height for easier calculation.
	private float mHalfWidthNormal;
	private float mHalfHeightNormal;

	private float mHalfWidthPressed;
	private float mHalfHeightPressed;

	// Indicates whether this thumb is currently pressed and active.
	private boolean mIsPressed = false;

	// The y-position of the thumb in the parent view. This should not change.
	private float mY = 0;

	// The current x-position of the thumb in the parent view.
	private float mX = 0;

	// mPaint to draw the thumbs if attributes are selected
	private Paint mPaintNormal;
	private Paint mPaintPressed;

	// Radius of the new thumb if selected
	private float mThumbRadiusPx = 0;

	// Toggle to select bitmap thumbImage or not
	private boolean mUseBitmap;

	// Colors of the thumbs if they are to be drawn
	private int mThumbColorNormal;
	private int mThumbColorPressed;

	private Context context;

	private int thumbImageNormal, thumbImagePressed;
	private int index = 0;

	private ThumbType thumbType;

	public enum ThumbType
	{
		PRICE, LBS, INCH, MINUTES, NUMBER
	}

	// Constructors ////////////////////////////////////////////////////////////
	Thumb ( Context ctx, float y, int thumbColorNormal, int thumbColorPressed, float thumbRadiusDP, int thumbImageNormal, int thumbImagePressed, ThumbType thumbType, int index )
	{
		this.index = index;
		this.thumbType = thumbType;
		init ( ctx, y, thumbColorNormal, thumbColorPressed, thumbRadiusDP, thumbImageNormal, thumbImagePressed );
	}

	Thumb ( Context ctx, float y, int thumbColorNormal, int thumbColorPressed, float thumbRadiusDP, int thumbImageNormal, int thumbImagePressed )
	{
		init ( ctx, y, thumbColorNormal, thumbColorPressed, thumbRadiusDP, thumbImageNormal, thumbImagePressed );

	}
	
	private String inchToFoot(int inches){
		int feet = inches / 12;
		int leftover = inches % 12;
		return feet + "' " +  leftover + "\"";
	}

	private String getAdditionalText ( int index )
	{
		switch ( thumbType )
		{
			case PRICE:
				return "$" + index;

			case LBS:
				return index + " lbs";
				
			case MINUTES:
				return index + " m";

			case INCH:
				return inchToFoot(index);

			default:
				return index + "";
		}
	}

	private String getText ( int index )
	{
		switch ( thumbType )
		{
			case PRICE:
				return "$" + index;
				
			case MINUTES:
				return index + " m";

			case INCH:
				return  inchToFoot(index);

			default:
				return index + "";
		}
	}

	private void init ( Context ctx, float y, int thumbColorNormal, int thumbColorPressed, float thumbRadiusDP, int thumbImageNormal, int thumbImagePressed )
	{
		final Resources res = ctx.getResources ();
		this.context = ctx;
		this.thumbImageNormal = thumbImageNormal;
		this.thumbImagePressed = thumbImagePressed;
		mImageNormal = BitmapFactory.decodeResource ( res, thumbImageNormal );
		mImagePressed = BitmapFactory.decodeResource ( res, thumbImagePressed );

		// If any of the attributes are set, toggle bitmap off

		if ( thumbRadiusDP == -1 && thumbColorNormal == -1 && thumbColorPressed == -1 )
		{

			mUseBitmap = true;

		} else
		{

			mUseBitmap = false;

			// If one of the attributes are set, but the others aren't, set the
			// attributes to default
			if ( thumbRadiusDP == -1 )
				mThumbRadiusPx = TypedValue.applyDimension ( TypedValue.COMPLEX_UNIT_DIP, DEFAULT_THUMB_RADIUS_DP, res.getDisplayMetrics () );
			else
				mThumbRadiusPx = TypedValue.applyDimension ( TypedValue.COMPLEX_UNIT_DIP, thumbRadiusDP, res.getDisplayMetrics () );

			if ( thumbColorNormal == -1 )
				mThumbColorNormal = DEFAULT_THUMB_COLOR_NORMAL;
			else
				mThumbColorNormal = thumbColorNormal;

			if ( thumbColorPressed == -1 )
				mThumbColorPressed = DEFAULT_THUMB_COLOR_PRESSED;
			else
				mThumbColorPressed = thumbColorPressed;

			// Creates the paint and sets the Paint values
			mPaintNormal = new Paint ();
			mPaintNormal.setColor ( mThumbColorNormal );
			mPaintNormal.setAntiAlias ( true );

			mPaintPressed = new Paint ();
			mPaintPressed.setColor ( mThumbColorPressed );
			mPaintPressed.setAntiAlias ( true );
		}

		mHalfWidthNormal = mImageNormal.getWidth () / 2f;
		mHalfHeightNormal = mImageNormal.getHeight () / 2f;

		mHalfWidthPressed = mImagePressed.getWidth () / 2f;
		mHalfHeightPressed = mImagePressed.getHeight () / 2f;

		Log.d ( "Thumb", mHalfWidthPressed + " " + "mHalfWidthNormal" );

		// Sets the minimum touchable area, but allows it to expand based on
		// image size
		int targetRadius = ( int ) Math.max ( MINIMUM_TARGET_RADIUS_DP, thumbRadiusDP );

		mTargetRadiusPx = TypedValue.applyDimension ( TypedValue.COMPLEX_UNIT_DIP, targetRadius, res.getDisplayMetrics () );

		mX = mHalfWidthNormal;
		mY = y;
	}

	// Package-Private Methods /////////////////////////////////////////////////

	float getHalfWidth ()
	{
		return mHalfWidthNormal;
	}

	float getHalfHeight ()
	{
		return mHalfHeightNormal;
	}

	void setX ( float x )
	{
		mX = x;
	}

	float getX ()
	{
		return mX;
	}

	boolean isPressed ()
	{
		return mIsPressed;
	}

	void press ()
	{
		mIsPressed = true;
	}

	void release ()
	{
		mIsPressed = false;
	}

	/**
	 * Determines if the input coordinate is close enough to this thumb to
	 * consider it a press.
	 * 
	 * @param x
	 *            the x-coordinate of the user touch
	 * @param y
	 *            the y-coordinate of the user touch
	 * @return true if the coordinates are within this thumb's target area;
	 *         false otherwise
	 */
	boolean isInTargetZone ( float x, float y )
	{

		if ( Math.abs ( x - mX ) <= mTargetRadiusPx && Math.abs ( y - mY ) <= mTargetRadiusPx )
		{
			return true;
		}
		return false;
	}

	/**
	 * Draws this thumb on the provided canvas.
	 * 
	 * @param canvas
	 *            Canvas to draw on; should be the Canvas passed into {#link
	 *            View#onDraw()}
	 */
	public Bitmap drawTextToBitmap ( Context gContext, int gResId, int index, boolean ifDrawTop )
	{
		String thumbTextIndex = getText ( index );

		Resources resources = gContext.getResources ();
		float scale = resources.getDisplayMetrics ().density;
		Bitmap bitmap = BitmapFactory.decodeResource ( resources, gResId );
		android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig ();
		if ( bitmapConfig == null )
		{
			bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		}
		bitmap = bitmap.copy ( bitmapConfig, true );
		Canvas canvas = new Canvas ( bitmap );

		// drawText is centerThumb
		Paint paint = new Paint ( Paint.ANTI_ALIAS_FLAG );
		paint.setColor ( Color.WHITE );
		paint.setTextSize ( ( int ) ( 9 * scale ) );
		Rect boundsText = new Rect ();
		paint.getTextBounds ( thumbTextIndex, 0, thumbTextIndex.length (), boundsText );
		int x = ( bitmap.getWidth () - boundsText.width () ) / 2;
		int y = ( bitmap.getHeight () + boundsText.height () ) / 2;

		canvas.drawText ( thumbTextIndex, x, y, paint );

		if ( ifDrawTop )
		{
			String thumbAddiditionalTextIndex = getAdditionalText ( index );
			paint = new Paint ( Paint.ANTI_ALIAS_FLAG );
			paint.setColor ( Color.WHITE );
			paint.setTextSize ( ( int ) ( 14 * scale ) );
			boundsText = new Rect ();
			paint.getTextBounds ( thumbAddiditionalTextIndex, 0, thumbAddiditionalTextIndex.length (), boundsText );
			x = ( bitmap.getWidth () - boundsText.width () ) / 2;
			y = ( int ) ( 26 * scale );

			canvas.drawText ( thumbAddiditionalTextIndex, x, y, paint );
		}

		return bitmap;
	}

	public void setTrumbIndex ( int index )
	{
		this.index = index;

	}

	void draw ( Canvas canvas )
	{

		// If a bitmap is to be printed. Determined by thumbRadius attribute.
		if ( mUseBitmap )
		{

			final Bitmap bitmap = ( mIsPressed ) ? mImagePressed : mImageNormal;

			if ( mIsPressed )
			{
				final float topPressed = mY - mHalfHeightPressed;
				final float leftPressed = mX - mHalfWidthPressed;
				canvas.drawBitmap ( drawTextToBitmap ( context, ( mIsPressed ) ? thumbImagePressed : thumbImageNormal, index, mIsPressed ), leftPressed, topPressed, null );
			} else
			{
				final float topNormal = mY - mHalfHeightNormal;
				final float leftNormal = mX - mHalfWidthNormal;
				canvas.drawBitmap ( drawTextToBitmap ( context, ( mIsPressed ) ? thumbImagePressed : thumbImageNormal, index, mIsPressed ), leftNormal, topNormal, null );
			}

		} else
		{

			// Otherwise use a circle to display.
			if ( mIsPressed )
				canvas.drawCircle ( mX, mY, mThumbRadiusPx, mPaintPressed );
			else
				canvas.drawCircle ( mX, mY, mThumbRadiusPx, mPaintNormal );
		}
	}
}
