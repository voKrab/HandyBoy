package com.vallverk.handyboy.model.job;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vallverk.handyboy.FileManager;
import com.vallverk.handyboy.MainActivity;

/**
 * Manager of all exists job types 
 */
public class JobTypeManager implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JobTypeManager instance;

	public static JobTypeManager getInstance ()
	{
		if ( instance == null )
		{
			instance = ( JobTypeManager ) FileManager.loadObject ( MainActivity.getInstance (), FileManager.JOB_TYPE_MANAGER_SAVE_PATH, new JobTypeManager () );
		}
		return instance;
	}

	public JobTypeManager ()
	{
		jobTypes = new HashMap < TypeJobEnum, TypeJob > ();
	}

	private Map < TypeJobEnum, TypeJob > jobTypes;

	public Boolean isValid ()
	{
		return !jobTypes.isEmpty ();
	}

	public void update ( String dataString ) throws Exception
	{
		Map < TypeJobEnum, TypeJob > newJobTypes = new HashMap < TypeJobEnum, TypeJob > ();
		JSONArray arrayData = new JSONObject ( dataString ).getJSONArray ( "list" );
		for ( int i = 0; i < arrayData.length (); i++ )
		{
			JSONObject item = new JSONObject ( arrayData.getString ( i ) );
			TypeJob jobType = new TypeJob ( item );
			newJobTypes.put ( jobType.getEnumValue (), jobType );
		}
		this.jobTypes = newJobTypes;
	}

	public void save ()
	{
		FileManager.saveObject ( MainActivity.getInstance (), FileManager.JOB_TYPE_MANAGER_SAVE_PATH, this );
	}

	public TypeJob getJobType ( TypeJobEnum key )
	{
		return jobTypes.get ( key );
	}
	
	public TypeJob getJobType ( String idString )
	{
		for ( TypeJob jobType : jobTypes.values () )
		{
			if ( jobType.getId () == Integer.parseInt ( idString ) )
			{
				return jobType;
			}
		}
		return null;
	}

	public List < TypeJob > getJobTypes ( JobCategory category )
	{
		List < TypeJob > jobTypesForcategory = new ArrayList < TypeJob > (); 
		for ( TypeJob jobType : jobTypes.values () )
		{
			if ( jobType.getCategory () == category )
			{
				jobTypesForcategory.add ( jobType );
			}
		}
		return jobTypesForcategory;
	}

	public int getImageResource ( String string )
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
