package com.vallverk.handyboy.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.controller.RegistrationController;

public class HelpViewFragment extends BaseFragment
{
	private View backImageView;
	private View backTextView;
	private Button nextButton;
	private RegistrationController registrationController;
	private CheckBox acceptCheckBox;
	private WebView webview;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.help_layout, null );
		
		backImageView = view.findViewById ( R.id.backImageView );
		backTextView = view.findViewById ( R.id.backTextView );
		webview = ( WebView ) view.findViewById ( R.id.webView );
		
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new MyWebViewClient());
				
		return view;
	}
	
	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		
		addListeners ();
	}

	@Override
	public Animation onCreateAnimation ( int transit, boolean enter, int nextAnim )
	{
		Animation anim;
		if ( enter )
		{
			anim = AnimationUtils.loadAnimation ( getActivity (), R.anim.right_to_center );
			anim.setAnimationListener ( new AnimationListener ()
			{
				public void onAnimationEnd ( Animation animation )
				{
					updateComponents ();
				}

				public void onAnimationRepeat ( Animation animation )
				{
				}

				public void onAnimationStart ( Animation animation )
				{
				}
			} );
		} else
		{
			anim = AnimationUtils.loadAnimation ( getActivity (), R.anim.center_to_right );
		}
		return anim;
	}
	
	private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }
    }
	
	private void updateComponents ()
	{
		webview.loadUrl ( "https://handyboy.zendesk.com/hc/en-us/categories/200210955-FAQ" );
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