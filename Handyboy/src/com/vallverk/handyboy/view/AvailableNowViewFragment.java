package com.vallverk.handyboy.view;

import org.json.JSONObject;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.FilterManager.SearchType;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.feed.FeedViewFragment.CommunicationSearch;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.FontUtils;
import com.vallverk.handyboy.view.base.FontUtils.FontStyle;

public class AvailableNowViewFragment extends BaseFragment
{
	private static final int MIN_PERCENT = 5;
	private ImageView backImageView;
	private TextView backTextView;
	private SeekBar discountSeekBar;
	private TextView discountPersent;

	private TextView titleAvailable;
	private TextView subTitleAvailable;

	private TextView line1TextView;
	private TextView line2TextView;
	private TextView line3TextView;
	private TextView line4TextView;

	private Button acceptButton;
	private Dialog acceptDialog;
	
	private APIManager apiManager;
	private UserAPIObject user;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.available_now_layout, null );
		backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
		backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
		discountSeekBar = ( SeekBar ) view.findViewById ( R.id.discountSeekBar );
		discountPersent = ( TextView ) view.findViewById ( R.id.discountPersent );

		titleAvailable = ( TextView ) view.findViewById ( R.id.titleAvailable );
		subTitleAvailable = ( TextView ) view.findViewById ( R.id.subTitleAvailable );

		line1TextView = ( TextView ) view.findViewById ( R.id.line1TextView );
		line2TextView = ( TextView ) view.findViewById ( R.id.line2TextView );
		line3TextView = ( TextView ) view.findViewById ( R.id.line3TextView );
		line4TextView = ( TextView ) view.findViewById ( R.id.line4TextView );

		acceptButton = ( Button ) view.findViewById ( R.id.acceptButton );

		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		apiManager = APIManager.getInstance ();
		user = apiManager.getUser ();
		setListeners ();
		updateViews ();
	}

	private void updateViews ()
	{
		discountPersent.setText ( makePercentString ( MIN_PERCENT ) );
		discountSeekBar.setProgress ( 0 );
		discountSeekBar.setMax ( 35 );
		FontUtils.getInstance ( getActivity () ).applyStyle ( titleAvailable, FontStyle.BOLD );
		FontUtils.getInstance ( getActivity () ).applyStyle ( subTitleAvailable, FontStyle.REGULAR );
		FontUtils.getInstance ( getActivity () ).applyStyle ( line1TextView, FontStyle.SEMIBOLD );
		FontUtils.getInstance ( getActivity () ).applyStyle ( line2TextView, FontStyle.SEMIBOLD );
		FontUtils.getInstance ( getActivity () ).applyStyle ( line3TextView, FontStyle.SEMIBOLD );
		FontUtils.getInstance ( getActivity () ).applyStyle ( line4TextView, FontStyle.LIGHT );
		FontUtils.getInstance ( getActivity () ).applyStyle ( discountPersent, FontStyle.EXTRA_BOLD );
	}

	private String makePercentString ( int percent )
	{
		return percent + "% off";
	}

	private void setListeners ()
	{
		backTextView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				controller.onBackPressed ();
			}
		} );

		backImageView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
				controller.onBackPressed ();
			}
		} );

		discountSeekBar.setOnSeekBarChangeListener ( new OnSeekBarChangeListener ()
		{
			@Override
			public void onStopTrackingTouch ( SeekBar seekBar )
			{
			}

			@Override
			public void onStartTrackingTouch ( SeekBar seekBar )
			{
			}

			@Override
			public void onProgressChanged ( SeekBar seekBar, int progress, boolean fromUser )
			{
				discountPersent.setText ( makePercentString ( progress + MIN_PERCENT ) );
			}
		} );

		acceptButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
				showAcceptDialog ();
			}
		} );
	}

	private void sendAvailableNow ()
	{
		new AsyncTask < Void, Void, String > ()
		{
			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					JSONObject postParameters = new JSONObject ();
					postParameters.accumulate ( UserParams.SERVICE_ID.toString (), user.getString ( UserParams.SERVICE_ID ) );
					postParameters.accumulate ( "discount", discountSeekBar.getProgress () + MIN_PERCENT );
					String request = ServerManager.postRequest ( ServerManager.ADD_AVAILABLE_NOW, postParameters );
					JSONObject jsonRequest = new JSONObject(request);
					result = jsonRequest.getString ( "parameters" );
				} catch ( Exception e )
				{
					result = e.getMessage ();
					e.printStackTrace ();
				}
				return result;
			}

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
					CommunicationSearch communicationSearch = new CommunicationSearch ();
					communicationSearch.sort = SearchType.AVAILABLE_NOW;
					controller.setCommunicationValue ( CommunicationSearch.class.getSimpleName (), communicationSearch );
					controller.setState ( VIEW_STATE.FEED );
				} else
				{
					
				}
			}
		}.execute ();
	}

	private void showAcceptDialog ()
	{
		if ( acceptDialog == null )
		{
			acceptDialog = new Dialog ( getActivity () );
			acceptDialog.requestWindowFeature ( Window.FEATURE_NO_TITLE );
			acceptDialog.setContentView ( R.layout.available_dialog_layout );
			View noButton = acceptDialog.findViewById ( R.id.dialogNoButton );
			View yesButton = acceptDialog.findViewById ( R.id.dialogYesButton );
			noButton.setOnClickListener ( new OnClickListener ()
			{

				@Override
				public void onClick ( View v )
				{
					acceptDialog.dismiss ();
					controller.setState ( VIEW_STATE.DASHBOARD );
				}
			} );

			yesButton.setOnClickListener ( new OnClickListener ()
			{

				@Override
				public void onClick ( View v )
				{
					acceptDialog.dismiss ();
					sendAvailableNow();
				}
			} );

			acceptDialog.getWindow ().setBackgroundDrawable ( new ColorDrawable ( android.graphics.Color.TRANSPARENT ) );
		}

		acceptDialog.show ();
	}
}
