package com.aslmmovic.bosta_task.data.data_source

import com.aslmmovic.bosta_task.data.data_source.network.ApiService
import com.aslmmovic.bosta_task.data.model.ApiResponse
import com.aslmmovic.bosta_task.data.model.City
import com.aslmmovic.bosta_task.data.model.ResultApi
import com.aslmmovic.bosta_task.domain.repository.CitiesRepository
import com.google.gson.JsonParseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CitiesRemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun getAllCities(countryId: String): ResultApi<List<City>> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCitiesAndDistricts(countryId)
                if (response.isSuccessful) {
                    ResultApi.Success(response.body()?.data ?: emptyList())
                } else {
                    ResultApi.Error(IOException("HTTP error: ${response.code()}"))
                }
            } catch (e: IOException) {
                ResultApi.Error(e)
            } catch (e: HttpException) {
                ResultApi.Error(IOException("Network error"))
            } catch (e: JsonParseException) {
                ResultApi.Error(IOException("Data parsing error"))
            } catch (e: Exception) {
                ResultApi.Error(IOException("Unknown error"))
            }
        }
}


class CitiesRepositoryImpl @Inject constructor(private val remoteDataSource: CitiesRemoteDataSource) :
    CitiesRepository {
    override suspend fun getCitiesAndDistricts(countryId: String) = flow {
        emit(ResultApi.Loading) // Emit loading state
        emit(remoteDataSource.getAllCities(countryId))
    }.catch { e ->
        emit(ResultApi.Error(IOException("Error fetching cities")))
    }
}

