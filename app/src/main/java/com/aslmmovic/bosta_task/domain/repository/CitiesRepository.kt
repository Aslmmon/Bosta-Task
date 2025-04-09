package com.aslmmovic.bosta_task.domain.repository

import com.aslmmovic.bosta_task.data.model.City

interface CitiesRepository {
    suspend fun getCitiesAndDistricts(countryId: String): Result<List<City>>
}