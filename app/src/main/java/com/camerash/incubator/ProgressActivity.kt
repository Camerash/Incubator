package com.camerash.incubator

import android.animation.ValueAnimator
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.constraint.ConstraintSet
import android.support.transition.AutoTransition
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.camerash.incubator.model.Pizza
import kotlinx.android.synthetic.main.activity_progress.*

class ProgressActivity : AppCompatActivity(), ServiceConnection{

    private var service: BluetoothService? = null
    private val handler = Handler {
        when (it.what) {
            Bluetooth.MESSAGE_STATE_CHANGE -> checkState()
            Bluetooth.MESSAGE_READ -> handleMessage(it.arg1.toString())
            else -> {}
        }
        true
    }
    private var made = false
    private val pizza: Pizza by lazy { intent.getSerializableExtra(MainActivity.PIZZA_KEY) as Pizza }

    private val animator = ValueAnimator.ofFloat(0f, 360f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)
        setupView()
        startBluetoothService()
    }

    private fun setupView() {
        progress_image.setImageResource(pizza.drawableRes)
        animator.repeatMode = ValueAnimator.RESTART
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener {
            progress_image.rotation = it.animatedValue as Float
        }
        animator.start()
    }

    private fun startBluetoothService() {
        val intent = Intent(this, BluetoothService::class.java)
        bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    override fun onServiceConnected(componentName: ComponentName, binder: IBinder) {
        if (binder is BluetoothService.BluetoothBinder) {
            this.service = binder.getService()
            this.service?.registerHandler(this.handler)

            val device = PrefUtils.getDefaultBtDevice(this)
            device ?: return
            if(this.service?.isConnected() != true) {
                this.service?.connect(device)
            } else {
                makePizza()
            }
        }
    }

    override fun onServiceDisconnected(componentName: ComponentName) {
        this.service = null
    }

    private fun checkState() {
        if(this.service?.isConnected() == true) {
            makePizza()
        }
    }

    private fun makePizza() {
        if(made) return
        made = true
        this.service?.sendString("1, 180, 1")
    }

    private fun handleMessage(message: String) {
        // Pizza finished
        animator.pause()
        message_text_view.setText(R.string.your_pizza_is_ready)

        TransitionManager.beginDelayedTransition(root, AutoTransition())
        val set = ConstraintSet()
        set.clone(root)
        set.setVisibility(finish_button.id, View.VISIBLE)
        set.applyTo(root)
    }
}
