package com.vallverk.handyboy.view.base;

import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FontUtils
{
	public enum FontStyle
	{
		SEMIBOLD ( 0 ), SEMIBOLD_ITALIC ( 1 ), REGULAR ( 2 ), LIGHT ( 3 ), ITALIC ( 4 ), BOLD ( 5 ), BOLD_ITALIC ( 6 ), EXTRA_BOLD ( 7 ), EXTRA_BOLD_ITALIC ( 8 );

		int id;

		FontStyle ( int id )
		{
			this.id = id;
		}

		static FontStyle fromId ( int id )
		{
			for ( FontStyle f : values () )
			{
				if ( f.id == id )
					return f;
			}
			throw new IllegalArgumentException ();
		}
	}

	private static FontUtils instance;
	private Map < FontStyle, Typeface > styles;

	public static FontUtils getInstance ( Context context )
	{
		if ( instance == null )
		{
			instance = new FontUtils ( context );
		}
		return instance;
	}

	public FontUtils ( Context context )
	{
		styles = new HashMap < FontStyle, Typeface > ();
		styles.put ( FontStyle.BOLD, Typeface.createFromAsset ( context.getAssets (), "fonts/OpenSans-Bold.ttf" ) );
		styles.put ( FontStyle.BOLD_ITALIC, Typeface.createFromAsset ( context.getAssets (), "fonts/OpenSans-BoldItalic.ttf" ) );
		styles.put ( FontStyle.EXTRA_BOLD, Typeface.createFromAsset ( context.getAssets (), "fonts/OpenSans-ExtraBold.ttf" ) );
		styles.put ( FontStyle.EXTRA_BOLD_ITALIC, Typeface.createFromAsset ( context.getAssets (), "fonts/OpenSans-ExtraBoldItalic.ttf" ) );
		styles.put ( FontStyle.ITALIC, Typeface.createFromAsset ( context.getAssets (), "fonts/OpenSans-Italic.ttf" ) );
		styles.put ( FontStyle.LIGHT, Typeface.createFromAsset ( context.getAssets (), "fonts/OpenSans-Light.ttf" ) );
		styles.put ( FontStyle.REGULAR, Typeface.createFromAsset ( context.getAssets (), "fonts/OpenSans-Regular.ttf" ) );
		styles.put ( FontStyle.SEMIBOLD, Typeface.createFromAsset ( context.getAssets (), "fonts/OpenSans-Semibold.ttf" ) );
		styles.put ( FontStyle.SEMIBOLD_ITALIC, Typeface.createFromAsset ( context.getAssets (), "fonts/OpenSans-SemiboldItalic.ttf" ) );
	}

	public void applyStyle ( TextView view, FontStyle style )
	{
		view.setTypeface ( styles.get ( style ) );
	}

	public void applyStyle ( EditText view, FontStyle style )
	{
		view.setTypeface ( styles.get ( style ) );
	}

	public void applyStyle ( Button button, FontStyle style )
	{
		button.setTypeface ( styles.get ( style ) );
	}

	public void applyStyle ( ViewGroup group, FontStyle style )
	{
		int count = group.getChildCount ();
		View v;
		for ( int i = 0; i < count; i++ )
		{
			v = group.getChildAt ( i );
			if ( v instanceof TextView )
			{
				applyStyle ( ( TextView ) v, style );
			} else if ( v instanceof ViewGroup )
			{
				applyStyle ( ( ViewGroup ) v, style );
			}
		}
	}
}
