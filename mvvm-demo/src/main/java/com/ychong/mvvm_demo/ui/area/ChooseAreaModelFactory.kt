package com.ychong.mvvm_demo.ui.area

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ychong.mvvm_demo.data.PlaceRepository

@Suppress("UNCHECKED_CAST")
class ChooseAreaModelFactory(private val repository: PlaceRepository): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass:Class<T>):T{
        return ChooseAreaViewModel(repository) as T
    }
}

