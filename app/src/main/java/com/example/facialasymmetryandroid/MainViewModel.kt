package com.example.facialasymmetryandroid

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facialasymmetryandroid.model.ReturnString
import com.google.gson.Gson
import com.router.cointts.repository.ServerRecieverService
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {
    val loadingLiveData = MutableLiveData<Boolean>()
    val imageUrlLiveData = MutableLiveData<String>()
    val returnString = MutableLiveData<ReturnString>()

    private val serverRecieverService: ServerRecieverService

    init {
            val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1000, TimeUnit.SECONDS)
            .readTimeout(1000, TimeUnit.SECONDS)
            .writeTimeout(1000, TimeUnit.SECONDS)
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
            val type1 = serverRecieverService.postImage1(body)
            val type2 = serverRecieverService.postImage2(body)
            val type3 = serverRecieverService.postImage3(body)
            val type4 = serverRecieverService.postImage4(body)
            val type5 = serverRecieverService.postImage5(body)
            val type6 = serverRecieverService.postImage6(body)

            fun funType(typeStr: String): Call<ResponseBody> {
                return when (typeStr) {
                    "Type1" -> type1
                    "Type2" -> type2
                    "Type3" -> type3
                    "Type4" -> type4
                    "Type5" -> type5
                    "Type6" -> type6
                    else -> type1
                }
            }
            if (type != null) {
                funType(type).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        //응답오는 과정에서 에러 발생 시 ,  얼굴찾지 못함 토스트 출력
                        if (!response.isSuccessful) {
                            Log.d(TAG, "onResponse: 에러 " + response.toString())
                            return
                        }

                        //파이썬 코드로부터 응답받는 부분
                        try {
                            //텍스트와 이미지 가져오기
                            Gson().fromJson(
                                response.body()!!.string(),
                                ReturnString::class.java
                            ).also {
                                Log.d(TAG, "onResponse: ${it.message}")
                                imageUrlLiveData.value = it.imageBytes
                                returnString.value = it
                                loadingLiveData.value = false
                                //it.message : 메시지 출력
                            }
                        } catch (e: IOException) {
                            Log.d(TAG, "onResponse: 텍스트와 이미지 가져오는 부분에서 에러")
                            loadingLiveData.value = false
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d(TAG, "onFailure: 연결 실패 + ${t}")
                        loadingLiveData.value = false
                    }
                })
            }
        }

    }

}
