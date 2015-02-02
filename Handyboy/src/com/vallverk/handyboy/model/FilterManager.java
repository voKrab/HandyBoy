package com.vallverk.handyboy.model;

import android.location.Address;

import com.vallverk.handyboy.server.ServerManager;

public class FilterManager
{
	private static FilterManager instance;

	public enum SearchType
	{
		GLOBAL, PROXIMITY, AVAILABLE_NOW
	}

	private boolean isSearchByFilter;

	// SEARCH PARAMETERS
	private SearchType searchType;
	private String searchString;
	private String jobid;
	private String jobName;

	// FILTER PARAMETERS
	private Address address;

	private String heightFrom;
	private String heightTo;

	private String ageFrom;
	private String ageTo;

	private String ethnicity;

	private String weightFrom;
	private String weightTo;

	private String bodyType;

	private String priceFrom;
	private String priceTo;

	private String sex;

	// SEARCH METHODS
	public static FilterManager getInstance ()
	{
		if ( instance == null )
		{
			instance = new FilterManager ();
		}
		return instance;
	}

	public FilterManager ()
	{
		searchType = SearchType.GLOBAL;
		searchString = "";
		jobid = "";
	}

	public SearchType getSearchType ()
	{
		return searchType;
	}

	public void setSearchType ( SearchType searchType )
	{
		this.searchType = searchType;
	}

	public String getSearchString ()
	{
		return searchString;
	}

	public void setSearchString ( String searchString )
	{
		this.searchString = searchString;
	}

	public void setJobId ( String jobId )
	{
		this.jobid = jobId;
	}

	public void setJobName ( String jobName )
	{
		this.jobName = jobName;
	}

	public String getJobName ()
	{
		return jobName;
	}

	private String makeSearchUrl ( int limit, int offset )
	{
		String url = ServerManager.SEARCH_URL;
		url += "name=" + searchString;
		url += "&sort=" + searchType.ordinal ();
		url += "&limit=" + limit;
		url += "&offset=" + offset;
		url += "&latitude=" + MyLocationManager.imHere.getLatitude();
		url += "&longitude=" + MyLocationManager.imHere.getLongitude();
		return url;
	}

	private String makeSearchByJobUrl ( int limit, int offset )
	{
		String url = ServerManager.SEARCH_FOR_JOB_TYPE_URL;
		url += "jobid=" + jobid;
		url += "&limit=" + limit;
		url += "&sort=" + searchType.ordinal ();
		url += "&offset=" + offset;
        url += "&latitude=" + MyLocationManager.imHere.getLatitude();
        url += "&longitude=" + MyLocationManager.imHere.getLongitude();
		return url;
	}

	private String inchToFoot ( int inches )
	{
		int feet = inches / 12;
		int leftover = inches % 12;
		return feet + "." + leftover;
	}

	private String makeFilterUrl ( int limit, int offset )
	{
		String url = ServerManager.FILTER_URL;
		url += "&height[from]=" + inchToFoot ( Integer.parseInt ( heightFrom ) );
		url += "&height[to]=" + inchToFoot ( Integer.parseInt ( heightTo ) );

		url += "&age[from]=" + ageFrom;
		url += "&age[to]=" + ageTo;

		for ( String tempEthnicity : ethnicity.split ( "," ) )
		{
			if ( tempEthnicity != null && !tempEthnicity.trim ().isEmpty () ){
				url += "&ethnicity[]=" + tempEthnicity.trim ();
			}
		}

		url += "&weight[from]=" + weightFrom;
		url += "&weight[to]=" + weightTo;

		for ( String tempBodyType : bodyType.split ( "," ) )
		{
			if ( tempBodyType != null && !tempBodyType.trim ().isEmpty () ){
				url += "&bodyType[]=" + tempBodyType.trim ();
			}
		}

		url += "&price[from]=" + priceFrom;
		url += "&price[to]=" + priceTo;

		for ( String tempOrientation : sex.split ( "," ) )
		{
			if ( tempOrientation != null && !tempOrientation.trim ().isEmpty () )
			{
				url += "&orientation[]=" + tempOrientation.trim ();
			}
		}
		url += "&limit=" + limit;
		url += "&offset=" + offset;

		if ( address != null )
		{
			url += "&latitude=" + address.getLatitude ();
			url += "&longitude=" + address.getLatitude ();
		}

		url += "&sort=" + searchType.ordinal ();
		if ( !jobid.isEmpty () )
		{
			url += "jobid=" + jobid;
		}

		return url;
	}

	public String getSearchUrl ( int limit, int offset )
	{
		if ( isSearchByFilter )
		{
			return makeFilterUrl ( limit, offset );
		} else if ( jobid.isEmpty () )
		{
			return makeSearchUrl ( limit, offset );
		} else
		{
			return makeSearchByJobUrl ( limit, offset );
		}
	}

	// FILTER METHODS

	public void setIsSearchByFilter ( boolean isSearchByFilter )
	{
		this.isSearchByFilter = isSearchByFilter;
	}

	public void setAdress ( Address address )
	{
		this.address = address;
	}

	public void setHeight ( String heightFrom, String heightTo )
	{
		this.heightFrom = heightFrom;
		this.heightTo = heightTo;
	}

	public void setHeight ( int heightFrom, int heightTo )
	{
		setHeight ( String.valueOf ( heightFrom ), String.valueOf ( heightTo ) );
	}

	public void setAge ( String ageFrom, String ageTo )
	{
		this.ageFrom = ageFrom;
		this.ageTo = ageTo;
	}

	public void setAge ( int ageFrom, int ageTo )
	{
		setAge ( String.valueOf ( ageFrom ), String.valueOf ( ageTo ) );
	}

	public void setWeight ( String weightFrom, String weightTo )
	{
		this.weightFrom = weightFrom;
		this.weightTo = weightTo;
	}

	public void setWeight ( int weightFrom, int weightTo )
	{
		setWeight ( String.valueOf ( weightFrom ), String.valueOf ( weightTo ) );
	}

	public void setPrice ( String priceFrom, String priceTo )
	{
		this.priceFrom = priceFrom;
		this.priceTo = priceTo;
	}

	public void setPrice ( int priceFrom, int priceTo )
	{
		setPrice ( String.valueOf ( priceFrom ), String.valueOf ( priceTo ) );
	}

    public void clearFilter(){
        setSearchString ( "" );
        setJobId ( "" );
        setIsSearchByFilter ( false );
    }

	public void setEthnicity ( String ethnicity )
	{
		this.ethnicity = ethnicity;
	}

	public void setBodyType ( String bodyType )
	{
		this.bodyType = bodyType;
	}

	public void setSex ( String sex )
	{
		this.sex = sex;
	}
}
