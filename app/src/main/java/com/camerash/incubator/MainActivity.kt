package com.camerash.incubator

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.camerash.incubator.model.Pizza

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    inner class PizzaAdapter(private val pizzaList: List<Pizza>) :
            RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
                PizzaViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_topping, parent, false))

        override fun getItemCount(): Int = pizzaList.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val vh = holder as PizzaViewHolder
            vh.bind(pizzaList[position])
        }

        internal inner class PizzaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bind(pizza: Pizza) {

            }
        }
    }
}
