package com.aslmmovic.bosta_task.data

import com.aslmmovic.bosta_task.data.data_source.CitiesRemoteDataSource
import com.aslmmovic.bosta_task.data.data_source.network.ApiService
import com.aslmmovic.bosta_task.data.model.ApiResponse
import com.aslmmovic.bosta_task.data.model.City
import com.aslmmovic.bosta_task.data.model.ResultApi
import com.google.gson.JsonParseException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

/**
 * Unit tests for [CitiesRemoteDataSource].
 *
 * This class tests the behavior of [CitiesRemoteDataSource] by mocking the [ApiService]
 * and verifying that the data source correctly handles different scenarios, such as
 * successful data retrieval, JSON parsing errors, and unknown exceptions.
 */
class CitiesRemoteDataSourceTest {

    private lateinit var apiService: ApiService
    private lateinit var dataSource: CitiesRemoteDataSource

    /**
     * Sets up the test environment before each test.
     *
     * Initializes the [apiService] mock and creates an instance of [CitiesRemoteDataSource]
     * with the mock API service.
     */
    @Before
    fun setup() {
        apiService = mock()
        dataSource = CitiesRemoteDataSource(apiService)
    }

    /**
     * Tests the scenario where the data source successfully retrieves a list of cities.
     *
     * Mocks the API service to return a successful response with mock city data and
     * verifies that the data source returns the same successful result.
     *
     * Test Naming Convention: `[Method under test]_[Scenario]_[Expected result]`
     */
    @Test
    fun getAllCities_success_returnsSuccessResult() = runBlocking {
        // Arrange
        val mockCities = listOf(City("1", "City A", "City A Other Name", cityCode = "123", listOf()))
        val mockResponse = ApiResponse(success = true, data = mockCities, message = "")
        val response = Response.success(mockResponse)
        whenever(apiService.getCitiesAndDistricts("someCountryId")).thenReturn(response)

        // Act
        val result = dataSource.getAllCities("someCountryId")

        // Assert
        assertEquals(ResultApi.Success(mockCities), result)
    }

    /**
     * Tests the scenario where the API service throws a [JsonParseException].
     *
     * Mocks the API service to throw a [JsonParseException] and verifies that the data
     * source returns an error result with the message "Data parsing error".
     *
     * Test Naming Convention: `[Method under test]_[Scenario]_[Expected result]`
     */
    @Test
    fun getAllCities_jsonParseException_returnsErrorResult() = runBlocking {
        // Arrange
        val exception = JsonParseException("Parsing error")
        whenever(apiService.getCitiesAndDistricts("someCountryId")).thenThrow(exception)

        // Act
        val result = dataSource.getAllCities("someCountryId")

        // Assert
        if (result is ResultApi.Error) {
            assertEquals("Data parsing error", result.exception.message)
        } else {
            assertEquals("ResultApi.Error", result.toString())
        }
    }

    /**
     * Tests the scenario where the API service throws an unknown [RuntimeException].
     *
     * Mocks the API service to throw a [RuntimeException] and verifies that the data
     * source returns an error result with the message "Unknown error".
     *
     * Test Naming Convention: `[Method under test]_[Scenario]_[Expected result]`
     */
    @Test
    fun getAllCities_unknownException_returnsErrorResult() = runBlocking {
        // Arrange
        val exception = RuntimeException("Unknown error")
        whenever(apiService.getCitiesAndDistricts("someCountryId")).thenThrow(exception)

        // Act
        val result = dataSource.getAllCities("someCountryId")

        // Assert
        if (result is ResultApi.Error) {
            assertEquals("Unknown error", result.exception.message)
        } else {
            assertEquals("ResultApi.Error", result.toString())
        }
    }
}