package com.vallverk.handyboy.model;

public class VerificationCode
{
	public static long ALIVE_TIME = 1000 * 60 * 60; // one hour
	
	private String code;
	private long sendedTime;
	
	public VerificationCode ( String code, long sendedTime )
	{
		this.code = code;
		this.sendedTime = sendedTime;
	}
	
	public String getCode ()
	{
		return code;
	}
	public void setCode ( String code )
	{
		this.code = code;
	}
	public long getSendedTime ()
	{
		return sendedTime;
	}
	public void setSendedTime ( long sendedTime )
	{
		this.sendedTime = sendedTime;
	}

	public boolean check ( String codeToCheck )
	{
		long now = System.currentTimeMillis ();
		if ( now - sendedTime > ALIVE_TIME )
		{
			return false;
		}
		return code.equals ( codeToCheck );
	}
}
