package com.aslmmovic.bosta_task.domain.use_case
import com.aslmmovic.bosta_task.domain.repository.CitiesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface GetCitiesUseCase {
    suspend operator fun invoke(countryId: String): Flow<*>
}

class GetCitiesUseCaseImpl @Inject constructor(private val repository: CitiesRepository) :
    GetCitiesUseCase {
    override suspend operator fun invoke(countryId: String): Flow<*> {
        return repository.getCitiesAndDistricts(countryId)
    }
}