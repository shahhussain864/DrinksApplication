package com.realestate.testapplication.viewModel

import android.content.Context
import androidx.lifecycle.*
import com.realestate.testapplication.MainActivity
import com.realestate.testapplication.Room.Drinks
import com.realestate.testapplication.model.ProductResponse
import com.realestate.testapplication.repository.Repository
import com.realestate.testapplication.utils.Constants
import com.winkells.store.dataStore.DataStoreManagment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(val repository: Repository) : ViewModel() {
    var myProduct: MutableLiveData<CustomModel> = MutableLiveData()
    var allFavDrinks: LiveData<List<Drinks?>?>? = MutableLiveData()
    var favDataUpdated: MutableStateFlow<Boolean>? = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            if(DataStoreManagment.getIsByName())
            {
                getProduct("margarita")
            }
            else{
                getProduct("m")
            }


        }

    }

    fun getProduct(s: String) {
        myProduct.postValue(CustomModel(MainActivity.Data_LOADING, null))
        viewModelScope.launch {
            repository.getData(s)
                .catch {
                    myProduct.postValue(CustomModel(MainActivity.Data_FAIL, null))
                }
                .collect {
                    if (it == null) {
                        myProduct.postValue(CustomModel(MainActivity.Data_FAIL, null))

                    } else {
                        myProduct.postValue(CustomModel(MainActivity.Data_SUCCESS, it))
                    }
                }
        }
    }


    fun getAllDrinks(context:Context){
        viewModelScope.launch {
            allFavDrinks=Constants.getMyAppDatabase(context)?.dao()?.getAllFavDrinks()
        }
    }




    data class CustomModel(val status: String, var data: ProductResponse?)


}