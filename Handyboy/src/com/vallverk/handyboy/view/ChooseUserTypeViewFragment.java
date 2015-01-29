package com.vallverk.handyboy.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.UserStatus;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.controller.RegistrationController;

public class ChooseUserTypeViewFragment extends BaseFragment
{
	private View workerContainer;
	private View customerContainer;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.choose_user_type_layout, null );
		
		workerContainer = view.findViewById ( R.id.workerContainer );
		customerContainer = view.findViewById ( R.id.customerContainer );
								
		return view;
	}
	
	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
        controller.setSwipeEnabled(false);
		addListeners ();
	}

	private void addListeners ()
	{
		final RegistrationController controller = MainActivity.getInstance ().getRegistrationController ();
		workerContainer.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.setUserStatus ( UserStatus.NEW_SERVICE );
				controller.nextStep ();
			}
		});
		customerContainer.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.setUserStatus ( UserStatus.NEW_CUSTOMER );
				controller.nextStep ();
			}
		});
	}
}