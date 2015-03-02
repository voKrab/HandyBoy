package com.vallverk.handyboy.view;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.AddressAPIObject;
import com.vallverk.handyboy.model.api.BankAccountAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserDetailsAPIObject;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.SingleChoiceSpinner;

import org.json.JSONException;
import org.json.JSONObject;

public class BankAccountViewFragment extends BaseFragment {
    private APIManager apiManager;
    private UserAPIObject user;

    //UI
    private View backImageView;
    private View backTextView;
    private EditText firstEditText;
    private EditText lastNameEditText;
    private EditText accountNumberEditText;
    private EditText routingNumberEditText;
    private EditText ssnNumberEditText;
    private EditText addressEditText;
    private EditText cityEditText;
    private EditText zipEditText;
    private TextView stateTextView;

    private JSONObject individualInfo;
    private Dialog acceptDialog;

    private SingleChoiceSpinner stateSpinner;
    private View saveButton;
    private boolean isUpdate;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bank_account_layout, null);
        backImageView = view.findViewById(R.id.backImageView);
        backTextView = view.findViewById(R.id.backTextView);
        firstEditText = (EditText) view.findViewById(R.id.firstEditText);
        lastNameEditText = (EditText) view.findViewById(R.id.lastNameEditText);
        accountNumberEditText = (EditText) view.findViewById(R.id.accountNumberEditText);
        routingNumberEditText = (EditText) view.findViewById(R.id.routingNumberEditText);
        ssnNumberEditText = (EditText) view.findViewById(R.id.ssnNumberEditText);
        addressEditText = (EditText) view.findViewById(R.id.addressEditText);
        cityEditText = (EditText) view.findViewById(R.id.cityEditText);
        zipEditText = (EditText) view.findViewById(R.id.zipEditText);
        stateTextView = (TextView) view.findViewById(R.id.stateTextView);
        stateSpinner = ( SingleChoiceSpinner ) view.findViewById ( R.id.stateSpinner );
        saveButton = view.findViewById(R.id.saveButton);
        return view;
    }

    @Override
    protected void init() {
        new AsyncTask<Void, Void, String>() {
            public void onPreExecute() {
                super.onPreExecute();
                controller.showLoader();
            }

            public void onPostExecute(String result) {
                super.onPostExecute(result);
                controller.hideLoader();
                if(result.isEmpty()){
                    //Toast.makeText(controller, result, Toast.LENGTH_LONG).show();
                    updateComponents();
                    isUpdate = true;
                }else{
                    isUpdate = false;
                }
            }



            @Override
            protected String doInBackground(Void... params) {
                String result = "";
                try{
                    JSONObject request = new JSONObject(ServerManager.getRequest(ServerManager.GET_BANK_ACCOUNT + user.getId() ));
                    request.getString("parameters");
                    individualInfo = request.getJSONObject("object").getJSONObject("individual");
                }catch (Exception ex){
                    result = ex.getMessage();
                    //Toast.makeText(controller, result, Toast.LENGTH_LONG).show();
                }

                return result;
            }
        }.execute();
    }

    private void showAcceptDialog ()
    {
        if ( acceptDialog == null )
        {
            acceptDialog = new Dialog( getActivity () );
            acceptDialog.requestWindowFeature ( Window.FEATURE_NO_TITLE );
            acceptDialog.setContentView ( R.layout.available_dialog_layout );
            TextView dialogTitleTextView = (TextView) acceptDialog.findViewById(R.id.dialogTitleTextView);
            dialogTitleTextView.setText("Update bank account?");
            TextView dialogBodyTextView = (TextView) acceptDialog.findViewById(R.id.dialogBodyTextView);
            dialogBodyTextView.setText( controller.getString(R.string.re_entered_bank_account_info));
            View noButton = acceptDialog.findViewById ( R.id.dialogNoButton );
            View yesButton = acceptDialog.findViewById ( R.id.dialogYesButton );
            noButton.setOnClickListener ( new OnClickListener ()
            {
                @Override
                public void onClick ( View v )
                {
                    acceptDialog.dismiss ();
                }
            } );

            yesButton.setOnClickListener ( new OnClickListener ()
            {
                @Override
                public void onClick ( View v )
                {
                    acceptDialog.dismiss ();
                    accountNumberEditText.setText("");
                    routingNumberEditText.setText("");
                    ssnNumberEditText.setText("");

                    firstEditText.setOnFocusChangeListener(null);
                    lastNameEditText.setOnFocusChangeListener(null);
                    accountNumberEditText.setOnFocusChangeListener(null);
                    routingNumberEditText.setOnFocusChangeListener(null);
                    ssnNumberEditText.setOnFocusChangeListener(null);
                    ssnNumberEditText.setOnFocusChangeListener(null);
                    addressEditText.setOnFocusChangeListener(null);
                    cityEditText.setOnFocusChangeListener(null);
                    zipEditText.setOnFocusChangeListener(null);
                }
            } );

            acceptDialog.getWindow ().setBackgroundDrawable ( new ColorDrawable( android.graphics.Color.TRANSPARENT ) );
        }

        acceptDialog.show ();
    }

    private void updateComponents(){

        if(individualInfo != null){
            try {
                firstEditText.setText(individualInfo.getString("firstName").toString());
                lastNameEditText.setText(individualInfo.getString("lastName").toString());
                accountNumberEditText.setText("********");
                routingNumberEditText.setText("********");
                ssnNumberEditText.setText("********");

                JSONObject addressData = individualInfo.getJSONObject("address");
                addressEditText.setText(addressData.getString("streetAddress"));
                cityEditText.setText(addressData.getString("locality"));
                zipEditText.setText(addressData.getString("postalCode"));
                stateTextView.setText(addressData.getString("region"));
                stateSpinner.setSelected(addressData.getString("region"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        /*if ( selectedAddressAPIObject != null )
        {
            String[] address = selectedAddressAPIObject.getString ( AddressAPIObject.AddressParams.ADDRESS ).toString ().split ( "," );
            addressDescriptionEditText.setText ( selectedAddressAPIObject.getString ( AddressAPIObject.AddressParams.DESCRIPTION ) );
            addressEditText.setText ( address.length > 0 ? address[0] : "" );
            subAddressEditText.setText ( address.length > 1 ? address[1] : "" );
            cityEditText.setText ( selectedAddressAPIObject.getString ( AddressAPIObject.AddressParams.CITY ) );
            zipEditText.setText ( selectedAddressAPIObject.getString ( AddressAPIObject.AddressParams.ZIP_CODE ) );
            stateTextView.setText ( selectedAddressAPIObject.getString ( AddressAPIObject.AddressParams.STATE ) );
        }*/
    }

    private void saveBankAccount() {

        final String firstName = firstEditText.getText().toString().trim();
        if (firstName.isEmpty()) {
            Toast.makeText(getActivity(), R.string.whats_your_name, Toast.LENGTH_LONG).show();
            return;
        }
        final String lastName = lastNameEditText.getText().toString().trim();
        if (lastName.isEmpty()) {
            Toast.makeText(getActivity(), R.string.whats_your_name, Toast.LENGTH_LONG).show();
            return;
        }

        final String accountNumber = accountNumberEditText.getText().toString().trim();
        if (accountNumber.isEmpty()) {
            Toast.makeText(getActivity(), R.string.what_account_number_massage, Toast.LENGTH_LONG).show();
            return;
        }

        final String routingNumber = routingNumberEditText.getText().toString().trim();
        if (routingNumber.isEmpty()) {
            Toast.makeText(getActivity(), R.string.what_routing_number_massage, Toast.LENGTH_LONG).show();
            return;
        }

        final String ssnNumber = ssnNumberEditText.getText().toString().trim();
        if (ssnNumber.isEmpty()) {
            Toast.makeText(getActivity(), R.string.what_routing_ssn_massage, Toast.LENGTH_LONG).show();
            return;
        }

        final String address = addressEditText.getText().toString().trim();
        if (address.isEmpty()) {
            Toast.makeText(getActivity(), R.string.what_address_massage, Toast.LENGTH_LONG).show();
            return;
        }

        final String city = cityEditText.getText().toString().trim();
        if (city.isEmpty()) {
            Toast.makeText(getActivity(), R.string.what_city_massage, Toast.LENGTH_LONG).show();
            return;
        }

        final String zip = zipEditText.getText().toString().trim();
        if (zip.isEmpty()) {
            Toast.makeText(getActivity(), R.string.what_zip_massage, Toast.LENGTH_LONG).show();
            return;
        }

        new AsyncTask<Void, Void, String>() {
            public void onPreExecute() {
                super.onPreExecute();
                controller.showLoader();
            }

            public void onPostExecute(String result) {
                super.onPostExecute(result);
                controller.hideLoader();
                if(result.isEmpty()){
                    //updateComponents();
                    controller.setState(ViewStateController.VIEW_STATE.YOUR_MONEY);
                }else {
                    if( controller.getString(R.string.re_entered_bank_account_info).equals(result)){
                        showAcceptDialog();
                    }else{
                        Toast.makeText(controller, result, Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            protected String doInBackground(Void... params) {
                String result = "";
                JSONObject postParams = new JSONObject();
                try {
                    postParams.put(BankAccountAPIObject.BankAccountParams.FIRST_NAME.toString(), firstName);
                    postParams.put(BankAccountAPIObject.BankAccountParams.LAST_NAME.toString(), lastName);
                    postParams.put(BankAccountAPIObject.BankAccountParams.ACCOUNT_NUMBER.toString(), accountNumber);
                    postParams.put(BankAccountAPIObject.BankAccountParams.ROUTING_NUMBER.toString(), routingNumber);
                    postParams.put(BankAccountAPIObject.BankAccountParams.SSN.toString(), ssnNumber);
                    postParams.put(BankAccountAPIObject.BankAccountParams.STREET_ADDRESS.toString(), address);
                    postParams.put(BankAccountAPIObject.BankAccountParams.LOCALITY.toString(), city);
                    postParams.put(BankAccountAPIObject.BankAccountParams.POSTAL_CODE.toString(), zip);
                    postParams.put(BankAccountAPIObject.BankAccountParams.REGION.toString(), stateSpinner.getSelectedItem().toString());
                    postParams.put("userId", user.getId().toString());

                    JSONObject request;
                    if(isUpdate){
                        if(accountNumberEditText.getText().toString().equals("********")){
                            result = controller.getString(R.string.re_entered_bank_account_info);
                        }else{
                            request = new JSONObject(ServerManager.postRequest(ServerManager.UPDATE_BANK_ACCOUNT, postParams.toString()));
                            result = request.getString("parameters");
                        }

                    }else {
                        request = new JSONObject(ServerManager.postRequest(ServerManager.SAVE_BANK_ACCOUNT, postParams.toString()));
                        result = request.getString("parameters");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    result = e.getMessage();
                }
                return result;
            }
        }.execute();



    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        apiManager = APIManager.getInstance();
        user = apiManager.getUser();
        stateSpinner.setData ( getActivity ().getResources ().getStringArray ( R.array.states ) );
        addListeners();
    }

    private View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus && isUpdate){
                showAcceptDialog();
            }
        }
    };

    protected void addListeners() {
        backImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.onBackPressed();
            }
        });

        backTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.onBackPressed();
            }
        });

        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBankAccount();
            }
        });

        stateSpinner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected ( AdapterView < ? > adapter, View view, int selected, long l )
            {
                //if(isUpdate){
                   // showAcceptDialog();
               // }
                stateTextView.setText ( adapter.getAdapter ().getItem ( selected ).toString () );
            }

            @Override
            public void onNothingSelected ( AdapterView < ? > adapterView )
            {
            }

        } );

        firstEditText.setOnFocusChangeListener(onFocusChangeListener);
        lastNameEditText.setOnFocusChangeListener(onFocusChangeListener);
        accountNumberEditText.setOnFocusChangeListener(onFocusChangeListener);
        routingNumberEditText.setOnFocusChangeListener(onFocusChangeListener);
        ssnNumberEditText.setOnFocusChangeListener(onFocusChangeListener);
        ssnNumberEditText.setOnFocusChangeListener(onFocusChangeListener);
        addressEditText.setOnFocusChangeListener(onFocusChangeListener);
        cityEditText.setOnFocusChangeListener(onFocusChangeListener);
        zipEditText.setOnFocusChangeListener(onFocusChangeListener);

    }
}