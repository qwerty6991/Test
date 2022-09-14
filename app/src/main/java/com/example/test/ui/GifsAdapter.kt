package com.example.test.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test.R
import com.example.test.databinding.ItemGifBinding
import com.example.test.domain.Gif

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

        Log.i("My_APP", "onBindViewHolder: $gif")
        with(holder.binding) {
            gifImageView.tag = gif
            setImage(gifImageView, gif)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGifBinding
            .inflate(inflater, parent, false)
        binding.gifImageView.setOnClickListener(this)
        return Holder(binding)
    }

    private fun setStatus(
        imageView: ImageView,
        @DrawableRes drawable: Int,
        @ColorRes colorRes: Int
    ) {
        val context = imageView.context
        imageView.setImageResource(drawable)
        imageView.imageTintList = ColorStateList
            .valueOf(ContextCompat.getColor(context, colorRes))
    }

    private fun setImage(imageView: ImageView, gif: Gif) {
        val context = imageView.context
        if (gif.imageUrl.isNotBlank()) {

            imageView.imageTintList = null
            Glide.with(context)
                .load(gif.imageUrl)
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