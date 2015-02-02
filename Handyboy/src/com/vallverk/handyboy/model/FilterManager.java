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

	private int heightFrom;
	private int heightTo;

	private int ageFrom;
	private int ageTo;

	private String ethnicity;

	private int weightFrom;
	private int weightTo;

	private String bodyType;

	private int priceFrom;
	private int priceTo;

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
        address = null;
        setHeight(48, 108);
        setAge(18, 99);
        setEthnicity("");
        setWeight(300, 90);
        setSex("");
        setBodyType("");
        setPrice(0, 500);
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
		url += "&height[from]=" + inchToFoot ( heightFrom  );
		url += "&height[to]=" + inchToFoot (  heightTo );

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

	public void setAddress ( Address address )
	{
		this.address = address;
	}

    public Address getAddress(){
        return this.address;
    }

	public void setHeight ( int heightFrom, int heightTo )
	{
		this.heightFrom = heightFrom;
		this.heightTo = heightTo;
	}

    public int getHeightFrom(){
        return  heightFrom;
    }

    public int getHeightTo(){
        return  heightTo;
    }

	public void setAge ( int ageFrom, int ageTo )
	{
		this.ageFrom = ageFrom;
		this.ageTo = ageTo;
	}

    public int getAgeFrom() {
        return ageFrom;
    }

    public int getAgeTo() {
        return ageTo;
    }

    public void setWeight ( int weightFrom, int weightTo )
	{
		this.weightFrom = weightFrom;
		this.weightTo = weightTo;
	}

    public int getWeightFrom() {
        return weightFrom;
    }

    public int getWeightTo() {
        return weightTo;
    }

    public void setPrice ( int priceFrom, int priceTo )
	{
		this.priceFrom = priceFrom;
		this.priceTo = priceTo;
	}

    public int getPriceFrom() {
        return priceFrom;
    }

    public int getPriceTo() {
        return priceTo;
    }

    public void clearFilter(){
        //setSearchString ( "" );
       //setJobId ( "" );
        //setIsSearchByFilter ( false );
        instance = null;
    }

	public void setEthnicity ( String ethnicity )
	{
		this.ethnicity = ethnicity;
	}

    public String getEthnicity(){
        return  ethnicity;
    }

	public void setBodyType ( String bodyType )
	{
		this.bodyType = bodyType;
	}

    public  String getBodyType(){
        return  bodyType;
    }

	public void setSex ( String sex )
	{
		this.sex = sex;
	}

    public String getSex() {
        return sex;
    }
}
