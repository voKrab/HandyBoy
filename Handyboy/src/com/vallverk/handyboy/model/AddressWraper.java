package com.vallverk.handyboy.model;

import android.location.Address;

public class AddressWraper
{
	private Address address;

	public AddressWraper ( Address address )
	{
		this.address = address;
	}
	
	public Address getAddress ()
	{
		return address;
	}

	public void setAddress ( Address address )
	{
		this.address = address;
	}
	
	public String toString ()
	{
		return address.getCountryName () + " - " + address.getLocality ();
	}

	public boolean isValid ()
	{
		return address.getCountryName () != null && address.getLocality () != null;
	}
}
