package com.realestate.testapplication.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.realestate.testapplication.R
import com.realestate.testapplication.Room.Drinks
import com.realestate.testapplication.databinding.SearchfragmentadaptercustomBinding
import com.realestate.testapplication.model.DrinksItem
import com.realestate.testapplication.utils.Constants
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class SearchFragmentAdapter(var data: List<DrinksItem?>?, var context: Context ) :
    RecyclerView.Adapter<SearchFragmentAdapter.MyHolder>() {

    class MyHolder(var productViews: SearchfragmentadaptercustomBinding) :
        RecyclerView.ViewHolder(productViews.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
    val binding=SearchfragmentadaptercustomBinding.inflate(
        LayoutInflater.from(context),
        parent,
        false
    )
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.productViews.favIcon.setImageResource(R.drawable.favempty)
            Picasso.get().load(data?.get(position)?.strDrinkThumb).placeholder(R.drawable.placeholder).into(
                holder.productViews.shapeableImageView
            )
         takeIf { data?.get(position)?.strAlcoholic=="Alcoholic"}?.let {
            holder.productViews.isAlch.isChecked=true
         }?:run{
             holder.productViews.isAlch.isChecked=false
         }
        holder.productViews.productName.text = data?.get(position)?.strDrink
        holder.productViews.productDesc.text = data?.get(position)?.strInstructions

        CoroutineScope(Dispatchers.Main).launch {
            val id = data?.get(position)?.idDrink
            try{
                val data=Constants.getMyAppDatabase(context)?.dao()?.getFavDrinks(id)
                if(data?.isNullOrEmpty()==false)
                {
                    holder.productViews.favIcon.setImageResource(R.drawable.favfull)
                }
                else{
                    holder.productViews.favIcon.setImageResource(R.drawable.favempty)
                }
            }catch (ex:Exception){

            }
        }


    holder.productViews.favIcon.setOnClickListener {
        CoroutineScope(Dispatchers.Main).launch {
            val id = data?.get(position)?.idDrink
            try{
                val data1=Constants.getMyAppDatabase(context)?.dao()?.getFavDrinks(id)
                if(data1?.isNullOrEmpty()==false)
                {
                    try{
                        Constants.getMyAppDatabase(context)?.dao()?.deleteDrinks(data?.get(position)?.idDrink)
                        Toast.makeText(context , "Removed from favourite", Toast.LENGTH_SHORT).show()
                        holder.productViews.favIcon.setImageResource(R.drawable.favempty)
                    }
                    catch (ex:Exception){
                        Toast.makeText(context , "Removed from favourite failed", Toast.LENGTH_SHORT).show()
                    }

                }
                else{
                    Constants.getAlert(context)?.show()
                    var filePath =  ""
                    withContext(Dispatchers.Default){
                        var bitmap=Constants.getBitmapFromURL(data?.get(position)?.strDrinkThumb)
                        bitmap=Constants.getResizedBitmap(bitmap,60,60)
                        try {
                            var file = context.getDir("Images", Context.MODE_PRIVATE)
                            file = File(file, "img${data?.get(position)?.idDrink}.jpg")
                            val out = FileOutputStream(file)
                            bitmap?.compress(Bitmap.CompressFormat.JPEG, 85, out)
                            out.flush()
                            out.close()
                            Log.i("Seiggailion", "Image saved.")
                            filePath=file.path
                            val singleDrink=data?.get(position)
                            val model=Drinks(singleDrink?.idDrink , singleDrink?.strDrink , singleDrink?.strInstructions ,filePath , singleDrink?.strAlcoholic )
                            Constants.getMyAppDatabase(context)?.dao()?.addDrinks(model)
                            withContext(Dispatchers.Main){
                                Toast.makeText(context , "Added to favourite", Toast.LENGTH_SHORT).show()
                                holder.productViews.favIcon.setImageResource(R.drawable.favfull)
                            }

                        } catch (e: Exception) {
                            withContext(Dispatchers.Main){

                                Toast.makeText(context , "Add to favourite failed", Toast.LENGTH_SHORT).show()
                            }
                        }finally {
                            withContext(Dispatchers.Main) {
                                Constants.getAlert(context)?.dismiss()
                            }
                        }


                    }
                }
            }catch (ex:Exception){
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Add to favourite failed ${ex.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            finally {
                withContext(Dispatchers.Main) {
                    Constants.getAlert(context)?.dismiss()
                }
            }
        }
    }


    }

    override fun getItemCount(): Int {
        return data?.size?:0
    }



}