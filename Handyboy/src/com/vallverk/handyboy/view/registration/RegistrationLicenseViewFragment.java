package com.vallverk.handyboy.view.registration;

import java.io.IOException;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.job.TypeJobEnum;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.BitmapPreviewDialogFragment;
import com.vallverk.handyboy.view.base.SingleChoiceSpinner;
import com.vallverk.handyboy.view.controller.RegistrationController;
import com.vallverk.handyboy.view.jobdescription.HelpDialogFragment;

public class RegistrationLicenseViewFragment extends BaseFragment
{
	private RegistrationController controller;
	private View backImageView;
	private View backTextView;

	private TextView stateTextView;
	private SingleChoiceSpinner stateSpinner;

	private TextView massageMonthTextView;
	private TextView massageDayTextView;
	private TextView massageYearTextView;

	private TextView trainerMonthTextView;
	private TextView trainerDayTextView;
	private TextView trainerYearTextView;

	private ImageView proofFileImageView;
	private Bitmap proofFile;
	private TextView fileStatusTextView;
	private View chooseFileButton;
	private View showHelpTextView;

	private View submitButton;

	private View massageTrainerLayout;
	private View personalTrainerLayout;

	private long massageDate;
	private long trainerDate;
	
	private EditText massageLicenceNumber;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.registration_license_layout, null );

		backImageView = view.findViewById ( R.id.backImageView );
		backTextView = view.findViewById ( R.id.backTextView );

		stateTextView = ( TextView ) view.findViewById ( R.id.stateTextView );
		stateSpinner = ( SingleChoiceSpinner ) view.findViewById ( R.id.stateSpinner );

		massageMonthTextView = ( TextView ) view.findViewById ( R.id.massageMonthTextView );
		massageDayTextView = ( TextView ) view.findViewById ( R.id.massageDayTextView );
		massageYearTextView = ( TextView ) view.findViewById ( R.id.massageYearTextView );

		trainerMonthTextView = ( TextView ) view.findViewById ( R.id.trainerMonthTextView );
		trainerDayTextView = ( TextView ) view.findViewById ( R.id.trainerDayTextView );
		trainerYearTextView = ( TextView ) view.findViewById ( R.id.trainerYearTextView );
		
		submitButton = view.findViewById ( R.id.submitButton );

		massageTrainerLayout = view.findViewById ( R.id.massageTrainerLayout );
		personalTrainerLayout = view.findViewById ( R.id.personalTrainerLayout );
		
		massageLicenceNumber = (EditText) view.findViewById ( R.id.massageLicenceNumber );

		proofFileImageView = ( ImageView ) view.findViewById ( R.id.proofFileImageView );
		fileStatusTextView = ( TextView ) view.findViewById ( R.id.fileStatusTextView );
		chooseFileButton = view.findViewById ( R.id.chooseFileButton );
		showHelpTextView = view.findViewById ( R.id.showHelpTextView );
		fileStatusTextView = ( TextView ) view.findViewById ( R.id.fileStatusTextView );
		
		return view;
	}

	public void chooseMedia ( MediaType mediaType )
	{
		switch ( mediaType )
		{
			case PHOTO:
			{
				FragmentTransaction ft = getActivity ().getSupportFragmentManager ().beginTransaction ();
				DialogFragment newFragment = new ChoosePhotoDialogFragment ();
				newFragment.show ( ft, null );
				break;
			}

			case VIDEO:
			{
				FragmentTransaction ft = getActivity ().getSupportFragmentManager ().beginTransaction ();
				DialogFragment newFragment = new ChooseVideoDialogFragment ();
				newFragment.show ( ft, null );
				break;
			}

			case PHOTO_AND_VIDEO:
			{
				FragmentTransaction ft = getActivity ().getSupportFragmentManager ().beginTransaction ();
				DialogFragment newFragment = new ChoosePhotoVideoDialogFragment ();
				newFragment.show ( ft, null );
				break;
			}
		}
	}

	private boolean isLicense ( boolean isMasseur )
	{

		String typeJobsString = controller.getTypeJobs ();
		if ( typeJobsString == null )
		{
			return false;
		}
		String[] typeJobs = typeJobsString.split ( "," );

		for ( String temString : typeJobs )
		{
			if ( isMasseur && TypeJobEnum.MASSEUR.toString ().equals ( temString.trim () ) )
			{
				return true;
			} else if ( !isMasseur && TypeJobEnum.PERSONAL_TRAINER.toString ().equals ( temString.trim () ) )
			{
				return true;
			}
		}
		return false;
	}

	private void updateProofFileComponents ()
	{
		if ( proofFile == null )
		{
			proofFileImageView.setVisibility ( View.GONE );
			fileStatusTextView.setVisibility ( View.VISIBLE );
		} else
		{
			proofFileImageView.setImageBitmap ( proofFile );
			proofFileImageView.setVisibility ( View.VISIBLE );
			fileStatusTextView.setVisibility ( View.GONE );
		}
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onCreate ( savedInstanceState );
		controller = MainActivity.getInstance ().getRegistrationController ();
		updateComponents ();
		addListeners ();
	}

	private void updateComponents ()
	{
		stateSpinner.setData ( getActivity ().getResources ().getStringArray ( R.array.states ) );
		if ( isLicense ( true ) )
		{
			massageTrainerLayout.setVisibility ( View.VISIBLE );
		} else
		{
			massageTrainerLayout.setVisibility ( View.GONE );
		}

		if ( isLicense ( false ) )
		{
			personalTrainerLayout.setVisibility ( View.VISIBLE );
		} else
		{
			personalTrainerLayout.setVisibility ( View.GONE );
		}

	}

	public abstract class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
	{

		@Override
		public Dialog onCreateDialog ( Bundle savedInstanceState )
		{
			final Calendar c = Calendar.getInstance ();
			int year = c.get ( Calendar.YEAR );
			int month = c.get ( Calendar.MONTH );
			int day = c.get ( Calendar.DAY_OF_MONTH );
			return new DatePickerDialog ( getActivity (), this, year, month, day );
		}
	}

	private OnClickListener massageDateClickListener = new OnClickListener ()
	{
		@Override
		public void onClick ( View v )
		{
			DatePickerFragment picker = new DatePickerFragment ()
			{
				@Override
				public void onDateSet ( DatePicker view, int year, int month, int day )
				{
					Calendar calendar = Calendar.getInstance ();
					calendar.set ( year, month + 1, day );
					massageDate = calendar.getTimeInMillis ();
					massageMonthTextView.setText ( "" + ( month + 1 ) );
					massageDayTextView.setText ( "" + day );
					massageYearTextView.setText ( "" + ( year ) );
				}
			};
			picker.show ( getFragmentManager (), "datePicker" );
		}
	};

	private OnClickListener trainerDateClickListener = new OnClickListener ()
	{
		@Override
		public void onClick ( View v )
		{
			DatePickerFragment picker = new DatePickerFragment ()
			{
				@Override
				public void onDateSet ( DatePicker view, int year, int month, int day )
				{
					Calendar calendar = Calendar.getInstance ();
					calendar.set ( year, month + 1, day );
					trainerDate = calendar.getTimeInMillis ();
					trainerMonthTextView.setText ( "" + ( month + 1 ) );
					trainerDayTextView.setText ( "" + day );
					trainerYearTextView.setText ( "" + ( year ) );
				}
			};
			picker.show ( getFragmentManager (), "datePicker" );
		}
	};

	protected void addListeners ()
	{
		backImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.prevStep ();
			}
		} );
		backTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.prevStep ();
			}
		} );

		stateSpinner.setOnItemSelectedListener ( new OnItemSelectedListener ()
		{
			@Override
			public void onItemSelected ( AdapterView < ? > adapter, View view, int selected, long l )
			{
				stateTextView.setText ( adapter.getAdapter ().getItem ( selected ).toString () );
			}

			@Override
			public void onNothingSelected ( AdapterView < ? > adapterView ) { }
			
		} );
		
		showHelpTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				FragmentTransaction ft = MainActivity.getInstance ().getSupportFragmentManager ().beginTransaction ();
				DialogFragment newFragment = new HelpDialogFragment ();
				newFragment.show ( ft, null );
			}
		} );

		chooseFileButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				chooseMedia ( MediaType.PHOTO );
			}
		} );
		
		proofFileImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				FragmentTransaction ft = MainActivity.getInstance ().getSupportFragmentManager ().beginTransaction ();
				DialogFragment newFragment = new BitmapPreviewDialogFragment ( proofFile );
				newFragment.show ( ft, null );
			}
		} );

		submitButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				if ( massageTrainerLayout.getVisibility () == View.VISIBLE )
				{
					String licenceNumberString = massageLicenceNumber.getText ().toString ();
					if ( massageDate == 0 )
					{
						Toast.makeText ( getActivity(), getString ( R.string.what_date_massage ), Toast.LENGTH_LONG ).show ();
						return;
					}
					
					if(licenceNumberString.isEmpty ()){
						Toast.makeText ( getActivity(), getString ( R.string.what_license_number_massage ), Toast.LENGTH_LONG ).show ();
						return;
					}
					
					controller.setLicenceNumber ( licenceNumberString );
					controller.setUState ( stateTextView.getText ().toString () );
					controller.setMassageDate ( massageDate );
				}

				if ( personalTrainerLayout.getVisibility () == View.VISIBLE )
				{
					if ( trainerDate == 0 )
					{
						Toast.makeText ( getActivity(), getString ( R.string.what_date_personal_trainer ), Toast.LENGTH_LONG ).show ();
						return;
					}
					
					if ( proofFileImageView != null && proofFile == null )
					{
						Toast.makeText ( MainActivity.getInstance (), R.string.upload_proof_of_current_personal_trainers_insurance, Toast.LENGTH_LONG ).show ();
						return;
					}
					
					controller.setTrainerDate ( trainerDate );
					controller.setApproveFile ( proofFile );
				}
				
				controller.nextStep ();
			}
		} );

		massageMonthTextView.setOnClickListener ( massageDateClickListener );
		massageDayTextView.setOnClickListener ( massageDateClickListener );
		massageYearTextView.setOnClickListener ( massageDateClickListener );

		trainerMonthTextView.setOnClickListener ( trainerDateClickListener );
		trainerDayTextView.setOnClickListener ( trainerDateClickListener );
		trainerYearTextView.setOnClickListener ( trainerDateClickListener );
	}
	
	@Override
	protected void photoFromEditor ( Uri uri )
	{
		try
		{
			final Bitmap bitmap = Tools.decodeSampledBitmapFromResource ( MainActivity.MAX_POSTED_IMAGE_WIDTh, MainActivity.MAX_POSTED_IMAGE_HEIGHT, getActivity ().getContentResolver (), uri );
			proofFile = bitmap;
			updateProofFileComponents ();
		} catch ( IOException e )
		{
			e.printStackTrace ();
		}
	}
}