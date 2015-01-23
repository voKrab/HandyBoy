package com.vallverk.handyboy.view.registration;

import java.io.IOException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.controller.RegistrationController;

public class RegistrationServiceSelphieViewFragment extends BaseFragment
{
	private View backImageView;
	private View backTextView;
	private RegistrationController registrationController;
	private Button nextButton;
	private ImageView selphieImageView;
	private Bitmap selphieBitmap;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.registration_service_selphie_layout, null );
		
		backImageView = view.findViewById ( R.id.backImageView );
		backTextView = view.findViewById ( R.id.backTextView );
		nextButton = ( Button ) view.findViewById ( R.id.nextButton );
		selphieImageView = ( ImageView ) view.findViewById ( R.id.selphieImageView );
		
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
		selphieBitmap = registrationController.getSelphie ();
		if ( selphieBitmap != null )
		{
			selphieImageView.setImageBitmap ( selphieBitmap );
		}
	}

	private void addListeners ()
	{
		selphieImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				chooseImage ( MediaEditorType.AVIARY, ChooseType.CAMERA );
			}
		});
		nextButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				next ();
			}
		});
		backImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				registrationController.prevStep ();
			}
		});
		backTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				registrationController.prevStep ();
			}
		});
	}

	protected void next ()
	{
		if ( selphieBitmap == null )
		{
			Toast.makeText ( controller, R.string.please_make_selphie, Toast.LENGTH_LONG ).show ();
			return;
		}
		registrationController.setSelfie ( selphieBitmap ); 
		registrationController.nextStep ();
	}
	
	@Override
	protected void photoFromEditor ( Uri uri )
	{
		try
		{
			final Bitmap bitmap = Tools.decodeSampledBitmapFromResource ( MainActivity.MAX_POSTED_IMAGE_WIDTh, MainActivity.MAX_POSTED_IMAGE_HEIGHT, getActivity ().getContentResolver (), uri );
			selphieBitmap = bitmap;
			selphieImageView.setImageBitmap ( bitmap );
		} catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
	}
}