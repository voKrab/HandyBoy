package com.vallverk.handyboy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;

import com.vallverk.handyboy.model.AddressWraper;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Oleg Barkov
 * 
 *         A set of tools for different tasks
 */
public class Tools
{

	public static Date shiftTimeZone ( Date date, TimeZone sourceTimeZone, TimeZone targetTimeZone )
	{
		Calendar sourceCalendar = Calendar.getInstance ();
		sourceCalendar.setTime ( date );
		sourceCalendar.setTimeZone ( sourceTimeZone );

		Calendar targetCalendar = Calendar.getInstance ();
		for ( int field : new int[] { Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND } )
		{
			targetCalendar.set ( field, sourceCalendar.get ( field ) );
		}
		targetCalendar.setTimeZone ( targetTimeZone );
		return targetCalendar.getTime ();
	}

	public static Location getLastKnownLoaction ( Context context )
	{
		LocationManager manager = ( LocationManager ) context.getSystemService ( Context.LOCATION_SERVICE );
		Location location = null;
		List < String > providers = manager.getProviders ( false );
		for ( String provider : providers )
		{
			location = manager.getLastKnownLocation ( provider );
			// maybe try adding some Criteria here
			if ( location != null )
				return location;
		}
		// at this point we've done all we can and no location is returned
		return null;
	}

	public static Location getLocation ( Context context )
	{
		// String locationProvider = LocationManager.NETWORK_PROVIDER;
		// locationManager.requestLocationUpdates(locationProvider, 0, 0,
		// locationListener);

		LocationManager locationManager = ( LocationManager ) context.getSystemService ( Activity.LOCATION_SERVICE );
		// Criteria criteria = new Criteria ();
		// String bestProvider = locationManager.getBestProvider ( criteria,
		// false );
		// Location location = locationManager.getLastKnownLocation (
		// bestProvider );

		boolean isGPSEnabled = locationManager.isProviderEnabled ( LocationManager.GPS_PROVIDER );
		if ( isGPSEnabled )
		{
			String locationProvider = LocationManager.GPS_PROVIDER;
			Location location = locationManager.getLastKnownLocation ( locationProvider );
			return location;
		} else
		{
			showSettingsAlert ( context );
		}
		return null;
	}

	public static void showSettingsAlert ( final Context context )
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder ( context );

		alertDialog.setTitle ( "GPS Settings" );
		alertDialog.setMessage ( "GPS is not enabled. Do you want to go to settings menu?" );

		alertDialog.setPositiveButton ( "Settings", new DialogInterface.OnClickListener ()
		{
			public void onClick ( DialogInterface dialog, int which )
			{
				Intent intent = new Intent ( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
				context.startActivity ( intent );
			}
		} );
		alertDialog.setNegativeButton ( "Cancel", new DialogInterface.OnClickListener ()
		{
			public void onClick ( DialogInterface dialog, int which )
			{
				dialog.cancel ();
			}
		} );
		alertDialog.show ();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public static int sizeOf ( Bitmap data )
	{
		if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1 )
		{
			return data.getRowBytes () * data.getHeight () / 4;
		} else
		{
			return data.getByteCount () / 4;
		}
	}

	public static float sizeOfInMB ( Bitmap data )
	{
		return sizeOf ( data ) / ( float ) ( 1024 * 1024 );
	}

	@TargetApi(11)
	static public < T > void executeAsyncTask ( AsyncTask < T, ?, ? > task, T... params )
	{
		if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
		{
			task.executeOnExecutor ( AsyncTask.THREAD_POOL_EXECUTOR, params );
		} else
		{
			task.execute ( params );
		}
	}

	public static Bitmap fitToBounds ( Bitmap image, int widthToFit )
	{
		int photoWidth = image.getWidth ();
		int photoHeight = image.getHeight ();
		float scale = widthToFit / ( float ) Math.min ( photoWidth, photoHeight );
		int scaledWidth;
		int scaledHeight;
		if ( photoWidth < photoHeight )
		{
			scaledWidth = widthToFit;
			scaledHeight = ( int ) ( photoHeight * scale );
		} else
		{
			scaledWidth = ( int ) ( photoWidth * scale );
			scaledHeight = widthToFit;
		}
		return Bitmap.createScaledBitmap ( image, scaledWidth, scaledHeight, true );
	}

	public static Bitmap fitToBoundsUseMinimumLength ( Bitmap image, int widthToFit )
	{
		int photoWidth = image.getWidth ();
		int photoHeight = image.getHeight ();
		if ( photoHeight == widthToFit && photoWidth == widthToFit )
		{
			return image;
		}

		int scaledWidth;
		int scaledHeight;
		if ( photoWidth < photoHeight )
		{
			float ratio = photoHeight / ( float ) photoWidth;
			scaledWidth = ( int ) ( widthToFit / ratio );
			scaledHeight = widthToFit;
		} else
		{
			float ratio = photoWidth / ( float ) photoHeight;
			scaledWidth = widthToFit;
			scaledHeight = ( int ) ( widthToFit / ratio );
		}
		return Bitmap.createScaledBitmap ( image, scaledWidth, scaledHeight, true );
	}

	public static void setListViewHeightBasedOnChildren ( ListView listView )
	{
		android.widget.ListAdapter listAdapter = listView.getAdapter ();
		if ( listAdapter == null )
		{
			// pre-condition
			return;
		}

		int totalHeight = listView.getPaddingTop () + listView.getPaddingBottom ();
		for ( int i = 0; i < listAdapter.getCount (); i++ )
		{
			View listItem = listAdapter.getView ( i, null, listView );
			if ( listItem instanceof ViewGroup )
			{
				listItem.setLayoutParams ( new LayoutParams ( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
			}
			listItem.measure ( 0, 0 );
			totalHeight += listItem.getMeasuredHeight ();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams ();
		params.height = totalHeight + ( listView.getDividerHeight () * ( listAdapter.getCount () - 1 ) );
		listView.setLayoutParams ( params );
	}

	// public static Bitmap getRealSizeImage ( int id, Context context )
	// {
	// List < RealSizeImageData > data = ( List < RealSizeImageData > )
	// FileManager.loadObject ( context, FileManager.REAL_SIZE_IMAGES_DATA_PATH,
	// DefaultObjectFactory.getRealSizeData () );
	// for ( RealSizeImageData realSizeImageData : data )
	// {
	// if ( realSizeImageData.newsId == id )
	// {
	// return realSizeImageData.image.bitmap;
	// }
	// }
	// return null;
	// }

	public static int calculateInSampleSize ( BitmapFactory.Options options, int reqWidth, int reqHeight )
	{
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if ( height > reqHeight || width > reqWidth )
		{

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			inSampleSize *= 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ( ( halfHeight / inSampleSize ) > reqHeight && ( halfWidth / inSampleSize ) > reqWidth )
			{
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource ( Resources res, int resId, int reqWidth, int reqHeight )
	{

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options ();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource ( res, resId, options );

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize ( options, reqWidth, reqHeight );

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource ( res, resId, options );
	}

	public static Bitmap decodeSampledBitmapFromResource ( String file, int reqWidth, int reqHeight )
	{
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options ();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile ( file, options );

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize ( options, reqWidth, reqHeight );

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile ( file, options );
	}

	public static Bitmap decodeSampledBitmapFromResource ( int reqWidth, int reqHeight, ContentResolver contentResolver, Uri selectedImage ) throws IOException
	{
		// First decode with inJustDecodeBounds=true to check dimensions
		InputStream imageStream = contentResolver.openInputStream ( selectedImage );
		final BitmapFactory.Options options = new BitmapFactory.Options ();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream ( imageStream, null, options );

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize ( options, reqWidth, reqHeight );

		// Decode bitmap with inSampleSize set
		imageStream = contentResolver.openInputStream ( selectedImage );
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeStream ( imageStream, null, options );
		bitmap = fitToBoundsUseMinimumLength ( bitmap, MainActivity.MAX_POSTED_IMAGE_WIDTh ); // temp
		return bitmap;
	}

	private static Bitmap normalize ( Bitmap bitmap )
	{
		Bitmap normilizedBitmap = bitmap;
		if ( bitmap.getWidth () > bitmap.getHeight () )
		{
			Matrix matrix = new Matrix ();
			matrix.postRotate ( -90 );
			normilizedBitmap = Bitmap.createBitmap ( bitmap, 0, 0, bitmap.getWidth (), bitmap.getHeight (), matrix, true );
		}
		return normilizedBitmap;
	}

	public static String getTimeAgo ( Long time )
	{
		// EN localization
		// int timeAgoInSeconds = ( int ) ( ( System.currentTimeMillis () - time
		// ) / 1000 );
		// String timeAgo = "";
		// int mitutesAgo = timeAgoInSeconds / 60;
		// int hoursAgo = timeAgoInSeconds / 3600;
		// int daysAgo = hoursAgo / 24;
		// int weeksAgo = daysAgo / 7;
		// if ( weeksAgo > 0 )
		// {
		// timeAgo = weeksAgo + " week" + getSuffiks ( weeksAgo ) + " ago";
		// } else if ( daysAgo > 0 )
		// {
		// timeAgo = daysAgo + " day" + getSuffiks ( daysAgo ) + " ago";
		// } else if ( hoursAgo > 0 )
		// {
		// timeAgo = hoursAgo + " hour" + getSuffiks ( hoursAgo ) + " ago";
		// } else if ( mitutesAgo > 0 )
		// {
		// timeAgo = mitutesAgo + " minute" + getSuffiks ( mitutesAgo ) +
		// " ago";
		// } else
		// {
		// timeAgo = "just now";
		// }
		// return timeAgo;

		// RU localization
		int timeAgoInSeconds = ( int ) ( ( System.currentTimeMillis () - time ) / 1000 );
		String timeAgo = "";
		int mitutesAgo = timeAgoInSeconds / 60;
		int hoursAgo = timeAgoInSeconds / 3600;
		int daysAgo = hoursAgo / 24;
		int weeksAgo = daysAgo / 7;
		if ( weeksAgo > 0 )
		{
			if ( weeksAgo > 4 )
			{
				timeAgo = "больше месяца";
			} else
			{
				timeAgo = weeksAgo + " нед. назад";
			}
		} else if ( daysAgo > 0 )
		{
			timeAgo = daysAgo + " д. назад";
		} else if ( hoursAgo > 0 )
		{
			timeAgo = hoursAgo + " ч. назад";
		} else if ( mitutesAgo > 0 )
		{
			timeAgo = mitutesAgo + " м. назад";
		} else
		{
			timeAgo = "меньше минуты";
		}
		return timeAgo;
	}

	public static String getSuffiks ( int timeAgo )
	{
		return timeAgo == 1 ? "" : "s";
	}

	public static int fromDPToPX ( int dp, Context activity )
	{
		Resources r = activity.getResources ();
		float px = TypedValue.applyDimension ( TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics () );
		return ( int ) px;
	}

	public static Bitmap cropToRect ( Bitmap bitmap, int width )
	{
		bitmap = fitToBounds ( bitmap, width );
		Bitmap cropedBitmap = Bitmap.createBitmap ( bitmap, ( bitmap.getWidth () - width ) / 2, ( bitmap.getHeight () - width ) / 2, width, width );
		return cropedBitmap;
	}

	public static boolean isEmpty ( String text )
	{
		return text == null || text.isEmpty ();
	}

	public static float getFileSizeInBM ( Uri fileUri ) throws Exception
	{
		InputStream inputStream = MainActivity.getInstance ().getContentResolver ().openInputStream ( fileUri );
		byte[] data = IOUtils.toByteArray ( inputStream );
		return data.length / ( 1024 * 1024 );
	}

	public static float getFileSize ( Uri fileUri ) throws Exception
	{
		InputStream inputStream = MainActivity.getInstance ().getContentResolver ().openInputStream ( fileUri );
		byte[] data = IOUtils.toByteArray ( inputStream );
		return data.length;
	}

	public static String printKeyHash ( Activity context )
	{
		PackageInfo packageInfo;
		String key = null;
		try
		{
			// getting application package name, as defined in manifest
			String packageName = context.getApplicationContext ().getPackageName ();

			// Retriving package info
			packageInfo = context.getPackageManager ().getPackageInfo ( packageName, PackageManager.GET_SIGNATURES );

			Log.e ( "Package Name=", context.getApplicationContext ().getPackageName () );

			for ( Signature signature : packageInfo.signatures )
			{
				MessageDigest md = MessageDigest.getInstance ( "SHA" );
				md.update ( signature.toByteArray () );
				key = new String ( Base64.encode ( md.digest (), 0 ) );

				// String key = new String(Base64.encodeBytes(md.digest()));
				Log.e ( "Key Hash=", key );

			}
		} catch ( NameNotFoundException e1 )
		{
			Log.e ( "Name not found", e1.toString () );
		}

		catch ( NoSuchAlgorithmException e )
		{
			Log.e ( "No such an algorithm", e.toString () );
		} catch ( Exception e )
		{
			Log.e ( "Exception", e.toString () );
		}

		return key;
	}

	public static String getTimeAM_PM ( long time )
	{
		String delegate = "hh:mm aaa";
		String formatedDate = ( String ) DateFormat.format ( delegate, time );
		return formatedDate;
	}

	public static String getLocationText ( double latitude, double longitude )
	{
		String locationText = "";
		try
		{
			Geocoder gcd = new Geocoder ( MainActivity.getInstance ().getBaseContext (), Locale.getDefault () );
			Address location = gcd.getFromLocation ( latitude, longitude, 1 ).get ( 0 );
			AddressWraper wraper = new AddressWraper ( location );
			locationText = wraper.toString ();
		} catch ( Exception e )
		{
			e.printStackTrace ();
		}
		return locationText;
	}

	public static String toSimpleString ( Date date )
	{
		SimpleDateFormat format = new SimpleDateFormat ( "dd.MM.yyyy" );
		String reportDate = format.format ( date );
		return reportDate;
	}

	public static String toDateString ( Date date )
	{
		SimpleDateFormat format = new SimpleDateFormat ( "EEEE, MMMM, dd" );
		String reportDate = format.format ( date );
		return reportDate;
	}

	public static String toDateString ( String dateString )
	{
		return toDateString ( fromStringToDate ( dateString ) );
	}

	public static Date fromStringToDate ( String dateString )
	{
		return fromStringToDate ( dateString, "dd.MM.yyyy" );
	}
	
	public static String fromDateToString ( Date date, String dateFormat )
	{
		SimpleDateFormat format = new SimpleDateFormat ( dateFormat );
		return format.format ( date );
	}
	
	public static Date fromStringToDate ( String dateString, String dateFormat )
	{
		SimpleDateFormat format = new SimpleDateFormat ( dateFormat );
		try
		{
			Date date = format.parse ( dateString );
			return date;
		} catch ( Exception e )
		{
			e.printStackTrace ();
			return null;
		}

	}

	public static String fromTimestampToString ( long timeStamp )
	{
		return fromTimestampToString ( timeStamp, "dd/MM/yyyy" );
	}

	public static String fromTimestampToString ( long timeStamp, String sformat )
	{
		SimpleDateFormat format = new SimpleDateFormat ( sformat );
		Date netDate =  new Date ( timeStamp );
		return format.format ( netDate );
	}

    public static String getYouTubeId(String url){
        if(url.contains("youtu.be")){
            return url.substring(url.lastIndexOf("/") + 1, url.length());
        }else{
            String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(url);

            if(matcher.find()){
                return matcher.group();
            }
        }
        return "";
    }

    public static String getVideoImagePreview(String youTubeId){
        return  "http://img.youtube.com/vi/" + youTubeId + "/hqdefault.jpg";
    }

    public static String getYoutubeVideoUrl(String youTubeId){
        return  "http://youtu.be/" + youTubeId;
    }

    public static String inchToFoot ( int inches )
    {
        int feet = inches / 12;
        int leftover = inches % 12;
        return feet + "." + leftover;
    }

    public static int getInches ( int height )
    {
        return height % 12;
    }

    public static int getFeets ( int height )
    {
        return height / 12;
    }
}
