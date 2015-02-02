package com.vallverk.handyboy.view.base;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.view.GridViewAdapter;

import java.util.List;

public class BaseGridFragment extends BaseFragment
{
	public static int numberAttemptsGetLocation = 0;
	protected View toolBarView;
	public List objects;
	protected View view;
	protected PullToRefreshGridView gridViewPTR;
	protected GridView gridView;
	private ArrayAdapter gridAdapter;
	protected TextView emptyTextView;
	private Refresher refresher;

	public void onAttach ( final Activity activity )
	{
		super.onAttach ( activity );
	}

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.base_grid_layout, null );
		} else
		{
			( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}

	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );

		// objects = new ArrayList<ParseObject> ();

		if ( gridViewPTR == null )
		{
			emptyTextView = ( TextView ) getView ().findViewById ( R.id.emptyTextView );
			emptyTextView.setVisibility ( View.GONE );

			gridViewPTR = ( PullToRefreshGridView ) getView ().findViewById ( R.id.baseGridView );
			gridView = gridViewPTR.getRefreshableView ();
			gridAdapter = getAdapter ();
			gridView.setAdapter ( gridAdapter );
			int spacing = Tools.fromDPToPX ( 0, getActivity () );
			gridView.setColumnWidth ( ( int ) ( MainActivity.SCREEN_WIDTH / 3f - spacing / 3f ) );
			gridView.setStretchMode ( GridView.STRETCH_SPACING );
			gridView.setHorizontalSpacing ( spacing );
			gridView.setVerticalSpacing ( spacing );
			gridView.setNumColumns ( 3 );
			addListeners ();
		}
	}

	protected ArrayAdapter getAdapter ()
	{
		return new GridViewAdapter ( getActivity () );
	}

	protected void addListeners ()
	{
		gridViewPTR.setOnRefreshListener ( new OnRefreshListener < GridView > ()
		{
			@Override
			public void onRefresh ( PullToRefreshBase < GridView > refreshView )
			{
				refreshView.getLoadingLayoutProxy ().setLastUpdatedLabel ( "" );
				// Do work to refresh the list here.
				refreshData ();
			}
		} );

		gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ParseObject productObject = ( ParseObject ) objects.get (
                // position );
                // FashionGramApplication.temp.push ( productObject );
                // FashionGramApplication.viewStateController.setState (
                // VIEW_STATE.PRODUCT_DETAILS );
            }
        });
        gridViewPTR.setOnScrollListener ( new AbsListView.OnScrollListener ()
        {
            @Override
            public void onScrollStateChanged ( AbsListView view, int scrollState )
            {
            }

            @Override
            public void onScroll ( AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount )
            {
//                int countItemsInRow = 3;
                if ( firstVisibleItem + visibleItemCount >= totalItemCount && refresher.isHasMore () && refresher.isNeedLoadMore ( totalItemCount ) )
                {
                    loadMoreItems ( totalItemCount );
                }
            }
        } );
	}

    public void setRefresher ( Refresher refresher )
	{
		this.refresher = refresher;
	}

    private void loadMoreItems ( final int totalItemCount )
    {
        new AsyncTask < Void, Void, String > ()
        {
            private List items;

            @Override
            protected void onPreExecute ()
            {
                super.onPreExecute ();
//                controller.showLoader ();
            }

            public void onPostExecute ( String result )
            {
                super.onPostExecute ( result );
//                controller.hideLoader ();
                if ( result.isEmpty () && !items.isEmpty() )
                {
                    objects.addAll ( items );
                    gridAdapter.addAll ( items );
                    gridAdapter.notifyDataSetChanged();
                }
            }

            @Override
            protected String doInBackground ( Void... params )
            {
                String result = "";
                try
                {
                    items = refresher.loadMoreItems ( totalItemCount );
                } catch ( Exception ex )
                {
                    ex.printStackTrace ();
                    result = ex.getMessage ();
                }
                return result;
            }
        }.execute ();
    }

	public void refreshData ()
	{
		new AsyncTask < Void, Void, String > ()
		{
			public void onPostExecute ( String result )
			{
				super.onPostExecute ( result );
				if ( result.isEmpty () )
				{
					updateContent ();
                }
				gridViewPTR.onRefreshComplete ();
			}

			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
                    refresher.init ();
					objects = refresher.refresh ();
                    refresher.setHasMore ( objects.size () == refresher.pageLimit );
                } catch ( Exception ex )
				{
					ex.printStackTrace ();
					result = ex.getMessage ();
				}
				return result;
			}
		}.execute ();
	}

	protected void updateContent ()
	{
		updateContent ( true );
	}

	protected void updateContent ( boolean isRemoveFreezed )
	{
		gridAdapter.clear ();
		gridAdapter.addAll ( objects );
		gridAdapter.notifyDataSetChanged ();

		emptyTextView.setVisibility ( objects.isEmpty () ? View.VISIBLE : View.GONE );
	}

	public void onResume ()
	{
		super.onResume ();
	}

	public void onPause ()
	{
		super.onPause ();
	}
}
