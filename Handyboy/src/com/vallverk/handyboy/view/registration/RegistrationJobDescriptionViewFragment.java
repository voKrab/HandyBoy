package com.vallverk.handyboy.view.registration;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.job.JobTypeManager;
import com.vallverk.handyboy.model.job.TypeJob;
import com.vallverk.handyboy.model.job.TypeJobEnum;
import com.vallverk.handyboy.model.job.TypejobLevel;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.BitmapPreviewDialogFragment;
import com.vallverk.handyboy.view.controller.RegistrationController;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegistrationJobDescriptionViewFragment extends BaseFragment
{
	private UserAPIObject user;
	private RegistrationController controller;
	private View backImageView;
	private View backTextView;
	private Map < TypeJob, ImageView > jobTypesMap;
	private View additionalInfoContainer;
	private TypeJob jobType;
	private int[] resources;
	private int[] selectedResources;
	private TextView jobTypeTextView;
	private Button chooseFileButton;
	private View showHelpTextView;
	private Bitmap proofFile;
	private View fileStatusTextView;
	private ImageView proofFileImageView;
	private SeekBar priceSeekBar;
	private TextView priceTextView;
	private TextView priceRangeTextView;
	private EditText descriptionEditText;
	private Button nextButton;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.registration_job_description_layout, null );

		backImageView = view.findViewById ( R.id.backImageView );
		backTextView = view.findViewById ( R.id.backTextView );
		jobTypesMap = new HashMap < TypeJob, ImageView > ();
		JobTypeManager jobTypeManager = JobTypeManager.getInstance ();
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.PERSONAL_TRAINER ), ( ImageView ) view.findViewById ( R.id.jobType1ImageView ) );
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.POOL_BOY ), ( ImageView ) view.findViewById ( R.id.jobType2ImageView ) );
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.ERRAND_BOY ), ( ImageView ) view.findViewById ( R.id.jobType7ImageView ) );

		//		jobTypesMap.put ( jobTypeManager.getJobType ( JobTypeEnum.BODY ), ( ImageView ) view.findViewById ( R.id.jobType3ImageView ) );
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.BARTENDER ), ( ImageView ) view.findViewById ( R.id.jobType4ImageView ) );
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.MASSEUR ), ( ImageView ) view.findViewById ( R.id.jobType5ImageView ) );
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.HOUSEKEEPER ), ( ImageView ) view.findViewById ( R.id.jobType8ImageView ) );
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.YARD_WORK ), ( ImageView ) view.findViewById ( R.id.jobType9ImageView ) );
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.SERVER_WAITER ), ( ImageView ) view.findViewById ( R.id.jobType10ImageView ) );
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.GOGO_BOY ), ( ImageView ) view.findViewById ( R.id.jobType11ImageView ) );
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.CAR_WASH ), ( ImageView ) view.findViewById ( R.id.jobType6ImageView ) );

//		jobTypesMap.put ( jobTypeManager.getJobType ( JobTypeEnum.HAIR ), ( ImageView ) view.findViewById ( R.id.jobType12ImageView ) );
//		jobTypesMap.put ( jobTypeManager.getJobType ( JobTypeEnum.FACE ), ( ImageView ) view.findViewById ( R.id.jobType13ImageView ) );
//		jobTypesMap.put ( jobTypeManager.getJobType ( JobTypeEnum.NAILS ), ( ImageView ) view.findViewById ( R.id.jobType14ImageView ) );
		additionalInfoContainer = view.findViewById ( R.id.additionalInfoContainer );
		jobTypeTextView = ( TextView ) view.findViewById ( R.id.jobTypeTextView );
		chooseFileButton = ( Button ) view.findViewById ( R.id.chooseFileButton );
		showHelpTextView = view.findViewById ( R.id.showHelpTextView );
		fileStatusTextView = view.findViewById ( R.id.fileStatusTextView );
		proofFileImageView = ( ImageView ) view.findViewById ( R.id.proofFileImageView );
		priceSeekBar = ( SeekBar ) view.findViewById ( R.id.priceSeekBar );
		priceTextView = ( TextView ) view.findViewById ( R.id.priceTextView );
		priceRangeTextView = ( TextView ) view.findViewById ( R.id.priceRangeTextView );
		descriptionEditText = ( EditText ) view.findViewById ( R.id.descriptionEditText );
		nextButton = ( Button ) view.findViewById ( R.id.nextButton );
		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );

		controller = MainActivity.getInstance ().getRegistrationController ();
		user = APIManager.getInstance ().getUser ();
		resources = new int[] { R.drawable.dumbbels_na, R.drawable.circle_na, R.drawable.man_na, R.drawable.martini_na, R.drawable.hand_na, R.drawable.venik_na, R.drawable.grass_na, R.drawable.foodcover_na, R.drawable.panties_na, R.drawable.car_na };
		selectedResources = new int[] { R.drawable.dumbbels_a, R.drawable.circle_a, R.drawable.man_a, R.drawable.martini_a, R.drawable.hand_a, R.drawable.venik_a, R.drawable.grass_a, R.drawable.foodcover_a, R.drawable.panties_a, R.drawable.car_a };
		updateComponents ();
		// setupTestData ();
		addListeners ();
	}

	public static class HelpDialogFragment extends DialogFragment
	{
		@Override
		public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
		{
			getDialog ().getWindow ().requestFeature ( Window.FEATURE_NO_TITLE );
			View layout = inflater.inflate ( R.layout.prof_file_help_layout, container, false );
			return layout;
		}

		public void onActivityCreated ( Bundle savedInstanceState )
		{
			super.onActivityCreated ( savedInstanceState );

			Window window = getDialog ().getWindow ();
			WindowManager.LayoutParams attributes = window.getAttributes ();
			// must setBackgroundDrawable(TRANSPARENT) in onActivityCreated()
			window.getAttributes ().windowAnimations = R.style.dialog_animation_fade;
			window.setBackgroundDrawable ( new ColorDrawable ( Color.TRANSPARENT ) );
			window.setLayout ( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
			window.setDimAmount ( 0 );
		}
	}

	private void updateComponents ()
	{
		jobType = controller.getJobType ();
		updateJobTypesComponents ();
		if ( jobType != null )
		{
			this.proofFile = controller.getApproveFile ();
			updateProofFileComponents ();
			int price = controller.getServicePrice ();
			priceSeekBar.setProgress ( price - jobType.getMinCost () );
			descriptionEditText.setText ( controller.getServiceDescription () );
			priceTextView.setText ( "" + price + "$" );
			priceRangeTextView.setText ( jobType.getMinCost () + "$" + " - " + jobType.getMaxCost () + "$" );
		}
	}

	protected void addListeners ()
	{
		nextButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				next ();
			}
		});
		priceSeekBar.setOnSeekBarChangeListener ( new OnSeekBarChangeListener ()
		{
			@Override
			public void onStopTrackingTouch ( SeekBar seekBar )
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch ( SeekBar seekBar )
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged ( SeekBar seekBar, int progress, boolean fromUser )
			{
				int offset = jobType == null ? 0 : jobType.getMinCost ();
				int price = progress + offset;
				priceTextView.setText ( "" + price + "$" );
			}
		} );
		proofFileImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				FragmentTransaction ft = getActivity ().getSupportFragmentManager ().beginTransaction ();
                MainActivity.getInstance ().setCommunicationValue ( "bitmap", proofFile );
				DialogFragment newFragment = new BitmapPreviewDialogFragment ();
				newFragment.show ( ft, null );
			}
		});
		showHelpTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				FragmentTransaction ft = getActivity ().getSupportFragmentManager ().beginTransaction ();
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
		for ( final TypeJob jobTypeKey : jobTypesMap.keySet () )
		{
			ImageView imageView = jobTypesMap.get ( jobTypeKey );
			imageView.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View v )
				{
					jobType = jobTypeKey;
					proofFile = null;
					updateJobTypesComponents ();
				}
			} );
		}
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
	}

	protected void next ()
	{
		if ( jobType == null )
		{
			Toast.makeText ( getActivity (), R.string.choose_job_type, Toast.LENGTH_LONG ).show ();
			return;
		}
		String serviceDescription = descriptionEditText.getText ().toString ().trim ();
		if ( serviceDescription.isEmpty () )
		{
			Toast.makeText ( getActivity (), R.string.describe_your_services, Toast.LENGTH_LONG ).show ();
			return;
		}
		if ( jobType.isRequiredApprove () )
		{
			if ( proofFile == null )
			{
				Toast.makeText ( getActivity (), R.string.upload_proof_of_current_personal_trainers_insurance, Toast.LENGTH_LONG ).show ();
				return;
			}
		}
		
		int price = priceSeekBar.getProgress () + jobType.getMinCost ();
		TypeJobServiceAPIObject typejobAPIObject = new TypeJobServiceAPIObject ( "", "" + jobType.getId (), price, TypejobLevel.SOME_EXPERIENCE.toString (), "" );
		controller.setServiceDescription ( serviceDescription );
		controller.setJobType ( jobType );
		if ( jobType.isRequiredApprove () )
		{
			controller.setApproveFile ( proofFile );
		}
		controller.setTypejobAPiObject ( typejobAPIObject );
		controller.nextStep ();
	}

	protected void updateJobTypesComponents ()
	{
		for ( final TypeJob jobTypeKey : jobTypesMap.keySet () )
		{
			int index = Arrays.asList ( TypeJobEnum.values () ).indexOf ( jobTypeKey.getEnumValue () );
			ImageView imageView = jobTypesMap.get ( jobTypeKey );
			imageView.setImageResource ( resources[index] );
		}
		if ( jobType != null )
		{
			int selectedIndex = Arrays.asList ( TypeJobEnum.values () ).indexOf ( jobType.getEnumValue () );
			ImageView imageView = jobTypesMap.get ( jobType );
			imageView.setImageResource ( selectedResources[selectedIndex] );
			jobTypeTextView.setText ( jobType.getName () );
			if ( jobType.isRequiredApprove () )
			{
				additionalInfoContainer.setVisibility ( View.VISIBLE );
			} else
			{
				additionalInfoContainer.setVisibility ( View.GONE );
			}
			updateProofFileComponents ();
			priceSeekBar.setMax ( jobType.getPriceDelta () );
			int priceOffset = jobType.getMinCost ();
			priceSeekBar.setProgress ( jobType.getMediumPrice () - priceOffset );
			priceRangeTextView.setText ( jobType.getMinCost () + "$" + " - " + jobType.getMaxCost () + "$" );
		} else
		{
			jobTypeTextView.setText ( R.string.choose_job_type );
			additionalInfoContainer.setVisibility ( View.GONE );
			priceSeekBar.setMax ( MainActivity.MAX_SERVICE_PRICE );
			priceSeekBar.setProgress ( MainActivity.DEFAULT_SERVICE_PRICE );
			priceRangeTextView.setText ( getActivity ().getString ( R.string.choose_job_type ) );
		}
	}

	protected void photoFromEditor ( Uri uri )
	{
		try
		{
			Bitmap bitmap = Tools.decodeSampledBitmapFromResource ( MainActivity.MAX_POSTED_IMAGE_WIDTh, MainActivity.MAX_POSTED_IMAGE_HEIGHT, getActivity ().getContentResolver (), uri );
			proofFile = bitmap;
			updateProofFileComponents ();
		} catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
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
}