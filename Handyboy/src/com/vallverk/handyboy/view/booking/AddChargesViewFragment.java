package com.vallverk.handyboy.view.booking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.view.base.BaseFragment;

public class AddChargesViewFragment extends BaseFragment {
    private ImageView backImageView;
    private TextView backTextView;
    private View crossImageView;
    private APIManager apiManager;
    private UserAPIObject user;
    private EditText reasonEditText;
    private EditText priceEditText;
    private TextView sendRequestButton;
    private TextView cancelButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.add_charges_layout, container, false);
            backImageView = (ImageView) view.findViewById(R.id.backImageView);
            backTextView = (TextView) view.findViewById(R.id.backTextView);
            crossImageView = view.findViewById(R.id.crossImageView);
            reasonEditText = (EditText) view.findViewById(R.id.reasonEditText);
            priceEditText = (EditText) view.findViewById(R.id.priceEditText);
            sendRequestButton = (TextView) view.findViewById(R.id.sendRequestButton);
            cancelButton = (TextView) view.findViewById(R.id.cancelButton);
        } else {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        apiManager = APIManager.getInstance();
        user = apiManager.getUser();
        initViews();
        addListeners();
    }

    private void initViews() {
        float scale = getResources().getDisplayMetrics().density;
    }

    @Override
    protected void updateFonts() {
        // FontUtils.getInstance ( controller ).applyStyle ( noFavoritesTitle,
        // FontStyle.LIGHT );
        // FontUtils.getInstance ( controller ).applyStyle ( noFavoritesBody,
        // FontStyle.REGULAR );
    }

    private void addListeners() {
        backTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                controller.onBackPressed();
            }
        });
        backImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                controller.onBackPressed();
            }
        });
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.onBackPressed();
            }
        });
        sendRequestButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
    }

    private void send() {
    }
}