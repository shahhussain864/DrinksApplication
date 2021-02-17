package com.winkells.store.dataStore


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


object DataStoreManagment {
    private lateinit var dataStore: DataStore<Preferences>
    private val isByName = booleanPreferencesKey("isByName")

    fun initilizeDatStore(context: Context) {
        dataStore = context.createDataStore(
            name = "settings"
        )
    }

    suspend fun getIsByName(): Boolean {
        return dataStore.data
            .map { currentPreferences ->
                // Unlike Proto DataStore, there's no type safety here.
                currentPreferences[isByName] ?: true
            }.first()
    }


    suspend fun setIsByName(emai: Boolean) {
        dataStore.edit {
            it[isByName] = emai
        }
    }



}