package com.realestate.testapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.realestate.testapplication.network.ApiService
import com.realestate.testapplication.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MyBroadCastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if ("android.intent.action.BOOT_COMPLETED" == intent.action) {
            startAlarm(context)
        }
        else{

            getData(context)
        }

    }


    fun getData(context: Context) {
        val ret = Retrofit.Builder()
            .baseUrl(Constants.BaseUrl).addConverterFactory(GsonConverterFactory.create()).build()

        val apiServic: ApiService = ret.create(ApiService::class.java)
        val intent = Intent(context, MainActivity::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val data = apiServic.getProducts("")
                val size = data?.drinks?.size ?: 0
                val rnds = (0 until size).random()


                var bitmap: Bitmap?
                withContext(Dispatchers.IO) {
                    bitmap = Constants.getBitmapFromURL(
                        data?.drinks?.get(rnds)
                            ?.strDrinkThumb
                    )
                    bitmap = Constants.getResizedBitmap(bitmap, 60, 60)
                }
                NotificationManager.createNotification(
                    context, intent, data?.drinks?.get(rnds)?.strDrink,
                    data?.drinks?.get(rnds)?.strInstructions, bitmap
                )
            } catch (ex: Exception) {

                try {
                    val lFav = Constants.getMyAppDatabase(context.applicationContext)?.dao()
                        ?.getAllFavDrinks2()
                    if (lFav.isNullOrEmpty()) {
                        NotificationManager.createNotification(
                            context, intent, "Need Some Drinks!",
                            "Need some drinks open app now", null
                        )
                    } else {
                        val rnds = (lFav.indices).random()

                        val filePath: String = File(lFav[rnds]?.strDrinkThumb ?: "").path
                        val bitmap = BitmapFactory.decodeFile(filePath)
                        val data = lFav[rnds]
                        NotificationManager.createNotification(
                            context, intent, data?.strDrink,
                            data?.strInstructions, bitmap
                        )
                    }
                } catch (ex: Exception) {

                    NotificationManager.createNotification(
                        context, intent, "Need Some Drinks!",
                        "Need some drinks open app now", null
                    )
                }


            }
        }


    }

    private fun startAlarm(context: Context) {

        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("GMT+5:00");
        calendar[Calendar.HOUR_OF_DAY] = 14

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(context, MyBroadCastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}