package com.example.test.domain

import com.example.test.data.room.GifRoomEntity

data class Gif(
    val id: String,
    val imageUrl: String?
) {
    fun toRoomGifEntity(): GifRoomEntity = GifRoomEntity(
        id = id,
        imageUrl = imageUrl
    )
}
