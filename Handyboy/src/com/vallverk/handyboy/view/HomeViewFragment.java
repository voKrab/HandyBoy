package com.vallverk.handyboy.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.view.base.BaseFragment;

public class HomeViewFragment extends BaseFragment
{
	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate ( R.layout.home_layout, null );
		
		view.findViewById ( R.id.textView1 ).startAnimation ( AnimationUtils.loadAnimation ( getActivity(), R.anim.tremble ) );
		
		return view;
	}
	
	@Override
	public void onActivityCreated ( Bundle savedInstanceState )
	{
		super.onActivityCreated ( savedInstanceState );
	}
}