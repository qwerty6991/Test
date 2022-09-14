package com.example.test.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.test.domain.Gif
import org.jetbrains.annotations.NotNull

@Entity(tableName = "gifs")
data class GifRoomEntity(
    @PrimaryKey @NotNull val id: String,
    val imageUrl: String?
) {

    companion object {
        fun fromGif(gif: Gif): GifRoomEntity {
            return GifRoomEntity(
                id = gif.id,
                imageUrl = gif.imageUrl
            )
        }
    }
}
