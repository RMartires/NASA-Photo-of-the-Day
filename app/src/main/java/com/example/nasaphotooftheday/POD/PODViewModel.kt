package com.example.nasaphotooftheday.POD

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

class PODViewModel constructor(application: Application) : AndroidViewModel(application) {

    private var podRepository: PODRepository
    public var pod : LiveData<List<POD>> = liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
        var temppod = getPOD()
        emitSource(temppod)
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