package com.vallverk.handyboy.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.DiscountAPIObject;
import com.vallverk.handyboy.model.api.DiscountAPIObject.DiscountParams;
import com.vallverk.handyboy.model.api.GalleryAPIObject;
import com.vallverk.handyboy.model.api.GalleryAPIObject.GalleryAPIParams;
import com.vallverk.handyboy.model.api.ReviewAPIObject;
import com.vallverk.handyboy.model.api.ReviewAPIObject.ReviewParams;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject.TypeJobServiceParams;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.model.api.UserDetailsAPIObject;
import com.vallverk.handyboy.model.api.UserDetailsAPIObject.UserDetailsParams;
import com.vallverk.handyboy.model.job.JobTypeManager;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.RatingView;
import com.vallverk.handyboy.view.booking.ReviewsClientViewFragment.ReviewItemUserObject;
import com.vallverk.handyboy.view.booking.ReviewsClientViewFragment.ReviewsListAdapter;
import com.vallverk.handyboy.view.custom.FlakeOMeterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandyBoyViewFragment extends BaseFragment
{
	// private MediaFlipperView flipperView;
	// private FlipperStatusView flipperStatusView;

	private SliderLayout mDemoSlider;

	private UserAPIObject handyboy;
	private TextView nameTextView;
	private View backImageView;
	private View backTextView;
	private ImageView chatImageView;
	private boolean isMyUser;
	private RatingView ratingView;
	private FlakeOMeterView flakeOMeterView;
	private TextView parametersTextView;
	private TextView descriptionTextView;
	private LinearLayout typejobsContainer;
	private TextView typejobNameTextView;
	private TextView priceTextView;
	private TextView typejobDescriptionTextView;
	private TextView reviewsTitle;
	private ImageView mainAvatarImageView;
	private LinearLayout reviewsContainer;
	private View showMoreButton;
	private APIManager apiManager;
	protected UserDetailsAPIObject serviceDetails;
	private TextView minPriceTextView;
	private LinearLayout priceContainer;
	private List < TypeJobServiceAPIObject > typejobs;
	private TypeJobServiceAPIObject selectedJob;
	private Map < TypeJobServiceAPIObject, ImageView > jobsMap;
	private ImageView likeImageView;
	private ImageView blockImageView;
	private ImageView shareImageView;
	private View availableNowContainer;
	private View bookHimContainer;
	private View flagHimButton;
	private ArrayList < GalleryAPIObject > galleryItems;
	private TextView discountTextView;
	private DiscountAPIObject discountAPIObject;
	static private List reviewsData;
	private LayoutInflater layoutInflater;
	private boolean isShowGallery;

	private DisplayImageOptions avatarLoadOptions = new DisplayImageOptions.Builder ().showImageOnFail ( R.drawable.avatar ).showImageForEmptyUri ( R.drawable.avatar ).cacheInMemory ( true ).cacheOnDisc ().build ();

	public interface Refresher
	{
		public List < Object > refresh () throws Exception;
	}

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		view = inflater.inflate ( R.layout.handy_boy_page_layout, null );
		/*
		 * flipperView = ( MediaFlipperView ) view.findViewById (
		 * R.id.mediaViewFlipper ); flipperStatusView = ( FlipperStatusView )
		 * view.findViewById ( R.id.flipperStatusView );
		 * flipperView.setStatusLayout ( flipperStatusView );
		 */

		nameTextView = ( TextView ) view.findViewById ( R.id.nameTextView );
		backImageView = view.findViewById ( R.id.backImageView );
		backTextView = view.findViewById ( R.id.backTextView );
		chatImageView = ( ImageView ) view.findViewById ( R.id.chatImageView );
		ratingView = ( RatingView ) view.findViewById ( R.id.ratingView );
		flakeOMeterView = ( FlakeOMeterView ) view.findViewById ( R.id.flakeOMeterView );
		parametersTextView = ( TextView ) view.findViewById ( R.id.parametersTextView );
		descriptionTextView = ( TextView ) view.findViewById ( R.id.descriptionTextView );
		typejobsContainer = ( LinearLayout ) view.findViewById ( R.id.typejobsContainer );
		typejobNameTextView = ( TextView ) view.findViewById ( R.id.typejobNameTextView );
		priceTextView = ( TextView ) view.findViewById ( R.id.priceTextView );
		typejobDescriptionTextView = ( TextView ) view.findViewById ( R.id.typejobDescriptionTextView );
		minPriceTextView = ( TextView ) view.findViewById ( R.id.minPriceTextView );
		priceContainer = ( LinearLayout ) view.findViewById ( R.id.priceContainer );
		priceTextView = ( TextView ) view.findViewById ( R.id.typejobPriceTextView );
		likeImageView = ( ImageView ) view.findViewById ( R.id.likeImageView );
		blockImageView = ( ImageView ) view.findViewById ( R.id.blockImageView );
		availableNowContainer = view.findViewById ( R.id.availableNowContainer );
		discountTextView = ( TextView ) view.findViewById ( R.id.discountTextView );
		bookHimContainer = view.findViewById ( R.id.bookHimContainer );
		flagHimButton = view.findViewById ( R.id.flagHimButton );
		shareImageView = ( ImageView ) view.findViewById ( R.id.shareImageView );
		reviewsContainer = ( LinearLayout ) view.findViewById ( R.id.reviewsContainer );
		reviewsTitle = ( TextView ) view.findViewById ( R.id.reviewsTitle );
		showMoreButton = view.findViewById ( R.id.showMoreButton );

		mainAvatarImageView = ( ImageView ) view.findViewById ( R.id.mainAvatarImageView );
		mainAvatarImageView.setVisibility ( View.GONE );

		mDemoSlider = ( SliderLayout ) view.findViewById ( R.id.slider );
		mDemoSlider.setVisibility ( View.GONE );
		/*
		 * } else { ( ( ViewGroup ) view.getParent () ).removeView ( view ); }
		 */
		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );

		apiManager = APIManager.getInstance ();
		UserAPIObject newUser = ( UserAPIObject ) controller.getCommunicationValue ( UserAPIObject.class.getSimpleName () );
		this.handyboy = newUser == null ? handyboy : newUser;
		layoutInflater = LayoutInflater.from ( controller );
		isMyUser = handyboy.getId ().equals ( apiManager.getUser ().getId () );
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
				if ( result != null && result.isEmpty () )
				{
					checkLike ();
					checkBlock ();
					loadReviews ();
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
					handyboy.fetch ( ServerManager.USER_FETCH_URI.replace ( "id=1", "id=" + handyboy.getId () ) );
					serviceDetails = ( UserDetailsAPIObject ) apiManager.getAPIObject ( handyboy.getId (), UserDetailsAPIObject.class, ServerManager.USER_DETAILS_FETCH_URI );
					typejobs = APIManager.getInstance ().getTypeJobs ( handyboy );
					loadPhotos ();
					loadDiscount ();
				} catch ( Exception ex )
				{
					result = ex.getMessage ();
					ex.printStackTrace ();
				}
				return result;
			}
		}.execute ();
	}

	protected void loadDiscount ()
	{
		try
		{
			this.discountAPIObject = null;
			DiscountAPIObject discountAPIObject = new DiscountAPIObject ();
			discountAPIObject.fetch ( ServerManager.GET_AVAILABLE_NOW + handyboy.getString ( UserParams.SERVICE_ID ) );
			this.discountAPIObject = discountAPIObject;
		} catch ( Exception ex )
		{
			// ignore
		}
	}

	private void loadReviews ()
	{
		new AsyncTask < Void, Void, String > ()
		{
			public void onPreExecute ()
			{
				super.onPreExecute ();
			}

			public void onPostExecute ( String result )
			{
				super.onPostExecute ( result );
				if ( result != null && result.isEmpty () )
				{
					if ( reviewsData != null && reviewsData.size () > 0 )
					{
						reviewsTitle.setVisibility ( View.VISIBLE );
						reviewsContainer.removeAllViews ();
						if ( reviewsData.size () > 3 )
						{
							showMoreButton.setVisibility ( View.VISIBLE );
						} else
						{
							showMoreButton.setVisibility ( View.GONE );
						}
						for ( int i = 0; i < reviewsData.size (); i++ )
						{
							if ( i < 3 )
							{
								ReviewItemUserObject reviewItemUserObject = ( ReviewItemUserObject ) reviewsData.get ( i );
								LinearLayout reviewItem = ( LinearLayout ) layoutInflater.inflate ( R.layout.review_hb_item_layout, null );

								ImageView avatarImage = ( ImageView ) reviewItem.findViewById ( R.id.myAvatarImage );

								TextView descriptionTextView = ( TextView ) reviewItem.findViewById ( R.id.descriptionTextView );
								descriptionTextView.setText ( reviewItemUserObject.reviewAPIObject.getString ( ReviewParams.CONTENT ) );

								TextView nameTextView = ( TextView ) reviewItem.findViewById ( R.id.nameTextView );
								nameTextView.setText ( reviewItemUserObject.userAPIObject.getShortName () );

								RatingView ratingView = ( RatingView ) reviewItem.findViewById ( R.id.ratingView );
								ImageLoader.getInstance ().displayImage ( reviewItemUserObject.userAPIObject.getString ( UserParams.AVATAR ), avatarImage, avatarLoadOptions );
								try
								{
									ratingView.setRating ( Float.parseFloat ( reviewItemUserObject.reviewAPIObject.getValue ( ReviewParams.RATING ).toString () ) );
								} catch ( NumberFormatException ex )
								{
									ex.printStackTrace ();
								}

								reviewsContainer.addView ( reviewItem );
							}
						}
					} else
					{
						reviewsTitle.setVisibility ( View.GONE );
					}
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
					reviewsData = new ArrayList < ReviewItemUserObject > ();
					JSONArray requestJsonArray;
					JSONObject requestJsonObject;
					String request = ServerManager.getRequest ( ServerManager.GET_REVIEWS + "ownerId=" + handyboy.getId () );
					requestJsonObject = new JSONObject ( request );
					requestJsonArray = new JSONArray ( requestJsonObject.getString ( "list" ) );
					for ( int i = 0; i < requestJsonArray.length (); i++ )
					{
						JSONObject tempJsonObject = requestJsonArray.getJSONObject ( i );
						ReviewItemUserObject reviewItemUserObject = new ReviewItemUserObject ();
						reviewItemUserObject.userAPIObject = new UserAPIObject ( tempJsonObject.getJSONObject ( "reviewer" ) );
						reviewItemUserObject.reviewAPIObject = new ReviewAPIObject ( tempJsonObject );
						reviewsData.add ( reviewItemUserObject );
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

	protected void loadPhotos () throws Exception
	{
		String url = ServerManager.GET_GALLERY_URI + handyboy.getValue ( UserParams.SERVICE_ID ).toString ();
		String responseText = ServerManager.getRequest ( url );
        galleryItems = new ArrayList<GalleryAPIObject>();
		if ( responseText.isEmpty () )
		{
			throw new Exception ();
		}
		if ( responseText != null && !responseText.isEmpty () )
		{
			JSONArray arrayData = new JSONArray ( responseText );

			for ( int i = 0; i < arrayData.length (); i++ )
			{
				JSONObject jsonObject = new JSONObject ( arrayData.getString ( i ) );
				GalleryAPIObject galleryItem = new GalleryAPIObject ( jsonObject );
				galleryItems.add ( galleryItem );
			}
		}
	}

	@Override
	public void onPause ()
	{
		updateLike ( likeImageView.isActivated () );
		updateBlock ( blockImageView.isActivated () );
		super.onPause ();
	}

	private void checkLike ()
	{
		new AsyncTask < Void, Void, Boolean > ()
		{
			public void onPreExecute ()
			{
				super.onPreExecute ();
			}

			public void onPostExecute ( Boolean result )
			{
				super.onPostExecute ( result );
				likeImageView.setActivated ( result );
			}

			@Override
			protected Boolean doInBackground ( Void... params )
			{
				boolean result = false;
				try
				{
					String url = ServerManager.CHECK_FAVORITE_URI;
					JSONObject favoriteObject = new JSONObject ();
					favoriteObject.put ( "userId", apiManager.getUser ().getId () );
					favoriteObject.put ( "serviceId", handyboy.getValue ( UserParams.SERVICE_ID ).toString () );
					String responseText = ServerManager.postRequest ( url, favoriteObject );
					if ( responseText != null && !responseText.isEmpty () )
					{
						JSONObject resultObject = new JSONObject ( responseText );
						result = resultObject.getBoolean ( "check" );
					}
				} catch ( Exception ex )
				{
					result = false;
				}

				return result;
			}
		}.execute ();
	}

	private void checkBlock ()
	{
		new AsyncTask < Void, Void, Boolean > ()
		{
			public void onPreExecute ()
			{
				super.onPreExecute ();
			}

			public void onPostExecute ( Boolean result )
			{
				super.onPostExecute ( result );
				blockImageView.setActivated ( result );
			}

			@Override
			protected Boolean doInBackground ( Void... params )
			{
				boolean result = false;
				try
				{
					String url = ServerManager.CHECK_BLOCK_URI;
					JSONObject favoriteObject = new JSONObject ();
					favoriteObject.put ( "userId", apiManager.getUser ().getId () );
					// favoriteObject.put ( "blockUserId", user.getValue (
					// UserParams.SERVICE_ID ).toString () );
					favoriteObject.put ( "blockUserId", handyboy.getId () );
					String responseText = ServerManager.postRequest ( url, favoriteObject );
					if ( responseText != null && !responseText.isEmpty () )
					{
						JSONObject resultObject = new JSONObject ( responseText );
						result = resultObject.getBoolean ( "check" );
					}
				} catch ( Exception ex )
				{
					result = false;
				}

				return result;
			}
		}.execute ();
	}

	private void updateLike ( final boolean isDelete )
	{
		new AsyncTask < Void, Void, String > ()
		{
			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					String url;
					if ( isDelete )
					{
						url = ServerManager.ADD_FAVORITE_URI;
					} else
					{
						url = ServerManager.DELETE_FAVORITE_URI;
					}

					JSONObject favoriteObject = new JSONObject ();
					favoriteObject.put ( "userId", apiManager.getUser ().getId () );
					favoriteObject.put ( "serviceId", handyboy.getValue ( UserParams.SERVICE_ID ).toString () );
					ServerManager.postRequest ( url, favoriteObject );
				} catch ( Exception ex )
				{
					result = ex.getMessage ();
				}

				return result;
			}
		}.execute ();

	}

	private void updateBlock ( final boolean isDelete )
	{
		new AsyncTask < Void, Void, String > ()
		{
			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					String url;
					if ( isDelete )
					{
						url = ServerManager.ADD_BLOCK_URI;
					} else
					{
						url = ServerManager.DELETE_BLOCK_URI;
					}

					JSONObject favoriteObject = new JSONObject ();
					favoriteObject.put ( "userId", apiManager.getUser ().getId () );
					// favoriteObject.put ( "blockUserId", user.getValue (
					// UserParams.SERVICE_ID ).toString () );
					favoriteObject.put ( "blockUserId", handyboy.getId () );
					ServerManager.postRequest ( url, favoriteObject );
				} catch ( Exception ex )
				{
					result = ex.getMessage ();
				}

				return result;
			}
		}.execute ();

	}

	protected void clearFields ()
	{
		ratingView.setRating ( 0 );
		flakeOMeterView.setRating ( 0 );
		minPriceTextView.setVisibility ( View.INVISIBLE );
		parametersTextView.setText ( "" );
		descriptionTextView.setText ( "" );
		parametersTextView.setVisibility ( View.INVISIBLE );
		descriptionTextView.setVisibility ( View.INVISIBLE );
		typejobsContainer.removeAllViews ();
		typejobsContainer.setVisibility ( View.GONE );
		priceContainer.setVisibility ( View.INVISIBLE );
		typejobNameTextView.setVisibility ( View.INVISIBLE );
		typejobDescriptionTextView.setText ( "" );
		availableNowContainer.setVisibility ( View.GONE );
	}

	private void updateComponents ()
	{
		final String mediaString = ( String ) handyboy.getValue ( UserParams.AVATAR );

		if ( galleryItems.size () > 0 )
		{
			mDemoSlider.removeAllSliders ();


            //DefaultSliderView defaultSliderView = new DefaultSliderView ( controller );
			//defaultSliderView.image ( mediaString );
			//mDemoSlider.addSlider ( defaultSliderView );

			for ( GalleryAPIObject galleryItem : galleryItems )
			{
				if ( galleryItem.getValue ( GalleryAPIParams.STATUS ).toString ().equals ( "approved" ) )
				{
					isShowGallery = true;
					if ( galleryItem.getString ( GalleryAPIParams.TYPE ).equals ( "image" ) )
					{
                        DefaultSliderView defaultSliderView = new DefaultSliderView ( controller );
						defaultSliderView.image ( galleryItem.getString ( GalleryAPIParams.URL ) );
                        defaultSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {
                                controller.setCommunicationValue("galleryItems", galleryItems);
                                controller.setState(VIEW_STATE.GALLERY);
                            }
                        });
						mDemoSlider.addSlider ( defaultSliderView );
					} else
					{
						YouTubeSliderView youTubeSliderView = new YouTubeSliderView ( controller );
                        youTubeSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {
                                controller.setCommunicationValue("galleryItems", galleryItems);
                                controller.setState(VIEW_STATE.GALLERY);
                            }
                        });
						youTubeSliderView.image ( Tools.getVideoImagePreview ( galleryItem.getString ( GalleryAPIParams.URL ) ) );
						youTubeSliderView.setYoutubeId ( galleryItem.getString ( GalleryAPIParams.URL ) );
						mDemoSlider.addSlider ( youTubeSliderView );
					}
				}
			}

            try {
                JSONObject jsonGalleryAPIObject = new JSONObject();
                jsonGalleryAPIObject.put(GalleryAPIParams.URL.toString(), mediaString);
                jsonGalleryAPIObject.put(GalleryAPIParams.TYPE.toString(), "image");
                jsonGalleryAPIObject.put(GalleryAPIParams.STATUS.toString(), "approved");

                GalleryAPIObject galleryAPIObject = new GalleryAPIObject(jsonGalleryAPIObject);
                galleryItems.add(0, galleryAPIObject);


            } catch (Exception e) {
                e.printStackTrace();
            }


            mDemoSlider.setPresetTransformer ( SliderLayout.Transformer.ZoomOutSlide );
			mDemoSlider.setPresetIndicator ( SliderLayout.PresetIndicators.Center_Bottom );

			mDemoSlider.setCustomAnimation ( new DescriptionAnimation () );
			mDemoSlider.setDuration ( 10000 );
			mainAvatarImageView.setVisibility ( View.GONE );
			mDemoSlider.setVisibility ( View.VISIBLE );
		}
		if ( !isShowGallery )
		{
			ImageLoader.getInstance ().displayImage ( mediaString, mainAvatarImageView, avatarLoadOptions );
			mainAvatarImageView.setVisibility ( View.VISIBLE );
			mDemoSlider.setVisibility ( View.GONE );

            mainAvatarImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject jsonGalleryAPIObject = new JSONObject();
                    try {
                        jsonGalleryAPIObject.put(GalleryAPIParams.URL.toString(), mediaString);
                        jsonGalleryAPIObject.put(GalleryAPIParams.TYPE.toString(), "image");
                        jsonGalleryAPIObject.put(GalleryAPIParams.STATUS.toString(), "approved");

                        GalleryAPIObject galleryAPIObject = new GalleryAPIObject(jsonGalleryAPIObject);
                        List<GalleryAPIObject> galleryAPIObjectList = new ArrayList<GalleryAPIObject>();
                        galleryAPIObjectList.add(galleryAPIObject);
                        controller.setCommunicationValue("galleryItems", galleryAPIObjectList);
                        controller.setState(VIEW_STATE.GALLERY);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
		}

		String name = handyboy.getString ( UserParams.FIRST_NAME ) + " " + handyboy.getString ( UserParams.LAST_NAME );
		nameTextView.setText ( name );
		chatImageView.setVisibility ( isMyUser ? View.GONE : View.VISIBLE );
		blockImageView.setVisibility ( isMyUser ? View.GONE : View.VISIBLE );
		likeImageView.setVisibility ( isMyUser ? View.GONE : View.VISIBLE );
		float rating = Float.parseFloat ( serviceDetails.getString ( UserDetailsParams.RATING ) );
		ratingView.setRating ( rating );
		float reliability = Float.parseFloat ( serviceDetails.getString ( UserDetailsParams.RELIABILITY ) );
		flakeOMeterView.setRating ( reliability );
		String parameters = createParameters ();
		parametersTextView.setText ( parameters );
		parametersTextView.setVisibility ( View.VISIBLE );
		descriptionTextView.setText ( serviceDetails.getString ( UserDetailsParams.DESCRIPTION ) );
		descriptionTextView.setVisibility ( View.VISIBLE );
		int minPrice = calculateMinPrice ();
		minPriceTextView.setText ( "$" + minPrice + "+" );
		minPriceTextView.setVisibility ( View.VISIBLE );
		updateJobsContainer ();
		updateDiscountComponents ();
	}

	private void updateDiscountComponents ()
	{
		if ( discountAPIObject == null || discountAPIObject.getInt ( DiscountParams.DISCOUNT ) == 0 )
		{
			availableNowContainer.setVisibility ( View.GONE );
		} else
		{
			discountTextView.setText ( controller.getResources ().getString ( R.string.discount_off, discountAPIObject.getInt ( DiscountParams.DISCOUNT ) ) );
			availableNowContainer.setVisibility ( View.VISIBLE );
		}
	}

	private void updateJobsContainer ()
	{
		jobsMap = new HashMap < TypeJobServiceAPIObject, ImageView > ();
		typejobsContainer.setVisibility ( View.VISIBLE );
		JobTypeManager manager = JobTypeManager.getInstance ();
		for ( final TypeJobServiceAPIObject typeJobObject : typejobs )
		{
			ImageView imageView = new ImageView ( controller );
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams ( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
			lp.setMargins ( Tools.fromDPToPX ( 13, controller ), 0, 0, 0 );
			imageView.setLayoutParams ( lp );
			// imageView.setImageResource ( manager.getImageResource (
			// typeJobObject.getString ( TypeJobServiceParams.TYPEJOB_ID ) ) );
			imageView.setImageResource ( R.drawable.dumbbels_na );
			typejobsContainer.addView ( imageView );
			jobsMap.put ( typeJobObject, imageView );
			imageView.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View v )
				{
					if ( selectedJob == typeJobObject )
					{
						return;
					}
					selectedJob = typeJobObject;
					updateJobSelection ();
				}
			} );
		}
		selectedJob = typejobs.get ( 0 );
		updateJobSelection ();
	}

	private void updateJobSelection ()
	{
		for ( TypeJobServiceAPIObject job : typejobs )
		{
			ImageView imageView = jobsMap.get ( job );
			boolean isSelected = job == selectedJob;
			imageView.setImageResource ( job.getImageResource ( isSelected ) );
		}
		typejobNameTextView.setText ( selectedJob.getName () );
		typejobNameTextView.setVisibility ( View.VISIBLE );
		priceTextView.setText ( selectedJob.getString ( TypeJobServiceParams.PRICE ) );
		priceContainer.setVisibility ( View.VISIBLE );
		typejobDescriptionTextView.setText ( selectedJob.getString ( TypeJobServiceParams.DESCRIPTION ) );
	}

	private int calculateMinPrice ()
	{
		int minPrice = Integer.MAX_VALUE;
		for ( TypeJobServiceAPIObject typeJobObject : typejobs )
		{
			int price = Integer.parseInt ( typeJobObject.getString ( TypeJobServiceParams.PRICE ) );
			minPrice = Math.min ( price, minPrice );
		}
		return minPrice;
	}

	private String createParameters ()
	{
		String parameters = "";
		try
		{
			int height = serviceDetails.getInt ( UserDetailsParams.HEIGHT );
            int inches = Tools.getInches ( height );
            int feets = Tools.getFeets ( height );
			parameters = feets + "'" + inches + "\"";
			parameters += " " + serviceDetails.getString ( UserDetailsParams.WEIGHT ) + "lbs, ";
			parameters += serviceDetails.getString ( UserDetailsParams.HEIR_COLOR ) + ", ";
			parameters += serviceDetails.getString ( UserDetailsParams.EYE_COLOR ) + ", ";
			parameters += serviceDetails.getString ( UserDetailsParams.BODY_TYPE ) + ", ";
			String location = handyboy.getLocationText ();
			parameters += serviceDetails.getString ( UserDetailsParams.SEX ) + ( location.isEmpty () ? "" : ", " + location );
		} catch ( Exception ex )
		{
			parameters = "Feet.Inches || 6,4 || weight without lbs. Пожалуйста уважайте другие платформы пишите данные по сервису в общем формате";
			ex.printStackTrace ();
		}
		return parameters;
	}

	private void addListeners ()
	{
		availableNowContainer.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{

			}
		} );
		bookHimContainer.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.startBooking ( handyboy, selectedJob, null );
			}
		} );
		availableNowContainer.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.startBooking ( handyboy, selectedJob, discountAPIObject );
			}
		} );
		flagHimButton.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				flagHim ();
			}
		} );
		chatImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.setCommunicationValue ( UserAPIObject.class.getSimpleName (), handyboy );
				controller.setState ( VIEW_STATE.CHAT );
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

		showMoreButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				DialogFragment newFragment = new HbReviewsDialogFragment ();
				newFragment.show ( controller.getSupportFragmentManager ().beginTransaction (), null );
			}
		} );

		likeImageView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View arg0 )
			{
				if ( likeImageView.isActivated () )
				{
					likeImageView.setActivated ( false );
				} else
				{
					likeImageView.setActivated ( true );
				}
			}
		} );

		blockImageView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
				if ( blockImageView.isActivated () )
				{
					blockImageView.setActivated ( false );
				} else
				{
					blockImageView.setActivated ( true );
				}
			}
		} );

		shareImageView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				Intent shareIntent = new Intent ();
				shareIntent.setAction ( Intent.ACTION_SEND );
				shareIntent.putExtra ( Intent.EXTRA_STREAM, handyboy.getString ( UserParams.AVATAR ) );
				shareIntent.putExtra ( Intent.EXTRA_TITLE, handyboy.getString ( UserParams.FIRST_NAME ) );
				shareIntent.putExtra ( Intent.EXTRA_TEXT, "Test Text Text Text" );

				shareIntent.setType ( "image/png" );
				startActivity ( Intent.createChooser ( shareIntent, "Share with ..." ) );
			}
		} );
	}

	protected void flagHim ()
	{
		flagHimButton.setVisibility ( View.GONE );
		Toast.makeText ( controller, R.string.you_flagged_this_handy_boy, Toast.LENGTH_LONG ).show ();
	}

	public static class HbReviewsDialogFragment extends DialogFragment
	{
		protected View toolBarView;
		public List objects;
		protected View view;
		protected PullToRefreshListView listViewPTR;
		protected ListView listView;
		private ArrayAdapter listAdapter;
		protected TextView emptyTextView;
		private Refresher refresher;
		private boolean isShowEmpty = true;

		private View crossImageView;

		@Override
		public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
		{
			getDialog ().getWindow ().requestFeature ( Window.FEATURE_NO_TITLE );
			View layout = inflater.inflate ( R.layout.review_hb_layout, container, false );
			crossImageView = layout.findViewById ( R.id.crossImageView );
			return layout;
		}

		@Override
		public void onActivityCreated ( Bundle savedInstanceState )
		{
			super.onActivityCreated ( savedInstanceState );

			Window window = getDialog ().getWindow ();
			window.setBackgroundDrawable ( new ColorDrawable ( Color.TRANSPARENT ) );
			window.setLayout ( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT );

			if ( listViewPTR == null )
			{
				emptyTextView = ( TextView ) getView ().findViewById ( R.id.emptyTextView );
				emptyTextView.setVisibility ( View.GONE );

				listViewPTR = ( PullToRefreshListView ) getView ().findViewById ( R.id.baseListView );
				listView = listViewPTR.getRefreshableView ();
				listView.setDivider ( null );
				setAdapter ( getAdapter () );
				addListeners ();
			}

			setRefresher ( new Refresher ()
			{

				@Override
				public List < Object > refresh () throws Exception
				{
					return reviewsData;
				}
			} );
			refreshData ();
		}

		protected ArrayAdapter getAdapter ()
		{
			return new ReviewsListAdapter ( getActivity () );
		}

		protected void addListeners ()
		{
			listViewPTR.setOnRefreshListener ( new OnRefreshListener < ListView > ()
			{
				@Override
				public void onRefresh ( PullToRefreshBase < ListView > refreshView )
				{
					refreshView.getLoadingLayoutProxy ().setLastUpdatedLabel ( "" );
					// Do work to refresh the list here.
					refreshData ();
				}
			} );

			crossImageView.setOnClickListener ( new OnClickListener ()
			{

				@Override
				public void onClick ( View v )
				{
					HbReviewsDialogFragment.this.dismiss ();
				}
			} );

			listView.setOnItemClickListener ( new OnItemClickListener ()
			{
				public void onItemClick ( AdapterView < ? > parent, View view, int position, long id )
				{
					// ParseObject productObject = ( ParseObject ) objects.get (
					// position );
					// FashionGramApplication.temp.push ( productObject );
					// FashionGramApplication.viewStateController.setState (
					// VIEW_STATE.PRODUCT_DETAILS );
				}
			} );
		}

		public void setRefresher ( Refresher refresher )
		{
			this.refresher = refresher;
		}

		public void refreshData ()
		{
			new AsyncTask < Void, Void, String > ()
			{
				public void onPostExecute ( String result )
				{
					super.onPostExecute ( result );
					if ( result.isEmpty () )
					{
						updateContent ();
					}
					listViewPTR.onRefreshComplete ();
				}

				@Override
				protected String doInBackground ( Void... params )
				{
					String result = "";
					try
					{
						objects = refresher.refresh ();
					} catch ( Exception ex )
					{
						ex.printStackTrace ();
						result = ex.getMessage ();
					}
					return result;
				}
			}.execute ();
		}

		protected void updateContent ()
		{
			updateContent ( true );
		}

		protected void updateContent ( boolean isRemoveFreezed )
		{
			listAdapter.clear ();
			listAdapter.addAll ( objects );
			listAdapter.notifyDataSetChanged ();

			if ( isShowEmpty )
			{
				emptyTextView.setVisibility ( objects.isEmpty () ? View.VISIBLE : View.GONE );
			} else
			{
				emptyTextView.setVisibility ( View.GONE );
			}
		}

		public void onResume ()
		{
			super.onResume ();
		}

		public void onPause ()
		{
			super.onPause ();
		}

		public void setAdapter ( ArrayAdapter adapter )
		{
			listAdapter = adapter;
			listView.setAdapter ( listAdapter );
		}
	}

	public static class YouTubeSliderView extends BaseSliderView
	{

		private String youtubeId;

		public YouTubeSliderView ( Context context )
		{
			super ( context );
		}

		@Override
		public View getView ()
		{
			View v = LayoutInflater.from ( getContext () ).inflate ( R.layout.youtube_slider_view, null );
			ImageView target = ( ImageView ) v.findViewById ( com.daimajia.slider.library.R.id.daimajia_slider_image );
			ImageView playVideoImageView = ( ImageView ) v.findViewById ( R.id.playVideoImageView );

			playVideoImageView.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View v )
				{

					Intent intent = new Intent ( Intent.ACTION_VIEW, Uri.parse ( "vnd.youtube://" + youtubeId ) );
					getContext ().startActivity ( intent );
				}
			} );

			bindEventAndShow ( v, target );
			return v;
		}

		public void setYoutubeId ( String youtubeId )
		{
			this.youtubeId = youtubeId;
		}
	}
}
