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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
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
	private CheckBox termsOfServicePrivacyPolicyCheckBox;
	private CheckBox servicesContractCheckBox;
    private TextView contractTextView;
	private Button phoneVerificationButton;
	private VerificationCode verificationCode;
	private UserAPIObject user;
	private TextView termsTextView;
	private TextView privacyPolicyTextView;

    private Dialog codeVerificationDialog;

    private boolean isVerificationOk = false;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.facebook_registration_layout, null );

		backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
		backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
		phoneEditText = ( EditText ) view.findViewById ( R.id.phoneEditText );
        termsOfServicePrivacyPolicyCheckBox = ( CheckBox ) view.findViewById ( R.id.termsOfServicePrivacyPolicyCheckBox );
        servicesContractCheckBox = ( CheckBox ) view.findViewById ( R.id.servicesContractCheckBox );
		phoneVerificationButton = ( Button ) view.findViewById ( R.id.phoneVerificationButton );
		termsTextView = ( TextView ) view.findViewById ( R.id.termsTextView );
		privacyPolicyTextView = ( TextView ) view.findViewById ( R.id.privacyPolicyTextView );
        contractTextView = (TextView) view.findViewById(R.id.contractTextView);
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
		user = ( UserAPIObject ) MainActivity.getInstance ().getCommunicationValue ( UserAPIObject.class.getSimpleName () );
	}


    private void showCodeVerificationDialog ()
    {
        if ( codeVerificationDialog == null )
        {
            codeVerificationDialog = new Dialog ( getActivity () );
            codeVerificationDialog.setCancelable ( false );
            codeVerificationDialog.requestWindowFeature ( Window.FEATURE_NO_TITLE );
            codeVerificationDialog.getWindow ().setBackgroundDrawable ( new ColorDrawable( android.graphics.Color.TRANSPARENT ) );
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
                    //
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
                        // phoneVerificationButton.setText (
                        // R.string.registration );
                    }
                }
            } );

        }
        String phone = phoneEditText.getText ().toString ().trim ();
        if ( phoneNumberValidation ( phone ) )
        {
            if(phone.indexOf("+1") == 0){
                sendSms ( phone );
            }else{
                if(phone.indexOf("1") != 0){
                    phone = "1" + phone;
                }
                sendSms ( "+" + phone );
            }
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
		phoneVerificationButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
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
            /*String phone = s.toString ().replace ( "+", "" ).trim ();
            if ( phone != null && !phone.isEmpty () )
            {
                phone = "+" + phone;
                phoneEditText.removeTextChangedListener ( phoneTextWatcher );
                phoneEditText.setText ( phone );
                phoneEditText.setSelection ( phoneEditText.getText ().length () );
                phoneEditText.addTextChangedListener ( phoneTextWatcher );
            }*/
        }
    };

	protected void registration ()
	{
		String phone = phoneEditText.getText ().toString ().trim ();
		if ( user == null )
		{
			user = new UserAPIObject ();
		}
		user.putValue ( UserParams.PHONE_NUMBER, phone );
		//user.putValue ( UserParams.DOB, date );
		user.putValue ( UserParams.STATUS, UserStatus.NEW.toString () );
		//user.putValue ( UserParams.EMAIL, "generated" + new Random ().nextInt ( 1000 ) + "@gmail.com" );

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
		if ( !termsOfServicePrivacyPolicyCheckBox.isChecked () || !servicesContractCheckBox.isChecked () )
		{
			Toast.makeText ( getActivity (), R.string.your_must_accept_our_terms_of_service_and_privacy_policy, Toast.LENGTH_LONG ).show ();
			return;
		}
        long date = Long.parseLong(user.getValue(UserParams.DOB).toString());
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

		if ( controller.isDebugMode () )
		{
			registration ();
		} else
		{
            if ( !phoneNumberValidation ( phoneEditText.getText ().toString ().trim () ) )
            {
                return;
            }
            showCodeVerificationDialog ();
		}
	}

    private boolean phoneNumberValidation ( String phoneNumber )
    {
       // String regexStr = "^\\+[0-9]{10,13}$";

        String regexStr = "^(?:(?:\\+?1\\s*(?:[.-]\\s*)?)?(?:\\(\\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\\s*\\)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\\s*(?:[.-]\\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\\s*(?:[.-]\\s*)?([0-9]{4})(?:\\s*(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?$";
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