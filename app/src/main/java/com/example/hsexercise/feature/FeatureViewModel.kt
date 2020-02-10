package com.example.hsexercise.feature

import android.app.Application
import androidx.lifecycle.*
import com.example.hsexercise.api.Resource
import com.example.hsexercise.feature.database.FeatureModel
import com.example.hsexercise.repository.FeaturesRepository

class FeatureViewModel : ViewModel() {
    var featuresList : LiveData<Resource<List<FeatureModel>>> = MutableLiveData()

    class Factory :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) = FeatureViewModel() as T
    }

    fun getFeaturesList(application: Application) {
        featuresList = FeaturesRepository.getFeaturesList(application, viewModelScope)
    }
}
