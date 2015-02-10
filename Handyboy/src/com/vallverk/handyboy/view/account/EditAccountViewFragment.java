package com.vallverk.handyboy.view.account;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.vallverk.handyboy.FileManager;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.SettingsManager;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.AddressAPIObject;
import com.vallverk.handyboy.model.api.AddressAPIObject.AddressParams;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.FontUtils;
import com.vallverk.handyboy.view.base.FontUtils.FontStyle;

import org.json.JSONObject;

import java.util.List;

public class EditAccountViewFragment extends BaseFragment
{
	private UserAPIObject user;
	private APIManager apiManager;


	// UI
	private TextView accountNameTextView;
	private TextView accountIdTextView;

	private TextView emailTitle;
	private TextView passwordTitle;
	private TextView phoneTitle;
	private TextView savedAdressTitle;
	private TextView bankAccountTextView;

	private EditText phoneEditText;
	private TextView emailEditText;
	private TextView passwordEditText;

	private TextView pushNotificationTextView;
	private TextView emailNotificationTextView;

	private ToggleButton pushNotificationTogglebutton;
	private ToggleButton emailNotificationTogglebutton;

	private View pushNotificationLayout;
	private View addAdressButton;

	private ImageView menuImageView;

	private TextView transactionHistoryButton;

	private TextView blockListButton;

	public List < AddressAPIObject > listAddressAPIObjects;
	public LinearLayout addressContainer;
	public LayoutInflater layoutInflater;

    public  Dialog acceptDialog;
    public View deleteAccountButton;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.account_edit_layout, null );
		accountNameTextView = ( TextView ) view.findViewById ( R.id.accountNameTextView );
		accountIdTextView = ( TextView ) view.findViewById ( R.id.accountIdTextView );

		emailTitle = ( TextView ) view.findViewById ( R.id.emailTitle );
		passwordTitle = ( TextView ) view.findViewById ( R.id.passwordTitle );
		phoneTitle = ( TextView ) view.findViewById ( R.id.phoneTitle );
		savedAdressTitle = ( TextView ) view.findViewById ( R.id.savedAdressTitle );

		phoneEditText = ( EditText ) view.findViewById ( R.id.phoneEditText );
		emailEditText = ( TextView ) view.findViewById ( R.id.emailEditText );
		passwordEditText = ( TextView ) view.findViewById ( R.id.passwordEditText );

		bankAccountTextView = ( TextView ) view.findViewById ( R.id.bankAccountTextView );

		pushNotificationTextView = ( TextView ) view.findViewById ( R.id.pushNotificationTextView );
		emailNotificationTextView = ( TextView ) view.findViewById ( R.id.emailNotificationTextView );

		// saveTopBarImageView = ( ImageView ) view.findViewById (
		// R.id.saveTopBarImageView );
		// saveTopBarTextView = ( TextView ) view.findViewById (
		// R.id.saveTopBarTextView );

		pushNotificationTogglebutton = ( ToggleButton ) view.findViewById ( R.id.pushNotificationTogglebutton );
		emailNotificationTogglebutton = ( ToggleButton ) view.findViewById ( R.id.emailNotificationTogglebutton );

		transactionHistoryButton = ( TextView ) view.findViewById ( R.id.transactionHistoryButton );

		menuImageView = ( ImageView ) view.findViewById ( R.id.menuImageView );

		blockListButton = ( TextView ) view.findViewById ( R.id.blockListButton );

		pushNotificationLayout = view.findViewById ( R.id.pushNotificationLayout );
		addAdressButton = view.findViewById ( R.id.addAdressButton );

		addressContainer = ( LinearLayout ) view.findViewById ( R.id.addressContainer );
        deleteAccountButton = view.findViewById(R.id.deleteAccountButton);

		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		apiManager = APIManager.getInstance ();
		user = apiManager.getUser ();
		layoutInflater = LayoutInflater.from ( controller );

		updateComponents ();
		addListeners ();
		updateFonts ();
	}

	@Override
	protected void init ()
	{
		// UserAPIObject user = APIManager.getInstance ().getUser ();
		// if ( user == null )
		// {
		// return;
		// }
		if ( user.isService () )
		{
            pushNotificationLayout.setVisibility ( View.GONE );
		} else
		{
            pushNotificationLayout.setVisibility ( View.VISIBLE );
		}

		addAdressButton.setVisibility ( View.VISIBLE );
		new AsyncTask < Void, Void, String > ()
		{

			@Override
			protected String doInBackground ( Void... params )
			{
				String status = "";
				try
				{
					listAddressAPIObjects = apiManager.loadList ( ServerManager.GET_ADDRESSES + user.getId (), AddressAPIObject.class );
				} catch ( Exception e )
				{
					e.printStackTrace ();
				}
				return status;
			}

			public void onPreExecute ()
			{
				super.onPreExecute ();
				controller.showLoader ();
			}

			public void onPostExecute ( String status )
			{
				super.onPostExecute ( status );
				controller.hideLoader ();
				updateAddressList ();
			}
		}.execute ();
	}

	private void updateAddressList ()
	{
		addressContainer.removeAllViews ();
		if ( listAddressAPIObjects.size () > 0 )
		{
			savedAdressTitle.setVisibility ( View.VISIBLE );
			for ( AddressAPIObject addressAPIObject : listAddressAPIObjects )
			{
				LinearLayout addressItem = ( LinearLayout ) layoutInflater.inflate ( R.layout.account_address_item, null );

				TextView addressDescription = ( TextView ) addressItem.findViewById ( R.id.addressDescription );
				addressDescription.setText ( addressAPIObject.getString ( AddressParams.DESCRIPTION ) );

				addressDescription.setTag ( addressAPIObject );
				addressDescription.setOnClickListener ( new OnClickListener ()
				{
					@Override
					public void onClick ( View view )
					{
						controller.setCommunicationValue ( AddressAPIObject.class.getSimpleName (), view.getTag () );
						controller.setState ( VIEW_STATE.ADD_ADDRESS );
					}
				} );
				addressContainer.addView ( addressItem );
			}
		} else
		{
			savedAdressTitle.setVisibility ( View.GONE );
		}
	}

	private void updateComponents ()
	{
		accountNameTextView.setText ( user.getValue ( UserParams.FIRST_NAME ).toString () + " " + user.getValue ( UserParams.LAST_NAME ).toString ().charAt ( 0 ) );
		accountNameTextView.setSelected ( true );
		accountIdTextView.setText ( "ID: " + user.getId ().toString () );

		emailEditText.setHint ( user.getValue ( UserParams.EMAIL ).toString () );
		phoneEditText.setText ( user.getValue ( UserParams.PHONE_NUMBER ).toString () );

        pushNotificationTogglebutton.setChecked(SettingsManager.getBoolean (SettingsManager.Params.IS_PUSH_NOTIFICATION, true, controller));
	}

	protected void addListeners ()
	{
		pushNotificationTogglebutton.setOnCheckedChangeListener ( new OnCheckedChangeListener ()
		{

			@Override
			public void onCheckedChanged ( CompoundButton compoundButton, boolean isChecked )
			{
                SettingsManager.setBoolean (SettingsManager.Params.IS_PUSH_NOTIFICATION, isChecked, controller);
			}
		} );

		menuImageView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				controller.openMenu ();
			}
		} );

		transactionHistoryButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				controller.setState ( VIEW_STATE.TRANSACTION_HISTORY );
			}
		} );

		blockListButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				controller.setState ( VIEW_STATE.BLOCK_LIST );
			}
		} );

		phoneEditText.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				controller.setState ( VIEW_STATE.CHANGE_PHONE );
			}
		} );
		addAdressButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				controller.setState ( VIEW_STATE.ADD_ADDRESS );
			}
		} );

		bankAccountTextView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View v )
			{
				controller.setState ( VIEW_STATE.CREDIT_CARD );
			}
		} );

        deleteAccountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showAcceptDialog();
            }
        });
	}

    private void deleteUser(){
        new AsyncTask < Void, Void, String > ()
        {

            @Override
            protected String doInBackground ( Void... params )
            {
                String status = "";
                try
                {
                    //listAddressAPIObjects = apiManager.loadList ( ServerManager.GET_ADDRESSES + user.getId (), AddressAPIObject.class );
                    JSONObject jsonParams = new JSONObject();
                    jsonParams.put("id", user.getId());
                    JSONObject request = new JSONObject(ServerManager.postRequest(ServerManager.DELETE_USER_ACCOUNT, jsonParams.toString()));
                    status = request.getString("parameters");
                } catch ( Exception e )
                {
                    e.printStackTrace ();
                }
                return status;
            }

            public void onPreExecute ()
            {
                super.onPreExecute ();
            }

            public void onPostExecute ( String status )
            {
                super.onPostExecute ( status );
                if(status.isEmpty()){
                    //controller.setState ( VIEW_STATE.EXIT );
                    controller.logout();
                }
            }
        }.execute ();
    }

	@Override
	protected void updateFonts ()
	{
		FontUtils.getInstance ( getActivity () ).applyStyle ( accountNameTextView, FontStyle.EXTRA_BOLD );
		FontUtils.getInstance ( getActivity () ).applyStyle ( accountIdTextView, FontStyle.REGULAR );

		FontUtils.getInstance ( getActivity () ).applyStyle ( emailTitle, FontStyle.SEMIBOLD );
		FontUtils.getInstance ( getActivity () ).applyStyle ( passwordTitle, FontStyle.SEMIBOLD );
		FontUtils.getInstance ( getActivity () ).applyStyle ( phoneTitle, FontStyle.SEMIBOLD );
		FontUtils.getInstance ( getActivity () ).applyStyle ( savedAdressTitle, FontStyle.SEMIBOLD );

		FontUtils.getInstance ( getActivity () ).applyStyle ( phoneEditText, FontStyle.BOLD );
		FontUtils.getInstance ( getActivity () ).applyStyle ( emailEditText, FontStyle.BOLD );
		FontUtils.getInstance ( getActivity () ).applyStyle ( passwordEditText, FontStyle.BOLD );

		FontUtils.getInstance ( getActivity () ).applyStyle ( pushNotificationTextView, FontStyle.SEMIBOLD );
		FontUtils.getInstance ( getActivity () ).applyStyle ( emailNotificationTextView, FontStyle.SEMIBOLD );

	}

    private void showAcceptDialog ()
    {
        if ( acceptDialog == null )
        {
            acceptDialog = new Dialog( getActivity () );
            acceptDialog.requestWindowFeature ( Window.FEATURE_NO_TITLE );
            acceptDialog.setContentView ( R.layout.available_dialog_layout );
            View noButton = acceptDialog.findViewById ( R.id.dialogNoButton );
            View yesButton = acceptDialog.findViewById ( R.id.dialogYesButton );
            acceptDialog.findViewById(R.id.dialogBodyTextView).setVisibility(View.GONE);
            TextView dialogTitleTextView = (TextView) acceptDialog.findViewById(R.id.dialogTitleTextView);
            dialogTitleTextView.setText(controller.getString(R.string.delete_account_dialog_title));
            noButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    acceptDialog.dismiss();
                }
            });

            yesButton.setOnClickListener ( new OnClickListener ()
            {

                @Override
                public void onClick ( View v )
                {
                    acceptDialog.dismiss ();
                    deleteUser();
                }
            } );

            acceptDialog.getWindow ().setBackgroundDrawable ( new ColorDrawable( android.graphics.Color.TRANSPARENT ) );
        }

        acceptDialog.show ();
    }

}