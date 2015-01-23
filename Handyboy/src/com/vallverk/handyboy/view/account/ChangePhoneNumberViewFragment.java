package com.vallverk.handyboy.view.account;

import java.util.Random;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.VerificationCode;
import com.vallverk.handyboy.twilio.SmsSender;
import com.vallverk.handyboy.view.base.BaseFragment;

public class ChangePhoneNumberViewFragment extends BaseFragment
{
	private ImageView backImageView;
	private TextView backTextView;

	private EditText phoneEditText;
	private Button phoneVerificationButton;

	private VerificationCode verificationCode;
	private Dialog codeVerificationDialog;
	private boolean isVerificationOk = false;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.change_phone_number_layout, null );

		backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
		backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
		phoneEditText = ( EditText ) view.findViewById ( R.id.phoneEditText );

		phoneVerificationButton = ( Button ) view.findViewById ( R.id.phoneVerificationButton );
		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		updateComponents ();
		addListeners ();
	}

	private void updateComponents ()
	{

	}

	private void addListeners ()
	{

		phoneVerificationButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				showCodeVerificationDialog ();
			}
		} );

		backImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.onBackPressed ();
			}
		} );
		backTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.onBackPressed ();
			}
		} );
	}

	private void showCodeVerificationDialog ()
	{
		if ( codeVerificationDialog == null )
		{
			codeVerificationDialog = new Dialog ( getActivity () );
			codeVerificationDialog.requestWindowFeature ( Window.FEATURE_NO_TITLE );
			codeVerificationDialog.getWindow ().setBackgroundDrawable ( new ColorDrawable ( android.graphics.Color.TRANSPARENT ) );
			codeVerificationDialog.setContentView ( R.layout.code_verification_dialog_layout );
			View noButton = codeVerificationDialog.findViewById ( R.id.dialogNoButton );
			View yesButton = codeVerificationDialog.findViewById ( R.id.dialogYesButton );
			final EditText verificationCodeField = ( EditText ) codeVerificationDialog.findViewById ( R.id.verificationCodeField );
			noButton.setOnClickListener ( new OnClickListener ()
			{

				@Override
				public void onClick ( View v )
				{
					codeVerificationDialog.dismiss ();
				}
			} );

			yesButton.setOnClickListener ( new OnClickListener ()
			{

				@Override
				public void onClick ( View v )
				{
					codeVerificationDialog.dismiss ();
					String code = verificationCodeField.getText ().toString ().trim ();
					if ( code.isEmpty () )
					{
						Toast.makeText ( getActivity (), R.string.please_enter_code, Toast.LENGTH_LONG ).show ();
					} else if ( !verificationCode.check ( code ) )
					{
						Toast.makeText ( getActivity (), "You are entered the wrong code", Toast.LENGTH_LONG ).show ();
					} else
					{
						isVerificationOk = true;
						controller.onBackPressed ();
					}
				}
			} );

		}
		String phone = phoneEditText.getText ().toString ().trim ();
		if ( phoneNumberValidation ( phone ) )
		{
			sendSms ( phone );
			codeVerificationDialog.show ();
		}
	}

	private void sendSms ( final String phone )
	{
		new AsyncTask < Void, Void, String > ()
		{
			public void onPostExecute ( String result )
			{
				super.onPostExecute ( result );
				if ( !result.isEmpty () ) // success registration
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
					String sendedCode = generateCode ();
					verificationCode = new VerificationCode ( sendedCode, System.currentTimeMillis () );

					SmsSender.send ( phone, verificationCode );
				} catch ( Exception ex )
				{
					ex.printStackTrace ();
					result = ex.getLocalizedMessage ();
				}
				return result;
			}
		}.execute ();
	}

	private boolean phoneNumberValidation ( String phoneNumber )
	{
		String regexStr = "^\\+[0-9]{10,13}$";
		if ( phoneNumber.matches ( regexStr ) == false )
		{
			Toast.makeText ( getActivity (), "Please enter valid phone number", Toast.LENGTH_SHORT ).show ();
			return false;
		}
		return true;
	}

	protected String generateCode ()
	{
		String code = "";
		Random random = new Random ();
		int codeLength = 4;
		for ( int i = 0; i < codeLength; i++ )
		{
			int number = random.nextInt ( 10 );
			code += number;
		}
		return code;
	}
}