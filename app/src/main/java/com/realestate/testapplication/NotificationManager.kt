package com.realestate.testapplication


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


object NotificationManager {
    var pendingIntent: PendingIntent? = null
    var notifManager: NotificationManager? = null
    var builder: NotificationCompat.Builder? = null
    var name = "ridems_rider_channel"
    var id = "ridems_rider_channel_id" // The user-visible name of the channel.

    var description = "channel for ridems app"
    var soundUri:Uri?=null
    fun createNotification(
        context: Context,
        intent: Intent,
        title: String?,
        body: String?,
        img: Bitmap?
    ) {
            soundUri = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + context.packageName + "/raw/kitchenorder"
            );


        setUpChannel()


        notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationLayout = RemoteViews(context.packageName, R.layout.notificationcustomlayout)
        val dateFormat: DateFormat = SimpleDateFormat("hh:mm a")
        builder = NotificationCompat.Builder(context, id)
        pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        if(img!=null) {
            notificationLayout.setImageViewBitmap(R.id.shapeableImageView2, img)
        }
        notificationLayout.setTextViewText(R.id.name, title)
        notificationLayout.setTextViewText(R.id.des, body)
        notificationLayout.setTextViewText(R.id.time, dateFormat.format(Date()))
        builder?.setSmallIcon(R.drawable.ic_launcher_foreground)
        ?.setStyle(NotificationCompat.DecoratedCustomViewStyle())
            ?.setContent(notificationLayout)
                ?.setAutoCancel(true)
                ?.setSound(soundUri)
                ?.setContentIntent(pendingIntent)

        val notification = builder?.build()
       notifManager?.notify(1, notification)
    }




    fun setUpChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            var mChannel: NotificationChannel? = notifManager?.getNotificationChannel(id)
            if (mChannel == null) {
                mChannel = NotificationChannel(id, name, importance)
                mChannel.description =description
                mChannel.enableVibration(true)
                mChannel.lightColor = Color.GREEN
                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                val audioAttributes = AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build()
                mChannel.setSound(soundUri, audioAttributes)
            notifManager?.createNotificationChannel(mChannel)
            }


        }
    }
}