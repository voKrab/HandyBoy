package com.vallverk.handyboy.view.jobdescription;

import com.vallverk.handyboy.model.api.TypeJobServiceAPIObject;
import com.vallverk.handyboy.model.job.TypeJob;

public class ControllerFactory
{
	public static BaseController create ( TypeJobServiceAPIObject job, TypeJob typeJob )
	{
		switch ( typeJob.getEnumValue () )
		{
			case PERSONAL_TRAINER:
			{
				return new PersonalTrainerViewController ( job, typeJob );
			}
			case BARTENDER:
			{
				return new BartenderViewController ( job, typeJob );
			}
			case ERRAND_BOY:
			{
				return new ErrandBoyViewController ( job, typeJob );
			}
			case YARD_WORK:
			{
				return new GardenerViewController ( job, typeJob );
			}
			case SERVER_WAITER:
			{
				return new ServerViewController ( job, typeJob );
			}
			
			case GOGO_BOY:
			{
				return new GoGoBoyViewController( job, typeJob );
			}
			
			case HOUSEKEEPER:
			{
				return new HousekeeperViewController( job, typeJob );
			}
			
			case POOL_BOY:
			{
				return new PoolBoyViewController( job, typeJob );
			}
			
			case MASSEUR:
			{
				return new MasseurViewController( job, typeJob );
			}
			case CAR_WASH:
			{
				return new CarWashBoyViewController ( job, typeJob );
			}
			default:
			{
				return new PersonalTrainerViewController ( job, typeJob );
			}
		}
	}
}
