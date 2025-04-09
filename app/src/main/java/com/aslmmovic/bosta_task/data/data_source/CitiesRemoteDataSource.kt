package com.aslmmovic.bosta_task.data.data_source

import com.aslmmovic.bosta_task.data.data_source.network.ApiService
import com.aslmmovic.bosta_task.data.model.City
import com.aslmmovic.bosta_task.data.model.DistrictWithCity
import com.aslmmovic.bosta_task.domain.repository.CitiesRepository
import com.google.gson.JsonSyntaxException
import java.io.IOException
import javax.inject.Inject

class CitiesRemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun getCitiesAndDistricts(countryId: String) =
        apiService.getCitiesAndDistricts(countryId)
}


class CitiesRepositoryImpl @Inject constructor(private val remoteDataSource: CitiesRemoteDataSource) :
    CitiesRepository {

    override suspend fun getCitiesAndDistricts(countryId: String): Result<List<City>> {
        return try {
            val response = remoteDataSource.getCitiesAndDistricts(countryId)
            if (response.isSuccessful) {
                val cities = response.body()?.data ?: emptyList()
                Result.success(cities)
            } else {
                Result.failure(Exception("HTTP Error: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Network error"))
        } catch (e: JsonSyntaxException) {
            Result.failure(Exception("Parsing error"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

