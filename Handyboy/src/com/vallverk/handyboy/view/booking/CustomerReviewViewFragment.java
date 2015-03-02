package com.vallverk.handyboy.view.booking;

import com.vallverk.handyboy.ViewStateController;
import com.vallverk.handyboy.model.BookingStatusEnum;
import com.vallverk.handyboy.model.api.APIManager;
import com.vallverk.handyboy.model.api.BookingDataManager;
import com.vallverk.handyboy.model.api.BookingDataObject;
import com.vallverk.handyboy.model.api.ReviewAPIObject;
import com.vallverk.handyboy.model.api.ReviewAPIObject.ReviewParams;

public class CustomerReviewViewFragment extends ServiceReviewViewFragment
{
	@Override
	protected ReviewAPIObject createReviewAPIObject ( BookingDataObject bookingDataObject, float rating, String review )
	{
		ReviewAPIObject reviewAPIObject = new ReviewAPIObject ();
		reviewAPIObject.putValue ( ReviewParams.OWNER_ID, bookingDataObject.getService ().getId () );
		reviewAPIObject.putValue ( ReviewParams.REVIEWER_ID, bookingDataObject.getCustomer ().getId () );
		reviewAPIObject.putValue ( ReviewParams.RATING, rating );
		reviewAPIObject.putValue ( ReviewParams.CONTENT, review );
		return reviewAPIObject;
	}

	@Override
	protected void changeStatus () throws Exception
	{
		BookingDataManager manager = BookingDataManager.getInstance ();
		BookingDataObject bookingDataObject = manager.getActiveBooking ();
		if ( manager.getActiveBookingStatus () == BookingStatusEnum.WAITING_FOR_REVIEW )
		{
			bookingDataObject.getBookingAPIObject ().changeStatus ( APIManager.getInstance ().getUser (), bookingDataObject.getService (), BookingStatusEnum.APPROVED );
		}
	}

	@Override
	protected void changeState ()
	{
        BookingStatusEnum bookingStatus = BookingDataManager.getInstance ().getActiveBookingStatus ();
        if ( bookingStatus == BookingStatusEnum.APPROVED || bookingStatus == BookingStatusEnum.WAITING_FOR_REVIEW )
        {
            controller.setState ( ViewStateController.VIEW_STATE.BOOK_AGAIN );
        }
	}
}