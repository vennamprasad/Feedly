package com.appyhigh.feedly.retrofit

import com.appyhigh.feedly.data.model.NewsResource
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiResponse {
    @GET("top-headlines")
    fun getTopHeadlines(
        @Query("country") country: String?,
        @Query("apiKey") apiKey: String?
    ): Call<NewsResource?>?

    @GET("top-headlines")
    fun getWorldHeadlines(
        @Query("sources") sources: String?,
        @Query("apiKey") apiKey: String?
    ): Call<NewsResource?>?

    @GET("top-headlines")
    fun getCategoryOfHeadlines(
        @Query("country") country: String?,
        @Query("category") category: String?,
        @Query("apiKey") apiKey: String?
    ): Call<NewsResource?>?


}