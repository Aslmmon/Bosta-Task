package com.aslmmovic.bosta_task.domain.di

import com.aslmmovic.bosta_task.data.data_source.CitiesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.aslmmovic.bosta_task.domain.repository.CitiesRepository
import com.aslmmovic.bosta_task.data.data_source.CitiesRemoteDataSource
import com.aslmmovic.bosta_task.domain.use_case.GetCitiesUseCase
import com.aslmmovic.bosta_task.domain.use_case.GetCitiesUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideCitiesRepository(remoteDataSource: CitiesRemoteDataSource): CitiesRepository {
        return CitiesRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideGetCitiesUseCase(repository: CitiesRepository): GetCitiesUseCase {
        return GetCitiesUseCaseImpl(repository)
    }
}