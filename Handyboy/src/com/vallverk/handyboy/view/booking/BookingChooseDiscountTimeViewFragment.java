package com.vallverk.handyboy.view.booking;

import com.vallverk.handyboy.model.schedule.BookingTime;

public class BookingChooseDiscountTimeViewFragment extends BookingChooseTimeViewFragment
{
	@Override
	protected void init ()
	{
		bookingController = controller.getBookingController ();
		segmentStartItem = null;
		bookingTime = new BookingTime ();
		bookingTime.updateFromDiscount ( bookingController.getDiscount () );

		addCroosImageViewToItems ();
		addListeners ();
		updateComponents ();
		
		dateChooserView.offSelectors ();
		dateChooserView.setOnDateChangeListener ( null );
	}
}