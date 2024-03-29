package com.appsian.aaproject.api

import androidx.lifecycle.LiveData
import com.appsian.aaproject.BuildConfig
import com.appsian.aaproject.db.entities.Repo
import com.appsian.aaproject.utils.ApiResponse
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface MyApiService {
    /**
     * https://api.github.com/search/repositories?q=java&sort=stars&order=desc
     * Gets data. Using retrofit here with rxjava to get the data from the datasource
     *
     * @return the data
     */
    @GET("search/repositories")
    fun getData(@Query("q") q: String):
            LiveData<ApiResponse<List<Repo>>>

    companion object {
        fun create(): MyApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create())
                .baseUrl(BuildConfig.END_POINT)
                .build()

            return retrofit.create(MyApiService::class.java)
        }
    }
}