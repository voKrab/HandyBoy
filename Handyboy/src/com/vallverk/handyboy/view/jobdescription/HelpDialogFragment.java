package com.vallverk.handyboy.view.jobdescription;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

import com.vallverk.handyboy.R;

public class HelpDialogFragment extends DialogFragment
{
	@Override
	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		getDialog ().getWindow ().requestFeature ( Window.FEATURE_NO_TITLE );
		View layout = inflater.inflate ( R.layout.prof_file_help_layout, container, false );
		return layout;
	}

	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );

		Window window = getDialog ().getWindow ();
		WindowManager.LayoutParams attributes = window.getAttributes ();
		// must setBackgroundDrawable(TRANSPARENT) in onActivityCreated()
		window.getAttributes ().windowAnimations = R.style.dialog_animation_fade;
		window.setBackgroundDrawable ( new ColorDrawable ( Color.TRANSPARENT ) );
		window.setLayout ( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
		window.setDimAmount ( 0 );
	}
}