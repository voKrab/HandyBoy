package com.vallverk.handyboy.view.registration;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.controller.RegistrationController;

public class RegistrationServiceVideoViewFragment extends BaseFragment
{
	private View backImageView;
	private View backTextView;
	private Button nextButton;
	private RegistrationController registrationController;
	private VideoView videoView;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.registration_service_video_layout, null );

		backImageView = view.findViewById ( R.id.backImageView );
		backTextView = view.findViewById ( R.id.backTextView );
		nextButton = ( Button ) view.findViewById ( R.id.nextButton );
		videoView = ( VideoView ) view.findViewById ( R.id.videoView );

		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );

		registrationController = MainActivity.getInstance ().getRegistrationController ();

		updateComponents ();
		addListeners ();
	}

	private void updateComponents ()
	{
		// String path = "https://www.youtube.com/watch?v=JAdtfGtCnoU";
		// Uri uri = Uri.parse ( path );
		MediaController mediaController = new MediaController ( controller );
		mediaController.setAnchorView ( videoView );
		Uri uri = Uri.parse ( "rtsp://v7.cache3.c.youtube.com/CjgLENy73wIaLwkeUryQ8ZkCqRMYJCAkFEIJbXYtZ29vZ2xlSARSB3Jlc3VsdHNg6KnB9MbH8sVODA==/0/0/0/video.3gp" );
		videoView.setMediaController ( mediaController );
		videoView.setVideoURI ( uri );
		videoView.requestFocus ();
		videoView.start ();
	}

	private void addListeners ()
	{
		nextButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				next ();
			}
		} );
		backImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				registrationController.prevStep ();
			}
		} );
		backTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				registrationController.prevStep ();
			}
		} );
	}

	protected void next ()
	{
		videoView.stopPlayback ();
//		registrationController.nextStep ();
		registrationController.finish ();
	}
}