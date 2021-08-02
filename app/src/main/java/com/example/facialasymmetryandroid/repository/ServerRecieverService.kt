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
    @POST("/res/type1")
    fun postImage1(
        @Part image : MultipartBody.Part
    ): Call<ResponseBody>

    @Multipart
    @POST("/res/type2")
    fun postImage2(
        @Part image : MultipartBody.Part
    ): Call<ResponseBody>

    @Multipart
    @POST("/res/type3")
    fun postImage3(
        @Part image : MultipartBody.Part
    ): Call<ResponseBody>

    @Multipart
    @POST("/res/type4")
    fun postImage4(
        @Part image : MultipartBody.Part
    ): Call<ResponseBody>

    @Multipart
    @POST("/res/type5")
    fun postImage5(
        @Part image : MultipartBody.Part
    ): Call<ResponseBody>

    @Multipart
    @POST("/res/type6")
    fun postImage6(
        @Part image : MultipartBody.Part
    ): Call<ResponseBody>



}