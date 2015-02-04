package com.vallverk.handyboy.model.schedule;

import android.location.Address;

import org.json.JSONArray;
import org.json.JSONObject;

public class ScheduleForDay
{
	protected int[][] items;
	protected int selectedItem;
	protected int defaultItem;
	private boolean dayOff;
	private Address address;

	public ScheduleForDay ()
	{
		this ( new int[10][5] );
	}

	public ScheduleForDay ( int[][] items )
	{
		this.items = items;
		this.selectedItem = getSelectedItem ();
		this.defaultItem = getDefaultItem ();
	}

	public Address getAddress ()
	{
		return address;
	}

	public void setAddress ( Address address )
	{
		this.address = address;
	}

	protected int getDefaultItem ()
	{
		return 0;
	}

	protected int getSelectedItem ()
	{
		return 1;
	}

	public int[][] getItems ()
	{
		return items;
	}

	public void setItems ( int[][] items )
	{
		this.items = items;
	}

	public int[][] getBridges ()
	{
		int[][] bridges = new int[10][4];
		for ( int i = 0; i < items.length; i++ )
		{
			for ( int j = 0; j < items[i].length - 1; j++ )
			{
				if ( items[i][j] == selectedItem && items[i][j + 1] == selectedItem )
				{
					bridges[i][j] = 1;
				}
			}
		}
		return bridges;
	}

	public void setSelected ( int row, int col, boolean isSelected )
	{
		items[row][col] = isSelected ? selectedItem : defaultItem;
	}

	public boolean isSelected ( int row, int col )
	{
		return items[row][col] == selectedItem;
	}

	public void removeWindow ( int row, int col )
	{
		removeRightPartOfWorkWindow ( row, col );
		items[row][col] = selectedItem;// for succes removing left park of work
										// window
		removeLeftPartOfWorkWindow ( row, col );
	}

	private void removeLeftPartOfWorkWindow ( int row, int col )
	{
		for ( int i = row; i >= 0; i-- )
		{
			for ( int j = i == row ? col : items[i].length - 1; j >= 0; j-- )
			{
				if ( items[i][j] != selectedItem )
				{
					return;
				}
				items[i][j] = defaultItem;
			}
		}
	}

	private void removeRightPartOfWorkWindow ( int row, int col )
	{
		for ( int i = row; i < items.length; i++ )
		{
			for ( int j = i == row ? col : 0; j < items[i].length; j++ )
			{
				if ( items[i][j] != selectedItem )
				{
					return;
				}
				items[i][j] = defaultItem;
			}
		}
	}

	public JSONArray createTimeJSONArray () throws Exception
	{
		JSONArray jsonArray = new JSONArray ();
		WorkWindowPoint startPoint = null;
		WorkWindowPoint endPoint = null;
		for ( int i = 0; i < items.length; i++ )
		{
			for ( int j = 0; j < items[i].length; j++ )
			{
				if ( items[i][j] == 1 )
				{
					if ( startPoint == null )
					{
						startPoint = new WorkWindowPoint ( i, j );
					} else
					{
						endPoint = new WorkWindowPoint ( i, j );
					}
				} else
				{
					if ( startPoint != null && endPoint != null )
					{
						WorkWindow workWindow = new WorkWindow ( startPoint, endPoint );
						jsonArray.put ( workWindow.createJSONObject () );
					}
					startPoint = endPoint = null;
				}
			}
		}
		return jsonArray;
	}

	public void updateFromServer ( JSONArray jsonArray ) throws Exception
	{
		for ( int i = 0; i < jsonArray.length (); i++ )
		{
			JSONObject jsonObject = jsonArray.getJSONObject ( i );
			WorkWindow workWindow = new WorkWindow ( jsonObject );
			applyWorkWindow ( workWindow );
		}
	}

	protected void applyWorkWindow ( WorkWindow workWindow )
	{
		int sr = workWindow.startPoint.row;
		int er = workWindow.endPoint.row;
		int sc = workWindow.startPoint.col;
		int ec = workWindow.endPoint.col;
		for ( int i = sr; i <= er; i++ )
		{
			for ( int j = ( i == sr ? sc : 0 ); j <= ( i == er ? ec : items[i].length - 1 ); j++ )
			{
				items[i][j] = selectedItem;
			}
		}
	}

	protected int[] getItemsAsArray ()
	{
		int[] array = new int[items.length * items[0].length];
		int index = 0;
		for ( int i = 0; i < items.length; i++ )
		{
			for ( int j = 0; j < items[i].length; j++ )
			{
				array[index++] = items[i][j];
			}
		}
		return array;
	}

	public boolean isDayOff ()
	{
		return dayOff;
	}

	public void setDayOff ( boolean dayOff )
	{
		this.dayOff = dayOff;
	}
}
