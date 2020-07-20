package com.example.nasaphotooftheday.POD

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.nasaphotooftheday.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

class PODViewModel constructor(application: Application) : AndroidViewModel(application) {

    public var pods = ArrayList<POD>()

    private var podRepository: PODRepository
    public var pod : LiveData<List<POD>> = liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
        try{
            var temppod = getPOD()
            emitSource(temppod)
        }catch (e:NullPointerException){
         Log.d("d","d")
        }
    }

    init {
        val podDao = PODDatabase.getDatabase(application).podDao()
        podRepository = PODRepository(podDao)
    }

    public suspend fun getPOD(): LiveData<List<POD>> {
        return podRepository.getPOD()
    }

    public fun getWithDate(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            podRepository.getWithDate(date)
        }
    }

}