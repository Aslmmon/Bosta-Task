package com.aslmmovic.bosta_task.data.data_source.network

import com.aslmmovic.bosta_task.data.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("cities/getAllDistricts")
    suspend fun getCitiesAndDistricts(@Query("countryId") countryId: String): Response<ApiResponse>
}