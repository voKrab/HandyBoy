package com.vallverk.handyboy.view.base;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.vallverk.handyboy.R;

public class BitmapPreviewDialogFragment extends DialogFragment
{
	private ImageView bitmapImageView;
	private ImageView closeImageView;
	private Bitmap bitmap;

	public BitmapPreviewDialogFragment ( Bitmap bitmap )
	{
		this.bitmap = bitmap;
	}
	
	@Override
	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		getDialog ().getWindow ().requestFeature ( Window.FEATURE_NO_TITLE );
		View layout = inflater.inflate ( R.layout.bitmap_preview_layout, container, false );
		bitmapImageView = ( ImageView ) layout.findViewById ( R.id.bitmapImageView );
		closeImageView = ( ImageView ) layout.findViewById ( R.id.closeImageView );
		return layout;
	}

	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );

		Window window = getDialog ().getWindow ();
		WindowManager.LayoutParams attributes = window.getAttributes ();
		// must setBackgroundDrawable(TRANSPARENT) in onActivityCreated()
		//window.getAttributes ().windowAnimations = R.style.dialog_animation_fade;
		window.setBackgroundDrawable ( new ColorDrawable ( Color.TRANSPARENT ) );
		window.setLayout ( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT );
		//window.setDimAmount ( 0 );
		
		updateComponents ();
		addListeners ();
	}

	private void updateComponents ()
	{
		bitmapImageView.setImageBitmap ( bitmap );
	}

	private void addListeners ()
	{
		closeImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				dismiss ();
			}
		});
	}
}
