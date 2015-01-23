package com.vallverk.handyboy.view.base;

import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.Tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;

public class BitmapUtils
{
	public static Bitmap getRoundedBitmap ( Bitmap bitmap, int width, int height )
	{
		// final Bitmap output = Bitmap.createBitmap ( bitmap.getWidth (),
		// bitmap.getHeight (), Bitmap.Config.ARGB_8888 );
		final Bitmap output = Bitmap.createBitmap ( width, height, Bitmap.Config.ARGB_8888 );

		final Canvas canvas = new Canvas ( output );

		final int color = Color.RED;
		final Paint paint = new Paint ();
		final Rect rect = new Rect ( 0, 0, width, height );
		final RectF rectF = new RectF ( rect );

		paint.setAntiAlias ( true );
		canvas.drawARGB ( 0, 0, 0, 0 );
		paint.setColor ( color );
		canvas.drawOval ( rectF, paint );

		int w = bitmap.getWidth ();
		int h = bitmap.getHeight ();
		int x = ( w - width ) / 2;
		int y = ( h - height ) / 2;

		final Rect rect2 = new Rect ( x, y, x + width, y + height );

		paint.setXfermode ( new PorterDuffXfermode ( Mode.SRC_IN ) );
		canvas.drawBitmap ( bitmap, rect2, rect, paint );

		bitmap.recycle ();

		return output;
	}

	public static void lowerBitmapSize ( Bitmap bitmap, float maxSize )
	{
		float size = Tools.sizeOfInMB ( bitmap );
		Bitmap scaledPhoto = null;
		float tempScale = 1f;
		float step = 1.1f;
		while ( size > maxSize )
		{
			tempScale *= step;
			int newsWidth = ( int ) ( bitmap.getWidth () / tempScale );
			int newsHeight = ( int ) ( bitmap.getHeight () / tempScale );
			scaledPhoto = Bitmap.createScaledBitmap ( bitmap, newsWidth, newsHeight, true );
			size = Tools.sizeOfInMB ( scaledPhoto );
		}
		if ( scaledPhoto != null )
		{
			bitmap = scaledPhoto;
		}
	}

	@SuppressLint("NewApi")
	public static void blur ( Bitmap bitmap, Context context )
	{
		final RenderScript rs = RenderScript.create ( context );
		final Allocation input = Allocation.createFromBitmap ( rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT );
		final Allocation output = Allocation.createTyped ( rs, input.getType () );
		final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create ( rs, Element.U8_4 ( rs ) );
		script.setRadius ( 25f );
		script.setInput ( input );
		script.forEach ( output );
		output.copyTo ( bitmap );
	}

	public static Drawable fromAssets ( String path, Context context )
	{
		java.io.InputStream ims = null;
		try
		{
			ims = context.getAssets ().open ( path + ".png" );
		} catch ( IOException e )
		{
			// TODO Auto-generated catch block
			// e.printStackTrace ();
			// Log.i ( "IMAGE", e.getMessage () );
		}
		// load image as Drawable
		Drawable d = Drawable.createFromStream ( ims, null );
		// set image to ImageView
		return d;
	}

	public static File createFile ( Bitmap bitmap ) throws Exception
	{
		File f = new File ( MainActivity.getInstance ().getCacheDir (), "temp.jpg" );
		f.createNewFile ();

		// Convert bitmap to byte array
		ByteArrayOutputStream bos = new ByteArrayOutputStream ();
		bitmap.compress ( CompressFormat.PNG, 0 /* ignored for PNG */, bos );
		byte[] bitmapdata = bos.toByteArray ();

		// write the bytes in file
		FileOutputStream fos = new FileOutputStream ( f );
		fos.write ( bitmapdata );
		return f;
	}

	public static File createFileToInstagram ( Bitmap bitmap ) throws Exception
	{
		File storageDir = new File ( Environment.getExternalStorageDirectory () + "/DCIM/Camera/fashiongram/" );
		if ( !storageDir.exists () )
		{
			storageDir.createNewFile ();
		}
		File storagePath = new File ( Environment.getExternalStorageDirectory () + "/DCIM/Camera/", "toInstagram.jpg" );
		storagePath.createNewFile ();

		// Convert bitmap to byte array
		ByteArrayOutputStream bos = new ByteArrayOutputStream ();
		bitmap.compress ( CompressFormat.PNG, 0 /* ignored for PNG */, bos );
		byte[] bitmapdata = bos.toByteArray ();

		// write the bytes in file
		FileOutputStream fos = new FileOutputStream ( storagePath );
		fos.write ( bitmapdata );
		return storagePath;
	}
}
