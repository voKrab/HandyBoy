package com.vallverk.handyboy.model;

public enum UserRole
{
	ACCEPTED ( "accepted" ), GUEST ( "guest" ), WAITING_FOR_REVIEW ( "waiting4review" );

	String name;

	UserRole ( String name )
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

	public static UserRole fromString ( String string )
	{
		for ( UserRole category : UserRole.values () )
		{
			if ( category.toString ().equals ( string ) )
			{
				return category;
			}
		}
		return null;
	}
}
