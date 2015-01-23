package com.vallverk.handyboy.view.account;

import java.io.Serializable;

public class HandyBoySettings implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public boolean isResivePushNotification;
	public boolean isResiveMailNotification;

	public HandyBoySettings ( boolean isResivePushNotification, boolean isResiveMailNotification )
	{
		this.isResivePushNotification = isResivePushNotification;
		this.isResiveMailNotification = isResiveMailNotification;
	}
}
