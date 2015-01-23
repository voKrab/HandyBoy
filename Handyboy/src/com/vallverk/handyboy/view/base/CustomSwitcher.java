package com.vallverk.handyboy.view.base;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CustomSwitcher extends FrameLayout
{
	private ImageView selectorImageView;
	private ImageView onOffImageView;

	private boolean isActive = false;
	private int duration = 250;

	private TransitionDrawable onOfTransitionDrawable;
	private ColorDrawable[] transitionColor = { new ColorDrawable ( Color.TRANSPARENT ), new ColorDrawable ( Color.WHITE ) };

	private OnSwitchedChangeListener onSwitchedChangeListener;

	public CustomSwitcher ( Context context )
	{
		super ( context );
		init ( context, null );
	}

	public CustomSwitcher ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		init ( context, attrs );
	}

	public CustomSwitcher ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
		init ( context, attrs );
	}

	public interface OnSwitchedChangeListener
	{
		public void onSwitchChange ( boolean isActive );
	}

	public AnimatorListener animatorListener = new AnimatorListener ()
	{

		@Override
		public void onAnimationStart ( Animator animation )
		{
		}

		@Override
		public void onAnimationRepeat ( Animator animation )
		{
		}

		@Override
		public void onAnimationEnd ( Animator animation )
		{
			if ( onSwitchedChangeListener != null )
			{
				onSwitchedChangeListener.onSwitchChange ( isActive );
			}
		}

		@Override
		public void onAnimationCancel ( Animator animation )
		{
		}
	};

	public void setOnSwitchedChangeListener ( OnSwitchedChangeListener onSwitchedChangeListener )
	{
		this.onSwitchedChangeListener = onSwitchedChangeListener;
	}

	public boolean isActive ()
	{
		return isActive;
	}

	public boolean setActive ( boolean isActive, boolean isCallBack )
	{
		this.isActive = isActive;
		setActiveResourseAnimation ( isCallBack );
		return isActive;
	}

	public void setDuration ( int duration )
	{
		this.duration = duration;
	}

	private void setActiveResourseAnimation ( boolean isCallBack )
	{
		if ( isActive )
		{
			onOffImageView.animate ().translationX ( Tools.fromDPToPX ( 14, getContext () ) ).setDuration ( duration ).setListener ( isCallBack ? animatorListener : null ).start ();
			setBackgroundDrawable ( onOfTransitionDrawable );
			onOfTransitionDrawable.startTransition ( duration );
		} else
		{
			onOffImageView.animate ().translationX ( Tools.fromDPToPX ( -14, getContext () ) ).setDuration ( duration ).setListener ( isCallBack ? animatorListener : null ).start ();
			setBackgroundDrawable ( onOfTransitionDrawable );
			onOfTransitionDrawable.reverseTransition ( duration );
		}
	}

	private void init ( Context context, AttributeSet attrs )
	{
		onOfTransitionDrawable = new TransitionDrawable ( transitionColor );
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams ( FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT );
		layoutParams.gravity = Gravity.CENTER;

		onOffImageView = new ImageView ( context );
		onOffImageView.setLayoutParams ( layoutParams );
		onOffImageView.setImageResource ( R.drawable.on_off );
		addView ( onOffImageView );

		selectorImageView = new ImageView ( context );
		selectorImageView.setLayoutParams ( layoutParams );
		selectorImageView.setImageResource ( R.drawable.selector );
		addView ( selectorImageView );

		TypedArray typedArray = context.obtainStyledAttributes ( attrs, R.styleable.CustomSwitcher, 0, 0 );
		try
		{
			boolean isTempActive = typedArray.getBoolean ( R.styleable.CustomSwitcher_isActive, false );
			setActive ( isTempActive, false );
			if ( isTempActive )
			{
				setBackgroundColor ( Color.WHITE );
			} else
			{
				setBackgroundColor ( Color.TRANSPARENT );
			}

		} catch ( Exception ex )
		{
			ex.printStackTrace ();
		} finally
		{
			typedArray.recycle ();
		}

		this.setOnClickListener ( new OnClickListener ()
		{

			@Override
			public void onClick ( View view )
			{

				if ( isActive () )
				{
					setActive ( false, true );
				} else
				{
					setActive ( true, true );
				}
			}
		} );
	}

}
