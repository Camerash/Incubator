package com.camerash.incubator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.camerash.incubator.model.Pizza
import com.camerash.incubator.model.Topping
import com.camerash.incubator.model.ToppingPayload
import com.camerash.incubator.model.ToppingValue
import com.camerash.incubator.view.BoxedVertical
import kotlinx.android.synthetic.main.activity_topping_picker.*
import kotlinx.android.synthetic.main.item_topping.view.*


class ToppingPickerActivity : AppCompatActivity(), PaymentBottomSheetFragment.OnPaymentCompleteListener {

    private val pizza: Pizza by lazy { intent.getSerializableExtra(MainActivity.PIZZA_KEY) as Pizza }
    private val adapter = ToppingAdapter(toppingList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topping_picker)
        setupView()
        setupRecyclerView()
    }

    private fun setupView() {
        back_button.setOnClickListener { finish() }
        next_button.setOnClickListener {
            val paymentFragment = PaymentBottomSheetFragment.newInstance(pizza)
            paymentFragment.show(supportFragmentManager, getString(R.string.order_fragment))
        }
    }

    private fun setupRecyclerView() {
        topping_recycler_view.layoutManager = GridLayoutManager(this, 3)
        topping_recycler_view.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
            finish()
    }

    override fun onPaymentComplete(pizza: Pizza) {
        val toppingValueList = adapter.toppingValueList.map { it.value }.toMutableList()
        toppingValueList.removeAt(3) // Remove Red Pepper
        toppingValueList.removeAt(3) // Remove Green Pepper
        val toppingPayload = ToppingPayload(toppingValueList)

        val intent = Intent(this@ToppingPickerActivity, ProgressActivity::class.java)
        intent.putExtra(MainActivity.PIZZA_KEY, pizza)
        intent.putExtra(TOPPING_KEY, toppingPayload)
        startActivityForResult(intent, REQUEST_CODE)
    }

    companion object {
        const val MAX_AMOUNT = 2
        const val DEFAULT_AMOUNT = 1
        const val REQUEST_CODE = 1
        const val TOPPING_KEY = "TOPPING"

        val toppingList = listOf(
            Topping(
                "Tomato sause",
                MAX_AMOUNT,
                DEFAULT_AMOUNT,
                R.color.tomato_sauce
            ),
            Topping(
                "Pineapple",
                MAX_AMOUNT,
                DEFAULT_AMOUNT,
                R.color.pineapple
            ),
            Topping(
                "Pepperoni",
                MAX_AMOUNT,
                DEFAULT_AMOUNT,
                R.color.pepperoni
            ),
            Topping(
                "Red Pepper",
                MAX_AMOUNT,
                DEFAULT_AMOUNT,
                R.color.red_pepper
            ),
            Topping(
                "Green Pepper",
                MAX_AMOUNT,
                DEFAULT_AMOUNT,
                R.color.green_pepper
            ),
            Topping(
                "Cheese",
                MAX_AMOUNT,
                DEFAULT_AMOUNT,
                R.color.cheese
            )
        )
    }

    inner class ToppingAdapter(private val toppingList: List<Topping>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        val toppingValueList = toppingList.map { ToppingValue(it.name, it.defaultAmount) }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            ToppingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_topping, parent, false))

        override fun getItemCount(): Int = toppingList.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val vh = holder as ToppingViewHolder
            vh.bind(toppingList[position])
        }

        internal inner class ToppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bind(topping: Topping) {
                itemView.seekbar.max = topping.maxAmount
                itemView.seekbar.setProgressColor(ContextCompat.getColor(itemView.context, topping.color))
                itemView.seekbar.value = topping.defaultAmount

                itemView.seekbar.setOnBoxedPointsChangeListener(object : BoxedVertical.OnValuesChangeListener {
                    override fun onPointsChanged(view: BoxedVertical, value: Int) {
                        toppingValueList.find { it.name == topping.name }?.value = value
                    }

                    override fun onStartTrackingTouch(var1: BoxedVertical?) {}

                    override fun onStopTrackingTouch(var1: BoxedVertical?) {}
                })
                itemView.topping_name.text = topping.name
            }
        }
    }
}
