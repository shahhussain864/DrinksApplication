package com.realestate.testapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.realestate.testapplication.adapters.PagerAdapter
import com.realestate.testapplication.databinding.ActivityMainBinding
import com.realestate.testapplication.utils.Constants
import com.realestate.testapplication.viewModel.MyViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val viewModel: MyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pager.adapter = PagerAdapter(this)
        binding.pager.offscreenPageLimit = 1
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Home"
                }
                1 -> {
                    tab.text = "Favourites"
                }
            }
        }.attach()
        startAlarm()
    }


    private fun startAlarm() {

        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("GMT+5:00");
        calendar[Calendar.HOUR_OF_DAY] = 14

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(this, MyBroadCastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
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

    override fun onDestroy() {
        Constants.alertDialog = null
        super.onDestroy()
    }

    companion object {
        const val Data_LOADING = "Loading"
        const val Data_FAIL = "Failed"
        const val Data_SUCCESS = "success"
    }
}