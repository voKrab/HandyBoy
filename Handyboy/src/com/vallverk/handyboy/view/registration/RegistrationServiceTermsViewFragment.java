package com.vallverk.handyboy.view.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.controller.RegistrationController;

public class RegistrationServiceTermsViewFragment extends BaseFragment
{
	private View backImageView;
	private View backTextView;
	private Button nextButton;
	private RegistrationController registrationController;
	private CheckBox acceptCheckBox;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.registration_service_terms_layout, null );
		
		backImageView = view.findViewById ( R.id.backImageView );
		backTextView = view.findViewById ( R.id.backTextView );
		nextButton = ( Button ) view.findViewById ( R.id.nextButton );
		acceptCheckBox = ( CheckBox ) view.findViewById ( R.id.acceptCheckBox );
				
		return view;
	}
	
	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		
		registrationController = MainActivity.getInstance ().getRegistrationController ();
		
		addListeners ();
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
		if ( !acceptCheckBox.isChecked () )
		{
			Toast.makeText ( controller, "Please accept contract", Toast.LENGTH_LONG ).show ();
			return;
		}
		registrationController.nextStep ();
	}
}