package com.vallverk.handyboy.model.job;

public enum TypeJobEnum
{
	PERSONAL_TRAINER ( "Personal Trainer" ), 
	POOL_BOY ( "Pool Boy" ),
	ERRAND_BOY ( "Errand Boy" ), 
	BARTENDER ( "Bartender" ),
	MASSEUR ( "Masseur" ), 
	HOUSEKEEPER ( "Housekeeper" ),
	YARD_WORK ( "Yard Work" ),
	SERVER_WAITER ( "Server/Waiter" ),
	GOGO_BOY ( "Go-Go Boy" ), 
	CAR_WASH ( "Car Wash" );
	
	String name;

	TypeJobEnum ( String name )
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

	public static TypeJobEnum fromString ( String string )
	{
		for ( TypeJobEnum category : TypeJobEnum.values () )
		{
			if ( category.toString ().equals ( string ) )
			{
				return category;
			}
		}
		return null;
	}
}
