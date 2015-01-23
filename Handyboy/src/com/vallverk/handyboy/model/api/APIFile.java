package com.vallverk.handyboy.model.api;

import java.io.Serializable;

import org.json.JSONObject;

import com.vallverk.handyboy.server.ServerManager;

public class APIFile implements Serializable
{
	private static final long serialVersionUID = 1L;
	private byte[] data;
	private String url;
	
	public APIFile ( byte[] data )
	{
		this.data = data;
	}
	
	public String save () throws Exception
	{
		String urlString = "";
		String responseString = ServerManager.multipartPostRequest ( ServerManager.UPLOAD_FILE, data );
		JSONObject responseJSON = new JSONObject ( responseString );
		urlString = responseJSON.getString ( "url" );
		setUrl ( ServerManager.PROTOCOL + ServerManager.HOST + urlString );
		return urlString;
	}
	
	public byte[] load ()
	{
		return data;
	}

	public String getUrl ()
	{
		return url;
	}

	public void setUrl ( String url )
	{
		this.url = url;
	}
}
