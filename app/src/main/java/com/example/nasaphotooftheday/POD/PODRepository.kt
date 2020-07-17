package com.example.nasaphotooftheday.POD

import android.util.Log
import androidx.lifecycle.LiveData

class PODRepository constructor(private val podDao: PODDao){
    var webApi = WebApi()


    suspend fun getPOD():LiveData<List<POD>> {
        refreshPOD()
        return podDao.load()
    }


    suspend fun refreshPOD() {
        val response = webApi.ProvideWebApi().getPOD("WIELb0gec4r9BI0okJmeAol74BfYjlvnphgp6RU0").execute()
        //Log.d("res refresh",response.body().toString())
        podDao.save(response.body()!!)
    }

    suspend fun getWithDate(date:String){
        val response = webApi.ProvideWebApi().getWithDate("WIELb0gec4r9BI0okJmeAol74BfYjlvnphgp6RU0",date).execute()
        Log.d("res",response.body().toString())
        podDao.save(response.body()!!)
    }

}