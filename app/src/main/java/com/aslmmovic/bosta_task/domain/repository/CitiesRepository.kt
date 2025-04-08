package com.aslmmovic.bosta_task.domain.repository

import com.aslmmovic.bosta_task.data.model.DistrictWithCity

interface CitiesRepository {
    suspend fun getCitiesAndDistricts(countryId: String): Result<List<DistrictWithCity>>
}