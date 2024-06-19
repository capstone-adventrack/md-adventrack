package com.example.adventrack.features.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adventrack.databinding.ItemNearbyPlaceBinding
import com.example.adventrack.domain.PlaceModel

class PlaceAdapter : ListAdapter<PlaceModel, PlaceAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemNearbyPlaceBinding.inflate(
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

    class MyViewHolder(private val binding: ItemNearbyPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlaceModel, onItemClickListener: OnItemClickListener) {
            binding.apply {
                tvNearbyPlaceName.text = item.name
                Glide.with(itemView.context)
                    .load(item.imageUrl)
                    .into(ivNearbyPlace)
                root.setOnClickListener {
                    onItemClickListener.onItemClick(item.id)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PlaceModel>() {
            override fun areItemsTheSame(oldItem: PlaceModel, newItem: PlaceModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: PlaceModel,
                newItem: PlaceModel,
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(id: String)
    }
}