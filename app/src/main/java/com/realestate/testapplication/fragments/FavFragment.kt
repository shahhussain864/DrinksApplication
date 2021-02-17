package com.realestate.testapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.realestate.testapplication.DiffUti
import com.realestate.testapplication.Room.Drinks
import com.realestate.testapplication.adapters.FavFragmentAdapter
import com.realestate.testapplication.databinding.FragmentSeelctFavBinding
import com.realestate.testapplication.utils.Constants.hide
import com.realestate.testapplication.utils.Constants.show
import com.realestate.testapplication.viewModel.MyViewModel


class FavFragment : Fragment() {

    private lateinit var viewModel: MyViewModel
    lateinit var adapter: FavFragmentAdapter
    var myData: ArrayList<Drinks?>? = ArrayList()
    lateinit var binding: FragmentSeelctFavBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.let {
            ViewModelProviders.of(it).get(MyViewModel::class.java)
        }!!

        binding = FragmentSeelctFavBinding.inflate(layoutInflater, container, false)

        adapter = FavFragmentAdapter(data = myData , requireActivity())
        binding.rec.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.rec.adapter = adapter

        activity?.let {

            viewModel.getAllDrinks(it)
        }
        observe()
        return binding.root
    }


    fun observe() {

        viewModel.allFavDrinks?.observe(viewLifecycleOwner, {
            viewModel.favDataUpdated?.value = true

            if(it.isNullOrEmpty()){
                binding.noDrinkIsFound.show()
            }
            else{
                binding.noDrinkIsFound.hide()
            }

            val diffCallback = DiffUti(myData, it)
            val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback)

            myData?.clear()
            it?.let { it1 -> myData?.addAll(it1) }
            diffResult.dispatchUpdatesTo(adapter)


        })


    }


}