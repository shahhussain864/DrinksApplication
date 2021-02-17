package com.realestate.testapplication.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.room.Room
import com.realestate.testapplication.R
import com.realestate.testapplication.Room.MyAppDatabase
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object Constants {
    const val BaseUrl="https://www.thecocktaildb.com/api/"

    fun View.show(){
        this.visibility=View.VISIBLE
    }


    fun View.hide(){
        this.visibility=View.GONE
    }

    private var myAppDatabase: MyAppDatabase? = null
    fun getMyAppDatabase(c: Context?): MyAppDatabase? {
        if (myAppDatabase == null) {
            myAppDatabase = c?.let { Room.databaseBuilder(it, MyAppDatabase::class.java, "Storedb").fallbackToDestructiveMigration().build() }
        }
        return myAppDatabase
    }

    fun View.invisible(){
        this.visibility=View.INVISIBLE
    }
      fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url
                .openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    var alertDialog:AlertDialog?=null
    fun getAlert(context: Context):AlertDialog?{

        if(alertDialog==null) {

            val builder = AlertDialog.Builder(context)
                .setView(R.layout.alertview)
                .setCancelable(false)

            alertDialog = builder.create()
        }
        return alertDialog

        }



    fun getResizedBitmap(bm: Bitmap?, newHeight: Int, newWidth: Int): Bitmap? {
        val width = bm?.width
        val height = bm?.height
        val scaleWidth = newWidth.toFloat() / (width?:0)
        val scaleHeight = newHeight.toFloat() / (height?:0)
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        return bm?.let {
            Bitmap.createBitmap(
                it, 0, 0, width?:0, height?:0,
                matrix, false
            )
        }
    }

}