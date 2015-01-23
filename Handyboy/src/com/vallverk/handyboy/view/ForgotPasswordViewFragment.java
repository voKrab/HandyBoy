package com.vallverk.handyboy.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;

public class ForgotPasswordViewFragment extends BaseFragment
{
	private ImageView backImageView;
	private TextView backTextView;
	private EditText emailEditText;
	private Button restoreButton;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.forgot_password_layout, null );
		
		backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
		backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
		emailEditText = ( EditText ) view.findViewById ( R.id.emailEditText );
		restoreButton = ( Button ) view.findViewById ( R.id.restoreButton );
						
		return view;
	}
	
	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		
		addListeners ();
	}

	private void addListeners ()
	{
		backImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				getActivity ().onBackPressed ();
			}
		});
		backTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				getActivity ().onBackPressed ();
			}
		});
		restoreButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				restorePassword ();
			}
		});
	}

	protected void restorePassword ()
	{
		final String email = emailEditText.getText ().toString ().trim ();		
		if ( email.isEmpty () )
		{
			Toast.makeText ( getActivity(), R.string.whats_your_email, Toast.LENGTH_LONG ).show ();
			return;
		}
		new AsyncTask < Void, Void, String > ()
		{
			public void onPreExecute ()
			{
				super.onPreExecute ();
				MainActivity.getInstance ().showLoader ();
			}
			public void onPostExecute ( String result )
			{
				super.onPostExecute ( result );
				MainActivity.getInstance ().hideLoader ();
				if ( result.isEmpty () ) //success restore
				{
					getActivity ().onBackPressed ();
					Toast.makeText ( getActivity (), R.string.password_successfully_restored_please_check_your_email, Toast.LENGTH_LONG ).show ();
				} else
				{
					Toast.makeText ( getActivity(), result, Toast.LENGTH_LONG ).show ();
				}
			}
			
			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					result = ServerManager.restorePassswordRequets ( email );
				} catch ( Exception ex )
				{
					result = getActivity ().getString ( R.string.error );
					ex.printStackTrace ();
				}
				return result;
			}
		}.execute ();
	}
}