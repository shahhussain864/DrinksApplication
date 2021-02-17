package com.realestate.testapplication

import android.app.Application
import com.winkells.store.dataStore.DataStoreManagment
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseClass : Application() {
    override fun onCreate() {
        super.onCreate()
        DataStoreManagment.initilizeDatStore(this)
    }
}