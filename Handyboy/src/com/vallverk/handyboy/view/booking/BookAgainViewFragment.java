package com.vallverk.handyboy.view.booking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.view.base.BaseFragment;

public class BookAgainViewFragment extends BaseFragment
{
	private ImageView backImageView;
	private TextView backTextView;
    private View cancelButton;
    private View bookHimAgainButton;
	private APIManager apiManager;
	private UserAPIObject user;
    private BookingDataManager bookingDataManager;
	
	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.book_again_layout, container, false );
            cancelButton = view.findViewById(R.id.cancelButton);
            bookHimAgainButton = view.findViewById(R.id.bookHimAgainButton);
		} else
		{
			//( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		apiManager = APIManager.getInstance ();
		user = apiManager.getUser ();
        bookingDataManager = BookingDataManager.getInstance ();
		addListeners ();
	}

	@Override
	protected void updateFonts ()
	{
		// FontUtils.getInstance ( controller ).applyStyle ( noFavoritesTitle,
		// FontStyle.LIGHT );
		// FontUtils.getInstance ( controller ).applyStyle ( noFavoritesBody,
		// FontStyle.REGULAR );
	}

	private void addListeners ()
	{
		/*backTextView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				controller.onBackPressed ();
			}
		} );*/

		/*backImageView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
				controller.onBackPressed ();
			}
		} );*/

        bookHimAgainButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setCommunicationValue(UserAPIObject.class.getSimpleName (), bookingDataManager.getActiveBooking ().getService() );
                controller.setState(ViewStateController.VIEW_STATE.HANDY_BOY_PAGE);
            }
        });

        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setState(ViewStateController.VIEW_STATE.GIGS);
            }
        });
	}
}