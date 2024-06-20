package com.example.adventrack.features.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adventrack.databinding.ItemEntryTicketBinding
import com.example.adventrack.domain.model.EntryTicketModel

class EntryTicketAdapter :
    ListAdapter<EntryTicketModel, EntryTicketAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEntryTicketBinding.inflate(
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
            onItemClickListener
        )

    }

    class MyViewHolder(private val binding: ItemEntryTicketBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EntryTicketModel, onItemClickListener: OnItemClickListener) {
            binding.apply {
                tvPrice.text = item.price
                tvTitle.text = item.name
                tvDesc.text = item.description
                ibPlusItem.setOnClickListener {
                    onItemClickListener.onAddItemClick(item.type)
                }
                ibMinusItem.setOnClickListener {
                    onItemClickListener.onMinusItemClick(item.type)
                }
                tvQuantity.text = item.quantity.toString()
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

    interface OnItemClickListener {
        fun onAddItemClick(type : String)
        fun onMinusItemClick(type: String)
    }
}