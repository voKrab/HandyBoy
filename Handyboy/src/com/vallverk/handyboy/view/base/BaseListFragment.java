package com.vallverk.handyboy.view.base;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.view.GridViewAdapter;

import java.util.List;

public class BaseListFragment extends BaseFragment
{
	public static int numberAttemptsGetLocation = 0;
	protected View toolBarView;
	public List objects;
	protected View view;
	protected PullToRefreshListView listViewPTR;
	protected ListView listView;
	private ArrayAdapter listAdapter;
	protected TextView emptyTextView;
	private Refresher refresher;
	private boolean isShowEmpty = true;

	public void onAttach ( final Activity activity )
	{
		super.onAttach ( activity );
	}

	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( view == null )
		{
			view = inflater.inflate ( R.layout.base_list_layout, null );
//            if ( listViewPTR == null )
//            {
                emptyTextView = ( TextView ) view.findViewById ( R.id.emptyTextView );
                emptyTextView.setVisibility ( View.GONE );

                listViewPTR = ( PullToRefreshListView ) view.findViewById ( R.id.baseListView );
                listView = listViewPTR.getRefreshableView ();
                listView.setDivider ( null );
                setAdapter ( getAdapter () );
                addListeners ();
//            }
		} else
		{
			//( ( ViewGroup ) view.getParent () ).removeView ( view );
		}
		return view;
	}

	public void setIsShowEmpty ( boolean isShowEmpty )
	{
		this.isShowEmpty = isShowEmpty;
	}

    @Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
	}

    protected ArrayAdapter getAdapter ()
	{
		return new GridViewAdapter ( getActivity () );
	}

	protected void addListeners ()
	{
		listViewPTR.setOnRefreshListener ( new OnRefreshListener < ListView > ()
		{
			@Override
			public void onRefresh ( PullToRefreshBase < ListView > refreshView )
			{
				refreshView.getLoadingLayoutProxy ().setLastUpdatedLabel ( "" );
				// Do work to refresh the list here.
				refreshData ();
			}
		} );

		listView.setOnItemClickListener ( new OnItemClickListener ()
		{
			public void onItemClick ( AdapterView < ? > parent, View view, int position, long id )
			{
				// ParseObject productObject = ( ParseObject ) objects.get (
				// position );
				// FashionGramApplication.temp.push ( productObject );
				// FashionGramApplication.viewStateController.setState (
				// VIEW_STATE.PRODUCT_DETAILS );
			}
		} );
	}

	public interface Refresher
	{
		public List < Object > refresh () throws Exception;
	}

	public void setRefresher ( Refresher refresher )
	{
		this.refresher = refresher;
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
				listViewPTR.onRefreshComplete ();
			}

			@Override
			protected String doInBackground ( Void... params )
			{
				String result = "";
				try
				{
					objects = refresher.refresh ();
				} catch ( Exception ex )
				{
					ex.printStackTrace ();
					result = ex.getMessage ();
                    if ( result == null )
                    {
                        result = controller.getString ( R.string.error );
                    }
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
		listAdapter.clear ();
		listAdapter.addAll ( objects );
		listAdapter.notifyDataSetChanged ();

		if(isShowEmpty){
			emptyTextView.setVisibility ( objects.isEmpty () ? View.VISIBLE : View.GONE );
		}else{
			emptyTextView.setVisibility ( View.GONE );
		}
	}

	public void onResume ()
	{
		super.onResume ();
	}

	public void onPause ()
	{
		super.onPause ();
	}
	
	public PullToRefreshListView getListView(){
		return listViewPTR;
	}

	public void setAdapter ( ArrayAdapter adapter )
	{
		listAdapter = adapter;
		listView.setAdapter ( listAdapter );
	}
}
