package com.vallverk.handyboy.view.controller;

import com.vallverk.handyboy.MainActivity;
import com.vallverk.handyboy.R;
import com.vallverk.handyboy.Tools;
import com.vallverk.handyboy.ViewStateController.VIEW_STATE;
import com.vallverk.handyboy.gcm.PushNotification;
import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject;
import com.vallverk.handyboy.model.api.AddonServiceAPIObject.AddonServiceAPIParams;
import com.vallverk.handyboy.model.api.AddressAPIObject;
import com.vallverk.handyboy.model.api.BookingAPIObject;
import com.vallverk.handyboy.model.api.BookingAPIObject.BookingAPIParams;
import com.vallverk.handyboy.model.api.CreditCardAPIObject;
import com.vallverk.handyboy.model.api.DiscountAPIObject;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject.TypeJobServiceParams;
import com.vallverk.handyboy.model.api.UserAPIObject;
import com.vallverk.handyboy.model.api.UserAPIObject.UserParams;
import com.vallverk.handyboy.model.job.AddonId;
import com.vallverk.handyboy.model.job.TypeJob;
import com.vallverk.handyboy.model.job.TypeJobEnum;
import com.vallverk.handyboy.model.schedule.BookingTime;
import com.vallverk.handyboy.pubnub.PubnubManager.ActionType;
import com.vallverk.handyboy.server.ServerManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookingController
{
	private MainActivity controller;
	private UserAPIObject handyBoy;
	private UserAPIObject customer;
	private AddressAPIObject addressAPIObject;
	private CreditCardAPIObject creditCardAPIObject;
	private TypeJobServiceAPIObject job;
	private VIEW_STATE currentState;
	private VIEW_STATE prevState;
	private float price;
	private List < AddonServiceAPIObject > addons;
	private BookingTime bookingTime;
	private DiscountAPIObject discountAPIObject;
	private String specialRequest;
	private TempData tempData;
	private LawnMovingType lawnMovingType;

	private int countRooms;
	private int countBathRooms;

	public void setLawnMovingType ( LawnMovingType lawnMovingType )
	{
		this.lawnMovingType = lawnMovingType;
	}

	public LawnMovingType getLawnMovingType ()
	{
		return lawnMovingType;
	}

	public int getCountBathRooms ()
	{
		return countBathRooms;
	}

	public void setCountBathRooms ( int countBathRooms )
	{
		this.countBathRooms = countBathRooms;
	}

	public int getCountRooms ()
	{
		return countRooms;
	}

	public void setCountRooms ( int countRooms )
	{
		this.countRooms = countRooms;
	}

	public float getHours ()
	{
		return bookingTime == null ? 0 : bookingTime.getHours ();
	}

	public float getSuggestionHours ()
	{
        float suggestionTime = 0;
        TypeJob typeJob = job.getTypeJob ();
        suggestionTime = typeJob.getMinTime () / 60f;
		return suggestionTime;
	}

	public enum LawnMovingType
	{
		SMALL, MEDIUM, LARGE, EXTRA_LARGE;
	}

	public class TempData
	{
		public boolean isOtherLocation;
	}

	public BookingController ( UserAPIObject handyBoy, TypeJobServiceAPIObject job, UserAPIObject user, DiscountAPIObject discountAPIObject, MainActivity controller )
	{
		this.handyBoy = handyBoy;
		this.customer = user;
		this.controller = controller;
		this.job = job;
		this.discountAPIObject = discountAPIObject;
		this.tempData = new TempData ();
		this.specialRequest = "";
	}

	public AddressAPIObject getAddress ()
	{
		return this.addressAPIObject;
	}

	public void setAddress ( AddressAPIObject addressAPIObject )
	{
		this.addressAPIObject = addressAPIObject;
	}

	public CreditCardAPIObject getCreditCard ()
	{
		return this.creditCardAPIObject;
	}

	public void setCreditCard ( CreditCardAPIObject creditCardAPIObject )
	{
		this.creditCardAPIObject = creditCardAPIObject;
	}

	public TypeJobServiceAPIObject getJob ()
	{
		return job;
	}

	public void setJob ( TypeJobServiceAPIObject job )
	{
		this.job = job;
	}

	public UserAPIObject getUser ()
	{
		return customer;
	}

	public void setUser ( UserAPIObject user )
	{
		this.customer = user;
	}

	public UserAPIObject getHandyBoy ()
	{
		return handyBoy;
	}

	public void setHandyBoy ( UserAPIObject handyBoy )
	{
		this.handyBoy = handyBoy;
	}

	public String getJobName ()
	{
		return job.getTypeJob ().getName ();
	}

	public String getJobInfo ()
	{
		return handyBoy.getShortName () + " $" + getPricePerHour () + "/h.";
	}

	public int getImageResource ()
	{
		switch ( job.getTypeJob ().getEnumValue () )
		{
			case BARTENDER:
			{
				return R.drawable.martini_book;
			}
			case CAR_WASH:
			{
				return R.drawable.car_book;
			}
			case ERRAND_BOY:
			{
				return R.drawable.man_book;
			}
			case GOGO_BOY:
			{
				return R.drawable.panties_book;
			}
			case HOUSEKEEPER:
			{
				return R.drawable.venik_book;
			}
			case MASSEUR:
			{
				return R.drawable.hand_book;
			}
			case PERSONAL_TRAINER:
			{
				return R.drawable.dumbbels_book;
			}
			case POOL_BOY:
			{
				return R.drawable.circle_book;
			}
			case SERVER_WAITER:
			{
				return R.drawable.foodcover_book;
			}
			case YARD_WORK:
			{
				return R.drawable.grass_book;
			}
		}
		return 0;
	}

	public void prevStep ()
	{
		if ( currentState == VIEW_STATE.BOOKING )
		{
			controller.cancelBooking ();
		} else
		{
			currentState = getPrevState ();
			controller.setState ( currentState );
		}
	}

	private VIEW_STATE getPrevState ()
	{
		switch ( currentState )
		{
			case CHOOSE_ADDONS:
			case CHOOSE_ADDRESS:
			case CREDIT_CARD:
			case BOOKING_CHOOSE_DISCOUNT_TIME:
			case BOOKING_CHOOSE_TIME:
			{
				return VIEW_STATE.BOOKING;
			}
			case ADD_ADDRESS:
			{
				return VIEW_STATE.CHOOSE_ADDRESS;
			}
		}
		return null;
	}

	public String getPriceString ()
	{
		price = getPrice ();
		return "$" + price + "0";
	}

	private float getPrice ()
	{
		if ( bookingTime == null )
		{
			return 0.0f;
		}
		float hours = bookingTime.getHours ();
		int addonPrice = getAddonsPrice ();
		int pricePerHour = getPricePerHour ();
		int discount = isHasDiscount () ? Integer.parseInt ( getDiscount ().getString ( DiscountAPIObject.DiscountParams.DISCOUNT ) ) : 0;
		price = hours * pricePerHour * ( 1 - discount / 100f ) + addonPrice;
		// price = hours * pricePerHour + addonPrice;
		return price;
	}

	private int getPricePerHour ()
	{
		return job.getInt ( TypeJobServiceParams.PRICE );
	}

	public void setState ( VIEW_STATE newState )
	{
		currentState = newState;
		controller.setState ( currentState );
	}

	public void setAddons ( List < AddonServiceAPIObject > addons )
	{
		this.addons = addons;
	}

	public List < AddonServiceAPIObject > getAddons ()
	{
		if ( addons == null )
		{
			addons = new ArrayList < AddonServiceAPIObject > ();
		}
		return addons;
	}

	public int getAddonsPrice ()
	{
		int price = 0;
		for ( AddonServiceAPIObject addon : addons )
		{
			price += addon.getInt ( AddonServiceAPIParams.PRICE );
		}
		return price;
	}

	public void setBookingTime ( BookingTime bookingTime )
	{
		this.bookingTime = bookingTime;
	}

	public BookingTime getBookingTime ()
	{
		return bookingTime;
	}

	public String getBookingComment ()
	{
		if ( isHouseKeeper () )
		{
			if ( getCountBathRooms () > 0 && getCountRooms () > 0 )
			{
				return "Count BathRooms " + getCountBathRooms () + " and " + "Count Rooms " + getCountRooms ();
			} else if ( getCountBathRooms () > 0 )
			{
				return "Count BathRooms " + getCountBathRooms ();
			} else if ( getCountRooms () > 0 )
			{
				return "Count Rooms " + getCountRooms ();
			}
		} else if ( isYardWorker () )
		{
			switch ( getLawnMovingType () )
			{
				case LARGE:
					return controller.getString ( R.string.lawm_moving_large );
				case MEDIUM:
					return controller.getString ( R.string.lawm_moving_medium );
				case SMALL:
					return controller.getString ( R.string.lawm_moving_small );
				case EXTRA_LARGE:
					return controller.getString ( R.string.lawm_moving_extra_large );
			}
		}
		return "";
	}

	public void booking () throws Exception
	{
		if ( addressAPIObject == null )
		{
			throw new Exception ( controller.getString ( R.string.choose_address ) );
		}
		if ( creditCardAPIObject == null )
		{
			throw new Exception ( controller.getString ( R.string.choose_credit_card ) );
		}
		if ( bookingTime == null )
		{
			throw new Exception ( controller.getString ( R.string.choose_time ) );
		}
		BookingAPIObject bookingAPIObject = new BookingAPIObject ();
		bookingAPIObject.putValue ( BookingAPIParams.SERVICE_ID, handyBoy.getString ( UserParams.SERVICE_ID ) );
		bookingAPIObject.putValue ( BookingAPIParams.CUSTOMER_ID, customer.getId () );
		bookingAPIObject.putValue ( BookingAPIParams.TYPE_JOB_SERVICE_ID, job.getId () );
		bookingAPIObject.putValue ( BookingAPIParams.ADDRESS_ID, addressAPIObject.getId () );
		bookingAPIObject.putValue ( BookingAPIParams.CREDIT_CARD_ID, creditCardAPIObject.getId () );
		bookingAPIObject.putValue ( BookingAPIParams.TIME, bookingTime.createTimeJSONArray ().toString () );
		bookingAPIObject.putValue ( BookingAPIParams.DATE, Tools.toSimpleString ( bookingTime.getDate () ) );
		bookingAPIObject.putValue ( BookingAPIParams.TOTAL_PRICE, price );
		bookingAPIObject.putValue ( BookingAPIParams.TOTAL_HOURS, bookingTime.getHours () );
		bookingAPIObject.putValue ( BookingAPIParams.STATUS, "" + BookingStatusEnum.PENDING.toInt () );
		bookingAPIObject.putValue ( BookingAPIParams.SPECIAL_REQUEST, specialRequest );
		bookingAPIObject.putValue ( BookingAPIParams.COMMENT, getBookingComment () );

		JSONObject jsonObject = bookingAPIObject.createJSON ( null );
		if ( addons != null )
		{
			jsonObject.accumulate ( "addons", createAddonsIdArray () );
		}
		String request = ServerManager.postRequest ( ServerManager.CREATE_BOOKING, jsonObject );
		JSONObject requestJSON = new JSONObject ( request );
		bookingAPIObject.setId ( requestJSON.getString ( "id" ) );

		JSONObject pushData = PushNotification.createJSON ( controller.getString ( R.string.you_get_new_job_request ), ActionType.BOOKING );
		// PushNotification.send ( handyBoy, pushData );
	}

	private JSONArray createAddonsIdArray ()
	{
		JSONArray addonIdsJsonArray = new JSONArray ();
		List < String > listAddonIds = new ArrayList < String > ();
		for ( AddonServiceAPIObject addon : addons )
		{
			listAddonIds.add ( addon.getId () );
			addonIdsJsonArray.put ( addon.getId () );
		}
		return addonIdsJsonArray;
	}

	public boolean isHasDiscount ()
	{
		return discountAPIObject != null;
	}

	public DiscountAPIObject getDiscount ()
	{
		return discountAPIObject;
	}

	public List < AddressAPIObject > loadAddresses () throws Exception
	{
		tempData.isOtherLocation = false;
		List < AddressAPIObject > listAddressAPIObjects = new ArrayList < AddressAPIObject > ();
		APIManager apiManager = controller.getAPIManager ();
		if ( isPersonalTrainer () )
		{
			List < AddonServiceAPIObject > allAddons = APIManager.getInstance ().loadList ( ServerManager.ADDONS_SERVICE_GET.replace ( "serviceId=1", "serviceId=" + handyBoy.getString ( UserParams.SERVICE_ID ) ), AddonServiceAPIObject.class );
			AddonServiceAPIObject otherLocation = getAddon ( allAddons, AddonId.OTHER_LOCATION );
			if ( otherLocation != null )
			{
				tempData.isOtherLocation = true;
				listAddressAPIObjects = apiManager.loadList ( ServerManager.GET_ADDRESSES + customer.getId (), AddressAPIObject.class );
			}
			AddonServiceAPIObject gym = getAddon ( allAddons, AddonId.GYM );
			if ( gym != null )
			{
				JSONObject data = gym.getData ();
				String url = ServerManager.GET_ADDRESS.replace ( "objectId=", "objectId=" + data.getString ( "id" ) );
				AddressAPIObject gymAddress = ( AddressAPIObject ) apiManager.getAPIObject ( data.getString ( "id" ), AddressAPIObject.class, url );
				listAddressAPIObjects.add ( gymAddress );
			}
		} else
		{
			listAddressAPIObjects = apiManager.loadList ( ServerManager.GET_ADDRESSES + customer.getId (), AddressAPIObject.class );
		}
		return listAddressAPIObjects;
	}

	public boolean isPersonalTrainer ()
	{
		return job.getTypeJob ().getEnumValue () == TypeJobEnum.PERSONAL_TRAINER;
	}

	public boolean isHouseKeeper ()
	{
		return job.getTypeJob ().getEnumValue () == TypeJobEnum.HOUSEKEEPER;
	}

	public boolean isYardWorker ()
	{
		return job.getTypeJob ().getEnumValue () == TypeJobEnum.YARD_WORK;
	}

	private AddonServiceAPIObject getAddon ( List < AddonServiceAPIObject > allAddons, int addonId )
	{
		for ( AddonServiceAPIObject addon : allAddons )
		{
			if ( addon.getString ( AddonServiceAPIParams.JOB_ADDONS_ID ).equals ( "" + addonId ) )
			{
				return addon;
			}
		}
		return null;
	}

	public TempData getTempData ()
	{
		return tempData;
	}

	public void setSpecialRequest ( String specialRequest )
	{
		this.specialRequest = specialRequest;
	}

	public String getSpecialRequest ()
	{
		return specialRequest;
	}
}
