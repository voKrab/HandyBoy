package com.vallverk.handyboy.model.api;

import java.io.Serializable;
import org.json.JSONObject;

import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;

public class GalleryAPIObject extends APIObject implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum GalleryAPIParams
	{
		SERVICE_ID ( "serviceId" ), TYPE ( "type" ), URL ( "url" ), DESCRIPTION ( "description" ), STATUS ( "status" );

		String name;

		GalleryAPIParams ( String name )
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

		public static GalleryAPIParams fromString ( String string )
		{
			for ( GalleryAPIParams category : GalleryAPIParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}
	}

	/*public GalleryAPIObject ( String serviceId, String type, String url, String description, String status )
	{
		super ();

		putValue ( GalleryAPIParams.SERVICE_ID, serviceId );
		putValue ( GalleryAPIParams.TYPE, type );
		putValue ( GalleryAPIParams.URL, url );
		putValue ( GalleryAPIParams.DESCRIPTION, description );
		putValue ( GalleryAPIParams.STATUS, status );
	}*/

	public GalleryAPIObject ( JSONObject jsonObject ) throws Exception
	{
		update ( jsonObject );
	}
	
	@Override
	public Object[] getParams ()
	{
		return GalleryAPIParams.values ();
	}
}
