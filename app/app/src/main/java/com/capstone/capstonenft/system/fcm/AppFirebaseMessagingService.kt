package com.capstone.capstonenft.system.fcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.capstone.capstonenft.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class AppFirebaseMessagingService : FirebaseMessagingService() {


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        power()
        setNoti(
            remoteMessage.notification!!.title!!,
            remoteMessage.notification!!.body!!
        )
    }

    @SuppressLint("InvalidWakeLockTag")
    private fun power() {

        val pm =
            getSystemService(Context.POWER_SERVICE) as PowerManager

        var wakeLock =
            pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, "call"
            )

        wakeLock.acquire(500)
        wakeLock.release()

        Handler(Looper.getMainLooper()).postDelayed({
            wakeLock = pm.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE,
                "call"
            )
            wakeLock.acquire(500)
            wakeLock.release()
        }, 300)

    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setNoti(title: String?, content: String?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${packageName}-${getString(R.string.app_name)}"
            val channel = NotificationChannel(
                "NFT",
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "NFT"
            channel.setShowBadge(true)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)


            val builder = NotificationCompat.Builder(this, "NFT")  // 4
            builder.setSmallIcon(R.mipmap.ic_launcher)    // 5
            builder.setContentTitle(title)    // 6
            builder.setContentText(content)    // 7
            builder.setDefaults(NotificationCompat.DEFAULT_ALL);
            builder.priority = NotificationCompat.PRIORITY_MAX    // 8
            builder.setAutoCancel(true)   // 9

            notificationManager.notify(0, builder.build())    // 11

        } else {
            val builder = NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_MAX)

            with(NotificationManagerCompat.from(baseContext)) {
                notify(3, builder.build())
            }
        }
    }

}