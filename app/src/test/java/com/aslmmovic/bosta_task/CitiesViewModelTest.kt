package com.aslmmovic.bosta_task.presenation.ui

import com.aslmmovic.bosta_task.data.model.DistrictWithCity
import com.aslmmovic.bosta_task.domain.use_case.GetCitiesUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Unit tests for [CitiesViewModel].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CitiesViewModelTest {

    private lateinit var viewModel: CitiesViewModel
    private val getCitiesUseCase: GetCitiesUseCase = mock()
    private val testDispatcher = StandardTestDispatcher()

    /**
     * Sets up the test environment before each test.
     */
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CitiesViewModel(getCitiesUseCase)
    }

    /**
     * Cleans up the test environment after each test.
     */
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Tests that [CitiesViewModel.fetchCities] sets the [UiState] to [UiState.Loading] initially.
     */
    @Test
    fun `fetchCities sets loading state initially`() = runTest {
        val countryId = "testCountryId"
        whenever(getCitiesUseCase(countryId)).thenReturn((Result.success(emptyList())))

        viewModel.fetchCities(countryId)
        assertThat(viewModel.uiState.value).isEqualTo(UiState.Loading)
    }

    /**
     * Tests that [CitiesViewModel.fetchCities] sets the [UiState] to [UiState.Success]
     * when the [GetCitiesUseCase] returns a successful result.
     */
    @Test
    fun `fetchCities sets success state on successful use case result`() = runTest {
        val countryId = "testCountryId"
        val cities = listOf(
            DistrictWithCity(
                cityId = "1",
                cityName = "TestCity",
                cityOtherName = "OtherCity",
                cityCode = "TC",
                district = mock()
            )
        )
        whenever(getCitiesUseCase(countryId)).thenReturn((Result.success(cities)))

        viewModel.fetchCities(countryId)
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value).isEqualTo(UiState.Success(cities))
    }

    /**
     * Tests that [CitiesViewModel.fetchCities] sets the [UiState] to [UiState.Error]
     * when the [GetCitiesUseCase] returns a failed result.
     */
    @Test
    fun `fetchCities sets error state on failed use case result`() = runTest {
        val countryId = "testCountryId"
        val errorMessage = "Test error message"
        whenever(getCitiesUseCase(countryId)).thenReturn(
            (
                    Result.failure(
                        Exception(
                            errorMessage
                        )
                    )
                    )
        )

        viewModel.fetchCities(countryId)
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value).isEqualTo(UiState.Error(errorMessage))
    }
}