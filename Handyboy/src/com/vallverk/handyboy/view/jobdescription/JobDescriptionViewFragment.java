package com.vallverk.handyboy.view.jobdescription;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject.TypeJobServiceParams;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.model.job.JobTypeManager;
import com.vallverk.handyboy.model.job.TypeJob;
import com.vallverk.handyboy.model.job.TypeJobEnum;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobDescriptionViewFragment extends BaseFragment
{
	private UserAPIObject user;
	private View backImageView;
	private View backTextView;
	private Map < TypeJob, ImageView > jobTypesMap;
	private View additionalInfoContainer;
	private int[] resources;
	private int[] selectedResources;
    private int[] disabledResources;
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
	private LinearLayout jobsContainer;
	private BaseController currentController;
	private Map < TypeJob, View > jobDetailsMap;
	private List < TypeJobServiceAPIObject > defaultJobs;
	private List < String > enabledTypejobs;
	protected List < AddonServiceAPIObject > addons;
	
	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.job_description_layout, null );

		backImageView = view.findViewById ( R.id.backImageView );
		backTextView = view.findViewById ( R.id.backTextView );
		jobTypesMap = new HashMap < TypeJob, ImageView > ();
		JobTypeManager jobTypeManager = JobTypeManager.getInstance ();
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.PERSONAL_TRAINER ), ( ImageView ) view.findViewById ( R.id.jobType1ImageView ) );
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.POOL_BOY ), ( ImageView ) view.findViewById ( R.id.jobType2ImageView ) );
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.ERRAND_BOY ), ( ImageView ) view.findViewById ( R.id.jobType7ImageView ) );
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.BARTENDER ), ( ImageView ) view.findViewById ( R.id.jobType4ImageView ) );
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.MASSEUR ), ( ImageView ) view.findViewById ( R.id.jobType5ImageView ) );
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.HOUSEKEEPER ), ( ImageView ) view.findViewById ( R.id.jobType8ImageView ) );
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.YARD_WORK ), ( ImageView ) view.findViewById ( R.id.jobType9ImageView ) );
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.SERVER_WAITER ), ( ImageView ) view.findViewById ( R.id.jobType10ImageView ) );
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.GOGO_BOY ), ( ImageView ) view.findViewById ( R.id.jobType11ImageView ) );
		jobTypesMap.put ( jobTypeManager.getJobType ( TypeJobEnum.CAR_WASH ), ( ImageView ) view.findViewById ( R.id.jobType6ImageView ) );
		jobsContainer = ( LinearLayout ) view.findViewById ( R.id.jobsContainer );

		return view;
	}

	@Override
	protected void init ()
	{
		new AsyncTask < Void, Void, String > ()
		{
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
					updateComponents ();
					addListeners ();
				} else
				{
					controller.onBackPressed ();
					Toast.makeText ( controller, result, Toast.LENGTH_LONG ).show ();
				}
			}

			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					List < TypeJobServiceAPIObject > typejobs = APIManager.getInstance ().getTypeJobs ( user );
					defaultJobs = typejobs;
					loadEnabledTypejobs ();
					loadAddons ();
				} catch ( Exception ex )
				{
					result = ex.getMessage ();
					ex.printStackTrace ();
				}
				return result;
			}
		}.execute ();
	}

	protected void loadAddons () throws Exception
	{
		addons = APIManager.getInstance ().loadList ( ServerManager.ADDONS_SERVICE_GET.replace ( "serviceId=1", "serviceId=" + user.getString ( UserParams.SERVICE_ID ) ), AddonServiceAPIObject.class );
	}

	protected void loadEnabledTypejobs () throws Exception
	{
		String responseString = ServerManager.getRequest ( ServerManager.SERVICE_GET_ENABLED_TYPE_JOBS.replace ( "serviceId=1", "serviceId=" + user.getString ( UserParams.SERVICE_ID ) ) );
		ServerManager.checkErrors ( responseString );
		JSONObject responceObject = new JSONObject ( responseString );
		String list = responceObject.getString ( "list" );
		list = list.substring ( 1, list.length () - 1 ).replaceAll ( "\"", "" ).trim ();
		enabledTypejobs = Arrays.asList ( list.split ( "," ) );
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );

		user = APIManager.getInstance ().getUser ();
		resources = new int[] { R.drawable.dumbbels_na, R.drawable.circle_na, R.drawable.man_na, R.drawable.martini_na, R.drawable.hand_na, R.drawable.venik_na, R.drawable.grass_na, R.drawable.foodcover_na, R.drawable.panties_na, R.drawable.car_na };
		selectedResources = new int[] { R.drawable.dumbbels_a, R.drawable.circle_a, R.drawable.man_a, R.drawable.martini_a, R.drawable.hand_a, R.drawable.venik_a, R.drawable.grass_a, R.drawable.foodcover_a, R.drawable.panties_a, R.drawable.car_a };
        disabledResources = new int[] { R.drawable.dumbbels_nona, R.drawable.circle_nona, R.drawable.man_nona, R.drawable.martini_nona, R.drawable.hand_nona, R.drawable.venik_nona, R.drawable.grass_nona, R.drawable.foodcover_nona, R.drawable.panties_nona, R.drawable.car_nona };
    }

	private void updateComponents ()
	{
		updateJobTypesIndicators ();
		updateJobsContainer ();
	}

	private void addJobTypeIndicatorsListeners ()
	{
		for ( final TypeJob jobTypeKey : jobTypesMap.keySet () )
		{
			ImageView imageView = jobTypesMap.get ( jobTypeKey );
			imageView.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View v )
				{
					clickOnJob ( jobTypeKey );
				}
			} );
		}
	}

    private void clickOnJob ( final TypeJob jobTypeKey )
    {
        if ( containsInDefaultOnJobs ( defaultJobs, jobTypeKey ) )
        {
            if ( currentController != null )
            {
                currentController.hideDetails ();
            }
          // if ( defaultJobs.size () == 1 )
            if(getCountOnTypeJobs(defaultJobs) == 1 || getCountOnTypeJobs(defaultJobs) == 0)
            {
                Toast.makeText ( controller, R.string.you_can_not_delete_the_last_type_of_work, Toast.LENGTH_LONG ).show ();
                return;
            }
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener ()
            {
                @Override
                public void onClick ( DialogInterface dialog, int which )
                {
                    switch ( which )
                    {
                        case DialogInterface.BUTTON_POSITIVE:
                        {
                            removeJob ( get ( defaultJobs, jobTypeKey ) );
                            break;
                        }
                        // Yes button clicked

                        case DialogInterface.BUTTON_NEGATIVE:
                        {
                            // No button clicked
                            break;
                        }
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder ( controller );
            builder.setMessage ( R.string.remove_job ).setPositiveButton ( "Yes", dialogClickListener ).setNegativeButton ( "No", dialogClickListener ).show ();
        } else
        {
            if ( !isEnabledJob ( jobTypeKey ) )
            {
                Toast.makeText ( controller, R.string.this_type_of_work_is_not_available_to_you, Toast.LENGTH_LONG ).show ();
                return;
            }
            if ( currentController != null && currentController.getTypeJob () == jobTypeKey && currentController.isNewJob () )
            {
                currentController.hideDetails ();
            } else
            {
                if ( currentController != null )
                {
                    currentController.hideDetails ();
                }
                final LayoutInflater inflater = ( LayoutInflater ) controller.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
                TypeJobServiceAPIObject jobApiObject = get ( defaultJobs, jobTypeKey );
                if ( jobApiObject != null )
                {
                    jobApiObject.restore ();
                }
                BaseController viewController = ControllerFactory.create ( jobApiObject, jobTypeKey );
                final View view = viewController.createView ( inflater, JobDescriptionViewFragment.this );
                jobsContainer.addView ( view );
                jobDetailsMap.put ( jobTypeKey, view );
                viewController.showDetails ();
            }
        }
        updateJobTypesIndicators ();
    }

    private void removeJob ( final TypeJobServiceAPIObject job )
	{
		final TypeJob typejob = job.getTypeJob ();
		new AsyncTask < Void, Void, String > ()
		{
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
//					defaultJobs.remove ( job );
					removeJob ( typejob );
					updateJobTypesIndicators ();
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
                    job.off ();
					job.update ( ServerManager.TYPE_JOB_SERVICE_SAVE );
				} catch ( Exception ex )
				{
					ex.printStackTrace ();
					result = ex.getMessage ();
				}
				return result;
			}
		}.execute ();
	}
	
	protected void removeJob ( TypeJob typejob )
	{
		jobsContainer.removeView ( jobDetailsMap.get ( typejob ) );
		jobDetailsMap.remove ( typejob );
	}

	protected void updateJobTypesIndicators ()
	{
		JobTypeManager jobTypeManager = JobTypeManager.getInstance ();
		for ( final TypeJob jobTypeKey : jobTypesMap.keySet () )
		{
			int index = Arrays.asList ( TypeJobEnum.values () ).indexOf ( jobTypeKey.getEnumValue () );
			ImageView imageView = jobTypesMap.get ( jobTypeKey );
            imageView.setImageResource ( isEnabledJob ( jobTypeKey ) ? resources[index] : disabledResources[index] );
		}
		for ( TypeJobServiceAPIObject jobAPIObject : defaultJobs )
		{
            if ( jobAPIObject.isOff () || jobAPIObject.isRestore () )
            {
                continue;
            }
            TypeJob jobType = jobTypeManager.getJobType ( jobAPIObject.getString ( TypeJobServiceParams.TYPEJOB_ID ) );
			int selectedIndex = Arrays.asList ( TypeJobEnum.values () ).indexOf ( jobType.getEnumValue () );
			ImageView imageView = jobTypesMap.get ( jobType );
			imageView.setImageResource ( selectedResources[selectedIndex] );
		}
		if ( currentController != null && currentController.isNewJob () )
		{
			TypeJob jobType = currentController.getTypeJob ();
			ImageView imageView = jobTypesMap.get ( currentController.getTypeJob () );
			int selectedIndex = Arrays.asList ( TypeJobEnum.values () ).indexOf ( jobType.getEnumValue () );
			imageView.setImageResource ( selectedResources[selectedIndex] );
		}
	}

    private boolean isEnabledJob ( TypeJob jobTypeKey )
    {
        return enabledTypejobs.contains ( "" + jobTypeKey.getId () );
    }

    protected TypeJobServiceAPIObject get ( List < TypeJobServiceAPIObject > list, TypeJob typejob )
	{
		for ( TypeJobServiceAPIObject job : list )
		{
			if ( job.getTypeJob () == typejob )
			{
				return job;
			}
		}
		return null;
	}

	protected boolean containsInDefaultOnJobs ( List < TypeJobServiceAPIObject > list, TypeJob typejob )
	{
		for ( TypeJobServiceAPIObject job : list )
		{
            if ( job.isOff () || job.isRestore () )
            {
                continue;
            }
			if ( job.getTypeJob () == typejob )
			{
				return true;
			}
		}
		return false;
	}

    private int getCountOnTypeJobs(List < TypeJobServiceAPIObject > list){
        int count = 0;
        for(TypeJobServiceAPIObject tempTypeJob : list){
            Log.d("Jobs", tempTypeJob.getName() + " " + tempTypeJob.isOn());
            if(tempTypeJob.isOn())
            {
                count ++;
            }
        }
        Log.d("Jobs", "=======" + count + "=======" );
        return  count;
    }

	private void updateJobsContainer ()
	{
		jobDetailsMap = new HashMap < TypeJob, View > ();
		JobTypeManager jobTypeManager = JobTypeManager.getInstance ();
		jobsContainer.removeAllViews ();
		final LayoutInflater inflater = ( LayoutInflater ) controller.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
		for ( TypeJobServiceAPIObject jobAPIObject : defaultJobs )
		{
            if ( jobAPIObject.isOff () )
            {
                continue;
            }
			TypeJob jobType = jobAPIObject.getTypeJob ();
			BaseController viewController = ControllerFactory.create ( jobAPIObject, jobType );
			final View view = viewController.createView ( inflater, this );
			jobsContainer.addView ( view );
			jobDetailsMap.put ( jobType, view );
		}
	}

	protected void addListeners ()
	{
		addJobTypeIndicatorsListeners ();
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

	protected void next ()
	{
		// if ( jobType == null )
		// {
		// Toast.makeText ( getActivity (), R.string.choose_job_type,
		// Toast.LENGTH_LONG ).show ();
		// return;
		// }
		// String serviceDescription = descriptionEditText.getText ().toString
		// ().trim ();
		// if ( serviceDescription.isEmpty () )
		// {
		// Toast.makeText ( getActivity (), R.string.describe_your_services,
		// Toast.LENGTH_LONG ).show ();
		// return;
		// }
		// if ( jobType.isRequiredApprove () )
		// {
		// if ( proofFile == null )
		// {
		// Toast.makeText ( getActivity (),
		// R.string.upload_proof_of_current_personal_trainers_insurance,
		// Toast.LENGTH_LONG ).show ();
		// return;
		// }
		// }
		//
		// int price = priceSeekBar.getProgress () + jobType.getMinCost ();
		// TypeJobServiceAPIObject typejobAPIObject = new
		// TypeJobServiceAPIObject ( "", "" + jobType.getId (), price, 0,
		// TypejobLevel.SOME_EXPERIENCE.toString (), "" );
		// controller.setServiceDescription ( serviceDescription );
		// controller.setJobType ( jobType );
		// if ( jobType.isRequiredApprove () )
		// {
		// controller.setApproveFile ( proofFile );
		// }
		// controller.setTypejobAPiObject ( typejobAPIObject );
		// controller.nextStep ();
	}

	protected void photoFromEditor ( Uri uri )
	{
		try
		{
			Bitmap bitmap = Tools.decodeSampledBitmapFromResource ( MainActivity.MAX_POSTED_IMAGE_WIDTh, MainActivity.MAX_POSTED_IMAGE_HEIGHT, getActivity ().getContentResolver (), uri );
			currentController.photoFromEditor ( bitmap );
		} catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
	}

	public void setCurrentController ( BaseController newCurrentController )
	{
		if ( currentController != null )
		{
			if ( currentController.isNewJob () )
			{
				removeJob ( currentController.getTypeJob () );
			}
			currentController.onlyHideDetails ();
		}
		this.currentController = newCurrentController;
		updateJobTypesIndicators ();
	}

	public void addToDefault ( TypeJobServiceAPIObject jobObject )
	{
        boolean isContains = false;
        int index = 0;
        for(TypeJobServiceAPIObject tempObject : defaultJobs){
            if(tempObject.getId() == jobObject.getId()){
                isContains  = true;
                defaultJobs.set(index, jobObject);
            }
            index++;
        }

        if(!isContains){
            defaultJobs.add (jobObject );
        }

		updateJobTypesIndicators ();
	}
}