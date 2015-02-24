package com.vallverk.handyboy.view.registration;

import java.util.Calendar;
import java.util.Random;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.UserStatus;
import com.vallverk.handyboy.model.VerificationCode;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.twilio.SmsSender;
import com.vallverk.handyboy.view.base.BaseFragment;

public class RegistrationViewFragment extends BaseFragment
{
	private ImageView backImageView;
	private TextView backTextView;
	private EditText firstEditText;
	private EditText lastEditText;
	private EditText emailEditText;
	private EditText passwordEditText;
	private TextView monthTextView;
	private TextView dayTextView;
	private TextView yearTextView;
	private EditText phoneEditText;
	private CheckBox termsOfServicePrivacyPolicyCheckBox;
	private CheckBox servicesContractCheckBox;
	private Button phoneVerificationButton;
	private long date;
	private VerificationCode verificationCode;
	private UserAPIObject user;
	private TextView termsTextView;
	private TextView privacyPolicyTextView;
    private TextView contractTextView;
	private Dialog codeVerificationDialog;

	private boolean isVerificationOk = false;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.registration_layout, null );
		backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
		backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
		firstEditText = ( EditText ) view.findViewById ( R.id.firstEditText );
		lastEditText = ( EditText ) view.findViewById ( R.id.lastEditText );
		emailEditText = ( EditText ) view.findViewById ( R.id.emailEditText );
		passwordEditText = ( EditText ) view.findViewById ( R.id.passwordEditText );
		monthTextView = ( TextView ) view.findViewById ( R.id.monthTextView );
		dayTextView = ( TextView ) view.findViewById ( R.id.dayTextView );
		yearTextView = ( TextView ) view.findViewById ( R.id.yearTextView );
		phoneEditText = ( EditText ) view.findViewById ( R.id.phoneEditText );
        termsOfServicePrivacyPolicyCheckBox = ( CheckBox ) view.findViewById ( R.id.termsOfServicePrivacyPolicyCheckBox );
        servicesContractCheckBox = ( CheckBox ) view.findViewById ( R.id.servicesContractCheckBox );
		phoneVerificationButton = ( Button ) view.findViewById ( R.id.phoneVerificationButton );
        contractTextView = (TextView) view.findViewById(R.id.contractTextView);
		termsTextView = ( TextView ) view.findViewById ( R.id.termsTextView );
		privacyPolicyTextView = ( TextView ) view.findViewById ( R.id.privacyPolicyTextView );
		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		if ( controller.isDebugMode () )
		{
			setupTestData ();
		}

		updateComponents ();
		addListeners ();
	}

	private void updateComponents ()
	{
		user = ( UserAPIObject ) MainActivity.getInstance ().getCommunicationValue ( UserAPIObject.class.getSimpleName () );
		if ( user != null )
		{
			firstEditText.setText ( user.getValue ( UserParams.FIRST_NAME ).toString () );
			lastEditText.setText ( user.getValue ( UserParams.LAST_NAME ).toString () );
		}
	}

	private void setupTestData ()
	{
		firstEditText.setText ( "Oleg" );
		lastEditText.setText ( "Barkov" );
		emailEditText.setText ( "tuser@gmail.com" );
		passwordEditText.setText ( "123456" );
		phoneEditText.setText ( "+380637799999" );
        termsOfServicePrivacyPolicyCheckBox.setChecked ( true );
        servicesContractCheckBox.setChecked ( true );

	}

	public abstract class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
	{

		@Override
		public Dialog onCreateDialog ( Bundle savedInstanceState )
		{
			int minUserOld = 18;
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance ();
			int year = c.get ( Calendar.YEAR );
			year -= minUserOld;
			int month = c.get ( Calendar.MONTH );
			int day = c.get ( Calendar.DAY_OF_MONTH );

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog ( getActivity (), this, year, month, day );
		}
	}

	private void showCodeVerificationDialog ()
	{
		if ( codeVerificationDialog == null )
		{
			codeVerificationDialog = new Dialog ( getActivity () );
			codeVerificationDialog.setCancelable ( false );
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
					String code = verificationCodeField.getText ().toString ().trim ();
					if ( code.isEmpty () )
					{
						Toast.makeText ( getActivity (), R.string.please_enter_code, Toast.LENGTH_LONG ).show ();
					} else if ( !verificationCode.check ( code ) )
					{
						Toast.makeText ( getActivity (), "You are entered the wrong code", Toast.LENGTH_LONG ).show ();
					} else
					{
						codeVerificationDialog.dismiss ();
						isVerificationOk = true;
						registration ();
					}
				}
			} );

		}
		String phone = phoneEditText.getText ().toString ().trim ();
		if ( phoneNumberValidation ( phone ) )
		{
			sendSms ( "+" + phone );
			codeVerificationDialog.show ();
		}
	}

	private void addListeners ()
	{
		termsTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.setState ( VIEW_STATE.TERMS );
			}
		} );
        contractTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setState ( VIEW_STATE.CONTRACT );
            }
        });
		privacyPolicyTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.setState ( VIEW_STATE.PRIVACY_POLICY );
			}
		} );
		OnClickListener dateListener = new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				DatePickerFragment picker = new DatePickerFragment ()
				{
					@Override
					public void onDateSet ( DatePicker view, int year, int month, int day )
					{

						Calendar c = Calendar.getInstance ();
						c.set ( year, month + 1, day );
						date = c.getTimeInMillis ();
						monthTextView.setText ( "" + ( month + 1 ) );
						dayTextView.setText ( "" + day );
						yearTextView.setText ( "" + ( year ) );
					}
				};
				picker.show ( getFragmentManager (), "datePicker" );
			}
		};
		monthTextView.setOnClickListener ( dateListener );
		dayTextView.setOnClickListener ( dateListener );
		yearTextView.setOnClickListener ( dateListener );
		phoneVerificationButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				/*
				 * if ( codeContainer.getVisibility () == View.VISIBLE ) {
				 * registration (); } else { firstStepRegistration (); }
				 */

				firstStepRegistration ();
			}
		} );
		backImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				getActivity ().onBackPressed ();
			}
		} );
		backTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				getActivity ().onBackPressed ();
			}
		} );

		phoneEditText.addTextChangedListener ( phoneTextWatcher );
	}

	private TextWatcher phoneTextWatcher = new TextWatcher ()
	{

		@Override
		public void onTextChanged ( CharSequence s, int start, int before, int count )
		{
		}

		@Override
		public void beforeTextChanged ( CharSequence s, int start, int count, int after )
		{
		}

		@Override
		public void afterTextChanged ( Editable s )
		{
			String phone = s.toString ().replace ( "+", "" ).trim ();
			if ( phone != null && !phone.isEmpty () )
			{
				phone = "+" + phone;
				phoneEditText.removeTextChangedListener ( phoneTextWatcher );
				phoneEditText.setText ( phone );
				phoneEditText.setSelection ( phoneEditText.getText ().length () );
				phoneEditText.addTextChangedListener ( phoneTextWatcher );
			}
		}
	};

	protected void registration ()
	{
		String firstName = firstEditText.getText ().toString ().trim ();
		String lastName = lastEditText.getText ().toString ().trim ();
		String email = emailEditText.getText ().toString ().trim ();
		String password = passwordEditText.getText ().toString ().trim ();
		String phone = phoneEditText.getText ().toString ().trim ();
		if ( user == null )
		{
			user = new UserAPIObject ();
		}
		user.putValue ( UserParams.PASSWORD, password );
		user.putValue ( UserParams.EMAIL, email );
		user.putValue ( UserParams.FIRST_NAME, firstName );
		user.putValue ( UserParams.LAST_NAME, lastName );
		user.putValue ( UserParams.PHONE_NUMBER, phone );
		user.putValue ( UserParams.DOB, date );
		user.putValue ( UserParams.STATUS, UserStatus.NEW.toString () );

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
				if ( result.isEmpty () ) // success registration
				{
					APIManager apiManager = APIManager.getInstance ();
					apiManager.setUser ( user );
					MainActivity.getInstance ().enter ();
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
					String registrationResult = user.registration ();
					if ( !registrationResult.isEmpty () )
					{
						result = registrationResult;
					}
				} catch ( Exception ex )
				{
					result = ex.getMessage ();
					ex.printStackTrace ();
				}
				return result;
			}
		}.execute ();
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

	protected void firstStepRegistration ()
	{
		String firstName = firstEditText.getText ().toString ().trim ();
		if ( firstName.isEmpty () )
		{
			Toast.makeText ( getActivity (), R.string.whats_your_name, Toast.LENGTH_LONG ).show ();
			return;
		}

		if ( firstName.length () < 2 )
		{
			Toast.makeText ( getActivity (), R.string.names_require, Toast.LENGTH_LONG ).show ();
			return;
		}

		String lastName = lastEditText.getText ().toString ().trim ();
		if ( lastName.isEmpty () )
		{
			Toast.makeText ( getActivity (), R.string.whats_your_name, Toast.LENGTH_LONG ).show ();
			return;
		}

		if ( lastName.length () < 2 )
		{
			Toast.makeText ( getActivity (), R.string.names_require, Toast.LENGTH_LONG ).show ();
			return;
		}

		String email = emailEditText.getText ().toString ().trim ();
		if ( email.isEmpty () )
		{
			Toast.makeText ( getActivity (), R.string.whats_your_email, Toast.LENGTH_LONG ).show ();
			return;
		}
		if ( !Patterns.EMAIL_ADDRESS.matcher ( email ).matches () )
		{
			Toast.makeText ( getActivity (), R.string.email_address_not_valid, Toast.LENGTH_LONG ).show ();
			return;
		}
		String password = passwordEditText.getText ().toString ().trim ();
		if ( password.isEmpty () )
		{
			Toast.makeText ( getActivity (), R.string.what_password_would_you_like_to_use, Toast.LENGTH_LONG ).show ();
			return;
		}

		if ( password.length () < 6 )
		{
			Toast.makeText ( getActivity (), R.string.passwords_must_have, Toast.LENGTH_LONG ).show ();
			return;
		}
		// if ( )
		if ( date == 0 )
		{
			Toast.makeText ( getActivity (), R.string.how_old_are_you, Toast.LENGTH_LONG ).show ();
			return;
		}
		long time = System.currentTimeMillis ();
		Calendar calendar = Calendar.getInstance ();
		calendar.setTimeInMillis ( time );
		int yearNow = calendar.get ( Calendar.YEAR );
		calendar.setTimeInMillis ( date );
		int old = yearNow - calendar.get ( Calendar.YEAR );
		if ( old < 18 )
		{
			Toast.makeText ( getActivity (), R.string.you_are_still_young, Toast.LENGTH_LONG ).show ();
			return;
		}

		if ( !phoneNumberValidation ( phoneEditText.getText ().toString ().trim () ) )
		{
			return;
		}
		if ( !termsOfServicePrivacyPolicyCheckBox.isChecked () || !servicesContractCheckBox.isChecked () )
		{
			Toast.makeText ( getActivity (), R.string.your_must_accept_our_terms_of_service_and_privacy_policy, Toast.LENGTH_LONG ).show ();
			return;
		}

		showCodeVerificationDialog ();
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