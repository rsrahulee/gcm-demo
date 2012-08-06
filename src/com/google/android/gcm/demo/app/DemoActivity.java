/*
 * Copyright 2012 Google Inc.
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
package com.google.android.gcm.demo.app;

import static com.google.android.gcm.demo.app.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.google.android.gcm.demo.app.CommonUtilities.EXTRA_MESSAGE;
import static com.google.android.gcm.demo.app.CommonUtilities.SENDER_ID;
import static com.google.android.gcm.demo.app.CommonUtilities.SERVER_URL;

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Main UI for the demo app.
 */
public class DemoActivity extends Activity {

	TextView mDisplay;
	AsyncTask<Void, Void, Void> mRegisterTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("DemoActivity----------", "onCreate");
		// checkNotNull(SERVER_URL, "SERVER_URL");
		// checkNotNull(SENDER_ID, "SENDER_ID");
		// // Make sure the device has the proper dependencies.
		// GCMRegistrar.checkDevice(this);
		// // Make sure the manifest was properly set - comment out this line
		// // while developing the app, then uncomment it when it's ready.
		// GCMRegistrar.checkManifest(this);
		// setContentView(R.layout.main);
		// mDisplay = (TextView) findViewById(R.id.display);
		// registerReceiver(mHandleMessageReceiver,
		// new IntentFilter(DISPLAY_MESSAGE_ACTION));
		// final String regId = GCMRegistrar.getRegistrationId(this);
		// if (regId.equals("")) {
		// // Automatically registers application on startup.
		// GCMRegistrar.register(this, SENDER_ID);
		// } else {
		// // Device is already registered on GCM, check server.
		// if (GCMRegistrar.isRegisteredOnServer(this)) {
		// // Skips registration.
		// mDisplay.append(getString(R.string.already_registered) + "\n");
		// } else {
		// // Try to register again, but not in the UI thread.
		// // It's also necessary to cancel the thread onDestroy(),
		// // hence the use of AsyncTask instead of a raw thread.
		// final Context context = this;
		// mRegisterTask = new AsyncTask<Void, Void, Void>() {
		//
		// @Override
		// protected Void doInBackground(Void... params) {
		// boolean registered =
		// ServerUtilities.register(context, regId);
		// // At this point all attempts to register with the app
		// // server failed, so we need to unregister the device
		// // from GCM - the app will try to register again when
		// // it is restarted. Note that GCM will send an
		// // unregistered callback upon completion, but
		// // GCMIntentService.onUnregistered() will ignore it.
		// if (!registered) {
		// GCMRegistrar.unregister(context);
		// }
		// return null;
		// }
		//
		// @Override
		// protected void onPostExecute(Void result) {
		// mRegisterTask = null;
		// }
		//
		// };
		// mRegisterTask.execute(null, null, null);
		// }
		// }
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("DemoActivity----------", "onStart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("DemoActivity----------", "onResume");

		// checkNotNull(SERVER_URL, "SERVER_URL");
		checkNotNull(SENDER_ID, "SENDER_ID");
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);
		setContentView(R.layout.main);
		mDisplay = (TextView) findViewById(R.id.display);
		registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			// Automatically registers application on startup.
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM, check server.
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
				mDisplay.append(getString(R.string.already_registered) + "\n");
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						boolean registered = ServerUtilities.register(context, regId);
						// At this point all attempts to register with the app
						// server failed, so we need to unregister the device
						// from GCM - the app will try to register again when
						// it is restarted. Note that GCM will send an
						// unregistered callback upon completion, but
						// GCMIntentService.onUnregistered() will ignore it.
						if (!registered) {
							GCMRegistrar.unregister(context);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("DemoActivity----------", "onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("DemoActivity----------", "onStop");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/*
		 * Typically, an application registers automatically, so options below
		 * are disabled. Uncomment them if you want to manually register or
		 * unregister the device (you will also need to uncomment the equivalent
		 * options on options_menu.xml).
		 */
		/*
		 * case R.id.options_register: GCMRegistrar.register(this, SENDER_ID);
		 * return true; case R.id.options_unregister:
		 * GCMRegistrar.unregister(this); return true;
		 */
		case R.id.options_clear:
			mDisplay.setText(null);
			return true;
		case R.id.options_exit:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		unregisterReceiver(mHandleMessageReceiver);
		GCMRegistrar.onDestroy(this);
		super.onDestroy();
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config, name));
		}
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			if (newMessage.contains("ACCOUNT_MISSING")) {
				Log.i("ACCOUNT_MISSING--------------", "ACCOUNT_MISSING");
				ShowMessageBox();
			} else if (newMessage.contains("PHONE_REGISTRATION_ERROR")) {
				Toast.makeText(DemoActivity.this, "Registration to GCM failed", Toast.LENGTH_SHORT).show();
			} else if (newMessage.contains("SERVICE_NOT_AVAILABLE")) {
				Toast.makeText(DemoActivity.this, "GCM Server not available", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(DemoActivity.this, "Successfully registered to GCM", Toast.LENGTH_SHORT).show();
			}
			mDisplay.append(newMessage + "\n");
		}
	};

	public void ShowMessageBox() {
		AlertDialog exitAlert = new AlertDialog.Builder(this).create();
		exitAlert.setTitle("Login to Google Account");
		exitAlert.setMessage("This app needs login to google account");

		exitAlert.setButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent();
				intent.setClassName("com.google.android.gsf", "com.google.android.gsf.login.AccountIntroActivity");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplication().startActivity(intent);
			}
		});
		exitAlert.setButton2("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		exitAlert.show();
	}
}