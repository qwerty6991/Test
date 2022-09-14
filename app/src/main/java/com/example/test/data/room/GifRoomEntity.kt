package com.example.test.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.test.domain.Gif

@Entity(tableName = "gifs")
data class GifRoomEntity(
    @PrimaryKey override val id: String,
    override val imageUrl: String
) : Gif {

    constructor(gif: Gif) : this(
        id = gif.id,
        imageUrl = gif.imageUrl
    )

}