package com.example.adventrack.features.order.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.adventrack.databinding.ItemOrderBinding
import com.example.adventrack.domain.model.OrderModel

class OrderAdapter : ListAdapter<OrderModel, OrderAdapter.OrderViewHolder>(DIFF_UTIL){
    private lateinit var onItemClickListener: OnItemClickListener
    class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderModel, onItemClickListener: OnItemClickListener) {
            binding.apply {
                tvTitle.text = item.name
                tvLocation.text = item.place
                tvPrice.text = item.price.toString()
                tvDate.text = item.date
                tvQuantity.text = item.quantity.toString()
                root.setOnClickListener {
                    onItemClickListener.onItemClick(item.id)
                }
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }

    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<OrderModel>(){
            override fun areItemsTheSame(oldItem: OrderModel, newItem: OrderModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: OrderModel, newItem: OrderModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    interface OnItemClickListener {
        fun onItemClick(id: Int)
    }
}