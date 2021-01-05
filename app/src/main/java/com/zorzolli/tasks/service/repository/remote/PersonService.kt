package com.zorzolli.tasks.service.repository.remote

import com.zorzolli.tasks.service.model.HeaderModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PersonService {

    @POST("Authentication/Login")
    @FormUrlEncoded
    fun login(@Field("email")email: String,
               @Field("password")password: String
    ): Call<HeaderModel>

    @POST("Authentication/Create")
    @FormUrlEncoded
    fun create(
        @Field("name") name: String,
        @Field("email")email: String,
        @Field("password")password: String,
        @Field("receiveNews") news: Boolean
    ): Call<HeaderModel>

}