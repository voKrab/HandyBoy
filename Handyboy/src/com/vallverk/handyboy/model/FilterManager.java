package com.vallverk.handyboy.model;

import android.location.Address;
import android.text.format.DateFormat;

import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.server.ServerManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

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

    private String timeTo;
    private String timeFrom;

    private long date;

    private float rating;

    private UserAPIObject user;

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
        setHeight(48, 108);
        setAge(18, 99);
        setEthnicity("");
        setWeight(90, 300);
        setSex("");
        setBodyType("");
        setPrice(0, 150);
        setRating(0.0f);
        setDate(0);
        setTimeFrom("");
        setTimeTo("");
		searchType = SearchType.GLOBAL;
		searchString = "";
        isSearchByFilter = false;
        user = APIManager.getInstance().getUser();
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
        url += "&userId=" + user.getId();
		url += "&limit=" + limit;
		url += "&offset=" + offset;
        if ( !jobid.isEmpty () )
        {
            url += "&jobId=" + jobid;
        }

        if(MyLocationManager.imHere != null) {
            url += "&latitude=" + MyLocationManager.imHere.getLatitude();
            url += "&longitude=" + MyLocationManager.imHere.getLongitude();
        }
		return url;
	}

	private String makeSearchByJobUrl ( int limit, int offset )
	{
		/*String url = ServerManager.SEARCH_FOR_JOB_TYPE_URL;
		url += "jobid=" + jobid;
        url += "&userId=" + user.getId();
		url += "&limit=" + limit;
		url += "&sort=" + searchType.ordinal ();
		url += "&offset=" + offset;
        if(MyLocationManager.imHere != null) {
            url += "&latitude=" + MyLocationManager.imHere.getLatitude();
            url += "&longitude=" + MyLocationManager.imHere.getLongitude();
        }
		return url;*/

        return  makeSearchUrl(limit, offset);
	}

	private String makeFilterUrl ( int limit, int offset )
	{
		String url = ServerManager.FILTER_URL;
		url += "&height[from]=" + heightFrom;
		url += "&height[to]=" +  heightTo;
        url += "&userId=" + user.getId();
		url += "&age[from]=" + ageFrom;
		url += "&age[to]=" + ageTo;

		for ( String tempEthnicity : ethnicity.split ( "," ) )
		{
			if ( tempEthnicity != null && !tempEthnicity.trim ().isEmpty () ){
				//url += "&ethnicity[]=" + tempEthnicity.trim ();
                try {
                    url += "&ethnicity[]=" + URLEncoder.encode(tempEthnicity.trim (), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
			}
		}

		url += "&weight[from]=" + weightFrom;
		url += "&weight[to]=" + weightTo;

		for ( String tempBodyType : bodyType.split ( "," ) )
		{
			if ( tempBodyType != null && !tempBodyType.trim ().isEmpty () ){
				//url += "&bodyType[]=" + tempBodyType.trim ();
                try {
                    url += "&bodyType[]=" + URLEncoder.encode(tempBodyType.trim (), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
			}
		}

		url += "&price[from]=" + priceFrom;
		url += "&price[to]=" + priceTo;

		for ( String tempOrientation : sex.split ( "," ) )
		{
			if ( tempOrientation != null && !tempOrientation.trim ().isEmpty () )
			{
                try {
                    url += "&orientation[]=" + URLEncoder.encode(tempOrientation.trim (), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
		}

		if ( address != null )
		{
			url += "&location[latitude]=" + address.getLatitude ();
			url += "&location[longitude]=" + address.getLongitude();
		}

        if(MyLocationManager.imHere != null) {
            url += "&sortlatitude=" + MyLocationManager.imHere.getLatitude();
            url += "&sortlongitude=" + MyLocationManager.imHere.getLongitude();
        }

		url += "&sort=" + searchType.ordinal ();
		if ( !jobid.isEmpty () )
		{
			url += "&jobId=" + jobid;
		}

        if(!getTimeFrom().isEmpty() && !getTimeTo().isEmpty()){
            url += "&time[from]=" + getTimeFrom();
            url += "&time[to]=" + getTimeTo();
        }

        if(getDate() > 0){
            url += "&date=" + DateFormat.format("dd.MM.yyyy", new Date(getDate())).toString();
        }

        url += "&rating=" + (int)rating;

        url += "&limit=" + limit;
        url += "&offset=" + offset;

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

    public boolean getIsSearchByFilter(){
        return  isSearchByFilter;
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
        if(heightFrom > heightTo){
            this.heightFrom = heightTo;
            this.heightTo = heightFrom;
        }else{
            this.heightFrom = heightFrom;
            this.heightTo = heightTo;
        }
	}

    public int getHeightFrom(){
        return  heightFrom;
    }

    public int getHeightTo(){
        return  heightTo;
    }

	public void setAge ( int ageFrom, int ageTo )
	{
        if(ageFrom > ageTo){
            this.ageFrom = ageTo;
            this.ageTo = ageFrom;
        }else{
            this.ageFrom = ageFrom;
            this.ageTo = ageTo;
        }
	}

    public int getAgeFrom() {
        return ageFrom;
    }

    public int getAgeTo() {
        return ageTo;
    }

    public void setWeight ( int weightFrom, int weightTo )
	{
        if(weightFrom > weightTo){
            this.weightFrom = weightTo;
            this.weightTo = weightFrom;
        }else{
            this.weightFrom = weightFrom;
            this.weightTo = weightTo;
        }

	}

    public int getWeightFrom() {
        return weightFrom;
    }

    public int getWeightTo() {
        return weightTo;
    }

    public void setPrice ( int priceFrom, int priceTo )
	{
        if(priceFrom > priceTo){
            this.priceFrom = priceTo;
            this.priceTo = priceFrom;
        }else{
            this.priceFrom = priceFrom;
            this.priceTo = priceTo;
        }
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

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public long getDate() {
       return  date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
