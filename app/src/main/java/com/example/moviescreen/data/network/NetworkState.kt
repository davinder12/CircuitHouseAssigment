package com.example.moviescreen.data.network

import android.content.Context
import androidx.annotation.StringRes


@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status,
    val msg: String? = null,
    @StringRes val stringRes: Int? = null
) {

    fun getErrorMessage(context: Context): String? {
        return stringRes?.let { context.getStringOrDefault(it, msg) } ?: msg
    }

    companion object {
        private const  val InternetMessage = "No address associated with hostname"
        const val  INTERNET_CONNECTION_MSG ="No Internet Connection"

        val success = NetworkState(Status.SUCCESS)
        val loading = NetworkState(Status.RUNNING)

        fun error(msg: String?) = NetworkState(
            Status.FAILED,
            msg = filterMessage(msg)
        )

        fun error(@StringRes stringRes: Int?) = NetworkState(
            Status.FAILED,
            stringRes = stringRes
        )

        fun error(msg: String?, @StringRes stringRes: Int?) = NetworkState(
            Status.FAILED,
            msg = msg,
            stringRes = stringRes

        )

        private fun filterMessage(message:String?): String? {
           return when {
                message?.contains(InternetMessage) == true -> INTERNET_CONNECTION_MSG
                else -> message
            }
        }
    }

    enum class Status {
        RUNNING,
        SUCCESS,
        FAILED
    }
}