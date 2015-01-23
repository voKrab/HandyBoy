package com.vallverk.handyboy.view;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.view.base.BaseFragment;

public class NavigationDrawerFragment extends BaseFragment
{
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
	private NavigationDrawerCallbacks mCallbacks;

	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListView;
	private View mFragmentContainerView;
	private int mCurrentSelectedPosition = 0;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;
	private ImageView closeImageView;

	private View dashboardContainer;
	private View searchContainer;
	private View profileContainer;
	private View accountContainer;
	private View helpContainer;
	private View exitContainer;

	private View dashboardDevider;
	private View searchDevider;

	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
		setHasOptionsMenu ( true );
		addListeners ();
	}

	public void updateViews ()
	{
		if ( APIManager.getInstance ().getUser ().isService () )
		{
			dashboardContainer.setVisibility ( View.VISIBLE );
			searchContainer.setVisibility ( View.GONE );
			searchDevider.setVisibility ( View.GONE );
		} else
		{
			dashboardContainer.setVisibility ( View.GONE );
			searchContainer.setVisibility ( View.VISIBLE );
			dashboardDevider.setVisibility ( View.GONE );
		}

	}

	private void addListeners ()
	{
		closeImageView.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View view )
			{
				mDrawerLayout.closeDrawer ( mFragmentContainerView );
			}
		} );
		exitContainer.setOnClickListener ( new OnClickListener ()
		{
			@Override
			public void onClick ( View view )
			{
				MainActivity.getInstance ().logout ();
			}
		} );

		profileContainer.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
				if ( APIManager.getInstance ().getUser ().isService () )
				{
					controller.setState ( VIEW_STATE.SERVICE_EDIT_PROFILE );
				} else
				{
					controller.setState ( VIEW_STATE.CUSTOMER_EDIT_PROFILE );
				}

			}
		} );

		accountContainer.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
				controller.setState ( VIEW_STATE.ACCOUNT );
			}
		} );

		dashboardContainer.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
				controller.setState ( VIEW_STATE.DASHBOARD );
			}
		} );

		searchContainer.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{
				controller.setState ( VIEW_STATE.FEED );
			}
		} );
		
		helpContainer.setOnClickListener ( new OnClickListener()
		{
			
			@Override
			public void onClick ( View view )
			{
				controller.setState ( VIEW_STATE.HELP );
			}
		} );
	}

	@Override
	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.fragment_navigation_drawer, container, false );

		closeImageView = ( ImageView ) view.findViewById ( R.id.closeImageView );
		dashboardContainer = view.findViewById ( R.id.dashboardButton );
		searchContainer = view.findViewById ( R.id.searchButton );
		profileContainer = view.findViewById ( R.id.profileButton );
		accountContainer = view.findViewById ( R.id.accountButton );
		helpContainer = view.findViewById ( R.id.helpButton );
		exitContainer = view.findViewById ( R.id.exitButton );

		dashboardDevider = view.findViewById ( R.id.dashboardDevider );
		searchDevider = view.findViewById ( R.id.searchDevider );
		return view;
	}

	public boolean isDrawerOpen ()
	{
		return mDrawerLayout != null && mDrawerLayout.isDrawerOpen ( mFragmentContainerView );
	}

	public void setUp ( int fragmentId, DrawerLayout drawerLayout )
	{
		mFragmentContainerView = getActivity ().findViewById ( fragmentId );
		mDrawerLayout = drawerLayout;
		mDrawerLayout.setDrawerShadow ( R.drawable.drawer_shadow, GravityCompat.START );
	}

	private void selectItem ( int position )
	{
		mCurrentSelectedPosition = position;
		if ( mDrawerListView != null )
		{
			mDrawerListView.setItemChecked ( position, true );
		}
		if ( mDrawerLayout != null )
		{
			mDrawerLayout.closeDrawer ( mFragmentContainerView );
		}
		if ( mCallbacks != null )
		{
			mCallbacks.onNavigationDrawerItemSelected ( position );
		}
	}

	@Override
	public void onAttach ( Activity activity )
	{
		super.onAttach ( activity );
		try
		{
			mCallbacks = ( NavigationDrawerCallbacks ) activity;
		} catch ( ClassCastException e )
		{
			throw new ClassCastException ( "Activity must implement NavigationDrawerCallbacks." );
		}
	}

	@Override
	public void onDetach ()
	{
		super.onDetach ();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState ( Bundle outState )
	{
		super.onSaveInstanceState ( outState );
		outState.putInt ( STATE_SELECTED_POSITION, mCurrentSelectedPosition );
	}

	@Override
	public void onConfigurationChanged ( Configuration newConfig )
	{
		super.onConfigurationChanged ( newConfig );
		mDrawerToggle.onConfigurationChanged ( newConfig );
	}

	@Override
	public boolean onOptionsItemSelected ( MenuItem item )
	{
		if ( mDrawerToggle.onOptionsItemSelected ( item ) )
		{
			return true;
		}

		if ( item.getItemId () == R.id.action_example )
		{
			Toast.makeText ( getActivity (), "Example action.", Toast.LENGTH_SHORT ).show ();
			return true;
		}

		return super.onOptionsItemSelected ( item );
	}

	public static interface NavigationDrawerCallbacks
	{
		void onNavigationDrawerItemSelected ( int position );
	}

	public void setSwipeEnabled ( boolean isEnabled )
	{
		mDrawerLayout.setDrawerLockMode ( isEnabled ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_OPEN, mFragmentContainerView );
		if ( !isEnabled )
		{
			mDrawerLayout.closeDrawer ( mFragmentContainerView );
		}
	}

	public void show ()
	{
		mDrawerLayout.openDrawer ( mFragmentContainerView );
	}

	public void close ()
	{
		mDrawerLayout.closeDrawer ( mFragmentContainerView );
	}
}
