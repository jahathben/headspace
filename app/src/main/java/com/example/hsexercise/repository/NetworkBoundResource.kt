package com.example.hsexercise.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.hsexercise.api.NetworkResponse
import com.example.hsexercise.api.Resource

/**
 * This class enforces our default flow to get data - load from database, if not available
 * make api call, and save data to database, and surface results from database. this will be
 * called by Repository classes
 */

abstract class NetworkBoundResource<ResultType, RequestType> {
    private val result: MediatorLiveData<Resource<ResultType>> = MediatorLiveData()

    init {
        val loadedFromDB = this.loadFromDatabase()
        result.addSource(loadedFromDB) { data ->
            result.removeSource(loadedFromDB)
            if (shouldFetch(data)) {
                result.postValue(Resource.Loading(null))
                doNetworkFetch(loadedFromDB)
            } else {
                result.addSource<ResultType>(loadedFromDB) { newData ->
                    setValue(Resource.Success(newData))
                }
            }
        }
    }

    private fun doNetworkFetch(loadedFromDB: LiveData<ResultType>) {
        val apiResponse = fetchService()
        result.addSource(apiResponse) { response ->
            response?.let {
                when (response.isSuccessful) {
                    true -> {
                        response.body?.let {
                            saveFetchData(it)
                            val loaded = loadFromDatabase()
                            result.addSource(loaded) { newData ->
                                newData?.let {
                                    setValue(
                                        Resource.Success(
                                            newData
                                        )
                                    )
                                }
                            }
                        }
                    }
                    false -> {
                        result.removeSource(loadedFromDB)
                        onFetchFailed(response.message)
                        response.message?.let {
                            result.addSource<ResultType>(loadedFromDB) { newData ->
                                setValue(
                                    Resource.Error(
                                        it,
                                        newData
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        result.value = newValue
    }

    fun asLiveData(): LiveData<Resource<ResultType>> {
        return result
    }

    @WorkerThread
    protected abstract fun saveFetchData(items: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDatabase(): LiveData<ResultType>

    @MainThread
    protected abstract fun fetchService(): LiveData<NetworkResponse<RequestType>>

    @MainThread
    protected abstract fun onFetchFailed(message: String?)
}