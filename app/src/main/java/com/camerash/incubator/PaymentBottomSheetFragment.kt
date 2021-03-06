package com.camerash.incubator

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.transition.AutoTransition
import android.support.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.camerash.incubator.model.Pizza
import kotlinx.android.synthetic.main.fragment_payment_bottom_sheet.*

/**
 * @author Camerash
 */
class PaymentBottomSheetFragment: BottomSheetDialogFragment() {

    private val pizza: Pizza by lazy { arguments?.getSerializable(MainActivity.PIZZA_KEY) as Pizza }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_payment_bottom_sheet, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        root.clipToOutline = true
        pizza_image.setImageResource(pizza.drawableRes)
        pizza_info.text = getString(R.string.pizza_quantity, pizza.name)
        order_button.setOnClickListener {
            dismiss()
            val act = activity ?: return@setOnClickListener
            if(act is OnPaymentCompleteListener)
                act.onPaymentComplete(pizza)
        }

        visa.setOnClickListener { updatePaymentInfo(R.drawable.visa, R.string.visa_info) }
        mastercard.setOnClickListener { updatePaymentInfo(R.drawable.mastercard, R.string.mastercard_info) }
        wechat_pay.setOnClickListener { updatePaymentInfo(R.drawable.wechat_pay, R.string.wechat_pay) }
        alipay.setOnClickListener { updatePaymentInfo(R.drawable.alipay, R.string.alipay) }
    }

    private fun updatePaymentInfo(icon: Int, info: Int) {
        TransitionManager.beginDelayedTransition(root, AutoTransition())

        payment_icon.setImageResource(icon)
        payment_info.setText(info)
        expansion_layout.collapse(true)
    }

    interface OnPaymentCompleteListener {
        fun onPaymentComplete(pizza: Pizza)
    }

    companion object {
        fun newInstance(pizza: Pizza): PaymentBottomSheetFragment {
            return PaymentBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(MainActivity.PIZZA_KEY, pizza)
                }
            }
        }
    }
}