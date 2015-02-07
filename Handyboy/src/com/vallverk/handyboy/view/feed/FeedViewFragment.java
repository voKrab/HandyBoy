package com.vallverk.handyboy.view.feed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.MainActivity.ApplicationAction;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.CommunicationManager;
import com.vallverk.handyboy.model.FilterManager;
import com.vallverk.handyboy.model.FilterManager.SearchType;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.job.JobCategory;
import com.vallverk.handyboy.model.job.JobTypeManager;
import com.vallverk.handyboy.model.job.TypeJob;
import com.vallverk.handyboy.view.base.AnimatedExpandableListView;
import com.vallverk.handyboy.view.base.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.base.BaseGridFragment;
import com.vallverk.handyboy.view.base.FontUtils;
import com.vallverk.handyboy.view.base.FontUtils.FontStyle;
import com.vallverk.handyboy.view.base.Refresher;

import java.util.ArrayList;
import java.util.List;

public class FeedViewFragment extends BaseFragment
{
	private View view;
	private BaseGridFragment gridFragment;
	private ImageView menuImageView;

	private LinearLayout globalSearchLayout;
	private TextView globalSearchTextView;

	private LinearLayout byProximitySearchLayout;
	private TextView byProximitySearchTextView;

	private LinearLayout availableNowSearchLayout;
	private TextView availableNowSearchTextView;

	private View searchButton;
	private View searchDevider;

	private LinearLayout editTextSearchLayout;
	private LinearLayout typeSearchLayout;
	private TextView searchGroupNameTextView;
	private ImageView arrowInsearchImageView;
	private ImageView crossButton;

	private Button filterButton;
	private TextView searchTextView;
	private AnimatedExpandableListView boyTypeExpandableListView;
	private int previousGroup = -1;

	List < GroupItem > jobItems;

	private FilterManager filterManager;

	private CommunicationSearch communicationSearch;

	public static class CommunicationSearch
	{
		public String jobGroupName;
		public String jobGroupId;

		public String jobTypeName;
		public String jobTypeId;

		public SearchType sort;
	}

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.feed_layout, container, false );
			menuImageView = ( ImageView ) view.findViewById ( R.id.menuImageView );

			globalSearchLayout = ( LinearLayout ) view.findViewById ( R.id.globalSearchLayout );
			globalSearchTextView = ( TextView ) view.findViewById ( R.id.globalSearchTextView );

			byProximitySearchLayout = ( LinearLayout ) view.findViewById ( R.id.byProximitySearchLayout );
			byProximitySearchTextView = ( TextView ) view.findViewById ( R.id.byProximitySearchTextView );

			availableNowSearchLayout = ( LinearLayout ) view.findViewById ( R.id.availableNowSearchLayout );
			availableNowSearchTextView = ( TextView ) view.findViewById ( R.id.availableNowSearchTextView );

			editTextSearchLayout = ( LinearLayout ) view.findViewById ( R.id.editTextSearchLayout );
			typeSearchLayout = ( LinearLayout ) view.findViewById ( R.id.typeSearchLayout );
			searchGroupNameTextView = ( TextView ) view.findViewById ( R.id.searchGroupNameTextView );
			arrowInsearchImageView = ( ImageView ) view.findViewById ( R.id.arrowInsearchImageView );

			searchButton = view.findViewById ( R.id.searchButton );
			searchDevider = view.findViewById ( R.id.searchDevider );
			filterButton = ( Button ) view.findViewById ( R.id.filterButton );

			searchTextView = ( TextView ) view.findViewById ( R.id.searchTextView );
			crossButton = ( ImageView ) view.findViewById ( R.id.crossButton );

			boyTypeExpandableListView = ( AnimatedExpandableListView ) view.findViewById ( R.id.boyTypeExpandableListView );

		} else
		{
			( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}

	@Override
	protected void updateFonts ()
	{
		FontUtils.getInstance ( controller ).applyStyle ( globalSearchTextView, FontStyle.BOLD_ITALIC );
		FontUtils.getInstance ( controller ).applyStyle ( byProximitySearchTextView, FontStyle.BOLD_ITALIC );
		FontUtils.getInstance ( controller ).applyStyle ( availableNowSearchTextView, FontStyle.BOLD_ITALIC );
	}

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		communicationSearch = ( CommunicationSearch ) controller.getCommunicationValue ( CommunicationSearch.class.getSimpleName () );

		filterManager = FilterManager.getInstance ();
		if ( gridFragment == null )
		{
			gridFragment = ( BaseGridFragment ) getActivity ().getSupportFragmentManager ().findFragmentById ( R.id.baseGridViewFragment );
			gridFragment.setRefresher ( new Refresher ( 25 )
			{
				@Override
				public List < Object > refresh () throws Exception
				{
					Log.d ( "Feed", filterManager.getSearchUrl ( pageLimit, loadedItems ) );
					List handyboys = APIManager.getInstance ().loadList ( filterManager.getSearchUrl ( pageLimit, loadedItems ), UserAPIObject.class );
                    Log.d ( "Feed", "List size=" + handyboys.size() );
					return handyboys;
				}

				@Override
				public List < Object > loadMoreItems () throws Exception
				{
					Log.d ( "Feed", filterManager.getSearchUrl ( pageLimit, loadedItems ) );
					List handyboys = APIManager.getInstance ().loadList ( filterManager.getSearchUrl ( pageLimit, loadedItems ), UserAPIObject.class );
                    System.out.println ( "loadMoreItems: " + loadedItems + " " + handyboys.size () );
                    return handyboys;
				}
			} );
			CommunicationManager.getInstance ().addListener ( ApplicationAction.SEARCH, new Handler ()
			{
				@Override
				public void dispatchMessage ( Message message )
				{
					gridFragment.refreshData ();
				}
			} );
		}
		initViews ();
		addListeners ();
		updateFonts ();
        updateFilterButton();

		if ( communicationSearch != null )
		{
			activateSearchType ( communicationSearch.sort );
			if ( communicationSearch.jobTypeId != null && !communicationSearch.jobTypeId.isEmpty () && communicationSearch.jobTypeName != null && !communicationSearch.jobTypeName.isEmpty () )
			{
				searchByJobId ( communicationSearch.jobTypeId, communicationSearch.jobTypeName );
			}
		} else
		{
			activateSearchType ( filterManager.getSearchType () );
		}

	}

    private void updateFilterButton(){
        if(filterManager.getIsSearchByFilter()){
            filterButton.setActivated(true);
        }else{
            filterButton.setActivated(false);
        }
    }

	@SuppressLint("NewApi")
	private void initViews ()
	{
		arrowInsearchImageView.setActivated ( false );

		// set data to ExpandableList
		jobItems = new ArrayList < GroupItem > ();

		GroupItem groupItem = new GroupItem ();
		groupItem.title = JobCategory.HOUSE_BOY.toString ().toUpperCase ();
		groupItem.items = new ArrayList < FeedViewFragment.ChildItem > ();

		for ( TypeJob typeJob : JobTypeManager.getInstance ().getJobTypes ( JobCategory.HOUSE_BOY ) )
		{
			ChildItem childItem = new ChildItem ();
			childItem.title = typeJob.getName ().toUpperCase ();
			childItem.id = String.valueOf ( typeJob.getId () );
			groupItem.items.add ( childItem );
		}

		jobItems.add ( groupItem );

		groupItem = new GroupItem ();
		groupItem.title = JobCategory.MUSCLE_BOY.toString ().toUpperCase ();
		groupItem.items = new ArrayList < FeedViewFragment.ChildItem > ();

		for ( TypeJob typeJob : JobTypeManager.getInstance ().getJobTypes ( JobCategory.MUSCLE_BOY ) )
		{
			ChildItem childItem = new ChildItem ();
			childItem.title = typeJob.getName ().toUpperCase ();
			childItem.id = String.valueOf ( typeJob.getId () );
			groupItem.items.add ( childItem );
		}

		jobItems.add ( groupItem );

		groupItem = new GroupItem ();
		groupItem.title = JobCategory.PARTY_BOY.toString ().toUpperCase ();
		groupItem.items = new ArrayList < FeedViewFragment.ChildItem > ();

		for ( TypeJob typeJob : JobTypeManager.getInstance ().getJobTypes ( JobCategory.PARTY_BOY ) )
		{
			ChildItem childItem = new ChildItem ();
			childItem.title = typeJob.getName ().toUpperCase ();
			childItem.id = String.valueOf ( typeJob.getId () );
			groupItem.items.add ( childItem );
		}

		jobItems.add ( groupItem );

		if ( android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2 )
		{
			boyTypeExpandableListView.setIndicatorBounds ( MainActivity.SCREEN_WIDTH - Tools.fromDPToPX ( 30, controller ), MainActivity.SCREEN_WIDTH );

		} else
		{
			boyTypeExpandableListView.setIndicatorBoundsRelative ( MainActivity.SCREEN_WIDTH - Tools.fromDPToPX ( 30, controller ), MainActivity.SCREEN_WIDTH );
		}
		ExampleAdapter adapter = new ExampleAdapter ( controller );
		adapter.setData ( jobItems );
		boyTypeExpandableListView.setAdapter ( adapter );
	}

	private static class GroupItem
	{
		String title;
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

	private class ExampleAdapter extends AnimatedExpandableListAdapter
	{
		private LayoutInflater inflater;

		private List < GroupItem > items;

		public ExampleAdapter ( Context context )
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
				convertView = inflater.inflate ( R.layout.expandable_group_item, parent, false );
				holder.title = ( TextView ) convertView.findViewById ( R.id.expandable_group_item );
				// holder.hint = ( TextView ) convertView.findViewById (
				// R.id.textHint );
				convertView.setTag ( holder );
			} else
			{
				holder = ( ChildHolder ) convertView.getTag ();
			}

			holder.title.setText ( item.title );
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
				convertView = inflater.inflate ( R.layout.expandable_category_item, parent, false );
				holder.title = ( TextView ) convertView.findViewById ( R.id.expandable_category_item );
				convertView.setTag ( holder );
			} else
			{
				holder = ( GroupHolder ) convertView.getTag ();
			}

			holder.title.setText ( item.title );

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

	private void searchByJobId ( String typeJobId, String typeJobName )
	{
		searchGroupNameTextView.setText ( typeJobName );
		filterManager.setJobId ( typeJobId );
		filterManager.setJobName ( typeJobName );
		filterManager.setIsSearchByFilter ( false );
		gridFragment.refreshData ();
	}

	private void activateSearchType ( SearchType searchType )
	{

		switch ( searchType )
		{
			case GLOBAL:
				globalSearchLayout.performClick ();
				break;

			case AVAILABLE_NOW:
				availableNowSearchLayout.performClick ();
				break;

			case PROXIMITY:
				byProximitySearchLayout.performClick ();
				break;

			default:
				break;
		}
	}

	private OnClickListener onSearchTypeClickListener = new OnClickListener ()
	{

		@Override
		public void onClick ( View view )
		{
			switch ( view.getId () )
			{
				case R.id.globalSearchLayout:
					globalSearchLayout.setActivated ( true );
					byProximitySearchLayout.setActivated ( false );
					availableNowSearchLayout.setActivated ( false );
					filterManager.setSearchType ( SearchType.GLOBAL );

					break;

				case R.id.byProximitySearchLayout:
					globalSearchLayout.setActivated ( false );
					byProximitySearchLayout.setActivated ( true );
					availableNowSearchLayout.setActivated ( false );
					filterManager.setSearchType ( SearchType.PROXIMITY );
					break;

				case R.id.availableNowSearchLayout:
					globalSearchLayout.setActivated ( false );
					byProximitySearchLayout.setActivated ( false );
					availableNowSearchLayout.setActivated ( true );
					filterManager.setSearchType ( SearchType.AVAILABLE_NOW );

					break;

				default:
					break;
			}
			gridFragment.refreshData ();
		}

	};

	private void addListeners ()
	{
		menuImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View view )
			{
				controller.openMenu ();
			}
		} );

		searchButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{

				if ( searchButton.isActivated () )
				{
					searchButton.setActivated ( false );
					editTextSearchLayout.setVisibility ( View.GONE );
					searchDevider.setVisibility ( View.GONE );
					typeSearchLayout.setVisibility ( View.GONE );

				} else
				{
					searchButton.setActivated ( true );
					editTextSearchLayout.setVisibility ( View.VISIBLE );
					searchDevider.setVisibility ( View.VISIBLE );
					typeSearchLayout.setVisibility ( View.VISIBLE );
					// searchButton.setVisibility ( View.GONE );
				}
			}
		} );

		globalSearchLayout.setOnClickListener ( onSearchTypeClickListener );
		byProximitySearchLayout.setOnClickListener ( onSearchTypeClickListener );
		availableNowSearchLayout.setOnClickListener ( onSearchTypeClickListener );

		boyTypeExpandableListView.setOnGroupClickListener ( new OnGroupClickListener ()
		{

			@Override
			public boolean onGroupClick ( ExpandableListView parent, View v, int groupPosition, long id )
			{

				if ( boyTypeExpandableListView.isGroupExpanded ( groupPosition ) )
				{
					boyTypeExpandableListView.collapseGroupWithAnimation ( groupPosition );
					previousGroup = -1;
				} else
				{
					boyTypeExpandableListView.expandGroupWithAnimation ( groupPosition );
					if ( previousGroup != -1 )
					{
						boyTypeExpandableListView.collapseGroupWithAnimation ( previousGroup );
					}
					previousGroup = groupPosition;
				}

				return true;
			}

		} );

		boyTypeExpandableListView.setOnChildClickListener ( new OnChildClickListener ()
		{

			@Override
			public boolean onChildClick ( ExpandableListView parent, View v, int groupPosition, int childPosition, long id )
			{
				typeSearchLayout.performClick ();
				searchByJobId ( jobItems.get ( groupPosition ).items.get ( childPosition ).id, jobItems.get ( groupPosition ).items.get ( childPosition ).title );
				return true;
			}
		} );

		typeSearchLayout.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				if ( arrowInsearchImageView.isActivated () )
				{
					boyTypeExpandableListView.setVisibility ( View.GONE );
					arrowInsearchImageView.setActivated ( false );
				} else
				{
					arrowInsearchImageView.setActivated ( true );
					boyTypeExpandableListView.setVisibility ( View.VISIBLE );
				}
			}
		} );

		filterButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View v )
			{
				controller.setState ( VIEW_STATE.FILTER );
			}
		} );

		searchTextView.setOnEditorActionListener ( new OnEditorActionListener ()
		{
			public boolean onEditorAction ( TextView v, int actionId, KeyEvent event )
			{
				if ( actionId == EditorInfo.IME_ACTION_SEARCH )
				{
                    filterManager.clearFilter();
                    filterManager = FilterManager.getInstance ();
                    updateFilterButton();
					filterManager.setSearchString ( searchTextView.getText ().toString () );
					InputMethodManager imm = ( InputMethodManager ) controller.getSystemService ( Context.INPUT_METHOD_SERVICE );
					imm.hideSoftInputFromWindow ( searchTextView.getWindowToken (), 0 );
					gridFragment.refreshData ();
					return true;
				}
				return false;
			}
		} );

		crossButton.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
                filterManager.setSearchString ( "" );
                filterManager.setJobId ( "" );
                filterManager.setIsSearchByFilter ( false );
				searchTextView.setText ( "" );
				searchGroupNameTextView.setText ( "" );
				searchButton.performClick ();
				boyTypeExpandableListView.setVisibility ( View.GONE );
				arrowInsearchImageView.setActivated ( false );
				gridFragment.refreshData ();
                updateFilterButton();
			}
		} );

	}
}