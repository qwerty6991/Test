package com.example.test.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test.R
import com.example.test.databinding.ItemGifBinding

class GifsAdapter(
) : PagingDataAdapter<GifUiEntity, GifsAdapter.Holder>(
    diffCallback = GifItemCallback()
), View.OnClickListener {

    override fun onClick(v: View) {
        val gif = v.tag as GifUiEntity
        if (v.id == R.id.gifImageView) {
        } else {
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val gif = getItem(position) ?: return

        with(holder.binding) {
            gifImageView.tag = gif
            gif.imageUrl?.let { loadImageGif(gifImageView, it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGifBinding
            .inflate(inflater, parent, false)
        binding.gifImageView.setOnClickListener(this)
        return Holder(binding)
    }


    private fun loadImageGif(imageView: ImageView, url: String) {
        val context = imageView.context
        if (url.isNotBlank()) {
            Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_baseline_image)
                .error(R.drawable.ic_baseline_broken_image)
                .centerInside()
                .into(imageView)
        } else {
            imageView.imageTintList = ColorStateList.valueOf(Color.LTGRAY)
            Glide.with(context)
                .load(R.drawable.ic_baseline_image)
                .into(imageView)
        }
    }

    class Holder(val binding: ItemGifBinding) : RecyclerView.ViewHolder(binding.root)

    interface Listener {

    }
}