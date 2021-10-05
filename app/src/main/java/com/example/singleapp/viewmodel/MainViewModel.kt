package com.example.singleapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val data: MutableLiveData<String> = MutableLiveData()

    fun getXxxData() {
        viewModelScope.launch {
            val async1 = async(Dispatchers.IO) { getInt() }
            val async2 = async(Dispatchers.IO) { getString() }
            val result = async2.await() + async1.await()
            data.value = result
        }
    }

    private suspend fun getInt(): Int {
        delay(1000)
        return 28
    }

    private suspend fun getString(): String {
        delay(1000)
        return " years old"
    }
}