package com.example.es.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.es.R
import com.example.es.ui.MainActivity

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("AAA", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
//        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification != null) {
            createNotification(
                message.notification!!.title.toString(),
                message.notification!!.body.toString()
            )
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun createNotification(title: String, message: String) {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent
            .getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_elite_logo)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
//            .setContentIntent(pendingIntent)
        builder.setContent(getRemoteView(title, message))

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                    .apply {
                        enableVibration(true)
                        enableLights(true)
                    }
            manager.createNotificationChannel(channel)
        }

        manager.notify(0, builder.build())
    }

    private fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews("com.example.es", R.layout.notification)
        remoteView.setTextViewText(R.id.notification_title, title)
        remoteView.setTextViewText(R.id.notification_message, message)
        remoteView.setImageViewResource(R.id.notification_logo, R.drawable.ic_elite_logo)
        return remoteView
    }

    companion object {
        private const val channelId = "CHANNEL_ID"
        private const val channelName = "com.example.es"
    }
}