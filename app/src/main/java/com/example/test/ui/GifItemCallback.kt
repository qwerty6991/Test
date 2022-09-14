package com.example.test.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.test.domain.Gif

class GifItemCallback : DiffUtil.ItemCallback<Gif>() {

    override fun areItemsTheSame(oldItem: Gif, newItem: Gif): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Gif, newItem: Gif): Boolean {
        return oldItem == newItem
    }

}