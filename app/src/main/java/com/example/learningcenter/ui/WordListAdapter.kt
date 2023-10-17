package com.example.learningcenter.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.learningcenter.R
import com.example.learningcenter.databinding.WordListItemBinding
import com.example.learningcenter.model.Word

class WordListAdapter(private val onWordClicked: (Word) -> Unit) : ListAdapter<Word, WordListAdapter.ItemViewHolder>(DiffCallback) {

    class ItemViewHolder(var binding: WordListItemBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(item: Word){
            binding.apply {
                wordName.text = item.wordTitle
                wordTranscript.text = item.wordTranscript
            }
            if(item.isDone){
                binding.apply {
                    isDoneWord.setImageResource(R.drawable.ic_baseline_done_24)
                    isDoneWord.visibility = View.VISIBLE
                }
            } else {
                binding.isDoneWord.setImageResource(R.drawable.ic_baseline_not_done_24)
                binding.isDoneWord.visibility = View.VISIBLE
            }


        }
    }
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Word>() {
            override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
                return oldItem.wordTitle == newItem.wordTitle
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            WordListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener{
            onWordClicked(current)
        }
        holder.bind(current)
    }


}