package com.example.learningcenter.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.learningcenter.R
import com.example.learningcenter.databinding.ServicesListItemBinding
import com.example.learningcenter.model.Services
import kotlin.coroutines.coroutineContext

class ServicesAdapter(private val onItemClicked: (Services) -> Unit) :
    ListAdapter<Services, ServicesAdapter.ServicesViewHolder>(DiffCallback) {

    private lateinit var context: Context

    class ServicesViewHolder(private var binding: ServicesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(service: Services) {
            binding.title.setText(service.serviceName)
            binding.priceTitle.text = service.servicePrice
            // Load the images into the ImageView using the Coil library.
            binding.serviceImage.setImageResource(service.stringResourceId)

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {
        context = parent.context
        return ServicesViewHolder(
            ServicesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Services>() {
            override fun areItemsTheSame(oldItem: Services, newItem: Services): Boolean {
                return (oldItem.id == newItem.id ||
                        oldItem.serviceLink == newItem.serviceLink
                        )
            }

            override fun areContentsTheSame(oldItem: Services, newItem: Services): Boolean {
                return oldItem == newItem
            }
        }
    }


}