package com.camerash.incubator

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * @author Camerash
 */
object PrefUtils {

    const val KEY = "Device"
    const val PREF_NAME = "Incubator_Pref"

    fun saveDefaultBtDevice(context: Context, device: BluetoothDevice) {
        val json = Gson().toJson(device)
        context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putString(KEY, json).apply()
    }

    fun getDefaultBtDevice(context: Context): BluetoothDevice? {
        val device = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(KEY, null)
        device ?: return null
        return Gson().fromJson(device)
    }

    private inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)
}