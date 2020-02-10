package com.example.hsexercise.api

import retrofit2.Retrofit

class ApiConnection {
    companion object {
        fun getFeatureService(): FeatureService {
            return getRetrofitService().create(FeatureService::class.java)
        }

        private fun getRetrofitService(): Retrofit {
            return NetworkProvider.provideRestClient().createRetrofitAdapter()
        }
    }
}