package com.example.food2you_restaurantsonly.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.food2you_restaurantsonly.data.local.entities.Food
import com.example.food2you_restaurantsonly.databinding.FoodItemBinding
import com.squareup.picasso.Picasso

class FoodAdapter(var foodList: List<Food>, private val context: Context, private val listener: OnFoodClickListener): RecyclerView.Adapter<FoodAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val meal = foodList[position]

        holder.typeTextView.text = meal.type
        holder.nameTextView.text = meal.name
        holder.priceTextView.text = "€${meal.price}"

        Picasso.with(context).load(meal.imgUrl).into(holder.imageView)

        holder.onFoodClickListener(meal, listener)

        holder.deleteFood(meal, listener)

    }

    override fun getItemCount(): Int = foodList.size


    class MyViewHolder(itemView: FoodItemBinding): RecyclerView.ViewHolder(itemView.root) {
        val typeTextView = itemView.typeTextView
        val imageView = itemView.mealImg
        val nameTextView = itemView.nameTextView
        val priceTextView = itemView.priceTextView
        val deleteImg = itemView.deleteImg


        fun onFoodClickListener(food: Food, listener: OnFoodClickListener) {
            itemView.setOnClickListener {
                listener.onFoodClicked(food)
            }
        }

        fun deleteFood(food: Food, listener: OnFoodClickListener) {
            deleteImg.setOnClickListener {
                listener.deleteFood(food)
            }
        }

    }


    interface OnFoodClickListener {
        fun onFoodClicked(food: Food)
        fun deleteFood(food: Food)
    }

}