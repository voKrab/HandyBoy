package com.vallverk.handyboy.model;

/**
 * Created by Олег on 26.01.2015.
 */
public enum AdditionalChargeStatusEnum
{
	REQUESTED ( "0" ), ACCEPTED ( "1" ), DECLINED ( "2" );

	String name;

	AdditionalChargeStatusEnum ( String name )
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

	public static AdditionalChargeStatusEnum fromString ( String string )
	{
		for ( AdditionalChargeStatusEnum category : AdditionalChargeStatusEnum.values () )
		{
			if ( category.toString ().equals ( string ) )
			{
				return category;
			}
		}
		return null;
	}
}
