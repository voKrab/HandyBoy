package com.vallverk.handyboy.model.job;

public enum JobCategory
{
	MUSCLE_BOY ( "Muscle Boy" ), HOUSE_BOY ( "House Boy" ), PARTY_BOY ( "Party Boy" ), PRETTY_BOY ( "Pretty Boy" );

	String name;

	JobCategory ( String name )
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

	public static JobCategory fromString ( String string )
	{
		for ( JobCategory category : JobCategory.values () )
		{
			if ( category.toString ().equals ( string ) )
			{
				return category;
			}
		}
		return null;
	}
}
