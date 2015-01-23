package com.vallverk.handyboy.model;

public enum UserStatus
{
	ACCEPTED ( "accepted" ), NEW_SERVICE ( "newService" ), NEW_CUSTOMER ( "newCustomer" ), NEW ( "new" );

	String name;

	UserStatus ( String name )
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

	public static UserStatus fromString ( String string )
	{
		for ( UserStatus category : UserStatus.values () )
		{
			if ( category.toString ().equals ( string ) )
			{
				return category;
			}
		}
		return null;
	}
}
