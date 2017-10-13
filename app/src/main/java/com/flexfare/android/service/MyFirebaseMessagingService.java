//package com.flexfare.android.service;
//
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//
//import com.flexfare.android.social.FlexFare;
//import com.flexfare.android.R;
//import com.flexfare.android.newsfeed.chat.ChatActivity;
//import com.flexfare.android.newsfeed.model.PushNotification;
//import com.flexfare.android.utils.Constants;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//
//import org.greenrobot.eventbus.EventBus;
//
///**
// * Created by kodenerd on 9/27/17.
// */
//
//public class MyFirebaseMessagingService extends FirebaseMessagingService{
//
//    private static final String TAG = "MyFirebaseMsgService";
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//
//            String title = remoteMessage.getData().get("title");
//            String message = remoteMessage.getData().get("text");
//            String username = remoteMessage.getData().get("username");
//            String uid = remoteMessage.getData().get("uid");
//            String fcmToken = remoteMessage.getData().get("fcm_token");
//
//            // Don't show notification if chat activity is open.
//            if (!FlexFare.isChatActivityOpen()) {
//                sendNotification(title,
//                        message,
//                        username,
//                        uid,
//                        fcmToken);
//            } else {
//                EventBus.getDefault().post(new PushNotification(title,
//                        message,
//                        username,
//                        uid,
//                        fcmToken));
//            }
//        }
//    }
//
//    /**
//     * Create and show a simple notification containing the received FCM message.
//     */
//    private void sendNotification(String title,
//                                  String message,
//                                  String receiver,
//                                  String receiverUid,
//                                  String firebaseToken) {
//        Intent intent = new Intent(this, ChatActivity.class);
//        intent.putExtra(Constants.ARG_RECEIVER, receiver);
//        intent.putExtra(Constants.ARG_RECEIVER_UID, receiverUid);
//        intent.putExtra(Constants.ARG_FIREBASE_TOKEN, firebaseToken);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0, notificationBuilder.build());
//    }
//}
