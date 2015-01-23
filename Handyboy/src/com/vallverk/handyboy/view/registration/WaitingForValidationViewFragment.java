package com.vallverk.handyboy.view.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.view.base.BaseFragment;

public class WaitingForValidationViewFragment extends BaseFragment
{
	private TextView okayButton;
	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.waiting_for_validation_layout, null );
		okayButton = (TextView) view.findViewById ( R.id.okayButton );
		okayButton.setOnClickListener ( new OnClickListener()
		{
			@Override
			public void onClick ( View v )
			{
				controller.setState ( VIEW_STATE.EXIT );
			}
		} );
		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
	}
}