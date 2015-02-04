package com.vallverk.handyboy.view.feed;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.graphics.Color;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemClickListener;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.AddressWraper;
import com.vallverk.handyboy.model.FilterManager;
import com.vallverk.handyboy.view.AddressAutocompleteAdapter;
import com.vallverk.handyboy.view.base.AutocompleteAdapter;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.MultiChoiceSpinner;
import com.vallverk.handyboy.view.base.RatingView;
import com.vallverk.handyboy.view.base.rangeslider.RangeBar;
import com.vallverk.handyboy.view.base.wheel.AbstractWheel;
import com.vallverk.handyboy.view.base.wheel.adapters.NumericWheelAdapter;

public class FilterViewFragment extends BaseFragment
{
	private static final int START_HOUR = 14;
	private static final int START_MINUTE = 35;
	private static final int MIN_AGE = 18;
	private static final int MAX_AGE = 99;
	
	private ImageView crossImageView;
	private Button searchButton;

	private RangeBar tallRangebar;
	private RangeBar weightRangebar;
	private RangeBar priceRangebar;

	private AbstractWheel minAgeSelector;
	private AbstractWheel maxAgeSelector;

	private MultiChoiceSpinner ethentitySpinner;
	private MultiChoiceSpinner bodyTypeSpinner;
	private MultiChoiceSpinner sexSpinner;

	private TextView monthTextView;
	private TextView dayTextView;
	private TextView yearTextView;
	
	private AutoCompleteTextView whereAtEditText;
	
	private long selectedDate;
	private TextView fromTimeTextView;
	private TextView toTimeTextView;
	private TimePickerDialog fromTimePickerDialog;
	private TimePickerDialog toTimePickerDialog;
	
	private FilterManager filterManager;
	
	private RatingView ratingView;
	

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.filter_layout, null );
		crossImageView = ( ImageView ) view.findViewById ( R.id.crossImageView );

		tallRangebar = ( RangeBar ) view.findViewById ( R.id.tallRangebar );
		weightRangebar = ( RangeBar ) view.findViewById ( R.id.haightRangebar );
		priceRangebar = ( RangeBar ) view.findViewById ( R.id.priceRangebar );

		minAgeSelector = ( AbstractWheel ) view.findViewById ( R.id.minAgeSelector );
		maxAgeSelector = ( AbstractWheel ) view.findViewById ( R.id.maxAgeSelector );

		ethentitySpinner = ( MultiChoiceSpinner ) view.findViewById ( R.id.ethentitySpinner );
		bodyTypeSpinner = ( MultiChoiceSpinner ) view.findViewById ( R.id.bodyTypeSpinner );
		sexSpinner = ( MultiChoiceSpinner ) view.findViewById ( R.id.sexSpinner );

		monthTextView = ( TextView ) view.findViewById ( R.id.monthTextView );
		dayTextView = ( TextView ) view.findViewById ( R.id.dayTextView );
		yearTextView = ( TextView ) view.findViewById ( R.id.yearTextView );
		
		fromTimeTextView = (TextView) view.findViewById ( R.id.fromTimeTextView);
		toTimeTextView = (TextView) view.findViewById ( R.id.toTimeTextView);
		
		whereAtEditText = (AutoCompleteTextView) view.findViewById ( R.id.whereAtEditText );
		searchButton = (Button) view.findViewById ( R.id.searchButton );
		
		ratingView = (RatingView) view.findViewById ( R.id.ratingView );
		ratingView.setCanClicable ( true );
		return view;
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		filterManager =  FilterManager.getInstance ();
		initViews ();
		setListeners ();
		updateViews ();
	}

	private OnTimeSetListener fromTimeCallBack = new OnTimeSetListener ()
	{
		public void onTimeSet ( TimePicker view, int hourOfDay, int minute )
		{
			fromTimeTextView.setText ( hourOfDay + ":" +  minute);
		}
	};
	
	private OnTimeSetListener toTimeCallBack = new OnTimeSetListener ()
	{
		public void onTimeSet ( TimePicker view, int hourOfDay, int minute )
		{
			toTimeTextView.setText ( hourOfDay + ":" +  minute);
		}
	};

	public abstract class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
	{

		@Override
		public Dialog onCreateDialog ( Bundle savedInstanceState )
		{
			final Calendar c = Calendar.getInstance ();
            if(selectedDate > 0){
                c.setTime(new Date(selectedDate));
            }
			int year = c.get ( Calendar.YEAR );
			int month = c.get ( Calendar.MONTH );
			int day = c.get ( Calendar.DAY_OF_MONTH );
			return new DatePickerDialog ( getActivity (), this, year, month, day );
		}
	}
	
	private OnClickListener dateClickListener = new OnClickListener ()
	{
		@Override
		public void onClick ( View v )
		{
			DatePickerFragment picker = new DatePickerFragment ()
			{
				@Override
				public void onDateSet ( DatePicker view, int year, int month, int day )
				{
                    Calendar calendar = Calendar.getInstance ();
					calendar.set ( year, month, day );
					selectedDate = calendar.getTimeInMillis ();
					monthTextView.setText ( "" + ( month) );
					dayTextView.setText ( "" + day );
					yearTextView.setText ( "" + ( year ) );
				}
			};
			picker.show ( getFragmentManager (), "datePicker" );
		}
	};

	private void initViews ()
	{
		float scale = getResources ().getDisplayMetrics ().density;

		tallRangebar.setThumbImageNormal ( R.drawable.thumb );
		tallRangebar.setThumbImagePressed ( R.drawable.thumb_bar );
        Log.d("Filter", filterManager.getHeightTo() + " " + filterManager.getHeightFrom());
		tallRangebar.setMaxMin(108, 48);
		tallRangebar.setTickHeight ( 0 );
		tallRangebar.setConnectingLineColor ( Color.parseColor ( "#C52432" ) );
		tallRangebar.setBarColor ( Color.parseColor ( "#AEAEAE" ) );
		tallRangebar.setBarWeight ( ( int ) ( 7 * scale ) );
		tallRangebar.setConnectingLineWeight ( 7 );
		tallRangebar.setThumbIndices ( filterManager.getHeightTo(),  filterManager.getHeightFrom() );

		weightRangebar.setThumbImageNormal ( R.drawable.thumb );
		weightRangebar.setThumbImagePressed ( R.drawable.thumb_bar );
		weightRangebar.setMaxMin ( 300, 90 );
		weightRangebar.setTickHeight ( 0 );
		weightRangebar.setConnectingLineColor ( Color.parseColor ( "#C52432" ) );
		weightRangebar.setBarColor ( Color.parseColor ( "#AEAEAE" ) );
		weightRangebar.setBarWeight ( ( int ) ( 7 * scale ) );
		weightRangebar.setConnectingLineWeight ( 7 );
		weightRangebar.setThumbIndices ( filterManager.getWeightTo(),  filterManager.getWeightFrom() );

		priceRangebar.setThumbImageNormal ( R.drawable.thumb );
		priceRangebar.setThumbImagePressed ( R.drawable.thumb_bar );
		priceRangebar.setMaxMin ( 150, 0 );
		priceRangebar.setTickHeight ( 0 );
		priceRangebar.setConnectingLineColor ( Color.parseColor ( "#C52432" ) );
		priceRangebar.setBarColor ( Color.parseColor ( "#AEAEAE" ) );
		priceRangebar.setBarWeight ( ( int ) ( 7 * scale ) );
		priceRangebar.setConnectingLineWeight ( 7 );
		priceRangebar.setThumbIndices ( filterManager.getPriceTo(), filterManager.getPriceFrom() );

		NumericWheelAdapter minAgeAdapter = new NumericWheelAdapter ( controller, MIN_AGE, MAX_AGE );
		minAgeAdapter.setItemResource ( R.layout.wheel_adapter_item );
		minAgeAdapter.setItemTextResource ( R.id.ageTextItem );
		minAgeSelector.setViewAdapter ( minAgeAdapter );
		minAgeSelector.setCurrentItem ( filterManager.getAgeFrom() - MIN_AGE );
		minAgeSelector.setCyclic ( true );

		NumericWheelAdapter maxAgeAdapter = new NumericWheelAdapter ( controller, MIN_AGE, MAX_AGE );
		maxAgeAdapter.setItemResource ( R.layout.wheel_adapter_item );
		maxAgeAdapter.setItemTextResource ( R.id.ageTextItem );
		maxAgeSelector.setViewAdapter ( maxAgeAdapter );
		maxAgeSelector.setCurrentItem ( filterManager.getAgeTo() - MIN_AGE);
		maxAgeSelector.setCyclic ( true );

		ethentitySpinner.setItems ( Arrays.asList ( getResources ().getStringArray ( R.array.ethentity ) ), "", "", R.layout.spinner_item_white );
        if(!filterManager.getEthnicity().isEmpty()) {
            ethentitySpinner.setSelection(filterManager.getEthnicity());
        }

		bodyTypeSpinner.setItems ( Arrays.asList ( getResources ().getStringArray ( R.array.body_types ) ), "", "", R.layout.spinner_item_white );
        if(!filterManager.getBodyType().isEmpty()){
            bodyTypeSpinner.setSelection(filterManager.getBodyType());
        }

        sexSpinner.setItems ( Arrays.asList ( getResources ().getStringArray ( R.array.sexuality ) ), "", "", R.layout.spinner_item_white );
        if(!filterManager.getSex().isEmpty()){
            sexSpinner.setSelection(filterManager.getSex());
        }
		
		fromTimePickerDialog = new TimePickerDialog ( controller, fromTimeCallBack, START_HOUR, START_HOUR, true );
		toTimePickerDialog = new TimePickerDialog ( controller, toTimeCallBack, START_HOUR, START_MINUTE, true );

        Address address = filterManager.getAddress();
        if(address != null){
            whereAtEditText.setAdapter ( new AddressAutocompleteAdapter ( getActivity (), Tools.getLocationText(address.getLatitude(), address.getLongitude())));
            whereAtEditText.setText( Tools.getLocationText(address.getLatitude(), address.getLongitude()));
        }else{
            whereAtEditText.setAdapter ( new AddressAutocompleteAdapter ( getActivity (), whereAtEditText.getText ().toString () ) );
        }

        ratingView.setRating(filterManager.getRating());
        fromTimeTextView.setText(filterManager.getTimeFrom());
        toTimeTextView.setText(filterManager.getTimeTo());
        selectedDate = filterManager.getDate();

        final Calendar c = Calendar.getInstance ();
        if(selectedDate > 0){
            c.setTime(new Date(selectedDate));

            int year = c.get ( Calendar.YEAR );
            int month = c.get ( Calendar.MONTH );
            int day = c.get ( Calendar.DAY_OF_MONTH );

            monthTextView.setText ( "" + ( month ) );
            dayTextView.setText ( "" + day );
            yearTextView.setText ( "" + ( year ) );
        }


	}

	private void updateViews ()
	{
		// FontUtils.getInstance ( getActivity () ).applyStyle ( titleAvailable,
		// FontStyle.BOLD );
	}
	
	

	private void setListeners ()
	{
		whereAtEditText.setOnItemClickListener ( new OnItemClickListener ()
		{
			public void onItemClick ( AdapterView < ? > adapterView, View view, int position, long l )
			{
				AutocompleteAdapter adapter = ( AutocompleteAdapter ) adapterView.getAdapter ();
				AddressWraper addressWraper = ( AddressWraper ) adapter.getValue ( position );
				filterManager.setAddress(addressWraper.getAddress());
			}
		} );
		
		crossImageView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
                filterManager.clearFilter();
				controller.onBackPressed ();
			}
		} );
		
		toTimeTextView.setOnClickListener ( new OnClickListener()
		{
			
			@Override
			public void onClick ( View v )
			{
				toTimePickerDialog.show ();
			}
		} );
		
		fromTimeTextView.setOnClickListener ( new OnClickListener()
		{
			
			@Override
			public void onClick ( View v )
			{
				fromTimePickerDialog.show ();
			}
		} );
		
		monthTextView.setOnClickListener ( dateClickListener );
		dayTextView.setOnClickListener ( dateClickListener );
		yearTextView.setOnClickListener ( dateClickListener );
		
		searchButton.setOnClickListener ( new OnClickListener()
		{
			
			@Override
			public void onClick ( View v )
			{
				filterManager.setHeight (  tallRangebar.getLeftValue (), tallRangebar.getRightValue ());

                if((minAgeSelector.getCurrentItem() + MIN_AGE) < maxAgeSelector.getCurrentItem() + MIN_AGE){
					filterManager.setAge (minAgeSelector.getCurrentItem() + MIN_AGE, maxAgeSelector.getCurrentItem() + MIN_AGE );
				}else{
					filterManager.setAge (maxAgeSelector.getCurrentItem() + MIN_AGE, minAgeSelector.getCurrentItem() + MIN_AGE );
				}
				
				filterManager.setEthnicity ( ethentitySpinner.getSelectedItems () );
				filterManager.setWeight ( weightRangebar.getLeftValue(), weightRangebar.getRightValue() );
				filterManager.setBodyType ( bodyTypeSpinner.getSelectedItems () );
				filterManager.setPrice ( priceRangebar.getLeftValue (), priceRangebar.getRightValue());
				filterManager.setSex ( sexSpinner.getSelectedItems () );

                filterManager.setRating(ratingView.getRating());
                if(selectedDate > 0){
                    filterManager.setDate(selectedDate);
                }

                filterManager.setTimeFrom(fromTimeTextView.getText().toString());
                filterManager.setTimeTo(toTimeTextView.getText().toString());

                filterManager.setIsSearchByFilter ( true );
				controller.setState ( VIEW_STATE.FEED );
			}
		} );
	}
}
