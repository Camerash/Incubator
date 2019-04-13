package com.camerash.incubator

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.camerash.incubator.model.Pizza
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_pizza.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        setupRecyclerView()
    }

    private fun setupView() {
        welcome_message.text = getString(R.string.welcome_message)
    }

    private fun setupRecyclerView() {
        pizza_recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        pizza_recycler_view.layoutManager = LinearLayoutManager(this)
        pizza_recycler_view.adapter = PizzaAdapter(pizzaList)
    }

    companion object {
        val pizzaList = listOf(
            Pizza(
                "Hawaiian",
                R.drawable.pizza_hawaiian
            ),
            Pizza(
                "Pepperoni",
                R.drawable.pizza_pepperoni
            ),
            Pizza(
                "Meat Deluxe",
                R.drawable.pizza_meat_deluxe
            ),
            Pizza(
                "Vegetarian Supreme",
                R.drawable.pizza_vegetarian_supreme
            ),Pizza(
                "Ham & Cheese",
                R.drawable.pizza_ham_and_cheese
            )
        )
    }

    inner class PizzaAdapter(private val pizzaList: List<Pizza>) :
            RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
                PizzaViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pizza, parent, false))

        override fun getItemCount(): Int = pizzaList.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val vh = holder as PizzaViewHolder
            vh.bind(pizzaList[position])
        }

        internal inner class PizzaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bind(pizza: Pizza) {
                itemView.image.setImageResource(pizza.drawableRes)
                itemView.pizza_name.text = pizza.name

                itemView.order_button.setOnClickListener {
                    // Directly go to order page
                }

                itemView.customize_button.setOnClickListener {
                    // Go to topping slider page
                    startActivity(Intent(this@MainActivity, ToppingPickerActivity::class.java))
                }
            }
        }
    }
}
