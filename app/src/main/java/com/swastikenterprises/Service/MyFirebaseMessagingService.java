package com.swastikenterprises.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.swastikenterprises.Home.HomeActivity;
import com.swastikenterprises.R;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService
{
	public static final String FCM_PARAM = "picture";
	private static final String CHANNEL_NAME = "FCM";
	private static final String CHANNEL_DESC = "Firebase Cloud Messaging";
	private int numMessages = 0;

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage)
	{
		super.onMessageReceived(remoteMessage);
		Log.i("come", "come");


		//Log.d("FROM", remoteMessage.getFrom());
		if(remoteMessage.getData().size()>0 && remoteMessage.getData()!=null)
		{
			Map<String, String> data = remoteMessage.getData();
			sendNotification(data);
		}
	}

	private void sendNotification(Map<String, String> data)
	{
		Bundle bundle = new Bundle();
		bundle.putString("title", data.get("title"));
		bundle.putString("body", data.get("body"));
		bundle.putString("tag", "offer");

		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtras(bundle);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
				.setContentTitle(data.get("title"))
				.setContentText(data.get("body"))
				.setAutoCancel(true)
				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setContentIntent(pendingIntent)
//				.setContentInfo("Hello")
				.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo))
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.setSmallIcon( R.mipmap.ic_logo);

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(0, notificationBuilder.build());
	}
}