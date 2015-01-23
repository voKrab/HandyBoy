package com.vallverk.handyboy.view.jobdescription;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.job.TypejobLevel;

public class JobLevelView extends FrameLayout
{
	private TypejobLevel level;
	private View lowLevelView;
	private View mediumLevelView;
	private View hiLevelView;
	private int startWidth = 0; 
	
	public JobLevelView ( Context context )
	{
		super ( context );
		init ( context, null );
	}

	public JobLevelView ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		init ( context, attrs );
	}

	public JobLevelView ( Context context, AttributeSet attrs, int defStyle )
	{
		super ( context, attrs, defStyle );
		init ( context, attrs );
	}

	private void init ( final Context context, AttributeSet attrs )
	{
		level = TypejobLevel.SOME_EXPERIENCE;
		final LayoutInflater inflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
		final View view = inflater.inflate ( R.layout.job_level_layout, null );
		if ( !isInEditMode () )
		{
			final View levelView = view.findViewById ( R.id.levelView );
			lowLevelView = view.findViewById ( R.id.lowLevelView );
			mediumLevelView = view.findViewById ( R.id.mediumLevelView );
			hiLevelView = view.findViewById ( R.id.hiLevelView );
			final TextView levelTextView = ( TextView ) view.findViewById ( R.id.levelTextView );
			levelTextView.setText ( level.toString () );
			final int duration = 250;
			lowLevelView.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View v )
				{
					if ( level == TypejobLevel.SOME_EXPERIENCE )
					{
						return;
					}
					level = TypejobLevel.SOME_EXPERIENCE;
					levelTextView.setVisibility ( View.GONE );
					levelView.animate ().translationX ( lowLevelView.getX () - Tools.fromDPToPX ( 8, context ) ).setDuration ( duration ).setListener ( new AnimatorListener ()
					{
						@Override
						public void onAnimationStart ( Animator animation )
						{
							levelTextView.setText ( level.toString () );
							levelTextView.setVisibility ( View.VISIBLE );
						}
						
						@Override
						public void onAnimationRepeat ( Animator animation )
						{
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationEnd ( Animator animation )
						{
							
						}
						
						@Override
						public void onAnimationCancel ( Animator animation )
						{
							// TODO Auto-generated method stub
							
						}
					} );
				}
			});
			mediumLevelView.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View v )
				{
					if ( level == TypejobLevel.EXPERIENCED )
					{
						return;
					}
					level = TypejobLevel.EXPERIENCED;
					levelTextView.setVisibility ( View.GONE );
					levelView.animate ().translationX ( mediumLevelView.getX () - Tools.fromDPToPX ( 8, context ) ).setDuration ( duration ).setListener ( new AnimatorListener ()
					{
						@Override
						public void onAnimationStart ( Animator animation )
						{
							levelTextView.setText ( level.toString () );
							levelTextView.setVisibility ( View.VISIBLE );
						}
						
						@Override
						public void onAnimationRepeat ( Animator animation )
						{
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationEnd ( Animator animation )
						{
							
						}
						
						@Override
						public void onAnimationCancel ( Animator animation )
						{
							// TODO Auto-generated method stub
							
						}
					} );
				}
			});
			hiLevelView.setOnClickListener ( new OnClickListener ()
			{
				@Override
				public void onClick ( View v )
				{
					if ( level == TypejobLevel.VERY_EXPERIENCED )
					{
						return;
					}
					level = TypejobLevel.VERY_EXPERIENCED;
					levelTextView.setVisibility ( View.GONE );
					levelView.animate ().translationX ( hiLevelView.getX () - Tools.fromDPToPX ( 8, context ) ).setDuration ( duration ).setListener ( new AnimatorListener ()
					{
						@Override
						public void onAnimationStart ( Animator animation )
						{
							levelTextView.setText ( level.toString () );
							levelTextView.setVisibility ( View.VISIBLE );
						}
						
						@Override
						public void onAnimationRepeat ( Animator animation )
						{
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationEnd ( Animator animation )
						{
							
						}
						
						@Override
						public void onAnimationCancel ( Animator animation )
						{
							// TODO Auto-generated method stub
							
						}
					} );
				}
			});
			
//			headerCheckBox.setTranslationX ( headerLeftPadding );
//			final View detailsContainer = view.findViewById ( R.id.detailsContainer );
//			final int darkBlueColor = context.getResources ().getColor ( R.color.dark_blue );
//			final int textDarkGrey = context.getResources ().getColor ( R.color.text_dark_grey );
//			headerCheckBox.setOnClickListener ( new OnClickListener ()
//			{
//				@Override
//				public void onClick ( View v )
//				{
//					boolean isChecked = headerCheckBox.isChecked ();
//					if ( isChecked )
//					{
//						headerCheckBox.setTextColor ( darkBlueColor );
//						detailsContainer.setVisibility ( View.VISIBLE );
//						// view.setVisibility ( View.GONE );
//					} else
//					{
//						headerCheckBox.setTextColor ( textDarkGrey );
//						detailsContainer.setVisibility ( View.GONE );
//					}
//				}
//			} );
		}
		addView ( view, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT );
	}
	
	public String toString ()
	{
		return level.toString ();
	}

	@Override
    public void onMeasure ( int widthMeasureSpec, int heightMeasureSpec )
	{
		super.onMeasure ( widthMeasureSpec, heightMeasureSpec );
		if ( startWidth != widthMeasureSpec )
		{
			setLevel ( this.level );
		}
		startWidth = widthMeasureSpec;
	}
	
	public void setLevel ( final TypejobLevel jobLevel )
	{
		this.level = jobLevel;
		post ( new Runnable ()
		{
			@Override
			public void run ()
			{
				level = null;
				switch ( jobLevel )
				{
					case SOME_EXPERIENCE:
					{
						lowLevelView.performClick ();
						break;
					}
					case EXPERIENCED:
					{
						mediumLevelView.performClick ();
						break;
					}
					case VERY_EXPERIENCED:
					{
						hiLevelView.performClick ();
						break;
					}
				}
			}
		} );
	}
}
