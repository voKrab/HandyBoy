package com.vallverk.handyboy.model.job;

public interface AddonId
{
	int EMPTY = 0;												// 0
	int SHIRTLESS_1 = EMPTY + 1;								// 1
	int SHIRTLESS_2 = SHIRTLESS_1 + 1;							// 2
	int TRUCK_SUV = SHIRTLESS_2 + 1;							// 3
	int EXTERIOR = TRUCK_SUV + 1;								// 4
	int WAX = EXTERIOR + 1;										// 5
	int TIRE_DRESSING = WAX + 1;								// 6
	//int DETAILING = TIRE_DRESSING + 1;						// 7
	int ATTIRE_1 = 8;								            // 8
	int SHIRTLESS_3 = ATTIRE_1 + 1;								// 9
	int BATHING_SUIT_1  = SHIRTLESS_3 + 1;						// 10
	int BOARD_SHORTS_1 = BATHING_SUIT_1 + 1;					// 11
	int TRUNKS_1 = BOARD_SHORTS_1 + 1;							// 12
	int SPEEDO_1 = TRUNKS_1 + 1;								// 13
	int SHIRTLESS_4 = SPEEDO_1 + 1;                             // 14
	int WINDOWS = SHIRTLESS_4 + 1;								// 15
	int LAUNDRY = WINDOWS + 1;									// 16
	int BRINGS_OWN_CLEANING_SUPPLIES = LAUNDRY + 1;				// 17
	int ECO_FRIENDLY = BRINGS_OWN_CLEANING_SUPPLIES + 1;		// 18
	int SHIRTLESS_5 = ECO_FRIENDLY + 1;							// 19
	int SHIRTLESS_6 = SHIRTLESS_5 + 1;							// 20
	int BATHING_SUIT_2 = SHIRTLESS_6 + 1;                       // 21
	int BOARD_SHORTS_2 = BATHING_SUIT_2 + 1;					// 22
	int TRUNKS_2 = BOARD_SHORTS_2 + 1;							// 23
	int SPEEDO_2 = TRUNKS_2 + 1;								// 24	
	int ATTIRE_2 = SPEEDO_2 + 1;								// 25
	int BOXERS = ATTIRE_2 + 1;									// 26
	int BRIEFS = BOXERS + 1;                                    // 27
	int JOCKSTRAP = BRIEFS + 1;                                 // 28
	int GYM = JOCKSTRAP + 1;                                    // 29
	//int SHIRTLESS_7 = GYM + 1;								// 30
    int OTHER_LOCATION = 31;//int OTHER_LOCATION = SHIRTLESS_7 + 1;						// 31
	//int SHIRTLESS_8 = OTHER_LOCATION + 1;						// 32
	//int YOUR_LOCATION = SHIRTLESS_8 + 1;						// 33
	//int SHIRTLESS_9 = YOUR_LOCATION + 1;						// 34
	//int SHIRTLESS_10 = SHIRTLESS_9 + 1;						// 35
	//masseur
	int WHAT_TYPE_OF_MASSAGE_DO_YOU_OFFER = 36;	                // 36
	int SWEDISH = WHAT_TYPE_OF_MASSAGE_DO_YOU_OFFER + 1;		// 37
	int DEEP_TISSUE = SWEDISH + 1;                              // 38
	int NON_THERAPEUTIC = DEEP_TISSUE + 1;                      // 39
	int COMBINATION = NON_THERAPEUTIC + 1;                      // 40
	int HOT_STONE = COMBINATION + 1;                            // 41
	int SPORTS = HOT_STONE + 1;                                 // 42
	int THAI = SPORTS + 1;
	int AROMATHERAPHY = THAI + 1;
	int REFLEXOLOGY = AROMATHERAPHY + 1;
	int TRIGGER_POINT = REFLEXOLOGY + 1;
	int FOOT = TRIGGER_POINT + 1;
	int REIKI = FOOT + 1;
	int ACUPRESSURE = REIKI + 1;
	int ASHIATSU = ACUPRESSURE + 1;
	int CHAIR = ASHIATSU + 1;
	int COUPLES = CHAIR + 1;
	int CRANIOSACRAL = COUPLES + 1;
	int ASALEN = CRANIOSACRAL + 1;
	int LYMPHATIC = ASALEN + 1;
	int MEDICAL = LYMPHATIC + 1;
	int MYOFASCIAL_RELEASE = MEDICAL + 1;
	int SHIATSU = MYOFASCIAL_RELEASE + 1;
	int STRUCTURAL_INTEGRATION = SHIATSU + 1;
	int OTHER = STRUCTURAL_INTEGRATION + 1;
}
