package com.vallverk.handyboy.view.base;

import java.util.ArrayList;
import java.util.List;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MultiChoiceSpinner extends Spinner implements OnMultiChoiceClickListener, OnCancelListener
{
	private List < String > items;
	private boolean[] selected;
	private String fullText;
	private MultiSpinnerListener listener;
	private String emptyText;
	private String spinnerText;
	private String fullSelectedText;

	private int layout;

	public MultiChoiceSpinner ( Context context )
	{
		super ( context );
	}

	public MultiChoiceSpinner ( Context context, AttributeSet attributeSet )
	{
		super ( context, attributeSet );
	}

	public MultiChoiceSpinner ( Context context, AttributeSet attributeSet, int i )
	{
		super ( context, attributeSet, i );
	}

	@Override
	public void onClick ( DialogInterface dialog, int which, boolean isChecked )
	{
		if ( isChecked )
			selected[which] = true;
		else
			selected[which] = false;
	}

	@Override
	public void onCancel ( DialogInterface dialog )
	{
		updateSelection ();
	}

	private void updateSelection ()
	{
		// refresh text on spinner
		StringBuffer spinnerBuffer = new StringBuffer ();
		int amountSelected = 0;
		for ( int i = 0; i < items.size (); i++ )
		{
			if ( selected[i] == true )
			{
				spinnerBuffer.append ( items.get ( i ) );
				spinnerBuffer.append ( ", " );
				amountSelected++;
			}
		}
		spinnerText = "";
		fullSelectedText = createFullSelectedText ( spinnerBuffer.toString () );
		
		if(amountSelected == 0){
			spinnerText = emptyText;
		}else if(amountSelected == items.size ()){
			if (!fullText.isEmpty () ){
				spinnerText = fullText;
			}else{
				spinnerText = fullSelectedText;
			}
		}else{
			spinnerText = fullSelectedText;
		}
		
		if ( layout == 0 )
		{
			ArrayAdapter < String > adapter = new ArrayAdapter < String > ( getContext (), android.R.layout.simple_spinner_item, new String[] { spinnerText } );
			setAdapter ( adapter );
		} else
		{
			ArrayAdapter < String > adapter = new ArrayAdapter < String > ( getContext (), R.layout.spinner_item_white, new String[] { spinnerText } );
			setAdapter ( adapter );
		}

		if ( listener != null )
		{
			listener.onItemsSelected ( selected );
		}
	}

	private String createFullSelectedText ( String string )
	{
		if ( string.length () > 2 )
			string = string.substring ( 0, string.length () - 2 );
		return string;
	}

	@Override
	public boolean performClick ()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder ( getContext () );
		builder.setMultiChoiceItems ( items.toArray ( new CharSequence[items.size ()] ), selected, this );
		builder.setPositiveButton ( android.R.string.ok, new DialogInterface.OnClickListener ()
		{
			@Override
			public void onClick ( DialogInterface dialog, int which )
			{
				// setBackgroundResource ( R.drawable.field_na );
				dialog.cancel ();
			}
		} );
		builder.setOnCancelListener ( this );
		AlertDialog alertDialog = builder.create ();
		alertDialog.show ();
		int width = ( int ) ( MainActivity.SCREEN_WIDTH * 0.85 );
		int height = ( int ) ( MainActivity.SCREEN_HEIGHT * 0.5 );
		alertDialog.getWindow ().setLayout ( width, height );
		// setBackgroundResource ( R.drawable.field );
		return true;
	}

	public void setAdapterItem ( int layout )
	{
		ArrayAdapter < String > adapter = new ArrayAdapter < String > ( getContext (), layout, new String[] { emptyText } );
		setAdapter ( adapter );
	}

	public void setItems ( List < String > items, String emptyText, String fullText )
	{
		setItems ( items, emptyText, fullText, null );
		setAdapterItem ( android.R.layout.simple_spinner_item );
	}

	public void setItems ( List < String > items, String emptyText, String fullText, int layout )
	{
		setItems ( items, emptyText, fullText, null );
		this.layout = layout;
		setAdapterItem ( layout );
	}

	public void setItems ( List < String > items, String emptyText, String fullText, MultiSpinnerListener listener )
	{
		this.items = items;
		this.emptyText = emptyText;
		this.spinnerText = new String ( emptyText );
		this.fullText = fullText;
		this.listener = listener;
		this.fullSelectedText = "";

		selected = new boolean[items.size ()];

		// setBackgroundResource ( R.drawable.field_na );
	}

	public interface MultiSpinnerListener
	{
		public void onItemsSelected ( boolean[] selected );
	}

	public boolean isEmpty ()
	{
		return spinnerText.equals ( emptyText );
	}

	public String getSelectedItems ()
	{
		return fullSelectedText;
	}

	@Override
	public void setSelection ( int position )
	{
		selected[position] = true;
		updateSelection ();
	}

	public void setSelection ( String data )
	{
		if ( data == null )
		{
			return;
		}
		String[] values = data.split ( "," );
		for ( String value : values )
		{
			value = value.trim ();
			int index = items.indexOf ( value );
			selected[index] = true;
		}
		updateSelection ();
	}
	
	public List < Integer > getSelectedIndexes ()
	{
		List < Integer > selectedIndexes = new ArrayList < Integer > (); 
		if ( fullSelectedText == null )
		{
			return selectedIndexes;
		}
		String[] values = fullSelectedText.split ( "," );
		for ( String value : values )
		{
			value = value.trim ();
			int index = items.indexOf ( value );
			if ( index >= 0 )
			{
				selectedIndexes.add ( index );
			}
		}
		return selectedIndexes;
	}
}