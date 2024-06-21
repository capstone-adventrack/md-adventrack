package com.example.adventrack.features.transaction.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.adventrack.databinding.ItemTicketPurchasedBinding
import com.example.adventrack.domain.model.EntryTicketModel
import com.example.adventrack.utils.convertMillisToDateString

class PurchasedTicketAdapter :
    ListAdapter<EntryTicketModel, PurchasedTicketAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemTicketPurchasedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(
            item,
        )
    }

    class MyViewHolder(private val binding: ItemTicketPurchasedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EntryTicketModel) {
            binding.apply {
                tvPrice.text = item.price
                if (item.price == "IDR 0") {
                    tvPrice.text = "Free Entry"
                }
                tvDate.text = item.timestamp.toLong().convertMillisToDateString()
                tvTitle.text = item.name
                tvLocation.text = item.address
                tvQuantity.text =
                    if (item.quantity == 1) "${item.quantity} Ticket" else "${item.quantity} Tickets"
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EntryTicketModel>() {
            override fun areItemsTheSame(
                oldItem: EntryTicketModel,
                newItem: EntryTicketModel,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: EntryTicketModel,
                newItem: EntryTicketModel,
            ): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}