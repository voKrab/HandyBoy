package com.vallverk.handyboy.view.jobdescription;

import android.animation.LayoutTransition;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject.AddonServiceAPIParams;
import com.vallverk.handyboy.model.api.JobAddonsAPIObject;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject.TypeJobServiceParams;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.model.job.JobTypeManager;
import com.vallverk.handyboy.model.job.TypeJob;
import com.vallverk.handyboy.model.job.TypejobLevel;
import com.vallverk.handyboy.server.ServerManager;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class BaseController
{
	protected JobDescriptionViewFragment fragment;
	protected View saveButton;
	protected LinearLayout detailsContainer;
	protected View headerContainer;
	protected TypeJobServiceAPIObject job;
	protected TypeJob typejob;
	private DistancePriceView distancePriceView;
	private EditText descriptionEditText;
	private JobLevelView jobLevelView;
	private EditText experienceEditText;
	private PricePerHourView pricePerHourView;
	private Bitmap proofFile;
	private TextView fileStatusTextView;
	protected LinearLayout baseComponents;
	protected LayoutTransition layoutTransition;
	protected MainActivity controller;
	
	private JobTypeManager jobTypeManager;

	public BaseController ( TypeJobServiceAPIObject job, TypeJob typeJob )
	{
		this.controller = MainActivity.getInstance ();
		this.job = job;
		this.typejob = typeJob;
	}

	public abstract View createView ( LayoutInflater inflater, final JobDescriptionViewFragment fragment );

	public void photoFromEditor ( Bitmap bitmap )
	{
		proofFile = bitmap;
//		updateProofFileComponents ();
	}
	
	

	protected void createView ( View view )
	{
		pricePerHourView = ( PricePerHourView ) view.findViewById ( R.id.pricePerHourView1 );
		distancePriceView = ( DistancePriceView ) view.findViewById ( R.id.distancePriceView );
		descriptionEditText = ( EditText ) view.findViewById ( R.id.descriptionEditText );
		jobLevelView = ( JobLevelView ) view.findViewById ( R.id.jobLevelView );
		experienceEditText = ( EditText ) view.findViewById ( R.id.experienceEditText );
		saveButton = view.findViewById ( R.id.saveButton );
		baseComponents = ( LinearLayout ) view.findViewById ( R.id.baseComponents );
//		LayoutTransition layoutTransition = ( ( ViewGroup ) view ).getLayoutTransition ();
//		layoutTransition.enableTransitionType ( LayoutTransition.CHANGING );
//		baseComponents.setLayoutTransition ( layoutTransition );
		
//		proofFileImageView = ( ImageView ) view.findViewById ( R.id.proofFileImageView );
//		if ( proofFileImageView != null )
//		{
//			fileStatusTextView = ( TextView ) view.findViewById ( R.id.fileStatusTextView );
//			updateProofFileComponents ();
//			View showHelpTextView = view.findViewById ( R.id.showHelpTextView );
//			showHelpTextView.setOnClickListener ( new OnClickListener ()
//			{
//				@Override
//				public void onClick ( View v )
//				{
//					FragmentTransaction ft = MainActivity.getInstance ().getSupportFragmentManager ().beginTransaction ();
//					DialogFragment newFragment = new HelpDialogFragment ();
//					newFragment.show ( ft, null );
//				}
//			} );
//			View chooseFileButton = view.findViewById ( R.id.chooseFileButton );
//			chooseFileButton.setOnClickListener ( new OnClickListener ()
//			{
//				@Override
//				public void onClick ( View v )
//				{
//					fragment.chooseMedia ( MediaType.PHOTO );
//				}
//			} );
//			View proofFileImageView = view.findViewById ( R.id.proofFileImageView );
//			proofFileImageView.setOnClickListener ( new OnClickListener ()
//			{
//				@Override
//				public void onClick ( View v )
//				{
//					FragmentTransaction ft = MainActivity.getInstance ().getSupportFragmentManager ().beginTransaction ();
//					DialogFragment newFragment = new BitmapPreviewDialogFragment ( proofFile );
//					newFragment.show ( ft, null );
//				}
//			} );
//		}

		saveButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				save ();
			}
		} );

		detailsContainer = ( LinearLayout ) view.findViewById ( R.id.detailsContainer );
		headerContainer = view.findViewById ( R.id.headerContainer );
		detailsContainer.setVisibility ( View.GONE );
		headerContainer.setClickable ( true );
		headerContainer.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				boolean isHide = detailsContainer.getVisibility () == View.GONE;
				if ( isHide )
				{
					showDetails ();
				} else
				{
					hideDetails ();
				}
			}
		} );
//		layoutTransition = new LayoutTransition ();
//		layoutTransition.enableTransitionType ( LayoutTransition.CHANGING );
		setupLayoutAnimation ( view );
		updateComponents ();
	}

	private void updateComponents ()
	{
		jobTypeManager = JobTypeManager.getInstance ();
        pricePerHourView.setRecommendPrice ( typejob.getRecommendMinCost (),  typejob.getRecommendMaxCost () );
        pricePerHourView.setMinMaxPrice ( typejob.getMinCost (), typejob.getMaxCost () );
        pricePerHourView.setPrice(typejob.getRecommendCost());

        if ( job == null )
		{
			return;
		}
		//int distancePrice = Integer.parseInt ( job.getString ( TypeJobServiceParams.PRICE_DISTANCE ) );
		//if ( distancePrice != 0 )
		//{
		//	distancePriceView.setPrice ( distancePrice );
		//}
		descriptionEditText.setText ( job.getString ( TypeJobServiceParams.DESCRIPTION ) );
		TypejobLevel jobLevel = TypejobLevel.fromString ( job.getString ( TypeJobServiceParams.LEVEL ) );
		jobLevelView.setLevel ( jobLevel );
		pricePerHourView.setPrice ( Integer.parseInt ( job.getValue ( TypeJobServiceParams.PRICE ).toString() ) );

		
		updatePriceAddons ();
	}

	protected void updatePriceAddons ()
	{
		AddonPriceViewBase[] priceAddons = getPriceAddons ();
		if ( priceAddons == null )
		{
			return;
		}

		for ( AddonPriceViewBase addonView : priceAddons )
		{
			JobAddonsAPIObject addon = ( JobAddonsAPIObject ) addonView.getTag ();
			AddonServiceAPIObject addonService = getAddonService ( addon.getId () );
			addonView.updateComponents ( addonService );
		}
	}
	
	protected void updateSelection ( CheckBox checkBox, View container, AddonPriceViewBase[] addons )
	{
		for ( AddonPriceViewBase addon : addons )
		{
			if ( addon.isChecked () )
			{
				checkBox.setChecked ( true );
				container.setVisibility ( View.VISIBLE );
				return;
			}
		}
		checkBox.setChecked ( false );
		container.setVisibility ( View.GONE );
	}

	protected AddonServiceAPIObject getAddonService ( String id )
	{
		for ( AddonServiceAPIObject addon : fragment.addons )
		{
			if ( addon.getString ( AddonServiceAPIParams.JOB_ADDONS_ID ).equals ( id ) )
			{
				return addon;
			}
		}
		return null;
	}

	private void setupLayoutAnimation ( View view )
	{
		LayoutTransition layoutTransition = ( ( ViewGroup ) view ).getLayoutTransition ();
		layoutTransition.enableTransitionType ( LayoutTransition.CHANGING );
		ViewGroup detailsContainer = ( ViewGroup ) view.findViewById ( R.id.detailsContainer );
		detailsContainer.setLayoutTransition ( layoutTransition );
		baseComponents.getLayoutTransition ().enableTransitionType ( LayoutTransition.CHANGING );
	}

//	private void updateProofFileComponents ()
//	{
//		if ( proofFile == null )
//		{
//			proofFileImageView.setVisibility ( View.GONE );
//			fileStatusTextView.setVisibility ( View.VISIBLE );
//		} else
//		{
//			proofFileImageView.setImageBitmap ( proofFile );
//			proofFileImageView.setVisibility ( View.VISIBLE );
//			fileStatusTextView.setVisibility ( View.GONE );
//		}
//	}

	protected void save ()
	{
		final String description = descriptionEditText.getText ().toString ().trim ();
		if ( description.isEmpty () )
		{
			Toast.makeText ( MainActivity.getInstance (), R.string.describe_your_services, Toast.LENGTH_LONG ).show ();
			return;
		}
		
//		if ( proofFileImageView != null && proofFile == null )
//		{
//			Toast.makeText ( MainActivity.getInstance (), R.string.upload_proof_of_current_personal_trainers_insurance, Toast.LENGTH_LONG ).show ();
//			return;
//		}
		
		String result = postFieldsValidate ();
		if ( !result.isEmpty () )
		{
			Toast.makeText ( MainActivity.getInstance (), result, Toast.LENGTH_LONG ).show ();
			return;
		}

		new AsyncTask < Void, Void, String > ()
		{
			MainActivity controller = MainActivity.getInstance ();
			private boolean isNewJob;

			public void onPreExecute ()
			{
				super.onPreExecute ();
				controller.showLoader ();
			}

			public void onPostExecute ( String result )
			{
				super.onPostExecute ( result );
				controller.hideLoader ();
				if ( result.isEmpty () )
				{
					if ( isNewJob )
					{
						fragment.addToDefault ( job );
					}
					hideDetails ();
				} else
				{
					Toast.makeText ( controller, result, Toast.LENGTH_LONG ).show ();
				}
			}

			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					APIManager apiManager = APIManager.getInstance ();
					UserAPIObject user = apiManager.getUser ();
					String proofUri = "";
//					if ( proofFileImageView != null )
//					{
//						proofUri = apiManager.saveBitmap ( proofFile );
//					}

					isNewJob = isNewJob ();
					if ( job == null )
					{
						job = new TypeJobServiceAPIObject ();
					}
					job.putValue ( TypeJobServiceParams.SERVICE_ID, user.getString ( UserParams.SERVICE_ID ) );
					job.putValue ( TypeJobServiceParams.TYPEJOB_ID, "" + typejob.getId () );
					job.putValue ( TypeJobServiceParams.DESCRIPTION, description );
					job.putValue ( TypeJobServiceParams.PRICE, "" + pricePerHourView.getPrice () );
					//job.putValue ( TypeJobServiceParams.PRICE_DISTANCE, distancePriceView.getPrice () );
					job.putValue ( TypeJobServiceParams.APROVE_FILE, proofUri );
					job.putValue ( TypeJobServiceParams.LEVEL, jobLevelView.toString () );
                    job.on ();
					if ( isNewJob )
					{
						result = apiManager.insert ( job, ServerManager.TYPE_JOB_SERVICE_SAVE );
					} else
					{
						result = apiManager.update ( job, ServerManager.TYPE_JOB_SERVICE_SAVE );
					}
					saveAddons ();
				} catch ( Exception ex )
				{
					ex.printStackTrace ();
					result = ex.getMessage ();
				}
				return result;
			}
		}.execute ();
	}
	
	protected String postFieldsValidate ()
	{
		return "";
	}

	protected void saveAddons () throws Exception
	{
		AddonPriceViewBase[] addonItems = getPriceAddons ();
		if ( addonItems == null )
		{
			return;
		}
		JSONArray jsonArray = new JSONArray ();
		for ( AddonPriceViewBase item : addonItems )
		{
			JSONObject jsonObject = new JSONObject ();
			JobAddonsAPIObject addonItem = ( JobAddonsAPIObject ) item.getTag ();
			jsonObject.put ( AddonServiceAPIParams.TYPE_JOB_SERVICE_ID.toString (), job.getId () );
			jsonObject.put ( AddonServiceAPIParams.JOB_ADDONS_ID.toString (), addonItem.getId () );
			jsonObject.put ( "isChecked", item.isChecked () );
            if(item.getPrice () > 0) {
                jsonObject.put(AddonServiceAPIParams.PRICE.toString(), item.getPrice());
            }else {
                jsonObject.put(AddonServiceAPIParams.PRICE.toString(), "");
            }
			jsonArray.put(jsonObject);
		}
		ServerManager.postRequest ( ServerManager.ADDONS_SERVICE_SET, jsonArray );
	}

	public void hideDetails ()
	{
//        if ( job != null && job.isRestore() )
//        {
//            job.off ();
//        }
		detailsContainer.setVisibility ( View.GONE );
		fragment.setCurrentController ( null );
	}

	public void showDetails ()
	{
		detailsContainer.setVisibility ( View.VISIBLE );
		fragment.setCurrentController ( this );
	}

	public boolean isNewJob ()
	{
		return job == null || job.isRestore ();
	}

	public TypeJob getTypeJob ()
	{
		return typejob;
	}

	public void onlyHideDetails ()
	{
		detailsContainer.setVisibility ( View.GONE );
	}

    public TypeJobServiceAPIObject getJob(){
        return job;
    }
	
	protected AddonPriceViewBase[] getPriceAddons ()
	{
		return null;
	}
}
