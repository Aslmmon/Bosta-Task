package com.aslmmovic.bosta_task.domain.use_case

import com.aslmmovic.bosta_task.data.model.DistrictWithCity
import com.aslmmovic.bosta_task.domain.repository.CitiesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface GetCitiesUseCase {
    suspend operator fun invoke(countryId: String): Result<List<DistrictWithCity>>
}

class GetCitiesUseCaseImpl @Inject constructor(private val repository: CitiesRepository) : GetCitiesUseCase {
    override suspend operator fun invoke(countryId: String): Result<List<DistrictWithCity>>{
        return repository.getCitiesAndDistricts(countryId)
    }
}