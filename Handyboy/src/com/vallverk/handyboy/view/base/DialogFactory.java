package com.vallverk.handyboy.view.base;

import android.app.AlertDialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public class DialogFactory
{
	public static Dialog show ( String message, Context context )
	{
		final ProgressDialog dialog = new ProgressDialog ( context );
		dialog.setMessage ( message );
		dialog.setIndeterminate ( true );
		dialog.setCancelable ( false );
		dialog.show ();
		return dialog;
	}

	public static AlertDialog alertDialog ( String title, String message, Context context, AlertButton positiveButton, AlertButton negativeButton, View view )
	{
		AlertDialog.Builder builder = new AlertDialog.Builder ( context ).setIcon ( android.R.drawable.ic_dialog_alert ).setTitle ( title ).setMessage ( message );
		if ( positiveButton != null )
		{
			builder.setPositiveButton ( positiveButton.text, positiveButton.listener );
		}
		if ( negativeButton != null )
		{
			builder.setNegativeButton ( negativeButton.text, negativeButton.listener );
		}
		if ( view != null )
		{
			builder.setView ( view );
		}
		return builder.show ();
	}

	static class AlertButton
	{
		protected String text;
		protected DialogInterface.OnClickListener listener;

		public AlertButton ( String text, DialogInterface.OnClickListener listener )
		{
			this.text = text;
			this.listener = listener;
		}
	}
}
