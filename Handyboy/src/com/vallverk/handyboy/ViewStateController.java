package com.vallverk.handyboy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;

import com.vallverk.handyboy.view.AvailableNowViewFragment;
import com.vallverk.handyboy.view.BOTViewFragment;
import com.vallverk.handyboy.view.BankAccountViewFragment;
import com.vallverk.handyboy.view.ChatViewFragment;
import com.vallverk.handyboy.view.ChatsViewFragment;
import com.vallverk.handyboy.view.ChooseCategoryViewFragment;
import com.vallverk.handyboy.view.ChooseJobTypeViewFragment;
import com.vallverk.handyboy.view.ChooseUserTypeViewFragment;
import com.vallverk.handyboy.view.ContractViewFragment;
import com.vallverk.handyboy.view.DashboardViewFragment;
import com.vallverk.handyboy.view.FavoritesViewFragment;
import com.vallverk.handyboy.view.ForgotPasswordViewFragment;
import com.vallverk.handyboy.view.HandyBoyViewFragment;
import com.vallverk.handyboy.view.HelpViewFragment;
import com.vallverk.handyboy.view.LoginViewFragment;
import com.vallverk.handyboy.view.PrivacyPolicyViewFragment;
import com.vallverk.handyboy.view.SplashViewFragment;
import com.vallverk.handyboy.view.TermsViewFragment;
import com.vallverk.handyboy.view.YourMoneyViewFragment;
import com.vallverk.handyboy.view.account.BlockListViewFragment;
import com.vallverk.handyboy.view.account.ChangePhoneNumberViewFragment;
import com.vallverk.handyboy.view.account.EditAccountViewFragment;
import com.vallverk.handyboy.view.account.TransactionHistoryViewFragment;
import com.vallverk.handyboy.view.address.AddAddressViewFragment;
import com.vallverk.handyboy.view.address.AddressViewFragment;
import com.vallverk.handyboy.view.base.BaseFragment;
import com.vallverk.handyboy.view.booking.ActiveGigViewFragment;
import com.vallverk.handyboy.view.booking.AddChargesViewFragment;
import com.vallverk.handyboy.view.booking.BookAgainViewFragment;
import com.vallverk.handyboy.view.booking.BookAnotherViewFragment;
import com.vallverk.handyboy.view.booking.BookingChooseDiscountTimeViewFragment;
import com.vallverk.handyboy.view.booking.BookingChooseTimeViewFragment;
import com.vallverk.handyboy.view.booking.BookingViewFragment;
import com.vallverk.handyboy.view.booking.ChargesViewFragment;
import com.vallverk.handyboy.view.booking.CheckoutViewFragment;
import com.vallverk.handyboy.view.booking.ChooseAddonsViewFragment;
import com.vallverk.handyboy.view.booking.CreditCardViewFragment;
import com.vallverk.handyboy.view.booking.CustomerReviewViewFragment;
import com.vallverk.handyboy.view.booking.GigConfirmViewFragment;
import com.vallverk.handyboy.view.booking.GigCustomerViewFragment;
import com.vallverk.handyboy.view.booking.GigServiceViewFragment;
import com.vallverk.handyboy.view.booking.GigsViewFragment;
import com.vallverk.handyboy.view.booking.LeaveViewFragment;
import com.vallverk.handyboy.view.booking.NextGigViewFragment;
import com.vallverk.handyboy.view.booking.ProblemClientViewFragment;
import com.vallverk.handyboy.view.booking.ProblemServiceViewFragment;
import com.vallverk.handyboy.view.booking.ReviewsClientViewFragment;
import com.vallverk.handyboy.view.booking.ServiceReviewViewFragment;
import com.vallverk.handyboy.view.booking.ServicedViewFragment;
import com.vallverk.handyboy.view.feed.FeedViewFragment;
import com.vallverk.handyboy.view.feed.FilterViewFragment;
import com.vallverk.handyboy.view.jobdescription.JobDescriptionViewFragment;
import com.vallverk.handyboy.view.profile.CustomerEditProfileViewFragment;
import com.vallverk.handyboy.view.profile.GalleryViewFragment;
import com.vallverk.handyboy.view.profile.ServiceEditProfileViewFragment;
import com.vallverk.handyboy.view.registration.FacebookRegistrationViewFragment;
import com.vallverk.handyboy.view.registration.RegistrationCustomerBIOViewFragment;
import com.vallverk.handyboy.view.registration.RegistrationLicenseViewFragment;
import com.vallverk.handyboy.view.registration.RegistrationServiceBIOViewFragment;
import com.vallverk.handyboy.view.registration.RegistrationServiceSelphieViewFragment;
import com.vallverk.handyboy.view.registration.RegistrationServiceTermsViewFragment;
import com.vallverk.handyboy.view.registration.RegistrationServiceVideoViewFragment;
import com.vallverk.handyboy.view.registration.RegistrationViewFragment;
import com.vallverk.handyboy.view.registration.WaitingForValidationViewFragment;
import com.vallverk.handyboy.view.schedule.CustomScheduleViewFragment;
import com.vallverk.handyboy.view.schedule.WeeklyScheduleViewFragment;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleg Barkov
 * 
 *         Controller for all data/events related with view states, fragments
 *         and navigation scheme of application
 */
public class ViewStateController implements Serializable
{
	/**
	 * All application view state
	 */

	//REGISTRATION_CUSTOMER_BIO,
	public enum VIEW_STATE
	{
		CHOOSE_ADDRESS, GALLERY, ADD_ADDRESS, REGISTRATION_SERVICE_LICENSE, REGISTRATION_SERVICE_VIDEO, REGISTRATION_SERVICE_TERMS, REGISTRATION_SERVICE_SELPHIE, EXIT, SPLASH, LOGIN, REGISTRATION, FACEBOOK_REGISTRATION, HOME, FORGOT_PASSWORD, CHOOSE_USER_TYPE, PROFILE, SERVICE_EDIT_PROFILE, CUSTOMER_EDIT_PROFILE, REGISTRATION_SERVICE_BIO,  FAVORITES, CHATS, CALENDAR, CHOOSE_CATEGORY, FEED, DASHBOARD, CHOOSE_JOB_TYPE, HANDY_BOY_PAGE, CHAT, TERMS, CONTRACT, PRIVACY_POLICY, AVAILABLE_NOW, JOB_DESCRIPTIONS, WEEKLY_SCHEDULE, YOUR_MONEY, ACCOUNT, TRANSACTION_HISTORY, BLOCK_LIST, CUSTOM_SCHEDULE, FILTER, HELP, CHANGE_PHONE, WAITING_FOR_VALIDATION, BOOKING, CHOOSE_ADDONS, CREDIT_CARD, BOOKING_CHOOSE_TIME, BOOKING_CHECKOUT, GIGS, GIG_SERVICE, GIG_CUSTOMER, NEXT_GIG, ACTIVE_GIG, PROBLEM_CUSTOMER, PROBLEM_SERVICE, BOOK_ANOTHER, SERVICED, BOOKING_CHOOSE_DISCOUNT_TIME, ADD_CHARGES, CHARGES, SERVICE_REVIEW, LEAVE_TIP, CUSTOMER_REVIEW, REVIEWS_CLIENT, BANK_ACCOUNT, GIG_COMPLETED, BOOK_AGAIN
	}

	private static final long serialVersionUID = 1L;

	/**
	 * Current view state
	 */
	private VIEW_STATE state;
	/**
	 * Main controller
	 */
	private transient MainActivity controller;
	/**
	 * Current Fragment
	 */
	private transient BaseFragment currentFragment;
	private FragmentManager manager;
	/**
	 * map of created fragments
	 */
	private Map < VIEW_STATE, BaseFragment > fragments;
	/**
	 * Previous view state
	 */
	private VIEW_STATE prevState;

	public ViewStateController ( MainActivity controller )
	{
		this.state = VIEW_STATE.SPLASH;
		this.controller = controller;
		manager = controller.getSupportFragmentManager ();
		fragments = new HashMap < VIEW_STATE, BaseFragment > ();
	}

	/**
	 * Change view state
	 */
	public void setState ( VIEW_STATE newState )
	{
		setState ( newState, null );
	}

	private void commitState ( FragmentTransaction fragmentTransaction )
	{
		fragmentTransaction.replace ( R.id.container, currentFragment );
		fragmentTransaction.commitAllowingStateLoss ();
	}

	/**
	 * Change view state with params
	 */
	public void setState ( VIEW_STATE newState, Bundle bundle )
	{
		// System.out.println ( prevState + " " + newState + " " + state );
		prevState = VIEW_STATE.valueOf ( state.toString () );
		updateTabBar ( newState );
		switch ( newState )
		{
			case LEAVE_TIP:
			{
				currentFragment = new LeaveViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				break;
			}

			case CUSTOMER_REVIEW:
			case SERVICE_REVIEW:
			{
				currentFragment = newState == VIEW_STATE.SERVICE_REVIEW ? new ServiceReviewViewFragment () : new CustomerReviewViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				controller.setSwipeEnabled ( false );
				break;
			}

			case BOOKING_CHOOSE_DISCOUNT_TIME:
			{
				currentFragment = new BookingChooseDiscountTimeViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				break;
			}

			case BOOKING_CHOOSE_TIME:
			{
				currentFragment = new BookingChooseTimeViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				break;
			}

			case CHOOSE_ADDONS:
			{
				currentFragment = new ChooseAddonsViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				break;
			}

			case BOOKING:
			{
				currentFragment = new BookingViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();

				if ( prevState == VIEW_STATE.CHOOSE_ADDRESS || prevState == VIEW_STATE.CHOOSE_ADDONS || prevState == VIEW_STATE.CREDIT_CARD || prevState == VIEW_STATE.BOOKING_CHOOSE_TIME || prevState == VIEW_STATE.BOOKING_CHOOSE_DISCOUNT_TIME )
				{
					fragmentTransaction.setCustomAnimations ( R.anim.left_to_center, R.anim.center_to_right );
				} else
				{
					fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );
				}

				commitState ( fragmentTransaction );
				break;
			}

			case WAITING_FOR_VALIDATION:
			{
				currentFragment = new WaitingForValidationViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

				commitState ( fragmentTransaction );
				break;
			}

			case CUSTOM_SCHEDULE:
			{
				currentFragment = new CustomScheduleViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				break;
			}

			case YOUR_MONEY:
			{
                currentFragment = getFragment ( YourMoneyViewFragment.class, newState );
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				break;
			}
            case BANK_ACCOUNT:
            {
                currentFragment = new BankAccountViewFragment();
                FragmentTransaction fragmentTransaction = manager.beginTransaction ();
                fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

                commitState ( fragmentTransaction );
                break;
            }


			case WEEKLY_SCHEDULE:
			{
				currentFragment = new WeeklyScheduleViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				break;
			}

			case JOB_DESCRIPTIONS:
			{
				currentFragment = new JobDescriptionViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				break;
			}

			case PRIVACY_POLICY:
			{
				currentFragment = new PrivacyPolicyViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				break;
			}

			case TERMS:
			{
				currentFragment = new TermsViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				break;
			}
            case CONTRACT:
            {
                currentFragment = new ContractViewFragment();
                FragmentTransaction fragmentTransaction = manager.beginTransaction ();
                fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

                commitState ( fragmentTransaction );
                break;
            }


			case REGISTRATION_SERVICE_LICENSE:
			{
				currentFragment = new RegistrationLicenseViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				if ( prevState == VIEW_STATE.REGISTRATION_SERVICE_SELPHIE )
				{
					fragmentTransaction.setCustomAnimations ( R.anim.left_to_center, R.anim.center_to_right );
				} else
				{
					fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );
				}

				commitState ( fragmentTransaction );
				break;
			}

			case REGISTRATION_SERVICE_VIDEO:
			{
				currentFragment = new RegistrationServiceVideoViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				/*
				 * if ( prevState == VIEW_STATE.REGISTRATION_CREDIT_CARD ) {
				 * fragmentTransaction.setCustomAnimations (
				 * R.anim.left_to_center, R.anim.center_to_right ); } else {
				 */
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );
				// }

				commitState ( fragmentTransaction );
				break;
			}
			case REGISTRATION_SERVICE_TERMS:
			{
				currentFragment = new RegistrationServiceTermsViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				if ( prevState == VIEW_STATE.REGISTRATION_SERVICE_VIDEO )
				{
					fragmentTransaction.setCustomAnimations ( R.anim.left_to_center, R.anim.center_to_right );
				} else
				{
					fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );
				}

				commitState ( fragmentTransaction );
				break;
			}
			case REGISTRATION_SERVICE_SELPHIE:
			{
				currentFragment = new RegistrationServiceSelphieViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				if ( prevState == VIEW_STATE.REGISTRATION_SERVICE_TERMS )
				{
					fragmentTransaction.setCustomAnimations ( R.anim.left_to_center, R.anim.center_to_right );
				} else
				{
					fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );
				}

				commitState ( fragmentTransaction );
				break;
			}
			case AVAILABLE_NOW:
			{
				currentFragment = new AvailableNowViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				break;
			}

			case DASHBOARD:
			{
				currentFragment = getFragment ( DashboardViewFragment.class, newState );
                //currentFragment = new BOTViewFragment();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				if ( controller.isMenuOpen () )
				{
					fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );
				} else
				{
					if ( prevState == VIEW_STATE.AVAILABLE_NOW || prevState == VIEW_STATE.JOB_DESCRIPTIONS || prevState == VIEW_STATE.WEEKLY_SCHEDULE || prevState == VIEW_STATE.YOUR_MONEY || prevState == VIEW_STATE.CUSTOM_SCHEDULE )
					{
						fragmentTransaction.setCustomAnimations ( R.anim.left_to_center, R.anim.center_to_right );
					} else
					{
						fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );
					}
					// fragmentTransaction.setCustomAnimations (
					// R.anim.up_to_center, R.anim.center_to_down );
				}

				commitState ( fragmentTransaction );
				if ( controller.isMenuOpen () )
				{
					controller.closeMenu ();
				}
				break;
			}
			case CHATS:
			{
				currentFragment = getFragment ( ChatsViewFragment.class, newState );
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				if ( prevState == VIEW_STATE.CHAT )
				{
					fragmentTransaction.setCustomAnimations ( R.anim.left_to_center, R.anim.center_to_right );
				} else
				{
					fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );
				}

				commitState ( fragmentTransaction );
				break;
			}

			case CHAT:
			{
				currentFragment = getFragment ( ChatViewFragment.class, newState );
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				break;
			}

			case HANDY_BOY_PAGE:
			{
				currentFragment = getFragment ( HandyBoyViewFragment.class, newState );
				//currentFragment = new HandyBoyViewFragment();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				if ( prevState == VIEW_STATE.CHAT || prevState == VIEW_STATE.BOOKING )
				{
					fragmentTransaction.setCustomAnimations ( R.anim.left_to_center, R.anim.center_to_right );
				} else
				{
					fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );
				}

				commitState ( fragmentTransaction );
				break;
			}

			case FEED:
			{
				currentFragment = getFragment ( FeedViewFragment.class, newState );
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				// fragmentTransaction.setCustomAnimations (
				// R.anim.card_flip_left_in, R.anim.card_flip_left_out );
				// fragmentTransaction.setCustomAnimations (
				// android.R.anim.fade_in, android.R.anim.fade_out );
				if ( prevState == VIEW_STATE.HANDY_BOY_PAGE )
				{
					fragmentTransaction.setCustomAnimations ( R.anim.left_to_center, R.anim.center_to_right );
				} else
				{
					fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );
				}

				// fragmentTransaction.commitAllowingStateLoss ();
				commitState ( fragmentTransaction );
				if ( controller.isMenuOpen () )
				{
					controller.closeMenu ();
				}
				break;
			}

			case CHOOSE_JOB_TYPE:
			{
				currentFragment = new ChooseJobTypeViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				break;
			}

			case CHOOSE_CATEGORY:
			{
				currentFragment = new ChooseCategoryViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

				commitState ( fragmentTransaction );
				break;
			}

			/*case REGISTRATION_CUSTOMER_BIO:
			{
				currentFragment = new RegistrationCustomerBIOViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();

				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );
				// }

				commitState ( fragmentTransaction );
				break;
			}*/

			/*
			 * case REGISTRATION_CREDIT_CARD: { currentFragment = new
			 * RegistrationCreditCardViewFragment (); FragmentTransaction
			 * fragmentTransaction = manager.beginTransaction ();
			 * fragmentTransaction.setCustomAnimations ( R.anim.right_to_center,
			 * R.anim.center_to_left ); fragmentTransaction.replace (
			 * R.id.container, currentFragment ); commitState (
			 * fragmentTransaction ); break; }
			 */

			/*
			 * case REGISTRATION_JOB_DESCRIPTION: { currentFragment = new
			 * RegistrationJobDescriptionViewFragment (); FragmentTransaction
			 * fragmentTransaction = manager.beginTransaction (); if ( prevState
			 * == VIEW_STATE.REGISTRATION_SERVICE_TERMS ) {
			 * fragmentTransaction.setCustomAnimations ( R.anim.left_to_center,
			 * R.anim.center_to_right ); } else {
			 * fragmentTransaction.setCustomAnimations ( R.anim.right_to_center,
			 * R.anim.center_to_left ); } fragmentTransaction.replace (
			 * R.id.container, currentFragment ); commitState (
			 * fragmentTransaction ); break; }
			 */

			case REGISTRATION_SERVICE_BIO:
			{
				currentFragment = new RegistrationServiceBIOViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				if ( prevState == VIEW_STATE.REGISTRATION_SERVICE_SELPHIE || prevState == VIEW_STATE.REGISTRATION_SERVICE_LICENSE )
				{
					fragmentTransaction.setCustomAnimations ( R.anim.left_to_center, R.anim.center_to_right );
				} else
				{
					fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );
				}

				commitState ( fragmentTransaction );
				break;
			}

			case PROFILE:
			{
				currentFragment = new RegistrationServiceBIOViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				break;
			}

			case SERVICE_EDIT_PROFILE:
			{
				currentFragment = new ServiceEditProfileViewFragment ();
				// currentFragment = new RegistrationServiceBIOViewFragment ();
				// currentFragment = new WaitingForValidationViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				controller.closeMenu ();
				break;
			}

			case CUSTOMER_EDIT_PROFILE:
			{
				currentFragment = new CustomerEditProfileViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				controller.closeMenu ();
				break;
			}

			case CHOOSE_USER_TYPE:
			{
				currentFragment = new ChooseUserTypeViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				if ( prevState == VIEW_STATE.REGISTRATION_SERVICE_BIO )
				{
					fragmentTransaction.setCustomAnimations ( R.anim.left_to_center, R.anim.center_to_right );
				} else
				{
					fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );
				}

				commitState ( fragmentTransaction );
				break;
			}

			case FORGOT_PASSWORD:
			{
				currentFragment = new ForgotPasswordViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				break;
			}

			case FILTER:
			{
				currentFragment = new FilterViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				break;
			}

			case HELP:
			{
				currentFragment = new HelpViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				break;
			}

			case FAVORITES:
			{
				currentFragment = getFragment ( FavoritesViewFragment.class, newState );
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				// fragmentTransaction.setCustomAnimations (
				// R.anim.card_flip_left_in, R.anim.card_flip_left_out );
				fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

				commitState ( fragmentTransaction );
				break;
			}

			case BLOCK_LIST:
			{
				currentFragment = getFragment ( BlockListViewFragment.class, newState );
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				if ( prevState == VIEW_STATE.ACCOUNT )
				{
					fragmentTransaction.setCustomAnimations ( R.anim.left_to_center, R.anim.center_to_right );
				} else
				{
					fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );
				}

				commitState ( fragmentTransaction );
				break;
			}

			/*
			 * case CALENDAR: case HOME: { currentFragment = getFragment (
			 * HomeViewFragment.class, newState ); FragmentTransaction
			 * fragmentTransaction = manager.beginTransaction (); //
			 * fragmentTransaction.setCustomAnimations ( //
			 * R.anim.card_flip_left_in, R.anim.card_flip_left_out );
			 * fragmentTransaction.setCustomAnimations ( android.R.anim.fade_in,
			 * android.R.anim.fade_out ); fragmentTransaction.replace (
			 * R.id.container, currentFragment ); commitState (
			 * fragmentTransaction ); break; }
			 */

			case FACEBOOK_REGISTRATION:
			{
				currentFragment = new FacebookRegistrationViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				if ( prevState == VIEW_STATE.TERMS || prevState == VIEW_STATE.PRIVACY_POLICY )
				{
					fragmentTransaction.setCustomAnimations ( R.anim.left_to_center, R.anim.center_to_right );
				} else
				{
					fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );
				}

				commitState ( fragmentTransaction );
				break;
			}

			case REGISTRATION:
			{
				// currentFragment = new RegistrationViewFragment ();
				currentFragment = getFragment ( RegistrationViewFragment.class, newState );
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				if ( prevState == VIEW_STATE.TERMS || prevState == VIEW_STATE.PRIVACY_POLICY )
				{
					fragmentTransaction.setCustomAnimations ( R.anim.left_to_center, R.anim.center_to_right );
				} else
				{
					fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );
				}

				commitState ( fragmentTransaction );
				break;
			}

			case LOGIN:
			{
				currentFragment = getFragment ( LoginViewFragment.class, newState );
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				if ( prevState == VIEW_STATE.REGISTRATION || prevState == VIEW_STATE.FORGOT_PASSWORD )
				{
					fragmentTransaction.setCustomAnimations ( R.anim.left_to_center, R.anim.center_to_right );
				} else
				{
					fragmentTransaction.setCustomAnimations ( android.R.anim.fade_in, android.R.anim.fade_out );
				}
                controller.setSwipeEnabled ( false );
				commitState ( fragmentTransaction );
				break;
			}

			case SPLASH:
			{
				currentFragment = new SplashViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();

				commitState ( fragmentTransaction );
				controller.setSwipeEnabled ( false );
				break;
			}

			case EXIT:
			{
				Intent intent = new Intent ( Intent.ACTION_MAIN );
				intent.addCategory ( Intent.CATEGORY_HOME );
				intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );
				controller.startActivity ( intent );
				break;
			}
			case ACCOUNT:
			{
				currentFragment = new EditAccountViewFragment ();
				// currentFragment = new AddressViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				controller.closeMenu ();
				break;
			}

			case CHANGE_PHONE:
			{
				currentFragment = new ChangePhoneNumberViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				controller.closeMenu ();
				break;
			}

			case TRANSACTION_HISTORY:
			{
				currentFragment = new TransactionHistoryViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				controller.closeMenu ();
				break;
			}

			case CHOOSE_ADDRESS:
			{
				currentFragment = new AddressViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				if ( prevState == VIEW_STATE.ADD_ADDRESS )
				{
					fragmentTransaction.setCustomAnimations ( R.anim.left_to_center, R.anim.center_to_right );
				} else
				{
					fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );
				}

				commitState ( fragmentTransaction );
				controller.closeMenu ();
				break;
			}

			case ADD_ADDRESS:
			{
				currentFragment = new AddAddressViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				controller.closeMenu ();
				break;
			}
			case CREDIT_CARD:
			{
				currentFragment = new CreditCardViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.right_to_center, R.anim.center_to_left );

				commitState ( fragmentTransaction );
				controller.closeMenu ();
				break;
			}

			case BOOKING_CHECKOUT:
			{
				currentFragment = new CheckoutViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

				commitState ( fragmentTransaction );
				break;
			}


            case BOOK_AGAIN:
            {
                currentFragment = new BookAgainViewFragment();
                FragmentTransaction fragmentTransaction = manager.beginTransaction ();
                fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

                commitState ( fragmentTransaction );
                break;
            }

			case GIGS:
			{
				currentFragment = getFragment ( GigsViewFragment.class, newState );
				// currentFragment = new GigViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

				commitState ( fragmentTransaction );
				break;
			}

			case GIG_SERVICE:
			{
				currentFragment = new GigServiceViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

				commitState ( fragmentTransaction );
				break;
			}

			case GIG_CUSTOMER:
			{
				currentFragment = new GigCustomerViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

				commitState ( fragmentTransaction );
				break;
			}

			case NEXT_GIG:
			{
				currentFragment = new NextGigViewFragment ();
				// currentFragment = new GigViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

				commitState ( fragmentTransaction );
				break;
			}

			case ACTIVE_GIG:
			{
				currentFragment = new ActiveGigViewFragment ();
				// currentFragment = new GigViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

				commitState ( fragmentTransaction );
				break;
			}

			case PROBLEM_CUSTOMER:
			{
				currentFragment = new ProblemClientViewFragment ();
				// currentFragment = new GigViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

				commitState ( fragmentTransaction );
				break;
			}

			case PROBLEM_SERVICE:
			{
				currentFragment = new ProblemServiceViewFragment ();
				// currentFragment = new GigViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

				commitState ( fragmentTransaction );
				break;
			}

			case BOOK_ANOTHER:
			{
				currentFragment = new BookAnotherViewFragment ();
				// currentFragment = new GigViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

				commitState ( fragmentTransaction );
				break;
			}

			case SERVICED:
			{
				currentFragment = new ServicedViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

				commitState ( fragmentTransaction );
				controller.setSwipeEnabled ( false );
				controller.closeMenu ();
				break;
			}

			case ADD_CHARGES:
			{
				currentFragment = new AddChargesViewFragment ();
				// currentFragment = new GigViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

				commitState ( fragmentTransaction );
				break;
			}

			case CHARGES:
			{
				currentFragment = new ChargesViewFragment ();
				// currentFragment = new GigViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

				commitState ( fragmentTransaction );
				break;
			}

			case REVIEWS_CLIENT:
			{
				currentFragment = getFragment ( ReviewsClientViewFragment.class, newState );
				//currentFragment = new ReviewsClientViewFragment ();
				// currentFragment = new GigViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

				commitState ( fragmentTransaction );
				break;
			}

			case GALLERY:
			{
				currentFragment = new GalleryViewFragment ();
				// currentFragment = new GigViewFragment ();
				FragmentTransaction fragmentTransaction = manager.beginTransaction ();
				fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

				commitState ( fragmentTransaction );
				break;
			}

            case GIG_COMPLETED:
            {
                currentFragment = new GigConfirmViewFragment();
                // currentFragment = new GigViewFragment ();
                FragmentTransaction fragmentTransaction = manager.beginTransaction ();
                fragmentTransaction.setCustomAnimations ( R.anim.up_to_center, R.anim.center_to_down );

                commitState ( fragmentTransaction );
                break;
            }

			default:
				break;
		}
		if ( newState != VIEW_STATE.EXIT )
		{
			this.state = VIEW_STATE.valueOf ( newState.toString () );
			if ( currentFragment != null )
			{
				fragments.put ( state, currentFragment );
			}
		}
	}

	public void updateTabBar ()
	{
		updateTabBar ( state );
	}

	/**
	 * Update tabbar dependence of the state
	 */
	public void updateTabBar ( VIEW_STATE newState )
	{
		VIEW_STATE[] statesWithTabBar = new VIEW_STATE[] { VIEW_STATE.CALENDAR, VIEW_STATE.HOME, VIEW_STATE.FAVORITES, VIEW_STATE.CHOOSE_CATEGORY, VIEW_STATE.DASHBOARD, VIEW_STATE.FEED, VIEW_STATE.CHATS, VIEW_STATE.CHOOSE_JOB_TYPE, VIEW_STATE.HANDY_BOY_PAGE, VIEW_STATE.CHAT, VIEW_STATE.JOB_DESCRIPTIONS, VIEW_STATE.AVAILABLE_NOW, VIEW_STATE.WEEKLY_SCHEDULE, VIEW_STATE.CUSTOM_SCHEDULE, VIEW_STATE.YOUR_MONEY, VIEW_STATE.BOOKING_CHECKOUT, VIEW_STATE.GIGS, VIEW_STATE.GIG_SERVICE, VIEW_STATE.GIG_CUSTOMER, VIEW_STATE.NEXT_GIG, VIEW_STATE.ACTIVE_GIG, VIEW_STATE.PROBLEM_CUSTOMER, VIEW_STATE.PROBLEM_SERVICE, VIEW_STATE.BOOK_ANOTHER, VIEW_STATE.ADD_CHARGES, VIEW_STATE.CHARGES };

		boolean isWithTabBar = Arrays.asList ( statesWithTabBar ).contains ( newState );
		if ( isWithTabBar )
		{
			controller.showTabBar ();
		} else
		{
			controller.hideTabBar ();
		}
	}

	public void onBackPressed ()
	{
        if ( currentFragment != null )
        {
            currentFragment.onBackPressed ();
        }
		if ( isRegistrationControllerState () )
		{
			controller.getRegistrationController ().prevStep ();
		}
		if ( controller.isBookingState () )
		{
			controller.getBookingController ().prevStep ();
		} else
		{
			VIEW_STATE newState = getBackState ( state );
			if ( newState != null )
			{
				setState ( newState );
			}
		}
	}

	private boolean isRegistrationControllerState ()
	{
		return /*state == VIEW_STATE.REGISTRATION_CUSTOMER_BIO ||*/ state == VIEW_STATE.REGISTRATION_SERVICE_BIO || state == VIEW_STATE.REGISTRATION_SERVICE_SELPHIE || state == VIEW_STATE.REGISTRATION_SERVICE_TERMS || state == VIEW_STATE.REGISTRATION_SERVICE_VIDEO || state == VIEW_STATE.REGISTRATION_SERVICE_LICENSE;
	}

	public boolean onTouchEvent ( MotionEvent event )
	{
		if ( currentFragment != null )
		{
			return currentFragment.onTouchEvent ( event );
		}
		return false;
	}

	public void onNewIntent ( Intent intent )
	{
		if ( currentFragment != null )
		{
			currentFragment.onNewIntent ( intent );
		}
	}

	private VIEW_STATE getBackState ( VIEW_STATE currentState )
	{
		switch ( currentState )
		{
			case FAVORITES:
			case CALENDAR:
			case CHATS:
			case FEED:
			case HOME:
			case WAITING_FOR_VALIDATION:
			case LOGIN:
			{
				return VIEW_STATE.EXIT;
			}

			case FORGOT_PASSWORD:
			case FACEBOOK_REGISTRATION:
			case REGISTRATION:
			{
				return VIEW_STATE.LOGIN;
			}
			case REGISTRATION_SERVICE_BIO:
			{
				return VIEW_STATE.CHOOSE_USER_TYPE;
			}

			case CUSTOMER_EDIT_PROFILE:
			case HANDY_BOY_PAGE:
			{
				return VIEW_STATE.FEED;
			}

            case YOUR_MONEY:
			case SERVICE_EDIT_PROFILE:
			case AVAILABLE_NOW:
			{
				return VIEW_STATE.DASHBOARD;
			}

            case BOOK_AGAIN:
            case GIG_COMPLETED:
            case CHARGES:
			case ACTIVE_GIG:
			case GIG_CUSTOMER:
			case NEXT_GIG:
			case GIG_SERVICE:
			{
				return VIEW_STATE.GIGS;
			}
            case CREDIT_CARD:
            case CONTRACT:
            case PRIVACY_POLICY:
            case TERMS:
            case BANK_ACCOUNT:
			case ADD_ADDRESS:
			case GALLERY:
			case LEAVE_TIP:
			case PROBLEM_CUSTOMER:
			case PROBLEM_SERVICE:
			case BOOK_ANOTHER:
			case CHOOSE_ADDRESS:
			case ACCOUNT:
			case CHANGE_PHONE:
			case HELP:
			case FILTER:
			case WEEKLY_SCHEDULE:
			case CUSTOM_SCHEDULE:
			case JOB_DESCRIPTIONS:
			case CHAT:
			case TRANSACTION_HISTORY:
			case REVIEWS_CLIENT:
			case BLOCK_LIST:
            case ADD_CHARGES:
			{
				return prevState;
			}
			default:
				return null;
		}
	}

	private BaseFragment getFragment ( Class fragmentClass, VIEW_STATE newState )
	{
		Fragment fragment = fragments.get ( newState );
		if ( fragment != null )
		{
			return ( BaseFragment ) fragment;
		}
		try
		{
			fragment = ( Fragment ) fragmentClass.newInstance ();
		} catch ( InstantiationException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace ();
		} catch ( IllegalAccessException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
		return ( BaseFragment ) fragment;
	}

	public FragmentActivity getView ()
	{
		return controller;
	}

	public void setView ( MainActivity view )
	{
		this.controller = view;
	}

	public Fragment getCurrentFragment ()
	{
		return currentFragment;
	}

	public void clearBackStateStack ()
	{
		for ( Fragment fragment : manager.getFragments () )
		{
			if ( fragment != null )
			{
				manager.beginTransaction ().remove ( fragment ).commit ();
			}
		}
		fragments.clear ();
	}

	public VIEW_STATE getState ()
	{
		return state;
	}
}
