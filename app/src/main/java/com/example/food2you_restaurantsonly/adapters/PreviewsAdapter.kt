package com.example.food2you_restaurantsonly.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.food2you_restaurantsonly.databinding.PreviewItemBinding

class PreviewsAdapter: RecyclerView.Adapter<PreviewsAdapter.MyViewHolder>() {

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    })

    var previews: List<String>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = PreviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val preview = differ.currentList[position]

        holder.previewTv.text = preview

    }

    override fun getItemCount(): Int = differ.currentList.size

    class MyViewHolder(itemView: PreviewItemBinding): RecyclerView.ViewHolder(itemView.root) {
        val previewTv = itemView.previewTv
    }
}