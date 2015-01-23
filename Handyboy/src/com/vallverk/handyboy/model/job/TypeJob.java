package com.vallverk.handyboy.model.job;

import java.io.Serializable;

import org.json.JSONObject;

public class TypeJob implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JobCategory category;
	private String name;
	private String description;
	private int minCost;
	private int maxCost;
	private int minTime;
	private int recommendMinCost;
	private int recommendMaxCost;
	private int recommendCost;
	private int id;

	public JobCategory getCategory ()
	{
		return category;
	}

	public void setCategory ( JobCategory category )
	{
		this.category = category;
	}

	public String getName ()
	{
		return name;
	}

	public void setName ( String name )
	{
		this.name = name;
	}

	public String getDescription ()
	{
		return description;
	}

	public void setDescription ( String description )
	{
		this.description = description;
	}

	public int getMinCost ()
	{
		return minCost;
	}

	public void setMinCost ( int minCost )
	{
		this.minCost = minCost;
	}

	public int getMaxCost ()
	{
		return maxCost;
	}

	public void setMaxCost ( int maxCost )
	{
		this.maxCost = maxCost;
	}

	public int getMinTime ()
	{
		return minTime;
	}

	public void setMinTime ( int minTime )
	{
		this.minTime = minTime;
	}

	public int getId ()
	{
		return id;
	}

	public void setId ( int id )
	{
		this.id = id;
	}

	public TypeJob ( int id, JobCategory category, String name, String description, int minCost, int maxCost, int minTime, int recommendMinCost, int recommendMaxCost, int recommendCost )
	{
		this.category = category;
		this.name = name;
		this.description = description;
		this.minCost = minCost;
		this.maxCost = maxCost;
		this.minTime = minTime;
		this.id = id;
		this.recommendCost = recommendCost;
		this.recommendMaxCost = recommendMaxCost;
		this.recommendMinCost = recommendMinCost;
	}

	public TypeJob ( JSONObject item ) throws Exception
	{
		this ( item.getInt ( "id" ), JobCategory.fromString ( item.getString ( "nameJob" ) ), item.getString ( "name" ), item.getString ( "description" ), item.getInt ( "minCost" ), item.getInt ( "maxCost" ), item.getInt ( "minTime" ), item.getInt ( "recommendMinCost" ), item.getInt ( "recommendMaxCost" ), item.getInt ( "recommendCost" ) );
	}

	public TypeJobEnum getEnumValue ()
	{
		return TypeJobEnum.fromString ( name );
	}

	public boolean isRequiredApprove ()
	{
		TypeJobEnum jobTypeEnum = getEnumValue ();
		return jobTypeEnum == TypeJobEnum.PERSONAL_TRAINER || jobTypeEnum == TypeJobEnum.MASSEUR;
	}

	public int getPriceDelta ()
	{
		return maxCost - minCost;
	}

	public int getMediumPrice ()
	{
		return ( maxCost + minCost ) / 2;
	}

	public String toString ()
	{
		return name + " " + id;
	}

	public int getRecommendCost ()
	{
		return recommendCost;
	}

	public void setRecommendCost ( int recommendCost )
	{
		this.recommendCost = recommendCost;
	}

	public int getRecommendMaxCost ()
	{
		return recommendMaxCost;
	}

	public void setRecommendMaxCost ( int recommendMaxCost )
	{
		this.recommendMaxCost = recommendMaxCost;
	}

	public int getRecommendMinCost ()
	{
		return recommendMinCost;
	}

	public void setRecommendMinCost ( int recommendMinCost )
	{
		this.recommendMinCost = recommendMinCost;
	}

}
