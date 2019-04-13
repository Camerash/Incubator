package com.camerash.incubator

import android.app.ProgressDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ArrayAdapter
import com.camerash.incubator.model.Pizza
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_pizza.view.*

class MainActivity : AppCompatActivity(), ServiceConnection {

    private var service: BluetoothService? = null
    private val progressDialog: ProgressDialog by lazy {
        val dialog = ProgressDialog(this)
        dialog.setTitle(R.string.loading)
        dialog
    }

    private val handler = Handler {
        when (it.what) {
            Bluetooth.MESSAGE_STATE_CHANGE -> checkState()
            else -> {}
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        setupRecyclerView()
        startBluetoothService()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupView() {
        welcome_message.text = getString(R.string.welcome_message)
    }

    private fun setupRecyclerView() {
        pizza_recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        pizza_recycler_view.layoutManager = LinearLayoutManager(this)
        pizza_recycler_view.adapter = PizzaAdapter(pizzaList)
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
            if(device == null) {
                getBluetoothDeviceList()
            } else {
                this.service?.connect(device)
                progressDialog.show()
            }
        }
    }

    override fun onServiceDisconnected(componentName: ComponentName) {
        this.service = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.bluetooth -> getBluetoothDeviceList()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getBluetoothDeviceList() {
        progressDialog.show()
        this.service?.initializeBluetooth {
            val list = it.toList()
            val deviceNameList = list.map { device -> device.name }
            progressDialog.dismiss()
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle(R.string.select_bt_device)
            dialog.setAdapter(ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, deviceNameList)) { dialog, pos ->
                val device = list[pos]
                PrefUtils.saveDefaultBtDevice(this, device)
                this.service?.connect(device)
                progressDialog.show()
            }
            dialog.show()
        }
    }

    private fun checkState() {
        if(this.service?.isConnected() == true) {
            progressDialog.dismiss()
            mask.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        if(service != null) return
        val intent = Intent(this, BluetoothService::class.java)
        bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    override fun onPause() {
        super.onPause()
        service ?: return
        unbindService(this)
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

        val PIZZA_KEY = "Pizza"
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
                    val intent = Intent(this@MainActivity, ToppingPickerActivity::class.java)
                    intent.putExtra(PIZZA_KEY, pizza)
                    startActivity(intent)
                }
            }
        }
    }
}
