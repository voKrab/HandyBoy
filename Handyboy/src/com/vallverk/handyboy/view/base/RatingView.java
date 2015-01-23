package com.vallverk.handyboy.view.base;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.job.TypejobLevel;

public class RatingView extends FrameLayout
{
	float rating;
	private ImageView star1ImageView;
	private ImageView star2ImageView;
	private ImageView star3ImageView;
	private ImageView star4ImageView;
	private ImageView star5ImageView;
	private ArrayList < ImageView > stars;

	public RatingView ( Context context )
	{
		super ( context );
		init ( context, null );
	}

	public RatingView ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		init ( context, attrs );
	}

	public RatingView ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
		init ( context, attrs );
	}

	private void init ( final Context context, AttributeSet attrs )
	{
		rating = 0;
		final LayoutInflater inflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
		final View view = inflater.inflate ( R.layout.rating_layout, null );
		TypedArray ta = context.obtainStyledAttributes ( attrs, R.styleable.RatingView, 0, 0 );
		try
		{
			rating = ta.getFloat ( R.styleable.RatingView_rating, 0 );

		} finally
		{
			ta.recycle ();
		}
		if ( !isInEditMode () )
		{
			star1ImageView = ( ImageView ) view.findViewById ( R.id.star1ImageView );
			star2ImageView = ( ImageView ) view.findViewById ( R.id.star2ImageView );
			star3ImageView = ( ImageView ) view.findViewById ( R.id.star3ImageView );
			star4ImageView = ( ImageView ) view.findViewById ( R.id.star4ImageView );
			star5ImageView = ( ImageView ) view.findViewById ( R.id.star5ImageView );
			stars = new ArrayList < ImageView > ();
			stars.add ( star1ImageView );
			stars.add ( star2ImageView );
			stars.add ( star3ImageView );
			stars.add ( star4ImageView );
			stars.add ( star5ImageView );
			setRating ( rating );
		}
		addView ( view );
	}

	public void setRating ( float rating )
	{
		this.rating = rating;
		int realPart = ( int ) rating;
		int currentStar = 0;
		for ( ; currentStar < realPart; currentStar++ )
		{
			ImageView star = stars.get ( currentStar );
			star.setImageResource ( R.drawable.red_star );
		}
		if ( currentStar >= stars.size () )
		{
			return;
		}
		ImageView lastStar = stars.get ( currentStar );
		float mod = rating - realPart;
		if ( Math.round ( mod ) == 1 )
		{
			lastStar.setImageResource ( R.drawable.red_star );
		} else
		{
			if ( mod >= 0.25f )
			{
				lastStar.setImageResource ( R.drawable.half_red_star );
			} else
			{
				lastStar.setImageResource ( R.drawable.gray_star );
			}
		}
		currentStar++;
		for ( ; currentStar < stars.size (); currentStar++ )
		{
			ImageView star = stars.get ( currentStar );
			star.setImageResource ( R.drawable.gray_star );
		}
	}

	private OnClickListener clickListener = new OnClickListener ()
	{

		@Override
		public void onClick ( View view )
		{
			switch ( view.getId () )
			{
				case R.id.star1ImageView:
					setRating ( 1.0f );
					break;

				case R.id.star2ImageView:
					setRating ( 2.0f );
					break;

				case R.id.star3ImageView:
					setRating ( 3.0f );
					break;

				case R.id.star4ImageView:
					setRating ( 4.0f );
					break;

				case R.id.star5ImageView:
					setRating ( 5.0f );
					break;

				default:
					break;
			}

		}
	};

	public void setCanClicable ( boolean isClicable )
	{
		star1ImageView.setOnClickListener ( clickListener );
		star2ImageView.setOnClickListener ( clickListener );
		star3ImageView.setOnClickListener ( clickListener );
		star4ImageView.setOnClickListener ( clickListener );
		star5ImageView.setOnClickListener ( clickListener );
	}
	
	public float getRating ()
	{
		return rating;
	}
}
