package com.vallverk.handyboy.view.controller;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.UserStatus;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.GalleryAPIObject;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserDetailsAPIObject;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject.TypeJobServiceParams;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.model.api.UserDetailsAPIObject.UserDetailsParams;
import com.vallverk.handyboy.model.job.TypeJob;
import com.vallverk.handyboy.model.job.TypeJobEnum;
import com.vallverk.handyboy.server.ServerManager;

/**
 * 
 * @author Oleg Barkov
 * 
 *         Controller for registration step two
 * 
 */
public class RegistrationController
{
	/**
	 * Current registration state
	 */
	private VIEW_STATE currentState;
	/**
	 * Previous registration state
	 */
	private VIEW_STATE prevState;
	/**
	 * Main controller registration state
	 */
	private MainActivity controller;

	/**
	 * Service or Customer
	 */
	private UserStatus userStatus;
	/**
	 * Service details object
	 */
	private UserDetailsAPIObject userDetails;
	private TypeJobServiceAPIObject typejobAPiObject;
	private Bitmap avatar;
	private TypeJob jobType;
	/**
	 * Job type license
	 */
	private Bitmap jobTypeApproveFile;
	private long trainerDate;

	private String licenceNumber;
	private String uState;
	private long massageDate;
	/**
	 * Selfie
	 */
	private Bitmap selfieBitmap;
	private String typeJobs;

	public RegistrationController ()
	{
		prevState = null;
		currentState = null;
		controller = MainActivity.getInstance ();
		userStatus = UserStatus.NEW;
	}

	public UserDetailsAPIObject getUserDetails ()
	{
		return userDetails;
	}

	public void setUserDetails ( UserDetailsAPIObject userDetails )
	{
		this.userDetails = userDetails;
	}

	/**
	 * Next registration step
	 */
	public void nextStep ()
	{
		prevState = currentState;
		currentState = getNextStep();
		applyState ();
	}

	/**
	 * Previous registration step
	 */
	public void prevStep ()
	{
		currentState = prevState;
		prevState = getPrevStep ();
		applyState();
	}

	/**
	 * apply new registration state
	 */
	private void applyState ()
	{
		try {
			if (currentState == VIEW_STATE.CHOOSE_USER_TYPE) {
				prevState = null;
				userStatus = UserStatus.NEW;
				// userDetails = null;
			}
			controller.setState(currentState);
		}catch (Exception e){
		}
	}

	private VIEW_STATE getNextStep ()
	{
		if ( currentState == null )
		{
			return VIEW_STATE.CHOOSE_USER_TYPE;
		}
		switch ( currentState )
		{
			case CHOOSE_USER_TYPE:
			{
				if( userStatus == UserStatus.NEW_SERVICE){
					return VIEW_STATE.REGISTRATION_SERVICE_BIO;
				}else{
					//createCustomer();
					finish();
				}
				break;
				//return userStatus == UserStatus.NEW_SERVICE ? VIEW_STATE.REGISTRATION_SERVICE_BIO : VIEW_STATE.REGISTRATION_CUSTOMER_BIO;
			}
			case REGISTRATION_SERVICE_BIO:
			{
				return isNeedLicense () ? VIEW_STATE.REGISTRATION_SERVICE_LICENSE : VIEW_STATE.REGISTRATION_SERVICE_SELPHIE;
			}
			case REGISTRATION_SERVICE_SELPHIE:
			{
				//return VIEW_STATE.REGISTRATION_SERVICE_TERMS;
                return VIEW_STATE.REGISTRATION_SERVICE_VIDEO;
			}
			case REGISTRATION_SERVICE_LICENSE:
			{
				return VIEW_STATE.REGISTRATION_SERVICE_SELPHIE;
			}
			/*
			 * case REGISTRATION_JOB_DESCRIPTION: { return
			 * VIEW_STATE.REGISTRATION_SERVICE_TERMS; }
			 */
			/*case REGISTRATION_SERVICE_TERMS:
			{
				return VIEW_STATE.REGISTRATION_SERVICE_VIDEO;
			}*/
			case REGISTRATION_SERVICE_VIDEO:
            {
                return VIEW_STATE.CHOOSE_USER_TYPE;
            }
				/*
				 * case REGISTRATION_CUSTOMER_BIO: { return
				 * VIEW_STATE.REGISTRATION_CREDIT_CARD; }
				 */
		}
	    return null;
	}

	private boolean isNeedLicense ()
	{
		if ( typeJobs == null )
		{
			return false;
		}
		String[] values = typeJobs.split ( "," );
		for ( String value : values )
		{
			value = value.trim ();
			if ( TypeJobEnum.PERSONAL_TRAINER.toString ().equals ( value ) || TypeJobEnum.MASSEUR.toString ().equals ( value ) )
			{
				return true;
			}
		}
		return false;
	}

	private VIEW_STATE getPrevStep ()
	{
		switch ( currentState )
		{
			case CHOOSE_USER_TYPE:
			{
				return null;
			}
			//case REGISTRATION_CUSTOMER_BIO: //TODO
			case REGISTRATION_SERVICE_BIO:
			{
				return VIEW_STATE.CHOOSE_USER_TYPE;
			}
			/*
			 * case REGISTRATION_JOB_DESCRIPTION: { return
			 * VIEW_STATE.REGISTRATION_SERVICE_SELPHIE; }
			 */
			case REGISTRATION_SERVICE_SELPHIE:
			{
				return isNeedLicense () ? VIEW_STATE.REGISTRATION_SERVICE_LICENSE : VIEW_STATE.REGISTRATION_SERVICE_BIO;
			}
			/*case REGISTRATION_SERVICE_TERMS:
			{
				return VIEW_STATE.REGISTRATION_SERVICE_SELPHIE;
			}*/
			case REGISTRATION_SERVICE_VIDEO:
			{
				//return VIEW_STATE.REGISTRATION_SERVICE_TERMS;
                return VIEW_STATE.REGISTRATION_SERVICE_SELPHIE;
			}

			case REGISTRATION_SERVICE_LICENSE:
			{
				return VIEW_STATE.REGISTRATION_SERVICE_BIO;
			}
			/*
			 * case REGISTRATION_CREDIT_CARD: { return userStatus ==
			 * UserStatus.NEW_SERVICE ? VIEW_STATE.REGISTRATION_SERVICE_VIDEO :
			 * VIEW_STATE.REGISTRATION_CUSTOMER_BIO; }
			 */
		}
		return null;
	}

	public void setUserStatus ( UserStatus userStatus )
	{
		this.userStatus = userStatus;
	}

	public Bitmap getAvatar ()
	{
		return avatar;
	}

	public void setAvatar ( Bitmap avatar )
	{
		this.avatar = avatar;
	}

	public void setServiceDescription ( String serviceDescription )
	{
		userDetails.putValue ( UserDetailsParams.DESCRIPTION, serviceDescription );
	}

	public void setJobType ( TypeJob jobType )
	{
		this.jobType = jobType;
	}

	public void setServicePrice ( int price )
	{
		typejobAPiObject.putValue ( TypeJobServiceParams.PRICE, price );
	}

	public void setApproveFile ( Bitmap proofFile )
	{
		this.jobTypeApproveFile = proofFile;
	}

	public Bitmap getApproveFile ()
	{
		return jobTypeApproveFile;
	}

	public TypeJob getJobType ()
	{
		return jobType;
	}

	public int getServicePrice ()
	{
		return ( Integer ) typejobAPiObject.getValue ( TypeJobServiceParams.PRICE );
	}

	public String getServiceDescription ()
	{
		return ( String ) userDetails.getValue ( UserDetailsParams.DESCRIPTION );
	}

	public void setLicenceNumber ( String licenceNumber )
	{
		this.licenceNumber = licenceNumber;
	}

	public String getLicenceNumber ()
	{
		return licenceNumber;
	}

	public void setUState ( String uState )
	{
		this.uState = uState;
	}

	public String getUState ()
	{
		return uState;
	}

	public void setMassageDate ( long massageDate )
	{
		this.massageDate = massageDate;
	}

	public long getMassageDate ()
	{
		return massageDate;
	}

	public void setTrainerDate ( long trainerDate )
	{
		this.trainerDate = trainerDate;
	}

	public long getTrainerDate ()
	{
		return trainerDate;
	}

	/**
	 * try save collected data and enter in application
	 */
	public void finish ()
	{
		switch ( userStatus )
		{
			case NEW_SERVICE:
			{
				createService ();
				break;
			}

			case NEW_CUSTOMER:
			{
				createCustomer ();
				break;
			}
		}
	}

	/**
	 * 1. If jobType is required approve file ( Personal Treiner or Masseur )
	 * save this file 2. Insert Service with jobType 3. Save avatar 4. Update
	 * user. avatar, serviceId, status
	 **/




	private void createService ()
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
				if ( result.isEmpty () )// success
				{
					controller.enter ();
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
					/*
					 * 1. Insert Service
					 * 2. If jobType is required approve file
					 * ( Personal Treiner or Masseur ) save this file 
					 * 3. Save avatar
					 * 4. Update user. avatar, serviceId, status
					 */
					APIManager apiManager = APIManager.getInstance ();
					apiManager.insert ( userDetails, ServerManager.USER_DETAILS_INSERT_URI );
					UserAPIObject user = apiManager.getUser ();
					user.putValue ( UserParams.SERVICE_ID, userDetails.getId () );
					user.putValue ( UserParams.STATUS, userStatus.toString () );
                    if(avatar != null) {
                        String avatarUrl = apiManager.saveBitmap(avatar);
                        user.putValue(UserParams.AVATAR, avatarUrl);
                    }
					apiManager.update ( user, ServerManager.USER_UPDATE_URI );


                    //save selfie
                    String selphieUrl = "";

                    if ( getSelphie() != null )
                    {
                        selphieUrl = apiManager.saveBitmap ( getSelphie() );
                    }

                    JSONObject jsonObject = new JSONObject ();
                    jsonObject.accumulate ( GalleryAPIObject.GalleryAPIParams.SERVICE_ID.toString (), user.getValue ( UserParams.SERVICE_ID ).toString () );
                    jsonObject.accumulate ( GalleryAPIObject.GalleryAPIParams.URL.toString (), selphieUrl );
                    jsonObject.accumulate ( GalleryAPIObject.GalleryAPIParams.TYPE.toString (), "selfie" );
                    jsonObject.accumulate ( GalleryAPIObject.GalleryAPIParams.STATUS.toString (), "" );
                    GalleryAPIObject galleryAPIObject = new GalleryAPIObject ( jsonObject );
                    result = apiManager.insert ( galleryAPIObject, ServerManager.ADD_GALLERY_URI );

					
					//Save license
					if ( jobTypeApproveFile != null && trainerDate != 0 )
					{
						String url = apiManager.saveBitmap ( jobTypeApproveFile );
						JSONObject licenceParams = new JSONObject ();
						licenceParams.accumulate ( UserParams.SERVICE_ID.toString (), userDetails.getId () );
						licenceParams.accumulate ( "type", 2 );
						licenceParams.accumulate ( "file", url );
						licenceParams.accumulate ( "expirie", getTrainerDate () );
						licenceParams.accumulate ( "number", "" );
						licenceParams.accumulate ( "state", "" );
						ServerManager.postRequest ( ServerManager.LICENSE_ADD, licenceParams );
					}

					if ( licenceNumber != null && massageDate != 0 )
					{
						JSONObject licenceParams = new JSONObject ();
						licenceParams.accumulate ( UserParams.SERVICE_ID.toString (), userDetails.getId () );
						licenceParams.accumulate ( "type", 1 );
						licenceParams.accumulate ( "file", "" );
						licenceParams.accumulate ( "expirie", getMassageDate () );
						licenceParams.accumulate ( "number", getLicenceNumber () );
						licenceParams.accumulate ( "state", getUState () );
						ServerManager.postRequest ( ServerManager.LICENSE_ADD, licenceParams );
					}


				} catch ( Exception ex )
				{
					ex.printStackTrace ();
					result = ex.getMessage ();
				}
				return result;
			}
		}.execute ();
	}

	
	private void createCustomer ()
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
				if ( result.isEmpty () )// success
				{
					controller.enter ();
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
//					user.putValue ( UserParams.STATUS, userStatus.toString () ); // 23.12.2014 change
					user.putValue ( UserParams.STATUS, UserStatus.NEW_CUSTOMER.toString () );
					user.putValue ( UserParams.ROLE, UserStatus.ACCEPTED.toString () );
                    if(avatar != null) {
                        String avatarUrl = apiManager.saveBitmap(avatar);
                        user.putValue(UserParams.AVATAR, avatarUrl);
                    }
					apiManager.update ( user, ServerManager.USER_UPDATE_URI );
				} catch ( Exception ex )
				{
					ex.printStackTrace ();
					result = ex.getMessage ();
				}
				return result;
			}
		}.execute ();
	}

	public void setSelfie ( Bitmap selfieBitmap )
	{
		this.selfieBitmap = selfieBitmap;
	}

	public Bitmap getSelphie ()
	{
		return selfieBitmap;
	}

	public TypeJobServiceAPIObject getTypejobAPiObject ()
	{
		return typejobAPiObject;
	}

	public void setTypejobAPiObject ( TypeJobServiceAPIObject typejobAPiObject )
	{
		this.typejobAPiObject = typejobAPiObject;
	}

	public void setTypeJobs ( String typeJobs )
	{
		this.typeJobs = typeJobs;
	}

	public String getTypeJobs ()
	{
		return typeJobs;
	}
}
