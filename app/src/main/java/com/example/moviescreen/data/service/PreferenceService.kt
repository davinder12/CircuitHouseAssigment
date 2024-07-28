package com.example.moviescreen.data.service

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject


/**
 * Preference service
 *
 */
class PreferenceService @Inject constructor(context: Context) {

   // private val defaultSharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val resources: Resources = context.resources
  //  val masterKeyAlias = //MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val defaultSharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
    context,
    "Assigment",
    masterKeyAlias,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)


    fun getInt(@StringRes resId: Int, default: Int = 0): Int {
        return defaultSharedPreferences.getInt(resources.getString(resId), default)
    }

    fun putInt(@StringRes resId: Int, value: Int) {
        defaultSharedPreferences.edit().putInt(resources.getString(resId), value).apply()
    }

}
