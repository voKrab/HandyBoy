package com.vallverk.handyboy.model.api;

import com.vallverk.handyboy.model.AdditionalChargeStatusEnum;

import org.json.JSONObject;

import java.io.Serializable;

public class AdditionalChargesAPIObject extends APIObject implements Serializable
{
	private static final long serialVersionUID = 1L;

    public boolean isRequested ()
    {
        return AdditionalChargeStatusEnum.fromString ( getString ( AdditionalChargesParams.STATUS ) ) == AdditionalChargeStatusEnum.REQUESTED;
    }

    public enum AdditionalChargesParams
	{
		BOOKING_ID ( "bookingId" ), STATUS ( "status" ), REASON ( "reason" ), PRICE ( "price" );

		String name;

		AdditionalChargesParams ( String name )
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

		public static AdditionalChargesParams fromString ( String string )
		{
			for ( AdditionalChargesParams category : AdditionalChargesParams.values () )
			{
				if ( category.toString ().equals ( string ) )
				{
					return category;
				}
			}
			return null;
		}
	}

    public AdditionalChargesAPIObject ()
    {

    }

    public AdditionalChargesAPIObject ( String reason, String price, String bookingId, AdditionalChargeStatusEnum status )
    {
        putValue ( AdditionalChargesParams.REASON, reason );
        putValue ( AdditionalChargesParams.PRICE, price );
        putValue ( AdditionalChargesParams.BOOKING_ID, bookingId );
        putValue ( AdditionalChargesParams.STATUS, status.toString () );
    }

	public AdditionalChargesAPIObject ( JSONObject jsonObject ) throws Exception
	{
		update ( jsonObject );
	}

	@Override
	public Object[] getParams ()
	{
		return AdditionalChargesParams.values ();
	}
}
