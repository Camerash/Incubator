package com.camerash.incubator

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Binder
import android.os.Handler



/**
 * @author Camerash
 */
class BluetoothService: Service() {

    val binder = BluetoothBinder()
    var handler = Handler()
    val bt = Bluetooth(this, handler)

    fun initializeBluetooth(callback: (Set<BluetoothDevice>) -> Unit) {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(!bluetoothAdapter.isEnabled) {
            bluetoothAdapter.enable()
            Handler().postDelayed({
                bt.start()
                callback(bt.pairedDevices)
            }, 1000)
        } else {
            bt.start()
            callback(bt.pairedDevices)
        }
    }

    fun connect(device: BluetoothDevice) {
        bt.connectDevice(device)
    }

    fun isConnected(): Boolean = bt.state == Bluetooth.STATE_CONNECTED

    fun sendString(message: String) {
        bt.sendMessage(message)
    }

    fun registerHandler(handler: Handler) {
        bt.mHandler = handler
    }

    inner class BluetoothBinder : Binder() {
        fun getService(): BluetoothService = this@BluetoothService
    }

    override fun onBind(intent: Intent): BluetoothBinder = binder
}