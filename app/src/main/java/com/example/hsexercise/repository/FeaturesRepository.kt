package com.example.hsexercise.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.hsexercise.api.ApiConnection
import com.example.hsexercise.api.NetworkResponse
import com.example.hsexercise.api.Resource
import com.example.hsexercise.common.DatabaseProvider
import com.example.hsexercise.common.HeadspaceRoomDatabase
import com.example.hsexercise.feature.database.FeatureModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object FeaturesRepository {
    fun getFeaturesList(
        application: Application,
        viewModelScope: CoroutineScope
    ): LiveData<Resource<List<FeatureModel>>> {
        val database by lazy { DatabaseProvider.provideRoomDatabase(application) }

        // return a NetworkBoundResource obj and furnish implementation details for abstract class
        return object : NetworkBoundResource<List<FeatureModel>, List<FeatureModel>>() {
            override fun saveFetchData(items: List<FeatureModel>) {
                viewModelScope.launch { saveToDB(database, items) }
            }

            override fun shouldFetch(data: List<FeatureModel>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun loadFromDatabase(): LiveData<List<FeatureModel>> {
                return database.featureTableDao().getAll()
            }

            override fun fetchService(): LiveData<NetworkResponse<List<FeatureModel>>> {
                val api by lazy { ApiConnection.getFeatureService() }
                return api.getFeatures()
            }

            override fun onFetchFailed(message: String?) {
                Log.e("BooksRepository", message)
            }
        }.asLiveData()
    }

    private suspend fun saveToDB(
        database: HeadspaceRoomDatabase,
        items: List<FeatureModel>
    ) {
        withContext(Dispatchers.IO) {
            database.featureTableDao().insertAll(items)
        }
    }
}