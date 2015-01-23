package com.vallverk.handyboy.view.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.controller.RegistrationController;

public class RegistrationCreditCardViewFragment extends BaseFragment
{
	private UserAPIObject user;
	private RegistrationController controller;
	private View backImageView;
	private View backTextView;
	private Button nextButton;
	private Button skipButton;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.registration_credit_card_layout, null );

		backImageView = view.findViewById ( R.id.backImageView );
		backTextView = view.findViewById ( R.id.backTextView );
		nextButton = ( Button ) view.findViewById ( R.id.nextButton );
		skipButton = ( Button ) view.findViewById ( R.id.skipButton );
		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onCreate ( savedInstanceState );

		controller = MainActivity.getInstance ().getRegistrationController ();
		user = APIManager.getInstance ().getUser ();
		updateComponents ();
		// setupTestData ();
		addListeners ();
	}

	private void updateComponents ()
	{
		
	}

	protected void addListeners ()
	{
		skipButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				skip ();
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
				controller.prevStep ();
			}
		} );
		backTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.prevStep ();
			}
		} );
	}

	protected void skip ()
	{
		controller.finish ();
	}

	protected void next ()
	{
		Toast.makeText ( getActivity (), "Not implemented", Toast.LENGTH_LONG ).show ();
//		if ( jobType == null )
//		{
//			Toast.makeText ( getActivity (), R.string.choose_job_type, Toast.LENGTH_LONG ).show ();
//			return;
//		}
//		String serviceDescription = descriptionEditText.getText ().toString ().trim ();
//		if ( serviceDescription.isEmpty () )
//		{
//			Toast.makeText ( getActivity (), R.string.describe_your_services, Toast.LENGTH_LONG ).show ();
//			return;
//		}
//		if ( jobType.isRequiredApprove () )
//		{
//			if ( proofFile == null )
//			{
//				Toast.makeText ( getActivity (), R.string.upload_proof_of_current_personal_trainers_insurance, Toast.LENGTH_LONG ).show ();
//				return;
//			}
//		}
//		controller.setServiceDescription ( serviceDescription );
//		controller.setJobType ( jobType );
//		controller.setServicePrice ( priceSeekBar.getProgress () + jobType.getMinCost () );
//		if ( jobType.isRequiredApprove () )
//		{
//			controller.setApproveFile ( proofFile );
//		}
//		controller.nextStep ();
	}
}