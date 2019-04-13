package com.camerash.incubator

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_topping.view.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        topping_recycler_view.layoutManager = GridLayoutManager(this, 3)
        topping_recycler_view.adapter = ToppingAdapter(toppingList)
    }

    companion object {
        val toppingList = listOf(
            Topping(
                "Cheese",
                20,
                10,
                R.color.cheese
            ),
            Topping(
                "Tomato sause",
                20,
                10,
                R.color.tomato_sauce
            ),
            Topping(
                "Pepperoni",
                20,
                10,
                R.color.pepperoni
            ),
            Topping(
                "Red Pepper",
                20,
                10,
                R.color.red_pepper
            ),
            Topping(
                "Green Pepper",
                20,
                10,
                R.color.green_pepper
            ),
            Topping(
                "Yellow Pepper",
                20,
                10,
                R.color.yellow_pepper
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

                itemView.seekbar.setOnBoxedPointsChangeListener(object: BoxedVertical.OnValuesChangeListener {
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
