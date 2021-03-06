package com.example.facialasymmetryandroid

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facialasymmetryandroid.model.ReturnString
import com.google.gson.Gson
import com.router.cointts.repository.ServerRecieverService
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


class MainViewModel : ViewModel() {
    val loadingLiveData = MutableLiveData<Boolean>()
    val returnStringLiveData = MutableLiveData<ReturnString>()

    private val serverRecieverService: ServerRecieverService

    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl("http://220.69.208.242:80")
            .build()

        serverRecieverService = retrofit.create(ServerRecieverService::class.java)
    }

    fun postImage(type: String , body : MultipartBody.Part) {

        loadingLiveData.value = true

        viewModelScope.launch {

            fun funType(typeStr: String): Call<ResponseBody> {
                return when (typeStr) {
                    "Type1" -> serverRecieverService.postImage1(body)
                    "Type2" -> serverRecieverService.postImage2(body)
                    "Type3" -> serverRecieverService.postImage3(body)
                    "Type4" -> serverRecieverService.postImage4(body)
                    "Type5" -> serverRecieverService.postImage5(body)
                    "Type6" -> serverRecieverService.postImage6(body)
                    else -> serverRecieverService.postImage1(body)
                }
            }

            if (type != null) {
                funType(type).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        //???????????? ???????????? ?????? ?????? ??? ,  ????????? ?????? ?????? ??????
                        if (!response.isSuccessful) {
                            Log.d(TAG, "onResponse: " + response.toString())
                            return
                        }

                        //????????? ??????????????? ???????????? ??????
                        try {
                            //val gsonBuilder = GsonBuilder()
                            //val gson = gsonBuilder.create()
                            //val returnString: ReturnString = gson.fromJson(response.body()!!.string(), ReturnString::class.java)

                            var returnStr = response.body()!!.string().replace("\\" , "")
                            returnStr = returnStr.substring(1)
                            returnStr = returnStr.substring(0,returnStr.length-2)
                            //???????????? ????????? ????????????
                            Gson().fromJson(
                                returnStr,
                                ReturnString::class.java
                            ).also {
                                returnStringLiveData.value = it
                                GlobalApplication.returnString = it
                                loadingLiveData.value = false
                            }
                        } catch (e: IOException) {
                            Log.d(TAG, "onResponse: ???????????? ????????? ???????????? ???????????? ??????")
                            loadingLiveData.value = false
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d(TAG, "onFailure: ?????? ?????? + ${t}")
                        returnStringLiveData.value = null
                        loadingLiveData.value = false
                    }
                })
            }
        }

    }

}
