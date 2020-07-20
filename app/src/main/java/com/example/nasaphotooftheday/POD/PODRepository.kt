package com.example.nasaphotooftheday.POD

import android.util.Log
import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PODRepository constructor(private val podDao: PODDao){
    var webApi = WebApi()


    suspend fun getPOD():LiveData<List<POD>> {
        refreshPOD()
        return podDao.load()
    }


    suspend fun refreshPOD() {
        try {
            val response =
                webApi.ProvideWebApi().getPOD("WIELb0gec4r9BI0okJmeAol74BfYjlvnphgp6RU0").execute()
            podDao.save(response.body()!!)
        }catch (e:NullPointerException){
            Log.d("err",e.toString())
        }
    }

    suspend fun getWithDate(date:String){
        try{
            val response = webApi.ProvideWebApi().getWithDate("WIELb0gec4r9BI0okJmeAol74BfYjlvnphgp6RU0",date).execute()
            Log.d("res",response.body().toString())
            podDao.save(response.body()!!)
            podDao.loadWithDate(date)
        }catch (e:NullPointerException){
            Log.d("err",e.toString())
        }

    }

}