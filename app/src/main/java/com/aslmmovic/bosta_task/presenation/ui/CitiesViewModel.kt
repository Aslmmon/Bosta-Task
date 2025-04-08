package com.aslmmovic.bosta_task.presenation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aslmmovic.bosta_task.data.model.DistrictWithCity
import com.aslmmovic.bosta_task.domain.use_case.GetCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(private val getCitiesUseCase: GetCitiesUseCase) :
    ViewModel() {

    private val _uiState = MutableLiveData<UiState<List<DistrictWithCity>>>()
    val uiState: LiveData<UiState<List<DistrictWithCity>>> = _uiState

    fun fetchCities(countryId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = getCitiesUseCase(countryId)
            result.fold(
                onSuccess = { cities ->
                    _uiState.value = UiState.Success(cities)
                },
                onFailure = { error ->
                    _uiState.value = UiState.Error(error.message ?: "An unknown error occurred.")
                }
            )
        }
    }
}

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}