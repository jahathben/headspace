package com.example.hsexercise.api

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Emitting response & failures from Retrofit as LiveData
 */
class LiveDataCallAdapter<R>(private val responseType: Type) :
    CallAdapter<R, LiveData<NetworkResponse<R>>> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): LiveData<NetworkResponse<R>> {
        return object : LiveData<NetworkResponse<R>>() {
            var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            postValue(NetworkResponse(response))
                        }

                        override fun onFailure(call: Call<R>, throwable: Throwable) {
                            postValue(NetworkResponse(throwable))
                        }
                    })
                }
            }
        }
    }
}