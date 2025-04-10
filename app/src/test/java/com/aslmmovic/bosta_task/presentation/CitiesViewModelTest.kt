package com.aslmmovic.bosta_task.presentation

import com.aslmmovic.bosta_task.common.ErrorMessageProvider
import com.aslmmovic.bosta_task.data.model.City
import com.aslmmovic.bosta_task.data.model.ResultApi
import com.aslmmovic.bosta_task.domain.use_case.GetCitiesUseCase
import com.aslmmovic.bosta_task.presenation.ui.CitiesViewModel
import com.aslmmovic.bosta_task.presenation.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.anyString
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class CitiesViewModelTest {

    private lateinit var getCitiesUseCase: GetCitiesUseCase
    private lateinit var errorMessageProvider: ErrorMessageProvider
    private lateinit var viewModel: CitiesViewModel
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        // Set the main dispatcher for coroutines to our TestDispatcher
        Dispatchers.setMain(testDispatcher)
        getCitiesUseCase = mock()
        errorMessageProvider = mock()
        viewModel = CitiesViewModel(getCitiesUseCase, errorMessageProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher
    }


    @Test
    fun `fetchCities - success`() = runBlocking {
        // Arrange
        val mockCities =
            listOf(City("1", "City A", "City A Other Name", cityCode = "123", listOf()))
        val successResult: Flow<ResultApi<List<City>>> = flowOf(ResultApi.Success(mockCities))
        `when`(getCitiesUseCase.invoke(anyString())).thenReturn(successResult)

        // Act
        viewModel.fetchCities("someCountryId")

        // Assert
        assertEquals(UiState.Success(mockCities), viewModel.uiState.value)
        assertNull(viewModel.errorMessage.value)
    }

}