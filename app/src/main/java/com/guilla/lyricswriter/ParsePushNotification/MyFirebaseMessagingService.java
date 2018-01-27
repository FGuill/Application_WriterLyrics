package com.guilla.lyricswriter.ParsePushNotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.activity.ProfilActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            String followerTitle=null;
            String followerBody=null;
            if (remoteMessage.getData().get("type")!=null){
                if (remoteMessage.getData().get("type").equalsIgnoreCase("notifInvit")) {
                    String commentFrom= remoteMessage.getData().get("commentFrom");
                    String commentid= remoteMessage.getData().get("commentid");
                    String projectName= remoteMessage.getData().get("projectname");

                    followerTitle= "CollabNote";
                    followerBody= commentFrom+" "+getString(R.string.notification_answeredtomessage)+" "+projectName;
                    NotificationNewDisasterAdded(followerTitle,followerBody);
                }
                if (remoteMessage.getData().get("type").equalsIgnoreCase("notifInvitAccecpted")) {
                    String commentFrom= remoteMessage.getData().get("commentFrom");
                    String commentid= remoteMessage.getData().get("commentid");
                    String projectName= remoteMessage.getData().get("projectname");

                    followerTitle= "CollabNote";
                    followerBody= commentFrom+" "+getString(R.string.notification_inivitationaccepted)+" "+projectName;
                    NotificationNewDisasterAdded(followerTitle,followerBody);
                }

            }

            if (remoteMessage.getData().get("followerbody")!=null){
                followerTitle= "Application LifeTips";
                String user = remoteMessage.getData().get("iduser").toString();
                String bodynotif=user+" "+getString(R.string.notification_follower);

                NotificationNewFollower(followerTitle,bodynotif);
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Message Notification Tag: " + remoteMessage.getNotification().getTag());
            Log.d(TAG, "Message Notification Sound: " + remoteMessage.getNotification().getSound());
            Log.d(TAG, "Message Notification Icon: " + remoteMessage.getNotification().getIcon());
            Log.d(TAG, "Message Notification Icon: " + remoteMessage.getNotification().getClickAction());
            String messageText = remoteMessage.getNotification().getTitle();
            if (messageText == null)
                messageText = remoteMessage.getNotification().getBody();

            sendNotification(remoteMessage.getNotification().getBody().toString());

        }else {
            Log.d("Notification", "Message Notification is null: ");

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, ProfilActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        long[] nullify = {0,0,0};
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("ShareTips")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(nullify)
        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void NotificationNewDisasterAdded(String title,String messageBody) {
        Intent intent = new Intent(this, ProfilActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notification", 1);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 5, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_logonotification)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void NotificationNewFollower(String title,String messageBody) {
        Intent intent = new Intent(this, ProfilActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notification", 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 5, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void NotificationInvitationProject(String title,String messageBody) {
        Intent intent = new Intent(this, ProfilActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notification", 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 5, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}