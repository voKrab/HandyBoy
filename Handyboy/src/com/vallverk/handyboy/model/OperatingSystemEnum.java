package com.vallverk.handyboy.model;

public enum OperatingSystemEnum
{
	ANDROID ( "android" ), IOS ( "ios" );
	
	String name;

	OperatingSystemEnum ( String name )
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

	public static OperatingSystemEnum fromString ( String string )
	{
		for ( OperatingSystemEnum category : OperatingSystemEnum.values () )
		{
			if ( category.toString ().equals ( string ) )
			{
				return category;
			}
		}
		return null;
	}
}
