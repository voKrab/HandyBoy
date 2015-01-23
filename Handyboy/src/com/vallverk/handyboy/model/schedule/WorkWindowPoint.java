package com.vallverk.handyboy.model.schedule;

public class WorkWindowPoint
{
	protected int row;
	protected int col;
	
	public WorkWindowPoint ( int row, int col )
	{
		this.row = row;
		this.col = col;
	}
	
	public WorkWindowPoint ( String data )
	{
		int[] rowCol = DayTimes.getRowCol ( data );
		row = rowCol[0];
		col = rowCol[1];
	}

	public String toString ()
	{
		return DayTimes.getTime ( row, col );
	}
}
