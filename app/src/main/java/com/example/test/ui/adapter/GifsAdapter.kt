package com.example.test.ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test.R
import com.example.test.databinding.ItemGifBinding
import com.example.test.domain.Gif

class GifsAdapter() : PagingDataAdapter<Gif, GifsAdapter.Holder>(
    diffCallback = GifItemCallback()
) {

    var gifActionListener: GifActionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemGifBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {
        val gif = getItem(position) ?: return
        holder.bind(gif)
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

    inner class Holder(private val binding: ItemGifBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(gif: Gif) {
            with(binding) {
                gifImageView.tag = gif
                gif.imageUrl?.let { loadImageGif(gifImageView, it) }
                gifImageView.setOnLongClickListener {
                    showPopupMenu(gifImageView)
                    return@setOnLongClickListener true
                }
            }
        }

    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val context = view.context
        val gif = view.tag as Gif

        popupMenu.menu.add(0, ID_OPEN, Menu.NONE, context.getString(R.string.open_gif))
        popupMenu.menu.add(0, ID_DELETE, Menu.NONE, context.getString(R.string.delete_gif))

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_OPEN -> {
                    gifActionListener?.onOpenGifFragment(gif)
                }
                ID_DELETE -> {
                    gifActionListener?.onGifDelete(gif)
                }

            }
            return@setOnMenuItemClickListener true

        }

        popupMenu.show()
    }

    companion object {
        private const val ID_OPEN = 1
        private const val ID_DELETE = 2
    }


    interface GifActionListener {

        fun onOpenGifFragment(gif: Gif)

        fun onGifDelete(gif: Gif)
    }

}