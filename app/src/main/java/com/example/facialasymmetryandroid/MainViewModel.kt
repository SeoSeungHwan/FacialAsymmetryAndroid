package com.example.facialasymmetryandroid

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.router.cointts.repository.ServerRecieverService
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit


class MainViewModel :ViewModel() {

    val responseBody = MutableLiveData<Call<ResponseBody>>()

    private val serverRecieverService : ServerRecieverService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(ServerRecieverService.BASE_URL)
            .build()

        serverRecieverService = retrofit.create(ServerRecieverService::class.java)
    }


    fun postImage(body : MultipartBody.Part){
        viewModelScope.launch {
            responseBody.value = serverRecieverService.postImage(body)
        }
    }
}