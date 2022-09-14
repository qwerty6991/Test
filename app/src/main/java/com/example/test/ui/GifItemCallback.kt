package com.example.test.ui

import androidx.recyclerview.widget.DiffUtil

class GifItemCallback : DiffUtil.ItemCallback<GifUiEntity>() {

    override fun areItemsTheSame(oldItem: GifUiEntity, newItem: GifUiEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GifUiEntity, newItem: GifUiEntity): Boolean {
        return oldItem == newItem
    }

}