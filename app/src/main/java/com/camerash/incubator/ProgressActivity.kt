package com.camerash.incubator

import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintSet
import android.support.transition.AutoTransition
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.LinearInterpolator
import com.camerash.incubator.model.Pizza
import kotlinx.android.synthetic.main.activity_progress.*

class ProgressActivity : AppCompatActivity() {

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
        animator.duration = ANIM_DURATION
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            progress_image.rotation = it.animatedValue as Float
        }
        animator.start()

        finish_button.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun startBluetoothService() {
        MainActivity.service?.registerHandler(this.handler)
        val device = PrefUtils.getDefaultBtDevice(this)
        device ?: return
        if(MainActivity.service?.isConnected() != true) {
            MainActivity.service?.connect(device)
        } else {
            makePizza()
        }
    }
    private fun checkState() {
        if(MainActivity.service?.isConnected() == true) {
            makePizza()
        }
    }

    private fun makePizza() {
        if(made) return
        made = true
        MainActivity.service?.sendString("1, 180, 1")
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

    override fun onBackPressed() {
        // Do nothing
    }

    companion object {
        const val ANIM_DURATION = 2000L
    }
}
