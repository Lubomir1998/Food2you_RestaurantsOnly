package com.example.food2you_restaurantsonly.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.food2you_restaurantsonly.data.local.entities.Order
import com.example.food2you_restaurantsonly.databinding.OrderItemBinding
import java.text.SimpleDateFormat
import java.util.*

class OrderAdapter(private val listener: OnOrderClickListener): RecyclerView.Adapter<OrderAdapter.MyViewHolder>() {

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    })

    var orders: List<Order>
    get() = differ.currentList
    set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val order = differ.currentList[position]

        holder.apply {
            addressTv.text = order.address
            phoneNumberTv.text = order.phoneNumber.toString()

            val foods = order.food
            for(food in foods) {
                foodTv.text =
                    if (food == foods[0]) "${food.quantity} ${food.name}" else ", ${food.quantity} ${food.name}"
            }

            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val dateString = dateFormat.format(order.timestamp)

            timeTv.text = dateString

            onOrderClickListener(listener, order)

        }

    }

    override fun getItemCount(): Int = differ.currentList.size

    // "${if(item == list[0]) "${item.quantity} ${item.name}" else ", ${item.quantity} ${item.name}"}"


    class MyViewHolder(itemView: OrderItemBinding): RecyclerView.ViewHolder(itemView.root) {
        val addressTv = itemView.addressTv
        val phoneNumberTv = itemView.phoneNumberTv
        val foodTv = itemView.foodTv
        val timeTv = itemView.timeTv

        fun onOrderClickListener(listener: OnOrderClickListener, order: Order) {
            itemView.setOnClickListener {
                listener.onOrderClicked(order)
            }
        }

    }


    interface OnOrderClickListener {
        fun onOrderClicked(order: Order)
    }

}