package com.vallverk.handyboy.model.api;

import com.vallverk.handyboy.server.ServerManager;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class APIObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	protected String id;
	private Map < String, Object > values;

	public APIObject ()
	{
		values = new HashMap < String, Object > ();
	}

	public Map < String, Object > getValues ()
	{
		return values;
	}

	public void setValues ( Map < String, Object > values )
	{
		this.values = values;
	}

	protected String insert ( String uri, KeyValuePair[] additionalParams ) throws Exception
	{
		JSONObject jsonObject = createJSON ( additionalParams );
		String request = ServerManager.postRequest ( uri, jsonObject );
		return request;
	}

	public String update ( String uri ) throws Exception
	{
		return update ( uri, false );
	}

    public String update ( String uri, boolean withoutId ) throws Exception
    {
        JSONObject jsonObject = createJSON ( null );
        if ( withoutId )
        {
            jsonObject.remove ( "id" );
        }
        String request = ServerManager.postRequest ( uri, jsonObject );
        return request;
    }

	public void fetch () throws Exception
	{

	}

	public void fetch ( String uri ) throws Exception
	{
		String responseString = ServerManager.getRequest ( uri );
		JSONObject responseJSON = new JSONObject ( responseString );
		String error = responseJSON.getString ( "parameters" );
		if ( !error.isEmpty () )
		{
			throw new Exception ( error );
		}
		String values = responseJSON.getString ( "object" );
		JSONObject jsonItem = new JSONObject ( values );
		update ( jsonItem );
	}

	public void update ( JSONObject jsonItem ) throws Exception
	{
		for ( Object key : getParams () )
		{
			String paramName = key.toString ();
			if ( !jsonItem.isNull ( paramName ) )
			{
				putValue ( paramName, jsonItem.get ( paramName ) );
			}
		}
		if ( !jsonItem.isNull ( "id" ) )
		{
			setId ( jsonItem.getString ( "id" ) );
		}
	}

	public JSONObject createJSON ( KeyValuePair[] additionalParams ) throws Exception
	{
		JSONObject jsonObject = new JSONObject ();
		for ( String key : values.keySet () )
		{
			Object value = values.get ( key );
			jsonObject.accumulate ( key, value );
		}
		if ( jsonObject.isNull ( "id" ) )
		{
			jsonObject.accumulate ( "id", id );
		}
		if ( additionalParams != null )
		{
			for ( KeyValuePair pair : additionalParams )
			{
				jsonObject.accumulate ( pair.key, pair.value );
			}
		}
		return jsonObject;
	}

	public void putValue ( Object key, Object value )
	{
		values.put ( key.toString (), value );
	}

	protected void removeValue ( Object key )
	{
		values.remove ( key );
	}

	public Object getValue ( Object key )
	{
		return values.get ( key.toString () );
	}

	public String getString ( Object key )
	{
		return ( String ) getValue ( key );
	}

	public int getInt ( Object key )
	{
		return Integer.parseInt ( getString ( key ) );
	}

	public String getId ()
	{
		return id;
	}

	public void setId ( String id )
	{
		this.id = id;
	}

	public String getDeleteURI ()
	{
		return "Delete URI is not defined";
	}

	public abstract Object[] getParams ();

	public boolean equals ( Object obj )
	{
		if ( !getClass ().getSimpleName ().equals ( obj.getClass ().getSimpleName () ) )
		{
			return false;
		}
		APIObject apiObject = ( APIObject ) obj;
		return id.equals ( apiObject.getId () );
	}
}
