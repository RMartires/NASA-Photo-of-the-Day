package com.example.nasaphotooftheday.POD

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Webservice {
    @GET("/planetary/apod")
    fun getPOD(@Query("api_key") api_key:String): Call<POD>

    @GET("/planetary/apod")
    fun getWithDate( @Query("api_key") api_key:String,@Query("date") date:String): Call<POD>
}

class WebApi{
    public fun ProvideWebApi(): Webservice {
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(
                RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create())
            .baseUrl("https://api.nasa.gov")
            .build()

        return retrofit.create(Webservice::class.java)
    }
}