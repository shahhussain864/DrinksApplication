package com.realestate.testapplication.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.realestate.testapplication.fragments.SearchFragment
import com.realestate.testapplication.fragments.FavFragment

class PagerAdapter(fm:FragmentActivity) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                SearchFragment()
            }
            1->{
                FavFragment()
            }
            else->{
                SearchFragment()
            }
        }
    }
}