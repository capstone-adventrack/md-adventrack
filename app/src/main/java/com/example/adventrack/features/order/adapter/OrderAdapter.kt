package com.example.adventrack.features.order.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.adventrack.databinding.ItemOrderBinding
import com.example.adventrack.databinding.ItemTicketPurchasedBinding
import com.example.adventrack.domain.model.OrderModel
import com.example.adventrack.features.transaction.adapter.PurchasedTicketAdapter
import com.example.adventrack.utils.convertMillisToDateString
import com.example.adventrack.utils.stringIDRToInteger
import com.example.adventrack.utils.withCurrencyFormat

class OrderAdapter : ListAdapter<OrderModel, OrderAdapter.OrderViewHolder>(DIFF_UTIL){

    class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderModel) {
            binding.apply {
                tvTitle.text = item.name
                tvLocation.text = item.place
                tvPrice.text = stringIDRToInteger(item.price ?: "0").toString().withCurrencyFormat()
                tvDate.text = item.timestamp?.toLong()?.convertMillisToDateString()
                tvQuantity.text = item.quantity.toString()
                tvStatus.text = "PAID"
            }
        }
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
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(
            item,
        )
    }
}