package com.vallverk.handyboy.view.base;

import com.vallverk.handyboy.R;

import android.content.Context;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FlipperStatusView extends LinearLayout
{
	private ImageView[] items;
	private int activeItems;
	
	public FlipperStatusView ( Context context )
	{
		super ( context );
		// TODO Auto-generated constructor stub
	}

	public FlipperStatusView ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );

		LayoutInflater inflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
		if ( inflater != null )
		{
			inflater.inflate ( R.layout.flipper_status_layout, this );
		}
		if ( !isInEditMode () )
		{
			init ( 7 );
		}
	}

	public void init ( int maxItems )
	{
		items = new ImageView[maxItems];
		items[0] = ( ImageView ) findViewById ( R.id.status1 );
		items[1] = ( ImageView ) findViewById ( R.id.status2 );
		items[2] = ( ImageView ) findViewById ( R.id.status3 );
		items[3] = ( ImageView ) findViewById ( R.id.status4 );
		items[4] = ( ImageView ) findViewById ( R.id.status5 );
		items[5] = ( ImageView ) findViewById ( R.id.status6 );
		items[6] = ( ImageView ) findViewById ( R.id.status7 );
		offAll ();
	}

	private void offAll ()
	{
		for ( int i = 0; i < items.length; i++ )
		{
			items[i].setVisibility ( View.GONE );
		}
		activeItems = 0;
	}

	public FlipperStatusView ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
	}

	public void on ( int amount )
	{
		offAll ();
		for ( int i = 0; i < amount; i++ )
		{
			items[i].setVisibility ( View.VISIBLE );
		}
		activeItems = amount;
	}

	public int select ( int selectItem )
	{
		if ( selectItem == activeItems )
		{
			selectItem = 0;
		}
		if ( selectItem == -1 )
		{
			selectItem = activeItems - 1;
		}
		for ( int i = 0; i < items.length; i++ )
		{
			items[i].setImageResource ( i == selectItem ? R.drawable.point_wth_red : R.drawable.flipper_status_item_na );
		}
		return selectItem;
	}
}
