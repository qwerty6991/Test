package com.example.test.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gifs")
data class GifRoomEntity(
    @PrimaryKey val id: String,
    val imageUrl: String?
)
