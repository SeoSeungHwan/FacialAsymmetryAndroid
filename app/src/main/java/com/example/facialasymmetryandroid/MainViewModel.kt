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
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

class MainViewModel : ViewModel() {
    val loadingLiveData = MutableLiveData<Boolean>()
    val bitmapLiveData = MutableLiveData<Bitmap>()

    private val serverRecieverService: ServerRecieverService

    init {
        val retrofit = Retrofit
            .Builder()
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
                        //응답오는 과정에서 에러 발생 시 ,  얼굴을 찾지 못할 경우
                        if (!response.isSuccessful) {
                            Log.d(TAG, "onResponse: " + response.toString())
                            return
                        }

                        //파이썬 코드로부터 응답받는 부분
                        try {
                            //텍스트와 이미지 가져오기
                            Gson().fromJson(
                                response.body()!!.string(),
                                ReturnString::class.java
                            ).also {
                                val encodeByte = android.util.Base64.decode(it.imageBytes, android.util.Base64.DEFAULT)
                                val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
                                bitmapLiveData.value = bitmap
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
