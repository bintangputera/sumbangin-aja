package com.bintangpoetra.sumbanginaja.data.auth.remote

import com.bintangpoetra.sumbanginaja.data.auth.model.UserItem
import com.bintangpoetra.sumbanginaja.data.lib.BaseResponse
import retrofit2.http.*

interface AuthService {

    @POST("login")
    @FormUrlEncoded
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): BaseResponse<UserItem>

    @POST("register")
    @FormUrlEncoded
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("type") type: String
    ): BaseResponse<UserItem>

    @GET("get-profile")
    suspend fun getProfileDetail(
        @Header("Authorization") token: String
    ): BaseResponse<UserItem>

}