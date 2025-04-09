package com.aslmmovic.bosta_task.common

import android.content.Context
import com.aslmmovic.bosta_task.R
import com.google.gson.JsonParseException
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ErrorMessageProvider @Inject constructor(
    @ApplicationContext private val context: Context // Inject Context
) {

    fun getErrorMessage(exception: Throwable): String {
        return when (exception) {
            is IOException -> context.getString(R.string.network_error) + " " + exception.message
            is JsonParseException -> context.getString(R.string.data_parsing_error)
            is HttpException -> context.getString(R.string.server_error) + " Code: " + exception.code()
            else -> context.getString(R.string.unknown_error)
        }
    }
}
