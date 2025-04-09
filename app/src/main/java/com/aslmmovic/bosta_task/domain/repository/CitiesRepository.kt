package com.aslmmovic.bosta_task.domain.repository

import kotlinx.coroutines.flow.Flow

interface CitiesRepository {
    suspend fun getCitiesAndDistricts(countryId: String): Flow<*>
}