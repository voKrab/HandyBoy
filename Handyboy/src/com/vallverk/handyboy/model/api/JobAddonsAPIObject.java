package com.vallverk.handyboy.model.api;

import java.io.Serializable;
import org.json.JSONObject;

import com.vallverk.handyboy.model.api.JobAddonsAPIObject.JobAddonsAPIParams;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;

public class JobAddonsAPIObject extends APIObject implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum JobAddonsAPIParams
	{
		NAME ( "name" ), MIN_COST ( "minCost" ), MAX_COST ( "maxCost" ), TIME ( "time" ), PARENT ( "parent" ), TYPE_JOB_ID ( "typeJobId" );

		String name;

		JobAddonsAPIParams ( String name )
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

		public static JobAddonsAPIParams fromString ( String string )
		{
			for ( JobAddonsAPIParams category : JobAddonsAPIParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}
	}

	public JobAddonsAPIObject ( String serviceId, String type, String url, String description, String status )
	{
		super ();

//		putValue ( JobAddonsAPIParams.SERVICE_ID, serviceId );
//		putValue ( JobAddonsAPIParams.TYPE, type );
//		putValue ( JobAddonsAPIParams.URL, url );
//		putValue ( JobAddonsAPIParams.DESCRIPTION, description );
//		putValue ( JobAddonsAPIParams.STATUS, status );
	}

	public JobAddonsAPIObject ()
	{
		
	}
	
	public JobAddonsAPIObject ( JSONObject jsonObject ) throws Exception
	{
		update ( jsonObject );
	}
	
	@Override
	public Object[] getParams ()
	{
		return JobAddonsAPIParams.values ();
	}
}
