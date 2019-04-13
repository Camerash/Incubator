package com.camerash.incubator

import android.content.ClipData
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_topping.view.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    inner class ToppingAdapter(private val toppingList: List<Topping>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            ToppingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_topping, parent, false))

        override fun getItemCount(): Int = toppingList.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val vh = holder as ToppingViewHolder
            vh.bind(toppingList[position])
        }

        @Suppress("DEPRECATION")
        internal inner class ToppingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            fun bind(topping: Topping) {
                itemView.image.setImageResource(topping.pickerDrawRes)
                itemView.topping_name.text = topping.name

                itemView.image.setOnLongClickListener {
                    val parcel = Parcel.obtain()
                    parcel.writeSerializable(topping)
                    val clipData = ClipData.CREATOR.createFromParcel(parcel)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        it.startDragAndDrop(clipData, View.DragShadowBuilder(it), null, 0)
                    } else {
                        it.startDrag(clipData, View.DragShadowBuilder(it), null, 0)
                    }
                    true
                }
            }
        }
    }
}
