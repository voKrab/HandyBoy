package com.vallverk.handyboy.facebook;

import java.io.IOException;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.sromku.simple.fb.Permissions;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebook.OnLoginListener;
import com.sromku.simple.fb.SimpleFacebook.OnPublishListener;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.entities.Feed;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;

public class FacebookManager
{
	public final int REQUEST_LOGIN = 64206;
	//public static String APP_ID = "1460655034217478";
	//public static String APP_SECRET = "85e02fb7aea95f8eeb5b780a26f70385";

    public static String APP_ID = "1531252457117882";
    public static String APP_SECRET = "7c3436cfe436e861e26ba910a97b5878";
	public static String APP_NAMESPACE = "handyboy";

	private Session.StatusCallback statusCallback = new SessionStatusCallback ();
	private Fragment fragment;
	private Activity activity;
	private Bundle savedInstanceState;
	private SimpleFacebook facebook;

	public void init ( Bundle savedInstanceState, Fragment fragment )
	{
		this.fragment = fragment;
		this.activity = fragment.getActivity ();
		this.savedInstanceState = savedInstanceState;

		// initialize facebook configuration
		Permissions[] permissions = new Permissions[] { Permissions.BASIC_INFO, Permissions.EMAIL, Permissions.USER_PHOTOS, Permissions.USER_BIRTHDAY, Permissions.USER_LOCATION, Permissions.PUBLISH_ACTION, Permissions.PUBLISH_STREAM, Permissions.USER_ABOUT_ME };
		SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder ().setAppId ( APP_ID ).setNamespace ( APP_NAMESPACE ).setPermissions ( permissions ).setDefaultAudience ( SessionDefaultAudience.FRIENDS ).build ();
		SimpleFacebook.setConfiguration ( configuration );
		facebook = SimpleFacebook.getInstance ( activity );
	}

	private class SessionStatusCallback implements Session.StatusCallback
	{
		@Override
		public void call ( Session session, SessionState state, Exception exception )
		{
			if ( exception != null )
			{
			}

			if ( state == SessionState.OPENED )
			{
//                Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
//                        activity, Arrays.asList(
//                        "user_location", "user_birthday",
//                        "user_likes", "email"));
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest ( activity, Arrays.asList ( "publish_actions", "user_location", "user_birthday","user_likes", "email" ) );
				session.requestNewPublishPermissions ( newPermissionsRequest );
			} else if ( state == SessionState.OPENED_TOKEN_UPDATED )
			{
				loadUser ();
			} else if ( state == SessionState.CLOSED_LOGIN_FAILED )
			{
				session.closeAndClearTokenInformation ();
			} else if ( state == SessionState.CLOSED )
			{
				session.close ();
			}
		}
	}

	public void login ()
	{
		Session session = createSession ();
		if ( session.isOpened () )
		{
			loadUser ();
		} else
		{
			StatusCallback callback = new StatusCallback ()
			{
				public void call ( Session session, SessionState state, Exception exception )
				{
					if ( exception != null )
					{
						// init ( savedInstanceState, fragment );
					} else if ( session.isOpened () )
					{
						loadUser ();
					}

					if ( state == SessionState.CLOSED_LOGIN_FAILED )
					{
						session.closeAndClearTokenInformation ();
						Session.setActiveSession ( null );
					} else if ( state == SessionState.CLOSED )
					{
						session.close ();
						Session.setActiveSession ( null );
					}
				}
			};
            Session.OpenRequest openRequest = new Session.OpenRequest(fragment);
            openRequest.setPermissions(Arrays.asList("user_location", "user_birthday", "email"));
          //  openRequest.setCallback(callback);
            //session = new Session(activity);
            //Session.setActiveSession(session);
            //session.openForRead(openRequest);
			session.openForRead ( openRequest.setCallback(callback) );
		}
	}

    //TODO
	private Session createSession ()
	{
		Settings.addLoggingBehavior ( LoggingBehavior.INCLUDE_ACCESS_TOKENS );
		Session session = Session.getActiveSession ();
		if ( session == null || session.getState () == SessionState.CLOSED_LOGIN_FAILED )
		{
			if ( savedInstanceState != null )
			{
				session = Session.restoreSession ( activity, null, statusCallback, savedInstanceState );
			}
			if ( session == null )
			{
				session = new Session ( activity );
			}
			Session.setActiveSession ( session );
		}
		return session;
	}

	public void loadUser ()
	{
		MainActivity.getInstance ().showLoader ();
		Session session = Session.getActiveSession ();
		Request request = Request.newMeRequest ( session, new GraphUserCallback ()
		{
			public void onCompleted ( GraphUser user, Response response )
			{
				if ( user != null )
				{
					signIn ( user );
				} else
				{
					MainActivity.getInstance ().hideLoader ();
					Toast.makeText ( activity, "Данные не загружены", Toast.LENGTH_LONG ).show ();
				}
				//dialog.dismiss ();
			}
		} );
		Request.executeBatchAsync ( request );
	}

	public void logout ()
	{
		Session session = Session.getActiveSession ();
		if ( !session.isClosed () )
		{
			session.closeAndClearTokenInformation ();
		}
	}

    //TODO
	public Session.StatusCallback getStatusCallback ()
	{
		return statusCallback;
	}

	// public void addCallback ()
	// {
	// // Session.getActiveSession ().addCallback ( statusCallback );
	// }
	//
	// public void removeCallback ()
	// {
	// // Session.getActiveSession ().removeCallback ( statusCallback );
	// }

	public void onActivityResult ( int requestCode, int resultCode, Intent data )
	{
		if ( requestCode == REQUEST_LOGIN )
		{
			Session.getActiveSession ().onActivityResult ( activity, requestCode, resultCode, data );
		}
	}

	public void onSaveInstanceState ( Bundle outState )
	{
		Session session = Session.getActiveSession ();
		Session.saveSession ( session, outState );
	}

	public void signIn ( final GraphUser fbUser )
	{
		new AsyncTask < Void, Void, String > ()
		{
			private UserAPIObject user;

			public void onPostExecute ( String result )
			{
				super.onPostExecute ( result );
				MainActivity.getInstance ().hideLoader ();
				if ( result.isEmpty () )
				{
					APIManager.getInstance ().setUser ( user );
					MainActivity.getInstance ().enter ();
				} else
				{
					user = new UserAPIObject ();
					String email = fbUser.getProperty ( "email" ).toString ();
                    String birthday = fbUser.getProperty ( "birthday" ).toString();
					user.putValue(UserParams.FACEBOOK_UID, fbUser.getId());
                    user.putValue ( UserParams.FIRST_NAME, fbUser.getFirstName () );
                    user.putValue ( UserParams.LAST_NAME, fbUser.getLastName () );
                    user.putValue ( UserParams.AVATAR, "http://graph.facebook.com/"+ fbUser.getId () +"/picture?type=large" );
                    Log.d("FACEBOOK_REGISTRATION", "birthday=" + birthday);
                    if(birthday != null) {
                        user.putValue(UserParams.DOB, Tools.fromStringToDate(birthday, "dd/MM/yyyy").getTime() / 1000);
                    }else{
                        user.putValue(UserParams.DOB, 0);
                    }

                    if(email != null){
                        user.putValue ( UserParams.EMAIL, email );
                    }


					MainActivity.getInstance ().setCommunicationValue ( UserAPIObject.class.getSimpleName (), user );
					MainActivity.getInstance ().setState ( VIEW_STATE.FACEBOOK_REGISTRATION );
				}
			}
			
			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					user = new UserAPIObject ();
					user.putValue ( UserParams.FACEBOOK_UID, fbUser.getId () );
					result = user.loginWithFacebook ();
				} catch ( Exception ex )
				{
					ex.printStackTrace ();
					result = MainActivity.getInstance ().getString ( R.string.error );
				}
				return result;
			}
		}.execute ();
				
//		Thread thread = new Thread ( new Runnable ()
//		{
//			@Override
//			public void run ()
//			{
//				try
//				{
//					String userId = user.getId ();
////					ParseQuery < ParseObject > query = new ParseQuery < ParseObject > ( "_User" );
////					query.whereEqualTo ( UserParams.ID_FB.toString (), userId );
////					List < ParseObject > users = query.find ();
////					if ( users != null && !users.isEmpty () ) // login
//					if ( false )
//					{
////						ParseUser userParseObject = ( ParseUser ) users.get ( 0 );
////						String password = SocialPassword.getPassword ( userId, SocialType.FACEBOOK );
////						if ( password == null ) // значит почистили кеш
////												// приложения или переустановили
////												// его
////						{
////							registration ( user );
////						} else
////						{
////							ParseUser.logInInBackground ( userParseObject.getUsername (), password, new LogInCallback ()
////							{
////								@Override
////								public void done ( ParseUser requestUser, ParseException e )
////								{
////									if ( e != null )
////									{
////										FashionGramApplication.hideLoader ();
////										Toast.makeText ( activity, e.getMessage (), Toast.LENGTH_LONG ).show ();
////									} else
////									{
////										FashionGramApplication.initView ();
////									}
////								}
////							} );
////						}
//					} else
//					// registration
//					{
//						registration ( user );
//					}
//				} catch ( Exception ex )
//				{
//					ex.printStackTrace ();
//					MainActivity.getInstance ().hideLoader ();
//				}
//			}
//		} );
//		thread.start ();
	}

//	protected void registration ( GraphUser user )
//	{
//		String userId = user.getId ();
//		UserParseObject userDataObject = FashionGramApplication.getUserDataObject ();
//		userDataObject.setParameter ( UserParams.USERNAME, user.getName () );
//		String email = user.getProperty ( "email" ).toString ();
//		userDataObject.setParameter ( UserParams.EMAIL, email );
//		userDataObject.setParameter ( UserParams.ID_FB, userId );
//		userDataObject.setParameter ( UserParams.REGISTER_TYPE, SocialType.FACEBOOK.toInt () );
//		try
//		{
//			URL url = new URL ( "https://graph.facebook.com/" + user.getId () + "/picture?type=large" );
//			Bitmap avatar = BitmapFactory.decodeStream ( url.openConnection ().getInputStream () );
//			userDataObject.setAvatar ( avatar );
//		} catch ( MalformedURLException e )
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace ();
//		} catch ( IOException e )
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace ();
//		}
//		MainActivity.getInstance ().hideLoader ();
//		MainActivity.getInstance ().setState ( VIEW_STATE.REGISTRATION );
//	}

	public boolean isSigned ()
	{
		Session session = createSession ();
		return session.isOpened ();
	}

//	public void autorizationAndShare ( final ProductParseObject productObject, final Handler handler )
//	{
//		FashionGramApplication.showLoader ();
//		new Thread ( new Runnable ()
//		{
//			@Override
//			public void run ()
//			{
//				try
//				{
//					Session session = createSession ();
//					if ( session.isOpened () )
//					{
//						share ( productObject, handler );
//					} else
//					{
//						StatusCallback callback = new StatusCallback ()
//						{
//							public void call ( Session session, SessionState state, Exception exception )
//							{
//								if ( exception != null )
//								{
//									init ( savedInstanceState, fragment );
//								} else if ( session.isOpened () )
//								{
//									share ( productObject, handler );
//								}
//							}
//						};
//						session.openForRead ( new Session.OpenRequest ( fragment ).setCallback ( callback ) );
//					}
//				} catch ( final Exception ex )
//				{
//					fragment.getView ().post ( new Runnable ()
//					{
//						@Override
//						public void run ()
//						{
//							Toast.makeText ( activity, ex.getMessage (), Toast.LENGTH_LONG ).show ();
//							ex.printStackTrace ();
//						}
//					} );
//				}
//				FashionGramApplication.hideLoader ();
//			}
//		} ).start ();
//	}

//	protected void share ( final ProductParseObject productObject, final Handler handler )
//	{
//		OnLoginListener onLoginListener = new SimpleFacebook.OnLoginListener ()
//		{
//			@Override
//			public void onFail ( String reason )
//			{
//			}
//
//			@Override
//			public void onException ( Throwable throwable )
//			{
//			}
//
//			@Override
//			public void onThinking ()
//			{
//			}
//
//			@Override
//			public void onNotAcceptingPermissions ()
//			{
//			}
//
//			@Override
//			public void onLogin ()
//			{
//				shareNextStep ( productObject, handler );
//			}
//		};
//		facebook.login ( onLoginListener );
//	}
//
//	protected void shareNextStep ( ProductParseObject productObject, final Handler handler )
//	{
//		OnPublishListener onPublishListener = new SimpleFacebook.OnPublishListener ()
//		{
//			@Override
//			public void onFail ( String reason )
//			{
//			}
//
//			@Override
//			public void onException ( Throwable throwable )
//			{
//			}
//
//			@Override
//			public void onThinking ()
//			{
//			}
//
//			@Override
//			public void onComplete ( String postId )
//			{
//				handler.dispatchMessage ( handler.obtainMessage () );
//			}
//		};
//
//		// build feed
//		Feed feed = new Feed.Builder ().setMessage ( "" ).setName ( "" ).setCaption ( "http://fashiongram.ru/product/?id=" + productObject.getObjectId () ).setDescription ( "" ).setPicture ( productObject.getGeneralImage ().getUrl () ).build ();
//
//		// publish the feed
//
//		facebook.publish ( feed, onPublishListener );
//	}
}
