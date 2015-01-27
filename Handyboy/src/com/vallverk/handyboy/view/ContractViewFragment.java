package com.vallverk.handyboy.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.controller.RegistrationController;

public class ContractViewFragment extends BaseFragment
{
	private View backImageView;
	private View backTextView;
	private Button nextButton;
	private RegistrationController registrationController;
	private CheckBox acceptCheckBox;
	private WebView webview;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.contract_layout, null );
		
		backImageView = view.findViewById ( R.id.backImageView );
		backTextView = view.findViewById ( R.id.backTextView );
		webview = ( WebView ) view.findViewById ( R.id.webView );
				
		return view;
	}
	
	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
				
		addListeners ();
	}

    @Override
    protected void init() {
        updateComponents();
    }

    private void updateComponents ()
	{
		webview.loadUrl ( "file:///android_asset/HandyBoy_Contract.html" );
	}

	private void addListeners ()
	{
		backImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.onBackPressed ();
			}
		});
		backTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.onBackPressed ();
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