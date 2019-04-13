package com.camerash.incubator

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Context.MODE_PRIVATE


/**
 * @author Camerash
 */
object PrefUtils {

    const val KEY = "device_address"
    const val PREF_NAME = "Incubator_Pref"

    fun saveDefaultBtDevice(context: Context, device: BluetoothDevice) {
        context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putString(KEY, device.address).apply()
    }

    fun getDefaultBtDevice(context: Context): BluetoothDevice? {
        val deviceAddress = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(KEY, null)
        deviceAddress ?: return null

        val devices = BluetoothAdapter.getDefaultAdapter().bondedDevices
        return devices.firstOrNull{ it.address == deviceAddress }
    }

}