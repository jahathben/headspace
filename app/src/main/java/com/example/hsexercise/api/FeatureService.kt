package com.example.hsexercise.api

import androidx.lifecycle.LiveData
import com.example.hsexercise.feature.database.FeatureModel
import retrofit2.http.GET

interface FeatureService {
    @GET("v2/list")
    fun getFeatures(): LiveData<NetworkResponse<List<FeatureModel>>>
}