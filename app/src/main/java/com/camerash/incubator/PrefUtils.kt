package com.camerash.incubator

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson


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
        return Gson().fromJson(device, BluetoothDevice::class.java)
    }

}