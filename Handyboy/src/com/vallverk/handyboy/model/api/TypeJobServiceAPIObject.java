package com.vallverk.handyboy.model.api;

import org.json.JSONObject;

import com.vallverk.handyboy.R;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.model.job.JobTypeManager;
import com.vallverk.handyboy.model.job.TypeJob;
import com.vallverk.handyboy.server.ServerManager;

public class TypeJobServiceAPIObject extends APIObject
{
	private static final long serialVersionUID = 1L;

	public enum TypeJobServiceParams
	{
		DESCRIPTION ( "description" ), SERVICE_ID ( "serviceId" ), TYPEJOB_ID ( "typeJobId" ), PRICE ( "price" ), /*PRICE_DISTANCE ( "priceDistance" ),*/ LEVEL ( "level" ), APROVE_FILE ( "aproveFile" );

		String name;

		TypeJobServiceParams ( String name )
		{
			this.name = name;
		}

		public String toString ()
		{
			return name;
		}

		public void setName ( String name )
		{
			this.name = name;
		}

		public static TypeJobServiceParams fromString ( String string )
		{
			for ( TypeJobServiceParams category : TypeJobServiceParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}
	}
	
	public TypeJobServiceAPIObject ( String serviceId, String typeJobId, int price, String level, String aproveFile )
	{
		super ();
		
		putValue ( TypeJobServiceParams.SERVICE_ID, serviceId );
		putValue ( TypeJobServiceParams.TYPEJOB_ID, typeJobId );
		putValue ( TypeJobServiceParams.PRICE, price );
		//putValue ( TypeJobServiceParams.PRICE_DISTANCE, priceDistance );
		putValue ( TypeJobServiceParams.LEVEL, level );
		putValue ( TypeJobServiceParams.APROVE_FILE, aproveFile );
	}
	
	public TypeJobServiceAPIObject ( JSONObject jsonObject ) throws Exception
	{
		update ( jsonObject );
	}

	public TypeJobServiceAPIObject ()
	{
		super ();
	}

	public int getImageResource ( boolean isSelected )
	{
		switch ( Integer.parseInt ( getString ( TypeJobServiceParams.TYPEJOB_ID ) ) )
		{
			case 1:
			{
				return isSelected ? R.drawable.hand_a : R.drawable.hand_na;
			}
			case 2:
			{
				return isSelected ? R.drawable.dumbbels_a : R.drawable.dumbbels_na;
			}
			case 3:
			{
				return isSelected ? R.drawable.venik_a : R.drawable.venik_na;
			}
			case 4:
			{
				return isSelected ? R.drawable.circle_a : R.drawable.circle_na;
			}
			case 5:
			{
				return isSelected ? R.drawable.grass_a : R.drawable.grass_na;
			}
			case 6:
			{
				return isSelected ? R.drawable.car_a : R.drawable.car_na;
			}
			case 7:
			{
				return isSelected ? R.drawable.man_a : R.drawable.man_na;
			}
			case 8:
			{
				return isSelected ? R.drawable.martini_a : R.drawable.martini_na;
			}
			case 9:
			{
				return isSelected ? R.drawable.panties_a : R.drawable.panties_na;
			}
			case 10:
			{
				return isSelected ? R.drawable.foodcover_a : R.drawable.foodcover_na;
			}
		}
		return 0;
	}

	public String getName ()
	{
		TypeJob jobType = getTypeJob ();
		return jobType.getName ();
	}

	public TypeJob getTypeJob ()
	{
		TypeJob jobType = JobTypeManager.getInstance ().getJobType ( getString ( TypeJobServiceParams.TYPEJOB_ID ) );
		return jobType;
	}
	
	public String getDeleteURI ()
	{
		return ServerManager.TYPE_JOB_SERVICE_DELETE;
	}
	
	@Override
	public Object[] getParams ()
	{
		return TypeJobServiceParams.values ();
	}
}
