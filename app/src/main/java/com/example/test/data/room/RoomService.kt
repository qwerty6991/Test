package com.example.test.data.room

import com.example.test.domain.Gif
import javax.inject.Inject


class RoomService @Inject constructor(
    private val gifsDao: GifsDao
) : RoomGifsRepository {
    override suspend fun saveGifs(saveGifs: List<Gif>) {
        gifsDao.saveGifs(saveGifs.map { it.toRoomGifEntity() })
    }
}