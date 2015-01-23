package com.vallverk.handyboy.model.schedule;

import org.json.JSONObject;

public class WorkWindow
{
	protected WorkWindowPoint startPoint;
	protected WorkWindowPoint endPoint;
	
	public WorkWindow ( WorkWindowPoint startPoint, WorkWindowPoint endPoint )
	{
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	public WorkWindow ( JSONObject jsonObject ) throws Exception
	{
		startPoint = new WorkWindowPoint ( jsonObject.getString ( "start" ) );
		endPoint = new WorkWindowPoint ( jsonObject.getString ( "end" ) );
	}

	public JSONObject createJSONObject () throws Exception
	{
		JSONObject jsonObject = new JSONObject ();
		jsonObject.put ( "start", startPoint.toString () );
		jsonObject.put ( "end", endPoint.toString () );
		return jsonObject;
	}
}
