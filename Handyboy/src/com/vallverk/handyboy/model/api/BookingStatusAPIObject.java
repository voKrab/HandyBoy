package com.vallverk.handyboy.model.api;

import org.json.JSONObject;

import com.vallverk.handyboy.model.api.BookingStatusAPIObject.BookingStateEnum;

public class BookingStatusAPIObject extends APIObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String isActive;

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public boolean checkIsActive(){
        if(getIsActive() != null){
            if(getIsActive().equals("YES")) return true;
            else  return false;
        }
        return false;
    }

    public enum BookingStateEnum
	{
		NORMAL ( "0" ), 
		WAITING_FOR_REVIEW ( "1" );
		
		String name;

		BookingStateEnum ( String name )
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

		public static BookingStateEnum fromString ( String string )
		{
			for ( BookingStateEnum category : BookingStateEnum.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}
	}
	
	public enum BookingStatusParams
	{
		USER_ID ( "userId" ), 
		BOOKING_ID ( "bookingId" ), 
		STATE ( "status" );
		
		String name;

		BookingStatusParams ( String name )
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

		public static BookingStatusParams fromString ( String string )
		{
			for ( BookingStatusParams category : BookingStatusParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}
	}

	public BookingStatusAPIObject () throws Exception
	{
	}

	public BookingStatusAPIObject ( JSONObject jsonItem ) throws Exception
	{
		update ( jsonItem );
	}

	@Override
	public Object[] getParams ()
	{
		return BookingStatusParams.values ();
	}

	public BookingStateEnum getState ()
	{
		return BookingStateEnum.fromString ( getString ( BookingStatusParams.STATE ) );
	}
}
