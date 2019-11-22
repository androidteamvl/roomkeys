package com.krys.kotlinamritlife.NetworkCall

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ApiList {

    @POST("check-mobile")
    fun checklogin(@Body login: JsonObject): Call<JsonObject>

    @POST("insert-mobile")
    fun insertmobile(@Body login: JsonObject): Call<JsonObject>

    @POST("user-login")
    fun userlogin(@Body login: JsonObject): Call<JsonObject>

    @POST("api-send-otp-forgot-password")
    fun sendotpforgotpassword(@Body city: JsonObject): Call<JsonObject>

    @POST("api-update-forgot-password")
    fun updateforgotpassword(@Body state: JsonObject): Call<JsonObject>

    @POST("api-get-category")
    fun getcategory(@Body district: JsonObject): Call<JsonObject>

    @POST("api-get-sub-category")
    fun getsubcategory(@Body city: JsonObject): Call<JsonObject>

    @POST("api-get-child-category")
    fun getchildcategory(@Body login: JsonObject): Call<JsonObject>

    @POST("api-get-wishlist")
    fun getwishlist(@Body login: JsonObject): Call<JsonObject>

    @POST("api-add-wishlist")
    fun addwishlist(@Body login: JsonObject): Call<JsonObject>

    @POST("api-create-post")
    fun createpost(@Body city: JsonObject): Call<JsonObject>

    @POST(" api-get-near-me-post")
    fun getnearme(@Body city: JsonObject): Call<JsonObject>

    @POST("api-get-post-by-category")
    fun postbycategory(@Body city: JsonObject): Call<JsonObject>

    @POST("api-home-data")
    fun homedata(@Body city: JsonObject): Call<JsonObject>

    @POST("api-get-post-detail")
    fun Getpostdetail(@Body city: JsonObject): Call<JsonObject>

    @POST("api-similar-post")
    fun apisimilarpost(@Body city: JsonObject): Call<JsonObject>


    @Multipart
    @POST("api-create-post-image")
    fun uploadMultipleFiles(
        @Part("app_key") description: RequestBody,
        @Part files: MultipartBody.Part,
        @Part files1: MultipartBody.Part,
        @Part files2: MultipartBody.Part,
        @Part files3: MultipartBody.Part,
        @Part files4: MultipartBody.Part,
        @Part files5: MultipartBody.Part,
        @Part files6: MultipartBody.Part,
        @Part files7: MultipartBody.Part,
        @Part files8: MultipartBody.Part,
        @Part files9: MultipartBody.Part): Call<JsonObject>

}