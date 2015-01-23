package com.vallverk.handyboy.model.job;

public enum TypejobLevel
{
	SOME_EXPERIENCE ( "Some Experience" ), EXPERIENCED ( "Experienced" ), VERY_EXPERIENCED ( "Very Experienced" );

	String name;

	TypejobLevel ( String name )
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

	public static TypejobLevel fromString ( String string )
	{
		for ( TypejobLevel category : TypejobLevel.values () )
		{
			if ( category.toString ().equals ( string ) )
			{
				return category;
			}
		}
		return null;
	}
}
