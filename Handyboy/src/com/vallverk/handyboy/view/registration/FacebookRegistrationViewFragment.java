package com.vallverk.handyboy.view.registration;

import java.util.Calendar;
import java.util.Random;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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

public class FacebookRegistrationViewFragment extends BaseFragment
{
	private ImageView backImageView;
	private TextView backTextView;
	private EditText phoneEditText;
	private CheckBox termsOfServiceCheckBox;
	private CheckBox privacyPolicyCheckBox;
	private Button phoneVerificationButton;
	private Button skipButton;

	private long date;
	private View codeContainer;
	private EditText codeEditText;
	private VerificationCode verificationCode;
	private UserAPIObject user;
	private TextView termsTextView;
	private TextView privacyPolicyTextView;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.facebook_registration_layout, null );

		backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
		backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
		phoneEditText = ( EditText ) view.findViewById ( R.id.phoneEditText );
		termsOfServiceCheckBox = ( CheckBox ) view.findViewById ( R.id.termsOfServiceCheckBox );
		privacyPolicyCheckBox = ( CheckBox ) view.findViewById ( R.id.privacyPolicyCheckBox );
		phoneVerificationButton = ( Button ) view.findViewById ( R.id.phoneVerificationButton );
		skipButton = ( Button ) view.findViewById ( R.id.skipButton );
		codeContainer = view.findViewById ( R.id.codeContainer );
		codeEditText = ( EditText ) view.findViewById ( R.id.codeEditText );
		termsTextView = ( TextView ) view.findViewById ( R.id.termsTextView );
		privacyPolicyTextView = ( TextView ) view.findViewById ( R.id.privacyPolicyTextView );

		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );

//		if ( controller.isDebugMode () )
//		{
//			setupTestData ();
//		}
		updateComponents ();
		addListeners ();
	}

	private void updateComponents ()
	{
		user = ( UserAPIObject ) MainActivity.getInstance ().getCommunicationValue ( UserAPIObject.class.getSimpleName () );
		if ( user != null )
		{
//			firstEditText.setText ( user.getValue ( UserParams.FIRST_NAME ).toString () );
//			lastEditText.setText ( user.getValue ( UserParams.LAST_NAME ).toString () );
		}
	}

	public abstract class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
	{

		@Override
		public Dialog onCreateDialog ( Bundle savedInstanceState )
		{
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance ();
			int year = c.get ( Calendar.YEAR );
			int month = c.get ( Calendar.MONTH );
			int day = c.get ( Calendar.DAY_OF_MONTH );

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog ( getActivity (), this, year, month, day );
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
		privacyPolicyTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.setState ( VIEW_STATE.PRIVACY_POLICY );
			}
		} );
		phoneVerificationButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				if ( codeContainer.getVisibility () == View.VISIBLE )
				{
					registration ();
				} else
				{
					firstStepRegistration ();
				}
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
	}

	protected void registration ()
	{
		if ( !controller.isDebugMode () )
		{
			String code = codeEditText.getText ().toString ().trim ();
			if ( code.isEmpty () )
			{
				Toast.makeText ( getActivity (), R.string.please_enter_code, Toast.LENGTH_LONG ).show ();
				return;
			}
			if ( !verificationCode.check ( code ) )
			{
				Toast.makeText ( getActivity (), "You are entered the wrong code", Toast.LENGTH_LONG ).show ();
				return;
			}
		}
		String phone = phoneEditText.getText ().toString ().trim ();
		if ( user == null )
		{
			user = new UserAPIObject ();
		}
		user.putValue ( UserParams.PHONE_NUMBER, phone );
		user.putValue ( UserParams.DOB, date );
		user.putValue ( UserParams.STATUS, UserStatus.NEW.toString () );
		user.putValue ( UserParams.EMAIL, "generated" + new Random ().nextInt ( 1000 ) + "@gmai.com" );

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

	protected void firstStepRegistration ()
	{
		final String phone = phoneEditText.getText ().toString ().trim ();
		if ( phone.isEmpty () )
		{
			Toast.makeText ( getActivity (), R.string.whats_your_cell_number, Toast.LENGTH_LONG ).show ();
			return;
		}
		if ( !termsOfServiceCheckBox.isChecked () || !privacyPolicyCheckBox.isChecked () )
		{
			Toast.makeText ( getActivity (), R.string.your_must_accept_our_terms_of_service_and_privacy_policy, Toast.LENGTH_LONG ).show ();
			return;
		}
		if ( controller.isDebugMode () )
		{
			registration ();
		} else
		{
			// send sms
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
						codeContainer.setVisibility ( View.VISIBLE );
						phoneVerificationButton.setText ( R.string.registration );
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
//			registration ();
		}
	}

	private boolean checkIllegalCharacters ( String text )
	{
		char firstLetter = text.charAt ( 0 );
		int ascii = ( int ) firstLetter;
		boolean isFirstLetterValid = ( ascii >= ( int ) 'A' && ascii <= ( int ) 'Z' ) || ( ascii >= ( int ) 'a' && ascii <= ( int ) 'z' );
		if ( !isFirstLetterValid )
		{
			return false;
		}
		for ( int i = 0; i < text.length (); i++ )
		{
			char letter = text.charAt ( i );
			ascii = ( int ) letter;
			boolean isValid = ( ascii >= ( int ) 'A' && ascii <= ( int ) 'Z' ) || ( ascii >= ( int ) 'a' && ascii <= ( int ) 'z' ) || ascii == ( int ) ' ' || ascii == ( int ) '\'' || ascii == ( int ) '-' || ( ascii >= ( int ) '0' && ascii <= ( int ) '9' );
			if ( !isValid )
			{
				return false;
			}
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