package com.vallverk.handyboy.model;

public enum BookingStatusEnum
{
	PENDING ( "REQUESTED", 1 ), CONFIRMED ( "CONFIRMED", 3 ), PERFORMED ( "ACTIVE", 5 ), APPROVED ( "COMPLETED", 7 ), WAITING_FOR_REVIEW ( "WAITING FOR REVIEW", 6 ), CANCELED_BY_HB ( "CANCELED", 0 ), CANCELED_BY_CUSTOMER ( "CANCELED", 4 ), DECLINED ( "CANCELED", 2 );

	String name;
	int code;

	BookingStatusEnum ( String name, int code )
	{
		this.name = name;
		this.code = code;
	}

	public String toString ()
	{
		return name;
	}

	public int toInt ()
	{
		return code;
	}
	
	public void setName ( String name )
	{
		this.name = name;
	}

	public static BookingStatusEnum fromString ( String string )
	{
		for ( BookingStatusEnum category : BookingStatusEnum.values () )
		{
			if ( category.toString ().equals ( string ) )
			{
				return category;
			}
		}
		return null;
	}

	public static BookingStatusEnum fromInt ( int value )
	{
		for ( BookingStatusEnum bookingEnum : BookingStatusEnum.values () )
		{
			if ( bookingEnum.toInt () == value )
			{
				return bookingEnum;
			}
		}
		return null;
	}
}
