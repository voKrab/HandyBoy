package com.vallverk.handyboy.view.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.vallverk.handyboy.R;

public class SingleChoiceSpinner extends Spinner
{
	public SingleChoiceSpinner ( Context context )
	{
		super ( context );
		init ( context );
	}

	public SingleChoiceSpinner ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		init ( context );
	}

	public SingleChoiceSpinner ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
		init ( context );
	}

	public SingleChoiceSpinner ( Context context, AttributeSet attrs, int defStyle, int mode )
	{
		super ( context, attrs, defStyle, mode );
		init ( context );
	}

	public SingleChoiceSpinner ( Context context, int mode )
	{
		super ( context, mode );
		init ( context );
	}

	private void init ( Context context )
	{
		final CharSequence[] items = { "" };
		init ( context, items );
	}

	private void init ( Context context, Object[] data )
	{
		ListeningArrayAdapter adapter = new ListeningArrayAdapter < Object > ( context, android.R.layout.simple_spinner_item, data );
		adapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
		adapter.addSpinnerListener ( new SpinnerListener ()
		{
			@Override
			public void onSpinnerExpanded ()
			{
				setBackgroundResource ( R.drawable.field );
			}

			@Override
			public void onSpinnerCollapsed ()
			{
				setBackgroundResource ( R.drawable.field_na );
			}
		} ); // Add your own spinner listener implementation here.
		setAdapter ( adapter );

		// isOpened = false;
		setBackgroundResource ( R.drawable.field_na );
	}

	public interface OnSpinnerEventsListener
	{
		void onSpinnerOpened ();

		void onSpinnerClosed ();
	}

	public class ListeningArrayAdapter < T > extends ArrayAdapter < T >
	{
		private ViewGroup itemParent;
		private final Collection < SpinnerListener > spinnerListeners = new ArrayList < SpinnerListener > ();
		private T[] objects;
		
		public ListeningArrayAdapter ( Context context, int resource, T[] objects )
		{
			super ( context, resource, objects );
			
			this.objects = objects;
		}

		public T[] getItems ()
		{
			return objects;
		}
		
		// Add the rest of the constructors here ...

		// Just grab the spinner view (parent of the spinner item view) and add
		// a listener to it.
		@Override
		public View getDropDownView ( int position, View convertView, ViewGroup parent )
		{
			if ( isParentTheListView ( parent ) )
			{
				itemParent = parent;
				addFocusListenerAsExpansionListener ();
			}

			return super.getDropDownView ( position, convertView, parent );
		}

		// Assumes the item view parent is a ListView (which it is when a
		// Spinner class is used)
		private boolean isParentTheListView ( ViewGroup parent )
		{
			return ( parent != itemParent && parent != null && ListView.class.isAssignableFrom ( parent.getClass () ) );
		}

		// Add a focus listener to listen to spinner expansion and collapse
		// events.
		private void addFocusListenerAsExpansionListener ()
		{
			final View.OnFocusChangeListener listenerWrapper = new OnFocusChangeListenerWrapper ( itemParent.getOnFocusChangeListener (), spinnerListeners );
			itemParent.setOnFocusChangeListener ( listenerWrapper );
		}

		// Utility method.
		public boolean isExpanded ()
		{
			return ( itemParent != null && itemParent.hasFocus () );
		}

		public void addSpinnerListener ( SpinnerListener spinnerListener )
		{
			spinnerListeners.add ( spinnerListener );
		}

		public boolean removeSpinnerListener ( SpinnerListener spinnerListener )
		{
			return spinnerListeners.remove ( spinnerListener );
		}

		// Listener that listens for 'expand' and 'collapse' events.
		private class OnFocusChangeListenerWrapper implements View.OnFocusChangeListener
		{
			private final Collection < SpinnerListener > spinnerListeners;
			private final View.OnFocusChangeListener originalFocusListener;

			private OnFocusChangeListenerWrapper ( View.OnFocusChangeListener originalFocusListener, Collection < SpinnerListener > spinnerListeners )
			{
				this.spinnerListeners = spinnerListeners;
				this.originalFocusListener = originalFocusListener;
			}

			@Override
			public void onFocusChange ( View view, boolean hasFocus )
			{
				if ( originalFocusListener != null )
				{
					originalFocusListener.onFocusChange ( view, hasFocus ); // Preserve
																			// the
																			// pre-existing
																			// focus
																			// listener
																			// (if
																			// any).
				}

				callSpinnerListeners ( hasFocus );
			}

			private void callSpinnerListeners ( boolean hasFocus )
			{
				for ( SpinnerListener spinnerListener : spinnerListeners )
				{
					if ( spinnerListener != null )
					{
						callSpinnerListener ( hasFocus, spinnerListener );
					}
				}
			}

			private void callSpinnerListener ( boolean hasFocus, SpinnerListener spinnerListener )
			{
				if ( hasFocus )
				{
					spinnerListener.onSpinnerExpanded ();
				} else
				{
					spinnerListener.onSpinnerCollapsed ();
				}
			}
		}
	}

	public interface SpinnerListener
	{
		void onSpinnerExpanded ();

		void onSpinnerCollapsed ();
	}

	public void setData ( Object[] data )
	{
		init ( getContext (), data );
	}

	public void setSelected ( Object value )
	{
		int index = Arrays.asList ( ( ( ListeningArrayAdapter ) getAdapter () ).getItems () ).indexOf ( value );
		setSelection ( index );
	}
}
