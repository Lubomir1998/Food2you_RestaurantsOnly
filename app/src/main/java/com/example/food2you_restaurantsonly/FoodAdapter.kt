package com.example.food2you_restaurantsonly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.food2you_restaurantsonly.data.local.entities.Food
import com.example.food2you_restaurantsonly.databinding.FoodItemBinding

class FoodAdapter(var foodList: List<Food>): RecyclerView.Adapter<FoodAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val meal = foodList[position]

        holder.typeTextView.text = meal.type
        holder.nameTextView.text = meal.name
        holder.priceTextView.text = meal.price.toString()


        // now load image with glide


    }

    override fun getItemCount(): Int = foodList.size


    class MyViewHolder(itemView: FoodItemBinding): RecyclerView.ViewHolder(itemView.root) {
        val typeTextView = itemView.typeTextView
        val imageView = itemView.mealImg
        val nameTextView = itemView.nameTextView
        val priceTextView = itemView.priceTextView
    }

}