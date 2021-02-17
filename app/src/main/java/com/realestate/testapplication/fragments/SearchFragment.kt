package com.realestate.testapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.realestate.testapplication.MainActivity
import com.realestate.testapplication.adapters.SearchFragmentAdapter
import com.realestate.testapplication.databinding.FragmentSearchBinding
import com.realestate.testapplication.model.DrinksItem
import com.realestate.testapplication.utils.Constants
import com.realestate.testapplication.utils.Constants.hide
import com.realestate.testapplication.utils.Constants.show
import com.realestate.testapplication.viewModel.MyViewModel
import com.winkells.store.dataStore.DataStoreManagment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var viewModel: MyViewModel
    lateinit var binding: FragmentSearchBinding

    //    private val viewModel: MyViewModel by viewModels(
//        ownerProducer = { this.requireActivity() }
//    )
    var searchtext: String? = ""

    var myData: ArrayList<DrinksItem?>? = ArrayList()
     var adapter: SearchFragmentAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.let { ViewModelProviders.of(it).get(MyViewModel::class.java) }!!

        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        observe()

        adapter = SearchFragmentAdapter(myData, requireActivity())
        binding.rec.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.rec.adapter = adapter

        onClickEvent()
        getLastState()
        observeRadiogroup()
        return binding.root
    }

    fun getLastState() {

        viewLifecycleOwner.lifecycleScope.launch {
            if (DataStoreManagment.getIsByName()) {
                binding.radio1ByName.isChecked = true
            } else {
                binding.radio2ByAlphabets.isChecked = true
            }


        }
    }

    fun observeRadiogroup() {
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->

            viewLifecycleOwner.lifecycleScope.launch {
                when (binding.radio1ByName.isChecked) {
                    true -> {
                        DataStoreManagment.setIsByName(true)
                    }
                    false -> {
                        DataStoreManagment.setIsByName(false)
                    }
                }
            }

        }
    }

    fun onClickEvent() {
        binding.searchBtn.setOnClickListener {
            activity?.let { it1 -> Constants.hideKeyboard(it1) }
            if (searchtext.isNullOrEmpty()) {
                Toast.makeText(activity, "Empty String", Toast.LENGTH_SHORT).show()
            } else {
                if (binding.radio1ByName.isChecked) {
                    viewModel.getProduct(searchtext ?: "")
                } else {
                    viewModel.getProduct(searchtext?.get(0).toString())
                }
            }
        }


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (!p0.isNullOrEmpty()) {
                    if (binding.radio1ByName.isChecked) {
                        viewModel.getProduct(p0)
                    } else {
                        viewModel.getProduct(p0[0].toString())
                    }
                } else {
                    Toast.makeText(activity, "Empty String", Toast.LENGTH_SHORT).show()
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                searchtext = p0
                return false
            }
        })
    }


    fun observe() {

        
        lifecycleScope.launch {
            viewModel.favDataUpdated?.collect {
                if(it){
                    adapter?.notifyDataSetChanged()
                    viewModel.favDataUpdated?.value=false
                }
            }
        }




        viewModel.myProduct.observe(viewLifecycleOwner, {
            when (it.status) {
                MainActivity.Data_LOADING -> {
                    binding.progressBar.show()
                    binding.noProductFound.hide()
                    myData?.clear()
                    adapter?.notifyDataSetChanged()
                }

                MainActivity.Data_FAIL -> {
                    binding.progressBar.hide()
                    binding.noProductFound.show()
                }

                MainActivity.Data_SUCCESS -> {
                    binding.progressBar.hide()
                    myData?.clear()
                    it.data?.drinks?.let { it1 -> myData?.addAll(it1) }
                    adapter?.notifyDataSetChanged()
                }

            }


        })


    }




}