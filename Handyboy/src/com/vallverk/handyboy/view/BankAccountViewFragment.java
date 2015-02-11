package com.vallverk.handyboy.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.view.base.BaseFragment;

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

    private View saveButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bank_account_layout, null);
        backImageView = view.findViewById(R.id.backImageView);
        backTextView = view.findViewById(R.id.backTextView);
        firstEditText = (EditText) view.findViewById(R.id.firstEditText);
        lastNameEditText = (EditText) view.findViewById(R.id.lastNameEditText);
        accountNumberEditText = (EditText) view.findViewById(R.id.accountNumberEditText);
        routingNumberEditText = (EditText) view.findViewById(R.id.routingNumberEditText);
        ssnNumberEditText = (EditText) view.findViewById(R.id.ssnNumberEditText);
        saveButton = view.findViewById(R.id.saveButton);
        return view;
    }

    @Override
    protected void init() {
        new AsyncTask<Void, Void, String>() {
            public void onPreExecute() {
                super.onPreExecute();
            }

            public void onPostExecute(String result) {
                super.onPostExecute(result);
            }

            @Override
            protected String doInBackground(Void... params) {
                String result = "";
                return result;
            }
        }.execute();
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


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        apiManager = APIManager.getInstance();
        user = apiManager.getUser();

        addListeners();
    }

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

            }
        });
    }
}