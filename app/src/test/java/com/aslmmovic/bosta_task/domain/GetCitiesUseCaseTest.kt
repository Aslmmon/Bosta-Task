package com.aslmmovic.bosta_task.domain

import com.aslmmovic.bosta_task.data.model.City
import com.aslmmovic.bosta_task.data.model.ResultApi
import com.aslmmovic.bosta_task.domain.repository.CitiesRepository
import com.aslmmovic.bosta_task.domain.use_case.GetCitiesUseCase
import com.aslmmovic.bosta_task.domain.use_case.GetCitiesUseCaseImpl
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.IOException

/**
 * Unit tests for [GetCitiesUseCaseImpl].
 *
 * This class tests the behavior of the [GetCitiesUseCaseImpl] by mocking the [CitiesRepository]
 * and verifying that the use case handles different scenarios correctly, such as successful
 * data retrieval, error handling, and loading state emissions.
 */
class GetCitiesUseCaseImplTest {

    private lateinit var citiesRepository: CitiesRepository
    private lateinit var getCitiesUseCase: GetCitiesUseCase

    /**
     * Sets up the test environment before each test.
     *
     * Initializes the [citiesRepository] mock and creates an instance of [GetCitiesUseCaseImpl]
     * with the mock repository.
     */
    @Before
    fun setup() {
        citiesRepository = mock()
        getCitiesUseCase = GetCitiesUseCaseImpl(citiesRepository)
    }

    /**
     * Tests the scenario where the use case successfully retrieves a list of cities.
     *
     * Mocks the repository to return a successful result with mock city data and verifies
     * that the use case emits the same successful result.
     *
     * Test Naming Convention: `[Method under test]_[Scenario]_[Expected result]`
     */
    @Test
    fun invoke_success_emitsSuccessResult() = runBlocking {
        // Arrange
        val mockCities =
            listOf(City("1", "City A", "City A Other Name", cityCode = "123", listOf()))
        whenever(citiesRepository.getCitiesAndDistricts("someCountryId")).thenReturn(
            flowOf(
                ResultApi.Success(mockCities)
            )
        )

        // Act
        getCitiesUseCase("someCountryId").collect {
            // Assert
            assertEquals(ResultApi.Success(mockCities), it)
        }
    }

    /**
     * Tests the scenario where the use case encounters an error while retrieving cities.
     *
     * Mocks the repository to return an error result and verifies that the use case emits
     * the same error result.
     *
     * Test Naming Convention: `[Method under test]_[Scenario]_[Expected result]`
     */
    @Test
    fun invoke_error_emitsErrorResult() = runBlocking {
        // Arrange
        val error = IOException("Network error")
        whenever(citiesRepository.getCitiesAndDistricts("someCountryId")).thenReturn(
            flowOf(
                ResultApi.Error(error)
            )
        )

        // Act
        getCitiesUseCase("someCountryId").collect {
            // Assert
            assertEquals(ResultApi.Error(error), it)
        }
    }

    /**
     * Tests the scenario where the use case emits a loading state followed by a successful result.
     *
     * Mocks the repository to return a flow that emits a loading state and then a successful
     * result, and verifies that the use case emits the same sequence of results.
     *
     * Test Naming Convention: `[Method under test]_[Scenario]_[Expected result]`
     */
    @Test
    fun invoke_loadingThenSuccess_emitsLoadingThenSuccessResults() = runBlocking {
        // Arrange
        val mockCities =
            listOf(City("1", "City A", "City A Other Name", cityCode = "123", listOf()))
        whenever(citiesRepository.getCitiesAndDistricts("someCountryId")).thenReturn(
            flowOf(
                ResultApi.Loading,
                ResultApi.Success(mockCities)
            )
        )

        // Act
        val results = mutableListOf<ResultApi<List<City>>>()
        getCitiesUseCase("someCountryId").collect {
            results.add(it as ResultApi<List<City>>)
        }

        // Assert
        assertEquals(2, results.size)
        assertEquals(ResultApi.Loading, results[0])
        assertEquals(ResultApi.Success(mockCities), results[1])
    }
}