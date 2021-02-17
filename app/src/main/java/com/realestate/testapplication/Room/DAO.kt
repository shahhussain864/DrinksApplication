package com.realestate.testapplication.Room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DAO {
    //region CART
    @Insert
    fun addDrinks(drinks: Drinks?)

    @Query("select * from favdrinks where idDrink=:id")
    suspend fun getFavDrinks(id: String?): List<Drinks?>?

    @Query("select * from favdrinks")
     fun getAllFavDrinks(): LiveData<List<Drinks?>?>

    @Query("select * from favdrinks")
    suspend fun getAllFavDrinks2(): List<Drinks?>?

    @Query("delete from favdrinks where idDrink=:id")
   suspend fun deleteDrinks(id: String?)
}