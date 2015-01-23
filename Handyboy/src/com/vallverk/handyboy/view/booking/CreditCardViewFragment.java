package com.vallverk.handyboy.view.booking;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.CreditCardAPIObject;
import com.vallverk.handyboy.model.api.CreditCardAPIObject.CreditCardParams;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.server.ServerManager;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.SingleChoiceSpinner;
import android.animation.LayoutTransition;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CreditCardViewFragment extends BaseFragment
{
	/*
	 * http://help-api.vallverk.com/method/detail/id/83
	 * http://help-api.vallverk.com/method/detail/id/84
	 * http://help-api.vallverk.com/method/detail/id/85
	 * http://help-api.vallverk.com/method/detail/id/86
	 */

	// Pattern creditCardPattern = Pattern.compile (
	// "^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|6(?:011|5[0-9]{2})[0-9]{12}|(?:2131|1800|35\\d{3})\\d{11})$"
	// );

	private ImageView backImageView;
	private TextView backTextView;

	private APIManager apiManager;
	private UserAPIObject user;

	private TextView cvvTitleTextView;
	private ImageView cvvDetailsImageView;
	private View openCardDetailsLayout;
	private View addCreditCardDetailsLayout;
	private LinearLayout mainContainer;
	private LinearLayout useCreditCardLayout;
	private EditText cardNumberEditText;
	private Button submitNewCardButton;
	private Button saveButton;
	private TextView cardExpDateEditText;
	private TextView cardNameEditText;
	private TextView cvvEditText;
	private TextView zipEditText;

	private SingleChoiceSpinner creditCardsSpinner;
	private long selectedDate;

	private List < CreditCardAPIObject > listCreditCardAPIObject;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.credit_card_layout, container, false );
			backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
			backTextView = ( TextView ) view.findViewById ( R.id.backTextView );

			cvvTitleTextView = ( TextView ) view.findViewById ( R.id.cvvTitleTextView );
			cvvDetailsImageView = ( ImageView ) view.findViewById ( R.id.cvvDetailsImageView );
			openCardDetailsLayout = view.findViewById ( R.id.openCardDetailsLayout );
			addCreditCardDetailsLayout = view.findViewById ( R.id.addCreditCardDetailsLayout );
			mainContainer = ( LinearLayout ) view.findViewById ( R.id.mainContainer );
			useCreditCardLayout = ( LinearLayout ) view.findViewById ( R.id.useCreditCardLayout );
			cardNumberEditText = ( EditText ) view.findViewById ( R.id.cardNumberEditText );
			submitNewCardButton = ( Button ) view.findViewById ( R.id.submitNewCardButton );
			cardExpDateEditText = ( TextView ) view.findViewById ( R.id.cardExpDateEditText );
			creditCardsSpinner = ( SingleChoiceSpinner ) view.findViewById ( R.id.creditCardsSpinner );
			cardNameEditText = ( TextView ) view.findViewById ( R.id.cardNameEditText );
			cvvEditText = ( TextView ) view.findViewById ( R.id.cvvEditText );
			zipEditText = ( TextView ) view.findViewById ( R.id.zipEditText );
			saveButton = ( Button ) view.findViewById ( R.id.saveButton );

			mainContainer.getLayoutTransition ().enableTransitionType ( LayoutTransition.CHANGING );

		} else
		{
			( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}

	@Override
	protected void init ()
	{
		getCreditCards ();
		updateComponents ();
		addListeners ();
		updateFonts ();
	}

	private void getCreditCards ()
	{
		new AsyncTask < Void, Void, String > ()
		{

			@Override
			protected String doInBackground ( Void... params )
			{
				String status = "";
				try
				{
					listCreditCardAPIObject = new ArrayList < CreditCardAPIObject > ();
					listCreditCardAPIObject = apiManager.loadList ( ServerManager.GET_CREDIT_CARDS + user.getId (), CreditCardAPIObject.class );
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
				updateCardList ();
			}
		}.execute ();
	}

	private void saveCreditCard ()
	{
		final String creditCardName = cardNameEditText.getText ().toString ();
		if ( creditCardName.isEmpty () )
		{
			Toast.makeText ( controller, "Enter credit card name", Toast.LENGTH_LONG ).show ();
			return;
		}

		final String creditCardNumber = cardNumberEditText.getText ().toString ();
		if ( creditCardNumber.isEmpty () )
		{
			Toast.makeText ( controller, "Enter credit card number", Toast.LENGTH_LONG ).show ();
			return;
		}

		/*
		 * if ( creditCardPattern.matcher ( creditCardNumber ).matches () ) {
		 * Toast.makeText ( controller, "Credit card number not valid",
		 * Toast.LENGTH_LONG ).show (); }
		 */

		final String cvv2 = cvvEditText.getText ().toString ();
		if ( cvv2.isEmpty () )
		{
			Toast.makeText ( controller, "Enter credit card cvv", Toast.LENGTH_LONG ).show ();
			return;
		}

		final String expDate = cardExpDateEditText.getText ().toString ();
		if ( expDate.isEmpty () )
		{
			Toast.makeText ( controller, "Enter credit card expdate", Toast.LENGTH_LONG ).show ();
			return;
		}

		/*final String zip = zipEditText.getText ().toString ();
		if ( zip.isEmpty () )
		{
			Toast.makeText ( controller, "Enter credit card zip", Toast.LENGTH_LONG ).show ();
			return;
		}*/

		new AsyncTask < Void, Void, String > ()
		{

			@Override
			protected String doInBackground ( Void... params )
			{
				String status = "";
				try
				{
					JSONObject newCreditCardJsonObject = new JSONObject ();
					newCreditCardJsonObject.accumulate ( CreditCardParams.CARD_NAME.toString (), creditCardName );
					newCreditCardJsonObject.accumulate ( CreditCardParams.CARD_NUMBER.toString (), creditCardNumber );
					newCreditCardJsonObject.accumulate ( CreditCardParams.CVV.toString (), cvv2 );
					newCreditCardJsonObject.accumulate ( CreditCardParams.EXP_DATE.toString (), expDate );
					// newCreditCardJsonObject.accumulate (
					// CreditCardParams.ZIP_CODE.toString (), zip );
					newCreditCardJsonObject.accumulate ( CreditCardParams.USER_ID.toString (), user.getId () );
					Log.d("LOG", newCreditCardJsonObject.toString ());
					String request = ServerManager.postRequest ( ServerManager.SAVE_CARD, newCreditCardJsonObject );
					JSONObject requestJsonObject = new JSONObject ( request );
					status = requestJsonObject.getString ( "parameters" );
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
				if ( status.isEmpty () )
				{
					addCreditCardDetailsLayout.setVisibility ( View.GONE );
					submitNewCardButton.setVisibility ( View.GONE );
					cardNumberEditText.setText ( "" );
					cardNameEditText.setText ( "" );
					cardExpDateEditText.setText ( "" );
					cvvEditText.setText ( "" );
					zipEditText.setText ( "" );
					getCreditCards ();
				} else
				{
					Toast.makeText ( controller, status, Toast.LENGTH_LONG ).show ();
				}
			}
		}.execute ();
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		apiManager = APIManager.getInstance ();
		user = apiManager.getUser ();
	}

	public void updateCardList ()
	{
		if ( listCreditCardAPIObject != null && listCreditCardAPIObject.size () > 0 )
		{
			useCreditCardLayout.setVisibility ( View.VISIBLE );
			saveButton.setVisibility ( View.VISIBLE );
			List < String > cardNamesList = new ArrayList < String > ();
			for ( CreditCardAPIObject tempObject : listCreditCardAPIObject )
			{
				cardNamesList.add ( tempObject.getString ( CreditCardParams.CARD_NAME ) );
			}
			creditCardsSpinner.setData ( cardNamesList.toArray () );
			CreditCardAPIObject savedcreditCardAPIObject = controller.getBookingController ().getCreditCard ();
			if ( savedcreditCardAPIObject != null )
			{
				creditCardsSpinner.setSelected ( savedcreditCardAPIObject.getString ( CreditCardParams.CARD_NAME ) );
			}
		} else
		{
			useCreditCardLayout.setVisibility ( View.GONE );
			saveButton.setVisibility ( View.GONE );
		}
	}

	private void updateComponents ()
	{

	}

	/*
	 * public abstract class DatePickerFragment extends DialogFragment
	 * implements DatePickerDialog.OnDateSetListener {
	 * 
	 * @Override public Dialog onCreateDialog ( Bundle savedInstanceState ) {
	 * final Calendar c = Calendar.getInstance (); int year = c.get (
	 * Calendar.YEAR ); int month = c.get ( Calendar.MONTH ); int day = c.get (
	 * Calendar.DAY_OF_MONTH ); return new DatePickerDialog ( getActivity (),
	 * this, year, month, day ); } }
	 */

	private OnDateSetListener onDateSetListener = new OnDateSetListener ()
	{

		@Override
		public void onDateSet ( DatePicker view, int year, int monthOfYear, int dayOfMonth )
		{
			Calendar calendar = Calendar.getInstance ();
			calendar.set ( year, monthOfYear + 1, dayOfMonth );
			selectedDate = calendar.getTimeInMillis ();
			cardExpDateEditText.setText ( Tools.fromTimestampToString ( selectedDate, "MM/yy" ) );
		}
	};

	private void createDialogWithoutDateField ()
	{
		final Calendar c = Calendar.getInstance ();
		int year = c.get ( Calendar.YEAR );
		int month = c.get ( Calendar.MONTH );
		int day = c.get ( Calendar.DAY_OF_MONTH );
		DatePickerDialog dp = new DatePickerDialog ( controller, onDateSetListener, year, month, day );

		try
		{
			Field[] datePickerDialogFields = dp.getClass ().getDeclaredFields ();
			for ( Field datePickerDialogField : datePickerDialogFields )
			{
				if ( datePickerDialogField.getName ().equals ( "mDatePicker" ) )
				{
					datePickerDialogField.setAccessible ( true );
					DatePicker datePicker = ( DatePicker ) datePickerDialogField.get ( dp );
					Field datePickerFields[] = datePickerDialogField.getType ().getDeclaredFields ();
					for ( Field datePickerField : datePickerFields )
					{
						if ( "mDayPicker".equals ( datePickerField.getName () ) || "mDaySpinner".equals ( datePickerField.getName () ) )
						{
							datePickerField.setAccessible ( true );
							Object dayPicker = new Object ();
							dayPicker = datePickerField.get ( datePicker );
							( ( View ) dayPicker ).setVisibility ( View.GONE );
						}
					}
				}

			}
		} catch ( Exception ex )
		{
			ex.printStackTrace ();
		}
		dp.show ();
	}	

	private OnClickListener dateClickListener = new OnClickListener ()
	{
		@Override
		public void onClick ( View v )
		{
			createDialogWithoutDateField ();

		}
	};

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

		cvvTitleTextView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				if ( cvvDetailsImageView.getVisibility () == View.GONE )
				{
					cvvDetailsImageView.setVisibility ( View.VISIBLE );
				} else
				{
					cvvDetailsImageView.setVisibility ( View.GONE );
				}

			}
		} );

		openCardDetailsLayout.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				addCreditCardDetailsLayout.setVisibility ( View.VISIBLE );
				submitNewCardButton.setVisibility ( View.VISIBLE );
			}
		} );

		cardNumberEditText.setOnFocusChangeListener ( new OnFocusChangeListener ()
		{

			@Override
			public void onFocusChange ( View v, boolean hasFocus )
			{
				if ( hasFocus )
				{
					addCreditCardDetailsLayout.setVisibility ( View.VISIBLE );
					submitNewCardButton.setVisibility ( View.VISIBLE );
				}
			}
		} );

		saveButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
				controller.getBookingController ().setCreditCard ( listCreditCardAPIObject.get ( creditCardsSpinner.getSelectedItemPosition () ) );
				controller.onBackPressed ();
			}
		} );
		cardExpDateEditText.setOnClickListener ( dateClickListener );
		submitNewCardButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				saveCreditCard ();
			}
		} );
	}
}