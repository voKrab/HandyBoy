package com.vallverk.handyboy.view.base;

import java.io.File;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.library.Constants;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
//import com.aviary.android.feather.FeatherActivity;
//import com.aviary.android.feather.library.Constants;

public class BaseFragment extends Fragment
{
	public enum ChooseType
	{
		GALLERY, CAMERA;
	}

	public enum MediaEditorType
	{
		AVIARY;
	}

	public enum MediaType
	{
		PHOTO, VIDEO, PHOTO_AND_VIDEO
	}

	protected FontUtils fontUtils;
	protected MainActivity controller;
	protected View view;

	public static File SAVE_PATH = new File ( Environment.getExternalStoragePublicDirectory ( Environment.DIRECTORY_DCIM ), "CameraContentDemo.jpeg" );
	public final static int FROM_GALLERY = 100;
	public final static int FROM_EDITOR = FROM_GALLERY + 1;
	public final static int FROM_GALLERY_VIDEO = FROM_EDITOR + 1;
	public final static int FROM_CAMERA = FROM_GALLERY_VIDEO + 1;
	public final static int FROM_CAMERA_VIDEO = FROM_CAMERA + 1;

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );

		this.controller = ( MainActivity ) getActivity ();
		// this.controller = MainActivity.getInstance ();
		fontUtils = FontUtils.getInstance ( getActivity () );
		updateFonts ();

		getView ().setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View arg0 )
			{
				controller.hideKeyboard ();
			}
		} );
		clearFields ();
	}

	protected void clearFields ()
	{
		// TODO Auto-generated method stub

	}

    public void onBackPressed ()
	{
		controller.hideKeyboard ();
	}

	protected void updateFonts ()
	{
		// fontUtils.applyStyle ( ( ViewGroup ) getView (), FontStyle.LIGHT );
	}

	public void onNewIntent ( Intent intent )
	{
		// TODO Auto-generated method stub
	}

	public boolean onTouchEvent ( MotionEvent event )
	{
		// TODO Auto-generated method stub
		return false;
	}

	protected void chooseImage ( MediaEditorType mediaEditor, ChooseType chooseType )
	{
		switch ( mediaEditor )
		{
			case AVIARY:
			{
				switch ( chooseType )
				{
					case GALLERY:
					{
						Intent photoPickerIntent = new Intent ( Intent.ACTION_PICK );
						photoPickerIntent.setType ( "image/*" );
						startActivityForResult ( photoPickerIntent, FROM_GALLERY );
						break;
					}
					case CAMERA:
					{
						Intent cameraIntent = new Intent ( MediaStore.ACTION_IMAGE_CAPTURE );
						cameraIntent.putExtra ( MediaStore.EXTRA_VIDEO_QUALITY, 1 );
						cameraIntent.putExtra ( MediaStore.EXTRA_OUTPUT, Uri.fromFile ( SAVE_PATH ) );
						startActivityForResult ( cameraIntent, FROM_CAMERA );
						break;
					}
				}
				//
				// Intent photoPickerIntent = new Intent ( Intent.ACTION_PICK );
				// photoPickerIntent.setType ( "image/*" );
				// startActivityForResult ( photoPickerIntent, FROM_GALLERY );
				break;
			}
		}
	}

	protected void chooseVideo ( ChooseType chooseType )
	{
		switch ( chooseType )
		{
			case GALLERY:
			{
				Intent photoPickerIntent = new Intent ( Intent.ACTION_PICK );
				photoPickerIntent.setType ( "video/*" );
				startActivityForResult ( photoPickerIntent, FROM_GALLERY_VIDEO );
				break;
			}
			case CAMERA:
			{
				Intent takeVideoIntent = new Intent ( MediaStore.ACTION_VIDEO_CAPTURE );
				if ( takeVideoIntent.resolveActivity ( getActivity ().getPackageManager () ) != null )
				{
					startActivityForResult ( takeVideoIntent, FROM_CAMERA_VIDEO );
				}
				break;
			}
		}
	}

	@Override
	public void onActivityResult ( int requestCode, int resultCode, Intent imageReturnedIntent )
	{
		if ( resultCode == Activity.RESULT_OK )
		{
			// Bitmap photo = null;
			switch ( requestCode )
			{
				case FROM_GALLERY:
				{
					Uri selectedImage = imageReturnedIntent.getData ();
					Intent newIntent = new Intent ( getActivity (), FeatherActivity.class );
					newIntent.setData ( selectedImage );
					newIntent.putExtra ( Constants.EXTRA_IN_API_KEY_SECRET, getActivity ().getString ( R.string.aviary_secret ) );
					startActivityForResult ( newIntent, FROM_EDITOR );
					break;
				}

				case FROM_CAMERA:
				{
					// Uri selectedImage = imageReturnedIntent.getData ();
					Intent newIntent = new Intent ( getActivity (), FeatherActivity.class );
					newIntent.setData ( Uri.parse ( SAVE_PATH.getAbsolutePath () ) );
					newIntent.putExtra ( Constants.EXTRA_IN_API_KEY_SECRET, getActivity ().getString ( R.string.aviary_secret ) );
					startActivityForResult ( newIntent, FROM_EDITOR );
					break;
				}

				// case FROM_CAMERA:
				// {
				// // Bundle bundle = imageReturnedIntent.getExtras ();
				// // Uri selectedImage = Uri.parse ( SAVE_PATH.getAbsolutePath
				// () );
				//
				// Uri selectedImage = imageReturnedIntent.getData ();
				// //Uri selectedImage = Uri.parse
				// (getAbsolutePath(imageReturnedIntent.getData ()));
				// photoFromEditor ( selectedImage );
				// break;
				// }
				//
				// case FROM_GALLERY:
				case FROM_EDITOR:
				{
					// Uri selectedImage = Uri.parse
					// (getAbsolutePath(imageReturnedIntent.getData
					// ()));//imageReturnedIntent.getData ();
					Uri selectedImage = imageReturnedIntent.getData ();
					photoFromEditor ( selectedImage );
					break;
				}

				case FROM_CAMERA_VIDEO:
				case FROM_GALLERY_VIDEO:
				{
					try
					{
						Uri mediaUri = imageReturnedIntent.getData ();
						if ( Tools.getFileSize ( mediaUri ) <= MainActivity.MAX_VIDEO_SIZE )
						{
							videoFromEditor ( mediaUri );
						} else
						{
							Toast.makeText ( getActivity (), R.string.file_size_more_than_10_mb, Toast.LENGTH_LONG ).show ();
						}
					} catch ( Exception ex )
					{
						ex.printStackTrace ();
					}
					break;
				}
			}
		}
	}

	protected void photoFromEditor ( Uri uri )
	{

	}

	protected void videoFromEditor ( Uri uri )
	{

	}

	protected void chooseImage ( MediaEditorType mediaEditor )
	{
		chooseImage ( mediaEditor, ChooseType.GALLERY );
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

	@SuppressLint("ValidFragment")
	public class ChoosePhotoDialogFragment extends DialogFragment
	{
		private Button fromGalleryButton;
		private Button fromCameraButton;

		@Override
		public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
		{
			getDialog ().getWindow ().requestFeature ( Window.FEATURE_NO_TITLE );
			View layout = inflater.inflate ( R.layout.choose_photo_layout, container, false );
			fromGalleryButton = ( Button ) layout.findViewById ( R.id.fromGalleryButton );
			fromCameraButton = ( Button ) layout.findViewById ( R.id.fromCameraButton );
			return layout;
		}

		public void onActivityCreated ( Bundle savedInstanceState )
		{
			super.onActivityCreated ( savedInstanceState );

			Window window = getDialog ().getWindow ();
			window.setBackgroundDrawable ( new ColorDrawable ( Color.TRANSPARENT ) );
			window.setLayout ( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT );

			fromGalleryButton.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View v )
				{
					chooseImage ( MediaEditorType.AVIARY, ChooseType.GALLERY );
					dismiss ();
				}
			} );

			fromCameraButton.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View v )
				{
					chooseImage ( MediaEditorType.AVIARY, ChooseType.CAMERA );
					dismiss ();
				}
			} );
		}
	}

	@SuppressLint("ValidFragment")
	public class ChooseVideoDialogFragment extends DialogFragment
	{
		private Button fromGalleryButton;
		private Button fromCameraButton;

		@Override
		public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
		{
			getDialog ().getWindow ().requestFeature ( Window.FEATURE_NO_TITLE );
			View layout = inflater.inflate ( R.layout.choose_photo_layout, container, false );
			fromGalleryButton = ( Button ) layout.findViewById ( R.id.fromGalleryButton );
			fromCameraButton = ( Button ) layout.findViewById ( R.id.fromCameraButton );
			return layout;
		}

		public void onActivityCreated ( Bundle savedInstanceState )
		{
			super.onActivityCreated ( savedInstanceState );

			Window window = getDialog ().getWindow ();
			window.setBackgroundDrawable ( new ColorDrawable ( Color.TRANSPARENT ) );
			window.setLayout ( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT );

			fromGalleryButton.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View v )
				{
					chooseVideo ( ChooseType.GALLERY );
					dismiss ();
				}
			} );

			fromCameraButton.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View v )
				{
					chooseVideo ( ChooseType.CAMERA );
					dismiss ();
				}
			} );
		}
	}

	@SuppressLint("ValidFragment")
	public class ChoosePhotoVideoDialogFragment extends DialogFragment
	{
		private View photoButton;
		private View videoButton;

		@Override
		public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
		{
			getDialog ().getWindow ().requestFeature ( Window.FEATURE_NO_TITLE );
			View layout = inflater.inflate ( R.layout.choose_media_type_layout, container, false );
			photoButton = layout.findViewById ( R.id.photoButton );
			videoButton = layout.findViewById ( R.id.videoButton );
			return layout;
		}

		public void onActivityCreated ( Bundle savedInstanceState )
		{
			super.onActivityCreated ( savedInstanceState );

			Window window = getDialog ().getWindow ();
			window.setBackgroundDrawable ( new ColorDrawable ( Color.TRANSPARENT ) );
			window.setLayout ( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT );

			photoButton.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View v )
				{
					chooseMedia ( MediaType.PHOTO );
					dismiss ();
				}
			} );

			videoButton.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View v )
				{
					chooseMedia ( MediaType.VIDEO );
					dismiss ();
				}
			} );
		}
	}

	public void onPause ()
	{
		super.onPause ();

		if ( controller != null )
		{
			controller.hideKeyboard ();
		}
	}

	public void clear ()
	{
		if ( view == null )
		{
			return;
		}
		ViewGroup parent = ( ( ViewGroup ) view.getParent () );
		if ( parent == null )
		{
			return;
		}
		parent.removeView ( view );
	}

	@Override
	public Animation onCreateAnimation ( int transit, final boolean enter, int nextAnim )
	{
		if ( nextAnim == 0 )
		{
			init ();
			return null;
		} else
		{
			Animation anim = AnimationUtils.loadAnimation ( getActivity (), nextAnim );
			anim.setAnimationListener ( new AnimationListener ()
			{
				public void onAnimationStart ( Animation animation )
				{
				}

				public void onAnimationRepeat ( Animation animation )
				{
				}

				public void onAnimationEnd ( Animation animation )
				{
					if ( enter )
					{
						init ();
					}
				}
			} );
			return anim;
		}
	}

	protected void init ()
	{

	}

	@Override
	public void onSaveInstanceState ( Bundle outState ) // Bug on API Level > 11
	{
		outState.putString ( "WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE" );
		super.onSaveInstanceState ( outState );
	}
}
