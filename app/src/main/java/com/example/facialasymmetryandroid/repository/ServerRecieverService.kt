package com.router.cointts.repository

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ServerRecieverService {
    companion object{
        const val BASE_URL = "http://220.69.208.242:80"
    }


    @Multipart
    @POST("/res")
    suspend fun postImage(
        @Part image : MultipartBody.Part
    ): Call<ResponseBody>


}