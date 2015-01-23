package com.vallverk.handyboy.view.account;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.view.base.AnimatedExpandableListView;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.AnimatedExpandableListView.AnimatedExpandableListAdapter;

public class TransactionHistoryViewFragment extends BaseFragment
{
	private ImageView backImageView;
	private TextView backTextView;
	
	private AnimatedExpandableListView transactionExpandableListView;

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.transaction_history_layout, null );
		backImageView = ( ImageView ) view.findViewById ( R.id.backImageView );
		backTextView = ( TextView ) view.findViewById ( R.id.backTextView );
		transactionExpandableListView = ( AnimatedExpandableListView ) view.findViewById ( R.id.transactionExpandableListView );
		return view;
	}

	@Override
	protected void init ()
	{

	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		updateComponents ();
		addListeners ();
		updateFonts ();
	}

	@SuppressLint("NewApi")
	private void updateComponents ()
	{
		TransactionHistoryAdapter transactionHistoryAdapter = new TransactionHistoryAdapter ( controller );
		List < GroupItem > groupItems = new ArrayList < TransactionHistoryViewFragment.GroupItem > ();
		
		GroupItem groupItem;
		
		for ( int i = 0; i < 100; i++ )
		{
			groupItem = new GroupItem();
			groupItem.items.add ( new ChildItem () );
			groupItems.add ( groupItem );
		}
		
		transactionHistoryAdapter.setData ( groupItems );
		
		if ( android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2 )
		{
			transactionExpandableListView.setIndicatorBounds ( MainActivity.SCREEN_WIDTH - Tools.fromDPToPX ( 30, controller ), MainActivity.SCREEN_WIDTH );

		} else
		{
			transactionExpandableListView.setIndicatorBoundsRelative ( MainActivity.SCREEN_WIDTH - Tools.fromDPToPX ( 30, controller ), MainActivity.SCREEN_WIDTH );
		}

		transactionExpandableListView.setAdapter ( transactionHistoryAdapter );
	}

	protected void addListeners ()
	{
		backTextView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				controller.onBackPressed ();
			}
		} );

		backImageView.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
				controller.onBackPressed ();
			}
		} );

		
		transactionExpandableListView.setOnGroupClickListener ( new OnGroupClickListener ()
		{

			@Override
			public boolean onGroupClick ( ExpandableListView parent, View v, int groupPosition, long id )
			{
				if ( transactionExpandableListView.isGroupExpanded ( groupPosition ) )
				{
					transactionExpandableListView.collapseGroupWithAnimation ( groupPosition );
				} else
				{
					transactionExpandableListView.expandGroupWithAnimation ( groupPosition );
				}

				return true;
			}

		} );
	}

	@Override
	protected void updateFonts ()
	{
	}

	private static class GroupItem
	{
		private String userName;
		private String gigId;
		private String nameSession;
		private String price;
		private String totalPrice;
		List < ChildItem > items = new ArrayList < ChildItem > ();
	}

	private static class ChildItem
	{
		String title;
		String id;
	}

	private static class ChildHolder
	{
		TextView title;
	}

	private static class GroupHolder
	{
		TextView title;
	}

	private class TransactionHistoryAdapter extends AnimatedExpandableListAdapter
	{
		private LayoutInflater inflater;

		private List < GroupItem > items;

		public TransactionHistoryAdapter ( Context context )
		{
			inflater = LayoutInflater.from ( context );
		}

		public void setData ( List < GroupItem > items )
		{
			this.items = items;
		}

		@Override
		public ChildItem getChild ( int groupPosition, int childPosition )
		{
			return items.get ( groupPosition ).items.get ( childPosition );
		}

		@Override
		public long getChildId ( int groupPosition, int childPosition )
		{
			return childPosition;
		}

		@Override
		public View getRealChildView ( int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent )
		{
			ChildHolder holder;
			ChildItem item = getChild ( groupPosition, childPosition );
			if ( convertView == null )
			{
				holder = new ChildHolder ();
				convertView = inflater.inflate ( R.layout.transaction_child_item, parent, false );
				// holder.title = ( TextView ) convertView.findViewById (
				// R.id.expandable_group_item );
				// holder.hint = ( TextView ) convertView.findViewById (
				// R.id.textHint );
				convertView.setTag ( holder );
			} else
			{
				holder = ( ChildHolder ) convertView.getTag ();
			}

			// holder.title.setText ( item.title );
			// holder.hint.setText ( item.hint );

			return convertView;
		}

		@Override
		public int getRealChildrenCount ( int groupPosition )
		{
			return items.get ( groupPosition ).items.size ();
		}

		@Override
		public GroupItem getGroup ( int groupPosition )
		{
			return items.get ( groupPosition );
		}

		@Override
		public int getGroupCount ()
		{
			return items.size ();
		}

		@Override
		public long getGroupId ( int groupPosition )
		{
			return groupPosition;
		}

		@Override
		public View getGroupView ( int groupPosition, boolean isExpanded, View convertView, ViewGroup parent )
		{
			GroupHolder holder;
			GroupItem item = getGroup ( groupPosition );
			if ( convertView == null )
			{
				holder = new GroupHolder ();
				convertView = inflater.inflate ( R.layout.transaction_group_item, parent, false );
				holder.title = ( TextView ) convertView.findViewById ( R.id.expandable_category_item );
				convertView.setTag ( holder );
			} else
			{
				holder = ( GroupHolder ) convertView.getTag ();
			}

			return convertView;
		}

		@Override
		public boolean hasStableIds ()
		{
			return true;
		}

		@Override
		public boolean isChildSelectable ( int arg0, int arg1 )
		{
			return true;
		}

	}

}