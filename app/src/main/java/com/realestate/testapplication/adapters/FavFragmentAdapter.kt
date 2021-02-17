package com.realestate.testapplication.adapters

import android.content.Context
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
import java.io.File

class FavFragmentAdapter(var data: List<Drinks?>?, var context: Context) :
    RecyclerView.Adapter<FavFragmentAdapter.MyHolder>() {

    class MyHolder(var productViews: SearchfragmentadaptercustomBinding) :
        RecyclerView.ViewHolder(productViews.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding= SearchfragmentadaptercustomBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        Picasso.get().load(File(data?.get(holder.adapterPosition)?.strDrinkThumb?:"")).placeholder(R.drawable.placeholder).into(
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
                val data= Constants.getMyAppDatabase(context)?.dao()?.getFavDrinks(id)
                if(data?.isNullOrEmpty()==false)
                {
                    holder.productViews.favIcon.setImageResource(R.drawable.favfull)
                }
            }catch (ex: Exception){

            }
        }


        holder.productViews.favIcon.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                   try{
                            Constants.getMyAppDatabase(context)?.dao()?.deleteDrinks(data?.get(holder.adapterPosition)?.idDrink)
                            Toast.makeText(context , "Removed from favourite", Toast.LENGTH_SHORT).show()
                            holder.productViews.favIcon.setImageResource(R.drawable.favempty)
                        }
                        catch (ex: Exception){
                            Toast.makeText(context , "Removed from favourite failed", Toast.LENGTH_SHORT).show()
                        }



            }
        }


    }

    override fun getItemCount(): Int {
        return data?.size?:0
    }



}