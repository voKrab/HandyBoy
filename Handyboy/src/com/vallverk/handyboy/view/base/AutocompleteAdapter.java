package com.vallverk.handyboy.view.base;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Filter;

public abstract class AutocompleteAdapter extends ArrayAdapter < String >
{
	protected static int MAX_RESULTS = 10;
	
	protected static final String TAG = "SuggestionAdapter";
	private List < Object > data;

	public AutocompleteAdapter ( Activity context, String nameFilter )
	{
		super ( context, android.R.layout.simple_dropdown_item_1line );
		data = new ArrayList < Object > ();
	}

	@Override
	public int getCount ()
	{
		return data.size ();
	}

	@Override
	public String getItem ( int index )
	{
		return data.get ( index ).toString ();
	}

	@Override
	public Filter getFilter ()
	{
		Filter myFilter = new Filter ()
		{
			@Override
			protected FilterResults performFiltering ( CharSequence constraint )
			{
				FilterResults filterResults = new FilterResults ();
				if ( constraint != null )
				{
					// A class that queries a web API, parses the data and
					// returns an ArrayList<GoEuroGetSet>
					List findedData = getData ( constraint.toString () );
					data.clear ();
					for ( int i = 0; i < findedData.size (); i++ )
					{
						Object item = findedData.get ( i );
						data.add ( item );
					}

					// Now assign the values and count to the FilterResults
					// object
					filterResults.values = data;
					filterResults.count = data.size ();
				}
				return filterResults;
			}

			@Override
			protected void publishResults ( CharSequence contraint, FilterResults results )
			{
				if ( results != null && results.count > 0 )
				{
					notifyDataSetChanged ();
				} else
				{
					notifyDataSetInvalidated ();
				}
			}
		};
		return myFilter;
	}

	protected abstract List getData ( String constraint );
	
	public Object getValue ( int position )
	{
		return data.get ( position );
	}
}
