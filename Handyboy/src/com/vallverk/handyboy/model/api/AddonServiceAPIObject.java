package com.vallverk.handyboy.model.api;

import java.io.Serializable;

import org.json.JSONObject;

public class AddonServiceAPIObject extends APIObject implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum AddonServiceAPIParams
	{
		TYPE_JOB_SERVICE_ID ( "typeJobServiceId" ), JOB_ADDONS_ID ( "jobAddonsId" ), PRICE ( "price" ), DATA ( "data" );

		String name;

		AddonServiceAPIParams ( String name )
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

		public static AddonServiceAPIParams fromString ( String string )
		{
			for ( AddonServiceAPIParams category : AddonServiceAPIParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}
	}

	public AddonServiceAPIObject ()
	{
		super ();
	}
	
	public AddonServiceAPIObject ( String serviceId, String type, String url, String description, String status )
	{
		super ();
	}

	public AddonServiceAPIObject ( JSONObject jsonObject ) throws Exception
	{
		update ( jsonObject );
	}
	
	@Override
	public Object[] getParams ()
	{
		return AddonServiceAPIParams.values ();
	}

	public JSONObject getData () throws Exception
	{
		JSONObject data = new JSONObject ( getString ( AddonServiceAPIParams.DATA ) );
		return data;
	}
}
