/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vallverk.handyboy.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.vallverk.handyboy.MainActivity.ApplicationAction;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver
{
	@Override
	public void onReceive ( Context context, Intent intent )
	{
		System.out.println ( "GcmBroadcastReceiver:onReceive" );
		ComponentName comp = new ComponentName ( context.getPackageName (), GcmIntentService.class.getName () );
		startWakefulService ( context, ( intent.setComponent ( comp ) ) );
		setResultCode ( Activity.RESULT_OK );
//		intent.setAction ( ApplicationAction.GCM_NOTIFICATION.toString () );
//		context.sendBroadcast ( intent );
//		context.getApplicationContext ().sendBroadcast ( intent );
	}
}
