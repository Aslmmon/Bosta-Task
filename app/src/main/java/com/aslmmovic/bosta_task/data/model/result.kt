package com.aslmmovic.bosta_task.data.model

import java.io.IOException

sealed class ResultApi<out T> {
    data class Success<out T>(val data: T) : ResultApi<T>()
    data class Error(val exception: IOException) : ResultApi<Nothing>()
    object Loading : ResultApi<Nothing>()
}