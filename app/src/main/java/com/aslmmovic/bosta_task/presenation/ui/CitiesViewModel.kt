package com.aslmmovic.bosta_task.presenation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aslmmovic.bosta_task.common.ErrorMessageProvider
import com.aslmmovic.bosta_task.data.model.City
import com.aslmmovic.bosta_task.data.model.ResultApi
import com.aslmmovic.bosta_task.domain.use_case.GetCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val errorMessageProvider: ErrorMessageProvider // Inject the provider
) :
    ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<City>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<City>>> = _uiState.asStateFlow()


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    fun fetchCities(countryId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            getCitiesUseCase(countryId)
                .catch { e ->
                    _uiState.value = UiState.Error(e.message?:"")
                }.collect { result ->
                    when (result) {
                        is ResultApi.Loading -> _uiState.value = UiState.Loading
                        is ResultApi.Success<*> -> _uiState.value =
                            UiState.Success(result.data as List<City>)

                        is ResultApi.Error -> {
                            _uiState.value = UiState.Error(result.exception.message ?: "")
                            _errorMessage.value = errorMessageProvider.getErrorMessage(result.exception)
                        }
                    }
                }
        }
    }

}

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

