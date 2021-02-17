package com.realestate.testapplication

import androidx.recyclerview.widget.DiffUtil
import com.realestate.testapplication.Room.Drinks

class DiffUti(var oldList:List<Drinks?>?, var newList:List<Drinks?>?) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList?.size?:0
    }

    override fun getNewListSize(): Int {
        return newList?.size?:0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList?.get(oldItemPosition)?.idDrink==newList?.get(newItemPosition)?.idDrink
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
         val oldEmployee = oldList?.get(oldItemPosition);
         val newEmployee = newList?.get(newItemPosition);

        return oldEmployee?.strInstructions==newEmployee?.strInstructions

    }
}