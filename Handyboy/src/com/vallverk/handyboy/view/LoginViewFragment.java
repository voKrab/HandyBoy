package com.vallverk.handyboy.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.facebook.FacebookManager;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.view.base.BaseFragment;

public class LoginViewFragment extends BaseFragment
{
	private Button loginWithFacebookButton;
	private EditText emailEditText;
	private EditText passwordEditText;
	private TextView forgotPasswordTextView;
	private Button signinButton;
	private Button registrationButton;
	private Button skipButton;
	private FacebookManager facebook;
	private View mainContainer;

	int i = 0;
    private Bundle savedInstanceState;

    public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.login_layout, null );

		loginWithFacebookButton = ( Button ) view.findViewById ( R.id.loginWithFacebookButton );
		emailEditText = ( EditText ) view.findViewById ( R.id.emailEditText );
		passwordEditText = ( EditText ) view.findViewById ( R.id.passwordEditText );
		forgotPasswordTextView = ( TextView ) view.findViewById ( R.id.forgotPasswordTextView );
		signinButton = ( Button ) view.findViewById ( R.id.signinButton );
		registrationButton = ( Button ) view.findViewById ( R.id.registrationButton );
		skipButton = ( Button ) view.findViewById ( R.id.skipButton );
		mainContainer = view.findViewById ( R.id.mainContainer );

		return view;
	}

	@Override
	protected void init ()
	{
		facebook = new FacebookManager ();
		facebook.init ( savedInstanceState, this );

		addListeners ();
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
        this.savedInstanceState = savedInstanceState;
	}

	private void addListeners ()
	{
		getView ().setOnLongClickListener ( new OnLongClickListener ()
		{
			@Override
			public boolean onLongClick ( View v )
			{
				emailEditText.setText ( "vokrab@gmail.com" );
				passwordEditText.setText ( "123456" );
				controller.setDebugMode ( true );
				return false;
			}
		} );

		getView ().setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				// TODO Auto-generated method stub
				i++;
				Handler handler = new Handler ();
				Runnable r = new Runnable ()
				{

					@Override
					public void run ()
					{
						i = 0;
					}
				};

				if ( i == 1 )
				{
					// Single click
					handler.postDelayed ( r, 250 );

				} else if ( i == 2 )
				{
					// Double click
					i = 0;

					emailEditText.setText ( "timm.kasianov@gmail.com" );
					passwordEditText.setText ( "qwerty" );
					controller.setDebugMode ( true );
				}

			}
		} );

		mainContainer.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.hideKeyboard ();
			}
		} );

		forgotPasswordTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				MainActivity.getInstance ().setState ( VIEW_STATE.FORGOT_PASSWORD );
			}
		} );
		loginWithFacebookButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				facebook.login ();
			}
		} );
		signinButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				login ();
			}
		} );
		registrationButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				MainActivity.getInstance ().setState ( VIEW_STATE.REGISTRATION );
			}
		} );
	}

	protected void login ()
	{
		final String email = emailEditText.getText ().toString ().trim ();
		if ( email.isEmpty () )
		{
			Toast.makeText ( getActivity (), R.string.whats_your_email, Toast.LENGTH_LONG ).show ();
			return;
		}
		final String password = passwordEditText.getText ().toString ().trim ();
		if ( password.isEmpty () )
		{
			Toast.makeText ( getActivity (), R.string.what_password_would_you_like_to_use, Toast.LENGTH_LONG ).show ();
			return;
		}
		new AsyncTask < Void, Void, String > ()
		{
			private UserAPIObject user;

			@Override
			public void onPreExecute ()
			{
				super.onPreExecute ();
				MainActivity.getInstance ().showLoader ();
			}

			@Override
			public void onPostExecute ( String result )
			{
				super.onPostExecute ( result );
				MainActivity.getInstance ().hideLoader ();
				if ( result.isEmpty () ) // success login
				{
					APIManager.getInstance ().setUser ( user );
					MainActivity.getInstance ().enter ( false );
				} else
				{
					Toast.makeText ( getActivity (), result, Toast.LENGTH_LONG ).show ();
				}
			}

			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					user = new UserAPIObject ( password, email );
					String loginResult = user.login ();
					if ( !loginResult.isEmpty () )
					{
						result = loginResult;
					}
				} catch ( Exception ex )
				{
					result = getActivity ().getString ( R.string.error );
					ex.printStackTrace ();
				}
				return result;
			}
		}.execute ();
	}

	@Override
	public void onActivityResult ( int requestCode, int resultCode, Intent data )
	{
		super.onActivityResult ( requestCode, resultCode, data );

		facebook.onActivityResult ( requestCode, resultCode, data );
	}
}