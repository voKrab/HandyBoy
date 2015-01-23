package com.vallverk.handyboy.model.schedule;

public class DayTimes
{
	public static String[][] times = createTimes ();

	private static String[][] createTimes ()
	{
		String[][] times = new String[10][5];
		for ( int i = 0; i < times.length; i++ )
		{
			for ( int j = 0; j < times[i].length; j++ )
			{
				int length = i * times[i].length + j;
				times[i][j] = "" + length / 2 + ":" + ( length % 2 == 0 ? "00" : "30" );
			}
		}
		return times;
	}

	public static String getTime ( int row, int col )
	{
		return times[row][col];
	}

	public static int[] getRowCol ( String data )
	{
		int[] rowCol = new int[2];
		for ( int i = 0; i < times.length; i++ )
		{
			for ( int j = 0; j < times[i].length; j++ )
			{
				if ( times[i][j].equals ( data ) )
				{
					rowCol[0] = i;
					rowCol[1] = j;
					return rowCol;
				}
			}
		}
		return null;
	}
}
