package com.vallverk.handyboy.view.schedule;

import com.vallverk.handyboy.R;

import android.view.View;
import android.widget.ImageView;

public class SelectedItem
{
	protected int row;
	protected int col;
	protected View view;
	
	public SelectedItem ( int row, int col, View view )
	{
		this.row = row;
		this.col = col;
		this.view = view;
	}

	public void select ()
	{
		view.setBackgroundResource ( R.color.red );
	}

	public int length ( int rowLength )
	{
		return row * rowLength + col;
	}

	public boolean isEquals ( int row, int col )
	{
		
		return this.row == row && this.col == col;
	}
}
