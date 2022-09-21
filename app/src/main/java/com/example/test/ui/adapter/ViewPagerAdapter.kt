package com.example.test.ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test.R
import com.example.test.databinding.ImageSliderItemBinding
import com.example.test.domain.Gif

class ViewPagerAdapter(
) : PagingDataAdapter<Gif, ViewPagerAdapter.Holder>(
    diffCallback = GifItemCallback()
) {

    var startPosition: Int = 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val gif = getItem(position) ?: return
        with(holder.binding) {
            idIVImage.tag = gif
            gif.imageUrl?.let { loadImageGif(idIVImage, it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ImageSliderItemBinding
            .inflate(inflater, parent, false)
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

    class Holder(val binding: ImageSliderItemBinding) : RecyclerView.ViewHolder(binding.root)


}
