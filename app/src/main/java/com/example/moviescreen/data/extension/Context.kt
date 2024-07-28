package com.example.moviescreen.data.extension

import android.content.Context
import android.content.res.Resources
import androidx.annotation.StringRes
import org.json.JSONObject


fun Context.getStringOrDefault(
    @StringRes stringRes: Int,
    default: String?
): String? {
    @Suppress("SwallowedException")
    return try {
        getString(stringRes)
    } catch (e: Resources.NotFoundException) {
        default
    }
}


fun <T> String.convertJsonToModelClass(type: () -> T): T? {
    try {
        val data = JSONObject(this)
        val status = data.getJSONObject("status")
        if (status.getString("code").equals("1", ignoreCase = true))
            return type()
    } catch (e: Exception) {
    }
    return null
}









