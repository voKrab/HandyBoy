package com.vallverk.handyboy.model.api;

import android.graphics.Color;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.gcm.PushNotification;
import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.CommunicationManager;
import com.vallverk.handyboy.model.CommunicationManager.CommunicationAction;
import com.vallverk.handyboy.pubnub.NotificationWithDataAction;
import com.vallverk.handyboy.pubnub.PubnubManager;
import com.vallverk.handyboy.pubnub.PubnubManager.ActionType;
import com.vallverk.handyboy.server.ServerManager;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankAccountAPIObject extends APIObject implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public enum BankAccountParams
    {
        FIRST_NAME( "firstName" ),
        LAST_NAME( "lastName" ),
        ACCOUNT_NUMBER( "accountNumber" ),
        ROUTING_NUMBER( "routingNumber" ),
        SSN( "ssn" ),
        STREET_ADDRESS( "streetAddress" ),
        LOCALITY( "locality" ),
        REGION( "region" ),
        POSTAL_CODE( "postalCode" );

        String name;

        BankAccountParams ( String name )
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

        public static BankAccountParams fromString ( String string )
        {
            for ( BankAccountParams category : BankAccountParams.values () )
            {
                if ( category.toString ().equals ( string ) )
                {
                    return category;
                }
            }
            return null;
        }
    }

    public BankAccountAPIObject () throws Exception
    {
    }

    public BankAccountAPIObject ( JSONObject jsonItem ) throws Exception
    {
        update ( jsonItem );
    }

    @Override
    public Object[] getParams ()
    {
        return BankAccountParams.values ();
    }
}
